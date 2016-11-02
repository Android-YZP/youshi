package com.mkch.youshi.bean;

import com.tencent.TIMGroupMemberRoleType;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by ZJ on 2016/11/1.
 * 群聊成员
 */
@Table(name = "group_friend")
public class GroupFriend {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    //群成员头像（memberHead）
    @Column(name = "member_head")
    private String memberHead;//头像
    //群成员昵称（memberName）
    @Column(name = "member_name")
    private String memberName;//昵称
    //群成员名片（memberCard）
    @Column(name = "member_card")
    private String memberCard;//名片
    //群成员角色（memberRole）
    @Column(name = "member_role")
    private TIMGroupMemberRoleType memberRole;//角色
    public static final int MEMBER_ROLE_OWNER = 1;//群主
    public static final int MEMBER_ROLE_NORMAL = 2;//普通成员

    public GroupFriend() {
    }

    public GroupFriend(String memberName,String memberCard, String memberHead, TIMGroupMemberRoleType memberRole) {
        this.memberName = memberName;
        this.memberCard = memberCard;
        this.memberHead = memberHead;
        this.memberRole = memberRole;
    }

    public String getMemberHead() {
        return memberHead;
    }

    public void setMemberHead(String memberHead) {
        this.memberHead = memberHead;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCard() {
        return memberCard;
    }

    public void setMemberCard(String memberCard) {
        this.memberCard = memberCard;
    }

    public TIMGroupMemberRoleType getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(TIMGroupMemberRoleType memberRole) {
        this.memberRole = memberRole;
    }

    @Override
    public String toString() {
        return "GroupFriend{" +
                "id=" + id +
                ", memberHead='" + memberHead + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberCard='" + memberCard + '\'' +
                ", memberRole=" + memberRole +
                '}';
    }
}
