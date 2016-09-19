package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.exception.ServiceException;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.NetWorkUtil;
import com.mkch.youshi.view.CustomSmartImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInformationActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle, mTvName, mTvYoushiNumber, mTvSex;
    private LinearLayout mLayoutHead, mLayoutName, mLayoutYoushiNumber, mLayoutSex, mLayoutSignature;
    private User mUser;
    private static ProgressDialog mProgressDialog = null;
    //头像选择相关变量
    private File mFile;
    private Uri imageUri;
    private CustomSmartImageView mIvHead;
    private String mpicName = "touxiang.jpg";
    private String mPicPath = Environment.getExternalStorageDirectory().getPath() + "/";
    private File tempFile = new File(Environment.getExternalStorageDirectory(),
            getPhotoFileName());
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    //保存选择的性别
    private String[] sex_list = {"男", "女"};
    private int checkedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        //判断文件夹是否存在,不存在则创建
        File file = new File(mPicPath);
        if (!file.exists()) {
            file.mkdir();
        }
        initView();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUser = CommonUtil.getUserInfo(UserInformationActivity.this);//初始化用户信息
        initData();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mLayoutHead = (LinearLayout) findViewById(R.id.layout_user_information_head);
        mIvHead = (CustomSmartImageView) findViewById(R.id.iv_user_information_head);
        mLayoutName = (LinearLayout) findViewById(R.id.layout_user_information_name);
        mTvName = (TextView) findViewById(R.id.tv_user_information_name);
        mLayoutYoushiNumber = (LinearLayout) findViewById(R.id.layout_user_information_youshi_number);
        mTvYoushiNumber = (TextView) findViewById(R.id.tv_user_information_youshi_number);
        mLayoutSex = (LinearLayout) findViewById(R.id.layout_user_information_sex);
        mTvSex = (TextView) findViewById(R.id.tv_user_information_sex);
        mLayoutSignature = (LinearLayout) findViewById(R.id.layout_user_information_signature);
    }

    private void initData() {
        mTvTitle.setText("个人信息");
        mUser = CommonUtil.getUserInfo(this);
        //设置头像,本地没有就用默认头像
        if (mUser != null) {
            Log.d("jlj", "---------------------result = " + mUser.getHeadPic());
            if (mUser.getSexCache().equals("男")) {
                checkedItem = 0;
            } else {
                checkedItem = 1;
            }
            mIvHead.setImageUrl(mUser.getHeadPic(), R.drawable.maillist);
            mTvName.setText(mUser.getNickName());
            mTvSex.setText(mUser.getSex());
            if (mUser.getYoushiNumber() != null) {
                mTvYoushiNumber.setText(mUser.getYoushiNumber());
                mLayoutYoushiNumber.setEnabled(false);
            }
        } else {
            mIvHead.setImageResource(R.drawable.maillist);
        }
//        if(mPscenterUser!=null){
//            mLineUserInfo.setVisibility(View.GONE);
//            mLineUserInfoLogined.setVisibility(View.VISIBLE);
//            mIvHead.setImageUrl(mPscenterUser.getUserImg(), R.drawable.creative_no_img);
//            mTvUserName.setText(mPscenterUser.getUsername());
//            //开启副线程-获取用户详情
//            getUserDetailFromNet(mPscenterUser.getId());
//        }else{
//            mLineUserInfo.setVisibility(View.VISIBLE);
//            mLineUserInfoLogined.setVisibility(View.GONE);
//            //清0数据
//            mTvCreativeNum.setText("0");
//            mTvDesignNum.setText("0");
//            mTvFansNum.setText("0");
//
//            if(mPullToRefreshScrollView!=null&&mPullToRefreshScrollView.isRefreshing()){
//                // Call onRefreshComplete when the list has been refreshed.
//                mPullToRefreshScrollView.onRefreshComplete();
//            }

//        }
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformationActivity.this.finish();
            }
        });
        mLayoutName.setOnClickListener(new UserInformationOnClickListener());
        mLayoutYoushiNumber.setOnClickListener(new UserInformationOnClickListener());
        //弹出对话框，用户选择性别后更新界面
        mLayoutSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserInformationActivity.this).setTitle("您的性别为").setSingleChoiceItems(
                        sex_list, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkedItem = which;
                                mTvSex.setText(sex_list[which]);
                                mUser.setSex(sex_list[which]);
                                mUser.setSexCache(sex_list[which]);
                                CommonUtil.saveUserInfo(mUser, UserInformationActivity.this);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        mLayoutSignature.setOnClickListener(new UserInformationOnClickListener());
        //头像点击事件
        mLayoutHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class UserInformationOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.layout_user_information_name:
                    _intent = new Intent(UserInformationActivity.this, ReviseNameActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_user_information_youshi_number:
                    _intent = new Intent(UserInformationActivity.this, ReviseYoushiNumberActivity.class);
                    startActivity(_intent);
                    break;
                case R.id.layout_user_information_signature:
                    _intent = new Intent(UserInformationActivity.this, ReviseSignatureActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    Dialog alertDialog;

    private void showOptionDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View _OptionView = layoutInflater.inflate(R.layout.layout_photo_option, null);
        TextView _camera = (TextView) _OptionView.findViewById(R.id.tv_camera);
        TextView _cancle = (TextView) _OptionView.findViewById(R.id.tv_cancel);
        TextView _photo = (TextView) _OptionView.findViewById(R.id.tv_photo);
        //拍照选取图片的点击事件
        _camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFile = new File(mPicPath, mpicName);
                imageUri = Uri.fromFile(mFile);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                alertDialog.dismiss();
            }
        });

        //选取图库图片的点击事件
        _photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFile = new File(mPicPath, mpicName);
                imageUri = Uri.fromFile(mFile);

                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                alertDialog.dismiss();
            }
        });
        //取消事件
        _cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = new AlertDialog.Builder(this).
                setView(_OptionView).
                create();
        alertDialog.show();
    }

    /**
     * 处理图片的剪辑
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("zj", "--------------------------" + resultCode);
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                startPhotoZoom(imageUri);
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null)
                    startPhotoZoom(data.getData());
                break;
            case PHOTO_REQUEST_CUT:// 返回的结果
                try {
                    if (resultCode == 0) {
                    } else {
                        sentPicToNext();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//將剪切的文件输入到imageUri中
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片传递到下一个界面上
    private void sentPicToNext() throws FileNotFoundException {
        Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
        saveBitmap(photo);  //保存BitMap到本地
        //上传图片到服务器
        sendPicToServer();
        if (photo == null) {
            mIvHead.setImageResource(R.drawable.maillist);
        } else {
            mIvHead.setImageBitmap(photo);
        }

        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photodata = baos.toByteArray();
            System.out.println(photodata.toString());
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存bitmap为File文件
     */
    public void saveBitmap(Bitmap bm) {
        File f = new File(mPicPath, mpicName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(UserInformationActivity activity) {
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
                    ((UserInformationActivity) mActivity.get()).showTip(errorMsg);
                    break;
                case CommonConstants.FLAG_UPLOAD_HEADPIC_SUCCESS:
//                    //上传头像成功
//                    ((UserInformationActivity) mActivity.get()).sendPicSuccess();
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
     * 上传图片到服务器
     */
    private void sendPicToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(mPicPath + mpicName);
                    String code = CommonUtil.getUserInfo(UserInformationActivity.this).getLoginCode();
                    String _withPhoto = NetWorkUtil.getResultFromUrlConnectionWithPhoto(CommonConstants.UploadHeadPicAndroid, null, mpicName, code, file);
                    //解析出上传图片的地址
                    JSONObject _result = new JSONObject(_withPhoto);
                    String _datas = _result.getString("Datas");
                    mUser.setHeadPic("http://192.168.3.8:1001" + _datas.toString());
                    CommonUtil.saveUserInfo(mUser, UserInformationActivity.this);
                    myHandler.sendEmptyMessage(CommonConstants.FLAG_UPLOAD_HEADPIC_SUCCESS);
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtil.sendErrorMessage("上传图片失败，数据异常", myHandler);
                }
            }
        }).start();
    }
}
