package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;
import com.mkch.youshi.adapter.NewFriendListAdapter;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.receiver.FriendsReceiver;

import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LinearLayout mLayoutAddPhone;
    private ListView mListView;

    //广播接收
    private FriendsReceiver mFriendsReceiver;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int _what = msg.what;
            switch (_what){
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(NewFriendActivity.this, errorMsg, Toast.LENGTH_SHORT).show();

                    break;
                case FriendsReceiver.RECEIVE_REQUEST_ADD_FRIEND:
                    String _request_jid = (String) msg.obj;
                    Toast.makeText(NewFriendActivity.this, _request_jid, Toast.LENGTH_SHORT).show();
                    //更新UI界面，获取最新的用户列表
                    updateUIfromReceiver();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);

        }
    };

    /**
     * 更新UI界面，获取最新的用户列表
     */
    private void updateUIfromReceiver() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutAddPhone = (LinearLayout) findViewById(R.id.layout_new_friend_add_phone);
        mListView = (ListView) findViewById(R.id.list_new_friend);
    }

    private void initData() {
        mTvTitle.setText("新的朋友");
        List<Friend> mFriends = new ArrayList<>();
        ListAdapter mAdapter = new NewFriendListAdapter(NewFriendActivity.this,mFriends);
        mListView.setAdapter(mAdapter);

        //注册广播
        mFriendsReceiver = new FriendsReceiver(mHandler);
        IntentFilter _intent_filter = new IntentFilter("yoshi.action.friendsbroadcast");
        registerReceiver(mFriendsReceiver,_intent_filter);


    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewFriendActivity.this.finish();
            }
        });
        mLayoutAddPhone.setOnClickListener(new NewFriendOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class NewFriendOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent _intent = null;
            switch (v.getId()) {
                case R.id.layout_new_friend_add_phone:
                    _intent = new Intent(NewFriendActivity.this, PhoneContactsActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除广播
        unregisterReceiver(mFriendsReceiver);
    }
}
