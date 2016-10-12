package com.mkch.youshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.Equipment;

import java.util.ArrayList;

/**
 * 收藏文件列表
 */
public class CommonEquipmentListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Equipment> equipments = new ArrayList<>();
    private Context mContext;

    public CommonEquipmentListAdapter() {
    }

    public CommonEquipmentListAdapter(Context context, ArrayList<Equipment> equipments) {
        this.mContext = context;
        this.equipments = equipments;
    }

    @Override
    public int getCount() {
        return equipments.size() != 0 ? equipments.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return equipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_common_equipment, null);
        }
        TextView tvEquipmentNameItem = (TextView) convertView.findViewById(R.id.tv_common_equipment_name);
        tvEquipmentNameItem.setText(equipments.get(position).getEquipmentName());
        TextView tvEquipmentTypeItem = (TextView) convertView.findViewById(R.id.tv_common_equipment_type);
        tvEquipmentTypeItem.setText(equipments.get(position).getEquipmentType());
        return convertView;
    }
}
