package com.mkch.youshi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.VibratorUtil;

import java.util.Calendar;

public class FriendsReceiver extends BroadcastReceiver {
    public static final int RECEIVE_REQUEST_ADD_FRIEND = 199;
    private User mUser;

    public FriendsReceiver() {
    }

    private Handler mHandler;

    public FriendsReceiver(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("yoshi.action.friendsbroadcast")) {
            final String _request_jid = intent.getStringExtra("_request_jid");
            Log.d("jlj", "FriendsReceiver---------------------onReceive-jid=" + _request_jid);
            mUser = CommonUtil.getUserInfo(context);

            //判断是否全天免打扰
            if (mUser.getDisturb() == null || !mUser.getDisturb()) {
                //如果全天免打扰关闭或者还没设置，接着判断是否开启夜间免打扰
                if (mUser.getNight() == null || !mUser.getNight()) {
                    //如果夜间免打扰关闭或者还没设置,只需要判断提示音、振动是否开启
                    if (mUser.getVibrate() == null || mUser.getVibrate()) {
                        VibratorUtil.Vibrate(context, 100);   //震动100ms
                    }
                } else {
                    //如果夜间免打扰开启，获取当前时间和22：00到8:00比较
                    Calendar cal = Calendar.getInstance();// 当前日期
                    int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
                    int minute = cal.get(Calendar.MINUTE);// 获取分钟
                    int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
                    final int start = 22 * 60;// 起始时间 22:00的分钟数
                    final int end = 8 * 60;// 结束时间 8:00的分钟数
                    if (minuteOfDay <= start && minuteOfDay >= end) {
                        //如果不处于夜间，再判断提示音、振动是否开启
                        if (mUser.getVibrate() == null || mUser.getVibrate()) {
                            VibratorUtil.Vibrate(context, 100);   //震动100ms
                        }
                    }
                }
            }
            //获取成功
            Message _msg = new Message();
            _msg.what = RECEIVE_REQUEST_ADD_FRIEND;
            _msg.obj = _request_jid;
            mHandler.sendMessage(_msg);
        }
    }
}
