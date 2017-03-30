package com.nfl.libraryoflibrary.utils.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by fuli.niu on 2017/3/1.
 * 主要用于接收服务器信息
 */

public class CommonBean<T> implements BeanInterface{
    private int status ;
    private String msg ;
    private T data ;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getJSONString() {
        return new Gson().toJson(this) ;
    }
}
