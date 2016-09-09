package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.UnLoginedUser;
import com.mkch.youshi.util.CheckUtil;
import com.mkch.youshi.util.CommonUtil;

import java.lang.ref.WeakReference;

public class UserRegPhoneActivity extends Activity {
    private ImageView mIvBack;
    private TextView mTvTitle;
    private Button mBtnCommitPhone;
    //手机号
    private EditText mEtPhone;
	//验证码
	private LinearLayout mLayoutCode;
	private EditText mEtCode;
	private ImageView mIvCode;
    //是否选中checkbox
    private CheckBox mCbIsRead;
    private TextView mTvIsRead;
    private TextView mTvProtocal;

	//未登录的user的信息
	private UnLoginedUser mUnLoginedUser;
//	//业务层
//	private IUserBusiness mUserBusiness = new UserBusinessImp();
	private String mPhone,mCode;

    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg_phone);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtPhone = (EditText) findViewById(R.id.et_user_reg_phone);
		mLayoutCode = (LinearLayout) findViewById(R.id.layout_user_reg_phone_code);
		mEtCode = (EditText) findViewById(R.id.et_user_reg_phone_code);
		mIvCode = (ImageView) findViewById(R.id.iv_user_reg_phone_code);
        mBtnCommitPhone = (Button) findViewById(R.id.btn_user_reg_getcode_phone_commit);
        mCbIsRead = (CheckBox) findViewById(R.id.cb_user_reg_phone_ischecked);
        mTvIsRead = (TextView) findViewById(R.id.tv_user_reg_phone_isread);
        mTvProtocal = (TextView) findViewById(R.id.tv_user_reg_phone_read_protocal);
    }

    private void initData() {
        mTvTitle.setText("注册");
		//获取未登录时app内保存的信息
		mUnLoginedUser = CommonUtil.getUnLoginedUser(this);
		if(mUnLoginedUser!=null&&mUnLoginedUser.getTokenID()!=null&&!mUnLoginedUser.getTokenID().equals("")){
			String _tokenID = mUnLoginedUser.getTokenID();
			isShowPicCodeFromNet(_tokenID);
		}
	}

	/**
	 * 查看是否需要显示图片验证码
	 * @param _tokenID
	 */
	private void isShowPicCodeFromNet(final String _tokenID) {
		//弹出进度框
		mProgressDialog = ProgressDialog.show(UserRegPhoneActivity.this, null, "加载中..",true,false);
		//开启副线程-查看是否需要显示图片验证码
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					//tokenID
//					String tokenID = coverTokenID();
//
//					String _tokenID_result = regUserService.isShowImagePicCode(tokenID);
//					Log.i("jlj-regphone-result-isShowImagePicCode=======", _tokenID_result);
//					JSONObject tokenIDResult = new JSONObject(_tokenID_result);
//					JSONObject tokenIDQueryResult = tokenIDResult.getJSONObject("QueryResult");
//					boolean tokenIDSuccess = tokenIDQueryResult.getBoolean("Success");
//					if(tokenIDSuccess){
//						m_pic_code_url = CommonConstants.NOW_ADDRESS + tokenIDQueryResult.getString("PicCode");
//						//通知界面显示图片验证码
//						handler.sendEmptyMessage(FLAG_TO_GET_PICCODE_AGAIN_SUCCESS);
//					}else{
//
//						//获取错误代码，并查询出错误文字
//						String ErrorCode = tokenIDQueryResult.getString("ErrorCode");
//						if(ErrorCode!=null&&ErrorCode.equals("1040")){
//							//TokenID不存在
//							handler.sendEmptyMessage(FLAG_TO_GET_TOKENID_AGAIN_SUCCESS);
//						}else{
//							handler.sendEmptyMessage(FLAG_TO_GET_TOKENID_SUCCESS);
//						}
//
//					}
//				} catch (ConnectTimeoutException e) {
//					e.printStackTrace();
//					CommonUtil.sendErrorMessage(MSG_REQUEST_TIMEOUT,handler);
//				}catch (SocketTimeoutException e) {
//					e.printStackTrace();
//					CommonUtil.sendErrorMessage(MSG_SERVER_RESPONSE_TIMEOUT,handler);
//				}
//				catch (ServiceException e) {
//					e.printStackTrace();
//					CommonUtil.sendErrorMessage(e.getMessage(),handler);
//				} catch (Exception e) {
//					//what = 0;sendmsg 0;
//					e.printStackTrace();
//					CommonUtil.sendErrorMessage(MSG_CHECK_ERROR,handler);
//				}
//			}
//		}).start();
	}

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRegPhoneActivity.this.finish();
            }
        });
		mBtnCommitPhone.setOnClickListener(new UserRegPhoneOnClickListener());
//		mTvIsRead.setOnClickListener(new UserRegPhoneOnClickListener());
//		mTvProtocal.setOnClickListener(new UserRegPhoneOnClickListener());
    }
//
//
	private static class MyHandler extends Handler {
		private final WeakReference<Activity> mActivity;
		public MyHandler(UserRegPhoneActivity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if(mProgressDialog!=null){
				mProgressDialog.dismiss();
			}
			int flag = msg.what;
			switch (flag) {
//			case 0:
//				String errorMsg = (String)msg.getData().getSerializable("ErrorMsg");
//				((UserRegPhoneActivity)mActivity.get()).showTip(errorMsg);
//				break;
//			case CommonConstants.FLAG_GET_REG_MOBILEMGS_REGISTER_SUCCESS:
//				((UserRegPhoneActivity)mActivity.get()).goNextActivity();
//				break;
			default:
				break;
			}
		}
	}
//
//	private MyHandler handler = new MyHandler(this);
//
//	private void showTip(String str){
//		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//	}
//
//	/**
//	 * 进入下一个注册流程
//	 */
//	public void goNextActivity() {
//		Intent _intent = new Intent(UserRegPhoneActivity.this,UserRegCodeActivity.class);
//		_intent.putExtra("_phone", mPhone);
//		startActivity(_intent);
//		UserRegPhoneActivity.this.finish();
//	}
//
	private class UserRegPhoneOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_user_reg_getcode_phone_commit:
				UserCommitPhoneNumer();
				break;
//			case R.id.tv_user_reg_phone_read_protocal:
//				//用户协议
//				Intent _intent = new Intent(UserRegPhoneActivity.this,ArticleDetailActivity.class);
//				_intent.putExtra("_article_title", "用户协议");
//				_intent.putExtra("_article_id", 13);//用户协议
//				UserRegPhoneActivity.this.startActivity(_intent);
//				break;
			case R.id.tv_user_reg_phone_isread:
				//是否已读
				if(mCbIsRead.isChecked()){
					mCbIsRead.setChecked(false);
				}else{
					mCbIsRead.setChecked(true);
				}
				break;
			default:
				break;
			}
		}

	}
	/**
	 * 用户提交手机号码
	 */
	public void UserCommitPhoneNumer() {
		mPhone = mEtPhone.getText().toString();
		mCode = mEtCode.getText().toString();
		boolean ischecked = mCbIsRead.isChecked();
		if(!ischecked){
			Toast.makeText(UserRegPhoneActivity.this, "您未同意注册协议", Toast.LENGTH_SHORT).show();
			return;
		}
		if(mPhone!=null&&!mPhone.equals("")){
			if(!CheckUtil.checkMobile(mPhone)){
				Toast.makeText(UserRegPhoneActivity.this, "手机号格式输入有误", Toast.LENGTH_LONG).show();
				return;
			}
			//弹出加载进度条
			mProgressDialog = ProgressDialog.show(UserRegPhoneActivity.this, "请稍等", "正在玩命获取中...",true,true);
			mProgressDialog = new ProgressDialog(UserRegPhoneActivity.this, R.style.progress_dialog);
			mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			mProgressDialog.show();
			//开启副线程-检查手机号码是否存在
//			checkPhoneFromNet(mPhone);
			//若检查不通过、提示报错信息；检查通过，跳转到下一个界面
			//进度条消失
		}else{
			Toast.makeText(UserRegPhoneActivity.this, "您未填写手机号", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 开启副线程-检查手机号码是否存在
	 * @param phone
	 */
//	private void checkPhoneFromNet(final String phone) {
//		//使用xutils3访问网络并获取返回值
//		RequestParams requestParams = new RequestParams(CommonConstants.JOIN);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					String result = mUserBusiness.getMobilemsgRegister(phone);
//					Log.d(CommonConstants.LOGCAT_TAG_NAME + "_result_reg_phone_getMobilemsgRegister", result);
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
//					CommonUtil.sendErrorMessage("注册-获取验证码："+CommonConstants.MSG_GET_ERROR,handler);
//				}
//			}
//		}).start();
//	}
}
