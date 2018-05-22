package com.models.cloud.h5.controller.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.BaseDictConstant;
import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.protocolfactory.impl.PrePaymentOrderInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.QueryCardListInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.QueryRealNameInfoInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.SocialSecurityQueryInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.VerifyUserPaymentPwdInterfaceImpl;
import com.models.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.hint.Hint;
/**
 * 
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年9月20日 
 * @time 下午1:42:34
 * @version V1.0
 * @修改记录
 *
 */
@Service("verifyPWDToNextServiceImpl")
public class VerifyPWDToNextServiceImpl implements DoPageService {
	private Logger logger = Logger.getLogger(VerifyPWDToNextServiceImpl.class);
	
	@Resource
	private VerifyUserPaymentPwdInterfaceImpl verifyUserPaymentPwdInterfaceImpl;
	@Resource
	private PrePaymentOrderInterfaceImpl prePaymentOrderInterfaceImpl;
	@Resource
	QueryCardListInterfaceImpl queryCardListInterfaceImpl;
	@Resource
	PayUserServiceGWImpl payUserServiceGWImpl;
	@Resource
	QueryRealNameInfoInterfaceImpl queryRealNameInfoInterfaceImpl;
	@Resource
	SocialSecurityQueryInterfaceImpl socialSecurityQueryInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map, Model model, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		if(logger.isInfoEnabled()){
			logger.info("验证支付密码");
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	
		
		//1.验证支付密码
		Map<String,Object> verifyPwdMap = new HashMap<String,Object>();
		verifyPwdMap.put("interfaceCode", "verifyPayPassword");
		verifyPwdMap.put("accountId", String.valueOf((Long) sessionMap.get("accountId")));
		verifyPwdMap.put("payPassword", (String)map.get("payPassword"));
		verifyPwdMap.put("hardwareId", (String)map.get("hardwareId"));//硬件id
		if(logger.isInfoEnabled()){
			logger.info("请求 "+verifyPwdMap.get("interfaceCode")+" 接口，请求参数："+verifyPwdMap);
		}
		resultMap = verifyUserPaymentPwdInterfaceImpl.doService(verifyPwdMap);
		
		if(logger.isInfoEnabled()){
			logger.info("请求 "+verifyPwdMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
		}
		
		resultMap = ConvertUtils.getMappingHintMessage((String)verifyPwdMap.get("interfaceCode"),resultMap);
		if(logger.isInfoEnabled()){
			logger.info("错误码转换结果："+resultMap);
		}
		if(!resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			return resultMap;
		}
		
		sessionOrderMap.put("payToken", resultMap.get("payToken"));
		session.setAttribute(SmsConstant.SESSION_USER_ORDER_KEY,sessionOrderMap);
		Map<String,Object> queryMap = new HashMap<String,Object>();
		//支付通道方式:1-商业支付 2-社保支付 3-混合支付
		String usePayChannelType = (String)sessionOrderMap.get("usePayChannelType");
		if(usePayChannelType.equals("1")){
			List businessPayTypesList = (List)sessionOrderMap.get("businessPayTypes");
			if(null == businessPayTypesList || businessPayTypesList.isEmpty()){
				resultMap.put("resultCode","999");
				resultMap.put("resultDesc","此支付方式暂不支持");
				return resultMap;
			}
			if(((Map)businessPayTypesList.get(0)).get("channelCode").equals(BaseDictConstant.PP_FUND_TYPE_ID_EBZF)){
				//查询银行卡列表 
				if(logger.isInfoEnabled()){
					logger.info("查询银行卡列表");
				}
				
				queryMap.put("interfaceCode", "queryCardList");
				queryMap.put("accountId", String.valueOf((Long) sessionMap.get("accountId")));
				queryMap.put("payToken", resultMap.get("payToken"));
				queryMap.put("hardwareId", (String)map.get("hardwareId"));
				if(logger.isInfoEnabled()){
					logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，请求参数："+queryMap);
				}
				resultMap = queryCardListInterfaceImpl.doService(queryMap);
				if(logger.isInfoEnabled()){
					logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
				}
				resultMap.put("type", BaseDictConstant.PP_FUND_TYPE_ID_EBZF);
				resultMap = ConvertUtils.getMappingHintMessage((String)queryMap.get("interfaceCode"),resultMap);
				if(logger.isInfoEnabled()){
					logger.info("错误码转换结果："+resultMap);
				}
			}
		}else if(usePayChannelType.equals("2") || usePayChannelType.equals("3")){
			List medicarePayTypesList = (List)sessionOrderMap.get("medicarePayTypes");
			if(null == medicarePayTypesList || medicarePayTypesList.isEmpty()){
				resultMap.put("resultCode","999");
				resultMap.put("resultDesc","此支付方式暂不支持");
				return resultMap;
			}
			
			//医保支付区别主副卡并跳转到不同的页面
			//社保卡ID
			String socialSecurityBindId = (String)sessionOrderMap.get("socialSecurityBindId");
			//查询社保卡列表
			if(logger.isInfoEnabled()){
				logger.info("查询社保卡列表");
			}
			Map<String,Object> querySocialMap = new HashMap<String,Object>();
			querySocialMap.put("interfaceCode","socialSecurityQuery");
			querySocialMap.put("userId",String.valueOf((Long)sessionMap.get("userId")));
			resultMap =socialSecurityQueryInterfaceImpl.doService(querySocialMap);
			if(logger.isInfoEnabled()){
				logger.info("请求 "+querySocialMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
			}
			resultMap = ConvertUtils.getMappingHintMessage((String)querySocialMap.get("interfaceCode"),resultMap);
			if(logger.isInfoEnabled()){
				logger.info("错误码转换结果："+resultMap);
			}
			if(!resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
				return resultMap;
			}
			List socialSecurityList = (List) resultMap.get("socialSecurityList");
			if(null ==socialSecurityList || socialSecurityList.isEmpty()){
				resultMap.put("resultCode","999");
				resultMap.put("resultDesc","此支付方式暂不支持");
				return resultMap;
			}
			String isMain = "";
			for(int i=0;i<socialSecurityList.size(); i++){
				if(socialSecurityBindId.equals(String.valueOf((Long)((Map)socialSecurityList.get(i)).get("socialSecurityId")))){
					
					isMain = String.valueOf(((Map)socialSecurityList.get(i)).get("isMain"));
					if("1".equals(isMain)){
						//主卡-------->查询实名认证信息,验证医保账户
						if(logger.isInfoEnabled()){
							logger.info("查询实名认证信息,验证医保账户");
						}
						Map<String,Object> queryRealNameInfoMap = new HashMap<String,Object>();
						queryRealNameInfoMap.put("interfaceCode","queryRealNameInfo");
						queryRealNameInfoMap.put("userId",String.valueOf((Long)sessionMap.get("userId")));
						resultMap = queryRealNameInfoInterfaceImpl.doService(queryRealNameInfoMap);
						if(logger.isInfoEnabled()){
							logger.info("请求 "+queryRealNameInfoMap.get("interfaceCode")+" 接口，返回结果："+resultMap);
						}
						resultMap = ConvertUtils.getMappingHintMessage((String)queryRealNameInfoMap.get("interfaceCode"),resultMap);
						if(logger.isInfoEnabled()){
							logger.info("错误码转换结果："+resultMap);
						}
						
					}
				}
			}
			resultMap.put("isMain",isMain);
			resultMap.put("type","SBZF");
			
		}else{
			resultMap.put("resultCode","999");
			resultMap.put("resultDesc","此支付方式暂不支持");
		}
		
		return resultMap;
	}

}
