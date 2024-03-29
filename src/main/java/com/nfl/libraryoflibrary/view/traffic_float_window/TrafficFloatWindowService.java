package com.nfl.libraryoflibrary.view.traffic_float_window;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.Nullable;
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
import com.nfl.libraryoflibrary.utils.ExceptionTool;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.ToastTool;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 流量悬浮窗服务
 */
public class TrafficFloatWindowService extends Service {

    // 定义浮动窗口布局
    public RelativeLayout mFloatLayout;
    private RelativeLayout rl_delete;
    private ImageView iv_traffic_type;
    public LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    public WindowManager mWindowManager;
    public TextView mFloatView;
    float preX = 0f, preY = 0f;// 触摸流量悬浮窗的位置
    int pX = 0, pY = 0;// 流量悬浮窗相对于屏幕的位置

    private Timer timer;
    private TimerTask timerTask;
    private boolean isWifi = false;// 网络状态是否是wifi
    private ConnectivityManager cm;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (null != mFloatView) {
                    mFloatView.setText(TrafficInfo.getNetSpeed2String());
                    if (null != iv_traffic_type) {
                        iv_traffic_type.setImageResource(isWifi() ?
                                R.drawable.lol_icon_wifi : R.drawable.lol_icon_liuliang
                        );
                    }
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == mFloatLayout) {
            createFloatView();
        }
        if (null != iv_traffic_type) {
            // 根据系统广播切换图标，Nexus6p Android7.0 接收不到网络状态改变广播
            // 程序被内存清理工具直接清理后会NPE所以不使用该方法
//            iv_traffic_type.setImageResource(intent.getBooleanExtra("isWifi" , false) ?
//                    R.drawable.lol_icon_wifi : R.drawable.lol_icon_liuliang
//            );
        } else {
        }
        return START_STICKY_COMPATIBILITY;
    }

    /**
     * 创建流量悬浮窗
     */
    private void createFloatView() {
        destroyTrafficFloatWindow();
        initTrafficFloatWindowParams();
        initTrafficFloatWindowView();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            } else {
                //Android6.0以上
                LogTool.i("6.0 以上的悬浮窗");
                mWindowManager.addView(mFloatLayout, wmParams);// 添加mFloatLayout
            }
        } else {
            //Android6.0以下，不用动态声明权限
            try {
                LogTool.i("6.0 以下的悬浮窗");
                mWindowManager.addView(mFloatLayout, wmParams);// 添加mFloatLayout
            } catch (Exception e) {
                LogTool.i(ExceptionTool.getExceptionTraceString(e));
                ToastTool.showShortToast("没有悬浮窗权限");
            }
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
    private void destroyTrafficFloatWindow() {
        if (mFloatLayout != null && mWindowManager != null) {
            mWindowManager.removeView(mFloatLayout);
            mFloatLayout = null;
        }
    }

    /**
     * 初始化流量悬浮窗参数
     */
    private void initTrafficFloatWindowParams() {
        wmParams = new LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦
        // （实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;// 默认位置：左上角
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;
        // 设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;
    }

    /**
     * 初始化流量悬浮窗视图
     */
    private void initTrafficFloatWindowView() {
        // 获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.float_layout, null);
        rl_delete = (RelativeLayout) mFloatLayout.findViewById(R.id.rl_delete);
        iv_traffic_type = (ImageView) mFloatLayout.findViewById(R.id.iv_traffic_type);
        mFloatView = (TextView) mFloatLayout.findViewById(R.id.speed);
//        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
            }
        });
        // 设置监听流量悬浮窗的点击事件
        mFloatLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                rl_delete.setVisibility(rl_delete.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                rl_delete.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rl_delete.setVisibility(View.GONE);
                    }
                }, 3000);
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

    /**
     * 判断网络类型是否是WIFI
     *
     * @return
     */
    private boolean isWifi() {
        isWifi = false;// 默认为false;原因见下面注释
        if (null == cm) {
            // 先尝试获得系统服务
            cm = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (cm == null) {
            // 获取不到CONNECTIVITY_SERVICE时返回false
            isWifi = false;
        } else {
            try {
                // cm.getActiveNetworkInfo()在Nexus6p Android7.0上由wifi切换到手机流量时返回为null，所以isWifi默认为false
                isWifi = cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
            } catch (Exception e) {
            }
        }
        return isWifi;
    }
}