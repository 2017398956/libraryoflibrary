package com.nfl.libraryoflibrary.view.traffic_float_window;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 流量悬浮窗服务
 */
public class TrafficFloatWindowService extends Service {

    // 定义浮动窗口布局
    public RelativeLayout mFloatLayout;
    private RelativeLayout rl_delete ;
    private ImageView iv_traffic_type ;
    public LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    public WindowManager mWindowManager;
    public TextView mFloatView;
    float preX = 0f , preY = 0f ;// 触摸流量悬浮窗的位置
    int pX = 0 , pY = 0 ;// 流量悬浮窗相对于屏幕的位置

    private Timer timer ;
    private TimerTask timerTask ;

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        } ;
        timer = new Timer() ;
//        timer.
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY_COMPATIBILITY;
    }

    /**
     * 创建流量悬浮窗
     */
    private void createFloatView() {
        destroyTrafficFloatWindow();
        initTrafficFloatWindowParams() ;
        initTrafficFloatWindowView() ;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                startActivity(intent);
                return;
            } else {
                //Android6.0以上
                mWindowManager.addView(mFloatLayout, wmParams);// 添加mFloatLayout
            }
        } else {
            //Android6.0以下，不用动态声明权限
            mWindowManager.addView(mFloatLayout, wmParams);// 添加mFloatLayout
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, TrafficFloatWindowService.class));
    }

    /**
     * 移除流量悬浮窗
     */
    private void destroyTrafficFloatWindow(){
        if (mFloatLayout != null && mWindowManager != null) {
            mWindowManager.removeView(mFloatLayout) ;
        }
    }

    /**
     * 初始化流量悬浮窗参数
     */
    private void initTrafficFloatWindowParams(){
        wmParams = new LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦
        // （实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 默认位置：左上角
        wmParams.width = LayoutParams.WRAP_CONTENT ;
        wmParams.height = LayoutParams.WRAP_CONTENT;
        // 设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
    }

    /**
     * 初始化流量悬浮窗视图
     */
    private void initTrafficFloatWindowView(){
        // 获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.float_layout, null);
        rl_delete = (RelativeLayout) mFloatLayout.findViewById(R.id.rl_delete) ;
        iv_traffic_type = (ImageView) mFloatLayout.findViewById(R.id.iv_traffic_type) ;
        mFloatView = (TextView) mFloatLayout.findViewById(R.id.speed);
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                int action = event.getAction() ;
                if(action == MotionEvent.ACTION_DOWN){
                    preX = event.getRawX() ;
                    preY = event.getRawY() ;
                    pX = wmParams.x ;
                    pY = wmParams.y ;
                }else if(action == MotionEvent.ACTION_MOVE){
                    wmParams.x = pX + (int) (event.getRawX() - preX) ;
                    wmParams.y = pY + (int) (event.getRawY() - preY) ;
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);// 刷新
                }
                return false; // 此处必须返回false，否则OnClickListener获取不到监听
            }
        });
        // 设置监听流量悬浮窗的点击事件
        mFloatLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                rl_delete.setVisibility(rl_delete.getVisibility() == View.GONE ? View.VISIBLE : View.GONE );
            }
        });
        rl_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyTrafficFloatWindow();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}