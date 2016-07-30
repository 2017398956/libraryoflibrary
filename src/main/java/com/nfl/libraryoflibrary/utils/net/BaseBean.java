package com.nfl.libraryoflibrary.utils.net;

import java.util.ArrayList;

/**
 * Created by fuli.niu on 2016/7/14.
 */
public abstract class BaseBean<T> {
    public static final int STATUS_SUCCESS = 0;

    protected int status;
    protected String msg;
    protected ArrayList<T> data;

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

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    /**
     * 在当前的结尾处追加数据
     * @param another
     */
    public void append(BaseBean<T> another) {
        append(another, false);
    }

    /**
     * 追加数据
     * @param another 要追加的数据
     * @param addToFront
     */
    public void append(BaseBean<T> another, boolean addToFront) {
        if (another != null && another.getData() != null) {
            append(another.getData(), addToFront);
        }
    }

    /**
     * 在当前的结尾处追加数据
     */
    public void append(ArrayList<T> dataList) {
        append(dataList, false);
    }

    /**
     * 追加数据
     * @param dataList 要追加的数据
     * @param addToFront
     */
    public void append(ArrayList<T> dataList, boolean addToFront) {
        if (addToFront) {
            ArrayList<T> newData = new ArrayList<T>();
            newData.addAll(dataList);
            newData.addAll(data);
            this.data = newData;
        } else {
            this.data.addAll(dataList);
        }
    }
}
