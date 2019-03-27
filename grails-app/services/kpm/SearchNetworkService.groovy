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

    /**
     * searches in NDEx for a network with the query
     * @param query - query to search for
     * @return returns network ids who are found to that query
     */
    public searchNetwork(String query){
       return ndex.findNetworks(query,null,0,1000);
    }

    /**
     * downloads a NDEx network with a given id
     * @param id - id of the network
     * @return downloaded network
     */
    public downloadNetwork(UUID id){
       return ndex.getNetworkSmaller(id)
    }

    /**
     * downloads a NDEx network with a given id (returns a stream)
     * @param id - id of the network
     * @return stream of the network
     */
    public downloadNetworkStream(UUID id) {
        return ndex.getNetworkAsCXStream(id)
    }
}
