package com.models.cloud.pay.escrow.huiyue.service.impl;

import com.huiyue.verify.api.VerifyFun;
import com.huiyue.verify.util.NumberUtil;
import com.models.cloud.core.common.JsonStringUtils;
import com.models.cloud.pay.escrow.huiyue.constant.HyConstant;
import com.models.cloud.pay.escrow.huiyue.model.HyRequest;
import com.models.cloud.pay.escrow.huiyue.model.HyResponse;
import com.models.cloud.pay.escrow.huiyue.service.HuiYueService;
import com.models.cloud.util.CacheUtils;
import com.models.cloud.util.CipherAesEnum;
import com.models.cloud.util.EncryptUtils;
import com.models.cloud.util.ValidateUtils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 慧阅验证服务Impl
 * Created by yacheng.ji on 2017/3/9.
 */
@Service("huiYueServiceImpl")
public class HuiYueServiceImpl implements HuiYueService {

    private static final Logger logger = Logger.getLogger(HuiYueServiceImpl.class);

    public HyResponse realPersonVerifySyncScore(HyRequest hyRequest) throws Exception {
        if(logger.isInfoEnabled()){
            logger.info("HuiYueServiceImpl --> realPersonVerifySyncScore 输入参数：" + JsonStringUtils.objectToJsonString(hyRequest));
        }
        if(ValidateUtils.isEmpty(hyRequest.getUserName())){
            return new HyResponse(HyConstant.RESPONSE_RETURN_CODE_FAIL, "用户姓名为空");
        }
        if(ValidateUtils.isEmpty(hyRequest.getIdNumber())){
            return new HyResponse(HyConstant.RESPONSE_RETURN_CODE_FAIL, "身份证号为空");
        }
        if(ValidateUtils.isEmpty(hyRequest.getUserPhotoBase64())){
            return new HyResponse(HyConstant.RESPONSE_RETURN_CODE_FAIL, "用户照片为空");
        }
        String userName = String.valueOf(hyRequest.getUserName()).trim();
        String idNumber = String.valueOf(hyRequest.getIdNumber()).trim();
        String userPhotoBase64 = String.valueOf(hyRequest.getUserPhotoBase64()).trim();
        String cpId = EncryptUtils.aesDecrypt(CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_VERIFY_CPID), CipherAesEnum.PPINTFPRAR);
        String transNo = NumberUtil.getTransNo();
        String desPwd = EncryptUtils.aesDecrypt(CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_VERIFY_3DES_PWD), CipherAesEnum.PPINTFPRAR);
        String ivps = EncryptUtils.aesDecrypt(CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_VERIFY_IVPS), CipherAesEnum.PPINTFPRAR);
        String md5Pwd = EncryptUtils.aesDecrypt(CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_VERIFY_MD5_PWD), CipherAesEnum.PPINTFPRAR);
        String url = CacheUtils.getDimSysConfConfValue(HyConstant.HY_REAL_PERSON_VERIFY_URL);
        if(logger.isInfoEnabled()){
            logger.info("请求慧阅实人验证接口[url=" + url + "] 输入参数：userName=" + userName + ",idNumber=" + idNumber + ",transNo=" + transNo);
        }
        VerifyFun verifyFun = new VerifyFun();
        String resultStr = verifyFun.RealPersonVerifySyncScore(userName, idNumber, cpId, transNo, desPwd, ivps, md5Pwd, userPhotoBase64, url);
        if(logger.isInfoEnabled()){
            logger.info("接口响应报文：" + resultStr);
        }
        if(ValidateUtils.isEmpty(resultStr)){
            return new HyResponse(HyConstant.RESPONSE_RETURN_CODE_FAIL, "响应报文为空");
        }
        return JsonStringUtils.jsonToObjectIgnorePro(resultStr, HyResponse.class);
    }
}
