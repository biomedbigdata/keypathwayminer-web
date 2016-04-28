package kpm.web.utils

/**
 * Created by: Martin
 * Date: 15-09-14
 */
public class StringUtil {
    public static Set<String> getLines(String file){
        def ret = new HashSet<String>();

        if(file == null){
            return ret;
        }

        def br = new BufferedReader(new StringReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] entries = line.split("\t");
            String id = entries[0].trim();
            ret.add(id);
        }

        br.close();

        return ret;
    }

    public static String getCSVFormat(Object[] list){
        String str = "";

        if (list.length == 0){
            return str;
        }

        for(Object obj : list){
            str += obj.toString() + ";";
        }

        return str;
    }

    public static String getCSVFormat(Object[][] listlist){
        String str = "";

        if (listlist.length == 0){
            return str;
        }

        for(Object[] obj : listlist){
            str += getCSVFormat(obj) + LineSeparator();
        }

        return str;
    }

    public static String LineSeparator(){
        return "<[LINEBREAK]>";
    }
}
