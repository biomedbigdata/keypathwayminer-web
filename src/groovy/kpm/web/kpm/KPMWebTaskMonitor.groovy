package kpm.web.kpm

import dk.sdu.kpm.taskmonitors.IKPMTaskMonitor
import progress.QuestService

/**
 * User: Martin
 * Date: 04-09-14
 */
public class KPMWebTaskMonitor implements IKPMTaskMonitor {
    private Long progressID;
    private String runID;
    private QuestService questService;

    public KPMWebTaskMonitor(Long progressID, String runID, QuestService questService){
        this.progressID = progressID;
        this.questService = questService;
        this.runID = runID;
    }

    public void setName(String s){
        questService.updateName(this.progressID, s);
    }

    public void setCancelled(){
        questService.cancelQuestWithRunID(this.runID);
    }

    @Override
    void setTitle(String s) {
        questService.updateTitle(this.progressID, s);
    }

    @Override
    void setProgress(double v) {

        // Ensuring we always have at most 100%
        if(v > 1){
            v = 1;
        }

        // Rounding to two decimals.
        v = Math.round(v*100.0)/100.0;

        questService.updateProgress(this.progressID, v);
    }

    @Override
    void setStatusMessage(String s) {
        questService.updateStatusMessage(this.progressID, s);
    }

    void setRunID(String s){
        questService.updateRunID(this.progressID, s);
    }
}
