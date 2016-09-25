package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.constant.ApplicationContext;

/**
 * Created by fuli.niu on 2016/7/12.
 */
public class ToastTool {

    private static Toast toast ;
    private static Looper mainLooper ;
    private static Looper myLooper ;

    public static void showShortToast(String info){
        new ToastCreater(Toast.LENGTH_SHORT , info) ;
    }

    public static void showShortToast(int resId){
        new ToastCreater(Toast.LENGTH_SHORT , resId) ;
    }

    public static void showLongToast(String info){
        new ToastCreater(Toast.LENGTH_LONG , info) ;
    }

    public static void showCustomShortToast(String info){
        new ToastCreater(ToastType.customShortToast , Toast.LENGTH_SHORT , info) ;
    }

    public static void showCustomLongToast(String info){
        new ToastCreater(ToastType.customShortToast , Toast.LENGTH_LONG , info) ;
    }

    public static void testToast(){
        new ToastCreater(ToastType.testToast , Toast.LENGTH_SHORT , "") ;
    }

    public static void showCustomTimeToast(View view , int time){
            new ToastCreater(view , time) ;
    }

    private static class ToastCreater{

        private ToastCreater(){
            mainLooper = Looper.getMainLooper() ;
            myLooper = Looper.myLooper();
            if(mainLooper != myLooper){
                Looper.prepare();
                myLooper = Looper.myLooper();
            }
            if(null == ApplicationContext.applicationContext)
                return;
            if(null != toast){
                toast.cancel();
            }
        }

        public ToastCreater(int toastLength , String info){
            this() ;
            toast = Toast.makeText(ApplicationContext.applicationContext , info , toastLength) ;
            executeAndQuit();
        }

        public ToastCreater(int toastLength , int resId){
            this() ;
            toast = Toast.makeText(ApplicationContext.applicationContext ,
                    ApplicationContext.applicationContext.getResources().getText(resId) ,
                    toastLength) ;
            executeAndQuit();
        }

        public ToastCreater(ToastType toastType , int toastLength , String info){
            this() ;
            if(toastType == ToastType.customShortToast){
                toast = Toast.makeText(ApplicationContext.applicationContext , info , toastLength) ;
                ViewGroup v = (ViewGroup) toast.getView() ;
                v.setBackgroundResource(R.drawable.toast_bg);
                ((TextView) v.getChildAt(0)).setTextColor(Color.WHITE);
                int padding = ConvertTool.dp2px(5) ;
                v.setPadding(padding , padding , padding , padding);
            }else if(toastType == ToastType.testToast){
                TextView message = new TextView(ApplicationContext.applicationContext);
                message.setText("My Toast");
                message.setBackgroundResource(R.drawable.toast_bg);
                message.setPadding(10, 10, 10, 10);
                message.setTextColor(Color.WHITE);

                toast = new Toast(ApplicationContext.applicationContext);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(toastLength);
                toast.setView(message);
            }
            executeAndQuit();
        }

        public ToastCreater(View view , int time){
            this() ;
            if(null == view){
                CToast.makeText("自定义时长Toast" , time).show();
            }else {
                CToast cToast = CToast.makeText("" , time);
                cToast.setmNextView(view);
                cToast.show();
            }
            if(mainLooper != myLooper && null != myLooper){
                Looper.loop();
                if (Build.VERSION.SDK_INT >= 18) {
                    myLooper.quitSafely();
                }else {
                    myLooper.quit();
                }
            }
        }

        private void executeAndQuit(){
            toast.show();
            if(mainLooper != myLooper && null != myLooper){
                Looper.loop();
                if (Build.VERSION.SDK_INT >= 18) {
                    myLooper.quitSafely();
                }else {
                    myLooper.quit();
                }
            }
        }
    }

    private enum ToastType{
        customShortToast , testToast
    }

    private static class CToast {

        private final Handler mHandler = new Handler();
        private int mDuration = 2000 ;// 默认时长
        private int mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        private int mX = 0 , mY = 0 ;
        private float mHorizontalMargin = 0.0f ;
        private float mVerticalMargin = 0.0f ;

        private View mView;
        private View mNextView;
        private WindowManager mWM;
        private WindowManager.LayoutParams mParams ;

        private CToast() {
            mParams = new WindowManager.LayoutParams() ;
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.windowAnimations = android.R.style.Animation_Toast;
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mParams.setTitle("Toast");
            mWM = (WindowManager) ApplicationContext.applicationContext.getSystemService(Context.WINDOW_SERVICE);
        }

        public static CToast makeText(CharSequence text, int duration) {

            CToast result = new CToast();

            LinearLayout mLayout = new LinearLayout(ApplicationContext.applicationContext);

            TextView tv = new TextView(ApplicationContext.applicationContext);
            tv.setText(text);
            tv.setTextColor(Color.BLACK);
            tv.setPadding(30, 20, 30, 20);
            tv.setGravity(Gravity.CENTER);

            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(10);//边角
            // gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);//矩形
            gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            gradientDrawable.setColor(Color.YELLOW);//填充色
            gradientDrawable.setStroke(3, Color.WHITE);//描边

            if (Build.VERSION.SDK_INT >= 16) {
                mLayout.setBackground(gradientDrawable);
            }
            mLayout.addView(tv);

            result.mNextView = mLayout;
            result.mDuration = duration;
            return result;
        }

        /**
         * schedule handleShow into the right thread
         */
        public void show() {
            mHandler.post(mShow);
            if (mDuration > 0) {
                mHandler.postDelayed(mHide, mDuration);
            }
        }

        private final Runnable mShow = new Runnable() {
            public void run() {
                handleShow();
            }
        };

        private final Runnable mHide = new Runnable() {
            public void run() {
                handleHide();
            }
        };

        private void handleShow() {
            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
                mParams.gravity = mGravity;
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                mWM.addView(mView, mParams);
            }
        }

        private void handleHide() {
            if (mView != null) {
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                mView = null;
            }
        }

        public void setmNextView(View mNextView) {
            this.mNextView = mNextView;
        }
    }
}
