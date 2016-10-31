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
import com.mkch.youshi.model.Group;

import org.xutils.x;

import java.util.List;

/**
 * 群聊列表
 * Created by SunnyJiang on 2016/8/18.
 */
public class GroupChatListAdapter extends BaseAdapter implements ListAdapter {
    private List<Group> listGroups;
    private Context mContext;

    public GroupChatListAdapter() {
    }

    public GroupChatListAdapter(Context context, List<Group> listGroups) {
        this.mContext = context;
        this.listGroups = listGroups;
    }

    @Override
    public int getCount() {
        return listGroups.size() != 0 ? listGroups.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return listGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_group_chat, null);
        }
        ImageView ivGroupChatItem = (ImageView) convertView.findViewById(R.id.iv_group_chat_image);
        x.image().bind(ivGroupChatItem, listGroups.get(position).getGroupHead());
        TextView tvGroupChatItem = (TextView) convertView.findViewById(R.id.tv_group_chat_name);
        tvGroupChatItem.setText(listGroups.get(position).getGroupName());
        return convertView;
    }
}
