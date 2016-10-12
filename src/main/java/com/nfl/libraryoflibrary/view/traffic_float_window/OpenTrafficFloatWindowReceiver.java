package com.nfl.libraryoflibrary.view.traffic_float_window;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.nfl.libraryoflibrary.utils.NetUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * 接收到通知后通过service启动流量悬浮窗
 */
public class OpenTrafficFloatWindowReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent intent = new Intent(context, TrafficFloatWindowService.class) ;
		intent.putExtra("isWifi" , NetUtils.isWifi(context)) ;
		context.startService(intent);
	}

}
