package com.nfl.libraryoflibrary.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by fuli.niu on 2016/9/1.
 */
public class JSONTool {

    public static Map<String, String> parseJSONString2Map(String jsonStr) {
        Map<String, String> map = new HashMap<String, String>();
        // 最外层解析
        try {
            JSONObject json = new JSONObject(jsonStr);
            Iterator iterator = json.keys();
            while (iterator.hasNext()) {
                String temp = (String) iterator.next();
                map.put(temp, (String) json.get(temp));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String> parseJSONObject2Map(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<String, String>();
        // 最外层解析
        try {
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String temp = (String) iterator.next();
                map.put(temp, (String) jsonObject.get(temp));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

}
