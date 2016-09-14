package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

public class NewMessageNoticeActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LinearLayout mLayoutDisturb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message_notice);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutDisturb = (LinearLayout) findViewById(R.id.layout_new_message_notice_disturb);
    }

    private void initData() {
        mTvTitle.setText("新消息通知");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMessageNoticeActivity.this.finish();
            }
        });
        mLayoutDisturb.setOnClickListener(new NewMessageNoticeOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class NewMessageNoticeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.layout_new_message_notice_disturb:
                    _intent = new Intent(NewMessageNoticeActivity.this, DisturbTimeActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
