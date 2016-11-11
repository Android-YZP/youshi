package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.NetScheduleModel;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.StringUtils;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.util.WeekUtils;

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
    private ArrayList<NetScheduleModel.ViewModelBean.TimeSpanListBean> mTimeSpanListBeans = new ArrayList<>();
    private TextView mTvHabitChooseTime;
    private ProgressDialog mProgressDialog;
    private double mLatitude;
    private double mLongitude;
    private TextView mTvPlace;
    private List<String> mFriends = new ArrayList<>();
    private List<Friend> allChooseFriends = new ArrayList<>();
    private TextView mTvSubmission;
    private int mEventID;
    private Schedule schedule = new Schedule();
    private ArrayList<Schedule> mScheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_habit);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        //得到一个从详情界面传过来的,已经存在日程
        Intent intent = getIntent();
        mEventID = intent.getIntExtra("eventID", -1);
        if (mEventID != -1) {
            mScheduleList = DBHelper.findSch(mEventID + "");
            schedule = mScheduleList.get(0);
            mEtTheme.setText(schedule.getTitle());
            mTvPlace.setText(schedule.getAddress());
            setRBChecked(schedule.getLabel());//设置选择好的标签

            //周期
            mTvHabitCircle.setText("一周" + schedule.getTimes_of_week() + "次");
            mWeek =WeekUtils.replaceWeek3(schedule.getWhich_week()) ;//1,2,3=>123
            mTvHabitWeek.setText(WeekUtils.replaceWeek1(mWeek));//123=>周一 周二 周三
            mTvPersonalEventDescription.setText(schedule.getRemark());

            //时间段的选择
            //时间段的设置
            ArrayList<Schtime> schTimes = DBHelper.findSchTime(mEventID);
            if (!schTimes.isEmpty()) {
                for (int i = 0; i < schTimes.size(); i++) {
                    NetScheduleModel.ViewModelBean.TimeSpanListBean timeSpanListBean =
                            new NetScheduleModel.ViewModelBean.TimeSpanListBean();
                    timeSpanListBean.setStartTime(schTimes.get(i).getBegin_time());
                    timeSpanListBean.setEndTime(schTimes.get(i).getEnd_time());
                    timeSpanListBean.setTdate(schTimes.get(i).getDate());
                    timeSpanListBean.setRemindTime(schTimes.get(i).getWarning_time());
                    timeSpanListBean.setStatus(schTimes.get(i).getStatus());
                    mTimeSpanListBeans.add(timeSpanListBean);
                }
                if (mTimeSpanListBeans.size() > 0) {
                    mTvHabitChooseTime.setText("已选择");
                } else {
                    mTvHabitChooseTime.setText("未选择");
                }
            }
                //报送好友的初始化显示
                ArrayList<Schreport> repPer = DBHelper.findRepPer(schedule.getId());
                if (repPer != null && !repPer.isEmpty()) {
                    for (int i = 0; i < repPer.size(); i++) {
                        mFriends.add(repPer.get(i).getFriendid());
                    }
                    setRepFriends(mFriends);
                }

            //提前提醒
            mTvRemindBefore.setText(mScheduleList.get(0).getAhead_warn() + "分钟前");
            mRemindTime = mScheduleList.get(0).getAhead_warn();
        }
        mTvTitle.setText("添加个人习惯");
    }

    private void initView() {
        mTvCancel = (TextView) findViewById(R.id.tv_add_event_cancel);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
        mTvTitle = (TextView) findViewById(R.id.tv_add_event_title);
        mTvPlace = (TextView) findViewById(R.id.tv_personal_event_place);

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
        mTvSubmission = (TextView) findViewById(R.id.tv_submission);
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
                startActivityForResult(new Intent(AddPersonalHabitActivity.
                        this, ChooseAddressActivity.class), 6);
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
                if (mFriends != null && allChooseFriends != null) {
                    mFriends.clear();
                    allChooseFriends.clear();
                }
                startActivityForResult(new Intent(AddPersonalHabitActivity.
                        this, ChooseSomeoneActivity.class), 5);
                break;

            case R.id.tv_add_event_complete://完成
                if (TextUtils.isEmpty(mEtTheme.getText().toString())) {
                    showTip("请输入主题");
                    return;
                }
                //备注不为空
                if (TextUtils.isEmpty(mTvPersonalEventDescription.getText().toString())) {
                    showTip("请输入备注");
                    return;
                }
                //时间段不为空
                if (mTimeSpanListBeans == null) {
                    showTip("请选择时间段");
                    return;
                }
                if (mEventID != -1) {//重新编辑储存的时候,先删除联系人,时间段在添加联系人时间段
                    DBHelper.DeleteRepPer(mEventID);
                    DBHelper.DeleteSchTime(mEventID);
                }

                saveDataOfNet();
                saveDataOfDb();
                saveReporterToDb();
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

                        JSONObject datas = (JSONObject) _json_result.get("Datas");
                        int id = datas.getInt("Id");
                        Log.d("YZP", "---------------------_success = " + id + "");
                        updateServerIDAndSycStatus(id);//更新唯一ID
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
        viewModelBean.setPlace(mTvPlace.getText().toString());//地址
        viewModelBean.setLatitude(mLatitude + "");//维度
        viewModelBean.setLongitude(mLongitude + "");//精度
        viewModelBean.setWeekTimes(mWeek.length());//周期
        viewModelBean.setWeeks(WeekUtils.replaceWeek2(mWeek)); //周
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
     * 将报送人储存本地数据库
     */
    private void saveReporterToDb() {
        try {
            for (int i = 0; i < allChooseFriends.size(); i++) {
                mDbManager = DBHelper.getDbManager();
                Schreport schreport = new Schreport();
                schreport.setFriendid(allChooseFriends.get(i).getFriendid());
                schreport.setSid(schedule.getId());
                mDbManager.saveOrUpdate(schreport);
            }
        } catch (DbException e) {
            Log.d("yzp", e.getMessage() + "-----------");
            e.printStackTrace();
        }
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
            schedule.setType(2);//日程类型//个人习惯
            schedule.setTitle(mEtTheme.getText().toString());//标题,主题
            schedule.setLabel(mLable);//标签
            schedule.setAddress(mTvPlace.getText().toString());
            schedule.setLatitude(mLatitude + "");
            schedule.setLongitude(mLongitude + "");
            schedule.setSyc_status(0);//同步状态
            schedule.setTimes_of_week(mWeek.length());//周期
            schedule.setWhich_week(WeekUtils.replaceWeek2(mWeek));//周
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
    private void updateServerIDAndSycStatus(int dateid) {
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

        //返回报送人
        if (resultCode == 5 && requestCode == 5 && data != null) {
            String chooseFriends = data.getStringExtra("ChooseFriends");
            Gson gson = new Gson();
            mFriends = gson.fromJson(chooseFriends,//生成报送人上传对象
                    new TypeToken<List<String>>() {
                    }.getType());
            setRepFriends(mFriends);//设置朋友
        }
        //返回地址
        if (resultCode == 6 && requestCode == 6 && data != null) {
            String address = data.getStringExtra("address");
            mLatitude = data.getDoubleExtra("latitude", 0);
            mLongitude = data.getDoubleExtra("longitude", 0);
            if (!TextUtils.isEmpty(address)) {
                mTvPlace.setText(address);
            }
        }

        //返回周
        if (resultCode == 1 && requestCode == 1 && data != null) {
            mWeek = data.getStringExtra("Week");
            UIUtils.showTip(mWeek + "Week");
            String _week = WeekUtils.replaceWeek1(mWeek);//将1234567换成周一 周二 周三
            mTvHabitWeek.setText(_week);
            mTvHabitCircle.setText("每周" + mWeek.length() + "次");
        }

        //时间段
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


    //设置报送人
    private void setRepFriends(List<String> friends) {
        for (int i = 0; i < friends.size(); i++) {
            User _user = CommonUtil.getUserInfo(UIUtils.getContext());
            if (_user != null) {
                try {
                    mDbManager = DBHelper.getDbManager();
                    List<Friend> all = mDbManager.selector(Friend.class).where("userid", "=",
                            _user.getOpenFireUserName()).and("friendid", "=", friends.get(i)).findAll();
                    allChooseFriends.add(all.get(0));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        mTvSubmission.setText("");
        for (int i = 0; i < allChooseFriends.size(); i++) {
            String nickname = allChooseFriends.get(i).getNickname();
            if (!StringUtils.isEmpty(nickname)) {
                mTvSubmission.setText(mTvSubmission.getText().toString() + nickname + " ");
            } else {
                mTvSubmission.setText(mTvSubmission.getText().toString() + allChooseFriends.get(i).getFriendid() + " ");
            }
        }

    }


    //初始化标签
    private void setRBChecked(int lableNum) {
        int LableID = 0;
        switch (lableNum) {
            case 0:
                LableID = R.id.rb_person;
                break;
            case 1:
                LableID = R.id.rb_work;
                break;
            case 2:
                LableID = R.id.rb_entertainment;
                break;
            case 3:
                LableID = R.id.rb_important;
                break;
            case 4:
                LableID = R.id.rb_health;
                break;
            case 5:
                LableID = R.id.rb_other;
                break;
        }

        RadioButton RBCheck = (RadioButton) findViewById(LableID);
        RBCheck.setChecked(true);
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

