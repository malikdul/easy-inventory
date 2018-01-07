package com.example.easy.commons.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.example.easy.commons.component.Messages;

public class EasyBaseServiceImpl implements IEasyBaseConfig
{

	@Autowired
	protected Messages messages;

	@Value("${app.mode}")
	protected String appMode;

}
