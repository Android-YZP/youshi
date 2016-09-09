package com.mkch.youshi.bean;

/**
 * 未登录时用户的信息保存
 * @author JLJ
 *
 */
public class UnLoginedUser {
	private int versioncode;//用户前次安装的版本数量
	private String tokenID;//访问生成短信验证码时的令牌ID
	
	private boolean isNeedRefreshCate;//是否需要刷新分类
	
	public UnLoginedUser() {
		super();
	}
	public int getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}
	public String getTokenID() {
		return tokenID;
	}
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}
	
	public boolean isNeedRefreshCate() {
		return isNeedRefreshCate;
	}
	public void setNeedRefreshCate(boolean isNeedRefreshCate) {
		this.isNeedRefreshCate = isNeedRefreshCate;
	}
	
	public UnLoginedUser(int versioncode, String tokenID) {
		super();
		this.versioncode = versioncode;
		this.tokenID = tokenID;
	}
	@Override
	public String toString() {
		return "UnLoginedUser [versioncode="
				+ versioncode + ", tokenID=" + tokenID + "]";
	}
	
}
