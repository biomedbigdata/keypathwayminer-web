package kpm.web.utils.progress


public class Quest implements Serializable{
    public Quest(){
        name = "";
        statusMessage = "";
        title = "";
        isCompleted = false;
        progress = 0;
        runID = "";
        isCancelled = false;
        runParamsID = 0;
        createdDate = new Date();
    }

    String attachedToID;
    String name;
    boolean isCompleted;
    boolean isCancelled;
    String title;
    String statusMessage;
    String runID;
    Date createdDate;
    Long runParamsID;

    // Between 0 and 1.
    Double progress;

    static constraints = {
        attachedToID null: false
    }

}
