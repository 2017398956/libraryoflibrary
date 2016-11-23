package com.nfl.libraryoflibrary.utils.pedometer;

import com.nfl.libraryoflibrary.constant.ApplicationContext;
import com.nfl.libraryoflibrary.utils.LogTool;

/**
 * Created by fuli.niu on 2016/7/19.
 */
public class StepsCountTool {

    public static String getTodaySteps(){
        int result = 0 ;
        Database db = Database.getInstance(ApplicationContext.applicationContext) ;
        long today = Util4Pedometer.getToday();
        result = db.getSteps(today) + db.getSensorSteps(today) ;
        LogTool.i("您已经走了：" + result + "步。") ;
        return result + "" ;
    }
}
