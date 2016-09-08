package com.mkch.youshi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.PersonalDetialEventActivity;
import com.mkch.youshi.activity.PersonalDetialHabitActivity;
import com.mkch.youshi.activity.PersonalDetialsAffairActivity;
import com.mkch.youshi.bean.PersonalEventBean;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.MyProgress;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/9/6.
 */
public class PersonalCaledarFragment extends Fragment {

    private ListView mLvPersonCalendar;
    private ArrayList<PersonalEventBean> mEventBeens;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.personal_caledar_fragment, container, false);
        initView(view);
        initData();
        initListener();
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


    private void initListener() {
        mLvPersonCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int kind = mEventBeens.get(position).getKind();
                Intent intent = null;
                if (kind == PersonalEventBean.PERSONAL_EVENT) {
                     intent = new Intent(UIUtils.getContext(),
                            PersonalDetialEventActivity.class);
                } else if (kind == PersonalEventBean.PERSONAL_AFFAIR) {
                     intent = new Intent(UIUtils.getContext(),
                            PersonalDetialsAffairActivity.class);
                } else if (kind == PersonalEventBean.PERSONAL_HABIT) {
                     intent = new Intent(UIUtils.getContext(),
                            PersonalDetialHabitActivity.class);
                }
                if (intent!=null){
                    startActivity(intent);
                }
            }
        });
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
            if (kind == PersonalEventBean.PERSONAL_EVENT) {
                _tvTimeStop.setVisibility(View.VISIBLE);
                _pbProgress.setVisibility(View.INVISIBLE);
                _mbProgress.setVisibility(View.INVISIBLE);
                _tvProgress.setVisibility(View.INVISIBLE);
                _tvTimeAndStopTime.setVisibility(View.INVISIBLE);

                //设置进度显示
                _tvTimeStop.setText(mEventBeens.get(position).getEndTime() + "截止");
            } else if (kind == PersonalEventBean.PERSONAL_AFFAIR) {
                _mbProgress.setVisibility(View.GONE);
                _tvTimeStop.setVisibility(View.GONE);
                _pbProgress.setVisibility(View.VISIBLE);
                _tvProgress.setVisibility(View.VISIBLE);
                _tvTimeAndStopTime.setVisibility(View.VISIBLE);

                //设置进度显示
                int progress = mEventBeens.get(position).getProgress();
                _pbProgress.setSecondaryProgress(progress);
                _pbProgress.setProgress(progress - 3);
                _tvProgress.setText(progress + "%");
                _tvTimeAndStopTime.setText(mEventBeens.get(position).getEndTime() + "截止" + " 时长" +
                        mEventBeens.get(position).getTime() + "小时");

            } else if (kind == PersonalEventBean.PERSONAL_HABIT) {

                _tvTimeAndStopTime.setVisibility(View.VISIBLE);
                _pbProgress.setVisibility(View.GONE);
                _tvProgress.setVisibility(View.GONE);

                int times = mEventBeens.get(position).getTimes();
                if (times == 7) {
                    _mbProgress.setVisibility(View.GONE);
                    _tvTimeStop.setVisibility(View.VISIBLE);
                    _tvTimeStop.setText(mEventBeens.get(position).getCompleteTimes() + "/" + times);
                } else {
                    _mbProgress.setVisibility(View.VISIBLE);
                    _tvTimeStop.setVisibility(View.GONE);
                    _mbProgress.setNumber(times);
                    _mbProgress.setCompleteNumber(mEventBeens.get(position).getCompleteTimes());
                }
                _tvTimeAndStopTime.setText("一周" + times + "次");
            }
            return view;
        }
    }


    private void addTestData1() {
        PersonalEventBean personalEventBean = new PersonalEventBean();
        personalEventBean.setKind(PersonalEventBean.PERSONAL_EVENT);
        personalEventBean.setTheme("修电脑");
        personalEventBean.setEndTime("8月10日");
        personalEventBean.setComplete(false);
        mEventBeens.add(personalEventBean);
    }

    private void addTestData2() {
        PersonalEventBean personalEventBean = new PersonalEventBean();
        personalEventBean.setKind(PersonalEventBean.PERSONAL_AFFAIR);
        personalEventBean.setTheme("修电脑");
        personalEventBean.setProgress(65);
        personalEventBean.setEndTime("8月17日");
        personalEventBean.setTime("8");
        personalEventBean.setComplete(true);
        mEventBeens.add(personalEventBean);
    }

    private void addTestData3() {
        PersonalEventBean personalEventBean = new PersonalEventBean();
        personalEventBean.setKind(PersonalEventBean.PERSONAL_HABIT);
        personalEventBean.setTheme("修电脑");
        personalEventBean.setComplete(true);
        personalEventBean.setTimes(6);
        personalEventBean.setCompleteTimes(3);
        mEventBeens.add(personalEventBean);
    }

    private void addTestData4() {
        PersonalEventBean personalEventBean = new PersonalEventBean();
        personalEventBean.setKind(PersonalEventBean.PERSONAL_HABIT);
        personalEventBean.setTheme("晨跑");
        personalEventBean.setComplete(false);
        personalEventBean.setTimes(7);
        personalEventBean.setCompleteTimes(2);
        mEventBeens.add(personalEventBean);
    }

    private void addTestData5() {
        PersonalEventBean personalEventBean = new PersonalEventBean();
        personalEventBean.setKind(PersonalEventBean.PERSONAL_HABIT);
        personalEventBean.setTheme("唱歌");
        personalEventBean.setComplete(false);
        personalEventBean.setTimes(3);
        personalEventBean.setCompleteTimes(1);
        mEventBeens.add(personalEventBean);
    }
}
