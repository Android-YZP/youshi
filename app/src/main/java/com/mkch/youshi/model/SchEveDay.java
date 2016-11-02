package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Smith on 2016/11/1.
 */
@Table(name = "scheveday")
public class SchEveDay {
    /**
     * 主键自增长ID
     */
    @Column(name = "id", isId = true, autoGen = true)//主键自增长ID
    private int id;
    /**
     * 日期
     */
    @Column(name = "date")
    private String date;
    /**
     * 开始时间
     */
    @Column(name = "begin_time")
    private String begin_time;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private String end_time;

    /**
     * 提醒时间
     */
    @Column(name = "warning_time")
    private String warning_time;

    /**
     * 日程状态.是否开始(0未开始,1已经开始)
     */
    @Column(name = "status")
    private int status;

    /**
     * 外键ID,所属日程.
     */
    @Column(name = "sid")
    private int sid;

    @Override
    public String toString() {
        return "schtime{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", begin_time='" + begin_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", warning_time='" + warning_time + '\'' +
                ", status=" + status +
                ", sid=" + sid +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getWarning_time() {
        return warning_time;
    }

    public void setWarning_time(String warning_time) {
        this.warning_time = warning_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

}
