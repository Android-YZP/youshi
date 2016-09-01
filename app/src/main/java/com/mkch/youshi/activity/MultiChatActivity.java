package com.mkch.youshi.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mkch.youshi.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_multi_chat)
public class MultiChatActivity extends BaseActivity {
    @ViewInject(R.id.iv_multi_chat_topbar_back)
    private ImageView mIvBack;//返回按钮

    @ViewInject(R.id.tv_multi_chat_topbar_title)
    private TextView mTvTitle;//标题

    @ViewInject(R.id.iv_multi_chat_topbar_actions)
    private ImageView mIvActions;//其他操作

    private PopupWindow popupWindow;//其他操作弹出框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mTvTitle.setText("群聊");
    }

    @Event({R.id.iv_multi_chat_topbar_back,R.id.iv_multi_chat_topbar_actions})
    private void clickOneButton(View view) {
        switch (view.getId()) {
            case R.id.iv_multi_chat_topbar_back://返回按钮
                this.finish();
                break;
            case R.id.iv_multi_chat_topbar_actions://其他操作
                showPopupWindow(view);
                break;
        }
    }

    /**
     * 显示事件的添加框
     *
     * @param view
     */
    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.layout_multi_chat_pop_window, null);
        LinearLayout _line_Background = (LinearLayout) contentView.findViewById(R.id.line_background);
        _line_Background.getBackground().setAlpha(180);
        LinearLayout _line_event = (LinearLayout) contentView.findViewById(R.id.line_chat_pop_event);
        LinearLayout _line_edit = (LinearLayout) contentView.findViewById(R.id.line_chat_pop_edit);
        LinearLayout _line_group = (LinearLayout) contentView.findViewById(R.id.line_chat_pop_group);


        //添加事件
        _line_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiChatActivity.this, AddPersonalActivity.class));
                popupWindow.dismiss();
            }
        });

        //编辑事件
        _line_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiChatActivity.this, AddPersonalActivity.class));
                popupWindow.dismiss();
            }
        });

        //群组
        _line_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MultiChatActivity.this, AddPersonalActivity.class));
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        ColorDrawable dw = new ColorDrawable(0000);
        popupWindow.setBackgroundDrawable(dw);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }

}
