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
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.UIUtils;

import java.util.ArrayList;

public class PersonalDetialHabitActivity extends BaseDetailActivity {

    private TextView mTvTheme;
    private TextView mTvlabel;
    private TextView mTvLoc;
    private TextView mTvWTimes;
    private TextView mTvWWeeks;
    private TextView mTvTimes;
    private TextView mTvTotHour;
    private TextView mTvRepPer;
    private TextView mTvBeTime;
    private Schedule mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detial_habit);
        initView();
        initTopBar();
        initData();
        initMap();
        initDelete();
        setListener();
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
                "").address(mTvLoc.getText().toString()));
    }

    private void initView() {
        mTvTheme = (TextView) findViewById(R.id.tv_habit_theme);
        mTvlabel = (TextView) findViewById(R.id.tv_habit_label);
        mTvLoc = (TextView) findViewById(R.id.tv_habit_location);
        mTvWTimes = (TextView) findViewById(R.id.tv_habit_week_times);
        mTvWWeeks = (TextView) findViewById(R.id.tv_habit_which_weeks);
        mTvTimes = (TextView) findViewById(R.id.tv_habit_times);
        mTvRepPer = (TextView) findViewById(R.id.tv_habit_rep_person);
        mTvBeTime = (TextView) findViewById(R.id.tv_habit_before_time);
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
    // 时间段的初始化

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
                Intent intent = new Intent(PersonalDetialHabitActivity.this,
                        AddPersonalHabitActivity.class);
                intent.putExtra("eventID", mSchedule.getId());
                startActivity(intent);
            }
        });
    }


    private void initData() {
        //解析传过来的数据
        Gson gson = new Gson();
        Intent intent = getIntent();
        String s = intent.getStringExtra("mgonsn");

        mSchedule = gson.fromJson(s,
                new TypeToken<Schedule>() {
                }.getType());
        ArrayList<Schreport> repPer = CommonUtil.findRepPer(mSchedule.getId());

        mTvTheme.setText(mSchedule.getTitle());
        mTvlabel.setText(CommonUtil.getLabelName(mSchedule.getLabel()));
        mTvLoc.setText(mSchedule.getAddress());
        mTvWTimes.setText("一周" + mSchedule.getTimes_of_week() + "次");
        mTvWWeeks.setText(CommonUtil.replaceNumberWeek(mSchedule.getWhich_week()).substring(0,CommonUtil.
                replaceNumberWeek(mSchedule.getWhich_week()).length()-1));
        mTvBeTime.setText("提前" + mSchedule.getAhead_warn() + "分钟");
        //报送人
        if (repPer != null && repPer.size() != 0 && !repPer.isEmpty()) {
            for (int i = 0; i < repPer.size(); i++) {
                mTvRepPer.setText(mTvRepPer.getText().toString() +
                        CommonUtil.findFriName(repPer.get(i).getFriendid()));
            }
        }


        // 时间段的初始化
        ArrayList<Schtime> schTimes = CommonUtil.findSchTime(mSchedule.getId());
        if (schTimes != null && schTimes.size() != 0 && !schTimes.isEmpty()) {
            for (int i = 0; i < schTimes.size(); i++) {
                if (i % 2 == 0) {
                    mTvTimes.setText(mTvTimes.getText().toString() + " " +
                            schTimes.get(i).getBegin_time() + "~" + schTimes.get(i).getEnd_time() + " ");
                } else {
                    mTvTimes.setText(mTvTimes.getText().toString() + " " +
                            schTimes.get(i).getBegin_time() + "~" + schTimes.get(i).getEnd_time() + "\n ");
                }
            }
        }


    }


}
