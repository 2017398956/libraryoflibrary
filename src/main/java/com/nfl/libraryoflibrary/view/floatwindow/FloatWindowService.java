package com.nfl.libraryoflibrary.view.floatwindow;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import personal.nfl.permission.annotation.GetPermissions;

public class FloatWindowService extends Service {

    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;

    /**
     * 是否只在桌面显示 , 默认为true ，其具体取值根据用户的配置文件设置
     */
    private boolean isOnlyShowOnDesktop = true;

    private SharedPreferences sp = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * 开启服务的时候先加载配置文件
         */
        sp = getSharedPreferences("PersonalSettings", MODE_PRIVATE);
        isOnlyShowOnDesktop = sp.getBoolean("isOnlyShowOnDesktop", true);

        // 开启定时器，每隔0.5秒刷新一次
        openFloatWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    // @GetPermissions({Manifest.permission.SYSTEM_ALERT_WINDOW})
    private void openFloatWindow() {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {

            // 创建悬浮窗。（逻辑看else比较清楚）
            if (!isOnlyShowOnDesktop || isHome()) {
                if (!MyWindowManager.isWindowShowing()) {
                    // 还不存在悬浮窗，则创建悬浮窗
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyWindowManager
                                    .createSmallWindow(getApplicationContext());

                        }
                    });
                } else {
                    // 已有悬浮窗，则更新悬浮窗
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyWindowManager
                                    .updateUsedPercent(getApplicationContext());
                        }
                    });
                }
            } else {
                // 仅在桌面显示，但当前不是桌面，则移除悬浮窗。
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager
                                .removeSmallWindow(getApplicationContext());
                        MyWindowManager
                                .removeBigWindow(getApplicationContext());
                    }
                });
            }
        }
    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // return A android.app.ActivityManager for interacting with the global
        // activity state of the system.
        // 获得当前活跃度最高的任务并于桌面应用包名进行比较，如果相同，则当前界面为桌面
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
