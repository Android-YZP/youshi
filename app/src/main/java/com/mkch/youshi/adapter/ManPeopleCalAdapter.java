package com.mkch.youshi.adapter;

import com.mkch.youshi.Hoder.ManyPeopleCalHoder;
import com.mkch.youshi.Hoder.MyBaseHolder;
import com.mkch.youshi.Hoder.PersonalCalHoder;
import com.mkch.youshi.model.Schedule;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/10/12.
 */

public abstract class ManPeopleCalAdapter extends MyBaseAdapter<Schedule> {

    public ManPeopleCalAdapter(ArrayList<Schedule> data) {
        super(data);
    }

    @Override
    public MyBaseHolder<Schedule> gethoder() {
        return new ManyPeopleCalHoder() {
            @Override
            public void accept() {
                notifyDataSetChanged();
                accept2Net();

            }

            @Override
            public void refuse() {
                notifyDataSetChanged();
                refuse2Net();
            }
        };
    }



    public abstract void accept2Net();

    public abstract void refuse2Net();


}
