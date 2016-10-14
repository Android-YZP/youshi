package com.mkch.youshi.adapter;

import com.mkch.youshi.Hoder.MyBaseHolder;
import com.mkch.youshi.Hoder.PersonalCalHoder;
import com.mkch.youshi.model.Schedule;

import java.util.ArrayList;

/**
 * Created by Smith on 2016/10/12.
 */

public class PersonalCalAdapter extends MyBaseAdapter<Schedule> {

    public PersonalCalAdapter(ArrayList<Schedule> data) {
        super(data);
    }

    @Override
    public MyBaseHolder<Schedule> gethoder() {
        return new PersonalCalHoder();
    }

}
