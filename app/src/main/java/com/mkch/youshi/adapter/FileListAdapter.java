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
public class FileListAdapter extends BaseAdapter implements ListAdapter{
    private Integer[] fileImg = new Integer[]{R.drawable.txt,R.drawable.doc,R.drawable.ppt,R.drawable.jpg,R.drawable.mp4};
    private String[] fileName = new String[]{"企业网站需求分析说明书","科技厅众创空间资料","众创基地相关资料","宜兴众创基地图片","众创基地相关视频"};
    private String[] fileTime = new String[]{"15-09-21    09:51","15-09-21    09:51","15-09-21    09:51","15-09-21    09:51","15-09-21    09:51"};
    private Context mContext;

    public FileListAdapter(){
    }

    public FileListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
        public int getCount() {
        return fileName.length!=0?fileName.length:0;
    }

    @Override
    public Object getItem(int position) {return fileName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_file, null);
        }
        ImageView ivFileImageItem = (ImageView) convertView.findViewById(R.id.iv_file_image);
        ivFileImageItem.setImageResource(fileImg[position]);
        TextView tvFileNameItem = (TextView) convertView.findViewById(R.id.tv_file_name);
        tvFileNameItem.setText(fileName[position]);
        TextView tvFileTimeItem = (TextView) convertView.findViewById(R.id.tv_file_time);
        tvFileTimeItem.setText(fileTime[position]);
        return convertView;
    }
}
