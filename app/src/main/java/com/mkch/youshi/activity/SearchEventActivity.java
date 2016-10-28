package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.ManyPeopleItemView;
import com.mkch.youshi.view.PersonalItemView;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import java.util.ArrayList;
import java.util.List;


public class SearchEventActivity extends AppCompatActivity {

    private EditText mEtSearchTxt;
    private TextView mTcCancel;
    private ListView mLvSearchSch;
    private List<Schedule> mSchList = new ArrayList<>();
    private MySchAdapter mSchAdapter;
    public final  int PERSONAL_EVENT = 0;
    public final  int PERSONAL_AFFAIR = 1;
    public final  int PERSONAL_HABIT = 2;
    public final  int MANY_PEOPLE_EVENT = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_event);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mEtSearchTxt = (EditText) findViewById(R.id.et_search_text);
        mTcCancel = (TextView) findViewById(R.id.tv_search_cancel);
        mLvSearchSch = (ListView) findViewById(R.id.lv_search_sch);
    }

    private void initData() {
        mSchAdapter = new MySchAdapter();
        mLvSearchSch.setAdapter(mSchAdapter);
    }

    private void setListener() {
        //取消
        mTcCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLvSearchSch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int kind = mSchList.get(position).getType();
                Intent intent = null;
                if (kind == PERSONAL_EVENT) {
                    intent = new Intent(UIUtils.getContext(),
                            PersonalDetialEventActivity.class);
                } else if (kind == PERSONAL_AFFAIR) {
                    intent = new Intent(UIUtils.getContext(),
                            PersonalDetialsAffairActivity.class);
                } else if (kind == PERSONAL_HABIT) {
                    intent = new Intent(UIUtils.getContext(),
                            PersonalDetialHabitActivity.class);
                }else if (kind == MANY_PEOPLE_EVENT) {
                    intent = new Intent(UIUtils.getContext(),
                            ManyPeopleEventDetial.class);
                }
                if (intent!=null){
                    Gson gson = new Gson();
                    Schedule schedule = mSchList.get(position);
                    int serverid = mSchList.get(position).getServerid();
                    int mId = mSchList.get(position).getId();
                    String _gson_str = gson.toJson(schedule);//传一个数组的数据到详情界面
                    intent.putExtra("mgonsn", _gson_str);
                    intent.putExtra("Sid", serverid);
                    intent.putExtra("id", mId);
                    startActivity(intent);
                }

            }
        });


        //搜索框的文本改变监听
        mEtSearchTxt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //文本在变化是调用
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findSchFormDb(s + "");//模糊查询查找数据库的数据
                mSchAdapter.notifyDataSetChanged();
            }

            //文本变化之后
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 从数据库查询数据
     * @param title
     */
    private void findSchFormDb(String title) {
        DbManager dbManager = DBHelper.getDbManager();
        try {
            mSchList = dbManager.selector(Schedule.class)
                    .where("title", "like", "%" + title + "%")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    class MySchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSchList.size();
        }

        @Override
        public Schedule getItem(int position) {
            return mSchList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Schedule schedule = mSchList.get(position);
            int type = schedule.getType();//事件类型
            if (type < MANY_PEOPLE_EVENT) {//类型为个人事件
                View PersonView = UIUtils.inflate(R.layout.item_per_cal_layout);
                PersonalItemView mPerItemView = (PersonalItemView) PersonView.findViewById(R.id.piv_item_cal);

                mPerItemView.setType(type, schedule.getTimes_of_week());
                mPerItemView.setTheme(schedule.getTitle());
                mPerItemView.setPerAffProgress(schedule.getAffair_progress());
                mPerItemView.setPerEveProgress(schedule.getEnd_time());
                if (type == PERSONAL_AFFAIR) {//表示是事务
                    mPerItemView.setPerAffStopTime(schedule.getEnd_time(), schedule.getTotal_time());
                } else if (type == PERSONAL_HABIT) {//习惯
                    mPerItemView.setPerHabProgress(schedule.getHabit_complete_times(), schedule.getTimes_of_week());
                }

                return PersonView;
            } else {//类型为多人事件
                View ManyPeoView = UIUtils.inflate(R.layout.item_many_peo_cal_layout);
                ManyPeopleItemView mManyPeoItemView = (ManyPeopleItemView) ManyPeoView.findViewById(R.id.mpiv_item_cal);

                mManyPeoItemView.setType(schedule.getSch_status());
                mManyPeoItemView.setTheme(schedule.getTitle());
                mManyPeoItemView.setStopTime(schedule.getEnd_time());

                //接受
                mManyPeoItemView.getmBtnManyPeopleAccept().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CommonUtil.isnetWorkAvilable(UIUtils.getContext())) {
                            schedule.setSch_status(2);
                            notifyDataSetChanged();
//                            Change2Net(schedule.getServerid() + "", 1 + "");
                        } else {
                            UIUtils.showTip("请检查网络");
                        }
                    }
                });

                //拒绝
                mManyPeoItemView.getmBtnManyPeopleRefuse().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //在网络正常的状态下更改状态
                        if (CommonUtil.isnetWorkAvilable(UIUtils.getContext())) {
                            schedule.setSch_status(3);
                            notifyDataSetChanged();
//                            Change2Net(schedule.getServerid() + "", 2 + "");
                        } else {
                            UIUtils.showTip("请检查网络");
                        }
                    }
                });

                return ManyPeoView;
            }
        }
    }

}
