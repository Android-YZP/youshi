package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schjoiner;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.util.CommonUtil;

import java.util.ArrayList;

public class PersonalDetialsAffairActivity extends AppCompatActivity {

    private TextView mTvAffTheme;
    private TextView mTvAffLab;
    private TextView mTvAffloca;
    private TextView mTvAffTime;
    private TextView mTvAffWhiceW;
    private TextView mTvAffTimePice;
    private TextView mTvAffRepP;
    private TextView mTvAffBefTime;
    private TextView mTvAffTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detials_affair);
        initView();
        initData();
    }

    private void initView() {
        mTvAffTheme = (TextView) findViewById(R.id.tv_affair_theme);
        mTvAffLab = (TextView) findViewById(R.id.tv_affair_label);
        mTvAffloca = (TextView) findViewById(R.id.tv_affair_location);
        mTvAffTime = (TextView) findViewById(R.id.tv_affair_time);
        mTvAffWhiceW = (TextView) findViewById(R.id.tv_affair_which_week);
        mTvAffTimePice = (TextView) findViewById(R.id.tv_affair_time_pice);
        mTvAffTotalTime = (TextView) findViewById(R.id.tv_affair_total_time);
        mTvAffRepP = (TextView) findViewById(R.id.tv_affair_rep_per);
        mTvAffBefTime = (TextView) findViewById(R.id.tv_affair_before_time);
    }

    private void initData() {
        //解析传过来的数据
        Gson gson = new Gson();
        Intent intent = getIntent();
        String s = intent.getStringExtra("mgonsn");
        Schedule schedule = gson.fromJson(s,
                new TypeToken<Schedule>() {
                }.getType());

        ArrayList<Schreport> repPer = CommonUtil.findRepPer(schedule.getId());

        mTvAffTheme.setText(schedule.getTitle());
        mTvAffLab.setText(CommonUtil.getLabelName(schedule.getLabel()));
        mTvAffloca.setText(schedule.getAddress());
        mTvAffTime.setText(schedule.getBegin_time()+"至"+schedule.getEnd_time());
        mTvAffWhiceW.setText(CommonUtil.replaceNumberWeek(schedule.getWhich_week()).substring(0,
                CommonUtil.replaceNumberWeek(schedule.getWhich_week()).length()-1));
//        mTvAffTimePice.setText(schedule.gett());//时间段
        mTvAffTotalTime.setText(schedule.getTotal_time());
        //报送人
        if (repPer != null && repPer.size() != 0 && !repPer.isEmpty()) {
            for (int i = 0; i < repPer.size(); i++) {
                mTvAffRepP.setText(mTvAffRepP.getText().toString() +
                        CommonUtil.findFriName(repPer.get(i).getFriendid()));
            }
        }
        mTvAffBefTime.setText("提前" + schedule.getAhead_warn() + "分钟");

    }
}
