package com.pay.cloud.pay.payuser.service;

import java.util.List;

import com.pay.cloud.pay.payuser.entity.ActPBank;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
public interface ActPBankService {

    ActPBank findActPBankById(Long bankActId) throws Exception;

    List<ActPBank> findActPBankList(ActPBank record) throws Exception;

    int saveActPBank(ActPBank record) throws Exception;

    int updateActPBank(ActPBank record) throws Exception;

    void updateStatusForUnbind(Long accountId, String bindId) throws Exception;
}
