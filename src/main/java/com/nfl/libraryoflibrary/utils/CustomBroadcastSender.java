package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by fuli.niu on 2016/7/1.
 */
public class CustomBroadcastSender {

    // app 启动广播
    public static final String INTENT_ACTION_APP_START="cn.bill.app.gw.startapp";
    // 计步器开始计步广播
    private static final String INTENT_ACTION_PEDOMETER_START = "cn.bill.app.gw.pedometer.start" ;
    // 打开计步器界面广播
    public static final String INTENT_ACTION_START_PEDOMETER_ACTIVITY = "cn.bill.app.gw.action.startPedometerActivity" ;

    /**
     * 发送应用启动广播
     * @param context
     */
    public static void sendAppStartBroadCast(Context context){
        context.sendBroadcast(new Intent(INTENT_ACTION_APP_START));
        LogTool.i("AppStartBroadCastSended.") ;
//        context.sendBroadcast(new Intent(INTENT_ACTION_APP_START , );
    }

    /**
     * 发送计步器开始计步广播
     * @param context
     */
    public static void sendPedometerStartBroadCast(Context context){
        context.sendBroadcast(new Intent(INTENT_ACTION_PEDOMETER_START));
    }

    /**
     * 发送广播
     * @param context
     * @param action
     */
    public static void sendBroadcast(Context context , String action){
        Intent intent = new Intent(action) ;
        if(Build.VERSION.SDK_INT >= 12){
            intent.setFlags(32) ;// Intent.FLAG_INCLUDE_STOPPED_PACKAGES 版本3.1后才有
        }
        context.sendBroadcast(intent);
    }
}
