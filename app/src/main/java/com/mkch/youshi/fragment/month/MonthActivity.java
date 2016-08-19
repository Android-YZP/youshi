package com.mkch.youshi.fragment.month;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mkch.youshi.R;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthActivity extends AppCompatActivity{
    private static final int SUCCESS = 1;
    //    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private ViewFlipper flipper = null;
    private GridView gridView = null;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;
    /**
     * 当前的年月，现在日历顶端
     */
    private TextView currentMonth;
    /**
     * 上个月
     */
    private ImageView prevMonth;
    /**
     * 下个月
     */
    private ImageView nextMonth;
    private ViewPager viewPager;
    private PagerAdapter<GridView> gridViewPagerAdapter;
    private CalendarAdapter calendarAdapter;
    private GridView allItem;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    allItem.setAdapter(calV);
                    addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
            }
        }
    };
    private int temp = 498;

    public MonthActivity() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_month);
        findView();
        initData();
        setListener();//设置上一个月,下个月的监听
    }

    //初始化数据
    private void initData() {
        //CalendarCard[] views = new CalendarCard[3];
        GridView[] views = new GridView[6];//初始化数据
        for (int i = 0; i < 6; i++) {
            GridView gridView = addGridView2();
            calV = new CalendarAdapter(this, getResources(), jumpMonth + i, jumpYear, year_c, month_c, day_c);
            gridView.setAdapter(calV);
            views[i] = gridView;
            LogUtil.d(i+"YZP");
        }
        gridViewPagerAdapter = new PagerAdapter<>(views);
        viewPager.setAdapter(gridViewPagerAdapter);
        viewPager.setCurrentItem(498);
        //初始化日期Adapter,用于显示数据
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();//添加一个GridView布局显示当前月的数据
        gridView.setAdapter(calV);//设置Adapter显示数据
        addTextToTopTextView(currentMonth);//顶部的当前日期显示
    }

    //查找布局文件的对象
    private void findView() {
        currentMonth = (TextView) findViewById(R.id.tv_month_date);
//        prevMonth = (ImageView) findViewById(R.id.prevMonth);
//        nextMonth = (ImageView) findViewById(R.id.nextMonth);
        viewPager = (ViewPager) findViewById(R.id.vp);
        flipper = (ViewFlipper) findViewById(R.id.flipper);//用于添加详细的日期数据
    }

//    //初始化手势识别器
//    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
//            if (e1.getY() - e2.getY() > 120) {
//                // 像左滑动
//                enterNextMonth(gvFlag);
//                return true;
//            } else if (e1.getY() - e2.getY() < -120) {
//                // 向右滑动
//                enterPrevMonth(gvFlag);
//                return true;
//            }
//            return false;
//        }
//    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

//    // 更新日历视图
//    private void updateCalendarView(int arg0) {
//        mShowViews = adapter.getAllItems();
//        if (mDirection == SildeDirection.RIGHT) {
//            mShowViews[arg0 % mShowViews.length].rightSlide();
//        } else if (mDirection == SildeDirection.LEFT) {
//            mShowViews[arg0 % mShowViews.length].leftSlide();
//        }
//        mDirection = SildeDirection.NO_SILDE;
//    }


    /**
     * 左右滑动事件的处理
     * 拿到GridView重新设置Adapter
     */
    private void enterNextMonth2(final int pos) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                temp = pos - temp;
                if (temp > 0) {
                    jumpMonth++;
                    temp = pos;
                } else {
                    jumpMonth--;
                    temp = pos;
                }
                GridView[] allItems = gridViewPagerAdapter.getAllItems();
                Log.d("posYZP", "YZP" + (pos % allItems.length));
                allItem = allItems[pos % allItems.length];
                calV = new CalendarAdapter(MonthActivity.this, MonthActivity.this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
//                mHandler.sendEmptyMessage(SUCCESS);
                mHandler.sendEmptyMessageDelayed(SUCCESS, 200);
            }
        }).start();
    }

    /**
     * 移动到上一个月
     *  @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }


    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
//        gridView.setOnTouchListener(new View.OnTouchListener() {
//            // 将gridview中的触摸事件回传给gestureDetector
//            public boolean onTouch(View v, MotionEvent event) {
//                return MonthActivity.this.gestureDetector.onTouchEvent(event);
//            }
//        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    Toast.makeText(MonthActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();
                }
            }
        });
        gridView.setLayoutParams(params);
    }

    private GridView addGridView2() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();
        GridView _gridView = new GridView(this);
        _gridView.setNumColumns(7);
        _gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            _gridView.setColumnWidth(40);
        }
        _gridView.setGravity(Gravity.CENTER_VERTICAL);
        _gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        _gridView.setVerticalSpacing(1);
        _gridView.setHorizontalSpacing(1);
//        _gridView.setOnTouchListener(new View.OnTouchListener() {
//            // 将gridview中的触摸事件回传给gestureDetector
//
//            public boolean onTouch(View v, MotionEvent event) {
//                return MonthActivity.this.gestureDetector.onTouchEvent(event);
//            }
//        });

        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    Toast.makeText(MonthActivity.this, scheduleYear + "-" + scheduleMonth + "-" + scheduleDay, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(CalendarActivity.this, "点击了该条目",
                    // Toast.LENGTH_SHORT).show();
//                    TextView textView = (TextView) arg1.findViewById(R.id.tvtext);
//                    textView.setTextColor(Color.RED);
                }
            }
        });
        _gridView.setLayoutParams(params);
        return _gridView;
    }

    //设置监听
    private void setListener() {
        currentMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                flipper.clearAnimation();
                Toast.makeText(MonthActivity.this, "点击时间了", Toast.LENGTH_SHORT).show();
                calV = new CalendarAdapter(MonthActivity.this, getResources(), 2015, 7, 4);
                addGridView();//添加一个GridView布局显示当前月的数据
                gridView.setAdapter(calV);//设置Adapter显示数据
                flipper.removeAllViews();//把flipper中移除所有数据
                flipper.addView(gridView);//将GridView添加到flipper中
                addTextToTopTextView(currentMonth);//顶部的当前日期显示
                //计算出与当前位置的偏差。
                jumpMonth = -13;//1年龄3个月
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d("viewPager的位置", "YZP" + position);
                enterNextMonth2(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //3260782110
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


    }


}
