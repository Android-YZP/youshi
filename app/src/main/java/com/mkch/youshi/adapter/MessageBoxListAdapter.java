package com.mkch.youshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.model.MessageBox;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by SunnyJiang on 2016/8/31.
 * 消息盒子Adapter
 */
public class MessageBoxListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageBox> mMessageBoxes;
    private LayoutInflater mLayoutInflater;

    public MessageBoxListAdapter(Context mContext, List<MessageBox> mMessageBoxes) {
        this.mContext = mContext;
        this.mMessageBoxes = mMessageBoxes;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMessageBoxes.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageBoxes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgBoxViewHolder _msgBoxViewHolder;
        if (convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.lv_item_message_one_msg,null);
            _msgBoxViewHolder = new MsgBoxViewHolder();
            x.view().inject(_msgBoxViewHolder,convertView);
            convertView.setTag(_msgBoxViewHolder);

        }else {
            _msgBoxViewHolder = (MsgBoxViewHolder) convertView.getTag();
        }
        //logo
        MessageBox _msg_box = mMessageBoxes.get(position);
        ImageOptions _image_options = new ImageOptions.Builder()
                .setCircular(true)
                .build();
        x.image().bind(_msgBoxViewHolder.ivBoxLogo,_msg_box.getBoxLogo(),_image_options);
        _msgBoxViewHolder.tvTitle.setText(_msg_box.getTitle());//标题
        _msgBoxViewHolder.tvNums.setText(_msg_box.getNums()+"");//消息数
        _msgBoxViewHolder.tvInfo.setText(_msg_box.getInfo());//消息简语
        _msgBoxViewHolder.tvTime.setText(_msg_box.getLasttime());//时间
        return convertView;
    }

    private class MsgBoxViewHolder{
        @ViewInject(R.id.iv_item_message_one_msg_boxlogo)
        ImageView ivBoxLogo;
        @ViewInject(R.id.tv_item_message_one_msg_title)
        TextView tvTitle;
        @ViewInject(R.id.iv_item_message_one_msg_round_nums)
        TextView tvNums;
        @ViewInject(R.id.tv_item_message_one_msg_info)
        TextView tvInfo;
        @ViewInject(R.id.tv_item_message_one_msg_time)
        TextView tvTime;
    }
}
