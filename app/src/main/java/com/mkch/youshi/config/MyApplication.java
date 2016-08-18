package com.mkch.youshi.config;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by SunnyJiang on 2016/8/16.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);// 是否输出debug日志, 开启debug会影响性能.
    }
}
