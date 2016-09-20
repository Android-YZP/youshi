package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.NetScheduleModel;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.util.DBHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class AddPersonalHabitActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtTheme;
    private RelativeLayout mChooseAddress;
    private RadioGroup mRgLabel;
    private int mLable;
    private RelativeLayout mSubmission;
    private RelativeLayout mRemindBefore;
    private LinearLayout mRemark;
    private RelativeLayout mRlHabitCircle;
    private RelativeLayout mRlHabitWeek;
    private RelativeLayout mRlHabitChooseTime;
    private RelativeLayout mRlHabitAllTime;
    private TextView mTvCancel;
    private TextView mTvComplete;
    private TextView mTvTitle;
    private String mWeek;
    private int mRemindTime;
    private EditText mTvPersonalEventDescription;
    private TextView mTvRemindBefore;
    private TextView mTvHabitWeek;
    private TextView mTvHabitCircle;
    private DbManager mDbManager;
    private Schedule schedule;
    private ArrayList<NetScheduleModel.ViewModelBean.TimeSpanListBean> mTimeSpanListBeans;
    private TextView mTvHabitChooseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_habit);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        mTvTitle.setText("添加个人习惯");
    }

    private void initView() {
        mTvCancel = (TextView) findViewById(R.id.tv_add_event_cancel);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
        mTvTitle = (TextView) findViewById(R.id.tv_add_event_title);

        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);

        mRlHabitCircle = (RelativeLayout) findViewById(R.id.rl_habit_circle);
        mTvHabitCircle = (TextView) findViewById(R.id.tv_habit_circle);
        mRlHabitWeek = (RelativeLayout) findViewById(R.id.rl_habit_week);
        mTvHabitWeek = (TextView) findViewById(R.id.tv_habit_week);
        mRlHabitChooseTime = (RelativeLayout) findViewById(R.id.rl_habit_choose_time);
        mTvHabitChooseTime = (TextView) findViewById(R.id.tv_habit_choose_time);


        mSubmission = (RelativeLayout) findViewById(R.id.rl_submission);
        mRemindBefore = (RelativeLayout) findViewById(R.id.rl_remind_before);
        mRemark = (LinearLayout) findViewById(R.id.ll_remark);
        mTvPersonalEventDescription = (EditText) findViewById(R.id.et_add_event_description);
        mTvRemindBefore = (TextView) findViewById(R.id.tv_remind_before);
    }

    private void setListener() {
        //标签
        mRgLabel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_person:
                        Toast.makeText(AddPersonalHabitActivity.this, "点击了个人",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_work:
                        break;
                    case R.id.rb_entertainment:
                        break;
                    case R.id.rb_important:
                        break;
                    case R.id.rb_health:
                        break;
                    case R.id.rb_other:
                        break;
                }
            }
        });
        //选择地址
        mChooseAddress.setOnClickListener(this);

        //周期
        mRlHabitCircle.setOnClickListener(this);
        //周
        mRlHabitWeek.setOnClickListener(this);
        //选择时间
        mRlHabitChooseTime.setOnClickListener(this);


        //报送
        mSubmission.setOnClickListener(this);
        //提前提醒
        mRemindBefore.setOnClickListener(this);
        //备注
        mRemark.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mTvComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前半部分
            case R.id.rl_choose_address://选择地址
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseAddressActivity.class));
                break;

            //中间部分的点击事件
            case R.id.rl_habit_circle://周期
                Toast.makeText(AddPersonalHabitActivity.this, "动态计算出来的",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.rl_habit_week://周
                startActivityForResult(new Intent(AddPersonalHabitActivity.this,
                        ChooseWeekActivity.class), 1);
                break;

            case R.id.rl_habit_choose_time://选择时间
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseTimeActivity.class));
                break;
            //后半部分的点击事件
            case R.id.rl_submission://报送
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseSomeoneActivity.class));
                break;
            case R.id.tv_add_event_complete://完成
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        CalendarActivity.class));
                break;
            case R.id.tv_add_event_cancel://取消
                finish();
                break;
            case R.id.rl_remind_before://提前提醒
                startActivityForResult(new Intent(AddPersonalHabitActivity.this,
                        ChooseRemindBeforeActivity.class), 0);
                break;
            default:
                break;
        }
    }

    /**
     * 生成个人事务的json数据
     *
     * @return
     */
    private String createPersonJson() {
        NetScheduleModel netScheduleModel = new NetScheduleModel();
        NetScheduleModel.ViewModelBean viewModelBean = new NetScheduleModel.ViewModelBean();
        viewModelBean.setScheduleType(1);//事件类型
        viewModelBean.setSubject(mEtTheme.getText().toString());//主题
        viewModelBean.setPlace("宜兴");//地址
        viewModelBean.setLabel(mLable);//标签
        viewModelBean.setLatitude("21.323231");//维度
        viewModelBean.setLongitude("1.2901921");//精度
        viewModelBean.setTimeSpanList(mTimeSpanListBeans);//时间段集合
//        viewModelBean.setStartTime(mTwoStartDate.getText().toString());//开始时间
//        viewModelBean.setStopTime(mTwoEndDate.getText().toString());//结束时间
        viewModelBean.setWeeks(replaceWeek(mWeek));//结束时间
//        viewModelBean.setSendOpenFireNameList(mTvEndTime.getText().toString());报送人
        viewModelBean.setRemindType(mRemindTime);//提前提醒
        viewModelBean.setDescription(mTvPersonalEventDescription.getText().toString());//描述,备注
        netScheduleModel.setViewModel(viewModelBean);

        Gson gson = new Gson();
        String textJson = gson.toJson(netScheduleModel);
        return textJson;
    }

    /**
     * 将日程储存本地数据库
     * 数据库和网络数据的储存
     * 1.点击完成时(有网络上传网络更新同步状态的字段),没有网络直接保存数据库,(监听有网络就上传同步)
     * 2.各种字段的储存
     */
    private void saveDataOfDb() {
        try {
            Log.d("yzp", "-----------saveDataOfDb");
            mDbManager = DBHelper.getDbManager();
            schedule = new Schedule();
            schedule.setAddress("宜兴");
            schedule.setType(2);//日程类型//个人习惯
            schedule.setTitle(mEtTheme.getText().toString());//标题,主题
            schedule.setLabel(mLable);//标签
            schedule.setLatitude("21.323231");
            schedule.setLongitude("1.2901921");
            schedule.setAhead_warn(mRemindTime);//提前提醒
            schedule.setSyc_status(0);//同步状态
            schedule.setWhich_week(replaceWeek(mWeek));//周
            //时间段//报送人
            schedule.setRemark(mTvPersonalEventDescription.getText().toString());//备注
            mDbManager.saveOrUpdate(schedule);
            for (int i = 0; i < mTimeSpanListBeans.size(); i++) {
                Schtime schtime = new Schtime();
                schtime.setBegin_time(mTimeSpanListBeans.get(i).getStartTime());
                schtime.setEnd_time(mTimeSpanListBeans.get(i).getEndTime());
                schtime.setSid(schedule.getId());
                schtime.setStatus(0);
                mDbManager.saveOrUpdate(schtime);
            }

        } catch (DbException e) {
            Log.d("yzp", e.getMessage() + "-----------");
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && requestCode == 0 && data != null) {
            mRemindTime = data.getIntExtra("RemindTime", 0);
            if (mRemindTime != 0)
                mTvRemindBefore.setText(mRemindTime + "分钟前");
        }
        if (resultCode == 1 && requestCode == 1 && data != null) {
            mWeek = data.getStringExtra("Week");
            String _week = replaceWeek();//将1234567换成周一,周二,周三
            mTvHabitWeek.setText(_week);
            mTvHabitCircle.setText("每周" + mWeek.length() + "次");
        }

        if (resultCode == 2 && requestCode == 2 && data != null) {//选择时间段
            String _timeSpanListBeanListString = data.getStringExtra("TimeSpanListBeanList");
            Gson gson = new Gson();
            mTimeSpanListBeans = gson.fromJson(_timeSpanListBeanListString,
                    new TypeToken<List<NetScheduleModel.ViewModelBean.TimeSpanListBean>>() {
                    }.getType());

            if (mTimeSpanListBeans.size() > 0) {
                mTvHabitChooseTime.setText("已选择");
            } else {
                mTvHabitChooseTime.setText("未选择");
            }

        }
    }

    /**
     * 将123456换成周一周二周三
     *
     * @return
     */
    private String replaceWeek() {
        String _week1 = mWeek.replace("1", "周一 ");
        String _week2 = _week1.replace("2", "周二 ");
        String _week3 = _week2.replace("3", "周三 ");
        String _week4 = _week3.replace("4", "周四 ");
        String _week5 = _week4.replace("5", "周五 ");
        String _week6 = _week5.replace("6", "周六 ");
        return _week6.replace("7", "周日");
    }

    /**
     * 将123456换成周1.2.3.
     *
     * @return
     */
    private String replaceWeek(String week) {
        String _week1 = week.replace("1", "1,");
        String _week2 = _week1.replace("2", "2,");
        String _week3 = _week2.replace("3", "3,");
        String _week4 = _week3.replace("4", "4,");
        String _week5 = _week4.replace("5", "5,");
        String _week6 = _week5.replace("6", "6,");
        return _week6.replace("7", "7,");
    }
}

