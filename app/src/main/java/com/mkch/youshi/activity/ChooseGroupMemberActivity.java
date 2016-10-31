package com.mkch.youshi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ChooseGroupMemberAdapter;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.JsonUtils;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.ContactListView;
import com.mkch.youshi.view.HanziToPinyin;
import com.mkch.youshi.view.SideBar;
import com.tencent.TIMGroupManager;
import com.tencent.TIMValueCallBack;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseGroupMemberActivity extends KJActivity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
    private static ProgressDialog mProgressDialog = null;
    @BindView(id = R.id.list_choose_group_member)
    private ContactListView mListView;
    private TextView mFooterView;
    private List<Friend> datas = new ArrayList<>();
    //创建待加入群组的用户列表
    private ArrayList<String> listMembers = new ArrayList<String>();
    private ChooseGroupMemberAdapter mAdapter;
    private SideBar mSideBar;
    private TextView mDialog;
    private EditText mSearchInput;
    private ImageView mIvBack;
    private TextView mTvConfirm;
    private DbManager dbManager;//数据库管理对象
    private List<Friend> _friends;
    private User mUser;
    private String _self_openfirename, mGroupName, mMember;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_choose_group_member);
    }

    public void initData() {
        super.initData();
        mUser = CommonUtil.getUserInfo(this);
        dbManager = DBHelper.getDbManager();
        _self_openfirename = mUser.getOpenFireUserName();
        try {
            _friends = dbManager.selector(Friend.class).where("status", "=", "1").and("userid", "=", _self_openfirename).findAll();
            if (_friends != null && _friends.size() > 0) {
                mAdapter = new ChooseGroupMemberAdapter(mListView, _friends);
                mListView.setAdapter(mAdapter);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        Intent _intent = getIntent();
        if (_intent != null) {
            mGroupName = _intent.getStringExtra("groupName");
        }
        //加载好友列表
        getFriendListFromNet();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mIvBack = (ImageView) findViewById(R.id.iv_choose_group_member_back);
        mTvConfirm = (TextView) findViewById(R.id.tv_choose_group_member_confirm);
        mSideBar = (SideBar) findViewById(R.id.sidebar_choose_group_member);
        mDialog = (TextView) findViewById(R.id.tv_choose_group_member_dialog);
        mSearchInput = (EditText) findViewById(R.id.et_choose_group_member_search);
        mListView = (ContactListView) findViewById(R.id.list_choose_group_member);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);
        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(this, R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseGroupMemberActivity.this.finish();
            }
        });
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Integer, Boolean> state = mAdapter.mState;
                for (int j = 0; j < mAdapter.getCount(); j++) {
                    if (state.get(j) != null) {
                        Friend friend = mAdapter.getItem(j);
                        mMember = friend.getFriendid();
                        listMembers.add(mMember);
                    }
                }
                if (listMembers != null && listMembers.size() != 0) {
                    createGroup(mGroupName, listMembers);
                } else {
                    UIUtils.showTip("至少选择一个联系人");
                }
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
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(ChooseGroupMemberActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case CommonConstants.FLAG_GET_FRIEND_LIST_SHOW:
                    //加载好友列表
                    showListVerfy();
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    String errorMsg1 = ("认证错误");
                    Toast.makeText(ChooseGroupMemberActivity.this, errorMsg1, Toast.LENGTH_SHORT).show();
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR3:
                    //请求失败
                    String errorMsg3 = ("请求失败");
                    Toast.makeText(ChooseGroupMemberActivity.this, errorMsg3, Toast.LENGTH_SHORT).show();
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
        mAdapter = new ChooseGroupMemberAdapter(mListView, datas);
        mListView.setAdapter(mAdapter);
    }

    private MyHandler myHandler = new MyHandler();

    /**
     * 获取好友列表
     */
    private void getFriendListFromNet() {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.GetFriendList);
        //包装请求参数
        String code = CommonUtil.getUserInfo(this).getLoginCode();
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            JSONArray mDatas = _json_result.getJSONArray("Datas");
                            //再次获取网络端数据时清除datas的数据-from JLJ
                            datas.clear();
                            for (int i = 0; i < mDatas.length(); i++) {
                                Friend data = new Friend();
                                JSONObject jobj = mDatas.getJSONObject(i);
                                String HeadPic = JsonUtils.getString(jobj, "HeadPic");//头像
                                String _head_pic = null;
                                if (HeadPic != null && !HeadPic.equals("") && !HeadPic.equals("null")) {
                                    _head_pic = CommonConstants.NOW_ADDRESS_PRE + HeadPic;
                                    data.setHead_pic(_head_pic);
                                }
                                String Nickname = JsonUtils.getString(jobj, "NickName");
                                String MobileNumber = JsonUtils.getString(jobj, "MobileNumber");
                                String Remark = JsonUtils.getString(jobj, "Remark");
                                //若登录名为空，则显示OpenFireUserName
                                String OpenFireUserName = JsonUtils.getString(jobj, "OpenFireUserName");
                                if (Nickname != null && !Nickname.equals("") && !Nickname.equals("null")) {
                                    data.setNickname(Nickname);
                                    data.setPinyin(HanziToPinyin.getPinYin(Nickname));
                                } else {
                                    data.setPinyin(HanziToPinyin.getPinYin(OpenFireUserName));
                                }
                                String youshiNumber = JsonUtils.getString(jobj, "UserName");
                                data.setYoushi_number(youshiNumber);
                                String place = JsonUtils.getString(jobj, "Place");
                                data.setPlace(place);
                                String phoneNumber = JsonUtils.getString(jobj, "PhoneNumber");
                                data.setPhone_number(phoneNumber);
                                String description = JsonUtils.getString(jobj, "Description");
                                data.setDescription(description);
                                String sex = JsonUtils.getString(jobj, "Sex");
                                data.setSex(sex);
                                String sign = JsonUtils.getString(jobj, "Sign");
                                data.setSign(sign);
                                data.setFriendid(OpenFireUserName);
                                datas.add(data);
                                //存储所有的优时好友列表数据
                                User _self_user = CommonUtil.getUserInfo(ChooseGroupMemberActivity.this);
                                int status = 1;//已添加好友
                                String _self_userid = _self_user.getOpenFireUserName();
                                try {
                                    Friend _friend_tab = dbManager.selector(Friend.class)
                                            .where("friendid", "=", OpenFireUserName)
                                            .and("userid", "=", _self_userid)
                                            .findFirst();
                                    if (_friend_tab != null) {
                                        //有就更改他的字段
                                        _friend_tab.setNickname(Nickname);
                                        if (Nickname != null && !Nickname.equals("") && !Nickname.equals("null")) {
                                            _friend_tab.setNickname(Nickname);
                                            _friend_tab.setPinyin(HanziToPinyin.getPinYin(Nickname));
                                        } else {
                                            _friend_tab.setPinyin(HanziToPinyin.getPinYin(OpenFireUserName));
                                        }
                                        _friend_tab.setHead_pic(_head_pic);
                                        _friend_tab.setPhone(MobileNumber);
                                        _friend_tab.setRemark(Remark);
                                        _friend_tab.setStatus(1);
                                        _friend_tab.setYoushi_number(youshiNumber);
                                        _friend_tab.setPlace(place);
                                        _friend_tab.setPhone_number(phoneNumber);
                                        _friend_tab.setDescription(description);
                                        _friend_tab.setSex(sex);
                                        _friend_tab.setSign(sign);
                                        dbManager.saveOrUpdate(_friend_tab);
                                    } else {
                                        //没有就插入
                                        Friend _friend = new Friend(OpenFireUserName, _head_pic, Nickname, Remark, MobileNumber, youshiNumber, place, phoneNumber, description, sex, sign, status, _self_userid);
                                        dbManager.save(_friend);
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            myHandler.sendEmptyMessage(CommonConstants.FLAG_GET_FRIEND_LIST_SHOW);
                        } else {
                            String _Message = JsonUtils.getString(_json_result, "Message");
                            String _ErrorCode = JsonUtils.getString(_json_result, "ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                myHandler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                myHandler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR3);
                            } else {
                                CommonUtil.sendErrorMessage(_Message, myHandler);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
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

    private void createGroup(final String groupName, final ArrayList<String> members) {
        TIMGroupManager.getInstance().createGroup("Private", members, groupName, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-------createGroup", i + "Error:" + s);
                if (i == 80001) {
                    UIUtils.showTip("创建失败，群名含有敏感词");
                } else {
                    UIUtils.showTip("创建群失败");
                }
            }

            @Override
            public void onSuccess(String s) {
                Log.d("zzz-------createGroup", "createGroup is success");
                UIUtils.showTip("创建群成功");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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
        ArrayList<Friend> temp = new ArrayList<>(datas);
        for (Friend data : datas) {
            if (data.getNickname().contains(s) || data.getPinyin().contains(s)) {
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
