package com.mkch.youshi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.activity.BaseActivity;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.fragment.ContactsFragment;
import com.mkch.youshi.fragment.MessageFragment;
import com.mkch.youshi.fragment.TodayFragment;
import com.mkch.youshi.fragment.UserCenterFragment;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.receiver.FriendsReceiver;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.view.IndexTabBarLayout;
import com.mkch.youshi.view.NoScrollViewPager;
import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private IndexTabBarLayout mIndexTabBarLayout;//底部整个控件
    private NoScrollViewPager mViewPager;
    private int CACHE_PAGES = 3;
    //四个fragment
    private Fragment mTodayFragment;
    private Fragment mMessageFragment;
    private Fragment mContactsFragment;
    private Fragment mUserCenterFragment;
    private String mMonthChooseDate;
    //数据库管理对象
    private DbManager dbManager;
    //用户信息
    private User mUser;
    private String identify, userSig;
    public static final int ACCOUNT_TYPE = 7882;
    public static final int SDK_APPID = 1400016695;

    //广播接收
    private FriendsReceiver mFriendsReceiver;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int _what = msg.what;
            switch (_what) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case FriendsReceiver.RECEIVE_REQUEST_ADD_FRIEND:
                    String _friend_json = (String) msg.obj;
                    Gson _gson = new Gson();
                    Friend _friend = _gson.fromJson(_friend_json, Friend.class);
                    //昵称
                    String _nickname = _friend.getNickname();
                    String _content_text_username = null;
                    if (_nickname != null && !_nickname.equals("") && !_nickname.equals("null")) {
                        _content_text_username = _nickname;
                    } else {
                        _content_text_username = _friend.getFriendid();
                    }
                    //更新UI，刷新待接受的优时好友数量
                    updateUIfromReceiver();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 从广播接收后更新UI的待接受好友的数量
     */
    private void updateUIfromReceiver() {
        TextView _tv_new_friends_recevier_num = (TextView) mContactsFragment.getView().findViewById(R.id.tv_contacts_new_friend_number);
        String _self_openfirename = mUser.getOpenFireUserName();
        //从数据库获取请求好友的数量，并设置
        String _req_friend_num = "0";
        try {
            dbManager = DBHelper.getDbManager();
            //本登录用户的，待接受，并显示在新朋友的数量
            long count = dbManager.selector(Friend.class)
                    .where("status", "=", "2")
                    .and("showinnewfriend", "=", "1")
                    .and("userid", "=", _self_openfirename)
                    .count();
            _req_friend_num = String.valueOf(count);//请求好友的数量
            Log.d("jlj", "MainActivity---------------------" + _req_friend_num);
        } catch (DbException e) {
            e.printStackTrace();
        }
        //待接受好友数量，显示在UI控件
        if (_req_friend_num.equals("0")) {
            _tv_new_friends_recevier_num.setVisibility(View.GONE);

        } else {
            _tv_new_friends_recevier_num.setVisibility(View.VISIBLE);
            _tv_new_friends_recevier_num.setText(_req_friend_num);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TIMManager.getInstance().init(getApplicationContext());
        initView();
        initData();
        setListener();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        findView();
    }

    /**
     * 找到所有视图
     */
    private void findView() {
        mIndexTabBarLayout = (IndexTabBarLayout) findViewById(R.id.myIndexTabBarLayout);
        mViewPager = (NoScrollViewPager) findViewById(R.id.myViewPager);
        mViewPager.setOffscreenPageLimit(CACHE_PAGES);//设置预加载界面数量
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mIndexTabBarLayout.changeTabBarItems(position);
            }

            @Override
            public void onPageScrolled(int postion, float percent, int pxLocation) {
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
    }

    private void initData() {
        mTodayFragment = new TodayFragment();
        mMessageFragment = new MessageFragment();
        mContactsFragment = new ContactsFragment();
        mUserCenterFragment = new UserCenterFragment();
        mUser = CommonUtil.getUserInfo(this);
        tlsLogin();
    }

    //登录IM功能
    private void tlsLogin() {
        identify = mUser.getOpenFireUserName();
        userSig = mUser.getUserSig();
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(SDK_APPID));
        user.setIdentifier(identify);
        TIMManager.getInstance().login(SDK_APPID, user, userSig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz----------imsdkLogin", i + "Error:" + s);
            }

            @Override
            public void onSuccess() {
                Log.d("zzz----------imsdkLogin", "login is success");
            }
        });
    }

    /**
     * 设置点击切换界面监听
     */
    private void setListener() {
        mIndexTabBarLayout.setOnItemClickListener(new IndexTabBarLayout.IIndexTabBarCallBackListener() {
            @Override
            public void clickItem(int id) {
                switch (id) {
                    case R.id.index_today_item:
                        mViewPager.setCurrentItem(IndexTabBarLayout.FLAG_TODAY);//点击后设置当前页是主页
                        break;
                    case R.id.index_message_item:
                        mViewPager.setCurrentItem(IndexTabBarLayout.FLAG_MESSAGE);
                        break;
                    case R.id.index_contact_item:
                        mViewPager.setCurrentItem(IndexTabBarLayout.FLAG_CONTACT);
                        break;
                    case R.id.index_pscenter_item:
                        mViewPager.setCurrentItem(IndexTabBarLayout.FLAG_PSCENTER);
                        break;
                }
            }
        });
    }


    /**
     * 自定义ViewPager的适配器
     *
     * @author JLJ
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int postion) {
            switch (postion) {
                case IndexTabBarLayout.FLAG_TODAY:
                    return mTodayFragment;
                case IndexTabBarLayout.FLAG_MESSAGE:
                    return mMessageFragment;
                case IndexTabBarLayout.FLAG_CONTACT:
                    return mContactsFragment;
                case IndexTabBarLayout.FLAG_PSCENTER:
                    return mUserCenterFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    private long exitTime = 0;

    /**
     * 第二次点击返回，退出
     */
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            // ToastUtil.makeToastInBottom("再按一次退出应用", MainMyselfActivity);
            Toast.makeText(MainActivity.this, "再按一次退出优时", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        MainActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mMonthChooseDate = intent.getStringExtra("Date");
        Log.d("YZP", "-----------Main=" + mMonthChooseDate + "YZP");
    }

    public String getmMonthChooseDate() {
        return mMonthChooseDate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
