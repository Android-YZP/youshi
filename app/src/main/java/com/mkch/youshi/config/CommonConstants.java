package com.mkch.youshi.config;

import android.os.Environment;

public class CommonConstants {
    public static final String APP_PACKAGE_NAME = "com.mkch.youshi";
    /**
     * zhangjun
     * 从1开始
     */
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
    public static final int FLAG_UPLOAD_SUCCESS = 14;
    public static final int FLAG_CHANGE_NICKNAME_SUCCESS = 15;
    public static final int FLAG_CHANGE_YOUSHI_NUMBER_SUCCESS = 16;
    public static final int FLAG_CHANGE_SIGN_SUCCESS = 17;
    public static final int FLAG_CHANGE_ADDRESS_SUCCESS = 18;
    public static final int FLAG_REG_CODE_SHOW = 19;
    public static final int FLAG_CHANGE_PASSWORD_SUCCESS = 20;
    public static final int FLAG_CHANGE_PROTECTED_SUCCESS = 21;
    public static final int FLAG_CHANGE_ERROR1 = 22;
    public static final int FLAG_CHANGE_PASSWORD_ERROR2 = 23;
    public static final int FLAG_CHANGE_ERROR3 = 24;
    public static final int FLAG_CHANGE_VIEWMYSCHEDULE_SUCCESS = 25;
    public static final int FLAG_CHANGE_ADDMEVERIFY_SUCCESS = 26;
    public static final int FLAG_CHANGE_YOUSHI_NUMBER_IS_EXIST = 27;
    public static final int FLAG_REG_PHONE_TOKENID_NO_EXIST = 28;
    public static final int FLAG_CHANGE_YOUSHI_NUMBER_FAIL = 29;
    public static final int FLAG_MESSAGE_CODE_NO_EXIST = 30;
    public static final int FLAG_MESSAGE_CODE_IS_OVERDUE = 31;
    public static final int FLAG_GET_ADD_FRIEND_INFORMATION = 32;
    /**
     * yaozhongping
     * 从100开始
     */
    public static final String YOU_PAN_PIC_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/couldFile/";
    public static final int FLAG_GET_ADD_PERSONER_EVENT_SUCCESS = 103;
    public static final int FLAG_GET_ADD_PERSONER_EVENT_FAIL = 104;
    public static final int PHOTO_REQUEST_FILE = 105;// 从文件夹选择
    public static final int PHOTO_REQUEST_GALLERY = 106;// 从相册中选择

    /**
     * jianglongjian
     * 从200开始
     */
    public static final int FLAG_ALLOW_FRIEND_SUCCESS = 200;
    public static final int FLAG_DELETE_FRIEND_SUCCESS = 201;
    public static final int SEND_MSG_SUCCESS = 202;


    public static final String MSG_CONNECT_ERROR = "连接出错，请检查您的网络";
    public static final String MSG_CONNECT_TIMEOUT = "连接超时，请检查您的网络...";
    public static final String MSG_SERVER_TIMEOUT = "服务器响应超时...";
    public static final String MSG_DATA_EXCEPTION = "数据异常";

    /**
     * 测试地址
     */
    //文件的网络根地址
    public static final String FILE_ROOT_ADDRESS = "http://192.168.3.8:1001";

        public static final String TEST_ADDRESS = "http://192.168.3.8:1001/WebServices/YoShiServices/";
//    public static final String TEST_ADDRESS = "http://192.168.2.96:8012/WebServices/YoShiServices/";
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
    public static final String ReloadLoginPicCode = NOW_ADDRESS + "ReloadLoginPicCode";
    //注册
    public static final String JOIN = NOW_ADDRESS + "Join";
    //切换注册图片验证码
    public static final String ReloadMessagePicCode = NOW_ADDRESS + "ReloadMessagePicCode";
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
    public static final String GetFriendList = NOW_ADDRESS + "GetFriendList";
    //上传头像
    public static final String UploadHeadPicAndroid = NOW_ADDRESS + "UploadHeadPicAndroid";
    //修改昵称
    public static final String ChangeNickName = NOW_ADDRESS + "ChangeNickName";
    //修改优时账号
    public static final String ChangeYoShiUserName = NOW_ADDRESS + "ChangeYoShiUserName";
    //修改性别
    public static final String ChangeSex = NOW_ADDRESS + "ChangeSex";
    //修改地区
    public static final String ChangePlace = NOW_ADDRESS + "ChangePlace";
    //修改个性签名
    public static final String ChangeSign = NOW_ADDRESS + "ChangeSign";
    //修改手机号
    public static final String ChangeMobileNumber = NOW_ADDRESS + "ChangeMobileNumber";
    //修改密码
    public static final String ChangePassword = NOW_ADDRESS + "ChangePassword";
    //修改账号保护开关
    public static final String ChangeProtected = NOW_ADDRESS + "ChangeProtected";
    //修改是否允许好友查看日程表盘开关
    public static final String ChangeViewMySchedule = NOW_ADDRESS + "ChangeViewMySchedule";
    //修改加用户好友时是否需要身份验证开关
    public static final String ChangeAddmeVerify = NOW_ADDRESS + "ChangeAddmeVerify";
    //忘记密码
    public static final String ForgetPassword = NOW_ADDRESS + "ForgetPassword";
    //备注好友
    public static final String RemarkFriend = NOW_ADDRESS + "RemarkFriend";
    //zhangjun-----------------------------------------------end


    //yaozhongping-----------------------------------------------start

    //获取文件
//    public static final String GetCloudFile = NOW_ADDRESS + "GetCloudFile";
    //通过忧时账号\手机号查询用户
    public static final String SAVESCHEDULE = NOW_ADDRESS + "SaveSchedule";

    //通过忧时账号\手机号查询用户
    public static final String GetCloudFile = NOW_ADDRESS + "GetCloudFile";
    //删除优盘文件
    public static final String DeleteCloudFile = NOW_ADDRESS + "DeleteCloudFile";
    //上传文件
    public static final String UploadFileAndroid = NOW_ADDRESS + "UploadFileAndroid";
    //参与人接受拒绝 sVerifyCode-登录认证码
    public static final String JoinUserResult = NOW_ADDRESS + "JoinUserResult";
    //删除日程
    public static final String DeleteSchedule = NOW_ADDRESS + "DeleteSchedule";

    //yaozhongping-----------------------------------------------end


    //jianglongjian-----------------------------------------------start
    //根据openfireusername查询用户详细信息
    public static final String GetInfoByOpenFireName = NOW_ADDRESS + "GetInfoByOpenFireName";
    //同意好友添加 sVerifyCode-登录认证码
    public static final String AllowFriend = NOW_ADDRESS + "AllowFriend";
    //删除好友 sVerifyCode-登录认证码
    public static final String DeleteFriend = NOW_ADDRESS + "DeleteFriend";


    //jianglongjian-----------------------------------------------end
}
