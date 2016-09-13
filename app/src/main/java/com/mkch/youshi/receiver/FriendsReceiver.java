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
    public static final int RECEIVE_REQUEST_ADD_FRIEND = 11;

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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取该jid的用户名
                    String _openfire_username = XmppStringUtils.parseLocalpart(_request_jid);
                    Log.d("jlj","FriendsReceiver---------------------onReceive-_openfire_username="+_openfire_username);
                    //根据openfireusername查询该用户的信息，并保存于数据库
                    RequestParams requestParams = new RequestParams(CommonConstants.GetInfoByOpenFireName);
                    _openfire_username = "165094350";
                    //包装请求参数
                    String _req_json = "{\"OpenFireUserName\":\"" + _openfire_username + "\"}";
                    Log.d("jlj","FriendsReceiver------------------onReceive-req_json="+_req_json+","+CommonUtil.getUserInfo(context).getLoginCode());
                    requestParams.addBodyParameter("", _req_json);//用户名
                    requestParams.addHeader("sVerifyCode", CommonUtil.getUserInfo(context).getLoginCode());//头信息
                    x.http().post(requestParams, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d("jlj","FriendsReceiver---------------------onReceive-onSuccess:"+result);

                            Log.d("jlj","---------------------result = "+result);
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
                                            dbManager.save(_friend);
                                        }
                                        //获取成功
                                        Message _msg = new Message();
                                        _msg.what = RECEIVE_REQUEST_ADD_FRIEND;
                                        _msg.obj = _request_jid;
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
    }
}
