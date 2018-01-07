package com.example.easy.commons.helper;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.tuple.Pair;

public class Utility
{
	static SecureRandom secureRandom = new SecureRandom();

	//private static AtomicReference currentTime = new AtomicReference<>(System.currentTimeMillis());

	private static AtomicLong seed= new AtomicLong();

	public static int daysBetween(Date d1, Date d2)
	{
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static Date getCurrentDate(String format)
	{
		// 2017-01-02 15:00:40.0
		try
		{
			String dateInString = new SimpleDateFormat(format).format(getCurrentDate());

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date parsedDate = formatter.parse(dateInString);

			return parsedDate;
		} catch (Exception exp)
		{

		}
		return getCurrentDate();
	}
	
	public static Date getDateFromString(String format, String date)
	{
	    DateFormat df = new SimpleDateFormat(format); 
	    Date retDate = null;
	    try {
	        retDate = df.parse(date);
	        //String newDateString = df.format(startDate);
	        //System.out.println(newDateString);
	    } catch (ParseException e) {
	        //e.printStackTrace();
	    }
	    
	    return retDate;
	}

	public static String getDateInFormat(String format, Date date)
	{
		if (format == null || format.isEmpty())
		{
			format = "MMMM-dd-yyyy";
		}
		// 2017-01-02 15:00:40.0
		// MMMM dd, yyyy Jan 19 2014
		// dd MMMMM yyyy 12 Jan 2017
		try
		{
			String dateInString = new SimpleDateFormat(format).format(date);

			return dateInString;

		} catch (Exception exp)
		{

		}
		return "";
	}

	public static Date getCurrentDate()
	{

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return calendar.getTime();
		// return new Date();
	}

	public static Date addTimeInCurrentDate(long time)
	{
		long timestamp = System.currentTimeMillis();
		timestamp = timestamp + time;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		return calendar.getTime();
		// return new Date();
	}

	public static Date addDaysInCurrentDate(int days)
	{
		return addTimeInCurrentDate(days * 24 * 60 * 60 * 1000);

	}
	public static Date addYearInCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		cal.add(Calendar.YEAR, 1); // to get previous year add -1
		return cal.getTime();

	}

	public static Pair<Date, Date> getCurrentMonthDateRange()
	{
		Date begining, end;

		{
			Calendar calendar = getCalendarForNow();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			setTimeToBeginningOfDay(calendar);
			begining = calendar.getTime();
		}

		{
			Calendar calendar = getCalendarForNow();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			setTimeToEndofDay(calendar);
			end = calendar.getTime();
		}

		return Pair.of(begining, end);
	}

	public static Pair<Date, Date> getCurrentYearDateRange()
	{
		Date begining, end;

		{
			Calendar calendar = getCalendarForNow();
			calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
			setTimeToBeginningOfDay(calendar);
			begining = calendar.getTime();
		}

		{
			Calendar calendar = getCalendarForNow();
			calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
			setTimeToEndofDay(calendar);
			end = calendar.getTime();
		}

		return Pair.of(begining, end);
	}

	private static Calendar getCalendarForNow()
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		return calendar;
	}

	private static void setTimeToBeginningOfDay(Calendar calendar)
	{
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	private static void setTimeToEndofDay(Calendar calendar)
	{
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	}

	public static String nextSessionId()
	{
		return new BigInteger(256, secureRandom).toString(32);
	}

	public static int getRandomCode()
	{
		return secureRandom.nextInt((99999 - 10000) + 1) + 10000;
	}

	public static Long nextId()
	{

		return (long) Math.floor(Math.random() * 500000000L) + 10000000L;
	}

	

	public static long getNextUniqueIndex() {
	    return seed.getAndIncrement();
	}

	public static String humanReadableByteCount(long bytes, boolean si)
	{
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
		{
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static void main(String[] args)
	{
		Pair datePair = new Utility().getCurrentYearDateRange();
		Object startDate = datePair.getLeft();
		Object endDate = datePair.getRight();
		System.out.println(datePair);

		System.out.println(Utility.getRandomCode());

		System.out.println(Utility.nextSessionId());

		System.out.println("About");

		System.out.println(Utility.getNextUniqueIndex());
		System.out.println(Utility.getNextUniqueIndex());
		System.out.println(Utility.getNextUniqueIndex());
		System.out.println(Utility.getNextUniqueIndex());

	}

}
