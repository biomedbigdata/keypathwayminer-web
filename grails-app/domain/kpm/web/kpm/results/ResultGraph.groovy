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

    List edges;
    List nodes;

    static belongsTo = [owner: ResultSet]

    static hasMany = [
        edges: ResultEdge,
        nodes: ResultNode
    ]

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
