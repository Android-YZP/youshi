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
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.TimeBucketInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Smith on 2016/9/1.
 */
public class DialogFactory {

    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mStartHour;
    private static int mEndHour;
    private static int mMinute;
    private static int mStartMinute;
    private static int mEndMinute;

    private static NumberPicker mNpYear;
    private static NumberPicker mNpMonth;
    private static NumberPicker mNpDay;
    private static NumberPicker mNpHour;
    private static NumberPicker mNpMinute;
    private static TextView mTvChooseComplete;
    private static TextView mTvTimeShow;
    private static Button mBtnEndTime;
    private static Button mBtnStartTime;
    private static String mStartTime;
    private static String mEndTime;
    private static Boolean isStartTime;
    private static NumberPicker mNpStartHour;
    private static NumberPicker mNpStartMinute;
    private static NumberPicker mNpEndHour;
    private static NumberPicker mNpEndMinute;
    public static Dialog mChooseTimeDialog;
    public static void showDateDialog(Context context, final TextView textView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month;
        int mCurrentDate = t.monthDay;
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


    //选择开始结束时间的对话框;


    public static void showOptionDialog(Context context, final TextView textView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDay = t.monthDay;
        int mCurrentHour = t.hour;
        //选择框是
        if (textView.getId() == R.id.tv_end_time) {
            mCurrentHour = t.hour + 1;
        }
        int mCurrentMinute = t.minute;
        mYear = mCurrentYear;//初始化全局变量
        mMonth = mCurrentMonth;
        mDay = mCurrentDay;
        mHour = mCurrentHour;
        mMinute = mCurrentMinute;


        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View _OptionView = layoutInflater.inflate(R.layout.layout_choose_time, null);
        mNpYear = (NumberPicker) _OptionView.findViewById(R.id.np_year);
        mNpMonth = (NumberPicker) _OptionView.findViewById(R.id.np_month);
        mNpDay = (NumberPicker) _OptionView.findViewById(R.id.np_day);
        mNpHour = (NumberPicker) _OptionView.findViewById(R.id.np_hour);
        mNpMinute = (NumberPicker) _OptionView.findViewById(R.id.np_min);
        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        setNumberPickerDividerColor(context, mNpYear);//改变分割线的颜色
        setNumberPickerDividerColor(context, mNpMonth);
        setNumberPickerDividerColor(context, mNpDay);
        setNumberPickerDividerColor(context, mNpHour);
        setNumberPickerDividerColor(context, mNpMinute);
        mNpHour.setMaxValue(23);
        mNpHour.setMinValue(0);
        mNpHour.setValue(mCurrentHour);

        mNpMinute.setMaxValue(59);
        mNpMinute.setMinValue(0);
        mNpMinute.setValue(mCurrentMinute);

        mNpYear.setMaxValue(2036);
        mNpYear.setMinValue(1999);
        mNpYear.setValue(mCurrentYear);

        mNpMonth.setMaxValue(12);
        mNpMonth.setMinValue(1);
        mNpMonth.setValue(mCurrentMonth);

        mNpDay.setMinValue(1);
        mNpDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        mNpDay.setValue(mCurrentDay);


        mTvTimeShow.setText(getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mHour, mMinute, false)); //初始化头标题时间


        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();
                textView.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false));
            }
        });
        //选择事件监听
        //年
        mNpYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false)); //更新头标题时间
            }
        });
        //月
        mNpMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false)); //更新头标题时间

            }
        });
        //日
        mNpDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDay = newVal;
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false)); //更新头标题时间
            }
        });
        //时
        mNpHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mHour = newVal;
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false)); //更新头标题时间
            }
        });
        //分
        mNpMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMinute = newVal;
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false)); //更新头标题时间
            }
        });

        mChooseTimeDialog = new AlertDialog.Builder(context,R.style.style_dialog).
                setView(_OptionView).
                create();
        Window window = mChooseTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialog_style);  //添加动画
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mChooseTimeDialog.show();
    }

    /**
     * 全天的时间选择对话框
     */
    public static void showAllDayOptionDialog(Context context, final TextView textView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDay = t.monthDay;
        int mCurrentHour = t.hour;
        int mCurrentMinute = t.minute;
        mYear = mCurrentYear;//初始化全局变量
        mMonth = mCurrentMonth;
        mDay = mCurrentDay;
        mHour = mCurrentHour;
        mMinute = mCurrentMinute;
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View _OptionView = layoutInflater.inflate(R.layout.layout_allday_choose_time, null);
        mNpYear = (NumberPicker) _OptionView.findViewById(R.id.np_year);
        mNpMonth = (NumberPicker) _OptionView.findViewById(R.id.np_month);
        mNpDay = (NumberPicker) _OptionView.findViewById(R.id.np_day);
        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        setNumberPickerDividerColor(context, mNpYear);//改变分割线的颜色
        setNumberPickerDividerColor(context, mNpMonth);
        setNumberPickerDividerColor(context, mNpDay);

        mNpYear.setMaxValue(2036);
        mNpYear.setMinValue(1999);
        mNpYear.setValue(mCurrentYear);

        mNpMonth.setMaxValue(12);
        mNpMonth.setMinValue(1);
        mNpMonth.setValue(mCurrentMonth);

        mNpDay.setMinValue(1);
        mNpDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        mNpDay.setValue(mCurrentDay);
        mTvTimeShow.setText(getWeek(mCurrentYear, mCurrentMonth, mHour, mMinute, mCurrentDay, true)); //初始化头标题时间
        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();
                textView.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true));
            }
        });
        //选择事件监听
        //年
        mNpYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true)); //更新头标题时间
            }
        });
        //月
        mNpMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true)); //更新头标题时间

            }
        });
        //日
        mNpDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDay = newVal;
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true)); //更新头标题时间
            }
        });


        mChooseTimeDialog = new AlertDialog.Builder(context).
                setView(_OptionView).
                create();
        Window window = mChooseTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialog_style);  //添加动画
        mChooseTimeDialog.show();
    }


    /**
     * 时间段选择对话框
     */
    public static void showTimeBucketOptionDialog(final Context context,  final ArrayList<TimeBucketInfo> bucketInfos, final BaseAdapter adapter) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDay = t.monthDay;
        int mCurrentHour = t.hour;
        int mCurrentMinute = t.minute;
//        mYear = mCurrentYear;//初始化全局变量
//        mMonth = mCurrentMonth;
//        mDay = mCurrentDay;
//
//        mHour = mCurrentHour;
//        mMinute = mCurrentMinute;
        //初始化开始时间和结束时间
        mStartHour = mCurrentHour;
        mEndHour = mCurrentHour + 1;

        mStartMinute = mCurrentMinute;
        mEndMinute = mCurrentMinute;


        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View _OptionView = layoutInflater.inflate(R.layout.layout_choose_time_bucket, null);
        mNpStartHour = (NumberPicker) _OptionView.findViewById(R.id.np_starter_hour);
        mNpStartMinute = (NumberPicker) _OptionView.findViewById(R.id.np_starter_min);
        mNpEndHour = (NumberPicker) _OptionView.findViewById(R.id.np_end_hour);
        mNpEndMinute = (NumberPicker) _OptionView.findViewById(R.id.np_end_min);

        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        mBtnStartTime = (Button) _OptionView.findViewById(R.id.btn_dialog_start_time);
        mBtnEndTime = (Button) _OptionView.findViewById(R.id.btn_dialog_end_time);

        //初始化点击事件
        mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间
        final TimeBucketInfo _bucketInfo = new TimeBucketInfo();
        _bucketInfo.setStartTime(mCurrentHour + ":" + mCurrentMinute);
        _bucketInfo.setEndTime((mCurrentHour + 1) + ":" + mCurrentMinute);

        setNumberPickerDividerColor(context, mNpStartHour);//改变分割线的颜色
        setNumberPickerDividerColor(context, mNpStartMinute);
        setNumberPickerDividerColor(context, mNpEndHour);
        setNumberPickerDividerColor(context, mNpEndMinute);

        mNpStartHour.setMaxValue(23);
        mNpStartHour.setMinValue(0);
        mNpStartHour.setValue(mCurrentHour);

        mNpStartMinute.setMaxValue(59);
        mNpStartMinute.setMinValue(0);
        mNpStartMinute.setValue(mCurrentMinute);

        mNpEndHour.setMaxValue(23);
        mNpEndHour.setMinValue(0);
        mNpEndHour.setValue(mCurrentHour + 1);

        mNpEndMinute.setMaxValue(59);
        mNpEndMinute.setMinValue(0);
        mNpEndMinute.setValue(mCurrentMinute);


        mTvTimeShow.setText(getWeek(mCurrentYear, mCurrentMonth, mHour, mMinute, mCurrentDay, true)); //初始化头标题时间
        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();
                _bucketInfo.setStartTime(mStartHour + ":" + mStartMinute);
                _bucketInfo.setEndTime(mEndHour + ":" + mEndMinute);
                bucketInfos.add(_bucketInfo);//添加一个时间段的对象到集合中
                adapter.notifyDataSetChanged();//刷新Adapter
            }
        });
        //选择事件监听
        //开始小时
        mNpStartHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mStartHour = newVal;
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间

            }
        });
        //开始分钟
        mNpStartMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mStartMinute = newVal;
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间

            }
        });
        //结束小时
        mNpEndHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndHour = newVal;
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间

            }
        });
        //结束分钟
        mNpEndMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndMinute = newVal;
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间

            }
        });

        mChooseTimeDialog = new AlertDialog.Builder(context).
                setView(_OptionView).
                create();
        Window window = mChooseTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialog_style);  //添加动画
        mChooseTimeDialog.show();
    }

    /**
     * 一起选择开始时间和结束时间
     */
    public static void showTwoDayOptionDialog(final Context context, final TextView textView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDay = t.monthDay;
        int mCurrentHour = t.hour;
        int mCurrentMinute = t.minute;
        mYear = mCurrentYear;//初始化全局变量
        mMonth = mCurrentMonth;
        mDay = mCurrentDay;
        mHour = mCurrentHour;
        mMinute = mCurrentMinute;

        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View _OptionView = layoutInflater.inflate(R.layout.layout_twoday_choose_time, null);
        mNpYear = (NumberPicker) _OptionView.findViewById(R.id.np_year);
        mNpMonth = (NumberPicker) _OptionView.findViewById(R.id.np_month);
        mNpDay = (NumberPicker) _OptionView.findViewById(R.id.np_day);
        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        mBtnStartTime = (Button) _OptionView.findViewById(R.id.btn_dialog_start_time);
        mBtnEndTime = (Button) _OptionView.findViewById(R.id.btn_dialog_end_time);

        //初始化点击事件
        isStartTime = true;//初始化数据
        mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true));//更新头标题时间
        mBtnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnStartTime.setBackgroundColor(context.getResources().getColor(R.color.btn_back));
                mBtnEndTime.setBackgroundColor(context.getResources().getColor(R.color.text_white));
                isStartTime = true;
            }
        });
        mBtnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnEndTime.setBackgroundColor(context.getResources().getColor(R.color.btn_back));
                mBtnStartTime.setBackgroundColor(context.getResources().getColor(R.color.text_white));
                isStartTime = false;
            }
        });

        setNumberPickerDividerColor(context, mNpYear);//改变分割线的颜色
        setNumberPickerDividerColor(context, mNpMonth);
        setNumberPickerDividerColor(context, mNpDay);

        mNpYear.setMaxValue(2036);
        mNpYear.setMinValue(1999);
        mNpYear.setValue(mCurrentYear);

        mNpMonth.setMaxValue(12);
        mNpMonth.setMinValue(1);
        mNpMonth.setValue(mCurrentMonth);

        mNpDay.setMinValue(1);
        mNpDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        mNpDay.setValue(mCurrentDay);
        mTvTimeShow.setText(getWeek(mCurrentYear, mCurrentMonth, mHour, mMinute, mCurrentDay, true)); //初始化头标题时间
        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();
                textView.setText(mStartTime + " 至 " + mEndTime);
            }
        });
        //选择事件监听
        //年
        mNpYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpDay.setMaxValue(maxMonth);
                String _date = getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);
                if (isStartTime) {
                    mStartTime = _date;
                } else {
                    mEndTime = _date;
                }
            }
        });
        //月
        mNpMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpDay.setMaxValue(maxMonth);
                String _date = getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);//更新头标题时间
                if (isStartTime) {
                    mStartTime = _date;
                } else {
                    mEndTime = _date;
                }
            }
        });
        //日
        mNpDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDay = newVal;
                String _date = getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date); //更新头标题时间
                if (isStartTime) {
                    mStartTime = _date;
                } else {
                    mEndTime = _date;
                }
            }
        });


        mChooseTimeDialog = new AlertDialog.Builder(context).
                setView(_OptionView).
                create();
        Window window = mChooseTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialog_style);  //添加动画
        mChooseTimeDialog.show();
    }


    //设置NumberPicker分割线的颜色
    public static void setNumberPickerDividerColor(Context context, NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.common_topbar_bg_color)));
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

    public static String getWeek(int year, int month, int dayOfMonth, int hour, int minute, Boolean isAllDay) {
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

        //改变分钟和小时的显示方式
        String _hour;
        String _minute;
        if (hour / 10 == 0) {
            _hour = "0" + hour;
        } else {
            _hour = "" + hour;
        }
        if (minute / 10 == 0) {
            _minute = "0" + minute;
        } else {
            _minute = "" + minute;
        }
        if (isAllDay) {//判断是否选择了一整天的选项
            return year + "年" + month + "月" + dayOfMonth + "日" + "周" + week;
        } else {
            return year + "年" + month + "月" + dayOfMonth + "日" + "周" + week + " " + _hour + ":" + _minute;
        }
    }


}
