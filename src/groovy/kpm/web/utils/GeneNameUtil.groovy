package kpm.web.utils

import com.github.kevinsawicki.http.HttpRequest
import groovy.json.JsonSlurper
/**
 * Created by Martin on 01-03-2015.
 */
class GeneNameUtil {

    public static Map<String, String> getConvertedIDs(String nodeIdType, String replacementIdType, List<String> nodeIDs){
    HashMap<String, String> result = new HashMap<String, String>();

    if(nodeIDs == null || nodeIDs.size() == 0){
        return result;
    }

    if(nodeIdType == "" || replacementIdType == ""){
        for(String id : nodeIDs){
            result.put(id, id);
        }

        return result;
    }


    try {
        JsonSlurper slurper = new JsonSlurper();
        for(String nodeID: nodeIDs){

            if(result.containsKey(nodeID)){
               continue;
            }

            String urlLink = "http://rest.genenames.org/fetch/"+nodeIdType+"/"+nodeID;

            String response = HttpRequest.get(urlLink, true)
                    .accept("application/json")
                    .body();

            def lvl1 = slurper.parseText(response) as HashMap<String, Object>;
            if(lvl1 != null && lvl1.containsKey("response")){
                def lvl2 = lvl1.get("response") as HashMap<String, Object>;
                if(lvl2 != null){
                    def lvl3 = lvl2.get("docs") as HashMap<String, Object>;
                    if(lvl3 != null){
                        if(replacementIdType == "uniprot_ids" || replacementIdType == "refseq_accession" ) {
                            def replaceIDs = lvl3.get(replacementIdType) as ArrayList<String>
                            if(replaceIDs != null && replaceIDs.size() > 0){
                                result.put(nodeID, replaceIDs.get(0));
                            }
                        }else{
                            def replaceID = lvl3.get(replacementIdType);
                            if(replaceID != null){
                                result.put(nodeID, replaceID);
                            }
                        }
                    }
                }
            }
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
}