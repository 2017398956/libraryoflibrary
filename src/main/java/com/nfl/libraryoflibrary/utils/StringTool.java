package com.nfl.libraryoflibrary.utils;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by fuli.niu on 2016/11/30.
 */

public class StringTool {

    /**
     * 对于List有时需要获得像其他Object一样的字符串,
     * 特别是调试adapter时
     *
     * @param list
     * @return
     */
    public static String toString4List(List list){
        return list.getClass().getName() + "@" + Integer.toHexString(list.hashCode()) ;
    }

    /**
     * 判断字符串中是否仅包含字母数字和汉字
     * 各种字符的unicode编码的范围：
     * 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
     * 数字：[0x30,0x39]（或十进制[48, 57]）
     * 小写字母：[0x61,0x7a]（或十进制[97, 122]）
     * 大写字母：[0x41,0x5a]（或十进制[65, 90]）
     */
    public static boolean isLetterDigitOrChinese(String str) {
        if(TextUtils.isEmpty(str)){
            return true ;
        }
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        return str.matches(regex);
    }

    /**
     * 判断是否是身份证号码
     * @param identityCardNumber
     * @return
     */
    public static boolean isIdentityCardNumber(String identityCardNumber){
        if(TextUtils.isEmpty(identityCardNumber)){
            return false ;
        }
        String regex01 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        String regex02 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        return identityCardNumber.matches(regex01) || identityCardNumber.matches(regex02) ;
    }

}
