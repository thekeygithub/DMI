package com.models.cloud.gw.service.trade;

import java.util.Map;

import com.models.cloud.pay.trade.entity.TdOrder;

public interface PayOrderServiceGW {

    /**
     * 查询交易单
     * @param inputMap
     * @return
     * @throws Exception
     */
	Map<String, Object> queryOrder(Map<String, Object> inputMap) throws Exception;

}
