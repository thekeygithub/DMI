package com.ts.service.system.menufun.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupport;
import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.service.system.menu.MenuManager;
import com.ts.service.system.menufun.MenuFunManager;
import com.ts.service.system.role.impl.RoleService;
import com.ts.util.Const;
import com.ts.util.PageData;

/** 
 * 类名称：MenuService 菜单处理
 * 创建人：
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("menuFunService")
public class MenuFunService implements MenuFunManager{
	
	@Resource(name="menuService")
	private MenuManager menuService;
	
	@Resource(name="roleService")
	private RoleService roleService;

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 通过菜单ID获取菜单功能列表
	 * @param menu_id
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<MenuFun> listMenuFunByMenuId(String menu_id) throws Exception {
		return (List<MenuFun>) dao.findForList("MenuFunMapper.listMenuFunByMenuId", menu_id);
	}

	/**
	 * 保存菜单
	 * @param menuFun
	 * @throws Exception
	 */
	public void saveMenuFun(MenuFun menuFun) throws Exception{
		dao.save("MenuFunMapper.insertMenuFun", menuFun);
	}

	/**
	 * 删除菜单功能
	 * @param mf_id
	 * @throws Exception
	 */
	public void deleteMenuFunById(String mf_id) throws Exception {
		dao.save("MenuFunMapper.deleteRoleMenuFunRelaById", mf_id);
		dao.save("MenuFunMapper.deleteMenuFunById", mf_id);
	}
	
	/**
	 * 获取菜单功能
	 * @param mf_id
	 * @throws Exception
	 */
	public PageData getMenuFunById(String mf_id) throws Exception {
		return (PageData) dao.findForObject("MenuFunMapper.getMenuFunById", mf_id);
	}
	
	/**
	 * 更新编辑菜单功能
	 * @param menuFun
	 * @throws Exception
	 */
	public void edit(MenuFun menuFun) throws Exception {
		 dao.update("MenuFunMapper.updateMenuFun", menuFun);
	}
	
	/**
	 * 判断菜单某项功能是否有角色使用
	 * @param mF_ID
	 * @return
	 * @throws Exception
	 */
	public boolean checkMenuFunUesd(String MF_ID) throws Exception{
		Integer count = (Integer) dao.findForObject("MenuFunMapper.checkMenuFunUesd", MF_ID);
		if(count!=null && count >=1){
			return true;
		}
		return false;
	}

	/**
	 * 检查功能编码
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean checkCode(PageData pd) throws Exception{
		Integer count = (Integer) dao.findForObject("MenuFunMapper.checkCode", pd);
		if(count!=null && count >=1){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 获取所有菜单功能列表
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MenuFun> listAllMenuFun() throws Exception {
		return (List<MenuFun>) dao.findForList("MenuFunMapper.listAllMenuFun", null);
	}
	
	/**
	 *	 通过用户ID获取用户拥有的所有功能列表（包括用户角色下属角色拥有的所有功能）
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	public List<MenuFun> listAllMenuFunByUserId(String USER_ID) throws Exception {
		//查询出当前用户的所有角色，包括下级角色
		List<String> role_ids = roleService.listSubRoleByUsId(USER_ID,true);
		
		return listAllMenuFunByRoleIds(role_ids);
	}
	
	/**
	 * 通过角色ID获取当前角色及其下属角色的功能列表ID集合
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MenuFun> listAllMenuFunByRoleIds(List<String> ROLE_IDs) throws Exception {
		return (List<MenuFun>) dao.findForList("MenuFunMapper.listAllMenuFunByRoleIds", ROLE_IDs);
	}
	
	/**
	 * 查询当前角色已经拥有的功能权限ID集合
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> listMenuFunByRoleId(String ROLE_ID) throws Exception {
		return (List<String>) dao.findForList("MenuFunMapper.listMenuFunByRoleId", ROLE_ID);
	}
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(系统菜单及功能列表)(递归处理)
	 * @param string
	 * @param user
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listAllRoleMenuFun(String MENU_ID, User user ,String ROLE_ID) throws Exception {
		//存放页面展现的菜单功能列表树
		List<MenuFun> menuFunList = null;
		//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
		int roleType = roleService.getUserMaxRolesByUsId(user.getUSER_ID());
	
		if(roleType==0){
			//管理角色，显示全部菜单功能
			menuFunList = this.listAllMenuFun();
		}else if(roleType==1){
			//授权角色，显示权限内的菜单功能
			//包括用户所有角色及角色下属角色拥有的功能权限
			menuFunList = this.listAllMenuFunByUserId(user.getUSER_ID());
		}else{
			//业务角色直接返回空菜单功能
			 return Collections.EMPTY_LIST;
		}
		if(CollectionUtils.isEmpty(menuFunList)){
			return Collections.EMPTY_LIST;
		}
		//按照菜单id进行功能的分组
		Map<String,List<MenuFun>> map = new HashMap<String,List<MenuFun>>(menuFunList.size());
		for(MenuFun mf:menuFunList){
			if(map.containsKey(mf.getMENU_ID())){
				List<MenuFun> list = map.get(mf.getMENU_ID());
				list.add(mf);
			}else{
				List<MenuFun> list = new ArrayList<MenuFun>();
				list.add(mf);
				map.put(mf.getMENU_ID(), list);
			}
		}
		
		//查询出当前角色已经拥有的功能权限ID集合
		List<String> hasMenuFunSet = this.listMenuFunByRoleId(ROLE_ID);
		//组合菜单 和 菜单功能，形成新的功能菜单树形结构
		List<Menu> menuList = listAllRoleMenuFun2(MENU_ID,map,hasMenuFunSet);

		return menuList;
	}
	
	/**
	 * 组合菜单 和 菜单功能，形成新的功能菜单树形结构
	 * @param MENU_ID
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private List<Menu> listAllRoleMenuFun2(String MENU_ID,Map<String,List<MenuFun>> map,List<String> hasMenuFunSet) throws Exception {
		//所有的菜单树形结构
		List<Menu> menuList = menuService.listSubMenuByParentId(MENU_ID,true);
		if(menuList==null){return menuList;}
		for(int i=0 ; i<menuList.size(); ){
			Menu menu = menuList.get(i);
			List<Menu> subMenuList = this.listAllRoleMenuFun2(menu.getMENU_ID(),map,hasMenuFunSet);
			menu.setSubMenu(subMenuList);
			menu.setTarget("treeFrame");//此处用于区分菜单与功能，不可动
//			menu.setNoCheck(true);
			//最终叶子节点，增加下属功能权限显示
			if(CollectionUtils.isEmpty(subMenuList)){
				//获当前菜单的功能权限列表
				List<MenuFun> list = map.get(menu.getMENU_ID());
				if(CollectionUtils.isEmpty(list)){
					//当前叶子节点未设置功能权限，则去除当前叶子节点
					menuList.remove(i);
					continue;
				}
				for(MenuFun mf:list){
					Menu newMenu = new Menu();
					newMenu.setMENU_NAME(mf.getFUN_CODE()+" "+mf.getINTRODUCTION());//功能CODE+功能描述
					newMenu.setMENU_ID(mf.getMF_ID());
					newMenu.setTarget(Const.MENU_FUN_FLAG);//此处用于区分菜单与功能，不可动
					//如果当前功能 用户已经拥有，则设置选中
					if(hasMenuFunSet.contains(newMenu.getMENU_ID())){
						newMenu.setHasMenu(true);//在页面中默认显示被选中
					}
					subMenuList.add(newMenu);
				}
			}
			i++;
		}
		return menuList;
	}

}
