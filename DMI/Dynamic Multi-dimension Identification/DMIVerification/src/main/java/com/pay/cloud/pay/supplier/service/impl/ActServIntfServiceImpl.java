package com.pay.cloud.pay.supplier.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pay.cloud.pay.supplier.dao.SpServIntfMapper;
import com.pay.cloud.pay.supplier.service.ActServIntfService;

@Service("actServIntfServiceImpl")
public class ActServIntfServiceImpl implements ActServIntfService{

	@Resource
	private SpServIntfMapper spServIntfMapper;
	
	@Override
	public boolean hasPrivilege(Long actId, String interfaceCode) {
		List<String> list = null;
		list = this.selectActIdByIntfCode(interfaceCode);
		if(list == null || list.size() == 0){
			return false;
		}
		return list.contains(String.valueOf(actId));
	}

	@Override
	public List<String> selectActIdByIntfCode(String interfaceCode) {	
		List<String> list = spServIntfMapper.selectActIdByIntfCode(interfaceCode);
		return list;
	}
}
