package com.nfl.libraryoflibrary.utils.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nfl.libraryoflibrary.utils.LogTool;

public class StartPedometerReceiver extends BroadcastReceiver {

    public StartPedometerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogTool.i("新的计步器启动1") ;
        if(null != context){
            LogTool.i("新的计步器启动2") ;
            context.startService(new Intent(context , SensorListener.class)) ;
        }
    }
}
