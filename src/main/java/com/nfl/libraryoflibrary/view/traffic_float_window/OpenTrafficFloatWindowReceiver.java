package com.nfl.libraryoflibrary.view.traffic_float_window;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接收到通知后通过service启动流量悬浮窗
 */
public class OpenTrafficFloatWindowReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		context.startService(new Intent(context, TrafficFloatWindowService.class));
	}

}
