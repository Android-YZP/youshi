package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by ZJ on 2016/10/31.
 * 群聊列表
 */
@Table(name = "group_chat")
public class Group {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    //群聊ID（groupID）
    @Column(name = "group_id")
    private String groupID;
    //群聊名称（groupName）
    @Column(name = "group_name")
    private String groupName;
    //群聊头像（groupHead）
    @Column(name = "group_head")
    private String groupHead;
    //群聊公告（groupNotification）
    @Column(name = "group_notification")
    private String groupNotification;
    //用户ID（userId）
    @Column(name = "user_id")
    private String userId;

    public Group() {
    }

    public Group(String groupID, String groupName, String groupHead, String groupNotification, String userId) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.groupHead = groupHead;
        this.groupNotification = groupNotification;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupHead() {
        return groupHead;
    }

    public void setGroupHead(String groupHead) {
        this.groupHead = groupHead;
    }

    public String getGroupNotification() {
        return groupNotification;
    }

    public void setGroupNotification(String groupNotification) {
        this.groupNotification = groupNotification;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupID='" + groupID + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupHead='" + groupHead + '\'' +
                ", groupNotification='" + groupNotification + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
