package graphs

import com.github.kevinsawicki.http.HttpRequest
import grails.transaction.Transactional
import groovy.json.JsonSlurper

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

        try {
            String idQuery = "";
            for (String nodeID : nodeIDs) {
                if (result.containsKey(nodeID)) {
                    continue;
                }
                if (idQuery != "") {
                    idQuery = idQuery + "," + nodeID;
                } else {
                    idQuery = idQuery + nodeID;
                }
            }

            String scope = "";
            if (nodeIdType == "unknown") {
                scope = "entrezgene,ensembl.gene,ensembl.protein,uniprot,symbol,refseq.rna";
            } else if (nodeIdType == "entrez_id") {
                scope = "entrezgene";
            } else if (nodeIdType == "ensembl_gene_id") {
                scope = "ensembl.gene";
            } else if (nodeIdType == "uniprot_ids") {
                scope = "uniprot";
            } else if (nodeIdType == "symbol") {
                scope = "symbol";
            } else if (nodeIdType == "refseq_accession") {
                scope = "refseq.rna";
            } else {
                println("Kein Scope.");
            }

            //String urlLink = "http://mygene.info/v3/gene";
            String urlLink = "http://mygene.info/v3/query";
            String response = HttpRequest.post(urlLink, false, "q", idQuery, "scopes", scope, "fields", "symbol,name,taxid,entrezgene,ensembl.gene,ensembl.protein,uniprot,refseq.rna,ensembl.protein")
                    .accept("application/json")
                    .body();
            //println(response);
            JsonSlurper slurper = new JsonSlurper();
                def jsn = slurper.parseText(response) as ArrayList<String, Object>;
                if (jsn != null && jsn.size() > 0) {
                    def curid = 0;
                    def curquery = "";
                    for (int i = 0; i < jsn.size(); i++) {
                        if (jsn.get(i) != null) {
                            def jsn2 = jsn.get(i) as HashMap<String, Object>;
                            def replaceID;
                            if (jsn2 != null && jsn2.containsKey("query") && !jsn2.get("query").equals(curquery)) {
                                if (jsn2.get("query") == nodeIDs.get(curid)) {
                                    curquery = jsn2.get("query");
                                    if (replacementIdType.equals("entrez_id") && jsn2.containsKey("entrezgene") && jsn2.get("entrezgene") != null) {
                                        replaceID = jsn2.get("entrezgene");
                                    } else if (replacementIdType.equals("symbol") && jsn2.containsKey("symbol") && jsn2.get("symbol") != null) {
                                        replaceID = jsn2.get("symbol");
                                    } else if (replacementIdType.equals("ensembl_gene_id") && jsn2.containsKey("ensembl") && jsn2.get("ensembl") != null) {
                                        if (jsn2.get("ensembl").getClass().toString().equals("class groovy.json.internal.LazyMap")) {
                                            def ensembl2 = jsn2.get("ensembl") as HashMap<String, Object>;
                                            replaceID = ensembl2.get("gene");
                                        } else {
                                            def ensembl = jsn2.get("ensembl") as ArrayList<String, Object>;
                                            def gene = ensembl.get(0) as HashMap<String, Object>;
                                            replaceID = gene.get("gene");
                                        }
                                    }
                                    //welche uniprot id?? fürs erste mal swiss-prot
                                    else if (replacementIdType.equals("uniprot_ids") && jsn2.containsKey("uniprot") && jsn2.get("uniprot") != null) {
                                        def uniprot = jsn2.get("uniprot") as HashMap<String, Object>;
                                        replaceID = uniprot.get("Swiss-Prot");
                                    }
                                    //welche refseq id ?? fürs erste rna.get(0)
                                    else if (replacementIdType.equals("refseq_accession") && jsn2.containsKey("refseq") && jsn2.get("refseq") != null) {
                                        def refseq = jsn2.get("refseq") as HashMap<String, Object>;
                                        if (refseq.get("rna").getClass().toString().equals("class java.lang.String")) {
                                            def rna = refseq.get("rna");
                                            replaceID = rna;
                                        } else {
                                            def rna1 = refseq.get("rna") as ArrayList<String, Object>;
                                            replaceID = rna1.get(0);
                                        }
                                    } else {
                                        replaceID = jsn2.get("query");
                                        //println("Invalid ID.");
                                    }
                                    if (replaceID != null) {
                                        result.put(nodeIDs.get(curid), replaceID);
                                    }
                                    if(curid+1<nodeIDs.size()) {
                                        curid++;
                                    }
                                    else{
                                        break;
                                    }
                                    curquery="";
                                } else {
                                    //println("No IDs found.");
                                }
                            }
                        }
                    }
                    // println(nodeIdType+"--->"+replacementIdType+": "+result);
                } else {
                    for (int i = 0; i < nodeIDs.size(); i++) {
                        result.put(nodeIDs.get(i), nodeIDs.get(i));
                    }
                }

            } catch (MalformedURLException e ) {
                e.printStackTrace();
            } catch (IOException e ) {
                e.printStackTrace();
            } catch (Exception e ) {
                e.printStackTrace();
            }
        return result;
    }
}
