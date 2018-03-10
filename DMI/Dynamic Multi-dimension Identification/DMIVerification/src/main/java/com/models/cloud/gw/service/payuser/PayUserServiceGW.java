package com.models.cloud.gw.service.payuser;

import java.util.Map;

/**
 * C端用户相关业务
 * Created by yacheng.ji on 2016/4/7.
 */
public interface PayUserServiceGW {

	/**
	 * 创建用户支付通行证(Redis)
	 * @param accountId
	 * @param hardwareId
	 * @return
	 * @throws Exception
	 */
	String setPayTokenInRedis(Long accountId, String hardwareId) throws Exception;

	/**
	 * 获取支付通行证(Redis)
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	Map<String, String> getPayTokenInRedis(String accountId) throws Exception;

	/**
	 * 验证用户易保支付密码
	 * @param inputMap
	 * @return
	 * @throws Exception
     */
	Map<String, Object> verifyUserPaymentPassword(Map<String, Object> inputMap) throws Exception;

	/**
	 * 判断银行卡类型
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> bankCardNoType(Map<String, Object> inputMap) throws Exception;

	/**
	 * 
	 * @Description: 登录
	 * @Title: login  
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> login(Map<String, Object> receiveMap) throws Exception;

	/**
	 * 统一登录
	 * @param receiveMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> unifiedLogin(Map<String, Object> receiveMap) throws Exception;
	/**
	 *
	 * @Description: H5自动登录
	 * @Title: getLoginToken
	 * @param inputMap 参数集合
	 * @return 结果Map
	 * @throws Exception
	 */
	Map<String, Object> getLoginToken(Map<String, Object> inputMap) throws Exception;
	/**
	 * 统一token登录接口
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> loginForToken(Map<String, Object> inputMap) throws Exception;
	/**
	 * 
	 * @Description: 获取支持银行卡列表
	 * @Title: queryBankCardList  
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> queryBankCardList(Map<String, Object> receiveMap) throws Exception;

	/**
	 * 
	 * @Description: 获取配置文件
	 * @Title: queryConfiguration  
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> queryConfiguration(Map<String, Object> receiveMap) throws Exception;

	/**
	 *
	 * @Description: 检测用户是否注册
	 * @Title: checkUserCode
	 * @param inputMap 参数集合
	 * @return 结果Map
	 * @throws Exception
	 */
	Map<String, Object> checkUserCode(Map<String, Object> inputMap) throws Exception;

	/**
	 * 
	 * @Description: 注册
	 * @Title: register 
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> register(Map<String, Object> receiveMap) throws Exception;
	Map<String, Object> register2(Map<String, Object> receiveMap) throws Exception;
	/**
	 *
	 * @Description: 同步用户
	 * @Title: syncUser
	 * @param inputMap 参数集合
	 * @return 结果Map
	 * @throws Exception
	 */
	Map<String, Object> syncUser(Map<String, Object> inputMap) throws Exception;

	/**
	 * 
	 * @Description: 重置登录密码
	 * @Title: restLoginPass 
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> restLoginPass(Map<String, Object> receiveMap) throws Exception;
	/**
	 * 统一登录修改登录密码
	 * @param receiveMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> unifiedResetPassword(Map<String, Object> receiveMap) throws Exception;
	
	/**
	 * 
	 * @Description: 设置支付密码
	 * @Title: setPaymentPass 
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> setPaymentPass(Map<String, Object> receiveMap) throws Exception;

	/**
	 * 
	 * @Description: 设置支付密码
	 * @Title: setPaymentPass2 
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> setPaymentPass2(Map<String, Object> receiveMap) throws Exception;

	/**
	 * 
	 * @Description: 设置支付密码
	 * @Title: setPaymentPass3 
	 * @param receiveMap 参数集合
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> setPaymentPass3(Map<String, Object> receiveMap) throws Exception;
	
	/**
	 * 
	 * @Description: 登出系统
	 * @Title: loginOut 
	 * @param receiveMap
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> loginOut(Map<String, Object> receiveMap) throws Exception;
	
	/**
	 * 
	 * @Description: 检查是否设置支付密码
	 * @Title: loginOut 
	 * @param receiveMap
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> checkPayPwd(Long accountId);
	
	/**
	 * 实名认证
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> realNameGW(Map<String, Object> receiveMap);
	/**
	 * 导入实名认证
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> realNameImportGW(Map<String, Object> receiveMap);
	/**
	 * 通过实名认证修改支付密码
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> resetPayPasswordForRealNameGW(Map<String, Object> receiveMap);
	/**
	 * 修改登录账号
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> changeUserCodeGW(Map<String, Object> receiveMap);
	
	/**
	 * 获取登录验证码
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> verifyCodeListGW(Map<String, Object> receiveMap);

	Map<String, Object> faceRecognition(Map<String, Object> inputMap) throws Exception;

	Map<String, Object> realPersonVerify(Map<String, Object> inputMap) throws Exception;
}
