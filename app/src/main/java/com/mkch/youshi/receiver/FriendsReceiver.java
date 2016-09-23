package com.mkch.youshi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class FriendsReceiver extends BroadcastReceiver {
    public static final int RECEIVE_REQUEST_ADD_FRIEND = 199;

    public FriendsReceiver() {
    }

    private Handler mHandler;
    public FriendsReceiver(Handler handler){
        this.mHandler = handler;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("yoshi.action.friendsbroadcast")){
            final String _friend_json = intent.getStringExtra("_friend_json");
            Log.d("jlj","FriendsReceiver---------------------onReceive-_friend_json="+_friend_json);


            //获取成功
            Message _msg = new Message();
            _msg.what = RECEIVE_REQUEST_ADD_FRIEND;
            _msg.obj = _friend_json;
            mHandler.sendMessage(_msg);
        }
    }
}
