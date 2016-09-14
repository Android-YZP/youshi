package com.mkch.youshi.bean;

/**
 * 手机联系人信息
 *
 * @author JLJ
 */
public class ContactEntity implements Comparable<ContactEntity> {
    //联系人名称
    private String name;
    //联系人号码
    private String number;
    private int id;
    private boolean add;
    private String pinyin;
    private char firstChar;
    private String openFireUserName;

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

    public char getFirstChar() {
        return firstChar;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public String getOpenFireUserName() {
        return openFireUserName;
    }

    public void setOpenFireUserName(String openFireUsrName) {
        this.openFireUserName = openFireUsrName;
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
        return this.pinyin.compareTo(another.getPinyin());
    }
}
