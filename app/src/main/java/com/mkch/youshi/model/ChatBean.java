package com.mkch.youshi.model;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by SunnyJiang on 2016/8/24.
 */
@Table(name = "chat")
public class ChatBean {
    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "username")
    private String username;//消息的发送者
    @Column(name = "content")
    private String content;//消息的内容
    @Column(name = "type")
    private int type;//消息的类型，0:接收 1:发送
    public static final int MESSAGE_TYPE_IN = 0;
    public static final int MESSAGE_TYPE_OUT = 1;
    @Column(name = "date")
    private String date;//消息的日期
    //消息的模式，文本、录音、图片等
    @Column(name = "msgmodel")
    private int msgModel;
    public static final int MESSAGE_MODEL_TEXT = 1;
    public static final int MESSAGE_MODEL_SOUND = 2;
    public static final int MESSAGE_MODEL_PIC = 3;
    @Column(name = "duration")
    private int duration;//语音消息的时长
    @Column(name = "filename")
    private String fileName;//文件的名称
    //消息状态,成功、失败、等待
    @Column(name = "status")
    private int status;
    public static int MESSAGE_STATUS_SUCCESS = 1;
    public static int MESSAGE_STATUS_FAIL = 2;
    public static int MESSAGE_STATUS_WAIT = 3;

    //所属消息盒子
    @Column(name = "msgboxid")
    private int msgboxid;//外键表id

    private MessageBox getMessageBox(DbManager db) throws Exception {
        return db.findById(MessageBox.class, msgboxid);
    }

    public ChatBean() {
    }

    public ChatBean(String _openfirename, String _msg, int messageTypeOut, String now) {
        this.username = _openfirename;
        this.content = _msg;
        this.type = messageTypeOut;
        this.date = now;
        this.msgModel = MESSAGE_MODEL_TEXT;
    }

    public ChatBean(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public ChatBean(String username, String content, int type) {
        this.username = username;
        this.content = content;
        this.type = type;
    }

    /**
     * 发送文本
     *
     * @param username 发送者
     * @param content  内容
     * @param type     消息接收or发送
     * @param date     时间
     */
    public ChatBean(String username, String content, int type, String date, int msgboxid) {
        this.username = username;
        this.content = content;
        this.type = type;
        this.date = date;
        this.msgModel = MESSAGE_MODEL_TEXT;
        this.msgboxid = msgboxid;
    }

    /**
     * 语音消息
     *
     * @param username 发送者
     * @param date     时间
     * @param type     消息接收or发送
     * @param duration 时长
     * @param fileName 文件名
     */
    public ChatBean(String username, String date, int type, int duration, String fileName, String content) {
        this.username = username;
        this.date = date;
        this.type = type;
        this.duration = duration;
        this.fileName = fileName;
        this.content = (duration / 1000) + "\'" + (duration % 1000) + "\"语音消息";
        this.msgModel = MESSAGE_MODEL_SOUND;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMsgModel() {
        return msgModel;
    }

    public void setMsgModel(int msgModel) {
        this.msgModel = msgModel;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getMsgboxid() {
        return msgboxid;
    }

    public void setMsgboxid(int msgboxid) {
        this.msgboxid = msgboxid;
    }

    @Override
    public String toString() {
        return "ChatBean{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", date='" + date + '\'' +
                ", msgModel=" + msgModel +
                ", duration=" + duration +
                ", fileName='" + fileName + '\'' +
                ", status=" + status +
                ", msgboxid=" + msgboxid +
                '}';
    }
}
