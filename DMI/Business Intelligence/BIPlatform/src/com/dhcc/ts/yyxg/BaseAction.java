package com.dhcc.ts.yyxg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {

	protected final static Logger logger=Logger.getLogger(BaseAction.class);
	
	private static final long serialVersionUID = 1L;

	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	protected PrintWriter getWriter() throws IOException {
		HttpServletResponse response = getResponse();
		response.setCharacterEncoding("UTF-8");
		return response.getWriter();
	}
	
	protected Map<String, Object> getErrorMap(String message) {
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("code", "0");
		errorMap.put("message", message);
		return errorMap;
	}
	
	protected Map<String, Object> getSuccessMap() {
		Map<String, Object> successMap = new HashMap<String, Object>();
		successMap.put("code", "1");
		return successMap;
	}
}
