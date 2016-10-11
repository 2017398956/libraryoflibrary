package com.nfl.libraryoflibrary.view.traffic_float_window;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.utils.LogTool;

import java.util.List;

public class TrafficFloatWindowActivity extends ActionBarActivity {

	private ActivityManager mActivityManager;
	
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liuliang);
//		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//		mTextView = (TextView) findViewById(R.id.tv);
		LogTool.i("开启流量悬浮窗前");
		startService(new Intent(this , TrafficFloatWindowService.class)) ;
		LogTool.i("开启流量悬浮窗后");
	}
	
	private void updateProcessInfo() {
		String mText = "";
		mTextView.setText(mText);

		// 获取ActivityManager
		ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

		// 获取进程信息***************************************************
		List<RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();

		for (RunningAppProcessInfo info : infos) {
			String name = info.processName;
			int uid = info.uid;
			int pid = info.pid;

			mText = mTextView.getText().toString();
			mText += name + "\n\n";
			mTextView.setText(mText + " uid="+uid+" pid="+pid);

		}

	}
}
