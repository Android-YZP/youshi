package com.mkch.youshi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.UIUtils;

import java.util.ArrayList;

public class PersonalDetialEventActivity extends BaseDetailActivity {

    private TextView mTvEventBeTime;
    private TextView mTvEventRepPer;
    private TextView mTvEventEndTime;
    private TextView mTvEventStaTime;
    private TextView mTvEventLoc;
    private TextView mTvEventLabel;
    private TextView mTvEventTheme;
    private Schedule mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detial_event);
        initView();
        initTopBar();
        initData();
        initMap();
        initDelete();
        setListener();
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

    //初始化地图
    private void initMap() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        // Geo搜索
        mSearch.geocode(new GeoCodeOption().city(
                "").address(mTvEventLoc.getText().toString()));
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
        mTvEventTheme.setText(mSchedule.getTitle());
        mTvEventLabel.setText(CommonUtil.getLabelName(mSchedule.getLabel()));
        mTvEventLoc.setText(mSchedule.getAddress());
        mTvEventStaTime.setText(mSchedule.getBegin_time());
        mTvEventEndTime.setText(mSchedule.getEnd_time());
        //报送人
        if (repPer != null && repPer.size() != 0 && !repPer.isEmpty()) {
            for (int i = 0; i < repPer.size(); i++) {
                mTvEventRepPer.setText(mTvEventRepPer.getText().toString() +
                        DBHelper.findFriName(repPer.get(i).getFriendid()));
            }
        }
        mTvEventBeTime.setText("提前" + mSchedule.getAhead_warn() + "分钟");
    }

    private void setListener() {
        mTvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalDetialEventActivity.this,
                        AddPersonalEventActivity.class);
                intent.putExtra("eventID",mSchedule.getId());
                startActivity(intent);
            }
        });
    }
}
