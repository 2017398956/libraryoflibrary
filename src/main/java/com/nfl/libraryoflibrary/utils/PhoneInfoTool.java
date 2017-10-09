package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.nfl.libraryoflibrary.constant.ApplicationContext;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by fuli.niu on 2016/8/5.
 */
public class PhoneInfoTool {
    private static int mScreenHeight = 0;
    private static int mScreenWidth = 0;

    /**
     * 获得屏幕高度（含状态栏）
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        if (mScreenHeight == 0) {
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
            mScreenHeight = localDisplayMetrics.heightPixels;
            mScreenWidth = localDisplayMetrics.widthPixels;
        }
        return mScreenHeight;
    }

    /**
     * 获得屏幕宽度
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        if (mScreenWidth == 0) {
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
            mScreenHeight = localDisplayMetrics.heightPixels;
            mScreenWidth = localDisplayMetrics.widthPixels;
        }
        return mScreenWidth;
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight() {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = ApplicationContext.applicationContext.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 得到电池的状态
     *
     * @param activity
     */
    public static void getBatteryStatus(Activity activity) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = activity.registerReceiver(null, ifilter);
        //你可以读到充电状态,如果在充电，可以读到是usb还是交流电
        // 是否在充电
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        LogTool.i(isCharging == true ? "正在充电" : "没有充电");
        // 怎么充
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        LogTool.i(usbCharge == false ? "交流电" : "直流电");
    }

    /**
     * 不同手机的不同LED颜色值不一样
     * @param activity
     */
    private void setLEDLightColor(Activity activity) {
        int ID_LED = 19871103;
        NotificationManager nm = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.ledARGB = 0x00FF00;  //这里是颜色，我们可以尝试改变，理论上0xFF0000是红色，0x00FF00是绿色
        notification.ledOnMS = 10000;
        notification.ledOffMS = 10000;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
//        notification.largeIcon = BitmapFactory.decodeResource(getResources()) ;
        nm.notify(ID_LED, notification);
//        nm.cancel(ID_LED);
    }

    public static DisplayMetrics getMetrics(Activity activity) {
        return getMetrics() ;
    }

    public static DisplayMetrics getMetrics() {
        WindowManager winMgr = (WindowManager) ApplicationContext.applicationContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        winMgr.getDefaultDisplay().getMetrics(metric);
        return metric;
    }
}
