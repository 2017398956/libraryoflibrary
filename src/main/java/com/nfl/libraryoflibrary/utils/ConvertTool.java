package com.nfl.libraryoflibrary.utils;

import android.util.TypedValue;

import com.nfl.libraryoflibrary.constant.ApplicationContext;

/**
 * Created by fuli.niu on 2016/9/13.
 * dp sp px 互相转换工具
 */
public class ConvertTool {

    private ConvertTool() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int dp2px(int dp) {
        if(ApplicationContext.applicationContext != null){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ApplicationContext.applicationContext.getResources().getDisplayMetrics());
        }else {
            return 0 ;
        }
    }

    /**
     * sp转px
     * @param spVal
     * @return
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, ApplicationContext.applicationContext.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     * @param pxVal
     * @return
     */
    public static float px2dp(float pxVal) {
        final float scale = ApplicationContext.applicationContext.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     * @param pxVal
     * @return
     */
    public static float px2sp(float pxVal) {
        return (pxVal / ApplicationContext.applicationContext.getResources().getDisplayMetrics().scaledDensity);
    }
}
