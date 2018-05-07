package com.models.cloud.pay.payuser.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.pay.payuser.dao.ActPBankMapper;
import com.models.cloud.pay.payuser.entity.ActPBank;
import com.models.cloud.pay.payuser.service.ActPBankService;
import com.models.cloud.util.CacheUtils;
import com.models.cloud.util.DimDictEnum;

import java.util.List;

/**
 * Created by yacheng.ji on 2016/5/12.
 */
@Service("actPBankServiceImpl")
public class ActPBankServiceImpl implements ActPBankService {

    private static final Logger logger = Logger.getLogger(ActPBankServiceImpl.class);

    @Autowired
    private ActPBankMapper actPBankMapper;

    public ActPBank findActPBankById(Long bankActId) throws Exception {
        return actPBankMapper.findActPBankById(bankActId);
    }

    public List<ActPBank> findActPBankList(ActPBank record) throws Exception {
        return actPBankMapper.findActPBankList(record);
    }

    public int saveActPBank(ActPBank record) throws Exception {
        return actPBankMapper.saveActPBank(record);
    }

    public int updateActPBank(ActPBank record) throws Exception {
        return actPBankMapper.updateActPBank(record);
    }

    public void updateStatusForUnbind(Long accountId, String bindId) throws Exception {
        //更新状态act_p_bank状态为无效
        ActPBank selectActPBank = new ActPBank();
        selectActPBank.setActId(accountId);
        selectActPBank.setFromActTypeId(Short.valueOf(String.valueOf(DimDictEnum.ACCOUNT_TYPE_PRIVATE.getCode())));
        selectActPBank.setFundId(String.valueOf(CacheUtils.getDimSysConfConfValue(BaseDictConstant.CUR_PP_FUND_ID)).trim());
        selectActPBank.setPayPropC(bindId);
        selectActPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
        if(logger.isInfoEnabled()){
            logger.info("检查私人银行账号信息是否存在");
        }
        List<ActPBank> actPBankList = this.findActPBankList(selectActPBank);
        if(null != actPBankList && actPBankList.size() > 0){
            ActPBank updActPBank = new ActPBank();
            updActPBank.setBankActId(actPBankList.get(0).getBankActId());
            updActPBank.setValidFlag(Short.valueOf(String.valueOf(DimDictEnum.VALID_FLAG_INVALID.getCode())));
            if(logger.isInfoEnabled()){
                logger.info("<<存在，开始更新状态为无效 actPBankList.size=" + actPBankList.size());
            }
            this.updateActPBank(updActPBank);
            if(logger.isInfoEnabled()){
                logger.info("<<更新成功");
            }
        }else{
            if(logger.isInfoEnabled()){
                logger.info("<<不存在，无需处理");
            }
        }
    }
}
