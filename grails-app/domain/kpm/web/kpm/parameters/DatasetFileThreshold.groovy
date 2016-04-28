package kpm.web.kpm.parameters

import kpm.web.data.DatasetFile

class DatasetFileThreshold {

    RunParameters runParameters
    Double threshold
    DatasetFile datasetFile
    Boolean activeIfSmallerThanThreshold

    static belongsTo = [runParameters: RunParameters]
    static constraints = {
    }
}
