package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mkch.youshi.R;

public class AddPersonalHabitActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtTheme;
    private RelativeLayout mChooseAddress;
    private RadioGroup mRgLabel;

    private RelativeLayout mSubmission;
    private RelativeLayout mRemindBefore;
    private LinearLayout mRemark;
    private RelativeLayout mRlHabitCircle;
    private RelativeLayout mRlHabitWeek;
    private RelativeLayout mRlHabitChooseTime;
    private RelativeLayout mRlHabitAllTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_habit);
        initView();
        setListener();
    }

    private void initView() {
        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);

        mRlHabitCircle = (RelativeLayout) findViewById(R.id.rl_habit_circle);
        mRlHabitWeek = (RelativeLayout) findViewById(R.id.rl_habit_week);
        mRlHabitChooseTime = (RelativeLayout) findViewById(R.id.rl_habit_choose_time);
        mRlHabitAllTime = (RelativeLayout) findViewById(R.id.rl_habit_all_time);


        mSubmission = (RelativeLayout) findViewById(R.id.rl_submission);
        mRemindBefore = (RelativeLayout) findViewById(R.id.rl_remind_before);
        mRemark = (LinearLayout) findViewById(R.id.ll_remark);
    }

    private void setListener() {
        //标签
        mRgLabel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_person:
                        Toast.makeText(AddPersonalHabitActivity.this, "点击了个人",
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

        //周期
        mRlHabitCircle.setOnClickListener(this);
        //周
        mRlHabitWeek.setOnClickListener(this);
        //选择时间
        mRlHabitChooseTime.setOnClickListener(this);
        //总时长
        mRlHabitAllTime.setOnClickListener(this);

        //报送
        mSubmission.setOnClickListener(this);
        //提前提醒
        mRemindBefore.setOnClickListener(this);
        //备注
        mRemark.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前半部分
            case R.id.rl_choose_address://选择地址
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseAddressActivity.class));
                break;

            //中间部分的点击事件
            case R.id.rl_habit_circle://周期
                Toast.makeText(AddPersonalHabitActivity.this, "动态计算出来的",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_habit_week://周
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseWeekActivity.class));
                break;
            case R.id.rl_habit_choose_time://选择时间

                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseTimeActivity.class));
                break;
            case R.id.rl_habit_all_time://总时间
                Toast.makeText(AddPersonalHabitActivity.this, "计算出来的",
                        Toast.LENGTH_SHORT).show();
                break;

            //后半部分的点击事件
            case R.id.rl_submission://报送
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseSomeoneActivity.class));
                break;
            case R.id.rl_remind_before://提前提醒
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseRemindBeforeActivity.class));
                break;
            default:
                break;
        }
    }
}

