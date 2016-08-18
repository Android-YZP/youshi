package com.mkch.youshi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xutils.x;

/**
 * Created by SunnyJiang on 2016/8/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
