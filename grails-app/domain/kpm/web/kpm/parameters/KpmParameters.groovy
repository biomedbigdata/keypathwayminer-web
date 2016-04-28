package kpm.web.kpm.parameters

public class KpmParameters implements Serializable{

    public KpmParameters(){
        unmapped_nodes = "Add to positive list";
        removeBENs = true;
        graphID = 0;
        name = "";
        algorithm = Algorithm.Greedy;
        strategy = Strategy.INES;
        samePercentage_val = 0;
        samePercentage_val_max = 0;
        samePercentage_val_step = 0;
        samePercentage_useRange = false;
        createdDate = new Date();
    }

    String name
    Algorithm algorithm
    Strategy strategy
    boolean removeBENs
    String unmapped_nodes
    int computed_pathways
    Long graphID
    Date createdDate
    boolean l_samePercentage
    boolean samePercentage_useRange
    int samePercentage_val
    int samePercentage_val_step
    int samePercentage_val_max

    static constraints = {
        unmapped_nodes inList: ['Add to negative list', 'Add to positive list']
        removeBENs null: false, blank: false
    }

    static belongsTo = RunParameters
    static fetchMode = [k_values: 'eager', l_values: 'eager']

    String toString(){
        return name;
    }



}
