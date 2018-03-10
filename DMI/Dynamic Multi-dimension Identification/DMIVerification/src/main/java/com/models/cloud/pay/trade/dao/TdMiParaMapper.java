package com.models.cloud.pay.trade.dao;

import com.models.cloud.pay.trade.entity.TdMiPara;

public interface TdMiParaMapper {

    int saveTdMiPara(TdMiPara tdMiPara) throws Exception;

    TdMiPara queryTdMiPara(Long tdId) throws Exception;

    int updateTdMiPara(TdMiPara tdMiPara) throws Exception;
}