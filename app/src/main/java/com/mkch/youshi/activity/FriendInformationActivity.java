package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

import org.w3c.dom.Text;

public class FriendInformationActivity extends Activity {

	private ImageView mIvBack;
	private TextView mTvMore,mTvRemark,mTvSetting,mTvLine1,mTvLine2,mTvLine3,mTvLine4,mTvLine5,mTvLine6;
	private LinearLayout mLayoutPhone,mLayoutLabel,mLayoutDescribe;
//	//业务层
//	private IUserBusiness mUserBusiness = new UserBusinessImp();
//	private static ProgressDialog mProgressDialog = null;
//	private User mUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_information);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		mIvBack = (ImageView) findViewById(R.id.iv_friend_information_back);
		mTvMore = (TextView) findViewById(R.id.tv_friend_information_more);
		mTvSetting = (TextView) findViewById(R.id.tv_friend_information_setting);
		mTvRemark = (TextView) findViewById(R.id.tv_friend_information_setting);
		mLayoutPhone = (LinearLayout) findViewById(R.id.layout_friend_information_phone);
		mLayoutLabel = (LinearLayout) findViewById(R.id.layout_friend_information_label);
		mLayoutDescribe = (LinearLayout) findViewById(R.id.layout_friend_information_describe);
		mTvLine1 = (TextView) findViewById(R.id.tv_friend_information_line1);
		mTvLine2 = (TextView) findViewById(R.id.tv_friend_information_line2);
		mTvLine3 = (TextView) findViewById(R.id.tv_friend_information_line3);
		mTvLine4 = (TextView) findViewById(R.id.tv_friend_information_line4);
		mTvLine5 = (TextView) findViewById(R.id.tv_friend_information_line5);
		mTvLine6 = (TextView) findViewById(R.id.tv_friend_information_line6);
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
		mLayoutPhone.setVisibility(View.GONE);
		mLayoutLabel.setVisibility(View.GONE);
		mLayoutDescribe.setVisibility(View.GONE);
		mTvLine5.setVisibility(View.GONE);
		mTvLine6.setVisibility(View.GONE);
	}

	private void setListener() {
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FriendInformationActivity.this.finish();
			}
		});
		mTvMore.setOnClickListener(new FriendInformationOnClickListener());
		mTvSetting.setOnClickListener(new FriendInformationOnClickListener());
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
	*/
	private class FriendInformationOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			Intent _intent = null;
			switch (view.getId()) {
				case R.id.tv_friend_information_more:
					_intent = new Intent(FriendInformationActivity.this,InformationSettingActivity.class);
					startActivity(_intent);
					break;
				case R.id.tv_friend_information_setting:
					_intent = new Intent(FriendInformationActivity.this,InformationSettingActivity.class);
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
