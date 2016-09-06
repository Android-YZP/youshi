package com.mkch.youshi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;
import com.mkch.youshi.adapter.PhoneContactsListAdapter;

public class PhoneContactsActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_contacts);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mListView = (ListView) findViewById(R.id.list_phone_contacts);
    }

    private void initData() {
        mTvTitle.setText("手机通讯录");
        ListAdapter mAdapter = new PhoneContactsListAdapter(PhoneContactsActivity.this);
        mListView.setAdapter(mAdapter);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneContactsActivity.this.finish();
            }
        });
    }
}
