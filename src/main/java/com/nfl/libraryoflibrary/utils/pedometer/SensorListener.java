package com.nfl.libraryoflibrary.utils.pedometer;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;

import com.nfl.libraryoflibrary.constant.ApplicationContext;
import com.nfl.libraryoflibrary.utils.LogTool;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class SensorListener extends Service implements SensorEventListener {

    public static boolean isShutdowning = false;// 只有关机时才触发
    private Database db;
    public static int steps = 0;
    private final static int MICROSECONDS_IN_ONE_MINUTE = 60000000;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        // 关机时和没有进行数据库迁移都不往下执行
        if (isShutdowning) {
            return;
        }
		// 若步数异常(用户随意调整时间后，开关机)，修复步数
        StepsCountTool.getTodaySteps();
        LogTool.i("================is not TYPE_STEP_COUNTER?=======================");
        Sensor sensor = event.sensor;
        if (sensor.getType() != Sensor.TYPE_STEP_COUNTER) {
            LogTool.i("is not TYPE_STEP_COUNTER=======================");
            return;
        } else {
            int length = event.values.length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    LogTool.i(event.timestamp + "x" + i + ":" + event.values[i]);
                }
            }
        }
        steps = (int) event.values[0];
        // PedometerConstant.stepsFromSensor = steps ;
        db = Database.getInstance(this);
        long today = Util4Pedometer.getToday();
        long yesterDay = today - 24 * 60 * 60 * 1000;
        int correctSensorSteps;
        int off = db.getOffStatus(yesterDay);
        if (off == 0) {
            // 说明昨天没有关机
            correctSensorSteps = steps - db.getSensorSteps(yesterDay);
            if (correctSensorSteps < 0) {
                // 说明手机异常关机：扣电池或使用脚本关机导致关机广播没有发出
                db.updateOffStatus(yesterDay, 1);
				// 异常关机后重启先保存关机前的数据
                db.updateSteps(today , db.getSensorSteps(today));
                // 再重新计算步数，这样能最大可能的接近真实值
                db.updateSensorSteps(today, steps);
                LogTool.i("手机异常关机") ;
            } else {
                if(db.getSensorSteps(today) <= 0){
                    db.updateSteps(today , - correctSensorSteps);
                    LogTool.i("这里应该只更新一次Sensor" + correctSensorSteps) ;
                }
            db.updateSensorSteps(today, correctSensorSteps);

            }
        } else if (off == 1) {
            // 说明昨天关机
            correctSensorSteps = steps;
            db.updateSensorSteps(today, correctSensorSteps);
        } else {
            LogTool.i("计步器异常");
        }
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (isShutdowning) {
            return START_STICKY;
        }
        db = Database.getInstance(this);
        db.updateOffStatus(Util4Pedometer.getToday(), 0);
        // restart service every hour to get the current step count
        ((AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE))
                .set(AlarmManager.RTC, System.currentTimeMillis() + AlarmManager.INTERVAL_HOUR,
                        PendingIntent.getService(getApplicationContext(), 2,
                                new Intent(this, SensorListener.class),
                                PendingIntent.FLAG_UPDATE_CURRENT));
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reRegisterSensor();
    }

    @Override
    public void onTaskRemoved(final Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // Restart service in 500 ms
        ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                .set(AlarmManager.RTC, System.currentTimeMillis() + 500, PendingIntent
                        .getService(this, 3, new Intent(this, SensorListener.class), 0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != db) {
            db.close();
        }
        try {
            SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reRegisterSensor();
    }

    @TargetApi(19)
    private void reRegisterSensor() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(null == sm){
            LogTool.i("无法获得传感器管理组件") ;
        }else{
            LogTool.i("获得传感器管理组件") ;
        }
        try {
            sm.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)){
            LogTool.i("没有计步器") ;
        }else{
            LogTool.i("有计步器") ;
        }
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_NORMAL, 5 * MICROSECONDS_IN_ONE_MINUTE);
    }
}
