package com.mg.core.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/**
 * 날짜관련 기능을 지원하는 클래스.
 *
 */
public class DateUtil {

	/**
	 * 현재 년도를 반환합니다.
	 *
	 * @return 현재 년도
	 */
	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 현재 월을 반환합니다. 그레고리안 캘린더에서는 월이 0에서 시작하므로, 1을 더하여 반환합니다.
	 *
	 * @return 현재 월 (1월=1, ..., 12월=12)
	 */
	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 현재 일자를 반환합니다.
	 *
	 * @return 현재 일자.
	 */
	public static int getDate() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * 현재 시각을 반환합니다. 12시간 형태로 반환됩니다.
	 *
	 * @return 현재 시각 (12시간 기준).
	 */
	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR);
	}

	/**
	 * 현재 시각을 24시간 형태로 반환합니다.
	 *
	 * @return 현재 시각 (24시간 기준).
	 */
	public static int get24Hour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 현재 분을 반환합니다.
	 *
	 * @return 현재 분.
	 */
	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	/**
	 * 현재 초를 반환합니다.
	 *
	 * @return 현재 초.
	 */
	public static int getSecond() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}

	/**
	 * 지정된 Date 객체를 주어진 패턴으로 포맷하여 문자열로 반환합니다.
	 *
	 * @param datePattern 포맷팅할 패턴 문자열
	 * @param date        포맷팅할 Date 객체
	 * @return 포맷팅된 날짜 문자열
	 */
	public static String getFormatDate(String datePattern, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern(datePattern);
		return formatter.format(date);
	}

	/**
	 * 현재 날짜를 주어진 패턴으로 포맷하여 문자열로 반환합니다.
	 *
	 * @param datePattern 포맷팅할 패턴 문자열
	 * @return 포맷팅된 현재 날짜 문자열
	 */
	public static String getFormatDate(String datePattern) {
		return DateUtil.getFormatDate(datePattern, new Date());
	}

	/**
	 * 주어진 문자열이 지정된 날짜 형식에 맞는지 검증합니다.
	 *
	 * @param s      문자열로 표현된 날짜
	 * @param format 날짜 형식
	 * @return 날짜 형식이 유효하면 true, 그렇지 않으면 false
	 */
	public static boolean isValidDate(String s, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
		Date date = null;
		try {
			date = formatter.parse(s);
		} catch (ParseException e) {
			return false;
		}

		if (!formatter.format(date).equals(s)) {
			return false;
		}
		return true;
	}

	/**
	 * 지정된 날짜 문자열에 일수를 더하거나 빼서 결과 날짜를 반환합니다.
	 *
	 * @param yymmdd 날짜 문자열 ("yyyyMMdd" 형식)
	 * @param offset 더하거나 빼고자 하는 일수
	 * @return 계산된 날짜 문자열
	 */
	public static String getAddDateString(String yymmdd, int offset) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Integer.parseInt(yymmdd.substring(0, 4)), Integer.parseInt(yymmdd.substring(4, 6)) - 1,
				Integer.parseInt(yymmdd.substring(6, 8)));
		rightNow.add(Calendar.DATE, offset);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		return formatter.format(rightNow.getTime());
	}

	/**
	 * 지정된 날짜 문자열에 주를 더하거나 빼서 결과 날짜를 반환합니다.
	 *
	 * @param yymmdd 날짜 문자열 ("yyyyMMdd" 형식)
	 * @param offset 더하거나 빼고자 하는 주 수
	 * @return 계산된 날짜 문자열
	 */
	public static String getAddWeekString(String yymmdd, int offset) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Integer.parseInt(yymmdd.substring(0, 4)), Integer.parseInt(yymmdd.substring(4, 6)) - 1,
				Integer.parseInt(yymmdd.substring(6, 8)));
		rightNow.add(Calendar.WEDNESDAY, offset);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		return formatter.format(rightNow.getTime());
	}

	/**
	 * 지정된 날짜 문자열에 월을 더하거나 빼서 결과 날짜를 반환합니다.
	 *
	 * @param yymmdd 날짜 문자열 ("yyyyMMdd" 형식)
	 * @param offset 더하거나 빼고자 하는 월 수
	 * @return 계산된 날짜 문자열 ("yyyy-MM-dd" 형식)
	 */
	public static String getAddMonthString(String yymmdd, int offset) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Integer.parseInt(yymmdd.substring(0, 4)), Integer.parseInt(yymmdd.substring(4, 6)) - 1,
				Integer.parseInt(yymmdd.substring(6)));

		rightNow.add(Calendar.MONTH, offset);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

		return formatter.format(rightNow.getTime());
	}

	/**
	 * 현재 시간을 리눅스 타임스탬프 형식의 5바이트 배열로 반환합니다.
	 *
	 * @return 현재 시간의 5바이트 배열
	 */
	public static byte[] longToByteArray() {

		long value = System.currentTimeMillis() / 1000;
		return new byte[] {
				(byte) (value >> 32),
				(byte) (value >> 24),
				(byte) (value >> 16),
				(byte) (value >> 8),
				(byte) value
		};
	}

	/**
	 * 주어진 날짜 문자열을 지정된 패턴의 날짜 형식으로 변환합니다.
	 *
	 * @param date    변환할 날짜 문자열
	 * @param pattern 적용할 날짜 형식
	 * @return 형식화된 날짜 문자열
	 * @throws Exception 날짜 형식 변환 중 발생한 예외
	 */
	public static String getFormatString(String date, String pattern) throws Exception {
		/*
		 * java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat
		 * (pattern, java.util.Locale.KOREA); String dateString =
		 * formatter.format(date); return dateString;
		 */
		String returnValue = null;
		String tempFormat = null;

		date = getNumber(date);

		int dateLength = date.length();

		try {
			switch (dateLength) {
				case 6:
					tempFormat = "yyyyMM";
					break;
				case 8:
					tempFormat = "yyyyMMdd";
					break;
				case 10:
					tempFormat = "yyyyMMddHH";
					break;
				case 12:
					tempFormat = "yyyyMMddHHmm";
					break;
				case 14:
					tempFormat = "yyyyMMddHHmmss";
					break;
				default:
					throw new Exception("날짜 값이 원하는 형식과 틀립니다. " + date);
			}

			SimpleDateFormat sdf = new SimpleDateFormat(tempFormat);
			SimpleDateFormat sdf2 = new SimpleDateFormat(pattern);

			returnValue = sdf2.format(sdf.parse(date));
		} catch (Exception e) {
			throw e;
		}

		return returnValue;
	}

	/**
	 * 주어진 문자열에서 숫자만 추출하여 반환합니다.
	 *
	 * @param extractDate 숫자를 추출할 문자열
	 * @return 숫자만 포함된 문자열
	 */
	public static String getNumber(String extractDate) {
		StringBuffer returnValue = new StringBuffer();
		Pattern pattern = Pattern.compile("\\d");
		Matcher matcher = pattern.matcher(extractDate);

		for (int i = 0; matcher.find(i); i = matcher.end())
			returnValue.append(extractDate.substring(matcher.start(), matcher.end()));

		return returnValue.toString();
	}

	/**
	 * 현재 요일을 한국어로 반환합니다.
	 *
	 * @return 현재 요일
	 */
	public static String getDay() {
		DateFormatSymbols symbol = new DateFormatSymbols(Locale.KOREA);

		String[] dayofweek = symbol.getWeekdays();

		int day = Calendar.getInstance(TimeZone.getTimeZone("JST")).get(Calendar.DAY_OF_WEEK);

		return (dayofweek[day]);
	}

	/**
	 * 오늘 날짜의 요일을 반환합니다. (1 = 월요일, ..., 7 = 일요일)
	 *
	 * @return 오늘의 요일에 해당하는 숫자
	 */
	public static int getIntDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int day = 0;
		switch (dayOfWeek) {
			case 0:
				day = 7;
				break;
			case 1:
				day = 1;
				break;
			case 2:
				day = 2;
				break;
			case 3:
				day = 3;
				break;
			case 4:
				day = 4;
				break;
			case 5:
				day = 5;
				break;
			case 6:
				day = 6;
				break;
		}

		// 0 == 일요일 , 6 == 토요일
		return day;
	}

	/**
	 * 현재 요일을 축약된 형태로 반환합니다. (예: 월, 화, 수 등)
	 *
	 * @return 현재 요일의 축약된 형태
	 */
	public static String getShortDay() {
		DateFormatSymbols symbol = new DateFormatSymbols(Locale.KOREA);

		String[] dayofweek = symbol.getShortWeekdays();

		int day = Calendar.getInstance(TimeZone.getTimeZone("JST")).get(Calendar.DAY_OF_WEEK);

		return (dayofweek[day]);
	}

	/**
	 * 주어진 Date 객체의 날짜를 지정된 형식으로 문자열로 반환합니다.
	 *
	 * @param srcDate 변환할 날짜
	 * @param format  날짜 형식
	 * @return 형식화된 날짜 문자열
	 */
	public static String getDateString(Date srcDate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
		return formatter.format(srcDate);
	}

	/**
	 * 현재 날짜를 지정된 형식으로 문자열로 반환합니다.
	 *
	 * @param format 날짜 형식
	 * @return 형식화된 날짜 문자열
	 */
	public static String getDateString(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 현재 날짜를 "yyyyMMdd" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 날짜 문자열
	 */
	public static String getDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 현재 날짜를 "yyyy-MM-dd" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 날짜 문자열
	 */
	public static String getDateString2() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 현재 날짜를 "yyyy/MM/dd" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 날짜 문자열
	 */
	public static String getDateString3() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 현재 시간을 "HHmmss" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 시간 문자열
	 */
	public static String getTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 현재 시간을 "HH:mm:ss" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 시간 문자열
	 */
	public static String getTimeString2() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 현재 날짜와 시간을 "yyyy-MM-dd HH:mm:ss" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 날짜와 시간 문자열
	 */
	public static String getTimeStampString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 주어진 Date 객체의 날짜와 시간을 "yyyy-MM-dd-HH:mm:ss:SSS" 형식으로 반환합니다.
	 *
	 * @param dt 지정한 Date 객체
	 * @return 형식화된 시간 문자열
	 */
	public static String getTimeStampString(Date dt) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SSS", Locale.KOREA);
		return formatter.format(dt);
	}

	/**
	 * 현재 날짜와 시간을 "yyyy-MM-dd HH:mm:ss:SSS" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 시간 문자열
	 */
	public static String getTimeStampString2() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 주어진 Date 객체의 날짜와 시간을 "yyyy-MM-dd HH:mm:ss:SSS" 형식으로 반환합니다.
	 *
	 * @param dt 지정한 Date 객체
	 * @return 형식화된 시간 문자열
	 */
	public static String getTimeStampString2(Date dt) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.KOREA);
		return formatter.format(dt);
	}

	/**
	 * 현재 날짜와 시간을 "yyyyMMddHHmmssSSS" 형식으로 반환합니다.
	 *
	 * @return 형식화된 현재 시간 문자열
	 */
	public static String getTimeStamp2String() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 주어진 Date 객체의 날짜와 시간을 "yyyyMMddHHmmssSSS" 형식으로 반환합니다.
	 *
	 * @param dt 지정한 Date 객체
	 * @return 형식화된 시간 문자열
	 */
	public static String getTimeStamp2String(Date dt) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
		return formatter.format(dt);
	}

	/**
	 * 현재 날짜와 시간을 "yyyyMMddHHmmss" 형식으로 반환합니다.
	 *
	 * @return 현재 시간의 문자열 형태
	 */
	public static String getTimeStamp3String() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 주어진 Date 객체의 날짜와 시간을 "yyyyMMddHHmmss" 형식으로 반환합니다.
	 *
	 * @param dt 지정한 Date 객체
	 * @return 지정된 시간의 문자열 형태
	 */
	public static String getTimeStamp3String(Date dt) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
		return formatter.format(dt);
	}

	/**
	 * 현재 날짜와 시간을 "yyyyMMddHHmm" 형식으로 반환합니다.
	 *
	 * @return 현재 시간의 문자열 형태
	 */
	public static String getTimeStamp4String() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 현재 날짜와 시간을 "yyyy-MM-dd HH:mm" 형식으로 반환합니다.
	 *
	 * @return 현재 시간의 문자열 형태
	 */
	public static String getTimeStamp5String() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
		return formatter.format(new Date());
	}

	/**
	 * 주어진 Date 객체의 날짜와 시간을 "yyyyMMddHHmm" 형식으로 반환합니다.
	 *
	 * @param dt 지정한 Date 객체
	 * @return 지정된 시간의 문자열 형태
	 */
	public static String getTimeStamp4String(Date dt) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA);
		return formatter.format(dt);
	}

	/**
	 * 문자열로 표현된 날짜를 Date 객체로 변환합니다.
	 *
	 * @param dt 날짜와 시간을 나타내는 문자열 ("yyyy-MM-dd HH:mm" 형식)
	 * @return 변환된 Date 객체, 변환 실패 시 null 반환
	 */
	public static Date getStringToDate(String dt) {
		Date to = null;
		try {
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			to = transFormat.parse(dt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return to;
	}

	/**
	 * 주어진 시간이 특정 시간 범위 내에 있는지 검사합니다.
	 *
	 * @param time     검사할 시간
	 * @param fromTime 시작 시간
	 * @param toTime   종료 시간
	 * @return 범위 내에 있으면 true, 그렇지 않으면 false
	 */
	public static boolean isTrueTime(int time, int fromTime, int toTime) {
		if (time >= fromTime && time < toTime)
			return true;
		else
			return false;
	}

	/**
	 * 주어진 날짜 문자열의 형식을 변경합니다.
	 *
	 * @param oldType 변경 전 날짜 형식
	 * @param newType 변경 후 날짜 형식
	 * @param date    변환할 날짜 문자열
	 * @return 형식이 변경된 날짜 문자열, 변환 실패 시 원본 문자열 반환
	 */
	public static String _getDateFormat(String oldType, String newType, String date) {
		try {
			SimpleDateFormat oldDate = new SimpleDateFormat(oldType, Locale.KOREA);
			SimpleDateFormat newdate = new SimpleDateFormat(newType, Locale.KOREA);
			return newdate.format(oldDate.parse(date));
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 지정된 날짜에 일 수를 더하거나 뺀 날짜를 반환합니다.
	 *
	 * @param date   기준 날짜 ("yyyyMMdd" 형식)
	 * @param offset 더하거나 뺄 일 수
	 * @return 계산된 날짜
	 */
	public static String getDefaultDate(String date, int offset) {

		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1,
				Integer.parseInt(date.substring(6, 8)));

		cal.add(Calendar.DATE, offset);

		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH) + 1;
		int iDate = cal.get(Calendar.DATE);

		String sNewDate = "" + iYear;
		if (iMonth < 10)
			sNewDate += "0" + iMonth;
		else
			sNewDate += iMonth;
		if (iDate < 10)
			sNewDate += "0" + iDate;
		else
			sNewDate += iDate;

		return sNewDate;
	}

	/**
	 * 지정된 날짜에 일 수를 더하거나 빼서 결과 날짜를 반환합니다.
	 *
	 * @param date   기준 날짜 ("yyyy-MM-dd" 형식)
	 * @param offset 추가하거나 뺄 일 수 (음수일 경우 날짜를 뺌)
	 * @return 계산된 날짜
	 */
	public static String getDefaultDate2(String date, int offset) {

		Calendar cal = Calendar.getInstance();

		cal.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)) - 1,
				Integer.parseInt(date.substring(8, 10)));

		cal.add(Calendar.DATE, offset);

		int iYear = cal.get(Calendar.YEAR);
		int iMonth = cal.get(Calendar.MONTH) + 1;
		int iDate = cal.get(Calendar.DATE);

		String sNewDate = iYear + "-";

		if (iMonth < 10)
			sNewDate += "0" + iMonth;
		else
			sNewDate += iMonth;
		sNewDate += "-";

		if (iDate < 10)
			sNewDate += "0" + iDate;
		else
			sNewDate += iDate;

		return sNewDate;
	}

	/**
	 * 두 날짜의 차이를 일 수로 계산하여 반환합니다. 입력 형식은 yyyyMMdd여야 합니다.
	 *
	 * @param startDate 시작 일자
	 * @param endDate   종료 일자
	 * @return 두 날짜 사이의 일 수 차이
	 * @throws Exception 날짜 파싱 중 발생한 예외
	 */
	public static String getDistanceDates(String startDate, String endDate) throws Exception {
		String returnValue = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);

		long sTime = sDate.getTime();
		long eTime = eDate.getTime();
		long dTime = eTime - sTime;

		returnValue = (dTime / 1000 / 60 / 60 / 24) + "";

		return returnValue;
	}

	/**
	 * 두 Date 객체의 차이를 일 수로 계산하여 반환합니다.
	 *
	 * @param startDate 시작 일자
	 * @param endDate   종료 일자
	 * @return 두 날짜 사이의 일 수 차이
	 * @throws Exception 날짜 계산 중 발생한 예외
	 */
	public static String getDistanceDates(Date startDate, Date endDate) throws Exception {
		String returnValue = null;

		long sTime = startDate.getTime();
		long eTime = endDate.getTime();
		long dTime = eTime - sTime;

		returnValue = (dTime / 1000 / 60 / 60 / 24) + "";

		return returnValue;
	}

	/**
	 * 두 날짜의 차이를 일 수로 계산하여 반환합니다. 입력 형식은 yyyyMMdd여야 합니다.
	 *
	 * @param startDate 시작 일자
	 * @param endDate   종료 일자
	 * @return 두 날짜 사이의 일 수 차이
	 * @throws Exception 날짜 파싱 중 발생한 예외
	 */
	public static long getDistanceDates1(String startDate, String endDate) throws Exception {
		long returnValue = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);

		long sTime = sDate.getTime();
		long eTime = eDate.getTime();
		long dTime = eTime - sTime;

		returnValue = (dTime / 1000 / 60 / 60 / 24);

		return returnValue;
	}

	/**
	 * 두 Date 객체의 차이를 일 수로 계산하여 반환합니다.
	 *
	 * @param startDate 시작 일자
	 * @param endDate   종료 일자
	 * @return 두 날짜 사이의 일 수 차이
	 * @throws Exception 날짜 계산 중 발생한 예외
	 */
	public static long getDistanceDates1(Date startDate, Date endDate) throws Exception {
		long returnValue = 0;

		long sTime = startDate.getTime();
		long eTime = endDate.getTime();
		long dTime = eTime - sTime;

		returnValue = (long) Math.floor(dTime / 1000 / 60 / 60 / 24);

		return returnValue;
	}

	/**
	 * 두 날짜의 차이를 일 수로 계산하여 반환합니다. 날짜 형식은 format 매개변수로 지정됩니다.
	 *
	 * @param startDate 시작 일자
	 * @param endDate   종료 일자
	 * @param format    날짜 형식 지정 문자열
	 * @return 두 날짜 사이의 일 수 차이 문자열
	 * @throws Exception 날짜 파싱 및 계산 중 발생한 예외
	 */
	public static String getDistanceDates(String startDate, String endDate, String format) throws Exception {
		String returnValue = null;

		SimpleDateFormat sdf = new SimpleDateFormat(format);

		Date sDate = sdf.parse(startDate);
		Date eDate = sdf.parse(endDate);

		long sTime = sDate.getTime();
		long eTime = eDate.getTime();
		long dTime = eTime - sTime;

		returnValue = (dTime / 1000 / 60 / 60 / 24) + "";

		return returnValue;
	}

	/**
	 * 두 날짜의 차이를 시간 단위로 계산합니다.
	 *
	 * @param startDate 시작 날짜
	 * @param endDate   종료 날짜
	 * @return 두 날짜 사이의 시간 차이
	 * @throws Exception 날짜 처리 중 발생한 예외
	 */
	public static float getDistanceTime(Date startDate, Date endDate) throws Exception {
		float returnValue = 0;

		long sTime = startDate.getTime();
		long eTime = endDate.getTime();
		long dTime = eTime - sTime;

		returnValue = (dTime / 1000 / 60 / 60);

		return returnValue;
	}

	/**
	 * 입력받은 날짜와 현재 날짜의 차이를 분으로 계산합니다.
	 *
	 * @param startDate 시작일자 ("yyyy-MM-dd HH:mm:ss" 형식)
	 * @return 두 날짜의 차이(분)
	 * @throws Exception 날짜 처리 중 발생한 예외
	 */
	public static Long getDistanceTime(String startDate) throws Exception {

		// 시간 설정
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startday = sf.parse(startDate, new ParsePosition(0));

		long startTime = startday.getTime();

		// 현재의 시간 설정
		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		long endTime = endDate.getTime();

		long mills = endTime - startTime;

		// 분으로 변환
		long min = mills / 60000;

		return min;
	}

	/**
	 * 입력받은 날짜와 현재 날짜의 차이를 분 혹은 초로 계산합니다.
	 *
	 * @param startDate 시작일자 ("yyyy-MM-dd HH:mm:ss" 형식)
	 * @param type      결과 유형 ("min" 또는 "sec")
	 * @return 두 날짜의 차이(분 또는 초)
	 * @throws Exception 날짜 처리 중 발생한 예외
	 */
	public static Long getDistanceTime(String startDate, String type) throws Exception {

		// 시간 설정
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startday = sf.parse(startDate, new ParsePosition(0));

		long startTime = startday.getTime();

		// 현재의 시간 설정
		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();
		long endTime = endDate.getTime();

		long mills = endTime - startTime;

		System.out.println(mills);
		long min = mills / 60000;
		if (type.equals("sec")) {
			min = (mills / 1000) % 3600;
		}

		return min;
	}

	/**
	 * 주어진 Date 객체를 원하는 형식의 문자열로 변환합니다.
	 *
	 * @param srcDate 변환할 원본 Date 객체
	 * @param format  원하는 날짜 형식
	 * @return 변환된 날짜 문자열, Date 객체가 null인 경우 빈 문자열 반환
	 */
	public static String getDateFormatStr(Date srcDate, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		if (srcDate != null) {
			return sdf.format(srcDate);
		} else {
			return "";
		}
	}

	/**
	 * 파일의 CRC32 체크섬 값을 계산합니다.
	 *
	 * @param filePath 저장된 파일의 경로
	 * @return 계산된 CRC32 체크섬 값
	 */
	public static long getCRC32Value(String filePath) {
		CRC32 crc = new CRC32();

		try {

			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(filePath));
			byte buffer[] = new byte[32768];
			int length = 0;

			while ((length = in.read(buffer)) >= 0)
				crc.update(buffer, 0, length);

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return crc.getValue();
	}

	/**
	 * 바이트 배열의 CRC32 체크섬 값을 계산합니다.
	 *
	 * @param value 계산할 바이트 배열
	 * @return 계산된 CRC32 체크섬 값
	 */
	public static long getCRC32Value(byte[] value) {
		CRC32 crc = new CRC32();
		crc.update(value);
		return crc.getValue();
	}

	/**
	 * 주어진 날짜를 특정 형식으로 포맷하여 반환합니다.
	 *
	 * @param date   원본 날짜 문자열
	 * @param format 원하는 날짜 형식
	 * @return 포맷된 날짜 문자열
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static String getFormattedDate(String date, String format) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
		cal.setTime(sdf.parse(date));
		return sdf.format(cal.getTime());
	}

	/**
	 * 주어진 날짜가 현재 연도 및 월과 일치하는지 여부를 확인합니다.
	 *
	 * @param date 검사할 날짜 (형식: yyyy-MM)
	 * @return 현재 연월과 일치하면 true, 그렇지 않으면 false
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static boolean isThisMonth(String date) throws ParseException {
		Calendar getCal = Calendar.getInstance();
		Calendar setCal = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.KOREA);
		setCal.setTime(sdf.parse(date));

		if (getCal.get(Calendar.YEAR) == setCal.get(Calendar.YEAR)
				&& getCal.get(Calendar.MONTH) == setCal.get(Calendar.MONTH))
			return true;

		return false;
	}

	/**
	 * 주어진 날짜의 해당 월에 있는 전체 일수를 반환합니다.
	 *
	 * @param date 날짜 (형식: yyyy-MM)
	 * @return 해당 월의 총 일수
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static int getCountDaysOfMonth(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.KOREA);
		cal.setTime(sdf.parse(date));
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 주어진 연도의 날짜 수를 계산합니다.
	 *
	 * @param date 연도 (형식: yyyy)
	 * @return 해당 연도의 총 일수
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static int getCountDaysOfYear(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.KOREA);
		cal.setTime(sdf.parse(date));
		return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 두 날짜 사이의 월 차이를 계산합니다.
	 *
	 * @param stratDate 시작 날짜 (형식: yyyy-MM)
	 * @param endDate   종료 날짜 (형식: yyyy-MM)
	 * @return 두 날짜 사이의 월 차이
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static int getMonthDifference(String stratDate, String endDate) throws ParseException {
		int strtYear = Integer.parseInt(stratDate.substring(0, 4));
		int strtMonth = Integer.parseInt(stratDate.substring(5, 7));

		int endYear = Integer.parseInt(endDate.substring(0, 4));
		int endMonth = Integer.parseInt(endDate.substring(5, 7));

		int month = (endYear - strtYear) * 12 + (endMonth - strtMonth);

		return month;
	}

	/**
	 * 주어진 날짜의 해당 월의 마지막 날을 반환합니다.
	 *
	 * @param date 입력된 날짜 (yyyyMMdd 또는 yyyy-MM-dd 형식)
	 * @return 해당 월의 마지막 날
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static String getLastDayOfMonth(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = null;
		try {
			switch (date.length()) {
				case 8:
					sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
					break;
				case 10:
					sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
					break;
				default:
					throw new Exception("날짜 값이 원하는 형식과 틀립니다. " + date);
			}
			cal.setTime(sdf.parse(date));
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 주어진 날짜 문자열을 새로운 형식으로 변환합니다.
	 *
	 * @param date 원본 날짜 문자열 (yyyyMMdd 형식)
	 * @return 변환된 날짜 문자열 (yyyy.MM.dd 형식)
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static String getNewDateFormat(String date) throws ParseException {
		SimpleDateFormat original_format = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat new_format = new SimpleDateFormat("yyyy.MM.dd");
		String new_date = null;
		try {
			Date original_date = original_format.parse(date);

			new_date = new_format.format(original_date);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return new_date;
	}

	/**
	 * 주어진 날짜의 해당 월의 첫 번째 날을 반환합니다.
	 *
	 * @param date 입력된 날짜 (yyyyMMdd 또는 yyyy-MM-dd 형식)
	 * @return 해당 월의 첫 번째 날
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static String getFirstDayOfMonth(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = null;
		try {
			switch (date.length()) {
				case 8:
					sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
					break;
				case 10:
					sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
					break;
				default:
					throw new Exception("날짜 값이 원하는 형식과 틀립니다. " + date);
			}
			cal.setTime(sdf.parse(date));
			cal.set(Calendar.DAY_OF_MONTH, 1);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 주어진 날짜의 해당 년의 첫 번째 날을 반환합니다.
	 *
	 * @param date 입력된 날짜 (yyyyMMdd 또는 yyyy-MM-dd 형식)
	 * @return 해당 연도의 첫 번째 날
	 * @throws ParseException 날짜 형식이 올바르지 않을 경우 발생
	 */
	public static String getFirstDayOfYear(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = null;
		try {

			switch (date.length()) {
				case 8:
					sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
					break;
				case 10:
					sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
					break;
				default:
					throw new Exception("날짜 값이 원하는 형식과 틀립니다. " + date);
			}
			cal.setTime(sdf.parse(date));
			cal.set(Calendar.DAY_OF_YEAR, 1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 주어진 날짜의 해당 년도의 마지막 날을 반환합니다.
	 *
	 * @param date 입력받은 날짜 (yyyyMMdd 또는 yyyy-MM-dd 형식)
	 * @return 해당 년도의 마지막 날
	 * @throws ParseException 날짜 형식이 잘못되었을 경우 발생
	 */
	public static String getLastDayOfYear(String date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = null;
		try {
			switch (date.length()) {
				case 8:
					sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
					break;
				case 10:
					sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
					break;
				default:
					throw new Exception("날짜 값이 원하는 형식과 틀립니다. " + date);
			}
			cal.setTime(sdf.parse(date));
			cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 주어진 날짜에 지정된 필드의 값을 더한 날짜를 반환합니다.
	 *
	 * @param date   기준 날짜
	 * @param format 날짜 형식
	 * @param field  연산을 적용할 필드 (예: Calendar.MONTH)
	 * @param amount 더할 값
	 * @return 연산 후의 날짜
	 * @throws ParseException 날짜 형식이 잘못되었을 경우 발생
	 */
	public static String getAddedDate(String date, String format, int field, int amount) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
		cal.setTime(sdf.parse(date));
		cal.add(field, amount);
		return sdf.format(cal.getTime());
	}

	/**
	 * 주어진 날짜를 연, 월, 일로 분리하여 Map으로 반환합니다.
	 *
	 * @param date 날짜 (yyyy-MM-dd 형식)
	 * @return Map 객체, 키는 "year", "month", "day"
	 * @throws ParseException 날짜 형식이 잘못되었을 경우 발생
	 */
	public static Map<String, String> getSplitDateField(String date) throws ParseException {
		Map<String, String> rMap = new HashMap<String, String>();

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		cal.setTime(sdf.parse(date));

		rMap.put("year", Integer.toString(cal.get(Calendar.YEAR)));
		rMap.put("month", Integer.toString(cal.get(Calendar.MONTH) + 1));
		rMap.put("day", Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));

		return rMap;
	}

}
