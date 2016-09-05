package com.mkch.youshi.bean;

/**
 * Created by Smith on 2016/9/5.
 */
public class TimeBucketInfo {
    public String startTime;
    public String endTime;

    @Override
    public String toString() {
        return "TimeBucketInfo{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
