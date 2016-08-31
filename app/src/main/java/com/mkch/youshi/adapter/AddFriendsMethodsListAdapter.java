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
public class AddFriendsMethodsListAdapter extends BaseAdapter implements ListAdapter{
    private Integer[] addMethodsPics = new Integer[]{R.drawable.maillist,R.drawable.code};
    private String[] addMethodsTitles = new String[]{"手机通讯录","扫一扫"};
    private Context mContext;

    public AddFriendsMethodsListAdapter(){
    }

    public  AddFriendsMethodsListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
        public int getCount() {
        return addMethodsTitles.length!=0?addMethodsTitles.length:0;
    }

    @Override
    public Object getItem(int position) {return addMethodsTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_add_friends, null);
        }
        ImageView ivAddFriendsItem = (ImageView) convertView.findViewById(R.id.iv_add_friend_image);
        ivAddFriendsItem.setImageResource(addMethodsPics[position]);
        TextView tvAddFriendsItem = (TextView) convertView.findViewById(R.id.tv_add_friend_name);
        tvAddFriendsItem.setText(addMethodsTitles[position]);
        return convertView;
    }
}
