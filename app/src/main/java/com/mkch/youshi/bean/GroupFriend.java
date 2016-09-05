package com.mkch.youshi.bean;

/**
 * Created by SunnyJiang on 2016/9/5.
 */
public class GroupFriend {
    private String headpic;//头像
    private String nickname;//昵称

    public GroupFriend() {
    }

    public GroupFriend(String headpic, String nickname) {
        this.headpic = headpic;
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
