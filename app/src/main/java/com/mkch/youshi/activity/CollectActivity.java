package com.mkch.youshi.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.adapter.YoupanFileAdapter;
import com.mkch.youshi.bean.DeleteFileBean;
import com.mkch.youshi.bean.UpLoadFileResultBean;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.fragment.ChooseDocumentFileFragment;
import com.mkch.youshi.fragment.CollectFileFragment;
import com.mkch.youshi.model.YoupanFile;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.MyCallBack;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.util.XUtil;
import com.mkch.youshi.view.FileTabBarLayout;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends BaseActivity {

    private static final int FILE_SELECT_CODE = 5;
    private ImageView mIvBack;
    private FileTabBarLayout mDropBoxTabBarLayout;
    private ViewPager mViewPagerDropBox;
    private final static int FLAG_ITEM_0 = 0;
    private final static int FLAG_ITEM_1 = 1;
    private final static int FLAG_ITEM_2 = 2;
    private final static int FLAG_ITEM_3 = 3;
    private final static int FLAG_ITEM_4 = 4;
    private static final int PHOTO_REQUEST_CUT = 6;// 结果
    //下划线
    private View tabUnderLine;
    //当前页面
    private int currentIndex;
    //屏幕宽度
    private int screenWidth;
    //页面总个数
    private int fragSize = 5;
    private int mChooseNumber = 0;
    //设置预加载界面数量
    private int CACHE_PAGES = 4;
    private TextView mTvUpload;
    private Uri imageUri;
    private String mpicName = "touxiang.jpg";
    private String mPicPath = Environment.getExternalStorageDirectory().getPath() + "/";
    private ProgressDialog mProgressDialog;

    private TextView mTvChoofileNum;

    private Button mTvChooDelete;
    private Button mTvChooTransmit;
    private FileFragmentPagerAdapter mFileFragmetAdapter;
    private RelativeLayout mRlChooseBar;
    private TextView mTvFileTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox_file);
        initView();
        initData();
        setListener();

    }

    private void initView() {
        mTvUpload = (TextView) findViewById(R.id.tv_drop_box_upload);
        mIvBack = (ImageView) findViewById(R.id.iv_drop_box_file_back);
        mDropBoxTabBarLayout = (FileTabBarLayout) findViewById(R.id.custom_dropbox_file_tabbar);
        mViewPagerDropBox = (ViewPager) findViewById(R.id.viewPager_dropbox_file);
        mViewPagerDropBox.setOffscreenPageLimit(CACHE_PAGES);//设置预加载界面数量
        mTvChoofileNum = (TextView) findViewById(R.id.tv_choose_file_num);
        mTvFileTitle = (TextView) findViewById(R.id.tv_file_title);
        mTvChooDelete = (Button) findViewById(R.id.bt_choose_delete);
        mTvChooTransmit = (Button) findViewById(R.id.bt_choose_transmit);
        mRlChooseBar = (RelativeLayout) findViewById(R.id.rl_choose_bar);

        //初始化屏幕宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        //初始化tab选中后的下划线
        initTabUnderLine();
    }

    private void initData() {
        mTvUpload.setVisibility(View.INVISIBLE);
        mRlChooseBar.setVisibility(View.GONE);
        mTvFileTitle.setText("我的收藏");
        mFileFragmetAdapter = new FileFragmentPagerAdapter(this.getSupportFragmentManager());
        mViewPagerDropBox.setAdapter(mFileFragmetAdapter);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectActivity.this.finish();
            }
        });

        mDropBoxTabBarLayout.setOnItemClickListener(new FileTabBarLayout.IFileTabBarCallBackListener() {
            @Override
            public void clickItem(int id) {
                switch (id) {
                    case R.id.tv_file_item0:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_0);//点击后设置当前页是显示页
                        break;
                    case R.id.tv_file_item1:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_1);
                        break;
                    case R.id.tv_file_item2:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_2);
                        break;
                    case R.id.tv_file_item3:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_3);
                        break;
                    case R.id.tv_file_item4:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_4);
                        break;
                }
            }
        });

        mViewPagerDropBox.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mDropBoxTabBarLayout.changeTabBarItems(position);
                currentIndex = position;//当前页
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //从左到右
                if (currentIndex == position) {
                    LinearLayout.LayoutParams layoutParam = (LinearLayout.LayoutParams) tabUnderLine
                            .getLayoutParams();
                    layoutParam.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / fragSize) + currentIndex * (screenWidth / fragSize));
                    tabUnderLine.setLayoutParams(layoutParam);
                }
                //从右到左
                else if (currentIndex > position) {
                    LinearLayout.LayoutParams layoutParam = (LinearLayout.LayoutParams) tabUnderLine
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
                    return new CollectFileFragment(postion + 1);
                case FLAG_ITEM_1:
                    return new CollectFileFragment(postion + 1);
                case FLAG_ITEM_2:
                    return new CollectFileFragment(postion + 1);
                case FLAG_ITEM_3:
                    return new CollectFileFragment(postion + 1);
                case FLAG_ITEM_4:
                    return new CollectFileFragment(postion + 1);
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
        LinearLayout.LayoutParams layoutParam = (LinearLayout.LayoutParams) tabUnderLine.getLayoutParams();
        layoutParam.width = screenWidth / fragSize;
        tabUnderLine.setLayoutParams(layoutParam);
    }
}
