package com.models.cloud.pay.payuser.dao;

import com.models.cloud.pay.payuser.entity.LogSms;

public interface LogSmsMapper {

    int saveLogSms(LogSms record) throws Exception;
}