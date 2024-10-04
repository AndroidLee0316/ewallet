package com.pasc.business.ewallet.business.util;

import android.support.annotation.IntRange;

import com.pasc.business.ewallet.business.BundleKey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @date 2019/7/11
 * @des
 * @modify
 **/
public class DateUtil {
    public static int[] getYearMonth(String date) {
        int arr[] = new int[2];
        arr[0] = BundleKey.Trade.defaultYear;
        arr[1] = BundleKey.Trade.defaultMonth;
        try {
            String arrStr[] = date.split ("-");
            arr[0] = Integer.parseInt (arrStr[0]);
            arr[1] = Integer.parseInt (arrStr[1]);
        } catch (Exception e) {
            e.printStackTrace ();
            try {
                String arrStr[] = date.split ("/");
                arr[0] = Integer.parseInt (arrStr[0]);
                arr[1] = Integer.parseInt (arrStr[1]);
            } catch (Exception e2) {
                e2.printStackTrace ();

            }

        }
        return arr;
    }

    public static String yearMonthStr(int year,int month){
        String payYearMonth =year + "-" +  (month>9? month: "0"+month);
        return payYearMonth;
    }

    /**
     *
     * @param year
     * @param month 范围 0-11
     * @param nextCount
     * @param pattern
     * @return
     */
    public static List<String> getNextMonths(int year, @IntRange(from = 1,to = 12) int month, int nextCount, String pattern) {
        GregorianCalendar now = new GregorianCalendar (year,month-1,1);
        return getNextMonths (now,nextCount,pattern);
    }
    /**
     * 获取连续后几个月
     *
     * @param nextCount
     * @return
     */
    public static List<String> getNextMonths(int nextCount,String pattern) {
        GregorianCalendar now = new GregorianCalendar ();
        return getNextMonths (now,nextCount,pattern);
    }

    /**
     *
     * @param calendar
     * @param nextCount
     * @param pattern  "yyyy-MM"  日期格式
     * @return
     */
    public static List<String> getNextMonths(GregorianCalendar calendar,int nextCount,String pattern){
        SimpleDateFormat format = new SimpleDateFormat (pattern);
        String str = format.format (calendar.getTime ());
        List<String> strings = new ArrayList<> ();
        strings.add (str);
        boolean isNext = nextCount > 0;
        int add = isNext ? 1 : -1;
        for (int i = 0; i < Math.abs (nextCount); i++) {
            calendar.add (GregorianCalendar.MONTH, add); //可以是天数或月数 
            str = format.format (calendar.getTime ());
            strings.add (str);
        }
        return strings;
    }

    public static int[] getCurrentYearMonth(){
        int[] yearMonth=new int[2];
        GregorianCalendar gregorianCalendar =new GregorianCalendar();
        yearMonth[0]=gregorianCalendar.get (Calendar.YEAR);
        yearMonth[1]=gregorianCalendar.get (Calendar.MONTH)+1;
        return yearMonth;
    }
}
