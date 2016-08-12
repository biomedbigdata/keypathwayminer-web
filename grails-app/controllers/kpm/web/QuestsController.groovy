package kpm.web

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import kpm.web.base.BaseController
import kpm.web.utils.progress.Quest

class QuestsController extends BaseController{
    def questService

    def questsAttachedToID(){

        def attachedToID = params.get("attachedToID") as String;
        if(attachedToID == null || attachedToID.isEmpty()){
            attachedToID = getUserID();
        }

        def quests =  questService.getAllAttachedToId(attachedToID);

        render(view: "questsAttachedToID", model: [quests : quests, attachedToID : attachedToID]);
    }

    @Secured(['ROLE_ADMIN'])
    def queuedQuests(){

        def quests =  questService.getAllNotFinished();

        render(view: "questsAttachedToID", model: [quests : quests]);
    }


    def jsonQuests(String attachedToID){
        if(!attachedToID || attachedToID == null){
            return new ArrayList<Quest>() as JSON;
        }

        def quests = questService.getAllAttachedToId(attachedToID);




        if(!quests || quests == null){
            return new ArrayList<Quest>() as JSON;
        }

        render quests as JSON;
    }

    def jsonQuestsFinished(){

        List<Quest> quests = questService.getAllFinished();

        render quests.size();
    }

    def jsonQuestsCancelled(){

        List<Quest> quests = questService.getAllCancelled();

        render quests.size();
    }

    def jsonQuestsInQueue(){

        List<Quest> quests = questService.getAllNotFinished();

        render quests.size();
    }

    @Secured(['ROLE_ADMIN'])
    def jsonKillAllQuests(String password){
        def masterPassword = grailsApplication?.config?.kpm?.quests?.killall?.password?:"undefined_not_found";

        if(masterPassword.equals(password)){
            questService.killAll();
            render true;
            return;
        }

        render false;
    }
}
