package com.nfl.libraryoflibrary.utils;

import android.util.TypedValue;

import com.nfl.libraryoflibrary.constant.ApplicationContext;

/**
 * Created by fuli.niu on 2016/9/13.
 * dp sp px 互相转换工具
 */
public class ConvertTool {
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ApplicationContext.applicationContext.getResources().getDisplayMetrics());
    }
}
