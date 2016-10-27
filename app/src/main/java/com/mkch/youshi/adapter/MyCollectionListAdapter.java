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
 * Created by ZJ on 2016/8/18.
 */
public class MyCollectionListAdapter extends BaseAdapter implements ListAdapter {
    private Integer[] collectionImg = new Integer[]{R.drawable.txt, R.drawable.doc, R.drawable.ppt, R.drawable.jpg, R.drawable.mp4};
    private String[] collectionName = new String[]{"企业网站需求分析说明书.txt", "科技厅众创空间资料.doc", "众创基地相关资料.ppt", "宜兴众创基地图片.jpg", "众创基地相关视频.mp4"};
    private String[] collectionTime = new String[]{"15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51"};
    private Context mContext;

    public MyCollectionListAdapter() {
    }

    public MyCollectionListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return collectionName.length != 0 ? collectionName.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return collectionName[position];
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
        ImageView ivCollectionImageItem = (ImageView) convertView.findViewById(R.id.iv_file_image);
        ivCollectionImageItem.setImageResource(collectionImg[position]);
        TextView tvCollectionNameItem = (TextView) convertView.findViewById(R.id.tv_file_name);
        tvCollectionNameItem.setText(collectionName[position]);
        TextView tvCollectionTimeItem = (TextView) convertView.findViewById(R.id.tv_file_time);
        tvCollectionTimeItem.setText(collectionTime[position]);
        return convertView;
    }
}
