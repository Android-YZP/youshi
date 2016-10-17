package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.x;

public class FriendInformationActivity extends Activity {

    private ImageView mIvBack, mIvHead;
    private TextView mTvMore, mTvRemark, mTvYoushiNumber,mTvName, mTvSetting, mTvPlace;
    private View mLine1, mLine2, mLine3, mLine4, mLine5, mLine6;
    private LinearLayout mLayoutPhone, mLayoutDescribe, mLayoutPlace;
    private String contactID;
    private User mUser;
    private DbManager dbManager;//数据库管理对象

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
        mIvHead = (ImageView) findViewById(R.id.iv_friend_information_head);
        mTvRemark = (TextView) findViewById(R.id.tv_friend_information_remark);
        mTvYoushiNumber = (TextView) findViewById(R.id.tv_friend_information_youshi_number);
        mTvName = (TextView) findViewById(R.id.tv_friend_information_name);
        mTvSetting = (TextView) findViewById(R.id.tv_friend_information_setting);
        mLayoutPhone = (LinearLayout) findViewById(R.id.layout_friend_information_phone);
        mLayoutDescribe = (LinearLayout) findViewById(R.id.layout_friend_information_describe);
        mLine1 = (View) findViewById(R.id.tv_friend_information_line1);
        mLine2 = (View) findViewById(R.id.tv_friend_information_line2);
        mLine3 = (View) findViewById(R.id.tv_friend_information_line3);
        mLine4 = (View) findViewById(R.id.tv_friend_information_line4);
        mLine5 = (View) findViewById(R.id.tv_friend_information_line5);
        mLine6 = (View) findViewById(R.id.tv_friend_information_line6);
        mLayoutPlace = (LinearLayout) findViewById(R.id.layout_friend_information_place);
        mTvPlace = (TextView) findViewById(R.id.tv_friend_information_address);
    }

    private void initData() {
//		mTvSetting.setVisibility(View.GONE);
        mLayoutPhone.setVisibility(View.GONE);
        mLayoutDescribe.setVisibility(View.GONE);
        mLine5.setVisibility(View.GONE);
        mLine6.setVisibility(View.GONE);
        mUser = CommonUtil.getUserInfo(FriendInformationActivity.this);
        dbManager = DBHelper.getDbManager();
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            contactID = _bundle.getString("_contactID");
            //获取所点击的好友信息
            try {
                Friend friend = dbManager.selector(Friend.class)
                        .where("friendid", "=", contactID)
                        .and("userid", "=", mUser.getOpenFireUserName())
                        .findFirst();
                ImageOptions _image_options = new ImageOptions.Builder()
                        .setCircular(true)
                        .build();
                if (friend.getHead_pic() != null && !friend.getHead_pic().equals("") && !friend.getHead_pic().equals("null")) {
                    x.image().bind(mIvHead, friend.getHead_pic(), _image_options);
                } else {
                    mIvHead.setImageResource(R.drawable.default_headpic);
                }
                if (friend.getRemark() != null && !friend.getRemark().equals("") && !friend.getRemark().equals("null")) {
                    mTvRemark.setText(friend.getRemark());
                } else {
                    mTvRemark.setVisibility(View.GONE);
                }
                if (friend.getYoushi_number() != null && !friend.getYoushi_number().equals("") && !friend.getYoushi_number().equals("null")) {
                    mTvYoushiNumber.setText("优时号: " + friend.getYoushi_number());
                } else {
                    mTvYoushiNumber.setVisibility(View.GONE);
                }
                if (friend.getNickname() != null && !friend.getNickname().equals("") && !friend.getNickname().equals("null")) {
                    mTvName.setText("昵称: " + friend.getNickname());
                } else {
                    mTvName.setText("昵称: "+friend.getPhone());
                }
                if (friend.getPlace() != null && !friend.getPlace().equals("") && !friend.getPlace().equals("null")) {
                    mTvPlace.setText(friend.getPlace());
                } else {
                    mLayoutPlace.setVisibility(View.GONE);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
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
                    _intent.putExtra("_contactID", contactID);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
