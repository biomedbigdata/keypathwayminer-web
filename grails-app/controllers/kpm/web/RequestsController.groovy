package kpm.web

import grails.converters.JSON
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import kpm.web.base.BaseController
import kpm.web.data.DatasetFile
import kpm.web.exceptions.InvalidRequestException
import kpm.web.exceptions.InvalidRunParametersException
import kpm.web.graph.Graph
import kpm.web.kpm.parameters.RunParameters
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultNode
import kpm.web.kpm.results.ResultSet
import kpm.web.utils.GeneNameUtil
import kpm.web.utils.progress.Quest
import kpm.web.utils.requests.RequestAnswer
import kpm.web.utils.requests.RequestRunStatus

/**
 * For use as a service to called from other apps/sites.
 */
public class RequestsController extends BaseController{
    def runParametersService
    def questService
    def queueService

    def index() {}

    private RunParameters parseJSON(String jsonRunParameters, String jsonDatasets, String jsonGraph){
        HashSet<DatasetFile> datasets = new HashSet<DatasetFile>();

        def slurper = new JsonSlurper();

        if(jsonGraph != null && jsonGraph != ""){
            def graphMap = slurper.parseText(jsonGraph) as HashMap<String, Object>;
            if(graphMap != null && graphMap.size() > 0){
                def graph = new Graph().updateValuesByJSON(graphMap);
            }
        }
        // Parsing the datasets:
        def datasetMap = slurper.parseText(jsonDatasets) as ArrayList<HashMap<String, Object>>;

        if (datasetMap == null || datasetMap.size() == 0) {
            throw new InvalidRequestException("No datasets were found. (Also must be a list)");
        }

        for (HashMap<String, Object> jsonDataset : datasetMap) {
            def dataset = new DatasetFile().updateValuesByJSON(jsonDataset);
            datasets.add(dataset);
        }

        // Parsing the run parameters.
        def runParamsMap = slurper.parseText(jsonRunParameters) as HashMap<String, Object>;

        def runParamsID = runParametersService.createFromJSON(runParamsMap, datasets);

        return RunParameters.get(runParamsID);
    }

    def submit(){
        def res = new RequestAnswer();

        if(!request.getParameter("kpmSettings")){
            res.comment = "kpmSettings parameter is missing.";
            res.success = false;
            render res as JSON;
            return;
        }

        if(!request.getParameter("datasets")){
            res.comment = "datasets parameter is missing.";
            res.success = false;
            render res as JSON;
            return;
        }

        def jsonRunParameters = request.getParameter("kpmSettings") as String;
        def jsonDatasets = request.getParameter("datasets") as String;
        def jsonGraph = request.getParameter("graph") as String;
        def jsonNodeLabelType = request.getParameter("nodeLabelType") as String;
        def jsonNodeLabelSubstitute = request.getParameter("nodeLabelSubstitute") as String;

        try {
            def settings = parseJSON(jsonRunParameters, jsonDatasets, jsonGraph);
            if (!settings) {
                res.comment = "The input parameters were not parsed successfully.";
                res.success = false;
                render res as JSON;
                return;
            }

            def quest = questService.newQuest(settings.attachedToID);
            def questID = quest.getId();

            def runParamsID = (Long) settings.getId();
            queueService.enqueue(runParamsID, questID);

            def finishedCorrectly = true;
            def stillRunning = true;
            while(stillRunning){
                quest = Quest.get(questID);

                if(!quest){
                    throw new InvalidRunParametersException("[KpmRunnable] No quest found.");
                }

                quest.refresh();

                println("[$quest.progress] $quest.statusMessage");

                if(quest.isCancelled || quest.isCompleted){
                    stillRunning = false;

                    if(quest.isCancelled){
                        finishedCorrectly = false;
                    }
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    stillRunning = false;
                    finishedCorrectly = false;
                }
            }


            if(!finishedCorrectly){
                throw new Exception("The run did not finish correctly.");
            }

            List<ResultSet> resultSets = ResultSet.findAllByRunID(quest.runID);

            if(!resultSets || resultSets.size() == 0){

                throw new InvalidRunParametersException("No results found.");
            }

            for(ResultSet resultSet : resultSets){
                def resGraphs = ResultGraph.findAllByOwner(resultSet);
                if(!resGraphs || resGraphs.size() == 0){
                    continue;
                }

                res.resultGraphs = resGraphs
                res.runID = resultSet.runID;
                res.resultGraphs.addAll(resGraphs);
            }

            try{
                if(jsonNodeLabelType != null && jsonNodeLabelSubstitute != null){
                    def allNodes = new HashSet<String>();
                    for(ResultGraph g: res.resultGraphs){
                        for(ResultNode n: g.nodes){
                            if(!allNodes.contains(n.name)){
                                allNodes.add(n.name);
                            }
                        }
                    }

                    def map = GeneNameUtil.getConvertedIDs(jsonNodeLabelType, jsonNodeLabelSubstitute, allNodes.toList());
                    for(ResultGraph g: res.resultGraphs){
                        g.convertNames(map);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            res.resultUrl = createLink(controller: "results", action: "seeResults", params: [runID:res.runID], absolute: true).toString();
            res.comment = "Finished successfully with " + res.resultGraphs.size()+ " graphs.";

        }catch(InvalidRequestException e){
            res.comment = e.message;
            res.success = false;
        }catch(InvalidRunParametersException e){
            res.comment = e.message;
            res.success = false;
        }catch(Exception e){
            res.comment = e.message;
            res.success = false;
        }

        render res as JSON;
    }

    def submitAsync(){
        def res = new RequestAnswer();

        if(!request.getParameter("kpmSettings")){
            res.comment = "kpmSettings parameter is missing.";
            res.success = false;
            render res as JSON;
            return;
        }

        if(!request.getParameter("datasets")){
            res.comment = "datasets parameter is missing.";
            res.success = false;
            render res as JSON;
            return;
        }
        def jsonRunParameters = request.getParameter("kpmSettings") as String;
        def jsonDatasets = request.getParameter("datasets") as String;
        def jsonGraph = request.getParameter("graph") as String;

        try {
            def settings = parseJSON(jsonRunParameters, jsonDatasets, jsonGraph);
            if (!settings) {
                res.comment = "The input parameters were not parsed successfully.";
                res.success = false;
                render res as JSON;
                return;
            }

            def runID = UUID.randomUUID().toString();
            def quest = questService.newQuest(settings.attachedToID, runID);
            def questID = quest.getId();

            def runParamsID = (Long) settings.getId();
            queueService.enqueue(runParamsID, questID);
            res.comment = "KPM run enqueued, with quest ID '$questID'.";
            res.questID = questID;

            quest.refresh();
            res.runID = quest.runID;

        }catch(InvalidRequestException e){
            res.comment = e.message;
            res.success = false;
        }catch(InvalidRunParametersException e){
            res.comment = e.message;
            res.success = false;
        }catch(Exception e){
            res.comment = e.message;
            res.success = false;
        }

        res.resultUrl = createLink(controller: "results", action: "seeResults", params: [runID:res.runID], absolute: true).toString();
        render res as JSON;
    }

    def runStatus(){
        def res = new RequestRunStatus();

        def questID = request.getParameter("questID") as long;
        if(questID == null){
            res.runExists = false;
            render res as JSON;
            return;
        }

        def quest = Quest.get(questID);
        if(!quest){
            res.runExists = false;
            render res as JSON;
            return;
        }

        quest.refresh();
        res.completed = quest.isCompleted;
        res.cancelled = quest.isCancelled;
        res.progress = quest.progress;
        res.runExists = true;

        render res as JSON;
    }

    def results(){
        def res = new RequestAnswer();

        def jsonNodeLabelType = request.getParameter("nodeLabelType") as String;
        def jsonNodeLabelSubstitute = request.getParameter("nodeLabelSubstitute") as String;

        def questID = request.getParameter("questID") as long;
        if(questID == null){
            res.comment = "No run attached to the given quest ID '$questID'";
            res.success = false;
            render res as JSON;
            return;
        }

        def quest = Quest.get(questID);
        if(!quest){
            res.comment = "No run attached to the given quest ID '$questID'";
            res.success = false;
            render res as JSON;
            return;
        }

        res.questID = quest.id;

        if(!quest.isCompleted && !quest.isCancelled){
            res.comment = "Run attached to the given quest ID '$questID', is still running.";
            res.success = false;
            render res as JSON;
            return;
        }

        if(quest.isCompleted){
            List<ResultSet> resultSets = ResultSet.findAllByRunID(quest.runID);
            for(ResultSet resultSet : resultSets){
                def resGraphs = ResultGraph.findAllByOwner(resultSet);
                if(!resGraphs || resGraphs.size() == 0){
                    continue;
                }
                res.resultGraphs = resGraphs;
            }
            try{
                if(jsonNodeLabelType != null && jsonNodeLabelSubstitute != null){
                    def allNodes = new HashSet<String>();
                    for(ResultGraph g: res.resultGraphs){
                        for(ResultNode n: g.nodes){
                            if(!allNodes.contains(n.name)){
                                allNodes.add(n.name);
                            }
                        }
                    }

                    def map = GeneNameUtil.getConvertedIDs(jsonNodeLabelType, jsonNodeLabelSubstitute, allNodes.toList());
                    for(ResultGraph g: res.resultGraphs){
                        g.convertNames(map);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            res.success = true;
        }

        if(quest.isCancelled){
            res.comment = "Run attached to the given quest ID '$questID', has been cancelled.";
            res.success = false;
        }

        render res as JSON;
    }

    def quests(String attachedToId, boolean hideTitle){

        if(attachedToId == null || attachedToId.isEmpty()){
            render "No attachedToID.";
            return;
        }

        if(hideTitle == null){
            hideTitle = false;
        }

        def quests =  questService.getAllAttachedToId(attachedToId);
        if(quests.size() == 0){
            render "Found no quests/runs attached to the ID: $attachedToId.";
            return;
        }

        render(view: "quests", model: [quests : quests, attachedToID : attachedToId, hideTitle : hideTitle]);
    }

}
