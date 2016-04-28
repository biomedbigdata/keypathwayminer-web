package kpm.web

import kpm.web.base.BaseController
import kpm.web.data.DatasetFile
import kpm.web.utils.FileUtil
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile

class DatasetsController extends BaseController{
    def datasetFileService
    def xlsxImportService
    def fileImportService

    def index() {
        def datasets = datasetFileService.get(getUserID(), true);

        render(view:"index", model: [datasetsList: datasets]);
    }
    def newDataset(){
        def redirectFrom = params.get("redirectFrom") as String;
        render(view:"newDataset", model: [redirectFrom: redirectFrom]);
    }

    def download(){
        def datasetFileInstance = DatasetFile.get(params.id)
        if(!datasetFileInstance){
            flash.error = "DatasetFile not found"
            return
        }
        def filename = FilenameUtils.getBaseName(datasetFileInstance.filename).toString() + ".txt"
        response.setContentType("application/octet-stream")
        response.setHeader("Content-Disposition","attachment; filename=\"${filename}\"")
        response.outputStream << datasetFileInstance.content.bytes
        return
    }

    def uploadDataset(){
        def datasetFile = params.datasetFile as MultipartFile;
        def name = params.datasetName as String;
        def description = params.description as String;

        if(!datasetFile){
            flash.message = "No file uploaded.";
        }else{
            if(datasetFileService.exists(getUserID(), name)) {
                flash.message = "A dataset with name '" + name + "' already exists.";
                render(view:"newDataset");
                return
            }else{

                def dataset = datasetFileService.newInstance(getUserID());
                dataset.filename = datasetFile.originalFilename;

                dataset.name = name;
                dataset.species = params.species
                dataset.type = params.type
                dataset.description = description;

                if(dataset.name == null){
                    dataset.name = dataset.filename;
                }

                def fileEnding = FilenameUtils.getExtension(dataset.filename)

                if(fileEnding.toLowerCase() == "xlsx") {
                    dataset.content = xlsxImportService.parseXLSXSheetToTSV(datasetFile, 0, params.type == "Indicator Matrix")
                }
                else if(fileEnding.toLowerCase() == "csv") {
                    dataset.content = fileImportService.convertCSVtoTSV(datasetFile)
                }
                else dataset.content = FileUtil.fileToString(datasetFile)
                datasetFileService.update(dataset);

                flash.okay = "Dataset '"+datasetFile.originalFilename+"' uploaded.";
            }
        }

        // Added redirecting.
        def redirectFrom = params.get("redirectFrom") as String;
        if(redirectFrom != null && redirectFrom.length() > 0){
            redirect(url: URLDecoder.decode(redirectFrom, "UTF-8"));
            return;
        }

        redirect(action:"index");
    }

    def deleteDataset(String id){
        datasetFileService.delete(id);
        redirect(action: "index");
    }
}
