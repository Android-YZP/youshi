package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;

public class RemarkInformationActivity extends Activity {

	private ImageView mIvBack;
	private EditText mEtPhone1, mEtPhone2,mEtPhone3,mEtPhone4,mEtPhone5;
	private TextView mTvFinish,mTvLine1,mTvLine2,mTvLine3,mTvLine4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remark_information);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_remark_information_back);
		mTvFinish = (TextView) findViewById(R.id.tv_remark_information_finish);
		mEtPhone1 = (EditText) findViewById(R.id.et_remark_information_phone1);
		mEtPhone2 = (EditText) findViewById(R.id.et_remark_information_phone2);
		mEtPhone3 = (EditText) findViewById(R.id.et_remark_information_phone3);
		mEtPhone4 = (EditText) findViewById(R.id.et_remark_information_phone4);
		mEtPhone5 = (EditText) findViewById(R.id.et_remark_information_phone5);
		mTvLine1 = (TextView) findViewById(R.id.tv_remark_information_line1);
		mTvLine2 = (TextView) findViewById(R.id.tv_remark_information_line2);
		mTvLine3 = (TextView) findViewById(R.id.tv_remark_information_line3);
		mTvLine4 = (TextView) findViewById(R.id.tv_remark_information_line4);
	}

	private void initData() {
        mEtPhone2.setVisibility(View.GONE);
		mTvLine1.setVisibility(View.GONE);
		mEtPhone3.setVisibility(View.GONE);
		mTvLine2.setVisibility(View.GONE);
		mEtPhone4.setVisibility(View.GONE);
		mTvLine3.setVisibility(View.GONE);
		mEtPhone5.setVisibility(View.GONE);
		mTvLine4.setVisibility(View.GONE);
	}

	private void setListener() {
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RemarkInformationActivity.this.finish();
			}
		});
		mTvFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RemarkInformationActivity.this.finish();
			}
		});
	}
}
