package kpm

import grails.transaction.Transactional
import kpm.web.exceptions.InvalidSuffixException
import kpm.web.utils.FileUtil
import org.springframework.web.multipart.MultipartFile
import graphs.ConvertIdsService
import java.io.*;

@Transactional
class SaveNetworkService {
    def graphsService
    def serviceMethod() {

    }

    /**
     * saves the network to a temporary file
     * @param network which needs to be saved
     * @return path were the network is saved
     * @throws IOException
     */
    File saveNetwork(String network)throws IOException{
        def ret="";
        try {
            File tmp = File.createTempFile("xyz", ".sif", null);
            FileWriter writer = new FileWriter(tmp);
            writer.write(network);
            writer.close();
            ret=tmp
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return ret;
    }

    /**
     * Converts a network in a sif formatted string and converts the ids of the network (TODO)
     * @param network - downloaded network from ndex
     * @param fromId - type of the current ids in the network
     * @param toId - type of id that the ids should be converted into
     * @return String with sif file content (id pp id)
     */
    String parseNetwork(org.ndexbio.model.cx.NiceCXNetwork network,String fromId,String toId){
        //String ret=""
        def edges=network.getEdges()
        //def nodesLeft=new ArrayList<String>()
        //def nodesRight=new ArrayList<String>()
        def nodes=network.getNodes()
        def source
        def target
        StringBuilder str=new StringBuilder()
        //TODO make this more efficient, it currently takes about 15min+ for networks with over 100.000 edges
        for(int i=0;i<edges.size();i++) {
            source=edges.get(edges.keySet().toArray()[i]).source
            target=edges.get(edges.keySet().toArray()[i]).target
            if(nodes.get(source).nodeName!=null && nodes.get(target).nodeName!=null) {
                //TODO convert ids (left side of the network and rigth side of the network)
                //writes directly to the string (this is to change, you need to convert the nodes before)
                str.append(nodes.get(source).nodeName).append("\tpp\t").append(nodes.get(target).nodeName).append("\n")
            }
        }
        /* Old code for id conversion
            def convertLeft=ConvertIdsService.getConvertedIDs(fromId,toId,nodesLeft);
            def convertRight=ConvertIdsService.getConvertedIDs(fromId,toId,nodesRight);

        for(int i=0;i<nodesLeft.size();i++){
            ret=ret+nodesLeft.get(i)+"\tpp\t"+ nodesRight.get(i)+"\n";
        }
        */
        return str.toString();
    }

    /**
     *
     * @param file - path of the sif file of the network
     * @param title - title of the network
     * @param desc - description of the network
     * @param idType - idType of the network
     * @param spec - species
     * @param userId - user that downloaded the network
     * @return - false, if network already exists, file is incorrect (no sif file, empty,...)
     *         - true, if network has been saved
     */
    boolean saveGraph(File file, String title, String desc, String idType, String spec, String userId){
        def graphFile = file as MultipartFile;
        def size=file.size();
        if(!graphFile || size == 0){
            return false;
        }else {
            if (graphsService.exists(userId, title)) {
                return false;
            } else {
                // we only support SIF files at the moment, so check if at least the extension fits.
                try {
                    FileUtil.checkForExtension(file.name, "sif")
                } catch (InvalidSuffixException ise) {
                    return false;
                }
                def graph = graphsService.newInstance(userId);
                graph.content = FileUtil.fileToString(file);
                graph.filename = file.name;
                graph.name = title;
                graph.species = spec;
                if(desc.length()>200){
                    graph.description = desc.substring(0,200);
                }
                else{
                    graph.description=desc;
                }
                graph.nodeIdType = idType;
                if (graph.name == null) {
                    graph.name = graph.filename;
                }
                //saves the graph into the database
                graphsService.update(graph);
                return true
            }
        }
    }
}
