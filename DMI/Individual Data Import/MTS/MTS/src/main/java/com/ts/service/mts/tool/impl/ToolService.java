package com.ts.service.mts.tool.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupportMts;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsToolT;
import com.ts.entity.mts.MtsToolkitT;
import com.ts.service.mts.tool.ToolManger;
import com.ts.util.PageData;

/**
 * 工具处理实现
 * @ClassName:ToolService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年12月4日下午1:50:52
 */
@Service("ToolService")
public class ToolService implements ToolManger {
	

	@Resource(name = "daoSupportMts")
	private DaoSupportMts dao;
	
	
	/**
	 * 查询程序的工具包信息
	 */
	@SuppressWarnings("unchecked")	
	public List<MtsToolkitT> findToolList(MtsToolkitT mtsToolT) throws Exception {
	
		return (List<MtsToolkitT>) dao.findForList("MtsToolkitMapper.findToolList", mtsToolT);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> toolkitListPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsToolkitMapper.toolkitlistPage", page);
		return list;
	}

	@Override
	public void addToolkit(MtsToolkitT mtsToolkitT) throws Exception {
		dao.save("MtsToolkitMapper.addToolkit", mtsToolkitT);
	}

	@Override
	public void editToolkit(MtsToolkitT mtsToolkitT) throws Exception {
		dao.update("MtsToolkitMapper.editToolkit", mtsToolkitT);
	}

	@Override
	public void deleteToolkit(MtsToolkitT mtsToolkitT) throws Exception {
		dao.delete("MtsToolkitMapper.deleteToolkit", mtsToolkitT);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MtsToolkitT> findToolkit(MtsToolkitT mtsToolkitT) throws Exception {
		return (List<MtsToolkitT>) dao.findForList("MtsToolkitMapper.findToolkit", mtsToolkitT);
	}

	@Override
	public List<MtsToolT> findTools(PageData mtsToolkitT) throws Exception {
		return (List<MtsToolT>) dao.findForList("MtsToolkitMapper.findTools", mtsToolkitT);
	}

	@Override
	public void addToolrel(MtsToolT mtsToolT) throws Exception {
		dao.save("MtsToolkitMapper.addToolrel", mtsToolT);
		
	}

	@Override
	public void editToolrel(MtsToolT mtsToolT) throws Exception {
		dao.update("MtsToolkitMapper.editToolrel", mtsToolT);
		
	}

	@Override
	public void deleteToolrel(MtsToolT mtsToolT) throws Exception {
		dao.delete("MtsToolkitMapper.deleteToolrel", mtsToolT);
	}
	
	@Override
	public List<MtsToolT> findToolrel(MtsToolT mtsToolT) throws Exception {
		return (List<MtsToolT>) dao.findForList("MtsToolkitMapper.findToolrel", mtsToolT);
	}

	@Override
	public List<PageData> toolListPage(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("MtsToolkitMapper.toollistPage", page);
		return list;
	}

	@Override
	public List<MtsToolT> toolList(PageData pd) throws Exception {
		return (List<MtsToolT>) dao.findForList("MtsToolkitMapper.findToolsByPd", pd);
	}

	@Override
	public void addTool(MtsToolT mtsToolT) throws Exception {
		dao.save("MtsToolkitMapper.addTool", mtsToolT);
		
	}

	@Override
	public void editTool(MtsToolT mtsToolT) throws Exception {
		dao.update("MtsToolkitMapper.editTool", mtsToolT);
		
	}

	@Override
	public void deleteTool(MtsToolT mtsToolT) throws Exception {
		dao.delete("MtsToolkitMapper.deleteTool", mtsToolT);
		
	}
}
