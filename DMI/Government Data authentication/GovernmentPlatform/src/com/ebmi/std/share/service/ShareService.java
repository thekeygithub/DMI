/**
 * 
 */
package com.ebmi.std.share.service;

import java.util.List;

import com.ebmi.jms.entity.ViewYyjbxx;

/**
 * 共享信息Service
 * 
 * @author xiangfeng.guan
 */
public interface ShareService {

	/**
	 * 获得医院信息
	 * 
	 * @param yybh 医院编号
	 * @return
	 * @throws Exception
	 */
	ViewYyjbxx getYyjbxx(String yybh) throws Exception;

	/**
	 * 查询医院信息
	 * 
	 * @return
	 * @throws Exception
	 */
	List<ViewYyjbxx> queryYyjbxx() throws Exception;

}
