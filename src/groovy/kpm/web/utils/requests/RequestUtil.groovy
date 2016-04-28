package kpm.web.utils.requests

import kpm.web.exceptions.InvalidRequestException

/**
 * Created by Martin on 07-10-2014.
 */
public class RequestUtil {
    public static Object ContainsKeyOrThrow(HashMap<String,Object> map, String field){
        if(map.containsKey(field)){
            return true;
        }

        throw new InvalidRequestException("Missing $field field.");
    }
}
