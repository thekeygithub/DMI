package com.pay.cloud.pay.supplier.dao;

import com.pay.cloud.pay.supplier.entity.ActSpFin;

public interface ActSpFinMapper {

    int insert(ActSpFin record);

    ActSpFin selectByActId(Long actId);

    int updateByActId(ActSpFin record);
}