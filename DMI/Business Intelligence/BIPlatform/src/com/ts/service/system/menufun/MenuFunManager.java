package com.ts.service.system.menufun;

import java.util.List;

import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.entity.system.User;
import com.ts.util.PageData;


/**MenuService 菜单功能处理接口
 * @author xsl
 */
public interface MenuFunManager {

	
	/**
	 * 通过菜单ID获取菜单功能列表
	 * @param menu_id
	 * @return
	 * @throws Exception 
	 */
	public List<MenuFun> listMenuFunByMenuId(String menu_id) throws Exception;
	
	/**
	 * 获取所有菜单功能列表
	 * @return
	 * @throws Exception
	 */
	public List<MenuFun> listAllMenuFun() throws Exception;

	/**
	 * 保存菜单功能
	 * @param menuFun
	 * @throws Exception
	 */
	public void saveMenuFun(MenuFun menuFun) throws Exception;

	/**
	 * 删除菜单功能
	 * @param mf_id
	 * @throws Exception
	 */
	public void deleteMenuFunById(String mf_id) throws Exception;

	/**
	 * 获取菜单功能
	 * @param mf_id
	 * @throws Exception
	 */
	public PageData getMenuFunById(String mf_id) throws Exception;

	/**
	 * 更新编辑菜单功能
	 * @param menuFun
	 * @throws Exception
	 */
	public void edit(MenuFun menuFun) throws Exception;
	
	/**
	 *	 通过用户ID获取用户拥有的所有功能列表（包括用户角色下属角色拥有的所有功能）
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	public List<MenuFun> listAllMenuFunByUserId(String user_ID) throws Exception;
	
	/**
	 * 通过角色ID获取功能列表ID集合
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	public List<MenuFun> listAllMenuFunByRoleIds(List<String> ROLE_IDs) throws Exception;

	/**
	 * 查询当前角色已经拥有的功能权限ID集合
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	public List<String> listMenuFunByRoleId(String rOLE_ID)  throws Exception;
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(系统菜单及功能列表)(递归处理)
	 * @param string
	 * @param user
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllRoleMenuFun(String string, User user ,String roleId ) throws Exception;

	/**
	 * 判断菜单某项功能是否有角色使用
	 * @param mF_ID
	 * @return
	 * @throws Exception
	 */
	public boolean checkMenuFunUesd(String mF_ID) throws Exception;

	/**
	 * 检查功能编码
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean checkCode(PageData pd) throws Exception;
	
}
