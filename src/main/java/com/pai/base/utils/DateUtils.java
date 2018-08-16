package com.pai.base.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	/** yyyy-MM-dd */
	public final static DateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	/** yyyyMMdd */
	public final static DateFormat SIMPLEDATEYMDFORMAT = new SimpleDateFormat("yyyyMMdd");
	/** yyyy-MM-dd HH:mm:ss */
	public final static DateFormat FULLDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** HH:mm:ss */
	public final static DateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");
	/** yyyyMM */
	public final static DateFormat MONTHDATEFORMAT = new SimpleDateFormat("yyyyMM");
	
	public final static DateFormat FULLDATENOFLAGFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	public static Timestamp getCurrentDate() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String getCurrentDateStr() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return SIMPLEDATEFORMAT.format(now);
	}

	public static String getCurrentDateStr(DateFormat df) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return df.format(now);
	}

	public static String format(String strDate, String pattern) {
		if(strDate==null || "".equals(strDate)){
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		String result=null;
		try {
			result= df.format(FULLDATEFORMAT.parse(strDate));
		} catch (ParseException e) {
			result="";
		}
		return result;
	}

	public static String format(String strDate, SimpleDateFormat df) {
		if(strDate==null || "".equals(strDate)){
			return "";
		}
		String result=null;
		try {
			result= df.format(FULLDATEFORMAT.parse(strDate));
		} catch (ParseException e) {
			result="";
		}
		return result;
	}
	
	public static String formatDate(Date date,String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}
	
	public static int getSeconds(String str) {
		try {
			switch (str.charAt(str.length() - 1)) {
			case 's':
				return Integer.parseInt(str.substring(0, str.length() - 1));
			case 'm':
				return Integer.parseInt(str.substring(0, str.length() - 1)) * 60;
			case 'h':
				return Integer.parseInt(str.substring(0, str.length() - 1)) * 3600;
			case 'd':
				return Integer.parseInt(str.substring(0, str.length() - 1)) * 86400;
			default:
				return Integer.parseInt(str);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String getCurrentDateStr(String format) {
		DateFormat ft = new SimpleDateFormat(format);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return ft.format(now);
	}

	public static String getCurrentFullDateStr() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return FULLDATEFORMAT.format(now);
	}
	public static long getCurrentFullDateNoFlag() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return Long.valueOf(FULLDATENOFLAGFORMAT.format(now));
	}
	
	public static long getFullDateNoFlag(long time) {
		Timestamp now = new Timestamp(time);
		return Long.valueOf(FULLDATENOFLAGFORMAT.format(now));
	}

	public static String getSimpleDateStr(Timestamp tms) {
		return SIMPLEDATEFORMAT.format(tms);
	}

	public static String getFullDateStr(Timestamp tms) {
		return FULLDATEFORMAT.format(tms);
	}

	public static String getMonthDateStr(Timestamp tms) {
		return MONTHDATEFORMAT.format(tms);
	}

	public static String getDateTimeStr(Timestamp tms) {
		return TIMEFORMAT.format(tms);
	}

	public static String getCustomFormatDateStr(Timestamp tms, String format) {
		DateFormat customFormat = new SimpleDateFormat(format);
		return customFormat.format(tms);
	}
	
	public static String getNowYear() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		return String.valueOf(year);
	}
	public static String getNowMonth() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 10) {
			return "0" + month;
		} else {
			return String.valueOf(month);
		}
	}

	public static Date stringFormatToSimpleDate(String simpleDateStr){
		Date result = null;
		try {
			if(simpleDateStr.length() == 8){
				simpleDateStr = simpleDateStr.substring(0,4)+"-"+simpleDateStr.substring(4,6)+"-"+simpleDateStr.substring(6,8);
			}
			result = SIMPLEDATEFORMAT.parse(simpleDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getWeekTextOfDate(Date date) {
		String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	public static String getWeekNumOfDate(Date date) {
		String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysCode[intWeek];
	}

	public static String getDateNMonthsLater(Date date,int offset){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,offset);
		return SIMPLEDATEFORMAT.format(calendar.getTime());
	}

	/**
	 * 计算两个日期相差天数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 */
	public static int getDaySub(String beginDateStr,String endDateStr,String pattern){
		int day=0;
		if(null==beginDateStr||"".equals(beginDateStr)){
			return day;
		}
		if(null==endDateStr||"".equals(endDateStr)){
			return day;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date beginDate;
		Date endDate;
		try{
			beginDate = format.parse(beginDateStr);
			endDate= format.parse(endDateStr);
			long dayL=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
			day = Integer.parseInt(String.valueOf(dayL));
		} catch (ParseException e){
			System.out.println(e.toString());
			return 0;
		}
		return day;
	}

	public static String getDatetimeNDaysAgo(Date date,int offset,String pattern){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,offset);
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(calendar.getTime());
	}

	public static boolean isValidDate(String str){
		boolean convertSuccess = true;
		if(str.length() == 8){
			str = str.substring(0,4) + "-" + str.substring(4,6) + "-" + str.substring(6);
		}
		try {
			SIMPLEDATEFORMAT.setLenient(false);
			SIMPLEDATEFORMAT.parse(str);
		} catch (Exception e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}

	public static String formatDateString(String dateStr){
		if(StringUtils.isBlank(dateStr)){
			return "";
		}
		if(dateStr.length() == 10){
			return dateStr;
		}
		if(dateStr.length() < 10){
			String[] arr = dateStr.split("-");
			if(arr.length != 3){
				return dateStr;
			}
			arr[1] = arr[1].length()==1?"0"+arr[1]:arr[1];
			arr[2] = arr[2].length()==1?"0"+arr[2]:arr[2];
			dateStr = arr[0]+"-"+ arr[1]+"-"+arr[2];
			return dateStr;
		}
		return dateStr;
	}

}
