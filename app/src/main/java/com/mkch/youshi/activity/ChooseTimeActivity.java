package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DialogFactory;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.view.AddTimeListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.mkch.youshi.bean.NetScheduleModel.*;

public class ChooseTimeActivity extends AppCompatActivity {

    private AddTimeListView mLvChooseTime;
    private TextView mTvComplete;
    private ArrayList<ViewModelBean.TimeSpanListBean> mTimeSpanListBeans;
    private chooseTimeAdapter mChooseTimeAdapter;
    private int hours;
    private int minutes;
    private int mEventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mLvChooseTime = (AddTimeListView) findViewById(R.id.lv_choose_time);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
    }

    private void initData() {
        mTvComplete.setText("完成");
        mTimeSpanListBeans = new ArrayList<>();
        mChooseTimeAdapter = new chooseTimeAdapter();

//        Intent intent = getIntent();
//        mEventID = intent.getIntExtra("eventID", -1);
//        if (mEventID != -1) {
//            ArrayList<Schtime> schTimes = CommonUtil.findSchTime(mEventID);
//            for (int i = 0; i < schTimes.size(); i++) {
//                ViewModelBean.TimeSpanListBean timeSpanListBean = new ViewModelBean.TimeSpanListBean();
//                timeSpanListBean.setStartTime(schTimes.get(i).getBegin_time());
//                timeSpanListBean.setEndTime(schTimes.get(i).getEnd_time());
//                timeSpanListBean.setRemindTime(schTimes.get(i).getWarning_time());
//                timeSpanListBean.setTdate(schTimes.get(i).getDate());
//                mTimeSpanListBeans.add(timeSpanListBean);
//            }
//        }

        mLvChooseTime.setAdapter(mChooseTimeAdapter);

    }

    private void setListener() {
        mTvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成,传送数据到启动界面
                sendResult();
            }
        });

        mLvChooseTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mLvChooseTime.getLastVisiblePosition()) {
                    DialogFactory.showTimeBucketOptionDialog(ChooseTimeActivity.this, mTimeSpanListBeans, mChooseTimeAdapter);
                }
            }
        });
    }


    //返回数据到启动界面
    public void sendResult() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        String _listString = gson.toJson(mTimeSpanListBeans);
        String _totalTime = TimesUtils.totalTime(mTimeSpanListBeans);
        int[] timeAndhour = TimesUtils.totalTimeAndhour(mTimeSpanListBeans);
        intent.putExtra("TimeSpanListBeanList", _listString);
        intent.putExtra("TotalTime", _totalTime);
        intent.putExtra("TotalTimeHour", timeAndhour[0]);
        intent.putExtra("TotalTimeMints", timeAndhour[1]);
        ChooseTimeActivity.this.setResult(2, intent);
        ChooseTimeActivity.this.finish();
    }


    class chooseTimeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTimeSpanListBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mTimeSpanListBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            view = View.inflate(ChooseTimeActivity.this, R.layout.item_list_choose_time_layout, null);
            TextView _tvStartTime = (TextView) view.findViewById(R.id.tv_choose_time_start_time);
            TextView _tvEndTime = (TextView) view.findViewById(R.id.tv_choose_time_end_time);
            _tvStartTime.setText(mTimeSpanListBeans.get(position).getStartTime());
            _tvEndTime.setText(mTimeSpanListBeans.get(position).getEndTime());
            return view;
        }
    }
}
