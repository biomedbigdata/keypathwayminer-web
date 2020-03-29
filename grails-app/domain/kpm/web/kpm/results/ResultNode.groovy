package kpm.web.kpm.results

/**
 * Created with IntelliJ IDEA.
 * User: Martin
 * Date: 26-09-14
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
public class ResultNode {
    public ResultNode(){
        nodeID = "";
        name = "";
        overlapCount = 1;
        color = "#ccc";
    }

    static belongsTo = ResultGraph

    public ResultNode(String csvFormattedNode){
        this();

        String[] parts = csvFormattedNode.split(";");
        if(parts.length < 4){
            return;
        }

        this.nodeID = parts[0];
        this.name = parts[1];

        try{
            this.overlapCount = Integer.parseInt(parts[2]);
        }catch(NumberFormatException nfe){
            this.overlapCount = 1;
        }

        this.color = parts[3];
    }

    String nodeID
    String name
    int overlapCount
    String color

    static constraints = {
        color null: true, blank: true
        overlapCount null: true, blank: true
    }


     public String toCSVFormat(){
        String str = "";

        if (this == null){
            return str;
        }

         str += nodeID + ";";
         str += name + ";";
         str += overlapCount + ";";
         str += color + ";";

        return str;
    }
}
