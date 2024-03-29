package com.nfl.libraryoflibrary.utils;

import android.util.Log;

import com.nfl.libraryoflibrary.constant.ApplicationContext;

/**
 * Created by fuli.niu on 2016/7/11.
 */
public class LogTool {

//    private final String  TAG = "libraryoflibrary" ;

    private static final String TAG = "NFL";

    public static void i(String info) {
        Log.i(TAG, info);
    }

    public static void i(int resId) {
        if (null != ApplicationContext.applicationContext) {
            Log.i(TAG, ApplicationContext.applicationContext.getString(resId));
        }
    }

    public static void i(String tag, String info) {
        Log.i(tag, info);
    }

    public static void d(String tag, String info) {
        Log.d(tag, info);
    }

    public static void e(String tag, String info) {
        Log.e(tag, info);
    }

    public static void e(String info) {
        LogTool.e(TAG, info);
    }
}
