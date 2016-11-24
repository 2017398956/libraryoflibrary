package com.nfl.libraryoflibrary.utils.pedometer;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nfl.libraryoflibrary.constant.ApplicationContext;
import com.nfl.libraryoflibrary.utils.LogTool;

public class ShutdownReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(final Context context, final Intent intent) {
        // 如何没接收到关机广播或数据库没迁移直接退出等下次启动时重新初始化（用户一般升级后不会关机），
        if (!"android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
            return;
        }
        // if the user used a root script for shutdown, the DEVICE_SHUTDOWN
        // broadcast might not be send. Therefore, the app will check this
        // setting on the next boot and displays an error message if it's not
        // set to true
        // 然而异常关机和正常安装app无法区分，此sp暂时无用
        context.getSharedPreferences("pedometer", Context.MODE_PRIVATE).edit()
                .putBoolean("correctShutdown", true).commit();
        // 接收到关机广播的时候SensorListener不应该再执行
        SensorListener.isShutdowning = true;
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
