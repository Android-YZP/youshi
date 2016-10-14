package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.NetScheduleModel;
import com.mkch.youshi.bean.NetScheduleModel.ViewModelBean.TimeSpanListBean;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.model.Schtime;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPersonalAffairActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtTheme;
    private RelativeLayout mChooseAddress;
    private RadioGroup mRgLabel;
    private RelativeLayout mAffairDate;
    private RelativeLayout mAffairWeek;
    private RelativeLayout mAffairChooseTime;
    private RelativeLayout mAffairAllTime;
    private RelativeLayout mSubmission;
    private RelativeLayout mRemindBefore;
    private LinearLayout mRemark;
    private TextView mTwoStartDate;
    private TextView mTvCancel;
    private TextView mTvComplete;
    private TextView mTvTitle;
    private TextView mTwoEndDate;
    private ProgressDialog mProgressDialog;
    private int mLable;
    private int mRemindTime;
    private TextView mTvRemindBefore;
    private EditText mEtPersonalEventDescription;
    private MyHandler handler = new MyHandler(this);
    private String mWeek = "1234567";
    private TextView mTvAffairWeek;
    private TextView mAffairTimeISChoose;
    private TextView mAffairTimeAllTime;
    private String mOneDayTotalTimeString;
    private int mTotalTimeHour;
    private int mTotalTimeMints;
    private int mAllDayChooseTimes;
    private double mLatitude;
    private List<String> mFriends;
    private List<Friend> allChooseFriends;
    private double mLongitude;


    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mEndYear;
    private static int mEndMonth;
    private static int mEndDay;
    private static int mMinute;
    private static NumberPicker mNpTwoOptionStartYear;
    private static NumberPicker mNpTwoOptionStartMonth;
    private static NumberPicker mNpTwoOptionStartDay;
    private static TextView mTvChooseComplete;
    private static TextView mTvTimeShow;
    private static Button mBtnEndTime;
    private static Button mBtnStartTime;
    public static Dialog mChooseTimeDialog;
    private static NumberPicker mNpTwoOptionEndYear;
    private static NumberPicker mNpTwoOptionEndMonth;
    private static NumberPicker mNpTwoOptionEndDay;
    private static LinearLayout mLLTwoOptionStartRootView;
    private static LinearLayout mLLTwoOptionEndRootView;
    private DbManager mDbManager;
    private Schedule schedule;
    private ArrayList<TimeSpanListBean> mTimeSpanListBeans;
    private TextView mTvPlace;
    private TextView mTvSubmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_affair);
        initView();
        initData();
        setListener();

        /**-
         * 在时间段选择界面,每选择一个时间段就判断是否有重叠的时间段.
         * 没有就储存到数据库中.
         * 计算出所有时间段的时间总和.传递给事务选择界面,
         */

        /**
         * 拿到一天的总时长,
         * 和选择的日期段,将每天计算出当天是星期几,和"周"进行比较.在"周"里面就加一天的总时长,
         * 循环,计算出所有时间的总时长
         */

    }


    private void initView() {
        mTvCancel = (TextView) findViewById(R.id.tv_add_event_cancel);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
        mTvTitle = (TextView) findViewById(R.id.tv_add_event_title);
        mEtTheme = (EditText) findViewById(R.id.et_theme);
        mChooseAddress = (RelativeLayout) findViewById(R.id.rl_choose_address);
        mRgLabel = (RadioGroup) findViewById(R.id.gr_label);
        mAffairDate = (RelativeLayout) findViewById(R.id.rl_affair_date);
        mTwoStartDate = (TextView) findViewById(R.id.tv_two_start_date);
        mTwoEndDate = (TextView) findViewById(R.id.tv_two_end_date);
        mTvPlace = (TextView) findViewById(R.id.tv_personal_event_place);
        mAffairWeek = (RelativeLayout) findViewById(R.id.rl_affair_week);
        mTvAffairWeek = (TextView) findViewById(R.id.tv_Add_Affair_week);
        mAffairChooseTime = (RelativeLayout) findViewById(R.id.rl_affair_choose_time);
        mAffairTimeISChoose = (TextView) findViewById(R.id.tv_add_affair_is_choose);
        mAffairTimeAllTime = (TextView) findViewById(R.id.tv_add_affair_all_time);
        mAffairAllTime = (RelativeLayout) findViewById(R.id.rl_affair_all_time);
        mSubmission = (RelativeLayout) findViewById(R.id.rl_submission);
        mRemindBefore = (RelativeLayout) findViewById(R.id.rl_remind_before);
        mTvRemindBefore = (TextView) findViewById(R.id.tv_remind_before);
        mTvSubmission = (TextView) findViewById(R.id.tv_submission);
        mRemark = (LinearLayout) findViewById(R.id.ll_remark);
        mEtPersonalEventDescription = (EditText) findViewById(R.id.et_add_event_description);
    }

    private void initData() {
        mTvTitle.setText("添加个人事务");
        allChooseFriends = new ArrayList<>();
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
        //日期
        mAffairDate.setOnClickListener(this);
        //周
        mAffairWeek.setOnClickListener(this);
        //时间段选择
        mAffairAllTime.setOnClickListener(this);
        //总时长
        mAffairChooseTime.setOnClickListener(this);

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
            case R.id.rl_choose_address://选择地址
                startActivityForResult(new Intent(AddPersonalAffairActivity.
                        this, ChooseAddressActivity.class), 6);
                break;
            case R.id.rl_affair_date://日期
                showTwoDayOptionDialog(this, mTwoStartDate, mTwoEndDate);
                break;
            case R.id.rl_affair_week://周
                startActivityForResult(new Intent(AddPersonalAffairActivity.this,
                        ChooseWeekActivity.class), 1);
                break;
            case R.id.rl_affair_choose_time://选择时间
                startActivityForResult(new Intent(AddPersonalAffairActivity.this,
                        ChooseTimeActivity.class), 2);

                break;
            case R.id.rl_affair_all_time://总时长
                Toast.makeText(AddPersonalAffairActivity.this, "4",
                        Toast.LENGTH_SHORT).show();
                break;

            //后半部分的点击事件
            case R.id.rl_submission://报送
                if (mFriends != null && allChooseFriends != null) {
                    mFriends.clear();
                    allChooseFriends.clear();
                }
                startActivityForResult(new Intent(AddPersonalAffairActivity.
                        this, ChooseSomeoneActivity.class), 5);
                break;
            case R.id.rl_remind_before://提前提醒
                startActivityForResult(new Intent(AddPersonalAffairActivity.
                        this, ChooseRemindBeforeActivity.class), 0);
                break;
            case R.id.tv_add_event_cancel://取消
                finish();
                break;
            case R.id.tv_add_event_complete://完成

                if (TextUtils.isEmpty(mEtTheme.getText().toString())) {
                    showTip("请输入主题");
                    return;
                }
                //备注不为空
                if (TextUtils.isEmpty(mEtPersonalEventDescription.getText().toString())) {
                    showTip("请输入备注");
                    return;
                }
                //时间段不为空
                if (mTimeSpanListBeans == null) {
                    showTip("请选择时间段");
                    return;
                }
                saveDataOfDb();
                saveReporterToDb();
                saveDataOfNet();
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
        mProgressDialog = ProgressDialog.show(AddPersonalAffairActivity.this, "请稍等", "正在保存中...", true, true);
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
                startActivity(new Intent(AddPersonalAffairActivity.this,
                        CalendarActivity.class));
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                AddPersonalAffairActivity.this.finish();
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
        viewModelBean.setScheduleType(1);//事件类型//个人事务
        viewModelBean.setSubject(mEtTheme.getText().toString());//主题
        viewModelBean.setPlace(mTvPlace.getText().toString());//地址
        viewModelBean.setLabel(mLable);//标签
        viewModelBean.setLatitude(mLatitude + "");//维度
        viewModelBean.setLongitude(mLongitude + "");//精度
        viewModelBean.setStartTime(mTwoStartDate.getText().toString());//开始时间
        viewModelBean.setStopTime(mTwoEndDate.getText().toString());//结束时间
        viewModelBean.setTimeSpanList(mTimeSpanListBeans);//时间段集合
        viewModelBean.setWeeks(replaceWeek(mWeek));//周
        viewModelBean.setTotalTime(mAffairTimeAllTime.getText().toString());//总时长
        viewModelBean.setSendOpenFireNameList(mFriends);//添加报送人
        viewModelBean.setRemindType(mRemindTime);//提前提醒
        viewModelBean.setDescription(mEtPersonalEventDescription.getText().toString());//描述,备注
        netScheduleModel.setViewModel(viewModelBean);
        Gson gson = new Gson();
        String textJson = gson.toJson(netScheduleModel);
        Log.d("createPersonJson", textJson + "-----------YZP");
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
            schedule.setAddress(mTvPlace.getText().toString());
            schedule.setType(1);//日程类型
            schedule.setTitle(mEtTheme.getText().toString());//标题,主题
            schedule.setLabel(mLable);//标签
            schedule.setLatitude(mLatitude + "");
            schedule.setLongitude(mLongitude + "");
            schedule.setAhead_warn(mRemindTime);//提前提醒
            schedule.setSyc_status(0);//同步状态
            schedule.setBegin_time(mTwoStartDate.getText().toString());
            schedule.setEnd_time(mTwoEndDate.getText().toString());
            schedule.setWhich_week(replaceWeek(mWeek));//周
            schedule.setTotal_time(mAffairTimeAllTime.getText().toString());//总时长
            //时间段
            schedule.setRemark(mEtPersonalEventDescription.getText().toString());//备注
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
        if (resultCode == 0 && requestCode == 0 && data != null) {//提前提醒
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

                for (int i = 0; i < mFriends.size(); i++) {
                    User _user = CommonUtil.getUserInfo(UIUtils.getContext());
                    if (_user != null) {
                        try {
                            mDbManager = DBHelper.getDbManager();
                            List<Friend> all = mDbManager.selector(Friend.class).where("userid", "=",
                                    _user.getOpenFireUserName()).and("friendid", "=", mFriends.get(i)).findAll();
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

        //返回地址
        if (resultCode == 6 && requestCode == 6 && data != null) {
            String address = data.getStringExtra("address");
            mLatitude = data.getDoubleExtra("latitude", 0);
            mLongitude = data.getDoubleExtra("longitude", 0);
            if (!TextUtils.isEmpty(address)) {
                mTvPlace.setText(address);
            }
        }
        if (resultCode == 1 && requestCode == 1 && data != null) {//选择周几
            mWeek = data.getStringExtra("Week");
            String _week = replaceWeek();//将1234567换成周一,周二,周三
            mTvAffairWeek.setText(_week);

            //计算有效时间天数
            mAllDayChooseTimes = chooseTotalTimes(mTwoStartDate.getText().toString(), mTwoEndDate.getText().toString());
            int _totalHour = mTotalTimeHour * mAllDayChooseTimes;
            int _totalMin = mTotalTimeMints * mAllDayChooseTimes;
            mAffairTimeAllTime.setText(_totalHour + "小时" + _totalMin + "分钟");//设置总时长
        }

        if (resultCode == 2 && requestCode == 2 && data != null) {//选择时间段
            String _timeSpanListBeanListString = data.getStringExtra("TimeSpanListBeanList");
            //一天的总时长
            mOneDayTotalTimeString = data.getStringExtra("TotalTime");
            mTotalTimeHour = data.getIntExtra("TotalTimeHour", 0);
            mTotalTimeMints = data.getIntExtra("TotalTimeMints", 0);
            Gson gson = new Gson();
            mTimeSpanListBeans = gson.fromJson(_timeSpanListBeanListString,
                    new TypeToken<List<TimeSpanListBean>>() {
                    }.getType());

            if (mTimeSpanListBeans.size() > 0) {
                mAffairTimeISChoose.setText("已选择");
            } else {
                mAffairTimeISChoose.setText("未选择");
            }

            //计算有效时间天数
            mAllDayChooseTimes = chooseTotalTimes(mTwoStartDate.getText().toString(), mTwoEndDate.getText().toString());
            int _totalHour = mTotalTimeHour * mAllDayChooseTimes;
            int _totalMin = mTotalTimeMints * mAllDayChooseTimes;
            Log.d("YZP_______", _totalHour + _totalMin + "PPP");
            mAffairTimeAllTime.setText(_totalHour + "小时" + _totalMin + "分钟");//设置总时长
        }
    }

    /**
     * 遍历二个日期之间的有效选择时间天数
     */
    private int chooseTotalTimes(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        int totlyTimes = 0;
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            List<Date> lists = CommonUtil.dateSplit(start, end);
            if (!lists.isEmpty()) {
                for (Date date : lists) {
                    String format = sdf.format(date);
                    String i = CommonUtil.dayForWeek(format) + "";
                    boolean isChoose = mWeek.contains(i);
                    if (isChoose) {
                        totlyTimes++;
                    }
                    System.out.println(totlyTimes);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return totlyTimes;
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


    /**
     * 一起选择开始时间和结束时间
     */
    public void showTwoDayOptionDialog(final Context context, final TextView StartTextView, final TextView EndTextView) {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        t.setToNow(); // 取得系统时间。
        int mCurrentYear = t.year;
        int mCurrentMonth = t.month + 1;
        int mCurrentDay = t.monthDay;
        int mCurrentHour = t.hour;
        int mCurrentMinute = t.minute;
        mYear = mCurrentYear;//初始化全局变量
        mEndYear = mCurrentYear;
        mEndMonth = mCurrentMonth;
        mMonth = mCurrentMonth;
        mDay = mCurrentDay;
        mEndDay = mCurrentDay + 1;
        mHour = mCurrentHour;
        mMinute = mCurrentMinute;

        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View _OptionView = layoutInflater.inflate(R.layout.layout_twoday_choose_time, null);
        mLLTwoOptionStartRootView = (LinearLayout) _OptionView.findViewById(R.id.ll_root_start_view);
        mLLTwoOptionEndRootView = (LinearLayout) _OptionView.findViewById(R.id.ll_root_end_view);
        //开始时间
        mNpTwoOptionStartYear = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_start_year);
        mNpTwoOptionStartMonth = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_start_month);
        mNpTwoOptionStartDay = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_start_day);

        //结束时间
        mNpTwoOptionEndYear = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_end_year);
        mNpTwoOptionEndMonth = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_end_month);
        mNpTwoOptionEndDay = (NumberPicker) _OptionView.findViewById(R.id.np_towOption_end_day);

        mTvChooseComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        mTvTimeShow = (TextView) _OptionView.findViewById(R.id.tv_dialog_time_show);

        mBtnStartTime = (Button) _OptionView.findViewById(R.id.btn_dialog_start_time);
        mBtnEndTime = (Button) _OptionView.findViewById(R.id.btn_dialog_end_time);

        /**
         *  点击开始显示开始的时间选择器.
         *  点击结束显示结束的时间选择器.
         *   值被传出的时候,判断时间的大小.
         *   结束时间小于等于开始时间,自动调整结束时间为开始时间的后一天.给出土司提示
         */


        //初始化点击事件
        mTvTimeShow.setText(DialogFactory.getWeek(mYear, mMonth, mDay, mHour, mMinute, true));//更新头标题时间

        mBtnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击开始按钮
                mBtnStartTime.setBackgroundColor(context.getResources().getColor(R.color.btn_back));
                mBtnEndTime.setBackgroundColor(context.getResources().getColor(R.color.text_white));
                mLLTwoOptionStartRootView.setVisibility(View.VISIBLE);
                mLLTwoOptionEndRootView.setVisibility(View.INVISIBLE);

                String _date = DialogFactory.getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);

            }
        });

        mBtnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击结束按钮
                mBtnEndTime.setBackgroundColor(context.getResources().getColor(R.color.btn_back));
                mBtnStartTime.setBackgroundColor(context.getResources().getColor(R.color.text_white));
                mLLTwoOptionStartRootView.setVisibility(View.INVISIBLE);
                mLLTwoOptionEndRootView.setVisibility(View.VISIBLE);

                String _date = DialogFactory.getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);//更新头标题时间


            }
        });

        DialogFactory.setNumberPickerDividerColor(context, mNpTwoOptionStartYear);//改变分割线的颜色
        DialogFactory.setNumberPickerDividerColor(context, mNpTwoOptionStartMonth);
        DialogFactory.setNumberPickerDividerColor(context, mNpTwoOptionStartDay);

        mNpTwoOptionStartYear.setMaxValue(2036);
        mNpTwoOptionStartYear.setMinValue(1999);
        mNpTwoOptionStartYear.setValue(mCurrentYear);

        mNpTwoOptionStartMonth.setMaxValue(12);
        mNpTwoOptionStartMonth.setMinValue(1);
        mNpTwoOptionStartMonth.setValue(mCurrentMonth);

        mNpTwoOptionStartDay.setMinValue(1);
        mNpTwoOptionStartDay.setMaxValue(DialogFactory.getDays(mCurrentYear, mCurrentMonth));
        mNpTwoOptionStartDay.setValue(mCurrentDay);
        //结束时间
        mNpTwoOptionEndYear.setMaxValue(2036);
        mNpTwoOptionEndYear.setMinValue(1999);
        mNpTwoOptionEndYear.setValue(mEndYear);

        mNpTwoOptionEndMonth.setMaxValue(12);
        mNpTwoOptionEndMonth.setMinValue(1);
        mNpTwoOptionEndMonth.setValue(mEndMonth);

        mNpTwoOptionEndDay.setMinValue(1);
        mNpTwoOptionEndDay.setMaxValue(DialogFactory.getDays(mCurrentYear, mCurrentMonth));
        mNpTwoOptionEndDay.setValue(mEndDay);


        //完成点击事件
        mTvChooseComplete.setOnClickListener(new View.OnClickListener() {

            private Date parseEndTime;
            private Date parseSrtartTime;

            @Override
            public void onClick(View v) {
                mChooseTimeDialog.dismiss();
                //开始时间应小于结束时间
                SimpleDateFormat _s_d_f = new SimpleDateFormat("yyyy年M月d日");
                try {
                    parseSrtartTime = _s_d_f.parse(mYear + "年" + mMonth + "月" + mDay + "日");
                    parseEndTime = _s_d_f.parse(mEndYear + "年" + mEndMonth + "月" + mEndDay + "日");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int i = parseSrtartTime.compareTo(parseEndTime);
                if (i == -1) {
                    StartTextView.setText(mYear + "年" + mMonth + "月" + mDay + "日");
                    EndTextView.setText(mEndYear + "年" + mEndMonth + "月" + mEndDay + "日");
                } else {
                    StartTextView.setText(mYear + "年" + mMonth + "月" + mDay + "日");
                    EndTextView.setText(mYear + "年" + mMonth + "月" + mDay + "日");
                    Toast.makeText(context, "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
                }


                //计算有效时间天数
                mAllDayChooseTimes = chooseTotalTimes(mTwoStartDate.getText().toString(), mTwoEndDate.getText().toString());
                int _totalHour = mTotalTimeHour * mAllDayChooseTimes;
                int _totalMin = mTotalTimeMints * mAllDayChooseTimes;
                Log.d("YZP_______", _totalHour + _totalMin + "PPP");
                mAffairTimeAllTime.setText(_totalHour + "小时" + _totalMin + "分钟");//设置总时长
            }
        });
        //选择事件监听
        //年
        mNpTwoOptionStartYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = DialogFactory.getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = DialogFactory.getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);

            }
        });
        //月
        mNpTwoOptionStartMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = DialogFactory.getDays(mYear, mMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = DialogFactory.getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);//更新头标题时间
            }
        });
        //日
        mNpTwoOptionStartDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDay = newVal;
                String _date = DialogFactory.getWeek(mYear, mMonth, mDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date); //更新头标题时间
            }
        });


        //结束年
        mNpTwoOptionEndYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndYear = newVal;
                int maxMonth = DialogFactory.getDays(mEndYear, mEndMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = DialogFactory.getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);

            }
        });
        //结束月
        mNpTwoOptionEndMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndMonth = newVal;
                int maxMonth = DialogFactory.getDays(mEndYear, mEndMonth);
                mNpTwoOptionStartDay.setMaxValue(maxMonth);
                String _date = DialogFactory.getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date);//更新头标题时间
            }
        });
        //结束日
        mNpTwoOptionEndDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mEndDay = newVal;
                String _date = DialogFactory.getWeek(mEndYear, mEndMonth, mEndDay, mHour, mMinute, true);
                mTvTimeShow.setText(_date); //更新头标题时间
            }
        });


        mChooseTimeDialog = new AlertDialog.Builder(context).
                setView(_OptionView).
                create();
        Window window = mChooseTimeDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialog_style);  //添加动画
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        mChooseTimeDialog.show();
    }


    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(AddPersonalAffairActivity activity) {
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
                    ((AddPersonalAffairActivity) mActivity.get()).showTip(errorMsg);
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
