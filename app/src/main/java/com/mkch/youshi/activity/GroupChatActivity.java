package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.GroupChatListAdapter;

public class GroupChatActivity extends Activity {

    private ImageView mIvBack, mAdd;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_group_chat_back);
        mAdd = (ImageView) findViewById(R.id.iv_group_chat_add);
        mListView = (ListView) findViewById(R.id.list_group_chat);
    }

    private void initData() {
        ListAdapter mAdapter = new GroupChatListAdapter(GroupChatActivity.this);
        mListView.setAdapter(mAdapter);
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
