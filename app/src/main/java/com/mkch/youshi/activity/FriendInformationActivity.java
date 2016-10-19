package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

public class FriendInformationActivity extends Activity {

    private ImageView mIvBack, mIvHead;
    private TextView mTvMore, mTvRemark, mTvYoushiNumber, mTvName, mTvSetting, mTvPlace, mTvSign;
    private TextView mTvPhone1, mTvPhone2, mTvPhone3, mTvPhone4, mTvPhone5, mTvDescription;
    private View mLine1, mLine2, mLine3, mLine4, mLine5, mLine6, mLine7, mLine8;
    private LinearLayout mLayoutPhone, mLayoutDescribe, mLayoutShow, mLayoutPlace, mLayoutSign;
    private Button mBtnSendMessage;
    private boolean isShowPlace, isShowSign;
    private String contactID;
    private ArrayList<String> remarkPhones = new ArrayList<>();
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
        mTvPhone1 = (TextView) findViewById(R.id.tv_friend_information_phone1);
        mTvPhone2 = (TextView) findViewById(R.id.tv_friend_information_phone2);
        mTvPhone3 = (TextView) findViewById(R.id.tv_friend_information_phone3);
        mTvPhone4 = (TextView) findViewById(R.id.tv_friend_information_phone4);
        mTvPhone5 = (TextView) findViewById(R.id.tv_friend_information_phone5);
        mTvDescription = (TextView) findViewById(R.id.tv_friend_information_describe);
        mLine1 = (View) findViewById(R.id.tv_friend_information_line1);
        mLine2 = (View) findViewById(R.id.tv_friend_information_line2);
        mLine3 = (View) findViewById(R.id.tv_friend_information_line3);
        mLine4 = (View) findViewById(R.id.tv_friend_information_line4);
        mLine5 = (View) findViewById(R.id.tv_friend_information_line5);
        mLine6 = (View) findViewById(R.id.tv_friend_information_line6);
        mLine7 = (View) findViewById(R.id.tv_friend_information_line7);
        mLine8 = (View) findViewById(R.id.tv_friend_information_line8);
        mLayoutShow = (LinearLayout) findViewById(R.id.layout_friend_information_place_and_sign);
        mLayoutPlace = (LinearLayout) findViewById(R.id.layout_friend_information_place);
        mLayoutSign = (LinearLayout) findViewById(R.id.layout_friend_information_signature);
        mTvPlace = (TextView) findViewById(R.id.tv_friend_information_address);
        mTvSign = (TextView) findViewById(R.id.tv_friend_information_signature);
        mBtnSendMessage = (Button) findViewById(R.id.btn_friend_information_send_message);
    }

    private void initData() {
        mLayoutPhone.setVisibility(View.GONE);
        mTvPhone1.setVisibility(View.GONE);
        mTvPhone2.setVisibility(View.GONE);
        mTvPhone3.setVisibility(View.GONE);
        mTvPhone4.setVisibility(View.GONE);
        mTvPhone5.setVisibility(View.GONE);
        mLine1.setVisibility(View.GONE);
        mLine2.setVisibility(View.GONE);
        mLine3.setVisibility(View.GONE);
        mLine4.setVisibility(View.GONE);
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
                //加载好友头像
                if (friend.getHead_pic() != null && !friend.getHead_pic().equals("") && !friend.getHead_pic().equals("null")) {
                    x.image().bind(mIvHead, friend.getHead_pic(), _image_options);
                } else {
                    mIvHead.setImageResource(R.drawable.default_headpic);
                }
                //加载好友备注
                if (friend.getRemark() != null && !friend.getRemark().equals("") && !friend.getRemark().equals("null")) {
                    mTvRemark.setText(friend.getRemark());
                } else {
                    mTvRemark.setVisibility(View.GONE);
                }
                //加载好友优时号
                if (friend.getYoushi_number() != null && !friend.getYoushi_number().equals("") && !friend.getYoushi_number().equals("null")) {
                    mTvYoushiNumber.setText("优时号: " + friend.getYoushi_number());
                } else {
                    mTvYoushiNumber.setVisibility(View.GONE);
                }
                //加载好友昵称
                if (friend.getNickname() != null && !friend.getNickname().equals("") && !friend.getNickname().equals("null")) {
                    mTvName.setText("昵称: " + friend.getNickname());
                } else {
                    mTvName.setText("昵称: " + friend.getPhone());
                }
                //加载好友备注电话
                if (friend.getPhone_number() != null && !friend.getPhone_number().equals("") && !friend.getPhone_number().equals("null")) {
                    String[] phones = friend.getPhone_number().split("\\|");
                    for (int i = 0; i < phones.length; i++) {
                        if (phones[i] != null && !phones[i].equals("") && !phones[i].equals("null")) {
                            remarkPhones.add(phones[i]);
                        }
                    }
                    showPhone();
                } else {
                    mLayoutPhone.setVisibility(View.GONE);
                }
                //加载好友描述
                if (friend.getDescription() != null && !friend.getDescription().equals("") && !friend.getDescription().equals("null")) {
                    mTvDescription.setText(friend.getDescription());
                } else {
                    mLayoutDescribe.setVisibility(View.GONE);
                }
                //加载好友地区
                if (friend.getPlace() != null && !friend.getPlace().equals("") && !friend.getPlace().equals("null")) {
                    mTvPlace.setText(friend.getPlace());
                    isShowPlace = true;
                } else {
                    mLayoutPlace.setVisibility(View.GONE);
                    isShowPlace = false;
                }
                //加载好友签名
                if (friend.getSign() != null && !friend.getSign().equals("") && !friend.getSign().equals("null")) {
                    mTvSign.setText(friend.getSign());
                    isShowSign = true;
                } else {
                    mLayoutSign.setVisibility(View.GONE);
                    isShowSign = false;
                }
                //判断是否显示地区和签名
                if (!isShowPlace && !isShowSign) {
                    mLayoutShow.setVisibility(View.GONE);
                    mLine7.setVisibility(View.GONE);
                    mLine8.setVisibility(View.GONE);
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
        mBtnSendMessage.setOnClickListener(new FriendInformationOnClickListener());
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
                    _intent.putExtra("_contactID", contactID);
                    startActivity(_intent);
                    FriendInformationActivity.this.finish();
                    break;
                case R.id.tv_friend_information_setting:
                    _intent = new Intent(FriendInformationActivity.this, RemarkInformationActivity.class);
                    _intent.putExtra("_contactID", contactID);
                    startActivity(_intent);
                    break;
                case R.id.btn_friend_information_send_message:
                    _intent = new Intent(FriendInformationActivity.this, ChatActivity.class);
                    _intent.putExtra("_openfirename", contactID);
                    startActivity(_intent);
                    FriendInformationActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    }

    //根据获取的备注电话号码，来判断如何显示界面
    private void showPhone() {
        switch (remarkPhones.size()) {
            case 0:
                mLayoutPhone.setVisibility(View.GONE);
                break;
            case 1:
                mLayoutPhone.setVisibility(View.VISIBLE);
                mTvPhone1.setVisibility(View.VISIBLE);
                mLine5.setVisibility(View.VISIBLE);
                mTvPhone1.setText(remarkPhones.get(0));
                break;
            case 2:
                mLayoutPhone.setVisibility(View.VISIBLE);
                mTvPhone1.setVisibility(View.VISIBLE);
                mLine1.setVisibility(View.VISIBLE);
                mTvPhone2.setVisibility(View.VISIBLE);
                mLine5.setVisibility(View.VISIBLE);
                mTvPhone1.setText(remarkPhones.get(0));
                mTvPhone2.setText(remarkPhones.get(1));
                break;
            case 3:
                mLayoutPhone.setVisibility(View.VISIBLE);
                mTvPhone1.setVisibility(View.VISIBLE);
                mLine1.setVisibility(View.VISIBLE);
                mLine2.setVisibility(View.VISIBLE);
                mTvPhone2.setVisibility(View.VISIBLE);
                mTvPhone3.setVisibility(View.VISIBLE);
                mLine5.setVisibility(View.VISIBLE);
                mTvPhone1.setText(remarkPhones.get(0));
                mTvPhone2.setText(remarkPhones.get(1));
                mTvPhone3.setText(remarkPhones.get(2));
                break;
            case 4:
                mLayoutPhone.setVisibility(View.VISIBLE);
                mTvPhone1.setVisibility(View.VISIBLE);
                mLine1.setVisibility(View.VISIBLE);
                mLine2.setVisibility(View.VISIBLE);
                mLine3.setVisibility(View.VISIBLE);
                mTvPhone2.setVisibility(View.VISIBLE);
                mTvPhone3.setVisibility(View.VISIBLE);
                mTvPhone4.setVisibility(View.VISIBLE);
                mLine5.setVisibility(View.VISIBLE);
                mTvPhone1.setText(remarkPhones.get(0));
                mTvPhone2.setText(remarkPhones.get(1));
                mTvPhone3.setText(remarkPhones.get(2));
                mTvPhone4.setText(remarkPhones.get(3));
                break;
            case 5:
                mLayoutPhone.setVisibility(View.VISIBLE);
                mTvPhone1.setVisibility(View.VISIBLE);
                mLine1.setVisibility(View.VISIBLE);
                mLine2.setVisibility(View.VISIBLE);
                mLine3.setVisibility(View.VISIBLE);
                mLine4.setVisibility(View.VISIBLE);
                mTvPhone2.setVisibility(View.VISIBLE);
                mTvPhone3.setVisibility(View.VISIBLE);
                mTvPhone4.setVisibility(View.VISIBLE);
                mTvPhone5.setVisibility(View.VISIBLE);
                mLine5.setVisibility(View.VISIBLE);
                mTvPhone1.setText(remarkPhones.get(0));
                mTvPhone2.setText(remarkPhones.get(1));
                mTvPhone3.setText(remarkPhones.get(2));
                mTvPhone4.setText(remarkPhones.get(3));
                mTvPhone5.setText(remarkPhones.get(4));
                break;
            default:
                break;
        }
    }
}
