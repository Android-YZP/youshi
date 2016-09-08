package com.mkch.youshi.bean;

/**
 * Created by SunnyJiang on 2016/9/7.
 */
public class LoginUserJson {
    private User viewModel;

    public LoginUserJson() {
    }

    public LoginUserJson(User viewModel) {
        this.viewModel = viewModel;
    }

    public User getViewModel() {
        return viewModel;
    }

    public void setViewModel(User viewModel) {
        this.viewModel = viewModel;
    }
}
