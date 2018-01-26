package com.ts.controller.system.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

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
import com.ts.util.UuidUtil;
import com.ts.util.app.AppUtil;
/** 
 * 类名称：MenuController 菜单处理
 * 创建人：
 * 创建时间：2015年10月27日
 * @version
 */
@Controller
@RequestMapping(value="/menu")
public class MenuController extends BaseController {

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
	@RequestMapping(value="/listAllMenu")
	@Rights(code="menu/listAllMenu")
	public ModelAndView listAllMenu(Model model,String MENU_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			if(Tools.isEmpty(MENU_ID)){
				MENU_ID = Const.TREE_ROOT_ID;
			}
			List<Menu> listMenu = null;
			User user = getCurrentUser();
			//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
			int roleType = Jurisdiction.getUserMaxRoles();
			if(roleType==0){
				//管理角色，显示全部菜单功能
				listMenu = menuService.listAllMenu(Const.TREE_ROOT_ID);
			}else if(roleType==1){
				//授权角色，显示权限内的菜单
				listMenu = menuService.listValidMenu(Const.TREE_ROOT_ID,user.getUSER_ID());
			}else{
				//业务角色直接返回空菜单功能
				listMenu = null;
			}
			
			//外加一层根节点
			List<Menu> list = new ArrayList<Menu>();
			Menu menu = new Menu();
			menu.setMENU_NAME("菜单");
			menu.setMENU_ID(Const.TREE_ROOT_ID);
			menu.setNoCheck(true);
			menu.setTarget("treeFrame");
			if(roleType==0){
				menu.setMENU_URL("menu/list.do?MENU_ID=0");
			}
			menu.setSubMenu(listMenu);
			menu.setOpen(true);
			list.add(menu);
			
			JSONArray arr = JSONArray.fromObject(list);
			String json = arr.toString();
			json = json.replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "children").replaceAll("hasMenu", "checked").replaceAll("MENU_URL", "url");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("MENU_ID",MENU_ID);
			mv.setViewName("system/menu/menu_ztree");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 显示菜单列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	@Rights(code="menu/list")
	public ModelAndView list()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try{
			String MENU_ID = pd.get("MENU_ID")==null?null:pd.get("MENU_ID").toString();
			int roleType = Jurisdiction.getUserMaxRoles();
			if(Tools.isEmpty(MENU_ID)){
				if(roleType==0){//管理角色默认查询根目录
					MENU_ID = Const.TREE_ROOT_ID;
				}else{
					return null;
				}
			}
			PageData pds = menuService.getMenuById(pd);
			//页面显示的菜单列表
			List<Menu> menuList = menuService.listSubMenuByParentId(MENU_ID,true);
			
			//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
			if(roleType==0){
				//管理角色，显示全部菜单功能
			}else if(roleType==1){
				//授权角色，显示权限内的菜单
				List<MenuFun> validMenuList = menuFunService.listAllMenuFunByUserId(getCurrentUser().getUSER_ID());
				if(CollectionUtils.isEmpty(validMenuList)){
					menuList = null;
				}else{
					List<String> validMenuFunCodes = Jurisdiction.getMfList(validMenuList);
					//过滤功能列表
					validMenuListByIds(menuList,validMenuFunCodes);
					if(pds!=null){
						String url = pds.get("MENU_URL")==null?null:(String)pds.get("MENU_URL");
						if(Jurisdiction.checkRights(url,validMenuFunCodes)){
							mv.addObject("HASMENU","1");	//新增按钮权限
						}
					}
				}
			}else{
				//业务角色直接返回空菜单功能
				menuList = null;
			}
			mv.addObject("pd", pds);	//传入父菜单所有信息
			mv.addObject("MENU_ID", MENU_ID);
			mv.addObject("MSG", null == pd.get("MSG")?"list":pd.get("MSG").toString()); //MSG=change 则为编辑或删除后跳转过来的
			mv.addObject("menuList", menuList);
			mv.addObject("ROLE_TYPE", roleType);
			mv.setViewName("system/menu/menu_list");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	//过滤掉无权限的菜单
	private void validMenuListByIds(List<Menu> menuList,List<String> validMenuFunCodes) {
		for(int i=0;i<menuList.size();){
			Menu menu = menuList.get(i);
			if(Jurisdiction.checkRights(menu.getMENU_URL(),validMenuFunCodes)){
				i++;
			}else{
				menuList.remove(i);
			}
		}
	}

	/**
	 * 请求新增菜单页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	@Rights(code="menu/toAdd")
	public ModelAndView toAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			String MENU_ID = pd.getString("MENU_ID");
			pd.put("MENU_ID",MENU_ID);
			mv.addObject("pds", menuService.getMenuById(pd));	//传入父菜单所有信息
			mv.addObject("MENU_ID", MENU_ID);					//传入菜单ID，作为子菜单的父菜单ID用
			mv.addObject("MSG", "add");							//执行状态 add 为添加
			mv.setViewName("system/menu/menu_edit");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}	
	
	/**
	 * 保存菜单信息
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	@Rights(code="menu/add")
	public ModelAndView add(Menu menu)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"保存菜单");
		ModelAndView mv = this.getModelAndView();
		try{
			menu.setMENU_ID(UuidUtil.get32UUID());
			menu.setMENU_ICON("menu-icon fa fa-leaf black");//默认菜单图标
			menuService.saveMenu(menu); //保存菜单
			//重新加载菜单
			menuService.reloadMenu(menu.getPARENT_ID());
			
			logService.saveMenuLog(menu,0,Jurisdiction.getUsername());
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.addObject("msg","failed");
		}
		mv.setViewName("redirect:/menu/list.do?MSG=change&MENU_ID="+menu.getPARENT_ID()); //保存成功跳转到列表页面
		return mv;
	}
	
	/**
	 * 删除菜单
	 * @param MENU_ID
	 * @param out
	 */
	@RequestMapping(value="/delete")
	@Rights(code="menu/delete",type="ajax",dataType="json")
	@ResponseBody
	public Object delete(@RequestParam String MENU_ID)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除菜单");
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "";
		try{
			PageData pd = new PageData();
			pd = this.getPageData();
			PageData menu = menuService.getMenuById(pd);
			menuService.deleteMenuById(MENU_ID);
			errInfo = "success";
			//重新加载菜单
			menuService.reloadMenu(menu.getString("PARENT_ID"));
			logService.saveMenuLog(menu,9,Jurisdiction.getUsername());
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	

	/**
	 * 检测菜单的功能是否被某角色使用
	 * @param MENU_ID
	 * @param out
	 */
	@RequestMapping(value="/checkMenuUsed")
	@ResponseBody
	public Object checkMenuUsed(@RequestParam String MENU_ID)throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		StringBuffer errInfo = new StringBuffer();
		try{
			 List<Menu> menus = menuService.listSubMenuByParentId(MENU_ID,true);
			if(menus!=null&&menus.size() > 0){//判断是否有子菜单，是：不允许删除
				errInfo.append("当前菜单存在子菜单");
			}
			if(menuService.checkMenuUsed(MENU_ID)){
				if(errInfo.length()!=0){
					errInfo.append("，且");
				}
				errInfo.append("菜单的功能被某角色使用中");
			}
			if(errInfo.length()==0){
				errInfo = errInfo.append("success");
			}else{
				errInfo.append("，确认删除！");
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo.toString());
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 请求编辑菜单页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toEdit")
	@Rights(code="menu/toEdit")
	public ModelAndView toEdit(String id)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd.put("MENU_ID",id);				//接收过来的要修改的ID
			pd = menuService.getMenuById(pd);	//读取此ID的菜单数据
			mv.addObject("pd", pd);				//放入视图容器
			pd.put("MENU_ID",pd.get("PARENT_ID").toString());			//用作读取父菜单信息
			mv.addObject("pds", menuService.getMenuById(pd));			//传入父菜单所有信息
			mv.addObject("MENU_ID", pd.get("PARENT_ID").toString());	//传入父菜单ID，作为子菜单的父菜单ID用
			mv.addObject("MSG", "edit");
			pd.put("MENU_ID",id);			//复原本菜单ID
			mv.setViewName("system/menu/menu_edit");
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
	@Rights(code="menu/edit")
	public ModelAndView edit(Menu menu)throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改菜单");
		ModelAndView mv = this.getModelAndView();
		try{
			menuService.edit(menu);
			//重新加载菜单
			menuService.reloadMenu(menu.getPARENT_ID());
			logService.saveMenuLog(menu,1,Jurisdiction.getUsername());
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.setViewName("redirect:/menu/list.do?MSG=change&MENU_ID="+menu.getPARENT_ID()); //保存成功跳转到列表页面
		return mv;
	}
	
	/**
	 * 请求编辑菜单图标页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toEditicon")
	@Rights(code="menu/toEditicon")
	public ModelAndView toEditicon(String MENU_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd.put("MENU_ID",MENU_ID);
			mv.addObject("pd", pd);
			mv.setViewName("system/menu/menu_icon");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**
	 * 保存菜单图标 
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/editicon")
	@Rights(code="menu/editicon")
	public ModelAndView editicon()throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改菜单图标");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			pd = menuService.editicon(pd);
			mv.addObject("msg","success");
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.addObject("msg","failed");
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	
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
//			if(menuList.get(i).isHasMenu() && 1==menuList.get(i).getMENU_STATUS() ){	//判断是否有此菜单权限并且是否隐藏
//				this.readMenu(menuList.get(i).getSubMenu(), roleRights);					//是：继续排查其子菜单
//			}else{
//				menuList.remove(i);
//				i--;
//			}
//		}
//		return menuList;
//	}
}
