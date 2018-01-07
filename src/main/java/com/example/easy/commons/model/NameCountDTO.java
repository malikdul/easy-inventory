package com.example.easy.commons.model;

public class NameCountDTO implements IBaseDTO
{

	private String name;
	private Integer count;
	private Double percent;

	public NameCountDTO()
	{

	}

	public NameCountDTO(String name, Integer count)
	{
		super();
		this.name = name;
		this.count = count;
	}
	
	public NameCountDTO(Integer count, Double percent, String name)
	{
		super();
		this.name = name;
		this.count = count;
		this.percent = percent;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getCount()
	{
		return count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}
	
	@Override
	public String toString() {
		return "NameCountDTO { name: " + name + " count: " + count + " percent: " + percent + " }"; 
	}
}
