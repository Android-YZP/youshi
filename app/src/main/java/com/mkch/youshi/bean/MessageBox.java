package com.mkch.youshi.bean;

/**
 * Created by SunnyJiang on 2016/8/31.
 * 首页-消息盒子-列表每一项
 */
public class MessageBox {
    private String boxLogo;//消息盒子logo图
    private String title;//群名称/用户名称/标题
    private String info;//简介/最新的消息
    private int nums;//消息数
    private String datetime;//时间

    public MessageBox(String boxLogo, String title, String info, int nums, String datetime) {
        this.boxLogo = boxLogo;
        this.title = title;
        this.info = info;
        this.nums = nums;
        this.datetime = datetime;
    }

    public String getBoxLogo() {
        return boxLogo;
    }

    public void setBoxLogo(String boxLogo) {
        this.boxLogo = boxLogo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
