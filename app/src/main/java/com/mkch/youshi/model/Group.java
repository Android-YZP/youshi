package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by ZJ on 2016/10/31.
 * 群聊
 */
@Table(name = "group")
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

    public Group() {
    }

    public Group(String groupID, String groupName, String groupHead) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.groupHead = groupHead;
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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupID='" + groupID + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupHead='" + groupHead + '\'' +
                '}';
    }
}
