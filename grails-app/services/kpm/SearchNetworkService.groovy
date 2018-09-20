package kpm

import grails.transaction.Transactional
import org.ndexbio.rest.client.NdexRestClient
import org.ndexbio.rest.client.NdexRestClientModelAccessLayer

@Transactional
class SearchNetworkService {
    /*für testserver

    def  _username = "cj1";
     def _password = "aaaaaaaaa";
     def _route = "dev.ndexbio.org";
     */

    def _route = "www.ndexbio.org";
    NdexRestClient client = new NdexRestClient(_route);
    NdexRestClientModelAccessLayer ndex = new NdexRestClientModelAccessLayer(client);
    def serviceMethod() {

    }

    public searchNetwork(String query){
       return ndex.findNetworks(query,null,0,1000);
    }

    public downloadNetwork(UUID id){
       return ndex.getNetwork(id);
    }
}
