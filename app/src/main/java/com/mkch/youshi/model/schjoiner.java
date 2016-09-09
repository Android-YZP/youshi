package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Smith on 2016/9/9.
 */
@Table(name="schjoiner")
public class schjoiner {
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
     * 优时好友ID（服务端/本地数据库）
     */
    @Column(name = "joiner_id")
    private int joiner_id;

    /**
     * (是否接受（0,未操作  1，接受  2,拒绝）)
     */
    @Column(name = "isaccept")
    private int isaccept;

    /**
     * 操作时间
     */
    @Column(name = "op_time")
    private String op_time;


    @Override
    public String toString() {
        return "schjoiner{" +
                "id=" + id +
                ", sid=" + sid +
                ", joiner_id=" + joiner_id +
                ", isaccept=" + isaccept +
                ", op_time='" + op_time + '\'' +
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

    public int getJoiner_id() {
        return joiner_id;
    }

    public void setJoiner_id(int joiner_id) {
        this.joiner_id = joiner_id;
    }

    public int getIsaccept() {
        return isaccept;
    }

    public void setIsaccept(int isaccept) {
        this.isaccept = isaccept;
    }

    public String getOp_time() {
        return op_time;
    }

    public void setOp_time(String op_time) {
        this.op_time = op_time;
    }
}
