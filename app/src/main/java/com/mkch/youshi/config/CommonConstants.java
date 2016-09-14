package com.mkch.youshi.config;

public class CommonConstants {
    public static final String APP_PACKAGE_NAME = "com.mkch.youshi";
    /**
     * zhangjun
     * 从1开始
     */
    public static final int FLAG_GET_REG_USER_LOGIN_SUCCESS = 1;
    public static final int FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_ERROR = 2;
    public static final int FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_SHOW = 3;
    public static final int FLAG_COVER_TOKEN_ID_SUCCESS = 4;
    public static final int FLAG_GET_USER_JOIN_IMG_VERIFY_SHOW = 5;
    public static final int FLAG_GET_USER_SEND_VERIFICATION_CODE = 6;
    public static final int FLAG_GET_REG_MOBILEMGS_VALIDATE_CAN_GET_AGAIN_SUCCESS = 7;
    public static final int FLAG_NO_GET_PICCODE = 8;
    public static final int FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_CHAGE = 9;
    public static final int FLAG_GET_REG_USER_LOGIN_ACCOUNT_OR_PASSWORD_ERROR = 10;
    public static final int FLAG_GET_PHONE_CONTACT_SHOW = 11;
    public static final int FLAG_GET_SEARCH_RESULT_SHOW = 12;
    public static final int FLAG_GET_FRIEND_LIST_SHOW = 13;
    /**
     * yaozhongping
     * 从100开始
     */


    /**
     * jianglongjian
     * 从200开始
     */
    public static final int FLAG_ALLOW_FRIEND_SUCCESS = 200;


    public static final String MSG_CONNECT_ERROR = "连接出错，请检查您的网络";
    public static final String MSG_CONNECT_TIMEOUT = "连接超时，请检查您的网络...";
    public static final String MSG_SERVER_TIMEOUT = "服务器响应超时...";
    public static final String MSG_DATA_EXCEPTION = "数据异常";

    /**
     * 测试地址
     */
    public static final String TEST_ADDRESS = "http://192.168.3.8:1001/WebServices/YoShiServices/";
    //	public static final String TRUE_ADDRESS = "http://www.maikejia.com/WebServices/YoShiServices/";
    public static final String NOW_ADDRESS = TEST_ADDRESS;

    public static final String TEST_ADDRESS_PRE = "http://192.168.3.8:1001";
    //    public static final String TRUE_ADDRESS_PRE = "http://192.168.3.8:1001";
    public static final String NOW_ADDRESS_PRE = TEST_ADDRESS_PRE;

    //zhangjun-----------------------------------------------start
    /**
     * 注册登录
     */
    //登录
    public static final String LOGIN = NOW_ADDRESS + "Login";
    //登录是否需要图片验证码
    public static final String CheckLoginPickCodeExist = NOW_ADDRESS + "CheckLoginPickCodeExist";
    //切换登录图片验证码
    public static final String ReloadLoginPicCode = NOW_ADDRESS + "ReloadLoginPicCode ";
    //注册
    public static final String JOIN = NOW_ADDRESS + "Join";
    //获取TokenID
    public static final String GetTokenID = NOW_ADDRESS + "GetTokenID";
    //注册是否需要图片验证码
    public static final String CheckMessagePickCodeExist = NOW_ADDRESS + "CheckMessagePickCodeExist";
    //判断手机号是否已注册
    public static final String MobileNumberIsExist = NOW_ADDRESS + "MobileNumberIsExist";
    //发送短信验证码
    public static final String SetVerificationCode = NOW_ADDRESS + "SetVerificationCode";
    //核对短信验证码
    public static final String MessageCodeIsValid = NOW_ADDRESS + "MessageCodeIsValid";
    //获取通讯录信息
    public static final String GetContactsInfo = NOW_ADDRESS + "GetContactsInfo";
    //通过忧时账号\手机号查询用户
    public static final String SearchUser = NOW_ADDRESS + "SearchUser";
    //获取好友列表
    public static final String GetFriendList = NOW_ADDRESS + "GetFriendList ";
    //zhangjun-----------------------------------------------end


    //yaozhongping-----------------------------------------------start


    //yaozhongping-----------------------------------------------end


    //jianglongjian-----------------------------------------------start
    //根据openfireusername查询用户详细信息
    public static final String GetInfoByOpenFireName = NOW_ADDRESS + "GetInfoByOpenFireName";
    //同意好友添加 sVerifyCode-登录认证码
    public static final String AllowFriend = NOW_ADDRESS + "AllowFriend";


    //jianglongjian-----------------------------------------------end
}
