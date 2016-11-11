package com.mkch.youshi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ChatReceiver extends BroadcastReceiver {
    public static final int RECEIVE_CHAT_MSG = 198;

    public ChatReceiver() {
    }

    private Handler mHandler;

    public ChatReceiver(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("yoshi.action.chatsbroadcast")) {
            final int chat_id = intent.getIntExtra("chat_id", 0);
            final int member_id = intent.getIntExtra("member_id", 0);
            Log.d("zzz", "ChatReceiver---------------------onReceive-chat_id=" + chat_id);
            //获取成功
            Message _msg = new Message();
            Bundle b = new Bundle();
            b.putInt("chat_id",chat_id);
            b.putInt("member_id",member_id);
            _msg.what = RECEIVE_CHAT_MSG;
            _msg.setData(b);
            mHandler.sendMessage(_msg);
        }
    }
}
