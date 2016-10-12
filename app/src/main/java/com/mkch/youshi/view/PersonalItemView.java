package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.util.UIUtils;

/**
 * Created by Smith on 2016/10/12.
 */

public class PersonalItemView extends FrameLayout{


    public PersonalItemView(Context context) {
        super(context);
        initView();
    }

    public PersonalItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonalItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        View view = UIUtils.inflate(R.layout.item_list_personal_calendar);
        CheckBox _isComplete = (CheckBox) view.findViewById(R.id.cb_complete);
        TextView _tvTheme = (TextView) view.findViewById(R.id.tv_lv_theme);
        ProgressBar _pbProgress = (ProgressBar) view.findViewById(R.id.pb_progress);
        MyProgress _mbProgress = (MyProgress) view.findViewById(R.id.mp_progress);
        TextView _tvTimeStop = (TextView) view.findViewById(R.id.tv_time_stop);
        TextView _tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        TextView _tvTimeAndStopTime = (TextView) view.findViewById(R.id.tv_time_and_stop_time);

        addView(view);
    }


}
