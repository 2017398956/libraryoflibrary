package com.nfl.libraryoflibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTool {

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		return formatter.format(currentTime);
	}

	public static String getDateString(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		return formatter.format(date);
	}

	/**
	 * 获得当前年月日
	 */
	public static String getNowDateSimple() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return formatter.format(currentTime);
	}

	/**
	 * 获得当前年月日
	 */
	public static String getDateSimpleString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return formatter.format(date);
	}

	/**
	 * 默认格式为方法内注释
	 * @param dateString
	 * @return
     */
	public static Date turnString2Date(String dateString){

//		yyyy-MM-dd 1970-01-01 | size = 10
//		yyyy-MM-dd HH:mm 1970-01-01 00:00 | size = 16
// 		yyyy-MM-dd HH:mm:ss 1970-01-01 00:00:00 | size = 19
//		yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000 | size = 21
//		yyyy-MM-dd HH:mm:ssZ 1970-01-01 00:00:00+0000 | size = 24
//		yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000 | size = 28
//		yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000 | size = 28

		SimpleDateFormat simpleDateFormat = null ;
		int dateStringSize = null == dateString ? 0 : dateString.length() ;
		if(10 == dateStringSize){
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
					Locale.CHINA) ;
		}else if(19 == dateStringSize){
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.CHINA) ;
		}else{
			return null ;
		}
		try {
			 return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null ;
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回时间类型 yyyy年MM月dd日
	 */
	public static String getNowDate2() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日",
				Locale.CHINA);
		return formatter.format(currentTime);
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回时间类型 yyyy年MM月dd日
	 */
	public static String getNowDate3() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd",
				Locale.CHINA);
		return formatter.format(currentTime);
	}

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
		return format.format(date);
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDateFormatedByYMD() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return formatter.format(currentTime);
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateFormatedByYMD(long currentTime ) {
		Date date = new Date(currentTime*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return formatter.format(date);
	}

	public static int getHourForDay() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int isDayorNight() {
		int hour = getHourForDay();
		if (hour >= 6 && hour < 18) {
			return DAY;
		} else {
			return NIGHT;
		}
	}

	public static String getAmOrPM(){
		int hour = getHourForDay();
		if (hour >= 0 && hour < 12) {
			return "AM";
		} else {
			return "PM";
		}
	}

	public static String getWeekday(){
		Date date=new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		return dateFm.format(date);
	}

	public static final int DAY = 0;
	public static final int NIGHT = 1;

}
