package kpm.web.kpm.parameters


public class PerturbationParameters{
    public PerturbationParameters(){
        startPercent = 0;
        stepPercent = 0;
        maxPercent = 0;
        graphsPerStep = 0;
        technique = "";
    }

    String technique
    int startPercent
    int stepPercent
    int maxPercent
    int graphsPerStep

    static belongsTo = RunParameters

    static constraints = {
    }

    public boolean isValid(){
        if(this.technique == ""){
            return false;
        }

        if(startPercent < 0 || maxPercent < 0 || startPercent > maxPercent || stepPercent < 1){
            return false;
        }

        if(graphsPerStep < 1){
            return false;
        }

        return true;
    }
}
