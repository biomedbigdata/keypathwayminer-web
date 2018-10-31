package kpm.web

import kpm.web.authentication.KpmRole
import kpm.web.base.BaseController
import kpm.web.exceptions.InvalidSuffixException
import kpm.web.utils.FileUtil
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.web.multipart.MultipartFile

class NetworksController extends BaseController{
    def graphsService

    def index() {
        def graphs = graphsService.get(getUserID(), true).unique();
        render(view:"index", model: [graphsList: graphs]);
    }

    def newGraph(){
    }

    def uploadGraph(){
        def graphFile = params.graphFile as MultipartFile;
        def name = params.graphName as String;
        def description = params.description as String;

        if(!graphFile || graphFile.size == 0){
            flash.message = "No file found. No network uploaded.";
        }else{
            if(graphsService.exists(getUserID(), name)) {
                flash.message = "A network with name '" + name + "' already exists.";
            }else{
                // we only support SIF files at the moment, so check if at least the extension fits.
                try{
                    FileUtil.checkForExtension(graphFile.originalFilename, "sif")
                }catch(InvalidSuffixException ise){
                    flash.message = ise.getMessage();
                    redirect(action: "index")
                }

                def graph = graphsService.newInstance(this.getUserID());

                graph.content = FileUtil.fileToString(graphFile);
                graph.filename = graphFile.originalFilename;
                graph.name = name;
                graph.species = params.species
                graph.description = description
                if(SpringSecurityUtils.ifAllGranted("ROLE_ADMIN"))
                    graph.isDefault = params.defaultNetwork

                if(graph.name == null){
                    graph.name = graph.filename;
                }

                graphsService.update(graph);
                flash.okay = "Network '"+graphFile.originalFilename+"' uploaded.";
            }
        }

        redirect(action:"index");
    }

    def viewGraphSigma(){

    }

    def deleteGraph(Long id){
        graphsService.delete(id);
        redirect(action: "index");
    }

}
