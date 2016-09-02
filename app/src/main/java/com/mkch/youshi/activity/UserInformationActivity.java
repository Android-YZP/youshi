package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.MainActivity;
import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;

public class UserInformationActivity extends Activity {

	private ImageView mIvBack;
	private TextView mTvTitle;
	private LinearLayout mLayoutName,mLayoutYoushiNumber,mLayoutSex,mLayoutSignature;
//	//业务层
//	private IUserBusiness mUserBusiness = new UserBusinessImp();
//	private static ProgressDialog mProgressDialog = null;
//	private User mUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
		mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
		mLayoutName = (LinearLayout) findViewById(R.id.layout_user_information_name);
		mLayoutYoushiNumber = (LinearLayout) findViewById(R.id.layout_user_information_youshi_number);
		mLayoutSex = (LinearLayout) findViewById(R.id.layout_user_information_sex);
		mLayoutSignature = (LinearLayout) findViewById(R.id.layout_user_information_signature);
//		mBtnCommitCode = (Button)findViewById(R.id.btn_user_forgot_commit);
//
//		//手机号介绍信息
//		mTvPhoneInfo = (TextView)findViewById(R.id.tv_user_forgot_intro);
//		//验证码
//		mEtSmsCode = (EditText)findViewById(R.id.et_user_forgot_code);
//		//新密码和确认密码
//		mEtPassword = (EditText)findViewById(R.id.et_user_forgot_password);
//		mEtPassAgain = (EditText)findViewById(R.id.et_user_forgot_password_again);
	}

	private void initData() {
		mTvTitle.setText("个人信息");
	}

	private void setListener() {
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				UserInformationActivity.this.finish();
			}
		});
		mLayoutName.setOnClickListener(new UserInformationOnClickListener());
		mLayoutYoushiNumber.setOnClickListener(new UserInformationOnClickListener());
		mLayoutSex.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(UserInformationActivity.this).setTitle("您的性别为").setSingleChoiceItems(
						new String[] { "男", "女" }, 0, new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();
			}
		});
		mLayoutSignature.setOnClickListener(new UserInformationOnClickListener());
//		mBtnLogin.setOnClickListener(new UserLoginOnClickListener());
//		mTvGoRegister.setOnClickListener(new UserLoginOnClickListener());
//		mTvGoForgot.setOnClickListener(new UserLoginOnClickListener());
//		mBtnVisitByEasy.setOnClickListener(new UserLoginOnClickListener());
	}
//
//	private static class MyHandler extends Handler{
//		private final WeakReference<Activity> mActivity;
//		public MyHandler(UserLoginActivity activity) {
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
//				((UserLoginActivity)mActivity.get()).showTip(errorMsg);
//				break;
//			case CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS:
//				((UserLoginActivity)mActivity.get()).saveUserInfo();
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
//	public void saveUserInfo() {
//		//保存用户信息，并关闭该界面
//		Log.d(CommonConstants.LOGCAT_TAG_NAME+"_user_login_info", mUser.toString());
//		CommonUtil.saveUserInfo(mUser,this);
//		Toast.makeText(UserLoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
//		UserLoginActivity.this.finish();
//	}
//
	/**
	 * 自定义点击监听类
	 * @author JLJ
	 *
	 */
	private class UserInformationOnClickListener implements View.OnClickListener {
	@Override
	public void onClick(View view) {
		Intent _intent = null;
		switch (view.getId()) {
			case R.id.layout_user_information_name:
				_intent = new Intent(UserInformationActivity.this,ReviseNameActivity.class);
				startActivity(_intent);
				break;
			case R.id.layout_user_information_youshi_number:
				_intent = new Intent(UserInformationActivity.this,ReviseYoushiNumberActivity.class);
				startActivity(_intent);
				break;
			case R.id.layout_user_information_signature:
				_intent = new Intent(UserInformationActivity.this,ReviseSignatureActivity.class);
				startActivity(_intent);
				break;
			default:
				break;
		}
	}
}
//
//		private void userLoginFromNet(final String account, final String password) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						String result = mUserBusiness.getUserLogin(account,password);
//						Log.d(CommonConstants.LOGCAT_TAG_NAME + "_result_user_login_getUserLogin", result);
//						JSONObject jsonObj = new JSONObject(result);
//						boolean Success = jsonObj.getBoolean("success");
//						if(Success){
//							//填充用户信息
//							fullUserInfo(jsonObj);
//							//获取成功
//							handler.sendEmptyMessage(CommonConstants.FLAG_GET_REG_USER_LOGIN_SUCCESS);
//						}else{
//							//获取错误代码，并查询出错误文字
//							String errorMsg = jsonObj.getString("errorMsg");
//							CommonUtil.sendErrorMessage(errorMsg,handler);
//						}
//					} catch (ConnectTimeoutException e) {
//						e.printStackTrace();
//						CommonUtil.sendErrorMessage(CommonConstants.MSG_REQUEST_TIMEOUT,handler);
//					}catch (SocketTimeoutException e) {
//						e.printStackTrace();
//						CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_RESPONSE_TIMEOUT,handler);
//					}
//					catch (ServiceException e) {
//						e.printStackTrace();
//						CommonUtil.sendErrorMessage(e.getMessage(),handler);
//					} catch (Exception e) {
//						//what = 0;sendmsg 0;
//						CommonUtil.sendErrorMessage("注册-用户登录："+CommonConstants.MSG_GET_ERROR,handler);
//					}
//				}
//				/**
//				 * 填充用户信息
//				 * @param jsonObj
//				 */
//				private void fullUserInfo(JSONObject jsonObj) {
//					mUser = new User();
//					mUser.setAccount(JsonUtils.getString(jsonObj, "account"));
//					mUser.setPassword(JsonUtils.getString(jsonObj, "password"));
//					mUser.setId(JsonUtils.getInt(jsonObj, "id"));
//					mUser.setUserImg(JsonUtils.getString(jsonObj, "userImg"));
//					mUser.setUsername(JsonUtils.getString(jsonObj, "username"));
//					mUser.setMobile(JsonUtils.getString(jsonObj, "mobile"));
//					mUser.setVerifyCode(JsonUtils.getString(jsonObj, "verifyCode"));
//				}
//			}).start();
//		}
//
//	}
}
