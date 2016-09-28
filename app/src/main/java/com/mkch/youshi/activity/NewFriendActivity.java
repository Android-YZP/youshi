package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.adapter.NewFriendListAdapter;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.receiver.FriendsReceiver;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends Activity implements NewFriendListAdapter.OnItemButtonClickListener {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtSearch;
    private LinearLayout mLayoutAddPhone;
    private ListView mListView;
    private View mLine1, mLine2;
    private DbManager dbManager;//数据库操作
    private List<Friend> mFriends;//数据
    private NewFriendListAdapter mAdapter;//adapter

    private static ProgressDialog mProgressDialog = null;//登录加载

    //广播接收
    private FriendsReceiver mFriendsReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int _what = msg.what;
            switch (_what) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(NewFriendActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case FriendsReceiver.RECEIVE_REQUEST_ADD_FRIEND:
                    String _friend_json = (String) msg.obj;
                    Gson _gson = new Gson();
                    Friend _friend = _gson.fromJson(_friend_json, Friend.class);
                    //昵称
                    String _nickname = _friend.getNickname();
                    String _content_text_username = null;
                    if (_nickname != null && !_nickname.equals("") && !_nickname.equals("null")) {
                        _content_text_username = _nickname;
                    } else {
                        _content_text_username = _friend.getFriendid();
                    }
                    //提示
                    Toast.makeText(NewFriendActivity.this, _content_text_username + ",请求添加您为好友！", Toast.LENGTH_LONG).show();
                    //更新UI界面，获取最新的用户列表
                    updateUIfromReceiver(_friend.getUserid());
                    break;
                case CommonConstants.FLAG_ALLOW_FRIEND_SUCCESS:
                    //更新UI界面，添加按钮变成已添加文字
                    updateUIfromAllowFriend();
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);

        }
    };

    /**
     * 更新UI界面，添加按钮变成已添加
     */
    private void updateUIfromAllowFriend() {
        Log.d("jlj", "--------------------------updateUIfromAllowFriend");
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 更新UI界面，获取最新的用户列表
     */
    private void updateUIfromReceiver(final String userid) {
        try {
            //本登录用户的，状态为已添加或待接受的，显示在新朋友的好友列表
            mFriends = dbManager.selector(Friend.class)
                    .where("status", "in", new int[]{1, 2})
                    .and("showinnewfriend", "=", "1")
                    .and("userid", "=", userid)
                    .findAll();
            Log.d("jlj", "mFriends size is --------------------" + mFriends.size());
            for (int i = 0; i < mFriends.size(); i++) {
                Log.d("jlj", "mFriends[" + i + "]=" + mFriends.get(i).toString());
            }
            mAdapter = new NewFriendListAdapter(this, mFriends);
            mAdapter.setOnItemButtonClickListener(this);
            mListView.setAdapter(mAdapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtSearch = (EditText) findViewById(R.id.et_new_friend_search);
        mLayoutAddPhone = (LinearLayout) findViewById(R.id.layout_new_friend_add_phone);
        mListView = (ListView) findViewById(R.id.list_new_friend);
        mLine1 = (View) findViewById(R.id.line1_new_friend);
        mLine2 = (View) findViewById(R.id.line2_new_friend);
    }

    private void initData() {
        mTvTitle.setText("新的朋友");
        User mUser = CommonUtil.getUserInfo(this);
        //查询数据库中待添加的好友列表
        dbManager = DBHelper.getDbManager();
        try {
            //本登录用户的，状态为已添加或待接受的，显示在新朋友的好友列表
            mFriends = dbManager.selector(Friend.class)
                    .where("status", "in", new int[]{1, 2})
                    .and("showinnewfriend", "=", "1")
                    .and("userid", "=", mUser.getOpenFireUserName())
                    .findAll();
            if (mFriends == null) {
                mLine1.setVisibility(View.GONE);
                mLine2.setVisibility(View.GONE);
                mFriends = new ArrayList<>();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        mAdapter = new NewFriendListAdapter(NewFriendActivity.this, mFriends);
        mAdapter.setOnItemButtonClickListener(this);
        mListView.setAdapter(mAdapter);
        //从广播中，接intent中内容，并更新UI
        String _friend_json = getIntent().getStringExtra("_friend_json");
        if (_friend_json != null && !_friend_json.equals("")) {
            Gson _gson = new Gson();
            Friend _friend = _gson.fromJson(_friend_json, Friend.class);
            //昵称
            String _nickname = _friend.getNickname();
            String _content_text_username = null;
            if (_nickname != null && !_nickname.equals("") && !_nickname.equals("null")) {
                _content_text_username = _nickname;
            } else {
                _content_text_username = _friend.getFriendid();
            }
            Toast.makeText(NewFriendActivity.this, _content_text_username + ",请求添加您为好友！", Toast.LENGTH_SHORT).show();
            //更新UI界面，获取最新的用户列表
            updateUIfromReceiver(_friend.getUserid());

        }
        //搜索框不弹出软键盘
        mEtSearch.setInputType(InputType.TYPE_NULL);

        //注册广播
        mFriendsReceiver = new FriendsReceiver(mHandler);
        IntentFilter _intent_filter = new IntentFilter("yoshi.action.friendsbroadcast");
        registerReceiver(mFriendsReceiver, _intent_filter);


    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewFriendActivity.this.finish();
            }
        });
        mLayoutAddPhone.setOnClickListener(new NewFriendOnClickListener());
        mEtSearch.setOnClickListener(new NewFriendOnClickListener());
        //给listView注册contextMenu
        registerForContextMenu(mListView);
    }

    /**
     * 设置上下文长按Item时的菜单
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_friend_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.new_friend_menu_del:
                //更改数据库中该条信息，并重新刷新UI
                Friend _friend = mFriends.get(info.position);
                try {
                    _friend.setShowinnewfriend(0);//不显示
                    dbManager.saveOrUpdate(_friend);
                } catch (DbException e) {
                    e.printStackTrace();
                    Log.d("jlj", "------------------" + e.getMessage());
                    return true;
                }
                mFriends.remove(info.position);//从集合中去除该对象
                mAdapter.notifyDataSetChanged();

                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    /**
     * 按了某个按钮之后
     *
     * @param position
     */
    @Override
    public void clickItemButton(int position) {
        Log.d("jlj", "NewFriendActivity----------------onClick=" + mFriends.get(position).getPhone());
        String _openfire_username = mFriends.get(position).getFriendid();
        //异步请求网络，接受该用户的好友添加
        addFriendFromNet(_openfire_username, position);

    }

    /**
     * 接受好友请求
     *
     * @param _openfire_username
     */
    private void addFriendFromNet(String _openfire_username, final int position) {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(this, null, "加载中...", true, true);
        //自己的信息
        final User _self_user = CommonUtil.getUserInfo(this);
        //根据openfireusername查询该用户的信息，并保存于数据库
        RequestParams requestParams = new RequestParams(CommonConstants.AllowFriend);
        //包装请求参数
        String _req_json = "{\"OpenFireName\":\"" + _openfire_username + "\"}";
        Log.d("jlj", "addFriendFromNet------------------req_json=" + _req_json + "," + _self_user.getLoginCode());
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", _self_user.getLoginCode());//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("jlj", "addFriendFromNet-onSuccess---------------------result = " + result);
                if (result != null) {
                    //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            //更改此好友的已添加状态
                            Friend _friend = mFriends.get(position);
                            _friend.setStatus(1);//已添加
                            Log.d("jlj", "----------------_friend-toString = " + _friend.toString());
                            //并更新数据库
                            try {
                                dbManager.saveOrUpdate(_friend);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            //提醒添加成功
                            mHandler.sendEmptyMessage(CommonConstants.FLAG_ALLOW_FRIEND_SUCCESS);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                Log.d("jlj", "getInfoByOpenFireName-------------1001");
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                Log.d("jlj", "getInfoByOpenFireName-------------1002");
                            } else {
                                CommonUtil.sendErrorMessage(_Message, mHandler);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, mHandler);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("jlj", "-------onError = " + ex.getMessage());
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, mHandler);
                } else if (ex instanceof ConnectTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, mHandler);
                } else if (ex instanceof SocketTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, mHandler);
                } else {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, mHandler);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("userLogin", "----onCancelled");
            }

            @Override
            public void onFinished() {
                Log.d("userLogin", "----onFinished");
                //取消进度加载对话框
            }
        });
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class NewFriendOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent _intent = null;
            switch (v.getId()) {
                case R.id.layout_new_friend_add_phone:
                    _intent = new Intent(NewFriendActivity.this, PhoneContactsActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.et_new_friend_search:
                    _intent = new Intent(NewFriendActivity.this, SearchResultActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除广播
        unregisterReceiver(mFriendsReceiver);
    }
}
