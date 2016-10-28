package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ChooseGroupMemberAdapter;

public class AddGroupChatActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtGroupName;
    private String groupName;
    private Button mBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_chat);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtGroupName = (EditText) findViewById(R.id.et_add_group_chat_name);
        mBtnConfirm = (Button) findViewById(R.id.btn_add_group_chat_confirm);
    }

    private void initData() {
        mTvTitle.setText("创建群");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGroupChatActivity.this.finish();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupName = mEtGroupName.getText().toString();
                if (groupName == null || groupName.equals("")) {
                    Toast.makeText(AddGroupChatActivity.this, "您未填写群名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(AddGroupChatActivity.this, ChooseGroupMemberAdapter.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });
    }
}
