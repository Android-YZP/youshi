package com.mkch.youshi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.mkch.youshi.model.Group;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.view.Expression;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.IOException;
import java.util.List;


/**
 * Created by SunnyJiang on 2016/8/23.
 */
public class ChartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CHART_TYPE_REV_TEXT = 1;
    public static final int CHART_TYPE_SEND_TEXT = 2;
    public static final int CHART_TYPE_REV_SOUND = 3;
    public static final int CHART_TYPE_SEND_SOUND = 4;
    public static final int CHART_TYPE_REV_PIC = 5;
    public static final int CHART_TYPE_SEND_PIC = 6;
    public static final int CHART_TYPE_REV_FILE = 7;
    public static final int CHART_TYPE_SEND_FILE = 8;
    public static final int CHART_TYPE_REV_FACE = 9;
    public static final int CHART_TYPE_SEND_FACE = 10;
    private List<ChatBean> mChatBeen;
    private String mSendNickname;//发送者昵称
    private String mFromNickname;//接受者昵称
    private Friend mFriend;
    private Group mGroup;
    private User mUser;
    private Context mContext;
    private String _headPic;

    public ChartListAdapter(List<ChatBean> mChatBeen, Friend mFriend, User mUser, Context mContext) {
        this.mChatBeen = mChatBeen;
        this.mFriend = mFriend;
        this.mUser = mUser;
        this.mContext = mContext;
        fullNicname();//填充两个昵称
    }

    public ChartListAdapter(List<ChatBean> mChatBeen, Group mGroup, User mUser, Context mContext) {
        this.mChatBeen = mChatBeen;
        this.mGroup = mGroup;
        this.mUser = mUser;
        this.mContext = mContext;
        fullNicname();//填充两个昵称
    }

    /**
     * 填充两个昵称
     */
    private void fullNicname() {
        if (mFriend != null) {
            String _fromNickname = mFriend.getNickname();
            String _senderNickname = mUser.getNickName();
            if (_fromNickname != null && !_fromNickname.equals("") && !_fromNickname.equals("null")) {
                this.mFromNickname = _fromNickname;
            } else {
                this.mFromNickname = mFriend.getFriendid();
            }
            if (_senderNickname != null && !_senderNickname.equals("") && !_senderNickname.equals("null")) {
                this.mSendNickname = _senderNickname;
            } else {
                this.mSendNickname = mUser.getOpenFireUserName();
            }
        } else if (mGroup != null) {
            String _fromNickname = mGroup.getGroupName();
            String _senderNickname = mUser.getNickName();
            if (_fromNickname != null && !_fromNickname.equals("") && !_fromNickname.equals("null")) {
                this.mFromNickname = _fromNickname;
            } else {
                this.mFromNickname = mGroup.getGroupID();
            }
            if (_senderNickname != null && !_senderNickname.equals("") && !_senderNickname.equals("null")) {
                this.mSendNickname = _senderNickname;
            } else {
                this.mSendNickname = mUser.getOpenFireUserName();
            }
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
        if (_chart_bean.getType() == 0 && _chart_bean.getMsgModel() == 1) {
            return CHART_TYPE_REV_TEXT;
        } else if (_chart_bean.getType() == 1 && _chart_bean.getMsgModel() == 1) {
            return CHART_TYPE_SEND_TEXT;
        } else if (_chart_bean.getType() == 0 && _chart_bean.getMsgModel() == 2) {
            return CHART_TYPE_REV_SOUND;
        } else if (_chart_bean.getType() == 1 && _chart_bean.getMsgModel() == 2) {
            return CHART_TYPE_SEND_SOUND;
        } else if (_chart_bean.getType() == 0 && _chart_bean.getMsgModel() == 3) {
            return CHART_TYPE_REV_PIC;
        } else if (_chart_bean.getType() == 1 && _chart_bean.getMsgModel() == 3) {
            return CHART_TYPE_SEND_PIC;
        } else if (_chart_bean.getType() == 0 && _chart_bean.getMsgModel() == 4) {
            return CHART_TYPE_REV_FILE;
        } else if (_chart_bean.getType() == 1 && _chart_bean.getMsgModel() == 4) {
            return CHART_TYPE_SEND_FILE;
        } else if (_chart_bean.getType() == 0 && _chart_bean.getMsgModel() == 5) {
            return CHART_TYPE_REV_FACE;
        } else if (_chart_bean.getType() == 1 && _chart_bean.getMsgModel() == 5) {
            return CHART_TYPE_SEND_FACE;
        } else if (_chart_bean.getType() == 0 && _chart_bean.getMsgModel() == 0) {
            return CHART_TYPE_REV_TEXT;
        } else if (_chart_bean.getType() == 1 && _chart_bean.getMsgModel() == 0) {
            return CHART_TYPE_SEND_TEXT;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == CHART_TYPE_REV_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_left_txt, parent, false);
            return new FromTxtViewHolder(view);
        } else if (viewType == CHART_TYPE_SEND_TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_right_txt, parent, false);
            return new SendTxtViewHolder(view);
        } else if (viewType == CHART_TYPE_REV_SOUND) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_left_sound, parent, false);
            return new FromSoundViewHolder(view);
        } else if (viewType == CHART_TYPE_SEND_SOUND) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_right_sound, parent, false);
            return new SendSoundViewHolder(view);
        } else if (viewType == CHART_TYPE_REV_PIC) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_left_pic, parent, false);
            return new FromPicViewHolder(view);
        } else if (viewType == CHART_TYPE_SEND_PIC) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_right_pic, parent, false);
            return new SendPicViewHolder(view);
        } else if (viewType == CHART_TYPE_REV_FILE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_left_file, parent, false);
            return new FromFileViewHolder(view);
        } else if (viewType == CHART_TYPE_SEND_FILE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_right_file, parent, false);
            return new SendFileViewHolder(view);
        } else if (viewType == CHART_TYPE_REV_FACE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_left_expression, parent, false);
            return new FromFaceViewHolder(view);
        } else if (viewType == CHART_TYPE_SEND_FACE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_list_right_expression, parent, false);
            return new SendFaceViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatBean _char_bean = mChatBeen.get(position);
        boolean closeEnough = false;
        if (position != 0) {
            ChatBean _char_bean_before = mChatBeen.get(position - 1);//前一个聊天信息
            closeEnough = TimesUtils.isCloseEnough(_char_bean_before.getDate(), _char_bean.getDate());
        }
        ImageOptions _image_options = new ImageOptions.Builder()
                .setCircular(true)
                .build();
        if (_char_bean != null) {
            if (holder instanceof FromTxtViewHolder) {
                //左侧-接受文本信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((FromTxtViewHolder) holder).tv_item_from_time.setVisibility(View.GONE);
                } else {
                    ((FromTxtViewHolder) holder).tv_item_from_time.setVisibility(View.VISIBLE);
                    ((FromTxtViewHolder) holder).tv_item_from_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((FromTxtViewHolder) holder).tv_item_from_nickname.setText(mFromNickname);
                //显示内容
                ((FromTxtViewHolder) holder).tv_item_from_txt.setText(_char_bean.getContent());
                if (mFriend != null) {
                    _headPic = mFriend.getHead_pic();//头像地址
                } else {
                    _headPic = mGroup.getGroupHead();
                }
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((FromTxtViewHolder) holder).iv_item_from_headpic, _headPic, _image_options);
                } else {
                    ((FromTxtViewHolder) holder).iv_item_from_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof SendTxtViewHolder) {
                //右侧-发送文本信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((SendTxtViewHolder) holder).tv_item_send_time.setVisibility(View.GONE);
                } else {
                    ((SendTxtViewHolder) holder).tv_item_send_time.setVisibility(View.VISIBLE);
                    ((SendTxtViewHolder) holder).tv_item_send_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((SendTxtViewHolder) holder).tv_item_send_nickname.setText(mSendNickname);
                //显示内容
                ((SendTxtViewHolder) holder).tv_item_send_txt.setText(_char_bean.getContent());
                //头像
                String _headPic = mUser.getHeadPic();//头像地址
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((SendTxtViewHolder) holder).iv_item_send_headpic, _headPic, _image_options);
                } else {
                    ((SendTxtViewHolder) holder).iv_item_send_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof FromSoundViewHolder) {
                //左侧-接受语音信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((FromSoundViewHolder) holder).tv_item_from_time.setVisibility(View.GONE);
                } else {
                    ((FromSoundViewHolder) holder).tv_item_from_time.setVisibility(View.VISIBLE);
                    ((FromSoundViewHolder) holder).tv_item_from_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((FromSoundViewHolder) holder).tv_item_from_nickname.setText(mFromNickname);
                //显示内容
                ((FromSoundViewHolder) holder).tv_item_from_duration.setText(_char_bean.getDuration() + "'");
                if (mFriend != null) {
                    _headPic = mFriend.getHead_pic();//头像地址
                } else {
                    _headPic = mGroup.getGroupHead();
                }
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((FromSoundViewHolder) holder).iv_item_from_headpic, _headPic, _image_options);
                } else {
                    ((FromSoundViewHolder) holder).iv_item_from_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof SendSoundViewHolder) {
                //右侧-发送语音信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((SendSoundViewHolder) holder).tv_item_send_time.setVisibility(View.GONE);
                } else {
                    ((SendSoundViewHolder) holder).tv_item_send_time.setVisibility(View.VISIBLE);
                    ((SendSoundViewHolder) holder).tv_item_send_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((SendSoundViewHolder) holder).tv_item_send_nickname.setText(mSendNickname);
                //显示内容
                ((SendSoundViewHolder) holder).tv_item_send_duration.setText(_char_bean.getDuration() + "'");
                //头像
                String _headPic = mUser.getHeadPic();//头像地址
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((SendSoundViewHolder) holder).iv_item_send_headpic, _headPic, _image_options);
                } else {
                    ((SendSoundViewHolder) holder).iv_item_send_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof FromPicViewHolder) {
                //左侧-接受图片信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((FromPicViewHolder) holder).tv_item_from_time.setVisibility(View.GONE);
                } else {
                    ((FromPicViewHolder) holder).tv_item_from_time.setVisibility(View.VISIBLE);
                    ((FromPicViewHolder) holder).tv_item_from_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((FromPicViewHolder) holder).tv_item_from_nickname.setText(mFromNickname);
                //显示内容
                String path = _char_bean.getFileName();
                Bitmap bm = BitmapFactory.decodeFile(path);
                ((FromPicViewHolder) holder).iv_item_from_image.setImageBitmap(bm);
                if (mFriend != null) {
                    _headPic = mFriend.getHead_pic();//头像地址
                } else {
                    _headPic = mGroup.getGroupHead();
                }
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((FromPicViewHolder) holder).iv_item_from_headpic, _headPic, _image_options);
                } else {
                    ((FromPicViewHolder) holder).iv_item_from_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof SendPicViewHolder) {
                //右侧-发送图片信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((SendPicViewHolder) holder).tv_item_send_time.setVisibility(View.GONE);
                } else {
                    ((SendPicViewHolder) holder).tv_item_send_time.setVisibility(View.VISIBLE);
                    ((SendPicViewHolder) holder).tv_item_send_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((SendPicViewHolder) holder).tv_item_send_nickname.setText(mSendNickname);
                //显示内容
                String path = _char_bean.getFileName();
                Bitmap bm = BitmapFactory.decodeFile(path);
                ((SendPicViewHolder) holder).iv_item_send_image.setImageBitmap(bm);
                //头像
                String _headPic = mUser.getHeadPic();//头像地址
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((SendPicViewHolder) holder).iv_item_send_headpic, _headPic, _image_options);
                } else {
                    ((SendPicViewHolder) holder).iv_item_send_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof FromFileViewHolder) {
                //左侧-接受文件信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((FromFileViewHolder) holder).tv_item_from_time.setVisibility(View.GONE);
                } else {
                    ((FromFileViewHolder) holder).tv_item_from_time.setVisibility(View.VISIBLE);
                    ((FromFileViewHolder) holder).tv_item_from_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((FromFileViewHolder) holder).tv_item_from_nickname.setText(mFromNickname);
                //显示内容
                ((FromFileViewHolder) holder).tv_item_from_file.setText(_char_bean.getFileName());
                //头像
                if (mFriend != null) {
                    _headPic = mFriend.getHead_pic();//头像地址
                } else {
                    _headPic = mGroup.getGroupHead();
                }
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((FromFileViewHolder) holder).iv_item_from_headpic, _headPic, _image_options);
                } else {
                    ((FromFileViewHolder) holder).iv_item_from_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof SendFileViewHolder) {
                //右侧-发送文件信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((SendFileViewHolder) holder).tv_item_send_time.setVisibility(View.GONE);
                } else {
                    ((SendFileViewHolder) holder).tv_item_send_time.setVisibility(View.VISIBLE);
                    ((SendFileViewHolder) holder).tv_item_send_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((SendFileViewHolder) holder).tv_item_send_nickname.setText(mSendNickname);
                //显示内容
                ((SendFileViewHolder) holder).tv_item_send_file.setText(_char_bean.getFileName());
                //头像
                String _headPic = mUser.getHeadPic();//头像地址
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((SendFileViewHolder) holder).iv_item_send_headpic, _headPic, _image_options);
                } else {
                    ((SendFileViewHolder) holder).iv_item_send_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof FromFaceViewHolder) {
                //左侧-接受表情信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((FromFaceViewHolder) holder).tv_item_from_time.setVisibility(View.GONE);
                } else {
                    ((FromFaceViewHolder) holder).tv_item_from_time.setVisibility(View.VISIBLE);
                    ((FromFaceViewHolder) holder).tv_item_from_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((FromFaceViewHolder) holder).tv_item_from_nickname.setText(mFromNickname);
                //显示内容
                ((FromFaceViewHolder) holder).iv_item_from_face.setImageResource(Expression.expressions[_char_bean.getExpPosition()]);
                //头像
                if (mFriend != null) {
                    _headPic = mFriend.getHead_pic();//头像地址
                } else {
                    _headPic = mGroup.getGroupHead();
                }
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((FromFaceViewHolder) holder).iv_item_from_headpic, _headPic, _image_options);
                } else {
                    ((FromFaceViewHolder) holder).iv_item_from_headpic.setImageResource(R.drawable.default_headpic);
                }
            } else if (holder instanceof SendFaceViewHolder) {
                //右侧-发送表情信息=========================================
                //显示时间
                //判断两个时间靠近则不显示
                if (closeEnough) {
                    ((SendFaceViewHolder) holder).tv_item_send_time.setVisibility(View.GONE);
                } else {
                    ((SendFaceViewHolder) holder).tv_item_send_time.setVisibility(View.VISIBLE);
                    ((SendFaceViewHolder) holder).tv_item_send_time.setText(_char_bean.getDate());
                }
                //显示昵称
                ((SendFaceViewHolder) holder).tv_item_send_nickname.setText(mSendNickname);
                //显示内容
                ((SendFaceViewHolder) holder).iv_item_send_face.setImageResource(Expression.expressions[_char_bean.getExpPosition()]);
                //头像
                String _headPic = mUser.getHeadPic();//头像地址
                if (_headPic != null && !_headPic.equals("") && !_headPic.equals("null")) {
                    x.image().bind(((SendFaceViewHolder) holder).iv_item_send_headpic, _headPic, _image_options);
                } else {
                    ((SendFaceViewHolder) holder).iv_item_send_headpic.setImageResource(R.drawable.default_headpic);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChatBeen.size();
    }

    //发送文本适配器
    class SendTxtViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_send_nickname;//发送者昵称
        private TextView tv_item_send_time;//发送时间
        private TextView tv_item_send_txt;//发送内容
        private ImageView iv_item_send_headpic;//发送者头像

        public SendTxtViewHolder(View itemView) {
            super(itemView);
            tv_item_send_nickname = (TextView) itemView.findViewById(R.id.tv_right_user);
            tv_item_send_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_right_time);
            tv_item_send_txt = (TextView) itemView.findViewById(R.id.tv_right_say_content);
            iv_item_send_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_right_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //接受文本适配器
    class FromTxtViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_from_nickname;//接受者昵称
        private TextView tv_item_from_time;//接收时间
        private TextView tv_item_from_txt;//发送内容
        private ImageView iv_item_from_headpic;//接受者头像

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
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //发送语音适配器
    class SendSoundViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_send_nickname;//发送者昵称
        private TextView tv_item_send_time;//发送时间
        private TextView tv_item_send_duration;//语音时长
        private ImageView iv_item_send_headpic;//发送者头像

        public SendSoundViewHolder(View itemView) {
            super(itemView);
            tv_item_send_nickname = (TextView) itemView.findViewById(R.id.tv_right_sound_user);
            tv_item_send_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_right_sound_time);
            tv_item_send_duration = (TextView) itemView.findViewById(R.id.tv_right_say_duration);
            iv_item_send_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_right_sound_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //接受语音适配器
    class FromSoundViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_from_nickname;//接受者昵称
        private TextView tv_item_from_time;//接收时间
        private TextView tv_item_from_duration;//语音时长
        private ImageView iv_item_from_headpic;//接受者头像

        public FromSoundViewHolder(View itemView) {
            super(itemView);
            tv_item_from_nickname = (TextView) itemView.findViewById(R.id.tv_left_sound_user);
            tv_item_from_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_left_sound_time);
            tv_item_from_duration = (TextView) itemView.findViewById(R.id.tv_left_say_duration);
            iv_item_from_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_left_sound_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //发送图片适配器
    class SendPicViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_send_nickname;//发送者昵称
        private TextView tv_item_send_time;//发送时间
        private ImageView iv_item_send_image;//发送的图片
        private ImageView iv_item_send_headpic;//发送者头像

        public SendPicViewHolder(View itemView) {
            super(itemView);
            tv_item_send_nickname = (TextView) itemView.findViewById(R.id.tv_right_pic_user);
            tv_item_send_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_right_pic_time);
            iv_item_send_image = (ImageView) itemView.findViewById(R.id.iv_right_pic);
            iv_item_send_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_right_pic_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //接受图片适配器
    class FromPicViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_from_nickname;//接受者昵称
        private TextView tv_item_from_time;//接收时间
        private ImageView iv_item_from_image;//接受的图片
        private ImageView iv_item_from_headpic;//接受者头像

        public FromPicViewHolder(View itemView) {
            super(itemView);
            tv_item_from_nickname = (TextView) itemView.findViewById(R.id.tv_left_pic_user);
            tv_item_from_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_left_pic_time);
            iv_item_from_image = (ImageView) itemView.findViewById(R.id.iv_left_pic);
            iv_item_from_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_left_pic_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //发送文件适配器
    class SendFileViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_send_nickname;//发送者昵称
        private TextView tv_item_send_time;//发送时间
        private TextView tv_item_send_file;//发送文件的名称
        private ImageView iv_item_send_headpic;//发送者头像

        public SendFileViewHolder(View itemView) {
            super(itemView);
            tv_item_send_nickname = (TextView) itemView.findViewById(R.id.tv_right_file_user);
            tv_item_send_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_right_file_time);
            tv_item_send_file = (TextView) itemView.findViewById(R.id.tv_right_file_name);
            iv_item_send_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_right_file_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //接受文件适配器
    class FromFileViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_from_nickname;//接受者昵称
        private TextView tv_item_from_time;//接收时间
        private TextView tv_item_from_file;//接受文件的名称
        private ImageView iv_item_from_headpic;//接受者头像

        public FromFileViewHolder(View itemView) {
            super(itemView);
            tv_item_from_nickname = (TextView) itemView.findViewById(R.id.tv_left_file_user);
            tv_item_from_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_left_file_time);
            tv_item_from_file = (TextView) itemView.findViewById(R.id.tv_left_file_name);
            iv_item_from_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_left_file_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //发送表情适配器
    class SendFaceViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_send_nickname;//发送者昵称
        private TextView tv_item_send_time;//发送时间
        private ImageView iv_item_send_face;//发送的表情
        private ImageView iv_item_send_headpic;//发送者头像

        public SendFaceViewHolder(View itemView) {
            super(itemView);
            tv_item_send_nickname = (TextView) itemView.findViewById(R.id.tv_right_expression_user);
            tv_item_send_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_right_expression_time);
            iv_item_send_face = (ImageView) itemView.findViewById(R.id.iv_right_expression);
            iv_item_send_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_right_expression_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //接受表情适配器
    class FromFaceViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_from_nickname;//接受者昵称
        private TextView tv_item_from_time;//接收时间
        private ImageView iv_item_from_face;//接受的表情
        private ImageView iv_item_from_headpic;//接受者头像

        public FromFaceViewHolder(View itemView) {
            super(itemView);
            tv_item_from_nickname = (TextView) itemView.findViewById(R.id.tv_left_expression_user);
            tv_item_from_time = (TextView) itemView.findViewById(R.id.tv_item_chat_list_left_expression_time);
            iv_item_from_face = (ImageView) itemView.findViewById(R.id.iv_left_expression);
            iv_item_from_headpic = (ImageView) itemView.findViewById(R.id.iv_item_chat_list_left_expression_headpic);
            //设置根布局的点击监听事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null) {
                        try {
                            myItemClickListener.onItemClick(v, getAdapterPosition());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myItemLongClickListener != null) {
                        myItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    //定义两个listener
    public interface MyItemClickListener {
        public void onItemClick(View view, int position) throws IOException;
    }

    public interface MyItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }

    private MyItemClickListener myItemClickListener;
    private MyItemLongClickListener myItemLongClickListener;

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.myItemLongClickListener = listener;
    }
}
