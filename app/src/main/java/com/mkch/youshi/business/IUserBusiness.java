package com.mkch.youshi.business;

public interface IUserBusiness {
	/**
	 * 用户登录
	 * @param pPhone
	 * @param pPassword
	 * @return
	 * @throws Exception
	 */
	public abstract String getUserLogin(String pPhone, String pPassword) throws Exception;



}