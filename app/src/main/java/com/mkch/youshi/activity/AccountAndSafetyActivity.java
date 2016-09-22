package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;

public class AccountAndSafetyActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle, mTvYoushiNumber, mTvPhone, mTvProtected;
    private LinearLayout mLayoutYoushiNumber, mLayoutPhone, mLayoutPassword, mLayoutProtect;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_and_safety);
        initView();
        initData();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData2();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutYoushiNumber = (LinearLayout) findViewById(R.id.layout_account_youshi_number);
        mTvYoushiNumber = (TextView) findViewById(R.id.tv_account_youshi_number);
        mLayoutPhone = (LinearLayout) findViewById(R.id.layout_account_phone);
        mTvPhone = (TextView) findViewById(R.id.tv_account_phone);
        mLayoutPassword = (LinearLayout) findViewById(R.id.layout_account_password);
        mLayoutProtect = (LinearLayout) findViewById(R.id.layout_account_protect);
        mTvProtected = (TextView) findViewById(R.id.tv_account_protected);
    }

    private void initData() {
        mTvTitle.setText("账号与安全");
    }

    /**
     * 加载用户信息
     */
    private void initData2() {
        mUser = CommonUtil.getUserInfo(AccountAndSafetyActivity.this);
        if (mUser != null) {
            if (mUser.getYoushiNumber() == null || mUser.getYoushiNumber().equals("")) {
                mTvYoushiNumber.setText("未设置");
            } else {
                mTvYoushiNumber.setText(mUser.getYoushiNumber());
                mLayoutYoushiNumber.setEnabled(false);
            }
            if (mUser.getMobileNumber() == null || mUser.getMobileNumber().equals("")) {
                mTvPhone.setText("");
            } else {
                //隐藏手机号中间四位
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mUser.getMobileNumber().length(); i++) {
                    char c = mUser.getMobileNumber().charAt(i);
                    if (i >= 3 && i <= 6) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }
                mTvPhone.setText(sb.toString());
            }
            if (mUser.getProtected() == null || mUser.getProtected().equals("")) {
                mTvProtected.setText("未保护");
                mTvProtected.setTextColor(Color.RED);
            } else if(mUser.getProtected()){
                mTvProtected.setText("已保护");
                mTvProtected.setTextColor(Color.GREEN);
            }else {
                mTvProtected.setText("未保护");
                mTvProtected.setTextColor(Color.RED);
            }
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountAndSafetyActivity.this.finish();
            }
        });
        mLayoutYoushiNumber.setOnClickListener(new AccountOnClickListener());
        mLayoutPhone.setOnClickListener(new AccountOnClickListener());
        mLayoutPassword.setOnClickListener(new AccountOnClickListener());
        mLayoutProtect.setOnClickListener(new AccountOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class AccountOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.layout_account_youshi_number:
                    _intent = new Intent(AccountAndSafetyActivity.this, ReviseYoushiNumberActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_account_phone:
                    _intent = new Intent(AccountAndSafetyActivity.this, RevisePhoneActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_account_password:
                    _intent = new Intent(AccountAndSafetyActivity.this, RevisePasswordActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_account_protect:
                    _intent = new Intent(AccountAndSafetyActivity.this, AccountProtectActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
