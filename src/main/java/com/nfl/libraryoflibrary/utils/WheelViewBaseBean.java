package com.nfl.libraryoflibrary.utils;

/**
 * Created by fuli.niu on 2017/3/6.
 * 用于滚轮选择器
 */

public abstract class WheelViewBaseBean<T> {

    protected int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // 这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public abstract T getPickerViewText();
}
