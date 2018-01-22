package com.pay.cloud.h5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 接收公共参数
 * @author qingsong.li
 *
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController {

	@RequestMapping(value = "error404")
	public String error404(){
		return "error/error404";
	}
	
	@RequestMapping(value = "error500")
	public String error500(){
		return "error/error500";
	}
}
