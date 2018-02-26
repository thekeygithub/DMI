package com.models.cloud.web.controller.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.models.cloud.constants.SmsConstant;
import com.models.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.models.cloud.util.UserUtil;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.web.controller.DoWebPageService;

@Service("pcPayOrderPageServiceImpl")
public class PayOrderPageServiceImpl implements DoWebPageService {

	@Resource(name="payUserServiceGWImpl")
	private PayUserServiceGWImpl payUserServiceGWImpl;

	private Logger logger = Logger.getLogger(PayOrderPageServiceImpl.class);

	public Map<String, Object> returnData(Map<String, Object> map,Model model, HttpServletRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);
		String checkFlag = UserUtil.checkPasswordAndRealName(request, payUserServiceGWImpl);
		if("1".equals(checkFlag)){
			model.addAttribute("phone", sessionMap.get("phone"));
			if(logger.isInfoEnabled()){
				logger.info("用户未设置支付密码");
			}
			request.setAttribute("page", "web/user/setpaymentpass");
			return resultMap;
		}
		if("2".equals(checkFlag)){
			if(logger.isInfoEnabled()){
				logger.info("用户未实名认证");
			}
			request.setAttribute("page", "web/trade/verifyPayPwd");
			return resultMap;
		}

		String isUnBind = request.getParameter("isUnBind");
		if(isUnBind == null || "".equals(isUnBind)){
			isUnBind = "0";
		}
		if(logger.isInfoEnabled()){
			logger.info("跳转到付款页面，请求参数：isUnBind："+isUnBind);
		}
		
		resultMap.put("isUnBind", isUnBind);
		return resultMap;
	}

	public Map<String, Object> returnDataByMap(Map<String, Object> map, HttpServletRequest request) {
		return null;
	}
}
