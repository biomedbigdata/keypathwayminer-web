package progress

import dk.sdu.kpm.logging.KpmLogger
import kpm.web.utils.progress.Quest
import kpm.web.utils.progress.QueueItem

import java.util.concurrent.Executors
import java.util.logging.Level

class QueueService {
    def kpmService
    def questService
    def grailsApplication

    static transactional = true

    /**
     * Enqueues the given progress/runparameters, making it wait for it to be their turn.
     * @param runParamID
     * @param progressID
     */
    def synchronized enqueue(Long runParamID, Long progressID) {
        try{
            def queueItem = new QueueItem();
            queueItem.runParamID = runParamID;
            queueItem.progressID = progressID;
            queueItem.enqueueDate = new Date();
            queueItem.save(flush: true, validate: true, failOnError: true);

            // Mark as queued.
            questService.updateStatusMessage(progressID, "Queued");

        }catch(Exception e){
            KpmLogger.log(Level.SEVERE, e);
        }
    }

    /**
     * Ensures that we keep the kpm run queue up to date, and starts the runs that needs starting.
     */
    def synchronized updateQueue(){
        try{
            def runningQuests = QueueItem.where { isRunning == true }.toList();

            for(def i = 0; i < runningQuests.size(); i++){
                def queueItem = runningQuests.get(i);
                def quest = Quest.get(queueItem.progressID);
                if(!quest){
                    println("Quest with ID $queueItem.progressID not found.");
                    queueItem.isRunning = false;
                    queueItem.save(flush: true, validate: true, failOnError: true);
                    continue;
                }
                quest.refresh();

                // remove jobs that have been running for an hour already
                Date oneHourAgo = new Date(System.currentTimeMillis() - 3600 * 1000)

                if(quest.createdDate < oneHourAgo){
                    quest.setIsCancelled(true)
                }

                if(quest.isCancelled || quest.isCompleted){
                    println("Quest with ID $queueItem.progressID finished/cancelled.");
                    queueItem.isRunning = false;
                    queueItem.save(flush: true, validate: true, failOnError: true);
                }

            }

            // Delete old quests that have been run
            QueueItem.where {isRunning == false && hasExecuted == true}.deleteAll();

            // Update quests, to weed out cancelled quests:
            def cancelledQueueItemIDs = new ArrayList<Long>();
            def enqueuedItems = QueueItem.where {isRunning == false && hasExecuted == false}.toList();
            for(def i = 0; i < enqueuedItems.size(); i++) {
                def queueItem = enqueuedItems.get(i);
                def quest = Quest.get(queueItem.progressID);
                if(!quest) {
                    println("Quest with ID $queueItem.progressID cancelled.");
                    cancelledQueueItemIDs.add(queueItem.id);
                }

                quest.refresh();
                if(quest.isCancelled || quest.isCompleted){
                    println("Quest with ID $queueItem.progressID cancelled or completed.");
                    cancelledQueueItemIDs.add(queueItem.id);
                }
            }

            // Delete the cancelled items that were queued
            def cancelledQueueItemIDsArray = cancelledQueueItemIDs.toArray();
            if(cancelledQueueItemIDsArray.length > 0){
                cancelledQueueItemIDs.each {
                    def queueItem = QueueItem.get(it);
                    if(!queueItem || queueItem == null){
                        return;
                    }

                    queueItem.delete(flush: true, validate: true, failOnError: true);
                };
            }

            // If there are less concurrent runs executing than the limit, we start new ones
            int maxConcurrentRuns = grailsApplication?.config?.kpm?.max?.concurrent?.runs?:1;
            def amountOfNewRunsAllowed = maxConcurrentRuns - QueueItem.where { isRunning == true }.toList().size();
            if(amountOfNewRunsAllowed > 0){

                def executor = Executors.newCachedThreadPool();
                def list = QueueItem.where{isRunning == false && hasExecuted == false}.list(sort:"enqueueDate").toList();

                // Start new runs:
                for(def i = 0; i < amountOfNewRunsAllowed && i < list.size(); i++){
                    def queueItem = list.get(i);
                    Long runParamID = queueItem.runParamID;
                    Long progressID = queueItem.progressID;
                    executor.submit({
                        try{
                            kpmService.start(runParamID, progressID);
                        }catch(Exception e){
                            KpmLogger.log(Level.SEVERE, e);
                        }
                    });
                    queueItem.isRunning = true;
                    queueItem.hasExecuted = true;
                    queueItem.save(flush: true, validate: true, failOnError: true);
                }

                // Update place in queue for the rest of the list.
                def queueNr = 1;
                for(def j = amountOfNewRunsAllowed; j < list.size(); j++){
                    def queueItem = list.get(j);
                    questService.updateStatusMessage(queueItem.progressID, "Queued (place in queue: $queueNr)");
                    queueNr++;
                }
            }


        }catch(Exception e){
            KpmLogger.log(Level.SEVERE, e);
        }
    }

    def synchronized void deQueueQuest(Long questId){
        def queue = QueueItem.where { progressID == questId }.toList();

        for (QueueItem item : queue){
            item.delete(flush: true, validate: true, failOnError: true);
        }
    }
}
