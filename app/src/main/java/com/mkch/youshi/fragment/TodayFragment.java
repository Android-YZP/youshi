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

import com.mkch.youshi.R;
import com.mkch.youshi.activity.AddManyPeopleEventActivity;
import com.mkch.youshi.activity.AddPersonalActivity;
import com.mkch.youshi.activity.SearchEventActivity;
import com.mkch.youshi.fragment.month.MonthActivity;
import com.mkch.youshi.fragment.week.DateAdapter;
import com.mkch.youshi.fragment.week.SpecialCalendar;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getActivity().getIntent();
        String _MonthChooseDate = intent.getStringExtra("Date");
        Log.d("YZP.............", _MonthChooseDate + "YZP");
        if (_MonthChooseDate != null) {//当有从日历月视图传过来数据时,重新初始化数据,并更新界面
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
        Intent intent = getActivity().getIntent();
        String _MonthChooseDate = intent.getStringExtra("Date");
        Log.d("YZP3", _MonthChooseDate + "-----------------");
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
                Toast.makeText(getActivity(), dateAdapter.getCurrentYear(selectPostion) + "年"
                        + dateAdapter.getCurrentMonth(selectPostion) + "月"
                        + dayNumbers[position] + "日", Toast.LENGTH_SHORT).show();
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

}
