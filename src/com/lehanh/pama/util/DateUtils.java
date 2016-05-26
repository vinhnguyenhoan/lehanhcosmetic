package com.lehanh.pama.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class DateUtils {

	public static final int calculateAge(Date bDay) {
		LocalDate birthdate = new LocalDate(bDay);
		LocalDate now = new LocalDate();// Today's date
		Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
		return period.getYears();
	}
	
	public static int calculateDate(Date date, Date beforeDate) {
		LocalDate lD = new LocalDate(date);
		LocalDate bD = new LocalDate(beforeDate);
		Period period = new Period(bD, lD, PeriodType.days());
		return period.getDays();
	}
	
	public static Date getDate(int year, int month, int day) {
		try {
			Date result = new DateTime(year, month, day, 0, 0, 0, 0).toDate();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	public static int[] getDate(Date birthDay) {
		try {
			DateTime bd = new DateTime(birthDay);
			return new int[] {bd.getYear(), bd.getMonthOfYear(), bd.getDayOfWeek()};
		} catch (Exception e) {
			return new int[] {0, 0, 0};
		}
	}

	private static final SimpleDateFormat spDF = new SimpleDateFormat("dd/MM/yyyy");
	
	public static Date convertDateDataType(String dateAsText) throws ParseException {
		if (StringUtils.isBlank(dateAsText)) {
			return null;
		}
		return spDF.parse(dateAsText);
	}

	public static String convertDateDataType(Date date) {
		if (date == null) {
			return null;
		}
		return spDF.format(date);
	}
	
	public static String convertDateDataType(Calendar date) {
		if (date == null) {
			return null;
		}
		return spDF.format(date.getTime());
	}
	
	public static final DateFormat UTIL_DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	public static final DateFormat SQL_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	public static java.sql.Date utilDateToSqlDate(java.util.Date uDate) throws ParseException {
		if (uDate == null) {
			return null;
		}
		return java.sql.Date.valueOf(SQL_DATE_FORMATTER.format(uDate));
	}

	public static java.util.Date sqlDateToutilDate(java.sql.Date sDate) throws ParseException {
		if (sDate == null) {
			return null;
		}
		return (java.util.Date) UTIL_DATE_FORMATTER.parse(UTIL_DATE_FORMATTER.format(sDate));
	}
	
	public static void main(String[] args) throws ParseException {
		Calendar g1 = GregorianCalendar.getInstance();
		g1.set(2015, 9, 18);

		Calendar g2 = GregorianCalendar.getInstance();
		g2.set(2015, 9, 30);

		System.out.println(calculateDate(g2.getTime(), g1.getTime()));
		
		
		Calendar g = GregorianCalendar.getInstance();
		g.set(1985, 9, 18);
		System.out.println(calculateAge(g.getTime()));
		
		Object r = convertDateDataType((String) null);
		System.out.println(r);
		
		r = convertDateDataType("11/11/2011");
		System.out.println(r);
		
		r = convertDateDataType((Date) r);
		System.out.println(r);
	}

}
