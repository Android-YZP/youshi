package com.mkch.youshi.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.model.CollectFile;
import com.mkch.youshi.model.YoupanFile;
import com.mkch.youshi.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Smith on 2016/10/27
 */

public class CollectFileAdapter extends BaseAdapter {
    private ArrayList<CollectFile> collectFiles;
    private int[] fileImg = new int[]{R.drawable.txt, R.drawable.jpg,
            R.drawable.doc, R.drawable.ppt,
            R.drawable.mp4, R.drawable.defaultx};

    public CollectFileAdapter(ArrayList<CollectFile> collectFiles) {
        this.collectFiles = collectFiles;
        //初始化点击事件的值
    }

    @Override
    public int getCount() {
        return collectFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return collectFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = UIUtils.inflate(R.layout.item_list_choose_file);
        final CollectFile collectFile = collectFiles.get(position);
        ImageView ivFileImageItem = (ImageView) convertView.findViewById(R.id.iv_file_choose_image);
        //设置图标
        if (collectFile.getSuf().equalsIgnoreCase("txt")) {//txt文档
            ivFileImageItem.setImageResource(fileImg[0]);
        } else if (collectFile.getSuf().equalsIgnoreCase("jpg")) {//txt文档
            ivFileImageItem.setImageResource(fileImg[1]);
        } else if (collectFile.getSuf().equalsIgnoreCase("doc")) {//doc文档
            ivFileImageItem.setImageResource(fileImg[2]);
        } else if (collectFile.getSuf().equalsIgnoreCase("ppt")) {//ppt文档
            ivFileImageItem.setImageResource(fileImg[3]);
        } else if (collectFile.getSuf().equalsIgnoreCase("mp4")) {//mp4文档
            ivFileImageItem.setImageResource(fileImg[4]);
        } else {//默认文件图标
            ivFileImageItem.setImageResource(fileImg[5]);
        }
        TextView tvFileNameItem = (TextView) convertView.findViewById(R.id.tv_file_choose_name);
        tvFileNameItem.setText(collectFile.getName());
        TextView tvFileTimeItem = (TextView) convertView.findViewById(R.id.tv_file_choose_time);
        tvFileTimeItem.setText(collectFile.getCreate_time());
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cb_complete);
        final LinearLayout llcheckBox = (LinearLayout) convertView.findViewById(R.id.ll_cb_complete);
        llcheckBox.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        return convertView;
    }
}
