package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.util.DialogFactory;

public class AddPersonalAffairActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtTheme;
    private RelativeLayout mChooseAddress;
    private RadioGroup mRgLabel;
    private RelativeLayout mAffairDate;
    private RelativeLayout mAffairWeek;
    private RelativeLayout mAffairChooseTime;
    private RelativeLayout mAffairAllTime;
    private RelativeLayout mSubmission;
    private RelativeLayout mRemindBefore;
    private LinearLayout mRemark;
    private TextView mTwoDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_affair);
        initView();
        setListener();
    }

    private void initView() {
        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);

        mAffairDate = (RelativeLayout) findViewById(R.id.rl_affair_date);
        mTwoDate = (TextView) findViewById(R.id.tv_two_date);
        mAffairWeek = (RelativeLayout) findViewById(R.id.rl_affair_week);
        mAffairChooseTime = (RelativeLayout) findViewById(R.id.rl_affair_choose_time);
        mAffairAllTime = (RelativeLayout) findViewById(R.id.rl_affair_all_time);

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
                        Toast.makeText(AddPersonalAffairActivity.this, "点击了个人",
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
        //日期
        mAffairDate.setOnClickListener(this);
        //周
        mAffairWeek.setOnClickListener(this);
        //时间段选择
        mAffairAllTime.setOnClickListener(this);
        //总时长
        mAffairChooseTime.setOnClickListener(this);

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
            case R.id.rl_choose_address://选择日期
                startActivity(new Intent(AddPersonalAffairActivity.this,
                        ChooseAddressActivity.class));
                break;

            case R.id.rl_affair_date://日期
               DialogFactory.showTwoDayOptionDialog(this,mTwoDate);
                break;
            case R.id.rl_affair_week://周
                startActivity(new Intent(AddPersonalAffairActivity.this,
                        ChooseWeekActivity.class));
                break;
            case R.id.rl_affair_choose_time://选择时间
                startActivity(new Intent(AddPersonalAffairActivity.this,
                        ChooseTimeActivity.class));

                break;
            case R.id.rl_affair_all_time://总时长
                Toast.makeText(AddPersonalAffairActivity.this, "4",
                        Toast.LENGTH_SHORT).show();
                break;

            //后半部分的点击事件
            case R.id.rl_submission://报送
                startActivity(new Intent(AddPersonalAffairActivity.
                        this, ChooseSomeoneActivity.class));
                break;
            case R.id.rl_remind_before://提前提醒
                startActivity(new Intent(AddPersonalAffairActivity.
                        this, ChooseRemindBeforeActivity.class));
                break;
            default:
                break;
        }
    }

}
