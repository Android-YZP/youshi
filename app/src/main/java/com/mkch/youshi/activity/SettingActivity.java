package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.util.CommonUtil;

public class SettingActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle, mTvQuit;
    private LinearLayout mLayoutAccount, mLayoutMessage, mLayoutPrivacy, mLayoutUniversality, mLayoutAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutAccount = (LinearLayout) findViewById(R.id.layout_setting_account_and_safety);
        mLayoutMessage = (LinearLayout) findViewById(R.id.layout_setting_new_message);
        mLayoutPrivacy = (LinearLayout) findViewById(R.id.layout_setting_privacy);
        mLayoutUniversality = (LinearLayout) findViewById(R.id.layout_setting_universality);
        mLayoutAbout = (LinearLayout) findViewById(R.id.layout_setting_about);
        mTvQuit = (TextView) findViewById(R.id.tv_setting_quit);
    }

    private void initData() {
        mTvTitle.setText("设置");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
        mLayoutAccount.setOnClickListener(new SettingOnClickListener());
        mLayoutMessage.setOnClickListener(new SettingOnClickListener());
        mLayoutPrivacy.setOnClickListener(new SettingOnClickListener());
        mLayoutUniversality.setOnClickListener(new SettingOnClickListener());
        mTvQuit.setOnClickListener(new SettingOnClickListener());
    }

    /**
     * 自定义点击监听类
     */
    private class SettingOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.layout_setting_account_and_safety:
                    _intent = new Intent(SettingActivity.this, AccountAndSafetyActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_setting_new_message:
                    _intent = new Intent(SettingActivity.this, NewMessageNoticeActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_setting_privacy:
                    _intent = new Intent(SettingActivity.this, PrivacyActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_setting_universality:
                    _intent = new Intent(SettingActivity.this, UniversalityActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_setting_about:
                    _intent = new Intent(SettingActivity.this, ReviseSignatureActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.tv_setting_quit:
                    CommonUtil.clearUserInfo(SettingActivity.this);
                    _intent = new Intent(SettingActivity.this, UserLoginActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
