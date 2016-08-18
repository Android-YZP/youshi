package com.mkch.youshi.activity;

import android.app.Activity;
import android.os.Bundle;

import com.mkch.youshi.R;
import com.mkch.youshi.fragment.MessageFragment;

/**
 * Created by SunnyJiang on 2016/8/18.
 */
public class TestMsgActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_msg);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.frag_test_msg, new MessageFragment()).commit();
        }
    }
}
