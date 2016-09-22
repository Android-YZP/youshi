package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;

public class UniversalityActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle,mTvEmptyLocal,mTvEmptyFinish;
    private LinearLayout mLayoutConflictNumber, mLayoutAffairTime;
    private View mLine1, mLine2;
    private TextView mTvConflictNumber, mTvAffairTime;
    private CheckBox mCBAutoFinish, mCBConflictPromise;
    private User mUser;
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
        mLine1 = (View) findViewById(R.id.line_universality_1);
        mLine2 = (View) findViewById(R.id.line_universality_2);
        mTvConflictNumber = (TextView) findViewById(R.id.tv_universality_conflict_number);
        mCBAutoFinish = (CheckBox) findViewById(R.id.cb_universality_auto_finish);
        mCBConflictPromise = (CheckBox) findViewById(R.id.cb_universality_conflict_promise);
        mLayoutAffairTime = (LinearLayout) findViewById(R.id.layout_universality_affair_time);
        mTvAffairTime = (TextView) findViewById(R.id.tv_universality_affair_time);
        mTvEmptyLocal = (TextView) findViewById(R.id.tv_universality_empty_local_data);
        mTvEmptyFinish = (TextView) findViewById(R.id.tv_universality_empty_finish_affair);
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(this);
        mTvTitle.setText("通用");
        //判断是否允许日程自动完成
        if (mUser.getAutoFinish() == null || mUser.getAutoFinish().equals("")) {
            mCBAutoFinish.setChecked(true);
        } else if (mUser.getAutoFinish()) {
            mCBAutoFinish.setChecked(true);
        } else {
            mCBAutoFinish.setChecked(false);
        }
        //判断是否允许日程时间冲突
        if (mUser.getConflictPromise() == null || mUser.getConflictPromise().equals("")) {
            mCBConflictPromise.setChecked(true);
            mLine1.setVisibility(View.VISIBLE);
            mLine2.setVisibility(View.VISIBLE);
            mLayoutConflictNumber.setVisibility(View.VISIBLE);
            if(mUser.getConflictNumber() == null || mUser.getConflictNumber().equals("")){
                mTvConflictNumber.setText("1");
            }else {
                mTvConflictNumber.setText(mUser.getConflictNumber());
            }
        } else if (mUser.getConflictPromise()) {
            mCBConflictPromise.setChecked(true);
            mLine1.setVisibility(View.VISIBLE);
            mLine2.setVisibility(View.VISIBLE);
            mLayoutConflictNumber.setVisibility(View.VISIBLE);
            mTvConflictNumber.setText(mUser.getConflictNumber());
            if(mUser.getConflictNumber() == null || mUser.getConflictNumber().equals("")){
                mTvConflictNumber.setText("1");
            }else {
                mTvConflictNumber.setText(mUser.getConflictNumber());
            }
        } else {
            mCBConflictPromise.setChecked(false);
            mLine1.setVisibility(View.GONE);
            mLine2.setVisibility(View.GONE);
            mLayoutConflictNumber.setVisibility(View.GONE);
        }
        if(mUser.getAffairTime() == null || mUser.getAffairTime().equals("")){
            mTvAffairTime.setText("1小时");
        }else {
            mTvAffairTime.setText(mUser.getAffairTime());
        }
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
                                mUser.setConflictNumber(number_list[which]);
                                CommonUtil.saveUserInfo(mUser, UniversalityActivity.this);
                            }
                        }).show();
            }
        });
        mCBAutoFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUser.setAutoFinish(isChecked);
                CommonUtil.saveUserInfo(mUser, UniversalityActivity.this);
                Toast.makeText(UniversalityActivity.this, "已修改是否允许日程自动完成", Toast.LENGTH_LONG).show();
            }
        });
        mCBConflictPromise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mLine1.setVisibility(View.VISIBLE);
                    mLine2.setVisibility(View.VISIBLE);
                    mLayoutConflictNumber.setVisibility(View.VISIBLE);
                    mUser.setConflictPromise(isChecked);
                    CommonUtil.saveUserInfo(mUser, UniversalityActivity.this);
                    Toast.makeText(UniversalityActivity.this, "已修改是否允许日程时间冲突", Toast.LENGTH_LONG).show();
                } else {
                    mLine1.setVisibility(View.GONE);
                    mLine2.setVisibility(View.GONE);
                    mLayoutConflictNumber.setVisibility(View.GONE);
                    mUser.setConflictPromise(isChecked);
                    CommonUtil.saveUserInfo(mUser, UniversalityActivity.this);
                    Toast.makeText(UniversalityActivity.this, "已修改是否允许日程时间冲突", Toast.LENGTH_LONG).show();
                }
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
                                mUser.setAffairTime(time_list[which]);
                                CommonUtil.saveUserInfo(mUser, UniversalityActivity.this);
                            }
                        }).show();
            }
        });
    }
}
