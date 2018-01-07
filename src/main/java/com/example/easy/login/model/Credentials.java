package com.example.easy.login.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement
@ApiModel
public class Credentials implements Serializable
{

	@Override
	public String toString()
	{
		return "Credentials [username=" + username + ", accountId=" + accountId + "]";
	}

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(required = true, name = "username", value = "UserName supplied for login verification",example="abc@xyz.com")
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	@ApiModelProperty(required = true, name = "password", value = "Password supplied for login verification",example="123456")
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@NotNull(message = "{username.not.null}")
	@NotEmpty(message = "{username.not.empty}")
	private String username;

	@NotNull(message = "{password.not.null}")
	@NotEmpty(message = "{password.not.empty}")
	private String password;

	private String accountId;

	// Getters and setters omitted
}