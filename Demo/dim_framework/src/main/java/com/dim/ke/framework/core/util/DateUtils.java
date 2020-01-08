package com.dim.ke.framework.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
    static final String formatPattern = "yyyy-MM-dd";
    static final String formatPattern1 = "HH:mm";

    /**
     * 自提单今日明日判断
     * @param timeStamp
     * @return
     */
    private static String getDateStr(long timeStamp){
//        String curDate = getCurrentDateString();
//        SimpleDateFormat formatDate = new SimpleDateFormat(formatPattern);
//        String takeDate = formatDate.format(timeStamp);
//        if(TextUtils.equals(curDate, takeDate)){
//            return "今日";
//        }
//        return "明日";
        SimpleDateFormat formatDate = new SimpleDateFormat("MM-dd HH:mm");
        String takeDate = formatDate.format(timeStamp);
        return takeDate;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getHourMinuteStr(long timeStamp) {
        SimpleDateFormat formatDate = new SimpleDateFormat("MM-dd HH:mm");
        String takeDate = formatDate.format(timeStamp);
        return takeDate;
    }



    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDateString() {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(getCurrentDate());
    }

    /**
     * 获取当前时间
     *
     * @param date
     * @return
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取制定毫秒数之前的日期
     *
     * @param timeDiff
     * @return
     */
    public static String getDesignatedDate(long timeDiff) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        long nowTime = System.currentTimeMillis();
        long designTime = nowTime - timeDiff;
        return format.format(designTime);
    }

    /**
     * 获取前几天的日期
     */
    public static String getPrefixDate(String count) {
        Calendar cal = Calendar.getInstance();
        int day = 0 - Integer.parseInt(count);
        cal.add(Calendar.DATE, day); // int amount 代表天数
        Date datNew = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(datNew);
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);
    }

    /**
     * 字符串转换日期
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        // str = " 2008-07-10 19:20:00 " 格式
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        if (!str.equals("") && str != null) {
            try {
                return format.parse(str);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String formatHomeOrderTime(long payTime) {
        long diff = (System.currentTimeMillis() - payTime) / 1000;
        String formatDate;
        if(diff / (60*60*24*365) > 0){//几年前
            formatDate = (diff / (60*60*24*365)) + "年前";
        }else if(diff / (60*60*24*30) > 0){//几月前
            formatDate = (diff / (60*60*24*30)) + "月前";
        }else if(diff / (60*60*24*7) > 0){//几星期
            formatDate = (diff / (60*60*24*7)) + "星期前";
        }else if(diff / (60*60*24) > 0){//几天前
            formatDate = (diff / (60*60*24)) + "天前";
        }else if(diff / (60*60) > 0){//几小时前
            formatDate = (diff / (60*60)) + "小时前";
        }else if(diff / (60) > 0){//几分钟前
            formatDate = (diff / 60) + "分钟前";
        }else{
            formatDate = "刚刚";
        }

        return formatDate;
    }

    // java中怎样计算两个时间如：“21:57”和“08:20”相差的分钟数、小时数 java计算两个时间差小时 分钟 秒 .
    public void timeSubtract() {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        try {
            begin = dfs.parse("2004-01-02 11:30:24");
            end = dfs.parse("2004-03-26 13:31:40");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60;
        System.out.println("" + day1 + "天" + hour1 + "小时" + minute1 + "分" + second1 + "秒");
    }

    /**
     * 将时间置为23时59分钟59秒
     *
     * @param date
     * @return
     */
    public static Date setFullPassDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 将时间后退2小时
     *
     * @param date
     * @return
     */
    public static Date getFallBack2Hour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 将时间精确到小时
     *
     * @param date
     * @return
     */
    public static Date getTimeHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取两个时间间隔的天数
     *
     * @param date
     * @return
     */
    public static long getDiffDays(Date startDate, Date endDate) {
        long difftime = endDate.getTime() - startDate.getTime();
        return difftime / (24L * 60L * 60L * 1000L);
    }

    /**
     * 根据日期获取当天起始时间
     *
     * @param date
     * @return
     */
    public static Date getStartDateOfCurrentDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据日期获取下一天起始时间
     *
     * @param date
     * @return
     */
    public static Date getStartDateOfNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据日期当前日期顺延一周后的起始时间
     *
     * @param date
     * @return
     */
    public static Date getStartDateOfNextSevenDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据日期当前日期顺延一周后的起始时间
     *
     * @param date
     * @return
     */
    public static Date getStartDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据日期当前日期顺延一月后的起始时间
     *
     * @param date
     * @return
     */
    public static Date getStartDateOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
     * 封装一天只能的时间区域
     */
    public static List<Date> getStaticByDateDateArea(Date date) {
        List<Date> dates = new ArrayList<Date>();
        Date startdate = getStartDateOfCurrentDay(date);
        Date nextday = getStartDateOfNextDay(date);
        int step = 2;
        dates.add(startdate);
        for (int i = 1; i < 12; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.HOUR_OF_DAY, i * step);
            dates.add(calendar.getTime());
        }
        dates.add(nextday);
        return dates;
    }

    /*
     * 封装一周之内时间区域
     */
    public static List<Date> getStaticByWeekDateArea(Date date) {
        List<Date> dates = new ArrayList<Date>();
        Date startdate = getStartDateOfCurrentDay(date);
        Date nextday = getStartDateOfNextSevenDay(date);
        dates.add(startdate);
        for (int i = 1; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(calendar.getTime());
        }
        dates.add(nextday);
        return dates;
    }

    /*
     * 封装一周之内时间区域List<String>
     */
    public static List<String> getStaticByWeekLabel(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        List<String> dates = new ArrayList<String>();
        Date startdate = getStartDateOfCurrentDay(date);
        Date nextday = getStartDateOfNextSevenDay(date);
        dates.add(dateFormat.format(startdate));
        for (int i = 1; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(dateFormat.format(calendar.getTime()));
        }
        return dates;
    }

    /*
     * 封装一月之内时间区域
     */
    public static List<Date> getStaticByMonthDateArea(Date date) {
        List<Date> dates = new ArrayList<Date>();
        Date startdate = getStartDateOfMonth(date);
        Date nextday = getStartDateOfNextMonth(date);
        long daydiff = getDiffDays(startdate, nextday);
        dates.add(startdate);
        for (int i = 1; i < daydiff; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(calendar.getTime());
        }
        dates.add(nextday);
        return dates;
    }

    /*
     * 封装一点时间之内的时间区域（天）
     */
    public static List<Date> getStaticBySE(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<Date>();

        long daydiff = getDiffDays(startDate, endDate);
        dates.add(startDate);
        for (int i = 1; i < daydiff; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(calendar.getTime());
        }
        dates.add(endDate);
        return dates;
    }

    /*
     * 封装一月之内时间区域
     */
    public static List<String> getStaticByMonthLabel(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        List<String> dates = new ArrayList<String>();
        Date startdate = getStartDateOfMonth(date);
        Date nextday = getStartDateOfNextMonth(date);
        long daydiff = getDiffDays(startdate, nextday);
        dates.add(dateFormat.format(startdate));
        for (int i = 1; i < daydiff; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startdate);
            calendar.add(Calendar.DAY_OF_MONTH, i);
            dates.add(dateFormat.format(calendar.getTime()));
        }
        return dates;
    }

    public static String formatDate(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String formatDate(long timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeString = sdf.format(new Date(timeStamp));//单位秒
        return timeString;
    }

    public static String formatDotDate(long timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        timeString = sdf.format(new Date(timeStamp));//单位秒
        return timeString;
    }

    public static String formatMessageDate(long timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        timeString = sdf.format(new Date(timeStamp));//单位秒
        return timeString;
    }


   /**
     * 时间间隔秒数转化为时分秒
     * @param between 秒数
     * @return
     */
    public static int[] parseTimeInterval2String(long between){
        int day1 = (int)between / (24 * 3600);
        int hour1 = (int) between % (24 * 3600) / 3600;
        int minute1 = (int)between % 3600 / 60;
        int second1 = (int)between % 60;
        int[] array=new int[4];
        array[0]=day1;
        array[1]=hour1;
        array[2]=minute1;
        array[3]=second1;
        System.out.println("gdsggdsgshdhs" + day1 + "天" + hour1 + "小时" + minute1 + "分" + second1 + "秒");
        return array;
    }
}
