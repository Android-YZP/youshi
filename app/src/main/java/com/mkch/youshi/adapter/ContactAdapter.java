package com.mkch.youshi.adapter;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.model.Friend;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 优时好友列表适配器
 */
public class ContactAdapter extends KJAdapter<Friend> implements SectionIndexer {

    private KJBitmap kjb = new KJBitmap();
    private List<Friend> datas;

    public ContactAdapter(AbsListView view, List<Friend> mDatas) {
        super(view, mDatas, R.layout.item_list_contact);
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
    public void convert(AdapterHolder holder, Friend item, boolean isScrolling, int position) {

        holder.setText(R.id.tv_contact_name, item.getNickname());
        ImageView headImg = holder.getView(R.id.iv_contact_head);
        ImageOptions _image_options = new ImageOptions.Builder()
                .setCircular(true)
                .build();
        if (item.getHead_pic() != null && !item.getHead_pic().equals("") && !item.getHead_pic().equals("null")) {
            x.image().bind(headImg, item.getHead_pic(), _image_options);
        } else {
            headImg.setImageResource(R.drawable.default_headpic);
        }
//        if (isScrolling) {
//            kjb.displayCacheOrDefult(headImg, item.getUrl(), R.drawable.default_head_rect);
//        } else {
//            kjb.displayWithLoadBitmap(headImg, item.getUrl(), R.drawable.default_head_rect);
//        }

        TextView tvLetter = holder.getView(R.id.tv_list_contact_catalog);
        View tvLine = holder.getView(R.id.line_list_contact);

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
