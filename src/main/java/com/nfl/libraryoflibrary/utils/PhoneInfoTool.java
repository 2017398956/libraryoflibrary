package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by fuli.niu on 2016/8/5.
 */
public class PhoneInfoTool {
    private static int mScreenHeight = 0 ;
    private static int mScreenWidth = 0 ;

    public static int getScreenHeight(Activity activity){
        if(mScreenHeight == 0){
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
            mScreenHeight = localDisplayMetrics.heightPixels;
            mScreenWidth = localDisplayMetrics.widthPixels;
        }
        return mScreenHeight ;
    }

    public static int getScreenWidth(Activity activity){
        if(mScreenWidth == 0){
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
            mScreenHeight = localDisplayMetrics.heightPixels;
            mScreenWidth = localDisplayMetrics.widthPixels;
        }
        return mScreenWidth ;
    }

}
