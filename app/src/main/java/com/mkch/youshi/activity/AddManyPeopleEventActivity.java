package com.mkch.youshi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mkch.youshi.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_many_people_event);
        initView();
        setListener();
    }

    private void initView() {
        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);

        mCbAllDay = (CheckBox) findViewById(R.id.cb_all_day);
        mStartTime = (RelativeLayout) findViewById(R.id.rl_start_time);
        mEndTime = (RelativeLayout) findViewById(R.id.rl_end_time);

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
                if (isChecked) {
                    Toast.makeText(AddManyPeopleEventActivity.this, "选择了全天",
                            Toast.LENGTH_SHORT).show();
                }
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前半部分
            case R.id.rl_choose_address://选择地址
                Toast.makeText(AddManyPeopleEventActivity.this, "1",
                        Toast.LENGTH_SHORT).show();
                break;

            //中间部分的点击事件
            case R.id.rl_start_time://开始时间
                Toast.makeText(AddManyPeopleEventActivity.this, "1",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_end_time://结束时间
                Toast.makeText(AddManyPeopleEventActivity.this, "2",
                        Toast.LENGTH_SHORT).show();
                break;


            //后半部分的点击事件
            case R.id.rl_many_people_participant://参与人
                Toast.makeText(AddManyPeopleEventActivity.this, "1",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_many_people_submission://报送
                Toast.makeText(AddManyPeopleEventActivity.this, "2",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_many_people_remind_before://提前提醒
                Toast.makeText(AddManyPeopleEventActivity.this, "3",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_many_people_uploading://上传
                Toast.makeText(AddManyPeopleEventActivity.this, "4",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}
