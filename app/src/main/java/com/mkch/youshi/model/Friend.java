package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by SunnyJiang on 2016/9/9.
 * 优时好友
 */
@Table(name = "friend")
public class Friend implements Comparable<Friend> {
    @Column(name = "id", isId = true, autoGen = true)
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
     * 是否在新朋友显示，默认为0
     */
    @Column(name = "showinnewfriend")
    private int showinnewfriend;
    /**
     * 所属用户ID（OpenFireUserName）
     */
    @Column(name = "userid")
    private String userid;

    /**
     * 拼音
     */
    @Column(name = "pinyin")
    private String pinyin;

    /**
     * 首字母
     */
    @Column(name = "first_char")
    private char firstChar;
    /**
     * 优时号
     */
    @Column(name = "youshi_number")
    private String youshi_number;
    /**
     * 地区
     */
    @Column(name = "place")
    private String place;
    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;
    /**
     * 个性签名
     */
    @Column(name = "sign")
    private String sign;

    public Friend() {
    }

    public Friend(String friendid, String head_pic, String nickname, String remark, String phone,String youshi_number, String place,String sex,String sign,int status, String userid) {
        this.friendid = friendid;
        this.head_pic = head_pic;
        this.nickname = nickname;
        this.remark = remark;
        this.phone = phone;
        this.youshi_number = youshi_number;
        this.place = place;
        this.sex = sex;
        this.sign = sign;
        this.status = status;
        this.userid = userid;
    }


    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
        String first = pinyin.substring(0, 1);
        if (first.matches("[A-Za-z]")) {
            firstChar = first.toUpperCase().charAt(0);
        } else {
            firstChar = '#';
        }
    }

    public char getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(char firstChar) {
        this.firstChar = firstChar;
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

    public int getShowinnewfriend() {
        return showinnewfriend;
    }

    public void setShowinnewfriend(int showinnewfriend) {
        this.showinnewfriend = showinnewfriend;
    }

    public String getYoushi_number() {
        return youshi_number;
    }

    public void setYoushi_number(String youshi_number) {
        this.youshi_number = youshi_number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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
                ", showinnewfriend=" + showinnewfriend +
                ", userid='" + userid + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", firstChar=" + firstChar +
                '}';
    }

    @Override
    public int compareTo(Friend another) {
        if (another != null && another.getPinyin() != null && !another.getPinyin().equals("")) {
            if (pinyin != null && !pinyin.equals("")) {
                return this.pinyin.compareTo(another.getPinyin());
            }
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Friend) {
            return this.id == ((Friend) o).getId();
        } else {
            return super.equals(o);
        }
    }
}
