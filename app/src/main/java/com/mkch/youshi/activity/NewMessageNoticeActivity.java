package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;

public class NewMessageNoticeActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LinearLayout mLayoutDisturb, mLayoutSound, mLayoutVibrate;
    private View mLine1,mLine2,mLine3;
    private CheckBox mCBSound, mCBVibrate;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message_notice);
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutDisturb = (LinearLayout) findViewById(R.id.layout_new_message_notice_disturb);
        mLayoutSound = (LinearLayout) findViewById(R.id.layout_new_message_notice_voice);
        mLayoutVibrate = (LinearLayout) findViewById(R.id.layout_new_message_notice_vibration);
        mCBSound = (CheckBox) findViewById(R.id.cb_new_message_notice_voice);
        mCBVibrate = (CheckBox) findViewById(R.id.cb_new_message_notice_vibration);
        mLine1 = (View) findViewById(R.id.view_new_message_notice_line1);
        mLine2 = (View) findViewById(R.id.view_new_message_notice_line2);
        mLine3 = (View) findViewById(R.id.view_new_message_notice_line3);
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(this);
        mTvTitle.setText("新消息通知");
        if (mUser.getDisturb() == null || !mUser.getDisturb()) {
            mLayoutSound.setVisibility(View.VISIBLE);
            mLayoutVibrate.setVisibility(View.VISIBLE);
            mLine1.setVisibility(View.VISIBLE);
            mLine2.setVisibility(View.VISIBLE);
            mLine3.setVisibility(View.VISIBLE);
            //判断提示音是否开启
            if (mUser.getSound() == null || mUser.getSound().equals("")) {
                mCBSound.setChecked(true);
            } else if (mUser.getSound()) {
                mCBSound.setChecked(true);
            } else {
                mCBSound.setChecked(false);
            }
            //判断振动是否开启
            if (mUser.getVibrate() == null || mUser.getVibrate().equals("")) {
                mCBVibrate.setChecked(true);
            } else if (mUser.getVibrate()) {
                mCBVibrate.setChecked(true);
            } else {
                mCBVibrate.setChecked(false);
            }
        } else {
            mLayoutSound.setVisibility(View.GONE);
            mLayoutVibrate.setVisibility(View.GONE);
            mLine1.setVisibility(View.GONE);
            mLine2.setVisibility(View.GONE);
            mLine3.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMessageNoticeActivity.this.finish();
            }
        });
        mLayoutDisturb.setOnClickListener(new NewMessageNoticeOnClickListener());
        mCBSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUser.setSound(isChecked);
                CommonUtil.saveUserInfo(mUser, NewMessageNoticeActivity.this);
                Toast.makeText(NewMessageNoticeActivity.this, "已修改提示音是否开启", Toast.LENGTH_LONG).show();
            }
        });
        mCBVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUser.setVibrate(isChecked);
                CommonUtil.saveUserInfo(mUser, NewMessageNoticeActivity.this);
                Toast.makeText(NewMessageNoticeActivity.this, "已修改振动是否开启", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class NewMessageNoticeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.layout_new_message_notice_disturb:
                    _intent = new Intent(NewMessageNoticeActivity.this, DisturbTimeActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
