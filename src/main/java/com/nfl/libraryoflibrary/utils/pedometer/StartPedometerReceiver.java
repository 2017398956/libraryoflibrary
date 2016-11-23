package com.nfl.libraryoflibrary.utils.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartPedometerReceiver extends BroadcastReceiver {

    public StartPedometerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(null != context){
            context.startService(new Intent(context , SensorListener.class)) ;
        }
    }
}
