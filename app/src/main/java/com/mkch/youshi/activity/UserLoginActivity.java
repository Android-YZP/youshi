package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.MainActivity;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.HttpResultBean;
import com.mkch.youshi.bean.LoginUserJson;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CommonUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 用户登录界面
 */
public class UserLoginActivity extends Activity {

    private EditText mEtAccount;//用户名
    private EditText mEtPassword;//密码
    private LinearLayout mLayoutCode;
    private EditText mEtCode;//验证码
    private ImageView mIvCode;//验证码图片
    private Button mBtnLogin;//登录按钮
    private TextView mTvGoRegister;//去注册
    private TextView mTvGoForgot;//去忘记密码

    private static ProgressDialog mProgressDialog = null;//登录加载
    private User mUser;//用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        initView();
        setListener();
    }

    private void initView() {
        mEtAccount = (EditText) findViewById(R.id.et_user_login_account);
        mEtPassword = (EditText) findViewById(R.id.et_user_login_password);
        mLayoutCode = (LinearLayout) findViewById(R.id.layout_user_login_code);
        mEtCode = (EditText) findViewById(R.id.et_user_login_code);
        mIvCode = (ImageView) findViewById(R.id.iv_user_login_code);
        mBtnLogin = (Button) findViewById(R.id.btn_user_login_commit);
        mTvGoRegister = (TextView) findViewById(R.id.tv_user_login_reg);
        mTvGoForgot = (TextView) findViewById(R.id.tv_user_login_forget);
        mLayoutCode.setVisibility(View.GONE);
    }

    private void setListener() {
        mBtnLogin.setOnClickListener(new UserLoginOnClickListener());
        mTvGoRegister.setOnClickListener(new UserLoginOnClickListener());
        mTvGoForgot.setOnClickListener(new UserLoginOnClickListener());
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(UserLoginActivity activity) {
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
                    ((UserLoginActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS:
                    //登录成功
                    ((UserLoginActivity) mActivity.get()).goMain();
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
     * 保存用户信息，提示登录成功，销毁本界面，进入主界面
     */
    public void goMain() {
        //保存用户信息，并关闭该界面
        CommonUtil.saveUserInfo(mUser, this);
        Toast.makeText(UserLoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
        UserLoginActivity.this.finish();
        Intent _intent = new Intent(this, MainActivity.class);
        startActivity(_intent);
    }

    private class UserLoginOnClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.btn_user_login_commit:
                    String account = mEtAccount.getText().toString();
                    String password = mEtPassword.getText().toString();

                    if (account == null || account.equals("")) {
                        Toast.makeText(UserLoginActivity.this, "您未填写手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password == null || password.equals("")) {
                        Toast.makeText(UserLoginActivity.this, "您未填写密码", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //弹出加载进度条
                    mProgressDialog = ProgressDialog.show(UserLoginActivity.this, "请稍等", "正在登录中...", true, true);
                    //发起网络登录
                    userLoginFromNet(account, password);
                    break;
                case R.id.tv_user_login_reg:
                    _intent = new Intent(UserLoginActivity.this, UserRegPhoneActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.tv_user_login_forget:
                    _intent = new Intent(UserLoginActivity.this, UserForgotCodeActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }

        /**
         * 发起网络登录
         *
         * @param account  账号
         * @param password 密码
         */
        private void userLoginFromNet(final String account, final String password) {
            //使用xutils3访问网络并获取返回值
            RequestParams requestParams = new RequestParams(CommonConstants.LOGIN);
            //包装请求参数
            User user = new User();
            user.setMobileNumber(account);
            user.setPassword(password);
            user.setClientType("Android");
            user.setOsUuid(CommonUtil.getMyUUID(UserLoginActivity.this));
            LoginUserJson _login_user = new LoginUserJson(user);
            final Gson gson = new Gson();
            String _user_json = gson.toJson(_login_user);
            Log.d("jlj", "_user_json is ----------------" + _user_json);
            requestParams.addBodyParameter("", _user_json);//用户名
            requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
            x.http().post(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (result != null) {
                        Log.d("userLogin", "----onSuccess:" + result);
                        //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                        HttpResultBean httpResultBean = gson.fromJson(result, HttpResultBean.class);
                        if (httpResultBean.getSuccess()){
                            //填充本地用户信息

                            //提醒登录成功
                            handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS);
                        }else{
                            CommonUtil.sendErrorMessage(httpResultBean.getMessage(), handler);
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
                    Log.d("userLogin", "----onCancelled");
                }

                @Override
                public void onFinished() {
                    Log.d("userLogin", "----onFinished");
                    //使用handler通知UI取消进度加载对话框

                }
            });
        }

    }
}
