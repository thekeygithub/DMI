package com.pay.cloud.pay.proplat.service;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpFeeRatioA;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
public interface PpFeeRatioAService {

    List<PpFeeRatioA> findPpFeeRatioAInfoList(PpFeeRatioA ppFeeRatioA) throws Exception;
}
