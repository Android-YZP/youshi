package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by ZJ on 2016/9/23.
 * 手机联系人
 */
@Table(name = "PhoneContact")
public class PhoneContact {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    /**
     * 优时好友ID（OpenFireUserName）
     */
    @Column(name = "contactID")
    private String contactID;
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
     * 是否在新朋友显示，默认为0
     */
    @Column(name = "showinnewfriend")
    private int showinnewfriend;
    /**
     * 所属用户ID（OpenFireUserName）
     */
    @Column(name = "userid")
    private String userid;

    public PhoneContact() {
    }

    public PhoneContact(String contactID, String head_pic, String nickname, String phone, int status, String userid) {
        this.contactID = contactID;
        this.head_pic = head_pic;
        this.nickname = nickname;
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

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String friendid) {
        this.contactID = friendid;
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

    public int getShowinnewfriend() {
        return showinnewfriend;
    }

    public void setShowinnewfriend(int showinnewfriend) {
        this.showinnewfriend = showinnewfriend;
    }

    @Override
    public String toString() {
        return "PhoneContact{" +
                "id=" + id +
                ", contactID='" + contactID + '\'' +
                ", head_pic='" + head_pic + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", showinnewfriend=" + showinnewfriend +
                ", userid='" + userid + '\'' +
                '}';
    }
}
