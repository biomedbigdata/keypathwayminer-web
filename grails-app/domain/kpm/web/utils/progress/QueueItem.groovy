package kpm.web.utils.progress

class QueueItem {
    public QueueItem(){
        runParamID = 0;
        progressID = 0;
        enqueueDate = null;
        isRunning = false;
        hasExecuted = false;
    }

    Long runParamID
    Long progressID
    Date enqueueDate
    boolean isRunning
    boolean hasExecuted

    static mapping = {
        sort enqueueDate : "asc"
    }
}
