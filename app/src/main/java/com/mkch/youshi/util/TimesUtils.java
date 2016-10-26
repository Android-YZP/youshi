package com.mkch.youshi.util;

import com.mkch.youshi.bean.NetScheduleModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
}

