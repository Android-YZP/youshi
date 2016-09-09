package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Smith on 2016/9/9.
 */
@Table(name="schreport")
public class schreport {
    /**
     * 主键自增长ID
     */
    @Column(name = "id",isId = true,autoGen = true)//主键自增长ID
    private int id;

    /**
     * 日程表的外键ID
     */
    @Column(name = "sid")
    private int sid;
    /**
     * 在friend表中可以查询相应信息
     */
    @Column(name = "friendid")
    private int friendid;

    @Override
    public String toString() {
        return "schreport{" +
                "id=" + id +
                ", sid=" + sid +
                ", friendid=" + friendid +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getFriendid() {
        return friendid;
    }

    public void setFriendid(int friendid) {
        this.friendid = friendid;
    }
}
