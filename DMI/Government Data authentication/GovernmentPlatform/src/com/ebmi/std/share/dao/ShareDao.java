/**
 * 
 */
package com.ebmi.std.share.dao;

import java.util.List;

import com.ebmi.jms.entity.ViewYyjbxx;

/**
 * @author xiangfeng.guan
 */
public interface ShareDao {

	List<ViewYyjbxx> queryYyjbxx() throws Exception;

	ViewYyjbxx getYyjbxx(String yybh) throws Exception;

}
