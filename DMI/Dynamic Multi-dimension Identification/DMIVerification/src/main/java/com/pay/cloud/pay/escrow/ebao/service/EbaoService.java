package com.pay.cloud.pay.escrow.ebao.service;

import java.util.Map;

import com.pay.cloud.pay.escrow.ebao.response.SocialCardInfo;

/**
 * 社保123服务接口
 * @author yanjie.ji
 * @date 2016年12月6日 
 * @time 下午1:47:54
 */
public interface EbaoService {
	/**
	 * 查询社保卡信息
	 * @param user_code 注册用户账号
	 * @return
	 * @throws Exception
	 */
	SocialCardInfo querySocialCard(String user_code)throws Exception;

	/**
	 * 卡数据查询
	 * @param fullName 姓名
	 * @param idNumber 身份证号
	 * @return 结果map
	 * @throws Exception
	 */
	Map<String, Object> cardDataQuery(String fullName, String idNumber) throws Exception;
}
