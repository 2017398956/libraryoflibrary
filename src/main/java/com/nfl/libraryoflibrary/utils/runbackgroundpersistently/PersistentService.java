package com.nfl.libraryoflibrary.utils.runbackgroundpersistently;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PersistentService extends Service {
    public PersistentService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // intent 有可能为null
        return Service.START_STICKY ;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
