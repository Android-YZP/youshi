package com.mkch.youshi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.fragment.FileFragment;
import com.mkch.youshi.view.FileTabBarLayout;

public class MyCollectionActivity extends BaseActivity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private FileTabBarLayout mFileTabBarLayout;
    private ViewPager mViewPagerFile;
    private final static int FLAG_ITEM_0 = 0;
    private final static int FLAG_ITEM_1 = 1;
    private final static int FLAG_ITEM_2 = 2;
    private final static int FLAG_ITEM_3 = 3;
    private final static int FLAG_ITEM_4 = 4;
    //下划线
    private View tabUnderLine;
    //当前页面
    private int currentIndex;
    //屏幕宽度
    private int screenWidth;
    //页面总个数
    private int fragSize = 5;
    //设置预加载界面数量
    private int CACHE_PAGES = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mFileTabBarLayout = (FileTabBarLayout) findViewById(R.id.custom_file_tabbar);
        mViewPagerFile = (ViewPager) findViewById(R.id.viewPager_file);
        mViewPagerFile.setOffscreenPageLimit(CACHE_PAGES);//设置预加载界面数量
        //初始化屏幕宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        //初始化tab选中后的下划线
        initTabUnderLine();
    }

    private void initData() {
        mTvTitle.setText("我的收藏");
        mViewPagerFile.setAdapter(new FileFragmentPagerAdapter(this.getSupportFragmentManager()));
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCollectionActivity.this.finish();
            }
        });
        mFileTabBarLayout.setOnItemClickListener(new FileTabBarLayout.IFileTabBarCallBackListener() {
            @Override
            public void clickItem(int id) {
                switch (id) {
                    case R.id.tv_file_item0:
                        mViewPagerFile.setCurrentItem(FLAG_ITEM_0);//点击后设置当前页是显示页
                        break;
                    case R.id.tv_file_item1:
                        mViewPagerFile.setCurrentItem(FLAG_ITEM_1);
                        break;
                    case R.id.tv_file_item2:
                        mViewPagerFile.setCurrentItem(FLAG_ITEM_2);
                        break;
                    case R.id.tv_file_item3:
                        mViewPagerFile.setCurrentItem(FLAG_ITEM_3);
                        break;
                    case R.id.tv_file_item4:
                        mViewPagerFile.setCurrentItem(FLAG_ITEM_4);
                        break;
                }
            }
        });

        mViewPagerFile.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mFileTabBarLayout.changeTabBarItems(position);
                currentIndex = position;//当前页
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //从左到右
                if (currentIndex == position) {
                    LinearLayout.LayoutParams layoutParam = (android.widget.LinearLayout.LayoutParams) tabUnderLine
                            .getLayoutParams();
                    layoutParam.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / fragSize) + currentIndex * (screenWidth / fragSize));
                    tabUnderLine.setLayoutParams(layoutParam);
                }
                //从右到左
                else if (currentIndex > position) {
                    LinearLayout.LayoutParams layoutParam = (android.widget.LinearLayout.LayoutParams) tabUnderLine
                            .getLayoutParams();
                    layoutParam.leftMargin = (int) (-(1 - positionOffset) * (screenWidth * 1.0 / fragSize) + currentIndex * (screenWidth / fragSize));
                    tabUnderLine.setLayoutParams(layoutParam);
                }
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
    }

    /**
     * 自定义ViewPager的适配器
     *
     * @author JLJ
     */
    private class FileFragmentPagerAdapter extends FragmentPagerAdapter {
        public FileFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int postion) {
            switch (postion) {
                case FLAG_ITEM_0:
                    return new FileFragment();
                case FLAG_ITEM_1:
                    return new FileFragment();
                case FLAG_ITEM_2:
                    return new FileFragment();
                case FLAG_ITEM_3:
                    return new FileFragment();
                case FLAG_ITEM_4:
                    return new FileFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    //初始化tab下划线
    private void initTabUnderLine() {
        tabUnderLine = (View) findViewById(R.id.tab_file_under_line);
        LinearLayout.LayoutParams layoutParam = (android.widget.LinearLayout.LayoutParams) tabUnderLine.getLayoutParams();
        layoutParam.width = screenWidth / fragSize;
        tabUnderLine.setLayoutParams(layoutParam);
    }
}
