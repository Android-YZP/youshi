package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;

public class AddFriendsActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtSearch;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtSearch = (EditText) findViewById(R.id.et_add_friends_search);
        mListView = (ListView) findViewById(R.id.list_add_friends);
    }

    private void initData() {
        mTvTitle.setText("添加好友");
        ListAdapter mAdapter = new AddFriendsMethodsListAdapter(AddFriendsActivity.this);
        mListView.setAdapter(mAdapter);
        //搜索框不弹出软键盘
        mEtSearch.setInputType(InputType.TYPE_NULL);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendsActivity.this.finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent _intent = null;
                switch (position) {
                    case 0:
                        _intent = new Intent(AddFriendsActivity.this, PhoneContactsActivity.class);
                        startActivity(_intent);
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
        });
        mEtSearch.setOnClickListener(new AddFriendsOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class AddFriendsOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.et_add_friends_search:
                    _intent = new Intent(AddFriendsActivity.this, SearchResultActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
