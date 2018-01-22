package com.pay.cloud.pay.proplat.dao;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpFundPlatform;

public interface PpFundPlatformMapper {

    List<PpFundPlatform> findPpFundPlatformList() throws Exception;
}