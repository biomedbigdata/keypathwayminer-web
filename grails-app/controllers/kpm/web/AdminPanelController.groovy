package kpm.web

class AdminPanelController {

    def index() { redirect action: "listQueuedRuns"}

    def listQueuedRuns(){
        def killAllQuestsPassword = grailsApplication?.config?.kpm?.quests?.killall?.password?:"";

        render(view: "listQueuedRuns", model: [killAllQuestsPassword: killAllQuestsPassword]);
    }
}
