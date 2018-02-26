package com.models.cloud.pay.payuser.dao;

import com.models.cloud.pay.payuser.entity.ActPerson;

public interface ActPersonMapper {

    ActPerson findActPersonById(Long actId) throws Exception;

	int saveActPerson(ActPerson actPerson) throws Exception;

	int updatePaymentPass(ActPerson actPerson) throws Exception;

	int updateActPersonInfo(ActPerson actPerson) throws Exception;
	
	int updateActPersonAuthMobile(ActPerson actPerson) throws Exception;

	int updateActPersonSiCardNo(ActPerson actPerson) throws Exception;
}