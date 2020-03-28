package data

import grails.util.Holders
import kpm.web.data.DatasetFile
import kpm.web.kpm.parameters.DatasetFileThreshold
import kpm.web.kpm.parameters.ExceptionParameters
import kpm.web.kpm.parameters.KpmParameters
import kpm.web.kpm.parameters.LParameters
import kpm.web.kpm.parameters.RunParameters
import kpm.web.utils.DirectoryReaderUtil
import kpm.web.utils.FileUtil
import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class DatasetFileService {
    def grailsApplication
    static transactional = true;

    public DatasetFile NewDefaultInstance(){
        return DatasetFile.withTransaction {
            def file = new DatasetFile();
            file.isDefault = true;
            file.save(flush: true, validate: true, failOnError: true);
        }
    }

    public DatasetFile newInstance(String attachedToID){
        return DatasetFile.withTransaction {
            def file = new DatasetFile();
            file.attachedToID = attachedToID;
            file.save(flush: true, validate: true, failOnError: true);
        }
    }

    public List<DatasetFile> get(String attachedToId, boolean includeDefaults){
        return DatasetFile.withTransaction {

            // Fetching the dataset files attached.
            def files = DatasetFile.where{attachedToID == attachedToId}.toList();

            if(includeDefaults){
                def defaults = DatasetFile.where{isDefault == true};

                if(defaults && defaults.count() > 0){
                    files.addAll(defaults.toList());
                }
            }

            return files.sort{ it.name };
        };
    }

    public Long getID(String attachedToID, String datasetName){
        def files = DatasetFile.findAllByAttachedToID(attachedToID);

        for(DatasetFile file: files){
            if(file.name.equals(datasetName)){
                return file.id;
            }
        }

        return 0;
    }

    public boolean exists(String attachedToID, String datasetName) {
        def files = DatasetFile.findAllByAttachedToID(attachedToID);
        for(DatasetFile file: files){
            if(file.name.equals(datasetName)){
                return true;
            }
        }

        return false;
    }

    public DatasetFile getByName(String attachedToID, String datasetName) {
        def files = DatasetFile.findAllByAttachedToID(attachedToID);
        for(DatasetFile file: files){
            if(file.name.equals(datasetName)){
                return file;
            }
        }

        return null;
    }

    public DatasetFile update(DatasetFile file){
        return DatasetFile.withTransaction {
            file.save(flush: true, validate: true, failOnError: true);
        }
    }

    public void resetOldData(){
        DatasetFile.withTransaction {
            Date today = new Date().clearTime();
            Date yesterday = (today - 1).clearTime() - 1;
            DatasetFile.where {createdDate < yesterday && isDefault == false}.deleteAll()
        }
    }

    public void reset(String attachedToID){
        DatasetFile.withTransaction {
            DatasetFile.where {attachedToID == attachedToID}.each { rp ->
                rp.delete();
            }
        }
    }

    public void delete(String id){
        DatasetFile.withTransaction {
            def dataset = DatasetFile.lock(id);
            if(dataset) {
                if (!dataset.isDefault || SpringSecurityUtils.ifAllGranted("ROLE_ADMIN")) {
                    def parameters = RunParameters.withCriteria {
                        datasetFiles {
                            idEq(id as long)
                        }
                    }

                    DatasetFileThreshold.findAllByDatasetFile(dataset).each{ it.delete()}

                    parameters.each {
                        ExceptionParameters.findAllByOwner(it.parameters).each{ eps -> eps.delete()}
                        LParameters.findAllByOwner(it.parameters).each {lps -> lps.delete()}
                        //it.parameters.delete();
                        it.delete()
                    }
                    dataset.delete();
                }
            }
        }
    }

    public void ensureDefaults(){
        def defaultDataDir = grailsApplication.getMainContext().getResource("/default_data/datasets").file;

        def listOfDefaults = new ArrayList<DatasetFile>();

        if(defaultDataDir.isDirectory()){
            def files = DirectoryReaderUtil.GetFilesFromDir(defaultDataDir);
            for(File file : files){

                // Only include those not already included.
                if(file != null && DatasetFile.where { isDefault == true && filename == file.getName() }.count() == 0){

                    def datasetFile = new DatasetFile()
                    def splitString = org.apache.commons.lang.StringUtils.split(file.name, '-')

                    datasetFile.content = FileUtil.fileToString(file)
                    datasetFile.filename = file.name
                    datasetFile.name = file.name
                            .replace('-', ' ')
                            .replace('.txt', '')
                            .replace('.sif', '')
                            .replace('.dat', '')
                            .replace('.csv', '')
                            .replace('.', ' ');
                    datasetFile.isDefault = true;
                    datasetFile.species = "Homo sapiens"
                    datasetFile.description = file.name.toString()
                    datasetFile.type = (splitString[0] == "huntington")?"Numerical Matrix":"Indicator Matrix"

                    listOfDefaults.add(datasetFile);
                }
            }
        }
        try {
            DatasetFile.withTransaction {
                listOfDefaults.each { defaultFile ->
                    println("|-> Saving " + defaultFile.name);
                    defaultFile.save(flush: true, validate: true, failOnError: true);
                    println("|-> Saved " + defaultFile.name);
                }
            }
        }catch(Exception e){
            println e.getMessage();
        }
    }
}