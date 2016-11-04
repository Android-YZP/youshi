package com.mkch.youshi.util;

/**
 * Created by Smith on 2016/10/24.
 */

public class WeekUtils {
    /**
     * 将123456换成周一周二周三
     *
     * @return
     */
    public static String replaceWeek1(String week) {
        String _week1 = week.replace("1", "周一");
        String _week2 = _week1.replace("2", "周二");
        String _week3 = _week2.replace("3", "周三");
        String _week4 = _week3.replace("4", "周四");
        String _week5 = _week4.replace("5", "周五");
        String _week6 = _week5.replace("6", "周六");
        String _week7 = _week6.replace("7", "周日");
        return _week7;
    }

    /**
     * 将123换成周1,2,3
     *
     * @return
     */
    public static String replaceWeek2(String week) {
        String _week1 = week.replace("1", "1,");
        String _week2 = _week1.replace("2", "2,");
        String _week3 = _week2.replace("3", "3,");
        String _week4 = _week3.replace("4", "4,");
        String _week5 = _week4.replace("5", "5,");
        String _week6 = _week5.replace("6", "6,");
        String _week7 = _week6.replace("7", "7,");
        return _week7.substring(0, _week7.length() - 1);

    }

    /**
     * 将1,2,3换成123
     *
     * @return
     */
    public static String replaceWeek3(String week) {
        String _week1 = week.replace("1,", "1");
        String _week2 = _week1.replace("2,", "2");
        String _week3 = _week2.replace("3,", "3");
        String _week4 = _week3.replace("4,", "4");
        String _week5 = _week4.replace("5,", "5");
        String _week6 = _week5.replace("6,", "6");
        String _week7 = _week6.replace("7,", "7");
        return _week7;
    }


}
