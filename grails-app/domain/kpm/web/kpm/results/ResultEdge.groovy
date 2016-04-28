package kpm.web.kpm.results

/**
 * Created with IntelliJ IDEA.
 * User: Martin
 * Date: 26-09-14
 * Time: 21:25
 * To change this template use File | Settings | File Templates.
 */
public class ResultEdge {
    public ResultEdge(){
        source = "";
        target = "";
        value = 0;
        relationshipType = "";
    }

    public ResultEdge(String csvFormattedEdge){
        this();

        String[] parts = csvFormattedEdge.split(";");
        if(parts.length < 4){
            return;
        }

        this.source = parts[0];
        this.target = parts[1];

        try{
            this.value = Integer.parseInt(parts[2]);
        }catch(NumberFormatException nfe){
            this.value = 1;
        }

        this.relationshipType = parts[3];
    }

    // Must be the ID of the source
    String source

    // Must be the ID of the target
    String target

    // The weight of the link between them
    int value

    // The type of the connection between them.
    String relationshipType

    public String toCSVFormat(){
        String str = "";

        if (this == null){
            return str;
        }

        str += source+ ";";
        str += target + ";";
        str += value + ";";
        str += relationshipType + ";";

        return str;
    }
}
