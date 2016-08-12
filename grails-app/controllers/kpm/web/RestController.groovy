package kpm.web

import grails.converters.JSON
import groovy.json.JsonOutput
import kpm.web.base.BaseController
import kpm.web.graph.Graph
import kpm.web.kpm.parameters.RunParameters
import kpm.web.utils.progress.Quest

class RestController extends BaseController {

    def graphsService
    def questService
    def springSecurityService

    def network(String name){

        if(!name) render JsonOutput.toJson([message: 'A name needs to be provided'])
        else{
            def graphs = graphsService.get(getUserID(), true)
            def graph

            for(def i = 0; i < graphs.size(); i++){
                def curGraph = graphs[i];
                if(curGraph.name == name){
                    graph = curGraph;
                    break;
                }
            }
            if(!graph) render JsonOutput.toJson([message: 'No network found'])
            else render graph as JSON;
        };
    }

    def availableNetworks(){
        def graphsList = Graph.findAllByIsDefault(true);
        if(graphsList.size() == 0)
            render JsonOutput.toJson([message: "No networks found"])
        else render graphsList as JSON;
    }

    def runParameters(){
        def parameters = RunParameters.get(params.id)
        println parameters
        if(parameters) render parameters as JSON;
        else if(!params.id) render JsonOutput.toJson([message: "Parameter id missing"])
        else render JsonOutput.toJson([message: "No parameters found for id ${params.id}"])
    }

    def jsonQuests(String attachedToID){
        if(!attachedToID || attachedToID == null){
            def roles = springSecurityService.getPrincipal().getAuthorities()
            for(def role in roles) {
                if (role.getAuthority() == "ROLE_ADMIN") {
                    def quests = questService.getAllNotFinished();
                    render quests as JSON
                }
            }
            return new ArrayList<Quest>() as JSON;
        }

        def quests = questService.getAllAttachedToId(attachedToID);

        if(!quests || quests == null){
            return new ArrayList<Quest>() as JSON;
        }

        render quests as JSON;
    }
    def questsInQueue(){

        List<Quest> quests = questService.getAllNotFinished();

        render JsonOutput.toJson([quests.size()]);
    }

/*
    def jsonKillAllQuests(String password){
        def masterPassword = grailsApplication?.config?.kpm?.quests?.killall?.password?:"undefined_not_found";

        if(masterPassword.equals(password)){
            questService.killAll();
            render true;
            return;
        }

        render false;
    }
*/
}
