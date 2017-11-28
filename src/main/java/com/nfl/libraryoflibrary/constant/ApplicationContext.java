package com.nfl.libraryoflibrary.constant;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by fuli.niu on 2016/7/13.
 */
public class ApplicationContext {

    public static Context applicationContext = null ;
	// 非主界面都可以使用下面的用户信息
    public static String USERNAME = "" ;
    public static int USERID = 0 ;
    public static String DEVID = "" ;
    public static String USERNAMECH = "" ;
    public static String TOKEN = "";
    public static String EXPIRE_TIME = "";

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground() {
        if(null == applicationContext){
            return true ;
        }
        ActivityManager am = (ActivityManager) applicationContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(applicationContext.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}
