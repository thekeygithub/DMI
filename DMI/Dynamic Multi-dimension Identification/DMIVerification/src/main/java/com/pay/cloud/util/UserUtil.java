package com.pay.cloud.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pay.cloud.constants.SmsConstant;
import com.pay.cloud.gw.service.payuser.impl.PayUserServiceGWImpl;
import com.pay.cloud.pay.payuser.entity.PayUser;
import com.pay.cloud.util.hint.Hint;

import java.util.Date;
import java.util.Map;

/**
 * 
 * @Description: 用户相关验证方法类
 * @ClassName: UserUtil 
 * @author: zhengping.hu
 * @date: 2016年4月25日 上午11:39:21
 */
public class UserUtil {

	/**
	 * 
	 * @Description: 用户状态是否是禁用或注销
	 * @Title: isUserFalse 
	 * @param user
	 * @return Hint
	 */
	public static Hint isUserFalse(PayUser user){
		if (user.getPayUserStatId() == 2) {// 被禁用
			return Hint.USER_25012_USER_FORBIDDEN;
		} else if (user.getPayUserStatId() == 9) {// 已注销
			return Hint.USER_25013_USER_WRITTEN_OFF;
		}else{
			return Hint.SYS_SUCCESS;
		}
	}
	
	/**
	 * 
	 * @Description: 密码验证
	 * @Title: comparePass 
	 * @param newpass
	 * @param user
	 * @return boolean
	 */
	public static boolean comparePass(String newpass, PayUser user){
		if (newpass!=null && newpass.trim().equals(user.getPwd())) {
			return true;
		} else{
			return false;
		}
	}

	/**
	 * 检测是否设置支付密码和实名认证 0-已设置且实名 1-未设置支付密码 2-未实名认证
	 * @param request
	 * @return
	 */
	public static String checkPasswordAndRealName(HttpServletRequest request, PayUserServiceGWImpl payUserServiceGWImpl){
		HttpSession session = request.getSession();
		Map<String,Object> sessionMap = (Map<String, Object>) session.getAttribute(SmsConstant.SESSION_USER_KEY);

		Map<String, Object> resultMap = payUserServiceGWImpl.checkPayPwd((Long) sessionMap.get("accountId"));
		if(!resultMap.get("resultCode").equals(Hint.SYS_SUCCESS.getCodeString())){
			return "1";
		}
		//检查是否实名认证(0否，1已实名认证)
		String realNameStatus = String.valueOf(sessionMap.get("realNameStatus")).trim();
		if("1".equals(realNameStatus)){
		}else{
			//未实名认证
			return "2";
		}
		return "0";
	}

	/**
	 * 计算用户周岁
	 * @param birthday 生日，格式：yyyy-MM-dd
	 * @return 周岁
     */
	public static int calcUserAge(String birthday) {
		String nowDate = DateUtils.dateToString(new Date(), "yyyy-MM-dd");
		String[] arr1 = nowDate.split("-");
		String[] arr2 = birthday.split("-");
		int age = Integer.parseInt(arr1[0]) - Integer.parseInt(arr2[0]);//年份相减，得出周岁
		//根据月日微调[判断是否大于当前月日(大于减一岁，小于等于不增减)]
		if(Integer.parseInt(arr2[1].concat(arr2[2])) > Integer.parseInt(arr1[1].concat(arr1[2]))){
			age = age - 1;
		}
		if(age < 0){
			age = 0;
		}
		return age;
	}
}
