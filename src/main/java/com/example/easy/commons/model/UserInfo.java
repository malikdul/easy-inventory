package com.example.easy.commons.model;

import java.security.Principal;

public class UserInfo implements Principal
{

	private final String name;
	private  final Integer userId;
	// private final String role;
	private  final Integer accountId;

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return "UserInfo [name=" + name + ", userId=" + userId + ", accountId=" + accountId + "]";
	}

	public Integer getUserId()
	{
		return userId;
	}

	public Integer getAccountId()
	{
		return accountId;
	}

	public UserInfo(String name, Integer accountId, Integer userId)
	{
		this.name = name;
		this.accountId = accountId;
		this.userId = userId;
	}
}