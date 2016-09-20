package com.mkch.youshi.model;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

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
    @Column(name = "type")
    private int type;//消息盒子的类型：单聊1、群聊2
    public static int MB_TYPE_CHAT = 1;
    public static int MB_TYPE_MUL_CHAT = 2;
    @Column(name = "jid")
    private String jid;//所属JID，跟type有关，若是单聊，如：JID=openfirename@yoshi.maikejia.com

    public List<ChatBean> getChatBeans(DbManager db) throws Exception{
        return db.selector(ChatBean.class).where("msgboxid","=",this.id).findAll();
    }

    public MessageBox() {
    }

    public MessageBox(String boxLogo, String title, String info, int nums, String lasttime, int istop, int type, String jid) {
        this.boxLogo = boxLogo;
        this.title = title;
        this.info = info;
        this.nums = nums;
        this.lasttime = lasttime;
        this.istop = istop;
        this.type = type;
        this.jid = jid;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
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
                ", type=" + type +
                ", jid='" + jid + '\'' +
                '}';
    }
}
