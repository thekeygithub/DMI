package com.models.cloud.pay.payuser.service;

import java.util.List;
import java.util.Map;

import com.models.cloud.pay.escrow.mi.pangu.response.PersonInfoRes;
import com.models.cloud.pay.payuser.entity.ActPFin;
import com.models.cloud.pay.payuser.entity.ActPerson;
import com.models.cloud.pay.payuser.entity.DimBank;
import com.models.cloud.pay.payuser.entity.LogPayUserLogin;
import com.models.cloud.pay.payuser.entity.PayUser;
import com.models.cloud.pay.payuser.entity.PayUserDevice;
import com.models.cloud.pay.supplier.entity.ActSp;

public interface PayUserService {

	/**
	 * 验证C端用户合法性
	 * @param accountId
	 * @param hardwareId
	 * @return
	 * @throws Exception
     */
	Map<String, Object> checkPayUserValidity(String accountId, String hardwareId, boolean isCheckRealName) throws Exception;

	/**
	 * 
	 * @Description: 通过手机号查询用户
	 * @Title: findByPayUserPhone 
	 * @param phone 手机号
	 * @return PayUser
	 */
	public PayUser findByPayUserPhone(String phone) throws Exception;

	/**
	 * 
	 * @Description: 通过用户编号查询用户
	 * @Title: findByPayUserId
	 * @param userId 用户编号
	 * @return PayUser
	 */
	public PayUser findByPayUserId(String userId) throws Exception;
	
	/**
	 * 
	 * @Description: 通过用户账户编号查询用户
	 * @Title: findByPayUserActId 
	 * @param actId 用户账户编号
	 * @return PayUser
	 * @throws Exception 
	 */
	public PayUser findByPayUserActId(String actId) throws Exception;
	
	/**
	 * 
	 * @Description:保存用户
	 * @Title: savePayUser 
	 * @param payUser 用户
	 * @return int
	 * @throws Exception 
	 */
	public int savePayUser(PayUser payUser) throws Exception;

	/**
	 * 
	 * @Description: 记录密码错误次数
	 * @Title: updatePassErrorCount 
	 * @param payUserId
	 * @param num 密码错误次数
	 * @return int
	 * @throws Exception
	 */
	int updatePassErrorCount(Long payUserId, int num) throws Exception;

	/**
	 * 
	 * @Description: 记录登录日记
	 * @Title: saveLoginLog 
	 * @param loginResFlag 操作结果状态，1成功0失败
	 * @param loginTypeId 1：登录，2：退出
	 * @param message 登录结果信息
	 * @param receiveMap
	 * @param user
	 * @return int
	 * @throws Exception
	 */
	int saveLoginLog(int loginResFlag,int loginTypeId, String message, Map<String, Object> receiveMap, PayUser user) throws Exception;

	/**
	 * 
	 * @Description:设置登录密码
	 * @Title: restLoginPass 
	 * @param phone 手机号
	 * @param password 密码
	 * @return int
	 * @throws Exception 
	 */
	int restLoginPass(String phone, String password) throws Exception;

	/**
	 * 
	 * @Description: 更新支付密码
	 * @Title: updatePaymentPass 
	 * @param actId
	 * @param password
	 * @return int
	 * @throws Exception 
	 */
	int updatePaymentPass(Long actId, String password) throws Exception;

	/**
	 * 
	 * @Description: 新加账户
	 * @Title: saveActPerson 
	 * @param actPerson
	 * @return int
	 * @throws Exception 
	 */
	int saveActPerson(ActPerson actPerson) throws Exception;
	
	/**
	 * 
	 * @Description: 获取支持的银行卡列表
	 * @Title: queryBankCardList 
	 * @param dimBank
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	Map<String, Object> queryBankCardList(DimBank dimBank) throws Exception;

	/**
	 * 
	 * @Description: 生成账户平台私人账户资金属性
	 * @Title: saveActPFin 
	 * @param actPFin
	 * @return int
	 * @throws Exception
	 */
	int saveActPFin(ActPFin actPFin) throws Exception;

	/**
	 * 
	 * @Description: 更新最后登录时间
	 * @Title: updateLastLoginTime 
	 * @param payUser
	 * @return int
	 * @throws Exception
	 */
	int updateLastLoginTime(PayUser payUser) throws Exception;
	
	/**
	 * 修改登录账号
	 * @param payUser
	 * @return
	 * @throws Exception
	 */
	int updateUserCode(PayUser payUser) throws Exception;

	/**
	 * 
	 * @Description: 登出系统
	 * @Title: loginOut 
	 * @param payUserId
	 * @return int 
	 * @throws Exception
	 */
	int loginOut(Long payUserId) throws Exception;

	/**
	 * 
	 * @Description: 通过账户编号获取账户
	 * @Title: findByActId 
	 * @param actId
	 * @return ActPerson
	 * @throws Exception 
	 */
	ActPerson findActPersonById(Long actId) throws Exception;

	/**
	 * 
	 * @Description: 保存终端设备标识
	 * @Title: savePayUserDevice 
	 * @param payUserDevice
	 * @return int
	 * @throws Exception
	 */
	int savePayUserDevice(PayUserDevice payUserDevice) throws Exception;
	
	/**
	 * 
	 * @Description: 终端设备标识查重
	 * @Title: findPayUserDevice 
	 * @param payUserDevice
	 * @return PayUserDevice
	 * @throws Exception 
	 */
	PayUserDevice findPayUserDevice(PayUserDevice payUserDevice) throws Exception;

	/**
	 * 
	 * @Description: 获取获取医保项目ID
	 * @Title: findByChannelAppId 
	 * @param appId
	 * @return ActSp
	 * @throws Exception 
	 */
	ActSp findByChannelAppId(String appId) throws Exception;

	/**
	 * 
	 * @Description: 获取某个账户最后一次登录信息
	 * @Title: selectByActIdLastLogin 
	 * @param actId
	 * @return LogPayUserLogin
	 * @throws Exception 
	 */
	LogPayUserLogin selectByActIdLastLogin(Long actId) throws Exception;

	/**
	 * 
	 * @Description: 终端设备标识更新
	 * @Title: findPayUserDevice 
	 * @param payUserDevice
	 * @return int
	 * @throws Exception 
	 */
	int updatePayUserDevice(PayUserDevice payUserDevice) throws Exception;

	/**
	 *
	 * @Description: 获取支持的银行卡列表
	 * @Title: queryDimBankList
	 * @param dimBank
	 * @return List<DimBank>
	 * @throws Exception
	 */
	List<DimBank> queryDimBankList(DimBank dimBank) throws Exception;
	
	
	/**
	 * 实名认证
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> realName(Map<String, Object> receiveMap);
	/**
	 * s实名认证导入
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> realNameImport(Map<String, Object> receiveMap);
	/**
	 * 通过实名认证修改支付密码
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> resetPayPasswordForRealName(Map<String, Object> receiveMap);
	
	/**
	 * 修改登录账号
	 * @param receiveMap
	 * @return
	 */
	Map<String,Object> changeUserCode(Map<String, Object> receiveMap);
	/**
	 * token登录
	 * @param inputMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loginForToken(Map<String, Object> inputMap) throws Exception;
	
	/**
	 * 统一登录
	 * @param receiveMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> unifiedLogin(Map<String, Object> receiveMap) throws Exception;
	
	/**
	 * 统一注册
	 * @param receiveMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> register2(Map<String, Object> receiveMap) throws Exception;

	int updateActPersonSiCardNo(ActPerson actPerson) throws Exception;

	PersonInfoRes getMiPersonalInfo(Long personalActId, String pSiCardNo, String fixedHospCode) throws Exception;

	void saveActPCertRec(String appId, Long userActId, String userName, String idNumber, Short certResFlag, String errInfo, boolean isSaveUcPCertRec) throws Exception;
}
