package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;
import com.mkch.youshi.adapter.CommonEquipmentListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccountProtectActivity extends Activity {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private ListView mListView;
    //长按后选择的操作列表
    private String[] operation_list = {"删除"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_protect);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mListView = (ListView) findViewById(R.id.list_account_protect);
    }

    private void initData() {
        mTvTitle.setText("账号保护");
        ListAdapter mAdapter = new CommonEquipmentListAdapter(AccountProtectActivity.this);
        mListView.setAdapter(mAdapter);


//        //保持TextView在GridView的最后一个
//        GridView gridView = new GridView(this);
//        TextView textView = new TextView(this);
//        ArrayList<String> list = new ArrayList();
//        gridView.addView(textView,list.size());
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountProtectActivity.this.finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(AccountProtectActivity.this).setTitle("请输入").setView(
                        new EditText(AccountProtectActivity.this)).setPositiveButton("确定", null)
                        .setNegativeButton("取消", null).show();
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(AccountProtectActivity.this).setItems(operation_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return true;
            }
        });
    }
}
