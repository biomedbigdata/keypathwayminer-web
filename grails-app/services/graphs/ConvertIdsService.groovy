package graphs

import com.github.kevinsawicki.http.HttpRequest
import grails.transaction.Transactional

@Transactional
class ConvertIdsService {

    def serviceMethod() {

    }
    static Map<String, String> getConvertedIDs(String nodeIdType, String replacementIdType, List<String> nodeIDs) {
        HashMap<String, String> result = new HashMap<String, String>();
        if (nodeIDs == null || nodeIDs.size() == 0) {
            return result;
        }

        if (nodeIdType == "" || replacementIdType == "") {
            for (String id : nodeIDs) {
                result.put(id, id);
            }

            return result;
        }
        String idQuery = "";
        for (String nodeID : nodeIDs) {
            if(result.containsKey(nodeID)){
                continue;
            }
            if (idQuery != ""){
                idQuery = idQuery +","+ nodeID;
            }
            else{
                idQuery = idQuery + nodeID;
            }
        }
        println idQuery;
        String urlLink = "http://mygene.info/v3/gene";
        String response = HttpRequest.post(urlLink, false,"ids",idQuery,"fields","symbol,name,taxid,entrezgene,ensembl.gene,uniprot,refseq.rna")
                .accept("application/json")
                .body();
        println(response);



        /*
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
         */
        return result;
    }
}
