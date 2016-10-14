package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
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

public class UserForgotCodeActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvPhoneInfo;
    private EditText mEtSmsCode;
    private EditText mEtPassword;
    private EditText mEtPassAgain;
    private Button mBtnCommit;
    private TextView mTvGetSmsAgain;
    private String mPhone;
    private String mSmsCode;
    private String mNewPassword;
    private String mAgainPassword;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot_code);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mTvPhoneInfo = (TextView) findViewById(R.id.tv_user_forgot_code_intro);
        mEtSmsCode = (EditText) findViewById(R.id.et_user_forgot_code_code);
        mEtPassword = (EditText) findViewById(R.id.et_user_forgot_code_new_password);
        mEtPassAgain = (EditText) findViewById(R.id.et_user_forgot_code_again_password);
        mBtnCommit = (Button) findViewById(R.id.btn_user_forgot_code_commit);
        mTvGetSmsAgain = (TextView) findViewById(R.id.tv_user_forgot_code_get_again);
    }

    private void initData() {
        mTvTitle.setText("找回密码");
        //初始化phone字符串和显示说明信息
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            mPhone = _bundle.getString("_phone");
            mTvPhoneInfo.setText(Html.fromHtml("您的手机<font color='#d81759'>" + mPhone + "</font>会收到一条含有6位数字验证码的短信"));
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UserForgotCodeActivity.this.finish();
            }
        });
        mTvGetSmsAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getcodeText = mTvGetSmsAgain.getText().toString();
                if (getcodeText != null && getcodeText.equals("重新发送验证码")) {
                    Intent _intent = new Intent(UserForgotCodeActivity.this, UserForgotPasswordActivity.class);
                    _intent.putExtra("_isEdit", "true");
                    _intent.putExtra("_phone", mPhone);
                    startActivity(_intent);
                    UserForgotCodeActivity.this.finish();
                } else {
                    Toast.makeText(UserForgotCodeActivity.this, "短信验证码正在发送,请耐心等待", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmsCode = mEtSmsCode.getText().toString();
                mNewPassword = mEtPassword.getText().toString();
                mAgainPassword = mEtPassAgain.getText().toString();
                if (mSmsCode == null || mSmsCode.equals("")) {
                    Toast.makeText(UserForgotCodeActivity.this, "您未填写短信验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mNewPassword == null || mNewPassword.equals("")) {
                    Toast.makeText(UserForgotCodeActivity.this, "您未填写新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAgainPassword == null || mAgainPassword.equals("")) {
                    Toast.makeText(UserForgotCodeActivity.this, "您未填写确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mNewPassword.equals(mAgainPassword)) {
                    Toast.makeText(UserForgotCodeActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //检查新密码的输入规则是否有误
                if (mNewPassword.length() < 6 || mNewPassword.length() > 15 || !CheckUtil.checkPassword(mNewPassword)) {
                    Toast.makeText(UserForgotCodeActivity.this, "密码格式是6到15位的字母数字组成", Toast.LENGTH_SHORT).show();
                    return;
                }
                ForgetPasswordFromNet(mPhone, mSmsCode, mNewPassword);
            }
        });
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(UserForgotCodeActivity activity) {
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
                    ((UserForgotCodeActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((UserForgotCodeActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_GET_REG_MOBILEMGS_VALIDATE_CAN_GET_AGAIN_SUCCESS:
                    //已重新发送验证码
                    String errorMsg4 = ("已重新发送验证码");
                    ((UserForgotCodeActivity) mActivity.get()).showTip(errorMsg4);
                    break;
                case CommonConstants.FLAG_CHANGE_PASSWORD_SUCCESS:
                    //重置成功
                    ((UserForgotCodeActivity) mActivity.get()).goLogin();
                    break;
                case CommonConstants.FLAG_MESSAGE_CODE_IS_OVERDUE:
                    //验证码已过期
                    String errorMsg5 = ("验证码已过期");
                    ((UserForgotCodeActivity) mActivity.get()).showTip(errorMsg5);
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
     * 出现图片验证码
     */
    private void goLogin() {
        this.showTip("密码重置成功");
        UserForgotCodeActivity.this.finish();
    }

    /**
     * 重置密码
     */
    private void ForgetPasswordFromNet(final String sPhone, String mVerifyCode, String mNewPassword) {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(UserForgotCodeActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ForgetPassword);
        //包装请求参数
        String _req_json = "{\"MobileNumber\":\"" + sPhone + "\",\"VerifyCode\":\"" + mVerifyCode + "\",\"NewPassword\":\"" + mNewPassword + "\"}";
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
                            handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_PASSWORD_SUCCESS);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1003")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_MESSAGE_CODE_IS_OVERDUE);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1004")) {
                                if (mProgressDialog != null) {
                                    mProgressDialog.dismiss();
                                }
                                Toast.makeText(UserForgotCodeActivity.this, "短信验证码不正确", Toast.LENGTH_LONG).show();
                                return;
                            } else if (_ErrorCode != null && _ErrorCode.equals("1004")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_MESSAGE_CODE_IS_OVERDUE);
                            } else {
                                CommonUtil.sendErrorMessage(_Message, handler);
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
