package kpm.web.kpm.results

/**
 * Created by: Martin
 * Date: 22-09-14
 */
public class ResultSet{
    public ResultSet(){
        this.k = 0;
        this.l = 0;
        this.runID = "";
        amountComputedNodeSets = 0;
        csvResultsInfoTable = "";
    }

    int k;
    int l;
    int amountComputedNodeSets;
    String runID;
    String csvResultsInfoTable;

    static hasMany = [nodeIDs : String]
    static mapping = {
        csvResultsInfoTable type :  'text'
    }
}
