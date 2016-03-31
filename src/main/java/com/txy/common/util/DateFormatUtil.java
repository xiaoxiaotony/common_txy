package com.txy.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

public final class DateFormatUtil {
	private static DateFormat dateFormat;
	private static DateFormat dateTimeFormat;
	private static DateFormat timeFormat;

	private DateFormatUtil() {

	}

	public static String formatDate(Date date) {
		return null == date ? null : getDateformat().format(date);
	}

	public static Date parseDate(String source) {
		Date result = null;
		try {
			result = StringUtils.hasText(source) ? getDateformat().parse(source) : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static Date parseDate(String source,String patform) {
		Date result = null;
		try {
			result = StringUtils.hasText(source) ? getDateformat(patform).parse(source) : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String formateDatetime(Date datetime) {
		return null == datetime ? null : getDatetimeformat().format(datetime);
	}
	public static String formateDatetime(Date datetime,String fmt) {
		return null == datetime ? null : getDatetimeformat(fmt).format(datetime);
	}

	public static Date parseDatetime(String source) {
		Date result = null;
		try {
			result = StringUtils.hasText(source) ? getDatetimeformat().parse(source) : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String formateTime(Date time) {
		return null == time ? null : getTimeformat().format(time);
	}

	public static Date parseTime(String source) {
		Date result = null;
		try {
			result = StringUtils.hasText(source) ? getTimeformat().parse(source) : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	private static DateFormat getDateformat() {
		if (null == dateFormat) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		}
		return dateFormat;
	}
	private static DateFormat getDateformat(String patform) {
		DateFormat dateFormat = new SimpleDateFormat(patform);
		return dateFormat;
	}

	private static DateFormat getDatetimeformat() {
		if (null == dateTimeFormat) {
			dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		return dateTimeFormat;
	}
	
	private static DateFormat getDatetimeformat(String fmt) {
		if (null == dateTimeFormat) {
			dateTimeFormat = new SimpleDateFormat(fmt);
		}
		return dateTimeFormat;
	}

	private static DateFormat getTimeformat() {
		if (null == timeFormat) {
			timeFormat = new SimpleDateFormat("HH:mm:ss");
		}
		return timeFormat;
	}
	
	public static Date getFirstDayOfQuarter(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		int curMonth = cDay.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			cDay.set(Calendar.MONTH, Calendar.JANUARY);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			cDay.set(Calendar.MONTH, Calendar.APRIL);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			cDay.set(Calendar.MONTH, Calendar.JULY);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			cDay.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 得到本季度最后一天的日期
	 * 
	 * @Methods Name getLastDayOfQuarter
	 * @return Date
	 */
	public static Date getLastDayOfQuarter(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		int curMonth = cDay.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			cDay.set(Calendar.MONTH, Calendar.MARCH);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			cDay.set(Calendar.MONTH, Calendar.JUNE);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			cDay.set(Calendar.MONTH, Calendar.AUGUST);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			cDay.set(Calendar.MONTH, Calendar.DECEMBER);
		}
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}
	
	/**
	 * 得到本月第一天的日期
	 * 
	 * @Methods Name getFirstDayOfMonth
	 * @return Date
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 得到本月最后一天的日期
	 * 
	 * @Methods Name getLastDayOfMonth
	 * @return Date
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 获取指定日期所在周的周一
	 * 
	 * @Methods Name getMonday
	 * @return Date
	 */
	public static Date getMonday(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_WEEK, 2);// 老外将周日定位第一天，周一取第二天
		return cDay.getTime();
	}

	/**
	 * 获取指定日期所在周日
	 * 
	 * @Methods Name getSunday
	 * @return Date
	 */
	public static Date getSunday(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		if (Calendar.DAY_OF_WEEK == cDay.getFirstDayOfWeek()) { // 如果刚好是周日，直接返回
			return date;
		} else {// 如果不是周日，加一周计算
			cDay.add(Calendar.DAY_OF_YEAR, 7);
			cDay.set(Calendar.DAY_OF_WEEK, 1);
			System.out.println(cDay.getTime());
			return cDay.getTime();
		}
	}
	
	
	/**
	 * 得到本年第一天的日期
	 * 
	 * @Methods Name getFirstDayOfMonth
	 * @return Date
	 */
	public static Date getFirstDayOfYear(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_YEAR, 1);
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 得到本年最后一天的日期
	 * 
	 * @Methods Name getLastDayOfMonth
	 * @return Date
	 */
	public static Date getLastDayOfYear(Date date) {
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		cDay.set(Calendar.DAY_OF_YEAR, cDay.getActualMaximum(Calendar.DAY_OF_YEAR));
		System.out.println(cDay.getTime());
		return cDay.getTime();
	}
	
		/**
	    * <p>Title: getSpecifiedMonthAfter</p>
	    * <p>Description: 计算传入时间加上传入月份后的年月日</p>
	    * @param specifiedDay 年月日
	    * @param monthNum 要增加的月份数（传入正数相加，负数相减）
	    * @return 传入时间加上传入月份后的时间如传入（2014-03-27,3） 返回的则是2014-06-27
	    */
	    public static String getSpecifiedMonthAfter(String specifiedDay, int monthNum) {
	        
	        Calendar c = Calendar.getInstance();
	        Date date = null;
	        try {
	            date = new SimpleDateFormat("yyyy-MM").parse(specifiedDay);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        c.setTime(date);
	        int month = c.get(Calendar.MONTH);
	        c.set(Calendar.MONTH, month + monthNum);

	        String dayAfter = new SimpleDateFormat("yyyy-MM")
	                .format(c.getTime());
	        return dayAfter;
	    }
	
	
	public static int getSeason(Date date) {  
		  
        int season = 0;  
  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        int month = c.get(Calendar.MONTH);  
        switch (month) {  
        case Calendar.JANUARY:  
        case Calendar.FEBRUARY:  
        case Calendar.MARCH:  
            season = 1;  
            break;  
        case Calendar.APRIL:  
        case Calendar.MAY:  
        case Calendar.JUNE:  
            season = 2;  
            break;  
        case Calendar.JULY:  
        case Calendar.AUGUST:  
        case Calendar.SEPTEMBER:  
            season = 3;  
            break;  
        case Calendar.OCTOBER:  
        case Calendar.NOVEMBER:  
        case Calendar.DECEMBER:  
            season = 4;  
            break;  
        default:  
            break;  
        }  
        return season;  
    }  
	
	 public static int getYear(Date date) {  
	        Calendar c = Calendar.getInstance();  
	        c.setTime(date);  
	        int year = c.get(Calendar.YEAR);  
	        return year;  
	    }  
}
