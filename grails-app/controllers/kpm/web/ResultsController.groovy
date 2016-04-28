package kpm.web

import grails.converters.JSON
import kpm.web.base.BaseController
import kpm.web.data.ImageFile
import kpm.web.kpm.results.CsvFormattedObject
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultNode
import kpm.web.kpm.results.ResultSet
import kpm.web.utils.GeneNameUtil
import kpm.web.utils.GraphUtil
import kpm.web.utils.StringUtil

class ResultsController extends BaseController{

    def index() {
        def attachedToID = params.get("attachedToID") as String;
        if(attachedToID == null || attachedToID.isEmpty()){
            attachedToID = getUserID();
            params.attachedToID = getUserID()
        }

        render(view: "index", model: [attachedToID: attachedToID]);
    }

    def seeResults(String runID){
        def images = ImageFile.findAllByAttachedToID(runID).toList();
        if(images && images.size() > 0){
            images = images.sort { x -> x.imageName };
        }

        def resultNodeSets = ResultSet.findAllByRunID(runID);
        def amountComputedNodeSets = 0;

        def k_values = new HashSet<Integer>();
        def l_values = new HashSet<Integer>();

        for(ResultSet resultSet : resultNodeSets){
            amountComputedNodeSets = resultSet.amountComputedNodeSets;

            if(!k_values.contains(resultSet.k)){
                k_values.add(resultSet.k);
            }
            if(!l_values.contains(resultSet.l)){
                l_values.add(resultSet.l);
            }
        }

        def computed_nodesetNr = new ArrayList<Integer>();

        for(def i = 1; i <= amountComputedNodeSets; i++) computed_nodesetNr.add(i);

        def k_list = k_values.toList().sort();
        def l_list = l_values.toList().sort();

        def isBatch = k_list.size() > 0 || l_list.size() > 0;
        render(view:"seeResults", model:[isBatch: isBatch, images:images, runID: runID, k_list: k_list, l_list: l_list, computedNr_list: computed_nodesetNr]);
    }

    def resultGraph(String runID){

        def resultNodeSet = ResultSet.findByRunID(runID);

        if(!resultNodeSet){
            flash.error = "No resulting node set found.";
            render new ResultGraph() as JSON;
            return;
        }

        def resGraphs = ResultGraph.findAllByOwner(resultNodeSet);

        render resGraphs.get(0) as JSON;
    }

    def resultGraphKL(String runId, int k_val, int l_val, int computedNodeSetNr, boolean switchNodeLabels, String nodeLabelType, String nodeLabelSubstitute){
        def resultSet = ResultSet.findWhere(runID: runId, k: k_val, l: l_val);
        if(!resultSet){
            flash.error = "No resultset was found for the given K and L values. [1]";
            render new ResultGraph() as JSON;
            return;
        }
        def resGraph = ResultGraph.findWhere(owner: resultSet, nodeSetNr: computedNodeSetNr);

        if(!resGraph){
            render "No nodeset graph were found for K = $k_val and L = $l_val.";
            render new ResultGraph() as JSON;
            return;
        }

        if(switchNodeLabels != null && switchNodeLabels && nodeLabelType != "" && nodeLabelSubstitute != ""){
            def nodeLabels = new ArrayList<String>();
            for(ResultNode node: resGraph.nodes){
                nodeLabels.add(node.name);
            }
            def convertedMap = GeneNameUtil.getConvertedIDs(nodeLabelType,  nodeLabelSubstitute, nodeLabels);

            resGraph.convertNames(convertedMap);
        }
        render resGraph as JSON;
    }

    def resultInfoTable(String runId, int k_val, int l_val){
        def resultSet = ResultSet.findWhere(runID: runId, k: k_val, l: l_val);

        if(!resultSet || resultSet == null){
            flash.error = "No resultset was found for the given K and L values. [1]";
            render flash as JSON;
            return;
        }

        def csvObject = new CsvFormattedObject();
        if(resultSet.csvResultsInfoTable != null){
            csvObject.data = resultSet.csvResultsInfoTable;
        }else{
            csvObject.data = "";
        }
        csvObject.lineSeparator = StringUtil.LineSeparator();

        render csvObject as JSON;
    }

    def resultGraphKLUnionSet(String runId, int k_val, int l_val, boolean switchNodeLabels, String nodeLabelType, String nodeLabelSubstitute) {

        def resultSet = ResultSet.findWhere(runID: runId, k: k_val, l: l_val);

        if(!resultSet){
            flash.error = "No graph was found for the given K and L values. [1]";
            render new ResultGraph() as JSON;
            return;
        }

        def resGraph = ResultGraph.findWhere(owner: resultSet, isUnionSet: true);

        if(!resGraph){
            render "No union nodeset graph was found for K = $k_val and L = $l_val.";
            render new ResultGraph() as JSON;
            return;
        }

        if(switchNodeLabels != null && switchNodeLabels && nodeLabelType != "" && nodeLabelSubstitute != ""){
            def nodeLabels = new ArrayList<String>();
            for(ResultNode node: resGraph.nodes){
                nodeLabels.add(node.name);
            }

            def convertedMap = GeneNameUtil.getConvertedIDs(nodeLabelType,  nodeLabelSubstitute, nodeLabels);

            resGraph.convertNames(convertedMap);
        }

        render resGraph as JSON;
    }

    def resultUnionGraphColors(String runId, int k_val, int l_val) {
        def resultSet = ResultSet.findWhere(runID: runId, k: k_val, l: l_val);

        if(!resultSet){
            flash.error = "No graph was found for the given K and L values. [1]";
            render new ResultGraph() as JSON;
            return;
        }

        def resGraph = ResultGraph.findWhere(owner: resultSet, isUnionSet: true);

        if(!resGraph){
            render "No union nodeset graph was found for K = $k_val and L = $l_val.";
            render new ResultGraph() as JSON;
            return;
        }

        getColorValueMapping(resGraph.maxNodeCount);
    }

    def getColorValueMapping(int maxVal){
        def colorMapping = GraphUtil.getColorValueMapping(1, maxVal);

        render colorMapping as JSON;
    }

    def getTextAsFile(String fileName, String text) {
        byte[] bytes = text.bytes

        response.setContentType("text/plain")
        response.setHeader("Content-disposition", "attachment; filename=\""+fileName+"\"")
        response.contentType = 'text-plain'
        response.setContentLength(bytes.size())
        response.outputStream << bytes
        response.outputStream.flush()
    }

    def getReplacementIDs(String nodeIdType, String replacementIdType){

        def nodeIDs = params.get("nodeIDs") as List<String>;

        def res = GeneNameUtil.getConvertedIDs(nodeIdType, replacementIdType, nodeIDs);

        render res as JSON;
    }
}
