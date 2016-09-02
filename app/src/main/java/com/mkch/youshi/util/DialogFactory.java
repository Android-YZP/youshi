package com.mkch.youshi.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mkch.youshi.R;

import java.lang.reflect.Field;
import java.util.Calendar;


/**
 * Created by Smith on 2016/9/1.
 */
public class DialogFactory {
    public static void showDateDialog(Context context, final TextView textView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month;
        int mCurrentDate = t.monthDay;
        int mCurrentHour = t.hour;
        int mCurrentMinute = t.minute;


        DatePickerDialog.OnDateSetListener dateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker,
                                          int year, int month, int dayOfMonth) {
                        int c = year / 100;
                        int d = dayOfMonth;
                        int y = year % 100;
                        int m = month + 1;
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
                        textView.setText(year + "年" + m + "月" + dayOfMonth + "日" + "  周" + week);

                    }
                };
        DatePickerDialog dialog = new DatePickerDialog(context,
                dateListener,
                mCurrentYear, mCurrentMonth, mCurrentDate);
        dialog.show();
    }


    public static void showOptionDialog(Context context) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDate = t.monthDay;
        int mCurrentHour = t.hour;
        int mCurrentMinute = t.minute;

        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View _OptionView = layoutInflater.inflate(R.layout.layout_choose_time, null);
        NumberPicker _npYear = (NumberPicker) _OptionView.findViewById(R.id.np_year);
        NumberPicker _npMonth = (NumberPicker) _OptionView.findViewById(R.id.np_month);
        NumberPicker _npDay = (NumberPicker) _OptionView.findViewById(R.id.np_day);
        NumberPicker _npHour = (NumberPicker) _OptionView.findViewById(R.id.np_hour);
        NumberPicker _npMin = (NumberPicker) _OptionView.findViewById(R.id.np_min);
        setNumberPickerDividerColor(context, _npYear);//改变分割线的颜色
        setNumberPickerDividerColor(context, _npMonth);
        setNumberPickerDividerColor(context, _npDay);
        setNumberPickerDividerColor(context, _npHour);
        setNumberPickerDividerColor(context, _npMin);
        _npHour.setMaxValue(23);
        _npHour.setMinValue(0);
        _npHour.setValue(mCurrentHour);

        _npMin.setMaxValue(59);
        _npMin.setMinValue(0);
        _npMin.setValue(mCurrentMinute);

        _npYear.setMaxValue(2036);
        _npYear.setMinValue(2016);
        _npYear.setValue(mCurrentYear);

        _npMonth.setMaxValue(12);
        _npMonth.setMinValue(1);
        _npMonth.setValue(mCurrentMonth);

        _npDay.setMinValue(1);
        _npDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        _npDay.setValue(mCurrentDate);

        Dialog mChooseTimeDialog = new AlertDialog.Builder(context).
                setView(_OptionView).
                create();
        Window window = mChooseTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        mChooseTimeDialog.show();
    }

    public static void setNumberPickerDividerColor(Context context, NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.week_day_line)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //指定年月可以获得相应的天数
    public static int getDays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month - 1);//再指定月份 Java月份从0开始算
        return calendar.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
    }


}
