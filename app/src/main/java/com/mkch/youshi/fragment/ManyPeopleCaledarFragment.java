package com.mkch.youshi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkch.youshi.R;

/**
 * Created by Smith on 2016/9/6.
 */
public class ManyPeopleCaledarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.many_people_caledar_fragment, container, false);
        return view;

    }
}
