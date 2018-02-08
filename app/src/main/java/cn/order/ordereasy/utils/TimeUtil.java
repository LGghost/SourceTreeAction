package cn.order.ordereasy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mr.Pan on 2017/9/21.
 */

public class TimeUtil {
    /**
     * 时间戳转换时间
     *
     * @param timestamp
     * @return
     */
    public static String getTimeStamp2Str(long timestamp, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String dateTime = df.format(timestamp * 1000);
        return dateTime;
    }

    public static String getTimeStamp2Str(Date timestamp, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String dateTime = df.format(timestamp);
        return dateTime;
    }

    public static int getCurrentYear() {
        Calendar rightNow = Calendar.getInstance();
        /*用Calendar的get(int field)方法返回给定日历字段的值。
        HOUR 用于 12 小时制时钟 (0 - 11)，HOUR_OF_DAY 用于 24 小时制时钟。*/
        Integer year = rightNow.get(Calendar.YEAR);
        return year;
    }

    public static int getCurrentMonth() {
        Calendar rightNow = Calendar.getInstance();
        /*用Calendar的get(int field)方法返回给定日历字段的值。
        HOUR 用于 12 小时制时钟 (0 - 11)，HOUR_OF_DAY 用于 24 小时制时钟。*/
        Integer month = rightNow.get(Calendar.MONTH);
        return month;
    }

    public static int getCurrentDay() {
        Calendar rightNow = Calendar.getInstance();
        /*用Calendar的get(int field)方法返回给定日历字段的值。
        HOUR 用于 12 小时制时钟 (0 - 11)，HOUR_OF_DAY 用于 24 小时制时钟。*/
        Integer day = rightNow.get(rightNow.DAY_OF_MONTH);
        return day;
    }

    public static List<String> getDateList(int num) {
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            calendar1.add(Calendar.DATE, -i - 1);
            String day = sdf1.format(calendar1.getTime());
            dateList.add(day);
        }
        return dateList;
    }

    public static String getDate(String time) {
        String[] str = time.split("-");

        return str[1] + "." + str[2];
    }

    public static long getDate1(String time) {
        String newStr = time.replaceAll("-", "");
        return Integer.parseInt(newStr);
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime) {
        Date date = stringToDate(strTime, "yyyy-MM-dd"); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();  // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getDate(int time){
        String date;
        if (time < 10) {
            date = "0" + time;
        } else {
            date = time + "";
        }
        return date;
    }

}
