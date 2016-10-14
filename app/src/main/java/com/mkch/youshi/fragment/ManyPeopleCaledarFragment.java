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
import com.mkch.youshi.adapter.ManPeopleCalAdapter;
import com.mkch.youshi.bean.ManyPeopleEvenBean;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.ManyPeopleItemView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/9/6.
 * 将个人日程,多人日程的列表完成,详情界面完善
 * 添加事件必要的接口以及数据字段完善.
 */
public class ManyPeopleCaledarFragment extends Fragment {

    private ListView mLvManyPeopleCalendar;
    private ArrayList<ManyPeopleEvenBean> mManyPeopleEvenBeens;
    private ArrayList<Schedule> mSchedules;

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
                Log.d("_gson_str", "_gson_str2" + position);
                return true;
            }
        });

        mLvManyPeopleCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Gson gson = new Gson();
                Schedule schedule = mSchedules.get(position);
                Intent i = new Intent(getActivity(),
                        ManyPeopleEventDetial.class);
                String _gson_str = gson.toJson(schedule);//传一个数组的数据到详情界面
                i.putExtra("mgonsn", _gson_str);
                startActivity(i);
            }
        });
    }

    /**
     * 从数据库中查找出所有的多人事件
     *
     * @return
     */
    private ArrayList<Schedule> initPerData() {
        DbManager mDbManager = DBHelper.getDbManager();
        try {
            ArrayList<Schedule> all = (ArrayList<Schedule>) mDbManager.selector(Schedule.class).where("type", "=",
                    3).findAll();
            Log.d("yzp", all.size() + "haha");
            return all;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initData() {
        mManyPeopleEvenBeens = new ArrayList<>();
        mSchedules = initPerData();
        if (mSchedules != null) {
            mLvManyPeopleCalendar.setAdapter(new ManPeopleCalAdapter(mSchedules) {
                //点击接受之后上传网络
                @Override
                public void accept2Net() {
                    UIUtils.showTip("接受,上传了网络");
                    //如果网络上传失败,则重新从数据库查找数据,刷新界面
                    //上传成功则更新数据库数据,同时将这个事件上传网络.
                }

                //点击拒绝之后上传网络
                @Override
                public void refuse2Net() {
                    UIUtils.showTip("拒绝,上传了网络");
                }
            });
        }
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

}
