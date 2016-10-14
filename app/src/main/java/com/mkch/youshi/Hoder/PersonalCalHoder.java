package com.mkch.youshi.Hoder;

import android.view.View;

import com.mkch.youshi.R;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.PersonalItemView;

/**
 * Created by Smith on 2016/10/12.
 */

public class PersonalCalHoder extends MyBaseHolder<Schedule> {

    private PersonalItemView mPerItemView;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.item_per_cal_layout);
        mPerItemView = (PersonalItemView) view.findViewById(R.id.piv_item_cal);
        return view;
    }

    @Override
    public void refreshView(Schedule data) {//设置数据
        int type = data.getType();
        mPerItemView.setType(type,data.getTimes_of_week());
        mPerItemView.setTheme(data.getTitle());
        mPerItemView.setPerAffProgress(data.getAffair_progress());
        mPerItemView.setPerEveProgress(data.getEnd_time());
        if (type == 1){//表示是事务
            mPerItemView.setPerAffStopTime(data.getEnd_time(),data.getTotal_time());
        }else if (type == 2){//习惯
            mPerItemView.setPerHabProgress(data.getHabit_complete_times(),data.getTimes_of_week());
        }

    }

}
