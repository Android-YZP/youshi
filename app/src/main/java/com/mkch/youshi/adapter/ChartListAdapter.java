package com.mkch.youshi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.ChatBean;

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

    public ChartListAdapter(List<ChatBean> mChatBeen) {
        this.mChatBeen = mChatBeen;
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

        //logo  //暂时
        ImageOptions _image_options = new ImageOptions.Builder()
                .setCircular(true)
                .build();

        if (_char_bean!=null){
            if (holder instanceof FromTxtViewHolder){
                ((FromTxtViewHolder)holder).tv_item_from_txt.setText(_char_bean.getContent());
                //暂时
                x.image().bind(((FromTxtViewHolder)holder).iv_item_from_headpic,"http://cdn.duitang.com/uploads/item/201502/04/20150204000709_QCzwf.thumb.224_0.jpeg",_image_options);
            }else if (holder instanceof SendTxtViewHolder){
                ((SendTxtViewHolder)holder).tv_item_send_txt.setText(_char_bean.getContent());
                //暂时
                x.image().bind(((SendTxtViewHolder)holder).iv_item_send_headpic,"http://p6.qhimg.com/t0126e0bed7fa0741a1.jpg",_image_options);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mChatBeen.size();
    }



    class SendTxtViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_item_send_txt;
        private ImageView iv_item_send_headpic;//暂时

        public SendTxtViewHolder(View itemView) {
            super(itemView);
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
        private TextView tv_item_from_txt;
        private ImageView iv_item_from_headpic;//暂时

        public FromTxtViewHolder(View itemView) {
            super(itemView);
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
