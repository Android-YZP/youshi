package com.mkch.youshi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.ManyPeopleEventDetial;
import com.mkch.youshi.bean.ManyPeopleEvenBean;
import com.mkch.youshi.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/9/6.
 */
public class ManyPeopleCaledarFragment extends Fragment {

    private ListView mLvManyPeopleCalendar;
    private ArrayList<ManyPeopleEvenBean> mManyPeopleEvenBeens;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.many_people_caledar_fragment, container, false);

        initView(view);
        initData();
        initListener();
        return view;


    }

    private void initView(View view) {
        mLvManyPeopleCalendar = (ListView) view.findViewById(R.id.lv_many_people_caledar);
    }

    private void initListener() {
        mLvManyPeopleCalendar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("_gson_str", "_gson_str2"+position);
                return true;
            }
        });
        mLvManyPeopleCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("_gson_str", "_gson_str"+position);
                Gson gson = new Gson();
                ManyPeopleEvenBean manyPeopleEvenBean = mManyPeopleEvenBeens.get(position);
                Intent i = new Intent(getActivity(),
                       ManyPeopleEventDetial.class);
                String _gson_str = gson.toJson(manyPeopleEvenBean);//传一个数组的数据到另外一个界面
                Log.d("_gson_str", _gson_str);
                i.putExtra("mgonsn", _gson_str);
                startActivity(i);
            }
        });
    }


    private void initData() {
        mManyPeopleEvenBeens = new ArrayList<>();
        testData1();
        testData1();
        testData1();
        testData2();
        testData2();
        testData2();
        testData3();
        testData3();
        testData3();
        testData4();
        testData4();
        testData4();
        testData4();
        testData5();
        testData5();
        testData5();
        testData5();
        mLvManyPeopleCalendar.setAdapter(new MyAdapter());
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mManyPeopleEvenBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return mManyPeopleEvenBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = UIUtils.inflate(R.layout.item_list_many_people_calendar);
            TextView _manyPeopleTheme = (TextView) view.findViewById(R.id.tv_lv_many_people_theme);
            TextView _manyPeopleStopTime = (TextView) view.findViewById(R.id.tv_lv_many_people_time_stop);
            TextView _manyPeopleSponsor = (TextView) view.findViewById(R.id.tv_lv_many_people_sponsor);
            final TextView _TvManyPeopleAccept = (TextView) view.findViewById(R.id.tv_lv_many_people_accept);
            final TextView _TvManyPeopleRefuse = (TextView) view.findViewById(R.id.tv_lv_many_people_refuse);
            TextView _manyPeopleCreationTime = (TextView) view.findViewById(R.id.tv_lv_many_people_creation_time);
            final TextView _BtnManyPeopleAccept = (TextView) view.findViewById(R.id.btn_lv_many_people_accept);
            final TextView _BtnManyPeopleRefuse = (TextView) view.findViewById(R.id.btn_lv_many_people_refuse);

            //接受的点击事件
            _BtnManyPeopleAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _BtnManyPeopleAccept.setVisibility(View.GONE);
                    _BtnManyPeopleRefuse.setVisibility(View.GONE);
                    _TvManyPeopleAccept.setVisibility(View.VISIBLE);
                }
            });
            //拒绝的点击事件
            _BtnManyPeopleRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _BtnManyPeopleAccept.setVisibility(View.GONE);
                    _BtnManyPeopleRefuse.setVisibility(View.GONE);
                    _TvManyPeopleRefuse.setVisibility(View.VISIBLE);
                }
            });

            int state = mManyPeopleEvenBeens.get(position).getState();
            String theme = mManyPeopleEvenBeens.get(position).getTheme();
            String stopTime = mManyPeopleEvenBeens.get(position).getStopTime();
            String sponsor = mManyPeopleEvenBeens.get(position).getSponsor();
            String creationTime = mManyPeopleEvenBeens.get(position).getCreationTime();
            _manyPeopleStopTime.setText(stopTime + "截止");
            _manyPeopleTheme.setText(theme);
            _manyPeopleCreationTime.setText(creationTime);
            if (state == ManyPeopleEvenBean.MANY_PEOPLE_SPONSOR) {
                _BtnManyPeopleAccept.setVisibility(View.GONE);
                _BtnManyPeopleRefuse.setVisibility(View.GONE);
                _TvManyPeopleAccept.setVisibility(View.GONE);
                _TvManyPeopleRefuse.setVisibility(View.GONE);
                _manyPeopleSponsor.setText("(发起者:我)");
            } else if (state == ManyPeopleEvenBean.MANY_PEOPLE_CHOOSE) {
                _BtnManyPeopleAccept.setVisibility(View.VISIBLE);
                _BtnManyPeopleRefuse.setVisibility(View.VISIBLE);
                _TvManyPeopleAccept.setVisibility(View.GONE);
                _TvManyPeopleRefuse.setVisibility(View.GONE);
                _manyPeopleSponsor.setText("(发起者:" + sponsor + ")");
            } else if (state == ManyPeopleEvenBean.MANY_PEOPLE_ACCEPT) {
                _BtnManyPeopleAccept.setVisibility(View.GONE);
                _BtnManyPeopleRefuse.setVisibility(View.GONE);
                _TvManyPeopleAccept.setVisibility(View.VISIBLE);
                _TvManyPeopleRefuse.setVisibility(View.GONE);
                _manyPeopleSponsor.setVisibility(View.GONE);
            } else if (state == ManyPeopleEvenBean.MANY_PEOPLE_REFUSE) {
                _BtnManyPeopleAccept.setVisibility(View.GONE);
                _BtnManyPeopleRefuse.setVisibility(View.GONE);
                _TvManyPeopleAccept.setVisibility(View.GONE);
                _manyPeopleSponsor.setVisibility(View.GONE);
                _TvManyPeopleRefuse.setVisibility(View.VISIBLE);
            }

            return view;
        }
    }


    private void testData1() {
        ManyPeopleEvenBean manyPeopleEvenBean = new ManyPeopleEvenBean();
        manyPeopleEvenBean.setState(ManyPeopleEvenBean.MANY_PEOPLE_SPONSOR);
        manyPeopleEvenBean.setTheme("买生日蛋糕");
        manyPeopleEvenBean.setCreationTime("2016年8月5日");
        manyPeopleEvenBean.setStopTime("8月10日");
        manyPeopleEvenBean.setSponsor("自己");
        mManyPeopleEvenBeens.add(manyPeopleEvenBean);
    }

    private void testData2() {
        ManyPeopleEvenBean manyPeopleEvenBean = new ManyPeopleEvenBean();
        manyPeopleEvenBean.setState(ManyPeopleEvenBean.MANY_PEOPLE_CHOOSE);
        manyPeopleEvenBean.setTheme("学游泳");
        manyPeopleEvenBean.setCreationTime("2016年8月5日");
        manyPeopleEvenBean.setStopTime("8月10日");
        manyPeopleEvenBean.setSponsor("小雨");
        mManyPeopleEvenBeens.add(manyPeopleEvenBean);
    }

    private void testData3() {
        ManyPeopleEvenBean manyPeopleEvenBean = new ManyPeopleEvenBean();
        manyPeopleEvenBean.setState(ManyPeopleEvenBean.MANY_PEOPLE_ACCEPT);
        manyPeopleEvenBean.setTheme("国内游");
        manyPeopleEvenBean.setCreationTime("2016年8月5日");
        manyPeopleEvenBean.setStopTime("8月10日");
        manyPeopleEvenBean.setSponsor("小雨");
        mManyPeopleEvenBeens.add(manyPeopleEvenBean);
    }


    private void testData4() {
        ManyPeopleEvenBean manyPeopleEvenBean = new ManyPeopleEvenBean();
        manyPeopleEvenBean.setState(ManyPeopleEvenBean.MANY_PEOPLE_REFUSE);
        manyPeopleEvenBean.setTheme("黄山爬山");
        manyPeopleEvenBean.setCreationTime("2016年8月5日");
        manyPeopleEvenBean.setStopTime("8月10日");
        manyPeopleEvenBean.setSponsor("小雨");
        mManyPeopleEvenBeens.add(manyPeopleEvenBean);
    }

    private void testData5() {
        ManyPeopleEvenBean manyPeopleEvenBean = new ManyPeopleEvenBean();
        manyPeopleEvenBean.setState(ManyPeopleEvenBean.MANY_PEOPLE_ACCEPT);
        manyPeopleEvenBean.setTheme("日本洗桑拿");
        manyPeopleEvenBean.setCreationTime("2016年8月5日");
        manyPeopleEvenBean.setStopTime("2016年8月10日");
        manyPeopleEvenBean.setSponsor("小雨");
        manyPeopleEvenBean.setLocation("宜兴");
        manyPeopleEvenBean.setComplete(true);
        manyPeopleEvenBean.setLabel("个人");
        manyPeopleEvenBean.setSubmission("小夏");
        manyPeopleEvenBean.setParticipants("老总");
        mManyPeopleEvenBeens.add(manyPeopleEvenBean);
    }
}
