package com.mkch.youshi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.TimeBucketInfo;
import com.mkch.youshi.util.DialogFactory;

import java.util.ArrayList;

public class ChooseTimeActivity extends AppCompatActivity {

    private ListView mLvChooseTime;
    private TextView mTvadd;
    private ArrayList<TimeBucketInfo> mTimeBucketInfos;
    private chooseTimeAdapter mChooseTimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mLvChooseTime = (ListView) findViewById(R.id.lv_choose_time);
        mTvadd = (TextView) findViewById(R.id.tv_add_event_complete);
    }

    private void initData() {
        mTvadd.setText("添加");
        mTimeBucketInfos = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            TimeBucketInfo bucketInfo = new TimeBucketInfo();
            bucketInfo.setStartTime("11"+":"+"0"+i);
            bucketInfo.setEndTime("11"+":"+(10+i));
            mTimeBucketInfos.add(bucketInfo);
        }
        mChooseTimeAdapter = new chooseTimeAdapter();
        mLvChooseTime.setAdapter(mChooseTimeAdapter);
    }

    private void setListener() {
        mTvadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFactory.showTimeBucketOptionDialog(ChooseTimeActivity.this,mTimeBucketInfos,mChooseTimeAdapter);
            }
        });
    }
    class chooseTimeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mTimeBucketInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mTimeBucketInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ChooseTimeActivity.this,R.layout.item_list_choose_time_layout,null);
            TextView _tvStartTime = (TextView) view.findViewById(R.id.tv_choose_time_start_time);
            TextView _tvEndTime = (TextView) view.findViewById(R.id.tv_choose_time_end_time);
            _tvStartTime.setText(mTimeBucketInfos.get(position).getStartTime());
            _tvEndTime.setText(mTimeBucketInfos.get(position).getEndTime());
            return view;
        }
    }


}
