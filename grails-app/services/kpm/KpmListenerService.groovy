package kpm

import dk.sdu.kpm.graph.GeneNode
import dk.sdu.kpm.graph.KPMGraph
import dk.sdu.kpm.logging.KpmLogger
import dk.sdu.kpm.results.IKPMResultItem
import dk.sdu.kpm.results.IKPMResultSet
import dk.sdu.kpm.results.IKPMRunListener
import kpm.web.data.ImageFile
import kpm.web.kpm.results.ResultEdge
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultNode
import kpm.web.kpm.results.ResultSet
import kpm.web.utils.GraphUtil
import kpm.web.utils.StringUtil

import java.util.logging.Level

class KpmListenerService implements IKPMRunListener{
    static transactional = true
    def questService
    def grailsApplication

    @Override
    void runFinished(IKPMResultSet resultSet) {
        def kpmRunID = resultSet.getKpmID();

        System.out.println("Finishing run: " + kpmRunID);
        try{
            questService.updateTitleByRunID(kpmRunID, "Performing post-run parsing");

            def width = 800;
            def height = 600;
            questService.updateMessageByRunID(kpmRunID,"Creating charts.");

            // Handling charts, removed for now
            /*

            for(String chartName : resultSet.getCharts().keySet()){
                def chart = resultSet.getCharts().get(chartName);

                def imageName = chartName;
                int indxOfLastSpace = imageName.lastIndexOf(" ");
                if(imageName.endsWith("%") && indxOfLastSpace == imageName.length() - 3) {
                    String start = chartName.substring(0, indxOfLastSpace);
                    String end = chartName.substring(indxOfLastSpace + 1);

                    imageName = start + " 0" + end;
                }

                def os = new ByteArrayOutputStream();
                try{
                    chart.writeChartAsJpeg(os, width, height);
                    def image = new ImageFile();
                    image.attachedToID = kpmRunID;
                    image.contentType = "image/jpeg";
                    image.data_base64 = os.toByteArray().encodeBase64();
                    image.size = image.data_base64.length();
                    image.width = width;
                    image.height = height;
                    image.imageName = imageName;
                    image.save(flush: true, validate: true, failOnError: true);

                }catch(Exception e){
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                finally {
                    os.close();
                }
            }
             */
            // Filtering all used edges into a map:
            def kpmGraph = resultSet.getKpmSettings().MAIN_GRAPH;
            System.out.println("allEdges.size(): " + kpmGraph.getEdgeList().size());


            // Saving standard set:
            questService.updateMessageByRunID(kpmRunID,"Parsing results.");

            for(def indx = 0; indx < resultSet.getResults().size(); indx++){
                    try{
                        IKPMResultItem batchResult = resultSet.getResults().get(indx);
                        def res = new ResultSet();
                        res.k = batchResult.getK();
                        res.l = batchResult.getL();


                        res.csvResultsInfoTable = StringUtil.getCSVFormat(batchResult.resultsInfoTableHeaders) + StringUtil.LineSeparator();
                        res.csvResultsInfoTable += StringUtil.getCSVFormat(batchResult.resultsInfoTable);

                        def klStr = "Saving graphs for k = $res.k, l = $res.l: ";
                        System.out.println(klStr);

                        res.nodeIDs = batchResult.getUnionNodeSet().keySet();
                        res.amountComputedNodeSets = batchResult.allComputedNodeSets.size();
                        res.runID = kpmRunID;
                        if (!res.save(flush: true, validate: true, failOnError: true)) {
                            res.errors.each {
                                println it
                            }
                        }
                        def i = 0;

                        questService.updateMessageByRunID(kpmRunID, klStr + "union graph.");
                        def unionNodeSet = batchResult.getUnionNodeSet();
                        this.saveResultGraph(unionNodeSet, true, i, res, kpmGraph, batchResult.getUnionNodeSetCounts());

                        for(Map<String, GeneNode> nodeSet : batchResult.allComputedNodeSets){
                            questService.updateMessageByRunID(kpmRunID, klStr + "graph nr. $i.");
                            i++;
                            this.saveResultGraph(nodeSet, false, i, res, kpmGraph, null);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        KpmLogger.log(Level.SEVERE, e);
                    }
                }
        }catch(Exception e){
            e.printStackTrace();
            KpmLogger.log(Level.SEVERE, e);
            questService.cancelQuestWithRunID(kpmRunID);
        }finally{
            // Complete the quest to signal we completed.
            System.out.println("Finished.");

            questService.completeQuestWithRunID(kpmRunID);
        }
    }

    @Override
    void runCancelled(String s, String runID) {
        System.out.println("The run was cancelled for some reason:\n" + s);

        questService.cancelQuestWithRunID(runID);
    }

    ResultGraph saveResultGraph(
            Map<String, GeneNode> nodeSet,
            boolean isUnionSet,
            int nodeSetNr,
            ResultSet res,
            KPMGraph kpmGraph,
            Map<String, Integer> nodeSetCounts
    ){
        def resGraph = new ResultGraph();

        try{
            def nodeMap = new HashMap<String, ResultNode>();
            def edgeMap = new HashMap<String, ResultEdge>();
            def maxAmount = 1;
            if(nodeSetCounts != null){
                for(int val : nodeSetCounts.values()){
                    if(val > maxAmount){
                        maxAmount = val;
                    }
                }
            }
            /* check size of solution */
            /*if(maxAmount > grailsApplication?.config?.kpm?.max?.result?.graph?.size?:100)

                throw new Exception("The result graph was too large. Change settings or repeat analysis in Cytoscape.")
             */
            for(GeneNode node : nodeSet.values() ){
                def edges = kpmGraph.getIncidentEdges(node);

                for(String incidentEdge : edges){
                    def edgeParts = incidentEdge.split(" ");

                    if(edgeParts.length != 3){
                        continue;
                    }

                    def edge = new String[2];
                    edge[0] = edgeParts[0];
                    edge[1] = edgeParts[2];

                    if(!nodeSet.containsKey(edge[0]) || !nodeSet.containsKey(edge[1])){
                        def resultNode = new ResultNode(nodeID: node.nodeId, name: node.nodeId);
                        if(nodeSetCounts != null && nodeSetCounts.containsKey(resultNode.nodeID)){
                            resultNode.overlapCount = nodeSetCounts.get(resultNode.nodeID);
                            resultNode.color = GraphUtil.getHexColorTemp(maxAmount, 1, resultNode.overlapCount);
                        }
                        if(!nodeMap.containsKey(resultNode.nodeID)){
                            nodeMap.put(resultNode.nodeID, resultNode);
                        }

                        continue;
                    }

                    def node1 = new ResultNode(nodeID: edge[0], name: edge[0]);
                    if(nodeSetCounts != null && nodeSetCounts.containsKey(node1.nodeID)){
                        node1.overlapCount = nodeSetCounts.get(node1.nodeID);
                        node1.color = GraphUtil.getHexColorTemp(maxAmount, 1, node1.overlapCount);
                    }

                    def node2 = new ResultNode(nodeID: edge[1], name: edge[1]);
                    if(nodeSetCounts != null && nodeSetCounts.containsKey(node2.nodeID)){
                        node2.overlapCount = nodeSetCounts.get(node2.nodeID);
                        node2.color = GraphUtil.getHexColorTemp(maxAmount, 1, node2.overlapCount);
                    }

                    def resEdge = new ResultEdge();
                    resEdge.source = node1.nodeID;
                    resEdge.target = node2.nodeID;
                    resEdge.value = 1;
                    resEdge.relationshipType = edgeParts[1];

                    if(!edgeMap.containsKey(incidentEdge)){
                        edgeMap.put(incidentEdge, resEdge);
                    }

                    // Add the nodes if it's the first time we see them.
                    if(!nodeMap.containsKey(node1.nodeID)){
                        nodeMap.put(node1.nodeID, node1);
                    }

                    if(!nodeMap.containsKey(node2.nodeID)){
                        nodeMap.put(node2.nodeID, node2);
                    }
                }
            }

            for(ResultEdge edge : edgeMap.values()){
                resGraph.addToEdges(edge)
            }
            for(ResultNode node : nodeMap.values()){
                resGraph.addToNodes(node)
            }

            resGraph.maxNodeCount = maxAmount;
            resGraph.isUnionSet = isUnionSet;
            resGraph.nodeSetNr = nodeSetNr;
            resGraph.k = res.k;
            resGraph.l = res.l;
            resGraph.setOwner(res);

            if (!resGraph.save(flush: true, validate: true, failOnError: true)) {
                resGraph.errors.each {
                    println it
                }
            }
        }catch(Exception e){
            println getStackTrace(e);
            KpmLogger.log(Level.SEVERE, e);
        }
        return resGraph;
    }

    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
