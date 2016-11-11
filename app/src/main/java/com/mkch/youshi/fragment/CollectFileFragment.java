package com.mkch.youshi.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChooseGroupMemberActivity;
import com.mkch.youshi.activity.DropBoxFileActivity;
import com.mkch.youshi.adapter.CollectFileAdapter;
import com.mkch.youshi.adapter.YoupanFileAdapter;
import com.mkch.youshi.model.ChatBean;
import com.mkch.youshi.model.CollectFile;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.IntentUtil;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.util.UIUtils;
import com.tencent.TIMFileElem;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import java.io.File;
import java.util.ArrayList;

public class CollectFileFragment extends Fragment {

    private ArrayList<CollectFile> mCollectFiles;
    private CollectFileAdapter mAdapter;
    private ListView mListView;
    private int mType;

    public CollectFileFragment(int mType) {
        this.mType = mType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_document_file, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mListView = (ListView) view.findViewById(R.id.list_document_file);
        SwipeRefreshLayout mSRL = (SwipeRefreshLayout) view.findViewById(R.id.srlayout_file);
//        mSRL.setVisibility(View.GONE);//影藏刷新的控件
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        //文档类型
        mCollectFiles = DBHelper.findCollectFile(mType);
        //判断所有的文件在文件夹中是否还存在
        for (int i = 0; i < mCollectFiles.size(); i++) {
            if (!new File(mCollectFiles.get(i).getLocal_address()).exists()) {
                //在数据库中删除这条记录
                DBHelper.deleteCollectFile(mCollectFiles.get(i));
            }
        }

        mCollectFiles = DBHelper.findCollectFile(mType);//重新查找数据
        if (mCollectFiles != null) {
            mAdapter = new CollectFileAdapter(mCollectFiles);//拿到activity
            mListView.setAdapter(mAdapter);
        }
    }

    private void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (YoupanFileAdapter.mChooseNum != 0) {//当有选择的时候,屏蔽listView的选择事件
                    return;
                }
                mCollectFiles = DBHelper.findCollectFile(mType);//更新数据
                CollectFile collectFile = mCollectFiles.get(position);
                String local_address = collectFile.getLocal_address();
                //判断本地文件是否存在
                if (new File(local_address).exists()) {//存在此文件直接打开
                    UIUtils.showTip(local_address);
                    Intent intent = null;
                    intent = IntentUtil.getRightIntent(collectFile.getSuf(), local_address);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        UIUtils.showTip("暂不支持打开此类型文件");
                    }
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showOptionDialog( position);
                return true;
            }
        });
    }

    //文件的选择对话框
    private void showOptionDialog(final int position) {
        final String[] items = {"查   看", "转   发", "删   除"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(getActivity());
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    checkFile(position);
                } else if (which == 1) {
                    sendFile(position);
                } else if (which == 2) {
                    deleteFile(position);
                }
            }
        });
        listDialog.show();
    }

    /**
     * 删除文件
     */
    private void deleteFile(int position) {
        //数据库删除.
        //文件删除
        //界面更新
        CollectFile collectFile = mCollectFiles.get(position);
        DBHelper.deleteCollectFile(collectFile);
        initData();
    }

    /**
     * 发送文件
     */
    private void sendFile(int position) {
        CollectFile collectFile = mCollectFiles.get(position);
        String local_address = collectFile.getLocal_address();
        Intent intent = new Intent(getActivity(), ChooseGroupMemberActivity.class);
        intent.putExtra("isSendFile", true);
        intent.putExtra("isCollectFile", true);
        intent.putExtra("localAddress", local_address);
        startActivity(intent);
    }

    /**
     * 查看文件
     */
    private void checkFile(int position) {
        mCollectFiles = DBHelper.findCollectFile(mType);//更新数据
        CollectFile collectFile = mCollectFiles.get(position);
        String local_address = collectFile.getLocal_address();
        //判断本地文件是否存在
        if (new File(local_address).exists()) {//存在此文件直接打开
            UIUtils.showTip(local_address);
            Intent intent = null;
            intent = IntentUtil.getRightIntent(collectFile.getSuf(), local_address);
            try {
                startActivity(intent);
            } catch (Exception e) {
                UIUtils.showTip("暂不支持打开此类型文件");
            }
        }
    }
}
