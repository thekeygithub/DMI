package com.models.cloud.gw.service.trade;

import com.models.cloud.pay.trade.entity.TdOrdExtWithdr;

/**
 * Created by yacheng.ji on 2016/12/16.
 */
public interface TdOrdExtWithdrService {

    TdOrdExtWithdr findTdOrdExtWithdr(Long tdId) throws Exception;

    int updateWithdrUpdDate(Long tdId) throws Exception;
}
