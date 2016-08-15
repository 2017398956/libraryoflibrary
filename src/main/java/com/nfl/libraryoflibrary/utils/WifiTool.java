package com.nfl.libraryoflibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by fuli.niu on 2016/8/11.
 */
public class WifiTool {
    // 自动连接自己的wifi热点（该热点信息没保存到手机中的情况），省的每次手动操作了
    private String wifiName;// 自动连接某个wifi时的wifi名称
    private String wifiPw;// 自动连接某个wifi时的WiFi密码
    // 定义一个WifiManager对象
    private WifiManager mWifiManager;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfigurations;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 定义一个WifiInfo对象
    private WifiInfo mWifiInfo;

    WifiManager.WifiLock mWifiLock;

    public WifiTool(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
//        mWifiManager
    }

    public WifiTool(Context context, String wifiName, String wifiPw) {
        this(context);
        this.wifiName = wifiName;
        this.wifiPw = wifiPw;
    }

    // 打开wifi
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 关闭wifi
    public void closeWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
            LogTool.i("wifi即将被关闭") ;
        }else{
            LogTool.i("wifi是关闭状态，不需要关闭") ;
        }
    }

    // 检查当前wifi状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定wifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁wifiLock
    public void releaseWifiLock() {
        // 判断是否锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个wifiLock
    public void createWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfigurations;
    }

    // 指定配置好的网络进行连接
    public void connetionConfiguration(int index) {
        if (index > mWifiConfigurations.size()) {
            return;
        }
        // 连接配置好指定ID的网络
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 查看扫描结果
    @SuppressLint("UseValueOf")
    public StringBuffer lookUpScan() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mWifiList.size(); i++) {
            sb.append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、 Capabilities、frequency、level
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIpAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID
    public int getNetWordId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到wifiInfo的所有信息
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    // 添加一个网络并连接
    public void addNetWork(WifiConfiguration configuration) {
        int wcgId = mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(wcgId, true);
    }

    // 断开指定ID的网络
    public void disConnectionWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * 连接wifi,成功返回true，失败返回false
     * 注意：WifiConfiguration 中参数的值比ScanResult中相同的参数多了一对双引号
     *
     * @return
     */
    public boolean connectToWiFi() {
        String wifiSSID = "\"" + wifiName + "\"";// 注意双引号
        String wifiPassWord = "\"" + wifiPw + "\"";// 注意双引号
        openWifi();
        startScan();
        WifiConfiguration mWifiConfiguration = null;
        if (mWifiConfigurations != null) {
            for (int i = 0; i < mWifiConfigurations.size(); i++) {
                mWifiConfiguration = mWifiConfigurations.get(i);
                if (mWifiConfiguration.SSID.equals(wifiSSID)) {
                    // 已保存过该WiFi的密码
                    return mWifiManager.enableNetwork(
                            mWifiConfiguration.networkId, true);
                }
            }
        }
        // 未保存过该WiFi的密码，则进行wifi信息的配置
        mWifiConfiguration = new WifiConfiguration();
        mWifiConfiguration.SSID = wifiSSID;
        mWifiConfiguration.preSharedKey = wifiPassWord;
        mWifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        mWifiConfiguration.hiddenSSID = false;
        mWifiManager.addNetwork(mWifiConfiguration);

        return mWifiManager.enableNetwork(mWifiConfiguration.networkId, true);
    }

    /**
     * 将WiFi信息打印在log上
     */
    public void printWifiInfo(){
        LogTool.i(mWifiManager.getDhcpInfo().toString() + "wifi信息：" + getWifiInfo()) ;
        closeWifi();
        mWifiInfo = mWifiManager.getConnectionInfo()  ;
        LogTool.i(mWifiManager.getDhcpInfo().toString() + "wifi信息2：" + getWifiInfo()) ;
    }
}
