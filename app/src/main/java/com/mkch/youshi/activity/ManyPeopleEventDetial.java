package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.ManyPeopleEvenBean;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;

public class ManyPeopleEventDetial extends AppCompatActivity {

    private ManyPeopleEvenBean mManyPeopleDatas;
    private TextView mTvEventsTheme;
    private TextView mTvEventsLabel;
    private TextView mTvEventLocation;
    private TextView mTvEventsStTime;
    private TextView mTvEventEndTime;
    private TextView mTvEventsRepPer;
    private TextView mTvEventsJoinPer;
    private TextView mTvEventsBeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many_people_event_detial);
        initView();
        initData();
    }

    private void initView() {
        mTvEventsTheme = (TextView) findViewById(R.id.tv_events_theme);
        mTvEventsLabel = (TextView) findViewById(R.id.tv_events_label);
        mTvEventLocation = (TextView) findViewById(R.id.tv_events_location);
        mTvEventsStTime = (TextView) findViewById(R.id.tv_events_start_time);
        mTvEventEndTime = (TextView) findViewById(R.id.tv_events_end_time);
        mTvEventsRepPer = (TextView) findViewById(R.id.tv_events_rep_person);
        mTvEventsJoinPer = (TextView) findViewById(R.id.tv_events_join_person);
        mTvEventsBeTime = (TextView) findViewById(R.id.tv_events_before_time);
    }


    private void initData() {
        //解析传过来的数据
        Gson gson = new Gson();
        Intent intent = getIntent();
        String s = intent.getStringExtra("mgonsn");

        Schedule schedule = gson.fromJson(s,
                new TypeToken<Schedule>() {
                }.getType());
        ArrayList<Schreport> repPer = findRepPer(schedule.getId());
        Log.e("传过来的数据2", repPer.get(0).getFriendid());

        mTvEventsTheme.setText(schedule.getTitle());
        mTvEventsLabel.setText(CommonUtil.getLabelName(schedule.getLabel()));
        mTvEventLocation.setText(schedule.getAddress());
        mTvEventsStTime.setText(schedule.getBegin_time());
        mTvEventEndTime.setText(schedule.getEnd_time());
//        mTvEventsRepPer.setText(schedule.get());//报送人以及参与人
//        mTvEventsJoinPer.setText(schedule.getAddress());
        mTvEventsBeTime.setText("提前" + schedule.getAhead_warn() + "分钟");
    }

    /**
     * 用日程id查找该日程的报送人
     *
     * @param sid
     */
    private ArrayList<Schreport> findRepPer(int sid) {
        DbManager mDbManager = DBHelper.getDbManager();
        try {
            ArrayList<Schreport> schreports = (ArrayList<Schreport>) mDbManager.selector(Schreport.class).where("sid", "=",
                    sid).findAll();
            Log.d("yzp", schreports.size() + "haha");
            return schreports;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

}
