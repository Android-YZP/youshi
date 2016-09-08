package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;

public class RevisePasswordActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private Button mBtnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_password);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mBtnCommit = (Button) findViewById(R.id.btn_revise_password_commit);
    }

    private void initData() {
        mTvTitle.setText("修改密码");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RevisePasswordActivity.this.finish();
            }
        });
        mBtnCommit.setOnClickListener(new RevisePasswordOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class RevisePasswordOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.btn_revise_password_commit:
                    _intent = new Intent(RevisePasswordActivity.this, RevisePasswordCodeActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
