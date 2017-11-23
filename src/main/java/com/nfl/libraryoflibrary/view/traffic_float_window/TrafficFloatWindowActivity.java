package com.nfl.libraryoflibrary.view.traffic_float_window;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;

import java.util.List;

public class TrafficFloatWindowActivity extends Activity {

    private ActivityManager mActivityManager;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liuliang);
        startService(new Intent(this, TrafficFloatWindowService.class));
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
            mTextView.setText(mText + " uid=" + uid + " pid=" + pid);

        }

    }
}
