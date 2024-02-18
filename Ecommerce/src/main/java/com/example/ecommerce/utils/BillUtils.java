package com.example.ecommerce.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** utility method means which is going to be a generic
    method which can be used in any service central classes
     that may be used like suppose we need a unique id so we
     write a method which is going to return a unique id **/
@Slf4j
public class BillUtils {

    private BillUtils(){
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{ \"message\" : \"" + responseMessage + " \"} ", httpStatus);
        //"message":"responseMessage"
    }

    public static String getUuid(){
        Date date = new Date();
        long time = date.getTime();
        return "Bill-"+time;
    }

    public static JSONArray getJsonArrayFromString(String data)throws JSONException{
        JSONArray jsonArray = new JSONArray((data));
        return jsonArray;
    }


    public static Map<String,Object> getMapFromJson(String data){
        if (!Strings.isNullOrEmpty(data))
            return new Gson().fromJson(data,new TypeToken<Map<String,Object>>(){}.getType());
        return new HashMap<>();
    }

    public static Boolean isFileExist(String path){
        log.info("inside isFileExist {}",path);
        try{
            File file = new File(path);
            return(file !=null && file.exists()) ? Boolean.TRUE :Boolean.FALSE;

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
