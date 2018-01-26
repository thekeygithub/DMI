package com.ts.service.system.menu;

import java.util.List;
import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.util.PageData;

/**MenuService 菜单处理接口
 * @author xsl
 */
public interface MenuManager {

	/**
	 * 根据父ID查询子菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listSubMenuByParentId(String parentId,boolean cacheFlag)throws Exception;
	
	/**
	 * 查询单个菜单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData getMenuById(PageData pd) throws Exception;
	
	/**
	 * 菜单保存
	 * @param menu
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception;
	
	/**
	 * 删除菜单
	 * @param MENU_ID
	 * @throws Exception
	 */
	public void deleteMenuById(String MENU_ID) throws Exception;
	
	/**
	 * 编辑存菜单图标 
	 * @param menu
	 * @throws Exception
	 */
	public void edit(Menu menu) throws Exception;
	
	/**
	 *  保存菜单图标 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData editicon(PageData pd) throws Exception;
	
	/**
	 *  获取所有菜单并填充每个菜单的子菜单列表(菜单管理)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenu(String MENU_ID) throws Exception;
	
	/**
	 * 根据用户ID的所有权限，去过滤菜单列表
	 * @param string
	 * @param user_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listValidMenu(String MENU_ID, String user_ID) throws Exception;
	
	/**
	 * 获取所有菜单并填充每个菜单的子菜单列表(菜单功能管理)(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuForFun(String MENU_ID) throws Exception;
	
	/**
	 * 系统菜单列表ALL
	 * @param MENU_ID
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listAllMenuQx(String MENU_ID) throws Exception;

	
	/**
	 * 系统菜单列表，根据权限过滤
	 * @param MENU_ID
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listValidAllMenuQx(String MENU_ID,List<String> mfList) throws Exception;
	/**
	 * 根据用户ID的所有权限，去过滤菜单列表
	 * @param string
	 * @param user_ID
	 * @return
	 * @throws Exception
	 */
	public List<Menu> listValidMenuForFun(String MENU_ID, String user_ID) throws Exception;

	/**
	 * 检测菜单的功能是否被某角色使用
	 * @param mENU_ID
	 * @return
	 * @throws Exception
	 */
	public boolean checkMenuUsed(String mENU_ID) throws Exception;

	/**
	 * 
	 * @throws Exception
	 */
	public void reloadMenu(String PARENT_ID) throws Exception;
	
	/**
	 * 当前菜单下级缓存菜单
	 * @param parentId
	 * @throws Exception
	 */
	public void reloadSubMenuByParentId(String parentId) throws Exception ;

}
