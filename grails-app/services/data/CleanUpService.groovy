package data

import grails.transaction.Transactional
import kpm.web.data.DatasetFile
import kpm.web.data.ImageFile
import kpm.web.graph.Graph
import kpm.web.kpm.results.ResultEdge
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultNode
import kpm.web.kpm.results.ResultSet
import kpm.web.utils.progress.Quest

@Transactional
class CleanUpService {
    def runParametersService
    def queueService
    def graphsService
    Date weekAgo;

    void execute() {
        try {

             Date today = new Date().clearTime();
             weekAgo = (today - 6).clearTime() - 1;

            deleteOldImages();

            deleteOldGraphs();

            deleteOldDatasets()

            deleteOldResults();

            deleteOldQuests();

        } catch (Exception e) {
            System.out.println(e.stackTrace)
            e.printStackTrace()
        }
    }

    /**
     * deletes all old quests that don't belong to a user and are older than the given date
     */
    private void deleteOldQuests() {
        def allQuests = Quest.list()
        def oldQuests = new ArrayList<Quest>()
        for (Quest quest : allQuests) {
            if (tryParseInt(quest.attachedToID)) {
                continue
            }
            if (quest.createdDate < weekAgo) {
                oldQuests.add(quest)
            }
        }
        for (Quest oldQuest : oldQuests) {
            Long id = oldQuest.id
            queueService.deQueueQuest(id)
            runParametersService.delete(oldQuest.runParamsID)
            oldQuest.delete(flush: true, validate: true, failOnError: true)
        }
    }

    /**
     * deletes all old graphs that don't belong to a user and are older than the given date
     */
    private void deleteOldGraphs() {
        def allGraphs = Graph.list()
        def oldGraphs = new ArrayList<Graph>()
        for (Graph graph : allGraphs) {
            if (tryParseInt(graph.attachedToID)) {
                continue
            }
            if (graph.createdDate < weekAgo) {
                println(graph.attachedToID)
                oldGraphs.add(graph)
            }
        }
        for (Graph oldGraph : oldGraphs) {
            Long id = oldGraph.id
            graphsService.delete(id)
        }
    }

    /**
     * deletes all old resultsets, resultgraphs, edges and nodes that don't belong to a user and are older than the given date
     */
    private void deleteOldResults() {
        def allQuests = Quest.list();
        def oldQuests = new ArrayList<Quest>()
        for (Quest quest : allQuests) {
            if (tryParseInt(quest.attachedToID)){
                continue
            }
            else if(quest.createdDate<weekAgo){
                oldQuests.add(quest);
            }
        }
        if(!oldQuests.isEmpty()) {
            def setdelete = ResultSet.findAll("from ResultSet as rs where rs.runID in (:quests)", [quests: oldQuests.runID]);
            if (!setdelete.isEmpty()) {
                def graphdelete = ResultGraph.findAll("from ResultGraph as rg where rg.owner in (:sets)", [sets: setdelete]);
                if(!graphdelete.isEmpty()) {
                    def edges=new ArrayList<ResultEdge>();
                    for(ResultGraph g : graphdelete){
                        edges.addAll(g.edges);
                    }
                     def nodes=new ArrayList<ResultNode>();
                    for(ResultGraph g : graphdelete){
                        nodes.addAll(g.nodes);
                    }
                     ResultGraph.deleteAll(graphdelete);
                    if(!edges.isEmpty()){
                        ResultEdge.deleteAll(edges);
                    }
                    if(!nodes.isEmpty()){
                          ResultNode.deleteAll(nodes);
                    }
                }
                ResultSet.deleteAll(setdelete);
            }
        }
    }

    /**
     * deletes all old Datasets that don't belong to a user and are older than the given date
     */
    private void deleteOldDatasets(){
        def allDatasets = DatasetFile.list();
        def oldDatasets = new ArrayList<DatasetFile>();
        for(DatasetFile dataset : allDatasets){
            // we only want to delete temporary user data
            if(tryParseInt(dataset.attachedToID)){
                continue;
            }
            if(dataset.createdDate < weekAgo){
                oldDatasets.add(dataset);
            }
        }
        DatasetFile.deleteAll(oldDatasets);
    }

    /**
     * deletes all old images that don't belong to a user and are older then the given date
     */
    private void deleteOldImages(){
        def allImages = ImageFile.list()
        def oldImages = new ArrayList<ImageFile>()
        for (ImageFile image : allImages) {
            // we only want to delete temporary user data
            if (tryParseInt(image.attachedToID)) {
                continue
            }
            if (image.createdDate < weekAgo) {
                oldImages.add(image)
            }
        }
        ImageFile.deleteAll(oldImages);
       /* for (ImageFile image : oldImages) {
            image.delete(flush: true, validate: true, failOnError: true)
        }
        */
    }

    /**
     *controls if the attachedid is a integer (belongs to a user)
     */
    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value)
            return true
        } catch (NumberFormatException nfe) {
            return false
        }
    }

}
