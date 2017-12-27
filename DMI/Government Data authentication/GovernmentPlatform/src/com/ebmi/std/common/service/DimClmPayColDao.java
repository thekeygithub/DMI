package com.ebmi.std.common.service;

import java.util.List;

import com.ebmi.std.common.cond.DimClmPayColCond;
import com.ebmi.std.common.entity.DimClmPayCol;

public interface DimClmPayColDao {
	
	/**
	 * 查询结算单支付金额字段配置
	 * @param cond
	 * @return
	 * @throws Exception
	 */
	public List<DimClmPayCol> queryClmPayCol(DimClmPayColCond cond)throws Exception;
	
}
