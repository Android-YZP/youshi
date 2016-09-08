package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.GroupChatListAdapter;
import com.mkch.youshi.adapter.ScheduleDetailAdapter;

public class ScheduleDetailActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private ListView mListView;
    //长按后选择的操作列表
    private String[] operation_list = {"下载", "转发", "删除"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mListView = (ListView) findViewById(R.id.list_schedule_detail);
    }

    private void initData() {
        mTvTitle.setText("周末一起烧烤");
        ListAdapter mAdapter = new ScheduleDetailAdapter(ScheduleDetailActivity.this);
        mListView.setAdapter(mAdapter);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleDetailActivity.this.finish();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent _intent = null;
                switch (position) {
                    case 0:
                        _intent = new Intent(ScheduleDetailActivity.this, FilePreviewPicActivity.class);
                        startActivity(_intent);
                        break;
                    case 3:
                        _intent = new Intent(ScheduleDetailActivity.this, FilePreviewActivity.class);
                        startActivity(_intent);
                        break;
                }
            }
        });

        //长按文件或收藏，弹出对话框选择操作
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(ScheduleDetailActivity.this).setItems(operation_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return true;
            }
        });
    }
}
