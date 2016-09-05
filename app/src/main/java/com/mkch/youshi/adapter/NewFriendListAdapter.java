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
 * 新的朋友列表
 * Created by SunnyJiang on 2016/8/18.
 */
public class NewFriendListAdapter extends BaseAdapter implements ListAdapter{
    private Integer[] newFriendHead = new Integer[]{R.drawable.maillist,R.drawable.maillist};
    private String[] newFriendName = new String[]{"张三","李四"};
    private String[] newFriendRemark = new String[]{"我是张三","我是李四"};
    private Context mContext;

    public NewFriendListAdapter(){
    }

    public NewFriendListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
        public int getCount() {
        return newFriendName.length!=0?newFriendName.length:0;
    }

    @Override
    public Object getItem(int position) {return newFriendName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_new_friend, null);
        }
        ImageView ivNewFriendItem = (ImageView) convertView.findViewById(R.id.iv_new_friend_head);
        ivNewFriendItem.setImageResource(newFriendHead[position]);
        TextView tvNewFriendNameItem = (TextView) convertView.findViewById(R.id.tv_new_friend_name);
        tvNewFriendNameItem.setText(newFriendName[position]);
        TextView tvNewFriendRemarkItem = (TextView) convertView.findViewById(R.id.tv_new_friend_remark);
        tvNewFriendRemarkItem.setText(newFriendRemark[position]);
        return convertView;
    }
}
