package com.mkch.youshi.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.location.Poi;
import com.mkch.youshi.R;
import com.mkch.youshi.util.UIUtils;

import java.util.List;

/**
 * Created by Smith on 2016/10/11.
 */

public class AddressAdapter extends BaseAdapter {
    List<Poi> list;

    public AddressAdapter(List<Poi> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View AddressView = UIUtils.inflate(R.layout.address_item);
        TextView tvAddress = (TextView) AddressView.findViewById(R.id.tv_address);
        tvAddress.setText(list.get(position).getName());
        return AddressView;
    }
}
