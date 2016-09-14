package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkch.youshi.R;


/**
 * 自定义控件-切换免打扰时间用
 */
public class DisturbTimeTabBarLayout extends LinearLayout {
    //tabbar的回调接口
    public interface IDisturbTimeTabBarCallBackListener {
        public void clickItem(int id);//按了某一项后
    }

    IDisturbTimeTabBarCallBackListener disturbTimeTabBarCallBackListener = null;

    public void setOnItemClickListener(IDisturbTimeTabBarCallBackListener disturbTimeTabBarCallBackListener) {
        this.disturbTimeTabBarCallBackListener = disturbTimeTabBarCallBackListener;
    }

    LinearLayout mLayoutOpen;
    LinearLayout mLayoutNight;
    LinearLayout mLayoutClose;
    LayoutInflater inflater;
    public final static int FLAG_OPEN = 0;
    public final static int FLAG_NIGHT = 1;
    public final static int FLAG_CLOSE = 2;

    public DisturbTimeTabBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_disturb_time, this);
        findView(view);
        initData();
        setListener();
    }

    /**
     * 找到所有layout控件
     *
     * @param view
     */
    private void findView(View view) {
        mLayoutOpen = (LinearLayout) view.findViewById(R.id.index_open_item);
        mLayoutNight = (LinearLayout) view.findViewById(R.id.index_night_item);
        mLayoutClose = (LinearLayout) view.findViewById(R.id.index_close_item);
    }

    /**
     * 初始化每个item的数据
     */
    private void initData() {
        //开启
        TextView _tvOpen = (TextView) mLayoutOpen.findViewById(R.id.tv_disturb_time_item);
        _tvOpen.setText("开启");
        ImageView _ivOpen = (ImageView) mLayoutOpen.findViewById(R.id.iv_disturb_time_item_gou);
        _ivOpen.setImageResource(0);
        //夜间
        TextView _tvNight = (TextView) mLayoutNight.findViewById(R.id.tv_disturb_time_item);
        _tvNight.setText("只在夜间开启");
        ImageView _ivNight = (ImageView) mLayoutNight.findViewById(R.id.iv_disturb_time_item_gou);
        _ivNight.setImageResource(0);
        //关闭
        TextView _tvClose = (TextView) mLayoutClose.findViewById(R.id.tv_disturb_time_item);
        _tvClose.setText("关闭");
        ImageView _ivClose = (ImageView) mLayoutClose.findViewById(R.id.iv_disturb_time_item_gou);
        _ivClose.setImageResource(R.drawable.gou);
    }

    /**
     * 点击事件监听
     */
    private void setListener() {
        mLayoutOpen.setOnClickListener(new MyItemClickListener());
        mLayoutNight.setOnClickListener(new MyItemClickListener());
        mLayoutClose.setOnClickListener(new MyItemClickListener());
    }

    private class MyItemClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.index_open_item:
                    //1-改变图片
                    changeTabBarItems(FLAG_OPEN);
                    break;
                case R.id.index_night_item:
                    changeTabBarItems(FLAG_NIGHT);
                    break;
                case R.id.index_close_item:
                    changeTabBarItems(FLAG_CLOSE);
                    break;
            }
            if (disturbTimeTabBarCallBackListener != null) {
                disturbTimeTabBarCallBackListener.clickItem(view.getId());
            }
        }


    }

    /**
     * 点击某个tabitem，切换以下内容：改变图片;改变文字的颜色;改变layout的背景
     *
     * @param index 索引值
     */
    public void changeTabBarItems(int index) {
        ImageView _ivOpen, _ivNight, _ivClose;
        switch (index) {
            case FLAG_OPEN:
                //开启
                _ivOpen = (ImageView) mLayoutOpen.findViewById(R.id.iv_disturb_time_item_gou);
                _ivOpen.setImageResource(R.drawable.gou);
                //只在夜间开启
                _ivNight = (ImageView) mLayoutNight.findViewById(R.id.iv_disturb_time_item_gou);
                _ivNight.setImageResource(0);
                //关闭
                _ivClose = (ImageView) mLayoutClose.findViewById(R.id.iv_disturb_time_item_gou);
                _ivClose.setImageResource(0);
                break;
            case FLAG_NIGHT:
                //开启
                _ivOpen = (ImageView) mLayoutOpen.findViewById(R.id.iv_disturb_time_item_gou);
                _ivOpen.setImageResource(0);
                //只在夜间开启
                _ivNight = (ImageView) mLayoutNight.findViewById(R.id.iv_disturb_time_item_gou);
                _ivNight.setImageResource(R.drawable.gou);
                //关闭
                _ivClose = (ImageView) mLayoutClose.findViewById(R.id.iv_disturb_time_item_gou);
                _ivClose.setImageResource(0);
                break;
            case FLAG_CLOSE:
                //开启
                _ivOpen = (ImageView) mLayoutOpen.findViewById(R.id.iv_disturb_time_item_gou);
                _ivOpen.setImageResource(0);
                //只在夜间开启
                _ivNight = (ImageView) mLayoutNight.findViewById(R.id.iv_disturb_time_item_gou);
                _ivNight.setImageResource(0);
                //关闭
                _ivClose = (ImageView) mLayoutClose.findViewById(R.id.iv_disturb_time_item_gou);
                _ivClose.setImageResource(R.drawable.gou);
                break;
        }
    }
}
