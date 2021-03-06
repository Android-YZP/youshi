package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.PhoneContactAdapter;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.ContactEntity;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.view.HanziToPinyin;
import com.mkch.youshi.view.SideBar;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class PhoneContactsActivity extends KJActivity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {
    //联系人名称
    private List<ContactEntity> mContacts = new ArrayList<>();
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
    private DbManager dbManager;//数据库管理对象
    private boolean isAdd;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_phone_contacts);
    }

    public void initData() {
        super.initData();
        dbManager = DBHelper.getDbManager();
        try {
            List<ContactEntity> contactEntities = dbManager.selector(ContactEntity.class).findAll();
            if (contactEntities != null && contactEntities.size() > 0) {
                mAdapter = new PhoneContactAdapter(mListView, contactEntities, this);
                mListView.setAdapter(mAdapter);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
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
                case CommonConstants.FLAG_CHANGE_ERROR1:
                    //认证错误
                    String errorMsg1 = ("认证错误");
                    ((PhoneContactsActivity) mActivity.get()).showTip(errorMsg1);
                    break;
                case CommonConstants.FLAG_CHANGE_ERROR3:
                    //请求失败
                    String errorMsg3 = ("请求失败");
                    ((PhoneContactsActivity) mActivity.get()).showTip(errorMsg3);
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
     * 加载联系人列表
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
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            //获取通讯录列表，遍历每个电话号码是否注册，是否已添加
                            JSONArray datas = _json_result.getJSONArray("Datas");
                            for (int i = 0; i < datas.length(); i++) {
                                JSONObject jobj = datas.getJSONObject(i);
                                String openFireUserName = jobj.getString("OpenFireUserName");
                                mContacts.get(i).setContactID(openFireUserName);
                                if (jobj.getString("HeadPic") != null && !jobj.getString("HeadPic").equals("") && !jobj.getString("HeadPic").equals("null")) {
                                    mContacts.get(i).setHeadPic(CommonConstants.TEST_ADDRESS_PRE + jobj.getString("HeadPic"));
                                }
                                isAdd = jobj.getBoolean("IsAdd");
                                int status = 0;
                                if (isAdd) {
                                    status = 1;
                                } else {
                                    status = 0;
                                }
                                mContacts.get(i).setStatus(status);
                                boolean IsRegister = jobj.getBoolean("IsRegister");
                                mContacts.get(i).setRegister(IsRegister);
                            }
                            //删除呢未注册的手机联系人信息
                            for (int i = 0; i < mContacts.size(); i++) {
                                if (!mContacts.get(i).isRegister()) {
                                    mContacts.remove(i);
                                    i--;
                                }
                            }
                            try {
                                //存储前先清空数据库中的手机联系人
                                List<ContactEntity> contactEntities = dbManager.selector(ContactEntity.class).findAll();
                                if (contactEntities != null && contactEntities.size() > 0) {
                                    dbManager.delete(contactEntities);
                                }

                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            User _self_user = CommonUtil.getUserInfo(PhoneContactsActivity.this);
                            String userID = _self_user.getOpenFireUserName();
                            //存储所有的已注册手机联系人列表数据
                            for (int i = 0; i < mContacts.size(); i++) {
                                String contactID = mContacts.get(i).getContactID();
                                String headPic = mContacts.get(i).getHeadPic();
                                String name = mContacts.get(i).getName();
                                String pinyin = mContacts.get(i).getPinyin();
                                String number = mContacts.get(i).getNumber();
                                int status = mContacts.get(i).getStatus();
                                //获取所有的优时好友列表
                                ContactEntity contactEntity = new ContactEntity(contactID, headPic, name, pinyin, number, status, userID);
                                try {
                                    dbManager.save(contactEntity);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            myHandler.sendEmptyMessage(CommonConstants.FLAG_GET_PHONE_CONTACT_SHOW);
                        } else {
                            String _Message = _json_result.getString("Message");
                            String _ErrorCode = _json_result.getString("ErrorCode");
                            if (_ErrorCode != null && _ErrorCode.equals("1001")) {
                                myHandler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR1);
                            } else if (_ErrorCode != null && _ErrorCode.equals("1002")) {
                                myHandler.sendEmptyMessage(CommonConstants.FLAG_CHANGE_ERROR3);
                            } else {
                                CommonUtil.sendErrorMessage(_Message, myHandler);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
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
