package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.MainActivity;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.JoinUserJson;
import com.mkch.youshi.bean.LoginUserJson;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CheckUtil;
import com.mkch.youshi.util.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class UserRegUserNameActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;

    //密码和确认密码
    private EditText mEtPassword;
    private EditText mEtPassAgain;
    private Button mBtnRegister;

    private String mPhone;
    private String mCode;
    private String mPassword;

//	//业务层
//	private IUserBusiness mUserBusiness = new UserBusinessImp();

    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg_username);
		initView();
		initData();
		setListener();
    }

	private void initView() {
		mIvBack = (ImageView)findViewById(R.id.iv_common_topbar_back);
		mTvTitle = (TextView)findViewById(R.id.tv_common_topbar_title);
		//密码和确认密码
		mEtPassword = (EditText)findViewById(R.id.et_user_reg_password);
		mEtPassAgain = (EditText)findViewById(R.id.et_user_reg_password_again);

		mBtnRegister = (Button)findViewById(R.id.btn_user_reg_register_commit);
	}

	private void initData() {
		mTvTitle.setText("注册");
		//初始化手机号和验证码
		Bundle _bundle = getIntent().getExtras();
		if(_bundle!=null){
			mPhone = _bundle.getString("_phone");
			mCode = _bundle.getString("_code");
		}
	}

	private void setListener() {
		mIvBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				UserRegUserNameActivity.this.finish();
			}
		});
		//注册
		mBtnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String password = mEtPassword.getText().toString();
				String passagain = mEtPassAgain.getText().toString();
				if(password==null||password.equals("")){
					Toast.makeText(UserRegUserNameActivity.this, "您未填写密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if(passagain==null||passagain.equals("")){
					Toast.makeText(UserRegUserNameActivity.this, "您未填写确认密码", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!password.equals(passagain)){
					Toast.makeText(UserRegUserNameActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				//检查密码、确认密码的输入规则是否输入有误
				if(passagain.length()<6||passagain.length()>15||!CheckUtil.checkPassword(password)){
					Toast.makeText(UserRegUserNameActivity.this, "密码格式是6到15位的字母数字组成", Toast.LENGTH_SHORT).show();
					return;
				}
				mPassword = password;//全局变量
				userRegisterFromNet(mPhone,password,mCode);//用户注册
			}

			private void userRegisterFromNet(final String mPhone,final String password, final String code) {
				//使用xutils3访问网络并获取返回值
				RequestParams requestParams = new RequestParams(CommonConstants.JOIN);
				//包装请求参数
				User user = new User();
				user.setMobileNumber(mPhone);
				user.setPassword(password);
				user.setClientType("Android");
				user.setVerifyCode(code);
				user.setClientVersion("V1.2");
				user.setOsUuid(CommonUtil.getMyUUID(UserRegUserNameActivity.this));
				JoinUserJson _reg_user = new JoinUserJson(user);
				final Gson gson = new Gson();
				String _user_json = gson.toJson(_reg_user);
				Log.d("zzzzzzzzzzzzzzz", "_user_json is ----------------" + _user_json);
				requestParams.addBodyParameter("", _user_json);//用户名
				requestParams.addHeader("sVerifyCode", "3D8829FE");//头信息
				x.http().post(requestParams, new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if (result != null) {
							Log.d("zzzzzzzzzzzzzz", "----onSuccess:" + result);
							try {
								JSONObject _json_result = new JSONObject(result);
								Boolean _success = (Boolean) _json_result.get("Success");
								if (_success) {
									Toast.makeText(UserRegUserNameActivity.this, "注册成功", Toast.LENGTH_LONG).show();
									UserRegUserNameActivity.this.finish();
								}else{
									Toast.makeText(UserRegUserNameActivity.this, "验证码不正确", Toast.LENGTH_LONG).show();
									return;
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
		});
	}
//
//	private static class MyHandler extends Handler{
//		private final WeakReference<Activity> mActivity;
//		public MyHandler(UserRegUserNameActivity activity) {
//			mActivity = new WeakReference<Activity>(activity);
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			if(mProgressDialog!=null){
//				mProgressDialog.dismiss();
//			}
//			int flag = msg.what;
//			switch (flag) {
//			case 0:
//				String errorMsg = (String)msg.getData().getSerializable("ErrorMsg");
//				((UserRegUserNameActivity)mActivity.get()).showTip(errorMsg);
//				break;
//			case CommonConstants.FLAG_GET_REG_USER_REGISTER_SUCCESS:
//				((UserRegUserNameActivity)mActivity.get()).regUserLogin();
//				break;
//			case CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS:
//				((UserRegUserNameActivity)mActivity.get()).saveUserInfo();
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//	private MyHandler handler = new MyHandler(this);
//
//	private void showTip(String str){
//		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//	}
//
//	public void regUserLogin(){
//		//开启副线程-发起登录
//		userLoginFromNet(mPhone,mPassword);
//	}
//
//	public void saveUserInfo() {
//		//保存用户信息，并关闭该界面
//		Log.d(CommonConstants.LOGCAT_TAG_NAME+"_user_login_info", mUser.toString());
//		CommonUtil.saveUserInfo(mUser,this);
//		Toast.makeText(UserRegUserNameActivity.this, "注册成功", Toast.LENGTH_LONG).show();
//		UserRegUserNameActivity.this.finish();
//	}
//
//	public void saveUserInfo(User user){
//		//构建对象
//		Gson gson = new Gson();
//		String gsonUser = gson.toJson(user);
//		//获取指定Key的SharedPreferences对象
//		SharedPreferences _SP = getSharedPreferences("UserInfo", MODE_PRIVATE);
//		//获取编辑
//		SharedPreferences.Editor _Editor = _SP.edit();
//		//按照指定Key放入数据
//		_Editor.putString("user", gsonUser);
//		//提交保存数据
//		_Editor.commit();
//	}
//
//	private User mUser;
//	private void userLoginFromNet(final String pPhone,final String pPassword) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					String result = mUserBusiness.getUserLogin(pPhone,pPassword);
//					Log.d(CommonConstants.LOGCAT_TAG_NAME + "_result_reg_user_login", result);
//					JSONObject jsonObj = new JSONObject(result);
//					boolean Success = jsonObj.getBoolean("success");
//					if(Success){
//						//填充用户信息
//						fullUserInfo(jsonObj);
//						//获取成功
//						handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS);
//					}else{
//						Message msg = new Message();
//						Bundle data = new Bundle();
//						//获取错误代码，并查询出错误文字
//						String errorMsg = jsonObj.getString("errorMsg");
//						data.putSerializable("ErrorMsg", errorMsg);
//						msg.setData(data);
//						handler.sendMessage(msg);
//					}
//				} catch (ConnectTimeoutException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", CommonConstants.MSG_REQUEST_TIMEOUT);
//					msg.setData(data);
//					handler.sendMessage(msg);
//				}catch (SocketTimeoutException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", CommonConstants.MSG_SERVER_RESPONSE_TIMEOUT);
//					msg.setData(data);
//					handler.sendMessage(msg);
//				}
//				catch (ServiceException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", e.getMessage());
//					msg.setData(data);
//					handler.sendMessage(msg);
//				} catch (Exception e) {
//					//what = 0;sendmsg 0;
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", "注册-用户登录："+CommonConstants.MSG_GET_ERROR);
//					msg.setData(data);
//					handler.sendMessage(msg);
//				}
//			}
//
//			private void fullUserInfo(JSONObject jsonObj) {
//				mUser = new User();
//				mUser.setAccount(JsonUtils.getString(jsonObj, "account"));
//				mUser.setPassword(JsonUtils.getString(jsonObj, "password"));
//				mUser.setId(JsonUtils.getInt(jsonObj, "id"));
//				mUser.setUserImg(JsonUtils.getString(jsonObj, "userImg"));
//				mUser.setUsername(JsonUtils.getString(jsonObj, "username"));
//				mUser.setMobile(JsonUtils.getString(jsonObj, "mobile"));
//				mUser.setVerifyCode(JsonUtils.getString(jsonObj, "verifyCode"));
//			}
//		}).start();
//	}
}
