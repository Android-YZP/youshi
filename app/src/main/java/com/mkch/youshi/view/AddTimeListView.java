package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.mkch.youshi.R;
import com.mkch.youshi.util.UIUtils;

/**
 * Created by Smith on 2016/9/20.
 */
public class AddTimeListView extends ListView {
    public AddTimeListView(Context context) {
        super(context);
        initView();
    }

    public AddTimeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AddTimeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        addFooterView(UIUtils.inflate(R.layout.item_list_choose_time_add_layout));
    }


}
