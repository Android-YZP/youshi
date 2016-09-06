package com.mkch.youshi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;

import java.util.ArrayList;

public class ChooseSomeoneActivity extends AppCompatActivity {

    private ListView mSomeoneListView;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_someone);
        initView();
        initData();
        setLister();
    }


    private void initView() {
        mSomeoneListView = (ListView) findViewById(R.id.lv_choose_someone);
    }

    private void initData() {
        names = new ArrayList<String>();
        names.add("李小龙");
        names.add("李大龙");
        names.add("李小龙");
        names.add("李大龙");
        mSomeoneListView.setAdapter(new ChooseAdapter());
    }

    private void setLister() {

    }

    class ChooseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ChooseSomeoneActivity.this,R.layout.item_list_choose_someone_layout,null);
            TextView _tvName = (TextView) view.findViewById(R.id.tv_lv_name);
            _tvName.setText(names.get(position));
            return view;
        }
    }
}
