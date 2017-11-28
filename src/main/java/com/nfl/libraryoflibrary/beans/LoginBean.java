package com.nfl.libraryoflibrary.beans;

import com.nfl.libraryoflibrary.utils.net.BaseBean;

/**
 * Created by fuli.niu on 2017/11/28.
 */

public class LoginBean extends BaseBean {
    private String msg;
    private String status;
    private String vid;
    private String AuthUser_AuthNum;
    private String AuthUser_AuthToken;
    private String AuthUser_AuthMAC;
    private String userId;
    private String devid;
    private String appKey;
    private String appVerion;
    private String requestTs;
    private String signature;
    private String jsessionid;
    private String expireTime;
    private String token;

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getAuthUser_AuthNum() {
        return AuthUser_AuthNum;
    }

    public void setAuthUser_AuthNum(String authUser_AuthNum) {
        AuthUser_AuthNum = authUser_AuthNum;
    }

    public String getAuthUser_AuthToken() {
        return AuthUser_AuthToken;
    }

    public void setAuthUser_AuthToken(String authUser_AuthToken) {
        AuthUser_AuthToken = authUser_AuthToken;
    }

    public String getAuthUser_AuthMAC() {
        return AuthUser_AuthMAC;
    }

    public void setAuthUser_AuthMAC(String authUser_AuthMAC) {
        AuthUser_AuthMAC = authUser_AuthMAC;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppVerion() {
        return appVerion;
    }

    public void setAppVerion(String appVerion) {
        this.appVerion = appVerion;
    }

    public String getRequestTs() {
        return requestTs;
    }

    public void setRequestTs(String requestTs) {
        this.requestTs = requestTs;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return getJSONString();
    }


}
