package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
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

public class UserRegCodeActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private Button mBtnCommitCode;
    //手机号和验证码
    private String mPhone;
    private String mSmsCode;
    //手机介绍信息
    private TextView mTvPhoneInfo;
    private TextView mTvGetSmsAgain;
    //验证码
    private EditText mEtSmsCode;
    //业务层
//	private IUserBusiness mUserBusiness = new UserBusinessImp();
    private static ProgressDialog mProgressDialog = null;
    //定义倒计时handler
    private static Handler getcodeHandler;
    private int num;
    private Thread mDownTimeThread;
    private boolean mStopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg_code);
        initView();
        initData();
        setListener();
    }

    //	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		mStopThread = true;//中断线程
//	}
//
    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mBtnCommitCode = (Button) findViewById(R.id.btn_user_reg_getcode_code_commit);

        //手机号介绍信息
        mTvPhoneInfo = (TextView) findViewById(R.id.tv_user_reg_intro);
        //验证码
        mEtSmsCode = (EditText) findViewById(R.id.et_user_reg_code);
    }

    private void initData() {
        mTvTitle.setText("注册");
        //初始化phone字符串和显示说明信息
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            mPhone = _bundle.getString("_phone");
            mTvPhoneInfo.setText(Html.fromHtml("您的手机<font color='#d81759'>" + mPhone + "</font>会收到一条含有4位数字验证码的短信"));
        }
        //60s后重新获取验证码
        canGetSmsCodeAgain();
    }

    /**
     * 是否可以再次发送验证码
     */
    private void canGetSmsCodeAgain() {
        //倒计时获取验证码
        mTvGetSmsAgain = (TextView) findViewById(R.id.tv_user_reg_code_getcode_again);
        getcodeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //接文字更新页面
                int flag = msg.what;
                if (flag == CommonConstants.FLAG_GET_REG_MOBILEMGS_VALIDATE_CAN_GET_AGAIN_SUCCESS) {
                    int num = msg.getData().getInt("number");
                    if (num == 0) {
                        //倒计时结束
                        mTvGetSmsAgain.setText("重新发送验证码");
                    } else {
                        //倒计时
                        mTvGetSmsAgain.setText("重新发送验证码(" + num + "s)");
                    }
                }

            }
        };
        getCodeFromNet();//重新倒计时

    }

    private void getCodeFromNet() {
        mDownTimeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i;
                for (i = 60; i >= 0; i--) {
                    try {
                        if (mStopThread) {
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putInt("number", i);
                    msg.what = CommonConstants.FLAG_GET_REG_MOBILEMGS_VALIDATE_CAN_GET_AGAIN_SUCCESS;
                    msg.setData(data);
                    getcodeHandler.sendMessage(msg);
                }
            }
        });
        mDownTimeThread.start();
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRegCodeActivity.this.finish();
            }
        });

//		mTvGetSmsAgain.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				String getcodeText = mTvGetSmsAgain.getText().toString();
//				if (getcodeText!=null&&getcodeText.equals("重新发送验证码")) {
//					//重新发送验证码到该手机
//					checkPhoneFromNetGetSmsCodeAgain(mPhone);
//				}else{
//					Toast.makeText(UserRegCodeActivity.this, "短信验证码正在发送,请耐心等待", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
        mBtnCommitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSmsCode = mEtSmsCode.getText().toString();
                if (mSmsCode != null && !mSmsCode.equals("")) {
                    //弹出加载进度条
                    mProgressDialog = ProgressDialog.show(UserRegCodeActivity.this, "请稍等", "正在玩命提交中...", true, true);
                    //开启副线程-验证是否是该手机收到了验证码
                    checkSmsCodeFromNet(mSmsCode);
                    //若检查不通过、提示报错信息；检查通过，跳转到下一个界面
                    //进度条消失
                } else {
                    Toast.makeText(UserRegCodeActivity.this, "您未填写短信验证码", Toast.LENGTH_SHORT).show();
                }

            }

            private void checkSmsCodeFromNet(final String smsCode) {
                //使用xutils3访问网络并获取返回值
                RequestParams requestParams = new RequestParams(CommonConstants.MessageCodeIsValid);
                //包装请求参数
                String _req_json = "{\"sMobileNumber\":\"" + mPhone + "\",\"VerifyCode\":\"" + smsCode + "\"}";
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
                                    Intent _intent = new Intent(UserRegCodeActivity.this, UserRegUserNameActivity.class);
                                    _intent.putExtra("_phone", mPhone);
                                    _intent.putExtra("_code", mSmsCode);
                                    startActivity(_intent);
                                    UserRegCodeActivity.this.finish();
                                } else {
                                    Toast.makeText(UserRegCodeActivity.this, "验证码不正确", Toast.LENGTH_LONG).show();
                                    return;
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

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(UserRegCodeActivity activity) {
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
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    ((UserRegCodeActivity) mActivity.get()).showTip(errorMsg);
                    break;
//			case CommonConstants.FLAG_GET_REG_MOBILEMGS_VALIDATE_SUCCESS:
//				((UserRegCodeActivity)mActivity.get()).goNextActivity();
//				break;
//			case CommonConstants.FLAG_GET_REG_MOBILEMGS_REGISTER_SUCCESS:
//				//重新发送验证码
//				((UserRegCodeActivity)mActivity.get()).sendAgainSuccess();
//				break;
                default:
                    break;
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
//
//	public void sendAgainSuccess() {
//		getCodeFromNet();//重新倒计时
//		Toast.makeText(UserRegCodeActivity.this, "短信验证码已发送,请耐心等待", Toast.LENGTH_SHORT).show();
//	}
//
//	public void goNextActivity() {
//		Intent _intent = new Intent(UserRegCodeActivity.this,UserRegUserNameActivity.class);
//		_intent.putExtra("_phone", mPhone);
//		_intent.putExtra("_code", mSmsCode);
//
//		startActivity(_intent);
//		UserRegCodeActivity.this.finish();
//	}
//
//	//重新获取验证码
//	private void checkPhoneFromNetGetSmsCodeAgain(final String phone) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					//差登录用户id
//					String result = mUserBusiness.getMobilemsgRegister(phone);
//					Log.d(CommonConstants.LOGCAT_TAG_NAME + "_result_reg_code_getMobilemsgRegister", result);
//					JSONObject jsonObj = new JSONObject(result);
//					boolean Success = jsonObj.getBoolean("success");
//					if(Success){
//						//获取成功
//						handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_MOBILEMGS_REGISTER_SUCCESS);
//					}else{
//						//获取错误代码，并查询出错误文字
//						String errorMsg = jsonObj.getString("errorMsg");
//						CommonUtil.sendErrorMessage(errorMsg,handler);
//					}
//				} catch (ConnectTimeoutException e) {
//					e.printStackTrace();
//					CommonUtil.sendErrorMessage(CommonConstants.MSG_REQUEST_TIMEOUT,handler);
//				}catch (SocketTimeoutException e) {
//					e.printStackTrace();
//					CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_RESPONSE_TIMEOUT,handler);
//				}
//				catch (ServiceException e) {
//					e.printStackTrace();
//					CommonUtil.sendErrorMessage(e.getMessage(),handler);
//				} catch (Exception e) {
//					//what = 0;sendmsg 0;
//					CommonUtil.sendErrorMessage("注册-重新获取验证码："+CommonConstants.MSG_GET_ERROR,handler);
//				}
//			}
//		}).start();
//	}
}
