package com.pay.cloud.pay.proplat.service;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpFundType;

/**
 * Created by yacheng.ji on 2016/8/23.
 */
public interface PpFundTypeService {

    List<PpFundType> findPpFundTypeList() throws Exception;
}