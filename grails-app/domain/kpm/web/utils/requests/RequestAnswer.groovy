package kpm.web.utils.requests

import kpm.web.kpm.results.ResultGraph

/**
 * Created by Martin on 06-10-2014.
 */
class RequestAnswer {
    public RequestAnswer(){
        comment = "";
        success = true;
        questID = 0;
        runID = "";
        resultUrl = "";
    }

    String comment
    boolean success
    long questID
    String runID
    String resultUrl

    static hasMany = [resultGraphs: ResultGraph]

}
