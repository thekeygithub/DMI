package com.models.cloud.pay.supplier.dao;

import java.util.List;

import com.models.cloud.pay.supplier.entity.ActSpBank;

public interface ActSpBankMapper {
    int deleteByPrimaryKey(Long bankActId);

    int insert(ActSpBank record);

    List<ActSpBank> getListByActId(Long actId);
    
    /**
     * 
     * @Description: 根据账户id查询账户的银行账号
     * @Title: selectByActId 
     * @param actId
     * @return ActSpBank
     */
    ActSpBank selectByActId(Long actId);
    
    /**
     * 
     * @Description: 根据银行卡号查询
     * @Title: selectByBankAccount 
     * @param record
     * @return ActSpBank
     */
    ActSpBank selectByBankAccount(ActSpBank record);

    void updateBankAccountValid(ActSpBank record);
    
   // int getCountByActId(Long actId);
}