package com.mkch.youshi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.mkch.youshi.activity.BaseActivity;
import com.mkch.youshi.fragment.ContactsFragment;
import com.mkch.youshi.fragment.MessageFragment;
import com.mkch.youshi.fragment.TodayFragment;
import com.mkch.youshi.fragment.UserCenterFragment;
import com.mkch.youshi.view.IndexTabBarLayout;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private IndexTabBarLayout mIndexTabBarLayout;//底部整个控件

    private ViewPager mViewPager;

    private int CACHE_PAGES = 3;
    //四个fragment
    private Fragment mTodayFragment;
    private Fragment mMessageFragment;
    private Fragment mContactsFragment;
    private Fragment mUserCenterFragment;

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
        mViewPager = (ViewPager)findViewById(R.id.myViewPager);
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

}
