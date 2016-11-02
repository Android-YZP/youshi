package com.mkch.youshi.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChooseFileActivity;
import com.mkch.youshi.activity.DropBoxFileActivity;
import com.mkch.youshi.adapter.YoupanFileAdapter;
import com.mkch.youshi.config.MyApplication;
import com.mkch.youshi.model.YoupanFile;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.IntentUtil;
import com.mkch.youshi.util.UIUtils;

import java.util.ArrayList;

public class ChooseDocumentFileFragment extends Fragment {

    private ArrayList<YoupanFile> mYoupanFiles;
    private YoupanFileAdapter mAdapter;
    private SwipeRefreshLayout mSRLayout;
    private ListView mListView;
    //保存选择的性别
    private int mType;

    public ChooseDocumentFileFragment(int mType) {
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
        mSRLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlayout_file);
        mSRLayout.setColorSchemeColors(R.color.common_topbar_bg_color);
        mListView = (ListView) view.findViewById(R.id.list_document_file);

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
        mYoupanFiles = CommonUtil.findYoupanFile(mType);
        if (mYoupanFiles != null && getActivity() != null) {
            DropBoxFileActivity activity = (DropBoxFileActivity) getActivity();
            mAdapter = new YoupanFileAdapter(mYoupanFiles, activity);//拿到activity
            mListView.setAdapter(mAdapter);
        }
    }

    private void setListener() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YoupanFile youpanFile = mYoupanFiles.get(position);
                UIUtils.showTip(youpanFile.getLocal_address());
                Intent intent = null;
                intent = IntentUtil.getRightIntent(youpanFile.getSuf(), youpanFile.getLocal_address());
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    UIUtils.showTip("暂不支持打开此类型文件");
                }
            }
        });
    }


}
