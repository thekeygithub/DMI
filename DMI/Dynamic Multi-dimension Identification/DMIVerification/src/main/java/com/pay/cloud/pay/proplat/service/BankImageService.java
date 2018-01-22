package com.pay.cloud.pay.proplat.service;

import com.pay.cloud.pay.payuser.entity.DimBank;

/**
 * 银行图片服务
 * Created by yacheng.ji on 2016/4/28.
 */
public interface BankImageService {

    /**
     * 获取银行图片Url
     * @param fundId 资金平台ID
     * @param bankCode 银行编码，如：ICBC、ABC
     * @param payActTypeId 支付账号类别 102-个人借记卡 103-个人信用卡
     * @return 图片URL
     * @throws Exception
     */
    String getBankImageUrl(String fundId, String bankCode, String payActTypeId) throws Exception;

    /**
     * 获取银行图片Url
     * @param bankImageId 银行图片ID
     * @return 图片URL
     * @throws Exception
     */
    String getBankImageUrl(String bankImageId) throws Exception;

    /**
     * 获取银行信息
     * @param fundId 资金平台ID
     * @param bankCode 银行编码，如：ICBC、ABC
     * @param payActTypeId 支付账号类别 102-个人借记卡 103-个人信用卡
     * @return 图片URL
     * @throws Exception
     */
    DimBank getDimBankInfo(String fundId, String bankCode, String payActTypeId) throws Exception;

    String getBankImageUrl(Long bankId) throws Exception;
}
