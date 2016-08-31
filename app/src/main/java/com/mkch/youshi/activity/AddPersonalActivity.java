package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

public class AddPersonalActivity extends AppCompatActivity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private RelativeLayout mRlEvent;
    private RelativeLayout mRlAffair;
    private RelativeLayout mRlHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);

        //事件
        mRlEvent = (RelativeLayout) findViewById(R.id.rl_event);
        //事务
        mRlAffair = (RelativeLayout) findViewById(R.id.rl_affair);
        //习惯
        mRlHabit = (RelativeLayout) findViewById(R.id.rl_habit);
    }

    private void initData() {
        mTvTitle.setText("新建个人日程");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //事件
        mRlEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPersonalActivity.this,
                        AddPersonalEventActivity.class));
            }
        });

        //事务
        mRlAffair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPersonalActivity.this,
                        AddPersonalAffairActivity.class));
            }
        });

        //习惯
        mRlHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPersonalActivity.this,
                        AddPersonalHabitActivity.class));
            }
        });
    }

}
