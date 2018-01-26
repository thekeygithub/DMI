package com.ts.controller.system.apimanager.rolemanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.service.system.apimanager.AccessibleField.AccessibleFieldManager;
import com.ts.service.system.apimanager.Relation.RelationManager;
import com.ts.service.system.role.RoleManager;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;

/** 
 * 类名称：RoleController 角色权限管理
 * 创建人：
 * 修改时间：2015年11月6日
 * @version
 */
@Controller
@RequestMapping(value="/api")
public class RoleManagerController extends BaseController {
	
	@Resource(name="relationService")
	private RelationManager relationService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="accessibleFieldService")
	private AccessibleFieldManager accessibleFieldService;
	
	/**
	 * 显示角色列表ztree(角色管理)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/rolemanager")
	@Rights(code="api/rolemanager")
	public ModelAndView listAllRole(Model model,String ROLE_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			if(Tools.isEmpty(ROLE_ID)){
				ROLE_ID = Const.TREE_ROOT_ID;
			}
			// 当前登录用户
			User user = getCurrentUser();
			//显示登陆用户可以使用的角色树
			List<Role> list = new ArrayList<Role>();
			Role role= new Role();
			role.setROLE_NAME("角色");
			role.setROLE_ID(Const.TREE_ROOT_ID);
			role.setNoCheck(true);
			role.setTarget("treeFrame");
			role.setROLE_URL("api/roleList.do?ROLE_ID=0");
			role.setSubRole(roleService.listUserAllAccessRole(role.getROLE_ID(),user,1));
			role.setOpen(true);
			list.add(role);
			JSONArray arr = JSONArray.fromObject(list);
			// 设置有效的url，将URL存在的值，替换为Ztree使用的参数
			valideJsonUrl(arr);
			String json = arr.toString();
			json = json.replaceAll("noCheck", "nocheck").replaceAll("ROLE_NAME", "name").replaceAll("subRole", "children").replaceAll("hasRole", "checked").replaceAll("chk", "chkDisabled");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("ROLE_ID",ROLE_ID);
			mv.setViewName("system/apimanager/rolemanager/roleManager_ztree");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 设置有效的url
	 * @param arr
	 */
	private void valideJsonUrl(JSONArray arr){
		if(arr == null){
			return ;
		}
		for(Object obj:arr){
			JSONObject myobj = (JSONObject) obj;
			if(myobj.containsKey("ROLE_URL") && !Tools.isEmpty((String)myobj.get("ROLE_URL"))){
				myobj.put("url", myobj.get("ROLE_URL"));
				myobj.remove("ROLE_URL");
			}
			if(myobj.get("subRole") instanceof JSONArray && ((JSONArray)myobj.get("subRole")).size()>0){
				valideJsonUrl((JSONArray)myobj.get("subRole"));
			}
			
		}
	}
	/**
	 * 显示管理字段列表
	 * @return
	 */
	@RequestMapping(value="/roleList")
	@Rights(code="api/roleList")
	public ModelAndView roleList(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			mv.setViewName("system/apimanager/rolemanager/roleManager_list");
			mv.addObject("ROLE_ID", pd.getString("ROLE_ID"));
			String init = pd.getString("init");
			if("0".equals(pd.getString("ROLE_ID"))){
				return mv;
			}
			if(init != null && "0".equals(init)){
				return mv;
			}
			String businessName = pd.getString("businessName");	//检索条件 关键词
			if(null != businessName && !"".equals(businessName)){
				pd.put("businessName", businessName.trim());
			}
			String fieldDisc = pd.getString("fieldDisc");
			if(null != fieldDisc && !"".equals(fieldDisc)){
				pd.put("fieldDisc", fieldDisc.trim());
			}
			
			List<PageData> roleYP = accessibleFieldService.listYPAccess(pd);
			List<PageData>	interfaceList = accessibleFieldService.listPdPageAccess();
			List<PageData> zTree = accessibleFieldService.listZtreeAccess(pd);
//			List<String> ids = relationService.listAllIds(pd);
			Map<String, List<PageData> > map = new HashMap<String, List<PageData>>();
			for(PageData  p:roleYP ){
				if(p.getString("isVdr") != null && !"".equals(p.getString("isVdr"))){
					p.put("isVal", "1");
				}else{
					p.put("isVal", "0");
				}
				if(p.getString("isVCol") != null && !"".equals(p.getString("isVCol"))){
					p.put("isCol", "1");
				}else{
					p.put("isCol", "0");
				}
				if(p.getString("isVflag") != null && !"".equals(p.getString("isVflag"))){
					p.put("flag", "1");
				}else{
					p.put("flag", "0");
				}
				if(map.containsKey( p.getString("IN_ID"))){
					List<PageData>  lis = map.get( p.getString("IN_ID"));
					lis.add(p);
				}else{
					List<PageData>  lis = new ArrayList<PageData>();
					lis.add(p);
					map.put(p.getString("IN_ID"), lis);
				}
			}
			mv.addObject("zTree", zTree);
			mv.addObject("resultMap", map);
			mv.addObject("interfaceList", interfaceList);
			mv.addObject("pd", pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mv;
	}	 
	/**保存用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	@Rights(code="api/saveDelete")
	@ResponseBody
	public Map<String, String> save(){
		logBefore(logger, Jurisdiction.getUsername()+"新增管理字段");
		Map<String,String>	pu = new HashMap<String, String>();
		boolean temp = false;
		PageData pd = new PageData();
		pd = this.getPageData();
		try {			
				PageData pds =  relationService.findById(pd);
				if("".equals(pds) || pds == null){
					PageData pda = accessibleFieldService.findById(pd);
					pd.put("TR_ID", this.get32UUID());	//ID
					pd.put("IN_ID", pda.getString("IN_ID"));
					pd.put("COL_RULE","");
					temp = relationService.save(pd);
					if(temp){
						pu.put("result","添加成功！");
					}else{
						pu.put("result","添加失败！");
					}
				}else{
					pu.put("result","添加失败！");
				}
		} catch (Exception e) {
			pu.put("result","添加失败！");
		}
		return pu;
	}	
	/**删除管理字段
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@Rights(code="api/saveDelete")
	@ResponseBody
	public Map<String, String> delete() {
		logBefore(logger, Jurisdiction.getUsername()+"删除管理字段");
		Map<String,String>	pu = new HashMap<String, String>();
		boolean temp = false;
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			PageData pda = relationService.findById(pd);
			pd.put("TR_ID", pda.getString("TR_ID"));	//ID
			temp = relationService.delete(pd);
			if(temp){
				pu.put("result","删除成功！");
			}else{
				pu.put("result","删除失败！");
			}
		} catch (Exception e) {
			pu.put("result","删除失败！");
		}
		return pu;
	}
	
	/**批量新增
	 * @return
	 */
	@RequestMapping(value="/addAllR")
	@Rights(code="api/addDeleteAllR")
	@ResponseBody
	public Map<String, String> addAllR() {
		logBefore(logger, Jurisdiction.getUsername()+"批量插入接口");
		Map<String,String>	pu = new HashMap<String, String>();
		boolean temp = false;
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String role = pd.getString("ROLE_ID");
			String[] dr =  pd.get("DR_ID").toString().split(",");
			Collection<String> list = java.util.Arrays.asList(dr);
			List<PageData> pdl = new ArrayList<PageData>();
			for(Object st:list){
				pd = new PageData();
				pd.put("DR_ID", st);
				pd.put("ROLE_ID", role);
				PageData pds =  relationService.findById(pd);
				if("".equals(pds) || pds == null){
					PageData pda = accessibleFieldService.findById(pd);
					pd.put("TR_ID", this.get32UUID());	//ID
					pd.put("ROLE_ID", role);
					pd.put("IN_ID", pda.getString("IN_ID"));
					pd.put("COL_RULE","");
					pdl.add(pd);
				}
			}
			temp = relationService.saveAll(pdl);
			if(temp){
				pu.put("result","批量添加成功！");
			}else{
				pu.put("result","批量添加失败！");
			}
		} catch (Exception e) {
			pu.put("result","批量添加失败！");
			logger.error(e.toString(), e);
		} 
		return pu;
	}
	/**批量删除
	 * @return
	 */
	@RequestMapping(value="/deleteAllR")
	@Rights(code="api/addDeleteAllR")
	@ResponseBody
	public Map<String, String> deleteAllR() {
		logBefore(logger, Jurisdiction.getUsername()+"批量删除接口");
//		ModelAndView mv = this.getModelAndView();
		Map<String,String>	pu = new HashMap<String, String>();
		boolean temp = false;
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			String role = pd.getString("ROLE_ID");
			String[] dr =  pd.get("DR_ID").toString().split(",");
			Collection<String> list = java.util.Arrays.asList(dr);
			List<PageData> ids = new ArrayList<PageData>();
			for(Object st:list){
				pd.put("ROLE_ID", role);
				pd.put("DR_ID", st);
				PageData pda = relationService.findById(pd);
				PageData pds = new PageData();
				pds.put("TR_ID", pda.getString("TR_ID"));	//ID
				ids.add(pds);
			}
			temp = relationService.deleteID(ids);
			if(temp){
				pu.put("result","批量删除成功！");
			}else{
				pu.put("result","批量删除失败！");
			}
//			mv.addObject("msg","success");
//			mv.setViewName("save_result");
		} catch (Exception e) {
			pu.put("result","批量删除失败！");
			logger.error(e.toString(), e);
		} 
		return pu;
	}
}