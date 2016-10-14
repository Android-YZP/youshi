package com.mkch.youshi.Hoder;

import android.view.View;

/**
 * Created by Smith on 2016/10/12.
 */

public abstract class MyBaseHolder<T>{
    private View mRootView;//一个Item的根布局
    private T data;//一个item的数据

    //当new出这个布局的时候就会加载布局,初始化控件,设置tag
    public MyBaseHolder() {
        mRootView = initView();
        //3.打一个标记tag
        mRootView.setTag(this);
    }

    //1.加载布局文件
    //2.初始化控件 findViewById
    public abstract View initView();

    //返回item的布局对象
    public View getmRootView(){
        return mRootView;
    }

    //设置当前item的的数据

    public void setData (T data){
        this.data = data;
        refreshView(data);
    }
    // 获取当前item的数据
    public T getData() {
        return data;
    }

    public abstract void refreshView(T data);
}
