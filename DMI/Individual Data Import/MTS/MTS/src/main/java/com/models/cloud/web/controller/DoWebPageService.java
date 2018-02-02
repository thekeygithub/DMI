package com.models.cloud.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface DoWebPageService {

	public Map<String,Object> returnData(Map<String,Object> map,Model model,HttpServletRequest request);
	
	public Map<String,Object> returnDataByMap(Map<String,Object> map,HttpServletRequest request);
}
