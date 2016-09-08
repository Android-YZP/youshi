package com.mkch.youshi.bean;

/**
 * Created by SunnyJiang on 2016/9/7.
 */
public class HttpResultBean {
    private Boolean Success;
    private Boolean NeedVerifyCode;
    private String Message;
    private String ErrorCode;
    private String Datas;
    private int Total;

    public HttpResultBean() {
    }

    public HttpResultBean(Boolean success, Boolean needVerifyCode, String message, String errorCode, String datas, int total) {
        Success = success;
        NeedVerifyCode = needVerifyCode;
        Message = message;
        ErrorCode = errorCode;
        Datas = datas;
        Total = total;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        Success = success;
    }

    public Boolean getNeedVerifyCode() {
        return NeedVerifyCode;
    }

    public void setNeedVerifyCode(Boolean needVerifyCode) {
        NeedVerifyCode = needVerifyCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getDatas() {
        return Datas;
    }

    public void setDatas(String datas) {
        Datas = datas;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    @Override
    public String toString() {
        return "HttpResultBean{" +
                "Success=" + Success +
                ", NeedVerifyCode=" + NeedVerifyCode +
                ", Message='" + Message + '\'' +
                ", ErrorCode='" + ErrorCode + '\'' +
                ", Datas='" + Datas + '\'' +
                ", Total=" + Total +
                '}';
    }
}
