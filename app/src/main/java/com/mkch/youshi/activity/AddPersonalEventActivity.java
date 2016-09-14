package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.util.DialogFactory;

public class AddPersonalEventActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mChooseAddress;
    private CheckBox mCbAllDay;
    private RelativeLayout mStartTime;
    private RelativeLayout mEndTime;
    private RelativeLayout mSubmission;
    private RelativeLayout mRemindBefore;
    private RadioGroup mRgLabel;
    private EditText mEtTheme;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private Boolean isAllDay = false;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private int mCurrentHour;
    private int mCurrentMinute;
    private TextView mTvCancel;
    private TextView mTvComplete;
    private TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_event);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        mCurrentYear = t.year;
        mCurrentMonth = t.month + 1;
        mCurrentDay = t.monthDay;
        mCurrentHour = t.hour;
        mCurrentMinute = t.minute;
        mTvStartTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour, mCurrentMinute, isAllDay));
        mTvEndTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour + 1, mCurrentMinute, isAllDay));
        mTvTitle.setText("添加个人事件");

    }


    private void initView() {
        mTvCancel = (TextView) findViewById(R.id.tv_add_event_cancel);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
        mTvTitle = (TextView) findViewById(R.id.tv_add_event_title);

        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);

        mCbAllDay = (CheckBox) findViewById(R.id.cb_all_day);
        mStartTime = (RelativeLayout) findViewById(R.id.rl_start_time);
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);
        mEndTime = (RelativeLayout) findViewById(R.id.rl_end_time);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);

        mSubmission = (RelativeLayout) findViewById(R.id.rl_submission);
        mRemindBefore = (RelativeLayout) findViewById(R.id.rl_remind_before);

    }

    private void setListener() {
        //标签
        mRgLabel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_person:
                        Toast.makeText(AddPersonalEventActivity.this, "点击了个人",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_work:
                        break;
                    case R.id.rb_entertainment:
                        break;
                    case R.id.rb_important:
                        break;
                    case R.id.rb_health:
                        break;
                    case R.id.rb_other:
                        break;
                }
            }
        });
        mChooseAddress.setOnClickListener(this);
        //全天
        mCbAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAllDay = isChecked;
                mTvStartTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour, mCurrentMinute, isAllDay));
                mTvEndTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour + 1, mCurrentMinute, isAllDay));
            }
        });
        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mSubmission.setOnClickListener(this);
        mRemindBefore.setOnClickListener(this);
        mTvComplete.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前半部分
            case R.id.rl_choose_address://选择地址
                startActivity(new Intent(AddPersonalEventActivity.
                        this, ChooseAddressActivity.class));
                break;
            //中间部分的点击事件
            case R.id.rl_start_time://开始时间
                if (isAllDay) {
                    DialogFactory.showAllDayOptionDialog(this, mTvStartTime,mTvEndTime);
                } else {
                    DialogFactory.showOptionDialog(this, mTvStartTime,mTvEndTime);
                }
                break;
            case R.id.rl_end_time://结束时间
                if (isAllDay) {
                    DialogFactory.showAllDayOptionDialog(this, mTvEndTime,mTvStartTime);
                } else {
                    DialogFactory.showOptionDialog(this, mTvEndTime,mTvStartTime);
                }
                break;
            //后半部分的点击事件
            case R.id.rl_submission://报送
                startActivity(new Intent(AddPersonalEventActivity.
                        this, ChooseSomeoneActivity.class));
                break;
            case R.id.rl_remind_before://提前提醒
                startActivity(new Intent(AddPersonalEventActivity.
                        this, ChooseRemindBeforeActivity.class));
                break;
            case R.id.tv_add_event_complete://完成
                startActivity(new Intent(AddPersonalEventActivity.
                        this, CalendarActivity.class));
                break;
            case R.id.tv_add_event_cancel://取消
                finish();
                break;
            default:
                break;
        }
    }
}