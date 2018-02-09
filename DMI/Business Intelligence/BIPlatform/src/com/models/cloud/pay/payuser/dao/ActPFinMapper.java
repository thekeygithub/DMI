package com.models.cloud.pay.payuser.dao;

import com.models.cloud.pay.payuser.entity.ActPFin;

public interface ActPFinMapper {

    ActPFin findActPFinById(Long actId);
    
    int saveActPFin(ActPFin actPFin);
}