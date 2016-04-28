package kpm.web.kpm.parameters

class LParameters implements Serializable{

    LParameters(){
        datasetFileID = "";
        val = 0;
        val_step = 0;
        val_max = 0;
        use_range = false;
        isPercentage = false;
        createdDate = new Date();
    }

    int val
    int val_step
    int val_max
    boolean use_range
    boolean isPercentage
    Date createdDate
    String datasetFileID

    KpmParameters owner
    static belongsTo = [owner: KpmParameters]

    static constraints = {
        datasetFileID null: false, blank: false
        owner null: true
    }

}
