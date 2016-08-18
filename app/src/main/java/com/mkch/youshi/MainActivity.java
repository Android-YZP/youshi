package com.mkch.youshi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mkch.youshi.activity.BaseActivity;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.btn_main_1)
    private Button mBtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event(value = R.id.btn_main_1,type = View.OnClickListener.class)
    private void clickBtn1(View view){
        LogUtil.d("button1 click-------------------");
    }
}
