package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu on 2016/8/19.
 */

public class RootDetector {

    // context最好传ApplicationContext
    private Context context;
    // 暂时只获得一个Root工具的信息就返回，所以其size最大为1
    private List<RootToolInfo> rootToolInfos;
    // root工具关键字，例如：一键root大师的包名为com.baidu.superroot.SuApplication，其关键字可以为superroot
    private List<String> filterInfos;
    private RootToolInfo rootToolInfo;
    // 从右往左依次代表checkRootMethod1()...checkRootMethod4() ;0 表示返回的是false ，1 表示返回的是true ;
    private int rootMethodInfo = 0x0000;

    private RootDetector() {
    }

    public RootDetector(Context context, List<String> filterInfos) {
        this();
        this.context = context;
        this.filterInfos = filterInfos;
        if(this.filterInfos == null){
            this.filterInfos = new ArrayList<>() ;
            this.filterInfos.add("root") ;
        }else if(filterInfos.size() < 1){
            this.filterInfos.add("root") ;
        }
        try {
            checkRootMethod1();
            checkRootMethod2();
            checkRootMethod3();
            checkRootMethod4();
        }catch (Exception e){
            ExceptionTool.getExceptionTraceString(e , true) ;
        }
    }

    /**
     * 设备是否被Root
     *
     * @return
     */
    public boolean isDeviceRooted() {
        return (rootMethodInfo & 0x1111) != 0;
    }

    public String getRootMethodInfo2String() {
        int num = 10000;
        if ((rootMethodInfo & 0x0001) != 0) {
            /**
             * 说明{@link #checkRootMethod1()} 的返回值为true
             */
            num += 1;
        }
        if ((rootMethodInfo & 0x0010) != 0) {
            num += 10;
        }
        if ((rootMethodInfo & 0x0100) != 0) {
            num += 100;
        }
        if ((rootMethodInfo & 0x1000) != 0) {
            num += 1000;
        }
        return (num + "").replaceFirst("1", "0x") ;
    }

    /**
     * root检测信息
     *
     * @return
     */
    public String getRootDetailInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("Root检测信息：" + getRootMethodInfo2String());
        sb.append(";");
        if (rootToolInfos.size() > 0) {
            rootToolInfo = rootToolInfos.get(0);
            sb.append(rootToolInfo.getAppName());
            sb.append(";");
            sb.append(rootToolInfo.getAppVersion());
            sb.append(";");
            sb.append(rootToolInfo.getPackageName());
            sb.append(";");
        }
        return sb.toString();
    }

    /**
     * 由于RootDetectorTool 要放在library中所以这里不使用switch ;
     * 一般不需要用文字标记哪个方法检测到了设备被root，直接传递{@link #rootMethodInfo}即可。
     *
     * @return
     */
    private String getRootDetailInfoBackup() {
        StringBuffer sb = new StringBuffer();
        sb.append("Root检测信息：");
        if ((rootMethodInfo & 0x0001) != 0) {
            /**
             * 说明{@link #checkRootMethod1()} 的返回值为true
             */
            sb.append("checkRootMethod1() = true ;");
        }
        if ((rootMethodInfo & 0x0010) != 0) {
            sb.append("checkRootMethod2() = true ;");
        }
        if ((rootMethodInfo & 0x0100) != 0) {
            sb.append("checkRootMethod3() = true ;");
        }
        if ((rootMethodInfo & 0x1000) != 0) {
            sb.append("checkRootMethod4() = true ;");
        }
        if (rootToolInfos.size() > 0) {
            rootToolInfo = rootToolInfos.get(0);
            sb.append(rootToolInfo.getAppName());
            sb.append(";");
            sb.append(rootToolInfo.getAppVersion());
            sb.append(";");
            sb.append(rootToolInfo.getPackageName());
            sb.append(";");
        }
        return sb.toString();
    }

    /**
     * 设备是否被root
     *
     * @return
     */
    private boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            rootMethodInfo = rootMethodInfo | 0x0001;
            return true;
        }
        rootMethodInfo = rootMethodInfo | 0x0000;
        return false;
    }

    private boolean checkRootMethod2() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                rootMethodInfo = rootMethodInfo | 0x0010;
                return true;
            }
        } catch (Exception e) {
        }
        rootMethodInfo = rootMethodInfo | 0x0000;
        return false;
    }

    private boolean checkRootMethod3() {
        if (new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null) {
            rootMethodInfo = rootMethodInfo | 0x0100;
            return true;
        } else {
            rootMethodInfo = rootMethodInfo | 0x0000;
            return false;
        }
    }

    private boolean checkRootMethod4() {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            if(null == rootToolInfos){
                rootToolInfos = new ArrayList<>() ;
            }
            for (PackageInfo packageInfo : pakageinfos) {
                for (String filterInfo : filterInfos) {
                    if (filterInfo.length() > 0 && packageInfo.packageName.toLowerCase().contains(filterInfo)) {
                        rootToolInfo = new RootToolInfo();
                        rootToolInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
                        rootToolInfo.setAppVersion(packageInfo.versionName);
                        rootToolInfo.setPackageName(packageInfo.packageName);
                        rootToolInfos.add(rootToolInfo);
                        rootMethodInfo = rootMethodInfo | 0x1000;
                        return true;
                    }
                }
            }
        }
        // app被销毁或没有root返回false
        rootMethodInfo = rootMethodInfo | 0x0000;
        return false;
    }

    public int getRootMethodInfo() {
        return rootMethodInfo;
    }

    public List<RootToolInfo> getRootToolInfos() {
        return rootToolInfos;
    }

    /**
     * 三方应用程序的过滤器
     *
     * @param info
     * @return true 三方应用 false 系统应用
     */
    private boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            // 代表的是系统的应用,但是被用户升级了. 用户应用
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            // 代表的用户的应用
            return true;
        }
        return false;
    }

    public class RootToolInfo {
        private String appName;
        private String appVersion;
        private boolean isUserApp;
        private String packageName;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public boolean isUserApp() {
            return isUserApp;
        }

        public void setUserApp(boolean userApp) {
            isUserApp = userApp;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
}

