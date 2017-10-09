package com.nfl.libraryoflibrary.utils.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public abstract class CustomCallBack4Download<T> implements CustomCallBackInterface<T>, Callback {

    private final int SUCCESS_ON_MAIN_THREAD = 2;
    private final int FAILURE_ON_MAIN_THREAD = 3;
    private final int FINALLY_ON_MAIN_THREAD = 4;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUCCESS_ON_MAIN_THREAD) {
//                LogTool.i("successOnMainThread start");
                successOnMainThread((String) msg.obj);
            } else if (msg.what == FAILURE_ON_MAIN_THREAD) {
//                LogTool.i("failureOnMainThread start");
                failureOnMainThread();
            } else if (msg.what == FINALLY_ON_MAIN_THREAD) {
//                LogTool.i("finallyOnMainThread start");
                finallyOnMainThread();
            }
        }
    };

    public boolean dismissProgressDialog() {
        // TODO 改变自定义 ProgressDialog 是否自动消失，默认自动消失
        return true;
    }

    @Override
    public abstract void failure();

    @Override
    public void failureOnMainThread() {
    }

    @Override
    public abstract void success(String result);

    public void success(InputStream inputStream) {

    }

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
        Message msg3 = new Message();
        msg3.what = FAILURE_ON_MAIN_THREAD;
        handler.sendMessage(msg3);
    }

    @Override
    public void finallyOnMainThread() {
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (dismissProgressDialog()) {
            CustomProgressBarDialog.dimissProgressBarDialog();
        }
        if (response.isSuccessful()) {
            CustomProgressBarDialog.dimissProgressBarDialog();
            success(response.body().byteStream());
        } else {
            // success("访问服务器失败-->com.nfl.libraryoflibrary.utils.net.CustomCallBack");
        }
        handler.sendEmptyMessage(FINALLY_ON_MAIN_THREAD);
    }

    @Override
    public void cancel() {

    }
}
