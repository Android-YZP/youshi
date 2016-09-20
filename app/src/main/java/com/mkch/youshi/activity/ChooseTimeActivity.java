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
import com.mkch.youshi.util.DialogFactory;
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
        String _totalTime = totalTime(mTimeSpanListBeans);
        intent.putExtra("TimeSpanListBeanList", _listString);
        intent.putExtra("TotalTime", _totalTime);
        intent.putExtra("TotalTimeHour", hours);
        intent.putExtra("TotalTimeMints", minutes);
        ChooseTimeActivity.this.setResult(2, intent);
        ChooseTimeActivity.this.finish();
    }

    /**
     * 计算出所有时间段的总时间
     * @return
     */
    private String totalTime(ArrayList<ViewModelBean.TimeSpanListBean> mTimeSpanListBeans) {
        SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < mTimeSpanListBeans.size(); i++) {
            try {
                String _startTime = mTimeSpanListBeans.get(i).getStartTime();
                String _endTime = mTimeSpanListBeans.get(i).getEndTime();
                Date dStart = _sdf.parse(_startTime);
                Date dEnd = _sdf.parse(_endTime);
                long diff = dEnd.getTime() - dStart.getTime();//这样得到的差值是微秒级别
                long days = diff / (1000 * 60 * 60 * 24);
                long hour = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minute = (diff - days * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
                hours = (int) (hours + hour);
                minutes = (int) (minutes + minute);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return hours + "小时" + minutes + "分钟";
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
