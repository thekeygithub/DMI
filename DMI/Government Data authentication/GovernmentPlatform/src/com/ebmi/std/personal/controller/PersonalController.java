package com.ebmi.std.personal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.utils.JsonStringUtils;
import com.ebmi.std.common.controller.BaseController;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.personal.service.PersonalService;
import com.gd.platform.client.ResultEntity;

/**
 * @Description: 用户体系
 * @ClassName: PersonalController
 * @author: zhengping.hu
 * @date: 2015年12月21日 上午11:50:55
 */
@Controller
@RequestMapping(value = "/personal")
public class PersonalController extends BaseController {

	@Resource(name = "personalService")
	private PersonalService personalService;

	/**
	 * @Description: 注册--社保信息验证（允许第三方平台注册时，要求同时验证社保信息与第三方平台账户是否绑定）
	 * @Title: validateSocialsecurityInfo
	 * @param name 姓名
	 * @param id_no 身份证
	 * @param sb_no 社保卡
	 * @param sb_pass 社保卡密码
	 * @return String 验证通过：返回p_mi_id ，验证不通过：返回空
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/validatesocialsecurityinfo", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> validateSocialsecurityInfo(String id_no, String name) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		ResultEntity re = null;
		try {
			// step1
			re = personalService.validateSocialsecurityInfo(id_no, name);
			if ("ok".equalsIgnoreCase(re.getStatus())) { // 验证通过
				Map<String, Object> map = JsonStringUtils.jsonStringToObject(re.getContent(), Map.class);
				if ("".equals(map.get("error"))) { // 成功
					Map<String, String> m = ((List<Map<String, String>>) (map.get("data"))).get(0);
					resultMap.put("reg_key", m.get("col0"));
					resultMap.put("phone", m.get("col1"));
				} else { // 失败
					resultMap.put("error", map.get("error").toString());
				}
			} else {
				resultMap.put("error", re.getStatus());
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("hsoft.general.user.regist.step1  接口返回>>>>>>>>>>>>>>>>>>>>>>>>>>>>>re.getStatus()>>>>>"+re.getStatus()+">>>>>>>re.getError()>>>>>>"+re.getError());
				logger.debug("Content>>>>>"+re.getContent());
             }	
			
			// step2
			String phone = resultMap.get("phone");
			String reg_key = resultMap.get("reg_key");
			if (StringUtils.isNotBlank(phone)) {
				re = personalService.validateSmsCode(id_no, name, reg_key, phone);
				if ("ok".equalsIgnoreCase(re.getStatus())) { // 验证通过
					Map<String, Object> map = JsonStringUtils.jsonStringToObject(re.getContent(), Map.class);
					if ("".equals(map.get("error"))) { // 成功
						Map<String, String> m = ((List<Map<String, String>>) (map.get("data"))).get(0);
						resultMap.put("sms_key", m.get("col0"));
					} else { // 注册失败
						resultMap.put("error", map.get("error").toString());
					}
				} else {
					resultMap.put("error", re.getStatus());
				}
				if (logger.isDebugEnabled()) {
					logger.debug("hsoft.general.user.regist.step2   接口返回>>>>>>>>>>>>>>>>>>>>>>>>>>>>>re.getStatus()>>>>>"+re.getStatus()+">>>>>>>re.getError()>>>>>>"+re.getError());
					logger.debug("Content>>>>>"+re.getContent());
	             }	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	/**
	 * 
	 * @param id_no
	 * @param name
	 * @param user_regist_key
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/smssendcode", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> sendSmsCode(String id_no, String name, String reg_key, String phone) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		try {
			ResultEntity re = personalService.validateSmsCode(id_no, name, reg_key, phone);
			if ("ok".equalsIgnoreCase(re.getStatus())) { // 验证通过
				Map<String, Object> map = JsonStringUtils.jsonStringToObject(re.getContent(), Map.class);
				if ("".equals(map.get("error"))) { // 成功
					Map<String, String> m = ((List<Map<String, String>>) (map.get("data"))).get(0);
					resultMap.put("sms_key", m.get("col0"));
				} else { // 注册失败
					resultMap.put("error", map.get("error").toString());
				}
			} else {
				resultMap.put("error", re.getStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}

	/**
	 * @Description: 第三方平台注册
	 * @Title: register
	 * @param reg_key 用户注册主键序号
	 * @param id_no 身份证号
	 * @param name 姓名
	 * @param user_name 用户名
	 * @param user_pass 用户密码
	 * @param phone 手机号
	 * @param email 电子邮箱
	 * @param qq QQ号
	 * @param weixin 微信号
	 * @param sms_key 短信发送授权序号
	 * @param sms_code 短信验证码
	 * @return boolean 注册成功：true，注册失败：false
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String register(String reg_key, String id_no, String name, String user_name, String user_pass, String phone, String email,
			String qq, String weixin, String sms_key, String sms_code) {
		String result = "";
		try {
			ResultEntity re = personalService.register(reg_key, id_no, name, user_name, user_pass, phone, email, qq, weixin, sms_key, sms_code);
			System.out.println(re.getStatus()+","+re.getError());
		    System.out.println(re.getContent());
		    
			if ("ok".equalsIgnoreCase(re.getStatus())) {
				Map<String, Object> map = JsonStringUtils.jsonStringToObject(re.getContent(), Map.class);
				result = String.valueOf(map.get("error"));
			} else {
				result = re.getError();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Description: 第三方平台验证用户名称是否重复
	 * @Title: validateUserName
	 * @param user_name 用户名称
	 * @return boolean true:用户名不存在，false：用户名存在
	 */
	@RequestMapping(value = "/validateusername", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody boolean validateUserName(String user_name) {
		boolean result = false;
		try {
			result = personalService.validateUserName(user_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Description: 第三方平台更新手机号
	 * @Title: pushUserPhone
	 * @param p_mi_id 用户编号
	 * @param phone 手机号
	 * @return boolean true:更新成功，false：更新失败
	 */
	@RequestMapping(value = "/pushuserphone", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody boolean pushUserPhone(String p_mi_id, String phone) {
		boolean result = false;
		try {
			result = personalService.pushUserPhone(p_mi_id, phone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Description: 第三方平台更新密码
	 * @Title: pushUserPass
	 * @param p_mi_id 用户编号
	 * @param password 密码
	 * @return boolean true:更新成功，false：更新失败
	 */
	@RequestMapping(value = "/pushuserpass", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> pushUserPass(String name, String oldPass, String newPass) {
		return personalService.pushUserPass(name, oldPass, newPass);
	}

	/**
	 * @Description: 第三方平台或MI，通过用户身份证号取用户p_mi_id
	 * @Title: getMIUserByUserIdNo
	 * @param id_no 身份证号
	 * @return String p_mi_id
	 */
	@RequestMapping(value = "/getmiuserbyuseridno", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getMIUserByUserIdNo(String id_no) {
		String p_mi_id = "";
		try {
			p_mi_id = personalService.getMIUserByUserIdNo(id_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p_mi_id;
	}

	/**
	 * @Description: 第三方平台或MI，通过用户社保卡号取用户p_mi_id
	 * @Title: getMIUserByUserSSCard
	 * @param sb_no 社保卡号
	 * @return String p_mi_id
	 */
	@RequestMapping(value = "/getmiuserbyusersscard", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getMIUserByUserSSCard(String sb_no) {
		String p_mi_id = "";
		try {
			p_mi_id = personalService.getMIUserByUserSSCard(sb_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p_mi_id;
	}

	/**
	 * @Description: 第三方平台或MI，通过用户编号取用户
	 * @Title: getMIUser
	 * @param p_mi_id 社保卡号
	 * @return String {真实姓名:"", 社保卡号 :"",身份证号 :""}
	 */
	@RequestMapping(value = "/getmiuser", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getMIUser(String p_mi_id) {
		String result = "";
		try {
			result = personalService.getMIUser(p_mi_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Description: 第三方平台登录
	 * @Title: loginOther
	 * @param user_name 用户账户
	 * @param password 用户密码
	 * @return String
	 */
	@RequestMapping(value = "/loginother", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String loginOther(String user_name, String password) {
		String p_mi_id = "";
		try {
			p_mi_id = personalService.loginOther(user_name, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p_mi_id;
	}

	/**
	 * @Description: 判断用户角色，动态返回角色
	 * @Title: getRoleId
	 * @param p_mi_id
	 * @param user_name
	 * @param user_pass
	 * @param phone
	 * @return Integer
	 */
	@RequestMapping(value = "/getroleid", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Integer getRoleId(String id_no, String name, String user_name, String user_pass, String phone) {
		Integer role_id = 0;
		try {
			role_id = personalService.getRoleId(id_no, name, user_name, user_pass, phone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role_id;
	}
	//checkuser
	
	@RequestMapping(value = "/checkuser", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody boolean checkuser(String username, String password) {
		boolean res = false;
		try {
			res = personalService.checkuser(username,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 第三方发送短信找回密码
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/passwordrecovery", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> passwordrecovery(String user_name) {
		return personalService.passwordrecovery(user_name) ;
	}
	
	@RequestMapping(value = "/callbackpassword", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, String> callbackpassword(String name, String password, String onlyCode, String verify) {
		return personalService.callbackpassword(name, password, onlyCode, verify) ;
	}
	
}
