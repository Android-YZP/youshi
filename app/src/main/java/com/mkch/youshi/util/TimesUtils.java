package com.mkch.youshi.util;

import com.mkch.youshi.bean.NetScheduleModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by SunnyJiang on 2016/8/26.
 */
public class TimesUtils {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNow() {
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return _sdf.format(new Date());
    }

    /**
     * 判断两个时间段是否在某个时长内，（初定5分钟）
     * 若返回true，表示足够靠近，不显示时间；
     * 若返回false，表示不在某个时长内，显示时间。
     *
     * @param beforeTime 前一个时间
     * @param afterTime  后一个时间
     * @return
     */
    public static boolean isCloseEnough(String beforeTime, String afterTime) {
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date _before_date = _sdf.parse(beforeTime);
            Date _after_date = _sdf.parse(afterTime);
            long _minute = (_after_date.getTime() - _before_date.getTime()) / (1000 * 60);
            if (_minute <= 5) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 计算出所有时间段的总时间
     *
     * @return
     */
    public static String totalTime(ArrayList<NetScheduleModel.ViewModelBean.TimeSpanListBean> mTimeSpanListBeans) {
        SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm");
        int hours = 0;
        int minutes = 0;
        for (int i = 0; i < mTimeSpanListBeans.size(); i++) {
            try {
                String _startTime = mTimeSpanListBeans.get(i).getStartTime();
                String _endTime = mTimeSpanListBeans.get(i).getEndTime();
                Date dStart = _sdf.parse(_startTime);
                Date dEnd = _sdf.parse(_endTime);
                long diff = dEnd.getTime() - dStart.getTime();//这样得到的差值是微秒级别
                long days = diff / (1000 * 60 * 60 * 24);
                long hour = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minute = (diff - days * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
                hours = (int) (hours + hour);
                minutes = (int) (minutes + minute);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return hours + "小时" + minutes + "分钟";
    }

    /**
     * 计算出所有时间段的总时间
     *
     * @return
     */
    public static int[] totalTimeAndhour(ArrayList<NetScheduleModel.ViewModelBean.TimeSpanListBean> mTimeSpanListBeans) {
        SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm");
        int hours = 0;
        int minutes = 0;
        int[] totaltimes = new int[0];
        for (int i = 0; i < mTimeSpanListBeans.size(); i++) {
            try {
                String _startTime = mTimeSpanListBeans.get(i).getStartTime();
                String _endTime = mTimeSpanListBeans.get(i).getEndTime();
                Date dStart = _sdf.parse(_startTime);
                Date dEnd = _sdf.parse(_endTime);
                long diff = dEnd.getTime() - dStart.getTime();//这样得到的差值是微秒级别
                long days = diff / (1000 * 60 * 60 * 24);
                long hour = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minute = (diff - days * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
                hours = (int) (hours + hour);
                minutes = (int) (minutes + minute);
                totaltimes = new int[]{hours, minutes};
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return totaltimes;
    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年M月d日");//yyyy年M月d日E
        try {
            Date dt = df.parse(date);
            String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getWeek(int year, int month, int dayOfMonth) {

        int c = year / 100;
        int d = dayOfMonth;
        int y = year % 100;
        int m = month;
        if (m == 1 || m == 2) {
            y = year - 1;
            m = month + 12;
        }
        // 运用Zeller公式计算星期
        int w = (y + (y / 4) + (c / 4) - 2 * c
                + (26 * (m + 1) / 10) + d - 1) % 7;
        if (w < 0) {
            w += 7;
        }
        if (month == 0 || month == 1) {
            w += 2;
        }
        if (w >= 7) {
            w = w % 7;
        }
        String week = null;
        switch (w) {
            case 0:
                week = "日";
                break;
            case 1:
                week = "一";
                break;
            case 2:
                week = "二";
                break;
            case 3:
                week = "三";
                break;
            case 4:
                week = "四";
                break;
            case 5:
                week = "五";
                break;
            case 6:
                week = "六";
                break;
            default:
                break;
        }
        return "周" + week;

    }

    /**
     * 判断二个时间的大小
     *
     * @param DATE1
     * @param DATE2
     * @param isAllDay
     * @return
     */
    public static int compare_date(String DATE1, String DATE2, boolean isAllDay) {
        SimpleDateFormat df = null;
        if (isAllDay) {
            df = new SimpleDateFormat("yyyy年MM月dd日E");//yyyy年M月d日E
        } else {
            df = new SimpleDateFormat("yyyy年MM月dd日EHH:mm");//yyyy年M月d日E
        }
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断二个小时分钟的的大小
     *
     * @param DATE1
     * @param DATE2
     * @param isAllDay
     * @return
     */
    public static int compareHourmin(String DATE1, String DATE2) {//1是d1大
        SimpleDateFormat df = null;
        df = new SimpleDateFormat("HH:mm");//yyyy年M月d日E
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到当前的时间
     *
     * @param isAllDay
     * @return
     */
    public static String getNowTime(boolean isAllDay) {
        Date date = new Date();
        SimpleDateFormat format = null;
        if (isAllDay) {
            format = new SimpleDateFormat("yyyy年MM月dd日E");//yyyy年M月d日E
        } else {
            format = new SimpleDateFormat("yyyy年MM月dd日EHH:mm");//yyyy年M月d日E
        }
        String time = format.format(date);
        return time;
    }

    /**
     * 得到当前的时间
     *
     * @param isAllDay
     * @return
     */
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat format = null;
        format = new SimpleDateFormat("yyyy年MM月dd日");//yyyy年M月d日E

        String time = format.format(date);
        return time;
    }

    /**
     * 得到Date对象
     *
     * @param isAllDay
     * @param text
     * @return
     */
    public static Date getDate(boolean isAllDay, String Date) {
//        Date date = new Date();
        SimpleDateFormat format = null;
        Date time = null;
        try {
            if (isAllDay) {
                format = new SimpleDateFormat("yyyy年MM月dd日E");//yyyy年M月d日E
            } else {
                format = new SimpleDateFormat("yyyy年MM月dd日EHH:mm");//yyyy年M月d日E
            }
            time = format.parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 得到Date对象
     *
     * @return
     */
    public static String getEveryDayTime(boolean isAllDay, Date date) {
//        Date date = new Date();
        SimpleDateFormat format = null;
        String time = null;
        if (isAllDay) {
            format = new SimpleDateFormat("yyyy年MM月dd日E");//yyyy年M月d日E
        } else {
            format = new SimpleDateFormat("yyyy年MM月dd日EHH:mm");//yyyy年M月d日E
        }
        time = format.format(date);
        return time;
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
//        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
//        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }

    public static String addDateMinut(boolean isAllDay, String day, int x) {
        SimpleDateFormat format = null;
        String time = null;
        format = new SimpleDateFormat("yyyy年MM月dd日EHH:mm");//yyyy年M月d日E

        //引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变
        //量day格式一致
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        System.out.println("front:" + format.format(date)); //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -x);// 24小时制负号表示减
        date = cal.getTime();
        System.out.println("after:" + format.format(date));  //显示更新后的日期
        cal = null;
        return format.format(date);

    }
    /**
     * 格式化数据用于查询
     *
     * @param year
     * @param month
     * @param day
     */
    public static String formatDate(int year, int month, String day) {
        String chooseMonth, chooseDay;


        if (month < 10) {//格式化数据
            chooseMonth = "0" + month + "月";
        } else {
            chooseMonth = month + "月";
        }
        if (Integer.parseInt(day) < 10) {//格式化数据
            chooseDay = "0" + day + "日";
        } else {
            chooseDay = day + "日";
        }

        return year + "年" + chooseMonth + chooseDay;
    }



}

