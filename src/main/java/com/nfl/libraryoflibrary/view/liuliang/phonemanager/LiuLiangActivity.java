package com.nfl.libraryoflibrary.view.liuliang.phonemanager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.view.liuliang.utils.TrafficInfo;

import java.util.List;

public class LiuLiangActivity extends ActionBarActivity {

	private ActivityManager mActivityManager;
	
	private TextView mTextView;
	
	Handler mHandler;
	TrafficInfo speed;
	
	ManagerService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liuliang);
		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		mTextView = (TextView) findViewById(R.id.tv);
		
		try {
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 1) {
						mTextView.setText(msg.obj + "kb/s");
						if(service != null)
							service.setSpeed(msg.obj+"kb/s");
					}
					super.handleMessage(msg);
				}

			};
			speed = new TrafficInfo(this,mHandler,10035);
			speed.startCalculateNetSpeed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Log.i("test","总流量="+speed.getTrafficInfo());
		
		Intent intent = new Intent(LiuLiangActivity.this, ManagerService.class);
		// startService(intent);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
		// startService(new Intent(this, GuardService.class));
		
	}
	
	private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder binder) {
        	Log.i("test","-------------------");
        	service = ((ManagerService.ServiceBinder) binder).getService();
        	
        }

        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
        	service = null;
        }

    };
	
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		speed.stopCalculateNetSpeed();
		unbindService(conn); 
	}
}
