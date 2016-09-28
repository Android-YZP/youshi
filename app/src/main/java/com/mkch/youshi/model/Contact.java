package com.mkch.youshi.model;

import org.xutils.db.annotation.Table;

/**
 * 优时联系人
 */
@Table(name = "friend")
public class Contact implements Comparable<Contact> {
    private int id;
    private String name;
    private String openFireName;
    private String url;
    private String pinyin;
    private char firstChar;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getFirstChar() {
        return firstChar;
    }

    public String getOpenFireName() {
        return openFireName;
    }

    public void setOpenFireName(String openFireName) {
        this.openFireName = openFireName;
    }

    @Override
    public int compareTo(Contact another) {
        return this.pinyin.compareTo(another.getPinyin());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Contact) {
            return this.id == ((Contact) o).getId();
        } else {
            return super.equals(o);
        }
    }
}
