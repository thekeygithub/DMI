package com.pay.cloud.pay.payuser.dao;

import java.util.List;

import com.pay.cloud.pay.payuser.entity.ActPBank;

public interface ActPBankMapper {

    ActPBank findActPBankById(Long bankActId) throws Exception;

    List<ActPBank> findActPBankList(ActPBank record) throws Exception;

    int saveActPBank(ActPBank record) throws Exception;

    int updateActPBank(ActPBank record) throws Exception;
}