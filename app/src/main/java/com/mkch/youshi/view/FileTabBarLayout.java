package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;

public class FileTabBarLayout extends LinearLayout {

    //tabbar的回调接口
    public interface IFileTabBarCallBackListener {
        public void clickItem(int id);//按了某一项后
    }

    IFileTabBarCallBackListener fileTabBarCallBackListener = null;

    public void setOnItemClickListener(IFileTabBarCallBackListener fileTabBarCallBackListener) {
        this.fileTabBarCallBackListener = fileTabBarCallBackListener;
    }

    LayoutInflater inflater;
    private TextView mTvItem0, mTvItem1, mTvItem2, mTvItem3, mTvItem4;
    private final static int FLAG_ITEM_0 = 0;
    private final static int FLAG_ITEM_1 = 1;
    private final static int FLAG_ITEM_2 = 2;
    private final static int FLAG_ITEM_3 = 3;
    private final static int FLAG_ITEM_4 = 4;

    public FileTabBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_file_tabbar, this);
        findView(view);
        initData();
        setListener();
    }

    private void findView(View view) {
        mTvItem0 = (TextView) view.findViewById(R.id.tv_file_item0);
        mTvItem1 = (TextView) view.findViewById(R.id.tv_file_item1);
        mTvItem2 = (TextView) view.findViewById(R.id.tv_file_item2);
        mTvItem3 = (TextView) view.findViewById(R.id.tv_file_item3);
        mTvItem4 = (TextView) view.findViewById(R.id.tv_file_item4);
    }

    private void initData() {
        mTvItem0.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
        mTvItem1.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
        mTvItem2.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
        mTvItem3.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
        mTvItem4.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
    }

    private void setListener() {
        mTvItem0.setOnClickListener(new FileChangeListItemClickListener());
        mTvItem1.setOnClickListener(new FileChangeListItemClickListener());
        mTvItem2.setOnClickListener(new FileChangeListItemClickListener());
        mTvItem3.setOnClickListener(new FileChangeListItemClickListener());
        mTvItem4.setOnClickListener(new FileChangeListItemClickListener());
    }

    private class FileChangeListItemClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_file_item0:
                    //1-改变文字的颜色;
                    changeTabBarItems(FLAG_ITEM_0);
                    break;
                case R.id.tv_file_item1:
                    changeTabBarItems(FLAG_ITEM_1);
                    break;
                case R.id.tv_file_item2:
                    changeTabBarItems(FLAG_ITEM_2);
                    break;
                case R.id.tv_file_item3:
                    changeTabBarItems(FLAG_ITEM_3);
                    break;
                case R.id.tv_file_item4:
                    changeTabBarItems(FLAG_ITEM_4);
                    break;
            }
            //2-实现页面的切换
            if (fileTabBarCallBackListener != null) {
                fileTabBarCallBackListener.clickItem(view.getId());
            }
        }
    }

    public void changeTabBarItems(int index) {
        switch (index) {
            case FLAG_ITEM_0:
                mTvItem0.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
                mTvItem1.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem2.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem3.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem4.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                break;
            case FLAG_ITEM_1:
                mTvItem0.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem1.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
                mTvItem2.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem3.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem4.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                break;
            case FLAG_ITEM_2:
                mTvItem0.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem1.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem2.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
                mTvItem3.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem4.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                break;
            case FLAG_ITEM_3:
                mTvItem0.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem1.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem2.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem3.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
                mTvItem4.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                break;
            case FLAG_ITEM_4:
                mTvItem0.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem1.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem2.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem3.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
                mTvItem4.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
                break;
        }
    }
}
