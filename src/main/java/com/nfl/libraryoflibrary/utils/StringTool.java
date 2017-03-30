package com.nfl.libraryoflibrary.utils;

import java.util.List;

/**
 * Created by fuli.niu on 2016/11/30.
 */

public class StringTool {

    /**
     * 对于List有时需要获得像其他Object一样的字符串,
     * 特别是调试adapter时
     * @param list
     * @return
     */
    public static String toString4List(List list){
        return list.getClass().getName() + "@" + Integer.toHexString(list.hashCode()) ;
    }
}
