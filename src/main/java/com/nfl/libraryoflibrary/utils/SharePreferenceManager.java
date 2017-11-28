package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.nfl.libraryoflibrary.constant.ApplicationContext;

/**
 * Created by fuli.niu on 2017/11/28.
 */

public class SharePreferenceManager {

    public static final String EMPTY_STR = "";
    // 最后登陆用户
    public static final String LASTUSER = "lastuser";
    // 最后登陆用户密码
    public static final String LASTUSER_PASSWORD = "lastuser_password";
    public static final String LOGIN_INFO_SP = "login_info";

    public static final SharedPreferences spLogin = ApplicationContext.applicationContext.getSharedPreferences(LOGIN_INFO_SP, Activity.MODE_PRIVATE);
}
