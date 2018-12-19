package kpm.web.kpm.results

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType

/**
 * Created by: Martin
 * Date: 22-09-14
 */
public class ResultGraph {

    public ResultGraph(){
        this.edges = new ArrayList<ResultEdge>();
        this.nodes = new ArrayList<ResultNode>();
        k = 0;
        l = 0;
        isUnionSet = false;
        nodeSetNr = 0;
        maxNodeCount = 1;
    }

    int k
    int l
    int nodeSetNr
    int maxNodeCount
    boolean isUnionSet
    String csvFormattedEdges;
    String csvFormattedNodes;

    List edges;
    List nodes;
    @Cascade(CascadeType.ALL)
    static belongsTo = [owner: ResultSet]
    static mapping = {
        csvFormattedEdges type :  'text'
        csvFormattedNodes type :  'text'
    }

    static hasMany = [
        edges: ResultEdge,
        nodes: ResultNode
    ]

    /*public void fillEdgeAndNodeLists(){
        String[] csvEdges = csvFormattedEdges.split(System.lineSeparator());
        for(String edge : csvEdges){
            if(edge != ""){
                edges.add(new ResultEdge(edge));
            }
        }

        String[] csvNodes = csvFormattedNodes.split(System.lineSeparator());
        for(String node : csvNodes){
            if(node != ""){
                nodes.add(new ResultNode(node));
            }
        }
    }*/

    public void convertNames(Map<String, String> nameChangeMap) {
        for(ResultNode node : nodes){
            if(nameChangeMap.containsKey(node.name)){
                node.name = nameChangeMap.get(node.name);
            }
        }

        for(ResultEdge edge : edges){
            if(nameChangeMap.containsKey(edge.source)){
                edge.source = nameChangeMap.get(edge.source);
            }

            if(nameChangeMap.containsKey(edge.target)){
                edge.target = nameChangeMap.get(edge.target);
            }
        }
    }
}
