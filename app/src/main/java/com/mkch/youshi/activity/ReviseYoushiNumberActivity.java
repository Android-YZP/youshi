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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReviseYoushiNumberActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtYoushiNumber;
    private String youshiNumber;
    private Button mBtnConfirm;
    private User mUser;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_youshi_number);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mUser = CommonUtil.getUserInfo(this);
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtYoushiNumber = (EditText) findViewById(R.id.et_revise_youshi_number);
        mBtnConfirm = (Button) findViewById(R.id.btn_revise_youshi_number_confirm);
    }

    private void initData() {
        mTvTitle.setText("设置优时号");
        mEtYoushiNumber.setKeyListener(new NumberKeyListener() {
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
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviseYoushiNumberActivity.this.finish();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youshiNumber = mEtYoushiNumber.getText().toString();
                if (youshiNumber == null || youshiNumber.equals("")) {
                    Toast.makeText(ReviseYoushiNumberActivity.this, "您未填写优时号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (youshiNumber.length() < 6) {
                    Toast.makeText(ReviseYoushiNumberActivity.this, "优时号不能少于6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                Pattern p = Pattern.compile("[a-zA-Z]");
                Matcher m = p.matcher(youshiNumber.substring(0, 1));
                if (!m.matches()) {
                    Toast.makeText(ReviseYoushiNumberActivity.this, "优时号必须以字母开头", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mUser.getYoushiNumber() == null || mUser.getYoushiNumber().equals("")) {
                    ChangeNickNameFromNet();
                }
            }
        });
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(ReviseYoushiNumberActivity activity) {
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
                    ((ReviseYoushiNumberActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((ReviseYoushiNumberActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_CHANGE_YOUSHI_NUMBER_IS_EXIST:
                    //认证错误
                    String errorMsg2 = ("优时号已存在,请重新输入");
                    ((ReviseYoushiNumberActivity) mActivity.get()).showTip(errorMsg2);
                    break;
                case CommonConstants.FLAG_CHANGE_YOUSHI_NUMBER_FAIL:
                    //认证错误
                    String errorMsg3 = ("保存失败");
                    ((ReviseYoushiNumberActivity) mActivity.get()).showTip(errorMsg3);
                    break;
                case CommonConstants.FLAG_CHANGE_YOUSHI_NUMBER_SUCCESS:
                    //修改成功
                    ((ReviseYoushiNumberActivity) mActivity.get()).updateUserInfo();
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
        mUser.setYoushiNumber(youshiNumber);
        CommonUtil.saveUserInfo(mUser, ReviseYoushiNumberActivity.this);
        Toast.makeText(this, "已修改", Toast.LENGTH_LONG).show();
        ReviseYoushiNumberActivity.this.finish();
    }

    /**
     * 修改优时账号
     */
    private void ChangeNickNameFromNet() {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(ReviseYoushiNumberActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.ChangeYoShiUserName);
        //包装请求参数
        String code = CommonUtil.getUserInfo(ReviseYoushiNumberActivity.this).getLoginCode();
        youshiNumber = mEtYoushiNumber.getText().toString();
        String _req_json = "{\"UserName\":\"" + youshiNumber + "\"}";
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
                            handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_YOUSHI_NUMBER_SUCCESS);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_YOUSHI_NUMBER_IS_EXIST);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1003")) {
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_YOUSHI_NUMBER_FAIL);
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
