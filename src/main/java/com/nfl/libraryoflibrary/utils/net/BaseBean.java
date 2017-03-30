package com.nfl.libraryoflibrary.utils.net;

import com.google.gson.Gson;

/**
 * Created by fuli.niu on 2016/7/14.
 * 主要用于服务器返回信息的data部分及无status无msg的bean
 */
public abstract class BaseBean implements BeanInterface{
    public String getJSONString() {
        return new Gson().toJson(this);
    }
}
