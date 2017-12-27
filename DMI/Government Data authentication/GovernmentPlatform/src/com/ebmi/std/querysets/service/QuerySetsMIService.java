package com.ebmi.std.querysets.service;

import java.util.List;
import java.util.Map;

import com.ebmi.std.querysets.dto.MaternReimbItem;
import com.ebmi.std.querysets.dto.TurnoutItem;

public interface QuerySetsMIService {

	/**
	 * 医保数据查询:转外就医
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	List<TurnoutItem> queryTurnoutListMI(Map<String, Object> condition) throws Exception;

	/**
	 * 医保数据查询:异地就医
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> queryAllopatryTreatMI(Map<String, Object> condition) throws Exception;

	/**
	 * 医保数据查询:生育报销
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	List<MaternReimbItem> queryMaternReimbMI(Map<String, Object> condition) throws Exception;

	/**
	 * 医保数据查询:慢性病待遇
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> queryChronicBenefitMI(Map<String, Object> condition) throws Exception;
	
}
