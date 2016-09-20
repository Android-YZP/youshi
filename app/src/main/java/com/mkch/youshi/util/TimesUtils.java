package com.mkch.youshi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SunnyJiang on 2016/8/26.
 */
public class TimesUtils {

    public static String getNow(){
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return _sdf.format(new Date());
    }
}
