package com.example.easy.commons.model;

public class MessageResponseDTO implements IBaseDTO
{
	
    private String code;
	private String message;
	private Long count;

	public MessageResponseDTO()
	{

	}

	public MessageResponseDTO(Long count)
	{
		this.count = count;
	}
	
	public MessageResponseDTO(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public Long getCount()
	{
		return count;
	}

	public void setCount(Long count)
	{
		this.count = count;
	}

}
