package com.models.cloud.gw.service.payuser.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.models.cloud.gw.service.payuser.UcUserAddrServiceGW;
import com.models.cloud.pay.payuser.service.UcUserAddrService;
/***
 * 用户中心-添加，修改收货地址
 * @author enjuan.ren
 *
 */
@Service("ucUserAddrServiceGWImpl")
public class UcUserAddrServiceGWImpl implements UcUserAddrServiceGW{
	
	@Resource(name="ucUserAddrServiceImpl")
	 UcUserAddrService ucuseraddrservice;
	
	@Override
	public Map<String, Object> saveUcUserAddr(Map<String, Object> inputMap) throws Exception {
		return ucuseraddrservice.saveUcUserAddr(inputMap);
	}

	@Override
	public Map<String, Object> ucUserAddrQuery(Map<String, Object> inputMap) throws Exception {
		return ucuseraddrservice.ucUserAddrQuery(inputMap);
	}

}
