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
import android.widget.Toast;

import com.mkch.youshi.activity.BaseActivity;
import com.mkch.youshi.bean.UnLoginedUser;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.fragment.ContactsFragment;
import com.mkch.youshi.fragment.MessageFragment;
import com.mkch.youshi.fragment.TodayFragment;
import com.mkch.youshi.fragment.UserCenterFragment;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.XmppHelper;
import com.mkch.youshi.view.IndexTabBarLayout;
import com.mkch.youshi.view.NoScrollViewPager;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;

import java.io.IOException;
import java.util.Collection;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements RosterListener {

    private static final int MSG_LOGIN_SUCCESS = 10;
    private IndexTabBarLayout mIndexTabBarLayout;//底部整个控件

    private NoScrollViewPager mViewPager;

    private int CACHE_PAGES = 3;
    //四个fragment
    private Fragment mTodayFragment;
    private Fragment mMessageFragment;
    private Fragment mContactsFragment;
    private Fragment mUserCenterFragment;
    private String mMonthChooseDate;

    /**
     * XMPP
     */
    private XMPPTCPConnection connection;
    private Roster mRoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    private void initData() {
        mTodayFragment = new TodayFragment();
        mMessageFragment = new MessageFragment();
        mContactsFragment = new ContactsFragment();
        mUserCenterFragment = new UserCenterFragment();
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
        mIndexTabBarLayout=(IndexTabBarLayout)findViewById(R.id.myIndexTabBarLayout);
        mViewPager = (NoScrollViewPager)findViewById(R.id.myViewPager);
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

        mainxmpp();//进入首页后，xmpp的所有操作

    }

    /**
     * 进入首页后，xmpp的所有操作
     */
    private void mainxmpp() {
        //获取连接
        connection = XmppHelper.getConnection();
        //用户自动登录
        User _user = CommonUtil.getUserInfo(MainActivity.this);
        final String _login_user = _user.getOpenFireUserName();
        final String _login_pwd = _user.getPassword();
        Log.d("jlj","MainActivity----------------------mainxmpp="+_login_user+","+_login_pwd);
        //开启副线程-登录-设置状态
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!connection.isConnected()){
                        connection.connect();
                    }
                    connection.login(_login_user, _login_pwd);
                    Presence presence = new Presence(Presence.Type.available);
                    presence.setStatus("我是在线状态");
                    connection.sendStanza(presence);


                }  catch (Exception e){
                    e.printStackTrace();
                    CommonUtil.sendErrorMessage("网络连接错误",mHandler);
                    connection.disconnect();
                    return;
                }
                mHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);

            }
        }).start();


    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;

                case MSG_LOGIN_SUCCESS:
                    addAllXmppListener();//添加所有的监听器
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 添加所有的监听器
     */
    private void addAllXmppListener() {
        Log.d("jlj","MainActivity----------------------addAllXmppListener");
        //接收好友请求监听
        mRoster = Roster.getInstanceFor(connection);
        //监听好友请求添加========================================
        connection.addAsyncStanzaListener(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                if (packet instanceof Presence){
                    Presence _p = (Presence)packet;
                    //获取请求者
                    String _request_jid = _p.getFrom();
                    if (_p.getType() == Presence.Type.subscribe){
                        //如果我没有此JID的好友，才弹出对话框选择是否需要接受好友请求
                        RosterEntry _entry = mRoster.getEntry(_request_jid);
                        if (_entry==null){
//                            Message _message = mHandler.obtainMessage(MSG_OPEN_REV_FRIEND_DIALOG, _request_jid);
//                            mHandler.sendMessage(_message);
                            //发送广播通知，有好友请求添加
                            Log.d("jlj","MainActivity-------------好友请求，"+_request_jid);



                            Intent _intent = new Intent();
                            _intent.setAction("yoshi.action.friendsbroadcast");
                            _intent.putExtra("_request_jid",_request_jid);
                            sendBroadcast(_intent);
                        }


                    }
                }
            }
        }, new StanzaFilter() {
            @Override
            public boolean accept(Stanza stanza) {
                //没有选择，全部接收
                return true;
            }
        });
        //监听是否需要更新好友列表========================================
        mRoster.addRosterListener(this);
    }

    @Override
    public void entriesAdded(Collection<String> addresses) {
        Log.d("JLJ","------------------entriesAdded");
    }

    @Override
    public void entriesUpdated(Collection<String> addresses) {
        Log.d("JLJ","------------------entriesUpdated");
    }

    @Override
    public void entriesDeleted(Collection<String> addresses) {
        Log.d("JLJ","------------------entriesDeleted");
    }

    @Override
    public void presenceChanged(Presence presence) {
        Log.d("JLJ","------------------presenceChanged");
    }


    /**
     * 自定义ViewPager的适配器
     * @author JLJ
     *
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


    private long exitTime=0;
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
        Log.d("YZP", "-----------Main="+ mMonthChooseDate + "YZP");
    }

    public String getmMonthChooseDate() {
        return mMonthChooseDate;
    }
}
