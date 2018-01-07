package com.example.easy.commons.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.easy.commons.component.Messages;
import com.example.easy.commons.model.UserInfo;

public class BaseRestController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected Messages messages;

	@Context
	protected SecurityContext securityContext;

	protected UserInfo userInfo = null;

	public BaseRestController() {
		logger.debug("BaseRestController instantiated.");
		if (securityContext != null) {
			try {
				//userInfo = (UserInfo) securityContext.getUserPrincipal();
				userInfo = new UserInfo("tester", 115, 100);
				logger.debug(userInfo.toString());
			} catch (Exception e) {
				logger.debug("Error processing userInfo.", e);
			}
		}

	}
}
