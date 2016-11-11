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
import android.widget.TextView;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.adapter.YoupanFileAdapter;
import com.mkch.youshi.bean.DeleteFileBean;
import com.mkch.youshi.bean.UpLoadFileResultBean;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.fragment.ChooseDocumentFileFragment;
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

public class DropBoxFileActivity extends BaseActivity {

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
    private static int mCurrentItem;//记录viewPager被滑动到那一页了
    public static boolean isCancle = false;

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
        mTvChooDelete = (Button) findViewById(R.id.bt_choose_delete);
        mTvChooTransmit = (Button) findViewById(R.id.bt_choose_transmit);

        //初始化屏幕宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        //初始化tab选中后的下划线
        initTabUnderLine();
    }

    private void initData() {
        mFileFragmetAdapter = new FileFragmentPagerAdapter(this.getSupportFragmentManager());
        mViewPagerDropBox.setAdapter(mFileFragmetAdapter);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DropBoxFileActivity.this.finish();
            }
        });

        //转发
        mTvChooTransmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YoupanFileAdapter.mChooseFile.size() > 0) {
                    //选择联系人
                    Intent intent = new Intent(DropBoxFileActivity.this, ChooseGroupMemberActivity.class);
                    intent.putExtra("isSendFile", true);
                    startActivity(intent);
                } else {
                    UIUtils.showTip("请先选择文件");
                }
            }
        });

        //上传
        mTvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框，选择图片，还是本地文件？
                showOptionDialog();
            }
        });

        mTvChooDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YoupanFileAdapter.mChooseFile.size() > 0) {
                    showDeleteDialog();
                } else {
                    UIUtils.showTip("请先选择文件");
                }
            }
        });

        mDropBoxTabBarLayout.setOnItemClickListener(new FileTabBarLayout.IFileTabBarCallBackListener() {
            @Override
            public void clickItem(int id) {
                switch (id) {
                    case R.id.tv_file_item0:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_0);//点击后设置当前页是显示页
                        mCurrentItem = 0;
                        break;
                    case R.id.tv_file_item1:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_1);
                        mCurrentItem = 1;
                        break;
                    case R.id.tv_file_item2:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_2);
                        mCurrentItem = 2;
                        break;
                    case R.id.tv_file_item3:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_3);
                        mCurrentItem = 3;
                        break;
                    case R.id.tv_file_item4:
                        mViewPagerDropBox.setCurrentItem(FLAG_ITEM_4);
                        mCurrentItem = 4;
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
                mCurrentItem = position;
                UIUtils.LogUtils(mCurrentItem + "");
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


    public TextView getmTvChoofileNum() {
        return mTvChoofileNum;
    }

    //上传文件的选择对话框
    private void showOptionDialog() {
        final String[] items = {"图  片", "本地文件"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(DropBoxFileActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    UploadChoosePic();
                } else if (which == 1) {
                    UploadChooseFile();
                }
            }
        });
        listDialog.show();
    }

    /**
     * 显示删除对话框
     */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除所选文件?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //从云端删除文件,从本地删除文件
                deleteFile2Net();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 从云端删除文件
     */
    private void deleteFile2Net() {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在删除....", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.DeleteCloudFile);
        //包装请求参数
        String _personEventJson = createDeleteFileJson();
        Log.d("YZP", "---------------------_personEventJson = " + _personEventJson);
        requestParams.addBodyParameter("", _personEventJson);//用户名
        String loginCode = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
        requestParams.addHeader("sVerifyCode", loginCode);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UIUtils.LogUtils(result);
                if (!result.isEmpty()) {
                    JSONObject _json_result = null;
                    try {
                        _json_result = new JSONObject(result);
                        String message = (String) _json_result.get("Message");
                        UIUtils.showTip(message);
                        Boolean success = (Boolean) _json_result.get("Success");
                        if (success) {// 网络端删除成功
                            DBHelper.deleteFile(YoupanFileAdapter.mChooseFile);
                            updateUI();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("jlj", "-------onError = " + ex.getMessage());
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    UIUtils.showTip(CommonConstants.MSG_CONNECT_ERROR);
                } else if (ex instanceof ConnectTimeoutException) {
                    UIUtils.showTip(CommonConstants.MSG_CONNECT_TIMEOUT);
                } else if (ex instanceof SocketTimeoutException) {
                    UIUtils.showTip(CommonConstants.MSG_SERVER_TIMEOUT);
                } else {
                    UIUtils.showTip(CommonConstants.MSG_DATA_EXCEPTION);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("userLogin", "----onCancelled");
            }

            @Override
            public void onFinished() {
                Log.d("userLogin", "----onFinished");
                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * 刷新界面
     */
    private void updateUI() {
        initData();
        mViewPagerDropBox.setCurrentItem(mCurrentItem, false);
    }

    private String createDeleteFileJson() {
        List<Integer> chooseFile = new ArrayList<>();
        for (int i = 0; i < YoupanFileAdapter.mChooseFile.size(); i++) {
            int fileID = Integer.parseInt(YoupanFileAdapter.mChooseFile.get(i).getFile_id());
            chooseFile.add(fileID);
        }
        DeleteFileBean deleteFileBean = new DeleteFileBean();
        deleteFileBean.setFileID(chooseFile);
        return new Gson().toJson(deleteFileBean);
    }

    //上传本地文件
    private void UploadChooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            UIUtils.showTip("请先安装文件夹管理软件");
        }
    }


    //上传图片
    private void UploadChoosePic() {
//        mFile = new File(mPicPath, mpicName);
//        imageUri = Uri.fromFile(mFile);

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CommonConstants.PHOTO_REQUEST_GALLERY);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CommonConstants.PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null) {
                    mProgressDialog = ProgressDialog.show(this, "请稍等", "正在上传中...", true, true);
                    Uri uri = data.getData();
                    String realFilePath = CommonUtil.getRealFilePath(this, uri);
                    File file = new File(realFilePath);//拿到了一个File文件对象
                    //存在本地数据库一份,上传网络一份,
                    save2Net(file, realFilePath);
                }
                break;
            case FILE_SELECT_CODE:
                if (data != null) {
                    mProgressDialog = ProgressDialog.show(this, "请稍等", "正在上传中...", true, true);
                    Uri uri = data.getData();
                    String realFilePath = CommonUtil.getRealFilePath(this, uri);
                    File file = new File(realFilePath);//拿到了一个File文件对象
                    save2Net(file, realFilePath);//上传网络
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //上传文件图片到网络
    private void save2Net(final File file, final String localpath) {
//        图片上传地址
        String url = CommonConstants.UploadFileAndroid;
        XUtil.UpLoadFile(url, file, new MyCallBack<String>() {

            public void onSuccess(String result) {
                super.onSuccess(result);
                //成功上传之后就在数据库建立记录,
                UIUtils.LogUtils(result);
                UpLoadFileResultBean analysis = analysisResult(result);
                if (analysis.isSuccess()) {//返回成功
                    UIUtils.showTip("上传成功");
                    save2DB(analysis, localpath);
                    updateUI();
                } else {
                    UIUtils.showTip(analysis.getMessage());
                }
            }

            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                UIUtils.showTip("上传失败");
            }

            @Override
            public void onFinished() {
                super.onFinished();
                mProgressDialog.dismiss();
            }
        });
    }


    /**
     * 储存文件信息到数据库
     * localpath 本地地址
     */
    private void save2DB(UpLoadFileResultBean analysis, String localpath) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = sdf.format(new java.util.Date());
            DbManager mDbManager = DBHelper.getDbManager();
            YoupanFile youpanFile = new YoupanFile();
            youpanFile.setCreate_time(date);
            youpanFile.setLocal_address(localpath);
            youpanFile.setServer_address(CommonConstants.FILE_ROOT_ADDRESS + analysis.getDatas().getUrl());
            youpanFile.setName(analysis.getDatas().getFileName());
            youpanFile.setSuf(analysis.getDatas().getFileSuf());
            youpanFile.setFile_id(analysis.getDatas().getFileID() + "");
            youpanFile.setType(analysis.getDatas().getType());//文件类型（1：文档，2：相册，3：视频，4：音频，5：其他）
            mDbManager.saveOrUpdate(youpanFile);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析返回值
     */
    private UpLoadFileResultBean analysisResult(String result) {
        Gson gson = new Gson();
        UpLoadFileResultBean upLoadFileResultBean = gson.fromJson(result, UpLoadFileResultBean.class);
        return upLoadFileResultBean;
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
                    return new ChooseDocumentFileFragment(postion + 1);
                case FLAG_ITEM_1:
                    return new ChooseDocumentFileFragment(postion + 1);
                case FLAG_ITEM_2:
                    return new ChooseDocumentFileFragment(postion + 1);
                case FLAG_ITEM_3:
                    return new ChooseDocumentFileFragment(postion + 1);
                case FLAG_ITEM_4:
                    return new ChooseDocumentFileFragment(postion + 1);
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
