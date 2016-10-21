package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.mkch.youshi.model.Schjoiner;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.UIUtils;

import java.util.ArrayList;

public class PersonalDetialsAffairActivity extends BaseDetailActivity {

    private TextView mTvAffTheme;
    private TextView mTvAffLab;
    private TextView mTvAffloca;
    private TextView mTvAffTime;
    private TextView mTvAffWhiceW;
    private TextView mTvAffTimePice;
    private TextView mTvAffRepP;
    private TextView mTvAffBefTime;
    private TextView mTvAffTotalTime;
    private Schedule mSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detials_affair);
        initView();
        initTopBar();
        initData();
        setListener();
        initMap();
        initDelete();
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
                "").address(mTvAffloca.getText().toString()));
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

    private void initData() {
        //解析传过来的数据
        Gson gson = new Gson();
        Intent intent = getIntent();
        String s = intent.getStringExtra("mgonsn");
        mSchedule = gson.fromJson(s,
                new TypeToken<Schedule>() {
                }.getType());

        ArrayList<Schreport> repPer = CommonUtil.findRepPer(mSchedule.getId());
        mTvAffTheme.setText(mSchedule.getTitle());
        mTvAffLab.setText(CommonUtil.getLabelName(mSchedule.getLabel()));
        mTvAffloca.setText(mSchedule.getAddress());
        mTvAffTime.setText(mSchedule.getBegin_time() + "至" + mSchedule.getEnd_time());
        mTvAffWhiceW.setText(CommonUtil.replaceNumberWeek(mSchedule.getWhich_week()).substring(0,
                CommonUtil.replaceNumberWeek(mSchedule.getWhich_week()).length() - 1));
//        mTvAffTimePice.setText(schedule.gett());//时间段
        mTvAffTotalTime.setText(mSchedule.getTotal_time());
        //报送人
        if (repPer != null && repPer.size() != 0 && !repPer.isEmpty()) {
            for (int i = 0; i < repPer.size(); i++) {
                mTvAffRepP.setText(mTvAffRepP.getText().toString() +
                        CommonUtil.findFriName(repPer.get(i).getFriendid()));
            }
        }
        mTvAffBefTime.setText("提前" + mSchedule.getAhead_warn() + "分钟");
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
                Intent intent = new Intent(PersonalDetialsAffairActivity.this,
                        AddPersonalAffairActivity.class);
                intent.putExtra("eventID",mSchedule.getId());
                startActivity(intent);
            }
        });
    }
}
