package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

public class AccountAndSafetyActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LinearLayout mLayoutYoushiNumber,mLayoutPhone,mLayoutPassword,mLayoutProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_and_safety);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutYoushiNumber = (LinearLayout) findViewById(R.id.layout_account_youshi_number);
        mLayoutPhone = (LinearLayout) findViewById(R.id.layout_account_phone);
        mLayoutPassword = (LinearLayout) findViewById(R.id.layout_account_password);
        mLayoutProtect = (LinearLayout) findViewById(R.id.layout_account_protect);
    }

    private void initData() {
        mTvTitle.setText("账号与安全");
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
