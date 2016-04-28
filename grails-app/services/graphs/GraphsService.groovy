package graphs

import grails.util.Holders
import kpm.web.graph.Graph
import kpm.web.kpm.results.ResultEdge
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultNode
import kpm.web.utils.DirectoryReaderUtil
import kpm.web.utils.FileUtil
import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class GraphsService {
    static transactional = true;

    def grailsApplication
    def springSecurityService

    public Graph NewDefaultInstance(){
        return Graph.withTransaction {
            def graph = new Graph();
            graph.isDefault = true;
            graph.save(flush: true, validate: true, failOnError: true);
        }
    }

    public Graph newInstance(String attachedToID){
        return Graph.withTransaction {
            def graph = new Graph();
            graph.attachedToID = attachedToID;
            graph.save(flush: true, validate: true, failOnError: true);
        }
    }

    public List<Graph> get(String attachedToId, boolean includeDefaults){
        return Graph.withTransaction {

            // Fetching the dataset graphs attached.
            def graphs = Graph.where{attachedToID == attachedToId}.toList();

            if(includeDefaults){
                def defaults = Graph.where{isDefault == true};

                if(defaults && defaults.count() > 0){
                    graphs.addAll(defaults.toList());
                }
            }

            return graphs.sort{ it.name };
        };
    }

    public Graph update(Graph graph){
        return Graph.withTransaction {
            graph.save(flush: true, validate: true, failOnError: true);
        }
    }

    public void reset(String attachedToID){
        Graph.withTransaction {
            Graph.where {attachedToID == attachedToID}.each { rp ->
                rp.delete();
            }
        }
    }

    public void delete(Long id){
        Graph.withTransaction {
            def graph = Graph.get(id);
            if(graph){
                if(!graph.isDefault || SpringSecurityUtils.ifAllGranted("ROLE_ADMIN"))
                graph.delete(flush: true, validate: true, failOnError: true);
            }
        }
    }

    public void ensureDefaults(){
        def listOfDefaults = new ArrayList<Graph>();
        def defaultDataDir = grailsApplication.getMainContext().getResource("/default_data/graphs").file;

        if(defaultDataDir.isDirectory()){
            def files = DirectoryReaderUtil.GetFilesFromDir(defaultDataDir);
            for(File file : files){

                if(file != null && Graph.where { isDefault == true && filename == file.getName()}.count() == 0){

                    String extension = FilenameUtils.getExtension(file.name);
                    if(extension == "sif"){
                        def splitString = org.apache.commons.lang.StringUtils.split(file.name, '-')
                        def defaultGraph = new Graph();
                        defaultGraph.filename = file.name
                        defaultGraph.content = FileUtil.fileToString(file)
                        defaultGraph.isDefault = true;
                        defaultGraph.species = splitString[1].replace('_', ' ')
                        defaultGraph.description = (splitString[0] == "BIOGRID")?"Parsed from BioGrid v.4.3.129":""
                        defaultGraph.name = defaultGraph.filename
                                .replace('-', ' ')
                                .replace('.sif', '')
                                .replace('.', ' ');
                        listOfDefaults.add(defaultGraph);
                    }
                }
            }
        }

        Graph.withTransaction {
            listOfDefaults.each { defaultGraph ->
                defaultGraph.save(flush: true, validate: true, failOnError: true);
                println("|-> Saved " + defaultGraph.name);
            }
        }
    }


    public Graph getByID(Long ID){
        return Graph.get(ID);
    }



    public ResultGraph parseEdges(String graphContent, HashSet<String> nodeIDs){

        def graph = new ResultGraph();

        def nodeMap = new HashMap<String, ResultNode>();
        def relationshipTypes = new HashSet<String>();

        def counter = 0;

        graphContent.eachLine { line ->
            if(counter % 500 == 0){
                println("|--> Read " + counter + " lines.");
            }
            counter++;

            // Splitting by all white spaces
            def nodeLink = line.split("\\s+");

            // Only introduce valid "sif" format.
            if (nodeLink.size() == 3) {
                // Assume that the first value is the ID/name
                def leftNode = new ResultNode();
                leftNode.name = nodeLink[0];

                def rightNode = new ResultNode();
                rightNode.name = nodeLink[2];

                if(!nodeIDs.contains(rightNode.getName()) || !nodeIDs.contains(leftNode.getName())){
                    return;
                }

                def link = new ResultEdge(source: leftNode.id, target: rightNode.id, value: 1, relationshipType: nodeLink[1]);

                if (!nodeMap.containsKey(leftNode.id)) {
                    graph.nodes.add(leftNode);
                    nodeMap.put(leftNode.id, leftNode);

                }

                if (!nodeMap.containsKey(rightNode.id)) {
                    graph.nodes.add(rightNode);
                    nodeMap.put(rightNode.id, rightNode);
                }

                if(!relationshipTypes.contains(nodeLink[1])){
                    relationshipTypes.add(nodeLink[1]);
                }

                graph.edges.add(link);
            }
        };

//        if (nodeMap.size() > 0) {
//            graph.nodes.addAll(nodeMap.values());
//        }
//
//        if(relationshipTypes.size() > 0){
//            graph.relationshipTypes.addAll(relationshipTypes);
//        }
        graph.save(flush: true)

        return graph;
    }

    public boolean exists(String attachedToID, String name) {
        def graphs = Graph.findAllByAttachedToID(attachedToID);
        for(Graph graph : graphs){
            if(graph.name.equals(name
                    .replace('-', ' ')
                    .replace('.sif', '')
                    .replace('.', ' '))){
                return true;
            }
        }

        return false;
    }

    public Graph get(String attachedToID, String name) {
        def graphs = Graph.findAllByAttachedToID(attachedToID);
        for(Graph graph : graphs){
            if(graph.name.equals(name)){
                return graph;
            }
        }

        return null;
    }
}