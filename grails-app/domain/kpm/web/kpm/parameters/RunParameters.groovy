package kpm.web.kpm.parameters

import kpm.web.data.DatasetFile
import kpm.web.graph.Graph
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType

public class RunParameters{
    public RunParameters(){
        isReadyToRun = false;
        linkType = "";
        withPerturbation = false;
        isBatch = true;
        isReadyToRun = false;
        positiveNodes = "";
        negativeNodes = "";
        goldStandardNodes = "";
        createdDate = new Date();
        selectedByUserAsCurrent = false;
        runID = "";
    }

    boolean withPerturbation
    boolean isBatch

    KpmParameters parameters
    PerturbationParameters perturbation
    String linkType
    boolean isReadyToRun
    String attachedToID
    String positiveNodes
    String negativeNodes
    String goldStandardNodes
    Date createdDate
    boolean selectedByUserAsCurrent
    String runID

    static hasMany = [datasetFiles: DatasetFile]

    static mapping = {
        goldStandardNodes type : 'text'
        positiveNodes type : 'text'
        negativeNodes type : 'text'
    }


    static fetchMode = [parameters: 'eager', perturbation: 'eager']


}
