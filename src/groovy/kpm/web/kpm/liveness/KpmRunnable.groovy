package kpm.web.kpm.liveness

import dk.sdu.kpm.logging.KpmLogger
import dk.sdu.kpm.runners.BatchRunWithPerturbationRunner
import dk.sdu.kpm.runners.BatchRunner
import kpm.web.utils.progress.Quest

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.logging.Level

/**
 * Created by Martin on 02-10-2014.
 */
public class KpmRunnable implements Runnable{

    private boolean withPerturbation;
    private BatchRunWithPerturbationRunner perturbationRunner;
    private BatchRunner batchRunner;
    private volatile boolean isFinished;
    private volatile Long questID;
    private volatile ExecutorService executor;

    public KpmRunnable(BatchRunWithPerturbationRunner pbr, Long questID){
        this.perturbationRunner = pbr;
        this.batchRunner = null;
        this.withPerturbation = true;
        this.questID = questID;
    }
    public KpmRunnable(BatchRunner br, Long questID){
        this.perturbationRunner = null;
        this.batchRunner = br;
        this.withPerturbation = false;
        this.questID = questID;
    }

    @Override
    void run() {
        this.executor = Executors.newCachedThreadPool();

        this.isFinished = false;

        if(this.withPerturbation){
            executor.submit(perturbationRunner);
        }else{
            executor.submit(batchRunner);
        }

        // We sleep every second, only checking once every second whether the runs has completed/failed/cancelled.
        executor.submit({
            try{
            while(!this.isFinished()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
    }

    private synchronized boolean isFinished(){
            def quest = Quest.get(questID);

            if(!quest){
                KpmLogger.log(Level.SEVERE, "(KpmRunnable) No quest found.");
                this.isFinished = true;
                cancelRuns();
                return true;
            }

            quest.refresh();

            if(quest.isCancelled || quest.isCompleted){
                this.isFinished = true;
            }

            // Remember to cancel the runs.
            if(quest.isCancelled){
                cancelRuns();
            }

        return this.isFinished;
    }

    private void cancelRuns(){
        if(this.batchRunner != null){
            this.batchRunner.cancel();
        }

        if(this.perturbationRunner != null){
            this.perturbationRunner.cancel();
        }
    }
}
