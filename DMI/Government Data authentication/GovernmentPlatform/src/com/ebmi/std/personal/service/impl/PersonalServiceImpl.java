package com.ebmi.std.personal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.core.utils.JsonStringUtils;
import com.ebmi.std.common.service.impl.TPBaseService;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.personal.dao.PersonalDao;
import com.ebmi.std.personal.service.PersonalService;
import com.gd.platform.client.Client;
import com.gd.platform.client.ResultEntity;

@Service("personalService")
public class PersonalServiceImpl extends TPBaseService implements PersonalService {
	private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);
	@Resource
	private PersonalDao personalDao;
	
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;

	/**
	 * @Description: 注册--社保信息验证（允许第三方平台注册时，要求同时验证社保信息与第三方平台账户是否绑定）
	 * @Title: validateSocialsecurityInfo
	 * @param name
	 * @param id_no
	 * @param sb_no
	 * @param sb_pass
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#validateSocialsecurityInfo(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public ResultEntity validateSocialsecurityInfo(String id_no, String name) throws Exception {
		// //验证通过：返回p_mi_id ，验证不通过：返回空
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("user_name",user_name);
		// params.put("user_pass", user_pass);
		// params.put("name", name);
		// params.put("id_no", id_no);
		// params.put("sb_no", sb_no);
		// params.put("sb_pass", sb_pass);
		// //对接平台 1：经办大厅，2：市民邮箱
		// int thirdType=1;
		// if(thirdType==1){
		//
		// }else if(thirdType==2){
		//
		// }
		// params.put("fun", "validateSocialsecurityInfo");
		// String str = execMiMethod(params,String.class);
		// return str;

//		return personalDao.validateSocialsecurityInfo(name, id_no, sb_no, sb_pass);
		
		// 调用step1验证姓名和身份证
		Map<String, String> step1Map = new HashMap<String, String>();
		step1Map.put("parm0", id_no);
		step1Map.put("parm1", name);
		return Client.callService("hsoft.general.user.regist.step1", JsonStringUtils.objectToJsonString(step1Map));
	}
	

	@Override
	public ResultEntity validateSmsCode(String id_no, String name, String reg_key, String phone) throws Exception {
		// 调用step1验证姓名和身份证
		Map<String, String> step2Map = new HashMap<String, String>();
		step2Map.put("parm0", id_no);
		step2Map.put("parm1", name);
		step2Map.put("parm2", reg_key);
		step2Map.put("parm3", phone);
		return Client.callService("hsoft.general.user.regist.step2", JsonStringUtils.objectToJsonString(step2Map));
	}

	/**
	 * @Description: 第三方平台注册
	 * @Title: register
	 * @param id_no
	 * @param name
	 * @param user_name
	 * @param user_pass
	 * @param phone
	 * @param email
	 * @param qq
	 * @param weixin
	 * @param sms_key
	 * @param sms_code
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#register(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, int)
	 */
	@Override
	public ResultEntity register(String reg_key, String id_no, String name, String user_name, String user_pass, String phone, String email,
			String qq, String weixin, String sms_key, String sms_code) throws Exception {
		// 注册成功：true，注册失败：false
		HashMap<String, Object> step3Map = new HashMap<String, Object>();
		step3Map.put("parm0", reg_key);
		step3Map.put("parm1", name);
		step3Map.put("parm2", user_pass);
		step3Map.put("parm3", user_name);
		step3Map.put("parm4", "2"); // 用户类别 固定值2
		step3Map.put("parm5", phone);
		step3Map.put("parm6", "1"); // 是否通过短信验证 固定值1
		step3Map.put("parm7", email);
		step3Map.put("parm8", qq);
		step3Map.put("parm9", weixin);
		step3Map.put("parm10", "1"); // 是否开通 固定值1
		step3Map.put("parm11", sms_code);
		step3Map.put("parm12", sms_key);
		// 对接平台 1：经办大厅，2：市民邮箱
//		int thirdType = 1;
//		if (thirdType == 1) {
//
//		} else if (thirdType == 2) {
//
//		}
//		params.put("fun", "register");
//		String str = execMiMethod(params, String.class);
//		return Boolean.parseBoolean(str);
		String parmes = JsonStringUtils.objectToJsonString(step3Map);
		return Client.callService("hsoft.general.user.regist.step3",parmes );
	}

	/**
	 * @Description: 第三方平台验证用户名称是否重复
	 * @Title: validateUserName
	 * @param user_name
	 * @param thirdType
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#validateUserName(java.lang.String,
	 *      int)
	 */
	@Override
	public boolean validateUserName(String user_name) throws Exception {
		// 第三方平台验证用户名称,true用户名不存在，false用户名存在
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", user_name);
		// 对接平台 1：经办大厅，2：市民邮箱
		int thirdType = 1;
		if (thirdType == 1) {

		} else if (thirdType == 2) {

		}
		params.put("fun", "validateUserName");
		String str = execMiMethod(params, String.class);
		return Boolean.parseBoolean(str);
	}

	/**
	 * @Description: 第三方平台更新手机号
	 * @Title: pushUserPhone
	 * @param p_mi_id
	 * @param phone
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#pushUserPhone(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean pushUserPhone(String p_mi_id, String phone) throws Exception {
		// 第三方平台更新手机号成功返回true，否则返回false
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p_mi_id", p_mi_id);
		params.put("phone", phone);
		params.put("fun", "pushUserPhone");
		String str = execMiMethod(params, String.class);
		return Boolean.parseBoolean(str);
	}

	/**
	 * @Description: 第三方平台更新密码
	 * @Title: pushUserPass
	 * @param p_mi_id
	 * @param password
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#pushUserPass(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Map<String, String> pushUserPass(String name, String oldPass, String newPass) {
		// 第三方平台更新密码成功返回true，否则返回false
		Map<String, String> params = new HashMap<String, String>();
		params.put("parm0", name);
		params.put("parm1", oldPass);
		params.put("parm2", newPass);
		Map<String, String> result = new HashMap<String, String>() ;
		TheKeyResultEntity res;
		try {
			res = baseApi.getDataInfo(params, BaseEntity.PASS_MODIFY);
			if(res != null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					if(lm!=null && !lm.isEmpty() ){//有元素的时候
						for (Map<String, String> map2 : lm) {
							result.put("code", "0") ;
							result.put("data", map2.get("col0")) ;
							return result ;
						}
					}
				}else{
					result.put("code", "1") ;
					result.put("error", res.getError()) ;
					return result ;
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		result.put("code", "1") ;
		result.put("error","调用第三方接口失败") ;
		return result ;
	}

	/**
	 * @Description: 第三方平台或MI，通过用户身份证号取用户
	 * @Title: getMIUserByUserIdNo
	 * @param id_no
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#getMIUserByUserIdNo(java.lang.String)
	 */
	@Override
	public String getMIUserByUserIdNo(String id_no) throws Exception {
		// //查询成功返回用户编号，其他返回空
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("id_no", id_no);
		// params.put("fun", "getMIUserByUserIdNo");
		// String str = execMiMethod(params,String.class);
		// return str;

		return personalDao.getMIUserByUserIdNo(id_no);
	}

	/**
	 * @Description: 第三方平台或MI，通过用户社保卡号取用户
	 * @Title: getMIUserByUserSSCard
	 * @param sb_no
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#getMIUserByUserSSCard(java.lang.String)
	 */
	@Override
	public String getMIUserByUserSSCard(String sb_no) throws Exception {
		// //查询成功返回用户编号，其他返回空
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("sb_no", sb_no);
		// params.put("fun", "getMIUserByUserSSCard");
		// String str = execMiMethod(params,String.class);
		// return str;

		return personalDao.getMIUserByUserSSCard(sb_no);
	}

	/**
	 * @Description: 第三方平台或MI，通过用户编号取用户
	 * @Title: getMIUser
	 * @param p_mi_id 用户编号
	 * @return String {真实姓名:"", 社保卡号 :"",身份证号 :""}
	 * @throws Exception
	 */
	@Override
	public String getMIUser(String p_mi_id) throws Exception {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("p_mi_id", p_mi_id);
		// params.put("fun", "getMIUser");
		// String str = execMiMethod(params,String.class);
		// return str;

		return personalDao.getMIUser(p_mi_id);
	}

	/**
	 * @Description: 第三方平台登录
	 * @Title: loginOther
	 * @param user_name
	 * @param password
	 * @param thirdType
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#loginOther(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	public String loginOther(String user_name, String password) throws Exception {
		// //登录成功返回用户编号，其他返回空
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("user_name", user_name);
		// params.put("password", password);
		// //对接平台 1：经办大厅，2：市民邮箱
		// int thirdType=1;
		// if(thirdType==1){
		//
		// }else if(thirdType==2){
		//
		// }
		// params.put("fun", "loginOther");
		// String str = execMiMethod(params,String.class);
		// return str;

		// return personalDao.loginOther(user_name, password);
		return "";
	}

	/**
	 * @Description: 判断用户角色，动态返回角色
	 * @Title: getRoleId
	 * @param p_mi_id
	 * @param user_name
	 * @param user_pass
	 * @param phone
	 * @return
	 * @throws Exception
	 * @see com.ebmi.std.personal.service.PersonalService#getRoleId(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Integer getRoleId(String id_no, String name, String user_name, String user_pass, String phone) throws Exception {
		return 2;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean checkuser(String user_name, String password) throws Exception {
//		parm0 String 必须 登录ID 
//		parm1 String 必须 登录密码 
		if(BaseEntity.TESTFLAG){//接口测试标志 false 代表真实调用接口   true代表返回模拟数据 
			return false;
		}
		HashMap<String, Object> step3Map = new HashMap<String, Object>();
		step3Map.put("parm0", user_name);
		step3Map.put("parm1", password);
		String parmes = JsonStringUtils.objectToJsonString(step3Map);
		if (logger.isDebugEnabled()) {
			logger.debug("checkuser 调用的接口方法  参数 argments：" + parmes);
         }	
		ResultEntity res =  Client.callService("hsoft.general.user.check",parmes );
		if(res!=null){
			if (logger.isDebugEnabled()) {
				logger.debug("checkuser hsoft.general.user.check 接口返回>>>>>>>>>>>>>>>>>>>>>>>>>>>>>re.getStatus()>>>>>"+res.getStatus()+">>>>>>>re.getError()>>>>>>"+res.getError());
				logger.debug("Content>>>>>"+res.getContent());
             }
			if(StringUtils.isNotBlank(res.getContent())){
				Map<String, String> m= JsonStringUtils.jsonStringToObject(res.getContent(), Map.class);
				if(m!=null){
					String error = m.get("error");
					if(StringUtils.isNoneBlank(error)){
						return false;
					}
				}
			}
		}
//		ok,
//		{"error":"","data":[{"col0":"true"}]}
        if(res!=null && "ok".equals(res.getStatus())&&StringUtils.isBlank(res.getError())){
        	return true;
        }
		return false;
	}


	@Override
	public Map<String, String> passwordrecovery(String user_name) {
		// TODO Auto-generated method stub
  		HashMap<String, String> map = new HashMap<String, String>();
  		HashMap<String, String> result = new HashMap<String, String>();
		map.put("parm0", user_name) ;
		TheKeyResultEntity res ;
		try {
   			res = baseApi.getDataInfo(map, BaseEntity.SMS_SEND) ;
   			if(res!=null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					if(lm!=null && (!lm.isEmpty()) ){//有元素的时候
						for (Map<String, String> map2 : lm) {
							result.put("code", "0") ;
							result.put("data", map2.get("col1")) ;
							return result ;
						}
					}
				}else{
					result.put("code", "1") ;
					result.put("error", res.getError()) ;
					return result ;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("error", "第三方接口调用失败") ;
			return result ;
		}
		result.put("error", "第三方接口调用失败") ;
		return result ;
	}


	@Override
	public Map<String, String> callbackpassword(String name, String password, String onlyCode, String verify) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("parm0", name) ;
		map.put("parm1", password) ;
		map.put("parm2", onlyCode) ;
		map.put("parm3", verify) ;
		Map<String, String> result = new HashMap<String, String>();
		TheKeyResultEntity res ;
		try {
			res = baseApi.getDataInfo(map, BaseEntity.PASS_GET) ;
			if(res != null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					if(lm!=null && !lm.isEmpty() ){//有元素的时候
						for (Map<String, String> map2 : lm) {
							result.put("code", "0") ;
							result.put("data", map2.get("col0")) ;
							return result ;
						}
					}
				}else{
					result.put("code", "1") ;
					result.put("error", res.getError()) ;
					return result ;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.put("code", "1") ;
		result.put("error", "调用用第三方接口失败") ;
		return result ;
	}
}
