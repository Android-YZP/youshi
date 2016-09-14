package com.mkch.youshi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.util.XmppStringUtils;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

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
            final String _request_jid = intent.getStringExtra("_request_jid");
            Log.d("jlj","FriendsReceiver---------------------onReceive-jid="+_request_jid);
            //获取成功
            Message _msg = new Message();
            _msg.what = RECEIVE_REQUEST_ADD_FRIEND;
            _msg.obj = _request_jid;
            mHandler.sendMessage(_msg);
        }
    }
}
