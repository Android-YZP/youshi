package com.mkch.youshi.Hoder;

import android.view.View;

import com.mkch.youshi.R;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.ManyPeopleItemView;
import com.mkch.youshi.view.PersonalItemView;

/**
 * Created by Smith on 2016/10/12.
 */

public abstract class ManyPeopleCalHoder extends MyBaseHolder<Schedule> {

    private ManyPeopleItemView mManyPeoItemView;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.item_many_peo_cal_layout);
        mManyPeoItemView = (ManyPeopleItemView) view.findViewById(R.id.mpiv_item_cal);
        return view;
    }

    @Override
    public void refreshView(final Schedule data) {//设置数据
//        mManyPeoItemView.setType(data.getSch_status());
        mManyPeoItemView.setType(data.getSch_status());
        mManyPeoItemView.setTheme(data.getTitle());
        mManyPeoItemView.setStopTime(data.getEnd_time());

        //接受
        mManyPeoItemView.getmBtnManyPeopleAccept().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setSch_status(2);
                accept();
            }
        });

        //拒绝
        mManyPeoItemView.getmBtnManyPeopleRefuse().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setSch_status(3);
                refuse();
            }
        });

    }

    public abstract void accept();

    public abstract void refuse();

}
