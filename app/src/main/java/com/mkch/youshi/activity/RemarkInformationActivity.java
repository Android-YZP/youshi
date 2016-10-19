package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class RemarkInformationActivity extends Activity {

    private ImageView mIvBack;
    private EditText mEtRemark;
    private EditText mEtPhone1, mEtPhone2, mEtPhone3, mEtPhone4, mEtPhone5, mEtDescription;
    private TextView mTvFinish;
    private View mLine1, mLine2, mLine3, mLine4;
    private String openFireName, remark, phone, phone1, phone2, phone3, phone4, phone5, description;
    private ArrayList<String> remarkPhones = new ArrayList<>();
    private static ProgressDialog mProgressDialog = null;
    private User mUser;
    private DbManager dbManager;//数据库管理对象

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
        mEtDescription = (EditText) findViewById(R.id.et_remark_information_describe);
        mLine1 = (View) findViewById(R.id.tv_remark_information_line1);
        mLine2 = (View) findViewById(R.id.tv_remark_information_line2);
        mLine3 = (View) findViewById(R.id.tv_remark_information_line3);
        mLine4 = (View) findViewById(R.id.tv_remark_information_line4);
    }

    private void initData() {
        mEtPhone2.setVisibility(View.GONE);
        mLine1.setVisibility(View.GONE);
        mEtPhone3.setVisibility(View.GONE);
        mLine2.setVisibility(View.GONE);
        mEtPhone4.setVisibility(View.GONE);
        mLine3.setVisibility(View.GONE);
        mEtPhone5.setVisibility(View.GONE);
        mLine4.setVisibility(View.GONE);
        mUser = CommonUtil.getUserInfo(this);
        dbManager = DBHelper.getDbManager();
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            openFireName = _bundle.getString("_contactID");
            try {
                Friend friend = dbManager.selector(Friend.class)
                        .where("friendid", "=", openFireName)
                        .and("userid", "=", mUser.getOpenFireUserName())
                        .findFirst();
                //加载好友备注
                if (friend.getRemark() != null && !friend.getRemark().equals("") && !friend.getRemark().equals("null")) {
                    mEtRemark.setText(friend.getRemark());
                }
                //加载好友备注电话
                if (friend.getPhone_number() != null && !friend.getPhone_number().equals("") && !friend.getPhone_number().equals("null")) {
                    String[] phones = friend.getPhone_number().split("\\|");
                    for (int i = 0; i < phones.length; i++) {
                        if (phones[i] != null && !phones[i].equals("") && !phones[i].equals("null")) {
                            remarkPhones.add(phones[i]);
                        }
                    }
                    showPhone();
                }
                //加载好友描述
                if (friend.getDescription() != null && !friend.getDescription().equals("") && !friend.getDescription().equals("null")) {
                    mEtDescription.setText(friend.getDescription());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemarkInformationActivity.this.finish();
            }
        });
        mEtPhone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mEtPhone2.setVisibility(View.GONE);
                    mLine1.setVisibility(View.GONE);
                } else {
                    mEtPhone2.setVisibility(View.VISIBLE);
                    mLine1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtPhone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mEtPhone3.setVisibility(View.GONE);
                    mLine2.setVisibility(View.GONE);
                } else {
                    mEtPhone3.setVisibility(View.VISIBLE);
                    mLine2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtPhone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mEtPhone4.setVisibility(View.GONE);
                    mLine3.setVisibility(View.GONE);
                } else {
                    mEtPhone4.setVisibility(View.VISIBLE);
                    mLine3.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEtPhone4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mEtPhone5.setVisibility(View.GONE);
                    mLine4.setVisibility(View.GONE);
                } else {
                    mEtPhone5.setVisibility(View.VISIBLE);
                    mLine4.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remark = mEtRemark.getText().toString();
                phone1 = mEtPhone1.getText().toString();
                phone2 = mEtPhone2.getText().toString();
                phone3 = mEtPhone3.getText().toString();
                phone4 = mEtPhone4.getText().toString();
                phone5 = mEtPhone5.getText().toString();
                phone = phone1 + "|" + phone2 + "|" + phone3 + "|" + phone4 + "|" + phone5;
                description = mEtDescription.getText().toString();
                RemarkFriendFromNet(openFireName, remark, phone, description);
            }
        });
    }

    //根据获取的备注电话号码，来判断如何显示界面
    private void showPhone() {
        switch (remarkPhones.size()) {
            case 1:
                mLine1.setVisibility(View.VISIBLE);
                mEtPhone2.setVisibility(View.VISIBLE);
                mEtPhone1.setText(remarkPhones.get(0));
                break;
            case 2:
                mLine1.setVisibility(View.VISIBLE);
                mLine2.setVisibility(View.VISIBLE);
                mEtPhone2.setVisibility(View.VISIBLE);
                mEtPhone3.setVisibility(View.VISIBLE);
                mEtPhone1.setText(remarkPhones.get(0));
                mEtPhone2.setText(remarkPhones.get(1));
                break;
            case 3:
                mLine1.setVisibility(View.VISIBLE);
                mLine2.setVisibility(View.VISIBLE);
                mLine3.setVisibility(View.VISIBLE);
                mEtPhone2.setVisibility(View.VISIBLE);
                mEtPhone3.setVisibility(View.VISIBLE);
                mEtPhone4.setVisibility(View.VISIBLE);
                mEtPhone1.setText(remarkPhones.get(0));
                mEtPhone2.setText(remarkPhones.get(1));
                mEtPhone3.setText(remarkPhones.get(2));
                break;
            case 4:
                mLine1.setVisibility(View.VISIBLE);
                mLine2.setVisibility(View.VISIBLE);
                mLine3.setVisibility(View.VISIBLE);
                mLine4.setVisibility(View.VISIBLE);
                mEtPhone2.setVisibility(View.VISIBLE);
                mEtPhone3.setVisibility(View.VISIBLE);
                mEtPhone4.setVisibility(View.VISIBLE);
                mEtPhone5.setVisibility(View.VISIBLE);
                mEtPhone1.setText(remarkPhones.get(0));
                mEtPhone2.setText(remarkPhones.get(1));
                mEtPhone3.setText(remarkPhones.get(2));
                mEtPhone4.setText(remarkPhones.get(3));
                break;
            case 5:
                mLine1.setVisibility(View.VISIBLE);
                mLine2.setVisibility(View.VISIBLE);
                mLine3.setVisibility(View.VISIBLE);
                mLine4.setVisibility(View.VISIBLE);
                mEtPhone2.setVisibility(View.VISIBLE);
                mEtPhone3.setVisibility(View.VISIBLE);
                mEtPhone4.setVisibility(View.VISIBLE);
                mEtPhone5.setVisibility(View.VISIBLE);
                mEtPhone1.setText(remarkPhones.get(0));
                mEtPhone2.setText(remarkPhones.get(1));
                mEtPhone3.setText(remarkPhones.get(2));
                mEtPhone4.setText(remarkPhones.get(3));
                mEtPhone5.setText(remarkPhones.get(4));
                break;
            default:
                break;
        }
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
        Toast.makeText(this, "已修改", Toast.LENGTH_LONG).show();
        RemarkInformationActivity.this.finish();
    }

    /**
     * 备注好友
     */
    private void RemarkFriendFromNet(final String openFireName, final String remark, final String phone, final String description) {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(RemarkInformationActivity.this, "请稍等", "正在修改中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.RemarkFriend);
        //包装请求参数
        String code = CommonUtil.getUserInfo(RemarkInformationActivity.this).getLoginCode();
        String _req_json = "{\"OpenFireName\":\"" + openFireName + "\",\"Remark\":\"" + remark + "\",\"PhoneNumber\":\"" + phone + "\",\"Description\":\"" + description + "\"}";
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
                            try {
                                Friend friend = dbManager.selector(Friend.class)
                                        .where("friendid", "=", openFireName)
                                        .and("userid", "=", mUser.getOpenFireUserName())
                                        .findFirst();
                                friend.setRemark(remark);
                                friend.setPhone_number(phone);
                                friend.setDescription(description);
                                dbManager.saveOrUpdate(friend);
                                handler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_NICKNAME_SUCCESS);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
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
