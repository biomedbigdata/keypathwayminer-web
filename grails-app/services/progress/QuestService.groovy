package progress

import dk.sdu.kpm.logging.KpmLogger
import kpm.web.exceptions.UnknownQuestException
import kpm.web.utils.progress.Quest
import org.hibernate.Hibernate

import java.util.concurrent.Callable
import java.util.logging.Level

/**
 * This service enables KPM to asynchronously update the Quest/task, providing the ability for users to see the progress of their KPM run.
 */
public class QuestService {
    def executorService
    static transactional = false

    /**
     * Ensures with both transactions and lock, that the work done on the Quest with the given ID is
     * locked and can be safely manipulated, without worrying about thread-safety (as it is thread-safe).
     * If the lock is not acquired at first, it will keep trying.
     * @param id
     * @param closure
     */
    private void withTransactionAsync(Long id, Closure<Quest> closure){
        executorService.submit({
            def saved = false;
            Quest.withTransaction {
                while(!saved){
                    try{
                        def quest = Quest.lock(id);

                        if(!quest){
                            throw new UnknownQuestException("No quest with the following ID was found: " + id.toString());
                        }

                        closure(quest);
                        saved = true;
                    }catch(CannotAcquireLockException){
                        println("Waiting - Could not acquire lock for id: " + id);
                    }
                }
            }
        } as Callable);
    }
    private void withTransactionAsyncByRunID(String runID, Closure<Quest> closure){
        executorService.submit({
            def saved = false;
            Quest.withTransaction {
                while(!saved){
                    try{
                        def quest = Quest.findByRunID(runID);

                        if(!quest){
                            throw new Exception("Quest was not found.");
                        }

                        quest = Quest.lock(quest.getId());
                        closure(quest)
                        saved = true;
                    }catch(CannotAcquireLockException){
                        println("Waiting - Could not acquire lock for id: " + id);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } as Callable);
    }

    /**
     * Creates a new quest/task with the attributed attachedToID set to the input parameter.
     * @param attachedToID
     * @return
     */
    public Quest newQuest(String attachedToID){
        return Quest.withTransaction {
            def quest = new Quest();
            quest.attachedToID = attachedToID;
            quest.save();
            quest.createdDate = new Date();
            return quest;
        }
    }

    public Quest newQuest(String attachedToID, String runID){
        return Quest.withTransaction {
            def quest = new Quest();
            quest.attachedToID = attachedToID;
            quest.runID = runID;
            quest.createdDate = new Date();
            quest.save();
            return quest;
        }
    }

    /**
     * Creates a new quest/task with the attributed attachedToID set to the input parameter.
     * @param attachedToID
     * @return
     */
    public Quest newQuest(String attachedToID, Long runParamsID){
        return Quest.withTransaction {
            def quest = new Quest();
            quest.attachedToID = attachedToID;
            quest.runParamsID = runParamsID;
            quest.createdDate = new Date();
            quest.save();
            return quest;
        }
    }

    /**
     * Updates the progress of the Quest with the input id. (asynchronously / with lock)
     * @param id
     * @param progress
     */
    public void updateProgress(Long id, double progress){
////        withTransactionAsync(id, { quest ->
//            if(quest.isCompleted){
//                return;
//            }
        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                def quest = Quest.lock(id);
                quest.progress = progress;
                quest.save();
            }
        }

//            if (!quest.save(flush: true, validate: true, failOnError: true)) {
//                quest.errors.each {
//                    println it
//                }
//            }
//        } as Closure<Quest>);
    }

    /**
     * Updates the title of the Quest with the input id. (asynchronously / with lock)
     * @param id
     * @param title
     */
    public void updateTitle(Long id, String title){

        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                def quest = Quest.lock(id);
                quest.title = title;
                quest.save();
            }
        }
    }

    /**
     * Updates the status message of the Quest with the input ID. (asynchronously / with lock)
     * @param id
     * @param statusMessage
     */
    public void updateStatusMessage(Long id, String statusMessage){
//        withTransactionAsync(id, { quest ->

        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                def quest = Quest.lock(id);
                if(quest.isCompleted){
                    return;
                }

                quest.statusMessage = statusMessage;
                quest.save();
            }
        }

//            if (!quest.save(flush: true, validate: true, failOnError: true)) {
//                quest.errors.each {
//                    println it
//                }
//            }
//        } as Closure<Quest>);
    }

    public void updateCreatedDate(Long id, Date newDate){
        withTransactionAsync(id, { quest ->
            quest.createdDate = newDate;
            if (!quest.save(flush: true, validate: true, failOnError: true)) {
                quest.errors.each {
                    println it
                }
            }
        } as Closure<Quest>);
    }

    /**
     * Updates the (KPM) runID of the Quest with the input ID. (asynchronously / with lock)
     * @param id
     * @param statusMessage
     */
    public void updateRunID(Long id, String runID){
        withTransactionAsync(id, { quest ->
            quest.runID = runID;
            if (!quest.save(flush: true, validate: true, failOnError: true)) {
                quest.errors.each {
                    println it
                }
            }
        } as Closure<Quest>);
    }

    public void updateName(Long id, String name) {
        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                def quest = Quest.lock(id);
                if(quest.isCompleted){
                    return;
                }
                quest.name = name;
                quest.save();
            }
        }
    }

    /**
     * Sets the Comp
     * @param id
     */
    public void completeQuestWithRunID(String runID){
        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                try{
                    def quest = Quest.findByRunID(runID);

                    if(!quest){
                        throw new Exception("Quest was not found.");
                    }

                    quest.isCompleted = true;
                    quest.progress = 1
                    quest.statusMessage = "Finished";
                    quest.save();

                }catch(Exception e){
                    KpmLogger.log(Level.SEVERE, e);
                }
            }
        }
    }

    /**
     * Sets the Comp
     * @param id
     */
    public void cancelQuestWithRunID(String runID){
        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                def quest = Quest.findByRunID(runID);

                if(!quest){
                    return;
                }

                if(quest.isCompleted){
                    return;
                }
                quest.isCancelled = true;
                quest.save();
            }
        }
    }

    public List<Quest> getAllAttachedToId(String attachedToId){
        return Quest.withTransaction {
            def results = new ArrayList<Quest>();

            try{

                def quests = Quest.findAll {
                    attachedToID == attachedToId
                }

                if(quests && quests != null && quests.size() > 0){
                    for(Quest quest : quests){
                        quest.refresh();
                        results.add(quest);
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return results;
        }
    }

    public List<Quest> getAllNotFinished(){
        return Quest.withTransaction {
            def results = new ArrayList<Quest>();

            try{

                def quests = Quest.findAll {
                    isCompleted != true && isCancelled != true
                }

                if(quests && quests != null && quests.size() > 0){
                    for(Quest quest : quests){
                        quest.refresh();
                        results.add(quest);
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            return results;
        }
    }

    public void updateTitleByRunID(String runID, String newTitle) {
        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                def quest = Quest.findByRunID(runID);

                if(!quest){
                    throw new Exception("Quest was not found.");
                }

                if(quest.isCompleted){
                    return;
                }
                quest.title = newTitle;
                quest.save();
            }
        }
    }

    public void updateMessageByRunID(String runID, String statusMessage) {
        Quest.withNewSession { hsession ->
            Quest.withNewTransaction { transaction ->
                def quest = Quest.findByRunID(runID);

                if (!quest) {
                    throw new Exception("Quest was not found.");
                }

                if (quest.isCompleted) {
                    return;
                }
                quest.statusMessage = statusMessage;
                quest.save();
            }
        }
    }

    public String getRunID(Long progressID){
        def quest = Quest.get(progressID);
        if(!quest){
            return "";
        }

        return quest.runID;
    }

    public void killAll() {

        def runIDs = new ArrayList<String>();

        Quest.withNewSession { hsession ->
            Quest.withTransaction { transaction ->
                try {

                    def quests = Quest.findAll {
                        isCompleted != true && isCancelled != true
                    }

                    if (quests && quests != null && quests.size() > 0) {
                        for (Quest quest : quests) {
                            runIDs.add(quest.runID);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(runIDs.size() == 0){
            return;
        }

        for(String runID : runIDs){
            this.cancelQuestWithRunID(runID);
        }
    }
}
