package com.mkch.youshi.bean;

/**
 * Created by Smith on 2016/9/7.
 */
public class ManyPeopleEvenBean {
    public final static int MANY_PEOPLE_CHOOSE = 1;//选择状态
    public final static int MANY_PEOPLE_REFUSE = 2;//拒绝状态
    public final static int MANY_PEOPLE_ACCEPT = 3;//接受状态
    public final static int MANY_PEOPLE_SPONSOR= 4;//自己是发起者的状态
    private int state;//状态
    private String Theme;//主题
    private String Sponsor;//发起者
    private String StopTime;//截止时间
    private String CreationTime;//创建时间
    private String label;//标签
    private String location;//位置
    private Boolean isComplete;//是否完成
    private String Participants;//参与人
    private String submission;//报送人

    @Override
    public String toString() {
        return "ManyPeopleEvenBean{" +
                "state=" + state +
                ", Theme='" + Theme + '\'' +
                ", Sponsor='" + Sponsor + '\'' +
                ", StopTime='" + StopTime + '\'' +
                ", CreationTime='" + CreationTime + '\'' +
                ", label='" + label + '\'' +
                ", location='" + location + '\'' +
                ", isComplete=" + isComplete +
                ", Participants='" + Participants + '\'' +
                ", submission='" + submission + '\'' +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTheme() {
        return Theme;
    }

    public void setTheme(String theme) {
        Theme = theme;
    }

    public String getSponsor() {
        return Sponsor;
    }

    public void setSponsor(String sponsor) {
        Sponsor = sponsor;
    }

    public String getStopTime() {
        return StopTime;
    }

    public void setStopTime(String stopTime) {
        StopTime = stopTime;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
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

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
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
}
