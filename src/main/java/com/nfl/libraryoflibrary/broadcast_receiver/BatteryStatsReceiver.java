package com.nfl.libraryoflibrary.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nfl.libraryoflibrary.utils.LogTool;

public class BatteryStatsReceiver extends BroadcastReceiver {
    public BatteryStatsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            //得到系统当前电量
            int level = intent.getIntExtra("level", 0);
            //取得系统总电量
            int total = intent.getIntExtra("scale", 100);
            LogTool.i("当前电量：" + (level * 100) / total + "%");
        } else if (Intent.ACTION_BATTERY_LOW.equals(action)) {

        } else if (Intent.ACTION_BATTERY_OKAY.equals(action)) {

        }
    }
}
