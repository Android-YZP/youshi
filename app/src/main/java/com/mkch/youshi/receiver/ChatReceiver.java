package com.mkch.youshi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ChatReceiver extends BroadcastReceiver {
    public static final int RECEIVE_CHAT_MSG = 198;
    public ChatReceiver() {
    }

    private Handler mHandler;
    public ChatReceiver(Handler handler){
        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("yoshi.action.chatsbroadcast")){
            final int chat_id = intent.getIntExtra("chat_id",0);
            Log.d("jlj","ChatReceiver---------------------onReceive-chat_id="+chat_id);
            //获取成功
            Message _msg = new Message();
            _msg.what = RECEIVE_CHAT_MSG;
            _msg.obj = chat_id;
            mHandler.sendMessage(_msg);
        }
    }
}
