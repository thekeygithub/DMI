package com.ts.service.system.menu.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupport;
import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.entity.system.User;
import com.ts.service.system.menu.MenuManager;
import com.ts.service.system.menufun.MenuFunManager;
import com.ts.util.Const;
import com.ts.util.Jurisdiction;
import com.ts.util.PageData;

/** 
 * 类名称：MenuService 菜单处理
 * 创建人：xingsilong
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("menuService")
public class MenuService implements MenuManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="menuFunService")
	private MenuFunManager menuFunService;
	
	/**
	 * 通过ID获取其子一级菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listSubMenuByParentId(String parentId,boolean cacheFlag) throws Exception {
		//缓存菜单
		List<Menu> list = null;
		if(cacheFlag){
			list = (List<Menu>) Const.SYS_MENU_MAP.get(Const.SYS_MENU_FATHER_ID+parentId);
		}
		if(CollectionUtils.isEmpty(list)){
			list = (List<Menu>) dao.findForList("MenuMapper.listSubMenuByParentId", parentId);
			Const.SYS_MENU_MAP.put(Const.SYS_MENU_FATHER_ID+parentId,list);
		}
		//返回给页面副本,防止勿改
		List<Menu> result = null;
		if(list != null){
			result = new ArrayList<Menu>(list.size());
			Iterator<Menu> iterator = list.iterator(); 
			while(iterator.hasNext()){ 
				result.add( iterator.next().clone()); 
			}
		}
		return result;
	}
	
	/**
	 * 通过菜单ID获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getMenuById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("MenuMapper.getMenuById", pd);
	}
	
	/**
	 * 新增菜单
	 * @param menu
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception {
		dao.save("MenuMapper.insertMenu", menu);
	}
	
	/**
	 * 删除菜单,删除子菜单，级联删除菜单功能，级联删除角色功能关系
	 * @param MENU_ID
	 * @throws Exception
	 */
	public void deleteMenuById(String MENU_ID) throws Exception {
		//删除菜单及所有下级菜单
		delAllMenuSubById(MENU_ID);
	}
	/**
	 * 删除菜单和菜单的所有下级菜单
	 * modify by xsl 2017
	 * @param MENU_ID
	 * @throws Exception 
	 */
	private void delAllMenuSubById(String MENU_ID) throws Exception{
		//查询出当前菜单的所有下级菜单
		List<Menu> menu_list = this.listSubMenuByParentId(MENU_ID,false);
		if(!CollectionUtils.isEmpty(menu_list)){
			for(Menu menu:menu_list){
				delAllMenuSubById(menu.getMENU_ID());
			}
		}
		PageData pd = new PageData();
		pd.put("MENU_ID", MENU_ID);
		dao.delete("MenuMapper.deleteRoleMfById", pd);//删除角色功能关系
		dao.delete("MenuMapper.deleteMenuFunById", pd);//删除菜单功能
		dao.delete("MenuMapper.deleteMenuById", pd);//删除菜单
	}
	
	/**
	 * 编辑
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public void edit(Menu menu) throws Exception {
		 dao.update("MenuMapper.updateMenu", menu);
	}
	
	/**
	 * 保存菜单图标 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData editicon(PageData pd) throws Exception {
		return (PageData)dao.findForObject("MenuMapper.editicon", pd);
	}
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(菜单管理)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenu(String MENU_ID) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID,true);
		if(!CollectionUtils.isEmpty(menuList)){
			for(Menu menu : menuList){
				menu.setMENU_URL("menu/list.do?MENU_ID="+menu.getMENU_ID());
				menu.setSubMenu(this.listAllMenu(menu.getMENU_ID()));
				menu.setTarget("treeFrame");
			}
		}
		return menuList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Menu> listValidMenu(String MENU_ID, String USER_ID)	throws Exception {
		//查询出用户可用的权限内的所有功能列表
		List<MenuFun> menuList = menuFunService.listAllMenuFunByUserId(USER_ID);
		if(CollectionUtils.isEmpty(menuList)){
			return Collections.EMPTY_LIST;
		}
		List<String> validMenuFunCodes = Jurisdiction.getMfList(menuList);
		return listValidMenu2(MENU_ID, validMenuFunCodes);
	}

	/**
	 * 根据功能过滤菜单列表
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	private List<Menu> listValidMenu2(String MENU_ID,List<String> validMenuFunCodes) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID,true);
		if(!CollectionUtils.isEmpty(menuList)){
			for(int i=0;i<menuList.size();){
				Menu menu = menuList.get(i);
				List<Menu> subList = this.listValidMenu2(menu.getMENU_ID(),validMenuFunCodes);
				menu.setSubMenu(subList);
				menu.setTarget("treeFrame");
				//最基本的叶子节点,权限验证
				if(Jurisdiction.checkRights(menu.getMENU_URL(),validMenuFunCodes)){
					menu.setHasMenu(true);
				}else{
					menu.setHasMenu(false);
					//无权限且子目录为空，删除当前节点
					if(subList==null||subList.size()==0){
						menuList.remove(i);
						continue;
					}
				}
				menu.setMENU_URL("menu/list.do?MENU_ID="+menu.getMENU_ID());
				i++;
			}
		}
		return menuList;
	}
	
	/**
	 * 系统菜单列表，根据权限过滤
	 * @param MENU_ID
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listValidAllMenuQx(String MENU_ID,List<String> validMenuFunCodes) throws Exception {
		//查询出用户可用的权限内的所有功能列表
		return listValidAllMenuQx2(MENU_ID,validMenuFunCodes);
	}
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(系统菜单列表)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listValidAllMenuQx2(String MENU_ID,List<String> validMenuFunCodes) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID,true);
		if(!CollectionUtils.isEmpty(menuList)){
			for(int i=0;i<menuList.size();){
				Menu menu = menuList.get(i);
				List<Menu> subList = this.listValidAllMenuQx2(menu.getMENU_ID(),validMenuFunCodes);
				menu.setSubMenu(subList);
				menu.setTarget("treeFrame");
				menu.setHasMenu(true);
				//最基本的叶子节点,权限验证
				if(Jurisdiction.checkRights(menu.getMENU_URL(),validMenuFunCodes)){
					menu.setHasMenu(true);
				}else{
					//无权限且子目录为空，删除当前节点
					if(subList==null||subList.size()==0){
						menuList.remove(i);
						continue;
					}
				}
				i++;
			}
		}
		return menuList;
	}
	
	/**
	 * 系统菜单列表，所有菜单
	 */
	public List<Menu> listAllMenuQx(String MENU_ID) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID,true);
		if(!CollectionUtils.isEmpty(menuList)){
			for(Menu menu : menuList){
				menu.setSubMenu(this.listAllMenuQx(menu.getMENU_ID()));
				menu.setTarget("treeFrame");
				menu.setHasMenu(true);
			}
		}
		return menuList;
	}
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(菜单功能管理)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuForFun(String MENU_ID) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID,true);
		if(!CollectionUtils.isEmpty(menuList)){
			for(Menu menu : menuList){
				List<Menu> subList = this.listAllMenuForFun(menu.getMENU_ID());
				if(CollectionUtils.isEmpty(subList)){
					//最基本的叶子节点，增加功能设置的链接
					menu.setMENU_URL("mf/list.do?MENU_ID="+menu.getMENU_ID());
				}else{
					menu.setMENU_URL(null);
				}
				menu.setSubMenu(subList);
				menu.setTarget("treeFrame");
			}
		}
		return menuList;
	}

	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(菜单功能管理)(递归处理)
	 * 且根据用户ID的所有权限，去过滤菜单列表
	 * @param string
	 * @param user_ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> listValidMenuForFun(String MENU_ID, String USER_ID) throws Exception{
		//查询出用户可用的权限内的所有功能列表
		List<MenuFun> menuList = menuFunService.listAllMenuFunByUserId(USER_ID);
		if(CollectionUtils.isEmpty(menuList)){
			return Collections.EMPTY_LIST;
		}
		List<String> validMenuFunCodes = new ArrayList<String>(menuList.size());
		for(MenuFun fun:menuList){
			validMenuFunCodes.add(fun.getFUN_CODE().trim().toUpperCase());
		}
		return listValidMenuForFun2(MENU_ID, validMenuFunCodes);
	}
	
	/**
	 * 根据功能过滤菜单列表
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	private List<Menu> listValidMenuForFun2(String MENU_ID,List<String> validMenuFunCodes) throws Exception {
		List<Menu> menuList = this.listSubMenuByParentId(MENU_ID,true);
		if(menuList!=null && menuList.size()==0){
			menuList = null ; //不可动，用于下方判断使用
		}
		if(!CollectionUtils.isEmpty(menuList)){
			for(int i=0;i<menuList.size();){
				Menu menu = menuList.get(i);
				List<Menu> subList = this.listValidMenuForFun2(menu.getMENU_ID(),validMenuFunCodes);
				menu.setSubMenu(subList);
				menu.setTarget("treeFrame");
				if(subList==null){
					//最基本的叶子节点,权限验证
					if(Jurisdiction.checkRights(menu.getMENU_URL(),validMenuFunCodes)){
						//最基本的叶子节点，增加功能设置的链接
						menu.setMENU_URL("mf/list.do?MENU_ID="+menu.getMENU_ID());
						i++;
					}else{
						menuList.remove(i);
					}
				}else if(subList.size()==0){
					//是目录，子目录被过滤掉了，所以list长度为0了
					menuList.remove(i);
				}else{
					//存在子目录，设置url为空
					menu.setMENU_URL(null);
					i++;
				}
			}
		}
		return menuList;
	}

	/**
	 * 检测菜单下的功能是否被角色使用
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public boolean checkMenuUsed(String MENU_ID) throws Exception {
		Integer count = (Integer) dao.findForObject("MenuMapper.checkMenuUsed", MENU_ID);
		if(count!=null && count >=1){
			return true;
		}
		return false;
	}

	/**
	 * 重新加载菜单
	 */
	public void reloadMenu(String PARENT_ID) throws Exception {
		//重新加载子节点缓存
		reloadSubMenuByParentId(PARENT_ID);
		
		//重新加载菜单
		List<Menu> allmenuList = new ArrayList<Menu>();
		int roleType = Jurisdiction.getUserMaxRoles();
		if(roleType==0){
			//获取所有菜单
			allmenuList = this.listAllMenuQx("0");
		}else{
			User user = Jurisdiction.getCurrentUser();
			//查询用户权限,重新从数据库读取
			List<MenuFun> list = menuFunService.listAllMenuFunByUserId(user.getUSER_ID());
			List<String> mfList = Jurisdiction.getMfList(list);
			//把用户权限信息放session中
			Jurisdiction.getSession().setAttribute(user.getUSERNAME()+Const.SESSION_ROLE_RIGHTS, mfList);
			allmenuList = this.listValidAllMenuQx("0",mfList);
		}
		Session session = Jurisdiction.getSession();
		//清空菜单session
		String name = Jurisdiction.getUsername();
		session.removeAttribute(name + Const.SESSION_CURRENT_MENU_LIST) ;
		session.removeAttribute(name + Const.SESSION_ALL_MENU_LIST) ;
		//菜单重新放入session中
		session.setAttribute(name + Const.SESSION_ALL_MENU_LIST, allmenuList);
	}
	
	/**
	 * 当前菜单下级缓存菜单
	 * @param parentId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void reloadSubMenuByParentId(String parentId) throws Exception {
		List<Menu> list = (List<Menu>) dao.findForList("MenuMapper.listSubMenuByParentId", parentId);
		Const.SYS_MENU_MAP.put(Const.SYS_MENU_FATHER_ID+parentId,list);
	}

}
