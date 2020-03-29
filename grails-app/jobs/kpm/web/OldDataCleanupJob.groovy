package kpm.web

import kpm.web.data.DatasetFile
import kpm.web.data.ImageFile
import kpm.web.graph.Graph
import kpm.web.utils.progress.Quest


class OldDataCleanupJob {
    def deletionService

    static triggers = {
        cron name: 'DailyTrigger', cronExpression: "0 0 2 * * ?"
        //cron name: 'FiveMinuteTrigger', cronExpression: "0 0/5 * * * ?"
    }

    def execute() {
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

        println "KPM data cleanup job - deleting data older than one week..."
        deletionService.deleteQuests(oldQuests)
        deletionService.deleteGraphs(oldGraphs)
        deletionService.deleteDatasets(oldDatasets)
        deletionService.deleteImages(oldImages)
        println "Data deletion completed successfully."
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
