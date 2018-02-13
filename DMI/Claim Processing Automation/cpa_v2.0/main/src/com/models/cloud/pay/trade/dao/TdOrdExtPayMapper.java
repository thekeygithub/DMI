package com.models.cloud.pay.trade.dao;

import com.models.cloud.pay.trade.entity.TdOrdExtPay;

public interface TdOrdExtPayMapper {

    int saveTdOrdExtPay(TdOrdExtPay record) throws Exception;

    TdOrdExtPay findTdOrdExtPay(Long tdId) throws Exception;

    int updateTdOrdExtPay(TdOrdExtPay record) throws Exception;
}