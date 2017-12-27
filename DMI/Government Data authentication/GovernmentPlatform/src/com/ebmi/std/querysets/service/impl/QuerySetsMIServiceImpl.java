package com.ebmi.std.querysets.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ebmi.std.querysets.dao.QuerySetsMIDao;
import com.ebmi.std.querysets.dto.MaternReimbItem;
import com.ebmi.std.querysets.dto.TurnoutItem;
import com.ebmi.std.querysets.service.QuerySetsMIService;

@Service("querySetsMIServiceImpl")
public class QuerySetsMIServiceImpl implements QuerySetsMIService {

	@Resource(name="querySetsMIDaoImpl")
	private QuerySetsMIDao querySetsMIDao;
	
	@Override
	public List<TurnoutItem> queryTurnoutListMI(Map<String, Object> condition) throws Exception {
		
		return querySetsMIDao.queryTurnoutTreatList(condition);
	}

	@Override
	public Map<String, Object> queryAllopatryTreatMI(Map<String, Object> condition) throws Exception {
		
		return querySetsMIDao.queryAllopatryTreatMI(condition);
	}

	@Override
	public List<MaternReimbItem> queryMaternReimbMI(Map<String, Object> condition) throws Exception {
		
		return querySetsMIDao.queryMaternReimbMI(condition);
	}

	@Override
	public Map<String, Object> queryChronicBenefitMI(Map<String, Object> condition) throws Exception {
		
		return querySetsMIDao.queryChronicBenefitMI(condition);
	}

}
