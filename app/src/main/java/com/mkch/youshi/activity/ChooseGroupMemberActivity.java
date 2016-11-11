package com.mkch.youshi.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ChooseGroupMemberAdapter;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.ContactListView;
import com.mkch.youshi.view.SideBar;
import com.tencent.TIMGroupManager;
import com.tencent.TIMValueCallBack;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseGroupMemberActivity extends KJActivity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
    @BindView(id = R.id.list_choose_group_member)
    private ContactListView mListView;
    private TextView mFooterView;
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
    private boolean isSendFile;
    private boolean isCollectFile;

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
                mFooterView = (TextView) View.inflate(this, R.layout.item_list_contact_count, null);
                mFooterView.setText(_friends.size() + "位联系人");
                isSendFile = getIntent().getBooleanExtra("isSendFile", false);
                isCollectFile = getIntent().getBooleanExtra("isCollectFile", false);
                String localAddress = getIntent().getStringExtra("localAddress");
                if (isSendFile) {//转发文件
                    if (isCollectFile){//转发收藏文件选择联系人
                        mAdapter = new ChooseGroupMemberAdapter(mListView, _friends
                                , ChooseGroupMemberActivity.this,true,localAddress);
                        UIUtils.LogUtils("走到这里了33333333333" + localAddress);
                    }else {//转发优盘文件选择联系人
                        mAdapter = new ChooseGroupMemberAdapter(mListView, _friends, ChooseGroupMemberActivity.this);
                        UIUtils.LogUtils("走到这里了1111111111" + isSendFile);
                    }
                } else {
                    mAdapter = new ChooseGroupMemberAdapter(mListView, _friends);
                    UIUtils.LogUtils("走到这里了222222222222" + isSendFile);
                }
                mListView.setAdapter(mAdapter);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        Intent _intent = getIntent();
        if (_intent != null) {
            mGroupName = _intent.getStringExtra("groupName");
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
        mIvBack = (ImageView) findViewById(R.id.iv_choose_group_member_back);
        mTvConfirm = (TextView) findViewById(R.id.tv_choose_group_member_confirm);
        if (isSendFile) mTvConfirm.setVisibility(View.INVISIBLE);//转发文件隐藏联系人
        mSideBar = (SideBar) findViewById(R.id.sidebar_choose_group_member);
        mDialog = (TextView) findViewById(R.id.tv_choose_group_member_dialog);
        mSearchInput = (EditText) findViewById(R.id.et_choose_group_member_search);
        mListView = (ContactListView) findViewById(R.id.list_choose_group_member);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);
        // 给listView设置adapter
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
        ArrayList<Friend> temp = new ArrayList<>(_friends);
        for (Friend data : _friends) {
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
