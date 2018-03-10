package com.models.cloud.pay.proplat.dao;

import java.util.List;

import com.models.cloud.pay.proplat.entity.PpFundType;

public interface PpFundTypeMapper {

    List<PpFundType> findPpFundTypeList() throws Exception;
}