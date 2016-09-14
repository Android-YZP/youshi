package com.mkch.youshi.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.AddFriendsActivity;
import com.mkch.youshi.activity.FriendInformationActivity;
import com.mkch.youshi.activity.GroupChatActivity;
import com.mkch.youshi.activity.NewFriendActivity;
import com.mkch.youshi.adapter.ContactAdapter;
import com.mkch.youshi.bean.Contact;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.view.HanziToPinyin;
import com.mkch.youshi.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.ui.BindView;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

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
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        mLayoutNewFriend = (LinearLayout) view.findViewById(R.id.layout_contacts_new_friend);
        mLayoutGroupChat = (LinearLayout) view.findViewById(R.id.layout_contacts_group_chat);
        mLayoutTest = (LinearLayout) view.findViewById(R.id.layout_contacts_test);
        mSideBar = (SideBar) view.findViewById(R.id.sidebar_contacts);
        mDialog = (TextView) view.findViewById(R.id.tv_contacts_dialog);
        mSearchInput = (EditText) view.findViewById(R.id.et_contacts_search);
        mListView = (ListView) view.findViewById(R.id.list_contacts);

        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(getActivity(), R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);
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
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case CommonConstants.FLAG_GET_FRIEND_LIST_SHOW:
                    //加载好友列表
                    showListVerfy();
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
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            JSONArray mDatas = _json_result.getJSONArray("Datas");
                            for (int i = 0; i < mDatas.length(); i++) {
                                Contact data = new Contact();
                                JSONObject jobj = mDatas.getJSONObject(i);
                                String name = jobj.getString("UserName");
                                data.setName(name);
                                data.setPinyin(HanziToPinyin.getPinYin(name));
                                datas.add(data);
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
