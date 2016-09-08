package com.mkch.youshi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.ManyPeopleEvenBean;

import java.util.ArrayList;
import java.util.List;

public class ManyPeopleEventDetial extends AppCompatActivity {

    private ManyPeopleEvenBean mManyPeopleDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many_people_event_detial);
        initView();
        initData();
    }

    private void initData() {
        //解析传过来的数据
        Gson gson = new Gson();
        Intent intent = getIntent();
        String s = intent.getStringExtra("mgonsn");
        Log.e("传过来的数据2", s);
        mManyPeopleDatas = gson.fromJson(s,
                new TypeToken<ManyPeopleEvenBean>() {
                }.getType());


    }

    private void initView() {

    }
}
