package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by SunnyJiang on 2016/8/31.
 * 首页-消息盒子-列表每一项
 */
@Table(name = "messagebox")
public class MessageBox {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    @Column(name = "boxlogo")
    private String boxLogo;//消息盒子logo图
    @Column(name = "title")
    private String title;//群名称/用户名称/标题
    @Column(name = "info")
    private String info;//简介/最新的消息
    @Column(name = "nums")
    private int nums;//消息数
    @Column(name = "lasttime")
    private String lasttime;//时间
    @Column(name = "istop")
    private int istop;//是否置顶

    public MessageBox(String boxLogo, String title, String info, int nums, String lasttime) {
        this.boxLogo = boxLogo;
        this.title = title;
        this.info = info;
        this.nums = nums;
        this.lasttime = lasttime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public int getIstop() {
        return istop;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }

    @Override
    public String toString() {
        return "MessageBox{" +
                "id=" + id +
                ", boxLogo='" + boxLogo + '\'' +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", nums=" + nums +
                ", lasttime='" + lasttime + '\'' +
                ", istop=" + istop +
                '}';
    }
}
