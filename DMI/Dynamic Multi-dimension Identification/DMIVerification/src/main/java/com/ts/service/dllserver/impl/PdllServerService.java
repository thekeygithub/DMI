package com.ts.service.dllserver.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportPAY;
import com.ts.entity.P_dll_server;
import com.ts.entity.Page;
import com.ts.service.dllserver.PdllServerManager;
import com.ts.util.PageData;


/**
 * dll配置服务
 * @author fus
 *
 */
@Service("dllServerService")
public class PdllServerService implements PdllServerManager {
	@Resource(name = "daoSupportPAY")
	private DaoSupportPAY dao;

	@SuppressWarnings("unchecked")
	public List<P_dll_server> listAllByHospId(String hospId) throws Exception {
		Map<String ,String> params = new HashMap<String,String>();
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("ipConfig.properties");
		Properties pes = new Properties();
		pes.load(inStream);
		String localIp  = pes.getProperty("LOCAL_IP");
		params.put("hospId", hospId);
		params.put("localIp", localIp);
		return (List<P_dll_server>)dao.findForList("PdllserverMapper.listAll", params);
	}
	
	/**
	 * 列表查询
	 */
	@Override
	public List<PageData> list(Page page) throws Exception {
		return (List<PageData>)dao.findForList("PdllserverMapper.datalistPage", page);
	}
	
	/**
	 * 增加
	 */
	@Override
	public void save(P_dll_server dllServer) throws Exception {
		dao.save("PdllserverMapper.save", dllServer);
	}
	/**
	 * 删除
	 */
	@Override
	public void delete(PageData pd) throws Exception {
		dao.delete("PdllserverMapper.delete", pd);
	}
	/**
	 * 编辑更新
	 */
	@Override
	public void edit(P_dll_server dllServer) throws Exception {
		dao.update("PdllserverMapper.edit", dllServer);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("PdllserverMapper.findById", pd);
	}
	
	/**
	 * 批量删除
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		dao.delete("PdllserverMapper.deleteAllD", ArrayDATA_IDS);
	}
	
	
	
}
