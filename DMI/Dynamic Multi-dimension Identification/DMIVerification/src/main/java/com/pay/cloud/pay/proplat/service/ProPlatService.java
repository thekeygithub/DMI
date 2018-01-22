package com.pay.cloud.pay.proplat.service;

import java.util.List;

import com.pay.cloud.pay.proplat.entity.PpIntfPara;
import com.pay.cloud.pay.proplat.entity.PpIntfProp;

/**
 * 项目于资金平台服务类
 */
public interface ProPlatService {
	
	/**
	 * 查询所有资金平台交互属性
	 * @return
	 * @throws Exception
	 */
	List<PpIntfProp> findPpIntfPropList() throws Exception;
	
	
	/**
	 * 查询所有资金平台属性参数
	 * @param funcId
	 * @return
	 * @throws Exception
	 */
	List<PpIntfPara> findPpIntfParaList() throws Exception;

}
