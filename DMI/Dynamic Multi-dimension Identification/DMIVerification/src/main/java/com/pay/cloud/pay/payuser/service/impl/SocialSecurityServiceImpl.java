package com.pay.cloud.pay.payuser.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.core.redis.RedisService;
import com.pay.cloud.pay.payuser.dao.UcBindMiMapper;
import com.pay.cloud.pay.payuser.entity.ActPerson;
import com.pay.cloud.pay.payuser.entity.PayUser;
import com.pay.cloud.pay.payuser.entity.UcBindAuthCol;
import com.pay.cloud.pay.payuser.entity.UcBindMi;
import com.pay.cloud.pay.payuser.service.PayUserService;
import com.pay.cloud.pay.payuser.service.SocialSecurityService;
import com.pay.cloud.pay.proplat.dao.PpMiProjectMapper;
import com.pay.cloud.pay.proplat.entity.PpMiProject;
import com.pay.cloud.pay.seq.service.SeqService;
import com.pay.cloud.util.ConvertUtils;
import com.pay.cloud.util.DimDictEnum;
import com.pay.cloud.util.SmsUtil;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

@Service("socialSecurityServiceImpl")
public class SocialSecurityServiceImpl implements SocialSecurityService{
	
	private static final Logger logger = Logger.getLogger(SocialSecurityServiceImpl.class);
	
	@Resource(name="payUserServiceImpl")
	private PayUserService payUserService;
	@Autowired
	private PpMiProjectMapper ppMiProjectMapper;
	@Autowired
	private UcBindMiMapper ucBindMiMapper;
	@Resource(name="seqServiceImpl")
	private SeqService seqService;
	@Resource(name="redisService")
	private RedisService redisService;
	/**
	 * 社保卡绑定
	 */
	@Override
	public Map<String, Object> socialSecurityBind(Map<String, Object> inputMap) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try{
			String userId = inputMap.get("userId")==null?"":inputMap.get("userId").toString().trim();
			String cityId = inputMap.get("cityId")==null?"":inputMap.get("cityId").toString().trim();
			String cardName = inputMap.get("cardName")==null?"":inputMap.get("cardName").toString().trim();
			String cardNo = inputMap.get("cardNo")==null?"":inputMap.get("cardNo").toString().trim();
			String socialSecurityNo = inputMap.get("socialSecurityNo")==null?"":inputMap.get("socialSecurityNo").toString().trim();
			String socialSecurityPassword = inputMap.get("socialSecurityPassword")==null?"":inputMap.get("socialSecurityPassword").toString().trim();
			
			PpMiProject ppMiProject = ppMiProjectMapper.selectByPrimaryKey(cityId);
			if(ppMiProject == null){
				resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33003_CITY_ID_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33003_CITY_ID_ERROR.getMessage());
				return resultMap;
			}
			
			PayUser payUser = payUserService.findByPayUserId(userId);
			if(payUser!=null && payUser.getActId()!=null){
				ActPerson actPerson = payUserService.findActPersonById(payUser.getActId());
				if(actPerson==null){
					resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
					resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
					return resultMap;
				}
				//判断是否实名认证
				if(actPerson.getRealNameFlag()!=null && actPerson.getRealNameFlag().equals(BaseDictConstant.ACT_PERSON_REAL_NAME_FLAG_TRUE)){
					/**
					 * 校验社保卡信息是否有效
					 */
					if(true){
						UcBindMi ucBindMi = new UcBindMi();
						ucBindMi.setActId(actPerson.getActId());
						ucBindMi.setpCertNo(cardNo.toUpperCase());
						ucBindMi.setMiCardNo(socialSecurityNo);
						ucBindMi.setMicId(ppMiProject.getMicId());
						List<UcBindMi>  ucBindMiList = ucBindMiMapper.selectByUcBindMi(ucBindMi);
						if(ucBindMiList!=null && ucBindMiList.size()>0){
							for(int i =  0;i < ucBindMiList.size();i ++){
								UcBindMi ubm = ucBindMiList.get(i);
								//判断是否有不可用的，如果不可用更改删除状态，如果可用返回错误信息
								if(ubm.getUcBindStatId().toString().equals(String.valueOf(BaseDictConstant.UC_BIND_MI_UC_BIND_STAT_ID_DISABLED))){
									UcBindMi updateUcBindMi = new UcBindMi();
									updateUcBindMi.setUcBindId(ubm.getUcBindId());
									updateUcBindMi.setUcBindStatId(BaseDictConstant.UC_BIND_MI_UC_BIND_STAT_ID_DEL);
									ucBindMiMapper.updateByPrimaryKeySelective(updateUcBindMi);
								}else if(ubm.getUcBindStatId().toString().equals(String.valueOf(BaseDictConstant.UC_BIND_MI_UC_BIND_STAT_ID_ABLED))){
									resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33008_ERROR.getCodeString());
									resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33008_ERROR.getMessage());
									return resultMap;
								}else{
									resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33008_ERROR.getCodeString());
									resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33008_ERROR.getMessage());
									return resultMap;
								}
							}
						}
						
						String pCertNo = actPerson.getpCertNo();
						Long ucBindId = seqService.updateSeqUcBindMi();
						ucBindMi.setUcBindId(ucBindId);
						ucBindMi.setpName(cardName);
						//---设置社保卡信息---
						String miCode = new Date().getTime()+"";
						ucBindMi.setMiPCode(miCode);
						String pmiId = new Date().getTime()+"";
						ucBindMi.setpMiId(pmiId);
						ucBindMi.setKfPrivTypeId(BaseDictConstant.UC_BIND_MI_KF_PRIV_TYPE_ID_1);
						//------end-------
						
						if(cardNo.equals(pCertNo)){
							ucBindMi.setMainBindFlag(BaseDictConstant.UC_BIND_MI_MAIN_BIND_FLAG_TRUE);
							resultMap.put("isMain", BaseDictConstant.UC_BIND_MI_MAIN_BIND_FLAG_TRUE);
						}else{
							ucBindMi.setMainBindFlag(BaseDictConstant.UC_BIND_MI_MAIN_BIND_FLAG_FALSE);
							resultMap.put("isMain", BaseDictConstant.UC_BIND_MI_MAIN_BIND_FLAG_FALSE);
						}
						ucBindMi.setUcBindStatId(BaseDictConstant.UC_BIND_MI_UC_BIND_STAT_ID_DISABLED);
						ucBindMiMapper.insertSelective(ucBindMi);
						
						resultMap.put("socialSecurityId", ucBindId);
						resultMap.put("cardName", cardName);
						resultMap.put("cardNo", cardNo);
						resultMap.put("socialSecurityNo", socialSecurityNo);
						resultMap.put("miPCode", miCode);
						resultMap.put("pMiId", pmiId);
						
						resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
						resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
						
						return resultMap;
					}else{//如果无效
						resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33004_MI_ERROR.getCodeString());
						resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33004_MI_ERROR.getMessage());
						return resultMap;
					}
					
				}else{
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33002_REAL_NAME_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33002_REAL_NAME_ERROR.getMessage());
					return resultMap;
				}
				
			}else{
			
				resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
				return resultMap;
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("访问接口出现异常：" + ex.getMessage(), ex);
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		
	}

	/**
	 * 社保卡解绑
	 */
	@Override
	public Map<String, Object> socialSecurityUnbind(Map<String, Object> inputMap) {
		String userId = inputMap.get("userId")==null?"":inputMap.get("userId").toString().trim();
		String socialSecurityId = inputMap.get("socialSecurityId")==null?"0":inputMap.get("socialSecurityId").toString().trim();
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try{
			PayUser payUser = payUserService.findByPayUserId(userId);
			if(payUser!=null && payUser.getActId()!=null){
				ActPerson actPerson = payUserService.findActPersonById(payUser.getActId());
				if(actPerson==null){
					resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
					resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
					return resultMap;
				}
				Long socialSecurityId_l = Long.parseLong(socialSecurityId);
				UcBindMi ucBindMi = ucBindMiMapper.selectByPrimaryKey(socialSecurityId_l);
				if(ucBindMi==null){
					
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33005_NOT_MESSAGE_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33005_NOT_MESSAGE_ERROR.getMessage());
					return resultMap;
				}
				if(!ucBindMi.getActId().toString().equals(actPerson.getActId().toString())){
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33007_USERID_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33007_USERID_ERROR.getMessage());
					return resultMap;
				}
				ucBindMi.setUcBindStatId(BaseDictConstant.UC_BIND_MI_UC_BIND_STAT_ID_DEL);
				ucBindMi.setUpdTime(new Date());
				ucBindMiMapper.updateByPrimaryKeySelective(ucBindMi);
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				return resultMap;
			}else{
				resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
				return resultMap;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("访问接口出现异常：" + ex.getMessage(), ex);
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
	}

	@Override
	public Map<String, Object> socialSecurityQuery(Map<String, Object> inputMap) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try{
			String userId = inputMap.get("userId")==null?"":inputMap.get("userId").toString().trim();
			String cityId = inputMap.get("cityId")==null?"":inputMap.get("cityId").toString().trim();
			PayUser payUser = payUserService.findByPayUserId(userId);
			if(payUser!=null && payUser.getActId()!=null){
				ActPerson actPerson = payUserService.findActPersonById(payUser.getActId());
				if(actPerson==null){
					resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
					resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
					return resultMap;
				}
				UcBindMi selectUucBindMi = new UcBindMi();
				if(!cityId.equals("")){
					PpMiProject ppMiProject = ppMiProjectMapper.selectByPrimaryKey(cityId);
					if(ppMiProject == null){
						resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33003_CITY_ID_ERROR.getCodeString());
						resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33003_CITY_ID_ERROR.getMessage());
						return resultMap;
					}
					selectUucBindMi.setMicId(ppMiProject.getMicId());
				}
				selectUucBindMi.setActId(actPerson.getActId());
				List<UcBindMi> ucBindMiList = ucBindMiMapper.selectByActId(selectUucBindMi);
				if(ucBindMiList!=null && ucBindMiList.size()>0){
					List<Map<String,Object>> ucBindMiListMap = new ArrayList<Map<String,Object>>();
					for(int i = 0;i <ucBindMiList.size();i ++){
						Map<String,Object> ucBindMap = new HashMap<String,Object>();
						UcBindMi ucBindMi = ucBindMiList.get(i);
						ucBindMap.put("socialSecurityId", ucBindMi.getUcBindId());
						ucBindMap.put("cityId", ucBindMi.getMicId());
						ucBindMap.put("cityId", ucBindMi.getMicId());
						ucBindMap.put("cardName", ucBindMi.getpName());
						ucBindMap.put("cardNo", ucBindMi.getpCertNo());
						ucBindMap.put("socialSecurityNo", ucBindMi.getMiCardNo());
						ucBindMap.put("miPCode", ucBindMi.getMiPCode());
						ucBindMap.put("pMiId", ucBindMi.getpMiId());
						ucBindMap.put("isMain", ucBindMi.getMainBindFlag());
						
						ucBindMiListMap.add(ucBindMap);
					}
					resultMap.put("socialSecurityList", ucBindMiListMap);
					resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					return resultMap;
				}else{
					resultMap.put("socialSecurityList", new ArrayList<Map<String,Object>>());
					resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
					resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
					return resultMap;
				}
			}else{
				resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
				resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
				return resultMap;
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("访问接口出现异常：" + ex.getMessage(), ex);
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
		
	}

	@Override
	public Map<String, Object> socialSecurityConfig(Map<String, Object> inputMap) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try{
			List<PpMiProject> ppMiProjectList = ppMiProjectMapper.PpMiProjectAllJoinUcBindAuthCol();
			if(ppMiProjectList!=null && ppMiProjectList.size()>0){
				List<Map<String, Object>> ppMiProjectListResult = new ArrayList<Map<String, Object>>();
				resultMap.put("cityList", new ArrayList());
				String cityName;
				for(int i = 0;i <ppMiProjectList.size();i ++){
					Map<String,Object> ppMiProjectMap = new HashMap<String,Object>();
					PpMiProject ppMiProject = ppMiProjectList.get(i);
					ppMiProjectMap.put("cityId", ppMiProject.getMicId());
					cityName = String.valueOf(ppMiProject.getProjNameL2()).trim();
					if(ValidateUtils.isEmpty(cityName)){
						cityName = "";
					}else {
						if(cityName.endsWith("市")){
							cityName = cityName.replaceAll("市", "");
						}
					}
					ppMiProjectMap.put("cityName", cityName);
					ppMiProjectMap.put("fieldList", new ArrayList());
					if(ppMiProject.getUcBindAuthColList()!=null && ppMiProject.getUcBindAuthColList().size()>0){
						List<String> colList = new ArrayList<String>(); 
						List<UcBindAuthCol> ucBindAuthColList = ppMiProject.getUcBindAuthColList();
						
						for(int j = 0;j < ucBindAuthColList.size();j ++){
							//Map<String,Object> colMap = new HashMap<String,Object>();
							UcBindAuthCol ucBindAuthCol = ucBindAuthColList.get(j);
							//colMap.put("fieldName", ucBindAuthCol.getColName());
							colList.add(ucBindAuthCol.getColName());
						}
						ppMiProjectMap.put("fieldList", colList);
					}
					ppMiProjectListResult.add(ppMiProjectMap);
				}
				resultMap.put("cityList", ppMiProjectListResult);
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				return resultMap;
			}
			resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33001_QUERY_COL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33001_QUERY_COL_ERROR.getMessage());
			return resultMap;
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("访问接口出现异常：" + ex.getMessage(), ex);
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
	}

	@Override
	public Map<String, Object> socialSecurityRealMain(Map<String, Object> inputMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String userId = inputMap.get("userId").toString().trim();
		String phone = inputMap.get("phone").toString().trim();
		Hint hint = SmsUtil.smsCodeCheck(phone, inputMap.get("hardwareId").toString(), inputMap.get("verifyCode").toString(), redisService);
		if(hint.getCode() != 0){
			logger.info("验证码验证失败");
			return ConvertUtils.genReturnMap(hint);
		}
		PayUser payUser = payUserService.findByPayUserId(userId);
		if(null == payUser || payUser.getActId() == null){
			resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
			resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
			return resultMap;
		}
		ActPerson actPerson = payUserService.findActPersonById(payUser.getActId());
		if(actPerson == null){
			resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
			resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
			return resultMap;
		}
		if(actPerson.getRealNameFlag() == null || actPerson.getRealNameFlag() != 1){
			resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33002_REAL_NAME_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33002_REAL_NAME_ERROR.getMessage());
			return resultMap;
		}
		String realNameChannel = DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString();
		if(null != actPerson.getpCertTypeId()){
			realNameChannel = actPerson.getpCertTypeId().toString();
		}
		if(realNameChannel.equals(DimDictEnum.REAL_NAME_FOUR_ELEMENTS_PUHUI.getCodeString())){
			if(!actPerson.getAuthMobile().equals(phone)){
				resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33010_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33010_ERROR.getMessage());
				return resultMap;
			}
		}else if(realNameChannel.equals(DimDictEnum.REAL_NAME_THREE_ELEMENTS_HUIYUE.getCodeString())){
			if(!payUser.getPayUserCode().equals(phone)){
				resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33012_ERROR.getCodeString());
				resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33012_ERROR.getMessage());
				return resultMap;
			}
		}
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	@Override
	public Map<String, Object> socialSecurityRealFu(Map<String, Object> inputMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userId = inputMap.get("userId").toString().trim();
		
			String socialSecurityId = inputMap.get("idCard").toString().trim();
			PayUser payUser = payUserService.findByPayUserId(userId);
			if(payUser!=null && payUser.getActId()!=null){
				ActPerson actPerson = payUserService.findActPersonById(payUser.getActId());
				if(actPerson==null){
					resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
					resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
					return resultMap;
				}
				if(actPerson.getpCertNo()==null || actPerson.getpCertId().equals("")){
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33002_REAL_NAME_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33002_REAL_NAME_ERROR.getMessage());
					return resultMap;
				}
				UcBindMi ucBindMi = ucBindMiMapper.selectByPrimaryKey(Long.parseLong(socialSecurityId));
				if(ucBindMi==null){
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33005_NOT_MESSAGE_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33005_NOT_MESSAGE_ERROR.getMessage());
					return resultMap;
				}
				if(!ucBindMi.getActId().toString().equals(actPerson.getActId().toString())){
					
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33011_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33011_ERROR.getMessage());
					return resultMap;
				}
				
				UcBindMi updateUcBindMi = new UcBindMi();
				updateUcBindMi.setUcBindId(ucBindMi.getUcBindId());
				updateUcBindMi.setUcBindStatId(BaseDictConstant.UC_BIND_MI_UC_BIND_STAT_ID_ABLED);
				ucBindMiMapper.updateByPrimaryKeySelective(updateUcBindMi);
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				return resultMap;
			}else{
				resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
				resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
				return resultMap;
			}
		}catch(Exception ex){
			logger.error("访问接口出现异常：" + ex.getMessage(), ex);
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
	}

	@Override
	public Map<String, Object> socialSecurityQueryMoney(Map<String, Object> inputMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String userId = inputMap.get("userId").toString().trim();
			String socialSecurityId = inputMap.get("socialSecurityId").toString().trim();
			PayUser payUser = payUserService.findByPayUserId(userId);
			if(payUser!=null && payUser.getActId()!=null){
				ActPerson actPerson = payUserService.findActPersonById(payUser.getActId());
				if(actPerson==null){
					resultMap.put("resultCode", Hint.USER_11001_LOGININFO_NOTEXIST.getCodeString());
					resultMap.put("resultDesc", Hint.USER_11001_LOGININFO_NOTEXIST.getMessage());
					return resultMap;
				}
				UcBindMi ucBindMi = ucBindMiMapper.selectByPrimaryKey(Long.parseLong(socialSecurityId));
				if(ucBindMi==null){
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33005_NOT_MESSAGE_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33005_NOT_MESSAGE_ERROR.getMessage());
					return resultMap;
				}
				if(!ucBindMi.getActId().toString().equals(actPerson.getActId().toString())){
					
					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33011_ERROR.getCodeString());
					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33011_ERROR.getMessage());
					return resultMap;
				}
				//不同地区走不同渠道查询余额
				logger.info("社保城市："+ucBindMi.getMicId());
				//
//				PpMiProject ppMiProject = ppMiProjectMapper.selectByPrimaryKey(ucBindMi.getMicId());
//				if(ppMiProject==null){
//					resultMap.put("resultCode", Hint.SOCIAL_SECURITY_33003_CITY_ID_ERROR.getCodeString());
//					resultMap.put("resultDesc", Hint.SOCIAL_SECURITY_33003_CITY_ID_ERROR.getMessage());
//					return resultMap;
//				}
				DecimalFormat df = new DecimalFormat("0.00"); // 保留几位小数
				resultMap.put("money",df.format(new BigDecimal(Math.random()*100)));
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				return resultMap;
			}else{
				resultMap.put("resultCode", Hint.USER_25009_USER_UNDEFINED.getCodeString());
				resultMap.put("resultDesc", Hint.USER_25009_USER_UNDEFINED.getMessage());
				return resultMap;
			}
		}catch(Exception ex){
			logger.error("访问接口出现异常：" + ex.getMessage(), ex);
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
			return resultMap;
		}
	}
	
}
