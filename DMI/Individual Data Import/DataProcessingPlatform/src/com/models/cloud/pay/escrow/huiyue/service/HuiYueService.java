package com.models.cloud.pay.escrow.huiyue.service;

import com.models.cloud.pay.escrow.huiyue.model.HyRequest;
import com.models.cloud.pay.escrow.huiyue.model.HyResponse;

/**
 * 慧阅验证服务
 * Created by yacheng.ji on 2017/3/9.
 */
public interface HuiYueService {

    HyResponse realPersonVerifySyncScore(HyRequest hyRequest) throws Exception;
}
