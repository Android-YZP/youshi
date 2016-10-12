package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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

public class RemarkInformationActivity extends Activity {

    private ImageView mIvBack;
    private EditText mEtRemark;
    private EditText mEtPhone1, mEtPhone2, mEtPhone3, mEtPhone4, mEtPhone5;
    private TextView mTvFinish;
    private View mLine1, mLine2, mLine3, mLine4;
    private String openFireName, remark;
    private User mUser;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_information);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_remark_information_back);
        mTvFinish = (TextView) findViewById(R.id.tv_remark_information_finish);
        mEtRemark = (EditText) findViewById(R.id.et_remark_information_remark);
        mEtPhone1 = (EditText) findViewById(R.id.et_remark_information_phone1);
        mEtPhone2 = (EditText) findViewById(R.id.et_remark_information_phone2);
        mEtPhone3 = (EditText) findViewById(R.id.et_remark_information_phone3);
        mEtPhone4 = (EditText) findViewById(R.id.et_remark_information_phone4);
        mEtPhone5 = (EditText) findViewById(R.id.et_remark_information_phone5);
        mLine1 = (View) findViewById(R.id.tv_remark_information_line1);
        mLine2 = (View) findViewById(R.id.tv_remark_information_line2);
        mLine3 = (View) findViewById(R.id.tv_remark_information_line3);
        mLine4 = (View) findViewById(R.id.tv_remark_information_line4);
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(this);
        mEtPhone2.setVisibility(View.GONE);
        mLine1.setVisibility(View.GONE);
        mEtPhone3.setVisibility(View.GONE);
        mLine2.setVisibility(View.GONE);
        mEtPhone4.setVisibility(View.GONE);
        mLine3.setVisibility(View.GONE);
        mEtPhone5.setVisibility(View.GONE);
        mLine4.setVisibility(View.GONE);
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            openFireName = _bundle.getString("_contactID");
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemarkInformationActivity.this.finish();
            }
        });
//        mTvFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                remark = mEtRemark.getText().toString();
//                if (remark != null && !remark.equals("")) {
//                    RemarkFriendFromNet(openFireName, remark);
//                }
//                RemarkInformationActivity.this.finish();
//            }
//        });
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(RemarkInformationActivity activity) {
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
                    ((RemarkInformationActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_CHANGE_NICKNAME_SUCCESS:
                    //修改成功
                    ((RemarkInformationActivity) mActivity.get()).updateUserInfo();
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((RemarkInformationActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR3:
                    //请求失败
                    String errorMsg3 = ("请求失败");
                    ((RemarkInformationActivity) mActivity.get()).showTip(errorMsg3);
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
//        mUser.setNickName(name);
        CommonUtil.saveUserInfo(mUser, RemarkInformationActivity.this);
        Toast.makeText(this, "已修改", Toast.LENGTH_LONG).show();
        RemarkInformationActivity.this.finish();
    }

    /**
     * 备注好友
     */
    private void RemarkFriendFromNet(final String openFireName, final String remark) {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(RemarkInformationActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.RemarkFriend);
        //包装请求参数
        String code = CommonUtil.getUserInfo(RemarkInformationActivity.this).getLoginCode();
        String _req_json = "{\"OpenFireName\":\"" + openFireName + "\",\"Remark\":\"" + remark + "\"}";
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
