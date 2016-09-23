package com.mkch.youshi.bean;

/**
 * 已登录用户信息
 *
 * @author JLJ
 */
public class User {
    private String MobileNumber;//手机号
    private String OpenFireUserName;//openfire用户名
    private String AppVersion;//客户端版本
    private String ClientType;//客户端类型
    private String ClientVersion;//客户端版本
    private String DeviceInfo;//设备信息
    private String Firm;//手机型号
    private String ImageVerifyCode;//图像验证码
    private String VerifyCode;//短信验证码
    private String LoginCode;

    public String getVerifyCode() {
        return VerifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        VerifyCode = verifyCode;
    }

    private String OsType;//客户端操作系统
    private String OsUuid;//uuid
    private String Password;//密码

    private String NickName;//昵称
    private String HeadPic;//头像
    private String youshiNumber;//优时账号
    private String RealName;//真实姓名
    private String Birthday;//生日
    private String Sex;//性别
    private String SexCache;//性别缓存
    private String Address;//地区
    private String Signature;//个性签名
    private String CertNo;//身份证
    private Boolean Protected;//是否账号保护
    private Boolean AddmeVerify;//是否接受好友验证
    private Boolean ViewMySchedule;//是否允许查看表盘
    private Boolean Recommend;//是否推荐通讯录好友
    private Boolean AutoFinish;//是否允许日程自动完成
    private Boolean ConflictPromise;//是否允许日程时间冲突
    private String ConflictNumber;//允许日程冲突的最大数量
    private String AffairTime;//事务单次时长
    private Boolean RealVerify;//是否实名认证
    private Boolean Sound;//提示音开关
    private Boolean Vibrate;//振动开关
    private Boolean Disturb;//是否免打扰
    private Boolean Night;//是否夜间
    private String TokenID;//令牌

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

    public String getYoushiNumber() {
        return youshiNumber;
    }

    public void setYoushiNumber(String youshiNumber) {
        this.youshiNumber = youshiNumber;
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

    public String getSexCache() {
        return SexCache;
    }

    public void setSexCache(String sexCache) {
        SexCache = sexCache;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
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

    public Boolean getRecommend() {
        return Recommend;
    }

    public void setRecommend(Boolean recommend) {
        Recommend = recommend;
    }

    public Boolean getAutoFinish() {
        return AutoFinish;
    }

    public void setAutoFinish(Boolean autoFinish) {
        AutoFinish = autoFinish;
    }

    public Boolean getConflictPromise() {
        return ConflictPromise;
    }

    public void setConflictPromise(Boolean conflictPromise) {
        ConflictPromise = conflictPromise;
    }

    public String getConflictNumber() {
        return ConflictNumber;
    }

    public void setConflictNumber(String conflictNumber) {
        ConflictNumber = conflictNumber;
    }

    public String getAffairTime() {
        return AffairTime;
    }

    public void setAffairTime(String affairTime) {
        AffairTime = affairTime;
    }

    public Boolean getRealVerify() {
        return RealVerify;
    }

    public void setRealVerify(Boolean realVerify) {
        RealVerify = realVerify;
    }

    public Boolean getSound() {
        return Sound;
    }

    public void setSound(Boolean sound) {
        Sound = sound;
    }

    public Boolean getVibrate() {
        return Vibrate;
    }

    public void setVibrate(Boolean vibrate) {
        Vibrate = vibrate;
    }

    public Boolean getDisturb() {
        return Disturb;
    }

    public void setDisturb(Boolean disturb) {
        Disturb = disturb;
    }

    public Boolean getNight() {
        return Night;
    }

    public void setNight(Boolean night) {
        Night = night;
    }

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        TokenID = tokenID;
    }

    public String getLoginCode() {
        return LoginCode;
    }

    public void setLoginCode(String loginCode) {
        LoginCode = loginCode;
    }

    public String getOpenFireUserName() {
        return OpenFireUserName;
    }

    public void setOpenFireUserName(String openFireUserName) {
        OpenFireUserName = openFireUserName;
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
