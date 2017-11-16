package com.nfl.libraryoflibrary.utils.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.nfl.libraryoflibrary.utils.ExceptionTool;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.view.CustomProgressBarDialog;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by fuli.niu on 2016/7/8.
 */
public abstract class CustomCallBack<T> implements CustomCallBackInterface<T>, Callback {

    private Class<T> clz;
    private final int RUN_ON_MAIN_THREAD = 1;
    private final int SUCCESS_ON_MAIN_THREAD = 2;
    private final int FAILURE_ON_MAIN_THREAD = 3;
    private final int FINALLY_ON_MAIN_THREAD = 4;
    private final int AUXILIARY_METHOD_ON_MAIN_THREAD = 5;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == RUN_ON_MAIN_THREAD) {
                LogTool.i("runOnMainThread start");
                runOnMainThread((T) msg.obj);
            } else if (msg.what == SUCCESS_ON_MAIN_THREAD) {
                LogTool.i("successOnMainThread start");
                successOnMainThread((String) msg.obj);
            } else if (msg.what == FAILURE_ON_MAIN_THREAD) {
                LogTool.i("failureOnMainThread start");
                failureOnMainThread();
            } else if (msg.what == FINALLY_ON_MAIN_THREAD) {
                LogTool.i("finallyOnMainThread start");
                finallyOnMainThread();
            } else if (msg.what == AUXILIARY_METHOD_ON_MAIN_THREAD) {
                LogTool.i("auxiliaryMethodOnMainThread start");
                auxiliaryMethodOnMainThread();
            }
        }
    };

    public CustomCallBack() {
    }

    /**
     * @param canRunOnMainThread
     * @Deprecated replaced by {@link CustomCallBack#CustomCallBack()}
     */
    @Deprecated
    public CustomCallBack(boolean canRunOnMainThread) {

    }

    public CustomCallBack(Class<T> clz) {
        this(true, clz);
    }

    /**
     * @param canRunOnMainThread
     * @param clz
     * @Deprecated replaced by {@link CustomCallBack#CustomCallBack(Class)}
     */
    @Deprecated
    public CustomCallBack(boolean canRunOnMainThread, Class<T> clz) {
        this.clz = clz;
    }

    public boolean dismissProgressDialog() {
        // TODO 改变自定义 ProgressDialog 是否自动消失，默认自动消失
        return true;
    }

    @Override
    public abstract void failure();

    @Override
    public void failureOnMainThread() {
        autoCloseProgressDialog();
    }

    public final void executeAuxiliaryMethodOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.sendEmptyMessage(AUXILIARY_METHOD_ON_MAIN_THREAD);
            return;
        }
    }

    /**
     * 该方法不参与网络访问周期，不能直接被调用，必须通过 {@link #executeAuxiliaryMethodOnMainThread()} 触发。
     */
    public void auxiliaryMethodOnMainThread() {
        // do nothing
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
        CustomHttpHelper.requstCount--;
        failure();
        /**
         * 发送消息调用 {@link #failureOnMainThread()}
         * */
        Message msg3 = new Message();
        msg3.what = FAILURE_ON_MAIN_THREAD;
        handler.sendMessage(msg3);
    }

    @Override
    public void finallyOnMainThread() {
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        CustomHttpHelper.requstCount--;
        if (dismissProgressDialog()) {
            CustomProgressBarDialog.dimissProgressBarDialog();
        } else {
            autoCloseProgressDialog();
        }

        if (response.isSuccessful()) {
            String resultTemp = response.body().string();
            success(resultTemp);
            if (null == resultTemp || "".equals(resultTemp) || clz == null) {
                return;
            }
            // 以下都是转换成 Bean 的操作
            Gson gson = new Gson();
            T t = null;
            try {
                // gson 转换可能会发生异常
                t = gson.fromJson(resultTemp, clz);
            } catch (Exception e) {
                LogTool.i(ExceptionTool.getExceptionTraceString(e));
            }
            if (null == t) {
                LogTool.i("CustomCallBack:没有使用有参构造方法或 GSON 转换异常");
            }
            runOnSelfThread(t);
            /**
             * 发送消息调用 {@link #runOnMainThread(Object)}
             * */
            Message msg1 = new Message();
            msg1.what = RUN_ON_MAIN_THREAD;
            msg1.obj = t;
            handler.sendMessage(msg1);
            /**
             * 发送消息调用 {@link #successOnMainThread(String)}
             * */
            Message msg2 = new Message();
            msg2.what = SUCCESS_ON_MAIN_THREAD;
            msg2.obj = resultTemp;
            handler.sendMessage(msg2);
        } else {
            LogTool.i("访问服务器失败 " + response.code());
            // success("访问服务器失败-->com.nfl.libraryoflibrary.utils.net.CustomCallBack");
        }
        /**
         * 发送消息调用 {@link #finallyOnMainThread()} (String)}
         * */
        handler.sendEmptyMessage(FINALLY_ON_MAIN_THREAD);
    }

    @Override
    public void cancel() {
        if (null != handler) {
            handler.removeMessages(RUN_ON_MAIN_THREAD);
            handler.removeMessages(SUCCESS_ON_MAIN_THREAD);
            handler.removeMessages(FAILURE_ON_MAIN_THREAD);
            handler.removeMessages(FINALLY_ON_MAIN_THREAD);
            handler.removeMessages(AUXILIARY_METHOD_ON_MAIN_THREAD);
        }
    }

    /**
     * 当不主动关闭加载框时，如果请求数为 0 也将其关闭
     */
    private void autoCloseProgressDialog() {
        if (CustomHttpHelper.requstCount <= 0) {
            CustomHttpHelper.requstCount = 0;
            CustomProgressBarDialog.dimissProgressBarDialog();
        }
    }
}
