package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PermissionUtil {

    public final static int REQUEST_FLOAT_WINDOW_PERMISSION = 10001;

    /**
     * 判断是否开启悬浮窗权限
     */
    public static boolean checkFloatPermission(Context context) {
        boolean canDrawOverlays;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            // Android 4.4 以下视为有权限
            canDrawOverlays = true;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Android 6.0 以下通过反射判断是否有权限
            try {
                Class<?> cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    canDrawOverlays = false;
                } else {
                    String str2 = (String) obj;
                    obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                    cls = Class.forName("android.app.AppOpsManager");
                    Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                    declaredField2.setAccessible(true);
                    Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                    int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                    canDrawOverlays = result == declaredField2.getInt(cls);
                }
            } catch (Exception e) {
                canDrawOverlays = false;
            }
        } else {
            canDrawOverlays = Settings.canDrawOverlays(context);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//                if (appOpsMgr == null) {
//                    canDrawOverlays = false;
//                } else {
//                    int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
//                            .getPackageName());
//                    canDrawOverlays = mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
//                }
//            }
        }
        return canDrawOverlays;
    }

    /**
     * 权限打开
     */
    public static void requestSettingCanDrawOverlays(Activity activity) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {//8.0以上
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            activity.startActivityForResult(intent, REQUEST_FLOAT_WINDOW_PERMISSION);
        } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, REQUEST_FLOAT_WINDOW_PERMISSION);
        } else {//4.4-6.0以下
            //无需处理了
        }
    }
}
