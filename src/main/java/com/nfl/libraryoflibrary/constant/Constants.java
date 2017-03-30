package com.nfl.libraryoflibrary.constant;

/**
 * Created by fuli.niu on 2016/8/2.
 */
public class Constants {
    public static final String APPVERSION = "2.340" ;// 这里必须为4位float类型的String
    public static final boolean isDebug = false ;
	public static final boolean canShowDBErgodicFloatWindow = false ;// 是否能显示数据管理悬浮窗
    public static final String UPDATE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BillOA/update/";
    // 增量包文件名，格式：低版本号(当前版本号)_高版本号(最新版版本号).apk ; 如：2.320_2.410.apk ;
    public static String PATCH_FLIE_NAME ;
    // 是否增量升级,默认false
    public static boolean IS_PATCH = false ;
    // 图片选择器最大张数
    public static int MAX_PICTURE_NUM = 3;
}
