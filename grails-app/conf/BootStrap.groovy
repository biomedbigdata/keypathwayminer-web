import grails.converters.JSON
import kpm.web.authentication.KpmRole
import kpm.web.authentication.KpmUser
import kpm.web.authentication.KpmUserKpmRole
import kpm.web.data.DatasetFile
import kpm.web.data.ImageFile
import kpm.web.graph.Graph
import kpm.web.kpm.parameters.ExceptionParameters
import kpm.web.kpm.parameters.KpmParameters
import kpm.web.kpm.parameters.LParameters
import kpm.web.kpm.parameters.PerturbationParameters
import kpm.web.kpm.parameters.RunParameters
import kpm.web.kpm.results.CsvFormattedObject
import kpm.web.kpm.results.ResultEdge
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultNode
import kpm.web.utils.FileWithText
import kpm.web.utils.progress.Quest
import kpm.web.utils.requests.RequestAnswer
import kpm.web.utils.requests.RequestRunStatus
import org.apache.commons.codec.binary.Base64

class BootStrap {
    def datasetFileService
    def graphsService

    def init = { servletContext ->

        def userRole = KpmRole.findByAuthority('ROLE_USER') ?: new KpmRole(authority: 'ROLE_USER').save(failOnError: true)
        def adminRole = KpmRole.findByAuthority('ROLE_ADMIN') ?: new KpmRole(authority: 'ROLE_ADMIN').save(failOnError: true)

        def adminUser = KpmUser.findByUsername('admin') ?: new KpmUser(
                username: 'admin',
                password: 'admin', //springSecurityService.encodePassword('admin'),
                email: 'martindissing@gmail.com',
                enabled: true).save(failOnError: true)

        if (!adminUser.authorities.contains(adminRole)) {
            KpmUserKpmRole.create adminUser, adminRole
        }

        if (!adminUser.authorities.contains(userRole)) {
            KpmUserKpmRole.create adminUser, userRole
        }

        JSON.registerObjectMarshaller(RequestAnswer){
            def output = [:]
            output["comment"] = it.comment
            output["success"] = it.success
            output["questID"] = it.questID
            output["resultGraphs"] = it.resultGraphs
            output["runID"] = it.runID
            output["resultUrl"] = it.resultUrl
            return output
        }

        JSON.registerObjectMarshaller(RequestRunStatus){
            def output = [:]
            output["completed"] = it.completed
            output["runExists"] = it.runExists
            output["cancelled"] = it.cancelled
            output["progress"] = it.progress
            return output
        }

        JSON.registerObjectMarshaller(ResultNode){
            def output = [:]
            output["gID"] = it.nodeID
            output["id"] = it.nodeID
            output["name"] = it.name
            output["overlapCount"] = it.overlapCount
            output["color"] = it.color
            return output
        }

        JSON.registerObjectMarshaller(CsvFormattedObject){
            def output = [:]
            output["data"] = it.data
            output["lineSeparator"] = it.lineSeparator
            return output
        }

        JSON.registerObjectMarshaller(FileWithText){
            def output = [:]
            output["content"] = it.content
            output["fileName"] = it.fileName
            return output
        }

        JSON.registerObjectMarshaller(ResultEdge){
            def output = [:]
            output["id"] = it.id
            output["source"] = it.source
            output["target"] = it.target
            output["value"] = it.value
            output["relationshipType"] = it.relationshipType
            return output
        }

        JSON.registerObjectMarshaller(ResultGraph){
            def output = [:]
            output["edges"] = it.edges
            output["nodes"] = it.nodes
            output["k"] = it.k
            output["l"] = it.l
            output["isUnionSet"] = it.isUnionSet
            output["nodeSetNr"] = it.nodeSetNr
            output["maxNodeCount"] = it.maxNodeCount
            return output
        }


        JSON.registerObjectMarshaller(Quest){
            def output = [:]
            output["id"] = it.id
            output["name"] = it.name
            output["title"] = it.title
            output["name"] = it.name
            output["attachedToID"] = it.attachedToID
            output["isCompleted"] = it.isCompleted
            output["isCancelled"] = it.isCancelled
            output["statusMessage"] = it.statusMessage
            output["runParamsID"] = it.runParamsID
            output["runID"] = it.runID
            output["progress"] = it.progress
            return output
        }

        JSON.registerObjectMarshaller(ImageFile){
            def output = [:]
            output["id"] = it.id
            output["height"] = it.height
            output["width"] = it.width
            output["size"] = it.size
            output["contentType"] = it.contentType
            output["imageName"] = it.imageName
            output["attachedToID"] = it.attachedToID

            if(it.data == null){
                it.data = new byte[0];
            }
            def data = new String(Base64.encodeBase64((byte[])it.data));
            output["data_base64"] = data;

            return output
        }

        JSON.registerObjectMarshaller(ExceptionParameters){
            def output = [:]

            output["val"] = it.val
            output["val_step"] = it.val_step
            output["val_max"] = it.val_max
            output["use_range"] = it.use_range
            output["isPercentage"] = it.isPercentage

            return output
        }

        JSON.registerObjectMarshaller(LParameters){
            def output = [:]

            output["val"] = it.val
            output["val_step"] = it.val_step
            output["val_max"] = it.val_max
            output["use_range"] = it.use_range
            output["isPercentage"] = it.isPercentage

            return output
        }

        JSON.registerObjectMarshaller(KpmParameters){
            def output = [:]

            output["name"] = it.name
            output["algorithm"] = it.algorithm
            output["strategy"] = it.strategy
            output["k_values"] = ExceptionParameters.findByOwner(it)
            output["removeBENs"] = it.removeBENs
            output["unmapped_nodes"] = it.unmapped_nodes
            output["computed_pathways"] = it.computed_pathways
            output["graphID"] = it.graphID
            output["l_values"] = LParameters.findByOwner(it)
            output["l_samePercentage"] = it.l_samePercentage
            output["samePercentage_val"] = it.samePercentage_val
            output["removeBENs"] = it.removeBENs

            return output
        }

        JSON.registerObjectMarshaller(PerturbationParameters){
            def output = [:]

            output["technique"] = it.technique
            output["startPercent"] = it.startPercent
            output["stepPercent"] = it.stepPercent
            output["maxPercent"] = it.maxPercent
            output["graphsPerStep"] = it.graphsPerStep

            return output
        }

        JSON.registerObjectMarshaller(Graph){
            def output = [:]
            output["graphID"] = it.id
            output["name"] = it.name
            return output
        }

        JSON.registerObjectMarshaller(RunParameters){
            def output = [:]

            output["withPerturbation"] = it.withPerturbation
            output["parameters"] = it.parameters
            output["perturbation"] = it.perturbation
            output["linkType"] = it.linkType
            output["attachedToID"] = it.attachedToID
            output["positiveNodes"] = it.positiveNodes
            output["negativeNodes"] = it.negativeNodes

            return output
        }


        System.out.println("\n| Adding default datasets if necessary:");
        datasetFileService.ensureDefaults();
        System.out.println("| Finished adding default dataset files.");

        System.out.println("\n| Adding default networks if necessary:");
        graphsService.ensureDefaults();
        System.out.println("| Finished adding network files.");

//        System.out.println("Adding test old stuff");
//        Date today = new Date().clearTime();
//        Date overAweekAgo = (today - 12).clearTime() - 1;
//        def datasetFile = new DatasetFile();
//        datasetFile.createdDate = overAweekAgo;
//        datasetFile.save(flush: true, validate: true, failOnError: true);
//        def imageFile = new ImageFile();
//        imageFile.createdDate = overAweekAgo;
//        imageFile.save(flush: true, validate: true, failOnError: true);
//        def quest = new Quest();
//        quest.attachedToID = "1141pinpin";
//        quest.createdDate = overAweekAgo;
//        quest.save(flush: true, validate: true, failOnError: true);
    }

    def destroy = {

    }
}
