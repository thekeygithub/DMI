package com.pay.cloud.pay.payuser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.pay.payuser.dao.LogSmsMapper;
import com.pay.cloud.pay.payuser.entity.LogSms;
import com.pay.cloud.pay.payuser.service.LogSmsService;

/**
 * Created by yacheng.ji on 2017/1/5.
 */
@Service("logSmsServiceImpl")
public class LogSmsServiceImpl implements LogSmsService {

    @Autowired
    private LogSmsMapper logSmsMapper;

    public int saveLogSms(LogSms record) throws Exception {
        return logSmsMapper.saveLogSms(record);
    }
}
