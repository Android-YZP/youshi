package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.RosterHelper;
import com.mkch.youshi.util.XmppHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.util.XmppStringUtils;
import org.xutils.image.ImageOptions;
import org.xutils.x;

public class QRCodeActivity extends Activity {

    private final static int REQUEST_CODE = 1;
    private ImageView mIvBack, mIvCode;
    private ImageView mIvHead;
    private TextView mTvScan, mTvName, mTvAddress;
    private User mUser;
    private XMPPTCPConnection connection; //connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initView();
        initData();
        setListener();
        ZXingLibrary.initDisplayOpinion(this);
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
                mTvAddress.setText("地区：无");
            } else {
                mTvAddress.setText("地区：" + mUser.getAddress());
            }
        }
        //生成二维码
        Bitmap mBitmap = CodeUtils.createImage(mUser.getOpenFireUserName(), 400, 400, null);
        mIvCode.setImageBitmap(mBitmap);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeActivity.this.finish();
            }
        });
        mTvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    //获取用户名和_OpenFireUsrName；并发起添加功能
                    //获取连接
                    connection = XmppHelper.getConnection();
                    final String _jid = XmppStringUtils.completeJidFrom(result, connection.getServiceName());//转jid
                    //发送请求添加好友
                    RosterHelper _roster_helper = RosterHelper.getInstance(connection);
                    String _nickname = XmppStringUtils.parseLocalpart(_jid);
                    _roster_helper.addEntry(_jid, _nickname, "Friends");
                    //立马删除好友
                    _roster_helper.removeEntry(_jid);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(QRCodeActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
