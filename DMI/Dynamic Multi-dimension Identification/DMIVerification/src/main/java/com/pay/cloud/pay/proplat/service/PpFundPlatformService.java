package com.pay.cloud.pay.proplat.service;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpFundPlatform;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
public interface PpFundPlatformService {

    List<PpFundPlatform> findPpFundPlatformList() throws Exception;
}
