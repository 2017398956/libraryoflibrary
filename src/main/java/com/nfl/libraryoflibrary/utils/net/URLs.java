package com.nfl.libraryoflibrary.utils.net;

import com.nfl.libraryoflibrary.BuildConfig;

/**
 * Created by fuli.niu on 2016/7/11.
 */
public class URLs {

    public static final String BASE_URL = BuildConfig.DEBUG == false ? "http://192.168.87.23" : "testhttp://mop.99bill.net" ;
    public static final String PERSONAL_STEPS = BASE_URL + ":8097/handler/HandlerSport.ashx" ;
 }
