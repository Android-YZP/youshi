package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bruce.pickerview.LoopScrollListener;
import com.bruce.pickerview.LoopView;
import com.mkch.youshi.R;
import com.mkch.youshi.util.TimesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Smith on 2016/10/28.
 */

public class TimePickerView extends FrameLayout {

    private LoopView mLvYear;
    private LoopView mLvMonth;
    private FrameLayout mFraLvDay;
    private LoopView mLvHour;
    private LoopView mLvMin;
    private int totalDay;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int textSize;
    private String mChooseYear;
    private String mChooseMonth;
    private String mChooseDay;
    private String mChooseHour;
    private String mChooseMinute;
    private int currentYear;
    private int currentMonth;
    private List<String> dayData;
    private Context context;

    private boolean isAllDay = false;//是否是全天

    public TimePickerView(Context context) {
        super(context);
        initView(context);
        initData(context);
    }

    public TimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context);
    }

    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData(context);
    }

    private void initView(Context context) {
        this.context = context;

        View v = inflate(context, R.layout.pick_time, null);
        mLvYear = (LoopView) v.findViewById(R.id.loop_view_year);
        mLvMonth = (LoopView) v.findViewById(R.id.loop_view_month);
        mFraLvDay = (FrameLayout) v.findViewById(R.id.ll_loop_view_day);
        mLvHour = (LoopView) v.findViewById(R.id.loop_view_hour);
        mLvMin = (LoopView) v.findViewById(R.id.loop_view_min);
        addView(v);
    }

    private void initData(Context context) {
        textSize = 18;//设定字体的大小
        //得到当前时间
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        mChooseYear = year + "年";
        mChooseMonth = getFormatDate(month) + "月";
        mChooseDay = getFormatDate(day) + "日";
        mChooseHour = getFormatDate(hour) + "";
        mChooseMinute = getFormatDate(minute) + "";


        currentYear = year;
        currentMonth = month;

        //初始化每个滚轮里面的数据
        dayData = new ArrayList<>();
        getYearData();
        getMonthData();
        Log.d("天数2", year + "" + month);
        setDayData(year, month);
        getHourData();
        getMinuteData();
        setIsAllDay(false);
    }

    private void getYearData() {
        final List<String> yearData = new ArrayList<>();
        for (int i = 1999; i < 2036; i++) {
            yearData.add(i + "年");
        }

        mLvYear.setInitPosition(year - 1999);
        mLvYear.setCanLoop(true);
        mLvYear.setLoopListener(new LoopScrollListener() {

            @Override
            public void onItemSelect(int item) {
                mChooseYear = yearData.get(item);

                //刷新日的天数
                currentYear = Integer.parseInt(mChooseYear.substring(0, mChooseYear.length() - 1));
                setDayData(currentYear, currentMonth);

                if (listener != null)
                    listener.onItemSelected(getDateString());
            }
        });
        mLvYear.setTextSize(textSize);//must be called before setDateList
        mLvYear.setDataList(yearData);
    }

    private void getMonthData() {
        final List<String> monthData = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (i >= 10) {
                monthData.add(i + "月");
            } else {
                monthData.add("0" + i + "月");
            }
        }

        mLvMonth.setInitPosition(month - 1);
        mLvMonth.setCanLoop(true);
        mLvMonth.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                mChooseMonth = monthData.get(item);

                //刷新日的天数
                currentMonth = Integer.parseInt(mChooseMonth.substring(0, mChooseMonth.length() - 1));
                setDayData(currentYear, currentMonth);

                if (listener != null)
                    listener.onItemSelected(getDateString());
            }
        });
        mLvMonth.setTextSize(textSize + 1);//must be called before setDateList
        mLvMonth.setDataList(monthData);
    }

    private void getHourData() {
        final List<String> hourData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i >= 10) {
                hourData.add(i + "");
            } else {
                hourData.add("0" + i);
            }
        }

        mLvHour.setInitPosition(hour);
        mLvHour.setCanLoop(true);
        mLvHour.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                mChooseHour = hourData.get(item);
                if (listener != null)
                    listener.onItemSelected(getDateString());
            }
        });
        mLvHour.setTextSize(textSize + 3);//must be called before setDateList
        mLvHour.setDataList(hourData);
    }

    private void getMinuteData() {
        final List<String> minData = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i >= 10) {
                minData.add(i + "");
            } else {
                minData.add("0" + i);
            }
        }

        mLvMin.setInitPosition(minute);
        mLvMin.setCanLoop(true);
        mLvMin.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                mChooseMinute = minData.get(item);
                if (listener != null)
                    listener.onItemSelected(getDateString());
            }
        });
        mLvMin.setTextSize(textSize + 3);//must be called before setDateList
        mLvMin.setDataList(minData);
    }

    /**
     * 根据年和月动态的获取天数
     */
    private void setDayData(int year, int month) {
        mFraLvDay.removeAllViews();
        LoopView lvDay = new LoopView(context);
        final List<String> dayData = new ArrayList<>();

        int daysByYearMonth = TimesUtils.getDaysByYearMonth(year, month);
        Log.d("天数", daysByYearMonth + "" + year + "" + month);

        for (int i = 1; i <= daysByYearMonth; i++) {
            if (i >= 10) {
                dayData.add(i + "日");
            } else {
                dayData.add("0" + i + "日");
            }
        }

        //设置默认的选中值:减一的原因是代码数数是从0开始,而天数是从1开始
        if (daysByYearMonth > day) {
            lvDay.setInitPosition(day - 1);

        } else {
            lvDay.setInitPosition(daysByYearMonth - 1);
            day = daysByYearMonth;
        }
        mChooseDay = day + "日";//年和月的变化会引起日的变化

        lvDay.setCanLoop(true);
        lvDay.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                day = item + 1;//储存用户选中的默认值
                mChooseDay = dayData.get(item);
                if (listener != null)
                    listener.onItemSelected(getDateString());
            }
        });

        lvDay.setTextSize(textSize + 1);//must be called before setDateList
        lvDay.setDataList(dayData);
        mFraLvDay.addView(lvDay);
    }

    /**
     * 格式化日期 1==>01
     */
    private String getFormatDate(int date) {
        if (date < 10) {
            return "0" + date;
        } else {
            return date + "";
        }
    }

    //观察者进行通讯的对象
    private OnItemSelectedListener listener;

    //定义一个接口，作为观察者
    public interface OnItemSelectedListener {
        public void onItemSelected(String date);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * 用于得到当前的时间字符串
     */
    private String getDateString() {
        if (isAllDay) {
            return mChooseYear + mChooseMonth + mChooseDay + mChooseHour;
        } else {
            return mChooseYear + mChooseMonth + mChooseDay + mChooseHour + ":" + mChooseMinute;
        }
    }

    public void setIsAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
        if (isAllDay) {
            mLvHour.setVisibility(GONE);
            mLvMin.setVisibility(GONE);
        } else {
            mLvHour.setVisibility(VISIBLE);
            mLvMin.setVisibility(VISIBLE);
        }
    }


    /**
     * 设置只有小时和分钟的选择
     */
    public void setSpanTime(boolean isSpanTime) {
        if (isSpanTime) {
            mLvYear.setVisibility(GONE);
            mLvMonth.setVisibility(GONE);
            mFraLvDay.setVisibility(GONE);
        } else {
            mLvYear.setVisibility(VISIBLE);
            mLvMonth.setVisibility(VISIBLE);
            mFraLvDay.setVisibility(VISIBLE);
        }
    }
}
