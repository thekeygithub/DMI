package com.pay.cloud.common.dict.dao;

import java.util.List;

import com.pay.cloud.common.dict.entity.DimBankCardBase;

public interface DimBankCardBaseMapper {

    List<DimBankCardBase> findDimBankCardBaseList() throws Exception;
}