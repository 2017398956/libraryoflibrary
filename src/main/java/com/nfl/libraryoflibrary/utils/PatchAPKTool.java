package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;

import com.nfl.libraryoflibrary.constant.Constants;

import nfl.bspatch.BspatchUtil;

/**
 * Created by fuli.niu on 2016/12/8.
 */

public class PatchAPKTool {

    private String path;
    private Thread patchThread;

    public PatchAPKTool(final Context context, final Handler handler) {
        path = Constants.UPDATE_FILE_PATH ;
        patchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = BspatchUtil.bspatch(ApkExtract.extract(context), path + "BillOA.apk", path + Constants.PATCH_FLIE_NAME);
                handler.sendEmptyMessage(result);
            }
        });
    }

    public void start() {
        if (null != patchThread && !patchThread.isAlive()) {
            patchThread.start();
        }
    }

    // 提取当前应用的apk
    private static class ApkExtract {
        public static String extract(Context context) {
            context = context.getApplicationContext();
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            String apkPath = applicationInfo.sourceDir;
            return apkPath;
        }
    }
}
