package com.mkch.youshi.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.ContactEntity;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 联系人列表适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 9/16/15.
 */
public class PhoneContactAdapter extends KJAdapter<ContactEntity> implements SectionIndexer {

    private KJBitmap kjb = new KJBitmap();
    private ArrayList<ContactEntity> datas;

    public PhoneContactAdapter(AbsListView view, ArrayList<ContactEntity> mDatas) {
        super(view, mDatas, R.layout.item_list_phone_contacts);
        datas = mDatas;
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Collections.sort(datas);
    }

    @Override
    public void convert(AdapterHolder helper, ContactEntity item, boolean isScrolling) {

    }

    @Override
    public void convert(AdapterHolder holder, ContactEntity item, boolean isScrolling, int position) {

        holder.setText(R.id.tv_phone_contacts_name, item.getName());
//        ImageView headImg = holder.getView(R.id.iv_phone_contacts_head);
//        if (isScrolling) {
//            kjb.displayCacheOrDefult(headImg, item.getUrl(), R.drawable.default_head_rect);
//        } else {
//            kjb.displayWithLoadBitmap(headImg, item.getUrl(), R.drawable.default_head_rect);
//        }

        TextView tvLetter = holder.getView(R.id.tv_list_phone_contacts_catalog);
        View tvLine = holder.getView(R.id.line_list_phone_contacts);
        Button btnAdd = holder.getView(R.id.btn_phone_contacts_add);

        if (item.isAdd()){
            btnAdd.setBackgroundColor(Color.WHITE);
            btnAdd.setText("已添加");
            btnAdd.setEnabled(false);
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
