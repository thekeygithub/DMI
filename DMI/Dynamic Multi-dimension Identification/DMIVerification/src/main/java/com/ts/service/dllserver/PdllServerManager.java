package com.ts.service.dllserver;

import java.util.List;

import com.ts.entity.P_dll_server;
import com.ts.entity.Page;
import com.ts.util.PageData;

/**
 * DLL服务配置接口
 * @author fus
 *
 */
public interface PdllServerManager {
	/**列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**新增
	 * @param P_dll_server
	 * @throws Exception
	 */
	public void save(P_dll_server dllServer)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(P_dll_server dllServer)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**
	 * 根据医院标号id查询所有所有医院的DLL地址
	 * @param hospId
	 * @return List<P_dll_server>
	 * @throws Exception
	 */
	public List<P_dll_server> listAllByHospId(String hospId)throws Exception;
	
}
