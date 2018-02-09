package com.models.cloud.pay.escrow.mts.service;

import com.models.cloud.pay.escrow.mts.request.MtsParam;
import com.models.cloud.pay.escrow.mts.response.MtsRespResult;
/**
 * MTS服务接口
 * @author yanjie.ji
 * @date 2016年12月5日 
 * @time 上午11:31:28
 */
public interface MTSService {
	/**
	 * 精确匹配
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public MtsRespResult mts(MtsParam req)throws Exception;

}
