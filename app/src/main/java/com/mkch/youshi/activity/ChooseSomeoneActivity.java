package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.StringUtils;
import com.mkch.youshi.util.UIUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class ChooseSomeoneActivity extends AppCompatActivity {

    private ListView mSomeoneListView;
    private DbManager mDbManager;
    private List<Friend> all;
    private List<String> netFriendList;
    private Button mSomeoneNumber;
    private int mChooseNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_someone);
        initView();
        initData();
        setLister();
    }


    private void initView() {
        mSomeoneListView = (ListView) findViewById(R.id.lv_choose_someone);
        mSomeoneNumber = (Button) findViewById(R.id.bt_choose_someone_number);

    }

    private void initData() {
        netFriendList = new ArrayList<>();
        getFriendsFromDb();
        if (all != null) {
            mSomeoneListView.setAdapter(new ChooseAdapter());
        }
    }


    private void setLister() {
        mSomeoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox _cbItem = (CheckBox) view.findViewById(R.id.cb_item);
                _cbItem.setChecked(!_cbItem.isChecked());
                if (_cbItem.isChecked()) {//从未选中到被选中的时候，添加操作
                    mChooseNumber++;
                    mSomeoneNumber.setText("确定(" + mChooseNumber + ")");
                    netFriendList.add(all.get(position).getFriendid());
                } else {//从选中到没有选中，删除操作
                    mChooseNumber--;
                    mSomeoneNumber.setText("确定(" + mChooseNumber + ")");
                    netFriendList.remove(all.get(position).getFriendid());
                }
                UIUtils.LogUtils(netFriendList.size() + "" + netFriendList.toString());
            }
        });
        //确定
        mSomeoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult();
            }
        });
    }

    /**
     * 得到好友列表
     */
    private void getFriendsFromDb() {
        UIUtils.LogUtils("all.size()+");
        try {
            User _user = CommonUtil.getUserInfo(UIUtils.getContext());
            if (_user != null) {
                mDbManager = DBHelper.getDbManager();
                all = mDbManager.selector(Friend.class).where("userid", "=", _user.getOpenFireUserName()).and("status", "=", 1).findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回联系人的集合
     */
    public void sendResult() {
        Intent intent = getIntent();
        Gson gson = new Gson();
        String ChooseFriends = gson.toJson(netFriendList);
        intent.putExtra("ChooseFriends", ChooseFriends);
        ChooseSomeoneActivity.this.setResult(5, intent);
        ChooseSomeoneActivity.this.finish();
    }

    class ChooseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return all.size();
        }

        @Override
        public Object getItem(int position) {
            return all.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ChooseSomeoneActivity.this, R.layout.item_list_choose_someone_layout, null);
            TextView _tvName = (TextView) view.findViewById(R.id.tv_lv_name);
            String nickname = all.get(position).getNickname();
            if (!StringUtils.isEmpty(nickname)) {
                _tvName.setText(nickname);
            } else {
                _tvName.setText(all.get(position).getFriendid());
            }
            return view;
        }
    }
}
