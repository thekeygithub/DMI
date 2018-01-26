package com.ts.controller.system.role;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.system.Menu;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.service.system.log.LogManager;
import com.ts.service.system.menufun.MenuFunManager;
import com.ts.service.system.role.RoleManager;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
/** 
 * 类名称：RoleController 角色权限管理
 * 创建人：
 * 修改时间：2015年11月6日
 * @version
 */
@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {
	
	@Resource(name="menuFunService")
	private MenuFunManager menuFunService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="logService")
	private LogManager logService;
	
	/**
	 * 显示角色列表ztree(角色管理)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listAllRole")
	@Rights(code="role/listAllRole")
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
			role.setROLE_URL("role/list.do?ROLE_ID=0");
			role.setSubRole(roleService.listUserAllValideRole(role.getROLE_ID(),user,1));
			role.setOpen(true);
			list.add(role);
			JSONArray arr = JSONArray.fromObject(list);
			// 设置有效的url，将URL存在的值，替换为Ztree使用的参数
			valideJsonUrl(arr);
			String json = arr.toString();
			json = json.replaceAll("noCheck", "nocheck").replaceAll("ROLE_NAME", "name").replaceAll("subRole", "children").replaceAll("hasRole", "checked").replaceAll("chk", "chkDisabled");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("ROLE_ID",ROLE_ID);
			mv.setViewName("system/role/role_ztree");
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
	
	/** 进入角色列表
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@Rights(code="role/list")
	public ModelAndView list()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			String ROLE_ID  = pd.getString("ROLE_ID");
			if(Tools.isEmpty(ROLE_ID )){
				return null;
			}
			User user = getCurrentUser();
			List<Role> roleList = roleService.listSubRoleByParentId(ROLE_ID);
			//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
			int roleType = Jurisdiction.getUserMaxRoles();
			if(roleType==0){
				//管理角色，显示全部菜单功能
			}else if(roleType==1){
				//授权角色，显示用户所有角色及角色下属角色
				//获取当前登陆用户可以使用的角色ID集合
				List<String> validRoleIds = roleService.listSubRoleByUsId(user.getUSER_ID(),true);
				//根据可用的角色ID集合过滤页面要显示的角色列表
				setValideRoleList(roleList,validRoleIds);
				if(validRoleIds.contains(ROLE_ID)){
					mv.addObject("HASROLE","1");	//新增按钮权限
				}
			}else{
				//业务角色直接返回空
				roleList = null;
			}
			mv.addObject("roleList",roleList);
			mv.addObject("MSG", null == pd.get("MSG")?"list":pd.get("MSG").toString()); //MSG=change 则为编辑或删除后跳转过来的
			mv.addObject("pd" ,roleService.getRoleById(pd));
			mv.addObject("ROLE_TYPE", roleType);
			mv.addObject("ROLE_ID", ROLE_ID);
			mv.setViewName("system/role/role_list");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 根据可用的角色ID集合过滤页面要显示的角色列表
	 * @param roleList
	 * @param valideIds
	 */
	private void setValideRoleList(List<Role> roleList, List<String> validRoleIds) {
		if(!CollectionUtils.isEmpty(roleList)){
			if(CollectionUtils.isEmpty(validRoleIds)){
				//如果可以的角色ID集合为空，则返回空列表
				roleList = null;
				return;
			}
			for(int i=0;i<roleList.size();){
				Role role = roleList.get(i);
				if(!validRoleIds.contains(role.getROLE_ID())){
					roleList.remove(i);
					continue;
				}
				i++;
			}
		}
	}

	/**去新增页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	@Rights(code="role/toAdd")
	public ModelAndView toAdd(){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
			if(null == pd.get("ROLE_ID")|| "".equals(pd.get("ROLE_ID").toString())){
				return null;
			}
			String ROLE_ID = pd.get("ROLE_ID").toString();//接收传过来的上级菜单ID,如果上级为顶级就取值“0”
			pd.put("ROLE_ID",ROLE_ID);
			mv.addObject("ROLE_ID", ROLE_ID);
			mv.addObject("MSG", "add");
			mv.addObject("pds", roleService.getRoleById(pd));	//传入父菜单所有信息
			int roleType = Jurisdiction.getUserMaxRoles();
			mv.addObject("ROLE_TYPE", roleType);
			mv.setViewName("system/role/role_edit");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存新增角色
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@Rights(code="role/add")
	public ModelAndView add(Role role,HttpServletResponse response,HttpServletRequest request )throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增角色");
		ModelAndView mv = this.getModelAndView();
		try{
			//验证角色类型是否超出权限
			int roleType = Jurisdiction.getUserMaxRoles();
			if(role.getROLE_TYPE()!=null && role.getROLE_TYPE()<roleType){
				// 跳转到超出权限页面
				response.sendRedirect(request.getContextPath() + Const.NO_RIGHTS);
				return null;
			}
			role.setROLE_ID(get32UUID()); //主键
			roleService.saveRole(role);
			logService.saveRoleLog(role,0,Jurisdiction.getUsername());
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.addObject("msg","failed");
		}
		mv.setViewName("redirect:/role/list.do?MSG=change&ROLE_ID="+role.getPARENT_ID()); //保存成功跳转到列表页面
		return mv;
	}
	
	/**请求编辑
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	@Rights(code="role/toEdit")
	public ModelAndView toEdit( String ROLE_ID )throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd.put("ROLE_ID",ROLE_ID);
			pd = roleService.getRoleById(pd);
			int roleType = Jurisdiction.getUserMaxRoles();
			mv.addObject("ROLE_TYPE", roleType);
			mv.addObject("pd", pd);
			mv.addObject("ROLE_ID", ROLE_ID);
			mv.addObject("MSG", "edit");
			pd.put("ROLE_ID",pd.get("PARENT_ID").toString());
			mv.addObject("pds", roleService.getRoleById(pd));	//传入父菜单所有信息
			pd.put("ROLE_ID",ROLE_ID);// 复原roleID
			mv.setViewName("system/role/role_edit");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存修改
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@Rights(code="role/edit")
	public ModelAndView edit(Role role,HttpServletResponse response,HttpServletRequest request)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改角色");
		ModelAndView mv = this.getModelAndView();
		try{
			//验证角色类型是否超出权限
			int roleType = Jurisdiction.getUserMaxRoles();
			if(role.getROLE_TYPE()!=null && role.getROLE_TYPE()<roleType){
				// 跳转到超出权限页面
				response.sendRedirect(request.getContextPath() + Const.NO_RIGHTS);
				return null;
			}
			roleService.edit(role);
			logService.saveRoleLog(role,1,Jurisdiction.getUsername());
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.setViewName("redirect:/role/list.do?MSG=change&ROLE_ID="+role.getPARENT_ID()); //保存成功跳转到列表页面
		return mv;
	}
	
	/**删除角色
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@Rights(code="role/delete",type="ajax",dataType="json")
	@ResponseBody
	public Object deleteRole(@RequestParam String ROLE_ID)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除角色");
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		String errInfo = "";
		try{
			pd.put("ROLE_ID", ROLE_ID);
			Role role = roleService.findRoleById(ROLE_ID);
			roleService.deleteRoleById(ROLE_ID);	//删除角色、级联删除角色权限关系
			logService.saveRoleLog(role,9,Jurisdiction.getUsername());
			errInfo = "success";
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**验证用户正在使用该角色
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkRoleUsed")
	@ResponseBody
	public Object checkRoleUsed(@RequestParam String ROLE_ID)throws Exception{
		// 验证是否存在用户使用该角色
		boolean exsit = roleService.checkRoleUsed(ROLE_ID);
		String errInfo = null;
		if(exsit){
			errInfo = "存在用户正在使用该角色，仍要删除？";
		}else{
			errInfo = "success";
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 显示菜单列表ztree(菜单功能授权菜单)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/menuFun")
	@Rights(code="role/menuFun")
	public ModelAndView menuFun(Model model,String ROLE_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			User user = getCurrentUser();
			//获取菜单和菜单功能列表
			List<Menu> menuList = menuFunService.listAllRoleMenuFun(Const.TREE_ROOT_ID,user,ROLE_ID);	
			
			JSONArray arr = JSONArray.fromObject(menuList);
			String json = arr.toString();
			json = json.replaceAll("MENU_ID", "id").replaceAll("noCheck", "nocheck").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "children").replaceAll("hasMenu", "checked");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("ROLE_ID",ROLE_ID);
			mv.addObject("functionFlag",Const.MENU_FUN_FLAG);
			mv.setViewName("system/role/menuFun");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存角色菜单权限
	 * @param ROLE_ID 角色ID
	 * @param menuIds 功能权限ID集合
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/saveMenuFun")
	@Rights(code="role/saveMenuFun")
	public void saveMenuFun(@RequestParam String ROLE_ID,@RequestParam String menuFunIds,PrintWriter out)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改角色的功能权限");
		try{
			if(null != menuFunIds && !"".equals(menuFunIds.trim())){
				String[] ids = Tools.str2StrArray(menuFunIds);
				ArrayList<PageData> pds = new ArrayList<PageData>();
				for(String id:ids){
					PageData mypd = new PageData();
					mypd.put("ID", get32UUID());
					mypd.put("ROLE_ID", ROLE_ID);
					mypd.put("MF_ID", id);
					pds.add(mypd);
				}
				//更新当前角色菜单权限
				roleService.updateRoleMF(ROLE_ID,pds);
			}else{
				//删除角色的所有权限(没有任何勾选)
				roleService.deleteRoleAllRights(ROLE_ID);
			}
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	
}