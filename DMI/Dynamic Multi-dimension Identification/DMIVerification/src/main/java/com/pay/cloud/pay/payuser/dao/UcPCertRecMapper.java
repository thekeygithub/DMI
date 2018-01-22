package com.pay.cloud.pay.payuser.dao;

import com.pay.cloud.pay.payuser.entity.UcPCertRec;

public interface UcPCertRecMapper {
    int deleteByPrimaryKey(Long pCertId);

    int insert(UcPCertRec record);

    int insertSelective(UcPCertRec record);

    UcPCertRec selectByPrimaryKey(Long pCertId);
    
    /**
     * 通过身份证号码查询
     * @param pCertId
     * @return
     */
    UcPCertRec selectByPCertNo(String pCertId);

    int updateByPrimaryKeySelective(UcPCertRec record);

    int updateByPrimaryKey(UcPCertRec record);
}