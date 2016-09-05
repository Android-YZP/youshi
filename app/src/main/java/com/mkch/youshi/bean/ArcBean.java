package com.mkch.youshi.bean;

/**
 * Created by SunnyJiang on 2016/8/11.
 */
public class ArcBean {
    private String starttime;
    private String endtime;
    private int notetype;//标签类型
    private int orderid;//排序编号
    private int overlap_line;//重叠的行

    public ArcBean(String starttime, String endtime, int notetype, int orderid, int overlap_line) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.notetype = notetype;
        this.orderid = orderid;
        this.overlap_line = overlap_line;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public int getNotetype() {
        return notetype;
    }

    public void setNotetype(int notetype) {
        this.notetype = notetype;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getOverlap_line() {
        return overlap_line;
    }

    public void setOverlap_line(int overlap_line) {
        this.overlap_line = overlap_line;
    }
}
