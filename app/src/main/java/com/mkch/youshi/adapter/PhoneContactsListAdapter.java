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
public class PhoneContactsListAdapter extends BaseAdapter implements ListAdapter{
    private Integer[] phoneContactsHead = new Integer[]{R.drawable.maillist,R.drawable.maillist};
    private String[] phoneContactsName = new String[]{"张三","李四"};
    private Context mContext;

    public PhoneContactsListAdapter(){
    }

    public PhoneContactsListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
        public int getCount() {
        return phoneContactsName.length!=0?phoneContactsName.length:0;
    }

    @Override
    public Object getItem(int position) {return phoneContactsName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_phone_contacts, null);
        }
        ImageView ivPhoneContactsItem = (ImageView) convertView.findViewById(R.id.iv_phone_contacts_head);
        ivPhoneContactsItem.setImageResource(phoneContactsHead[position]);
        TextView tvPhoneContactsItem = (TextView) convertView.findViewById(R.id.tv_phone_contacts_name);
        tvPhoneContactsItem.setText(phoneContactsName[position]);
        return convertView;
    }
}
