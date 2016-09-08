package com.mkch.youshi.config;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by SunnyJiang on 2016/8/16.
 */
public class MyApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);// 是否输出debug日志, 开启debug会影响性能.

        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
