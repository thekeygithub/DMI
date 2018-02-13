package com.models.cloud.pay.proplat.dao;

import java.util.List;

import com.models.cloud.pay.proplat.entity.PpFeeRatioA;

public interface PpFeeRatioAMapper {

    List<PpFeeRatioA> findPpFeeRatioAInfoList(PpFeeRatioA ppFeeRatioA) throws Exception;
}