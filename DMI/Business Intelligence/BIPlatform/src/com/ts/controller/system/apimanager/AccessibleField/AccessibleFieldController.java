package com.ts.controller.system.apimanager.AccessibleField;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.Page;
import com.ts.service.system.apimanager.AccessibleField.AccessibleFieldManager;
import com.ts.service.system.apimanager.Relation.RelationManager;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.app.AppUtil;
import com.ts.util.app.SessionAppMap;

/** 
 * 类名称：api可访问字段管理
 * 创建人：
 * 修改时间：2016年10月17日
 * @version
 */
@Controller
@RequestMapping(value="/api")
public class AccessibleFieldController extends BaseController {
	
	@Resource(name="accessibleFieldService")
	private AccessibleFieldManager accessibleFieldService;
	@Resource(name="relationService")
	private RelationManager relationService;
	
	/**显示接口列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/access")
	@Rights(code="api/access")
	public ModelAndView listInterface(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String businessName = pd.getString("businessName");							//检索条件 关键词
			if(null != businessName && !"".equals(businessName)){
				pd.put("businessName", businessName.trim());
			}
			String fieldDisc = pd.getString("fieldDisc");
			if(null != fieldDisc && !"".equals(fieldDisc)){
				pd.put("fieldDisc", fieldDisc.trim());
			}
			page.setPd(pd);
			List<PageData>	accessList = accessibleFieldService.listPdPageAccess(page);		//列出会员列表
			List<PageData>	interfaceList = accessibleFieldService.listPdPageAccess();
			List<PageData> access = new ArrayList<>();
			for(PageData obj : accessList){
				if(obj.getString("isVdr") != null && !"".equals(obj.getString("isVdr"))){
					obj.put("ISVAL", "1");
				}else{
					obj.put("ISVAL", "0");
				}
				access.add(obj);
			}
			mv.setViewName("system/apimanager/accessibleField_list");
			mv.addObject("accessList", access);
			mv.addObject("interfaceList", interfaceList);
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**去新增用户页面
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/goAddA")
	@Rights(code="api/goAddA")
	public ModelAndView goAddA() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/apimanager/accessibleField_edit");
		
		List<PageData>	interfaceList = accessibleFieldService.listPdPageAccess();
		mv.addObject("interfaceList", interfaceList);
		mv.addObject("msg", "saveA");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**保存用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveA")
	@Rights(code="api/saveA")
	public ModelAndView saveA() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增api管理字段");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("DR_ID", this.get32UUID());	//ID
		accessibleFieldService.saveA(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	/**删除用户
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteA")
	@Rights(code="api/deleteA")
	@ResponseBody
	public Object deleteA() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除字段");
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = null;
		try {
			relationService.deleteDRID(pd);
			String columnName = pd.getString("columnname");
			if(columnName != null && !"".equals(columnName)){
				Set<String> set = SessionAppMap.dataParamRuleMapMap.get(columnName);
				if(set != null){
					set = new HashSet<String>();
					SessionAppMap.dataParamRuleMapMap.put(columnName, set);
				}
			}
			errInfo = "success";
		} catch (Exception e) {
			e.printStackTrace();
			errInfo = "failed";
			// TODO: handle exception
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**修改用户
	 * @param out
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/editA")
	@Rights(code="api/editA")
	public ModelAndView editA(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改字段");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		mv.addObject("msg","success");
		try {
			pd = this.getPageData();
			accessibleFieldService.editA(pd);
		} catch (Exception e) {
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	/**去修改用户页面
	 * @return
	 */
	@RequestMapping(value="/goEditA")
	@Rights(code="api/goEditA")
	public ModelAndView goEditA(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = accessibleFieldService.findById(pd);						//根据ID读取
			mv.setViewName("system/apimanager/accessibleField_edit");
			List<PageData>	interfaceList = accessibleFieldService.listPdPageAccess();
			mv.addObject("interfaceList", interfaceList);
			mv.addObject("msg", "editA");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}						
		return mv;
	}
	/**删除时检查是否使用
	 * @param out
	 * @throws Exception 
	 */
	@RequestMapping(value="/checkAllDRID")
	@ResponseBody
	public Object checkAllDRID()throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		String DR_ID = pd.getString("DR_IDS");
		String[] DR_IDS = DR_ID.split(",");
		// 验证是否存在用户使用该角色
		boolean exsit = relationService.findByAllDRId(DR_IDS);
		String errInfo = null;
		if(exsit){
			errInfo = "存在数据正在被使用，确定是否仍要删除？";
		}else{
			errInfo = "success";
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**批量删除
	 * @return
	 */
	@RequestMapping(value="/deleteAllA")
	@Rights(code="api/deleteAllA")
	@ResponseBody
	public Object deleteAllA() {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除接口");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = null;
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String DR_IDS = pd.getString("DR_IDS");
			if(null != DR_IDS && !"".equals(DR_IDS)){
				String ArrayDR_IDS[] = DR_IDS.split(",");
				accessibleFieldService.deleteAllA(ArrayDR_IDS);
				String columnName = pd.getString("columnname");
				if(columnName != null && !"".equals(columnName)){
					String arr[] = columnName.split(",");
					for (String string : arr) {
						Set<String> set = SessionAppMap.dataParamRuleMapMap.get(string);
						if(set != null){
							set = new HashSet<String>();
							SessionAppMap.dataParamRuleMapMap.put(string, set);
						}
					}
				}
				pd.put("msg", "ok");
				errInfo = "success";
			}else{
				errInfo = "failed";
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			errInfo = "failed";
			logger.error(e.toString(), e);
		} finally {
			errInfo = "failed";
			logAfter(logger);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(pd, map);
	}
	
	/**下拉框选择字段说明
	 * @return
	 */
	@RequestMapping(value="/findBusinessName")
	@Rights(code="api/findBusinessName")
	@ResponseBody
	public Object findBusinessName(){
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "error";
		PageData pd = new PageData();
		pd = this.getPageData();
		String businessName = pd.getString("businessName");							//检索条件 关键词
		pd.put("type", businessName);
		try {
			List<String> interfaceList = accessibleFieldService.findByBusinessName(pd);
			errInfo = "success";
			map.put("interfaceList", interfaceList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("result", errInfo);				//返回结果		
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
	
}
