package com.pay.cloud.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.util.ValidateUtils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;

/**
 * 跳转中间页Controller
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年8月19日 
 * @time 下午4:56:25
 * @version V1.0
 * @修改记录
 *
 */
@Controller
@RequestMapping(value = "/ebaonet")
public class ToAppController {
	
	 private static final Logger logger = Logger.getLogger(ToAppController.class);

	@Resource
	private RedisService redisService;
	 
	 @RequestMapping("/toapp")
	 public String resultWaitingPage(Model model,HttpServletRequest request){
		 String queryString = request.getQueryString();
		 if(logger.isInfoEnabled()){
			 logger.info("从第三方支付通道返回e保钱包 queryString=" + queryString);
		 }
		 return "common/resultWaiting";
	 }

	@RequestMapping("/alipayFrontNotice")
	public String alipayFrontNotice(Model model,HttpServletRequest request){
		String queryString = request.getQueryString();
		if(logger.isInfoEnabled()){
			logger.info("支付宝前台通知e保钱包支付结果 queryString=" + queryString);
		}
		return "common/alipayFrontNotice";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/bkuMiddle")
	public String bkuMiddlePage(Model model, HttpServletRequest request){
		try{
			String data = String.valueOf(request.getParameter("data")).trim();
			if(logger.isInfoEnabled()){
				logger.info("银联支付通道中间页跳转 data=" + data);
			}
			if(ValidateUtils.isEmpty(data)){
				return "error/error";
			}
			String redisKey = "unionPayPostParams_".concat(data);
			Map dataMap = redisService.getMap(redisKey);
			if(logger.isInfoEnabled()){
				logger.info("dataMap=" + dataMap);
			}
			if(null == dataMap || dataMap.size() == 0){
				if(logger.isInfoEnabled()){
					logger.info("解析data出现异常");
				}
				return "error/error";
			}
			model.addAttribute("unionPayUrl", dataMap.remove("unionPayUrl"));
			model.addAttribute("data", dataMap);
			return "common/bkuMiddlePage";
		}catch (Exception e) {
			if(logger.isInfoEnabled()){
				logger.info("出现异常：" + e.getMessage());
			}
			return "error/error";
		}
	}
}