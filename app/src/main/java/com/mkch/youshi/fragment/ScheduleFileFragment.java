package com.mkch.youshi.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.FilePreviewActivity;
import com.mkch.youshi.activity.ScheduleDetailActivity;
import com.mkch.youshi.adapter.ScheduleListAdapter;

public class ScheduleFileFragment extends Fragment {

    private SwipeRefreshLayout mSRLayout;
    private ListView mListView;
    //长按后选择的操作列表
    private String[] operation_list = {"下载", "转发"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mSRLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlayout_file);
        mSRLayout.setColorSchemeColors(R.color.common_topbar_bg_color);
        mListView = (ListView) view.findViewById(R.id.list_file);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        ListAdapter mAdapter = new ScheduleListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }

    private void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent _intent = null;
                switch (position) {
                    case 0:
                        _intent = new Intent(getActivity(), ScheduleDetailActivity.class);
                        startActivity(_intent);
                        break;
                }
            }
        });

        //长按单个日程文件，弹出对话框选择操作
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity()).setItems(operation_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                return true;
            }
        });
    }
}
