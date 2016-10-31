package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.GroupChatListAdapter;
import com.mkch.youshi.model.Group;
import com.tencent.TIMGroupBaseInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends Activity {

    private ImageView mIvBack, mAdd;
    private ListView mListView;
    private List<Group> listGroups = new ArrayList<>();
    private GroupChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        initView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_group_chat_back);
        mAdd = (ImageView) findViewById(R.id.iv_group_chat_add);
        mListView = (ListView) findViewById(R.id.list_group_chat);
    }

    private void initData() {
        //获取群聊列表
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-------getGroupList", i + "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                Log.d("zzz-------getGroupList", "getGroupList is success");
                for(TIMGroupBaseInfo info : timGroupBaseInfos) {
                    Group group = new Group();
                    String groupID = info.getGroupId();
                    group.setGroupID(groupID);
                    String groupName = info.getGroupName();
                    group.setGroupName(groupName);
                    String groupHead = info.getFaceUrl();
                    group.setGroupHead(groupHead);
                    listGroups.add(group);
                }
                mAdapter = new GroupChatListAdapter(GroupChatActivity.this,listGroups);
                mListView.setAdapter(mAdapter);
            }
        });
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupChatActivity.this.finish();
            }
        });
        mAdd.setOnClickListener(new GroupChatOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author ZJ
     */
    private class GroupChatOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.iv_group_chat_add:
                    _intent = new Intent(GroupChatActivity.this, AddGroupChatActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
