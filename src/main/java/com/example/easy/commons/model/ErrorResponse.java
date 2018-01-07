package com.example.easy.commons.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorResponse
{

	private String message;
	private Integer code;
	private String details;

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Integer getCode()
	{
		return code;
	}

	public void setCode(Integer code)
	{
		this.code = code;
	}

	public ErrorResponse()
	{

	}

	public ErrorResponse(String message)
	{
		this.message = message;
	}

	public ErrorResponse(String message, Integer code)
	{
		this.message = message;
		this.code = code;
	}

	public ErrorResponse(String message, Integer code, String details)
	{
		this.message = message;
		this.code = code;
		this.details = details;
	}

}
