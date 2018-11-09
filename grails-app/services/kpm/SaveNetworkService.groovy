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

    String parseNetwork(org.ndexbio.model.cx.NiceCXNetwork network,String fromId,String toId){
        String ret="";
        def edges=network.getEdges();
        def nodesLeft=new ArrayList<String>();
        def nodesRight=new ArrayList<String>();
        for(int i=0;i<edges.size();i++) {
            def source=edges.get(edges.keySet().toArray()[i]).source;
            def target=edges.get(edges.keySet().toArray()[i]).target;
            def nodes=network.getNodes();
            def snode=nodes.get(source).nodeName;
            def tnode=nodes.get(target).nodeName;
            snode=snode.replace(" ","");
            tnode=tnode.replace(" ","");
            nodesLeft.add(snode);
            nodesRight.add(tnode);
        }
        def convertLeft=ConvertIdsService.getConvertedIDs(fromId,toId,nodesLeft);
        def convertRight=ConvertIdsService.getConvertedIDs(fromId,toId,nodesRight);
        def leftArray=new String[nodesLeft.size()];
        def rightArray=new String[nodesRight.size()];

        for(int i=0;i<nodesLeft.size();i++){
            if(convertLeft.containsKey(nodesLeft.get(i))){
                leftArray[i]=convertLeft.get(nodesLeft.get(i));
            }
            if(convertRight.containsKey(nodesRight.get(i))){
                rightArray[i]=convertRight.get(nodesRight.get(i));
            }
            ret=ret+leftArray[i]+" pp "+ rightArray[i]+" \n";
        }
        println(ret);
        return ret;

    }

    boolean saveGraph(File file,String title,String desc,String idType, String spec,String userId){
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
                graph.description = desc.substring(0,200);
                graph.nodeIdType = idType;
                if (graph.name == null) {
                    graph.name = graph.filename;
                }
                graphsService.update(graph);
                return true
            }
        }
    }
}
