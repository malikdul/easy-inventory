package com.example.easy.commons.model;

public class InfoDTO implements IBaseDTO {
	private String info;

	public InfoDTO(String info) {
		this.info = info;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
