package com.pay.cloud.pay.payuser.service;

import com.pay.cloud.pay.payuser.entity.ActPerson;

/**
 * Created by yacheng.ji on 2016/5/16.
 */
public interface ActPersonService {

    int updateActPersonInfo(ActPerson actPerson) throws Exception;
    /**
     * 
     * findActPersonById(由主键查询平台私人帐户) 
     * @param actId
     * @throws Exception  ActPerson 
     * @author wenhui.chen
     * @since  1.0.0
     */
    ActPerson findActPersonById(Long actId) throws Exception;
}
