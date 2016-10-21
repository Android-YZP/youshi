package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.ManyPeopleEvenBean;
import com.mkch.youshi.util.UIUtils;

/**
 * Created by Smith on 2016/10/13.
 */

public class ManyPeopleItemView extends FrameLayout {
    public final int MANY_PEOPLE_SPONSOR = 0;//自己是发起者的状态
    public final int MANY_PEOPLE_CHOOSE = 1;//待接受状态
    public final int MANY_PEOPLE_ACCEPT = 2;//接受状态
    public final int MANY_PEOPLE_REFUSE = 3;//拒绝状态
    private TextView mBtnManyPeopleAccept;
    private TextView mBtnManyPeopleRefuse;
    private TextView mManyPeopleCreationTime;
    private TextView mTvManyPeopleRefuse;
    private TextView mTvManyPeopleAccept;
    private TextView mManyPeopleSponsor;
    private TextView mManyPeopleStopTime;
    private TextView mManyPeopleTheme;

    public ManyPeopleItemView(Context context) {
        super(context);
        initView();
    }

    public ManyPeopleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ManyPeopleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = UIUtils.inflate(R.layout.item_list_many_people_calendar);
        mManyPeopleTheme = (TextView) view.findViewById(R.id.tv_lv_many_people_theme);
        mManyPeopleStopTime = (TextView) view.findViewById(R.id.tv_lv_many_people_time_stop);
        mManyPeopleSponsor = (TextView) view.findViewById(R.id.tv_lv_many_people_sponsor);
        mTvManyPeopleAccept = (TextView) view.findViewById(R.id.tv_lv_many_people_accept);
        mTvManyPeopleRefuse = (TextView) view.findViewById(R.id.tv_lv_many_people_refuse);
        mManyPeopleCreationTime = (TextView) view.findViewById(R.id.tv_lv_many_people_creation_time);
        mBtnManyPeopleAccept = (TextView) view.findViewById(R.id.btn_lv_many_people_accept);
        mBtnManyPeopleRefuse = (TextView) view.findViewById(R.id.btn_lv_many_people_refuse);
        addView(view);
    }

    /**
     * 根据状态初始化布局
     *
     * @param type 状态
     */
    public void setType(int type) {
        if (type == MANY_PEOPLE_SPONSOR) {//自己是发起者
            mBtnManyPeopleAccept.setVisibility(View.GONE);
            mBtnManyPeopleRefuse.setVisibility(View.GONE);
            mTvManyPeopleAccept.setVisibility(View.GONE);
            mTvManyPeopleRefuse.setVisibility(View.GONE);
        } else if (type == MANY_PEOPLE_CHOOSE) {//选择状态
            mBtnManyPeopleAccept.setVisibility(View.VISIBLE);
            mBtnManyPeopleRefuse.setVisibility(View.VISIBLE);
            mTvManyPeopleAccept.setVisibility(View.GONE);
            mTvManyPeopleRefuse.setVisibility(View.GONE);
        } else if (type == MANY_PEOPLE_ACCEPT) {//接受
            mBtnManyPeopleAccept.setVisibility(View.GONE);
            mBtnManyPeopleRefuse.setVisibility(View.GONE);
            mTvManyPeopleAccept.setVisibility(View.VISIBLE);
            mTvManyPeopleRefuse.setVisibility(View.GONE);
//            mManyPeopleSponsor.setVisibility(View.GONE);
        } else if (type == MANY_PEOPLE_REFUSE) {//拒绝
            mBtnManyPeopleAccept.setVisibility(View.GONE);
            mBtnManyPeopleRefuse.setVisibility(View.GONE);
            mTvManyPeopleAccept.setVisibility(View.GONE);
            mTvManyPeopleRefuse.setVisibility(View.VISIBLE);
//            mManyPeopleSponsor.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     */
    public void setTheme(String theme) {
        mManyPeopleTheme.setText(theme);
    }

    /**
     * 设置结束时间
     */
    public void setStopTime(String stopTime) {
        mManyPeopleStopTime.setText(stopTime);
    }

    public TextView getmBtnManyPeopleAccept() {
        return mBtnManyPeopleAccept;
    }

    public TextView getmBtnManyPeopleRefuse() {
        return mBtnManyPeopleRefuse;
    }

}
