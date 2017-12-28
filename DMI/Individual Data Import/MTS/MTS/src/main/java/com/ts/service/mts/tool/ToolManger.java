package com.ts.service.mts.tool;

import java.util.List;

import com.ts.entity.Page;
import com.ts.entity.mts.MtsToolT;
import com.ts.entity.mts.MtsToolkitT;
import com.ts.util.PageData;

/**
 * 工具相关的处理
 * @ClassName:ToolManger
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年12月4日下午1:49:34
 */
public interface ToolManger {
	
	public List<MtsToolkitT> findToolList(MtsToolkitT mtsToolkitT) throws Exception ;
	
	public List<PageData> toolkitListPage(Page page) throws Exception;

	public void addToolkit(MtsToolkitT mtsToolkitT) throws Exception;

	public void editToolkit(MtsToolkitT mtsToolkitT) throws Exception;

	public void deleteToolkit(MtsToolkitT mtsToolkitT) throws Exception;
	
	public List<MtsToolkitT> findToolkit(MtsToolkitT mtsToolkitT) throws Exception ;
	
	public List<MtsToolT> findTools(PageData mtsToolkitT) throws Exception;
	
	public void addToolrel(MtsToolT mtsToolT) throws Exception;

	public void editToolrel(MtsToolT mtsToolT) throws Exception;

	public void deleteToolrel(MtsToolT mtsToolT) throws Exception;
	
	public List<MtsToolT> findToolrel(MtsToolT mtsToolT) throws Exception;
	
	public List<PageData> toolListPage(Page page) throws Exception;
	
	public List<MtsToolT> toolList(PageData pd)throws Exception;
	
	public void addTool(MtsToolT mtsToolT) throws Exception;

	public void editTool(MtsToolT mtsToolT) throws Exception;

	public void deleteTool(MtsToolT mtsToolT) throws Exception;
	
	
	
	
}
