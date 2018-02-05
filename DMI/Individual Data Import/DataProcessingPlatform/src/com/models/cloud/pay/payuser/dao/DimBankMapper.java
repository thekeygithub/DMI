package com.models.cloud.pay.payuser.dao;

import java.util.List;

import com.models.cloud.pay.payuser.entity.DimBank;

public interface DimBankMapper {

	/**
	 * 
	 * @Description: 获取支持的银行卡列表
	 * @Title: queryDimBankList 
	 * @param dimBank
	 * @return List<DimBank>
	 * @throws Exception 
	 */
	List<DimBank> queryDimBankList(DimBank dimBank) throws Exception;

	/**
	 * 通过银行ID获取银行信息
	 * @param bankId
	 * @throws Exception
     */
	DimBank findDimBankByBankId(Long bankId) throws Exception;
}