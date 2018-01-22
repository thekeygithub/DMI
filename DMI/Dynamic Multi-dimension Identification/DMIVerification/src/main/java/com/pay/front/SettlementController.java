package com.pay.front;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 医保结算前置机模拟实现
 * 
 * <pre>
 * TODO 
 * 1、独立样例系统，各地遵照开发
 * 2、规划系统功能、日志系统的表结构
 * 3、统一接口标准，加密方式
 * 4、提供基础的工具支持各地社保结算接口调用（Socket/WebService/HTTP）
 * </pre>
 * 
 * @author xiangfeng.guan
 */
@Controller
public class SettlementController {

	/**
	 * 分发入口
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/front", method = RequestMethod.POST)
	public @ResponseBody Object dispatcher(@RequestBody Map<String, String> map) {
		String serviceName = map.get("service_name");
		switch (serviceName) {
		case "settlement":
		case "presettlement":
		case "settlementQuery":
		}
		return xxx(map);
	}

	private ResultDTO xxx(Map<String, String> map) {
		ResultDTO dto = new ResultDTO();
		dto.setSettle_id(UUID.randomUUID().toString());
		dto.setResult_code("0");
		dto.setResult_message("成功");
		dto.setAccount_bal("100.00");
		dto.setTotal_amt("0.03");
		dto.setAccount_pay("0.01");
		dto.setPersonal_pay("0.01");
		dto.setFund_pay("0.01");
		dto.setData("｛医保返回数据｝");
		return dto;
	}

}
