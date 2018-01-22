package com.pay.cloud.pay.payuser.dao;

import com.pay.cloud.pay.payuser.entity.ActPFin;

public interface ActPFinMapper {

    ActPFin findActPFinById(Long actId);
    
    int saveActPFin(ActPFin actPFin);
}