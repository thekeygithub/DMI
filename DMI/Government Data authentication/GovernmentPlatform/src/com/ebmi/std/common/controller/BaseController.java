package com.ebmi.std.common.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class BaseController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Resource
	protected HttpServletRequest request;

	@Resource
	protected HttpServletResponse response;

	@Resource
	protected HttpSession session;

}
