package com.pay.cloud.pay.proplat.dao;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpFundBankRel;

public interface PpFundBankRelMapper {

    List<PpFundBankRel> findPpFundBankRelList(PpFundBankRel ppFundBankRel) throws Exception;
}