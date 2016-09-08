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
 * 文件和收藏列表
 * Created by SunnyJiang on 2016/8/18.
 */
public class ScheduleDetailAdapter extends BaseAdapter implements ListAdapter {
    private Integer[] detailImg = new Integer[]{R.drawable.jpg, R.drawable.jpg, R.drawable.mp4, R.drawable.txt};
    private String[] detailName = new String[]{"周末一起烧烤图片1.jpg", "烧烤图片2.jpg", "烧烤视频.mp4", "烧烤人员名单.txt"};
    private String[] detailTime = new String[]{"15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51"};
    private Context mContext;

    public ScheduleDetailAdapter() {
    }

    public ScheduleDetailAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return detailName.length != 0 ? detailName.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return detailName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_file, null);
        }
        ImageView ivDetailImageItem = (ImageView) convertView.findViewById(R.id.iv_file_image);
        ivDetailImageItem.setImageResource(detailImg[position]);
        TextView tvDetailNameItem = (TextView) convertView.findViewById(R.id.tv_file_name);
        tvDetailNameItem.setText(detailName[position]);
        TextView tvDetailTimeItem = (TextView) convertView.findViewById(R.id.tv_file_time);
        tvDetailTimeItem.setText(detailTime[position]);
        return convertView;
    }
}
