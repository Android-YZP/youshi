package com.mkch.youshi.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mkch.youshi.Hoder.MyBaseHolder;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/10/12.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private ArrayList<T> data;

    public MyBaseAdapter(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyBaseHolder<T> myBaseHolder;
        if (convertView == null){
            //1.加载布局文件
            //2,初始化控件findViewById
            //3.打一个标记tag
            myBaseHolder = gethoder();
        }else {
            myBaseHolder = (MyBaseHolder<T>) convertView.getTag();
        }
        //设置内容刷新界面
        myBaseHolder.setData(getItem(position));
        return myBaseHolder.getmRootView();
    }

    public abstract MyBaseHolder<T> gethoder();


}
