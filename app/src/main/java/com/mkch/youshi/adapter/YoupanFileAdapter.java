package com.mkch.youshi.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChooseFileActivity;
import com.mkch.youshi.activity.DropBoxFileActivity;
import com.mkch.youshi.config.MyApplication;
import com.mkch.youshi.model.YoupanFile;
import com.mkch.youshi.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/10/27
 */

public class YoupanFileAdapter extends BaseAdapter {
    private ArrayList<YoupanFile> youpanFiles;
    private DropBoxFileActivity activity;
    private int[] fileImg = new int[]{R.drawable.txt, R.drawable.jpg,
            R.drawable.doc, R.drawable.ppt,
            R.drawable.mp4,R.drawable.defaultx};


    public YoupanFileAdapter(ArrayList<YoupanFile> youpanFiles, DropBoxFileActivity activity) {
        this.youpanFiles = youpanFiles;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return youpanFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return youpanFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = UIUtils.inflate(R.layout.item_list_choose_file);
        YoupanFile youpanFile = youpanFiles.get(position);
        ImageView ivFileImageItem = (ImageView) convertView.findViewById(R.id.iv_file_choose_image);
        //设置图标
        if (youpanFile.getSuf().equalsIgnoreCase("txt")) {//txt文档
            ivFileImageItem.setImageResource(fileImg[0]);
        } else if (youpanFile.getSuf().equalsIgnoreCase("jpg")) {//txt文档
            ivFileImageItem.setImageResource(fileImg[1]);
        } else if (youpanFile.getSuf().equalsIgnoreCase("doc")) {//doc文档
            ivFileImageItem.setImageResource(fileImg[2]);
        } else if (youpanFile.getSuf().equalsIgnoreCase("ppt")) {//ppt文档
            ivFileImageItem.setImageResource(fileImg[3]);
        } else if (youpanFile.getSuf().equalsIgnoreCase("mp4")) {//mp4文档
            ivFileImageItem.setImageResource(fileImg[4]);
        } else {//默认文件图标
            ivFileImageItem.setImageResource(fileImg[5]);
        }

        TextView tvFileNameItem = (TextView) convertView.findViewById(R.id.tv_file_choose_name);
        tvFileNameItem.setText(youpanFile.getName());
        TextView tvFileTimeItem = (TextView) convertView.findViewById(R.id.tv_file_choose_time);
        tvFileTimeItem.setText(youpanFile.getCreate_time());
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cb_complete);
        final LinearLayout llcheckBox = (LinearLayout) convertView.findViewById(R.id.ll_cb_complete);

        llcheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
                boolean isChoosed = activity.addChoosedNumber(checkBox.isChecked());
                if (!isChoosed){
                    checkBox.setChecked(!checkBox.isChecked());
                }
                //将选中的数据添加到数据集合中,这个集合需要放在Application中;

            }
        });


        return convertView;
    }
}
