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

import com.nfl.libraryoflibrary.utils.LogTool;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class SensorListener extends Service implements SensorEventListener {

    private Database db ;
    public static int steps = 0 ;
    private final static int MICROSECONDS_IN_ONE_MINUTE = 60000000;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
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
        db = Database.getInstance(this) ;
        long today = Util4Pedometer.getToday() ;
        long yesterDay = today - 24 * 60 * 60 * 1000 ;
        int correctSensorSteps ;
        int off = db.getOffStatus(yesterDay) ;
        if( off == 0) {
            // 说明昨天没有关机
            correctSensorSteps = steps - db.getSensorSteps(yesterDay) ;
            db.updateSensorSteps(today , correctSensorSteps);
        }else if(off == 1){
            // 说明昨天关机
            correctSensorSteps = steps ;
            db.updateSensorSteps(today , correctSensorSteps);
        }else{
            LogTool.i("计步器异常") ;
        }
        StepsCountTool.getTodaySteps() ;
    }

    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        db = Database.getInstance(this);
        db.updateOffStatus(Util4Pedometer.getToday() , 0);
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
        if(null != db){
            db.close();
        }
        try {
            SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(19)
    private void reRegisterSensor() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            sm.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_NORMAL, 5 * MICROSECONDS_IN_ONE_MINUTE);
    }
}
