package kpm.web.graph

import kpm.web.exceptions.InvalidRequestException
import kpm.web.utils.requests.RequestUtil

public class Graph{
    def graphsService

    public Graph(){
        attachedToID = "";
        isDefault = false;
        content = "";
        name = "";
        filename = "";
        description = "";
        species = ""
        createdDate = new Date();
    }

    String filename
    String description
    String attachedToID
    boolean isDefault
    String name
    static hasMany = [relationshipTypes: String]
    Date createdDate

    String content

    String species

    static constraints = {
        species inList: ["Homo sapiens", "Mus musculus", "Drosophila melanogaster", "Caenorhabditis elegans",
                         "Escherichia coli", "Saccharomyces cerevisiae",
                         "Arabidopsis thaliana", "Danio rerio", "other"]
    }

    static mapping = {
        content type :  'text'
        content description :  'text'
    }
//    @Cascade(CascadeType.ALL)
//    static hasMany = [edges: Edge, nodes: Node]

    public Graph updateValuesByJSON(HashMap<String, Object> result) throws InvalidRequestException {
        if (RequestUtil.ContainsKeyOrThrow(result, "name")) {
            this.name = result["name"] as String;
        }

        if (result.containsKey("filename")) {
            this.filename = result["filename"] as String;
        }else{
            this.filename = this.name;
        }

        if (RequestUtil.ContainsKeyOrThrow(result, "attachedToID")) {
            this.attachedToID = result["attachedToID"] as String;
        }

        if(graphsService.exists(this.attachedToID, this.name)){
            return graphsService.get(this.attachedToID, this.name);
        }

        if (RequestUtil.ContainsKeyOrThrow(result, "contentBase64")) {
            def contentBase64 = result["contentBase64"] as String;
            this.content = new String(contentBase64.decodeBase64());
        }

        if (!this.save(flush: true, validate: true, failOnError: true)) {
            this.errors.each {
                println it
            }
        }
    }

    String toString(){
        name + "(" + species + ")"
    }
}
