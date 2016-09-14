package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by SunnyJiang on 2016/9/9.
 * 优时好友
 */
@Table(name = "friend")
public class Friend {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    /**
     * 优时好友ID（OpenFireUserName）
     */
    @Column(name = "friendid")
    private String friendid;
    /**
     * 头像地址
     */
    @Column(name = "head_pic")
    private String head_pic;
    /**
     * 昵称（若无昵称，则为OpenFireUserName）
     */
    @Column(name = "nickname")
    private String nickname;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;
    /**
     * 状态（0，未添加 1，已添加，2，待接受）
     */
    @Column(name = "status")
    private int status;
    /**
     * 所属用户ID（OpenFireUserName）
     */
    @Column(name = "userid")
    private String userid;

    public Friend() {
    }

    public Friend(String friendid, String head_pic, String nickname, String remark, String phone, int status, String userid) {
        this.friendid = friendid;
        this.head_pic = head_pic;
        this.nickname = nickname;
        this.remark = remark;
        this.phone = phone;
        this.status = status;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", friendid='" + friendid + '\'' +
                ", head_pic='" + head_pic + '\'' +
                ", nickname='" + nickname + '\'' +
                ", remark='" + remark + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", userid='" + userid + '\'' +
                '}';
    }
}
