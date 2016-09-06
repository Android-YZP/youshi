package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.util.DialogFactory;

public class AddManyPeopleEventActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtTheme;
    private RelativeLayout mChooseAddress;
    private RadioGroup mRgLabel;
    private CheckBox mCbAllDay;
    private RelativeLayout mStartTime;
    private RelativeLayout mEndTime;
    private RelativeLayout mManyPeopleParticipant;
    private RelativeLayout mManyPeopleSubmission;
    private RelativeLayout mManyPeopleRemindBefore;
    private RelativeLayout mManyPeopleUploading;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_many_people_event);
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

    }

    private void initView() {
        mTvCancel = (TextView) findViewById(R.id.tv_add_event_cancel);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);

        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);

        mCbAllDay = (CheckBox) findViewById(R.id.cb_all_day);
        mStartTime = (RelativeLayout) findViewById(R.id.rl_start_time);
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);
        mEndTime = (RelativeLayout) findViewById(R.id.rl_end_time);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);

        mManyPeopleParticipant = (RelativeLayout) findViewById(R.id.rl_many_people_participant);
        mManyPeopleSubmission = (RelativeLayout) findViewById(R.id.rl_many_people_submission);
        mManyPeopleRemindBefore = (RelativeLayout) findViewById(R.id.rl_many_people_remind_before);
        mManyPeopleUploading = (RelativeLayout) findViewById(R.id.rl_many_people_uploading);
    }

    private void setListener() {
        //标签
        mRgLabel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_person:
                        Toast.makeText(AddManyPeopleEventActivity.this, "点击了个人",
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
        //选择地址
        mChooseAddress.setOnClickListener(this);

        //全天
        mCbAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isAllDay = isChecked;
                    mTvStartTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour, mCurrentMinute, isAllDay));
                    mTvEndTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour + 1, mCurrentMinute, isAllDay)); isAllDay = isChecked;
            }
        });
        //开始时间
        mStartTime.setOnClickListener(this);
        //结束时间
        mEndTime.setOnClickListener(this);

        //参与人
        mManyPeopleParticipant.setOnClickListener(this);
        //报送
        mManyPeopleSubmission.setOnClickListener(this);
        //提前提醒
        mManyPeopleRemindBefore.setOnClickListener(this);
        //上传
        mManyPeopleUploading.setOnClickListener(this);

        mTvComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前半部分
            case R.id.rl_choose_address://选择地址
                startActivity(new Intent(AddManyPeopleEventActivity.
                        this, ChooseAddressActivity.class));
                break;

            //中间部分的点击事件
            case R.id.rl_start_time://开始时间
                if (isAllDay) {
                    DialogFactory.showAllDayOptionDialog(this, mTvStartTime);
                } else {
                    DialogFactory.showOptionDialog(this, mTvStartTime);
                }
                break;
            case R.id.rl_end_time://结束时间
                if (isAllDay) {
                    DialogFactory.showAllDayOptionDialog(this, mTvEndTime);
                } else {
                    DialogFactory.showOptionDialog(this, mTvEndTime);
                }
                break;


            //后半部分的点击事件
            case R.id.rl_many_people_participant://参与人
                startActivity(new Intent(AddManyPeopleEventActivity.
                        this, ChooseSomeoneActivity.class));
                break;
            case R.id.rl_many_people_submission://报送
                startActivity(new Intent(AddManyPeopleEventActivity.
                        this, ChooseSomeoneActivity.class));
                break;
            case R.id.rl_many_people_remind_before://提前提醒
                startActivity(new Intent(AddManyPeopleEventActivity.
                        this, ChooseRemindBeforeActivity.class));
                break;
            case R.id.rl_many_people_uploading://上传
                Toast.makeText(AddManyPeopleEventActivity.this, "上传附件",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_add_event_complete://完成
                startActivity(new Intent(AddManyPeopleEventActivity.
                        this, CalendarActivity.class));
                break;
            default:
                break;
        }

    }
}
