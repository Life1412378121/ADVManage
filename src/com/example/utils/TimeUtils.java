/*
 * Copyright (C) 2012-2015 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.TextUtils;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"HH:mm:ss");
	public static final SimpleDateFormat DEFAULT_MIN_FORMAT = new SimpleDateFormat(
			"mm:ss");
	public static final SimpleDateFormat INT_DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyyMMdd");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy/MM/dd");
	public static final SimpleDateFormat DEL_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final SimpleDateFormat INT_HOUR_FORMAT = new SimpleDateFormat(
			"HH");
	public static final SimpleDateFormat Y_FORMAT_DATE = new SimpleDateFormat(
			"yyyy");
	public static final SimpleDateFormat M_FORMAT_DATE = new SimpleDateFormat(
			"MM");
	public static final SimpleDateFormat D_FORMAT_DATE = new SimpleDateFormat(
			"dd");

	private TimeUtils() {
		throw new AssertionError();
	}

	/**
	 * long time to int
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static int getCurrentHourInInt(long timeInMillis,
			SimpleDateFormat dateFormat) {
		String date = dateFormat.format(new Date(timeInMillis));
		int time = 0;
		if (!TextUtils.isEmpty(date)) {
			time = Integer.parseInt(date);
		}
		return time;
	}

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to int
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static long getCurrentDateInLong(long timeInMillis,
			SimpleDateFormat dateFormat) {
		String date = dateFormat.format(new Date(timeInMillis));
		long time = 0;
		if (!TextUtils.isEmpty(date)) {
			time = Long.parseLong(date);
		}
		return time;
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * 获取当前时间int
	 * 
	 * @return
	 */
	public static int getLocalTimeInInt() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			String format = INT_DATE_FORMAT_DATE.format(calendar.getTime());
			if (!TextUtils.isEmpty(format)) {
				return Integer.parseInt(format);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据不同的模式，获取当前时间int
	 * 
	 * @return
	 */
	public static int getLocalTimeInInt(SimpleDateFormat mFormat) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			String format = mFormat.format(calendar.getTime());
			if (!TextUtils.isEmpty(format)) {
				return Integer.parseInt(format);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
