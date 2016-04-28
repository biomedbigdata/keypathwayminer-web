package kpm.web

import kpm.web.base.BaseController

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
}
