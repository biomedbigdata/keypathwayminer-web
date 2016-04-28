package kpm.web.data

import kpm.web.exceptions.InvalidRequestException
import kpm.web.utils.requests.RequestUtil

/**
 * Created by: Martin
 * Date: 30-08-14
 */
public class DatasetFile{
    def datasetFileService

    public DatasetFile(){
        isDefault = false;
        name = "";
        filename = "";
        description = "";
        content = "";
        species = "";
        attachedToID = "";
        type = "";
        createdDate = new Date();
    }

    public DatasetFile updateValuesByJSON(HashMap<String, Object> result) throws InvalidRequestException{
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

        if(datasetFileService.exists(this.attachedToID, this.name)){
            return datasetFileService.getByName(this.attachedToID, this.name);
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

        return this;
    }

    String name
    String filename
    String description
    String content
    boolean isDefault
    String attachedToID
    Date createdDate
    String species
    String type

    static constraints = {
        species inList: ["Homo sapiens", "Mus musculus", "Drosophila melanogaster", "Caenorhabditis elegans",
                         "Escherichia coli", "Saccharomyces cerevisiae",
                         "Arabidopsis thaliana", "Danio rerio", "other"]
        type inList: ["Indicator Matrix", "Numerical Matrix"]
    }

    static mapping = {
        content type :  'text'
        content description :  'text'
        species length: 50
    }

    String toString(){
        name + "(" + species + ")"
    }
}
