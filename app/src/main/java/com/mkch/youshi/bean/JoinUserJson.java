package com.mkch.youshi.bean;

/**
 * 注册请求json
 * Created by SunnyJiang on 2016/9/7.
 */
public class JoinUserJson {
    private User viewModel;

    public JoinUserJson() {
    }

    public JoinUserJson(User viewModel) {
        this.viewModel = viewModel;
    }

    public User getViewModel() {
        return viewModel;
    }

    public void setViewModel(User viewModel) {
        this.viewModel = viewModel;
    }
}
