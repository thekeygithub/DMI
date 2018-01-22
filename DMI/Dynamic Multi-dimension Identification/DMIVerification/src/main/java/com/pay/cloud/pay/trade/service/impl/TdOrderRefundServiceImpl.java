package com.pay.cloud.pay.trade.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.trade.dao.TdOrdExtRetMapper;
import com.pay.cloud.pay.trade.dao.TdOrderMapper;
import com.pay.cloud.pay.trade.entity.TdOrdExtRet;
import com.pay.cloud.pay.trade.service.TdOrderRefundService;

@Service("tdOrderRefundServiceImpl")
public class TdOrderRefundServiceImpl implements TdOrderRefundService{
	
    @Autowired
    private TdOrderMapper tdOrderMapper;
    @Autowired
    private TdOrdExtRetMapper tdOrdExtRetMapper;

	public void saveTdOrdExtRetInfo(TdOrdExtRet tdOrdExtRet) throws Exception {
		tdOrdExtRetMapper.insertSelective(tdOrdExtRet);
	}

	public TdOrdExtRet findTdOrdExtRetByMerOrderId(Map<String, Object> params) throws Exception {
		return tdOrdExtRetMapper.selectByOrderCode(params) ;
	}

	public List<TdOrdExtRet> findTdOrdExtRetByRetTdId(Long retTdId) throws Exception {
		return tdOrdExtRetMapper.selectByRetTdId(retTdId);
	}

	public TdOrdExtRet findTdOrdExtRetByTdId(Long tdId) throws Exception {
		return tdOrdExtRetMapper.selectByPrimaryKey(tdId);
	}

	public Long findTdOrdCountByMerOrderId(Map<String, Object> params) throws Exception {
		long orderCount = 0;
		List<Long> orderCountList = tdOrderMapper.findTdOrderCountListByMerOrderId(params);
		for(Long count : orderCountList){
			orderCount = orderCount + count;
		}
		return orderCount;
	}

	public List<TdOrdExtRet> selectByRetList(Long tdId) throws Exception {
		return tdOrdExtRetMapper.selectByRetList(tdId);
	}
}
