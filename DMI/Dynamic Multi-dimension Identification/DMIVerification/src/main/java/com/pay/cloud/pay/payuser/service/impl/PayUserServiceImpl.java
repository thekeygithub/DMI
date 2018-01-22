package com.pay.cloud.pay.payuser.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.alibaba.fastjson.JSON;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.cert.info.CertResult;
import com.pay.cloud.cert.info.EbaoFourParam;
import com.pay.cloud.cert.server.CertServer;
import com.pay.cloud.cert.utils.IdCardNoUtils;
import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.pay.escrow.mi.pangu.request.PersonReq;
import com.pay.cloud.pay.escrow.mi.pangu.response.PersonInfoRes;
import com.pay.cloud.pay.escrow.mi.pangu.service.CommInfoService;
import com.pay.cloud.pay.payuser.dao.ActPCertRecMapper;
import com.pay.cloud.pay.payuser.dao.ActPFinMapper;
import com.pay.cloud.pay.payuser.dao.ActPersonMapper;
import com.pay.cloud.pay.payuser.dao.DimBankMapper;
import com.pay.cloud.pay.payuser.dao.LogOperActMapper;
import com.pay.cloud.pay.payuser.dao.LogPayUserLoginMapper;
import com.pay.cloud.pay.payuser.dao.PayUserDeviceMapper;
import com.pay.cloud.pay.payuser.dao.PayUserMapper;
import com.pay.cloud.pay.payuser.dao.UcPCertRecMapper;
import com.pay.cloud.pay.payuser.entity.ActPCertRec;
import com.pay.cloud.pay.payuser.entity.ActPFin;
import com.pay.cloud.pay.payuser.entity.ActPerson;
import com.pay.cloud.pay.payuser.entity.DimBank;
import com.pay.cloud.pay.payuser.entity.LogOperAct;
import com.pay.cloud.pay.payuser.entity.LogPayUserLogin;
import com.pay.cloud.pay.payuser.entity.PayUser;
import com.pay.cloud.pay.payuser.entity.PayUserDevice;
import com.pay.cloud.pay.payuser.entity.UcPCertRec;
import com.pay.cloud.pay.payuser.service.PayUserService;
import com.pay.cloud.pay.seq.service.SeqService;
import com.pay.cloud.pay.supplier.dao.ActSpMapper;
import com.pay.cloud.pay.supplier.entity.ActSp;
import com.pay.cloud.util.*;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.util.hint.Propertie;

@Service("payUserServiceImpl")
public class PayUserServiceImpl implements PayUserService {

    private static final Logger logger = Logger.getLogger(PayUserServiceImpl.class);

    @Autowired
    private PayUserMapper payUserMapper;
    @Autowired
    private ActPersonMapper actPersonMapper;
    @Autowired
    private LogPayUserLoginMapper logPayUserLoginMapper;
    @Autowired
    private DimBankMapper dimBankMapper;
    @Autowired
    private ActPFinMapper actPFinMapper;
    @Autowired
    private PayUserDeviceMapper payUserDeviceMapper;
    @Autowired
    private LogOperActMapper logOperActMapper;
    @Autowired
    private ActSpMapper actSpMapper;
    @Autowired
    private ActPCertRecMapper actPCertRecMapper;
    @Resource(name="redisService")
    private RedisService redisService;
    @Resource(name="certServerImpl")
    private CertServer CertServer;
    @Resource(name="ucPCertRecMapper")
    private UcPCertRecMapper ucPCertRecMapper;
    @Resource(name="seqServiceImpl")
    private SeqService seqServiceImpl;
    @Resource
    private CommInfoService commInfoServiceImpl;

    /**
     * 验证C端用户合法性
     * @param accountId
     * @return
     * @throws Exception
     */
    public Map<String, Object> checkPayUserValidity(String accountId, String hardwareId, boolean isCheckRealName) throws Exception {
        logger.info("PayUserServiceImpl --> checkPayUserValidity 输入参数：accountId=" + accountId + ",hardwareId=" + hardwareId);
        Map<String, Object> resultMap = new HashMap<>();
        PayUser selectPayUser = new PayUser();
        selectPayUser.setActId(Long.valueOf(accountId));
        PayUser payUser = payUserMapper.findPayUserInfo(selectPayUser);
        if(null == payUser){
            resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
            resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
            return resultMap;
        }
        if(payUser.getPayUserStatId() != DimDictEnum.PAY_USER_STAT_NORMAL.getCode()){
            resultMap.put("resultCode", Hint.USER_11002_USER_STATUS_INVALID.getCodeString());
            resultMap.put("resultDesc", Hint.USER_11002_USER_STATUS_INVALID.getMessage());
            return resultMap;
        }
        ActPerson actPerson = actPersonMapper.findActPersonById(Long.valueOf(accountId));
        if(null == actPerson){
            resultMap.put("resultCode", Hint.USER_11003_ACCOUNTINFO_NOTEXIST.getCodeString());
            resultMap.put("resultDesc", Hint.USER_11003_ACCOUNTINFO_NOTEXIST.getMessage());
            return resultMap;
        }
        if(actPerson.getActStatId() != DimDictEnum.ACCOUNT_STAT_NORMAL.getCode()){
            resultMap.put("resultCode", Hint.USER_11004_ACCOUNT_STATUS_INVALID.getCodeString());
            resultMap.put("resultDesc", Hint.USER_11004_ACCOUNT_STATUS_INVALID.getMessage());
            return resultMap;
        }
        if(isCheckRealName){
            if(null == actPerson.getRealNameFlag() || actPerson.getRealNameFlag() != 1){
                resultMap.put("resultCode", Hint.USER_11012_NOT_REAL_NAME.getCodeString());
                resultMap.put("resultDesc", Hint.USER_11012_NOT_REAL_NAME.getMessage());
                return resultMap;
            }
        }
        resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
        resultMap.put("payUser", payUser);
        resultMap.put("actPerson", actPerson);
        return resultMap;
    }

    public PayUser findByPayUserPhone(String phone) throws Exception {
        return payUserMapper.findByPayUserPhone(phone);
    }

    @Override
    public PayUser findByPayUserId(String userId) throws Exception {
        return payUserMapper.findByPayUserId(userId);
    }

    @Override
    public PayUser findByPayUserActId(String actId) throws Exception {
        return payUserMapper.findByPayUserActId(actId);
    }

    public int savePayUser(PayUser payUser) throws Exception {
        return payUserMapper.savePayUser(payUser);
    }

    @Override
    public int updatePassErrorCount(Long payUserId, int num) throws Exception {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("payUserId", payUserId);
        map.put("num", num);
        return payUserMapper.updatePassErrorCount(map);
    }

    @Override
    public int saveLoginLog(int loginResFlag,int loginTypeId, String message, Map<String, Object> receiveMap, PayUser user)
            throws Exception {
        LogPayUserLogin record=new LogPayUserLogin();

        record.setLoginTypeId(ConvertUtils.getShort(loginTypeId));//登录日志类型
        record.setPayUserId(user.getPayUserId());//支付平台用户ID
        record.setPayUserCode(user.getPayUserCode());//登录名
        record.setActId(user.getActId());//个人账户ID
        record.setMobile(user.getMobile());//手机号码
        record.setLoginResFlag(ConvertUtils.getShort(loginResFlag));//登录结果状态
//		record.setLoginFailTypeId(loginFailTypeId);//登录失败原因类型	
        record.setFailDesc(message);//失败描述
//		record.setUserAlias(userAlias);//终端别名		
        record.setDeviceCode(ConvertUtils.getString(receiveMap.get("hardwareId")));//终端设备号
//		record.setDeviceLabel(deviceLabel);//终端标签		
        record.setLogTime(user.getLastLoginTime());//日志时间
        record.setClientIp(ConvertUtils.getString(receiveMap.get("ipInfo")));//客户端IP
        record.setClientVer("");//客户端版本	--
        record.setPhoneModel("");//手机型号	--
        record.setPhoneScreen("");//手机屏幕分辨率--
        String uaInfo=ConvertUtils.getString(receiveMap.get("uaInfo"));
        if(uaInfo.length()>120){
            uaInfo=uaInfo.substring(0, 120);
        }
        record.setPhoneSysVer(uaInfo);//手机操作系统版本
        record.setPhoneOsTypeId(ConvertUtils.getShort(receiveMap.get("terminalType")));//交易操作通道类别,11-Android 12-IOS 21-微信 31-WEB
        record.setPhoneOper("");//手机运营商	--
        record.setPhoneNetType("");//手机联网方式	--
        return logPayUserLoginMapper.insert(record);
    }
    public static void main(String[] args) {
        System.out.println("".substring(0, 120));
    }
    @Override
    public int restLoginPass(String phone, String password) throws Exception {
        PayUser u=new PayUser();
        u.setMobile(phone);
        u.setPwd(password);
        return payUserMapper.restLoginPass(u);
    }

    @Override
    public int saveActPerson(ActPerson actPerson) throws Exception {
        return actPersonMapper.saveActPerson(actPerson);
    }

    @Override
    public int updatePaymentPass(Long actId, String payPwd) throws Exception {
        ActPerson actPerson=new ActPerson();
        actPerson.setActId(actId);
        actPerson.setPayPwd(payPwd);
        return actPersonMapper.updatePaymentPass(actPerson);
    }

    /**
     *
     * @Description: 获取支持的银行卡列表
     * @Title: queryDimBankList
     * @param dimBank
     * @return List<DimBank>
     * @throws Exception
     */
    public List<DimBank> queryDimBankList(DimBank dimBank) throws Exception {
        return dimBankMapper.queryDimBankList(dimBank);
    }

    @Override
    public Map<String, Object> queryBankCardList(DimBank dimBank) throws Exception {
        Map<String, Object> maplist=new HashMap<String, Object>();
        List<DimBank> li=dimBankMapper.queryDimBankList(dimBank);
        String bankIconUrl="";
        for (Iterator<DimBank> iterator = li.iterator(); iterator.hasNext();) {
            DimBank db=iterator.next();
            bankIconUrl=Propertie.APPLICATION.value("ebaoPaySystem.bankIconUrl").replace("{bankImageId}", db.getBankImageId()+"");
            db.setBankImageUrl(bankIconUrl);
        }
        maplist.put("banklist", li);
        return maplist;
    }

    @Override
    public int saveActPFin(ActPFin actPFin) throws Exception {
        return actPFinMapper.saveActPFin(actPFin);
    }

    @Override
    public int updateLastLoginTime(PayUser payUser) throws Exception {
        return payUserMapper.updateLastLoginTime(payUser);
    }

    @Override
    public int loginOut(Long payUserId) throws Exception {
        return payUserMapper.loginOut(payUserId);
    }

    @Override
    public ActPerson findActPersonById(Long actId) throws Exception {
        return actPersonMapper.findActPersonById(actId);
    }

    @Override
    public int savePayUserDevice(PayUserDevice payUserDevice) throws Exception {
        return payUserDeviceMapper.savePayUserDevice(payUserDevice);
    }

    @Override
    public PayUserDevice findPayUserDevice(PayUserDevice payUserDevice) throws Exception {
        return payUserDeviceMapper.findPayUserDevice(payUserDevice);
    }

    @Override
    public ActSp findByChannelAppId(String appId) throws Exception {
        return actSpMapper.findByChannelAppId(appId);
    }

    @Override
    public LogPayUserLogin selectByActIdLastLogin(Long actId) throws Exception {
        return logPayUserLoginMapper.selectByActIdLastLogin(actId);
    }

    @Override
    public int updatePayUserDevice(PayUserDevice payUserDevice) throws Exception {
        return payUserDeviceMapper.updatePayUserDevice(payUserDevice);
    }

    @Override
    public Map<String, Object> realNameImport(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String appId = String.valueOf(receiveMap.get("appId")).trim();
            ActSp channelSp = this.findByChannelAppId(appId);
            logger.info("查询登陆账号"+receiveMap.get("userId").toString());
            PayUser payUser = this.findByPayUserId(receiveMap.get("userId").toString());
            if(payUser!=null && payUser.getActId()!=null){

                ActPerson actPerson = this.findActPersonById(payUser.getActId());

                String paramCardNo = receiveMap.get("cardNo")==null?"":receiveMap.get("cardNo").toString();
                String paramCardName = receiveMap.get("cardName")==null?"":receiveMap.get("cardName").toString();
                String paramBankCode = EncryptUtils.aesEncrypt(ConvertUtils.getString(receiveMap.get("bankCode")), CipherAesEnum.CARDNOKEY);
                String paramPhone = receiveMap.get("phone").toString();
                String authType = ConvertUtils.getString(receiveMap.get("authType"));

                if(actPerson!=null){

                    String realNameFlag = actPerson.getRealNameFlag().toString();
                    //从实名记录里面查找是否存在 BANK_ACCOUNT  AUTH_MOBILE  P_ID_NO  CERT_RES_FLAG  ACT_NAME
                    ActPCertRec  actPCertRec = new ActPCertRec();
                    actPCertRec.setAuthMobile(paramPhone);
                    actPCertRec.setBankAccount(paramBankCode);
                    actPCertRec.setCertResFlag(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE);

                    if(realNameFlag.equals("1")){
                        logger.info("已实名认证");
                        resultMap.put("resultCode", Hint.CERT_31008_ERROR_ERROR.getCodeString());
                        resultMap.put("resultDesc", Hint.CERT_31008_ERROR_ERROR.getMessage());
                        return resultMap;
                    }else{
                        logger.info("未实名认证！");
                        //通过paramCardNo paramCardName paramBankCode paramPhone 去验证
                        if(paramCardNo.equals("")){
                            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
                            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "cardNo"));
                            return resultMap;
                        }
                        if(paramCardName.equals("")){
                            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
                            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "cardName"));
                            return resultMap;
                        }
                        actPCertRec.setpIdNo(paramCardNo.toUpperCase());
                        actPCertRec.setActName(paramCardName);
                    }

                    UcPCertRec ucPCertRec = ucPCertRecMapper.selectByPCertNo(paramCardNo);
                    if(ucPCertRec == null){
                        UcPCertRec ucPCertRecInsert = new UcPCertRec();
                        ucPCertRecInsert.setValidFlag(BaseDictConstant.UC_P_CERT_REC_VALID_FLAG_YES);
                        ucPCertRecInsert.setpCertNo(paramCardNo.toUpperCase());
                        ucPCertRecInsert.setpName(paramCardName);
                        ucPCertRecMapper.insert(ucPCertRecInsert);
                        ucPCertRec = ucPCertRecMapper.selectByPCertNo(paramCardNo);
                    }

                    if(ucPCertRec!=null){
                        //用户未认证，并且认证通过
                        ActPerson actPersonUpdate = new ActPerson();
                        actPersonUpdate.setActId(payUser.getActId());
                        actPersonUpdate.setRealNameFlag(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE);
                        actPersonUpdate.setpCertNo(paramCardNo.toUpperCase());
                        actPersonUpdate.setpGendId(IdCardNoUtils.getUserSexByIdNum(actPersonUpdate.getpCertNo()));
                        actPersonUpdate.setpBirth(IdCardNoUtils.getUserBirthByIdNum(actPersonUpdate.getpCertNo()));
                        actPersonUpdate.setpName(paramCardName);
                        actPersonUpdate.setAuthBankAccount(paramBankCode);
                        actPersonUpdate.setAuthMobile(paramPhone);
                        actPersonUpdate.setpCertId(ucPCertRec.getpCertId());
                        actPersonUpdate.setpCertTypeId(Short.valueOf(authType));//借用字段，保存实名认证通道类型
                        actPersonMapper.updateActPersonInfo(actPersonUpdate);
                    }else{
                        resultMap.put("resultCode", Hint.CERT_31009_PERSON_ID_ERROR.getCodeString());
                        resultMap.put("resultDesc", Hint.CERT_31009_PERSON_ID_ERROR.getMessage());
                        return resultMap;
                    }

                    ActPCertRec insertActPCertRec = new ActPCertRec();
                    insertActPCertRec.setActId(payUser.getActId());
                    insertActPCertRec.setBankAccount(actPCertRec.getBankAccount());
                    insertActPCertRec.setActName(actPCertRec.getActName());
                    insertActPCertRec.setAuthMobile(actPCertRec.getAuthMobile());
                    insertActPCertRec.setpIdNo(actPCertRec.getpIdNo().toUpperCase());
                    insertActPCertRec.setCertResFlag(actPCertRec.getCertResFlag());
                    insertActPCertRec.setErrInfo(actPCertRec.getErrInfo());
                    if(null != channelSp){
                        insertActPCertRec.setChanActId(channelSp.getActId());
                    }
                    insertActPCertRec.setRemark("0");
                    insertActPCertRec.setFromActTypeId(Short.valueOf(authType));
                    actPCertRecMapper.insertSelective(insertActPCertRec);//保存实名认证记录表

                    logger.info("实名认证通过");

                    resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
                    resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
                    return resultMap;
                }else{
                    resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
                    resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
                    return resultMap;
                }
            }else{
                resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
                resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
                return resultMap;
            }
        } catch (Exception ex) {

            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> realName(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String appId = String.valueOf(receiveMap.get("appId")).trim();
            ActSp channelSp = this.findByChannelAppId(appId);
            logger.info("查询登陆账号"+receiveMap.get("userId").toString());
            PayUser payUser = this.findByPayUserId(receiveMap.get("userId").toString());
            if(payUser!=null && payUser.getActId()!=null){

                ActPerson actPerson = this.findActPersonById(payUser.getActId());

                String paramCardNo = receiveMap.get("cardNo")==null?"":receiveMap.get("cardNo").toString();
                String paramCardName = receiveMap.get("cardName")==null?"":receiveMap.get("cardName").toString();
                String paramBankCode = EncryptUtils.aesEncrypt(receiveMap.get("bankCode").toString(), CipherAesEnum.CARDNOKEY);
                String paramPhone = receiveMap.get("phone").toString();
//				Hint hint=SmsUtil.smsCodeCheck(receiveMap.get("phone").toString(),receiveMap.get("hardwareId").toString(), receiveMap.get("verifyCode").toString(), redisService);
                String redisTokenKey = "realName_"+receiveMap.get("userId").toString();//验证码rediskey
                String redisRealNameErrorCountKey = "realName_error_count"+receiveMap.get("userId").toString();//实名认证错误次数key
                String realNameTokenTime = Propertie.APPLICATION.value("realName.token.time");

                //判断实名认证错误次数
                String redisRealNameErrorCountStr = String.valueOf(redisService.get(redisRealNameErrorCountKey)==null?"0":redisService.get(redisRealNameErrorCountKey)).trim();
                //实名认证最大错误次数
                String realNameErrorMaxCount = Propertie.APPLICATION.value("realName.error.maxCount")==null?"5":Propertie.APPLICATION.value("realName.error.maxCount");
                logger.info("实名认证错误次数："+redisRealNameErrorCountStr);
                logger.info("实名认证允许错误次数："+realNameErrorMaxCount);
                //判断做大错误次数
                if(Integer.parseInt(redisRealNameErrorCountStr)>=Integer.parseInt(realNameErrorMaxCount)){
                    resultMap.put("resultCode", Hint.CERT_31007_ERROR_ERROR.getCodeString());
                    resultMap.put("resultDesc", Hint.CERT_31007_ERROR_ERROR.getMessage().replace("{a}", redisRealNameErrorCountStr).replace("{b}", realNameErrorMaxCount));
                    return resultMap;
                }

//				if(hint.getCode()==0){
                logger.info("验证码通过");
                if(actPerson!=null){
						/*//判断是否是有效的卡号
						Map<String, String> yeepayResultMap = yeepayPaymentServiceImpl.bankCardCheck(EncryptUtils.aesDecrypt(paramBankCode, CipherAesEnum.CARDNOKEY));
						if(null != yeepayResultMap){
							yeepayResultMap.remove("cardno");
						}
						logger.info("卡信息为："+yeepayResultMap);
						if(yeepayResultMap!=null &&  yeepayResultMap.get("isvalid")!=null && yeepayResultMap.get("isvalid").toString().equals("1")){
							logger.info("是有效的卡号："+paramBankCode);
						}else{
							logger.info("无效的卡号："+paramBankCode);
							resultMap.put("resultCode", Hint.CERT_31002_BANK_CARD_ERROR.getCodeString());
							resultMap.put("resultDesc", Hint.CERT_31002_BANK_CARD_ERROR.getMessage());
							return resultMap;
						}*/

                    String realNameFlag = actPerson.getRealNameFlag().toString();
                    //从实名记录里面查找是否存在 BANK_ACCOUNT  AUTH_MOBILE  P_ID_NO  CERT_RES_FLAG  ACT_NAME
                    ActPCertRec actPCertRec = new ActPCertRec();
                    actPCertRec.setAuthMobile(paramPhone);
                    actPCertRec.setBankAccount(paramBankCode);
                    actPCertRec.setCertResFlag(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE);

                    if(realNameFlag.equals("1")){
                        logger.info("已实名认证");
                        String cardNo = actPerson.getpCertNo();
                        String cardName = actPerson.getpName();
                        //通过cardNo cardName paramBankCode paramPhone 去验证
                        actPCertRec.setpIdNo(cardNo.toUpperCase());
                        actPCertRec.setActName(cardName);
                    }else{
                        logger.info("未实名认证！");
                        //通过paramCardNo paramCardName paramBankCode paramPhone 去验证
                        if(paramCardNo.equals("")){
                            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
                            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "cardNo"));
                            return resultMap;
                        }
                        if(paramCardName.equals("")){
                            resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
                            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "cardName"));
                            return resultMap;
                        }
                        actPCertRec.setpIdNo(paramCardNo.toUpperCase());
                        actPCertRec.setActName(paramCardName);
                    }
                    String certMsg = Hint.TD_13036_ACT_PERSON_REAL_NAME_FALSE.getCodeString();//13036
                    String certCode = Hint.TD_13036_ACT_PERSON_REAL_NAME_FALSE.getMessage();
                    List<ActPCertRec> actPcertRecList = actPCertRecMapper.selectByFlagTrue(actPCertRec);
                    boolean isAuth = false;//是否实名认证通过
                    boolean isPuHui = false;//是否调用普惠
                    if(actPcertRecList!=null && actPcertRecList.size()>0){
                        isAuth = true;
                    }else{
                        isPuHui = true;
                        EbaoFourParam ebaoFourParam= new EbaoFourParam();
                        ebaoFourParam.setBankcard(EncryptUtils.aesDecrypt(actPCertRec.getBankAccount(), CipherAesEnum.CARDNOKEY));
                        ebaoFourParam.setIdcard(actPCertRec.getpIdNo());
                        ebaoFourParam.setMobile(actPCertRec.getAuthMobile());
                        ebaoFourParam.setRealname(actPCertRec.getActName());
                        CertResult certResult = CertServer.doCertFour(ebaoFourParam);
                        certMsg = certResult.getMsg();
                        certCode = certResult.getCode();
                        if(certCode!=null && CertResult.MATCH_CODE.equals(certCode)){
                            isAuth = true;
                            actPCertRec.setCertResFlag(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE);
                            actPCertRec.setErrInfo(certMsg);
                        }else{
                            actPCertRec.setCertResFlag(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_FALSE);
                            actPCertRec.setErrInfo(certMsg);
                        }
                        //actPCertRecMapper.insertSelective(actPCertRec);

                    }

//						ActPCertRec actPCertRecEntity = new ActPCertRec();
//						actPCertRecEntity.setActId(actId);
                    //如果账户未实名认证
                    if(actPerson.getRealNameFlag()!=null && actPerson.getRealNameFlag()==BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_FALSE && isAuth){

                        UcPCertRec ucPCertRec = ucPCertRecMapper.selectByPCertNo(paramCardNo);
                        if(ucPCertRec == null){
                            Date date=new Date();
                            UcPCertRec ucPCertRecInsert = new UcPCertRec();
                            ucPCertRecInsert.setValidFlag(BaseDictConstant.UC_P_CERT_REC_VALID_FLAG_YES);
                            ucPCertRecInsert.setpCertNo(paramCardNo.toUpperCase());
                            ucPCertRecInsert.setpName(paramCardName);
                            ucPCertRecInsert.setCrtTime(date);
                            ucPCertRecInsert.setUpdTime(date);
                            ucPCertRecMapper.insertSelective(ucPCertRecInsert);
                            ucPCertRec = ucPCertRecInsert;
                        }

                        if(null != ucPCertRec.getpCertId()){
                            //用户未认证，并且认证通过
                            ActPerson actPersonUpdate = new ActPerson();
                            actPersonUpdate.setActId(payUser.getActId());
                            actPersonUpdate.setRealNameFlag(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE);
                            actPersonUpdate.setpCertNo(paramCardNo.toUpperCase());
                            actPersonUpdate.setpGendId(IdCardNoUtils.getUserSexByIdNum(actPersonUpdate.getpCertNo()));
                            actPersonUpdate.setpBirth(IdCardNoUtils.getUserBirthByIdNum(actPersonUpdate.getpCertNo()));
                            actPersonUpdate.setpName(paramCardName);
                            actPersonUpdate.setAuthBankAccount(paramBankCode);
                            actPersonUpdate.setAuthMobile(paramPhone);
                            actPersonUpdate.setpCertId(ucPCertRec.getpCertId());
                            actPersonUpdate.setpCertTypeId(Short.valueOf(DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString()));//借用字段，保存实名认证通道类型
                            actPersonMapper.updateActPersonInfo(actPersonUpdate);
                        }else{
                            resultMap.put("resultCode", Hint.CERT_31009_PERSON_ID_ERROR.getCodeString());
                            resultMap.put("resultDesc", Hint.CERT_31009_PERSON_ID_ERROR.getMessage());
                            return resultMap;
                        }
                    }

                    //如果账户实名认证通过
                    if(actPerson.getRealNameFlag()!=null && actPerson.getRealNameFlag()==BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE && isAuth){
                        UcPCertRec ucPCertRec = ucPCertRecMapper.selectByPCertNo(actPerson.getpCertNo());
                        if(ucPCertRec!=null){
                            //用户未认证，并且认证通过
                            ActPerson actPersonUpdate = new ActPerson();
                            actPersonUpdate.setActId(payUser.getActId());
                            actPersonUpdate.setAuthBankAccount(paramBankCode);
                            actPersonUpdate.setAuthMobile(paramPhone);
                            actPersonUpdate.setpCertTypeId(Short.valueOf(DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString()));//借用字段，保存实名认证通道类型
                            actPersonMapper.updateActPersonAuthMobile(actPersonUpdate);
                        }else{
                            resultMap.put("resultCode", Hint.CERT_31009_PERSON_ID_ERROR.getCodeString());
                            resultMap.put("resultDesc", Hint.CERT_31009_PERSON_ID_ERROR.getMessage());
                            return resultMap;
                        }
                    }
                    String actName = actPCertRec.getActName();
                    String pidNo = actPCertRec.getpIdNo();
                    ActPCertRec insertActPCertRec = new ActPCertRec();
                    insertActPCertRec.setActId(payUser.getActId());
                    insertActPCertRec.setBankAccount(actPCertRec.getBankAccount());
                    insertActPCertRec.setActName(actPCertRec.getActName());
                    insertActPCertRec.setAuthMobile(actPCertRec.getAuthMobile());
                    insertActPCertRec.setpIdNo(actPCertRec.getpIdNo().toUpperCase());
                    insertActPCertRec.setCertResFlag(actPCertRec.getCertResFlag());
                    insertActPCertRec.setErrInfo(actPCertRec.getErrInfo());
                    insertActPCertRec.setFromActTypeId(Short.valueOf(DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString()));
                    if(null != channelSp){
                        insertActPCertRec.setChanActId(channelSp.getActId());
                    }
                    String remark = "0";//是否调用普惠进行实名认证 1-是 0否
                    if(isPuHui){
                        remark = "1";
                    }
                    insertActPCertRec.setRemark(remark);
                    actPCertRecMapper.insertSelective(insertActPCertRec);//保存实名认证记录表

                    if(isAuth){
                        logger.info("实名认证通过");
                        String randomString = Md5SaltUtils.getRandomString(32);
                        redisService.set(redisTokenKey, randomString);
                        redisService.expire(redisTokenKey, Integer.parseInt(realNameTokenTime) * 60);

                        resultMap.put("realNameName", "*"+actName.substring(1));
                        resultMap.put("realNameCardNo", pidNo.substring(0,1)+"****************"+pidNo.substring(pidNo.length()-1, pidNo.length()));
                        resultMap.put("token", randomString);
                        resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
                        resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
                        return resultMap;
                    }else{
                        //reids 基数错误次数

                        redisService.set(redisRealNameErrorCountKey,(Integer.parseInt(redisRealNameErrorCountStr)+1)+"");
                        redisService.expire(redisRealNameErrorCountKey, DateUtils.getDaySurplusSecond());

                        //认证失败
                        resultMap.put("resultCode", certCode);
                        resultMap.put("resultDesc", certMsg);
                        return resultMap;
                    }
                }else{
                    resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
                    resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
                    return resultMap;
                }
            }else{
                resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
                resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
                return resultMap;
            }
        } catch (Exception ex) {

            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> resetPayPasswordForRealName(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            logger.info("查询登陆账号"+receiveMap.get("userId").toString());
            PayUser payUser = this.findByPayUserId(receiveMap.get("userId").toString());
            if(payUser!=null && payUser.getActId()!=null){

                ActPerson actPerson = this.findActPersonById(payUser.getActId());
                if(actPerson!=null){
                    String token = receiveMap.get("token").toString();
                    logger.info("修改支付密码token："+token);
                    String redisTokenKey = "realName_"+receiveMap.get("userId").toString();
                    logger.info("redis key :"+redisTokenKey);
                    String realNameTokenStr = String.valueOf(redisService.get(redisTokenKey)==null?"":redisService.get(redisTokenKey)).trim();
                    logger.info("redis读取token："+realNameTokenStr);
                    if(token.equals(realNameTokenStr)){
                        String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
                        this.updatePaymentPass(payUser.getActId(), pass);
                        //删除支付密码错误计数
                        redisService.delete("payPwdSurplusCnt_".concat(payUser.getActId().toString()));
                        //删除实名认证秘钥
                        redisService.delete(redisTokenKey);
                        resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
                        resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
                        return resultMap;
                    }else{
                        resultMap.put("resultCode", Hint.USER_11011_TOKEN_INVALID.getCodeString());
                        resultMap.put("resultDesc", Hint.USER_11011_TOKEN_INVALID.getMessage());
                        return resultMap;
                    }
                }else{
                    resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
                    resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
                    return resultMap;
                }

            }else{
                resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
                resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
                return resultMap;
            }
        }catch (Exception ex) {

            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> changeUserCode(Map<String, Object> receiveMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try{
            String userId = receiveMap.get("userId").toString().trim();
            String oldUserCode = receiveMap.get("oldUserCode").toString().trim();
            String password = receiveMap.get("password").toString().trim();
            String newUserCode = receiveMap.get("newUserCode").toString().trim();

//			Hint hint=SmsUtil.smsCodeCheck(receiveMap.get("newUserCode").toString(),receiveMap.get("hardwareId").toString(), receiveMap.get("verifyCode").toString(), redisService);
//			if(hint.getCode()!=0){
//				logger.info("验证码验证失败");
//				return ConvertUtils.genReturnMap(hint);
//			}

            PayUser payUser1 =this.findByPayUserPhone(newUserCode);
            if(payUser1!=null){
                resultMap.put("resultCode", Hint.UC_CENTER_ADDR_32020_PHONE_USERD_NULL.getCodeString());
                resultMap.put("resultDesc", Hint.UC_CENTER_ADDR_32020_PHONE_USERD_NULL.getMessage());
                return resultMap;
            }

            PayUser payUser = this.findByPayUserId(userId);
            if(payUser!=null && payUser.getActId()!=null && payUser.getPayUserCode()!=null && payUser.getPayUserCode().equals(oldUserCode)){
                //获取账户
                ActPerson actPerson=this.findActPersonById(payUser.getActId());
                String pass=Md5SaltUtils.encodeMd5Salt(password.toLowerCase(), actPerson.getSaltVal());
                if(UserUtil.comparePass(pass,payUser)){
                    //TODO 需要重新修改 明确各个字段含义
                    LogOperAct logOperAct = new LogOperAct();
                    logOperAct.setActId(payUser.getActId());
                    logOperAct.setActTypeId(BaseDictConstant.LOG_OPER_ACT_TYPE_ID);
                    logOperAct.setOperCreFlag(BaseDictConstant.LOG_OPER_ACT_TYPE_ID);
                    logOperAct.setValOld(oldUserCode);
                    logOperAct.setValNew(newUserCode);
                    logOperActMapper.insert(logOperAct);

                    PayUser updatePayUser = new PayUser();
                    updatePayUser.setPayUserId(payUser.getPayUserId());
                    updatePayUser.setPayUserCode(newUserCode);
                    updatePayUser.setMobile(newUserCode);
                    updatePayUser.setUpdTime(new Date());
                    this.updateUserCode(updatePayUser);
                    return  ConvertUtils.genReturnMap(resultMap,Hint.SYS_SUCCESS);
                }else{
                    return ConvertUtils.genReturnMap(resultMap,Hint.USER_25010_USER_LOGIN_FAILED);
                }


            }else{
                resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
                resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
                return resultMap;
            }
        }catch (Exception ex) {

            logger.error("访问接口出现异常：" + ex.getMessage(), ex);
            resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
            return resultMap;
        }
    }

    @Override
    public int updateUserCode(PayUser payUser) throws Exception {
        return payUserMapper.updateUserCode(payUser);
    }

    @Override
    public Map<String, Object> loginForToken(Map<String, Object> inputMap) throws Exception{

        if(logger.isInfoEnabled()){
            logger.info("PayUserServiceGWImpl --> getLoginToken 输入参数：" + inputMap);
        }
        String userId = inputMap.get("userId").toString();
        String userCode = inputMap.get("userCode").toString();
        String hardwareId = inputMap.get("hardwareId").toString();
        String token = inputMap.get("token").toString();

        String redisTokenKey = userId+"_"+token+"_"+userCode;

        if(logger.isInfoEnabled()){
            logger.info("用户相关信息存入Redis中 token,redisKey=" + redisTokenKey );
        }
        String day = Propertie.APPLICATION.value("userCenter.token.time")==null?"2": Propertie.APPLICATION.value("userCenter.token.time");
        int day_i = Integer.parseInt(day);
        String tokenValue = redisService.get(redisTokenKey);
        Map<String, Object> responseMap = new HashMap<String, Object>();
        if(tokenValue==null || tokenValue.equals("")){
            responseMap.put("resultCode", Hint.USER_CENTER_32001_TOKEN_ERROR.getCodeString());
            responseMap.put("resultDesc", Hint.USER_CENTER_32001_TOKEN_ERROR.getMessage());
            return responseMap;
        }
//		if(!tokenValue.equals(hardwareId)){
//			responseMap.put("resultCode", Hint.USER_CENTER_32002_TOKEN_ERROR.getCodeString());
//			responseMap.put("resultDesc", Hint.USER_CENTER_32002_TOKEN_ERROR.getMessage());
//			return responseMap;
//		}

        redisService.expire(redisTokenKey,day_i*24*3600);


        //本地查询用户
        PayUser user = this.findByPayUserPhone(userCode);

        if(user!= null){

            //获取账户
            ActPerson actPerson=this.findActPersonById(user.getActId());


            Map<String, Object> returnMap = new HashMap<String,Object>();
            //returnMap.put("phone", user.getMobile());
            returnMap.put("userId", user.getPayUserId());
            returnMap.put("userCode", user.getPayUserCode());
            returnMap.put("accountId", user.getActId());
            returnMap.put("realNameStatus", "0");
            returnMap.put("personId", "");
            returnMap.put("realNameName", "");
            returnMap.put("realNameCardNo", "");
            if(actPerson!=null && actPerson.getRealNameFlag()!=null && actPerson.getRealNameFlag().toString().equals(String.valueOf(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE))){
                returnMap.put("realNameStatus", actPerson.getRealNameFlag());

                if(actPerson.getpCertId()==null){
                    UcPCertRec ucPCertRec = ucPCertRecMapper.selectByPCertNo(actPerson.getpCertNo());
                    if(ucPCertRec!=null){
                        returnMap.put("personId", ucPCertRec.getpCertId());
                    }
                }else{
                    returnMap.put("personId", actPerson.getpCertId());
                }

                String pname = actPerson.getpName();
                if(pname!=null && pname.length()>0){
                    returnMap.put("realNameName", "*"+pname.substring(1));
                }
                String certNo = actPerson.getpCertNo();
                if(certNo!=null && certNo.length()>0){
                    returnMap.put("realNameCardNo", certNo.substring(0,1)+"****************"+certNo.substring(certNo.length()-1, certNo.length()));
                }
                String realNameChannel = DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString();
                if(null != actPerson.getpCertTypeId()){
                    realNameChannel = actPerson.getpCertTypeId().toString();
                }
                returnMap.put("realNameChannel", realNameChannel);
            }
            if(null == actPerson || ValidateUtils.isEmpty(actPerson.getPayPwd())){
                returnMap.put("allowPament", "1");//不允许支付
            }else{
                returnMap.put("allowPament", "0");//允许支付
            }
            return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);

        }else{
            //用户不存在
//					return ConvertUtils.genReturnMap(Hint.USER_25009_USER_UNDEFINED);
            return ConvertUtils.genReturnMap(Hint.USER_25010_USER_LOGIN_FAILED);
        }


    }

    public Map<String, Object> unifiedLogin(Map<String, Object> receiveMap) throws Exception{

        //本地查询用户
        PayUser user = this.findByPayUserPhone(receiveMap.get("userCode").toString());

        if(user!= null){

            /**密码连续输错次数*/
            int TM_PWD_FAIL_CNT = Integer.valueOf(CacheUtils.getDimSysConfConfValue("APP_PWD_FAIL_CNT"));
            /**密码连续输错计数时间段*/
            int TM_PWD_FAIL_TIME = Integer.valueOf(CacheUtils.getDimSysConfConfValue("APP_PWD_FAIL_TIME"));
            /**密码连续输错锁定时间段*/
            int TM_PWD_LOCK_TIME = Integer.valueOf(CacheUtils.getDimSysConfConfValue("APP_PWD_LOCK_TIME"));


            if(user.getPwdFailCnt()==TM_PWD_FAIL_CNT&&System.currentTimeMillis() - user.getPwdFailTime().getTime() >= Long.parseLong(TM_PWD_LOCK_TIME+"") * 60000){
                //锁定时间超出，解锁
                this.updatePassErrorCount(user.getPayUserId(),0);//记录密码错误次数清零
                user.setPwdFailCnt(0);//当前用户错误次数清零
            }else if(user.getPwdFailCnt()==TM_PWD_FAIL_CNT&&System.currentTimeMillis() - user.getPwdFailTime().getTime() <= Long.parseLong(TM_PWD_LOCK_TIME+"") * 60000){
                //登录失败，锁定时间内，已锁定，密码第5次输入错误,已五次输入错误密码，请等待十五分钟后再试
                this.saveLoginLog(0,1,"密码输入错误5次，账号已被锁定，请15分钟之后重试",receiveMap, user);

                Map<String, Object> returnMap = new HashMap<String,Object>();
                returnMap.put("resultCode", Hint.USER_25014_PASS_CONTINUOUS_ERRO.getCodeString());
                returnMap.put("resultDesc", java.text.MessageFormat.format(Hint.USER_25014_PASS_CONTINUOUS_ERRO.getMessage(), TM_PWD_FAIL_CNT ,TM_PWD_LOCK_TIME));
                return returnMap;
            }

            //获取账户
            ActPerson actPerson=this.findActPersonById(user.getActId());
            String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
            if(UserUtil.comparePass(pass,user)){

                Hint hint =UserUtil.isUserFalse(user);
                if(hint.getCode()!=0){
                    //登录失败，用户被禁用或注销
                    this.saveLoginLog(0,1,"用户被禁用或注销",receiveMap, user);
                    return ConvertUtils.genReturnMap(hint);
                }


                //更新最后登录时间
                Date date=new Date();
                user.setUpdTime(date);
                user.setLastLoginTime(date);
                user.setDeviceCode(receiveMap.get("hardwareId").toString());

                //记录登录日志
                this.saveLoginLog(1,1,"登录成功",receiveMap, user);
                //更新最后登录时间
                this.updateLastLoginTime(user);
                //记录密码错误次数清零
                this.updatePassErrorCount(user.getPayUserId(),0);
                user.setPwdFailCnt(0);//当前用户错误次数清零
                //记录终端设备标识
                PayUserDevice payUserDevice=new PayUserDevice();
                payUserDevice.setActId(user.getActId());
                payUserDevice.setUserAlias("");//别名
                payUserDevice.setDeviceCode(receiveMap.get("hardwareId").toString());//设备号
                PayUserDevice pud=this.findPayUserDevice(payUserDevice);
                if(pud==null){
                    //记录不存在创建
                    payUserDevice.setPayUserId(user.getPayUserId());//支付平台用户ID
                    payUserDevice.setUpdTime(date);
                    payUserDevice.setCrtTime(date);
                    payUserDevice.setLastLoginTime(date);//最近登录时间
                    this.savePayUserDevice(payUserDevice);
                }else{
                    pud.setUpdTime(date);
                    pud.setLastLoginTime(date);//最近登录时间
                    this.updatePayUserDevice(pud);
                }


                String randomStr = Md5SaltUtils.getRandomString(32);
                String redisTokenKey = user.getPayUserId()+"_"+randomStr+"_"+user.getPayUserCode();
                String day = Propertie.APPLICATION.value("userCenter.token.time")==null?"2": Propertie.APPLICATION.value("userCenter.token.time");
                int day_i = Integer.parseInt(day);
                redisService.set(redisTokenKey, receiveMap.get("hardwareId").toString());
                redisService.expire(redisTokenKey,day_i*24*3600);

                Map<String, Object> returnMap = new HashMap<String,Object>();
                returnMap.put("token",randomStr);
                returnMap.put("userId", user.getPayUserId());
                returnMap.put("userCode",user.getPayUserCode());
                returnMap.put("accountId", user.getActId());
                returnMap.put("realNameStatus", "0");
                returnMap.put("personId", "");
                returnMap.put("realNameName", "");
                returnMap.put("realNameCardNo", "");
                if(actPerson.getRealNameFlag()!=null && actPerson.getRealNameFlag().toString().equals(String.valueOf(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE))){
                    returnMap.put("realNameStatus", actPerson.getRealNameFlag());

                    if(actPerson.getpCertId()==null){
                        UcPCertRec ucPCertRec = ucPCertRecMapper.selectByPCertNo(actPerson.getpCertNo());
                        if(ucPCertRec!=null){
                            returnMap.put("personId", ucPCertRec.getpCertId());
                        }
                    }else{
                        returnMap.put("personId", actPerson.getpCertId());
                    }

                    String pname = actPerson.getpName();
                    if(pname!=null && pname.length()>0){
                        returnMap.put("realNameName", "*"+pname.substring(1));
                    }
                    String certNo = actPerson.getpCertNo();
                    if(certNo!=null && certNo.length()>0){
                        returnMap.put("realNameCardNo", certNo.substring(0,1)+"****************"+certNo.substring(certNo.length()-1, certNo.length()));
                    }
                    String realNameChannel = DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString();
                    if(null != actPerson.getpCertTypeId()){
                        realNameChannel = actPerson.getpCertTypeId().toString();
                    }
                    returnMap.put("realNameChannel", realNameChannel);
                }
                if(actPerson.getPayPwd().trim().equals("")){
                    returnMap.put("allowPament", "1");//不允许支付
                }else{
                    returnMap.put("allowPament", "0");//允许支付
                }
                return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
            }else{
                //密码错误
                int num=0;
                if(user.getPwdFailTime()==null||user.getPwdFailCnt()==0){
                    //计数不存在，初次计数
                    num=1;
                }else{
                    if(System.currentTimeMillis() - user.getPwdFailTime().getTime() <= Long.parseLong(TM_PWD_FAIL_TIME+"") * 60000){
                        //计数时间在固定时间段内，叠加计数
                        num=user.getPwdFailCnt()+1;
                    }else{
                        //计数时间在固定时间段外，重新计数
                        num=1;
                    }
                }
                this.updatePassErrorCount(user.getPayUserId(),num);//记录密码错误次数

                Map<String, Object> returnMap = new HashMap<String,Object>();
                returnMap.put("failCnt", TM_PWD_FAIL_CNT-num);
                if(num==TM_PWD_FAIL_CNT){
                    //密码第5次输入错误,已五次输入错误密码，请等待十五分钟后再试
                    this.saveLoginLog(0,1,"密码输入错误5次，账号已被锁定，请15分钟之后重试",receiveMap, user);
                    returnMap.put("resultCode", Hint.USER_25014_PASS_CONTINUOUS_ERRO.getCodeString());
                    returnMap.put("resultDesc", java.text.MessageFormat.format(Hint.USER_25014_PASS_CONTINUOUS_ERRO.getMessage(), TM_PWD_FAIL_CNT ,TM_PWD_LOCK_TIME));
                    return returnMap;
                }else{
                    this.saveLoginLog(0,1,"密码验证不通过",receiveMap, user);
//					return ConvertUtils.genReturnMap(returnMap,Hint.USER_25008_VALIDATEPASS_FAILED);
                    return ConvertUtils.genReturnMap(returnMap,Hint.USER_25010_USER_LOGIN_FAILED);

                }
            }
        }else{
            //用户不存在
            //return ConvertUtils.genReturnMap(Hint.USER_25009_USER_UNDEFINED);
//			return ConvertUtils.genReturnMap(Hint.USER_25010_USER_LOGIN_FAILED);
            return this.register2(receiveMap);
        }

    }

    public Map<String, Object> register2(Map<String, Object> receiveMap) throws Exception{

//		Hint hint=SmsUtil.smsCodeCheck(receiveMap.get("userCode").toString(),receiveMap.get("hardwareId").toString(), receiveMap.get("verifyCode").toString(), redisService);
//		if(hint.getCode()==0){
        //验证码验证通过
        PayUser tu = this.findByPayUserPhone(receiveMap.get("userCode").toString());
        if(tu==null){
            ActSp actSp = this.findByChannelAppId(String.valueOf(receiveMap.get("appId")).trim());//渠道商户ID
            if(null == actSp){
                //获取医保项目ID失败
                return ConvertUtils.genReturnMap(Hint.USER_25034_OBTAINMIC_FAILED);
            }
            Date date=new Date();
            //新加账户
            Long actId=seqServiceImpl.updateSeqActId();//SEQUENCE获取账户ID
            ActPerson actPerson=new ActPerson();
            actPerson.setMicId(actSp.getMicId());//医保项目ID
            actPerson.setSiChkFlag(Short.valueOf("0"));//社保身份验证标识	0	未验证,1	已验证通过
            actPerson.setRealNameFlag(Short.valueOf("0"));//是否实名认证，1是0否
            actPerson.setActStatId(Short.valueOf("1"));//平台账户状态,1	正常,9	冻结
            actPerson.setValidFlag(Short.valueOf("1"));//是否有效,	1	有效,	0	无效
            actPerson.setPayPwd("");//支付密码初始化为空
            actPerson.setActId(actId);
            actPerson.setSaltVal(Md5SaltUtils.getSalt());
            actPerson.setUpdTime(date);
            actPerson.setCrtTime(date);
            this.saveActPerson(actPerson);

            //添加账户资金表
            ActPFin actPFin=new ActPFin();
            actPFin.setActId(actId);
            actPFin.setBal(BigDecimal.ZERO);
            actPFin.setAvalBal(BigDecimal.ZERO);
            actPFin.setGuartInit(BigDecimal.ZERO);
            actPFin.setGuartBal(BigDecimal.ZERO);
            actPFin.setTravel(BigDecimal.ZERO);
            actPFin.setFrz(BigDecimal.ZERO);
            actPFin.setLimPT(BigDecimal.ZERO);
            actPFin.setLimPDay(BigDecimal.ZERO);
            actPFin.setLimWT(BigDecimal.ZERO);
            actPFin.setLimWDay(BigDecimal.ZERO);
            actPFin.setWithDayAmt(BigDecimal.ZERO);
            actPFin.setUpdTime(date);
            actPFin.setCrtTime(date);
            this.saveActPFin(actPFin);//平台私人账户资金属性表

            //新加用户
            Long payUserId= seqServiceImpl.updateSeqUserId();//SEQUENCE获取用户ID
            PayUser u=new PayUser();
            u.setPayUserId(payUserId);
            u.setActId(actId);
            u.setPayUserCode(receiveMap.get("userCode").toString());
            u.setMobile(receiveMap.get("userCode").toString());
            String pass=Md5SaltUtils.encodeMd5Salt(receiveMap.get("password").toString().toLowerCase(), actPerson.getSaltVal());
            u.setPwd(pass);
            u.setUpdTime(date);
            u.setCrtTime(date);
            u.setLastLoginTime(date);
            u.setDeviceCode(receiveMap.get("hardwareId").toString());//设备号
            u.setChanActId(actSp.getActId());
            this.savePayUser(u);//注册用户并登录成功

            //记录登录日志
            this.saveLoginLog(1,1,"登录成功",receiveMap, u);
            //记录终端设备标识
            PayUserDevice payUserDevice=new PayUserDevice();
            payUserDevice.setActId(actId);
            payUserDevice.setUserAlias("");//别名
            payUserDevice.setDeviceCode(receiveMap.get("hardwareId").toString());//设备号
            PayUserDevice pud=this.findPayUserDevice(payUserDevice);
            if(pud==null){
                //记录不存在创建
                payUserDevice.setPayUserId(payUserId);//支付平台用户ID
                payUserDevice.setUpdTime(date);
                payUserDevice.setCrtTime(date);
                payUserDevice.setLastLoginTime(date);//最近登录时间
                this.savePayUserDevice(payUserDevice);
            }else{
                pud.setUpdTime(date);
                pud.setLastLoginTime(date);//最近登录时间
                this.updatePayUserDevice(pud);
            }

            Map<String, Object> returnMap = new HashMap<String,Object>();
            returnMap.put("userId", u.getPayUserId());
            returnMap.put("userCode", u.getPayUserCode());
            returnMap.put("accountId", u.getActId());

            String randomStr = Md5SaltUtils.getRandomString(32);
            String redisTokenKey = u.getPayUserId()+"_"+randomStr+"_"+u.getPayUserCode();
            redisService.set(redisTokenKey, receiveMap.get("hardwareId").toString());
            String day = Propertie.APPLICATION.value("userCenter.token.time")==null?"2": Propertie.APPLICATION.value("userCenter.token.time");
            int day_i = Integer.parseInt(day);
            redisService.expire(redisTokenKey, day_i*24*60*60);
            returnMap.put("token", randomStr);

            returnMap.put("realNameStatus", "0");
            returnMap.put("personId", "");
            returnMap.put("realNameName", "");
            returnMap.put("realNameCardNo", "");
            if(actPerson.getRealNameFlag()!=null && actPerson.getRealNameFlag().toString().equals(String.valueOf(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE))){
                returnMap.put("realNameStatus", actPerson.getRealNameFlag());
                returnMap.put("personId", actPerson.getpCertId());
                String pname = actPerson.getpName();
                if(pname!=null && pname.length()>0){
                    returnMap.put("realNameName", "*"+pname.substring(1));
                }
                String certNo = actPerson.getpCertNo();
                if(certNo!=null && certNo.length()>0){
                    returnMap.put("realNameCardNo", certNo.substring(0,1)+"****************"+certNo.substring(certNo.length()-1, certNo.length()));
                }
                String realNameChannel = DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString();
                if(null != actPerson.getpCertTypeId()){
                    realNameChannel = actPerson.getpCertTypeId().toString();
                }
                returnMap.put("realNameChannel", realNameChannel);
            }
            if(actPerson.getPayPwd().trim().equals("")){
                returnMap.put("allowPament", "1");//不允许支付
            }else{
                returnMap.put("allowPament", "0");//允许支付
            }
            return ConvertUtils.genReturnMap(returnMap,Hint.SYS_SUCCESS);
        }else{
            //手机号重复
            return ConvertUtils.genReturnMap(Hint.SP_12006_SP_TELNO_EXIST);
        }
//		}else{
//			//验证码验证不通过
//			 return ConvertUtils.genReturnMap(hint);
//		}

    }

    public int updateActPersonSiCardNo(ActPerson actPerson) throws Exception {
        return actPersonMapper.updateActPersonSiCardNo(actPerson);
    }

    /*public PersonInfoRes getMiPersonalInfo(Long personalActId, String pSiCardNo, String fixedHospCode) throws Exception {
        if(logger.isInfoEnabled()){
            logger.info("personalActId=" + personalActId + ",fixedHospCode=" + fixedHospCode);
        }
        if(null == personalActId || personalActId == 0 || ValidateUtils.isEmpty(fixedHospCode)){
            return null;
        }
        ActPerson actPerson = actPersonMapper.findActPersonById(personalActId);
        if(null == actPerson){
            if(logger.isInfoEnabled()){
                logger.info("未找到个人账户信息");
            }
            return null;
        }
        String pCertNo = String.valueOf(actPerson.getpCertNo()).trim();
        String pName = String.valueOf(actPerson.getpName()).trim();
        String siCardNo = String.valueOf(pSiCardNo).trim();
        if(ValidateUtils.isEmpty(pCertNo) || ValidateUtils.isEmpty(pName) || ValidateUtils.isEmpty(siCardNo)){
            if(logger.isInfoEnabled()){
                logger.info("用户身份证号or姓名or社保卡号为空 pCertNo=" + pCertNo + ",pName=" + pName + ",siCardNo=" + siCardNo);
            }
            return null;
        }
        boolean needQueryMiPersonalInfo = false;
        String siPCode = String.valueOf(actPerson.getActCode()).trim();//医保个人编号
        if(ValidateUtils.isEmpty(siPCode)){
            if(logger.isInfoEnabled()){
                logger.info("用户医保个人编号为空 siPCode=" + siPCode);
            }
            needQueryMiPersonalInfo = true;
        }
        String redisKey;
        String redisValue;
        PersonInfoRes personInfoRes = null;
        if(!needQueryMiPersonalInfo){
            redisKey = "medicalPersonalInfo_".concat(siPCode);
            redisValue = String.valueOf(redisService.get(redisKey)).trim();
            if(logger.isInfoEnabled()){
                logger.info("获取医保个人信息 redisKey=" + redisKey + ",redisValue=" + redisValue);
            }
            if(ValidateUtils.isEmpty(redisValue)){
                needQueryMiPersonalInfo = true;
            }else {
                personInfoRes = JsonStringUtils.jsonStringToObject(redisValue, PersonInfoRes.class);
                if(null != personInfoRes && personInfoRes.getRTNCode() == 1 &&
                        ValidateUtils.isNotEmpty(String.valueOf(personInfoRes.getCycid()).trim())){
                    //检测cycid是否已过期
                    if(true){//已过期，需要重新获取
                        needQueryMiPersonalInfo = true;
                    }
                }else {
                    needQueryMiPersonalInfo = true;
                }
            }
        }
        if(needQueryMiPersonalInfo){
            PersonReq personReq = new PersonReq();
            personReq.setCardID(siCardNo);//社保卡号
            personReq.setPerCardID(pCertNo);//身份证号
            personReq.setPerName(pName);//姓名
            if(logger.isInfoEnabled()){
                logger.info("查询医保个人信息，请求参数：fixedHospCode=" + fixedHospCode + ",personReq=" + JSON.toJSONString(personReq));
            }
            personInfoRes = commInfoServiceImpl.queryPerInfo(fixedHospCode, personReq);
            if(logger.isInfoEnabled()){
                logger.info("响应报文：personInfoRes=" + (personInfoRes == null ? null : JSON.toJSONString(personInfoRes)));
            }
            if(null != personInfoRes && personInfoRes.getRTNCode() == 1){
                //获取对方系统时间
                if(logger.isInfoEnabled()){
                    logger.info("开始获取医保系统时间");
                }
                SystemDateRes systemDateRes = commInfoServiceImpl.querySysTime();
                if(logger.isInfoEnabled()){
                    logger.info("响应报文：systemDateRes=" + (systemDateRes == null ? null : JSON.toJSONString(systemDateRes)));
                }
                if(null != systemDateRes && systemDateRes.getRTNCode() == 1){
                    String systemDate = String.valueOf(systemDateRes.getSysdate()).trim();
                    if(ValidateUtils.isNotEmpty(systemDate)){
                        try{
                            Date beginDate = DateUtils.strToDate(systemDate, "yyyy-MM-dd HH:mm:ss");
                            Date endDate = DateUtils.strToDate(systemDate.substring(0, 10).concat(" 23:59:59"), "yyyy-MM-dd HH:mm:ss");
                            int second = DateUtils.getSecondByDate(beginDate, endDate);
                            if(logger.isInfoEnabled()){
                                logger.info("beginDate=" + DateUtils.dateToString(beginDate, "yyyy-MM-dd HH:mm:ss") + ",endDate=" +
                                        DateUtils.dateToString(endDate, "yyyy-MM-dd HH:mm:ss") + ",相差秒数second=" + second);
                            }
                            if(second > 0){
                                redisKey = "medicalPersonalInfo_".concat(personInfoRes.getPersonID());
                                redisValue = JsonStringUtils.objectToJsonString(personInfoRes);
                                redisService.set(redisKey, redisValue);
                                redisService.expire(redisKey, second);
                                if(logger.isInfoEnabled()){
                                    logger.info("Redis保存医保个人信息 redisKey=" + redisKey + ",redisValue=" + redisValue + ",expire=" + second);
                                }
                            }
                        }catch (Exception e) {
                            logger.info("字符串转化为日期失败：" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if(null == personInfoRes || personInfoRes.getRTNCode() != 1){//返回结果失败或个人编号为空
            if(logger.isInfoEnabled()){
                logger.info(Hint.PAY_ORDER_13101.getMessage());
            }
            return null;
        }
        if(ValidateUtils.isEmpty(String.valueOf(actPerson.getActCode())) || ValidateUtils.isEmpty(actPerson.getSiCardNo())){
            ActPerson updActPerson = new ActPerson();
            updActPerson.setActId(personalActId);
            if(ValidateUtils.isEmpty(String.valueOf(actPerson.getActCode()))){
                updActPerson.setActCode(Long.parseLong(personInfoRes.getPersonID()));//借用字段 保存医保个人编号
            }
            if(ValidateUtils.isEmpty(actPerson.getSiCardNo())){
                updActPerson.setSiCardNo(siCardNo);
            }
            updActPerson.setUpdTime(new Date());
            this.updateActPersonSiCardNo(updActPerson);
        }
        return personInfoRes;
    }*/

    public PersonInfoRes getMiPersonalInfo(Long personalActId, String pSiCardNo, String fixedHospCode) throws Exception {
        if(logger.isInfoEnabled()){
            logger.info("personalActId=" + personalActId + ",fixedHospCode=" + fixedHospCode);
        }
        if(null == personalActId || personalActId == 0 || ValidateUtils.isEmpty(fixedHospCode)){
            return null;
        }
        ActPerson actPerson = actPersonMapper.findActPersonById(personalActId);
        if(null == actPerson){
            if(logger.isInfoEnabled()){
                logger.info("未找到个人账户信息");
            }
            return null;
        }
        String pCertNo = String.valueOf(actPerson.getpCertNo()).trim();
        String pName = String.valueOf(actPerson.getpName()).trim();
        String siCardNo = String.valueOf(pSiCardNo).trim();
        if(ValidateUtils.isEmpty(pCertNo) || ValidateUtils.isEmpty(pName) || ValidateUtils.isEmpty(siCardNo)){
            if(logger.isInfoEnabled()){
                logger.info("用户身份证号or姓名or社保卡号为空 pCertNo=" + pCertNo + ",pName=" + pName + ",siCardNo=" + siCardNo);
            }
            return null;
        }
        PersonReq personReq = new PersonReq();
        personReq.setCardID(siCardNo);//社保卡号
        personReq.setPerCardID(pCertNo);//身份证号
        personReq.setPerName(pName);//姓名
        if(logger.isInfoEnabled()){
            logger.info("查询医保个人信息，请求参数：fixedHospCode=" + fixedHospCode + ",personReq=" + JSON.toJSONString(personReq));
        }
        PersonInfoRes personInfoRes = commInfoServiceImpl.queryPerInfo(fixedHospCode, personReq);
        if(logger.isInfoEnabled()){
            logger.info("响应报文：personInfoRes=" + (personInfoRes == null ? null : JSON.toJSONString(personInfoRes)));
        }
        if(null == personInfoRes || personInfoRes.getRTNCode() != 1){//返回结果失败或个人编号为空
            if(logger.isInfoEnabled()){
                logger.info(Hint.PAY_ORDER_13101.getMessage());
            }
            return null;
        }
        if(ValidateUtils.isEmpty(String.valueOf(actPerson.getActCode())) || ValidateUtils.isEmpty(actPerson.getSiCardNo())){
            ActPerson updActPerson = new ActPerson();
            updActPerson.setActId(personalActId);
            if(ValidateUtils.isEmpty(String.valueOf(actPerson.getActCode()))){
                updActPerson.setActCode(Long.parseLong(personInfoRes.getPersonID()));//借用字段 保存医保个人编号
            }
            if(ValidateUtils.isEmpty(actPerson.getSiCardNo())){
                updActPerson.setSiCardNo(siCardNo);
            }
            updActPerson.setUpdTime(new Date());
            this.updateActPersonSiCardNo(updActPerson);
        }
        return personInfoRes;
    }

    public void saveActPCertRec(String appId, Long userActId, String userName, String idNumber, Short certResFlag, String errInfo, boolean isSaveUcPCertRec) throws Exception {
        ActPCertRec insertActPCertRec = new ActPCertRec();
        insertActPCertRec.setActId(userActId);
        insertActPCertRec.setActName(userName);
        insertActPCertRec.setpIdNo(idNumber.toUpperCase());
        insertActPCertRec.setCertResFlag(certResFlag);
        insertActPCertRec.setErrInfo(errInfo);
        ActSp channelSp = this.findByChannelAppId(appId);
        if(null != channelSp){
            insertActPCertRec.setChanActId(channelSp.getActId());
        }
        insertActPCertRec.setRemark("1");
        insertActPCertRec.setFromActTypeId(Short.valueOf(DimDictEnum.REAL_NAME_THREE_ELEMENTS_HUIYUE.getCodeString()));//借用字段，保存实名认证通道类型
        actPCertRecMapper.insertSelective(insertActPCertRec);//保存实名认证记录表

        if(isSaveUcPCertRec){
            UcPCertRec ucPCertRec = ucPCertRecMapper.selectByPCertNo(idNumber);
            if(null == ucPCertRec){
                Date nowDate = new Date();
                ucPCertRec = new UcPCertRec();
                ucPCertRec.setpCertNo(idNumber.toUpperCase());
                ucPCertRec.setpName(userName);
                ucPCertRec.setValidFlag(BaseDictConstant.UC_P_CERT_REC_VALID_FLAG_YES);
                ucPCertRec.setUpdTime(nowDate);
                ucPCertRec.setCrtTime(nowDate);
                ucPCertRec.setCertRecId(insertActPCertRec.getCertRecId());
                ucPCertRecMapper.insertSelective(ucPCertRec);
            }
            ActPerson actPersonUpdate = new ActPerson();
            actPersonUpdate.setActId(userActId);
            actPersonUpdate.setRealNameFlag(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE);
            actPersonUpdate.setpCertTypeId(Short.valueOf(DimDictEnum.REAL_NAME_THREE_ELEMENTS_HUIYUE.getCodeString()));//借用字段，保存实名认证通道类型
            actPersonUpdate.setpCertNo(idNumber.toUpperCase());
            actPersonUpdate.setpGendId(IdCardNoUtils.getUserSexByIdNum(actPersonUpdate.getpCertNo()));
            actPersonUpdate.setpBirth(IdCardNoUtils.getUserBirthByIdNum(actPersonUpdate.getpCertNo()));
            actPersonUpdate.setpName(userName);
            actPersonUpdate.setpCertId(ucPCertRec.getpCertId());
            actPersonMapper.updateActPersonInfo(actPersonUpdate);
        }
    }
}
