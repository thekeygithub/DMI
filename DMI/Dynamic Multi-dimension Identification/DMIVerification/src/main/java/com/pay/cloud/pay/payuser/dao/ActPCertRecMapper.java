package com.pay.cloud.pay.payuser.dao;

import java.util.List;

import com.pay.cloud.pay.payuser.entity.ActPCertRec;

public interface ActPCertRecMapper {

    int insertSelective(ActPCertRec record);

    ActPCertRec selectByPrimaryKey(Long certRecId);

    /**
     * BANK_ACCOUNT  AUTH_MOBILE  P_ID_NO  CERT_RES_FLAG  ACT_NAME
     * @param record
     * @return
     */
    List<ActPCertRec> selectByFlagTrue(ActPCertRec record);
}