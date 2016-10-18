package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.config.CommonConstants;

import java.lang.ref.WeakReference;

public class InformationSettingActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle, mTvSetting;
    private String contactID;
    private Button mBtnDelete;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_setting);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mTvSetting = (TextView) findViewById(R.id.tv_information_setting_setting);
        mBtnDelete = (Button) findViewById(R.id.btn_information_setting_delete_friend);
    }

    private void initData() {
        mTvTitle.setText("资料设置");
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            contactID = _bundle.getString("_contactID");
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InformationSettingActivity.this.finish();
            }
        });
        mTvSetting.setOnClickListener(new InformationSettingOnClickListener());
        mBtnDelete.setOnClickListener(new InformationSettingOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class InformationSettingOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.tv_information_setting_setting:
                    _intent = new Intent(InformationSettingActivity.this, RemarkInformationActivity.class);
                    _intent.putExtra("_contactID", contactID);
                    startActivity(_intent);
                    break;
                case R.id.btn_information_setting_delete_friend:
//                    deleteFriendFromNet(contactID);
                default:
                    break;
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(InformationSettingActivity activity) {
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
                    ((InformationSettingActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_DELETE_FRIEND_SUCCESS:
                    //删除成功
                    ((InformationSettingActivity) mActivity.get()).updateUserInfo();
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((InformationSettingActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR3:
                    //请求失败
                    String errorMsg3 = ("请求失败");
                    ((InformationSettingActivity) mActivity.get()).showTip(errorMsg3);
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
        Toast.makeText(this, "已删除", Toast.LENGTH_LONG).show();

        InformationSettingActivity.this.finish();
    }

//    /**
//     * 异步请求网络接口，删除该好友
//     *
//     * @param openfirename
//     */
//    private void deleteFriendFromNet(final String openfirename, final int position) {
//        if (getActivity() == null) {
//            return;
//        }
//        //弹出加载进度条
//        mProgressDialog = ProgressDialog.show(getActivity(), null, "加载中...", true, true);
//        //自己的信息
//        final User _self_user = CommonUtil.getUserInfo(getActivity());
//        //根据openfireusername查询该用户的信息，并保存于数据库
//        RequestParams requestParams = new RequestParams(CommonConstants.DeleteFriend);
//        //包装请求参数
//        String _req_json = "{\"OpenFireName\":\"" + openfirename + "\"}";
//        requestParams.addBodyParameter("", _req_json);//用户名
//        requestParams.addHeader("sVerifyCode", _self_user.getLoginCode());//头信息
//        x.http().post(requestParams, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                if (result != null) {
//                    //若result返回信息中删除成功
//                    try {
//                        JSONObject _json_result = new JSONObject(result);
//                        Boolean _success = (Boolean) _json_result.get("Success");
//                        if (_success) {
//                            //清除本地数据库该条好友信息，清除本地该条数据
//                            try {
//                                //本登录用户的，已添加状态的，好友
//                                Friend first = dbManager.selector(Friend.class)
//                                        .where("friendid", "=", openfirename)
//                                        .and("status", "=", 1)
//                                        .and("userid", "=", mUser.getOpenFireUserName())
//                                        .findFirst();
//                                if (first != null) {
//                                    dbManager.delete(first);
//                                }
//                            } catch (DbException e) {
//                                e.printStackTrace();
//                            }
//                            datas.remove(position);
//                            //提醒删除成功
//                            myHandler.sendEmptyMessage(CommonConstants.FLAG_DELETE_FRIEND_SUCCESS);
//                        } else {
//                            String _Message = _json_result.getString("Message");
//                            String _ErrorCode = _json_result.getString("ErrorCode");
//                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
//                                myHandler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
//                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
//                                myHandler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR3);
//                            } else {
//                                CommonUtil.sendErrorMessage(_Message, myHandler);
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, myHandler);
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                //使用handler通知UI提示用户错误信息
//                if (ex instanceof ConnectException) {
//                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, myHandler);
//                } else if (ex instanceof ConnectTimeoutException) {
//                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, myHandler);
//                } else if (ex instanceof SocketTimeoutException) {
//                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, myHandler);
//                } else {
//                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, myHandler);
//                }
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//            }
//
//            @Override
//            public void onFinished() {
//            }
//        });
//    }
}
