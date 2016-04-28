package kpm.web

import grails.converters.JSON
import kpm.web.base.BaseController
import kpm.web.graph.Graph
import kpm.web.kpm.parameters.Algorithm
import kpm.web.kpm.parameters.DatasetFileThreshold
import kpm.web.kpm.parameters.ExceptionParameters
import kpm.web.kpm.parameters.KpmParameters
import kpm.web.kpm.parameters.LParameters
import kpm.web.kpm.parameters.PerturbationParameters
import kpm.web.utils.FileUtil
import kpm.web.utils.FileWithText
import org.apache.commons.io.IOUtils
import org.hibernate.Hibernate
import org.springframework.web.multipart.MultipartFile

class RunKPMController extends BaseController{
    def runParametersService
    def datasetFileService
    def graphsService
    def queueService
    def questService

    def index() {
        runParametersService.newInstance(getUserID());

        redirect(action: "setupDatasets");
    }

    def setupDatasets(){
        def datasets = datasetFileService.get(getUserID(), true);

        if (request.getMethod() != 'POST') {
            render(view:"setupDatasets", model: [datasets: datasets]);
            return;
        }

        def setup = runParametersService.getSelected(getUserID());
        def datasetFileIDs =  params.list("selectedDatasets");
        if(datasets.any() && datasetFileIDs && datasetFileIDs.any()){
            for(def i = 0; i < datasetFileIDs.size(); i++){
                for(def j  = 0; j < datasets.size(); j++){
                    def selectedID = datasetFileIDs[i].toString();
                    def datasetID = datasets[j].getId().toString();
                    if(selectedID.equals(datasetID)){
                        setup.datasetFiles.add(datasets[j]);

                        if(params.keySet().contains("thresholdFor_" + datasetID)){

                            Double threshold = params.double("thresholdFor_" + datasetID)
                            if(threshold)
                                new DatasetFileThreshold(threshold: threshold,
                                        runParameters: setup,
                                        activeIfSmallerThanThreshold: (params.get("upperLower_" + datasetID)=="<") ,
                                        datasetFile: datasets[j]).save()
                            else{
                                flash.error = "Select a threshold for dataset " + datasets[j].name
                                setup.datasetFiles = null
                                render(view:"setupDatasets", model: [datasets: datasets]);
                                return
                            }
                        }
                        break;
                    }
                }
            }
        }
        def species = setup.datasetFiles.collect{it.species}.unique()

        if(species.size() > 1){
            flash.error = "Selected datasets stem from more than one species"
            setup.datasetFiles = null
            render(view:"setupDatasets", model: [datasets: datasets]);
            return
        }

        setup.positiveNodes =  params.get("positiveNodes") as String;
        setup.negativeNodes =  params.get("negativeNodes") as String;
        setup.goldStandardNodes = params.get("goldStandardNodes") as String;

        setup.linkType = params.get("linkType") as String;

        if(!setup.linkType){
            setup.linkType = "OR";
        }

        setup.save(flush: true, validate: true, failOnError: true);

        redirect(action:"parametersSetup", params: [species : species]);
    }


    // Step 3, parameters
    def parametersSetup() {
        println params
        def settings = runParametersService.getSelected(getUserID());
        if(!settings) {
            flash.message = "No settings found. Session expired. Please start a new run"
            redirect action:"setupDatasets"
        }
        def graphs = graphsService.get(getUserID(), true);

        if (request.getMethod() != 'POST') {
            def numbers = new HashMap<Integer, String>();
            for(def i = 1; i <= 20; i++){
                numbers.put(i, "$i%");
            }

            def allowedAlgorithms = new ArrayList<Algorithm>();
            Algorithm defaultAlgorithm = null;
            def allowedStr = grailsApplication?.config?.kpm?.allowed?.algorithms?:null;
            if(allowedStr != null) {
                for (String allowed : allowedStr) {
                    def alg = Algorithm.valueOf(allowed); // Note case-sensitivity
                    if (alg != null) {
                        allowedAlgorithms.add(alg);
                        defaultAlgorithm = alg;
                    }
                }
            } else{
                allowedAlgorithms.addAll([Algorithm.Greedy, Algorithm.ACO, Algorithm.Exact])
                defaultAlgorithm = Algorithm.Greedy
            }
            def maxConcurrentRuns = grailsApplication?.config?.kpm?.max?.allowed?.combinations?:20
            render(view: "parametersSetup", model: [currentSetup: settings, graphs: graphs.findAll{it.species == params.species}, datasets: settings.datasetFiles,
                                                    numberList: numbers, maxConcurrentRuns: maxConcurrentRuns, allowedAlgorithms: allowedAlgorithms, defaultAlgorithm: defaultAlgorithm]);
            return;
        } // We only continue with the next part, if we're posting

        def paramsSetup = params.get("parameterSetup") as KpmParameters;

        if(paramsSetup == null){
            flash.message = "An error occured. No parameter setup was found.";
            render(view: "parametersSetup", model: [currentSetup: settings, graphs: graphs, datasets: settings.datasetFiles]);
            return;
        }

        def k_values = params.get("k") as ExceptionParameters;

        if(k_values.use_range && k_values.val_step == 0){
            k_values.val_step = 1;
        }

        def lParams = new ArrayList<LParameters>();
        for (def i = 0; i < settings.datasetFiles.size(); i++) {
            def l = params.get("l" + i) as LParameters;


            if (l != null) {
                if(l.use_range && l.val_step == 0){
                    l.val_step = 1;
                }

                lParams.add(l);
            }else{
                flash.message = String.format("An error occured. No Case exceptions (L) were found for the dataset.");
                return;
            }
        }

        settings.refresh();
        runParametersService.updateValues(settings.getId(), paramsSetup, lParams, k_values);
        typeSelected("single");
    }

    def typeChoice() {    }

    def typeSelected(String choice) {
        def trimmedChoice = choice.trim().toLowerCase();

        def setup = runParametersService.getSelected(getUserID());
        setup.refresh();
        if (trimmedChoice.equals("single")) {
            setup.withPerturbation = false;
            setup.isBatch = false;
        } else if (trimmedChoice.equals("batch")) {
            setup.withPerturbation = false;
            setup.isBatch = true;
        } else {
            setup.withPerturbation = true;
            setup.isBatch = true;
        }

        setup.save(flush: true, validate: true, failOnError: true);
        if(setup.withPerturbation){
            redirect(action: "perturbationSetup");
        }else{
            redirect(action: "startRun");
        }
    }

    def perturbationSetup(){

        if (request.getMethod() != 'POST') {
            render(view:"perturbationSetup");
            return;
        }

        def perturbation = params.get("perturbation") as PerturbationParameters;

        if(perturbation){
            perturbation.save(flush: true, validate: true, failOnError: true);
            def setup = runParametersService.getSelected(getUserID());
            setup.perturbation = perturbation;
            setup.save(flush: true, validate: true, failOnError: true);
        }

        redirect(action: "startRun");
    }

    def startRun(){
        def settings = runParametersService.getSelected(getUserID());
        if (!settings?.isReadyToRun) {
            flash.message = "An error occurred during the settings check.";
        }
    }

    def startKPM(){
        def settings = runParametersService.getSelected(getUserID());
        if (!settings?.isReadyToRun) {
            flash.message = "An error occurred during the settings check.";
            redirect(action: "startRun");
            return;
        }

        def runParamsID = (Long) settings.getId();
        def questID = questService.newQuest(getUserID(), runParamsID).id;
        queueService.enqueue(runParamsID, questID);
        redirect(controller: "results", action: "index");
    }


    def cancelRun(String runID){
        questService.cancelQuestWithRunID(runID);
    }

    // Default error page.
    def error() {}

    def readFile(){
        def fileWithText = new FileWithText();

        def textFile = params.uploadedFile as MultipartFile;
        if(!textFile || textFile.size == 0){
            render fileWithText as JSON;
            return;
        }

        fileWithText.fileName = textFile.originalFilename;
        fileWithText.content = FileUtil.fileToString(textFile)

        render fileWithText as JSON;
    }

    // Actioner for the box to the left, where the settings are placed.
    def settingBox() {
        def settings = runParametersService.getSelected(getUserID());
        def graphs = graphsService.get(getUserID(), true);

        Graph graph = null;
        if(graphs.any() && settings.parameters && settings.parameters.graphID){
            for(def i = 0; i < graphs.size(); i++){
                if(graphs[i].id == settings.parameters.graphID){
                    graph = graphs[i];
                    break;
                }
            }
        }

        def k_values = ExceptionParameters.findByOwner(settings.parameters);
        def l_values = LParameters.findAllByOwner(settings.parameters);

        for(LParameters l: l_values){
            if(settings.parameters.l_samePercentage){
                if(settings.parameters.samePercentage_useRange){
                    l.val_max = settings.parameters.samePercentage_val_max;
                    l.val_step = settings.parameters.samePercentage_val_step;
                    l.use_range = true;
                }

                l.isPercentage = true;
                l.val = settings.parameters.samePercentage_val;
            }
        }

        render(view: "settingBox", model: [currentSetup: settings, selectedGraph: graph, k_values: k_values, l_values:l_values, selectedDatasets: settings.datasetFiles]);
    }

    // Actioner for the box to the left, where the settings are placed.
    def settingBoxByRunID(Long runParamsID) {
        def settings = runParametersService.getByID(runParamsID);

        if(settings == null){
            render "No settings found.";
            return;
        }

        def graphs = graphsService.get(getUserID(), true);

        Graph graph = null;
        if(graphs.any() && settings.parameters && settings.parameters.graphID){
            for(def i = 0; i < graphs.size(); i++){
                if(graphs[i].id == settings.parameters.graphID){
                    graph = graphs[i];
                    break;
                }
            }
        }

        def k_values = ExceptionParameters.findByOwner(settings.parameters);
        Hibernate.initialize(k_values);
        def l_values = LParameters.findAllByOwner(settings.parameters);
        l_values.each{
            Hibernate.initialize(it);
        }


        render(view: "settingBox", model: [currentSetup: settings, selectedGraph: graph, k_values: k_values, l_values:l_values, selectedDatasets: settings.datasetFiles]);
    }
}
