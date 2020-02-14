package org.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class DateUtil {

	/**
	 * Util of Date
	 * 
	 * @author ZP
	 */
	public static String DateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringDate = sdf.format(date);
		return stringDate;
	}

	public static String DateToString(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringDate = sdf.format(date);
		return stringDate;
	}

	public static String DateToStringNoHours(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String stringDate = sdf.format(date);
		return stringDate;
	}

	// 获取以当天为基准指定天数之前的日期
	public static String getDateBeforeDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(System.currentTimeMillis()));
		cal.add(Calendar.DATE, -days);
		return DateToString(cal.getTime());
	}

	public static String dateFormatter(java.util.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate;
		java.util.Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return day;
	}
}
