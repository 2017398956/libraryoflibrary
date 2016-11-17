package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu on 2016/11/16.
 */

public class PackageInfoProvider {

    private Context context;
    private List<AppInfo> appInfos;
    private List<String> filterInfos ;
    private AppInfo appInfo;

    private PackageInfoProvider(){
        filterInfos = new ArrayList<>() ;
        filterInfos.add("bluetooth") ;
    }

    public PackageInfoProvider(Context context) {
        this() ;
        this.context = context;
//        LogTool.i("该设备是否被Root：" + isRooted()) ;
    }

    /**
     * 获得应用安装信息
     * @return
     */
    public List<AppInfo> getAppInfo() {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        appInfos = new ArrayList();
        for (PackageInfo packageInfo : pakageinfos) {
            appInfo = new AppInfo();
            // 获取字符串方法
            // context.getString("R.string.app_name");
            // context.getResources().getString("R.string.app_name");
            //获取尺寸资源方法
            // context.getResources().getDimension("R.dimen.test");
            // 获取xml文件并且返回的是XmlResourceParse类，其继承与XmlPullParse
            // XmlResourceParser xmlrp = context.getResources().getXml(R.xml.yo);
            // 获取应用程序的名称，不是包名，而是清单文件中的labelname
            String str_name = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.setAppName(str_name);

            // 获取应用程序的版本号码
            String version = packageInfo.versionName;
            appInfo.setAppVersion(version);

            // 获取应用程序的快捷方式图标
            // Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
            // appInfo.setDrawable(drawable);

            // 获取应用程序是否是第三方应用程序
            appInfo.setUserApp(filterApp(packageInfo.applicationInfo));

            //给一同程序设置包名
            appInfo.setPackageName(packageInfo.packageName);

//            LogTool.i("版本号:" + version + ";程序名称:" + str_name);
            LogTool.i("程序包名称:" + appInfo.getPackageName());
            appInfos.add(appInfo);
            appInfo = null;
        }
        return appInfos;
    }

    /**
     * 判断手机是否被root
     * @return
     */
    public boolean isRooted(){
        if(context == null){
            // app被销毁返回false
            return false ;
        }
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : pakageinfos) {
            for(String filterInfo : filterInfos){
                if(packageInfo.packageName.toLowerCase().contains(filterInfo)){
                    return true ;
                }
            }
        }
        return false ;
    }

    /**
     * 三方应用程序的过滤器
     * @param info
     * @return true 三方应用 false 系统应用
     */
    public boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            // 代表的是系统的应用,但是被用户升级了. 用户应用
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            // 代表的用户的应用
            return true;
        }
        return false;
    }

    private class AppInfo {
        String appName ;
        String appVersion ;
        boolean isUserApp ;
        String packageName ;

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
