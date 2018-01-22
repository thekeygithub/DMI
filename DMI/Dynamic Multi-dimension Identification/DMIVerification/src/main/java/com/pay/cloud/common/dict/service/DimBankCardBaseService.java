package com.pay.cloud.common.dict.service;

import java.util.List;

import com.pay.cloud.common.dict.entity.DimBankCardBase;

/**
 * Created by yacheng.ji on 2017/2/10.
 */
public interface DimBankCardBaseService {

    List<DimBankCardBase> findDimBankCardBaseList() throws Exception;
}