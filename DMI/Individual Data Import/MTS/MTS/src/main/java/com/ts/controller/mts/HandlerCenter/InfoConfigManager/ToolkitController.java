package com.ts.controller.mts.HandlerCenter.InfoConfigManager;


import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.entity.mts.MtsArea;
import com.ts.entity.mts.MtsToolT;
import com.ts.entity.mts.MtsToolkitT;
import com.ts.service.mts.tool.ToolManger;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.RandomUtil;
import com.ts.util.app.AppUtil;

/**
 * 工具包管理
 * @ClassName:ToolkitController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhy
 * @date 2017年12月6日上午9:08:27
 */
@Controller
@RequestMapping(value = "/mtsToolkit")
public class ToolkitController extends BaseController {

	String menuUrl = "mtsToolkit/listToolkit.do"; // 菜单地址(权限用)
	@Resource(name = "ToolService")
	private ToolManger toolService;
	
	
	
	@RequestMapping(value = "/listToolkit")
	public ModelAndView listToolkit(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData> listToolkit = toolService.toolkitListPage(page); // 列出工具包列表
		mv.setViewName("mts/tool/toolkit_list");
		mv.addObject("listToolkit", listToolkit);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 去新增工具包页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddToolkit")
	public ModelAndView goAddToolkit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/tool/toolkit_edit");
		mv.addObject("msg", "saveToolkit");
		return mv;
	}

	/**
	 * 保存工具包
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveToolkit")
	public ModelAndView saveToolkit() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "保存MTS_TOOLKIT");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOLKIT_NAME = pd.getString("TOOLKIT_NAME"); 
		String COMMENTS = pd.getString("COMMENTS"); 
		String newId = RandomUtil.getRandomId();
		
		MtsToolkitT toolkit = new MtsToolkitT();
		toolkit.setTOOLKIT_NAME(TOOLKIT_NAME);
		toolkit.setCOMMENTS(COMMENTS);
		toolkit.setTOOLKIT_ID(newId);
		toolService.addToolkit(toolkit);
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 去修改工具包页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditToolkit")
	public ModelAndView goEditToolkit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOLKIT_ID = pd.getString("TOOLKIT_ID");
		MtsToolkitT toolkit = new MtsToolkitT();
		toolkit.setTOOLKIT_ID(TOOLKIT_ID);
		List<MtsToolkitT> toolkitList = toolService.findToolkit(toolkit);
		if(null != toolkitList && toolkitList.size() > 0){
			toolkit = toolkitList.get(0);
			pd.put("TOOLKIT_ID", toolkit.getTOOLKIT_ID());
			pd.put("TOOLKIT_NAME", toolkit.getTOOLKIT_NAME());
			pd.put("COMMENTS", toolkit.getCOMMENTS());
		}
		mv.setViewName("mts/tool/toolkit_edit");
		mv.addObject("msg", "editToolkit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 修改工具包
	 */
	@RequestMapping(value = "/editToolkit")
	public ModelAndView editToolkit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String TOOLKIT_ID = pd.getString("TOOLKIT_ID");
		String TOOLKIT_NAME = pd.getString("TOOLKIT_NAME");
		String COMMENTS = pd.getString("COMMENTS");
		MtsToolkitT toolkit = new MtsToolkitT();
		
		toolkit.setTOOLKIT_ID(TOOLKIT_ID);
		toolkit.setTOOLKIT_NAME(TOOLKIT_NAME);
		toolkit.setCOMMENTS(COMMENTS);
		toolService.editToolkit(toolkit);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除工具包
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delToolkit")
	public void delToolkit(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除MTS_TOOLKIT");
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOLKIT_ID = pd.getString("TOOLKIT_ID");
		MtsToolkitT toolkit = new MtsToolkitT();
		toolkit.setTOOLKIT_ID(TOOLKIT_ID);
		toolService.deleteToolkit(toolkit);
		out.write("success");
		out.close();
	}
	/**
	 * 验证工具包
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyMtsToolkit", method = RequestMethod.POST)
	public Map<String,String> verifyMtsToolkit(String TOOLKIT_ID,String TOOLKIT_NAME,String OPERATE) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();  
		String result = "false";
		MtsToolkitT toolkit = null;
		MtsToolkitT toolkitNew = null;
		List<MtsToolkitT> toolkitList = null;
		if("Add".equals(OPERATE)){
			toolkit = new MtsToolkitT();
			if(!"".equals(TOOLKIT_NAME) && null != TOOLKIT_NAME){
				toolkit.setTOOLKIT_NAME(TOOLKIT_NAME);
			}
			toolkitList = toolService.findToolkit(toolkit);
			if(toolkitList != null && toolkitList.size() > 0){
				result = "false";
			}else{
				result = "success";
			}
		}else if("Edit".equals(OPERATE)){
			toolkit = new MtsToolkitT();
			if(!"".equals(TOOLKIT_NAME) && null != TOOLKIT_NAME){
				toolkit.setTOOLKIT_NAME(TOOLKIT_NAME);
			}
			toolkitList = toolService.findToolkit(toolkit);
			if(toolkitList == null){
				result = "false";
			}else{
				int toolkitListSize = toolkitList.size();
				if(toolkitListSize == 0){
					result = "success";
				}else if(toolkitListSize == 1){
					toolkitNew = toolkitList.get(0);
					String temp_id = toolkitNew.getTOOLKIT_ID();
					if(temp_id.equals(TOOLKIT_ID)){
						result = "success";
					}else{
						result = "false";
					}
				}else{
					result = "false";
				}
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}
	/**
	 * 校验能否删除工具包
	 * @return
	 */
	@RequestMapping(value="/checkDelMtsToolkit")
	@ResponseBody
	public Object checkDelMtsToolkit(String TOOLKIT_ID,String TOOLKIT_NAME){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData toolkit = new PageData();
		try{
			if(!"".equals(TOOLKIT_NAME) && null != TOOLKIT_NAME){
				toolkit.put("TOOLKIT_NAME",TOOLKIT_NAME);
			}
			if(!"".equals(TOOLKIT_ID) && null != TOOLKIT_ID){
				toolkit.put("TOOLKIT_ID",TOOLKIT_ID);
			}
			List<MtsToolT> tools=toolService.findTools(toolkit);
			if(tools == null||tools.isEmpty()||tools.size()==0);
			else errInfo="工具包：["+TOOLKIT_NAME+"]已经包含工具信息，不能删除!";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo=e.getMessage();
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
		
	}
	
	/**
	 * 去工具配置页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goConfigTool")
	public ModelAndView goConfigTool() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		List<MtsToolT> toolList = toolService.findTools(pd);
		
		mv.setViewName("mts/tool/toolrel_list");
		mv.addObject("toolList", toolList);
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 去新增工具关联页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddToolrel")
	public ModelAndView goAddToolrel() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("mts/tool/toolrel_edit");
		mv.addObject("msg", "saveToolrel");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存工具包关联的工具信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveToolrel")
	public ModelAndView saveToolrel() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "保存MTS_TOOL_REL");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOLKIT_ID = pd.getString("TOOLKIT_ID"); 
		String TOOL_ID=pd.getString("TOOL_ID");
		String TOOL_ORDER = pd.getString("TOOL_ORDER"); 
		String newId = RandomUtil.getRandomId();
		
		MtsToolT tool = new MtsToolT();
		tool.setTOOLKIT_ID(TOOLKIT_ID);
		tool.setTOOL_ID(TOOL_ID);
		tool.setTOOL_ORDER(TOOL_ORDER);
		tool.setTOOL_REL_ID(newId);
		toolService.addToolrel(tool);
		
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 去修改工具包关联的工具页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditToolrel")
	public ModelAndView goEditToolrel() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOL_REL_ID = pd.getString("TOOL_REL_ID");
		MtsToolT toolrel = new MtsToolT();
		toolrel.setTOOL_REL_ID(TOOL_REL_ID);
		List<MtsToolT> toolrelList = toolService.findToolrel(toolrel);
		if(null != toolrelList && toolrelList.size() > 0){
			toolrel = toolrelList.get(0);
			pd.put("TOOL_REL_ID", toolrel.getTOOL_REL_ID());
			pd.put("TOOL_ORDER", toolrel.getTOOL_ORDER());
			pd.put("TOOL_ID", toolrel.getTOOL_ID());
			pd.put("TOOL_NAME", toolrel.getTOOL_NAME());
			pd.put("TOOL_PATH", toolrel.getTOOL_PATH());
			pd.put("TOOLKIT_ID", toolrel.getTOOLKIT_ID());
			pd.put("TOOLKIT_NAME", toolrel.getTOOLKIT_NAME());
		}
		mv.setViewName("mts/tool/toolrel_edit");
		mv.addObject("msg", "editToolrel");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 修改工具包的工具关联
	 */
	@RequestMapping(value = "/editToolrel")
	public ModelAndView editToolrel() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String TOOL_REL_ID = pd.getString("TOOL_REL_ID");
		String TOOLKIT_ID=pd.getString("TOOLKIT_ID");
		String TOOL_ID=pd.getString("TOOL_ID");
		String TOOL_ORDER = pd.getString("TOOL_ORDER");
		MtsToolT toolrel = new MtsToolT();
		
		toolrel.setTOOL_REL_ID(TOOL_REL_ID);
		toolrel.setTOOLKIT_ID(TOOLKIT_ID);
		toolrel.setTOOL_ID(TOOL_ID);
		toolrel.setTOOL_ORDER(TOOL_ORDER);
		
		toolService.editToolrel(toolrel);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除工具包
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delToolrel")
	public void delToolrel(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除MTS_TOOL_REL");
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOL_REL_ID = pd.getString("TOOL_REL_ID");
		
		MtsToolT toolrel = new MtsToolT();
		toolrel.setTOOL_REL_ID(TOOL_REL_ID);
		toolService.deleteToolrel(toolrel);
		out.write("success");
		out.close();
	}
	
	/**
	 * 选择工具
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/selectTool")
	public ModelAndView selectTool(Page page)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		List<MtsToolT> toolList=toolService.toolList(pd);
		mv.setViewName("mts/tool/toolrel_tool_list");
		mv.addObject("toolList",toolList);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 验证工具包配置工具信息
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyMtsToolrel", method = RequestMethod.POST)
	public Map<String,String> verifyMtsToolrel(String OPERATE,String TOOL_REL_ID,String TOOLKIT_ID,String TOOL_ID,String TOOL_ORDER) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();  
		String result = "false";
		PageData pd=new PageData();
		List<MtsToolT> toolrelList = null;
		if("Add".equals(OPERATE)){
			if(!"".equals(TOOL_ID) && null != TOOL_ID){
				pd.put("TOOL_ID", TOOL_ID);
			}
			
			if(!"".equals(TOOLKIT_ID) && null != TOOLKIT_ID){
				pd.put("TOOLKIT_ID", TOOLKIT_ID);
			}
			//判断工具包下是否已经包含工具
			toolrelList = toolService.findTools(pd);
			if(toolrelList != null && toolrelList.size() > 0){
				result = "工具包已经包含此工具,请选择其他工具!";
			}else{
				//校验顺序是否已经存在
				PageData pd_order=new PageData();
				pd_order.put("TOOLKIT_ID", TOOLKIT_ID);
				pd_order.put("TOOL_ORDER", TOOL_ORDER);
				toolrelList = toolService.findTools(pd_order);
				if(toolrelList != null && toolrelList.size() > 0){
					result = "工具包已经包含此序号,请指定其他序号!";
				}else{
					result = "success";
				}
			}
		}else if("Edit".equals(OPERATE)){
			if(!"".equals(TOOL_ID) && null != TOOL_ID){
				pd.put("TOOL_ID", TOOL_ID);
			}
			if(!"".equals(TOOLKIT_ID) && null != TOOLKIT_ID){
				pd.put("TOOLKIT_ID", TOOLKIT_ID);
			}
			//校验工具信息
			toolrelList = toolService.findTools(pd);
			if(toolrelList == null){
				result = "success";
			}else{
				int toolrelListSize = toolrelList.size();
				if(toolrelListSize == 0){
					result = "success";
				}else if(toolrelListSize == 1){
					MtsToolT tool = toolrelList.get(0);
					String temp_id = tool.getTOOL_REL_ID();
					if(temp_id.equals(TOOL_REL_ID)){
						result = "success";
					}else{
						result = "工具包已经包含此工具,请选择其他工具!";
					}
				}else{
					result = "工具包包含重复的工具,请检查!";
				}
			}
			if("success".equals(result)){
				//校验顺序的信息
				pd=new PageData();
				if(!"".equals(TOOL_ORDER) && null != TOOL_ORDER){
					pd.put("TOOL_ORDER", TOOL_ORDER);
				}
				if(!"".equals(TOOLKIT_ID) && null != TOOLKIT_ID){
					pd.put("TOOLKIT_ID", TOOLKIT_ID);
				}
				//校验工具信息
				toolrelList = toolService.findTools(pd);
				if(toolrelList == null){
					result = "success";
				}else{
					int toolrelListSize = toolrelList.size();
					if(toolrelListSize == 0){
						result = "success";
					}else if(toolrelListSize == 1){
						MtsToolT tool = toolrelList.get(0);
						String temp_id = tool.getTOOL_REL_ID();
						if(temp_id.equals(TOOL_REL_ID)){
							result = "success";
						}else{
							result = "工具包已经包含此序号,请指定其他序号!";
						}
					}else{
						result = "工具包包含重复的序号,请检查!";
					}
				}
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 工具列表信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listTool")
	public ModelAndView listTool(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData> listTool = toolService.toolListPage(page); // 列出工具列表
		mv.setViewName("mts/tool/tool_list");
		mv.addObject("listTool", listTool);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 去新增工具页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAddTool")
	public ModelAndView goAddTool() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("mts/tool/tool_edit");
		mv.addObject("msg", "saveTool");
		return mv;
	}

	/**
	 * 保存工具包
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveTool")
	public ModelAndView saveTool() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "保存MTS_TOOL");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOL_NAME = pd.getString("TOOL_NAME"); 
		String TOOL_PATH = pd.getString("TOOL_PATH"); 
		String COMMENTS = pd.getString("COMMENTS"); 
		String newId = RandomUtil.getRandomId();
		
		MtsToolT tool = new MtsToolT();
		tool.setTOOL_NAME(TOOL_NAME);
		tool.setTOOL_PATH(TOOL_PATH);
		tool.setCOMMENTS(COMMENTS);
		tool.setTOOL_ID(newId);
		toolService.addTool(tool);
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 去修改工具页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEditTool")
	public ModelAndView goEditTool() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOL_ID = pd.getString("TOOL_ID");
		MtsToolT tool = new MtsToolT();
		tool.setTOOL_ID(TOOL_ID);
		List<MtsToolT> toolList = toolService.toolList(pd);
		if(null != toolList && toolList.size() > 0){
			tool = toolList.get(0);
			pd.put("TOOL_ID", tool.getTOOL_ID());
			pd.put("TOOL_NAME", tool.getTOOL_NAME());
			pd.put("TOOL_PATH", tool.getTOOL_PATH());
			pd.put("COMMENTS", tool.getCOMMENTS());
		}
		mv.setViewName("mts/tool/tool_edit");
		mv.addObject("msg", "editTool");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 修改工具
	 */
	@RequestMapping(value = "/editTool")
	public ModelAndView editTool() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String TOOL_ID = pd.getString("TOOL_ID");
		String TOOL_NAME = pd.getString("TOOL_NAME");
		String TOOL_PATH = pd.getString("TOOL_PATH");
		String COMMENTS = pd.getString("COMMENTS");
		MtsToolT tool = new MtsToolT();
		
		tool.setTOOL_ID(TOOL_ID);
		tool.setTOOL_NAME(TOOL_NAME);
		tool.setTOOL_PATH(TOOL_PATH);
		tool.setCOMMENTS(COMMENTS);
		toolService.editTool(tool);
		mv.setViewName("save_result");
		return mv;
	}
	
	/**
	 * 删除工具
	 * 
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value = "/delTool")
	public void delTool(PrintWriter out) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "删除MTS_TOOL");
		PageData pd = new PageData();
		pd = this.getPageData();
		String TOOL_ID = pd.getString("TOOL_ID");
		MtsToolT tool = new MtsToolT();
		tool.setTOOL_ID(TOOL_ID);
		toolService.deleteTool(tool);
		out.write("success");
		out.close();
	}
	/**
	 * 验证工具包
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyMtsTool", method = RequestMethod.POST)
	public Map<String,String> verifyMtsTool(String TOOL_ID,String TOOL_NAME,String OPERATE) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();  
		String result = "false";
		PageData tool = new PageData();
		MtsToolT toolNew = null;
		List<MtsToolT> toolList = null;
		if("Add".equals(OPERATE)){
			if(!"".equals(TOOL_NAME) && null != TOOL_NAME){
				tool.put("TOOL_NAME",TOOL_NAME);
			}
			toolList = toolService.toolList(tool);
			if(toolList != null && toolList.size() > 0){
				result = "false";
			}else{
				result = "success";
			}
		}else if("Edit".equals(OPERATE)){
			if(!"".equals(TOOL_NAME) && null != TOOL_NAME){
				tool.put("TOOL_NAME",TOOL_NAME);
			}
			toolList = toolService.toolList(tool);
			if(toolList == null){
				result = "false";
			}else{
				int toolkitListSize = toolList.size();
				if(toolkitListSize == 0){
					result = "success";
				}else if(toolkitListSize == 1){
					toolNew = toolList.get(0);
					String temp_id = toolNew.getTOOL_ID();
					if(temp_id.equals(TOOL_ID)){
						result = "success";
					}else{
						result = "false";
					}
				}else{
					result = "false";
				}
			}
		}
		resultMap.put("result", result);
		return resultMap;
	}
	/**
	 * 校验能否删除工具
	 * @return
	 */
	@RequestMapping(value="/checkDelMtsTool")
	@ResponseBody
	public Object checkDelMtsTool(String TOOL_ID,String TOOL_NAME){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData toolkit = new PageData();
		try{
			if(!"".equals(TOOL_NAME) && null != TOOL_NAME){
				toolkit.put("TOOL_NAME",TOOL_NAME);
			}
			if(!"".equals(TOOL_ID) && null != TOOL_ID){
				toolkit.put("TOOL_ID",TOOL_ID);
			}
			List<MtsToolT> tools=toolService.findTools(toolkit);
			if(tools == null||tools.isEmpty()||tools.size()==0);
			else errInfo="工具：["+TOOL_NAME+"]应用于工具包，不能删除!";
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo=e.getMessage();
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
		
	}
	
}
