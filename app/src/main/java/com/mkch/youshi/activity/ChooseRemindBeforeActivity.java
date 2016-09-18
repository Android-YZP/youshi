package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.util.DialogFactory;

public class ChooseRemindBeforeActivity extends AppCompatActivity {

    private LinearLayout mRemindBefore60;
    private LinearLayout mRemindBefore30;
    private LinearLayout mRemindBefore20;
    private LinearLayout mRemindBefore10;
    private CheckBox mRemindChecked10;
    private CheckBox mRemindChecked20;
    private CheckBox mRemindChecked30;
    private CheckBox mRemindChecked60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_remind_before);
        initView();
        initData();
        setLister();
    }

    private void initView() {
        mRemindBefore60 = (LinearLayout) findViewById(R.id.ll_choose_remind_60);
        mRemindBefore30 = (LinearLayout) findViewById(R.id.ll_choose_remind_30);
        mRemindBefore20 = (LinearLayout) findViewById(R.id.ll_choose_remind_20);
        mRemindBefore10 = (LinearLayout) findViewById(R.id.ll_choose_remind_10);
        mRemindChecked10 = (CheckBox) findViewById(R.id.cb_choose_remind_10);
        mRemindChecked20 = (CheckBox) findViewById(R.id.cb_choose_remind_20);
        mRemindChecked30 = (CheckBox) findViewById(R.id.cb_choose_remind_30);
        mRemindChecked60 = (CheckBox) findViewById(R.id.cb_choose_remind_60);
    }

    private void initData() {
    }

    private void setLister() {
        mRemindBefore10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemindChecked10.setChecked(!mRemindChecked10.isChecked());
                sendResult(10);

            }
        });

        mRemindBefore20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemindChecked20.setChecked(!mRemindChecked20.isChecked());
                sendResult(20);
            }
        });
        mRemindBefore30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemindChecked30.setChecked(!mRemindChecked30.isChecked());
                sendResult(30);
            }
        });

        mRemindBefore60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemindChecked60.setChecked(!mRemindChecked60.isChecked());
                sendResult(60);
            }
        });

    }

    public void sendResult(int RemindTime){
        Intent intent = getIntent();
        intent.putExtra("RemindTime",RemindTime);
        ChooseRemindBeforeActivity.this.setResult(0,intent);
        ChooseRemindBeforeActivity.this.finish();
    }


}
