package com.nfl.libraryoflibrary.utils.pedometer;

import com.nfl.libraryoflibrary.constant.ApplicationContext;
import com.nfl.libraryoflibrary.utils.LogTool;

/**
 * Created by fuli.niu on 2016/7/19.
 */
public class StepsCountTool {
	
	/**
     * 获得当天的步数，若步数异常重置步数
     * @return
     */
    public static String getTodaySteps(){
        int result = 0 ;
        Database db = Database.getInstance(ApplicationContext.applicationContext) ;
        long today = Util4Pedometer.getToday();
        result = db.getSteps(today) + db.getSensorSteps(today) ;
		if(0 > result){
            // 说明数据异常，应该重置当前的步数
            db.updateSteps(today , - db.getSteps(today));
            db.updateSensorSteps(today , 0);
            result = 0 ;
        }
        LogTool.i("您已经走了：" + result + "步。") ;
        return result + "" ;
    }
}
