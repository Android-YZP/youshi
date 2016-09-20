package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.UIUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class AddPersonalHabitActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtTheme;
    private RelativeLayout mChooseAddress;
    private RadioGroup mRgLabel;
    private int mLable;
    private MyHandler handler = new MyHandler(this);
    private RelativeLayout mSubmission;
    private RelativeLayout mRemindBefore;
    private LinearLayout mRemark;
    private RelativeLayout mRlHabitCircle;
    private RelativeLayout mRlHabitWeek;
    private RelativeLayout mRlHabitChooseTime;
    private TextView mTvCancel;
    private TextView mTvComplete;
    private TextView mTvTitle;
    private String mWeek = "1234567";
    private int mRemindTime;
    private EditText mTvPersonalEventDescription;
    private TextView mTvRemindBefore;
    private TextView mTvHabitWeek;
    private TextView mTvHabitCircle;
    private DbManager mDbManager;
    private Schedule schedule;
    private ArrayList<NetScheduleModel.ViewModelBean.TimeSpanListBean> mTimeSpanListBeans;
    private TextView mTvHabitChooseTime;
    private ProgressDialog mProgressDialog;

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
                        mLable = 0;
                        break;
                    case R.id.rb_work:
                        mLable = 1;
                        break;
                    case R.id.rb_entertainment:
                        mLable = 2;
                        break;
                    case R.id.rb_important:
                        mLable = 3;
                        break;
                    case R.id.rb_health:
                        mLable = 4;
                        break;
                    case R.id.rb_other:
                        mLable = 5;
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

            case R.id.rl_habit_week://周
                startActivityForResult(new Intent(AddPersonalHabitActivity.this,
                        ChooseWeekActivity.class), 1);
                break;

            case R.id.rl_habit_choose_time://选择时间
                startActivityForResult(new Intent(AddPersonalHabitActivity.this,
                        ChooseTimeActivity.class), 2);
                break;
            //后半部分的点击事件
            case R.id.rl_submission://报送
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        ChooseSomeoneActivity.class));
                break;
            case R.id.tv_add_event_complete://完成
                saveDataOfNet();
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
     * 将日程储存网络
     */
    private void saveDataOfNet() {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(AddPersonalHabitActivity.this, "请稍等", "正在保存中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.SAVESCHEDULE);
        //包装请求参数
        String _personEventJson = createPersonJson();
        requestParams.addBodyParameter("", _personEventJson);//用户名
        String loginCode = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
        if (loginCode != null)
            requestParams.addHeader("sVerifyCode", loginCode);//头信息

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("androidYZP", "---------------------result = " + result);
//                if (result != null) {
                //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                try {
                    JSONObject _json_result = new JSONObject(result);
                    Boolean _success = (Boolean) _json_result.get("Success");
                    Log.d("YZP", "---------------------_success = " + _success);
                    if (!_success) {//保存失败
                        String _message = (String) _json_result.get("Message");
                        CommonUtil.sendErrorMessage(_message, handler);
                    } else {//保存成功
                        saveDataOfDb();
                        JSONObject datas = (JSONObject) _json_result.get("Datas");
                        int id = datas.getInt("Id");
                        Log.d("YZP", "---------------------_success = " + id + "");
                        updateServerIDAndUpdateSycStatus(id);//更新唯一ID
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("jlj", "-------onError = " + ex.getMessage());
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, handler);
                } else if (ex instanceof ConnectTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, handler);
                } else if (ex instanceof SocketTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, handler);
                } else {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, handler);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("userLogin", "----onCancelled");
            }

            @Override
            public void onFinished() {
                Log.d("userLogin", "----onFinished");
                //使用handler通知UI取消进度加载对话框
                startActivity(new Intent(AddPersonalHabitActivity.this,
                        CalendarActivity.class));
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                AddPersonalHabitActivity.this.finish();
            }
        });
    }


    /**
     * 生成个人事务的json数据
     *
     * @return
     */
    private String createPersonJson() {
        NetScheduleModel netScheduleModel = new NetScheduleModel();
        NetScheduleModel.ViewModelBean viewModelBean = new NetScheduleModel.ViewModelBean();
        viewModelBean.setScheduleType(2);//事件类型//个人习惯
        viewModelBean.setSubject(mEtTheme.getText().toString());//主题
        viewModelBean.setLabel(mLable);//标签
        viewModelBean.setPlace("宜兴");//地址
        viewModelBean.setLatitude("21.323231");//维度
        viewModelBean.setLongitude("1.2901921");//精度
        viewModelBean.setWeekTimes(mWeek.length());//周期
        viewModelBean.setWeeks(replaceWeek(mWeek));//周
        viewModelBean.setTimeSpanList(mTimeSpanListBeans);//时间段集合
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
            schedule.setType(2);//日程类型//个人习惯
            schedule.setTitle(mEtTheme.getText().toString());//标题,主题
            schedule.setLabel(mLable);//标签
            schedule.setAddress("宜兴");
            schedule.setLatitude("21.323231");
            schedule.setLongitude("1.2901921");
            schedule.setSyc_status(0);//同步状态
            schedule.setTimes_of_week(mWeek.length());//周期
            schedule.setWhich_week(replaceWeek(mWeek));//周
            //报送人
            schedule.setAhead_warn(mRemindTime);//提前提醒
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

    /**
     * 更新从服务端返回的唯一标识ID:serverID
     * 更新同步状态
     */
    private void updateServerIDAndUpdateSycStatus(int dateid) {
        try {
            schedule.setServerid(dateid);
            schedule.setSyc_status(1);
            mDbManager.saveOrUpdate(schedule);
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


    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(AddPersonalHabitActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    ((AddPersonalHabitActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_GET_ADD_PERSONER_EVENT_SUCCESS:
                    //保存个人事务成功
                    break;
                case CommonConstants.FLAG_GET_ADD_PERSONER_EVENT_FAIL://保存失败后调用
                    break;
                default:
                    break;
            }
        }
    }
}

