package com.mkch.youshi.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mkch.youshi.MainActivity;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.AddManyPeopleEventActivity;
import com.mkch.youshi.activity.AddPersonalActivity;
import com.mkch.youshi.activity.SearchEventActivity;
import com.mkch.youshi.bean.ArcBean;
import com.mkch.youshi.fragment.month.MonthActivity;
import com.mkch.youshi.fragment.week.DateAdapter;
import com.mkch.youshi.fragment.week.SpecialCalendar;
import com.mkch.youshi.model.SchEveDay;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.util.WeekUtils;
import com.mkch.youshi.view.DayCircleView;
import com.mkch.youshi.view.DayLineView;

import org.xutils.common.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Smith on 2016/8/18.
 */
public class TodayFragment extends Fragment implements GestureDetector.OnGestureListener {
    private ViewFlipper flipper1 = null;
    // private ViewFlipper flipper2 = null;
    private static String TAG = "ZzL";
    private GridView gridView = null;
    private GestureDetector gestureDetector = null;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private int week_c = 0;
    private int week_num = 0;
    private String currentDate = "";
    private static int jumpWeek = 0;
    private static int jumpMonth = 0;
    private static int jumpYear = 0;
    private DateAdapter dateAdapter;
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几,
    private int weeksOfMonth = 0;
    private SpecialCalendar sc = null;
    private boolean isLeapyear = false; // 是否为闰年
    private int selectPostion = 0;
    private String dayNumbers[] = new String[7];
    private int currentYear;
    private int currentMonth;
    private int currentWeek;
    private int currentDay;
    private int currentNum;
    private boolean isStart;// 是否是交接的月初
    //    private TextView mTvWeekDayDate;
    private TextView mTvWeekDayMonth;
    private ImageView mIvWeekdayAdd;
    private ImageView mIvWeekdaySearch;
    private String mChooseDate = null;
    private int mMidth;
    private int mHeight;
    private PopupWindow popupWindow;


    /**
     * 圆盘与直线时间轴
     */
    private LinearLayout mLine1;
    private LinearLayout mHsvLine2;
    private String mMonthChooseDate;
    private int mLapLine = 1;

    public TodayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        //得到屏幕的宽度和高度
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mMidth = wm.getDefaultDisplay().getWidth();
        mHeight = wm.getDefaultDisplay().getHeight();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        findView(view);
        setListener();
        initCalView();
//        initCircleAndLineTime();//初始化圆盘和直线时间轴
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        mMonthChooseDate = ((MainActivity) getActivity()).getmMonthChooseDate();
        if (mMonthChooseDate != null) {//当有从日历月视图传过来数据时,重新初始化数据,并更新界面
            mLapLine = 1;//初始化行数
            initData();
            updateUI(); //刷新界面
            //更新圆盘界面
            String chooseDate = TimesUtils.formatDate(currentYear, currentMonth, currentDay + "");
            initCircleAndLineTime(chooseDate);//初始化圆盘和直线时间轴
        }else {
            mLapLine = 1;//初始化行数
            initCircleAndLineTime(TimesUtils.getTime());//初始化圆盘和直线时间轴
            initData();
            updateUI(); //刷新界面
        }
    }


    //初始化日历视图
    private void initCalView() {
        gestureDetector = new GestureDetector(this);
        dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1);
        addGridView();
        dayNumbers = dateAdapter.getDayNumbers();
        gridView.setAdapter(dateAdapter);
        //标注当前日期位置的背景,
        selectPostion = dateAdapter.getTodayPosition(year_c + "", month_c + "", day_c + "");
        gridView.setSelection(selectPostion);
        flipper1.addView(gridView, 0);
    }

    //初始化日历数据
    private void initData() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        Log.d("YZP3", mMonthChooseDate + "-----------------");
        String _MonthChooseDate = mMonthChooseDate;
        if (_MonthChooseDate == null) {
            _MonthChooseDate = sdf.format(date);
        }
        currentDate = _MonthChooseDate;
        mChooseDate = _MonthChooseDate;
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        currentYear = year_c;
        currentMonth = month_c;
        currentDay = day_c;

        sc = new SpecialCalendar();
        getCalendar(year_c, month_c);
        week_num = getWeeksOfMonth();
        currentNum = week_num;
        if (dayOfWeek == 7) {
            week_c = day_c / 7 + 1;
        } else {
            if (day_c <= (7 - dayOfWeek)) {
                week_c = 1;
            } else {
                if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
                } else {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
                }
            }
        }
        currentWeek = week_c;
        getCurrent();
    }

    //查找布局文件对象
    private void findView(View view) {
        mTvWeekDayMonth = (TextView) view.findViewById(R.id.tv_weekday_month);
        mIvWeekdayAdd = (ImageView) view.findViewById(R.id.iv_weekday_add);
        mIvWeekdaySearch = (ImageView) view.findViewById(R.id.iv_weekday_search);
        flipper1 = (ViewFlipper) view.findViewById(R.id.flipper1);
        //获取从日历界面传递过来的日期,1.用传递过来的日期
        //2.用当前的日期
        mTvWeekDayMonth.setText(year_c + "/" + month_c);

        //圆盘控件和直线时间轴的父控件
        mLine1 = (LinearLayout) view.findViewById(R.id.line_circle);
        mHsvLine2 = (LinearLayout) view.findViewById(R.id.line_line_time);
    }

    //设置事件的监听
    private void setListener() {
        //点击进入月份
        mTvWeekDayMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击月份把当前选择的日期传递过去,
                //如果传过去的是今天的日期则直接启动,不携带参数传过去
                if (mChooseDate == null) mChooseDate = "今日";
                LogUtil.d("androidYZP" + mChooseDate);
                Intent _intent = new Intent(getActivity(), MonthActivity.class);
//                //得到用户当前标记的日期
                _intent.putExtra("ChooseDate", mChooseDate);
//                getActivity().finish();
                startActivity(_intent);
            }
        });
        mIvWeekdayAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
        mIvWeekdaySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    startActivity(new Intent(getActivity(), SearchEventActivity.class));
            }
        });
    }

    /**
     * 显示事件的添加框
     *
     * @param view
     */
    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.layout_pop_window, null);
        LinearLayout _LlBackground = (LinearLayout) contentView.findViewById(R.id.ll_background);
        TextView _TvPersonalEvent = (TextView) contentView.findViewById(R.id.tv_personal_event);
        TextView _TvManyPeopleEvent = (TextView) contentView.findViewById(R.id.tv_many_people_event);

        _LlBackground.getBackground().setAlpha(180);
        //个人事件按钮
        _TvPersonalEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    startActivity(new Intent(getActivity(), AddPersonalActivity.class));
                popupWindow.dismiss();
            }
        });

        // 多人事件按钮
        _TvManyPeopleEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    startActivity(new Intent(getActivity(), AddManyPeopleEventActivity.class));
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView,
                400, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        ColorDrawable dw = new ColorDrawable(0000);
        popupWindow.setBackgroundDrawable(dw);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //刷新界面
    private void updateUI() {
        addGridView();
        getCurrent();
        dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1);
        dayNumbers = dateAdapter.getDayNumbers();
        gridView.setAdapter(dateAdapter);
        //储存当前用户选择的日期
        mChooseDate = dateAdapter.getCurrentYear(selectPostion) + "-"
                + dateAdapter.getCurrentMonth(selectPostion) + "-"
                + dayNumbers[selectPostion];
        mTvWeekDayMonth.setText(dateAdapter.getCurrentYear(selectPostion) + "/"
                + dateAdapter.getCurrentMonth(selectPostion));
        flipper1.addView(gridView);//添加一个日历视图
        dateAdapter.setSeclection(selectPostion);//选中默认位置
        selectPostion = dateAdapter.getTodayPosition(year_c + "", month_c + "", day_c + "");//标注选中的背景
        this.flipper1.setInAnimation(AnimationUtils.loadAnimation(getActivity(),//动画
                R.anim.push_left_in));
        this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.push_left_out));
        this.flipper1.showNext();
        flipper1.removeViewAt(0);//移除第一个不用的视图
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 判断某年某月所有的星期数
     */
    public int getWeeksOfMonth(int year, int month) {
        // 先判断某月的第一天为星期几
        int preMonthRelax = 0;
        int dayFirst = getWhichDayOfWeek(year, month);
        int days = sc.getDaysOfMonth(sc.isLeapYear(year), month);
        if (dayFirst != 7) {
            preMonthRelax = dayFirst;
        }
        if ((days + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (days + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (days + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    /**
     * 判断某年某月的第一天为星期几
     */
    public int getWhichDayOfWeek(int year, int month) {
        return sc.getWeekdayOfMonth(year, month);
    }

    /**
     * 得到一周的最后一天
     *
     * @param year
     * @param month
     */
    public int getLastDayOfWeek(int year, int month) {
        return sc.getWeekDayOfLastMonth(year, month,
                sc.getDaysOfMonth(isLeapyear, month));
    }

    //得到某年某月的信息
    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
    }

    //得到这个月有几周
    public int getWeeksOfMonth() {
        // getCalendar(year, month);
        int preMonthRelax = 0;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        gridView = new GridView(getActivity());
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i(TAG, "day:" + dayNumbers[position]);
                selectPostion = position;
                dateAdapter.setSeclection(position);
                dateAdapter.notifyDataSetChanged();
                int Year = dateAdapter.getCurrentYear(selectPostion);
                int Month = dateAdapter.getCurrentMonth(selectPostion);
                String day = dayNumbers[position];
                String chooseDate = TimesUtils.formatDate(Year, Month, day);
                UIUtils.showTip(chooseDate);

                mLapLine = 1;//初始化行数
                initCircleAndLineTime(chooseDate);

            }
        });
        gridView.setLayoutParams(params);
    }


    @Override
    public void onPause() {
        super.onPause();
        jumpWeek = 0;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int gvFlag = 0;
        if (e1.getX() - e2.getX() > 80) {
            // 向左滑
            addGridView();
            currentWeek++;
            getCurrent();
            dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
                    currentMonth, currentWeek, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            dayNumbers = dateAdapter.getDayNumbers();
            gridView.setAdapter(dateAdapter);
            //储存当前用户选择的日期
            mChooseDate = dateAdapter.getCurrentYear(selectPostion) + "-"
                    + dateAdapter.getCurrentMonth(selectPostion) + "-"
                    + dayNumbers[selectPostion];
            mTvWeekDayMonth.setText(dateAdapter.getCurrentYear(selectPostion) + "/"
                    + dateAdapter.getCurrentMonth(selectPostion));
            gvFlag++;
            flipper1.addView(gridView, gvFlag);
            dateAdapter.setSeclection(selectPostion);
            this.flipper1.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_left_in));
            this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_left_out));
            this.flipper1.showNext();
            flipper1.removeViewAt(0);
            return true;
        } else if (e1.getX() - e2.getX() < -80) {
            addGridView();
            currentWeek--;
            getCurrent();
            dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
                    currentMonth, currentWeek, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            dayNumbers = dateAdapter.getDayNumbers();
            gridView.setAdapter(dateAdapter);
            //储存当前用户选择的日期
            mChooseDate = dateAdapter.getCurrentYear(selectPostion) + "-"
                    + dateAdapter.getCurrentMonth(selectPostion) + "-"
                    + dayNumbers[selectPostion] + "-";
            mTvWeekDayMonth.setText(dateAdapter.getCurrentYear(selectPostion) + "/"
                    + dateAdapter.getCurrentMonth(selectPostion));
            gvFlag++;
            flipper1.addView(gridView, gvFlag);
            dateAdapter.setSeclection(selectPostion);
            this.flipper1.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_right_in));
            this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_right_out));
            this.flipper1.showPrevious();
            flipper1.removeViewAt(0);
            return true;
        }
        return false;
    }


    /**
     * 重新计算当前的年月
     */
    public void getCurrent() {
        if (currentWeek > currentNum) {
            if (currentMonth + 1 <= 12) {
                currentMonth++;
            } else {
                currentMonth = 1;
                currentYear++;
            }
            currentWeek = 1;
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
        } else if (currentWeek == currentNum) {
            if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
            } else {
                if (currentMonth + 1 <= 12) {
                    currentMonth++;
                } else {
                    currentMonth = 1;
                    currentYear++;
                }
                currentWeek = 1;
                currentNum = getWeeksOfMonth(currentYear, currentMonth);
            }

        } else if (currentWeek < 1) {
            if (currentMonth - 1 >= 1) {
                currentMonth--;
            } else {
                currentMonth = 12;
                currentYear--;
            }
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
            currentWeek = currentNum - 1;
        }
    }


    /**
     * 初始化圆盘和直线时间轴
     */
    private void initCircleAndLineTime(String Date) {
        ArrayList<SchEveDay> TodaytimeBucket = CommonUtil.getTimeBucket(Date);
        if (TodaytimeBucket == null) TodaytimeBucket = new ArrayList<>();//防止今天没有其他日程报空异常
        //添加个人习惯到TodaytimeBucket中,让大圆盘显示.
        ArrayList<Schedule> habitSchs = CommonUtil.findHabitSch();
        if (habitSchs != null) {
            //遍历所有今天的习惯中的周有没有今天的日程
            for (int i = 0; i < habitSchs.size(); i++) {
                Schedule habitSch = habitSchs.get(i);
                String weeks = WeekUtils.replaceWeek1(WeekUtils.replaceWeek3(habitSch.getWhich_week()));
                //得到今天是周几
                String weekOfToday = TimesUtils.getWeekOfDate(Date);
                if (weeks.contains(weekOfToday)) {
                    //今日有习惯，添加到TodaytimeBucket中，让大圆盘显示。
                    ArrayList<Schtime> schTime = CommonUtil.findSchTime(habitSch.getId());
                    for (int j = 0; j < schTime.size(); j++) {
                        SchEveDay schEveDay = new SchEveDay();
                        schEveDay.setSid(habitSch.getId());
                        schEveDay.setBegin_time(schTime.get(j).getBegin_time());
                        schEveDay.setEnd_time(schTime.get(j).getEnd_time());
                        schEveDay.setDate(TimesUtils.getNowTime(true).substring(0, 11));
                        TodaytimeBucket.add(schEveDay);
                    }

                }
            }
        }

        //根据集合中的时间日期来把集合重新排序
        if (TodaytimeBucket != null && TodaytimeBucket.size() != 0) {//今天没有日程
            Collections.sort(TodaytimeBucket, new Comparator<SchEveDay>() {
                @Override
                public int compare(SchEveDay lhs, SchEveDay rhs) {
                    String _datetime_1 = lhs.getBegin_time();
                    String _datetime_2 = rhs.getBegin_time();
                    SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm");
                    Date _date_1 = null;
                    Date _date_2 = null;
                    try {
                        _date_1 = _sdf.parse(_datetime_1);
                        _date_2 = _sdf.parse(_datetime_2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return _date_1.compareTo(_date_2);
                }
            });


            //封装大圆盘初步数据
            List<ArcBean> _arc_beans2 = new ArrayList<>();
            for (int i = 0; i < TodaytimeBucket.size(); i++) {
                UIUtils.LogUtils("排序之后" + TodaytimeBucket.get(i).getBegin_time());
                ArcBean _bean1 = new ArcBean(TodaytimeBucket.get(i).getBegin_time()
                        , TodaytimeBucket.get(i).getEnd_time(),
                        CommonUtil.findSch(TodaytimeBucket.get(i).getSid() + "").get(0).getLabel() + 1,
                        i + 1,
                        0);
                _arc_beans2.add(_bean1);
            }

            //只要判断一个时间段比前一个时间段是否有重叠部分,有则在前一个基础上加一,
            // 先将数据封装成一个集合,再来处理这个数据判断和前一个数据是否有重叠部分,遍历所有数据
            _arc_beans2.get(0).setOverlap_line(1);//一天的第一个时间段默认在第一行
            for (int i = 0; i < _arc_beans2.size() - 1; i++) {
                String startTime = _arc_beans2.get(i + 1).getStarttime();//开始时间(拿到前个数据的开始时间和后一个数据的结束时间做比较)
                String endTime = _arc_beans2.get(i).getEndtime();

                if (TimesUtils.compareHourmin(startTime, endTime) < 0) {//开始时间 <上一个结束时间
                    _arc_beans2.get(i + 1).setOverlap_line(++mLapLine);
                } else {
                    _arc_beans2.get(i + 1).setOverlap_line(1);
                }
            }

            for (int i = 0; i < _arc_beans2.size(); i++) {
                UIUtils.LogUtils("排列之后的行数" + _arc_beans2.get(i).getStarttime() +
                        "==>" + _arc_beans2.get(i).getOverlap_line() + "");
            }

            //自定义圆盘
            DayCircleView _day_circle_view = new DayCircleView(getActivity(), _arc_beans2);
//            _day_circle_view.setLayoutParams(new LinearLayout.LayoutParams(1000,1000));
            mLine1.removeAllViews();
            mLine1.addView(_day_circle_view);


            //自定义直线时间轴控件
            DayLineView _day_line_view = new DayLineView(getActivity(), _arc_beans2);
            _day_line_view.setLayoutParams(new LinearLayout.LayoutParams(1480, ViewGroup.LayoutParams.MATCH_PARENT));
            mHsvLine2.removeAllViews();
            mHsvLine2.addView(_day_line_view);

        } else {
            List<ArcBean> _arc_beans = new ArrayList<>();
            ArcBean _bean1 = new ArcBean("12:00", "12:00", 7, 3, 1);
            _arc_beans.add(_bean1);
            //自定义圆盘

            DayCircleView _day_circle_view = new DayCircleView(UIUtils.getContext(), _arc_beans);
//            _day_circle_view.setLayoutParams(new LinearLayout.LayoutParams(1000,1000));
            mLine1.removeAllViews();
            mLine1.addView(_day_circle_view);


            //自定义直线时间轴控件
            DayLineView _day_line_view = new DayLineView(getActivity(), _arc_beans);
            _day_line_view.setLayoutParams(new LinearLayout.LayoutParams(1480, ViewGroup.LayoutParams.MATCH_PARENT));
            mHsvLine2.removeAllViews();
            mHsvLine2.addView(_day_line_view);
        }


//        if (getActivity() != null) {
//            try {
//                //圆盘控件
//                //数据集合
//                List<ArcBean> _arc_beans = new ArrayList<>();
//                ArcBean _bean1 = new ArcBean("13:30", "14:00", 1, 3, 1);
//                ArcBean _bean2 = new ArcBean("15:00", "16:00", 2, 4, 1);
//                ArcBean _bean3 = new ArcBean("17:00", "18:00", 3, 5, 1);
//                ArcBean _bean4 = new ArcBean("17:30", "20:00", 4, 6, 2);
//                ArcBean _bean8 = new ArcBean("17:50", "20:00", 1, 7, 3);
//                ArcBean _bean5 = new ArcBean("21:00", "22:00", 5, 8, 1);
//                ArcBean _bean6 = new ArcBean("00:00", "01:00", 6, 1, 1);
//                ArcBean _bean7 = new ArcBean("11:00", "13:00", 4, 2, 1);
//
//                _arc_beans.add(_bean1);
//                _arc_beans.add(_bean2);
//                _arc_beans.add(_bean3);
//                _arc_beans.add(_bean4);
//                _arc_beans.add(_bean5);
//                _arc_beans.add(_bean6);
//                _arc_beans.add(_bean7);
//                _arc_beans.add(_bean8);
//
//                //自定义圆盘
//                DayCircleView _day_circle_view = new DayCircleView(getActivity(), _arc_beans);
////            _day_circle_view.setLayoutParams(new LinearLayout.LayoutParams(1000,1000));
//                mLine1.addView(_day_circle_view);
//
//
//                //自定义直线时间轴控件
//                DayLineView _day_line_view = new DayLineView(getActivity(), _arc_beans);
//                _day_line_view.setLayoutParams(new LinearLayout.LayoutParams(1480, ViewGroup.LayoutParams.MATCH_PARENT));
//                mHsvLine2.addView(_day_line_view);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
