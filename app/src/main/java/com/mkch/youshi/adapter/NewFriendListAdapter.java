package com.mkch.youshi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.model.Friend;

import java.util.List;

/**
 * 新的朋友列表
 * Created by SunnyJiang on 2016/8/18.
 */
public class NewFriendListAdapter extends BaseAdapter implements ListAdapter{
    private Integer[] newFriendHead = new Integer[]{R.drawable.maillist,R.drawable.maillist};
    private String[] newFriendName = new String[]{"张三","李四"};
    private String[] newFriendRemark = new String[]{"我是张三","我是李四"};

    private List<Friend> mFriends;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public NewFriendListAdapter(){
    }

    public NewFriendListAdapter(Context context,List<Friend> mFriends) {
        this.mContext = context;
        this.mFriends = mFriends;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
        public int getCount() {
        return mFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FriendViewHolder friendViewHolder = null;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_new_friend, null);
            friendViewHolder = new FriendViewHolder();
            friendViewHolder.iv_new_friend_head = (ImageView) convertView.findViewById(R.id.iv_new_friend_head);
            friendViewHolder.tv_new_friend_name = (TextView) convertView.findViewById(R.id.tv_new_friend_name);
            friendViewHolder.tv_new_friend_remark = (TextView) convertView.findViewById(R.id.tv_new_friend_remark);
            friendViewHolder.btn_new_friend_add = (Button) convertView.findViewById(R.id.btn_new_friend_add);
            friendViewHolder.tv_new_friend_added = (TextView) convertView.findViewById(R.id.tv_new_friend_added);
            convertView.setTag(friendViewHolder);

        }else{
            friendViewHolder = (FriendViewHolder) convertView.getTag();
        }
        Friend friend = mFriends.get(position);
        if (friend!=null){
            //        friendViewHolder.iv_new_friend_head.setImageResource(newFriendHead[position]);
            String _nick_name = friend.getNickname();
            if (_nick_name!=null&&!_nick_name.equals("")){
                friendViewHolder.tv_new_friend_name.setText(_nick_name);
            }else{
                friendViewHolder.tv_new_friend_name.setText("未知");
            }

            String _remark = friend.getRemark();
            if (_remark!=null&&!_remark.equals("")){
                friendViewHolder.tv_new_friend_remark.setText(_remark);
            }else{
                friendViewHolder.tv_new_friend_remark.setText("");
            }
            int _status = friend.getStatus();
            if (_status==2){
                //待接受
                friendViewHolder.btn_new_friend_add.setVisibility(View.VISIBLE);
                friendViewHolder.tv_new_friend_added.setVisibility(View.GONE);
                friendViewHolder.btn_new_friend_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击了接接受
                        Log.d("jlj","----------------onClick="+mFriends.get(position).getNickname());

                    }
                });

            }else if (_status==1){
                //已添加
                friendViewHolder.btn_new_friend_add.setVisibility(View.GONE);
                friendViewHolder.tv_new_friend_added.setVisibility(View.VISIBLE);

            }


        }

        return convertView;
    }

    class FriendViewHolder{
        private ImageView iv_new_friend_head;
        private TextView tv_new_friend_name;
        private TextView tv_new_friend_remark;
        private Button btn_new_friend_add;
        private TextView tv_new_friend_added;
    }
}
