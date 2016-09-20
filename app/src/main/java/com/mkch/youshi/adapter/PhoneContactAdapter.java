package com.mkch.youshi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.ContactEntity;
import com.mkch.youshi.util.RosterHelper;
import com.mkch.youshi.util.XmppHelper;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.util.XmppStringUtils;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 联系人列表适配器
 */
public class PhoneContactAdapter extends KJAdapter<ContactEntity> implements SectionIndexer {

    private KJBitmap kjb = new KJBitmap();
    private ArrayList<ContactEntity> datas;
    private XMPPTCPConnection connection; //connection
    private Context mContext;//上下文

    public PhoneContactAdapter(AbsListView view, ArrayList<ContactEntity> mDatas, Context mContext) {
        super(view, mDatas, R.layout.item_list_phone_contacts);
        datas = mDatas;
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Collections.sort(datas);
        //获取连接
        connection = XmppHelper.getConnection();
        this.mContext = mContext;
    }

    @Override
    public void convert(AdapterHolder helper, ContactEntity item, boolean isScrolling) {

    }

    @Override
    public void convert(AdapterHolder holder, ContactEntity item, boolean isScrolling, final int position) {

        holder.setText(R.id.tv_phone_contacts_name, item.getName());
//        ImageView headImg = holder.getView(R.id.iv_phone_contacts_head);
//        if (isScrolling) {
//            kjb.displayCacheOrDefult(headImg, item.getUrl(), R.drawable.default_head_rect);
//        } else {
//            kjb.displayWithLoadBitmap(headImg, item.getUrl(), R.drawable.default_head_rect);
//        }

        TextView tvLetter = holder.getView(R.id.tv_list_phone_contacts_catalog);
        TextView tvAdded = holder.getView(R.id.tv_phone_contacts_added);
        View tvLine = holder.getView(R.id.line_list_phone_contacts);
        Button btnAdd = holder.getView(R.id.btn_phone_contacts_add);

        if (item.isAdd()) {
            btnAdd.setVisibility(View.GONE);
            tvAdded.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            tvAdded.setVisibility(View.GONE);
            //点击了某个手机联系人的添加按钮
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContactEntity _contactEntiy = datas.get(position);
                    if (_contactEntiy != null) {
                        final String _name = _contactEntiy.getName();
                        String _OpenFireUsrName = _contactEntiy.getOpenFireUserName();
                        Log.d("jlj", "btn---------------------onclick=" + _name + "," + _OpenFireUsrName);
                        //获取用户名和_OpenFireUsrName；并发起添加功能
                        final String _jid = XmppStringUtils.completeJidFrom(_OpenFireUsrName, connection.getServiceName());//转jid
                        AlertDialog.Builder _builder = new AlertDialog.Builder(mContext);
                        _builder.setTitle("添加好友");
                        _builder.setMessage("确定添加" + _jid + "为好友吗？");
                        _builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //发送请求添加好友
                                RosterHelper _roster_helper = RosterHelper.getInstance(connection);
//                                String _nickname = XmppStringUtils.parseLocalpart(_jid);
                                _roster_helper.addEntry(_jid, _name, "Friends");
                                //立马删除好友
                                _roster_helper.removeEntry(_jid);

                            }
                        });
                        _builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        _builder.show();
                    }
                }
            });
        }
        //如果是第0个
        if (position == 0) {
            tvLetter.setVisibility(View.VISIBLE);
            tvLetter.setText("" + item.getFirstChar());
            tvLine.setVisibility(View.VISIBLE);
        } else {
            //如果和上一个item的首字母不同，则认为是新分类的开始
            ContactEntity prevData = datas.get(position - 1);
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

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        ContactEntity item = datas.get(position);
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
