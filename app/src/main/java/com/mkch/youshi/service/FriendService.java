package com.mkch.youshi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChatActivity;
import com.mkch.youshi.activity.NewFriendActivity;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.ChatBean;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.MessageBox;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.util.XmppConnectionListener;
import com.mkch.youshi.util.XmppHelper;

import org.apache.http.conn.ConnectTimeoutException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.json.JSONObject;
import org.jxmpp.util.XmppStringUtils;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Created by SunnyJiang on 2016/9/14.
 */
public class FriendService extends Service implements RosterListener {
    private static final int FRIEND_ADD_REQUEST_SUCCESS = 10;

    private User mUser;
    /**
     * XMPP
     */
    private XMPPTCPConnection connection;
    private XmppConnectionListener xmppConnectionListener;//连接监听

    //好友
    private Roster mRoster;

    //通知
    private NotificationManager mNotification_manager;
    private Notification mNotification;


    //单聊
    private ChatManager chatmanager;
    private ChatManagerListener mChartManagerLisenter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mUser = CommonUtil.getUserInfo(this);
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
        connection.removeConnectionListener(xmppConnectionListener);
    }

    /**
     * 进入首页后，xmpp的所有操作
     */
    private void mainxmpp() {
        //获取连接
        connection = XmppHelper.getConnection();

        //用户自动登录
        final String _login_user = mUser.getOpenFireUserName();
        final String _login_pwd = mUser.getPassword();
        Log.d("jlj", "FriendsReceiver----------------------mainxmpp=" + _login_user + "," + _login_pwd);

        //添加断线重连监听
        xmppConnectionListener = new XmppConnectionListener(_login_user, _login_pwd);
        connection.addConnectionListener(xmppConnectionListener);

        //开启副线程-登录-设置状态
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean _logined = XmppHelper.connectAndLogin(connection, _login_user, _login_pwd);
                if (_logined) {
                    addAllXmppListener();//添加所有的监听器
                }


            }
        }).start();


    }


    /**
     * 添加所有的监听器
     */
    private void addAllXmppListener() {
        Log.d("jlj", "FriendsReceiver----------------------addAllXmppListener");
        addFriendListener();//添加好友请求监听
        addChatListener();//添加单聊监听
        addOffLineMessageListener();//添加离线消息监听

    }


    /**
     * 添加好友请求监听
     */
    private void addFriendListener() {
        //接收好友请求监听
        mRoster = Roster.getInstanceFor(connection);
        //监听好友请求添加========================================
        connection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                if (packet instanceof Presence) {
                    Presence _p = (Presence) packet;
                    //获取请求者
                    String _request_jid = _p.getFrom();
                    String _to_jid = _p.getTo();
                    if (_p.getType() == Presence.Type.subscribe) {
                        Log.d("jlj", "-------------------from:" + _request_jid + ",to:" + _to_jid + "=subscribe");
                        //如果我没有此JID的好友，才弹出对话框选择是否需要接受好友请求
                        RosterEntry _entry = mRoster.getEntry(_request_jid);
                        if (_entry == null) {
                            //发送广播通知，有好友请求添加
                            Log.d("jlj", "FriendsReceiver-------------好友请求，" + _request_jid);
                            //直接存入数据库，该好友信息
                            saveFriendToDB(_request_jid);

                        } else {
                            Log.d("jlj", "---------------------------该好友已添加");
                        }


                    } else if (_p.getType() == Presence.Type.subscribed) {
                        Log.d("jlj", "-------------------from:" + _request_jid + ",to:" + _to_jid + "=subscribed");
                    } else if (_p.getType() == Presence.Type.unsubscribe) {
                        Log.d("jlj", "-------------------from:" + _request_jid + ",to:" + _to_jid + "=unsubscribe");
                    } else if (_p.getType() == Presence.Type.unsubscribed) {
                        Log.d("jlj", "-------------------from:" + _request_jid + ",to:" + _to_jid + "=unsubscribed");
                    } else if (_p.getType() == Presence.Type.available) {
                        Log.d("jlj", "-------------------from:" + _request_jid + ",to:" + _to_jid + "=上线");
                    } else if (_p.getType() == Presence.Type.unavailable) {
                        Log.d("jlj", "-------------------from:" + _request_jid + ",to:" + _to_jid + "=下线");
                    } else {
                        Log.d("jlj", "-------------------from:" + _request_jid + ",to:" + _to_jid + "=其他");
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
     *
     * @param request_jid
     */
    private void saveFriendToDB(final String request_jid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //自己的信息
                mUser = CommonUtil.getUserInfo(FriendService.this);

                //获取该jid的用户名
                String _openfire_username = XmppStringUtils.parseLocalpart(request_jid);
                Log.d("jlj", "saveFriendToDB---------------------_openfire_username=" + _openfire_username);
                //根据openfireusername查询该用户的信息，并保存于数据库
                RequestParams requestParams = new RequestParams(CommonConstants.GetInfoByOpenFireName);
//                _openfire_username = "165094350";//test
                //包装请求参数
                String _req_json = "{\"OpenFireUserName\":\"" + _openfire_username + "\"}";
                Log.d("jlj", "saveFriendToDB------------------onReceive-req_json=" + _req_json + "," + mUser.getLoginCode());
                requestParams.addBodyParameter("", _req_json);//用户名
                requestParams.addHeader("sVerifyCode", mUser.getLoginCode());//头信息
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d("jlj", "saveFriendToDB---------------------onSuccess:" + result);
                        if (result != null) {
                            //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                            try {
                                JSONObject _json_result = new JSONObject(result);
                                Boolean _success = (Boolean) _json_result.get("Success");
                                if (_success) {
                                    JSONObject datas = _json_result.getJSONObject("Datas");
                                    String _user_json_str = datas.toString();
                                    Log.d("jlj", "FriendsReceiver---------------------datas = " + _user_json_str);
                                    String _friend_json = null;

                                    if (_user_json_str != null && !_user_json_str.equals("")) {
                                        Gson _gson = new Gson();
                                        User _user = _gson.fromJson(_user_json_str, User.class);
                                        String _self_user_openfirename = mUser.getOpenFireUserName();//自己的openfirename
                                        String _friend_openfirename = _user.getOpenFireUserName();//好友的openfirename

                                        //保存用户请求列表
                                        DbManager dbManager = DBHelper.getDbManager();
                                        //查询好友是否在我的好友列表，若存在，修改该好友信息；若不存在，新增该好友信息；
                                        Friend _friend = dbManager.selector(Friend.class)
                                                .where("userid", "=", _self_user_openfirename)
                                                .and("friendid", "=", _friend_openfirename)
                                                .findFirst();
                                        if (_friend == null) {
                                            Log.d("jlj", "FriendService-------------------------friend add");
                                            //新增该好友信息到数据库
                                            _friend = new Friend();
                                            _friend.setStatus(2);//待接收
                                            _friend.setShowinnewfriend(1);//接受好友请求
                                            if (_user.getHeadPic() != null && !_user.getHeadPic().equals("") && !_user.getHeadPic().equals("null")) {
                                                _friend.setHead_pic(CommonConstants.NOW_ADDRESS_PRE + _user.getHeadPic());//头像
                                            }
                                            if (_user.getNickName() != null && !_user.getNickName().equals("") && !_user.getNickName().equals("null")) {
                                                _friend.setNickname(_user.getNickName());//昵称
                                            }
                                            if (_user.getRealName() != null && !_user.getRealName().equals("") && !_user.getRealName().equals("null")) {
                                                _friend.setRemark(_user.getRealName());//备注
                                            }
                                            _friend.setPhone(_user.getMobileNumber());//手机号码
                                            _friend.setUserid(_self_user_openfirename);//自己的openfire用户名
                                            _friend.setFriendid(_friend_openfirename);//好友的openfire用户名
                                            dbManager.saveBindingId(_friend);
                                        } else {
                                            Log.d("jlj", "FriendService-------------------------friend update");
                                            //更新好友信息
                                            _friend.setStatus(2);//待接收
                                            _friend.setShowinnewfriend(1);//接受好友请求-显示状态
                                            if (_user.getHeadPic() != null && !_user.getHeadPic().equals("") && !_user.getHeadPic().equals("null")) {
                                                _friend.setHead_pic(CommonConstants.NOW_ADDRESS_PRE + _user.getHeadPic());//头像
                                            }
                                            if (_user.getNickName() != null && !_user.getNickName().equals("") && !_user.getNickName().equals("null")) {
                                                _friend.setNickname(_user.getNickName());//昵称
                                            }
                                            if (_user.getRealName() != null && !_user.getRealName().equals("") && !_user.getRealName().equals("null")) {
                                                _friend.setRemark(_user.getRealName());//备注
                                            }
                                            _friend.setPhone(_user.getMobileNumber());//手机号码
                                            _friend.setUserid(_self_user_openfirename);//自己的openfire用户名
                                            _friend.setFriendid(_friend_openfirename);//好友的openfire用户名
                                            dbManager.saveOrUpdate(_friend);
                                        }
                                        _friend_json = _gson.toJson(_friend);//该好友的信息json传输
                                    } else {
                                        Log.d("jlj", "-------------------------friend json is null");
                                        return;
                                    }
                                    Message _msg = new Message();
                                    _msg.what = FRIEND_ADD_REQUEST_SUCCESS;
                                    _msg.obj = _friend_json;
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
                        Log.d("jlj", "receiver-----------onError" + ex.getMessage());
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


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int _what = msg.what;
            switch (_what) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Log.d("jlj", "FriendService-Handler-errorMsg-----------------------" + errorMsg);

                    break;
                case FRIEND_ADD_REQUEST_SUCCESS:
                    String _friend_json = (String) msg.obj;
                    Log.d("jlj", "FriendService-Handler-_friend_json-----------------------" + _friend_json);
                    actionToNotifyNewFriendActivity(_friend_json);//去通知界面采取相应的动作
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);

        }
    };

    /**
     * 新增好友-去通知界面采取相应的动作
     *
     * @param _friend_json
     */
    private void actionToNotifyNewFriendActivity(String _friend_json) {
        //发送广播：若MainActivity处于显示状态，则通知界面更新UI
        boolean isForeground_MainActivity = CommonUtil.isForeground(FriendService.this, "com.mkch.youshi.MainActivity");

        //发送广播：若UI处于显示状态，则通知界面更新UI；若UI不处于显示状态，弹出通知栏，显示信息条数和最新的信息。
        boolean isForeground = CommonUtil.isForeground(FriendService.this, "com.mkch.youshi.activity.NewFriendActivity");
        if (isForeground || isForeground_MainActivity) {
            Intent _intent = new Intent();
            _intent.setAction("yoshi.action.friendsbroadcast");
            _intent.putExtra("_friend_json", _friend_json);
            sendBroadcast(_intent);
        } else {
            notifyInfo(_friend_json);
        }


    }


    /**
     * 通知有好友请求
     *
     * @param _friend_json
     */
    private void notifyInfo(String _friend_json) {
        Notification.Builder _builder = new Notification.Builder(this);

        //intent
        Intent _intent = new Intent(this, NewFriendActivity.class);
        _intent.putExtra("_friend_json", _friend_json);

        Gson _gson = new Gson();
        Friend _friend = _gson.fromJson(_friend_json, Friend.class);
        //昵称
        String _nickname = _friend.getNickname();
        String _content_text_username = null;
        if (_nickname != null && !_nickname.equals("") && !_nickname.equals("null")) {
            _content_text_username = _nickname;
        } else {
            _content_text_username = _friend.getFriendid();
        }

        //头像
        final Bitmap[] bitmaps = new Bitmap[1];

        String _head_pic_url = _friend.getHead_pic();
        if (_head_pic_url != null && !_head_pic_url.equals("") && !_head_pic_url.equals("null")) {
            //圆形图片
            ImageOptions _image_options = new ImageOptions.Builder()
                    .setCircular(true)
                    .setUseMemCache(false)
                    .build();
            //加载网络头像
            x.image().loadDrawable(_friend.getHead_pic(), _image_options, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
                    Log.d("jlj", "-----------------------load head pic onSuccess");
                    Bitmap _bitmap = ((BitmapDrawable) result).getBitmap();
                    bitmaps[0] = _bitmap;
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            Bitmap _bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.default_headpic))).getBitmap();
            bitmaps[0] = _bitmap;
        }


        //pendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT);


        //判断是否全天免打扰
        if (mUser.getDisturb() == null || !mUser.getDisturb()) {
            //如果全天免打扰关闭或者还没设置，接着判断是否开启夜间免打扰
            if (mUser.getNight() == null || !mUser.getNight()) {
                //如果夜间免打扰关闭或者还没设置,只需要判断提示音、振动是否开启
                if (mUser.getSound() == null || mUser.getSound()) {
                    _builder.setDefaults(Notification.DEFAULT_SOUND);
                }
                if (mUser.getVibrate() == null || mUser.getVibrate()) {
                    _builder.setDefaults(Notification.DEFAULT_VIBRATE);
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
                    if (mUser.getSound() == null || mUser.getSound()) {
                        _builder.setDefaults(Notification.DEFAULT_SOUND);
                    }
                    if (mUser.getVibrate() == null || mUser.getVibrate()) {
                        _builder.setDefaults(Notification.DEFAULT_VIBRATE);
                    }
                }
            }
        }
        //builder设置一些参数
        _builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmaps[0])
                .setContentTitle("好友请求")
                .setContentText(_content_text_username + ",请求添加您为好友")
//                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
//                .setFullScreenIntent(pendingIntent,true);

        mNotification = _builder.build();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        //通知
        mNotification_manager.notify(0, mNotification);
    }

    @Override
    public void entriesAdded(Collection<String> addresses) {
        Log.d("JLJ", "------------------entriesAdded");
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        Log.d("JLJ", "------------------entriesUpdated");
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        Log.d("JLJ", "------------------entriesDeleted");
    }

    @Override
    public void presenceChanged(Presence presence) {
        Log.d("JLJ", "------------------presenceChanged");
    }


    /**
     * 添加单聊监听
     */
    private void addChatListener() {
        //初始化聊天管理器
        chatmanager = ChatManager.getInstanceFor(connection);
        //监听收到消息，更新UI
        mChartManagerLisenter = new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                        String content = message.getBody();
                        Log.d("jlj", "addChatListener-----------------------------------接收消息processMessage content=" + content);
                        if (content != null) {
                            //保存消息至数据库
                            Gson _gson = new Gson();
                            ChatBean _chat_bean = _gson.fromJson(content, ChatBean.class);
                            DbManager dbManager = DBHelper.getDbManager();
                            int chat_id = 0;
                            try {
                                //查找该ChatBean所属的消息盒子
                                String _openfirename = _chat_bean.getUsername();//openfirename
                                Log.d("jlj", "addChatListener-----------------------------------_openfirename is" + _openfirename);
                                //获取该好友的一些信息
                                //查询本登录用户的，已添加的，某个好友
                                Friend _friend = dbManager.selector(Friend.class)
                                        .where("friendid", "=", _openfirename)
                                        .and("status", "=", 1)
                                        .and("userid", "=", mUser.getOpenFireUserName())
                                        .findFirst();
                                if (_friend == null) {
                                    Log.d("jlj", "addChatListener-----------------------------------发送的friend is null");
                                    return;
                                }

                                int _messagebox_id = 0;
                                if (_openfirename != null && !_openfirename.equals("")) {
                                    String _jid = XmppStringUtils.completeJidFrom(_openfirename, connection.getServiceName());
                                    Log.d("jlj", "addChatListener-----------------------------------jid=" + _jid);
                                    //查找消息盒子中：该jid是否存在，若不存在，新建消息盒子并返回消息盒子的ID；若存在，获取该消息盒子的ID
                                    MessageBox messageBox = dbManager.selector(MessageBox.class)
                                            .where("jid", "=", _jid).findFirst();

                                    if (messageBox == null) {
                                        messageBox = new MessageBox(_friend.getHead_pic(), _friend.getNickname(), _chat_bean.getContent(), 1, TimesUtils.getNow(), 1, MessageBox.MB_TYPE_CHAT, _jid);
                                        dbManager.saveBindingId(messageBox);//新增消息盒子
                                        _messagebox_id = messageBox.getId();
                                    } else {
                                        _messagebox_id = messageBox.getId();
                                    }
                                }

                                _chat_bean.setMsgboxid(_messagebox_id);//关联消息盒子

                                dbManager.saveBindingId(_chat_bean);//新增消息

                                chat_id = _chat_bean.getId();
                                Log.d("jlj", "addChatListener------------------------chat_id=" + chat_id);
                                actionToNotifyChatActivity(_chat_bean, chat_id, _friend);


                            } catch (DbException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                });


            }


        };
        chatmanager.addChatListener(mChartManagerLisenter);

    }

    /**
     * 添加离线消息监听
     */
    public void addOffLineMessageListener() {
        OfflineMessageManager offlineMessageManager = new OfflineMessageManager(connection);
        try {
            int messageCount = offlineMessageManager.getMessageCount();
            Log.d("jlj", "addOffLineMessageListener--------------------------messageCount=" + messageCount);
            List<org.jivesoftware.smack.packet.Message> messages = offlineMessageManager.getMessages();
            for (org.jivesoftware.smack.packet.Message message : messages) {
                String body = message.getBody();
                Log.d("jlj", "addOffLineMessageListener--------------------------body=" + body);
            }
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单聊-去通知界面采取相应的动作
     *
     * @param _chat_bean
     * @param chat_id
     * @param _friend
     */
    private void actionToNotifyChatActivity(ChatBean _chat_bean, int chat_id, Friend _friend) {
        //发送广播：若UI处于显示状态，则通知界面更新UI；若UI不处于显示状态，弹出通知栏，显示信息条数和最新的信息。
        boolean isForeground = CommonUtil.isForeground(FriendService.this, "com.mkch.youshi.activity.ChatActivity");
        if (isForeground) {
            Intent intent = new Intent();
            intent.putExtra("chat_id", chat_id);
            intent.setAction("yoshi.action.chatsbroadcast");
            sendBroadcast(intent);
        } else {
            //通知
            notifyInfoFromChat(_chat_bean, _friend);
        }
    }

    /**
     * 通知有消息
     */
    private void notifyInfoFromChat(ChatBean chatBean, Friend friend) {
        Notification.Builder _builder = new Notification.Builder(this);

        //intent
        Intent _intent = new Intent(this, ChatActivity.class);
        _intent.putExtra("_openfirename", friend.getFriendid());

        //头像
        final Bitmap[] bitmaps = new Bitmap[1];

        String _head_pic_url = friend.getHead_pic();
        if (_head_pic_url != null && !_head_pic_url.equals("") && !_head_pic_url.equals("null")) {
            //圆形图片
            ImageOptions _image_options = new ImageOptions.Builder()
                    .setCircular(true)
                    .setUseMemCache(false)
                    .build();
            //加载网络头像
            x.image().loadDrawable(friend.getHead_pic(), _image_options, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
                    Log.d("jlj", "-----------------------load head pic onSuccess");
                    Bitmap _bitmap = ((BitmapDrawable) result).getBitmap();
                    bitmaps[0] = _bitmap;
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        } else {
            Bitmap _bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.default_headpic))).getBitmap();
            bitmaps[0] = _bitmap;
        }


        //pendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //昵称
        String _nickname = friend.getNickname();
        String _content_title = null;
        if (_nickname != null && !_nickname.equals("") && !_nickname.equals("null")) {
            _content_title = _nickname;
        } else {
            _content_title = friend.getFriendid();
        }

        //builder设置一些参数
        _builder.setSmallIcon(R.mipmap.ic_launcher)//app的logo
                .setLargeIcon(bitmaps[0])//用户头像
                .setContentTitle(_content_title)//昵称
                .setContentText(chatBean.getContent())//消息内容
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
//                .setFullScreenIntent(pendingIntent,true);

        mNotification = _builder.build();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        //通知
        mNotification_manager.notify(0, mNotification);
    }


}
