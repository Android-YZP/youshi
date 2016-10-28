package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.mkch.youshi.model.Schjoiner;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.DialogFactory;
import com.mkch.youshi.util.StringUtils;
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

public class AddManyPeopleEventActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtTheme;
    private RelativeLayout mChooseAddress;
    private RadioGroup mRgLabel;
    private int mLable;
    private CheckBox mCbAllDay;
    private RelativeLayout mStartTime;
    private RelativeLayout mEndTime;
    private RelativeLayout mManyPeopleParticipant;
    private RelativeLayout mManyPeopleSubmission;
    private RelativeLayout mManyPeopleRemindBefore;
    private RelativeLayout mManyPeopleUploading;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private MyHandler handler = new MyHandler(this);
    private Boolean isAllDay = false;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private int mCurrentHour;
    private int mCurrentMinute;
    private List<String> mRepFriends = new ArrayList<>();
    private List<String> mJoinFriends = new ArrayList<>();
    private List<Friend> allChooseRepFriends = new ArrayList<>();
    private List<Friend> allChooseJoinFriends = new ArrayList<>();
    private TextView mTvCancel;
    private TextView mTvComplete;
    private TextView mTvTitle;
    private Schedule schedule = new Schedule();
    private int mRemindTime;
    private TextView mTvManyPeopleRemindBefore;
    private EditText mEtManyPeopleDesc;
    private DbManager mDbManager;
    private ProgressDialog mProgressDialog;
    private double mLatitude;
    private double mLongitude;
    private TextView mTvPlace;
    private TextView mTvSubmission;
    private TextView mTvJoinSubmission;
    private int mEventID;
    private ArrayList<Schedule> mScheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_many_people_event);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        //得到一个从详情界面传过来的,已经存在日程
        Intent intent = getIntent();
        mEventID = intent.getIntExtra("eventID", -1);
        if (mEventID != -1) {
            mScheduleList = CommonUtil.findSch(mEventID + "");
            schedule = mScheduleList.get(0);
            mEtTheme.setText(schedule.getTitle());
            //判断选择时间是不是全天
            String begin_time = schedule.getBegin_time();
            //根据时间的格式来判断日期的形式,(日期长度小于15为全天事件)
            if (begin_time.length() < 15) {//全天事件的初始化
                mCbAllDay.setChecked(true);
                isAllDay = true;
            }

            mTvStartTime.setText(begin_time);
            mTvEndTime.setText(schedule.getEnd_time());
            mTvPlace.setText(schedule.getAddress());
            mEtManyPeopleDesc.setText(schedule.getRemark());
            setRBChecked(schedule.getLabel());//设置选择好的标签
            //初始化标签
            mLable = schedule.getLabel();
            //提前提醒
            mTvManyPeopleRemindBefore.setText(schedule.getAhead_warn() + "分钟前");
            mRemindTime = schedule.getAhead_warn();

            //好友的初始化显示
            ArrayList<Schreport> repPer = CommonUtil.findRepPer(schedule.getId());
            for (int i = 0; i < repPer.size(); i++) {
                mRepFriends.add(repPer.get(i).getFriendid());
            }
            setRepFriends(mRepFriends);

            //参与人好友的初始化显示
            ArrayList<Schjoiner> joinPer = CommonUtil.findJoinPer(schedule.getId());
            for (int i = 0; i < joinPer.size(); i++) {
                mJoinFriends.add(joinPer.get(i).getJoiner_id());
            }
            setJoinFriends(mJoinFriends);

        } else {

            initTime();
            mTvStartTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour, mCurrentMinute, isAllDay));
            mTvEndTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour + 1, mCurrentMinute, isAllDay));
            mTvTitle.setText("添加多人日程");
        }

    }

    private void initTime() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        mCurrentYear = t.year;
        mCurrentMonth = t.month + 1;
        mCurrentDay = t.monthDay;
        mCurrentHour = t.hour;
        mCurrentMinute = t.minute;
    }

    private void initView() {
        mTvCancel = (TextView) findViewById(R.id.tv_add_event_cancel);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
        mTvTitle = (TextView) findViewById(R.id.tv_add_event_title);

        mTvPlace = (TextView) findViewById(R.id.tv_personal_event_place);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);
        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);

        mCbAllDay = (CheckBox) findViewById(R.id.cb_all_day);
        mStartTime = (RelativeLayout) findViewById(R.id.rl_start_time);
        mTvStartTime = (TextView) findViewById(R.id.tv_start_time);
        mEndTime = (RelativeLayout) findViewById(R.id.rl_end_time);
        mTvEndTime = (TextView) findViewById(R.id.tv_end_time);

        mManyPeopleParticipant = (RelativeLayout) findViewById(R.id.rl_many_people_participant);
        mManyPeopleSubmission = (RelativeLayout) findViewById(R.id.rl_many_people_submission);
        mTvSubmission = (TextView) findViewById(R.id.tv_many_people_submission);
        mTvJoinSubmission = (TextView) findViewById(R.id.rl_many_people_join_submission);
        mManyPeopleRemindBefore = (RelativeLayout) findViewById(R.id.rl_many_people_remind_before);
        mTvManyPeopleRemindBefore = (TextView) findViewById(R.id.tv_many_people_remind_before);
        mManyPeopleUploading = (RelativeLayout) findViewById(R.id.rl_many_people_uploading);
        mEtManyPeopleDesc = (EditText) findViewById(R.id.et_many_people_description);
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

        //全天
        mCbAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                initTime();
                isAllDay = isChecked;
                mTvStartTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour, mCurrentMinute, isAllDay));
                mTvEndTime.setText(DialogFactory.getWeek(mCurrentYear, mCurrentMonth, mCurrentDay, mCurrentHour + 1, mCurrentMinute, isAllDay));
                isAllDay = isChecked;
            }
        });
        //开始时间
        mStartTime.setOnClickListener(this);
        //结束时间
        mEndTime.setOnClickListener(this);

        //参与人
        mManyPeopleParticipant.setOnClickListener(this);
        //报送
        mManyPeopleSubmission.setOnClickListener(this);
        //提前提醒
        mManyPeopleRemindBefore.setOnClickListener(this);
        //上传
        mManyPeopleUploading.setOnClickListener(this);

        mTvComplete.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //前半部分
            case R.id.rl_choose_address://选择地址
                startActivityForResult(new Intent(AddManyPeopleEventActivity.
                        this, ChooseAddressActivity.class), 6);
                break;
            //中间部分的点击事件
            case R.id.rl_start_time://开始时间
                if (isAllDay) {
                    DialogFactory.showAllDayOptionDialog(this, mTvStartTime, mTvEndTime);
                } else {
                    DialogFactory.showOptionDialog(this, mTvStartTime, mTvEndTime);
                }
                break;
            case R.id.rl_end_time://结束时间
                if (isAllDay) {
                    DialogFactory.showAllDayOptionDialog(this, mTvEndTime, mTvStartTime);
                } else {
                    DialogFactory.showOptionDialog(this, mTvEndTime, mTvStartTime);
                }
                break;


            //后半部分的点击事件
            case R.id.rl_many_people_participant://参与人
                if (mJoinFriends != null && allChooseJoinFriends != null) {
                    mJoinFriends.clear();
                    allChooseJoinFriends.clear();
                }
                startActivityForResult(new Intent(AddManyPeopleEventActivity.
                        this, ChooseSomeoneActivity.class), 7);
                break;

            case R.id.rl_many_people_submission://报送
                if (mRepFriends != null && allChooseRepFriends != null) {
                    mRepFriends.clear();
                    allChooseRepFriends.clear();
                }
                startActivityForResult(new Intent(AddManyPeopleEventActivity.
                        this, ChooseSomeoneActivity.class), 5);
                break;
            case R.id.rl_many_people_remind_before://提前提醒
                startActivityForResult(new Intent(AddManyPeopleEventActivity.
                        this, ChooseRemindBeforeActivity.class), 0);
                break;
            case R.id.rl_many_people_uploading://上传文件
                startActivityForResult(new Intent(AddManyPeopleEventActivity.
                        this, ChooseFileActivity.class), 4);
                break;
            case R.id.tv_add_event_complete://完成
//                if (TextUtils.isEmpty(mEtTheme.getText().toString())) {
//                    showTip("请输入主题");
//                    return;
//                }
//                //备注不为空
//                if (TextUtils.isEmpty(mEtManyPeopleDesc.getText().toString())) {
//                    showTip("请输入备注");
//                    return;
//                }
//                //参与人不为空
//                if (TextUtils.isEmpty(mEtTheme.getText().toString())) {
//                    showTip("请选择参与人");
//                    return;
//                }
                if (mEventID != -1) {//当该日程是传送过来的哪个的时候，删除原有的
                    CommonUtil.DeleteRepPer(mEventID);
                    CommonUtil.DeleteJoinPer(mEventID);
                }
                saveDataOfNet();
                saveDataOfDb();
                saveReporterToDb();
                saveJoinerToDb();
                break;
            case R.id.tv_add_event_cancel://取消
                finish();
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
        mProgressDialog = ProgressDialog.show(AddManyPeopleEventActivity.this, "请稍等", "正在登录中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.SAVESCHEDULE);
        //包装请求参数
        String _personEventJson = createPersonEventJson();
        Log.d("YZP", "---------------------_personEventJson = " + _personEventJson);

        requestParams.addBodyParameter("", _personEventJson);//用户名
        String loginCode = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
        if (loginCode != null)
            requestParams.addHeader("sVerifyCode", loginCode);//头信息

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("androidYZP", "---------------------result = " + result);
                if (result != null) {
                    //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        Log.d("YZP", "---------------------_success = " + _success);
                        if (!_success) {//保存失败
                            String _message = (String) _json_result.get("Message");
                            CommonUtil.sendErrorMessage(_message, handler);
                        } else {//保存成功
                            updateSycStatus();//更新同步状态
                            JSONObject datas = (JSONObject) _json_result.get("Datas");
                            int id = datas.getInt("Id");
                            updateServerID(id);//更新唯一ID
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                startActivity(new Intent(AddManyPeopleEventActivity.this,
                        CalendarActivity.class));
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                AddManyPeopleEventActivity.this.finish();
            }
        });
    }


    /**
     * 生成多人事件的json数据
     *
     * @return
     */
    private String createPersonEventJson() {
        NetScheduleModel netScheduleModel = new NetScheduleModel();
        NetScheduleModel.ViewModelBean viewModelBean = new NetScheduleModel.ViewModelBean();
        viewModelBean.setScheduleType(3);//事件类型//
        viewModelBean.setSubject(mEtTheme.getText().toString());//主题
        viewModelBean.setPlace(mTvPlace.getText().toString());//地址
        viewModelBean.setLabel(mLable);//标签
        viewModelBean.setLatitude(mLatitude + "");//维度
        viewModelBean.setLongitude(mLongitude + "");//精度
        viewModelBean.setIsOneDay(isAllDay);//是否是全日
        viewModelBean.setStartTime(mTvStartTime.getText().toString());//开始时间
        viewModelBean.setStopTime(mTvEndTime.getText().toString());//结束时间
        viewModelBean.setSendOpenFireNameList(mRepFriends);//添加报送人
        viewModelBean.setJoinOpenFireNameList(mJoinFriends);//添加参与人
//        viewModelBean.setSendOpenFireNameList(mTvEndTime.getText().toString());报送人//参与人//附件
        viewModelBean.setRemindType(mRemindTime);//提前提醒
        viewModelBean.setDescription(mEtManyPeopleDesc.getText().toString());//描述,备注
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
            schedule.setAddress(mTvPlace.getText().toString());
            schedule.setType(3);//多人事件
            schedule.setSch_status(0);//自己是发起者状态
            schedule.setTitle(mEtTheme.getText().toString());
            schedule.setLabel(mLable);
            schedule.setLatitude(mLatitude + "");
            schedule.setLongitude(mLongitude + "");
            schedule.setAhead_warn(mRemindTime);
            schedule.setSyc_status(0);
            schedule.setRemark(mEtManyPeopleDesc.getText().toString());
            schedule.setIs_one_day(isAllDay);
            schedule.setBegin_time(mTvStartTime.getText().toString());
            schedule.setEnd_time(mTvEndTime.getText().toString());
            mDbManager.saveOrUpdate(schedule);
        } catch (DbException e) {
            Log.d("yzp", e.getMessage() + "-----------");
            e.printStackTrace();
        }
    }

    /**
     * 将报送人储存本地数据库
     */
    private void saveReporterToDb() {
        try {
            for (int i = 0; i < allChooseRepFriends.size(); i++) {
                mDbManager = DBHelper.getDbManager();
                Schreport schreport = new Schreport();
                schreport.setFriendid(allChooseRepFriends.get(i).getFriendid());
                schreport.setSid(schedule.getId());
                mDbManager.saveOrUpdate(schreport);
            }
        } catch (DbException e) {
            Log.d("yzp", e.getMessage() + "-----------");
            e.printStackTrace();
        }
    }

    /**
     * 将参与人储存本地数据库
     */
    private void saveJoinerToDb() {
        try {
            for (int i = 0; i < allChooseJoinFriends.size(); i++) {
                mDbManager = DBHelper.getDbManager();
                Schjoiner schjoiner = new Schjoiner();
                schjoiner.setJoiner_id(allChooseJoinFriends.get(i).getFriendid());
                schjoiner.setSid(schedule.getId());
                mDbManager.saveOrUpdate(schjoiner);
            }
        } catch (DbException e) {
            Log.d("yzp", e.getMessage() + "-----------");
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库的同步状态
     */
    private void updateSycStatus() {
        try {
            schedule.setSyc_status(1);
            mDbManager.saveOrUpdate(schedule);
        } catch (DbException e) {
            Log.d("yzp", e.getMessage() + "-----------");
            e.printStackTrace();
        }
    }

    /**
     * 更新从服务端返回的唯一标识ID:serverID
     */
    private void updateServerID(int dateid) {
        try {
            schedule.setServerid(dateid);
            mDbManager.saveOrUpdate(schedule);
        } catch (DbException e) {
            Log.d("yzp", e.getMessage() + "-----------");
            e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 && requestCode == 0 && data != null) {
            mRemindTime = data.getIntExtra("RemindTime", 0);
            if (mRemindTime != 0)
                mTvManyPeopleRemindBefore.setText(mRemindTime + "分钟前");
        }

        //返回报送人
        if (resultCode == 5 && requestCode == 5 && data != null) {
            String chooseFriends = data.getStringExtra("ChooseFriends");
            Gson gson = new Gson();
            mRepFriends = gson.fromJson(chooseFriends,//生成报送人上传对象
                    new TypeToken<List<String>>() {
                    }.getType());


            setRepFriends(mRepFriends);
        }
        //返回参与人
        if (resultCode == 5 && requestCode == 7 && data != null) {
            String chooseFriends = data.getStringExtra("ChooseFriends");

            Gson gson = new Gson();
            mJoinFriends = gson.fromJson(chooseFriends,//生成报送人上传对象
                    new TypeToken<List<String>>() {
                    }.getType());

            setJoinFriends(mJoinFriends);
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
    }

    //设置参与人
    private void setJoinFriends(List<String> mJoinFriends) {
        for (int i = 0; i < mJoinFriends.size(); i++) {
            User _user = CommonUtil.getUserInfo(UIUtils.getContext());
            if (_user != null) {
                try {
                    mDbManager = DBHelper.getDbManager();
                    List<Friend> all = mDbManager.selector(Friend.class).where("userid", "=",
                            _user.getOpenFireUserName()).and("friendid", "=", mJoinFriends.get(i)).findAll();
                    allChooseJoinFriends.add(all.get(0));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }

        mTvJoinSubmission.setText("");
        for (int i = 0; i < allChooseJoinFriends.size(); i++) {
            String nickname = allChooseJoinFriends.get(i).getNickname();
            if (!StringUtils.isEmpty(nickname)) {
                mTvJoinSubmission.setText(mTvJoinSubmission.getText().toString() + nickname + " ");
            } else {
                mTvJoinSubmission.setText(mTvJoinSubmission.getText().toString() + allChooseJoinFriends.get(i).getFriendid() + " ");
            }
        }
    }

    //设置报送人
    private void setRepFriends(List<String> mRepFriends) {
        for (int i = 0; i < mRepFriends.size(); i++) {
            User _user = CommonUtil.getUserInfo(UIUtils.getContext());
            if (_user != null) {
                try {
                    mDbManager = DBHelper.getDbManager();
                    List<Friend> all = mDbManager.selector(Friend.class).where("userid", "=",
                            _user.getOpenFireUserName()).and("friendid", "=", mRepFriends.get(i)).findAll();
                    allChooseRepFriends.add(all.get(0));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }

        mTvSubmission.setText("");
        for (int i = 0; i < allChooseRepFriends.size(); i++) {
            String nickname = allChooseRepFriends.get(i).getNickname();
            if (!StringUtils.isEmpty(nickname)) {
                mTvSubmission.setText(mTvSubmission.getText().toString() + nickname + " ");
            } else {
                mTvSubmission.setText(mTvSubmission.getText().toString() + allChooseRepFriends.get(i).getFriendid() + " ");
            }
        }
    }

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(AddManyPeopleEventActivity activity) {
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
                    ((AddManyPeopleEventActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_GET_ADD_PERSONER_EVENT_SUCCESS:
                    //保存个人事件成功
                    break;
                case CommonConstants.FLAG_GET_ADD_PERSONER_EVENT_FAIL://保存失败后调用
                    break;
                default:
                    break;
            }
        }
    }
}
