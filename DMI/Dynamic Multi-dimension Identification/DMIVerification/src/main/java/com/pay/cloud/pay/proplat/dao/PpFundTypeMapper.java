package com.pay.cloud.pay.proplat.dao;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpFundType;

public interface PpFundTypeMapper {

    List<PpFundType> findPpFundTypeList() throws Exception;
}