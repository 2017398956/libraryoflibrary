package com.nfl.libraryoflibrary.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.nfl.libraryoflibrary.utils.LogTool;

public class PowerConnectionReceiver extends BroadcastReceiver {
    public PowerConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            // 是否在充电
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            LogTool.i(isCharging == true ? "正在充电" : "没有充电") ;
            // 怎么充
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            LogTool.i(usbCharge == false ? "交流电" : "直流电") ;
        } else if (Intent.ACTION_BATTERY_LOW.equals(action)) {

        } else if (Intent.ACTION_BATTERY_OKAY.equals(action)) {

        }
    }
}
