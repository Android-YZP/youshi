package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;
import com.mkch.youshi.adapter.GroupChatListAdapter;

public class GroupChatActivity extends Activity {

	private ImageView mIvBack;
	private TextView mTvTitle;
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_chat);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
		mTvTitle = (TextView)findViewById(R.id.tv_common_topbar_title);
		mListView = (ListView) findViewById(R.id.list_group_chat);
	}

	private void initData() {
		mTvTitle.setText("群聊");
		ListAdapter mAdapter = new GroupChatListAdapter(GroupChatActivity.this);
		mListView.setAdapter(mAdapter);
	}

	private void setListener() {
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GroupChatActivity.this.finish();
			}
		});
	}
}
