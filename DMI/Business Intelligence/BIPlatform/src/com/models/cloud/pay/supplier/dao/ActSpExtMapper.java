package com.models.cloud.pay.supplier.dao;

import com.models.cloud.pay.supplier.entity.ActSpExt;

public interface ActSpExtMapper {

    int saveActSpExt(ActSpExt record);

    ActSpExt findActSpExtByActId(Long actId);

    int updateActSpExtByActId(ActSpExt record);
}