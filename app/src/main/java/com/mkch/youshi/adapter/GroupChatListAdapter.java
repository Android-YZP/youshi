package com.mkch.youshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mkch.youshi.R;

/**
 * 添加好友方式列表
 * Created by SunnyJiang on 2016/8/18.
 */
public class GroupChatListAdapter extends BaseAdapter implements ListAdapter{
    private Integer[] groupChatPics = new Integer[]{R.drawable.maillist,R.drawable.maillist,R.drawable.maillist};
    private String[] groupChatNames = new String[]{"群聊名称1","群聊名称2","群聊名称3"};
    private Context mContext;

    public GroupChatListAdapter(){
    }

    public GroupChatListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
        public int getCount() {
        return groupChatNames.length!=0?groupChatNames.length:0;
    }

    @Override
    public Object getItem(int position) {return groupChatNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_group_chat, null);
        }
        ImageView ivAddFriendsItem = (ImageView) convertView.findViewById(R.id.iv_group_chat_image);
        ivAddFriendsItem.setImageResource(groupChatPics[position]);
        TextView tvAddFriendsItem = (TextView) convertView.findViewById(R.id.tv_group_chat_name);
        tvAddFriendsItem.setText(groupChatNames[position]);
        return convertView;
    }
}
