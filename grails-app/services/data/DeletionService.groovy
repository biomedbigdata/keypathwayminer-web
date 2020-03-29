package data

import grails.transaction.Transactional
import kpm.web.data.DatasetFile
import kpm.web.data.ImageFile
import kpm.web.graph.Graph
import kpm.web.kpm.results.ResultGraph
import kpm.web.kpm.results.ResultSet
import kpm.web.utils.progress.Quest
import kpm.web.utils.requests.RequestAnswer

@Transactional
class DeletionService {
    def queueService
    def runParametersService
    def graphsService

    def deleteQuests(oldQuests) {
        try {

            for (Quest oldQuest : oldQuests) {
                Long id = oldQuest.id;
                queueService.deQueueQuest(id);
                runParametersService.delete(oldQuest.runParamsID);
                RequestAnswer.findAllByQuestID(id).each{
                    it.delete(flush: true, validate: true, failOnError: true)
                }
                ResultSet.findAllByRunID(oldQuest.runID).each{
                    ResultGraph.findAllByOwner(it).each{ rg ->
                        rg.delete(flush: true, validate: true, failOnError: true)
                    }
                    it.delete(flush: true, validate: true, failOnError: true)
                }
                oldQuest.delete(flush: true, validate: true, failOnError: true)
            }
        } catch (Exception e) {
            println e.getMessage()
            println e.getStackTrace()
        }}

    def deleteGraphs(oldGraphs) {
        try {
            for (Graph oldGraph : oldGraphs) {
                Long id = oldGraph.id;
                graphsService.delete(id);
            }
        } catch (Exception e) {
            println e.getMessage()
            println e.getStackTrace()
        }
    }

    def deleteDatasets(oldDatasets) {
    try {
        for (DatasetFile dataset : oldDatasets) {
            dataset.delete(flush: true, validate: true, failOnError: true);
        }
    } catch (Exception e) {
        println e.getMessage()
        println e.getStackTrace()
    }}

        def deleteImages(oldImages) {
            try {

                for (ImageFile image : oldImages) {
                    image.delete(flush: true, validate: true, failOnError: true);
                }
            } catch (Exception e) {
                println e.getMessage()
                println e.getStackTrace()
            }
        }
    }