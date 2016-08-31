package com.mkch.youshi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.AddFriendsActivity;
import com.mkch.youshi.activity.MyCollectionActivity;
import com.mkch.youshi.activity.MyFileActivity;
import com.mkch.youshi.activity.NewFriendActivity;
import com.mkch.youshi.activity.SettingActivity;
import com.mkch.youshi.activity.UserInformationActivity;

public class ContactsFragment extends Fragment {

	private ImageView mIvAddFriend;
	private LinearLayout mLayoutNewFriend;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container, false);
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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 查找所有view
	 * @param view
	 */
	private void findView(View view) {
		mIvAddFriend = (ImageView) view.findViewById(R.id.iv_contacts_topbar_add_friend);
		mLayoutNewFriend = (LinearLayout) view.findViewById(R.id.layout_contacts_new_friend);
	}

	/**
	 * 设置监听器
	 */
	private void setListener() {
		mIvAddFriend.setOnClickListener(new MyContactsOnClickListener());
		mLayoutNewFriend.setOnClickListener(new MyContactsOnClickListener());
	}

	/**
	 * 自定义点击监听类
	 * @author JLJ
	 *
	 */
	private class MyContactsOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			if (getActivity()!=null) {
				Intent _intent = null;
				switch (v.getId()) {
					case R.id.iv_contacts_topbar_add_friend:
						_intent = new Intent(getActivity(), AddFriendsActivity.class);
						getActivity().startActivity(_intent);
						break;
					case R.id.layout_contacts_new_friend:
						_intent = new Intent(getActivity(), NewFriendActivity.class);
						getActivity().startActivity(_intent);
						break;
					default:
						break;
				}
			}
		}
	}
}
