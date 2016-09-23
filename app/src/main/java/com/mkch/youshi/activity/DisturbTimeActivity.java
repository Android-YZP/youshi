package com.mkch.youshi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;

public class DisturbTimeActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LinearLayout mLayoutOpen, mLayoutNight, mLayoutClose;
    private ImageView mIvOpen, mIvNight, mIvClose;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disturb_time);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutOpen = (LinearLayout) findViewById(R.id.layout_disturb_time_open);
        mLayoutNight = (LinearLayout) findViewById(R.id.layout_disturb_time_night);
        mLayoutClose = (LinearLayout) findViewById(R.id.layout_disturb_time_close);
        mIvOpen = (ImageView) findViewById(R.id.iv_disturb_time_open);
        mIvNight = (ImageView) findViewById(R.id.iv_disturb_time_night);
        mIvClose = (ImageView) findViewById(R.id.iv_disturb_time_close);
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(this);
        mTvTitle.setText("消息免打扰");
        if (mUser.getDisturb() == null) {
            mIvOpen.setVisibility(View.GONE);
            mIvNight.setVisibility(View.GONE);
            mIvClose.setVisibility(View.VISIBLE);
        } else if(mUser.getDisturb()){
            mIvOpen.setVisibility(View.VISIBLE);
            mIvNight.setVisibility(View.GONE);
            mIvClose.setVisibility(View.GONE);
        }else if(mUser.getNight()){
            mIvOpen.setVisibility(View.GONE);
            mIvNight.setVisibility(View.VISIBLE);
            mIvClose.setVisibility(View.GONE);
        }else {
            mIvOpen.setVisibility(View.GONE);
            mIvNight.setVisibility(View.GONE);
            mIvClose.setVisibility(View.VISIBLE);
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisturbTimeActivity.this.finish();
            }
        });
        mLayoutOpen.setOnClickListener(new DisturbTimeOnClickListener());
        mLayoutNight.setOnClickListener(new DisturbTimeOnClickListener());
        mLayoutClose.setOnClickListener(new DisturbTimeOnClickListener());
    }

    /**
     * 自定义点击监听类
     */
    private class DisturbTimeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_disturb_time_open:
                    mUser.setDisturb(true);
                    mUser.setNight(false);
                    mIvOpen.setVisibility(View.VISIBLE);
                    mIvNight.setVisibility(View.GONE);
                    mIvClose.setVisibility(View.GONE);
                    CommonUtil.saveUserInfo(mUser, DisturbTimeActivity.this);
                    Toast.makeText(DisturbTimeActivity.this, "免打扰已开启", Toast.LENGTH_LONG).show();
                    break;
                case R.id.layout_disturb_time_night:
                    mUser.setDisturb(false);
                    mUser.setNight(true);
                    mIvOpen.setVisibility(View.GONE);
                    mIvNight.setVisibility(View.VISIBLE);
                    mIvClose.setVisibility(View.GONE);
                    CommonUtil.saveUserInfo(mUser, DisturbTimeActivity.this);
                    Toast.makeText(DisturbTimeActivity.this, "只在夜间开启", Toast.LENGTH_LONG).show();
                    break;
                case R.id.layout_disturb_time_close:
                    mUser.setDisturb(false);
                    mUser.setNight(false);
                    mIvOpen.setVisibility(View.GONE);
                    mIvNight.setVisibility(View.GONE);
                    mIvClose.setVisibility(View.VISIBLE);
                    CommonUtil.saveUserInfo(mUser, DisturbTimeActivity.this);
                    Toast.makeText(DisturbTimeActivity.this, "免打扰已关闭", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }
}
