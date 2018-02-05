package com.models.cloud.pay.proplat.dao;

import java.util.List;

import com.models.cloud.pay.proplat.entity.PpFundPlatform;

public interface PpFundPlatformMapper {

    List<PpFundPlatform> findPpFundPlatformList() throws Exception;
}