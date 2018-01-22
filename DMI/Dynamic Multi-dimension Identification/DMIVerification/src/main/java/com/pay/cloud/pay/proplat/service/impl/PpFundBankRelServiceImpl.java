package com.pay.cloud.pay.proplat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.proplat.dao.PpFundBankRelMapper;
import com.pay.cloud.pay.proplat.entity.PpFundBankRel;
import com.pay.cloud.pay.proplat.service.PpFundBankRelService;

import java.util.List;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
@Service("ppFundBankRelServiceImpl")
public class PpFundBankRelServiceImpl implements PpFundBankRelService {

    @Autowired
    private PpFundBankRelMapper ppFundBankRelMapper;

    public List<PpFundBankRel> findPpFundBankRelList(PpFundBankRel ppFundBankRel) throws Exception {
        return ppFundBankRelMapper.findPpFundBankRelList(ppFundBankRel);
    }
}
