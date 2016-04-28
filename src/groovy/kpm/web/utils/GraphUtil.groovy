package kpm.web.utils

/**
 * Created by Martin on 15-01-2015.
 */
public class GraphUtil {
    public static String getHexColorTemp(maxVal, minVal, actual) {
        def midVal = (maxVal - minVal)/2;
        def intR;
        def intG;
        def intB = Math.round(0);

        if (actual >= midVal){
            intR = 255;
            intG = Math.round(255 * ((maxVal - actual) / (maxVal - midVal)));
        }
        else{
            intG = 255;
            intR = Math.round(255 * ((actual - minVal) / (midVal - minVal)));
        }
        String hex = String.format("#%02x%02x%02x", intR, intG, intB);
        return hex;
    }

    public static HashMap<Integer, String> getColorValueMapping(int minVal, int maxVal){
        def result = new HashMap<Integer, String>();

        if(minVal > maxVal){
            return result;
        }

        for(def i = minVal; i <= maxVal; i++){
            def color = getHexColorTemp(maxVal, minVal, i);
            result.put(i, color);
        }

        return result;
    }
}
