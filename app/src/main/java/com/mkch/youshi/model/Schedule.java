package com.mkch.youshi.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Smith on 2016/9/9.
 */
@Table(name="schedule")
public class Schedule {
    /**
     * 主键自增长ID
     */
    @Column(name = "id",isId = true,autoGen = true)//主键自增长ID
    private int id;
    /**
     * 服务端编号
     */
    @Column(name = "serverid")
    private int serverid;
    /**
     * 日程类型
     */
    @Column(name = "type")
    private int type;
    /**
     * 主题
     */
    @Column(name = "title")
    private String title;
    /**
     * 标签
     */
    @Column(name = "label")
    private int label;
    /**
     * 地址
     */
    @Column(name = "address")
    private String address;
    /**
     * 经度
     */
    @Column(name = "longitude")
    private String longitude;
    /**
     * 纬度
     */
    @Column(name = "latitude")
    private String latitude;
    /**
     * 提前提醒
     */
    @Column(name = "ahead_warn")
    private int ahead_warn;
    /**
     * 同步状态,0未同步,1已同步
     */
    @Column(name = "syc_status")
    private int syc_status;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 是否是全天
     */
    @Column(name = "is_one_day")
    private boolean is_one_day;
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
     * 总时间
     */
    @Column(name = "total_time")
    private String total_time;
    /**
     * 一周几次
     */
    @Column(name = "times_of_week")
    private int times_of_week;
    /**
     * 周集合
     */
    @Column(name = "which_week")
    private String which_week;
    /**
     * 单次时长
     */
    @Column(name = "single_duration")
    private String single_duration;
    /**
     * 用户ID
     */
    @Column(name = "userid")
    private String userid;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", serverid=" + serverid +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", label=" + label +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", ahead_warn=" + ahead_warn +
                ", syc_status=" + syc_status +
                ", remark='" + remark + '\'' +
                ", is_one_day=" + is_one_day +
                ", begin_time='" + begin_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", total_time='" + total_time + '\'' +
                ", times_of_week=" + times_of_week +
                ", which_week='" + which_week + '\'' +
                ", single_duration='" + single_duration + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getAhead_warn() {
        return ahead_warn;
    }

    public void setAhead_warn(int ahead_warn) {
        this.ahead_warn = ahead_warn;
    }

    public int getSyc_status() {
        return syc_status;
    }

    public void setSyc_status(int syc_status) {
        this.syc_status = syc_status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean is_one_day() {
        return is_one_day;
    }

    public void setIs_one_day(boolean is_one_day) {
        this.is_one_day = is_one_day;
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

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public int getTimes_of_week() {
        return times_of_week;
    }

    public void setTimes_of_week(int times_of_week) {
        this.times_of_week = times_of_week;
    }

    public String getWhich_week() {
        return which_week;
    }

    public void setWhich_week(String which_week) {
        this.which_week = which_week;
    }

    public String getSingle_duration() {
        return single_duration;
    }

    public void setSingle_duration(String single_duration) {
        this.single_duration = single_duration;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
