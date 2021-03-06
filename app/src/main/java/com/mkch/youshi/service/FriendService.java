package com.mkch.youshi.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChatActivity;
import com.mkch.youshi.activity.NewFriendActivity;
import com.mkch.youshi.bean.GroupFriend;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.ChatBean;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.Group;
import com.mkch.youshi.model.MessageBox;
import com.mkch.youshi.model.YoupanFile;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.util.XUtil;
import com.mkch.youshi.view.Expression;
import com.mkch.youshi.view.HanziToPinyin;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElem;
import com.tencent.TIMFaceElem;
import com.tencent.TIMFileElem;
import com.tencent.TIMFriendAllowType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupSystemElem;
import com.tencent.TIMGroupSystemElemType;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMLocationElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMProfileSystemElem;
import com.tencent.TIMSNSChangeInfo;
import com.tencent.TIMSNSSystemElem;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMUser;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mkch.youshi.view.RecordButton.generate;
import static com.tencent.TIMGroupSystemElemType.TIM_GROUP_SYSTEM_CREATE_GROUP_TYPE;
import static com.tencent.TIMImageType.Original;
import static com.tencent.TIMImageType.Thumb;

/**
 * Created by ZJ on 2016/10/13.
 */
public class FriendService extends Service implements TIMMessageListener {
    public static final File AUDIO_DIR = new File(Environment.getExternalStorageDirectory(), "audio");
    public static final File PIC_DIR = new File(Environment.getExternalStorageDirectory(), "image");
    public static final File FILE_DIR = new File(Environment.getExternalStorageDirectory(), "");
    private File audioFile, imageFile, file;
    private TIMImage imageOriginal;
    private User mUser;
    private String identify, userSig;
    public static final int ACCOUNT_TYPE = 7882;
    public static final int SDK_APPID = 1400016695;
    //通知
    private NotificationManager mNotification_manager;
    private Notification mNotification;
    private String friendIdentify;
    private ProgressDialog mProgressDialog;
    private GroupFriend mGroupFriend;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TIMManager.getInstance().init(getApplicationContext());
        mUser = CommonUtil.getUserInfo(this);
        TIMManager.getInstance().addMessageListener(this);
        //通知service
        mNotification_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        tlsLogin();
    }

    //登录IM功能
    private void tlsLogin() {
        identify = mUser.getOpenFireUserName();
        userSig = mUser.getUserSig();
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(SDK_APPID));
        user.setIdentifier(identify);
        TIMManager.getInstance().login(SDK_APPID, user, userSig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz----------imsdkLogin", i + "Error:" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("zzz----------imsdkLogin", "login is success");
                //暂设置自己的好友验证方式为需要验证
                TIMFriendshipManager.getInstance().setAllowType(TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.d("zzz--------setAllowType", i + "Error:" + s);
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("zzz--------setAllowType", "setAllowType is success");
                    }
                });
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int _what = msg.what;
            switch (_what) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Log.d("zzzGetFriend-----------", errorMsg);
                    break;
                case CommonConstants.FLAG_GET_ADD_FRIEND_INFORMATION:
                    String _friend_json = (String) msg.obj;
                    actionToNotifyNewFriendActivity(_friend_json);//去通知界面采取相应的动作
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 查询该用户信息并存储于数据库
     *
     * @param identify
     */
    private void saveFriendToDB(final String identify) {
        //根据openfireusername查询该用户的信息，并保存于数据库
        RequestParams requestParams = new RequestParams(CommonConstants.GetInfoByOpenFireName);
        //包装请求参数
        String _req_json = "{\"OpenFireUserName\":\"" + identify + "\"}";
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", mUser.getLoginCode());//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            JSONObject datas = _json_result.getJSONObject("Datas");
                            String _user_json_str = datas.toString();
                            String _friend_json = null;
                            if (_user_json_str != null && !_user_json_str.equals("")) {
                                Gson _gson = new Gson();
                                User _user = _gson.fromJson(_user_json_str, User.class);
                                String _self_user_openfirename = mUser.getOpenFireUserName();//自己的openfirename
                                String _friend_openfirename = _user.getOpenFireUserName();//好友的openfirename
                                //保存用户请求列表
                                DbManager dbManager = DBHelper.getDbManager();
                                //查询好友是否在我的好友列表，若存在，更新该好友信息；若不存在，新增该好友信息；
                                Friend _friend = dbManager.selector(Friend.class)
                                        .where("userid", "=", _self_user_openfirename)
                                        .and("friendid", "=", _friend_openfirename)
                                        .findFirst();
                                if (_friend == null) {
                                    //新增该好友信息到数据库
                                    _friend = new Friend();
                                    _friend.setStatus(2);//待接收
                                    _friend.setShowinnewfriend(1);//接受好友请求
                                    if (_user.getHeadPic() != null && !_user.getHeadPic().equals("") && !_user.getHeadPic().equals("null")) {
                                        _friend.setHead_pic(CommonConstants.NOW_ADDRESS_PRE + _user.getHeadPic());//头像
                                    }
                                    if (_user.getNickName() != null && !_user.getNickName().equals("") && !_user.getNickName().equals("null")) {
                                        _friend.setNickname(_user.getNickName());//昵称
                                        _friend.setPinyin(HanziToPinyin.getPinYin(_user.getNickName()));
                                    } else {
                                        _friend.setPinyin(HanziToPinyin.getPinYin(_friend_openfirename));
                                    }
                                    if (_user.getRealName() != null && !_user.getRealName().equals("") && !_user.getRealName().equals("null")) {
                                        _friend.setRemark(_user.getRealName());//备注
                                    }
                                    _friend.setPhone(_user.getMobileNumber());//手机号码
                                    _friend.setUserid(_self_user_openfirename);//自己的openfire用户名
                                    _friend.setFriendid(_friend_openfirename);//好友的openfire用户名
                                    dbManager.saveBindingId(_friend);
                                } else {
                                    //更新好友信息
                                    _friend.setStatus(2);//待接收
                                    _friend.setShowinnewfriend(1);//接受好友请求-显示状态
                                    if (_user.getHeadPic() != null && !_user.getHeadPic().equals("") && !_user.getHeadPic().equals("null")) {
                                        _friend.setHead_pic(CommonConstants.NOW_ADDRESS_PRE + _user.getHeadPic());//头像
                                    }
                                    if (_user.getNickName() != null && !_user.getNickName().equals("") && !_user.getNickName().equals("null")) {
                                        _friend.setNickname(_user.getNickName());//昵称
                                        _friend.setPinyin(HanziToPinyin.getPinYin(_user.getNickName()));
                                    } else {
                                        _friend.setPinyin(HanziToPinyin.getPinYin(_friend_openfirename));
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
                                return;
                            }
                            Message _msg = new Message();
                            _msg.what = CommonConstants.FLAG_GET_ADD_FRIEND_INFORMATION;
                            _msg.obj = _friend_json;
                            mHandler.sendMessage(_msg);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                Log.d("zzz", "getInfoByOpenFireName-------------1001");
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                Log.d("zzz", "getInfoByOpenFireName-------------1002");
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
                .setContentIntent(pendingIntent);
        mNotification = _builder.build();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        //通知
        mNotification_manager.notify(0, mNotification);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        Log.d("zzz", "-----------------------onNewMessages");
        if (list != null && list.size() > 0) {
            for (TIMMessage timMessage : list) {
                long elementCount = timMessage.getElementCount();
                String sender = timMessage.getSender();
                TIMConversation conversation = timMessage.getConversation();
                TIMConversationType conversationType = conversation.getType();
                String peer = conversation.getPeer();
                //接收到系统消息
                if (conversationType == TIMConversationType.System) {
                    Log.d("zzz", "---System--------elementCount=" + elementCount);
                    if (elementCount > 0) {
                        for (int j = 0; j < elementCount; j++) {
                            TIMElem element = timMessage.getElement(j);
                            if (element instanceof TIMSNSSystemElem) {
                                List<TIMSNSChangeInfo> changeInfoList = ((TIMSNSSystemElem) element).getChangeInfoList();
                                for (TIMSNSChangeInfo timsnsChangeInfo : changeInfoList) {
                                    friendIdentify = timsnsChangeInfo.getIdentifier();
                                    Log.d("zzz", "changeInfoList one is-----" + friendIdentify);
                                    saveFriendToDB(friendIdentify);
                                }
                            }//接受到群系统信息
                            else if (element instanceof TIMGroupSystemElem) {
                                Log.d("zzz", "TIMGroupSystemElemType sender is-----" + sender);
                                TIMGroupSystemElemType msg = ((TIMGroupSystemElem) element).getSubtype();
                                Log.d("zzz", "TIMGroupSystemElemType is-----" + msg);
                                if (msg == TIM_GROUP_SYSTEM_CREATE_GROUP_TYPE) {
                                }
                            }//接受到用户资料变更系统通知
                            else if (element instanceof TIMProfileSystemElem) {
                                String name = ((TIMProfileSystemElem) element).getNickName();
                                Log.d("zzz", "TIMProfileSystemElem content is-----" + name);
                            } else {
                                Log.d("zzz", "-----------------------" + element.getType());
                            }
                        }
                    }
                } else if (conversationType == TIMConversationType.C2C) {
                    Log.d("zzz", "---C2C-----------------elementCount=" + elementCount);
                    if (elementCount > 0) {
                        for (int j = 0; j < elementCount; j++) {
                            TIMElem element = timMessage.getElement(j);
                            if (element instanceof TIMSNSSystemElem) {
                                List<TIMSNSChangeInfo> changeInfoList = ((TIMSNSSystemElem) element).getChangeInfoList();
                                for (TIMSNSChangeInfo timsnsChangeInfo : changeInfoList) {
                                    friendIdentify = timsnsChangeInfo.getIdentifier();
                                    Log.d("zzz", "changeInfoList one is-----" + friendIdentify);
                                    saveFriendToDB(friendIdentify);
                                }
                            }//接受到文本信息
                            else if (element instanceof TIMTextElem) {
                                String msg = ((TIMTextElem) element).getText();
                                Log.d("zzz", "TIMTextElem sender is-----" + sender);
                                Log.d("zzz", "TIMTextElem content is-----" + msg);
                                if (msg.length() > 12) {
                                    if (msg.substring(0, 12).equals("thisSendFile")) {
                                        String youpanFile = msg.substring(12, msg.length());
                                        Gson gson = new Gson();
                                        YoupanFile youpanFile1 = gson.fromJson(youpanFile, YoupanFile.class);
                                        file = new File(FILE_DIR, generate() + TimesUtils.getNow() + youpanFile1.getFile_id() + "");
                                        receiveSendFileMessage(sender, youpanFile1);
                                    } else {
                                        receiveTextMessage(sender, msg);
                                    }
                                } else {
                                    receiveTextMessage(sender, msg);
                                }
                            }//接受到语音信息
                            else if (element instanceof TIMSoundElem) {
                                int duration = (int) ((TIMSoundElem) element).getDuration() / 1000;
                                Log.d("zzz", "TIMSoundElem sender is-----" + sender);
                                Log.d("zzz", "TIMSoundElem duration is-----" + duration);
                                if (!AUDIO_DIR.exists()) {
                                    AUDIO_DIR.mkdir();
                                }
                                audioFile = new File(AUDIO_DIR, generate() + TimesUtils.getNow() + j + ".amr");
                                ((TIMSoundElem) element).getSoundToFile(audioFile.getAbsolutePath(), new TIMCallBack() {
                                    @Override
                                    public void onError(int i, String s) {
                                        Log.d("zzz------getSoundToFile", i + "Error:" + s);
                                    }

                                    @Override
                                    public void onSuccess() {
                                        Log.d("zzz------getSoundToFile", "getSoundToFile is success");
                                    }
                                });
                                receiveSoundMessage(sender, audioFile.getAbsolutePath(), duration);
                            }//接受到图片信息
                            else if (element instanceof TIMImageElem) {
                                Log.d("zzz", "TIMImageElem sender is-----" + sender);
                                //图片元素
                                TIMImageElem e = (TIMImageElem) element;
                                List<TIMImage> listImage = e.getImageList();
                                String uuid = "";
                                for (TIMImage image : listImage) {
                                    Log.d("zzz", "listImage is-----" + image);
                                    Log.d("zzz", "listImage is-----" + image.getType());
                                    uuid = image.getUuid();
                                    if (image.getType() == Thumb) {
                                        Log.d("zzz", "listImage is-----" + "Thumb");
                                        if (!PIC_DIR.exists()) {
                                            PIC_DIR.mkdir();
                                        }
                                        imageFile = new File(PIC_DIR, generate() + TimesUtils.getNow() + uuid + "Thumb" + ".jpg");
                                        image.getImage(imageFile.getAbsolutePath(), new TIMCallBack() {
                                            @Override
                                            public void onError(int i, String s) {
                                                Log.d("zzz-----getImage Thumb", i + "Error:" + s);
                                            }

                                            @Override
                                            public void onSuccess() {
                                                Log.d("zzz-----getImage Thumb", "getImage is success");
                                            }
                                        });
                                    }
                                    if (image.getType() == Original) {
                                        imageOriginal = image;
                                    }
                                }
                                receivePicMessage(sender, imageFile.getAbsolutePath(), imageOriginal, uuid);
                            }//接受到文件信息
                            else if (element instanceof TIMFileElem) {
                                Log.d("zzz", "TIMFileElem sender is-----" + sender);
                                if (!FILE_DIR.exists()) {
                                    FILE_DIR.mkdir();
                                }
                                String uuid = ((TIMFileElem) element).getUuid();
                                file = new File(FILE_DIR, generate() + TimesUtils.getNow() + uuid + "");
                                receiveFileMessage(sender, (TIMFileElem) element);
                            }//接受到表情信息
                            else if (element instanceof TIMFaceElem) {
                                Log.d("zzz", "TIMFaceElem sender is-----" + sender);
                                int position = ((TIMFaceElem) element).getIndex();
                                receiveFaceMessage(sender, position);
                            }//接受到位置信息
                            else if (element instanceof TIMLocationElem) {
                                String msg = ((TIMLocationElem) element).getDesc();
                                Log.d("zzz", "TIMLocationElem sender is-----" + sender);
                                Log.d("zzz", "TIMLocationElem content is-----" + msg);
                                receiveTextMessage(sender, msg);
                            }
                        }
                    }
                } else if (conversationType == TIMConversationType.Group) {
                    Log.d("zzz", "---Group---------------elementCount=" + elementCount);
                    //获取该群成员资料
                    List<String> identifiers = new ArrayList<>();
                    identifiers.add(sender);
//                    getGroupMember(peer, identifiers, elementCount, timMessage);
                }
            }
        }
        return false;
    }

    //获取该群聊成员名片
    private void getGroupMember(final String groupId, final List<String> identifiers, final long elementCount, final TIMMessage timMessage) {
        TIMGroupManager.getInstance().getGroupMembersInfo(groupId, identifiers, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-getGroupMembersInfo", i + "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                Log.d("zzz-getGroupMembersInfo", "getGroupMembersInfo is success");
                for (TIMGroupMemberInfo info : timGroupMemberInfos) {
                    String memberIdentifier = info.getUser();
                    String memberCard = info.getNameCard();
                    mGroupFriend = new GroupFriend();
                    mGroupFriend.setMemberIdentifier(memberIdentifier);
                    mGroupFriend.setMemberCard(memberCard);
                    getGroupMemberHead(identifiers, groupId, memberIdentifier, elementCount, timMessage);
                }
            }
        });
    }

    //获取群成员头像和昵称后保存在本地数据库
    private void getGroupMemberHead(final List<String> users, final String peer, final String sender, final long elementCount, final TIMMessage timMessage) {
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz-----getUsersProfile", i + "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                Log.d("zzz-----getUsersProfile", "getUsersProfile is success");
                for (TIMUserProfile timUserProfile : timUserProfiles) {
                    String memberHead = timUserProfile.getFaceUrl();
                    String memberName = timUserProfile.getNickName();
                    mGroupFriend.setMemberHead(memberHead);
                    //如果群名片为空，则设置该群成员名片为其昵称
                    if (mGroupFriend.getMemberCard() == null || mGroupFriend.getMemberCard().equals("")) {
                        mGroupFriend.setMemberCard(memberName);
                        mGroupFriend.setMemberName(memberName);
                    } else {
                        mGroupFriend.setMemberName(memberName);
                    }
                    //查询本地是否有该群成员信息，若有则刷新，若无则获取后保存消息至数据库
                    int member_id = 0;
                    try {
                        DbManager dbManager = DBHelper.getDbManager();
                        GroupFriend _groupFriend = dbManager.selector(GroupFriend.class).where("group_id", "=", peer).and("member_identifier", "=", sender).findFirst();
                        if (_groupFriend != null) {
                            //有就更改他的字段
                            _groupFriend.setGroupID(peer);
                            _groupFriend.setMemberIdentifier(sender);
                            _groupFriend.setMemberName(memberName);
                            _groupFriend.setMemberCard(mGroupFriend.getMemberCard());
                            _groupFriend.setMemberHead(memberHead);
                            dbManager.saveOrUpdate(_groupFriend);
                            member_id = _groupFriend.getId();
                        } else {
                            //没有就插入
                            dbManager.saveBindingId(mGroupFriend);
                            member_id = mGroupFriend.getId();
                        }
                        receiveGroupMessage(peer, sender, elementCount, timMessage, member_id);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //根据群聊消息类别来进行处理
    private void receiveGroupMessage(final String peer, final String sender, final long elementCount, final TIMMessage timMessage, int member_id) {
        if (elementCount > 0) {
            for (int j = 0; j < elementCount; j++) {
                TIMElem element = timMessage.getElement(j);
                //接受到信息
                if (element instanceof TIMTextElem) {
                    String msg = ((TIMTextElem) element).getText();
                    Log.d("zzz", "Group TIMTextElem group is-----" + peer);
                    Log.d("zzz", "Group TIMTextElem sender is-----" + sender);
                    Log.d("zzz", "Group TIMTextElem content is-----" + msg);
                    receiveGroupTextMessage(peer, sender, msg, member_id);
                }//接受到语音信息
                else if (element instanceof TIMSoundElem) {
                    int duration = (int) ((TIMSoundElem) element).getDuration() / 1000;
                    Log.d("zzz", "TIMSoundElem sender is-----" + sender);
                    Log.d("zzz", "TIMSoundElem duration is-----" + duration);
                    if (!AUDIO_DIR.exists()) {
                        AUDIO_DIR.mkdir();
                    }
                    audioFile = new File(AUDIO_DIR, generate() + TimesUtils.getNow() + j + ".amr");
                    ((TIMSoundElem) element).getSoundToFile(audioFile.getAbsolutePath(), new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Log.d("zzz------getSoundToFile", i + "Error:" + s);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d("zzz------getSoundToFile", "getSoundToFile is success");
                        }
                    });
                    receiveSoundMessage(sender, audioFile.getAbsolutePath(), duration);
                }//接受到图片信息
                else if (element instanceof TIMImageElem) {
                    Log.d("zzz", "TIMImageElem sender is-----" + sender);
                    //图片元素
                    TIMImageElem e = (TIMImageElem) element;
                    List<TIMImage> listImage = e.getImageList();
                    String uuid = "";
                    for (TIMImage image : listImage) {
                        Log.d("zzz", "listImage is-----" + image);
                        Log.d("zzz", "listImage is-----" + image.getType());
                        uuid = image.getUuid();
                        if (image.getType() == Thumb) {
                            Log.d("zzz", "listImage is-----" + "Thumb");
                            if (!PIC_DIR.exists()) {
                                PIC_DIR.mkdir();
                            }
                            imageFile = new File(PIC_DIR, generate() + TimesUtils.getNow() + uuid + "Thumb" + ".jpg");
                            image.getImage(imageFile.getAbsolutePath(), new TIMCallBack() {
                                @Override
                                public void onError(int i, String s) {
                                    Log.d("zzz-----getImage Thumb", i + "Error:" + s);
                                }

                                @Override
                                public void onSuccess() {
                                    Log.d("zzz-----getImage Thumb", "getImage is success");
                                }
                            });
                        }
                        if (image.getType() == Original) {
                            imageOriginal = image;
                        }
                    }
                    receivePicMessage(sender, imageFile.getAbsolutePath(), imageOriginal, uuid);
                }//接受到文件信息
                else if (element instanceof TIMFileElem) {
                    Log.d("zzz", "TIMFileElem sender is-----" + sender);
                    TIMFileElem e = (TIMFileElem) element;
                    String uuid = e.getUuid();
                    if (!FILE_DIR.exists()) {
                        FILE_DIR.mkdir();
                    }
                    file = new File(FILE_DIR, generate() + TimesUtils.getNow() + uuid + "");
                    receiveFileMessage(sender, (TIMFileElem) element);
                }//接受到表情信息
                else if (element instanceof TIMFaceElem) {
                    Log.d("zzz", "TIMFaceElem sender is-----" + sender);
                    int position = ((TIMFaceElem) element).getIndex();
                    receiveFaceMessage(sender, position);
                }//接受到位置信息
                else if (element instanceof TIMLocationElem) {
                    String msg = ((TIMLocationElem) element).getDesc();
                    Log.d("zzz", "TIMLocationElem sender is-----" + sender);
                    Log.d("zzz", "TIMLocationElem content is-----" + msg);
                    receiveTextMessage(sender, msg);
                }
            }
        }
    }

    //显示获取的文本聊天信息
    private void receiveTextMessage(final String sender, final String msg) {
        if (msg != null) {
            //保存消息至数据库
            ChatBean _chat_bean = new ChatBean(sender, msg, ChatBean.MESSAGE_TYPE_IN, TimesUtils.getNow());
            saveChatBean(sender, _chat_bean);
        }
    }

    //显示获取的群聊文本聊天信息
    private void receiveGroupTextMessage(final String peer, final String sender, final String msg, int member_id) {
        if (msg != null) {
            //保存消息至数据库
            ChatBean _chat_bean = new ChatBean(peer, sender, msg, ChatBean.MESSAGE_TYPE_IN, TimesUtils.getNow());
            saveGroupChatBean(peer, sender, _chat_bean, member_id);
        }
    }

    //显示获取的语音聊天信息
    private void receiveSoundMessage(final String sender, final String path, final int duration) {
        if (path != null) {
            //保存消息至数据库
            ChatBean _chat_bean = new ChatBean(sender, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_IN, duration, path, "[语音]");
            saveChatBean(sender, _chat_bean);
        }
    }

    //显示获取的图片聊天信息
    private void receivePicMessage(final String sender, final String path, final TIMImage image, final String uuid) {
        if (path != null) {
            //保存消息至数据库
            ChatBean _chat_bean = new ChatBean(sender, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_IN, path, "[图片]");
            //下载原图
            downloadOriginal(_chat_bean, image, uuid);
            saveChatBean(sender, _chat_bean);
        }
    }

    //下载图片信息原图
    private void downloadOriginal(final ChatBean chatbean, final TIMImage image, final String uuid) {
        imageFile = new File(PIC_DIR, generate() + TimesUtils.getNow() + "Original" + uuid + ".jpg");
        image.getImage(imageFile.getAbsolutePath(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz---getImage Original", i + "Error:" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("zzz---getImage Original", "getImage is success");
                try {
                    chatbean.setFileOriginal(imageFile.getAbsolutePath());
                    DbManager dbManager = DBHelper.getDbManager();
                    dbManager.saveOrUpdate(chatbean);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //显示获取的分享文件信息
    private void receiveSendFileMessage(final String sender, final YoupanFile youpanFile) {
        if (youpanFile != null) {
            String fileName = youpanFile.getName();
            //保存消息至数据库
            ChatBean _chat_bean = new ChatBean(sender, TimesUtils.getNow(), fileName, "[文件]", ChatBean.MESSAGE_TYPE_IN);
            //下载文件信息
            downloadSendFile(_chat_bean, youpanFile);
            saveChatBean(sender, _chat_bean);
        }
    }

    //下载分享文件信息
    private void downloadSendFile(final ChatBean chatbean, final YoupanFile youpanFile) {
        if (file != null) {
            buildAlertDialog_progress();
            XUtil.downLoadFile(youpanFile, CommonConstants.YOU_PAN_PIC_PATH +
                    youpanFile.getName(), mProgressDialog, false);
            try {
                chatbean.setFilePath(file.getAbsolutePath());
                DbManager dbManager = DBHelper.getDbManager();
                dbManager.saveOrUpdate(chatbean);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进度对话框
     */
    private void buildAlertDialog_progress() {
        mProgressDialog = new ProgressDialog(getApplicationContext());
        mProgressDialog.setMessage("正在下载...........");
        /**进度条样式 */
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "后台下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressDialog.dismiss();
            }
        });
        /**模糊效果 */
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    //显示获取的文件聊天信息
    private void receiveFileMessage(final String sender, final TIMFileElem elemnet) {
        if (elemnet != null) {
            String fileName = elemnet.getFileName();
            //保存消息至数据库
            ChatBean _chat_bean = new ChatBean(sender, TimesUtils.getNow(), fileName, "[文件]", ChatBean.MESSAGE_TYPE_IN);
            //下载文件信息
            downloadFile(_chat_bean, elemnet);
            saveChatBean(sender, _chat_bean);
        }
    }

    //下载文件信息
    private void downloadFile(final ChatBean chatbean, final TIMFileElem element) {
        if (file != null) {
            element.getToFile(file.getAbsolutePath(), new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    Log.d("zzz--------getToFile", i + "Error:" + s);
                }

                @Override
                public void onSuccess() {
                    Log.d("zzz--------getToFile", "getToFile is success");
                    try {
                        chatbean.setFilePath(file.getAbsolutePath());
                        DbManager dbManager = DBHelper.getDbManager();
                        dbManager.saveOrUpdate(chatbean);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //显示获取的表情聊天信息
    private void receiveFaceMessage(final String sender, final int position) {
        if (sender != null) {
            //保存消息至数据库
            ChatBean _chat_bean = new ChatBean(sender, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_IN, position, Expression.expressionMessages[position]);
            saveChatBean(sender, _chat_bean);
        }
    }

    //保存消息至数据库
    private void saveChatBean(final String sender, final ChatBean _chat_bean) {
        DbManager dbManager = DBHelper.getDbManager();
        int chat_id = 0;
        try {
            //获取该好友的一些信息
            //查询本登录用户的，已添加的，某个好友
            Friend _friend = dbManager.selector(Friend.class)
                    .where("friendid", "=", sender)
                    .and("status", "=", 1)
                    .and("userid", "=", mUser.getOpenFireUserName())
                    .findFirst();
            int _messagebox_id = 0;
            if (sender != null && !sender.equals("")) {
                String friendId = sender + "@" + "TIMConversationType.C2C";
                String selfId = mUser.getOpenFireUserName();
                //查找消息盒子中：该friendId和selfId是否存在，若不存在，新建消息盒子并返回消息盒子的ID；若存在，获取该消息盒子的ID
                MessageBox messageBox = dbManager.selector(MessageBox.class).where("friend_id", "=", friendId)
                        .and("self_id", "=", selfId).findFirst();
                if (messageBox == null) {
                    String title = null;
                    if (_friend.getNickname() == null || _friend.getNickname().equals("")) {
                        title = _friend.getPhone();
                    } else {
                        title = _friend.getNickname();
                    }
                    messageBox = new MessageBox(_friend.getHead_pic(), title, _chat_bean.getContent(), 1, TimesUtils.getNow(), 1, MessageBox.MB_TYPE_CHAT, friendId, selfId);
                    dbManager.saveBindingId(messageBox);//新增消息盒子
                    _messagebox_id = messageBox.getId();
                } else {
                    _messagebox_id = messageBox.getId();
                }
            }
            _chat_bean.setMsgboxid(_messagebox_id);//关联消息盒子
            dbManager.saveBindingId(_chat_bean);//新增消息
            chat_id = _chat_bean.getId();
            actionToNotifyChatActivity(_chat_bean, chat_id, _friend);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //保存群聊消息至数据库
    private void saveGroupChatBean(final String peer, final String sender, final ChatBean _chat_bean, int member_id) {
        DbManager dbManager = DBHelper.getDbManager();
        int chat_id = 0;
        try {
            //获取该群组和该成员的一些信息
            Group _group = dbManager.selector(Group.class).where("group_id", "=", peer).and("user_id", "=", mUser.getOpenFireUserName()).findFirst();
            int _messagebox_id = 0;
            if (peer != null && !peer.equals("")) {
                String friendId = peer + "@" + "TIMConversationType.Group";
                String selfId = mUser.getOpenFireUserName();
                //查找消息盒子中：该friendId和selfId是否存在，若不存在，新建消息盒子并返回消息盒子的ID；若存在，获取该消息盒子的ID
                MessageBox messageBox = dbManager.selector(MessageBox.class).where("friend_id", "=", friendId)
                        .and("self_id", "=", selfId).findFirst();
                if (messageBox == null) {
                    String title = null;
                    if (_group.getGroupName() != null && !_group.getGroupName().equals("")) {
                        title = _group.getGroupName();
                    }
                    messageBox = new MessageBox(_group.getGroupHead(), title, _chat_bean.getContent(), 1, TimesUtils.getNow(), 1, MessageBox.MB_TYPE_MUL_CHAT, friendId, selfId);
                    dbManager.saveBindingId(messageBox);//新增消息盒子
                    _messagebox_id = messageBox.getId();
                } else {
                    _messagebox_id = messageBox.getId();
                }
            }
            _chat_bean.setMsgboxid(_messagebox_id);//关联消息盒子
            dbManager.saveBindingId(_chat_bean);//新增消息
            chat_id = _chat_bean.getId();
            actionToNotifyGroupChatActivity(_chat_bean, chat_id, _group, sender, member_id);
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
     * 群聊-去通知界面采取相应的动作
     *
     * @param _chat_bean
     * @param chat_id
     * @param sender
     */
    private void actionToNotifyGroupChatActivity(ChatBean _chat_bean, int chat_id, Group group, String sender, int member_id) {
        //发送广播：若UI处于显示状态，则通知界面更新UI；若UI不处于显示状态，弹出通知栏，显示信息条数和最新的信息。
        boolean isForeground = CommonUtil.isForeground(FriendService.this, "com.mkch.youshi.activity.ChatActivity");
        if (isForeground) {
            Intent intent = new Intent();
            intent.putExtra("chat_id", chat_id);
            intent.putExtra("member_id", member_id);
            intent.setAction("yoshi.action.chatsbroadcast");
            sendBroadcast(intent);
        } else {
            //通知
            notifyInfoFromGroupChat(_chat_bean, group, sender);
        }
    }

    /**
     * 单聊通知有消息
     */
    private void notifyInfoFromChat(ChatBean chatBean, Friend friend) {
        Notification.Builder _builder = new Notification.Builder(this);
        //intent
        Intent _intent = new Intent(this, ChatActivity.class);
        _intent.putExtra("chatType", "C2C");
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

    /**
     * 群聊通知有消息
     */
    private void notifyInfoFromGroupChat(ChatBean chatBean, Group group, String sender) {
        Notification.Builder _builder = new Notification.Builder(this);
        //intent
        Intent _intent = new Intent(this, ChatActivity.class);
        _intent.putExtra("chatType", "Group");
        _intent.putExtra("groupID", group.getGroupID());
        _intent.putExtra("_openfirename", sender);
        //头像
        final Bitmap[] bitmaps = new Bitmap[1];
        String _head_pic_url = group.getGroupHead();
        if (_head_pic_url != null && !_head_pic_url.equals("") && !_head_pic_url.equals("null")) {
            //圆形图片
            ImageOptions _image_options = new ImageOptions.Builder()
                    .setCircular(true)
                    .setUseMemCache(false)
                    .build();
            //加载网络头像
            x.image().loadDrawable(group.getGroupHead(), _image_options, new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
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
            Bitmap _bitmap = ((BitmapDrawable) (getResources().getDrawable(R.drawable.groupchat))).getBitmap();
            bitmaps[0] = _bitmap;
        }
        //pendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //昵称
        String _nickname = group.getGroupName();
        String _content_title = null;
        if (_nickname != null && !_nickname.equals("") && !_nickname.equals("null")) {
            _content_title = _nickname;
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
