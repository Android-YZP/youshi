package com.mkch.youshi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.MyCollectionActivity;
import com.mkch.youshi.activity.MyFileActivity;
import com.mkch.youshi.activity.SettingActivity;
import com.mkch.youshi.activity.UserInformationActivity;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.view.CustomSmartImageView;

public class UserCenterFragment extends Fragment {

    private LinearLayout mInformation, mFile, mCollection, mSetting;
    private User mUser;
    private CustomSmartImageView mIvHead;
    private TextView mTvName, mTvYoushiNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUser = CommonUtil.getUserInfo(getActivity());//初始化用户信息
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 查找所有view
     *
     * @param view
     */
    private void findView(View view) {
        mIvHead = (CustomSmartImageView) view.findViewById(R.id.iv_user_center_head);
        mTvName = (TextView) view.findViewById(R.id.tv_user_center_name);
        mTvYoushiNumber = (TextView) view.findViewById(R.id.tv_user_center_youshi_number);
        mInformation = (LinearLayout) view.findViewById(R.id.layout_user_center_information);
        mFile = (LinearLayout) view.findViewById(R.id.layout_user_center_my_file);
        mCollection = (LinearLayout) view.findViewById(R.id.layout_user_center_my_collection);
        mSetting = (LinearLayout) view.findViewById(R.id.layout_user_center_setting);
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(getActivity());
        Log.d("jlj", "---------------------zzzzzzzzzzzzzzzzzzzz = " + mUser.getHeadPic());
        //设置头像,昵称和优时号,本地没有就用默认
        if (mUser != null) {
            mIvHead.setImageUrl(mUser.getHeadPic(), R.drawable.maillist);
            if (mUser.getNickName() == null ||mUser.getNickName().equals("")) {
                mTvName.setText(mUser.getMobileNumber());
            } else {
                mTvName.setText(mUser.getNickName());
            }
            if (mUser.getYoushiNumber() == null || mUser.getYoushiNumber().equals("")) {
                mTvYoushiNumber.setText("优时号: 无");
            } else {
                mTvYoushiNumber.setText("优时号: " + mUser.getYoushiNumber());
            }
        }
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        mInformation.setOnClickListener(new MyUserCenterOnClickListener());
        mFile.setOnClickListener(new MyUserCenterOnClickListener());
        mCollection.setOnClickListener(new MyUserCenterOnClickListener());
        mSetting.setOnClickListener(new MyUserCenterOnClickListener());
    }

    /**
     * 自定义点击监听类
     */
    private class MyUserCenterOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Intent _intent = null;
                switch (v.getId()) {
                    case R.id.layout_user_center_information:
                        _intent = new Intent(getActivity(), UserInformationActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                    case R.id.layout_user_center_my_file:
                        _intent = new Intent(getActivity(), MyFileActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                    case R.id.layout_user_center_my_collection:
                        _intent = new Intent(getActivity(), MyCollectionActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                    case R.id.layout_user_center_setting:
                        _intent = new Intent(getActivity(), SettingActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                }
            }
        }
    }
}
