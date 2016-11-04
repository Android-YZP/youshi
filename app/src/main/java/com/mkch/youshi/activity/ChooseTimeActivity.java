package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.NetScheduleModel;
import com.mkch.youshi.model.SchEveDay;
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DialogFactory;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.AddTimeListView;
import com.mkch.youshi.view.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.mkch.youshi.bean.NetScheduleModel.*;

public class ChooseTimeActivity extends AppCompatActivity {

    private AddTimeListView mLvChooseTime;
    private TextView mTvComplete;
    private ArrayList<ViewModelBean.TimeSpanListBean> mTimeSpanListBeans;
    private chooseTimeAdapter mChooseTimeAdapter;
    private int hours;
    private int minutes;
    private int mEventID;
    private AlertDialog mChooseTimeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mLvChooseTime = (AddTimeListView) findViewById(R.id.lv_choose_time);
        mTvComplete = (TextView) findViewById(R.id.tv_add_event_complete);
    }

    private void initData() {
        mTvComplete.setText("完成");
        mTimeSpanListBeans = new ArrayList<>();
        mChooseTimeAdapter = new chooseTimeAdapter();

//        Intent intent = getIntent();
//        mEventID = intent.getIntExtra("eventID", -1);
//        if (mEventID != -1) {
//            ArrayList<Schtime> schTimes = CommonUtil.findSchTime(mEventID);
//            for (int i = 0; i < schTimes.size(); i++) {
//                ViewModelBean.TimeSpanListBean timeSpanListBean = new ViewModelBean.TimeSpanListBean();
//                timeSpanListBean.setStartTime(schTimes.get(i).getBegin_time());
//                timeSpanListBean.setEndTime(schTimes.get(i).getEnd_time());
//                timeSpanListBean.setRemindTime(schTimes.get(i).getWarning_time());
//                timeSpanListBean.setTdate(schTimes.get(i).getDate());
//                mTimeSpanListBeans.add(timeSpanListBean);
//            }
//        }

        mLvChooseTime.setAdapter(mChooseTimeAdapter);

    }

    private void setListener() {
        mTvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成,传送数据到启动界面
                sendResult();
            }
        });

        mLvChooseTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mLvChooseTime.getLastVisiblePosition()) {
//                    DialogFactory.showTimeBucketOptionDialog(ChooseTimeActivity.this, mTimeSpanListBeans, mChooseTimeAdapter);
                    ChooseDialog(mTimeSpanListBeans, mChooseTimeAdapter);
                }
            }
        });
    }

    /**
     * 个人事务的对话框
     */
    private void ChooseDialog(final ArrayList<NetScheduleModel.ViewModelBean.TimeSpanListBean> TimeSpanListBeans, final BaseAdapter adapter) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View _OptionView = layoutInflater.inflate(R.layout.person_affair_span_time_dialog, null);
        final TimePickerView startTimePV = (TimePickerView) _OptionView.findViewById(R.id.tpv_start_choose_day);
        final TimePickerView endTimePV = (TimePickerView) _OptionView.findViewById(R.id.tpv_end_choose_day);
        final TextView tvComplete = (TextView) _OptionView.findViewById(R.id.tv_dialog_choose_complete);
        final TextView tvStartTimeShow = (TextView) _OptionView.findViewById(R.id.tv_start_time_show);
        final TextView tvEndTimeShow = (TextView) _OptionView.findViewById(R.id.tv_end_time_show);

        startTimePV.setSpanTime(true);
        endTimePV.setSpanTime(true);


        startTimePV.setOnItemSelectedListener(new TimePickerView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String date) {
                tvStartTimeShow.setText(date);
            }
        });
        endTimePV.setOnItemSelectedListener(new TimePickerView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(String date) {
                tvEndTimeShow.setText(date);
            }
        });

        //完成的点击事件
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断选择的时间与结束时间的大小
                int i = TimesUtils.compareHourmin(tvStartTimeShow.getText().toString(),
                        tvEndTimeShow.getText().toString());
                if (i < 0) {//开始时间小于结束时间zhengque
                    //添加之前做判断
                    if (TimeSpanListBeans != null && TimeSpanListBeans.size() > 0) {
                        for (int j = 0; j < TimeSpanListBeans.size(); j++) {
                            String chooseStartTime = tvStartTimeShow.getText().toString();
                            String chooseEndtTime = tvEndTimeShow.getText().toString();
                            int start = TimesUtils.compareHourmin(chooseStartTime,//选择的时间和已选择的结束时间
                                    TimeSpanListBeans.get(j).getEndTime());//>===1
                            int end = TimesUtils.compareHourmin(chooseEndtTime,//选择的时间和已选择的结束时间
                                    TimeSpanListBeans.get(j).getStartTime());

                            //保持选择的开始时间>已选择的结束时间
                            //选择的结束时间<已选择的开始时间
                            if (start > 0 || end < 0) {

                            } else {
                                UIUtils.showTip("与已选择时间产生冲突");
                                mChooseTimeDialog.dismiss();
                                return;
                            }
                        }
                    }

                    ViewModelBean.TimeSpanListBean timeSpanListBean = new ViewModelBean.TimeSpanListBean();
                    timeSpanListBean.setStartTime(tvStartTimeShow.getText().toString());
                    timeSpanListBean.setEndTime(tvEndTimeShow.getText().toString());

                    TimeSpanListBeans.add(timeSpanListBean);//添加一个时间段的对象到集合中

                    Collections.sort(TimeSpanListBeans, new Comparator<NetScheduleModel.ViewModelBean.TimeSpanListBean>() {
                        @Override
                        public int compare(NetScheduleModel.ViewModelBean.TimeSpanListBean lhs, NetScheduleModel.ViewModelBean.TimeSpanListBean rhs) {
                            String _datetime_1 = lhs.getStartTime();
                            String _datetime_2 = rhs.getStartTime();
                            SimpleDateFormat _sdf = new SimpleDateFormat("HH:mm");
                            Date _date_1 = null;
                            Date _date_2 = null;
                            try {
                                _date_1 = _sdf.parse(_datetime_1);
                                _date_2 = _sdf.parse(_datetime_2);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return _date_1.compareTo(_date_2);
                        }
                    });

                    adapter.notifyDataSetChanged();//刷新Adapter

                } else {//开始时间>结束时间//将结束时间设置成和开始时间一样
                    UIUtils.showTip("开始时间应小于结束时间");
                }
                mChooseTimeDialog.dismiss();
            }

        });

        mChooseTimeDialog = new AlertDialog.Builder(this, R.style.style_dialog).
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


    /**
     * 添加到每日日程中.
     * 遍历二个日程中的所有日程,判断每个日期的星期是不是在选择中,
     * 在则将今天的日期以及所选的每个时间段都添加到数据库,
     * 每个时间段都作为一个日期添加到数据库中.
     */


    //返回数据到启动界面
    public void sendResult() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        String _listString = gson.toJson(mTimeSpanListBeans);
        String _totalTime = TimesUtils.totalTime(mTimeSpanListBeans);
        int[] timeAndhour = TimesUtils.totalTimeAndhour(mTimeSpanListBeans);
        intent.putExtra("TimeSpanListBeanList", _listString);
        intent.putExtra("TotalTime", _totalTime);
        intent.putExtra("TotalTimeHour", timeAndhour[0]);
        intent.putExtra("TotalTimeMints", timeAndhour[1]);
        ChooseTimeActivity.this.setResult(2, intent);
        ChooseTimeActivity.this.finish();
    }


    class chooseTimeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTimeSpanListBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mTimeSpanListBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            view = View.inflate(ChooseTimeActivity.this, R.layout.item_list_choose_time_layout, null);
            TextView _tvStartTime = (TextView) view.findViewById(R.id.tv_choose_time_start_time);
            TextView _tvEndTime = (TextView) view.findViewById(R.id.tv_choose_time_end_time);
            _tvStartTime.setText(mTimeSpanListBeans.get(position).getStartTime());
            _tvEndTime.setText(mTimeSpanListBeans.get(position).getEndTime());
            return view;
        }
    }
}
