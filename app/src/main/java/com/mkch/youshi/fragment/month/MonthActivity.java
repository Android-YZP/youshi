package com.mkch.youshi.fragment.month;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.TestMsgActivity;

import org.xutils.common.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthActivity extends AppCompatActivity {
    private static final int SUCCESS = 1;
    //    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private GridView gridView = null;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private NumberPicker mNpSelectYear;
    private NumberPicker mNpSelectMonth;
    private TextView mTvNumPicMonthTitle;
    private TextView mTvNumPicYearTitle;

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
    private int mCurrentPos = 0;
    private ImageView mIvBack;

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
        //拿到从周视图传递过来的数据
        Intent _intent = getIntent();
        String _chooseDate = _intent.getStringExtra("ChooseDate");
        LogUtil.d(_chooseDate+"------------------------------");
        //如果检测到不是当天的日期,就改变月视图的初始值
        if (!_chooseDate.contains("今日")){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                Date parseDate = sdf.parse(_chooseDate);
                currentDate = sdf.format(parseDate); // 当期日期

            } catch (ParseException e) {
                e.printStackTrace();
            }
            year_c = Integer.parseInt(currentDate.split("-")[0]);
            month_c = Integer.parseInt(currentDate.split("-")[1]);
            day_c = Integer.parseInt(currentDate.split("-")[2]);
        }
        //初始化6个页面
        GridView[] views = new GridView[6];//初始化数据
        for (int i = 0; i < 6; i++) {
            GridView gridView = addGridView2();
            calV = new CalendarAdapter(this, getResources(), jumpMonth + i, jumpYear, year_c, month_c, day_c);
            gridView.setAdapter(calV);
            views[i] = gridView;
            LogUtil.d(i + "YZP");
        }
        gridViewPagerAdapter = new PagerAdapter<>(views);
        viewPager.setAdapter(gridViewPagerAdapter);
        viewPager.setCurrentItem(498);
        //初始化日期Adapter,用于显示数据
        currentMonth.setText(year_c + "年" + month_c + "月");
    }

    //查找布局文件的对象
    private void findView() {
        currentMonth = (TextView) findViewById(R.id.tv_month_date);
        mIvBack = (ImageView) findViewById(R.id.iv_month_back);
        viewPager = (ViewPager) findViewById(R.id.vp);
    }

    /**
     * 移动到下一个月
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
    }

    /**
     * 选择事件事件的处理
     * 拿到GridView重新设置Adapter
     */
    private void enterNextMonth3(final int pos, final int year, final int month) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GridView[] allItems = gridViewPagerAdapter.getAllItems();
                allItem = allItems[pos % allItems.length];
                calV = new CalendarAdapter(MonthActivity.this, getResources(), year, month, 4);
//                mHandler.sendEmptyMessage(SUCCESS);
                mHandler.sendEmptyMessageDelayed(SUCCESS, 200);
            }
        }).start();
    }

    /**
     * 左右滑动事件的处理
     * 拿到GridView重新设置Adapter
     */
    private void enterNextMonth2(final int pos) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                temp = pos - temp;//判断是左还是右划的算法。
                if (temp > 0) {
                    if (jumpMonth != (2049 - year_c) * 12 + (12 - month_c))//判断是否到2049年12月，到达这一个月就不再让jumpMonth增加，
                        jumpMonth++;
                    temp = pos;
                } else if (temp < 0) {
                    jumpMonth--;
                    temp = pos;
                }
                GridView[] allItems = gridViewPagerAdapter.getAllItems();
                allItem = allItems[pos % allItems.length];
                calV = new CalendarAdapter(MonthActivity.this, MonthActivity.this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
//                mHandler.sendEmptyMessage(SUCCESS);
                mHandler.sendEmptyMessageDelayed(SUCCESS, 200);
            }
        }).start();
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月
        calV = new CalendarAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        LogUtil.d(calV.getShowMonth() + "----------------");
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
        gridView.setLayoutParams(params);
    }

    private GridView addGridView2() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();
        GridView _gridView = new GridView(this);
        _gridView.setNumColumns(7);
        _gridView.setColumnWidth(40);
        if (Width == 720 && Height == 1280) {
            _gridView.setColumnWidth(40);
        }
        _gridView.setGravity(Gravity.CENTER_VERTICAL);
        _gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        _gridView.setVerticalSpacing(1);
        _gridView.setHorizontalSpacing(1);

        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                calV= (CalendarAdapter) arg0.getAdapter();//得到当前的adapter
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

                    //跳转周视图界面
                    String _ChooseDate = scheduleYear + "-" + scheduleMonth + "-" + scheduleDay;
                    Intent intent = new Intent(MonthActivity.this, TestMsgActivity.class);
                    intent.putExtra("Date",_ChooseDate);
                    finish();
                    jumpMonth = 0;
                    startActivity(intent);
                }
            }
        });
        _gridView.setLayoutParams(params);
        return _gridView;
    }

    //设置监听
    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                jumpMonth = 0;
                startActivity(new Intent(MonthActivity.this,TestMsgActivity.class));
            }
        });

        currentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showSelectDialog(year_c, month_c, year_c + "", month_c + "");
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                enterNextMonth2(position);
                mCurrentPos = position;
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

    /**
     * 弹出日期选择框
     */
    private void showSelectDialog(int CurrentValueYear, int CurrentValueMonth, String CurrentYear, String CurrentMonth) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MonthActivity.this);
        final LayoutInflater inflater = LayoutInflater.from(MonthActivity.this);
        View v = inflater.inflate(R.layout.num_picker_dialog, null);
        mNpSelectYear = (NumberPicker) v.findViewById(R.id.np_select_year);
        mTvNumPicMonthTitle = (TextView) v.findViewById(R.id.tv_number_picker_month_title);
        mTvNumPicYearTitle = (TextView) v.findViewById(R.id.tv_number_picker_year_title);
        mNpSelectMonth = (NumberPicker) v.findViewById(R.id.np_select_month);
        mNpSelectYear.setMinValue(1945);//初始化年设置属性
        mNpSelectYear.setMaxValue(2049);
        mNpSelectYear.setValue(CurrentValueYear);
        mNpSelectMonth.setMinValue(1);//初始化月设置属性
        mNpSelectMonth.setMaxValue(12);
        mNpSelectMonth.setValue(CurrentValueMonth);
        mTvNumPicYearTitle.setText(CurrentYear + "年");
        mTvNumPicMonthTitle.setText(CurrentMonth + "月");
        builder.setView(v);
        mNpSelectYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mTvNumPicYearTitle.setText(newVal + "年");
            }
        });
        mNpSelectMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mTvNumPicMonthTitle.setText(newVal + "月");
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int valueYear = mNpSelectYear.getValue();
                int valueMonth = mNpSelectMonth.getValue();
                enterNextMonth3(mCurrentPos, valueYear, valueMonth);//刷新界面
                //计算跳转的位置
                jumpMonth = (valueYear - year_c) * 12 + (valueMonth - month_c);//跳转到选择的位置
            }
        });
        builder.create().show();
    }

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
        this.finish();
        jumpMonth = 0;
        System.out.println("按下了back键   onBackPressed()");
        startActivity(new Intent(MonthActivity.this,TestMsgActivity.class));
    }

}
