package com.nfl.libraryoflibrary.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;

import java.util.List;

public class SystemUtil {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static String IMEI;
    public static float DENSITY;
    public static String OsInfo;

    /**
     * 退出程序
     */
    public static void exitApplication() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 判断是不是平板
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断进程是否运行
     * @return
     */
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }
}