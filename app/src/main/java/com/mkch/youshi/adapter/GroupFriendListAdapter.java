package com.mkch.youshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.GroupFriend;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by SunnyJiang on 2016/9/5.
 */
public class GroupFriendListAdapter extends BaseAdapter {
    private Context mContext;
    private List<GroupFriend> mGroupFriends;
    private LayoutInflater mLayoutInflater;

    public GroupFriendListAdapter(Context mContext, List<GroupFriend> mGroupFriends) {
        this.mContext = mContext;
        this.mGroupFriends = mGroupFriends;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mGroupFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroupFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupFriendViewHolder groupFriendViewHolder = null;
        if (convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.gv_item_group_friend_one_friend,null);
            groupFriendViewHolder = new GroupFriendViewHolder();
            x.view().inject(groupFriendViewHolder,convertView);
            convertView.setTag(groupFriendViewHolder);
        }else{
            groupFriendViewHolder = (GroupFriendViewHolder) convertView.getTag();
        }
        if (mGroupFriends!=null){
            GroupFriend groupFriend = mGroupFriends.get(position);
            if (groupFriend!=null){
                if (position==mGroupFriends.size()-1){
                    //若是gridview最后一个，则直接展示默认图片
                    groupFriendViewHolder.iv_group_friend_headpic.setBackgroundResource(R.drawable.group_detail_add);
                }else{
                    //设置头像
                    ImageOptions _image_options = new ImageOptions.Builder()
                            .setCircular(true)
                            .build();
                    x.image().bind(groupFriendViewHolder.iv_group_friend_headpic,groupFriend.getHeadpic(),_image_options);
                    //设置名称
                    groupFriendViewHolder.tv_group_friend_nickname.setText(groupFriend.getNickname());
                }
            }
        }


        return convertView;
    }

    private class GroupFriendViewHolder{
        @ViewInject(R.id.iv_item_one_friend_headpic)
        private ImageView iv_group_friend_headpic;
        @ViewInject(R.id.tv_item_one_friend_nickname)
        private TextView tv_group_friend_nickname;

    }
}
