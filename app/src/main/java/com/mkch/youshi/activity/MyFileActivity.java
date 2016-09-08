package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

public class MyFileActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LinearLayout mLayoutSchedule, mLayoutLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_file);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutSchedule = (LinearLayout) findViewById(R.id.layout_my_file_schedule_file);
        mLayoutLocal = (LinearLayout) findViewById(R.id.layout_my_file_drop_box_file);
    }

    private void initData() {
        mTvTitle.setText("我的文件");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFileActivity.this.finish();
            }
        });
        mLayoutSchedule.setOnClickListener(new MyFileOnClickListener());
        mLayoutLocal.setOnClickListener(new MyFileOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class MyFileOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.layout_my_file_schedule_file:
                    _intent = new Intent(MyFileActivity.this, ScheduleFileActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_my_file_drop_box_file:
                    _intent = new Intent(MyFileActivity.this, DropBoxFileActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }
}
