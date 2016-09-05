package com.mkch.youshi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;

public class MyFileActivity extends Activity {

	private ImageView mIvBack;
	private TextView mTvTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_file);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
		mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
	}

	private void initData() {
		mTvTitle.setText("我的文件");
	}

	private void setListener() {
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyFileActivity.this.finish();
			}
		});
	}
}
