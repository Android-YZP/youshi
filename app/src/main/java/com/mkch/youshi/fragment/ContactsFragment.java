package com.mkch.youshi.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.AddFriendsActivity;
import com.mkch.youshi.activity.ChatActivity;
import com.mkch.youshi.activity.FriendInformationActivity;
import com.mkch.youshi.activity.GroupChatActivity;
import com.mkch.youshi.activity.NewFriendActivity;
import com.mkch.youshi.adapter.ContactAdapter;
import com.mkch.youshi.bean.Contact;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.view.HanziToPinyin;
import com.mkch.youshi.view.SideBar;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {

    private ImageView mIvAddFriend;
    private LinearLayout mLayoutNewFriend, mLayoutGroupChat, mLayoutTest;
    private static ProgressDialog mProgressDialog = null;
    @BindView(id = R.id.list_contacts)
    private ListView mListView;
    private TextView mFooterView;
    private ArrayList<Contact> datas = new ArrayList<>();
    private ContactAdapter mAdapter;
    private SideBar mSideBar;
    private TextView mDialog;
    private EditText mSearchInput;

    private TextView mTvNewFriendNum;//请求添加好友的用户数量
    private DbManager dbManager;//数据库管理对象

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initData();//把该调用挪到了onResume-from JLJ
        setListener();
    }



    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 查找所有view
     *
     * @param view
     */
    private void findView(View view) {
        mIvAddFriend = (ImageView) view.findViewById(R.id.iv_contacts_topbar_add_friend);
        mTvNewFriendNum = (TextView) view.findViewById(R.id.tv_contacts_new_friend_number);
        mLayoutNewFriend = (LinearLayout) view.findViewById(R.id.layout_contacts_new_friend);
        mLayoutGroupChat = (LinearLayout) view.findViewById(R.id.layout_contacts_group_chat);
        mLayoutTest = (LinearLayout) view.findViewById(R.id.layout_contacts_test);
        mSideBar = (SideBar) view.findViewById(R.id.sidebar_contacts);
        mDialog = (TextView) view.findViewById(R.id.tv_contacts_dialog);
        mSearchInput = (EditText) view.findViewById(R.id.et_contacts_search);
        mListView = (ListView) view.findViewById(R.id.list_contacts);

        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);
        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(getActivity(), R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);
//        getFriendListFromNet();//把该调用挪到initData-from JLJ
    }

    /**
     * 初始化界面数据
     */
    private void initData() {
        //从数据库获取请求好友的数量，并设置
        String _req_friend_num = "0";
        try {
            dbManager = DBHelper.getDbManager();
            long count = dbManager.selector(Friend.class).where("status", "=", "2").and("showinnewfriend","=","1").count();
            _req_friend_num = String.valueOf(count);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (_req_friend_num.equals("0")){
            mTvNewFriendNum.setVisibility(View.GONE);

        }else{
            mTvNewFriendNum.setVisibility(View.VISIBLE);
            mTvNewFriendNum.setText(_req_friend_num);

        }
        //加载好友列表
        getFriendListFromNet();
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        mIvAddFriend.setOnClickListener(new MyContactsOnClickListener());
        mLayoutNewFriend.setOnClickListener(new MyContactsOnClickListener());
        mLayoutGroupChat.setOnClickListener(new MyContactsOnClickListener());
        mLayoutTest.setOnClickListener(new MyContactsOnClickListener());
        //注册contextmenu
        registerForContextMenu(mListView);
    }

    /**
     * 设置上下文长按Item时的菜单
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (getActivity()!=null){
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int _position = info.position;
        Contact _Contact = datas.get(_position);
        String _openfirename = _Contact.getOpenFireName();
        switch (item.getItemId()){
            case R.id.contact_menu_chat:
                //进入单聊界面聊天
                if (getActivity()!=null){
                    Intent _intent = new Intent(getActivity(), ChatActivity.class);
                    _intent.putExtra("_openfirename",_openfirename);
                    startActivity(_intent);
                }
                return true;
            case R.id.contact_menu_del:
                //异步请求网络接口，删除该好友
                deleteFriendFromNet(_openfirename,_position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    /**
     * 异步请求网络接口，删除该好友
     * @param openfirename
     */
    private void deleteFriendFromNet(String openfirename, final int position) {
        if (getActivity()==null){
            return;
        }
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(getActivity(), null, "加载中...", true, true);
        //自己的信息
        final User _self_user = CommonUtil.getUserInfo(getActivity());
        //根据openfireusername查询该用户的信息，并保存于数据库
        RequestParams requestParams = new RequestParams(CommonConstants.DeleteFriend);
        //包装请求参数
        String _req_json = "{\"OpenFireName\":\"" + openfirename + "\"}";
        Log.d("jlj","deleteFriendFromNet------------------req_json="+_req_json+","+_self_user.getLoginCode());
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", _self_user.getLoginCode());//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("jlj","deleteFriendFromNet-onSuccess---------------------result = "+result);
                if (result != null) {
                    //若result返回信息中删除成功
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            //清除本地数据库该条好友信息，清除本地该条数据
                            datas.remove(position);
                            //提醒删除成功
                            myHandler.sendEmptyMessage(CommonConstants.FLAG_DELETE_FRIEND_SUCCESS);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                Log.d("jlj", "deleteFriendFromNet-------------1001");
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                Log.d("jlj", "deleteFriendFromNet-------------1002");
                            } else {
                                CommonUtil.sendErrorMessage(_Message, myHandler);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, myHandler);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("jlj", "deleteFriendFromNet-------onError = " + ex.getMessage());
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
                Log.d("jlj", "deleteFriendFromNet----onCancelled");
            }

            @Override
            public void onFinished() {
                Log.d("jlj", "deleteFriendFromNet----onFinished");
            }
        });



    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case 0:
                    //出现错误
                    if (getActivity()!=null){
                        String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CommonConstants.FLAG_GET_FRIEND_LIST_SHOW:
                    //加载好友列表
                    showListVerfy();
                    break;
                case CommonConstants.FLAG_DELETE_FRIEND_SUCCESS:
                    //刷新UI
                    if (getActivity()!=null){
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                        mFooterView.setText(datas.size() + "位联系人");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取联系人列表
     */
    private void showListVerfy() {
        mFooterView.setText(datas.size() + "位联系人");
        mAdapter = new ContactAdapter(mListView, datas);
        mListView.setAdapter(mAdapter);
    }

    private MyHandler myHandler = new MyHandler();

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class MyContactsOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                Intent _intent = null;
                switch (v.getId()) {
                    case R.id.iv_contacts_topbar_add_friend:
                        _intent = new Intent(getActivity(), AddFriendsActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                    case R.id.layout_contacts_new_friend:
                        _intent = new Intent(getActivity(), NewFriendActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                    case R.id.layout_contacts_group_chat:
                        _intent = new Intent(getActivity(), GroupChatActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                    case R.id.layout_contacts_test:
                        _intent = new Intent(getActivity(), FriendInformationActivity.class);
                        getActivity().startActivity(_intent);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 获取好友列表
     */
    private void getFriendListFromNet() {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.GetFriendList);
        //包装请求参数
        String code = CommonUtil.getUserInfo(getActivity()).getLoginCode();
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("jlj","------------------result="+result);
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            JSONArray mDatas = _json_result.getJSONArray("Datas");
                            Log.d("jlj","------------------mDatas="+mDatas.toString());
                            //再次获取网络端数据时清除datas的数据-from JLJ
                            datas.clear();
                            //清除数据库中所有friend列表状态为1的好友
                            try {
                            List<Friend> _yoshi_friends = dbManager.selector(Friend.class)
                                    .where("status","=",1)
                                    .findAll();
                                dbManager.delete(_yoshi_friends);
                            } catch (DbException e) {
                                e.printStackTrace();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            for (int i = 0; i < mDatas.length(); i++) {
                                Contact data = new Contact();
                                JSONObject jobj = mDatas.getJSONObject(i);
//                                String name = jobj.getString("UserName");
                                String HeadPic = jobj.getString("HeadPic");
                                String Nickname = jobj.getString("NickName");
                                String MobileNumber = jobj.getString("MobileNumber");
                                String Remark = jobj.getString("Remark");
                                //若登录名为空，则显示OpenFireUserName
                                String OpenFireUserName = jobj.getString("OpenFireUserName");
                                Log.d("jlj","--------------Username="+Nickname+",OpenFireUserName="+OpenFireUserName);
                                if (Nickname!=null&&!Nickname.equals("")&&!Nickname.equals("null")){
                                    data.setName(Nickname);

                                }else{
                                    data.setName(OpenFireUserName);
                                }
                                data.setOpenFireName(OpenFireUserName);
                                data.setPinyin(HanziToPinyin.getPinYin(OpenFireUserName));
                                datas.add(data);
                                //临时使用-存储所有的优时好友列表数据-from JLJ
                                User _self_user = CommonUtil.getUserInfo(getActivity());
                                int status = 1;//已添加好友
                                String _self_userid = _self_user.getOpenFireUserName();
                                //获取所有的优时好友列表
                                Friend _friend = new Friend(OpenFireUserName,HeadPic,Nickname,Remark,MobileNumber,status,_self_userid);
                                try {
                                    dbManager.save(_friend);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            myHandler.sendEmptyMessage(CommonConstants.FLAG_GET_FRIEND_LIST_SHOW);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<Contact> temp = new ArrayList<>(datas);
        for (Contact data : datas) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
            mAdapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
