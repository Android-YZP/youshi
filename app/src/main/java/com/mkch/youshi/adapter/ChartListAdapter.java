package com.mkch.youshi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.model.ChatBean;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.TimesUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;


/**
 * Created by SunnyJiang on 2016/8/23.
 */
public class ChartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CHART_TYPE_REV = 1;
    public static final int CHART_TYPE_SEND = 2;
    private List<ChatBean> mChatBeen;
    private String mSendNickname;//发送者昵称
    private String mFromNickname;//接受者昵称
    private Friend mFriend;
    private User mUser;
    private Context mContext;

    public ChartListAdapter(List<ChatBean> mChatBeen, Friend mFriend, User mUser,Context mContext) {
        this.mChatBeen = mChatBeen;
        this.mFriend = mFriend;
        this.mUser = mUser;
        this.mContext = mContext;
        fullNicname();//填充两个昵称
    }

    /**
     * 填充两个昵称
     */
    private void fullNicname() {
        String _fromNickname = mFriend.getNickname();
        String _senderNickname = mUser.getNickName();
        if (_fromNickname!=null&&!_fromNickname.equals("")&&!_fromNickname.equals("null")){
            this.mFromNickname = _fromNickname;
        }else{
            this.mFromNickname = mFriend.getFriendid();
        }
        if (_senderNickname!=null&&!_senderNickname.equals("")&&!_senderNickname.equals("null")){
            this.mSendNickname = _senderNickname;
        }else{
            this.mSendNickname = mUser.getOpenFireUserName();
        }
    }

    /**
     * 这是一个添加一条数据并刷新界面的方法
     *
     * @param chart_bean
     */
    public void addData(ChatBean chart_bean) {
        mChatBeen.add(mChatBeen.size(), chart_bean);
        notifyItemInserted(mChatBeen.size());
    }


    @Override
    public int getItemViewType(int position) {
        ChatBean _chart_bean = mChatBeen.get(position);
        if (_chart_bean.getType()==0){
            return CHART_TYPE_REV;
        }else if (_chart_bean.getType()==1){
            return CHART_TYPE_SEND;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == CHART_TYPE_REV){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_left_txt,parent,false);
            return new FromTxtViewHolder(view);
        }else if (viewType == CHART_TYPE_SEND){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_right_txt,parent,false);
            return new SendTxtViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatBean _char_bean = mChatBeen.get(position);
        boolean closeEnough = false;
        if (position!=0){
            ChatBean _char_bean_before = mChatBeen.get(position-1);//前一个聊天信息
            closeEnough = TimesUtils.isCloseEnough(_char_bean_before.getDate(), _char_bean.getDate());
        }

        //logo  //暂时
        ImageOptions _image_options = new ImageOptions.Builder()
                .setCircular(true)
                .build();

        if (_char_bean!=null){
            if (holder instanceof FromTxtViewHolder){
                //左侧-接受的信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough){
                    ((FromTxtViewHolder)holder).tv_item_from_time.setVisibility(View.GONE);
                }else{
                    ((FromTxtViewHolder)holder).tv_item_from_time.setVisibility(View.VISIBLE);
                    ((FromTxtViewHolder)holder).tv_item_from_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((FromTxtViewHolder)holder).tv_item_from_nickname.setText(mFromNickname);
                //显示内容
                ((FromTxtViewHolder)holder).tv_item_from_txt.setText(_char_bean.getContent());
                String _headPic = mFriend.getHead_pic();//头像地址
                if (_headPic!=null&&!_headPic.equals("")&&!_headPic.equals("null")) {
                    x.image().bind(((FromTxtViewHolder) holder).iv_item_from_headpic, _headPic, _image_options);
                }else{
                    ((FromTxtViewHolder) holder).iv_item_from_headpic.setImageResource(R.drawable.default_headpic);

                }
            }else if (holder instanceof SendTxtViewHolder){
                //右侧-发送的信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough){
                    ((SendTxtViewHolder)holder).tv_item_send_time.setVisibility(View.GONE);
                }else{
                    ((SendTxtViewHolder)holder).tv_item_send_time.setVisibility(View.VISIBLE);
                    ((SendTxtViewHolder)holder).tv_item_send_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((SendTxtViewHolder)holder).tv_item_send_nickname.setText(mSendNickname);
                //显示内容
                ((SendTxtViewHolder)holder).tv_item_send_txt.setText(_char_bean.getContent());
                //头像
                String _headPic = mUser.getHeadPic();//头像地址
                if (_headPic!=null&&!_headPic.equals("")&&!_headPic.equals("null")){
                    x.image().bind(((SendTxtViewHolder)holder).iv_item_send_headpic,_headPic,_image_options);
                }else{
                    ((SendTxtViewHolder)holder).iv_item_send_headpic.setImageResource(R.drawable.default_headpic);
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return mChatBeen.size();
    }



    class SendTxtViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_item_send_nickname;//发送者昵称
        private TextView tv_item_send_time;//发送时间
        private TextView tv_item_send_txt;//发送内容
        private ImageView iv_item_send_headpic;//暂时

        public SendTxtViewHolder(View itemView) {
            super(itemView);
            tv_item_send_nickname = (TextView)itemView.findViewById(R.id.tv_right_user);
            tv_item_send_time = (TextView)itemView.findViewById(R.id.tv_item_chat_list_right_time);
            tv_item_send_txt = (TextView) itemView.findViewById(R.id.tv_right_say_content);
            iv_item_send_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_right_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener!=null){
                        myItemClickListener.onItemClick(v,getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener!=null){
                        myItemLongClickListener.onItemLongClick(v,getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    class FromTxtViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_item_from_nickname;//接受者昵称
        private TextView tv_item_from_time;//接收时间
        private TextView tv_item_from_txt;//发送内容
        private ImageView iv_item_from_headpic;//暂时

        public FromTxtViewHolder(View itemView) {
            super(itemView);
            tv_item_from_nickname = (TextView) itemView.findViewById(R.id.tv_left_user);
            tv_item_from_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_left_time);
            tv_item_from_txt = (TextView) itemView.findViewById(R.id.tv_left_say_content);
            iv_item_from_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_left_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener!=null){
                        myItemClickListener.onItemClick(v,getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener!=null){
                        myItemLongClickListener.onItemLongClick(v,getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }



    //定义两个listener
    public interface MyItemClickListener{
        public void onItemClick(View view, int position);
    }

    public interface MyItemLongClickListener{
        public void onItemLongClick(View view, int position);
    }

    private MyItemClickListener myItemClickListener;
    private MyItemLongClickListener myItemLongClickListener;

    public void setOnItemClickListener(MyItemClickListener listener){
        this.myItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.myItemLongClickListener = listener;
    }
}
