package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.mkch.youshi.util.UIUtils;

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
    private Schedule mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many_people_event_detial);
        initView();
        initData();
        initMap();
        initDelete();
        initTopBar();
        initListener();
    }
    private void initDelete() {
        mBtDeleteSch = (Button) findViewById(R.id.Bt_delete_sch);
        mBtDeleteSch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isnetWorkAvilable(UIUtils.getContext())) {
                    showAlertDialog();
                } else {
                    UIUtils.showTip("请检查网络");
                }
            }
        });
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

        mSchedule = gson.fromJson(s,
                new TypeToken<Schedule>() {
                }.getType());

        ArrayList<Schreport> repPer = DBHelper.findRepPer(mSchedule.getId());
        ArrayList<Schjoiner> joinPer = DBHelper.findJoinPer(mSchedule.getId());
        Log.e("传过来的数据2", joinPer.size() + "");

        mTvEventsTheme.setText(mSchedule.getTitle());
        mTvEventsLabel.setText(CommonUtil.getLabelName(mSchedule.getLabel()));
        mTvEventLocation.setText(mSchedule.getAddress());
        mTvEventsStTime.setText(mSchedule.getBegin_time());
        mTvEventEndTime.setText(mSchedule.getEnd_time());
        //报送人
        if (repPer != null && repPer.size() != 0 && !repPer.isEmpty()) {
            for (int i = 0; i < repPer.size(); i++) {
                mTvEventsRepPer.setText(mTvEventsRepPer.getText().toString() +
                        DBHelper.findFriName(repPer.get(i).getFriendid()));
            }
        }

        //参与人
        if (joinPer != null && joinPer.size() != 0 && !joinPer.isEmpty()) {
            for (int i = 0; i < joinPer.size(); i++) {
                mTvEventsJoinPer.setText(mTvEventsJoinPer.getText().toString() +
                        DBHelper.findFriName(joinPer.get(i).getJoiner_id()));
            }
        }

        //提前提醒
        mTvEventsBeTime.setText("提前" + mSchedule.getAhead_warn() + "分钟");
    }




    private void initListener() {
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManyPeopleEventDetial.this,
                        AddManyPeopleEventActivity.class);
                intent.putExtra("eventID",mSchedule.getId());
                startActivity(intent);
            }
        });
    }

}
