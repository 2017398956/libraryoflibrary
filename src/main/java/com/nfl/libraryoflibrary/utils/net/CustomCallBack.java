package com.nfl.libraryoflibrary.utils.net;

import android.os.Handler;
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == RUN_ON_MAIN_THREAD) {
                runOnMainThread((T) msg.obj);
            } else if (msg.what == SUCCESS_ON_MAIN_THREAD) {
                successOnMainThread((String) msg.obj);
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
    public abstract void success(String result);

    @UiThread
    @Override
    public void successOnMainThread(String result) {

    }

    @Override
    @UiThread
    public void runOnMainThread(T t) {
    }

    @Override
    public void runOnSelfThread(T t) {

    }

    @Override
    public void onFailure(Call call, IOException e) {
        failure();
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
            Message msg = new Message();
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
                    msg.what = RUN_ON_MAIN_THREAD;
                    msg.obj = t;
                    handler.sendMessage(msg);
                }
            }
            if (canRunOnMainThread) {
                msg.what = SUCCESS_ON_MAIN_THREAD;
                msg.obj = resultTemp;
                handler.sendMessage(msg);
            }
            canRunOnMainThread = false;
        }else {
            // success("访问服务器失败-->com.nfl.libraryoflibrary.utils.net.CustomCallBack");
        }
    }
}
