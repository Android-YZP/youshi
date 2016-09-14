package com.mkch.youshi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.view.DisturbTimeTabBarLayout;

public class DisturbTimeActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private DisturbTimeTabBarLayout mDisturbTimeTabBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disturb_time);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mDisturbTimeTabBarLayout = (DisturbTimeTabBarLayout) findViewById(R.id.myDisturbTimeTabBarLayout);
    }

    private void initData() {
        mTvTitle.setText("消息免打扰");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisturbTimeActivity.this.finish();
            }
        });
        //当用户选择免打扰时间后，设置对应发出提示音和振动的时间
        mDisturbTimeTabBarLayout.setOnItemClickListener(new DisturbTimeTabBarLayout.IDisturbTimeTabBarCallBackListener() {
            @Override
            public void clickItem(int id) {
                switch (id) {
                    case R.id.index_open_item:
                        break;
                    case R.id.index_night_item:
                        break;
                    case R.id.index_close_item:
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
