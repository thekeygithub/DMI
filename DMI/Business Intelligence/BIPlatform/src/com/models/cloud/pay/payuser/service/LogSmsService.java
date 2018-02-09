package com.models.cloud.pay.payuser.service;

import com.models.cloud.pay.payuser.entity.LogSms;

/**
 * Created by yacheng.ji on 2017/1/5.
 */
public interface LogSmsService {

    int saveLogSms(LogSms record) throws Exception;
}
