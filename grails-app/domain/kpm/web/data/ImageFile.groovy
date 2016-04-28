package kpm.web.data
/**
 * Created by: Martin
 * Date: 18-09-14
 */
public class ImageFile{
    public ImageFile(){
        data_base64 = "";
        height = 0;
        width = 0;
        size = 0;
        contentType = "";
        imageName = "";
        attachedToID = "";
        createdDate = new Date();
    }

    String attachedToID;
    String imageName;
    String contentType;
    String data_base64;
    int height;
    int width;
    int size;
    Date createdDate

    static mapping = {
        data_base64 type :  'text'
    }
}
