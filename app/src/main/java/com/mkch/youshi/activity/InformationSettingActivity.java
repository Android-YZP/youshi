package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;

public class InformationSettingActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle, mTvSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_setting);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mTvSetting = (TextView) findViewById(R.id.tv_information_setting_setting);
    }

    private void initData() {
        mTvTitle.setText("资料设置");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InformationSettingActivity.this.finish();
            }
        });
        mTvSetting.setOnClickListener(new InformationSettingOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class InformationSettingOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.tv_information_setting_setting:
                    _intent = new Intent(InformationSettingActivity.this, RemarkInformationActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
