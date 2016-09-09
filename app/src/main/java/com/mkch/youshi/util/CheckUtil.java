package com.mkch.youshi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SunnyJiang on 2016/9/1.
 */
public class CheckUtil {

    /**
     * 判断手机号格式是否错误
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(mobile);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
