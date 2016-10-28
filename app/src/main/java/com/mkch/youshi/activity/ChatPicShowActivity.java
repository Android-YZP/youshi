package com.mkch.youshi.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.mkch.youshi.R;

public class ChatPicShowActivity extends Activity {

    private ImageView mIvImage;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_pic_show);
        initView();
        initData();
    }

    private void initView() {
        mIvImage = (ImageView) findViewById(R.id.iv_chat_pic_show);
    }

    private void initData() {
        Bundle _bundle = getIntent().getExtras();
        if (_bundle != null) {
            imageName = _bundle.getString("image");
            Bitmap bm = BitmapFactory.decodeFile(imageName);
            mIvImage.setImageBitmap(bm);
        }
    }
}
