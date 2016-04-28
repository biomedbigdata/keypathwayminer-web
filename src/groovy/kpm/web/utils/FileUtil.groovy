package kpm.web.utils

import kpm.web.exceptions.InvalidSuffixException
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile

/**
 * Created by: Martin
 * Changed by: Markus List
 * Date: 30-08-14
 * Updated: 12-05-15
 */
public class FileUtil {

    static String fileToString(File file){
        FileUtils.readFileToString(file)
    }

    static String fileToString(MultipartFile multipartFile) {
        StringWriter writer = new StringWriter();
        IOUtils.copy(multipartFile.getInputStream(), writer, "UTF-8");
        return(writer.toString());
    }

    static boolean checkForExtension(filename, extension){
        String fileExtension = FilenameUtils.getExtension(filename);

        if (extension != fileExtension) {
            throw new InvalidSuffixException(String.format("The provided format was '%s' and not the supported '.sif'.", extension));
        }
    }
}
