package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.MainActivity;
import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;

public class UserInformationActivity extends Activity {

	private ImageView mIvBack;
	private TextView mTvTitle,mTvSex;
	private LinearLayout mLayoutName,mLayoutYoushiNumber,mLayoutSex,mLayoutSignature;
	//保存选择的性别
	private String[] sex_list = {"男", "女"};
	private int checkedItem = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
		mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
		mLayoutName = (LinearLayout) findViewById(R.id.layout_user_information_name);
		mLayoutYoushiNumber = (LinearLayout) findViewById(R.id.layout_user_information_youshi_number);
		mLayoutSex = (LinearLayout) findViewById(R.id.layout_user_information_sex);
		mTvSex = (TextView) findViewById(R.id.tv_user_information_sex);
		mLayoutSignature = (LinearLayout) findViewById(R.id.layout_user_information_signature);
	}

	private void initData() {
		mTvTitle.setText("个人信息");
	}

	private void setListener() {
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				UserInformationActivity.this.finish();
			}
		});
		mLayoutName.setOnClickListener(new UserInformationOnClickListener());
		mLayoutYoushiNumber.setOnClickListener(new UserInformationOnClickListener());
		//弹出对话框，用户选择性别后更新界面
		mLayoutSex.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(UserInformationActivity.this).setTitle("您的性别为").setSingleChoiceItems(
						sex_list, checkedItem, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								checkedItem = which;
								mTvSex.setText(sex_list[which]);
								dialog.dismiss();
							}
						}).show();
			}
		});
		mLayoutSignature.setOnClickListener(new UserInformationOnClickListener());
	}

	/**
	 * 自定义点击监听类
	 * @author JLJ
	 *
	 */
	private class UserInformationOnClickListener implements View.OnClickListener {
	@Override
	public void onClick(View view) {
		Intent _intent = null;
		switch (view.getId()) {
			case R.id.layout_user_information_name:
				_intent = new Intent(UserInformationActivity.this,ReviseNameActivity.class);
				startActivity(_intent);
				break;
			case R.id.layout_user_information_youshi_number:
				_intent = new Intent(UserInformationActivity.this,ReviseYoushiNumberActivity.class);
				startActivity(_intent);
				break;
			case R.id.layout_user_information_signature:
				_intent = new Intent(UserInformationActivity.this,ReviseSignatureActivity.class);
				startActivity(_intent);
				break;
			default:
				break;
		}
	}
}
}
