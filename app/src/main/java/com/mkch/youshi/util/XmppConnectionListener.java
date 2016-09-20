package com.mkch.youshi.util;

import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SunnyJiang on 2016/9/19.
 */
public class XmppConnectionListener implements ConnectionListener {
    private Timer tExit;
    private String username;
    private String password;
    private int logintime = 2000;

    public XmppConnectionListener(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void connected(XMPPConnection connection) {
        Log.d("jlj","---------------------------connected");

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d("jlj","---------------------------authenticated");
    }

    //连接正常断开
    @Override
    public void connectionClosed() {
        Log.d("jlj","---------------------------connectionClosed");
        //关闭连接
        XmppHelper.getConnection().disconnect();
        // 重连服务器
        tExit = new Timer();
        tExit.schedule(new timetask(), logintime);
    }

    //连接异常断开
    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d("jlj","---------------------------connectionClosedOnError");
        // 判斷為帳號已被登錄
        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {
            //关闭连接
            XmppHelper.getConnection().disconnect();
            // 重连服务器
            tExit = new Timer();
            tExit.schedule(new timetask(), logintime);
        }
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d("jlj","---------------------------reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d("jlj","---------------------------reconnectingIn");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d("jlj","---------------------------reconnectionFailed");
    }

    class timetask extends TimerTask {
        @Override
        public void run() {
            if (username != null && password != null) {
                Log.d("jlj", "timetask---------------------尝试登录");
                // 连接服务器
                XMPPTCPConnection connection = XmppHelper.getConnection();
                if (XmppHelper.connectAndLogin(connection,username, password)) {
                    Log.d("jlj", "timetask----------------------------登录成功");
                } else {
                    Log.d("TaxiConnectionListener", "重新登錄");
                    tExit.schedule(new timetask(), logintime);
                }
            }
        }
    }
}
