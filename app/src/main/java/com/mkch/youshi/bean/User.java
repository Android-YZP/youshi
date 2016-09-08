package com.mkch.youshi.bean;
/**
 * 已登录用户信息
 * @author JLJ
 *
 */
public class User {
    private String MobileNumber;
    private String AppVersion;
    private String ClientType;
    private String ClientVersion;
    private String DeviceInfo;
    private String Firm;
    private String ImageVerifyCode;
    private String OsType;
    private String OsUuid;
    private String Password;

    public User(String mobileNumber, String appVersion, String clientType, String clientVersion, String deviceInfo, String firm, String imageVerifyCode, String osType, String osUuid, String password) {
        MobileNumber = mobileNumber;
        AppVersion = appVersion;
        ClientType = clientType;
        ClientVersion = clientVersion;
        DeviceInfo = deviceInfo;
        Firm = firm;
        ImageVerifyCode = imageVerifyCode;
        OsType = osType;
        OsUuid = osUuid;
        Password = password;
    }

    public User() {
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getClientType() {
        return ClientType;
    }

    public void setClientType(String clientType) {
        ClientType = clientType;
    }

    public String getClientVersion() {
        return ClientVersion;
    }

    public void setClientVersion(String clientVersion) {
        ClientVersion = clientVersion;
    }

    public String getDeviceInfo() {
        return DeviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        DeviceInfo = deviceInfo;
    }

    public String getFirm() {
        return Firm;
    }

    public void setFirm(String firm) {
        Firm = firm;
    }

    public String getImageVerifyCode() {
        return ImageVerifyCode;
    }

    public void setImageVerifyCode(String imageVerifyCode) {
        ImageVerifyCode = imageVerifyCode;
    }

    public String getOsType() {
        return OsType;
    }

    public void setOsType(String osType) {
        OsType = osType;
    }

    public String getOsUuid() {
        return OsUuid;
    }

    public void setOsUuid(String osUuid) {
        OsUuid = osUuid;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
