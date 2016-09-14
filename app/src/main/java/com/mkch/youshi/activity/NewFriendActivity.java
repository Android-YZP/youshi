package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.NewFriendListAdapter;

public class NewFriendActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtSearch;
    private LinearLayout mLayoutAddPhone;
    private ListView mListView;

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
        mEtSearch = (EditText) findViewById(R.id.et_new_friend_search);
        mLayoutAddPhone = (LinearLayout) findViewById(R.id.layout_new_friend_add_phone);
        mListView = (ListView) findViewById(R.id.list_new_friend);
    }

    private void initData() {
        mTvTitle.setText("新的朋友");
        ListAdapter mAdapter = new NewFriendListAdapter(NewFriendActivity.this);
        mListView.setAdapter(mAdapter);
        //搜索框不弹出软键盘
        mEtSearch.setInputType(InputType.TYPE_NULL);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewFriendActivity.this.finish();
            }
        });
        mLayoutAddPhone.setOnClickListener(new NewFriendOnClickListener());
        mEtSearch.setOnClickListener(new NewFriendOnClickListener());
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
                case R.id.et_new_friend_search:
                    _intent = new Intent(NewFriendActivity.this, SearchResultActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
