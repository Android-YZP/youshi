package com.mkch.youshi.config;

public class CommonConstants {
	public static final String APP_PACKAGE_NAME = "com.mkch.youshi" ;
    /**
     * zhangjun
     * 从1开始
     */
    public static final int FLAG_GET_REG_USER_LOGIN_SUCCESS = 1;
    public static final int FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_ERROR = 2;
	public static final int FLAG_GET_REG_USER_LOGIN_IMG_VERIFY_SHOW = 3;

    /**
     * yaozhongping
     * 从100开始
     */


    /**
     * jianglongjian
     * 从200开始
     */


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
    //注册
	public static final String JOIN = NOW_ADDRESS + "Join";


    //zhangjun-----------------------------------------------end


    //yaozhongping-----------------------------------------------start


    //yaozhongping-----------------------------------------------end


    //jianglongjian-----------------------------------------------start


    //jianglongjian-----------------------------------------------end
}
