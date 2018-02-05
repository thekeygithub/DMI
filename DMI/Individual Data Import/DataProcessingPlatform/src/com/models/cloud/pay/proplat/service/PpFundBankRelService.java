package com.models.cloud.pay.proplat.service;

import java.util.List;

import com.models.cloud.pay.proplat.entity.PpFundBankRel;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
public interface PpFundBankRelService {

    List<PpFundBankRel> findPpFundBankRelList(PpFundBankRel ppFundBankRel) throws Exception;
}
