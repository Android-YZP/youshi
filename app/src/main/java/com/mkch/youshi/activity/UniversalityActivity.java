package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

public class UniversalityActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LinearLayout mLayoutConflictNumber,mLayoutAffairTime;
    private TextView mTvConflictNumber,mTvAffairTime;
    //保存允许冲突日程数量
    private String[] number_list = {"1", "2", "3", "4", "5"};
    private int checkedItem1 = 0;
    //保存事务单次时长
    private String[] time_list = {"1小时", "2小时", "3小时", "4小时", "5小时"};
    private int checkedItem2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universality);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutConflictNumber = (LinearLayout) findViewById(R.id.layout_universality_conflict_number);
        mTvConflictNumber = (TextView) findViewById(R.id.tv_universality_conflict_number);
        mLayoutAffairTime = (LinearLayout) findViewById(R.id.layout_universality_affair_time);
        mTvAffairTime = (TextView) findViewById(R.id.tv_universality_affair_time);
    }

    private void initData() {
        mTvTitle.setText("通用");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UniversalityActivity.this.finish();
            }
        });
        //弹出对话框，用户选择允许冲突日程数量后更新界面
        mLayoutConflictNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UniversalityActivity.this).setTitle("请选择允许冲突日程数量").setSingleChoiceItems(
                        number_list, checkedItem1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkedItem1 = which;
                                mTvConflictNumber.setText(number_list[which]);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        //弹出对话框，用户选择事务单次时长后更新界面
        mLayoutAffairTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UniversalityActivity.this).setTitle("请选择事务单次时长").setSingleChoiceItems(
                        time_list, checkedItem2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkedItem2 = which;
                                mTvAffairTime.setText(time_list[which]);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
}
