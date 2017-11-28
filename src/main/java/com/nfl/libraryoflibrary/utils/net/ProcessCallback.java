package com.nfl.libraryoflibrary.utils.net;

/**
 * Created by fuli.niu on 2017/8/3.
 */

public interface ProcessCallback {
    void onTaskStart(long totalLength);

    void onProcess(long process);

    void onTaskFinish(String json);

    void onTaskCanceled();

    void onError(String message);
}
