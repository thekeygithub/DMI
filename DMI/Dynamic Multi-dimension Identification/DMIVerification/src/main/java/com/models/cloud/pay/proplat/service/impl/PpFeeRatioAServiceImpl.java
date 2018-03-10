package com.models.cloud.pay.proplat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.proplat.dao.PpFeeRatioAMapper;
import com.models.cloud.pay.proplat.entity.PpFeeRatioA;
import com.models.cloud.pay.proplat.service.PpFeeRatioAService;

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
