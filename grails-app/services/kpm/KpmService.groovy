package kpm
import dk.sdu.kpm.Algo
import dk.sdu.kpm.Combine
import dk.sdu.kpm.KPMSettings
import dk.sdu.kpm.graph.KPMGraph
import dk.sdu.kpm.logging.KpmLogger
import dk.sdu.kpm.perturbation.IPerturbation
import dk.sdu.kpm.perturbation.PerturbationService
import dk.sdu.kpm.runners.BatchRunWithPerturbationParameters
import dk.sdu.kpm.runners.BatchRunWithPerturbationRunner
import dk.sdu.kpm.runners.BatchRunner
import fileParsers.GraphFileParserService
import kpm.web.exceptions.InvalidRunParametersException
import kpm.web.kpm.KPMWebTaskMonitor
import kpm.web.kpm.liveness.KpmRunnable
import kpm.web.kpm.parameters.Algorithm
import kpm.web.kpm.parameters.ExceptionParameters
import kpm.web.kpm.parameters.KpmParameters
import kpm.web.kpm.parameters.LParameters
import kpm.web.kpm.parameters.PerturbationParameters
import kpm.web.kpm.parameters.RunParameters
import kpm.web.kpm.parameters.Strategy
import kpm.web.utils.StringUtil

import java.util.logging.Level

class KpmService {
    static transactional = true
    def questService
    def graphsService
    def graphFileParserService
    def kpmListenerService
    def grailsApplication
    def numericToIndicatorMatrixService

    boolean start(Long runParamID, Long progressID){

        def kpmSettings = new KPMSettings();

        def kpmID = questService.getRunID(progressID);
        if(!kpmID.equals("")){
            kpmSettings.updateRunID(kpmID);
        }else{
            kpmID = kpmSettings.RunID;
        }

        def monitor = new KPMWebTaskMonitor(progressID, kpmSettings.RunID, questService);

        try{
            def runParams = RunParameters.get(runParamID);
            if(!runParams){
                monitor.setStatusMessage("[0] No parameters found. Exiting.");
                monitor.setProgress(1);
                return false;
            }
            runParams.refresh();
            def parameters = KpmParameters.get(runParams.id);
            parameters.refresh();

            monitor.setRunID(kpmID);
            monitor.setTitle("KPM run: setup");
            monitor.setName(parameters.name);

            // Getting the graph
            def graph = graphsService.getByID(parameters.graphID);
            if(!graph){
                monitor.setStatusMessage("[1] No graph found. Exiting.");
                monitor.setProgress(1);
                return false;
            }

            //Setting the matrix file map.
            kpmSettings.MATRIX_FILES_MAP = new HashMap<String, String>();

            if(runParams.goldStandardNodes != ""){
                def list = runParams.goldStandardNodes.split("\n");
                for(String goldStandardNode : list){
                    kpmSettings.VALIDATION_GOLDSTANDARD_NODES.add(goldStandardNode.trim());
                }
            }

            def k = ExceptionParameters.findByOwner(parameters);
            if(!k){
                monitor.setStatusMessage("[-1] No K. Exiting.");
                monitor.setProgress(1);
                return false;
            }else{
                k.refresh();
            }

            // Setting up K.
            if(k.use_range){
                kpmSettings.IS_BATCH_RUN = true;
                kpmSettings.INCLUDE_CHARTS = true;
                kpmSettings.MIN_K = k.val;
                kpmSettings.INC_K = k.val_step;
                kpmSettings.MAX_K = k.val_max;
            }else{
                kpmSettings.MIN_K = k.val;
                kpmSettings.INC_K = k.val;
                kpmSettings.MAX_K = k.val;
            }

            def l_values = LParameters.findAllByOwner(parameters);


            // We don't have any l definitions, so now we presume it is 1.
            if(l_values.size() == 0){
                if(runParams.datasetFiles.size() == 0){
                    monitor.setStatusMessage("[2a] No datasets found. Exiting.");
                    monitor.setProgress(1);
                    questService.cancelQuestWithRunID(kpmSettings.kpmRunID);
                    return false;
                }
                for(def datasetFile : runParams.datasetFiles){
                    String internalID = kpmSettings.externalToInternalIDManager.getOrCreateInternalIdentifier(datasetFile.id.toString());

                    kpmSettings.MATRIX_FILES_MAP.put(internalID, numericToIndicatorMatrixService.parseToIndicatorMatrix(datasetFile, runParams));
                    kpmSettings.CASE_EXCEPTIONS_MAP.put(internalID, 1);
                }
            }else if(parameters.l_samePercentage) {
                l_values = runParams.datasetFiles.collect{
                    it.id
                }
            }
            else{
                // Ensuring we have the datafiles.
                if(runParams.datasetFiles.size() != l_values.size()){
                    monitor.setStatusMessage("[2b] Number of dataset files did not match that of the L value set. Exiting.");
                    monitor.setProgress(1);
                    questService.cancelQuestWithRunID(kpmSettings.kpmRunID);
                    return false;
                }
            }

            // Setting the L values and the file map.
            monitor.setStatusMessage("Setting up L values.");

            for(def i = 0; i < l_values.size(); i++){
                def l_val = l_values.get(i);
                def datasetFile

                /*  when we have percentages for multiple datasets using l_values does not make any sense, especially the check here under 'else'
                    I work around this problem by using the dataset ids directly since we are anyway assigning the same l values to all of them.
                 */
                if(!parameters.l_samePercentage){
                    l_val.refresh();
                    datasetFile = runParams.datasetFiles.find{ df -> df.id == Long.parseLong(l_val.datasetFileID) };
                }
                else{
                    datasetFile = runParams.datasetFiles.find{ df -> df.id == l_val };
                }

                if(datasetFile == null){
                    monitor.setStatusMessage("[3] Dataset file did not match the L-values ID. Exiting.");
                    monitor.setProgress(1);
                    questService.cancelQuestWithRunID(kpmSettings.kpmRunID);
                    return;
                }

                String internalID = kpmSettings.externalToInternalIDManager.getOrCreateInternalIdentifier(datasetFile.id.toString());
                kpmSettings.MATRIX_FILES_MAP.put(internalID, numericToIndicatorMatrixService.parseToIndicatorMatrix(datasetFile, runParams));

                def columns = 0;

                if(datasetFile.content.contains("\t")){
                    columns = datasetFile.content.replaceAll("(?m)^[ \t]*\r?\n", "").split("\n")[0].split("\t").length - 1;
                }

                def isINES = parameters.strategy == Strategy.INES;

                // If option is set for same L percentage for all datasets, set same value.
                if(parameters.l_samePercentage && !parameters.samePercentage_useRange) {
                    if (parameters.samePercentage_val > 100 && isINES) {
                        parameters.samePercentage_val = 100;
                    }
                    kpmSettings.MIN_L.put(internalID, parameters.samePercentage_val);
                    kpmSettings.INC_L.put(internalID, parameters.samePercentage_val);
                    kpmSettings.MAX_L.put(internalID, parameters.samePercentage_val);
                    kpmSettings.MIN_PER = parameters.samePercentage_val;
                    kpmSettings.MAX_PER = parameters.samePercentage_val;
                    kpmSettings.INC_PER = 1;
                    kpmSettings.CALCULATE_ONLY_SAME_L_VALUES = true;

                }else if(parameters.l_samePercentage && parameters.samePercentage_useRange){
                    if (parameters.samePercentage_val > 100 && isINES) {
                        parameters.samePercentage_val = 100;
                    }
                    if (parameters.samePercentage_val_max > 100 && isINES) {
                        parameters.samePercentage_val_max = 100;
                    }

                    if (parameters.samePercentage_val_step > 100 && isINES) {
                        parameters.samePercentage_val_step = 100;
                    }

                    if(parameters.samePercentage_val_step == 0){
                        parameters.samePercentage_val_step = 1;
                    }

                    kpmSettings.MIN_L.put(internalID, parameters.samePercentage_val);
                    kpmSettings.INC_L.put(internalID, parameters.samePercentage_val_step);
                    kpmSettings.MAX_L.put(internalID, parameters.samePercentage_val_max);

                    kpmSettings.MIN_PER = parameters.samePercentage_val;
                    kpmSettings.MAX_PER = parameters.samePercentage_val_max;
                    kpmSettings.INC_PER = parameters.samePercentage_val_step;
                    kpmSettings.VARYING_L_ID.add(internalID);
                    kpmSettings.IS_BATCH_RUN = true;
                    kpmSettings.INCLUDE_CHARTS = true;
                    kpmSettings.CALCULATE_ONLY_SAME_L_VALUES = true;
                }else{
                    if(l_val.val > columns && isINES){
                        l_val.val = columns;
                    }
                    // Otherwise set the individual values.
                    if(l_val.use_range){
                        if(l_val.val_max > columns && isINES){
                            l_val.val_max = columns;
                        }

                        if(l_val.val_step > columns && isINES){
                            l_val.val_step = columns;
                        }
                        kpmSettings.MIN_L.put(internalID, l_val.val);
                        kpmSettings.INC_L.put(internalID, l_val.val_step);
                        kpmSettings.MAX_L.put(internalID, l_val.val_max);
                        kpmSettings.VARYING_L_ID.add(internalID);
                        kpmSettings.IS_BATCH_RUN = true;
                        kpmSettings.INCLUDE_CHARTS = true;
                    }else{
                        kpmSettings.MIN_L.put(internalID, l_val.val);
                        kpmSettings.INC_L.put(internalID, l_val.val);
                        kpmSettings.MAX_L.put(internalID, l_val.val);
                        kpmSettings.CASE_EXCEPTIONS_MAP.put(internalID, l_val.val);
                    }
                }

                // 26-04-2015, L parameters should always be in percentage on the web edition.
                //kpmSettings.IsCaseExceptionsInPercentage.put(internalID, true);
            }


            // Figure out how many cores to use
            int maxNumOfCores = Runtime.getRuntime().availableProcessors();
            int numOfCores = grailsApplication?.config?.kpm?.num?.of?.cores?:maxNumOfCores
            if(numOfCores > maxNumOfCores) numOfCores = maxNumOfCores
            kpmSettings.NUMBER_OF_PROCESSORS = numOfCores

            // Creating the KPMGraph, and updating the kpmSettings.
            monitor.setStatusMessage("Creating graph.");
            kpmSettings = graphFileParserService.createAndSetGraph(kpmSettings, graph.content, GraphFileParserService.Separator.TAB);
            monitor.setStatusMessage("Finished graph.");

            kpmSettings.REMOVE_BENs = parameters.removeBENs;

            // Setting up the algorithm.
            if(parameters.strategy == Strategy.GLONE) {
                kpmSettings.USE_INES = false;
                kpmSettings.REMOVE_BENs = false;
                if (parameters.algorithm == Algorithm.Greedy) {
                    kpmSettings.ALGO = Algo.EXCEPTIONSUMGREEDY;
                } else if (parameters.algorithm == Algorithm.ACO) {
                    kpmSettings.ALGO = Algo.EXCEPTIONSUMACO;
                } else if (parameters.algorithm == Algorithm.Exact) {
                    kpmSettings.ALGO = Algo.EXCEPTIONSUMOPTIMAL;
                } else {
                    monitor.setStatusMessage("[4a] No algorithm was found. Exiting.");
                    monitor.setProgress(1);
                    questService.cancelQuestWithRunID(kpmSettings.kpmRunID);
                    return;
                }
            }

            if(parameters.strategy == Strategy.INES) {
                if (parameters.algorithm == Algorithm.Greedy) {
                    kpmSettings.ALGO = Algo.GREEDY;
                } else if (parameters.algorithm == Algorithm.ACO) {
                    kpmSettings.ALGO = Algo.LCG;
                } else if (parameters.algorithm == Algorithm.Exact) {
                    kpmSettings.ALGO = Algo.OPTIMAL;
                } else {
                    monitor.setStatusMessage("[4b] No algorithm was found. Exiting.");
                    monitor.setProgress(1);
                    questService.cancelQuestWithRunID(kpmSettings.kpmRunID);
                    return;
                }
            }

            // Have to check whether it is INES or GLONE.
            def isINES = false;
            if(parameters.strategy == Strategy.INES){
                isINES = true;
            }

            kpmSettings.GENE_EXCEPTIONS = 0;
            kpmSettings.ITERATION_BASED = true;
            kpmSettings.NUM_STARTNODES = 30;
            kpmSettings.NUMBER_OF_SOLUTIONS_PER_ITERATION = 20;
            kpmSettings.MAX_ITERATIONS = 3000;
            kpmSettings.MAX_RUNS_WITHOUT_CHANGE = 100;


            // Set how to treat unmapped nodes (back nodes)
            if(parameters.unmapped_nodes.toLowerCase().contains("positive")){
                kpmSettings.MAIN_GRAPH.setTreatBackNodes(KPMGraph.IN_POSITIVE);
            }else{
                kpmSettings.MAIN_GRAPH.setTreatBackNodes(KPMGraph.IN_NEGATIVE);
            }

            kpmSettings.IS_BATCH_RUN = true;

            // Set the number of wanted solutions.
            kpmSettings.NUM_SOLUTIONS = parameters.computed_pathways;
            if(runParams.linkType.toLowerCase().equals("or")){
                kpmSettings.COMBINE_OPERATOR = Combine.OR;
            }
            if(runParams.linkType.toLowerCase().equals("and")){
                kpmSettings.COMBINE_OPERATOR = Combine.AND;
            }

            kpmSettings.MAIN_GRAPH.setPositiveList(StringUtil.getLines(runParams.positiveNodes));
            kpmSettings.MAIN_GRAPH.setNegativeList(StringUtil.getLines(runParams.negativeNodes));

            kpmSettings.STARTING_TIME = System.nanoTime();
            // Starts a perturbation run
            if(runParams.withPerturbation){
                def perturbation = PerturbationParameters.get(runParams.perturbation.id);
                perturbation.refresh();

                if(!runParams.perturbation.isValid()){
                    monitor.setStatusMessage("[5] Invalid perturbation settings. Exiting.");
                    monitor.setProgress(1);
                    questService.cancelQuestWithRunID(kpmSettings.kpmRunID);
                    return false;
                }

                // Default perturbation technique set to NODE-REMOVE
                def technique = PerturbationService.getPerturbation(IPerturbation.PerturbationTags.NodeRemoval);

                // if it's another, then set it to that.
                if(runParams.perturbation.technique.toLowerCase().equals("edge-removal")){
                    technique = PerturbationService.getPerturbation(IPerturbation.PerturbationTags.EdgeRemoval);
                }

                if(runParams.perturbation.technique.toLowerCase().equals("edge-rewire")){
                    technique = PerturbationService.getPerturbation(IPerturbation.PerturbationTags.EdgeRewire);
                }

                if(runParams.perturbation.technique.toLowerCase().equals("Node-swap")){
                    technique = PerturbationService.getPerturbation(IPerturbation.PerturbationTags.NodeSwap);
                }

                def bprp = new BatchRunWithPerturbationParameters(
                        technique,
                        runParams.perturbation.startPercent,
                        runParams.perturbation.stepPercent,
                        runParams.perturbation.maxPercent,
                        runParams.perturbation.graphsPerStep,
                        isINES,
                        parameters.name,
                        monitor,
                        kpmListenerService,
                        true);
                def perturbationRunner = new BatchRunWithPerturbationRunner(bprp, kpmSettings);

                def kpmRunnable = new KpmRunnable(perturbationRunner, progressID);
                kpmRunnable.run();

            }else{
                println(kpmSettings);
                // Performing the normal single/batch run
                def batchRun = new BatchRunner(kpmID, monitor, kpmListenerService, kpmSettings);
                def kpmRunnable = new KpmRunnable(batchRun, progressID);
                kpmRunnable.run();
            }

        }catch(Exception e){
            KpmLogger.log(Level.SEVERE, e);
            monitor.setProgress(1);
            monitor.setCancelled();
            monitor.setStatusMessage("An error occurred. See the error log.");
            return false;
        }

        return true;
    }

}
