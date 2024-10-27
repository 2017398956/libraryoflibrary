package com.nfl.libraryoflibrary.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.PatternMatcher;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by fuli.niu on 2016/8/11.
 */
public class WifiTool {

    private Context applicationContext;
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
        applicationContext = context.getApplicationContext();
        // 取得WifiManager对象
        mWifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    private boolean hasRegisterWifiConnectReceiver = false;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // FIXME:API 31 开始，不止STATUS_NETWORK_SUGGESTIONS_SUCCESS 这一个状态码表示成功
            if (!Objects.equals(intent.getAction(), WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
                return;
            }
            // do post connect processing here
            Log.d("NFL", "has added network suggestions.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                connectWifiBySsid();
            }
        }
    };

    private WeakReference<ComponentActivity> activityRef;

    public WifiTool(ComponentActivity activity) {
        this(activity.getApplicationContext());
        this.activityRef = new WeakReference<>(activity);
        activity.getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                if (hasRegisterWifiConnectReceiver && activityRef != null && activityRef.get() != null) {
                    activityRef.get().unregisterReceiver(broadcastReceiver);
                }
            }
        });
    }

    public WifiTool(Context context, String wifiName, String wifiPw) {
        this(context);
        this.wifiName = wifiName;
        this.wifiPw = wifiPw;
    }

    public void connectWifi(String wifiName, String wifiPw) {
        openWifi();
        this.wifiName = wifiName;
        this.wifiPw = wifiPw;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WifiNetworkSuggestion networkSuggestion = new WifiNetworkSuggestion.Builder()
                    .setSsid(wifiName)
//                    .setPriority(0)
//                    .setWpa3Passphrase("")
                    .setIsAppInteractionRequired(true)
                    .build();
            List<WifiNetworkSuggestion> suggestions = new ArrayList<>();
            suggestions.add(networkSuggestion);
            int status = mWifiManager.addNetworkSuggestions(suggestions);
            if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                // do error handling here
                Log.e("NFL", "add network suggestions failed!!!");
            }
            // Optional (Wait for post connection broadcast to one of your suggestions)
            IntentFilter intentFilter = new IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION);
            hasRegisterWifiConnectReceiver = true;
            if (activityRef != null && activityRef.get() != null) {
                activityRef.get().registerReceiver(broadcastReceiver, intentFilter);
            }
        } else {
            connectToWiFiBelowQ(wifiName, wifiPw);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void connectWifiBySsid() {
        ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // FIXME：是否应该在添加建议wifi成功后执行连接wifi的操作
        WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                .setSsidPattern(new PatternMatcher(this.wifiName, PatternMatcher.PATTERN_PREFIX))
//                .setWpa3Passphrase(this.wifiPw)
//                    .setBssidPattern(MacAddress.fromString("10:03:23:00:00:00"), MacAddress.fromString("ff:ff:ff:00:00:00"))
                .build();
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .setNetworkSpecifier(specifier)
                .build();
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                Log.d("NFL", "onAvailable$network");
//                connectivityManager.unregisterNetworkCallback(networkCallback)
                mWifiManager.disconnect();
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.d("NFL", "onUnavailable");
            }
        };
        connectivityManager.requestNetwork(networkRequest, networkCallback);
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
    public void connectNetByNetworkId(int index) {
        if (index > mWifiConfigurations.size()) {
            return;
        }
        // 连接配置好指定ID的网络
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    private final BroadcastReceiver wifiStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    mWifiList = mWifiManager.getScanResults();
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    break;
            }
        }
    };
    public void startScan() {
        // 注册 WiFi 扫描结果、WiFi 状态变化的广播接收
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION); // 监听wifi扫描结果
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // 监听wifi是开关变化的状态
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 监听wifi是否连接成功的广播
        applicationContext.registerReceiver(wifiStateChangeReceiver, intentFilter);

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

    /**
     * 根据已有配置信息接入某个wifi热点
     */
    private boolean addNetWork(WifiConfiguration config) {
        WifiInfo wifiinfo = mWifiManager.getConnectionInfo();
        if (null != wifiinfo) {
            mWifiManager.disableNetwork(wifiinfo.getNetworkId());
        }
        boolean result;
        if (config.networkId > 0) {
            result = mWifiManager.enableNetwork(config.networkId, true);
            mWifiManager.updateNetwork(config);
        } else {
            int i = mWifiManager.addNetwork(config);
            result = false;
            if (i > 0) {
                mWifiManager.saveConfiguration();
                return mWifiManager.enableNetwork(i, true);
            }
        }
        return result;
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
    private boolean connectToWiFiBelowQ(String wifiName, String wifiPw) {
        String wifiSSID = "\"" + wifiName + "\"";// 注意双引号
        String wifiPassWord = "\"" + wifiPw + "\"";// 注意双引号
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
        mWifiConfiguration = createWifiConfig(wifiName, wifiPw, WifiCipherType.WPA);
        return addNetWork(mWifiConfiguration);
    }

    public enum WifiCipherType {
        WEP, WPA, NO_PASS, INVALID
    }

    /**
     * 判断wifi热点支持的加密方式
     */
    public WifiCipherType getWifiCipher(String capabilities) {
        if (capabilities.isEmpty()) {
            return WifiCipherType.INVALID;
        } else if (capabilities.contains("WEP")) {
            return WifiCipherType.WEP;
        } else if (capabilities.contains("WPA") || capabilities.contains("WPA2")
                || capabilities.contains("WPS")) {
            return WifiCipherType.WPA;
        } else {
            return WifiCipherType.NO_PASS;
        }
    }


    private WifiConfiguration createWifiConfig(String ssid, String password, WifiCipherType type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        if (type == WifiCipherType.NO_PASS) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        if (type == WifiCipherType.WEP) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }

        if (type == WifiCipherType.WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;

        }
        return config;
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
