package com.nfl.libraryoflibrary.utils.net;

import com.nfl.libraryoflibrary.constant.Constants;

/**
 * Created by fuli.niu on 2016/7/11.
 */
public class URLs {

    public static final String BASE_URL = "http://mop.99bill.net" ;
//    public static final String BASE_URL = "http://192.168.87.23"  ;
    public static final String PERSONAL_STEPS = BASE_URL + ":8097/handler/HandlerSport.ashx" ;
    // 封面的上传地址
    public static String getUpLoadUrl(String loginName , String mDeviceId){
        return PERSONAL_STEPS +
                "?username=" + loginName +
                "&devid=" + mDeviceId +
                "&type=UploadBackgroundImage" +
                "&devtype=Android" +
                "&appversion=" + Constants.APPVERSION;
    }
 }
