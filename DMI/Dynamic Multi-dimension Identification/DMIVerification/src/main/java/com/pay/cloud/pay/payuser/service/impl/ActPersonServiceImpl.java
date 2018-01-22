package com.pay.cloud.pay.payuser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.payuser.dao.ActPersonMapper;
import com.pay.cloud.pay.payuser.entity.ActPerson;
import com.pay.cloud.pay.payuser.service.ActPersonService;

/**
 * Created by yacheng.ji on 2016/5/16.
 */
@Service("actPersonServiceImpl")
public class ActPersonServiceImpl implements ActPersonService {

    @Autowired
    private ActPersonMapper actPersonMapper;

    public int updateActPersonInfo(ActPerson actPerson) throws Exception {
        return actPersonMapper.updateActPersonInfo(actPerson);
    }

	public ActPerson findActPersonById(Long actId) throws Exception {
		return actPersonMapper.findActPersonById(actId);
	}
    
}
