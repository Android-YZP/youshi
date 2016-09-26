package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.UnLoginedUser;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CheckUtil;
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

public class UserRegPhoneActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private Button mBtnCommitPhone;
    //手机号
    private EditText mEtPhone;
    //验证码
    private LinearLayout mLayoutCode;
    private EditText mEtCode;
    private ImageView mIvCode;
    //是否选中checkbox
    private CheckBox mCbIsRead;
    private TextView mTvIsRead;
    private TextView mTvProtocal;
    private String mPhone, mCode;
    private String mIsEdit = "false";
    private boolean ischecked;
    private UnLoginedUser mUnLoginedUser;
    private String tokenID;
    private String mPicUrl;//图片验证码地址
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg_phone);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtPhone = (EditText) findViewById(R.id.et_user_reg_phone);
        mLayoutCode = (LinearLayout) findViewById(R.id.layout_user_reg_phone_code);
        mEtCode = (EditText) findViewById(R.id.et_user_reg_phone_code);
        mIvCode = (ImageView) findViewById(R.id.iv_user_reg_phone_code);
        mBtnCommitPhone = (Button) findViewById(R.id.btn_user_reg_getcode_phone_commit);
        mCbIsRead = (CheckBox) findViewById(R.id.cb_user_reg_phone_ischecked);
        mTvIsRead = (TextView) findViewById(R.id.tv_user_reg_phone_isread);
        mTvProtocal = (TextView) findViewById(R.id.tv_user_reg_phone_read_protocal);
    }

    private void initData() {
        mTvTitle.setText("注册");
        mLayoutCode.setVisibility(View.GONE);
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            mIsEdit = _bundle.getString("_isEdit");
            mPhone = _bundle.getString("_phone");
            if (mIsEdit.equals("true")) {
                mEtPhone.setText(mPhone);
                mEtPhone.setTextColor(getResources().getColor(R.color.user_login_reg_or_forget_color));
                mEtPhone.setEnabled(false);
            }
        }
        //1-检查tokenid是否为空，若为空，获取并覆盖本地SharedPreference数据；若不为空，获取直接使用
        //2-检查是否需要显示短信图片验证码，若需要，控件显示，并加载图片；
        mUnLoginedUser = CommonUtil.getUnLoginedUser(this);
        if (mUnLoginedUser != null && mUnLoginedUser.getTokenID() != null && !mUnLoginedUser.getTokenID().equals("") && !mUnLoginedUser.getTokenID().equals("null")) {
            tokenID = mUnLoginedUser.getTokenID();
            isShowPicCodeFromNet();
        } else {
            try {
                coverTokenID();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRegPhoneActivity.this.finish();
            }
        });
        mBtnCommitPhone.setOnClickListener(new UserRegPhoneOnClickListener());
        mTvIsRead.setOnClickListener(new UserRegPhoneOnClickListener());
        mIvCode.setOnClickListener(new UserRegPhoneOnClickListener());
//		mTvProtocal.setOnClickListener(new UserRegPhoneOnClickListener());
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(UserRegPhoneActivity activity) {
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
                    ((UserRegPhoneActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((UserRegPhoneActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_GET_USER_SEND_VERIFICATION_CODE:
                    //验证码发送太频繁
                    String errorMsg2 = ("验证码发送太频繁");
                    ((UserRegPhoneActivity) mActivity.get()).showTip(errorMsg2);
                    break;
                case CommonConstants.FLAG_REG_CODE_SHOW:
                    //图片验证码出现
                    ((UserRegPhoneActivity) mActivity.get()).showImgVerfy2();
                    break;
                case CommonConstants.FLAG_REG_PHONE_TOKENID_NO_EXIST:
                    //TokenID不存在
                    String errorMsg3 = ("TokenID不存在");
                    ((UserRegPhoneActivity) mActivity.get()).showTip(errorMsg3);
                    break;
                case CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_ERROR:
                    //图片验证码错误
                    ((UserRegPhoneActivity) mActivity.get()).imgVerifyError();
                    break;
                case CommonConstants.FLAG_COVER_TOKEN_ID_SUCCESS:
                    //覆盖TokenID成功
                    ((UserRegPhoneActivity) mActivity.get()).isShowPicCodeFromNet();
                    break;
                case CommonConstants.FLAG_GET_USER_JOIN_IMG_VERIFY_SHOW:
                    //需要图片验证码
                    ((UserRegPhoneActivity) mActivity.get()).showImgVerfy();
                    break;
                case CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_CHAGE:
                    //手动切换图片验证码
                    ((UserRegPhoneActivity) mActivity.get()).changeImgVerfy();
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

    /**
     * 需要图片验证码
     */
    private void showImgVerfy() {
        mLayoutCode.setVisibility(View.VISIBLE);
        x.image().bind(mIvCode, mPicUrl);
    }

    /**
     * 出现图片验证码
     */
    private void showImgVerfy2() {
        this.showTip("请输入图片验证码");
        mLayoutCode.setVisibility(View.VISIBLE);
        x.image().bind(mIvCode, mPicUrl);
    }

    /**
     * 图片验证码错误-提示并重刷图片验证码
     */
    private void imgVerifyError() {
        this.showTip("图片验证码错误");
        //把ImageView重新刷新图片验证码
        changeImgVerfy();//更新图片验证码
    }

    /**
     * 手动切换图片验证码
     */
    private void changeImgVerfy() {
        x.image().bind(mIvCode, mPicUrl);
    }

    /**
     * 覆盖tokenID
     *
     * @throws Exception
     */
    protected void coverTokenID() throws Exception {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.GetTokenID);
        requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            //覆盖tokenID
                            JSONObject datas = _json_result.getJSONObject("Datas");
                            tokenID = datas.getString("TokenID");
                            mUnLoginedUser.setTokenID(tokenID);
                            CommonUtil.saveUnLoginedUser(mUnLoginedUser, UserRegPhoneActivity.this);
                            //根据TokenID判断是否需要短信图片验证码
                            handler.sendEmptyMessage(CommonConstants.FLAG_COVER_TOKEN_ID_SUCCESS);
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
     * 查看是否需要显示图片验证码
     */
    private void isShowPicCodeFromNet() {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.CheckMessagePickCodeExist);
        //包装请求参数
        String _req_json = "{\"sTokenID\":\"" + tokenID + "\"}";
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            //如果需要短信图片验证码，输入框显示并加载验证码图片
                            JSONObject datas = _json_result.getJSONObject("Datas");
                            mPicUrl = CommonConstants.NOW_ADDRESS_PRE + datas.getString("PicCodePath");
                            handler.sendEmptyMessage(CommonConstants.FLAG_GET_USER_JOIN_IMG_VERIFY_SHOW);
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

    private class UserRegPhoneOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_user_reg_phone_code:
                    changePicCodeFromNet(tokenID);
                    break;
                case R.id.btn_user_reg_getcode_phone_commit:
                    UserCommitPhoneNumer();
                    break;
                case R.id.tv_user_reg_phone_isread:
                    //是否已读
                    if (mCbIsRead.isChecked()) {
                        mCbIsRead.setChecked(false);
                    } else {
                        mCbIsRead.setChecked(true);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 用户提交手机号码
     */
    public void UserCommitPhoneNumer() {
        mPhone = mEtPhone.getText().toString();
        mCode = mEtCode.getText().toString();
        ischecked = mCbIsRead.isChecked();
        if (!ischecked) {
            Toast.makeText(UserRegPhoneActivity.this, "您未同意注册协议", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPhone != null && !mPhone.equals("")) {
            if (!CheckUtil.checkMobile(mPhone)) {
                Toast.makeText(UserRegPhoneActivity.this, "手机号格式输入有误", Toast.LENGTH_LONG).show();
                return;
            }
            //弹出加载进度条
            mProgressDialog = ProgressDialog.show(UserRegPhoneActivity.this, "请稍等", "正在玩命获取中...", true, true);
            //开启副线程-检查手机号码是否存在
            checkPhoneFromNet(mPhone);
            //若检查不通过、提示报错信息；检查通过，跳转到下一个界面
            //进度条消失
        } else {
            Toast.makeText(UserRegPhoneActivity.this, "您未填写手机号", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开启副线程-检查手机号码是否存在
     *
     * @param phone
     */
    private void checkPhoneFromNet(final String phone) {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.MobileNumberIsExist);
        //包装请求参数
        String _req_json = "{\"sMobileNumber\":\"" + phone + "\"}";
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }
                            Toast.makeText(UserRegPhoneActivity.this, "手机号已注册", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            sendVerificationCodeFromNet(mPhone, tokenID, mCode);
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
     * 发送短信验证码
     *
     * @param sPhone,mTokenID,mPicCode
     */
    private void sendVerificationCodeFromNet(final String sPhone, String mTokenID, String mPicCode) {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.SetVerificationCode);
        //包装请求参数
        String _req_json = "{\"sMobileNumber\":\"" + sPhone + "\",\"sTokenID\":\"" + mTokenID + "\",\"sPicCode\":\"" + mPicCode + "\"}";
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            if (mProgressDialog != null) {
                                mProgressDialog.dismiss();
                            }
                            Intent _intent = new Intent(UserRegPhoneActivity.this, UserRegCodeActivity.class);
                            _intent.putExtra("_phone", mPhone);
                            _intent.putExtra("_tokenID", tokenID);
                            startActivity(_intent);
                            UserRegPhoneActivity.this.finish();
                        } else {
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1004")) {
                                JSONObject datas = _json_result.getJSONObject("Datas");
                                mPicUrl = CommonConstants.NOW_ADDRESS_PRE + datas.getString("PicCodePath");
                                handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_ERROR);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1005")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_GET_USER_SEND_VERIFICATION_CODE);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1006")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_REG_CODE_SHOW);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1008")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_REG_PHONE_TOKENID_NO_EXIST);
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
     * 切换登录图片验证码
     */
    private void changePicCodeFromNet(final String id) {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ReloadMessagePicCode);
        //包装请求参数
        String _req_json = "{\"sTokenID\":\"" + id + "\"}";
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            JSONObject datas = _json_result.getJSONObject("Datas");
                            mPicUrl = CommonConstants.NOW_ADDRESS_PRE + datas.getString("PicCodePath");
                            handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_CHAGE);
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
