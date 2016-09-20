package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

public class ChooseWeekActivity extends AppCompatActivity {

    private LinearLayout mChooseWeekEveryday;
    private LinearLayout mChooseWeek1;
    private LinearLayout mChooseWeek2;
    private LinearLayout mChooseWeek3;
    private LinearLayout mChooseWeek4;
    private LinearLayout mChooseWeek5;
    private LinearLayout mChooseWeek6;
    private CheckBox mCbChooseWeekEveryday;
    private CheckBox mCbChooseWeek1;
    private CheckBox mCbChooseWeek2;
    private CheckBox mCbChooseWeek3;
    private CheckBox mCbChooseWeek4;
    private CheckBox mCbChooseWeek5;
    private CheckBox mCbChooseWeek6;
    private CheckBox mCbChooseWeek7;
    private LinearLayout mChooseWeek7;
    private String week1 = "";
    private String week2 = "";
    private String week3 = "";
    private String week4 = "";
    private String week5 = "";
    private String week6 = "";
    private String week7 = "";
    private TextView mTvComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_week);
        initView();
        initData();
        setListener();

    }


    private void initView() {
        mChooseWeekEveryday = (LinearLayout) findViewById(R.id.choose_week_everyday);
        mChooseWeek1 = (LinearLayout) findViewById(R.id.choose_week1);
        mChooseWeek2 = (LinearLayout) findViewById(R.id.choose_week2);
        mChooseWeek3 = (LinearLayout) findViewById(R.id.choose_week3);
        mChooseWeek4 = (LinearLayout) findViewById(R.id.choose_week4);
        mChooseWeek5 = (LinearLayout) findViewById(R.id.choose_week5);
        mChooseWeek6 = (LinearLayout) findViewById(R.id.choose_week6);
        mChooseWeek7 = (LinearLayout) findViewById(R.id.choose_week7);
        mCbChooseWeekEveryday = (CheckBox) findViewById(R.id.cb_choose_week_everyday);
        mCbChooseWeek1 = (CheckBox) findViewById(R.id.cb_choose_week1);
        mCbChooseWeek2 = (CheckBox) findViewById(R.id.cb_choose_week2);
        mCbChooseWeek3 = (CheckBox) findViewById(R.id.cb_choose_week3);
        mCbChooseWeek4 = (CheckBox) findViewById(R.id.cb_choose_week4);
        mCbChooseWeek5 = (CheckBox) findViewById(R.id.cb_choose_week5);
        mCbChooseWeek6 = (CheckBox) findViewById(R.id.cb_choose_week6);
        mCbChooseWeek7 = (CheckBox) findViewById(R.id.cb_choose_week7);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
    }


    private void initData() {

    }

    private void setListener() {
        mTvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                if (mCbChooseWeek1.isChecked()) {
                    sb.append(1);
                }
                if (mCbChooseWeek2.isChecked()) {
                    sb.append(2);
                }
                if (mCbChooseWeek3.isChecked()) {
                    sb.append(3);
                }
                if (mCbChooseWeek4.isChecked()) {
                    sb.append(4);
                }
                if (mCbChooseWeek5.isChecked()) {
                    sb.append(5);
                }
                if (mCbChooseWeek6.isChecked()) {
                    sb.append(6);
                }
                if (mCbChooseWeek7.isChecked()) {
                    sb.append(7);
                }

                sendResult(sb.toString());
            }
        });


        mChooseWeekEveryday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeekEveryday.setChecked(!mCbChooseWeekEveryday.isChecked());
                mCbChooseWeek1.setChecked(mCbChooseWeekEveryday.isChecked());
                mCbChooseWeek2.setChecked(mCbChooseWeekEveryday.isChecked());
                mCbChooseWeek3.setChecked(mCbChooseWeekEveryday.isChecked());
                mCbChooseWeek4.setChecked(mCbChooseWeekEveryday.isChecked());
                mCbChooseWeek5.setChecked(mCbChooseWeekEveryday.isChecked());
                mCbChooseWeek6.setChecked(mCbChooseWeekEveryday.isChecked());
                mCbChooseWeek7.setChecked(mCbChooseWeekEveryday.isChecked());
            }
        });
        mChooseWeek7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeek7.setChecked(!mCbChooseWeek7.isChecked());
                mCbChooseWeekEveryday.setChecked(false);


            }
        });
        mChooseWeek6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeek6.setChecked(!mCbChooseWeek6.isChecked());
                mCbChooseWeekEveryday.setChecked(false);


            }
        });
        mChooseWeek5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeek5.setChecked(!mCbChooseWeek5.isChecked());
                mCbChooseWeekEveryday.setChecked(false);

            }
        });
        mChooseWeek4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeek4.setChecked(!mCbChooseWeek4.isChecked());
                mCbChooseWeekEveryday.setChecked(false);

            }
        });
        mChooseWeek3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeek3.setChecked(!mCbChooseWeek3.isChecked());
                mCbChooseWeekEveryday.setChecked(false);

            }
        });
        mChooseWeek2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeek2.setChecked(!mCbChooseWeek2.isChecked());
                mCbChooseWeekEveryday.setChecked(false);

            }
        });
        mChooseWeek1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbChooseWeek1.setChecked(!mCbChooseWeek1.isChecked());
                mCbChooseWeekEveryday.setChecked(false);

            }
        });
    }


    public void sendResult(String week) {
        Intent intent = getIntent();
        intent.putExtra("Week", week);
        ChooseWeekActivity.this.setResult(1, intent);
        ChooseWeekActivity.this.finish();
    }
}