package com.ebmi.std.querysets.dao;

import java.util.List;
import java.util.Map;

import com.ebmi.std.querysets.dto.MaternReimbItem;
import com.ebmi.std.querysets.dto.TurnoutItem;

public interface QuerySetsMIDao {

	List<TurnoutItem> queryTurnoutTreatList(Map<String, Object> condition) throws Exception;

	Map<String, Object> queryAllopatryTreatMI(Map<String, Object> condition) throws Exception;

	List<MaternReimbItem> queryMaternReimbMI(Map<String, Object> condition) throws Exception;

	Map<String, Object> queryChronicBenefitMI(Map<String, Object> condition) throws Exception;

}
