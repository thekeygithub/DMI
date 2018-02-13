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
import com.models.cloud.gw.protocolfactory.impl.ConfirmPaymentInterfaceImpl;
import com.models.cloud.gw.protocolfactory.impl.SmsValidateCodeImpl;
import com.models.cloud.h5.controller.DoPageService;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 
 * @Description
 * @encoding UTF-8 
 * @author haiyan.zhang
 * @date 2016年9月23日 
 * @time 下午4:07:53
 * @version V1.0
 * @修改记录
 *
 */

@Service("faceConfirmToNextPageImpl")
public class FaceConfirmToNextPageImpl implements DoPageService{
	private Logger logger = Logger.getLogger(FaceConfirmToNextPageImpl.class);
	
	@Resource(name="smsValidateCodeImpl")
	private SmsValidateCodeImpl smsValidateCodeImpl;

	@Resource
	private ConfirmPaymentInterfaceImpl confirmPaymentInterfaceImpl;
	
	@Override
	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		returnMap.put("resultCode", "0");
		returnMap.put("resultDesc", "操作成功");
		return returnMap;
	}

	@Override
	public Map<String, Object> returnDataByMap(Map<String, Object> receiveMap, HttpServletRequest request) {
		Map<String,Object> returnMap =  new HashMap<String,Object>();
		//支付通道方式:1-商业支付 2-社保支付 3-混合支付
		HttpSession session = request.getSession();
    	Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
    	Map<String,Object> sessionOrderMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_ORDER_KEY);
    	String usePayChannelType = (String)sessionOrderMap.get("usePayChannelType");
    	
    	if(usePayChannelType.equals("1")){
    		List businessPayTypesList = (List)sessionOrderMap.get("businessPayTypes");
			if(null == businessPayTypesList || businessPayTypesList.isEmpty()){
				returnMap.put("resultCode","999");
				returnMap.put("resultDesc","此支付方式暂不支持");
				return returnMap;
			}
    		if(((Map)businessPayTypesList.get(0)).get("channelCode").equals(BaseDictConstant.PP_FUND_TYPE_ID_EBZF)){
				if(logger.isInfoEnabled()){
					logger.info("纯商业支付--易pay支付");
				}
				returnMap.put("type","EBZF");
			}
    		return returnMap;
    		
		}else if(usePayChannelType.equals("2")){
			if(logger.isInfoEnabled()){
				logger.info("纯医保支付");
			}
			//纯医保确认支付
			Map<String,Object> queryMap = new HashMap<String,Object>();
			queryMap.put("interfaceCode","confirmPayment");
			queryMap.put("accountId", String.valueOf((Long) sessionMap.get("accountId")));
			queryMap.put("payOrderId", (String) sessionOrderMap.get("payOrderId"));
			queryMap.put("payToken", (String) sessionOrderMap.get("payToken"));
			queryMap.put("hardwareId", (String) receiveMap.get("hardwareId"));
			if(logger.isInfoEnabled()){
				logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，请求参数："+queryMap);
			}
			returnMap = confirmPaymentInterfaceImpl.doService(queryMap);
			if(logger.isInfoEnabled()){
				logger.info("请求 "+queryMap.get("interfaceCode")+" 接口，返回结果："+returnMap);
			}
			returnMap = ConvertUtils.getMappingHintMessage((String)queryMap.get("interfaceCode"),returnMap);
			if(logger.isInfoEnabled()){
				logger.info("错误码转换结果："+returnMap);
			}
			returnMap.put("type","SBZF");
			return returnMap;
			
		}else if(usePayChannelType.equals("3")){
			List businessPayTypesList = (List)sessionOrderMap.get("businessPayTypes");
			if(null == businessPayTypesList || businessPayTypesList.isEmpty()){
				returnMap.put("resultCode","999");
				returnMap.put("resultDesc","此支付方式暂不支持");
				return returnMap;
			}
			if(((Map)businessPayTypesList.get(0)).get("channelCode").equals(BaseDictConstant.PP_FUND_TYPE_ID_EBZF)){
				if(logger.isInfoEnabled()){
					logger.info("混合支付--医保+易pay支付");
				}
				returnMap.put("type","EBZF");
			}else if(((Map)businessPayTypesList.get(0)).get("channelCode").equals(BaseDictConstant.PP_FUND_TYPE_ID_CCB)){
				if(logger.isInfoEnabled()){
					logger.info("混合支付--医保+建行支付");
				}
				returnMap.put("type","CCB");
			}else if(((Map)businessPayTypesList.get(0)).get("channelCode").equals(BaseDictConstant.PP_FUND_TYPE_ID_ALI)
					|| ((Map)businessPayTypesList.get(0)).get("channelCode").equals(BaseDictConstant.PP_FUND_TYPE_ID_ALI_IN)){
				if(logger.isInfoEnabled()){
					logger.info("混合支付--医保+支付宝支付");
				}
				returnMap.put("type","ALI");
			}else if(((Map)businessPayTypesList.get(0)).get("channelCode").equals(BaseDictConstant.PP_FUND_TYPE_ID_BKU)){
				if(logger.isInfoEnabled()){
					logger.info("混合支付--医保+银联支付");
				}
				returnMap.put("type","BKU");
			}
		}else{
			returnMap.put("resultCode","999");
			returnMap.put("resultDesc","此支付方式暂不支持");
			return returnMap;
		}
    	
    	returnMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		returnMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return returnMap;
	}

}
