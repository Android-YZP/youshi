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

public class ReviseNameActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtName;
    private String name;
    private Button mBtnConfirm;
    private User mUser;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_name);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mUser = CommonUtil.getUserInfo(this);
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtName = (EditText) findViewById(R.id.et_revise_name);
        mBtnConfirm = (Button) findViewById(R.id.btn_revise_name_confirm);
    }

    private void initData() {
        mTvTitle.setText("修改昵称");
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviseNameActivity.this.finish();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEtName.getText().toString();
                if (name == null || name.equals("")) {
                    Toast.makeText(ReviseNameActivity.this, "您未填写昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                ChangeNickNameFromNet();
            }
        });
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(ReviseNameActivity activity) {
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
                    ((ReviseNameActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_CHANGE_NICKNAME_SUCCESS:
                    //修改成功
                    ((ReviseNameActivity) mActivity.get()).updateUserInfo();
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((ReviseNameActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR3:
                    //请求失败
                    String errorMsg3 = ("请求失败");
                    ((ReviseNameActivity) mActivity.get()).showTip(errorMsg3);
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
        mUser.setNickName(name);
        CommonUtil.saveUserInfo(mUser, ReviseNameActivity.this);
        Toast.makeText(this, "已修改", Toast.LENGTH_LONG).show();
        ReviseNameActivity.this.finish();
    }

    /**
     * 修改用户昵称
     */
    private void ChangeNickNameFromNet() {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(ReviseNameActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ChangeNickName);
        //包装请求参数
        String code = CommonUtil.getUserInfo(ReviseNameActivity.this).getLoginCode();
        name = mEtName.getText().toString();
        String _req_json = "{\"NickName\":\"" + name + "\"}";
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
                            handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_NICKNAME_SUCCESS);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR3);
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
