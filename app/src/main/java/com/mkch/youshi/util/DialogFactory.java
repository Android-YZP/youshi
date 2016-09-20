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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.NetScheduleModel;
import com.mkch.youshi.bean.TimeBucketInfo;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Smith on 2016/9/1.
 */
public class DialogFactory {

    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mEndYear;
    private static int mEndMonth;
    private static int mEndDay;
    private static String mEndMinute;
    private static String mEndHour;
    private static String mStartHour;
    private static int mMinute;
    private static String mStartMinute;

    private static NumberPicker mNpTwoOptionStartYear;
    private static NumberPicker mNpTwoOptionStartMonth;
    private static NumberPicker mNpTwoOptionStartDay;
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
    private static NumberPicker mNpTwoOptionEndYear;
    private static NumberPicker mNpTwoOptionEndMonth;
    private static NumberPicker mNpTwoOptionEndDay;
    private static LinearLayout mLLTwoOptionStartRootView;
    private static LinearLayout mLLTwoOptionEndRootView;

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


    /**
     * 选择开始结束时间的对话框;
     * OptionTextView 需要进行设置的对象;
     * CompareTextView 进行设置的对象
     */
    public static void showOptionDialog(final Context context, final TextView OptionTextView, final TextView CompareTextView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDay = t.monthDay;
        int mCurrentHour = t.hour;
        //判断点击的设置对象是开始时间还是结束时间,如果是结束时间,默认时间为当前时间加一小时
        if (OptionTextView.getId() == R.id.tv_end_time) {
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
        mNpTwoOptionStartYear = (NumberPicker) _OptionView.findViewById(R.id.np_year);
        mNpTwoOptionStartMonth = (NumberPicker) _OptionView.findViewById(R.id.np_month);
        mNpTwoOptionStartDay = (NumberPicker) _OptionView.findViewById(R.id.np_day);
        mNpHour = (NumberPicker) _OptionView.findViewById(R.id.np_hour);
        mNpMinute = (NumberPicker) _OptionView.findViewById(R.id.np_min);
        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        setNumberPickerDividerColor(context, mNpTwoOptionStartYear);//改变分割线的颜色
        setNumberPickerDividerColor(context, mNpTwoOptionStartMonth);
        setNumberPickerDividerColor(context, mNpTwoOptionStartDay);
        setNumberPickerDividerColor(context, mNpHour);
        setNumberPickerDividerColor(context, mNpMinute);
        mNpHour.setMaxValue(23);
        mNpHour.setMinValue(0);
        mNpHour.setValue(mCurrentHour);

        mNpMinute.setMaxValue(59);
        mNpMinute.setMinValue(0);
        mNpMinute.setValue(mCurrentMinute);

        mNpTwoOptionStartYear.setMaxValue(2036);
        mNpTwoOptionStartYear.setMinValue(1999);
        mNpTwoOptionStartYear.setValue(mCurrentYear);

        mNpTwoOptionStartMonth.setMaxValue(12);
        mNpTwoOptionStartMonth.setMinValue(1);
        mNpTwoOptionStartMonth.setValue(mCurrentMonth);

        mNpTwoOptionStartDay.setMinValue(1);
        mNpTwoOptionStartDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        mNpTwoOptionStartDay.setValue(mCurrentDay);
        /**
         * 传入OptionTextView操作时间的对象，传入CompareTextView结束时间的对象
         * 选择开始（结束）时间，点击完成时判断结束时间是否大于开始时间1小时，
         * 是：直接保存数据，显示数据
         * 否：土司提示，将结束时间设置为开始时间的后一小时
         */

        mTvTimeShow.setText(getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mHour, mMinute, false)); //初始化头标题时间
        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {

            private Date parseOptionTime;
            private Date parseCompareTime;

            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();

                //获取到比较时间的年月日时分
                SimpleDateFormat _s_d_f = new SimpleDateFormat("yyyy年M月d日E HH:mm");
                try {
                    parseCompareTime = _s_d_f.parse(CompareTextView.getText().toString());
                    parseOptionTime = _s_d_f.parse(getWeek(mYear, mMonth, mDay, mHour, mMinute, false));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(parseCompareTime);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);
                int Hours = parseCompareTime.getHours();
                int Minutes = parseCompareTime.getMinutes();

                //操作时间和比较时间的判断
                //1.判断操作时间是开始时间
                // 点击的开始时间,parseOptionTime<parseCompareTime;
                if (OptionTextView.getId() == R.id.tv_start_time) {
                    int i = parseOptionTime.compareTo(parseCompareTime);
                    if (i == -1) {
                        OptionTextView.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false));
                    } else {
                        OptionTextView.setText(getWeek(year, month, day, Hours - 1, Minutes, false));
                        Toast.makeText(context, "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
                    }
                } else if (OptionTextView.getId() == R.id.tv_end_time) {   // 点击的开始时间,parseOptionTime>parseCompareTime;
                    int i = parseOptionTime.compareTo(parseCompareTime);
                    if (i == 1) {
                        OptionTextView.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false));
                    } else {
                        OptionTextView.setText(getWeek(year, month, day, Hours + 1, Minutes, false));
                        Toast.makeText(context, "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //选择事件监听
        //年
        mNpTwoOptionStartYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false)); //更新头标题时间
            }
        });
        //月
        mNpTwoOptionStartMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, false)); //更新头标题时间
            }
        });
        //日
        mNpTwoOptionStartDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
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

        mChooseTimeDialog = new AlertDialog.Builder(context, R.style.style_dialog).
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
    public static void showAllDayOptionDialog(final Context context, final TextView OptionTextView, final TextView CompareTextView) {
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
        mNpTwoOptionStartYear = (NumberPicker) _OptionView.findViewById(R.id.np_year);
        mNpTwoOptionStartMonth = (NumberPicker) _OptionView.findViewById(R.id.np_month);
        mNpTwoOptionStartDay = (NumberPicker) _OptionView.findViewById(R.id.np_day);
        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        setNumberPickerDividerColor(context, mNpTwoOptionStartYear);//改变分割线的颜色
        setNumberPickerDividerColor(context, mNpTwoOptionStartMonth);
        setNumberPickerDividerColor(context, mNpTwoOptionStartDay);

        mNpTwoOptionStartYear.setMaxValue(2036);
        mNpTwoOptionStartYear.setMinValue(1999);
        mNpTwoOptionStartYear.setValue(mCurrentYear);

        mNpTwoOptionStartMonth.setMaxValue(12);
        mNpTwoOptionStartMonth.setMinValue(1);
        mNpTwoOptionStartMonth.setValue(mCurrentMonth);

        mNpTwoOptionStartDay.setMinValue(1);
        mNpTwoOptionStartDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        mNpTwoOptionStartDay.setValue(mCurrentDay);
        mTvTimeShow.setText(getWeek(mCurrentYear, mCurrentMonth, mHour, mMinute, mCurrentDay, true)); //初始化头标题时间
        /**
         * 传入开始时间，结束时间，是点击开始还是结束的标志
         * 选择开始（结束）时间，点击完成时判断结束时间是否大于开始时间1天，
         * 是：直接保存数据，显示数据
         * 否：土司提示，将结束时间设置为开始时间的后一天
         */


        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {
            private Date parseOptionTime;
            private Date parseCompareTime;

            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();

                //获取到比较时间的年月日时分
                SimpleDateFormat _s_d_f = new SimpleDateFormat("yyyy年M月d日E");
                try {
                    parseCompareTime = _s_d_f.parse(CompareTextView.getText().toString());
                    parseOptionTime = _s_d_f.parse(getWeek(mYear, mMonth, mDay, mHour, mMinute, false));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(parseCompareTime);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                //操作时间和比较时间的判断
                //1.判断操作时间是开始时间
                // 点击的开始时间,parseOptionTime<parseCompareTime;
                if (OptionTextView.getId() == R.id.tv_start_time) {
                    int i = parseOptionTime.compareTo(parseCompareTime);
                    if (i == -1) {
                        OptionTextView.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true));
                    } else {
                        OptionTextView.setText(getWeek(year, month, day - 1, mHour, mMinute, true));
                        Toast.makeText(context, "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
                    }
                } else if (OptionTextView.getId() == R.id.tv_end_time) {   // 点击的开始时间,parseOptionTime>parseCompareTime;
                    int i = parseOptionTime.compareTo(parseCompareTime);
                    if (i == 1) {
                        OptionTextView.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true));
                    } else {
                        OptionTextView.setText(getWeek(year, month, day + 1, mHour, mMinute, true));
                        Toast.makeText(context, "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //选择事件监听
        //年
        mNpTwoOptionStartYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true)); //更新头标题时间
            }
        });
        //月
        mNpTwoOptionStartMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true)); //更新头标题时间

            }
        });
        //日
        mNpTwoOptionStartDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDay = newVal;
                mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true)); //更新头标题时间
            }
        });


        mChooseTimeDialog = new AlertDialog.Builder(context, R.style.style_dialog).
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
     * 时间段选择对话框
     */
    public static void showTimeBucketOptionDialog(final Context context, final ArrayList<NetScheduleModel.ViewModelBean.TimeSpanListBean> TimeSpanListBeans, final BaseAdapter adapter) {
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
        if (mCurrentHour < 10) {
            mStartHour = "0" + mCurrentHour;
        } else {
            mStartHour = mCurrentHour + "";
        }

        if (mCurrentHour + 1 < 10) {
            mEndHour = "0" + mCurrentHour;
        } else {
            mEndHour = mCurrentHour + 1 + "";
        }

        if (mCurrentMinute < 10) {
            mStartMinute = "0" + mCurrentMinute;
        } else {
            mStartMinute = mCurrentMinute + "";
        }

        if (mCurrentMinute < 10) {
            mEndMinute = "0" + mCurrentMinute;
        } else {
            mEndMinute = mCurrentMinute + "";
        }


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
        final NetScheduleModel.ViewModelBean.TimeSpanListBean timeSpanListBean = new NetScheduleModel.ViewModelBean.TimeSpanListBean();

        timeSpanListBean.setStartTime(mCurrentHour + ":" + mCurrentMinute);
        timeSpanListBean.setEndTime((mCurrentHour + 1) + ":" + mCurrentMinute);

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


        mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间


        /**
         * 时间段是否可以重复
         * 不可以重复:  将已经选择的时间段的集合传入
         * 遍历集合所有值,满足:  (选择的最小值大于某时间段最小值,小于最大值
         * ||选择的最大值大于某时间段最小值,小于最大值)  表示有重复时间段
         * 可以重复:不需要判断直接添加值并输出
         */

        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {

            private Date parseEndTime;
            private Date parseSrtartTime;

            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();
                //解析字符串判断时间大小
                SimpleDateFormat _s_d_f = new SimpleDateFormat("HH:mm");
                try {
                    parseSrtartTime = _s_d_f.parse(mStartHour + ":" + mStartMinute);
                    parseEndTime = _s_d_f.parse(mEndHour + ":" + mEndMinute);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int i = parseSrtartTime.compareTo(parseEndTime);
                if (i == -1) {
                    timeSpanListBean.setStartTime(mStartHour + ":" + mStartMinute);
                    timeSpanListBean.setEndTime(mEndHour + ":" + mEndMinute);
                    TimeSpanListBeans.add(timeSpanListBean);//添加一个时间段的对象到集合中
                    adapter.notifyDataSetChanged();//刷新Adapter
                }else {
                    Toast.makeText(context, "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //选择事件监听
        //开始小时
        mNpStartHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal < 10) {
                    mStartHour = "0" + newVal;
                } else {
                    mStartHour = newVal + "";
                }
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间
            }
        });
        //开始分钟
        mNpStartMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal < 10) {
                    mStartMinute = "0" + newVal;
                } else {
                    mStartMinute = newVal + "";
                }
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间

            }
        });
        //结束小时
        mNpEndHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal < 10) {
                    mEndHour = "0" + newVal;
                } else {
                    mEndHour = newVal + "";
                }
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间

            }
        });
        //结束分钟
        mNpEndMinute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal < 10) {
                    mEndMinute = "0" + newVal;
                } else {
                    mEndMinute = newVal + "";
                }
                mTvTimeShow.setText(mStartHour + ":" + mStartMinute + "至" + mEndHour + ":" + mEndMinute);//更新头标题时间

            }
        });

        mChooseTimeDialog = new AlertDialog.Builder(context).
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
     * 一起选择开始时间和结束时间
     */
    public static void showTwoDayOptionDialog(final Context context, final TextView StartTextView, final TextView EndTextView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDay = t.monthDay;
        int mCurrentHour = t.hour;
        int mCurrentMinute = t.minute;
        mYear = mCurrentYear;//初始化全局变量
        mEndYear = mCurrentYear;
        mEndMonth = mCurrentMonth;
        mMonth = mCurrentMonth;
        mDay = mCurrentDay;
        mEndDay = mCurrentDay + 1;
        mHour = mCurrentHour;
        mMinute = mCurrentMinute;

        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View _OptionView = layoutInflater.inflate(R.layout.layout_twoday_choose_time, null);
        mLLTwoOptionStartRootView = (LinearLayout) _OptionView.findViewById(R.id.ll_root_start_view);
        mLLTwoOptionEndRootView = (LinearLayout) _OptionView.findViewById(R.id.ll_root_end_view);
        //开始时间
        mNpTwoOptionStartYear = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_start_year);
        mNpTwoOptionStartMonth = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_start_month);
        mNpTwoOptionStartDay = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_start_day);

        //结束时间
        mNpTwoOptionEndYear = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_end_year);
        mNpTwoOptionEndMonth = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_end_month);
        mNpTwoOptionEndDay = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_end_day);

        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        mBtnStartTime = (Button) _OptionView.findViewById(R.id.btn_dialog_start_time);
        mBtnEndTime = (Button) _OptionView.findViewById(R.id.btn_dialog_end_time);

        /**
         *  点击开始显示开始的时间选择器.
         *  点击结束显示结束的时间选择器.
         *   值被传出的时候,判断时间的大小.
         *   结束时间小于等于开始时间,自动调整结束时间为开始时间的后一天.给出土司提示
         */


        //初始化点击事件
        mTvTimeShow.setText(getWeek(mYear, mMonth, mDay, mHour, mMinute, true));//更新头标题时间

        mBtnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击开始按钮
                mBtnStartTime.setBackgroundColor(context.getResources().getColor(R.color.btn_back));
                mBtnEndTime.setBackgroundColor(context.getResources().getColor(R.color.text_white));
                mLLTwoOptionStartRootView.setVisibility(View.VISIBLE);
                mLLTwoOptionEndRootView.setVisibility(View.INVISIBLE);

                String _date = getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);

            }
        });

        mBtnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击结束按钮
                mBtnEndTime.setBackgroundColor(context.getResources().getColor(R.color.btn_back));
                mBtnStartTime.setBackgroundColor(context.getResources().getColor(R.color.text_white));
                mLLTwoOptionStartRootView.setVisibility(View.INVISIBLE);
                mLLTwoOptionEndRootView.setVisibility(View.VISIBLE);

                String _date = getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);//更新头标题时间
            }
        });

        setNumberPickerDividerColor(context, mNpTwoOptionStartYear);//改变分割线的颜色
        setNumberPickerDividerColor(context, mNpTwoOptionStartMonth);
        setNumberPickerDividerColor(context, mNpTwoOptionStartDay);

        mNpTwoOptionStartYear.setMaxValue(2036);
        mNpTwoOptionStartYear.setMinValue(1999);
        mNpTwoOptionStartYear.setValue(mCurrentYear);

        mNpTwoOptionStartMonth.setMaxValue(12);
        mNpTwoOptionStartMonth.setMinValue(1);
        mNpTwoOptionStartMonth.setValue(mCurrentMonth);

        mNpTwoOptionStartDay.setMinValue(1);
        mNpTwoOptionStartDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        mNpTwoOptionStartDay.setValue(mCurrentDay);
        //结束时间
        mNpTwoOptionEndYear.setMaxValue(2036);
        mNpTwoOptionEndYear.setMinValue(1999);
        mNpTwoOptionEndYear.setValue(mEndYear);

        mNpTwoOptionEndMonth.setMaxValue(12);
        mNpTwoOptionEndMonth.setMinValue(1);
        mNpTwoOptionEndMonth.setValue(mEndMonth);

        mNpTwoOptionEndDay.setMinValue(1);
        mNpTwoOptionEndDay.setMaxValue(getDays(mCurrentYear, mCurrentMonth));
        mNpTwoOptionEndDay.setValue(mEndDay);


        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();
                StartTextView.setText(mYear + "年" + mMonth + "月" + mDay + "日");
                EndTextView.setText(mEndYear + "年" + mEndMonth + "月" + mEndDay + "日");
            }
        });
        //选择事件监听
        //年
        mNpTwoOptionStartYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);

            }
        });
        //月
        mNpTwoOptionStartMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);//更新头标题时间
            }
        });
        //日
        mNpTwoOptionStartDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDay = newVal;
                String _date = getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date); //更新头标题时间
            }
        });


        //结束年
        mNpTwoOptionEndYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndYear = newVal;
                int maxMonth = getDays(mEndYear, mEndMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);

            }
        });
        //结束月
        mNpTwoOptionEndMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndMonth = newVal;
                int maxMonth = getDays(mEndYear, mEndMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);//更新头标题时间
            }
        });
        //结束日
        mNpTwoOptionEndDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndDay = newVal;
                String _date = getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date); //更新头标题时间
            }
        });


        mChooseTimeDialog = new AlertDialog.Builder(context).
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

    /**
     * @param year       年
     * @param month      月
     * @param dayOfMonth 日
     * @param hour       时
     * @param minute     分
     * @param isAllDay   是否是全天
     * @return
     */
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
