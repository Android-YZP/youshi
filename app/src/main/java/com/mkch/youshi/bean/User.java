package com.mkch.youshi.bean;
/**
 * 已登录用户信息
 * @author JLJ
 *
 */
public class User {
    private String MobileNumber;//手机号
    private String AppVersion;//客户端版本
    private String ClientType;//客户端类型
    private String ClientVersion;//客户端版本
    private String DeviceInfo;//设备信息
    private String Firm;//手机型号
    private String ImageVerifyCode;//图像验证码
    private String OsType;//客户端操作系统
    private String OsUuid;//uuid
    private String Password;//密码

    private String NickName;//昵称
    private String HeadPic;//头像
    private String RealName;//真实姓名
    private String Birthday;//生日
    private String Sex;//性别
    private String CertNo;//身份证
    private Boolean Protected;//是否账号保护
    private Boolean AddmeVerify;//是否接受好友验证
    private Boolean ViewMySchedule;//是否允许查看表盘
    private Boolean RealVerify;//是否实名认证


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

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String headPic) {
        HeadPic = headPic;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getCertNo() {
        return CertNo;
    }

    public void setCertNo(String certNo) {
        CertNo = certNo;
    }

    public Boolean getProtected() {
        return Protected;
    }

    public void setProtected(Boolean aProtected) {
        Protected = aProtected;
    }

    public Boolean getAddmeVerify() {
        return AddmeVerify;
    }

    public void setAddmeVerify(Boolean addmeVerify) {
        AddmeVerify = addmeVerify;
    }

    public Boolean getViewMySchedule() {
        return ViewMySchedule;
    }

    public void setViewMySchedule(Boolean viewMySchedule) {
        ViewMySchedule = viewMySchedule;
    }

    public Boolean getRealVerify() {
        return RealVerify;
    }

    public void setRealVerify(Boolean realVerify) {
        RealVerify = realVerify;
    }

    @Override
    public String toString() {
        return "User{" +
                "MobileNumber='" + MobileNumber + '\'' +
                ", AppVersion='" + AppVersion + '\'' +
                ", ClientType='" + ClientType + '\'' +
                ", ClientVersion='" + ClientVersion + '\'' +
                ", DeviceInfo='" + DeviceInfo + '\'' +
                ", Firm='" + Firm + '\'' +
                ", ImageVerifyCode='" + ImageVerifyCode + '\'' +
                ", OsType='" + OsType + '\'' +
                ", OsUuid='" + OsUuid + '\'' +
                ", Password='" + Password + '\'' +
                ", NickName='" + NickName + '\'' +
                ", HeadPic='" + HeadPic + '\'' +
                ", RealName='" + RealName + '\'' +
                ", Birthday='" + Birthday + '\'' +
                ", Sex='" + Sex + '\'' +
                ", CertNo='" + CertNo + '\'' +
                ", Protected=" + Protected +
                ", AddmeVerify=" + AddmeVerify +
                ", ViewMySchedule=" + ViewMySchedule +
                ", RealVerify=" + RealVerify +
                '}';
    }
}
