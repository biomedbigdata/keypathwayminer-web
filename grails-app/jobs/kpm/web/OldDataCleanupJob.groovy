package kpm.web

import kpm.web.data.DatasetFile
import kpm.web.data.ImageFile
import kpm.web.graph.Graph
import kpm.web.utils.progress.Quest


class OldDataCleanupJob {
    def runParametersService
    def queueService
    def graphsService

    static triggers = {
        cron name: 'DailyTrigger', cronExpression: "0 0 2 * * ?"
    }

    def execute() {
        try{
            def allQuests = Quest.findAll();


            Date today = new Date().clearTime();
            Date weekAgo = (today - 6).clearTime() - 1;

            def oldQuests = new ArrayList<Quest>();
            for(Quest quest : allQuests){
                // we only want to delete temporary user data
                if(tryParseInt(quest.attachedToID)){
                    continue;
                }

                if(quest.createdDate < weekAgo){
                    oldQuests.add(quest);
                }
            }

            for(Quest oldQuest : oldQuests){
                Long id = oldQuest.id;
                queueService.deQueueQuest(id);
                runParametersService.delete(oldQuest.runParamsID);
                oldQuest.delete(flush: true, validate: true, failOnError: true);
            }

            def allGraphs = Graph.findAll();
            def oldGraphs = new ArrayList<Graph>();
            for(Graph graph : allGraphs){

                // we only want to delete temporary user data
                if(tryParseInt(graph.attachedToID)){
                    continue;
                }

                if(graph.createdDate < weekAgo){
                    oldGraphs.add(graph);
                }
            }

            for(Graph oldGraph: oldGraphs){
                Long id = oldGraph.id;
                graphsService.delete(id);
            }

            def allDatasets = DatasetFile.findAll();
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

            for(DatasetFile dataset: oldDatasets){
                dataset.delete(flush: true, validate: true, failOnError: true);
            }

            def allImages = ImageFile.findAll();
            def oldImages = new ArrayList<ImageFile>();
            for(ImageFile image : allImages){

                // we only want to delete temporary user data
                if(tryParseInt(image.attachedToID)){
                    continue;
                }

                if(image.createdDate < weekAgo){
                    oldImages.add(image);
                }
            }

            for(ImageFile image: oldImages){
                image.delete(flush: true, validate: true, failOnError: true);
            }
        }catch(Exception e){
            System.out.println(e.stackTrace);
            e.printStackTrace();
        }
    }

    private boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException nfe)
        {
            return false;
        }
    }
}
