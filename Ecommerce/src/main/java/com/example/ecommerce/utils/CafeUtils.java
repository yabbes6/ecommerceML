package com.example.ecommerce.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** utility method means which is going to be a generic
    method which can be used in any service central classes
     that may be used like suppose we need a unique id so we
     write a method which is going to return a unique id **/
public class CafeUtils {

    private CafeUtils(){
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{ \"message\" : \"" + responseMessage + " \"} ", httpStatus);
        //"message":"responseMessage"
    }
}
