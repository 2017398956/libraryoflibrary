package com.nfl.libraryoflibrary.view.db_insight;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.nfl.libraryoflibrary.R;

public class DBInsightService extends Service {

    // 定义浮动窗口布局
    public RelativeLayout mFloatLayout;
    public WindowManager.LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    public WindowManager mWindowManager;
    public TextView mFloatView;
    float preX = 0f, preY = 0f;// 触摸悬浮窗的位置
    int pX = 0, pY = 0;// 悬浮窗相对于屏幕的位置
    public static boolean canShowDBInsight = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == mFloatLayout) {
            if (canShowDBInsight) {
                createFloatView();
            } else {
                destroyFloatWindow();
            }

        }
        return START_STICKY_COMPATIBILITY;
    }

    /**
     * 创建流量悬浮窗
     */
    private void createFloatView() {
        destroyFloatWindow();
        initFloatWindowParams();
        initFloatWindowView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Android 6.0以上
                mWindowManager.addView(mFloatLayout, wmParams);// 添加 mFloatLayout
            }
        } else {
            // Android6.0 以下，不用动态声明权限
            mWindowManager.addView(mFloatLayout, wmParams);// 添加mFloatLayout
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, DBInsightService.class));
    }

    /**
     * 移除流量悬浮窗
     */
    private void destroyFloatWindow() {
        if (mFloatLayout != null && mWindowManager != null) {
            mWindowManager.removeView(mFloatLayout);
            mFloatLayout = null;
        }
    }

    /**
     * 初始化流量悬浮窗参数
     */
    private void initFloatWindowParams() {
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦
        // （实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 默认位置：左上角
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
    }

    /**
     * 初始化流量悬浮窗视图
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initFloatWindowView() {
        // 获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.dbinsight_layout, null);
        mFloatView = mFloatLayout.findViewById(R.id.speed);
//        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener((v, event) -> {
            // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                preX = event.getRawX();
                preY = event.getRawY();
                pX = wmParams.x;
                pY = wmParams.y;
            } else if (action == MotionEvent.ACTION_MOVE) {
                wmParams.x = pX + (int) (event.getRawX() - preX);
                wmParams.y = pY + (int) (event.getRawY() - preY);
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
            }
            return false; // 此处必须返回false，否则OnClickListener获取不到监听
        });
        // 设置监听流量悬浮窗的点击事件
        mFloatLayout.setOnClickListener(arg0 -> {
            // 跳转到数据库列表界面
            Intent intent = new Intent(DBInsightService.this, DBInsightActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
