package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

import org.w3c.dom.Text;

public class FriendInformationActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvMore, mTvRemark, mTvSetting;
    private View mLine1, mLine2, mLine3, mLine4, mLine5, mLine6;
    private LinearLayout mLayoutPhone, mLayoutDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_information);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_friend_information_back);
        mTvMore = (TextView) findViewById(R.id.tv_friend_information_more);
        mTvSetting = (TextView) findViewById(R.id.tv_friend_information_setting);
        mTvRemark = (TextView) findViewById(R.id.tv_friend_information_setting);
        mLayoutPhone = (LinearLayout) findViewById(R.id.layout_friend_information_phone);
        mLayoutDescribe = (LinearLayout) findViewById(R.id.layout_friend_information_describe);
        mLine1 = (View) findViewById(R.id.tv_friend_information_line1);
        mLine2 = (View) findViewById(R.id.tv_friend_information_line2);
        mLine3 = (View) findViewById(R.id.tv_friend_information_line3);
        mLine4 = (View) findViewById(R.id.tv_friend_information_line4);
        mLine5 = (View) findViewById(R.id.tv_friend_information_line5);
        mLine6 = (View) findViewById(R.id.tv_friend_information_line6);
    }

    private void initData() {
//		mTvSetting.setVisibility(View.GONE);
        mLayoutPhone.setVisibility(View.GONE);
        mLayoutDescribe.setVisibility(View.GONE);
        mLine5.setVisibility(View.GONE);
        mLine6.setVisibility(View.GONE);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendInformationActivity.this.finish();
            }
        });
        mTvMore.setOnClickListener(new FriendInformationOnClickListener());
        mTvSetting.setOnClickListener(new FriendInformationOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class FriendInformationOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.tv_friend_information_more:
                    _intent = new Intent(FriendInformationActivity.this, InformationSettingActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.tv_friend_information_setting:
                    _intent = new Intent(FriendInformationActivity.this, RemarkInformationActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
