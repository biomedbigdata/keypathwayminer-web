package kpm.web

import grails.converters.JSON
import kpm.web.data.ImageFile

class ImageController {
    def getImagesAsJson(String attachedToID){
        def images = ImageFile.findAllByAttachedToID(attachedToID);
        render images as JSON;
    }

//    def getImage(String id){
//        def img = ImageFile.findById(id);
//
//        if(img){
//            render file: img.data_base64, fileName: img.imageName;
//        }
//    }
}
