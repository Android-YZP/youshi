package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.GroupChatListAdapter;
import com.mkch.youshi.model.Group;
import com.mkch.youshi.util.DBHelper;
import com.tencent.TIMGroupBaseInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMValueCallBack;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends Activity {

    private ImageView mIvBack, mAdd;
    private ListView mListView;
    private List<Group> listGroups = new ArrayList<>();
    private GroupChatListAdapter mAdapter;
    private DbManager dbManager;//数据库管理对象

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
        dbManager = DBHelper.getDbManager();
        listGroups.clear();
        getGroupList();
    }

    private void getGroupList() {
        //获取群聊列表
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-------getGroupList", i + "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                Log.d("zzz-------getGroupList", "getGroupList is success");
                for (TIMGroupBaseInfo info : timGroupBaseInfos) {
                    Group group = new Group();
                    String groupID = info.getGroupId();
                    group.setGroupID(groupID);
                    String groupName = info.getGroupName();
                    group.setGroupName(groupName);
                    String groupHead = info.getFaceUrl();
                    group.setGroupHead(groupHead);
                    listGroups.add(group);
                    //存储所有的群聊列表数据
                    try {
                        Group _group = dbManager.selector(Group.class).where("group_id", "=", groupID).findFirst();
                        if (_group != null) {
                            //有就更改他的字段
                            _group.setGroupID(groupID);
                            _group.setGroupName(groupName);
                            _group.setGroupHead(groupHead);
                            dbManager.saveOrUpdate(_group);
                        } else {
                            //没有就插入
                            Group group1 = new Group(groupID, groupName, groupHead);
                            dbManager.save(group1);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter = new GroupChatListAdapter(GroupChatActivity.this, listGroups);
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupChatActivity.this, ChatActivity.class);
                if (listGroups != null && listGroups.size() != 0) {
                    String groupID = listGroups.get(position).getGroupID();
                    intent.putExtra("chatType", "Group");
                    intent.putExtra("groupID", groupID);
                    startActivity(intent);
                }
            }
        });
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
