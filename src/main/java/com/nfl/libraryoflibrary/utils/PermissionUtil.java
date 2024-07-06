package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
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

    /**
     * 申请管理所有文件的权限
     * 需要和 {@link android.Manifest.permission#MANAGE_EXTERNAL_STORAGE} 一起使用
     *
     * @param activity
     * @param requestCode
     * @return true 有权限；false 没有权限，需要在 {@link Activity#onActivityResult(int, int, Intent)} 中处理
     */
    public static boolean requestManagerAllFiles(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                // ACTION_APPLICATION_DETAILS_SETTINGS
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                activity.startActivityForResult(intent, requestCode);
                return false;
            }
        }
        return true;
    }

    /**
     * 请求安装 apk 权限
     * 需要和 {@link android.Manifest.permission#REQUEST_INSTALL_PACKAGES}一起使用
     * @param activity
     * @param requestCode
     * @return true 有权限；false 没有权限，需要在 {@link Activity#onActivityResult(int, int, Intent)} 中处理
     */
    public static boolean requestInstallApk(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean haveInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                activity.startActivityForResult(intent, requestCode);
//                activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                    if (result.getResultCode() == RESULT_OK) {
//                        Toast.makeText(this, "已授权安装 APK", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(this, "未授权安装 APK", Toast.LENGTH_SHORT).show();
//                    }
//                }).launch(intent);
                return false;
            }
        }
        return true;
    }
}
