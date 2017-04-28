package com.nfl.libraryoflibrary.utils.net;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.nfl.libraryoflibrary.utils.ExceptionTool;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public abstract class CustomCallBack<T> implements CustomCallBackInterface<T>, Callback {

    private boolean canRunOnMainThread = false;
    private Class<T> clz;
    private final int RUN_ON_MAIN_THREAD = 1;
    private final int SUCCESS_ON_MAIN_THREAD = 2;
    private final int FAILURE_ON_MAIN_THREAD = 3 ;
    private Looper myLooper ;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == RUN_ON_MAIN_THREAD) {
                LogTool.i("runOnMainThread start") ;
                runOnMainThread((T) msg.obj);
            } else if (msg.what == SUCCESS_ON_MAIN_THREAD) {
                LogTool.i("successOnMainThread start") ;
                successOnMainThread((String) msg.obj);
            } else if (msg.what == FAILURE_ON_MAIN_THREAD){
                LogTool.i("failureOnMainThread start") ;
                failureOnMainThread();
            }
            if(null != myLooper && myLooper != Looper.getMainLooper()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    // myLooper.quitSafely();
                    myLooper.quit();
                }else {
                    myLooper.quit();
                }
                // myLooper = null ;
            }
        }
    };

    public CustomCallBack() {
        canRunOnMainThread = false;
    }

    public CustomCallBack(boolean canRunOnMainThread) {
        this.canRunOnMainThread = canRunOnMainThread;
    }

    public CustomCallBack(Class<T> clz) {
        this(true, clz);
    }

    public CustomCallBack(boolean canRunOnMainThread, Class<T> clz) {
        this.canRunOnMainThread = canRunOnMainThread;
        this.clz = clz;
    }

    public boolean dismissProgressDialog(){
        // TODO 改变自定义 ProgressDialog 是否自动消失，默认自动消失
        return true ;
    }

    @Override
    public abstract void failure();

    @Override
    public void failureOnMainThread() {
    }

    @Override
    public abstract void success(String result);

    @Override
    public void successOnMainThread(String result) {

    }

    @Override
    public void runOnMainThread(T t) {
    }

    @Override
    public void runOnSelfThread(T t) {

    }

    @Override
    public void onFailure(Call call, IOException e) {
        failure();
        if (null == Looper.myLooper() || Looper.myLooper() != Looper.getMainLooper()) {
            LogTool.i("onFailure 方法在非 UI 线程");
            looperPrepare();
            Message msg3 = new Message();
            msg3.what = FAILURE_ON_MAIN_THREAD;
            handler.sendMessage(msg3);
        Looper.loop();
        LogTool.i("跳出 CustomCallBack Looper 3") ;
        } else if (Looper.myLooper() == Looper.getMainLooper()) {
            LogTool.i("onFailure 方法在 UI 线程");
            failureOnMainThread();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (null == Looper.myLooper() || Looper.myLooper() != Looper.getMainLooper()) {
            LogTool.i("onResponse 方法在非 UI 线程");
        } else if (Looper.myLooper() == Looper.getMainLooper()) {
            LogTool.i("onResponse 方法在 UI 线程");
        }
        if(dismissProgressDialog()){
        CustomProgressBarDialog.dimissProgressBarDialog();
        }
        if(response.isSuccessful()) {
        String resultTemp = response.body().string() ;
		CustomProgressBarDialog.dimissProgressBarDialog();
        success(resultTemp);
        if(null == resultTemp || "".equals(resultTemp)){
            return;
        }
            
            if (clz != null) {
                Gson gson = new Gson();
                T t = null ;
                try{
                    // gson 转换可能会发生异常
                    t = gson.fromJson(resultTemp, clz);
                } catch (Exception e){
                    LogTool.i(ExceptionTool.getExceptionTraceString(e));
                }
                runOnSelfThread(t);
                if (canRunOnMainThread) {
                    looperPrepare();
                    Message msg1 = new Message();
                    msg1.what = RUN_ON_MAIN_THREAD;
                    msg1.obj = t;
                    handler.sendMessage(msg1);
                    Looper.loop();
                    LogTool.i("跳出 CustomCallBack Looper 1") ;
                }
            }
            if (canRunOnMainThread) {
                looperPrepare();
                Message msg2 = new Message();
                msg2.what = SUCCESS_ON_MAIN_THREAD;
                msg2.obj = resultTemp;
                handler.sendMessage(msg2);
                Looper.loop();
                LogTool.i("跳出 CustomCallBack Looper 2") ;
            }
            canRunOnMainThread = false;
        }else {
            // success("访问服务器失败-->com.nfl.libraryoflibrary.utils.net.CustomCallBack");
        }
    }

    /**
     * 有时 myLooper 为 null 但 ，已经执行过 Looper.prepare() ;
     * TODO 找出造成这种现象的原因
     */
    private void looperPrepare() {
        try {
            if(null == myLooper){
                Looper.prepare();
                myLooper = Looper.myLooper();
            }
        } catch (Exception e) {
            LogTool.i("Looper 初始化异常:" + ExceptionTool.getExceptionTraceString(e));
        } finally {
            myLooper = Looper.myLooper();
        }
    }
}
