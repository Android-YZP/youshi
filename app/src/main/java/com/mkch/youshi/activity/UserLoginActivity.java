package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.NumberKeyListener;
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
import com.mkch.youshi.bean.UnLoginedUser;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.PrefUtils;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;

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
    private long exitTime = 0;
    private String nickName;//昵称
    private String headUrl;//头像地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //当前app的真实版本号
        int currentCode = CommonUtil.getAppVersion(this).getVersionCode();
        UnLoginedUser unLoginedUser = CommonUtil.getUnLoginedUser(this);
        //是否首次安装
        if (unLoginedUser == null) {
            //覆盖versioncode=20
            unLoginedUser = new UnLoginedUser(currentCode, "");
            CommonUtil.saveUnLoginedUser(unLoginedUser, this);
//            //创建launch图标
//            CommonUtil.createShortCut(this);


            //第一次进入activity进入引导页面
            if (!PrefUtils.getBoolean(this, "is_user_guide_showed", false)) {
                startActivity(new Intent(UserLoginActivity.this, GuideActivity.class));
                finish();
            }

        } else {
            boolean isCoverInstallNewApp = false;//是否覆盖安装新版本的app
            //当前是最新版，就不用清除数据
            int oldversioncode = unLoginedUser.getVersioncode();//获取versioncode=20
            //21,20
            Log.i("jlj-userversion", "currentCode = " + currentCode + ",oldcode = " + oldversioncode);
            if (currentCode > oldversioncode) {
                isCoverInstallNewApp = true;//覆盖安装新版本的app
            }
            //覆盖安装，清除数据，跳转引导页，并覆盖用户版本信息
            if (isCoverInstallNewApp) {
//                Log.i("jlj-userversion-fugai", "clean");
//                //清除数据
//                DataCleanManager2.cleanApplicationData(MainActivity.this, new String[0]);
                //覆盖当前app的真实版本号versioncode=21
                unLoginedUser.setVersioncode(currentCode);
                CommonUtil.saveUnLoginedUser(unLoginedUser, this);
                return;
            }
        }
        //便于测试去掉登录
        User _user = CommonUtil.getUserInfo(this);
        if (_user != null) {
            Intent _intent = new Intent(this, MainActivity.class);
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
        //设置账户和密码输入格式和长度限制
        mEtAccount.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '_', '-'};
                return numberChars;
            }

            @Override
            public int getInputType() {
                return 20;
            }
        });
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
    }

    /**
     * 第二次点击返回，退出
     */
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(UserLoginActivity.this, "再按一次退出优时", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        UserLoginActivity.this.finish();
        super.onBackPressed();
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
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((UserLoginActivity) mActivity.get()).showTip(errorMsg1);
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
        changeImgVerfy();//更新图片验证码
    }

    /**
     * 手机号或密码错误-提示并重刷图片验证码
     */
    private void accountError() {
        this.showTip("账号或密码错误");
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
        //上传昵称到腾讯云
        TIMFriendshipManager.getInstance().setNickName(nickName, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Log.d("zzz-----setNickName", code + "Error:" + desc);
            }

            @Override
            public void onSuccess() {
                Log.d("zzz-----setNickName", "setNickName is success");
            }
        });
        if (headUrl != null) {
            //上传头像到腾讯云
            TIMFriendshipManager.getInstance().setFaceUrl(headUrl, new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    Log.d("zzz-----setFaceUrl", i + "Error:" + s);
                }

                @Override
                public void onSuccess() {
                    Log.d("zzz-----setFaceUrl", "setFaceUrl is success");
                }
            });
        }
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
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_NO_GET_PICCODE);
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
                        Toast.makeText(UserLoginActivity.this, "您未填写账号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password == null || password.equals("")) {
                        Toast.makeText(UserLoginActivity.this, "您未填写密码", Toast.LENGTH_SHORT).show();
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
                    //弹出加载进度条
                    mProgressDialog = ProgressDialog.show(UserLoginActivity.this, null, "正在加载中...", true, true);
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
                    _intent = new Intent(UserLoginActivity.this, UserForgotPasswordActivity.class);
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
                                if (datas.getString("NickName") != null && !datas.getString("NickName").equals("")) {
                                    nickName = datas.getString("NickName");
                                } else {
                                    nickName = datas.getString("OpenfireUserName");
                                }
                                user.setNickName(datas.getString("NickName"));
                                user.setYoushiNumber(datas.getString("UserName"));
                                if (datas.getString("HeadPic") != null && !datas.getString("HeadPic").equals("") && !datas.getString("HeadPic").equals("null")) {
                                    headUrl = CommonConstants.TEST_ADDRESS_PRE + datas.getString("HeadPic");
                                    user.setHeadPic(headUrl);
                                }
                                user.setLoginCode(datas.getString("LoginCode"));
                                if (datas.getString("UserName") != null && !datas.getString("UserName").equals("") && !datas.getString("UserName").equals("null")) {
                                    user.setYoushiNumber(datas.getString("UserName"));
                                }
                                user.setSex(datas.getString("Sex"));
                                user.setAddress(datas.getString("Place"));
                                user.setSignature(datas.getString("Sign"));
                                user.setProtected(datas.getBoolean("Protected"));
                                user.setAddmeVerify(datas.getBoolean("AddmeVerify"));
                                user.setViewMySchedule(datas.getBoolean("ViewMySchedule"));
                                user.setOpenFireUserName(datas.getString("OpenfireUserName"));
                                user.setPassword(password);
                                user.setUserSig(datas.getString("UserSign"));
                                CommonUtil.saveUserInfo(user, UserLoginActivity.this);
                            }
                            //提醒登录成功
                            handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1004")) {
                                JSONObject datas = _json_result.getJSONObject("Datas");
                                mPicUrl = CommonConstants.NOW_ADDRESS_PRE + datas.getString("PicCodePath");
                                handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_ERROR);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1006")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_SHOW);
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
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
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
