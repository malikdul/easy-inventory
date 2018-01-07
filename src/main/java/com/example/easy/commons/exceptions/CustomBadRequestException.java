/**
 * 
 */
package com.example.easy.commons.exceptions;

/**
 * CustBadRequestException
 */
public class CustomBadRequestException extends Exception
{
	private String code;

	public CustomBadRequestException(String message)
	{
		this(null, message);
	}

	public CustomBadRequestException(String code, String message)
	{
		super(message);
		this.code = code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

}
