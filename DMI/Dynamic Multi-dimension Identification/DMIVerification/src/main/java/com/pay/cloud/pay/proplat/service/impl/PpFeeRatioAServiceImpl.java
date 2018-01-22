package com.pay.cloud.pay.proplat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.proplat.dao.PpFeeRatioAMapper;
import com.pay.cloud.pay.proplat.entity.PpFeeRatioA;
import com.pay.cloud.pay.proplat.service.PpFeeRatioAService;

import java.util.List;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
@Service("ppFeeRatioAServiceImpl")
public class PpFeeRatioAServiceImpl implements PpFeeRatioAService {

    @Autowired
    private PpFeeRatioAMapper ppFeeRatioAMapper;

    public List<PpFeeRatioA> findPpFeeRatioAInfoList(PpFeeRatioA ppFeeRatioA) throws Exception {
        return ppFeeRatioAMapper.findPpFeeRatioAInfoList(ppFeeRatioA);
    }
}
