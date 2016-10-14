package com.mkch.youshi.bean;

/**
 * Created by Smith on 2016/9/6.
 */
public class PersonalEventBean {
    public final static int PERSONAL_EVENT = 0;
    public final static int PERSONAL_AFFAIR = 1;
    public final static int PERSONAL_HABIT = 2;
    private int kind;//种类
    private int times;//一周的次数
    private int CompleteTimes;//一周的完成次数
    private int progress;//进度
    private String theme;//主题
    private String label;//标签
    private String location;//位置
    private Boolean isAllday;//全天
    private Boolean isComplete;//是否完成
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String Participants;//参与人
    private String submission;//报送人
    private String Remind;//提前提醒
    private String timeBucket;//时间段
    private String week;//周
    private String cycle;//周期
    private String time;//时长

    public int getCompleteTimes() {
        return CompleteTimes;
    }

    public void setCompleteTimes(int completeTimes) {
        CompleteTimes = completeTimes;
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

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getAllday() {
        return isAllday;
    }

    public void setAllday(Boolean allday) {
        isAllday = allday;
    }

    public String getParticipants() {
        return Participants;
    }

    public void setParticipants(String participants) {
        Participants = participants;
    }

    public String getSubmission() {
        return submission;
    }

    public void setSubmission(String submission) {
        this.submission = submission;
    }

    public String getRemind() {
        return Remind;
    }

    public void setRemind(String remind) {
        Remind = remind;
    }

    public String getTimeBucket() {
        return timeBucket;
    }

    public void setTimeBucket(String timeBucket) {
        this.timeBucket = timeBucket;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
