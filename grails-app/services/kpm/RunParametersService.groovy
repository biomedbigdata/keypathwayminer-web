package kpm

import dk.sdu.kpm.logging.KpmLogger
import grails.transaction.Transactional
import kpm.web.data.DatasetFile
import kpm.web.exceptions.InvalidRequestException
import kpm.web.exceptions.UnknownQuestException
import kpm.web.graph.Graph
import kpm.web.kpm.parameters.*
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultSet
import kpm.web.utils.requests.RequestUtil
import org.hibernate.Hibernate

import java.util.logging.Level

@Transactional
class RunParametersService {
    static transactional = true
    def datasetFileService
    def graphsService

    public RunParameters newInstance(String attachedToID){
        return RunParameters.withTransaction {
            def params = RunParameters.findAllByAttachedToID(attachedToID);
            params.each{ p ->
                p.selectedByUserAsCurrent = false;
                p.save(flush: true, validate: true, failOnError: true);
            }

            def runParameters = new RunParameters();
            runParameters.attachedToID = attachedToID;
            runParameters.perturbation = new PerturbationParameters();
            runParameters.perturbation.save(validate: true, failOnError: true);


            def kpmParams = new KpmParameters();
            kpmParams.save(flush: true, validate: true, failOnError: true);
            kpmParams.refresh();

            def k_values = new ExceptionParameters();
            k_values.owner = kpmParams;
            k_values.save(flush: true, validate: true, failOnError: true);

            runParameters.parameters = kpmParams;
            runParameters.selectedByUserAsCurrent = true;
            runParameters.save(flush: true, validate: true, failOnError: true);

            return runParameters;
        }
    }

    public RunParameters getSelected(String attachedToId){
        def runParams = RunParameters.findAll{attachedToID == attachedToId && selectedByUserAsCurrent == true};

        if(runParams.size() == 0){
            runParams = RunParameters.findAll{attachedToID == attachedToId};
            if(runParams.size() > 0){
                def params = runParams.first();
                params.selectedByUserAsCurrent = true;
                return params;
            }
        }else if(runParams.size() == 1){
            return runParams.first();
        }else{
            def params = runParams.first();
            params.selectedByUserAsCurrent = true;
            return params;
        }

        return null;
    }

    public RunParameters get(String attachedToId){

        def size = RunParameters.findAll{attachedToID == attachedToId}.size();

        if(size == 0){
            return null;
        }

        def settings = RunParameters.where{attachedToID == attachedToId}.first();
        settings.lock();

        return settings;
    }

    public Long createFromJSON(HashMap<String, Object> result, HashSet<DatasetFile> datasets) throws InvalidRequestException{
        def  attachedToID = new String();

        if(RequestUtil.ContainsKeyOrThrow(result, "attachedToID")){
            attachedToID = result["attachedToID"] as String;
        }

        def runParams = newInstance(attachedToID);

        if(runParams.datasetFiles == null){
            runParams.datasetFiles = new HashSet<DatasetFile>();
        }

        datasets.each {
            runParams.datasetFiles.add(it);
        }

        if(result.containsKey("withPerturbation")){
            runParams.withPerturbation = result["withPerturbation"].equals("true");
        }else{
            runParams.withPerturbation = false;
        }

        if(result.containsKey("linkType")){
            runParams.linkType = result["linkType"] as String;
        }

        if(result.containsKey("goldStandardNodes")){
            runParams.goldStandardNodes = result["goldStandardNodes"] as String;
        }

        if(RequestUtil.ContainsKeyOrThrow(result, "attachedToID")){
            runParams.attachedToID = result["attachedToID"] as String;
        }

        if(result.containsKey("positiveNodes")){
            runParams.positiveNodes = result["positiveNodes"] as String;
        }

        if(result.containsKey("negativeNodes")){
            runParams.negativeNodes = result["negativeNodes"] as String;
        }

        if(runParams.withPerturbation){
            if(RequestUtil.ContainsKeyOrThrow(result,"perturbation")){
                def res = result["perturbation"] as HashMap<String, Object>;
                if(RequestUtil.ContainsKeyOrThrow(res, "technique")){
                    runParams.perturbation.technique = res["technique"] as String;
                }

                if(RequestUtil.ContainsKeyOrThrow(res, "startPercent")){
                    runParams.perturbation.startPercent = res["startPercent"] as int;
                }

                if(RequestUtil.ContainsKeyOrThrow(res, "stepPercent")){
                    runParams.perturbation.stepPercent = res["stepPercent"] as int;
                }

                if(RequestUtil.ContainsKeyOrThrow(res, "maxPercent")){
                    runParams.perturbation.maxPercent = res["maxPercent"] as int;
                }

                if(RequestUtil.ContainsKeyOrThrow(res, "graphsPerStep")){
                    runParams.perturbation.graphsPerStep = res["graphsPerStep"] as int;
                }
            }
        }

        if(RequestUtil.ContainsKeyOrThrow(result,"parameters")){
            def kpmParamMap = result["parameters"] as HashMap<String, Object>;

            if(RequestUtil.ContainsKeyOrThrow(kpmParamMap,"name")){
                runParams.parameters.name = kpmParamMap["name"] as String;
            }

            if(RequestUtil.ContainsKeyOrThrow(kpmParamMap,"algorithm")){
                runParams.parameters.algorithm = kpmParamMap["algorithm"] as Algorithm;
            }

            if(RequestUtil.ContainsKeyOrThrow(kpmParamMap,"strategy")){
                if(kpmParamMap["strategy"].equals("INES")){
                    runParams.parameters.strategy = Strategy.INES;
                }else{
                    runParams.parameters.strategy = Strategy.GLONE;
                }
            }

            if(RequestUtil.ContainsKeyOrThrow(kpmParamMap,"unmapped_nodes")){
                runParams.parameters.unmapped_nodes = kpmParamMap["unmapped_nodes"] as String;
            }

            if(RequestUtil.ContainsKeyOrThrow(kpmParamMap,"computed_pathways")){
                runParams.parameters.computed_pathways = kpmParamMap["computed_pathways"] as int;
            }

            def mustHaveGraphName = false;
            if(kpmParamMap.containsKey("graphID")){
                Long graphID;
                try{
                    graphID = kpmParamMap["graphID"] as Long;
                }catch(Exception e){
                    KpmLogger.log(Level.WARNING, e);
                    throw new InvalidRequestException("Graph ID '"+kpmParamMap["graphID"]+"' could not be parsed to a Long.");
                }

                def graph = Graph.get(graphID);
                if(!graph){
                    mustHaveGraphName = true;
                }else{
                    runParams.parameters.graphID = graph.id;
                }

            }else{
                mustHaveGraphName = true;
            }

            if(mustHaveGraphName){
                if(RequestUtil.ContainsKeyOrThrow(kpmParamMap,"graphName")){
                    def graphName = kpmParamMap["graphName"] as String;
                    def graph = graphsService.get(attachedToID, graphName);
                    if(graph == null){
                        throw new InvalidRequestException("Graph was not found in database. (Graph name: '$graphName', attached to ID: '$attachedToID')");
                    }

                    runParams.parameters.graphID = graph.id;
                }
            }
            if(kpmParamMap.containsKey("samePercentage_val")){
                runParams.parameters.samePercentage_val = kpmParamMap["samePercentage_val"] as int;
            }else if(runParams.parameters.l_samePercentage){
                // throw exception only when we want same percentage value, but don't have the value.
                throw new InvalidRequestException("Missing samePercentage_val field.");
            }

            runParams.parameters.save(flush: true, validate: true, failOnError: true);


            if(kpmParamMap.containsKey("removeBENs")){
                runParams.parameters.removeBENs = kpmParamMap["removeBENs"].equals("true");
            }else{
                runParams.parameters.removeBENs = false;
            }

            if(kpmParamMap.containsKey("l_samePercentage")){
                runParams.parameters.l_samePercentage = kpmParamMap["l_samePercentage"].equals("true");
            }else{
                runParams.parameters.l_samePercentage = false;
            }

            def l_valueKeys = kpmParamMap.keySet().findAll { x -> x.startsWith("l_values") };

            if (l_valueKeys != null) {
                l_valueKeys.each { l_valKey ->
                    def res = kpmParamMap[l_valKey] as HashMap<String, Object>;
                    def l_val = new LParameters();
                    if (RequestUtil.ContainsKeyOrThrow(res, "val")) {
                        l_val.val = res["val"] as int;
                    }

                    if(l_val.val < 0){
                        throw new InvalidRequestException("Field l_values[].val must be between > 0.");
                    }

                    def mustHaveRangeValues = false;
                    if (res.containsKey("use_range")) {
                        l_val.use_range = res["use_range"].equals("true");
                        mustHaveRangeValues = l_val.use_range;
                    }else{
                        l_val.use_range = false;
                    }

                    if (mustHaveRangeValues) {
                        if (RequestUtil.ContainsKeyOrThrow(res, "val_step")) {
                            l_val.val_step = res["val_step"] as int;
                        }

                        if (RequestUtil.ContainsKeyOrThrow(res, "val_max")) {
                            l_val.val_max = res["val_max"] as int;
                        }


                        if(l_val.val_max < 0){
                            throw new InvalidRequestException("Field l_val.val_max must be > 0.");
                        }

                        if(l_val.val >= l_val.val_max){
                            throw new InvalidRequestException("Field l_val.val_max must be greater than l_val.val.");

                        }

                        if(l_val.val_step < 0){
                            throw new InvalidRequestException("Field l_val.val_step must be > 0.");
                        }


                        if(l_val.val_max - l_val.val < l_val.val_step){
                            throw new InvalidRequestException("Field l_val.val_step must be lower than l_val.val_max - l_val.val.");

                        }
                    }

                    if (res.containsKey("isPercentage")) {
                        l_val.isPercentage = res["isPercentage"].equals("true");
                    } else {
                        l_val.isPercentage = false;
                    }

                    def datasetName = "";
                    if (RequestUtil.ContainsKeyOrThrow(res, "datasetName")) {
                        datasetName = res["datasetName"] as String;
                    }

                    def datasetID = datasetFileService.getID(attachedToID, datasetName);
                    if (datasetID == 0) {
                        throw new InvalidRequestException("Dataset '$datasetName' not found. (attached to ID: '$attachedToID')");
                    }

                    l_val.datasetFileID = datasetID.toString();

                    l_val.owner = runParams.parameters;
                    l_val.save(flush: true, validate: true, failOnError: true);
                }
            }

            def k_values = ExceptionParameters.findByOwner(runParams.parameters);
            if (kpmParamMap.containsKey("k_values")) {
                def res = kpmParamMap["k_values"] as HashMap<String, Object>;

                if (RequestUtil.ContainsKeyOrThrow(res, "val")) {
                    k_values.val = res["val"] as int;
                }

                if(k_values.val < 0){
                    throw new InvalidRequestException("Field k_values.val must be >= 0.");
                }

                def mustHaveRangeValues = false;
                if (res.containsKey("use_range")) {
                    k_values.use_range = res["use_range"].equals("true");
                    mustHaveRangeValues = k_values.use_range;
                }else{
                    k_values.use_range = false;
                }

                if (mustHaveRangeValues) {

                    if (RequestUtil.ContainsKeyOrThrow(res, "val_max")) {
                        k_values.val_max = res["val_max"] as int;
                    }

                    if(k_values.val_max < 0){
                        throw new InvalidRequestException("Field k_values.val_max must be > 0.");
                    }

                    if(k_values.val >= k_values.val_max){
                        throw new InvalidRequestException("Field k_values.val_max must be greater than k_values.val.");

                    }

                    if (RequestUtil.ContainsKeyOrThrow(res, "val_step")) {
                        k_values.val_step = res["val_step"] as int;
                    }


                    if(k_values.val_step < 0){
                        throw new InvalidRequestException("Field k_values.val_step must be > 0.");
                    }


                    if(k_values.val_max - k_values.val < k_values.val_step){
                        throw new InvalidRequestException("Field k_values.val_step must be lower than k_values.val_max - k_values.val.");

                    }

                }

                if (res.containsKey("isPercentage")) {
                    k_values.isPercentage = res["isPercentage"].equals("true");
                } else {
                    k_values.isPercentage = false;
                }

            }else{
                k_values.val = 0;
                k_values.use_range = false;
                k_values.isPercentage = false;
                k_values.val_max = 0;
                k_values.val_step = 0;
            }
            k_values.save(flush: true, validate: true, failOnError: true);
        }

        if (!runParams.save(flush: true, validate: true, failOnError: true)) {
            runParams.errors.each {
                println it
            }
        }

        return runParams.getId();
    }

    public void delete(Long runParameterID){
        def run = RunParameters.get(runParameterID)
        if(!run){
            return
        }
        try {
            if (run.parameters != null) {

                def k_values = ExceptionParameters.findByOwner(run.parameters);
                if (k_values != null) {
                    k_values.delete(flush: true, validate: true, failOnError: true);
                    println("k_values removed.");
                }

                def l_values = LParameters.findAllByOwner(run.parameters);
                if (l_values.size() > 0) {
                    LParameters.deleteAll(l_values);
                    println("l_values removed.");
                }

            }

            // KpmParameters and Perturbation parameters are deleted through cascade settings.
            run.delete(flush: true, validate: true, failOnError: true);
            println("run deleted");
        } catch(Exception e){
            println e.getMessage()
        }
    }

    public void reset(String attachedToID){
        RunParameters.withTransaction {

            RunParameters.where {attachedToID == attachedToID}.each { rp ->
                if(rp.parameters != null){

                    def k_values = ExceptionParameters.findByOwner(rp.parameters);
                    if(k_values != null){
                        k_values.delete();
                    }

                    def l_values = LParameters.findAllByOwner(rp.parameters);
                    if(l_values.size() > 0){
                        LParameters.deleteAll(l_values);
                    }
                }

                rp.delete();
            }
        }
    }

    public void updateValues(Long settingsID, KpmParameters paramsSetup, List<LParameters> lParams, ExceptionParameters kParams){
        RunParameters.withTransaction {
            try{
                if(!paramsSetup){
                    throw new Exception("No KpmParameters found.")
                }

                def settings = RunParameters.lock(settingsID);
                def kpmParameters = KpmParameters.lock(settings.parameters.id);

                kpmParameters.name = paramsSetup.name;
                kpmParameters.algorithm = paramsSetup.algorithm;
                kpmParameters.computed_pathways = paramsSetup.computed_pathways;
                kpmParameters.graphID = paramsSetup.graphID;
                kpmParameters.l_samePercentage = paramsSetup.l_samePercentage;
                kpmParameters.removeBENs = paramsSetup.removeBENs;
                if(paramsSetup.strategy == Strategy.INES){
                    kpmParameters.strategy = Strategy.INES;
                }else{
                    kpmParameters.strategy = Strategy.GLONE;
                }
                kpmParameters.unmapped_nodes = paramsSetup.unmapped_nodes;
                kpmParameters.samePercentage_val = paramsSetup.samePercentage_val;
                kpmParameters.samePercentage_val_max = paramsSetup.samePercentage_val_max;
                kpmParameters.samePercentage_val_step = paramsSetup.samePercentage_val_step;

                if(kpmParameters.samePercentage_val_step == 0){
                    kpmParameters.samePercentage_val_step = 1;
                }

                kpmParameters.samePercentage_useRange = paramsSetup.samePercentage_useRange;

                lParams.each { new_lVal ->
                    def l_value = LParameters.findWhere(owner: kpmParameters, datasetFileID: new_lVal.datasetFileID);
                    if(l_value != null){
                        l_value.delete(flush: true, validate: true, failOnError: true);
                    }

                    if(!new_lVal.val || new_lVal.val == null){
                        new_lVal.val = 0;
                    }

                    if(!new_lVal.val_step || new_lVal.val_step == null){
                        new_lVal.val_step = 0;
                    }

                    if(!new_lVal.val_max || new_lVal.val_max == null){
                        new_lVal.val_max = 0;
                    }

                    new_lVal.owner = kpmParameters;
                    if (!new_lVal.save(flush: true, validate: true, failOnError: true)) {
                        new_lVal.errors.each {
                            println it
                        }
                    }
                }

                def k_values = ExceptionParameters.findByOwner(kpmParameters);
                if(!k_values){
                    throw new UnknownQuestException("No K values found");
                }
                k_values.updateValues(kParams);

                if (!kpmParameters.save(flush: true, validate: true, failOnError: true)) {
                    kpmParameters.errors.each {
                        println it
                    }
                }

                settings.isReadyToRun = true;
                if (!settings.save(flush: true, validate: true, failOnError: true)) {
                    settings.errors.each {
                        println it
                    }
                }
            }   catch(Exception e)
            {
                KpmLogger.log(Level.SEVERE, e);
                System.out.println(e.stackTrace);
            }}
    }

    public RunParameters getByID(Long id) {
        return RunParameters.withTransaction {
            def runParams = RunParameters.get(id);

            if(!runParams){
                return null;
            }

            Hibernate.initialize(runParams);
            Hibernate.initialize(runParams.perturbation);
            Hibernate.initialize(runParams.parameters);

            return runParams;
        }
    }
}
