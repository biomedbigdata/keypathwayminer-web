package kpm.web



class KpmQueueHandlerJob {
    def queueService

    static triggers = {
      simple name:'secondsPassedTrigger', startDelay: 30000, repeatInterval: 15000 // execute job once every 15 seconds, start delay is 30 seconds.
    }

    def description = "Handler scheduling kpm runs, to avoid overload."

    def execute() {
        // This service ensures that we keep the kpm run queue up to date, and starts the runs that needs starting.
        queueService.updateQueue();
    }
}
