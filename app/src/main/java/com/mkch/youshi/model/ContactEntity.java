package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 手机联系人信息
 */
@Table(name = "phone_contact")
public class ContactEntity implements Comparable<ContactEntity> {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    //联系人名称
    @Column(name = "nickname")
    private String name;
    //联系人拼音
    @Column(name = "pinyin")
    private String pinyin;
    //联系人号码
    @Column(name = "phone")
    private String number;
    //优时好友ID（OpenFireUserName）
    @Column(name = "contact_id")
    private String contactID;
    //头像地址
    @Column(name = "head_pic")
    private String HeadPic;//头像
    //状态（0，未添加 1，已添加，2，待接受）
    @Column(name = "status")
    private int status;
    //是否在新朋友显示，默认为0
    @Column(name = "showinnewfriend")
    private int showinnewfriend;
    //所属用户ID（OpenFireUserName）
    @Column(name = "user_id")
    private String userID;
    private boolean IsRegister;
    private char firstChar;

    public ContactEntity() {
    }

    public ContactEntity(String contactID, String HeadPic, String name, String pinyin, String number, int status, String userID) {
        this.contactID = contactID;
        this.HeadPic = HeadPic;
        this.name = name;
        this.pinyin = pinyin;
        this.number = number;
        this.status = status;
        this.userID = userID;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String friendid) {
        this.contactID = friendid;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String headPic) {
        HeadPic = headPic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getShowinnewfriend() {
        return showinnewfriend;
    }

    public void setShowinnewfriend(int showinnewfriend) {
        this.showinnewfriend = showinnewfriend;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public char getFirstChar() {
        return firstChar;
    }

    public boolean isRegister() {
        return IsRegister;
    }

    public void setRegister(boolean register) {
        IsRegister = register;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContactEntity) {
            return this.id == ((ContactEntity) o).getId();
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int compareTo(ContactEntity another) {
        if (another!=null&&another.getPinyin()!=null&&!another.getPinyin().equals("")){
            if (pinyin!=null&&!pinyin.equals("")){
                return this.pinyin.compareTo(another.getPinyin());
            }
        }
        return 1;
    }
}
