package com.mkch.youshi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.EventBean;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.MyProgress;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/9/6.
 */
public class PersonalCaledarFragment extends Fragment {

    private ListView mLvPersonCalendar;
    private ArrayList<EventBean> mEventBeens;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.personal_caledar_fragment, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        mEventBeens = new ArrayList<>();
        addTestData1();
        addTestData2();
        addTestData2();
        addTestData2();
        addTestData3();
        addTestData3();
        addTestData3();
        addTestData4();
        addTestData4();
        addTestData4();
        addTestData5();
        addTestData5();
        addTestData5();
        mLvPersonCalendar.setAdapter(new MyAdapter());
    }


    private void initView(View view) {
        mLvPersonCalendar = (ListView) view.findViewById(R.id.lv_personal_caledar);
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mEventBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return mEventBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = UIUtils.inflate(R.layout.item_list_personal_calendar);
            CheckBox _isComplete = (CheckBox) view.findViewById(R.id.cb_complete);
            TextView _tvTheme = (TextView) view.findViewById(R.id.tv_lv_theme);
            ProgressBar _pbProgress = (ProgressBar) view.findViewById(R.id.pb_progress);
            MyProgress _mbProgress = (MyProgress) view.findViewById(R.id.mp_progress);
            TextView _tvTimeStop = (TextView) view.findViewById(R.id.tv_time_stop);
            TextView _tvProgress = (TextView) view.findViewById(R.id.tv_progress);
            TextView _tvTimeAndStopTime = (TextView) view.findViewById(R.id.tv_time_and_stop_time);

            Boolean isComplete = mEventBeens.get(position).getComplete();//是否已经完成
            String theme = mEventBeens.get(position).getTheme();//主题
            int kind = mEventBeens.get(position).getKind();//得到事件的种类

            _isComplete.setChecked(isComplete);//设置是否完成
            _tvTheme.setText(theme);//设置主题

            //判断是何种类型,进行不同的显示
            if (kind == EventBean.PERSONAL_EVENT){
                _tvTimeStop.setVisibility(View.VISIBLE);
                _pbProgress.setVisibility(View.INVISIBLE);
                _mbProgress.setVisibility(View.INVISIBLE);
                _tvProgress.setVisibility(View.INVISIBLE);
                _tvTimeAndStopTime.setVisibility(View.INVISIBLE);

                //设置进度显示
                _tvTimeStop.setText(mEventBeens.get(position).getEndTime()+"截止");
            }else if (kind == EventBean.PERSONAL_AFFAIR){
                _mbProgress.setVisibility(View.GONE);
                _tvTimeStop.setVisibility(View.GONE);
                _pbProgress.setVisibility(View.VISIBLE);
                _tvProgress.setVisibility(View.VISIBLE);
                _tvTimeAndStopTime.setVisibility(View.VISIBLE);

                //设置进度显示
                int progress = mEventBeens.get(position).getProgress();
                _pbProgress.setSecondaryProgress(progress);
                _pbProgress.setProgress(progress-3);
                _tvProgress.setText(progress + "%");
                _tvTimeAndStopTime.setText(mEventBeens.get(position).getEndTime() + "截止"+" 时长"+
                        mEventBeens.get(position).getTime()+"小时");

            }else if (kind == EventBean.PERSONAL_HABIT){

                _tvTimeAndStopTime.setVisibility(View.VISIBLE);
                _pbProgress.setVisibility(View.GONE);
                _tvProgress.setVisibility(View.GONE);

                int times = mEventBeens.get(position).getTimes();
                if (times ==7){
                    _mbProgress.setVisibility(View.GONE);
                    _tvTimeStop.setVisibility(View.VISIBLE);
                    _tvTimeStop.setText(mEventBeens.get(position).getCompleteTimes()+"/"+times);
                }else {
                    _mbProgress.setVisibility(View.VISIBLE);
                    _tvTimeStop.setVisibility(View.GONE);
                    _mbProgress.setNumber(times);
                    _mbProgress.setCompleteNumber(mEventBeens.get(position).getCompleteTimes());
                }
                _tvTimeAndStopTime.setText("一周"+times+"次");
            }
            return view;
        }
    }


    private void addTestData1() {
        EventBean eventBean = new EventBean();
        eventBean.setKind(EventBean.PERSONAL_EVENT);
        eventBean.setTheme("修电脑");
        eventBean.setEndTime("8月10日");
        eventBean.setComplete(false);
        mEventBeens.add(eventBean);
    }

    private void addTestData2() {
        EventBean eventBean = new EventBean();
        eventBean.setKind(EventBean.PERSONAL_AFFAIR);
        eventBean.setTheme("修电脑");
        eventBean.setProgress(65);
        eventBean.setEndTime("8月17日");
        eventBean.setTime("8");
        eventBean.setComplete(true);
        mEventBeens.add(eventBean);
    }

    private void addTestData3() {
        EventBean eventBean = new EventBean();
        eventBean.setKind(EventBean.PERSONAL_HABIT);
        eventBean.setTheme("修电脑");
        eventBean.setComplete(true);
        eventBean.setTimes(6);
        eventBean.setCompleteTimes(3);
        mEventBeens.add(eventBean);
    }

    private void addTestData4() {
        EventBean eventBean = new EventBean();
        eventBean.setKind(EventBean.PERSONAL_HABIT);
        eventBean.setTheme("晨跑");
        eventBean.setComplete(false);
        eventBean.setTimes(7);
        eventBean.setCompleteTimes(2);
        mEventBeens.add(eventBean);
    }

    private void addTestData5() {
        EventBean eventBean = new EventBean();
        eventBean.setKind(EventBean.PERSONAL_HABIT);
        eventBean.setTheme("唱歌");
        eventBean.setComplete(false);
        eventBean.setTimes(3);
        eventBean.setCompleteTimes(1);
        mEventBeens.add(eventBean);
    }
}
