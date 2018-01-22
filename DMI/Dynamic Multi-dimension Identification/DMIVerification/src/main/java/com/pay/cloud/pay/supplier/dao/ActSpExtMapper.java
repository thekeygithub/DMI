package com.pay.cloud.pay.supplier.dao;

import com.pay.cloud.pay.supplier.entity.ActSpExt;

public interface ActSpExtMapper {

    int saveActSpExt(ActSpExt record);

    ActSpExt findActSpExtByActId(Long actId);

    int updateActSpExtByActId(ActSpExt record);
}