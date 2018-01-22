package com.pay.cloud.pay.supplier.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.pay.seq.service.SeqService;
import com.pay.cloud.pay.supplier.dao.ActSpBankMapper;
import com.pay.cloud.pay.supplier.dao.ActSpExtMapper;
import com.pay.cloud.pay.supplier.dao.ActSpFinMapper;
import com.pay.cloud.pay.supplier.dao.ActSpMapper;
import com.pay.cloud.pay.supplier.dao.SpUserMapper;
import com.pay.cloud.pay.supplier.entity.ActSp;
import com.pay.cloud.pay.supplier.entity.ActSpBank;
import com.pay.cloud.pay.supplier.entity.ActSpExt;
import com.pay.cloud.pay.supplier.entity.ActSpFin;
import com.pay.cloud.pay.supplier.entity.SpUser;
import com.pay.cloud.pay.supplier.service.SupplierService;
import com.pay.cloud.util.AppidUtils;
import com.pay.cloud.util.CipherAesEnum;
import com.pay.cloud.util.EncryptUtils;
import com.pay.cloud.util.IdCreatorUtils;
import com.pay.cloud.util.Md5SaltUtils;
import com.pay.cloud.util.hint.Hint;
import com.pay.secrity.crypto.RSA;
@Service("supplierServiceImpl")
public class SupplierServiceImpl implements SupplierService {
	
	@Resource
	private SpUserMapper spUserMapper;

	@Resource
	private ActSpMapper actSpMapper;

	@Resource
	private ActSpFinMapper actSpFinMapper;

	@Resource
	private ActSpBankMapper actSpBankMapper;
	
	@Resource
	private ActSpExtMapper actSpExtMapper;
	
//	@Resource
//	private PpFundPlatformMapper ppFundPlatformMapper;
	
	@Resource
	private RedisService redisService;
	
	@Resource(name = "seqServiceImpl")
	private SeqService seqService;
	
	@Override
	public Map<String, Object> addSupplier(Map<String, Object> receiveMap) throws Exception {

		Date startDate = new Date();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 渠道商户判断
		String type = receiveMap.get("supplyTypeId").toString();
		boolean isChanAct = String.valueOf(BaseDictConstant.CHAN_ACT_FLAG_YES).equals(type)?true:false;
		if (isChanAct) {
			// 手机号码已经注册
			if(findSpUserByUserCode(receiveMap.get("telNo").toString())!=null){
				returnMap.put("resultCode", Hint.SP_12006_SP_TELNO_EXIST.getCodeString());
				returnMap.put("resultDesc", Hint.SP_12006_SP_TELNO_EXIST.getMessage());
				return returnMap;
			}
		}
			
		// 商户账户id
		Long actSpId = updateSeqActId();
		//盐值
		String salt = Md5SaltUtils.getSalt();
		// 保存账户信息
		saveActSp(actSpId,isChanAct,salt,startDate, receiveMap, returnMap);
		// 商户资金属性
		saveActSpFin(actSpId, startDate, receiveMap);
		Object ba = receiveMap.get("bankAccount");
		if(ba != null && !StringUtils.isEmpty(ba.toString())){
			//银行卡信息
			saveActSpBank(actSpId, startDate, receiveMap);
		}
	    //渠道商户
		if (isChanAct) {
			// 用户信息
			saveSpUser(actSpId, salt, startDate, receiveMap, returnMap);
	          // 扩展信息
			saveActSpExt(actSpId, startDate, returnMap);
		} 
		returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return returnMap;
	}

	@Override
	public Map<String, Object> updateSupplierState(Map<String, Object> receiveMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Long actId = Long.parseLong(receiveMap.get("actId").toString());
		Date date = new Date();
		// 1冻结2解冻
		String state = receiveMap.get("state").toString();
		String remark = receiveMap.get("remark").toString();
		ActSp actsp = this.findSpActByActId(actId);
		if(actsp == null){
			returnMap.put("resultCode", Hint.SP_12001_DEST_SP_NOT_EXIST.getCodeString());
			returnMap.put("resultDesc", Hint.SP_12001_DEST_SP_NOT_EXIST.getMessage());
			return returnMap;
		}else if(BaseDictConstant.OPER_FREEZE_SP.equals(state)){
			if(actsp.getActStatId() == BaseDictConstant.ACT_STAT_ID_FREEZEN){
				returnMap.put("resultCode", Hint.SP_12007_DEST_ACT_FREEZEN.getCodeString());
				returnMap.put("resultDesc", Hint.SP_12007_DEST_ACT_FREEZEN.getMessage());
			}else{
				updateActSp(actId, BaseDictConstant.ACT_STAT_ID_FREEZEN, remark, date);
			    updateSpUser(actId, BaseDictConstant.SP_USER_STAT_ID_FORBIDDEN, date);
			    returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			}
		}else if(BaseDictConstant.OPER_UNFREEZE_SP.equals(state)){
			if(actsp.getActStatId() == BaseDictConstant.ACT_STAT_ID_NORMAL){
				returnMap.put("resultCode", Hint.SP_12008_DEST_ACT_UNFREEZEN.getCodeString());
				returnMap.put("resultDesc", Hint.SP_12008_DEST_ACT_UNFREEZEN.getMessage());
			} else{
				updateActSp(actId, BaseDictConstant.ACT_STAT_ID_NORMAL, remark, date);
			    updateSpUser(actId, BaseDictConstant.SP_USER_STAT_ID_NORMAL, date);
			    returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			}
		}
		return returnMap;
	}

	private void saveActSpExt(Long actSpId, Date startDate, Map<String, Object> returnMap) throws Exception {
		ActSpExt actSpExt = new ActSpExt();
        // 账户ID
		actSpExt.setActId(actSpId);
		Map<String,String> miKeyPair = RSA.generateKeyPair();		
		Map<String,String> spKeyPair = RSA.generateKeyPair();
		
		String serverPubKey = EncryptUtils.aesEncrypt(miKeyPair.get(RSA.PUBLIC_KEY),CipherAesEnum.SECRETKEY);
		String serverPriKey = EncryptUtils.aesEncrypt(miKeyPair.get(RSA.PRIVATE_KEY),CipherAesEnum.SECRETKEY);
		String spPubKey = EncryptUtils.aesEncrypt(spKeyPair.get(RSA.PUBLIC_KEY),CipherAesEnum.SECRETKEY);
		String spPriKey = EncryptUtils.aesEncrypt(spKeyPair.get(RSA.PRIVATE_KEY),CipherAesEnum.SECRETKEY);
		// 平台公钥	
		actSpExt.setServerKeyPub(serverPubKey);
		// 平台私钥
		actSpExt.setServerKeyPri(serverPriKey);
		// 公钥
		actSpExt.setSpKeyPub(spPubKey);
		// 私钥
		actSpExt.setSpKeyPri(spPriKey);
		actSpExt.setUpdTime(startDate);
		// 创建时间	
		actSpExt.setCrtTime(startDate);
        //备注
		actSpExtMapper.saveActSpExt(actSpExt);
        //返回的密钥 
		returnMap.put("serverPubKey", EncryptUtils.aesDecrypt(serverPubKey, CipherAesEnum.SECRETKEY));
		//returnMap.put("serverPriKey", serverPriKey);
		returnMap.put("spPubKey", EncryptUtils.aesDecrypt(spPubKey, CipherAesEnum.SECRETKEY));
		returnMap.put("spPriKey", EncryptUtils.aesDecrypt(spPriKey, CipherAesEnum.SECRETKEY));
	}
	
	/**
	 * 
	 * @Description: 保存商户用户
	 * @Title: getSpUser
	 * @param actSpId
	 * @param receiveMap
	 * @return SpUser
	 */
	private void saveSpUser(Long actSpId,String salt, Date startDate,
			Map<String, Object> receiveMap, Map<String, Object> returnMap) {
		
		String telno = receiveMap.get("telNo").toString();
		//取手机号后6位
		String initPwd = telno.substring(telno.length() - 6);
		//md5
		initPwd = Md5SaltUtils.encodeMd5(initPwd);
		// TODOD
		// md5salt 加密 
		initPwd = Md5SaltUtils.encodeMd5Salt(initPwd, salt);
		SpUser spUser = new SpUser();
		// 商户用户ID 
		Long userId = updateSeqUserId();
		spUser.setSpUserId(userId);
		// 登录名
		spUser.setSpUserCode(telno);
		// 密码
		spUser.setPwd(initPwd);
		// 商户用户状态 
		spUser.setSpUserStatId(BaseDictConstant.SP_USER_STAT_ID_NORMAL);
		// 商户用户类别
		spUser.setSpUserTypeId(BaseDictConstant.SP_USER_TYPE_ID_NORMAL);
		// 昵称 
		spUser.setUserName(getString(receiveMap,"nickname"));
		
		// 电子邮件
		//spUser.setEmail(getString(receiveMap,"email"));
		// 手机号码 
		spUser.setMobile(telno);
		// 绑定账户ID
		spUser.setActId(actSpId);
		// 累计密码错误次数
		spUser.setPwdFailCnt(0);
		// 首次密码错误
		spUser.setPwdFailTime(null);
		// 使用客户端版本号
		spUser.setClientVerCode(getString(receiveMap,"version")); 
		// 更新时间 UPD_TIME DATETIME
		spUser.setUpdTime(startDate);
		// 创建时间 CRT_TIME DATETIME
		spUser.setCrtTime(startDate);
		
		spUserMapper.insert(spUser);
		
		returnMap.put("username", telno);
		returnMap.put("initPwd", initPwd);

	}

	/**
	 * 
	 * @Description: 封装银行(第三方)账号表-商户
	 * @Title: getActSpBank
	 * @param actSpId
	 *            商户账户id
	 * @param receiveMap
	 * @return ActSpBank
	 */
	private void saveActSpBank(Long actSpId, Date startDate,
			Map<String, Object> receiveMap) {
		
		ActSpBank actSpBank = new ActSpBank();
		Long id = IdCreatorUtils.getSpBankActId();
		// 银行账号ID
		actSpBank.setBankActId(id);
		// 平台账户类型 平台商户账户
		actSpBank.setFromActTypeId(BaseDictConstant.ACT_TYPE_ID_SP);
		actSpBank.setActId(actSpId);
		// 账户ID ACT_ID 
		String accountType = receiveMap.get("accountType")==null?"101":receiveMap.get("accountType").toString();
		// 支付账号类别 
		actSpBank.setPayActTypeId(Short.parseShort(accountType));
		// 银行ID BANK_ID 
		Object bankCode = receiveMap.get("bankCode");
		if(bankCode != null && !StringUtils.isEmpty(bankCode.toString())){
			actSpBank.setBankId(Long.parseLong(bankCode.toString()));
		}
		Object bankName = receiveMap.get("bankName");
		if(bankName != null && !StringUtils.isEmpty(bankName.toString())){
			actSpBank.setBankName(bankName.toString());
		}
		// 开户银行 BANK_NAME varchar(120) 120 FALSE FALSE FALSE 支行名称
		Object branchBankName = receiveMap.get("branchBankName");
		if(branchBankName != null && !StringUtils.isEmpty(branchBankName.toString())){
			//其他支付属性A  支行
			actSpBank.setPayPropA(branchBankName.toString());
		}
		// 银行账号 BANK_ACCOUNT varchar(64) 64 FALSE FALSE FALSE
		actSpBank.setBankAccount(receiveMap.get("bankAccount").toString());
		// 户名 ACT_NAME varchar(30) 30 FALSE FALSE FALSE
		actSpBank.setActName(receiveMap.get("actName").toString());
		// 验证手机号 AUTH_MOBILE 
		// 其他支付属性B 
		// 其他支付属性C 
		// 备注 REMARK varchar(500) 500 FALSE FALSE FALSE
		actSpBank.setValidFlag(BaseDictConstant.VALID_FLAG_YES);
		// 更新时间 UPD_TIME DATETIME
		actSpBank.setUpdTime(startDate);
		// 创建时间 CRT_TIME DATETIME
		actSpBank.setCrtTime(startDate);
		// 备注 REMARK varchar(500) 500
		actSpBank.setRemark(getString(receiveMap,"remark"));
		actSpBankMapper.insert(actSpBank);

	}

	/**
	 * 
	 * @Description: 封装商户账户属性表
	 * @Title: getActSp
	 * @param actSpId
	 *            商户账户id
	 * @param receiveMap
	 * @return ActSp
	 */
	private void saveActSpFin(Long actSpId, Date startDate,
			Map<String, Object> receiveMap) {
		ActSpFin actSpFin = new ActSpFin();
		// 账户ID 
		actSpFin.setActId(actSpId);
		// 平台信息ID1 
		// 平台信息ID2 
		// 平台信息ID3 
		// 备注 REMARK 
		BigDecimal init = BigDecimal.valueOf(0.0);
		// 账户余额 BAL
		actSpFin.setBal(init);
		// 账户可用余额 
		actSpFin.setAvalBal(init);
		// 初始保证金  
		actSpFin.setGuartInit(init);
		// 保证金余额 
		actSpFin.setGuartBal(init);
		// 支出在途资金
		actSpFin.setTravel(init);
		// 收入在途资金
		actSpFin.setInTravel(init);
		// 已冻结资金 
		actSpFin.setFrz(init);
		//初始化  限额不做限制
		// 每次支付限额
		actSpFin.setLimPT(init);
		// 每日支付限额 
		actSpFin.setLimPDay(init);
		// 每次提现限额 
		actSpFin.setLimWT(init);
		// 每日提现限额 
		actSpFin.setLimWDay(init);
		// 当日已提现金额
		actSpFin.setWithDayAmt(init);
		// 最后一次提现时间
		actSpFin.setLastWithTime(null);
		actSpFin.setCrtTime(startDate);
		actSpFin.setUpdTime(startDate);
		actSpFin.setRemark(null);
		actSpFinMapper.insert(actSpFin);

	}

	/**
	 * 
	 * @Description: 封装商户账户
	 * @Title: getActSp
	 * @param actSpId
	 *            商户账户id
	 * @param receiveMap
	 * @return ActSp
	 */
	private Long saveActSp(Long actSpId, boolean isChanAct, String salt, Date startDate,
			Map<String, Object> receiveMap, Map<String, Object> returnMap) {

		ActSp actSp = new ActSp();
		// 账户ID ACT_ID bigint
		actSp.setActId(actSpId);
		//盐值
		actSp.setSaltVal(salt);
		// 医保项目ID MIC_ID VARCHAR(10) 10
		actSp.setMicId(getString(receiveMap, "micId"));
		returnMap.put("actSpId",actSpId.toString());
		// 账户编码 ACT_CODE bigint
		if (isChanAct) {
			actSp.setChanActFlag(BaseDictConstant.CHAN_ACT_FLAG_YES);
			// 渠道账户APPID CHAN_APPID varchar(120) 120
			String appId = AppidUtils.getAppid();
			returnMap.put("appId", appId);
			actSp.setChanAppid(appId);
		} else {
			actSp.setChanActFlag(BaseDictConstant.CHAN_ACT_FLAG_NO);
			// 所属渠道账户ID CHAN_ACT_ID bigint
			actSp.setChanActId(Long.parseLong(receiveMap.get("operId")
					.toString()));
		}

		// 企业名称 ENT_NAME varchar(120) 120
		actSp.setEntName(receiveMap.get("entName").toString());
		// 企业类型 ENT_TYPE_ID smallint
		short t = Short.parseShort(receiveMap.get("entType").toString());
		actSp.setEntTypeId(t);
		// 商户授权码 ENT_PRIV_CODE varchar(30) 30
		actSp.setEntPrivCode(getString(receiveMap, "entPrivCode"));
		// 医保定点机构编码 MI_ENT_CODE varchar(30) 30
		actSp.setMiEntCode(getString(receiveMap, "miEntCode"));
		// 公司地址 ENT_ADDR varchar(500) 500
		actSp.setEntAddr(getString(receiveMap, "entAddr"));
		// 经度 LONGITUDE varchar(30) 30
		actSp.setLongitude(getString(receiveMap, "longitude"));
		// 纬度 LATITUDE varchar(30) 30
		actSp.setLatitude(getString(receiveMap, "latitude"));
		// 邮政编码 ZIPCODE DECIMAL(6) 6
		Object zipCode = receiveMap.get("zipCode");
		if(zipCode != null &&  zipCode.toString().trim().length() != 0 ){
			actSp.setZipcode(Integer.parseInt(zipCode.toString()));
		}
		// 联系人 LINK_MAN varchar(30) 30
		actSp.setLinkMan(receiveMap.get("linkMan").toString());
		// 联系电话1 PHONE1 varchar(30) 30
		actSp.setPhone1(receiveMap.get("phone1").toString());
		// 联系电话2 PHONE2 varchar(30) 30
		actSp.setPhone2(receiveMap.get("phone2").toString());
		// 联系电话3 PHONE3 varchar(30) 30
		actSp.setPhone3(receiveMap.get("phone3").toString());
		// 联系邮箱 ENT_EMAIL varchar(30) 30
		actSp.setEntEmail(receiveMap.get("entEmail").toString());
		// 经营范围 ENT_SCOPE varchar(500) 500
		actSp.setEntScope(receiveMap.get("entScope").toString());
		// 企业成立日期 ENT_START_DATE varchar(8) 8
		actSp.setEntStartDate(receiveMap.get("entStartDate").toString());
		// 企业营业期限 ENT_END_DATE varchar(8) 8
		actSp.setEntEndDate(receiveMap.get("entEndDate").toString());
		// 营业执照号 REG_CODE varchar(30) 30
		actSp.setRegCode(getString(receiveMap,"regCode"));
		// 营业执照图ID REG_IMAGE_ID varchar(32) 32
		actSp.setRegImageId(getString(receiveMap,"regImageId"));
		// 组织机构代码号 ORG_CODE varchar(30) 30
		actSp.setOrgCode(getString(receiveMap, "orgCode"));
		// 组织机构代码图ID ORG_IMAGE_ID varchar(32) 32
		actSp.setOrgImageId(getString(receiveMap, "orgImageId"));
		// 企业图片 IMAGE_ID varchar(32) 32
		actSp.setImageId(getString(receiveMap, "imageId"));
		// 平台账户状态 ACT_STAT_ID smallint
		// 是否有效 VALID_FLAG smallint
		actSp.setActStatId(BaseDictConstant.ACT_STAT_ID_NORMAL);
		actSp.setValidFlag(BaseDictConstant.VALID_FLAG_YES);
		// 更新时间 UPD_TIME DATETIME
		actSp.setUpdTime(startDate);
		// 创建时间 CRT_TIME DATETIME
		actSp.setCrtTime(startDate);
		// 备注 REMARK varchar(500) 500
		actSp.setRemark(getString(receiveMap,"remark"));	
		// 待审核
		actSp.setSpOpenChkFlag(BaseDictConstant.SP_OPEN_CHK_FLAG_WAIT);
		actSpMapper.insert(actSp);
		return actSpId;
	}

	@Override
	public Map<String, Object> subSupplierQuery(Map<String, Object> receiveMap) {
		// 渠道商户
		Long chanActId = Long.parseLong(receiveMap.get("actId").toString());
		// 合作商户查询
		List<ActSp> actspList = this.findActSpByChanActId(chanActId);
		List<Map<String, Object>> rl = new ArrayList<Map<String, Object>>();
		if (actspList != null && actspList.size() > 0) {
			for (int i = 0; i < actspList.size(); i++) {
				ActSp actSp = actspList.get(i);
				Map<String, Object> rm = new HashMap<String, Object>();
				rm.put("actId", actSp.getActId());
				rm.put("actName", actSp.getEntName());
				rm.put("validFlag", actSp.getValidFlag());
				rm.put("spOpenChkFlag", actSp.getSpOpenChkFlag());
				rm.put("remark", actSp.getRemark());
				rl.add(rm);
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		returnMap.put("resultDesc", "查询成功！");
		returnMap.put("actList", rl);
		return returnMap;

	}
	
	@Override
	public Map<String, Object> bindBankCard(Map<String, Object> receiveMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Long actId = Long.parseLong(receiveMap.get("actId").toString());
//		int count = actSpBankMapper.getCountByActId(actId);
//		if(count > CacheUtils.getDimSysConfConfValue(confId)){
//			
//		}
		ActSpBank ActSpBank = actSpBankMapper.selectByActId(actId);
		if(ActSpBank != null){
			returnMap.put("resultCode", Hint.SP_12009_HAVE_VALID_CARD.getCodeString());
			returnMap.put("resultDesc", "有銀行卡未解除綁定,无法绑定新的银行卡");
		}else{
			saveActSpBank(actId, new Date(),receiveMap);
			returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			returnMap.put("resultDesc", "绑定成功！");
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> operBankCard(Map<String, Object> receiveMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String cardNo = receiveMap.get("cardNo").toString();
		Long actId = Long.parseLong(receiveMap.get("actId").toString());
		Short state = Short.parseShort(receiveMap.get("state").toString());
		String remark = getString(receiveMap,"remark");
		Date date = new Date();
//		绑卡账号ID	actId	String	渠道商户账号或者合作账号
		ActSpBank record = new ActSpBank();
		record.setActId(actId);
		record.setBankAccount(cardNo);
		ActSpBank result = actSpBankMapper.selectByBankAccount(record);
		if(result == null){
			returnMap.put("resultCode", Hint.SP_12010_BANK_ACCOUNT_NOT_EXIST.getCodeString());
			returnMap.put("resultDesc", Hint.SP_12010_BANK_ACCOUNT_NOT_EXIST.getMessage());
			return returnMap;
		}
		// 有效操作
		if(BaseDictConstant.VALID_FLAG_YES==state){
			if(result.getValidFlag() == BaseDictConstant.VALID_FLAG_YES){
				returnMap.put("resultCode", Hint.SP_12011_BANK_ACCOUNT_VALID.getCodeString());
				returnMap.put("resultDesc", Hint.SP_12011_BANK_ACCOUNT_VALID.getMessage());
			}else{
				updateActSpBankValidFlag(actId, cardNo, BaseDictConstant.VALID_FLAG_YES, remark, date);
			    returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			}
	
		}else if(BaseDictConstant.VALID_FLAG_NO==state){
			if(result.getValidFlag() == BaseDictConstant.VALID_FLAG_NO){
				returnMap.put("resultCode", Hint.SP_12012_BANK_ACCOUNT_UNVALID.getCodeString());
				returnMap.put("resultDesc", Hint.SP_12012_BANK_ACCOUNT_UNVALID.getMessage());
			}else{
				updateActSpBankValidFlag(actId, cardNo, BaseDictConstant.VALID_FLAG_NO, remark, date);
			    returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			}
	
		}

		return returnMap;
	}
	
	@Override
	public ActSp findSpActByActId(Long actId) {
		return actSpMapper.selectByActId(actId);
	}

	@Override
	public List<ActSpBank> getBankCardListByActId(Long actId) {
		return actSpBankMapper.getListByActId(actId);
	}

	private long updateSeqActId() {
		return seqService.updateSeqActId();
	}

	private long updateSeqUserId() {
		return seqService.updateSeqUserId();
		
	}

//	@Override
//	public List<SpUser> findSpUserByActId(Long actId) {
//		List<SpUser> spUserList = spUserMapper.selectByActId(actId);
//		return spUserList;
//	}

	private void updateActSpBankValidFlag(Long actId, String cardNo, short validFlag, String remark, Date date){
		ActSpBank record = new ActSpBank();
		record.setActId(actId);
		record.setBankAccount(cardNo);
		record.setValidFlag(validFlag);
		record.setRemark(remark);
		record.setUpdTime(date);
		actSpBankMapper.updateBankAccountValid(record);
	}
	/**
	 * 
	 * @Description: 更新账户状态
	 * @Title: updateActSp 
	 * @param actId
	 * @param stateId
	 * @param remark
	 * @param date void
	 */
	private void updateActSp(Long actId, short stateId, String remark, Date date) {
		ActSp actSpUpdate = new ActSp();
		actSpUpdate.setActId(actId);
		actSpUpdate.setActStatId(stateId);
		actSpUpdate.setRemark(remark);
		actSpUpdate.setUpdTime(date);
		actSpMapper.updatStateByActId(actSpUpdate);
	}

	/**
	 * 
	 * @Description: 更新商户用户 
	 * @Title: updateSpUser 
	 * @param actId
	 * @param userStateId
	 * @param date void
	 */
	private void updateSpUser(Long actId, short userStateId, Date date) {
		// 根据账户id 冻结用户
		SpUser user = new SpUser();
		user.setActId(actId);
		user.setSpUserStatId(userStateId);
		user.setUpdTime(date);
		spUserMapper.updateByActId(user);
	}

	@Override
	public List<ActSp> findActSpByChanActId(Long chanActId) {
		return actSpMapper.selectByChanActId(chanActId);
	}


	/**
	 * 
	 * @Description: 从map中获取对应name的值的字符串
	 * @Title: getString 
	 * @param receiveMap
	 * @param name
	 * @return String
	 */
	private String getString(Map<String, Object> receiveMap, String name){
		Object o = receiveMap.get(name);
		if(o == null)return null;
		return o.toString();
	}

	/**
	 * 
	 * @Description: 根据登录名查询商户用户
	 * @Title: findSpUserByUserCode 
	 * @param spUserCode
	 * @return SpUser
	 */
	private SpUser findSpUserByUserCode(String spUserCode) {
		return spUserMapper.selectByUserCode(spUserCode);
	}

	@Override
	public ActSpBank findActSpBankByActId(Long actId) {
		return actSpBankMapper.selectByActId(actId);
	}

	@Override
	public ActSpExt findActSpExtByActId(Long actId) {
		return actSpExtMapper.findActSpExtByActId(actId);
	}
	
}
