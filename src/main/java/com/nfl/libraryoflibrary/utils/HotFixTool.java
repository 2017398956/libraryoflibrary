//package com.nfl.libraryoflibrary.utils;
//
//import android.app.Application;
//
//import com.nfl.libraryoflibrary.constant.Constants;
//import com.taobao.sophix.PatchStatus;
//import com.taobao.sophix.SophixManager;
//import com.taobao.sophix.listener.PatchLoadStatusListener;
//
///**
// * Created by fuli.niu on 2017/9/7.
// * sophix 工具类
// */
//
//public class HotFixTool {
//    public static void init(Application application) {
//        // initialize最好放在attachBaseContext最前面
//        SophixManager.getInstance().setContext(application)
//                .setAppVersion(Constants.APPVERSION)
//                .setAesKey(null)
//                .setEnableDebug(true)
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    @Override
//                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//                        LogTool.i("hotFixCode:" + code);
//                        // 补丁加载回调通知
//                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                            // 表明补丁加载成功
//                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
//                        } else {
//                            // 其它错误信息, 查看PatchStatus类说明
//                        }
//                    }
//                }).initialize();
//    }
//
//    public static void queryAndLoadNewPatch() {
//        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
//        SophixManager.getInstance().queryAndLoadNewPatch();
//    }
//}
