package com.nfl.libraryoflibrary.utils.net;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.UiThread;

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
                    myLooper.quitSafely();
                }else {
                    myLooper.quit();
                }
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
        if(null == myLooper){
            Looper.prepare();
            myLooper = Looper.myLooper() ;
        }
        Message msg = new Message();
        msg.what = FAILURE_ON_MAIN_THREAD;
        handler.sendMessage(msg);
        Looper.loop();
        LogTool.i("跳出 CustomCallBack Looper 3") ;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        if(dismissProgressDialog()){
        CustomProgressBarDialog.dimissProgressBarDialog();
        }
        if(response.isSuccessful()) {
        String resultTemp = response.body().string() ;
        if(null == resultTemp || "".equals(resultTemp)){
            return;
        }
            CustomProgressBarDialog.dimissProgressBarDialog();
        success(resultTemp);
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
                    Looper.prepare();
                    Message msg = new Message();
                    myLooper = Looper.myLooper() ;
                    msg.what = RUN_ON_MAIN_THREAD;
                    msg.obj = t;
                    handler.sendMessage(msg);
                    Looper.loop();
                    LogTool.i("跳出 CustomCallBack Looper 1") ;
                }
            }
            if (canRunOnMainThread) {
                if(null == myLooper){
                    Looper.prepare();
                    myLooper = Looper.myLooper() ;
                }
                Message msg = new Message();
                msg.what = SUCCESS_ON_MAIN_THREAD;
                msg.obj = resultTemp;
                handler.sendMessage(msg);
                Looper.loop();
                LogTool.i("跳出 CustomCallBack Looper 2") ;
            }
            canRunOnMainThread = false;
        }else {
            // success("访问服务器失败-->com.nfl.libraryoflibrary.utils.net.CustomCallBack");
        }
    }
}
