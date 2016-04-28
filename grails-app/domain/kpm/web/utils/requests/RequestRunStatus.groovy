package kpm.web.utils.requests

/**
 * Created by Martin on 13-01-2015.
 */
class RequestRunStatus{
    public RequestRunStatus(){
        completed = false;
        cancelled = false;
        runExists = false;
        progress = 0.0;
    }

    boolean completed
    boolean runExists
    boolean cancelled
    double progress
}
