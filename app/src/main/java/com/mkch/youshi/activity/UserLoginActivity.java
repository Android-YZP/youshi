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
import com.mkch.youshi.bean.LoginUserJson;
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

/**
 * 用户登录界面
 */
public class UserLoginActivity extends Activity {

    private EditText mEtAccount;//用户名
    private EditText mEtPassword;//密码
    private String mAccount;//手机号
    private LinearLayout mLayoutCode;//控制验证码是否显示
    private EditText mEtCode;//验证码
    private ImageView mIvCode;//验证码图片
    private Button mBtnLogin;//登录按钮
    private TextView mTvGoRegister;//去注册
    private TextView mTvGoForgot;//去忘记密码
    private static ProgressDialog mProgressDialog = null;//登录加载
    private User mUser;//用户信息
    private String mPicUrl;//图片验证码地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //便于测试去掉登录
        User _user = CommonUtil.getUserInfo(this);
        if (_user!=null){
            Intent _intent = new Intent(this,MainActivity.class);
            startActivity(_intent);
            this.finish();
            return;
        }

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
        mIvCode.setOnClickListener(new UserLoginOnClickListener());
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
                case CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_SHOW:
                    //图片验证码出现
                    ((UserLoginActivity) mActivity.get()).showImgVerfy();
                    break;
                case CommonConstants.FLAG_NO_GET_PICCODE:
                    //不需要图片验证码，直接发起登录
                    ((UserLoginActivity) mActivity.get()).sendLoginFromNet();
                    break;
                case CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_ERROR:
                    //图片验证码错误
                    ((UserLoginActivity) mActivity.get()).imgVerifyError();
                    break;
                case CommonConstants.FLAG_GET_REG_USER_LOGIN_ACCOUNT_OR_PASSWORD_ERROR:
                    //手机号或密码错误,提示并刷新验证码
                    ((UserLoginActivity) mActivity.get()).accountError();
                    break;
                case CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_CHAGE:
                    //手动切换图片验证码
                    ((UserLoginActivity) mActivity.get()).changeImgVerfy();
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    /**
     * 出现图片验证码
     */
    private void showImgVerfy() {
        mLayoutCode.setVisibility(View.VISIBLE);
        Toast.makeText(UserLoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
        x.image().bind(mIvCode, mPicUrl);
    }

    /**
     * 图片验证码错误-提示并重刷图片验证码
     */
    private void imgVerifyError() {
        this.showTip("验证码错误");
        //把ImageView重新刷新图片验证码
        changeImgVerfy();//更新图片验证码
    }

    /**
     * 手机号或密码错误-提示并重刷图片验证码
     */
    private void accountError() {
        this.showTip("手机号或密码错误");
        //把ImageView重新刷新图片验证码
        changePicCodeFromNet();//更新图片验证码
    }

    /**
     * 手动切换图片验证码
     */
    private void changeImgVerfy() {
        x.image().bind(mIvCode, mPicUrl);
    }

    /**
     * 发起登录请求
     */
    private void sendLoginFromNet() {
        String account = mEtAccount.getText().toString();
        String password = mEtPassword.getText().toString();
        userLoginFromNet(account, password, "");
    }

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存用户信息，提示登录成功，销毁本界面，进入主界面
     */
    public void goMain() {
        Toast.makeText(UserLoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
        UserLoginActivity.this.finish();
        Intent _intent = new Intent(this, MainActivity.class);
        startActivity(_intent);
    }

    /**
     * 检查是否需要图片验证码
     */
    protected void checkIsNeedValidImgFromNet() {
        mAccount = mEtAccount.getText().toString();
        //检查是否需要图片验证码
        RequestParams requestParams = new RequestParams(CommonConstants.CheckLoginPickCodeExist);
        //包装请求参数
        String _req_json = "{\"sMobileNumber\":\"" + mAccount + "\"}";
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
                            handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_SHOW);
                        } else {
                            handler.sendEmptyMessage(CommonConstants.FLAG_NO_GET_PICCODE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
    }

    /**
     * 自定义点击监听类
     */
    private class UserLoginOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.iv_user_login_code:
                    changePicCodeFromNet();
                    break;
                case R.id.btn_user_login_commit:
                    String account = mEtAccount.getText().toString();
                    String password = mEtPassword.getText().toString();
                    String code = mEtCode.getText().toString();
                    int isVisibel = mLayoutCode.getVisibility();
                    if (account == null || account.equals("")) {
                        Toast.makeText(UserLoginActivity.this, "您未填写手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password == null || password.equals("")) {
                        Toast.makeText(UserLoginActivity.this, "您未填写密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!CheckUtil.checkMobile(account)) {
                        Toast.makeText(UserLoginActivity.this, "手机号格式输入有误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isVisibel == View.VISIBLE && mEtCode.equals("")) {
                        Toast.makeText(UserLoginActivity.this, "您未填写验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isVisibel == View.VISIBLE && mEtCode == null) {
                        Toast.makeText(UserLoginActivity.this, "您未填写验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isVisibel == View.GONE) {
                        checkIsNeedValidImgFromNet();
                    } else {
                        userLoginFromNet(account, password, code);
                    }
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
    }

    /**
     * 发起网络登录
     *
     * @param account  账号
     * @param password 密码
     */
    private void userLoginFromNet(final String account, final String password, final String code) {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(UserLoginActivity.this, "请稍等", "正在登录中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.LOGIN);
        //包装请求参数
        User user = new User();
        user.setMobileNumber(account);
        user.setPassword(password);
        user.setClientType("Android");
        user.setImageVerifyCode(code);
        user.setOsUuid(CommonUtil.getMyUUID(UserLoginActivity.this));
        LoginUserJson _login_user = new LoginUserJson(user);
        final Gson gson = new Gson();
        String _user_json = gson.toJson(_login_user);
        requestParams.addBodyParameter("", _user_json);//用户名
        requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("jlj","---------------------result = "+result);
                if (result != null) {
                    //若result返回信息中登录成功，解析json数据并存于本地，再使用handler通知UI更新界面并进行下一步逻辑
                    try {
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            JSONObject datas = _json_result.getJSONObject("Datas");
                            //填充本地用户信息
                            if (datas != null) {
                                User user = new User();
                                user.setMobileNumber(datas.getString("MobileNumber"));
                                user.setNickName(datas.getString("NickName"));
                                user.setLoginCode(datas.getString("LoginCode"));
                                user.setOpenFireUserName(datas.getString("OpenfireUserName"));
                                user.setPassword(password);
                                CommonUtil.saveUserInfo(user, UserLoginActivity.this);
                            }
                            //提醒登录成功
                            handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1004")) {
                                JSONObject datas = _json_result.getJSONObject("Datas");
                                mPicUrl = CommonConstants.NOW_ADDRESS_PRE + datas.getString("PicCodePath");
                                handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_ERROR);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1007")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_ACCOUNT_OR_PASSWORD_ERROR);
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
                Log.d("jlj", "-------onError = " + ex.getMessage());
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

    /**
     * 切换登录图片验证码
     */
    private void changePicCodeFromNet() {
        String phone = mEtAccount.getText().toString();
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ReloadLoginPicCode);
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
