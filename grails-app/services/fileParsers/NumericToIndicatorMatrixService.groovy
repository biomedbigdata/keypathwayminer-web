package fileParsers

import grails.transaction.Transactional
import kpm.web.data.DatasetFile
import kpm.web.kpm.parameters.DatasetFileThreshold
import kpm.web.kpm.parameters.RunParameters

@Transactional
class NumericToIndicatorMatrixService {

    def parseToIndicatorMatrix(DatasetFile datasetFile, RunParameters runParameters) {

        //remove empty lines
        String content =  datasetFile.content
        //String content =  datasetFile.content.replaceAll("\n+", "\n");
        if(datasetFile.type != "Numerical Matrix") {
            return(content)
        }

        DatasetFileThreshold datasetFileThreshold = DatasetFileThreshold.findByDatasetFileAndRunParameters(datasetFile, runParameters)
        double threshold = datasetFileThreshold?.threshold
        boolean smallerThan = datasetFileThreshold?.activeIfSmallerThanThreshold
        if(!threshold) throw new Exception("Parsing of indicator matrix for dataset ${datasetFile.name} failed in absence of a threshold.")

        Scanner scanner = new Scanner(content)
        StringBuffer sb = new StringBuffer()

        //skip header
        if(scanner.hasNextLine()) scanner.nextLine()
        else{
            throw new Exception("Parsing of indicator matrix for dataset ${datasetFile.name} failed. No header.")
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] cells = line.split("\t")
            sb.append(cells[0])
            for(i in 1..cells.length-1)
            {
                Double value
                try{
                    value = Double.parseDouble(cells[i].toString())
                }catch(Exception e){
                    throw new Exception("Parsing of indicator matrix for dataset ${datasetFile.name} failed. ${e.getMessage()}")
                }

                if(smallerThan) {
                    if (value < threshold) sb.append("\t 1")
                    else sb.append("\t 0")
                }
                else{
                    if (value < threshold) sb.append("\t 0")
                    else sb.append("\t 1")
                }
            }
            if(scanner.hasNextLine()){
                sb.append("\n")
            }
        }
        scanner.close();

        return(sb.toString())
    }
}
