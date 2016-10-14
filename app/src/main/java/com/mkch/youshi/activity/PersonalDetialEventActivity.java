package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.util.CommonUtil;

import java.util.ArrayList;

public class PersonalDetialEventActivity extends AppCompatActivity {

    private TextView mTvEventBeTime;
    private TextView mTvEventRepPer;
    private TextView mTvEventEndTime;
    private TextView mTvEventStaTime;
    private TextView mTvEventLoc;
    private TextView mTvEventLabel;
    private TextView mTvEventTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detial_event);
        initView();
        initData();
    }

    private void initView() {
        mTvEventTheme = (TextView) findViewById(R.id.tv_event_theme);
        mTvEventLabel = (TextView) findViewById(R.id.tv_event_label);
        mTvEventLoc = (TextView) findViewById(R.id.tv_event_location);
        mTvEventStaTime = (TextView) findViewById(R.id.tv_event_start_time);
        mTvEventEndTime = (TextView) findViewById(R.id.tv_event_end_time);
        mTvEventRepPer = (TextView) findViewById(R.id.tv_event_rep_person);
        mTvEventBeTime = (TextView) findViewById(R.id.tv_event_before_time);
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
        mTvEventTheme.setText(schedule.getTitle());
        mTvEventLabel.setText(CommonUtil.getLabelName(schedule.getLabel()));
        mTvEventLoc.setText(schedule.getAddress());
        mTvEventStaTime.setText(schedule.getBegin_time());
        mTvEventEndTime.setText(schedule.getEnd_time());
        //报送人
        if (repPer != null && repPer.size() != 0 && !repPer.isEmpty()) {
            for (int i = 0; i < repPer.size(); i++) {
                mTvEventRepPer.setText(mTvEventRepPer.getText().toString() +
                        CommonUtil.findFriName(repPer.get(i).getFriendid()));
            }
        }
        mTvEventBeTime.setText("提前" + schedule.getAhead_warn() + "分钟");
    }
}
