package com.mkch.youshi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChatActivity;
import com.mkch.youshi.activity.MultiChatActivity;
import com.mkch.youshi.adapter.MessageBoxListAdapter;
import com.mkch.youshi.model.MessageBox;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SunnyJiang on 2016/8/18.
 */
@ContentView(R.layout.fragment_message)
public class MessageFragment extends Fragment {

    @ViewInject(R.id.et_message_search)
    private EditText mEtSearchMsgBox;

    @ViewInject(R.id.lv_message_msgs)
    private ListView mLvMsgs;//消息盒子列表

    private List<MessageBox> mMsgBoxes;
    private MessageBoxListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = x.view().inject(this,inflater,container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();//初始化数据
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        //清除EditText的焦点
        mEtSearchMsgBox.clearFocus();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //数据源
        mMsgBoxes = new ArrayList<>();
        MessageBox _msg_box_1 = new MessageBox("http://cdn.duitang.com/uploads/item/201502/04/20150204000709_QCzwf.thumb.224_0.jpeg",
                "张三","好的",8,"今天 9:00");
        MessageBox _msg_box_2 = new MessageBox("http://p6.qhimg.com/t0126e0bed7fa0741a1.jpg",
                "李四、王五、老李","老李：OK,明白",24,"今天 8:00");
        mMsgBoxes.add(_msg_box_1);
        mMsgBoxes.add(_msg_box_2);
        mMsgBoxes.add(_msg_box_1);
        mMsgBoxes.add(_msg_box_2);
        //适配器
        mAdapter = new MessageBoxListAdapter(getActivity(),mMsgBoxes);
        //设置适配器
        mLvMsgs.setAdapter(mAdapter);

    }

    private void setListener() {
        //点击某一项消息
        mLvMsgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //暂时用switch代替
                Intent _intent = null;
                switch (position){
                    case 0:
                        _intent = new Intent(getActivity(), ChatActivity.class);
                        startActivity(_intent);
                        break;
                    case 1:
                        _intent = new Intent(getActivity(), MultiChatActivity.class);
                        startActivity(_intent);
                        break;
                }

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
