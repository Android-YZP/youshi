package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_event);
        initView();
        setListener();
    }


    private void initView() {
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
                if (isChecked) {
                    Toast.makeText(AddPersonalEventActivity.this, "选择了全天",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mSubmission.setOnClickListener(this);
        mRemindBefore.setOnClickListener(this);
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
                DialogFactory.showOptionDialog(this);
                break;
            case R.id.rl_end_time://结束时间
                DialogFactory.showDateDialog(this, mTvEndTime);
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
            default:
                break;
        }

    }


}