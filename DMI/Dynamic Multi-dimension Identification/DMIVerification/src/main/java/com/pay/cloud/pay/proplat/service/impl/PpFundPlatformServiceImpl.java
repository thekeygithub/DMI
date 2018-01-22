package com.pay.cloud.pay.proplat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.proplat.dao.PpFundPlatformMapper;
import com.pay.cloud.pay.proplat.entity.PpFundPlatform;
import com.pay.cloud.pay.proplat.service.PpFundPlatformService;

import java.util.List;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
@Service("ppFundPlatformServiceImpl")
public class PpFundPlatformServiceImpl implements PpFundPlatformService {

    @Autowired
    private PpFundPlatformMapper ppFundPlatformMapper;

    public List<PpFundPlatform> findPpFundPlatformList() throws Exception {
        return ppFundPlatformMapper.findPpFundPlatformList();
    }
}
