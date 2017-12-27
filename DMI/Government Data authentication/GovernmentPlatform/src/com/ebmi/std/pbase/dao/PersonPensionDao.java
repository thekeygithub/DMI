package com.ebmi.std.pbase.dao;

import java.util.List;

import com.ebmi.std.pbase.cond.PSiEndowDetCond;
import com.ebmi.std.pbase.dto.PSiEndowDet;

public interface PersonPensionDao {

	/**
	 * 查询参保人养老金领取情况
	 * @param cond 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<PSiEndowDet> query(PSiEndowDetCond cond)throws Exception;
}
