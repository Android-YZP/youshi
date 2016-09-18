package com.mkch.youshi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.NewFriendActivity;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.receiver.FriendsReceiver;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.XmppHelper;

import org.apache.http.conn.ConnectTimeoutException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.json.JSONObject;
import org.jxmpp.util.XmppStringUtils;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Collection;

/**
 * Created by SunnyJiang on 2016/9/14.
 */
public class FriendService extends Service implements RosterListener {
    private static final int FRIEND_ADD_REQUEST_SUCCESS = 10;
    /**
     * XMPP
     */
    private XMPPTCPConnection connection;
    private Roster mRoster;

    //通知
    private NotificationManager mNotification_manager;
    private Notification mNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //通知service
        mNotification_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mainxmpp();//进入首页后，xmpp的所有操作


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 进入首页后，xmpp的所有操作
     */
    private void mainxmpp() {
        //获取连接
        connection = XmppHelper.getConnection();
        //用户自动登录
        User _user = CommonUtil.getUserInfo(this);
        final String _login_user = _user.getOpenFireUserName();
        final String _login_pwd = _user.getPassword();
        Log.d("jlj","MainActivity----------------------mainxmpp="+_login_user+","+_login_pwd);
        //开启副线程-登录-设置状态
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!connection.isConnected()){
                        connection.connect();
                    }
                    connection.login(_login_user, _login_pwd);
                    Presence presence = new Presence(Presence.Type.available);
                    presence.setStatus("我是在线状态");
                    connection.sendStanza(presence);


                }  catch (Exception e){
                    e.printStackTrace();
                    Log.d("jlj","--------------------------"+e.getMessage());
                    connection.disconnect();
                    return;
                }
                addAllXmppListener();//添加所有的监听器

            }
        }).start();


    }


    /**
     * 添加所有的监听器
     */
    private void addAllXmppListener() {
        Log.d("jlj","MainActivity----------------------addAllXmppListener");
        //接收好友请求监听
        mRoster = Roster.getInstanceFor(connection);
        //监听好友请求添加========================================
        connection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                if (packet instanceof Presence){
                    Presence _p = (Presence)packet;
                    //获取请求者
                    String _request_jid = _p.getFrom();
                    String _to_jid = _p.getTo();
                    if (_p.getType() == Presence.Type.subscribe){
                        Log.d("jlj","-------------------from:"+_request_jid+",to:"+_to_jid+"=subscribe");
                        //如果我没有此JID的好友，才弹出对话框选择是否需要接受好友请求
                        RosterEntry _entry = mRoster.getEntry(_request_jid);
                        if (_entry==null){
//                            Message _message = mHandler.obtainMessage(MSG_OPEN_REV_FRIEND_DIALOG, _request_jid);
//                            mHandler.sendMessage(_message);
                            //发送广播通知，有好友请求添加
                            Log.d("jlj","MainActivity-------------好友请求，"+_request_jid);
                            //直接存入数据库，该好友信息
                            saveFriendToDB(_request_jid);

                        }else{
                            Log.d("jlj","---------------------------该好友已添加");
                        }


                    }else if (_p.getType() == Presence.Type.subscribed){
                        Log.d("jlj","-------------------from:"+_request_jid+",to:"+_to_jid+"=subscribed");
                    }else if (_p.getType() == Presence.Type.unsubscribe){
                        Log.d("jlj","-------------------from:"+_request_jid+",to:"+_to_jid+"=unsubscribe");
                    }else if (_p.getType() == Presence.Type.unsubscribed){
                        Log.d("jlj","-------------------from:"+_request_jid+",to:"+_to_jid+"=unsubscribed");
                    }else if (_p.getType() == Presence.Type.available){
                        Log.d("jlj","-------------------from:"+_request_jid+",to:"+_to_jid+"=上线");
                    }else if (_p.getType() == Presence.Type.unavailable){
                        Log.d("jlj","-------------------from:"+_request_jid+",to:"+_to_jid+"=下线");
                    }else{
                        Log.d("jlj","-------------------from:"+_request_jid+",to:"+_to_jid+"=其他");
                    }
                }
            }
        }, new StanzaFilter() {
            @Override
            public boolean accept(Stanza stanza) {
                //没有选择，全部接收
                return true;
            }
        });
        //监听是否需要更新好友列表========================================
        mRoster.addRosterListener(this);
    }

    /**
     * 查询该用户信息并存储于数据库
     * @param request_jid
     */
    private void saveFriendToDB(final String request_jid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //自己的信息
                final User _self_user = CommonUtil.getUserInfo(FriendService.this);

                //获取该jid的用户名
                String _openfire_username = XmppStringUtils.parseLocalpart(request_jid);
                Log.d("jlj","saveFriendToDB---------------------_openfire_username="+_openfire_username);
                //根据openfireusername查询该用户的信息，并保存于数据库
                RequestParams requestParams = new RequestParams(CommonConstants.GetInfoByOpenFireName);
//                _openfire_username = "165094350";//test
                //包装请求参数
                String _req_json = "{\"OpenFireUserName\":\"" + _openfire_username + "\"}";
                Log.d("jlj","saveFriendToDB------------------onReceive-req_json="+_req_json+","+CommonUtil.getUserInfo(FriendService.this).getLoginCode());
                requestParams.addBodyParameter("", _req_json);//用户名
                requestParams.addHeader("sVerifyCode", _self_user.getLoginCode());//头信息
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("jlj","saveFriendToDB---------------------onSuccess:"+result);
                        if (result != null) {
                            //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                            try {
                                JSONObject _json_result = new JSONObject(result);
                                Boolean _success = (Boolean) _json_result.get("Success");
                                if (_success) {
                                    JSONObject datas = _json_result.getJSONObject("Datas");
                                    String _user_json_str = datas.toString();
                                    Log.d("jlj","FriendsReceiver---------------------datas = "+_user_json_str);
                                    if (_user_json_str!=null&&!_user_json_str.equals("")){
                                        Gson _gson = new Gson();
                                        User _user = _gson.fromJson(_user_json_str, User.class);
                                        //保存用户请求列表
                                        DbManager dbManager = DBHelper.getDbManager();
                                        Friend _friend = new Friend();
                                        _friend.setStatus(2);//待接收
                                        _friend.setHead_pic(_user.getHeadPic());//头像
                                        _friend.setNickname(_user.getNickName()==null?_user.getOpenFireUserName():_user.getNickName());//昵称
                                        _friend.setRemark(_user.getRealName());//备注
                                        _friend.setPhone(_user.getMobileNumber());//手机号码
                                        _friend.setUserid(_self_user.getOpenFireUserName());//自己的openfire用户名
                                        _friend.setFriendid(_user.getOpenFireUserName());//好友的openfire用户名
                                        dbManager.save(_friend);
                                    }
                                    Message _msg = new Message();
                                    _msg.what = FRIEND_ADD_REQUEST_SUCCESS;
                                    _msg.obj = request_jid;
                                    mHandler.sendMessage(_msg);

                                } else {
                                    String _Message = _json_result.getString("Message");
                                    String _ErrorCode = _json_result.getString("ErrorCode");
                                    if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                        Log.d("jlj", "getInfoByOpenFireName-------------1001");
                                    } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                        Log.d("jlj", "getInfoByOpenFireName-------------1002");
                                    } else {
                                        CommonUtil.sendErrorMessage(_Message, mHandler);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.d("jlj","receiver-----------onError"+ex.getMessage());
                        //使用handler通知UI提示用户错误信息
                        if (ex instanceof ConnectException) {
                            CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, mHandler);
                        } else if (ex instanceof ConnectTimeoutException) {
                            CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, mHandler);
                        } else if (ex instanceof SocketTimeoutException) {
                            CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, mHandler);
                        } else {
                            CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, mHandler);
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });


            }
        }).start();
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int _what = msg.what;
            switch (_what){
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Log.d("jlj","FriendService-Handler-errorMsg-----------------------"+errorMsg);

                    break;
                case FRIEND_ADD_REQUEST_SUCCESS:
                    String _request_jid = (String) msg.obj;
                    Log.d("jlj","FriendService-Handler-_request_jid-----------------------"+_request_jid);
                    actionToNotify(_request_jid);//去通知界面采取相应的动作
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);

        }
    };

    /**
     * 去通知界面采取相应的动作
     * @param _request_jid
     */
    private void actionToNotify(String _request_jid) {
        //发送广播：若UI处于显示状态，则通知界面更新UI；若UI不处于显示状态，弹出通知栏，显示信息条数和最新的信息。
        boolean isForeground = CommonUtil.isForeground(FriendService.this, "com.mkch.youshi.activity.NewFriendActivity");
        if (isForeground){
            Intent _intent = new Intent();
            _intent.setAction("yoshi.action.friendsbroadcast");
            _intent.putExtra("_request_jid",_request_jid);
            sendBroadcast(_intent);
        }else{
            notifyInfo(_request_jid);
        }
    }


    /**
     * 通知有好友请求
     * @param content
     */
    private void notifyInfo(String content) {
        Notification.Builder _builder = new Notification.Builder(this);

        //intent
        Intent _intent = new Intent(this, NewFriendActivity.class);
        _intent.putExtra("_request_jid",content);

        //头像
        Bitmap _bitmap =((BitmapDrawable)(getResources().getDrawable(R.drawable.maillist))).getBitmap();
        //pendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //昵称

        //builder设置一些参数
        _builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(_bitmap)
                .setContentTitle("好友请求")
                .setContentText(content+"请求添加您为好友")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent,true);

        mNotification = _builder.build();
        //通知
        mNotification_manager.notify(0,mNotification);
    }

    @Override
    public void entriesAdded(Collection<String> addresses) {
        Log.d("JLJ","------------------entriesAdded");
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        Log.d("JLJ","------------------entriesUpdated");
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        Log.d("JLJ","------------------entriesDeleted");
    }

    @Override
    public void presenceChanged(Presence presence) {
        Log.d("JLJ","------------------presenceChanged");
    }
}
