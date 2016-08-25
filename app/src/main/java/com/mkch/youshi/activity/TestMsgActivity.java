package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mkch.youshi.R;
import com.mkch.youshi.fragment.MessageFragment;
import com.mkch.youshi.fragment.TodayFragment;

import org.xutils.common.util.LogUtil;

/**
 * Created by SunnyJiang on 2016/8/18.
 */
public class TestMsgActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_msg);
        //全部采用重新加载的形式来做.
//        Intent _intent = getIntent();
//        String _chooseDate = _intent.getStringExtra("choose");
//        LogUtil.d(_chooseDate+"11111111111111111111111111111111111111111111");
//        Log.d("--------------YZP",_chooseDate+"YZP");
        if (savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.frag_test_msg, new TodayFragment()).commit();


        }

    }
}
