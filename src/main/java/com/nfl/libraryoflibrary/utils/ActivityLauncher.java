package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by fuli.niu on 2016/7/11.
 */
public class ActivityLauncher {

    public static final String pedometerActivity = "pedometerActivity" ;

    private static Intent intent = new Intent() ;

    public static void launcher(Context context , Class<?> activity){
        intent.setClass(context , activity) ;
        context.startActivity(intent);
    }
}
