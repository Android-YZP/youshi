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
 * 收藏文件列表
 * Created by SunnyJiang on 2016/8/18.
 */
public class CommonEquipmentListAdapter extends BaseAdapter implements ListAdapter {
    private String[] equipmentName = new String[]{"小米4c", "华为p9", "三星note7", "魅族pro6", "vivo X7"};
    private String[] equipmentType = new String[]{"android-22", "android-22", "android-22", "android-22", "android-22"};
    private Context mContext;

    public CommonEquipmentListAdapter() {
    }

    public CommonEquipmentListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return equipmentName.length != 0 ? equipmentName.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return equipmentName[position];
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
        tvEquipmentNameItem.setText(equipmentName[position]);
        TextView tvEquipmentTimeItem = (TextView) convertView.findViewById(R.id.tv_common_equipment_type);
        tvEquipmentTimeItem.setText(equipmentType[position]);
        return convertView;
    }
}
