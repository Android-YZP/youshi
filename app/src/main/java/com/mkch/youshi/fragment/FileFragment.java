package com.mkch.youshi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;
import com.mkch.youshi.adapter.FileListAdapter;

public class FileFragment extends Fragment {

    private SwipeRefreshLayout mSRLayout;
    private ListView mListView;
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        ListAdapter mAdapter = new FileListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }
}
