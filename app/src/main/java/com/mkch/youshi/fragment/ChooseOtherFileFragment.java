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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChooseFileActivity;
import com.mkch.youshi.activity.FilePreviewActivity;
import com.mkch.youshi.activity.FilePreviewPicActivity;
import com.mkch.youshi.adapter.DropBoxListAdapter;
import com.mkch.youshi.util.UIUtils;

public class ChooseOtherFileFragment extends Fragment {

    private SwipeRefreshLayout mSRLayout;
    private ListView mListView;
    //保存选择的性别
    private String[] operation_list = {"重命名", "删除"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_other_file, container, false);
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
        ListAdapter mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    private void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //界面更新
                CheckBox _checkBox = (CheckBox) view.findViewById(R.id.cb_complete);
                _checkBox.setChecked(!_checkBox.isChecked());
                boolean isAddSuccess = ((ChooseFileActivity) getActivity()).addChoosedNumber(_checkBox.isChecked());
                if (!isAddSuccess)
                    _checkBox.setChecked(!_checkBox.isChecked());
            }
        });

    }

    class MyAdapter extends BaseAdapter {
        private Integer[] fileImg = new Integer[]{R.drawable.txt,
                R.drawable.doc, R.drawable.ppt,
                R.drawable.jpg, R.drawable.mp4};
        private String[] fileName = new String[]{"企业网站需求分析说明书.txt", "科技厅众创空间资料.doc", "众创基地相关资料.ppt", "宜兴众创基地图片.jpg", "众创基地相关视频.mp4"};
        private String[] fileTime = new String[]{"15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51", "15-09-21    09:51"};

        @Override
        public int getCount() {
            return fileName.length != 0 ? fileName.length : 0;
        }

        @Override
        public Object getItem(int position) {
            return fileName[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = UIUtils.inflate(R.layout.item_list_choose_file);
            }
            ImageView ivFileImageItem = (ImageView) convertView.findViewById(R.id.iv_file_choose_image);
            ivFileImageItem.setImageResource(fileImg[position]);
            TextView tvFileNameItem = (TextView) convertView.findViewById(R.id.tv_file_choose_name);
            tvFileNameItem.setText(fileName[position]);
            TextView tvFileTimeItem = (TextView) convertView.findViewById(R.id.tv_file_choose_time);
            tvFileTimeItem.setText(fileTime[position]);
            return convertView;
        }
    }
}
