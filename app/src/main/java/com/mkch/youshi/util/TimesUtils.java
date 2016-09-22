package com.mkch.youshi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SunnyJiang on 2016/8/26.
 */
public class TimesUtils {

    /**
     * 获取当前时间
     * @return
     */
    public static String getNow(){
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return _sdf.format(new Date());
    }

    /**
     * 判断两个时间段是否在某个时长内，（初定5分钟）
     * 若返回true，表示足够靠近，不显示时间；
     * 若返回false，表示不在某个时长内，显示时间。
     * @param beforeTime 前一个时间
     * @param afterTime 后一个时间
     * @return
     */
    public static boolean isCloseEnough(String beforeTime,String afterTime){
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date _before_date = _sdf.parse(beforeTime);
            Date _after_date = _sdf.parse(afterTime);
            long _minute = (_after_date.getTime() - _before_date.getTime())/(1000*60);
            if (_minute<=5){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
