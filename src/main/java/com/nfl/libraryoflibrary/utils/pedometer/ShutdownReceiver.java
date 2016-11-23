package com.nfl.libraryoflibrary.utils.pedometer;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nfl.libraryoflibrary.utils.LogTool;

public class ShutdownReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(final Context context, final Intent intent) {
        context.startService(new Intent(context, SensorListener.class));
        Database db = Database.getInstance(context);
        long today = Util4Pedometer.getToday();
        long yesterDay = today - 24 * 60 * 60 * 1000;
        int correctSensorSteps = 0;
        int off = db.getOffStatus(yesterDay);
        if (off == 0) {
            // 说明昨天没有关机
            correctSensorSteps = SensorListener.steps - db.getSensorSteps(yesterDay);
            db.updateSteps(today, correctSensorSteps);
        } else if (off == 1) {
            // 说明昨天关机,计步器清零了
            correctSensorSteps = SensorListener.steps;
            db.updateSteps(today, correctSensorSteps);
        } else {
            LogTool.i("计步器异常");
        }
        db.updateOffStatus(today, 1);
        db.updateSensorSteps(today, 0);
        db.close();
    }
}
