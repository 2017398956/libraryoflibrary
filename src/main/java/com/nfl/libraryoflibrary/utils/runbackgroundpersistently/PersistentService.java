package com.nfl.libraryoflibrary.utils.runbackgroundpersistently;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import com.nfl.libraryoflibrary.utils.LogTool;

import java.util.Timer;
import java.util.TimerTask;

public class PersistentService extends Service {

    private Timer timer ;
    private TimerTask timerTask ;
    public PersistentService() {
        LogTool.i("PersistentService");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                LogTool.i("PersistentService log") ;
            }
        } ;
        timer = new Timer() ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // intent 有可能为null
        LogTool.i("PersistentService onStartCommand. And intent == null ? " + (intent == null) );
        LogTool.i(timer + " | " + timerTask) ;
//        timer.schedule(timerTask , 0 , 2000);
        return Service.START_STICKY ;
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogTool.i("PersistentService onBind");
        return null ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.i("PersistentService onDestroy");
        Intent intent = new Intent(getApplicationContext() , PersistentService.class) ;
        startService(intent) ;
    }

    /**
     * 提升Service的优先级为前台Service<br />
     * 版本适用到6.0
     */
    private void setForeGround(final Service persistentService , Service innerService){
        final int foreGroundPushId = 1 ;
        if(persistentService != null){
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                persistentService.startForeground(foreGroundPushId , new Notification());
            }else {
                persistentService.startForeground(foreGroundPushId , new Notification());
                if(innerService != null){
                    innerService.startForeground(foreGroundPushId , new Notification());
                    innerService.stopSelf();
                }
            }
        }
    }

}
