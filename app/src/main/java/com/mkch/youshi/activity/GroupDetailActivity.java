package com.mkch.youshi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.GroupFriendListAdapter;
import com.mkch.youshi.bean.GroupFriend;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.model.Group;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupDetailInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_group_detail)
public class GroupDetailActivity extends BaseActivity {

    @ViewInject(R.id.gv_group_detail_friends)
    private GridView mGvFriendHeadpics;//参与聊天的好友列表
    @ViewInject(R.id.iv_common_topbar_back)
    private ImageView mIvBack;
    @ViewInject(R.id.line_group_detail_groupname)
    private LinearLayout mLayoutGroupName;//群名称
    @ViewInject(R.id.tv_group_detail_groupname)
    private TextView mTvGroupName;//群名称
    @ViewInject(R.id.line_group_detail_group_notification)
    private LinearLayout mLayoutNotification;//群公告
    @ViewInject(R.id.tv_group_detail_group_notification)
    private TextView mTvGroupNotification;//群公告
    @ViewInject(R.id.line_group_detail_group_self_name)
    private LinearLayout mLayoutMemberCard;//群名片
    @ViewInject(R.id.tv_group_detail_group_self_name)
    private TextView mTvGroupCard;//群名片
    private List<GroupFriend> mGroupFriends = new ArrayList<>();
    private List<GroupFriend> datas = new ArrayList<>();
    private List<String> mMembers = new ArrayList<>();
    private GroupFriendListAdapter mAdapter;
    private String _groupID;
    private Group mGroup;
    private GroupFriend mGroupFriend;
    private User mUser;
    private String mUserId, newGroupName, newCard, newNotification;
    private DbManager dbManager;//数据库管理对象
    private List<String> mUsers = new ArrayList<String>();
    @ViewInject(R.id.line_group_detail_del_and_quit)
    private TextView mTvQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        dbManager = DBHelper.getDbManager();
        mUser = CommonUtil.getUserInfo(this);
        mUserId = mUser.getOpenFireUserName();
        Intent _intent = getIntent();
        if (_intent != null) {
            _groupID = _intent.getStringExtra("groupID");
            //加载本地数据
            try {
                //加载本地群成员列表
                datas = dbManager.selector(GroupFriend.class).where("group_id", "=", _groupID).findAll();
                if (datas != null && datas.size() > 0) {
                    //插入默认图片
                    GroupFriend groupFriend = new GroupFriend();
                    datas.add(groupFriend);
                    datas.add(groupFriend);
                    mAdapter = new GroupFriendListAdapter(GroupDetailActivity.this, datas);
                    mGvFriendHeadpics.setAdapter(mAdapter);
                }
                //加载本地群名称和群公告
                mGroup = dbManager.selector(Group.class).where("group_id", "=", _groupID).and("user_id", "=", mUserId).findFirst();
                if (mGroup != null) {
                    if (mGroup.getGroupName() != null && !mGroup.getGroupName().equals("")) {
                        mTvGroupName.setText(mGroup.getGroupName());
                    }
                    if (mGroup.getGroupNotification() != null && !mGroup.getGroupNotification().equals("")) {
                        mTvGroupNotification.setText(mGroup.getGroupNotification());
                    }
                }
                //加载本地群名片
                String name = "";
                if (mUser.getNickName() != null && !mUser.getNickName().equals("")) {
                    name = mUser.getNickName();
                } else {
                    name = mUser.getOpenFireUserName();
                }
                mGroupFriend = dbManager.selector(GroupFriend.class).where("group_id", "=", _groupID).and("member_identifier", "=", mUserId).findFirst();
                if (mGroupFriend != null) {
                    if (mGroupFriend.getMemberCard() != null && !mGroupFriend.getMemberCard().equals("")) {
                        mTvGroupCard.setText(mGroupFriend.getMemberCard());
                    } else {
                        mTvGroupCard.setText(name);
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            getMembers(_groupID);
            //创建待获取信息的群组Id列表
            ArrayList<String> groupList = new ArrayList<String>();
            groupList.add(_groupID);
            getGroupNotification(groupList);
        }
    }

    //获取群聊成员
    private void getMembers(final String groupID) {
        TIMGroupManager.getInstance().getGroupMembers(groupID, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-----getGroupMembers", i + "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                Log.d("zzz-----getGroupMembers", "getGroupMembers is success");
                for (TIMGroupMemberInfo info : timGroupMemberInfos) {
                    String memberIdentifier = info.getUser();
                    String memberCard = info.getNameCard();
                    TIMGroupMemberRoleType memberRole = info.getRole();
                    //先插入群成员名片和角色
                    GroupFriend groupFriend = new GroupFriend();
                    groupFriend.setMemberIdentifier(memberIdentifier);
                    groupFriend.setMemberCard(memberCard);
                    groupFriend.setMemberRole(memberRole);
                    mGroupFriends.add(groupFriend);
                    //待获取用户资料的群成员列表
                    mMembers.add(memberIdentifier);
                }
                //通过identifier获取群成员头像和昵称
                getGroupMemberHead(mMembers);
            }
        });
    }

    //获取群成员头像和昵称后保存在本地数据库
    private void getGroupMemberHead(final List<String> users) {
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-----getUsersProfile", i + "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                Log.d("zzz-----getUsersProfile", "getUsersProfile is success");
                for (int i = 0; i < timUserProfiles.size(); i++) {
                    String memberHead = timUserProfiles.get(i).getFaceUrl();
                    mGroupFriends.get(i).setMemberHead(memberHead);
                    String memberName = timUserProfiles.get(i).getNickName();
                    mGroupFriends.get(i).setMemberName(memberName);
                    String memberIdentifier = mGroupFriends.get(i).getMemberIdentifier();
                    TIMGroupMemberRoleType memberRole = mGroupFriends.get(i).getMemberRole();
                    String memberCard = mGroupFriends.get(i).getMemberCard();
                    //存储所有的群成员信息
                    try {
                        GroupFriend _groupFriend = dbManager.selector(GroupFriend.class).where("member_identifier", "=", memberIdentifier).and("group_id", "=", _groupID).findFirst();
                        if (_groupFriend != null) {
                            //有就更改他的字段
                            _groupFriend.setGroupID(_groupID);
                            _groupFriend.setMemberIdentifier(memberIdentifier);
                            _groupFriend.setMemberName(memberName);
                            _groupFriend.setMemberCard(memberCard);
                            _groupFriend.setMemberHead(memberHead);
                            _groupFriend.setMemberRole(memberRole);
                            dbManager.saveOrUpdate(_groupFriend);
                        } else {
                            //没有就插入
                            GroupFriend groupFriend1 = new GroupFriend(_groupID, memberIdentifier, memberName, memberCard, memberHead, memberRole);
                            dbManager.saveBindingId(groupFriend1);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                //插入默认图片
                GroupFriend groupFriend = new GroupFriend();
                mGroupFriends.add(groupFriend);
                mGroupFriends.add(groupFriend);
                mAdapter = new GroupFriendListAdapter(GroupDetailActivity.this, mGroupFriends);
                //设置适配器
                mGvFriendHeadpics.setAdapter(mAdapter);
            }
        });
    }

    //获取群公告
    private void getGroupNotification(final ArrayList<String> groupList) {
        TIMGroupManager.getInstance().getGroupDetailInfo(groupList, new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz--getGroupDetailInfo", i + "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
                Log.d("zzz--getGroupDetailInfo", "getGroupDetailInfo is success");
                for (TIMGroupDetailInfo info : timGroupDetailInfos) {
                    mTvGroupNotification.setText(info.getGroupNotification());
                    //存储群公告
                    try {
                        Group _group = dbManager.selector(Group.class).where("group_id", "=", _groupID).and("user_id", "=", mUserId).findFirst();
                        if (_group != null) {
                            _group.setGroupNotification(info.getGroupNotification());
                            dbManager.saveOrUpdate(_group);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Event({R.id.iv_common_topbar_back, R.id.line_group_detail_del_and_quit, R.id.line_group_detail_groupname, R.id.line_group_detail_group_notification, R.id.line_group_detail_group_self_name})
    private void clickButtons(View view) {
        switch (view.getId()) {
            case R.id.iv_common_topbar_back://返回按钮
                this.finish();//返回
                break;
            case R.id.line_group_detail_del_and_quit://退出并删除
                quitGroup();
                break;
            case R.id.line_group_detail_groupname://修改群名称
                final EditText etName = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入新的群名称").setView(
                        etName).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGroupName = etName.getText().toString();
                        modifyGroupName(_groupID, newGroupName);
                    }
                })
                        .setNegativeButton("取消", null).show();
                break;
            case R.id.line_group_detail_group_notification://修改群公告
                final EditText etNotification = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入新的群公告").setView(
                        etNotification).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newNotification = etNotification.getText().toString();
                        modifyGroupNotification(_groupID, newNotification);
                    }
                })
                        .setNegativeButton("取消", null).show();
                break;
            case R.id.line_group_detail_group_self_name://修改群名片
                final EditText etCard = new EditText(this);
                new AlertDialog.Builder(this).setTitle("请输入新的群昵称").setView(
                        etCard).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newCard = etCard.getText().toString();
                        modifyMemberCard(_groupID, mUserId, newCard);
                    }
                })
                        .setNegativeButton("取消", null).show();
                break;
        }
    }

    //修改群公告
    private void modifyGroupNotification(final String groupId, final String newNotification) {
        TIMGroupManager.getInstance().modifyGroupNotification(groupId, newNotification, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("modifyGroupNotification", i + "Error:" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("modifyGroupNotification", "modifyGroupNotification is success");
                mTvGroupNotification.setText(newNotification);
                //更新数据库
                try {
                    Group _group = dbManager.selector(Group.class).where("group_id", "=", _groupID).and("user_id", "=", mUserId).findFirst();
                    if (_group != null) {
                        _group.setGroupNotification(newNotification);
                        dbManager.saveOrUpdate(_group);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //修改群名称
    private void modifyGroupName(final String groupId, final String newName) {
        TIMGroupManager.getInstance().modifyGroupName(groupId, newName, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-----modifyGroupName", i + "Error:" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("zzz-----modifyGroupName", "modifyGroupName is success");
                mTvGroupName.setText(newGroupName);
                //更新数据库
                try {
                    Group _group = dbManager.selector(Group.class).where("group_id", "=", _groupID).and("user_id", "=", mUserId).findFirst();
                    if (_group != null) {
                        _group.setGroupName(newGroupName);
                        dbManager.saveOrUpdate(_group);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //修改群名片
    private void modifyMemberCard(final String groupId, final String identifier, final String newCard) {
        TIMGroupManager.getInstance().modifyGroupMemberInfoSetNameCard(groupId, identifier, newCard, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz----modifyMemberCard", i + "Error:" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("zzz----modifyMemberCard", "modifyMemberCard is success");
                mTvGroupCard.setText(newCard);
                //更新数据库
                try {
                    GroupFriend _groupFriend = dbManager.selector(GroupFriend.class).where("member_identifier", "=", mUserId).and("group_id", "=", _groupID).findFirst();
                    if (_groupFriend != null) {
                        _groupFriend.setMemberCard(newCard);
                        dbManager.saveOrUpdate(_groupFriend);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //退出并删除
    private void quitGroup() {
        TIMGroupManager.getInstance().quitGroup(_groupID, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-----quitGroup", i + "Error:" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("zzz-----quitGroup", "quitGroup is success");
                //清除本地数据库该条群聊信息
                try {
                    Group _group = dbManager.selector(Group.class).where("group_id", "=", _groupID).and("user_id", "=", mUserId).findFirst();
                    if (_group != null) {
                        dbManager.delete(_group);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
