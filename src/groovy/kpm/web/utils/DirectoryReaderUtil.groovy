package kpm.web.utils
/**
 * Created by: Martin
 * Date: 30-08-14
 */
public class DirectoryReaderUtil {

    public static List<File> GetFilesFromDir(File dir){
        if(dir == null || !dir.isDirectory()){
            throw new FileNotFoundException("Directory does not exist.");
        }

        def files = new ArrayList<File>();

        for(File file : dir.listFiles()){
            files.add(file);
        }

        return files;
    }
}
