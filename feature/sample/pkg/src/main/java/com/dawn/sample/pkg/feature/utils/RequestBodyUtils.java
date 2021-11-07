package com.dawn.sample.pkg.feature.utils;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author : LeeZhaoXing
 * @date : 2021/11/5
 * @desc :
 */
public class RequestBodyUtils {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");


    public static RequestBody change(Object data) {
        try {
            return RequestBody.create(MEDIA_TYPE, new Gson().toJson(data));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}