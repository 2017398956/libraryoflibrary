package com.nfl.libraryoflibrary.beans;

import com.nfl.libraryoflibrary.utils.net.BaseBean;

/**
 * Created by fuli.niu on 2017/8/4.
 * 埋点相关的 bean
 */

public class BuryingPointBean extends BaseBean {

    /**
     * serviceCode : 11 //主模块id
     * serviceName : demo //主模块名称
     * moduleName : demo1 //子模块名称
     * clientStartTime : 2017-08-02 10:19:01 //客户端开始时间
     * clientEndTime : 2017-08-02 10:19:03  //客户端结束时间
     * clickType : 0 //点击类型,0是本地按钮，1是服务端接口
     */

    private String serviceCode;
    private String serviceName;
    private String moduleName;
    private String clientStartTime;
    private String clientEndTime;
    private int clickType;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getClientStartTime() {
        return clientStartTime;
    }

    public void setClientStartTime(String clientStartTime) {
        this.clientStartTime = clientStartTime;
    }

    public String getClientEndTime() {
        return clientEndTime;
    }

    public void setClientEndTime(String clientEndTime) {
        this.clientEndTime = clientEndTime;
    }

    public int getClickType() {
        return clickType;
    }

    public void setClickType(int clickType) {
        this.clickType = clickType;
    }
}
