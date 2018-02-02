package com.models.cloud.pay.proplat.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.models.cloud.pay.payuser.dao.DimBankMapper;
import com.models.cloud.pay.payuser.entity.DimBank;
import com.models.cloud.pay.proplat.dao.PpFundBankRelMapper;
import com.models.cloud.pay.proplat.entity.PpFundBankRel;
import com.models.cloud.pay.proplat.service.BankImageService;
import com.models.cloud.util.DimDictEnum;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Propertie;

import java.util.List;

/**
 * 银行图片服务
 * Created by yacheng.ji on 2016/4/28.
 */
@Service("bankImageServiceImpl")
public class BankImageServiceImpl implements BankImageService {

    private static final Logger logger = Logger.getLogger(PayUserServiceGWImpl.class);

    @Autowired
    private DimBankMapper dimBankMapper;

    @Autowired
    private PpFundBankRelMapper ppFundBankRelMapper;

    /**
     * 获取银行图片Url
     * @param fundId 资金平台ID
     * @param bankCode 银行编码，如：ICBC、ABC
     * @param payActTypeId 支付账号类别 102-个人借记卡 103-个人信用卡
     * @return 图片URL
     * @throws Exception
     */
    public String getBankImageUrl(String fundId, String bankCode, String payActTypeId) throws Exception {

        DimBank dimBank = this.getDimBankInfo(fundId, bankCode, payActTypeId);
        if(null == dimBank || ValidateUtils.isEmpty(String.valueOf(dimBank.getBankImageId()).trim())){
            if(logger.isInfoEnabled()){
                logger.info("未找到有效的“银行定义表”相关信息或图片ID为空");
            }
            return "";
        }
        String bankImageUrl = Propertie.APPLICATION.value("ebaoPaySystem.bankIconUrl").replace("{bankImageId}", dimBank.getBankImageId().trim());
        if(logger.isInfoEnabled()){
            logger.info("获取的银行图片Url地址：" + bankImageUrl);
        }
        return bankImageUrl;
    }

    /**
     * 获取银行图片Url
     * @param bankImageId 银行图片ID
     * @return 图片URL
     * @throws Exception
     */
    public String getBankImageUrl(String bankImageId) throws Exception {
        if(ValidateUtils.isEmpty(bankImageId)){
            if(logger.isInfoEnabled()){
                logger.info("银行图片ID为空");
            }
            return "";
        }
        String bankImageUrl = Propertie.APPLICATION.value("ebaoPaySystem.bankIconUrl").replace("{bankImageId}", bankImageId.trim());
        if(logger.isInfoEnabled()){
            logger.info("获取的银行图片Url地址：" + bankImageUrl);
        }
        return bankImageUrl;
    }

    /**
     * 获取银行信息
     * @param fundId 资金平台ID
     * @param bankCode 银行编码，如：ICBC、ABC
     * @param payActTypeId 支付账号类别 102-个人借记卡 103-个人信用卡
     * @return 图片URL
     * @throws Exception
     */
    public DimBank getDimBankInfo(String fundId, String bankCode, String payActTypeId) throws Exception {
        fundId = String.valueOf(fundId).trim();
        bankCode = String.valueOf(bankCode).trim();
        payActTypeId = String.valueOf(payActTypeId).trim();
        if(logger.isInfoEnabled()){
            logger.info("获取银行图片ID fundId=" + fundId + ",bankCode=" + bankCode + ",payActTypeId=" + payActTypeId);
        }
        if(ValidateUtils.isEmpty(fundId) || ValidateUtils.isEmpty(bankCode) || ValidateUtils.isEmpty(payActTypeId)){
            return null;
        }
        PpFundBankRel ppFundBankRel = new PpFundBankRel();
        ppFundBankRel.setFundId(fundId);
        ppFundBankRel.setPpBankCode(bankCode);
        ppFundBankRel.setPayActTypeId(Short.valueOf(payActTypeId));
        ppFundBankRel.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
        List<PpFundBankRel> ppFundBankRelList = ppFundBankRelMapper.findPpFundBankRelList(ppFundBankRel);
        if(null == ppFundBankRelList || ppFundBankRelList.size() == 0){
            if(logger.isInfoEnabled()){
                logger.info("未获取到“资金平台支持银行卡类型”相关数据");
            }
            return null;
        }
        return dimBankMapper.findDimBankByBankId(ppFundBankRelList.get(0).getBankId());
    }

    public String getBankImageUrl(Long bankId) throws Exception {
        DimBank dimBank = dimBankMapper.findDimBankByBankId(bankId);
        if(null == dimBank || ValidateUtils.isEmpty(dimBank.getBankImageId())){
            return "";
        }
        return this.getBankImageUrl(dimBank.getBankImageId().trim());
    }
}
