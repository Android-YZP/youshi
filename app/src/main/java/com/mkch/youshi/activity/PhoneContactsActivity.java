package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.PhoneContactAdapter;
import com.mkch.youshi.bean.ContactEntity;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.view.HanziToPinyin;
import com.mkch.youshi.view.SideBar;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class PhoneContactsActivity extends KJActivity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {

    //联系人名称
    private ArrayList<ContactEntity> mContacts = new ArrayList<>();
    private ArrayList<String> mNumbers = new ArrayList<>();
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
    //联系人显示名称
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    //电话号码
    private static final int PHONES_NUMBER_INDEX = 1;
    private ImageView mIvBack;
    @BindView(id = R.id.list_phone_contacts)
    private ListView mListView;
    private PhoneContactAdapter mAdapter;
    private static ProgressDialog mProgressDialog = null;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_phone_contacts);
    }

    public void initData() {
        super.initData();
        getPhoneContacts();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        SideBar mSideBar = (SideBar) findViewById(R.id.sidebar_phone_contacts);
        TextView mDialog = (TextView) findViewById(R.id.tv_phone_contacts_dialog);
        mIvBack = (ImageView) findViewById(R.id.iv_phone_contacts__back);
        EditText mSearchInput = (EditText) findViewById(R.id.et_phone_contacts_search);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneContactsActivity.this.finish();
            }
        });
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(PhoneContactsActivity activity) {
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
                    ((PhoneContactsActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_GET_PHONE_CONTACT_SHOW:
                    //加载手机通讯录列表
                    ((PhoneContactsActivity) mActivity.get()).showListVerfy();
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler myHandler = new MyHandler(this);

    private void showTip(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取联系人列表
     */
    private void showListVerfy() {
        mAdapter = new PhoneContactAdapter(mListView, mContacts, this);
        mListView.setAdapter(mAdapter);
    }

    private void getPhoneContacts() {
        ContentResolver resolver = PhoneContactsActivity.this.getContentResolver();
        try {
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    // 得到手机号码
                    String phoneNumber = phoneCursor
                            .getString(PHONES_NUMBER_INDEX);
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor
                            .getString(PHONES_DISPLAY_NAME_INDEX);
                    ContactEntity mContact = new ContactEntity();
                    mContact.setName(contactName);
                    mContact.setNumber(phoneNumber);
                    mNumbers.add(phoneNumber);
                    mContact.setPinyin(HanziToPinyin.getPinYin(contactName));
                    mContacts.add(mContact);
                }
                getPhoneContactsFromNet();
                phoneCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取通讯录信息
     */
    private void getPhoneContactsFromNet() {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(PhoneContactsActivity.this, "请稍等", "正在获取中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.GetContactsInfo);
        //包装请求参数
        String code = CommonUtil.getUserInfo(PhoneContactsActivity.this).getLoginCode();
        final ArrayList<String> mPhones = new ArrayList<>();
        for (String str : mNumbers) {
            mPhones.add("\"" + str + "\"");
        }
        String _req_json = "{\"mobilelist\":" + mPhones.toString() + "}";
        _req_json = _req_json.replace(" ", "");//将有空格的地方进行替换
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        Log.d("zzzzzzzzzzzzzzzzzz", "----result:" + result);
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            //获取通讯录列表，遍历每个电话号码是否注册，是否已添加
                            JSONArray datas = _json_result.getJSONArray("Datas");
                            for (int i = 0; i < datas.length(); i++) {
                                JSONObject jobj = datas.getJSONObject(i);
                                String openFireUserName = jobj.getString("OpenFireUserName");
                                mContacts.get(i).setOpenFireUserName(openFireUserName);
                                boolean isAdd = jobj.getBoolean("IsAdd");
                                mContacts.get(i).setAdd(isAdd);
                            }
                            for (int i = 0; i < mContacts.size(); i++) {
                                if (mContacts.get(i).getOpenFireUserName().equals("null")) {
                                    mContacts.remove(i);
                                    i--;
                                }
                            }
                            myHandler.sendEmptyMessage(CommonConstants.FLAG_GET_PHONE_CONTACT_SHOW);
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
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, myHandler);
                } else if (ex instanceof ConnectTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, myHandler);
                } else if (ex instanceof SocketTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, myHandler);
                } else {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, myHandler);
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

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<ContactEntity> temp = new ArrayList<>(mContacts);
        for (ContactEntity data : mContacts) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
            mAdapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
