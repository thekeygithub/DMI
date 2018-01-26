package com.ts.controller.system.mf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ts.annotation.Rights;
import com.ts.controller.base.BaseController;
import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.entity.system.User;
import com.ts.service.system.log.LogManager;
import com.ts.service.system.menu.MenuManager;
import com.ts.service.system.menufun.MenuFunManager;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;
import com.ts.util.Tools;
import com.ts.util.app.AppUtil;
/** 
 * 类名称：MenuFunController 菜单功能管理
 * 创建人：
 * 创建时间：2016年9月22日
 * @version
 */
@Controller
@RequestMapping(value="/mf")
public class MenuFunController extends BaseController {

	@Resource(name="menuService")
	private MenuManager menuService;
	@Resource(name="menuFunService")
	private MenuFunManager menuFunService;
	@Resource(name="logService")
	private LogManager logService;
	/**
	 * 显示菜单列表ztree(菜单管理)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listAllMenuFun")
	@Rights(code="mf/listAllMenuFun")
	public ModelAndView listAllMenu(Model model,String MENU_ID) throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			List<Menu> listMenu = null;
			User user = getCurrentUser();
			//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
			int roleType = Jurisdiction.getUserMaxRoles();
			if(roleType==0){
				//管理角色，显示全部菜单功能
				listMenu = menuService.listAllMenuForFun(Const.TREE_ROOT_ID);
			}else if(roleType==1){
				//授权角色，显示权限内的菜单
				listMenu = menuService.listValidMenuForFun(Const.TREE_ROOT_ID,user.getUSER_ID());
			}else{
				//业务角色直接返回空菜单功能
				listMenu = null;
			}
			
			if(roleType<=1){
				//获取系统菜单树列表
				JSONArray arr = JSONArray.fromObject(listMenu);
				// 设置有效的url，将URL存在的值，替换为Ztree使用的参数
				valideJsonUrl(arr);
				String json = arr.toString();
				json = json.replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "children").replaceAll("hasMenu", "checked");
				model.addAttribute("zTreeNodes", json);
			}
			mv.addObject("MENU_ID",MENU_ID);
			mv.setViewName("system/mf/menu_ztree");
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
			if(myobj.get("subMenu") instanceof JSONArray && ((JSONArray)myobj.get("subMenu")).size()>0){
				valideJsonUrl((JSONArray)myobj.get("subMenu"));
			}else{
				if(myobj.containsKey("MENU_URL") && !Tools.isEmpty((String)myobj.get("MENU_URL"))){
					myobj.put("url", myobj.get("MENU_URL"));
					myobj.remove("MENU_URL");
				}
			}
		}
	}
	
	/**
	 * 显示菜单的功能列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	@Rights(code="mf/list")
	public ModelAndView list(String MENU_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			if(Tools.isEmpty(MENU_ID)){
				return  null;
			}
			//页面显示的功能列表
			List<MenuFun> menuFunList = menuFunService.listMenuFunByMenuId(MENU_ID);
			//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
			int roleType = Jurisdiction.getUserMaxRoles();
			if(roleType==0){
				//管理角色，显示全部菜单功能
			}else if(roleType==1){
				//授权角色，显示权限内的菜单
				List<MenuFun> validMenuList = menuFunService.listAllMenuFunByUserId(getCurrentUser().getUSER_ID());
				if(CollectionUtils.isEmpty(validMenuList)){
					menuFunList = null;
				}else{
					List<String> validMenuFunCodes = new ArrayList<String>(validMenuList.size());
					for(MenuFun fun:validMenuList){
						validMenuFunCodes.add(fun.getMF_ID());
					}
					//过滤功能列表
					validMenuListByIds(menuFunList,validMenuFunCodes);
				}
			}else{
				//业务角色直接返回空菜单功能
				menuFunList = null;
			}
			mv.addObject("MSG", null == pd.get("MSG")?"list":pd.get("MSG").toString()); //MSG=change 则为编辑或删除后跳转过来的
			mv.addObject("menuFunList", menuFunList);
			mv.addObject("ROLE_TYPE", roleType);
			mv.addObject("pd", menuService.getMenuById(pd));
			mv.addObject("MENU_ID",MENU_ID);
			mv.setViewName("system/mf/mf_list");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 过滤菜单功能列表
	 * @param menuFunList
	 * @param validMenuFunCodes
	 */
	private void validMenuListByIds(List<MenuFun> menuFunList,	List<String> validMenuFunCodes) {
		for(int i=0;i<menuFunList.size();){
			MenuFun fun = menuFunList.get(i);
			if(validMenuFunCodes.contains(fun.getMF_ID())){
				i++;
			}else{
				menuFunList.remove(i);
			}
		}
	}

	/**
	 * 请求新增菜单功能页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	@Rights(code="mf/toAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String MENU_ID = (String) pd.get("MENU_ID");
			pd.put("MENU_ID",MENU_ID);
			mv.addObject("pd", menuService.getMenuById(pd));	
			mv.addObject("MENU_ID", MENU_ID);
			mv.addObject("MSG", "add");							//执行状态 add 为添加
			mv.setViewName("system/mf/mf_edit");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}	
	
	/**
	 * 保存菜单功能信息
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	@Rights(code="mf/add")
	public ModelAndView add(MenuFun menuFun)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"保存菜单的功能");
		ModelAndView mv = this.getModelAndView();
		try{
			menuFun.setMF_ID(get32UUID());
			menuFunService.saveMenuFun(menuFun); //保存菜单
			logService.saveMenuFunLog(menuFun,0,Jurisdiction.getUsername());
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.addObject("msg","failed");
		}
		mv.setViewName("redirect:/mf/list.do?MSG='change'&MENU_ID="+menuFun.getMENU_ID()); //保存成功跳转到列表页面
		
		return mv;
	}
	
	/**
	 * 请求编辑菜单页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toEdit")
	@Rights(code="mf/toEdit")
	public ModelAndView toEdit(String MF_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		
		try{
			PageData pd = menuFunService.getMenuFunById(MF_ID);
			mv.addObject("pd", pd);				//功能信息信息
			mv.addObject("MSG", "edit");
			mv.addObject("MENU_ID",pd.get("MENU_ID"));	//菜单ID
			mv.setViewName("system/mf/mf_edit");

		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 保存编辑
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/edit")
	@Rights(code="mf/edit")
	public ModelAndView edit(MenuFun menuFun)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改菜单的功能");
		ModelAndView mv = this.getModelAndView();
		try{
			menuFunService.edit(menuFun);
			logService.saveMenuFunLog(menuFun,1,Jurisdiction.getUsername());
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.setViewName("redirect:/mf/list.do?MSG='change'&MENU_ID="+menuFun.getMENU_ID()); //保存成功跳转到列表页面
		return mv;
	}
	
	/**
	 * 删除菜单某项功能
	 * @param MF_ID
	 * @param out
	 */
	@RequestMapping(value="/delete")
	@Rights(code="mf/delete",type="ajax",dataType="json")
	@ResponseBody
	public Object delete(@RequestParam String MF_ID)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除菜单权限");
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "";
		try{
			PageData pd = menuFunService.getMenuFunById(MF_ID);
			//删除
			menuFunService.deleteMenuFunById(MF_ID);
			logService.saveMenuFunLog(pd,9,Jurisdiction.getUsername());
			errInfo = "success";
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 判断菜单某项功能是否有角色使用
	 * @param MF_ID
	 * @param out
	 */
	@RequestMapping(value="/checkMenuFunUesd")
	@ResponseBody
	public Object checkMenuFunUesd(@RequestParam String MF_ID)throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = null;
		boolean exsit = menuFunService.checkMenuFunUesd(MF_ID);
		if(exsit){
			errInfo = "存在角色正在使用该功能，仍要删除？";
		}else{
			errInfo = "success";
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	
	}
	
	/**
	 * 判断菜单某项功能是否有角色使用
	 * @param MF_ID
	 * @param out
	 */
	@RequestMapping(value="/checkCode")
	@ResponseBody
	public Object checkCode()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = null;
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			boolean exsit = menuFunService.checkCode(pd);
			if(exsit){
				errInfo = "false";
			}else{
				errInfo = "success";
			}
		} catch (Exception e) {
			errInfo = "false";
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	
	}
//
//	
//	/**
//	 * 显示菜单列表ztree(拓展左侧四级菜单)
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value="/otherlistMenu")
//	public ModelAndView otherlistMenu(Model model,String MENU_ID)throws Exception{
//		ModelAndView mv = this.getModelAndView();
//		try{
//			PageData pd = new PageData();
//			pd.put("MENU_ID", MENU_ID);
//			String MENU_URL = menuService.getMenuById(pd).getString("MENU_URL");
//			if("#".equals(MENU_URL.trim()) || "".equals(MENU_URL.trim()) || null == MENU_URL){
//				MENU_URL = "login_default.do";
//			}
//			String roleRights = Jurisdiction.getSession().getAttribute(Jurisdiction.getUsername() + Const.SESSION_ROLE_RIGHTS).toString();	//获取本角色菜单权限
//			List<Menu> athmenuList = menuService.listAllMenuQx(MENU_ID);					//获取某菜单下所有子菜单
//			athmenuList = this.readMenu(athmenuList, roleRights);							//根据权限分配菜单
//			JSONArray arr = JSONArray.fromObject(athmenuList);
//			String json = arr.toString();
//			json = json.replaceAll("MENU_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked").replaceAll("MENU_URL", "url").replaceAll("#", "");
//			model.addAttribute("zTreeNodes", json);
//			mv.addObject("MENU_URL",MENU_URL);		//本ID菜单链接
//			mv.setViewName("system/menu/menu_ztree_other");
//		} catch(Exception e){
//			logger.error(e.toString(), e);
//		}
//		return mv;
//	}
//	
//	/**根据角色权限获取本权限的菜单列表(递归处理)
//	 * @param menuList：传入的总菜单
//	 * @param roleRights：加密的权限字符串
//	 * @return
//	 */
//	public List<Menu> readMenu(List<Menu> menuList,String roleRights){
//		for(int i=0;i<menuList.size();i++){
//			menuList.get(i).setHasMenu(RightsHelper.testRights(roleRights, menuList.get(i).getMENU_ID()));
//			if(menuList.get(i).isHasMenu() && "1".equals(menuList.get(i).getMENU_STATE())){	//判断是否有此菜单权限并且是否隐藏
//				this.readMenu(menuList.get(i).getSubMenu(), roleRights);					//是：继续排查其子菜单
//			}else{
//				menuList.remove(i);
//				i--;
//			}
//		}
//		return menuList;
//	}
	
}
