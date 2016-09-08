package com.mkch.youshi.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.mkch.youshi.R;
import com.mkch.youshi.fragment.ManyPeopleCaledarFragment;
import com.mkch.youshi.fragment.PersonalCaledarFragment;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.PagerTab;

public class CalendarActivity extends BaseActivity {
    private ViewPager mViewPager;
    private PagerTab mPagerTab;
    private ImageView mIvCancel;
    private ImageView mIvSearch;
    private ImageView mIvAdd;
    private PopupWindow popupWindow;
    private Fragment personalCaledarFragment;
    private Fragment manyPeopleCaledarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, SearchEventActivity.class));
            }
        });

        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(mIvAdd);
            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_context_pager);
        mPagerTab = (PagerTab) findViewById(R.id.pager_tab);

        mIvCancel = (ImageView) findViewById(R.id.iv_cancel);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
    }

    private void initData() {
        personalCaledarFragment = new PersonalCaledarFragment();
        manyPeopleCaledarFragment = new ManyPeopleCaledarFragment();
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mPagerTab.setViewPager(mViewPager);
        //保证第一个条目是被选中状态
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
    }

    /**
     * 显示事件的添加框
     *
     * @param view
     */
    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(CalendarActivity.this).inflate(
                R.layout.layout_pop_window, null);
        LinearLayout _LlBackground = (LinearLayout) contentView.findViewById(R.id.ll_background);
        TextView _TvPersonalEvent = (TextView) contentView.findViewById(R.id.tv_personal_event);
        TextView _TvManyPeopleEvent = (TextView) contentView.findViewById(R.id.tv_many_people_event);

        _LlBackground.getBackground().setAlpha(180);
        //个人事件按钮
        _TvPersonalEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CalendarActivity.this != null)
                    startActivity(new Intent(CalendarActivity.this, AddPersonalActivity.class));
                popupWindow.dismiss();
            }
        });

        // 多人事件按钮
        _TvManyPeopleEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CalendarActivity.this != null)
                    startActivity(new Intent(CalendarActivity.this, AddManyPeopleEventActivity.class));
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView,
                400, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        ColorDrawable dw = new ColorDrawable(0000);
        popupWindow.setBackgroundDrawable(dw);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

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
                return personalCaledarFragment;
            } else if (position == 1) {
                return manyPeopleCaledarFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }
    }

}
