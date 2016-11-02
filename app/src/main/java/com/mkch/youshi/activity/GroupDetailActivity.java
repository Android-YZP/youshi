package com.mkch.youshi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.GroupFriendListAdapter;
import com.mkch.youshi.bean.GroupFriend;
import com.mkch.youshi.util.DBHelper;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import org.xutils.DbManager;
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
    private List<GroupFriend> mGroupFriends = new ArrayList<>();
    private GroupFriendListAdapter mAdapter;
    private String _groupID;
    private DbManager dbManager;//数据库管理对象
    private List<String> mUsers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        dbManager = DBHelper.getDbManager();
        Intent _intent = getIntent();
        if (_intent != null) {
            _groupID = _intent.getStringExtra("groupID");
            TIMGroupManager.getInstance().getGroupMembers(_groupID, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
                @Override
                public void onError(int i, String s) {
                    Log.d("zzz-----getGroupMembers", i + "Error:" + s);
                }

                @Override
                public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                    Log.d("zzz-----getGroupMembers", "getGroupMembers is success");
                    //获取群聊成员
                    for (TIMGroupMemberInfo info : timGroupMemberInfos) {
                        String memberCard = info.getNameCard();
                        TIMGroupMemberRoleType memberRole = info.getRole();
                        //先插入群成员名片和角色
                        GroupFriend groupFriend = new GroupFriend();
                        groupFriend.setMemberCard(memberCard);
                        groupFriend.setMemberRole(memberRole);
                        mGroupFriends.add(groupFriend);
                        //待获取用户资料的群成员列表
                        String memberIdentifier = info.getUser();
                        mUsers.add(memberIdentifier);
                    }
                    //通过identifier获取群成员头像和昵称
                    getGroupMemberHead(mUsers);
                }
            });
        }
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
//                    //存储所有的群成员信息
//                    try {
//                        GroupFriend _groupFriend = dbManager.selector(GroupFriend.class).where("member_name", "=", memberName).findFirst();
//                        if (_groupFriend != null) {
//                            //有就更改他的字段
//                            _groupFriend.setMemberName(memberName);
//                            _groupFriend.setMemberHead(memberHead);
//                            _groupFriend.setMemberRole(memberRole);
//                        } else {
//                            //没有就插入
//                            GroupFriend groupFriend1 = new GroupFriend(memberName, memberHead, memberRole);
//                            dbManager.save(groupFriend1);
//                        }
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                }
                //插入默认图片
                GroupFriend groupFriend = new GroupFriend();
                mGroupFriends.add(groupFriend);
                mAdapter = new GroupFriendListAdapter(GroupDetailActivity.this, mGroupFriends);
                //设置适配器
                mGvFriendHeadpics.setAdapter(mAdapter);
            }
        });
    }

    @Event({R.id.iv_common_topbar_back})
    private void clickButtons(View view) {
        switch (view.getId()) {
            case R.id.iv_common_topbar_back://返回按钮
                this.finish();//返回
                break;
        }
    }
}
