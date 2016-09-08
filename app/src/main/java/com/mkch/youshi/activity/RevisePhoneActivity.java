package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;

public class RevisePhoneActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private Button mBtnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_phone);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mBtnCommit = (Button) findViewById(R.id.btn_revise_phone_commit);
    }

    private void initData() {
        mTvTitle.setText("绑定新手机");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RevisePhoneActivity.this.finish();
            }
        });
        mBtnCommit.setOnClickListener(new RevisePhoneOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class RevisePhoneOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.btn_revise_phone_commit:
                    _intent = new Intent(RevisePhoneActivity.this, RevisePhoneCodeActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
