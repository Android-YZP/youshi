package com.mkch.youshi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.mkch.youshi.R;
import com.mkch.youshi.fragment.ManyPeopleCaledarFragment;
import com.mkch.youshi.fragment.PersonalCaledarFragment;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.PagerTab;

public class CalendarActivity extends BaseActivity {
    private ViewPager mViewPager;
    private PagerTab mPagerTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_context_pager);
        mPagerTab = (PagerTab) findViewById(R.id.pager_tab);
    }

    private void initData() {
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mPagerTab.setViewPager(mViewPager);
        //保证第一个条目是被选中状态
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
    }


    class MyAdapter extends FragmentPagerAdapter {
        //创建这个类的时候初始化页签标题
        private String[] mTabNames;
        public MyAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        //返回页签标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }


        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new PersonalCaledarFragment();
            }else if(position == 1){
                return new ManyPeopleCaledarFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }
    }

}
