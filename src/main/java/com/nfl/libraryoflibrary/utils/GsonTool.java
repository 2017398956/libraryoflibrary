package com.nfl.libraryoflibrary.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by fuli.niu on 2016/9/28.
 */

public class GsonTool {

    private static Gson gson = new Gson();

    //1、对象转string
    public static String object2String(Object o){
        return gson.toJson(o);
    }

    //2、string转对象
    public static Object string2Object(String str, Class<?> c){
        if (TextUtils.isEmpty(str)) {
            return null;
        } else {
            try {
        return gson.fromJson(str, c);
            } catch (Exception e) {
                LogTool.i(ExceptionTool.getExceptionTraceString(e));
                return null;
            }
        }
    }

    //2、string转对象
    public static Object string2Object(String str, Type type) {
        if (TextUtils.isEmpty(str)) {
            return null;
        } else {
            try {
                return gson.fromJson(str, type);
            } catch (Exception e) {
                LogTool.i(ExceptionTool.getExceptionTraceString(e));
                return null;
            }
        }
    }

    //3、对象List转string
    public static String list2String(List<Object> l){

        Type type = new TypeToken<List<Object>>() {
        }.getType();
        return gson.toJson(l, type);

    }

    //4、string转对象List
    public static Object string2List(String str, Type type){
        //Type type = new TypeToken<List<Object>>(){}.getType();
        return gson.fromJson(str, type);
    }

    //5、 将HashMap字符串转换为 String
    public static String map2String(Map<String,String> m){
        return gson.toJson(m);
    }

    //6、 将HashMap字符串转换为 String
    public static String string2Map(String str){
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return gson.fromJson(str, type);
    }
}
