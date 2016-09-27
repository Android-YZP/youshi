package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
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

public class RevisePasswordActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtOld, mEtNew, mEtConfirm;
    private String oldpwd, newpwd, againpwd;
    private Button mBtnCommit;
    private User mUser;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_password);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mUser = CommonUtil.getUserInfo(this);
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtOld = (EditText) findViewById(R.id.et_revise_password_old);
        mEtNew = (EditText) findViewById(R.id.et_revise_password_new);
        mEtConfirm = (EditText) findViewById(R.id.et_revise_password_confirm);
        mBtnCommit = (Button) findViewById(R.id.btn_revise_password_commit);
    }

    private void initData() {
        mTvTitle.setText("修改密码");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RevisePasswordActivity.this.finish();
            }
        });
        mBtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpwd = mEtOld.getText().toString();
                newpwd = mEtNew.getText().toString();
                againpwd = mEtConfirm.getText().toString();
                if (oldpwd == null || oldpwd.equals("")) {
                    Toast.makeText(RevisePasswordActivity.this, "您未填写原密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newpwd == null || newpwd.equals("")) {
                    Toast.makeText(RevisePasswordActivity.this, "您未填写新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (againpwd == null || againpwd.equals("")) {
                    Toast.makeText(RevisePasswordActivity.this, "您未填写确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newpwd.equals(againpwd)) {
                    Toast.makeText(RevisePasswordActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //检查新密码的输入规则是否有误
                if (newpwd.length() < 6 || newpwd.length() > 15 || !CheckUtil.checkPassword(newpwd)) {
                    Toast.makeText(RevisePasswordActivity.this, "密码格式是6到15位的字母数字组成", Toast.LENGTH_SHORT).show();
                    return;
                }
                //检查新旧密码是否相同
                if (oldpwd.equals(newpwd)) {
                    Toast.makeText(RevisePasswordActivity.this, "新密码不能和原密码相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                ChangePasswordFromNet();
            }
        });
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(RevisePasswordActivity activity) {
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
                    ((RevisePasswordActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_CHANGE_PASSWORD_SUCCESS:
                    //修改成功
                    ((RevisePasswordActivity) mActivity.get()).updateUserInfo();
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((RevisePasswordActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_CHANGE_PASSWORD_ERROR2:
                    //原密码错误
                    String errorMsg2 = ("原密码错误");
                    ((RevisePasswordActivity) mActivity.get()).showTip(errorMsg2);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR3:
                    //请求失败
                    String errorMsg3 = ("请求失败");
                    ((RevisePasswordActivity) mActivity.get()).showTip(errorMsg3);
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

    public void updateUserInfo() {
        mUser.setPassword(newpwd);
        CommonUtil.saveUserInfo(mUser, RevisePasswordActivity.this);
        Toast.makeText(this, "已修改", Toast.LENGTH_LONG).show();
        RevisePasswordActivity.this.finish();
    }

    /**
     * 修改用户密码
     */
    private void ChangePasswordFromNet() {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(RevisePasswordActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ChangePassword);
        //包装请求参数
        String code = CommonUtil.getUserInfo(RevisePasswordActivity.this).getLoginCode();
        String _req_json = "{\"Password\":\"" + oldpwd + "\",\"NewPassword\":\"" + newpwd + "\"}";
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
                            handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_PASSWORD_SUCCESS);
                        }else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_PASSWORD_ERROR2);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1003")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR3);
                            }else {
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
