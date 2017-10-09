package com.nfl.libraryoflibrary.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTool {

//    public static class Pattern{
//        public static final String str = "yyyy-MM-dd" ;
//    }

    private static String getDateString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.CHINA);
        return formatter.format(date);
    }

    private static String getDateString(String pattern) {
        return getDateString(new Date(), pattern);
	}

	public static String getDateString(Date date){
        return getDateString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获得当前年月日
     */
    public static String getDateSimpleString(Date date) {
        return getDateString(date, "yyyy-MM-dd");
	}

	/**
	 * 获得当前年月日
	 */
	public static String getNowDateSimple() {
        return getDateString("yyyy-MM-dd");
    }

    public static String getFirstDayOfMonth() {
        return getDateString("yyyy-MM") + "-01";
    }

    public static String getFirstDayOfPreviousMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(turnString2Date(date));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) - 1;
        if (month < 0) {
            month = month + 12;
            --year;
        }
        return year + "-" + month + "-01";
    }

    public static String getLastDayOfPreviousMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(turnString2Date(date));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) - 1;
        if (month < 0) {
            month = month + 12;
            --year;
        }
        calendar.set(Calendar.MONTH, month);
        return year + "-" + month + "-" + calendar.getActualMaximum(Calendar.MONTH);
    }

    public static String getFirstDayOfNextMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(turnString2Date(date));
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR) + month / 12;
        month = month % 12;
        return year + "-" + month + "-01";
    }

    public static String getLastDayOfNextMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(turnString2Date(date));
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR) + month / 12;
        month = month % 12;
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        return year + "-" + month + "-" + calendar.getActualMaximum(Calendar.MONTH);
    }

    public static String getLastDayOfMonth() {
        return getDateString("yyyy-MM") + "-" + Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDate() {
        return getDateString("yyyy-MM-dd HH:mm:ss");
    }

    public static String getNowDateAll() {
        return getDateString("yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 获得指定日期的前几天或者后几天的日期
     *
	 * @param appointDate 指定的日期
	 * @param beforeOrAfterDays 正：后几天；负：前几天
     * @return 日期格式 yyyy-MM-dd
     */
	public static String getDateSimpleFromACertainDate(Date appointDate , int beforeOrAfterDays){
		appointDate.setTime(appointDate.getTime() + beforeOrAfterDays * 24 * 60 * 60 * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return formatter.format(appointDate);
	}

	public static String getDateSimpleFromACertainDate(String appointDateString , int beforeOrAfterDays){
		Date appointDate = turnString2Date(appointDateString) ;
		if(null != appointDate){
			appointDate.setTime(appointDate.getTime() + beforeOrAfterDays * 24 * 60 * 60 * 1000);
		}else {
			return null ;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return formatter.format(appointDate);
	}

	/**
     * 获得当前时分
	 */
    public static String getTimeSimpleString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm",
				Locale.CHINA);
		return formatter.format(date);
	}

	/**
	 * 获得可用作文件名的时间戳
     *
	 * @return
     */
	public static String getTimeStamp() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS",
				Locale.CHINA);
		return formatter.format(currentTime);
	}

	/**
	 * 默认格式为方法内注释
     *
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
        if (TextUtils.isEmpty(dateString)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = null;
        dateString = dateString.replace("/", "-").replace("年", "-").replace("月", "-").replace("日", "");

        int dateStringSize = dateString.length();
		if(10 == dateStringSize){
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
					Locale.CHINA) ;
        } else if (16 == dateStringSize) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                    Locale.CHINA);
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

    public static long getMilliseconds(String dateString) {
        Date date = turnString2Date(dateString);
        return null == date ? 0 : date.getTime();
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

    public static String getDateStringFromMillisecondsString(String timeMillisecondsString) {
        long time = 0;
        try {
            time = Long.parseLong(timeMillisecondsString);
        } catch (Exception e) {
        }
        Date date = new Date();
        date.setTime(time);
        return 0 == time ? "" : getDateString(date);
    }
}
