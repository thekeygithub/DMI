package com.models.cloud.common.dict.dao;

import java.util.List;

import com.models.cloud.common.dict.entity.DimBankCardBase;

public interface DimBankCardBaseMapper {

    List<DimBankCardBase> findDimBankCardBaseList() throws Exception;
}