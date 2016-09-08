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
 * 日程文件列表
 * Created by SunnyJiang on 2016/8/18.
 */
public class ScheduleListAdapter extends BaseAdapter implements ListAdapter {
    private Integer[] scheduleImg = new Integer[]{R.drawable.file, R.drawable.file, R.drawable.file, R.drawable.file};
    private String[] scheduleName = new String[]{"周末一起烧烤", "下午两点开会", "会议", "众创会议"};
    private String[] scheduleTime = new String[]{"15-09-21    09:51      发起人：王伟", "15-09-21    09:51      发起人：王伟", "15-09-21    09:51      发起人：王伟", "15-09-21    09:51      发起人：王伟"};
    private Context mContext;

    public ScheduleListAdapter() {
    }

    public ScheduleListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return scheduleName.length != 0 ? scheduleName.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return scheduleName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_schedule_file, null);
        }
        ImageView ivScheduleImageItem = (ImageView) convertView.findViewById(R.id.iv_schedule_file_image);
        ivScheduleImageItem.setImageResource(scheduleImg[position]);
        TextView tvScheduleNameItem = (TextView) convertView.findViewById(R.id.tv_schedule_file_name);
        tvScheduleNameItem.setText(scheduleName[position]);
        TextView tvScheduleTimeItem = (TextView) convertView.findViewById(R.id.tv_schedule_file_time);
        tvScheduleTimeItem.setText(scheduleTime[position]);
        return convertView;
    }
}
