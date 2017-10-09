package com.nfl.libraryoflibrary.beans;

import android.text.TextUtils;

/**
 * Created by fuli.niu on 2017/6/12.
 * 实现 android-pickerview-master 滚轮效果，所需要的 bean 格式
 */

public class OptionPickerViewBaseBean implements OptionPickerViewBaseBeanInterface {

    private int id;
    private String data;// 显示的数据
    private String dataMirror;// 显示数据所对应的应该传递给服务器的值

    public OptionPickerViewBaseBean(){

    }

    public OptionPickerViewBaseBean(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public OptionPickerViewBaseBean(int id, String data, String dataMirror) {
        this.id = id;
        this.data = data;
        this.dataMirror = dataMirror;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataMirror() {
        if (TextUtils.isEmpty(dataMirror)) {
            return data;
        }
        return dataMirror;
    }

    public void setDataMirror(String dataMirror) {
        this.dataMirror = dataMirror;
    }

    // 这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        // 这里还可以判断文字超长截断再提供显示
        return data;
    }
}
