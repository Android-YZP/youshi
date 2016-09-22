package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CommonUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class PrivacyActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private CheckBox mCBLook, mCBValidate, mCBRecommend;
    private boolean isCheck1 = false;
    private boolean isCheck2 = false;
    private User mUser;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mCBLook = (CheckBox) findViewById(R.id.cb_privacy_look);
        mCBValidate = (CheckBox) findViewById(R.id.cb_privacy_validate);
        mCBRecommend = (CheckBox) findViewById(R.id.cb_privacy_recommend);
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(this);
        mTvTitle.setText("隐私");
        //判断是否允许好友查看日程表盘
        if (mUser.getViewMySchedule() == null || mUser.getViewMySchedule().equals("")) {
            mCBLook.setChecked(true);
        } else if (mUser.getViewMySchedule()) {
            mCBLook.setChecked(true);
        } else {
            mCBLook.setChecked(false);
        }
        //判断加用户好友时是否需要身份验证
        if (mUser.getAddmeVerify() == null || mUser.getAddmeVerify().equals("")) {
            mCBValidate.setChecked(true);
        } else if (mUser.getAddmeVerify()) {
            mCBValidate.setChecked(true);
        } else {
            mCBValidate.setChecked(false);
        }
        //判断是否向用户推荐通讯录好友
        if (mUser.getRecommend() == null || mUser.getRecommend().equals("")) {
            mCBRecommend.setChecked(true);
        } else if (mUser.getRecommend()) {
            mCBRecommend.setChecked(true);
        } else {
            mCBRecommend.setChecked(false);
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrivacyActivity.this.finish();
            }
        });
        mCBLook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ChangeViewMyScheduleFromNet(isChecked);
            }
        });
        mCBValidate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ChangeAddmeVerifyFromNet(isChecked);
            }
        });
        mCBRecommend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mUser.setRecommend(isChecked);
                CommonUtil.saveUserInfo(mUser, PrivacyActivity.this);
                Toast.makeText(PrivacyActivity.this, "已修改是否向用户推荐通讯录好友", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(PrivacyActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    ((PrivacyActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_CHANGE_VIEWMYSCHEDULE_SUCCESS:
                    //修改是否允许好友查看日程表盘成功
                    ((PrivacyActivity) mActivity.get()).updateUserInfo1();
                    break;
                case CommonConstants.FLAG_CHANGE_ADDMEVERIFY_SUCCESS:
                    //修改加用户好友时是否需要身份验证成功
                    ((PrivacyActivity) mActivity.get()).updateUserInfo2();
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((PrivacyActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR3:
                    //请求失败
                    String errorMsg3 = ("请求失败");
                    ((PrivacyActivity) mActivity.get()).showTip(errorMsg3);
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void updateUserInfo1() {
        mUser.setViewMySchedule(isCheck1);
        CommonUtil.saveUserInfo(mUser, PrivacyActivity.this);
        Toast.makeText(this, "已修改是否允许好友查看日程表盘", Toast.LENGTH_LONG).show();
    }

    public void updateUserInfo2() {
        mUser.setAddmeVerify(isCheck2);
        CommonUtil.saveUserInfo(mUser, PrivacyActivity.this);
        Toast.makeText(this, "已修改加用户好友时是否需要身份验证", Toast.LENGTH_LONG).show();
    }

    /**
     * 修改是否允许好友查看日程表盘开关
     */
    private void ChangeViewMyScheduleFromNet(final boolean result) {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(PrivacyActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ChangeViewMySchedule);
        //包装请求参数
        String code = CommonUtil.getUserInfo(PrivacyActivity.this).getLoginCode();
        isCheck1 = result;
        String _req_json = "{\"result\":\"" + result + "\"}";
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_VIEWMYSCHEDULE_SUCCESS);
                        } else {
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR3);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, handler);
                } else if (ex instanceof ConnectTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, handler);
                } else if (ex instanceof SocketTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, handler);
                } else {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, handler);
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
     * 修改加用户好友时是否需要身份验证开关
     */
    private void ChangeAddmeVerifyFromNet(final boolean result) {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(PrivacyActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ChangeAddmeVerify);
        //包装请求参数
        String code = CommonUtil.getUserInfo(PrivacyActivity.this).getLoginCode();
        isCheck2 = result;
        String _req_json = "{\"result\":\"" + result + "\"}";
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        Log.d("jlj", "---------------------result = " + result);
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ADDMEVERIFY_SUCCESS);
                        } else {
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR3);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, handler);
                } else if (ex instanceof ConnectTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, handler);
                } else if (ex instanceof SocketTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, handler);
                } else {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, handler);
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
}
