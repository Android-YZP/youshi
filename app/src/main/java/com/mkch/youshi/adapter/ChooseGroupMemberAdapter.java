package com.mkch.youshi.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.ChooseGroupMemberActivity;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.util.UIUtils;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 选择群成员列表适配器
 */
public class ChooseGroupMemberAdapter extends KJAdapter<Friend> implements SectionIndexer {

    private KJBitmap kjb = new KJBitmap();
    private List<Friend> datas;
    //记录checkbox的状态
    public HashMap<Integer, Boolean> mState = new HashMap<Integer, Boolean>();
    private static boolean isSendFile = false;
    private static ChooseGroupMemberActivity activity;
    private ProgressDialog mProgressDialog;


    public ChooseGroupMemberAdapter(AbsListView view, List<Friend> mDatas) {
        super(view, mDatas, R.layout.item_list_choose_group_member);
        isSendFile = false;
        datas = mDatas;
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Collections.sort(datas);
    }

    public ChooseGroupMemberAdapter(AbsListView view, List<Friend> mDatas, ChooseGroupMemberActivity activity) {
        super(view, mDatas, R.layout.item_list_choose_group_member);
        this.activity = activity;
        isSendFile = true;
        datas = mDatas;
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Collections.sort(datas);
    }

    @Override
    public void convert(AdapterHolder helper, Friend item, boolean isScrolling) {
    }

    @Override
    public void convert(AdapterHolder holder, Friend item, boolean isScrolling, final int position) {

        holder.setText(R.id.tv_choose_group_member_name, item.getNickname());
        ImageView headImg = holder.getView(R.id.iv_choose_group_member_head);
        ImageOptions _image_options = new ImageOptions.Builder()
                .setCircular(true)
                .build();
        if (item.getHead_pic() != null && !item.getHead_pic().equals("") && !item.getHead_pic().equals("null")) {
            x.image().bind(headImg, item.getHead_pic(), _image_options);
        } else {
            headImg.setImageResource(R.drawable.default_headpic);
        }
//        if (isScrolling) {
//            kjb.displayCacheOrDefult(headImg, item.getHeadPic(), R.drawable.default_headpic);
//        } else {
//            kjb.displayWithLoadBitmap(headImg, item.getHeadPic(), R.drawable.default_headpic);
//        }
        TextView tvLetter = holder.getView(R.id.tv_list_choose_group_member_catalog);
        View tvLine = holder.getView(R.id.line_list_choose_group_member);
        final CheckBox cbChoose = holder.getView(R.id.cb_choose_group_member);
        LinearLayout layout = holder.getView(R.id.layout_list_choose_group_member);
        if (isSendFile) cbChoose.setClickable(false);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSendFile) {//转发文件
                    showDiaLog(cbChoose);
                } else {
                    cbChoose.setChecked(!cbChoose.isChecked());
                }
            }
        });
        cbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mState.put(position, isChecked);
                } else {
                    mState.remove(position);
                }
            }
        });
        cbChoose.setChecked((mState.get(position) == null ? false : true));
        //如果是第0个
        if (position == 0) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText("" + item.getFirstChar());
            tvLine.setVisibility(View.VISIBLE);
        } else {
            //如果和上一个item的首字母不同，则认为是新分类的开始
            Friend prevData = datas.get(position - 1);
            if (item.getFirstChar() != prevData.getFirstChar()) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText("" + item.getFirstChar());
                tvLine.setVisibility(View.VISIBLE);
            } else {
                tvLetter.setVisibility(View.GONE);
                tvLine.setVisibility(View.GONE);
            }
        }
    }

    //是否转发该联系人
    private void showDiaLog(final CheckBox cbChoose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("确认发送文件给联系人?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //从云端删除文件,从本地删除文件
                cbChoose.setChecked(!cbChoose.isChecked());
                sendFile2SomeOne();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    //发送文件给某人
    private void sendFile2SomeOne() {
        mProgressDialog = ProgressDialog.show(activity, null, "正在发送....", true, true);
        String friendid = "";
        HashMap<Integer, Boolean> state = mState;
        for (int j = 0; j < getCount(); j++) {
            if (state.get(j) != null) {
                Friend friend = getItem(j);
                friendid = friend.getFriendid();
            }
        }
        if (friendid.isEmpty()) {
            UIUtils.showTip("请选择联系人");
            return;
        }

        String sendFile = new Gson().toJson(YoupanFileAdapter.mChooseFile);
        String peer = friendid;  //获取与用户 "sample_user_1" 的会话
        TIMConversation conversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                peer);                      //会话对方用户帐号//对方id

        //构造一条消息
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText("thisSendFile" + sendFile);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            // Log.d(tag, "addElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                UIUtils.showTip("发送失败");
                mProgressDialog.dismiss();
                activity.finish();
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                UIUtils.showTip("发送成功");
                mProgressDialog.dismiss();
                activity.finish();
            }
        });
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        Friend item = datas.get(position);
        return item.getFirstChar();
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = datas.get(i).getFirstChar();
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
