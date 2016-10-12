package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.JoinUserJson;
import com.mkch.youshi.bean.User;
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

public class UserRegUserNameActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    //密码和确认密码
    private EditText mEtPassword;
    private EditText mEtPassAgain;
    private Button mBtnRegister;
    private String mPhone;
    private String mCode;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg_username);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        //密码和确认密码
        mEtPassword = (EditText) findViewById(R.id.et_user_reg_password);
        mEtPassAgain = (EditText) findViewById(R.id.et_user_reg_password_again);
        mBtnRegister = (Button) findViewById(R.id.btn_user_reg_register_commit);
    }

    private void initData() {
        mEtPassword.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ',', '.', '?', '!', ';', ':'};
                return numberChars;
            }

            @Override
            public int getInputType() {
                return 15;
            }
        });
        mEtPassAgain.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ',', '.', '?', '!', ';', ':'};
                return numberChars;
            }

            @Override
            public int getInputType() {
                return 15;
            }
        });
        mBtnRegister.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ',', '.', '?', '!', ';', ':'};
                return numberChars;
            }

            @Override
            public int getInputType() {
                return 15;
            }
        });
        mTvTitle.setText("注册");
        //初始化手机号和验证码
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            mPhone = _bundle.getString("_phone");
            mCode = _bundle.getString("_code");
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(UserRegUserNameActivity activity) {
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
                    ((UserRegUserNameActivity) mActivity.get()).showTip(errorMsg);
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

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UserRegUserNameActivity.this.finish();
            }
        });
        //注册
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = mEtPassword.getText().toString();
                String passagain = mEtPassAgain.getText().toString();
                if (password == null || password.equals("")) {
                    Toast.makeText(UserRegUserNameActivity.this, "您未填写密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passagain == null || passagain.equals("")) {
                    Toast.makeText(UserRegUserNameActivity.this, "您未填写确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(passagain)) {
                    Toast.makeText(UserRegUserNameActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //检查密码、确认密码的输入规则是否输入有误
                if (passagain.length() < 6 || passagain.length() > 15 || !CheckUtil.checkPassword(password)) {
                    Toast.makeText(UserRegUserNameActivity.this, "密码格式是6到15位的字母数字和英文标点符号组成", Toast.LENGTH_SHORT).show();
                    return;
                }
                userRegisterFromNet(mPhone, password, mCode);//用户注册
            }

            private void userRegisterFromNet(final String mPhone, final String password, final String code) {
                //使用xutils3访问网络并获取返回值
                RequestParams requestParams = new RequestParams(CommonConstants.JOIN);
                //包装请求参数
                User user = new User();
                user.setMobileNumber(mPhone);
                user.setPassword(password);
                user.setClientType("Android");
                user.setVerifyCode(code);
                user.setClientVersion("V1.2");
                user.setOsUuid(CommonUtil.getMyUUID(UserRegUserNameActivity.this));
                JoinUserJson _reg_user = new JoinUserJson(user);
                final Gson gson = new Gson();
                String _user_json = gson.toJson(_reg_user);
                requestParams.addBodyParameter("", _user_json);//用户名
                requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {
                            try {
                                JSONObject _json_result = new JSONObject(result);
                                Boolean _success = (Boolean) _json_result.get("Success");
                                if (_success) {
                                    Toast.makeText(UserRegUserNameActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                    UserRegUserNameActivity.this.finish();
                                } else {
                                    String _Message = _json_result.getString("Message");
                                    CommonUtil.sendErrorMessage(_Message, handler);
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
        });
    }
}
