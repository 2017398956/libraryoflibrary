package com.nfl.libraryoflibrary.beans;

import android.text.TextUtils;

/**
 * Created by fuli.niu on 2017/6/12.
 * 实现 android-pickerview-master 滚轮效果，所需要的 bean 格式
 */

public interface OptionPickerViewBaseBeanInterface {

    // 这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    String getPickerViewText();
//    {
//        // 这里还可以判断文字超长截断再提供显示
//        return data;
//    }
}
