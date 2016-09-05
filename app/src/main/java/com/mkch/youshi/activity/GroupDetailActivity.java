package com.mkch.youshi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.GroupFriendListAdapter;
import com.mkch.youshi.bean.GroupFriend;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_group_detail)
public class GroupDetailActivity extends BaseActivity {

    @ViewInject(R.id.gv_group_detail_friends)
    private GridView mGvFriendHeadpics;//参与聊天的好友列表

    @ViewInject(R.id.iv_common_topbar_back)
    private ImageView mIvBack;

    private List<GroupFriend> mGroupFriends;
    private GroupFriendListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    private void initData() {
        //数据源
        mGroupFriends = new ArrayList<>();
        mGroupFriends.add(new GroupFriend("http://cdn.duitang.com/uploads/item/201502/04/20150204000709_QCzwf.thumb.224_0.jpeg","张三"));
        mGroupFriends.add(new GroupFriend("http://p6.qhimg.com/t0126e0bed7fa0741a1.jpg","李四"));
        mGroupFriends.add(new GroupFriend("http://cdn.duitang.com/uploads/item/201502/04/20150204000709_QCzwf.thumb.224_0.jpeg","王五"));
        mGroupFriends.add(new GroupFriend("",""));
        //adapter
        mAdapter = new GroupFriendListAdapter(this,mGroupFriends);
        //设置适配器
        mGvFriendHeadpics.setAdapter(mAdapter);

    }

    @Event({R.id.iv_common_topbar_back})
    private void clickButtons(View view){
        switch (view.getId()){
            case R.id.iv_common_topbar_back://返回按钮
                this.finish();//返回
                break;

        }
    }
}
