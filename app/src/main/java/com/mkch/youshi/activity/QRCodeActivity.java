package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.XmppHelper;
import com.mkch.youshi.view.Encoder;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.xutils.image.ImageOptions;
import org.xutils.x;

public class QRCodeActivity extends Activity {

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageView mIvBack, mIvCode;
    private ImageView mIvHead;
    private TextView mTvScan, mTvName, mTvAddress;
    private User mUser;
    private Encoder mEncoder;
    private DecodeTask mDecodeTask;
    private XMPPTCPConnection connection; //connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_qr_code_back);
        mTvScan = (TextView) findViewById(R.id.tv_qr_code_scan);
        mIvHead = (ImageView) findViewById(R.id.iv_qr_code_head);
        mTvName = (TextView) findViewById(R.id.tv_qr_code_name);
        mTvAddress = (TextView) findViewById(R.id.tv_qr_code_address);
        mIvCode = (ImageView) findViewById(R.id.iv_qr_code);
    }

    private void initData() {
        //获取连接
        connection = XmppHelper.getConnection();
        mUser = CommonUtil.getUserInfo(this);
        //圆形
        ImageOptions _image_options = new ImageOptions.Builder()
                .setCircular(true)
                .build();
        if (mUser != null) {
            if (mUser.getHeadPic() != null && !mUser.getHeadPic().equals("") && !mUser.getHeadPic().equals("null")) {
                x.image().bind(mIvHead, mUser.getHeadPic(), _image_options);
            } else {
                mIvHead.setImageResource(R.drawable.default_headpic);
            }
            if (mUser.getNickName() == null || mUser.getNickName().equals("")) {
                mTvName.setText(mUser.getMobileNumber());
            } else {
                mTvName.setText(mUser.getNickName());
            }
            if (mUser.getAddress() == null || mUser.getAddress().equals("")) {
                mTvAddress.setText("");
            } else {
                mTvAddress.setText(mUser.getAddress());
            }
        }
        final WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final Display display = manager.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        final int width = displaySize.x;
        final int height = displaySize.y;
        final int dimension = width < height ? width : height;

        mEncoder = new Encoder.Builder()
                .setBackgroundColor(0xFFFFFF)
                .setCodeColor(0xFF000000)
                .setOutputBitmapPadding(0)
                .setOutputBitmapWidth(dimension)
                .setOutputBitmapHeight(dimension)
                .build();

        mDecodeTask = new DecodeTask();
        mDecodeTask.execute(mUser.getOpenFireUserName());
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeActivity.this.finish();
            }
        });
        mTvScan.setOnClickListener(new QRCodeOnClickListener());
    }

    /**
     * 自定义点击监听类
     */
    private class QRCodeOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_qr_code_scan:
                    Intent _intent = new Intent();
                    _intent.setClass(QRCodeActivity.this, MipcaActivityCapture.class);
                    _intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(_intent, SCANNIN_GREQUEST_CODE);
                    break;
                default:
                    break;
            }
        }
    }

    private class DecodeTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return mEncoder.encode(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mIvCode.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    Log.d("jlj","zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz---------------------onclick="+bundle.getString("result"));
//                    //发送请求添加好友
//                    final String _jid = XmppStringUtils.completeJidFrom(bundle.getString("result"), connection.getServiceName());//转jid
//                    RosterHelper _roster_helper = RosterHelper.getInstance(connection);
//                    String _nickname = XmppStringUtils.parseLocalpart(_jid);
//                    _roster_helper.addEntry(_jid,_nickname,"Friends");
                }
                break;
        }
    }
}
