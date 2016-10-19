package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.ManyPeopleEvenBean;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schjoiner;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;

public class ManyPeopleEventDetial extends BaseDetailActivity {

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
        initMap();
    }

    private void initMap() {
                // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        // Geo搜索
        mSearch.geocode(new GeoCodeOption().city(
               "").address(mTvEventLocation.getText().toString()));
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

        ArrayList<Schreport> repPer = CommonUtil.findRepPer(schedule.getId());
        ArrayList<Schjoiner> joinPer = findJoinPer(schedule.getId());
        Log.e("传过来的数据2", joinPer.size() + "");

        mTvEventsTheme.setText(schedule.getTitle());
        mTvEventsLabel.setText(CommonUtil.getLabelName(schedule.getLabel()));
        mTvEventLocation.setText(schedule.getAddress());
        mTvEventsStTime.setText(schedule.getBegin_time());
        mTvEventEndTime.setText(schedule.getEnd_time());
        //报送人
        if (repPer != null && repPer.size() != 0 && !repPer.isEmpty()) {
            for (int i = 0; i < repPer.size(); i++) {
                mTvEventsRepPer.setText(mTvEventsRepPer.getText().toString() +
                        CommonUtil.findFriName(repPer.get(i).getFriendid()));
            }
        }
        //参与人
        if (joinPer != null && joinPer.size() != 0 && !joinPer.isEmpty()) {
            for (int i = 0; i < joinPer.size(); i++) {
                mTvEventsJoinPer.setText(mTvEventsJoinPer.getText().toString() +
                        CommonUtil.findFriName(joinPer.get(i).getJoiner_id()));
            }
        }
        //提前提醒
        mTvEventsBeTime.setText("提前" + schedule.getAhead_warn() + "分钟");
    }


    /**
     * 用日程id查找该日程的报送人
     * @param sid
     */
    private ArrayList<Schjoiner> findJoinPer(int sid) {
        DbManager mDbManager = DBHelper.getDbManager();
        try {
            ArrayList<Schjoiner> schjoiners = (ArrayList<Schjoiner>) mDbManager.selector(Schjoiner.class).where("sid", "=",
                    sid).findAll();
            Log.d("yzp", schjoiners.size() + "haha");
            return schjoiners;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

}
