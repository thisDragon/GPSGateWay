package com.analog.data.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import jodd.util.StringUtil;

/**
 * 日期时间操作的实用类
 * 
 * @author yexuhui 2012-7-26 上午10:08:12
 */
public class DateUtils {
	/*** yyyy-MM-dd */
	public static final String SIMPLE_FORMAT = "yyyy-MM-dd";
	/*** yyyy_MM_dd */
	public static final String SIMPLE_FORMAT2 = "yyyy_MM_dd";
	/*** yyyy-MM-dd HH:mm:ss */
	public static final String NORMAL_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/*** yyyy-MM-dd HH:mm:ss */
	public static final String NORMAL_FORMAT2 = "yyyy-MM-dd HH:mm";
	/*** yyyy-MM-dd HH:mm:ss.SSS */
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	/*** HH:mm:ss */
	public static final String TIME_FORMAT = "HH:mm:ss";
	/*** yyyyMMddHHmmss */
	public static final String NOT_FORMAT = "yyyyMMddHHmmss";
	/*** yyyy-MM ***/
	public static final String YEAR_MONTH = "yyyy-MM";

	/**
	 * 获取当前时间格式化表示
	 * 
	 * @param format
	 *            格式化字符串
	 * @return
	 */
	public static String getNowFormatTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static Date getNowTime() {
		return new Date();
	}

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static Timestamp getNowTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 获取某个基准时间往前或往后的x年的时间，往前设置x为负数
	 * 
	 * @param date
	 *            基准时间
	 * @param x
	 *            调校的年数
	 * @return 调校后的时间
	 * @author yexuhui
	 * @date 2013-5-6 下午3:39:34
	 */
	public static Date getNextYear(Date date, int x) {
		return getNextDate(date, x, Calendar.MONTH);
	}
	
	public static Timestamp getCurrtentTimeByTime(long time){
		SimpleDateFormat format =new  SimpleDateFormat(DEFAULT_FORMAT);
    	String simpleTime = format.format(new Date(time));
    	return Timestamp.valueOf(simpleTime);
	}

	/**
	 * 获取某个基准时间往前或往后的x月的时间，往前设置x为负数
	 * 
	 * @param date
	 *            基准时间
	 * @param x
	 *            调校的月数
	 * @return 调校后的时间
	 * @author yexuhui
	 * @date 2013-5-6 下午3:39:34
	 */
	public static Date getNextMonth(Date date, int x) {
		return getNextDate(date, x, Calendar.MONTH);
	}

	/**
	 * 获取某个基准时间往前或往后的x天的时间，往前设置x为负数
	 * 
	 * @param date
	 *            基准时间
	 * @param x
	 *            调校的天数
	 * @return 调校后的时间
	 * @author yexuhui
	 * @date 2013-5-6 下午3:39:34
	 */
	public static Date getNextDay(Date date, int x) {
		return getNextDate(date, x, Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取某个基准时间往前或往后的x小时的时间，往前设置x为负数
	 * 
	 * @param date
	 *            基准时间
	 * @param x
	 *            调校的小时数
	 * @return 调校后的时间
	 * @author yexuhui
	 * @date 2013-5-6 下午3:39:34
	 */
	public static Date getNextHour(Date date, int x) {
		return getNextDate(date, x, Calendar.HOUR);
	}

	/**
	 * 获取某个基准时间往前或往后的x分钟的时间，往前设置x为负数
	 * 
	 * @param date
	 *            基准时间
	 * @param x
	 *            调校的分钟数
	 * @return 调校后的时间
	 * @author yexuhui
	 * @date 2013-5-6 下午3:39:34
	 */
	public static Date getNextMinute(Date date, int x) {
		return getNextDate(date, x, Calendar.MINUTE);
	}

	/**
	 * 获取某个基准时间往前或往后的x秒的时间，往前设置x为负数
	 * 
	 * @param date
	 *            基准时间
	 * @param x
	 *            调校的秒数
	 * @return 调校后的时间
	 * @author yexuhui
	 * @date 2013-5-6 下午3:39:34
	 */
	public static Date getNextSecond(Date date, int x) {
		return getNextDate(date, x, Calendar.SECOND);
	}

	/**
	 * 获取下一个日期时间
	 * 
	 * @param dateStart
	 *            开始的日期时间
	 * @param field
	 *            Calendar字段类别，DAY_OF_YEAR、DAY_OF_MONTH、HOUR等
	 * @param amount
	 *            增量
	 * @return 日期时间 yexuhui 2012-8-2 上午10:43:32
	 */
	public static Date getNextDate(Date dateStart, int amount, int field) {
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(dateStart);
		calStart.add(field, amount);
		return calStart.getTime();
	}

	/**
	 * 字符串转化成时间
	 * 
	 * @param dateString
	 * @param dataFormat
	 * @return
	 * @author yexuhui
	 * @date 2013-5-6 下午3:56:51
	 */
	public static Date str2Date(String dateString, String dataFormat) {
		Date date = null;
		if (StringUtil.isEmpty(dateString))
			return null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
			date = sdf.parse(dateString);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期格式化为字符串
	 * 
	 * @param date
	 *            日期时间
	 * @param format
	 *            格式
	 * @return 格式化后的日期时间
	 * @author yexuhui
	 * @date 2013-5-6 下午3:56:20
	 */
	public static String date2Str(Date date, String format) {
		String result = "";
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(date);
		}
		return result;
	}

	/**
	 * timestamp转成字符串，带毫秒
	 * 
	 * @param time
	 *            日期时间
	 * @return 带毫秒的日期时间字符串
	 * @author yexuhui
	 * @date 2013-5-6 下午3:55:46
	 */
	public static String timestamp2Str(Timestamp time) {
		if (time == null)
			return null;
		return time.toString();
	}

	/**
	 * timestamp转成字符串，不带毫秒
	 * 
	 * @param time
	 *            日期时间
	 * @param format
	 *            日期字符串比宝石格式
	 * @return 日期字符串
	 * @author yexuhui
	 * @date 2013-5-6 下午3:55:10
	 */
	public static String timestamp2Str(Timestamp time, String format) {
		if (time == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(time);
	}

	/**
	 * str转化为timestamp
	 * 
	 * @param str
	 *            日期的字符串
	 * @param format
	 *            日期字符串格式
	 * @return 转化后的timestamp
	 * @author yexuhui
	 * @date 2013-5-6 下午3:51:13
	 */
	public static Timestamp str2Timestamp(String str, String format) {
		if (StringUtil.isEmpty(str))
			return null;
		Date date = str2Date(str, format);
		if (date == null)
			return null;
		return new Timestamp(date.getTime());
	}

	/**
	 * date类型转化XMLGregorianCalendar类型 该方法主要用于web service
	 * 
	 * @param date
	 *            日期
	 * @return 转化后的日期
	 * @author yexuhui
	 * @date 2013-5-6 下午3:53:29
	 */
	public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar gc = null;
		try {
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gc;
	}

	/**
	 * 判断realTime是否在regularTime的minites误差范围内
	 * 
	 * @param realTime
	 *            需要判断的时间
	 * @param regularTime
	 *            基准的时间
	 * @param minites
	 *            误差的分钟范围
	 * @return 布尔值 yexuhui 2012-7-25 上午11:38:22
	 */
	public static boolean timeInRange(Date realTime, Date regularTime, int minites) {
		long realMilli = realTime.getTime();
		long regularMilli = regularTime.getTime();
		long errorRange = minites * 60 * 1000;

		return (realMilli >= (regularMilli - errorRange) && realMilli <= (regularMilli + errorRange));
	}

	/**
	 * 按照指定格式获取连续几天的日期字符串，包含起止时间
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param dateFormat
	 *            日期格式
	 * @return yexuhui 2012-7-27 上午11:33:06
	 */
	public static String[] getSeqDay(Date startTime, Date endTime, String dateFormat) {
		if (endTime.getTime() < startTime.getTime())
			throw new RuntimeException("开始时间必须小于结束时间");

		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();

		startCalendar.setTime(startTime);
		endCalendar.setTime(endTime);

		Calendar startCalendar2 = Calendar.getInstance();
		Calendar endCalendar2 = Calendar.getInstance();

		startCalendar2.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
				startCalendar.get(Calendar.DAY_OF_MONTH));
		endCalendar2.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
				endCalendar.get(Calendar.DAY_OF_MONTH));

		double diff = ((double) endCalendar2.getTimeInMillis() - (double) startCalendar2.getTimeInMillis())
				/ (24 * 60 * 60 * 1000);
		int days = (int) Math.ceil(diff);

		String[] strDays = new String[days + 1];
		strDays[0] = date2Str(startTime, dateFormat);

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startTime);
		for (int i = 1; i <= days; i++) {
			calStart.add(Calendar.DAY_OF_YEAR, 1);
			strDays[i] = date2Str(calStart.getTime(), dateFormat);
		}

		return strDays;
	}
	
	/**
	 * 获取两个时间之间的间隔天数
	 * 
	 * @param dateStart
	 *            开始时间
	 * @param dateEnd
	 *            结束时间
	 * @return 间隔的小时数 LGL 2018-3-7 
	 */
	public static int getBetweenHoursAndRound(Date dateStart, Date dateEnd) {
		long range = dateEnd.getTime() - dateStart.getTime();
		double dd=(range / 1000) / 60 / 60;
		double db=dd/24;
		return (int) Math.ceil(db);
	}
	
	/**
	 * 获取两个时间之间的间隔小时数
	 * 
	 * @param dateStart
	 *            开始时间
	 * @param dateEnd
	 *            结束时间
	 * @return 间隔的小时数 yexuhui 2012-8-2 上午10:39:16
	 */
	public static int getBetweenHours(Date dateStart, Date dateEnd) {
		long range = dateEnd.getTime() - dateStart.getTime();
		return (int) ((range / 1000) / 60 / 60);
	}

	/**
	 * 获取两个时间之间的间隔分钟数
	 * 
	 * @param dateStart
	 *            开始时间
	 * @param dateEnd
	 *            结束时间
	 * @return 间隔的分钟数 yexuhui 2012-8-2 上午10:39:16
	 */
	public static int getBetweenMinutes(Date dateStart, Date dateEnd) {
		long range = dateEnd.getTime() - dateStart.getTime();
		return (int) ((range / 1000) / 60);
	}

	/**
	 * 获取两个时间之间的间隔秒数
	 * 
	 * @param dateStart
	 *            开始时间
	 * @param dateEnd
	 *            结束时间
	 * @return 间隔的秒数 yexuhui 2012-8-2 上午10:39:16
	 */
	public static int getBetweenSeconds(Date dateStart, Date dateEnd) {
		long range = dateEnd.getTime() - dateStart.getTime();
		return Math.abs((int) (range / 1000));
	}

	/**
	 * 获取某年某月 天数
	 * 
	 * @param month
	 * @param year
	 * @return
	 * @author linshunrong
	 * @date 2013-7-24 下午5:02:07
	 */
	public static int getCountMonthlyDays(int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);

		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 获取一个时间是否在某一个时间段中间
	 */
	public static boolean existBucket(Date time, Date startTime, Date endTime) {
		// 1970-01-01 15:37:30.0
		// 1970-01-01 16:37:30.0
		// 2016-10-18 15:07:14.0
		startTime = str2Date(timestamp2Str(new Timestamp(startTime.getTime()), TIME_FORMAT), TIME_FORMAT);
		endTime = str2Date(timestamp2Str(new Timestamp(endTime.getTime()), TIME_FORMAT), TIME_FORMAT);
		time = str2Date(timestamp2Str(new Timestamp(time.getTime()), TIME_FORMAT), TIME_FORMAT);
		int startInt = startTime.compareTo(time);
		int endInt = endTime.compareTo(time);
		if (startInt < 0 && endInt > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断两个时间是否是同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}
	
	
	

	/**
	 * 判断两个时间是否是同一 月
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isMonthDate(Date date1, Date date2) {
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		int month1 = c.get(Calendar.MONTH);
		c.setTime(date2);
		int month2 = c.get(Calendar.MONTH);
		return month1 == month2;
	}
	
	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShorts(Date currentTime, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取时间 小时:分;秒 HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTimeShort() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 把特定格式（0-2359）格式的时分转成标准时间（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param time
	 * @return
	 * @auther yinxingxing
	 * @date 2017年5月24日
	 */
	public static Date parsehhMMToStandDate(Integer time) {
		int Count = String.valueOf(time / 10).length();
		String timeStr = "";
		if (Count == 0) {
			timeStr = "00:0" + time + ":00";
		} else if (Count == 1) {
			timeStr = "00:" + time + ":00";
		} else if (Count == 2) {
			String splithh = String.valueOf(time).substring(0, 1);
			String splitMM = String.valueOf(time).substring(1, 3);
			timeStr = "0" + splithh + ":" + splitMM + ":00";
		} else if (Count == 3) {
			String splithh = String.valueOf(time).substring(0, 2);
			String splitMM = String.valueOf(time).substring(2, 4);
			timeStr = splithh + ":" + splitMM + ":00";
		}

		String date = DateUtils.timestamp2Str(new Timestamp((new Date()).getTime()), DateUtils.SIMPLE_FORMAT) + " "
				+ timeStr;
		return DateUtils.str2Date(date, DateUtils.NORMAL_FORMAT);
	}

	/**
	 * 获取某月所有的天数
	 * 
	 * @param month
	 * @auther yinxingxing
	 * @date 2017年6月8日
	 */
	public static List<Date> getAllTheDateOftheMonth(Date date) {
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		int month = cal.get(Calendar.MONTH);
		while (cal.get(Calendar.MONTH) == month) {
			list.add(cal.getTime());
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}

	/**
	 * 获取某月所有的天数
	 * 
	 * @param month
	 * @auther yinxingxing
	 * @date 2017年6月8日
	 */
	public static List<String> getAllTheDateOftheMonths(Date date) {
		List<String> list = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		int month = cal.get(Calendar.MONTH);
		while (cal.get(Calendar.MONTH) == month) {
			list.add(date2Str(cal.getTime(), "yyyy-MM-dd"));
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}

	/**
	 * 比较两个日期大小
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 * @auther yinxingxing
	 * @date 2017年6月8日
	 */
	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public static int getTimeInt(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String currTimes = formatter.format(date);
		String[] currTimeArr = currTimes.split(":");
		return Integer.valueOf(currTimeArr[0] + currTimeArr[1]);
	}
	
	/**
	 * auther yinxx
	 * 获取一个时间是否在某一个时间段中间(自用)
	 */
	public static boolean existStartAndEnd(Date time, Date startTime, Date endTime) {
		// 1970-01-01 15:37:30.0
		// 1970-01-01 16:37:30.0
		// 2016-10-18 15:07:14.0
		startTime = str2Date(timestamp2Str(new Timestamp(startTime.getTime()), TIME_FORMAT), TIME_FORMAT);
		endTime = str2Date(timestamp2Str(new Timestamp(endTime.getTime()), TIME_FORMAT), TIME_FORMAT);
		time = str2Date(timestamp2Str(new Timestamp(time.getTime()), TIME_FORMAT), TIME_FORMAT);
		int startInt = startTime.compareTo(time);
		int endInt = endTime.compareTo(time);
		if (startInt <= 0 && endInt > 0) {
			return true;
		}
		return false;
	}

	/**
     * 获取两个日期之间的所有日期
     * 
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            //tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }
    
    /**
	 * 获取代表当前时间的字符串
	 * @return 时间字符串
	 */
	public static String getFormatTime(){
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
	}
	
	/**
	* @Title: getCurrentDayFirstMoment
	* @Description: 获取当天第一时刻
	* @param @return    参数
	* @return Date    返回类型
	* @throws
	 */
	public static Date getCurrentDayFirstMoment(){
		return str2Date(date2Str(new Date(), SIMPLE_FORMAT)+" 00:00:00", NORMAL_FORMAT);
	}
	
	/**
	* @Title: getCurrentDayLastMoment
	* @Description: 获取当天最后一个时刻
	* @param @return    参数
	* @return Date    返回类型
	* @throws
	 */
	public static Date getCurrentDayLastMoment(){
		return str2Date(date2Str(new Date(), SIMPLE_FORMAT)+" 23:59:59", NORMAL_FORMAT);
	}
	
	public static void main(String[] args) {
//		String content = "I am noob " + "from runoob.com.";
		String content = "123.45,56.25";

//		String pattern = ".*runoob.*";
		String pattern = "[0-9,.]*";

		boolean isMatch = Pattern.matches(pattern, content);
		//System.out.println(isMatch);
	}
}