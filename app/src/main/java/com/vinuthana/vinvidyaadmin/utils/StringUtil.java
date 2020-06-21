package com.vinuthana.vinvidyaadmin.utils;

import android.os.Build;
import android.util.Base64;
import android.util.Log;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.UUID;

import java.nio.charset.StandardCharsets;

public class StringUtil {
    public static  String textToBase64(String text){
        byte[] data=new byte[0];
        String base64="";
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            data = text.getBytes(StandardCharsets.UTF_8);
        }else{
            try{
                data=text.getBytes("UTF-8");

              /*  String base64encodedString = Base64.getEncoder().encodeToString(
                        "TutorialsPoint?java8".getBytes("utf-8"));*/
            }catch (Exception e){
                Log.e("TAG_EXCEPTION ",e.toString());
            }
        }
        base64= Base64.encodeToString(data,Base64.DEFAULT);
        return base64;
    }
}
