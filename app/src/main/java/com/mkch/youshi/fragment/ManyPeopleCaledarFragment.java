package com.mkch.youshi.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.CalendarActivity;
import com.mkch.youshi.activity.ManyPeopleEventDetial;
import com.mkch.youshi.activity.UserInformationActivity;
import com.mkch.youshi.adapter.ManPeopleCalAdapter;
import com.mkch.youshi.bean.ManyPeopleEvenBean;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.ManyPeopleItemView;

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

import static android.R.attr.data;
import static com.mkch.youshi.activity.AddPersonalEventActivity.mProgressDialog;

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
                int serverid = mSchedules.get(position).getServerid();
                Intent i = new Intent(getActivity(),
                        ManyPeopleEventDetial.class);
                String _gson_str = gson.toJson(schedule);//传一个数组的数据到详情界面
                i.putExtra("mgonsn", _gson_str);
                i.putExtra("Sid", serverid);
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
            mLvManyPeopleCalendar.setAdapter(new MyAdapter(mSchedules));
        }
    }

    /**
     * 封装请求参数
     *
     * @return
     */
    private String createJson(String ScheduleID, String Result) {
        JSONObject _json_args = null;
        String _DatastoString = "";
        try {
            _json_args = new JSONObject();
            _json_args.put("ScheduleID", ScheduleID);
            _json_args.put("Result", Result);
            _DatastoString = _json_args.toString();
            UIUtils.LogUtils(_DatastoString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return _DatastoString;
    }

    /**
     * 拒绝或者接收
     */
    private void Change2Net(String ScheduleID, String Result) {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.JoinUserResult);
        //包装请求参数
        final String code = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
        String _req_json = createJson(ScheduleID, Result);
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    UIUtils.LogUtils(result + code);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, myHandler);
                } else if (ex instanceof ConnectTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, myHandler);
                } else if (ex instanceof SocketTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, myHandler);
                } else {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, myHandler);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    public class MyAdapter extends BaseAdapter {
        List<Schedule> schedules;

        public MyAdapter(List<Schedule> schedules) {
            this.schedules = schedules;
        }

        @Override
        public int getCount() {
            return schedules.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                //可以理解为从vlist获取view  之后把view返回给ListView

                convertView = UIUtils.inflate(R.layout.item_many_peo_cal_layout);
                holder.mManyPeoItemView = (ManyPeopleItemView) convertView.findViewById(R.id.mpiv_item_cal);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mManyPeoItemView.setType(schedules.get(position).getSch_status());
            holder.mManyPeoItemView.setTheme(schedules.get(position).getTitle());
            holder.mManyPeoItemView.setStopTime(schedules.get(position).getEnd_time());

            //接受
            holder.mManyPeoItemView.getmBtnManyPeopleAccept().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtil.isnetWorkAvilable(UIUtils.getContext())) {
                        schedules.get(position).setSch_status(2);
                        notifyDataSetChanged();
                        Change2Net(schedules.get(position).getServerid() + "", 1 + "");
                    } else {
                        UIUtils.showTip("请检查网络");
                    }
                }
            });

            //拒绝
            holder.mManyPeoItemView.getmBtnManyPeopleRefuse().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //在网络正常的状态下更改状态
                    if (CommonUtil.isnetWorkAvilable(UIUtils.getContext())) {
                        schedules.get(position).setSch_status(3);
                        notifyDataSetChanged();
                        Change2Net(schedules.get(position).getServerid() + "", 2 + "");
                    } else {
                        UIUtils.showTip("请检查网络");
                    }
                }
            });

            return convertView;
        }
    }


    //提取出来方便点
    public final class ViewHolder {
        private ManyPeopleItemView mManyPeoItemView;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(CalendarActivity activity) {
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
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    UIUtils.showTip(errorMsg);
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler myHandler = new MyHandler((CalendarActivity) getActivity());
}
