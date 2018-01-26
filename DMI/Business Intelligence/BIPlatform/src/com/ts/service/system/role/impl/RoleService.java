package com.ts.service.system.role.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.service.system.role.RoleManager;
import com.ts.util.BasicConst;
import com.ts.util.PageData;

/**	角色
 * @author xsl
 * 修改日期：2015.11.6
 */
@Service("roleService")
public class RoleService implements RoleManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**通过id查找
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findObjectById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("RoleMapper.findObjectById", pd);
	}
	
	/**添加
	 * @param pd
	 * @throws Exception
	 */
	public void add(PageData pd) throws Exception {
		dao.save("RoleMapper.insert", pd);
	}
	
	/**给当前角色附加菜单权限
	 * @param role
	 * @throws Exception
	 */
	public void updateRoleRights(Role role) throws Exception {
		dao.update("RoleMapper.updateRoleRights", role);
	}
	
	/**通过id查找
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public Role findRoleById(String ROLE_ID) throws Exception {
		return (Role) dao.findForObject("RoleMapper.findRoleById", ROLE_ID);
	}
	
	/**给全部子角色加菜单权限
	 * @param pd
	 * @throws Exception
	 */
	public void setAllRights(PageData pd) throws Exception {
		dao.update("RoleMapper.setAllRights", pd);
	}
	
	/**
	 * 用户所有可用的角色树
	 * @param string 根节点角色ID
	 * @param user 登陆用户
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Role> listUserAllValideRole(String ROLE_ID,User user,int type) throws Exception {
		//存放页面展现的菜单功能列表树
		List<Role> roleList = null;
		//当前用户的角色类型判断,如果是管理角色、授权角色业务角色
		int roleType = this.getUserMaxRolesByUsId(user.getUSER_ID());
		if(roleType==0){
			//管理角色，显示全部角色树
			roleList = this.listAllRole(ROLE_ID);
		}else if(roleType==1){
			//获取当前登陆用户可以使用的角色ID集合
			List<String> valideIds = this.listSubRoleByUsId(user.getUSER_ID(),true);
			//授权角色，显示用户所有角色及角色下属角色树
			roleList = this.listAllRoleValide(ROLE_ID,valideIds,type);
		}else{
			//业务角色直接返回空菜单功能
			 return Collections.EMPTY_LIST;
		}
		return roleList;
	}
	/**
	 * 字段管理所有可用的角色树
	 * @param string 根节点角色ID
	 * @param user 登陆用户
	 * @return
	 * @throws Exception
	 */
	public List<Role> listUserAllAccessRole(String ROLE_ID,User user,int type) throws Exception {
		//存放页面展现的菜单功能列表树 ，显示全部角色树
		List<Role> roleList = this.listAllAccRole(ROLE_ID);	
		return roleList;
	}
	
	/**
	 * 显示用户所有角色ID及角色下属角色ID集合（包括子角色）
	 * @param user_ID
	 * @param containCurrent 是否包含当前顶级结点，true 包括当前节点和其子节点,false时，仅仅返回子节点不包括父节点。
	 * @return
	 * @throws Exception 
	 */
	public List<String> listSubRoleByUsId(String USER_ID,boolean containCurrent) throws Exception {
		List<String> result = new ArrayList<String>();
		List<Role> roles_list = this.getUserRolesById(USER_ID);
		if(!CollectionUtils.isEmpty(roles_list)){
			for(Role role:roles_list){
				getAllRoleByUsId(role.getROLE_ID(),result,containCurrent);
			}
		}
		return result;
	}
	private void getAllRoleByUsId(String ROLE_ID,List<String> result,boolean containCurrent) throws Exception{
		List<Role> subRoles = listSubRoleByParentId(ROLE_ID);
		if(!CollectionUtils.isEmpty(subRoles)){
			for(Role role:subRoles){
				getAllRoleByUsId(role.getROLE_ID(),result,true);//子节点都需要包含
			}
		}
		if(containCurrent && !result.contains(ROLE_ID)){
			result.add(ROLE_ID);
		}
	}

	/**
	 * 根据已经拥有的角色ID集合，对角色树进行判断是否被选中
	 * @param roleList
	 * @param hasRoleIds
	 * @return
	 */
	public List<Role> setCheckedRole(List<Role> roleList,List<String> hasRoleIds){
		for(Role role : roleList){
			if(!CollectionUtils.isEmpty(hasRoleIds)){
				//用户的已经拥有的角色上设置选中checked=true
				if(hasRoleIds.contains(role.getROLE_ID())){
					role.setHasRole(true);
				}
			}
			role.setSubRole(this.setCheckedRole(role.getSubRole(),hasRoleIds));
			role.setTarget("treeFrame");
		}
		return roleList;
	}
	
	
	/**
	 * 获取当前角色下的所有角色，递归处理，不涉及权限
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Role> listAllRole(String ROLE_ID) throws Exception {
		List<Role> roleList = this.listSubRoleByParentId(ROLE_ID);
		if(!CollectionUtils.isEmpty(roleList)){
			for(Role role : roleList){
				role.setNoCheck(true);
				role.setROLE_URL("role/list.do?ROLE_ID="+role.getROLE_ID());
				role.setSubRole(this.listAllRole(role.getROLE_ID()));
				role.setTarget("treeFrame");
			}
		}
		return roleList;
	}
	/**
	 * 获取当前角色下的所有角色，递归处理，不涉及权限
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Role> listAllAccRole(String ROLE_ID) throws Exception {
		List<Role> roleList = this.listSubRoleByParentId(ROLE_ID);
		if(!CollectionUtils.isEmpty(roleList)){
			for(Role role : roleList){
				role.setNoCheck(true);
				role.setROLE_URL("api/roleList.do?ROLE_ID="+role.getROLE_ID());
				role.setSubRole(this.listAllAccRole(role.getROLE_ID()));
				role.setTarget("treeFrame");
			}
		}
		return roleList;
	}
	
	/**
	 * 获取当前角色下的所有可用的角色
	 * @param ROLE_ID
	 * @param valideIds 可以使用的角色ID集合
	 * @param type 0 用户角色设置，1角色管理
	 * @return
	 * @throws Exception
	 */
	public List<Role> listAllRoleValide(String ROLE_ID,List<String> valideIds,int type) throws Exception {
		List<Role> roleList = this.listSubRoleByParentId(ROLE_ID);
		if(!CollectionUtils.isEmpty(roleList)){
			for( int i =0; i<roleList.size();){
				Role role = roleList.get(i);
				role.setTarget("treeFrame");
				List<Role> list = this.listAllRoleValide(role.getROLE_ID(),valideIds,type);
				if(type==0){
					role.setSubRole(list);
					if(valideIds.contains(role.getROLE_ID())){
						role.setROLE_URL("role/list.do?ROLE_ID="+role.getROLE_ID());
					}else{
						//当前的角色，当前用户无权限使用,设置为不允许勾选
						role.setChk(true);
						//无权限且子节点为空，则删除当前节点
						if(CollectionUtils.isEmpty(list)){
							roleList.remove(i);
							continue;
						}
					}
				}else{
					role.setChk(true);
					role.setSubRole(list);
					if(valideIds.contains(role.getROLE_ID())){
						role.setROLE_URL("role/list.do?ROLE_ID="+role.getROLE_ID()+"&HASROLE="+1);
						//拥有角色权限，可编辑
						role.setHasRole(true);
					}else{
						//没有使用权限
						role.setHasRole(false);
						role.setROLE_URL("role/list.do?ROLE_ID="+role.getROLE_ID()+"&HASROLE="+0);
						//且子节点为空，则删除当前节点
						if(CollectionUtils.isEmpty(list)){
							roleList.remove(i);
							continue;
						}
					}
				}
				i++;
			}
		}
		return roleList;
	}

	/**通过用户ID获取用户拥有的角色集合，无层级关系
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getUserRolesById(String USER_ID) throws Exception {
		return (List<Role>) dao.findForList("RoleMapper.listUserRolesByUsId", USER_ID);
	}
	
	/**通过用户ID获取用户拥有的角色ID集合
	 * @param USER_ID
	 * @return
	 * @throws Exception
	 */
	public List<String> getUserRoleIdsById(String USER_ID) throws Exception {
		List<Role> list = this.getUserRolesById(USER_ID);
		List<String> hasRoleIds = null;
		if(!CollectionUtils.isEmpty(list)){
			hasRoleIds = new ArrayList<String>(list.size());
			for(Role role:list){
				hasRoleIds.add(role.getROLE_ID());
			}
		}
		return hasRoleIds;
	}
	
	/**
	 * 通过ID获取其子一级菜单
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Role> listSubRoleByParentId(String parentId) throws Exception {
		return (List<Role>) dao.findForList("RoleMapper.listSubRoleByParentId", parentId);
	}

	/**根据ID 获取角色信息
	 * @param pd
	 * @throws Exception
	 */
	public PageData getRoleById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("RoleMapper.getRoleById", pd);
	}

	/**保存
	 * @param role
	 * @throws Exception
	 */
	public void saveRole(Role role) throws Exception {
		dao.save("RoleMapper.insertRole", role);		
	}

	/**保存修改
	 * @param role
	 * @throws Exception
	 */
	public void edit(Role role) throws Exception {
		dao.update("RoleMapper.edit", role);
	}
	
	/**删除角色、删除角色权限关系、删除用户角色关系
	 * @param ROLE_ID
	 * @throws Exception
	 */
	public void deleteRoleById(String ROLE_ID) throws Exception {
		delAllRoleSubById(ROLE_ID);
	}
	/**
	 * 删除菜单和菜单的所有下级菜单
	 * modify by xsl 2017
	 * @param MENU_ID
	 * @throws Exception 
	 */
	private void delAllRoleSubById(String ROLE_ID) throws Exception{
		//查询出当前菜单的所有下级菜单
		List<Role> menu_list = this.listSubRoleByParentId(ROLE_ID);
		if(!CollectionUtils.isEmpty(menu_list)){
			for(Role role:menu_list){
				delAllRoleSubById(role.getROLE_ID());
			}
		}
		PageData pd = new PageData();
		pd.put("ROLE_ID", ROLE_ID);
		//删除用户-角色关系
		dao.delete("RoleMapper.delUserRoleRelaByRoleId", pd);
		// 删除角色-功能权限关系  
		dao.delete("RoleMapper.delRoleMfByRoleId", pd);
		//删除角色-接口数据权限关系
		dao.delete("RoleMapper.delRelationByRoleId", pd);
		//删除角色
		dao.delete("RoleMapper.deleteRoleById", pd);
	}
	/**
	 * 更新角色的功能权限 
	 * @param ROLE_ID
	 * @param pds
	 * @throws Exception
	 */
	public void updateRoleMF(String ROLE_ID, List<PageData> pds) throws Exception {
		PageData p = new PageData();
		p.put("ROLE_ID", ROLE_ID);
		p.put("DATABASE_TYPE", BasicConst.DATABASE_TYPE);
		dao.delete("RoleMapper.delRoleMfByRoleId", p);
		for(PageData pd:pds){
			dao.save("RoleMapper.insertRoleRights", pd);
		}
	}

	/**
	 * 删除角色的功能权限 
	 * @param rOLE_ID
	 * @throws Exception
	 */
	public void deleteRoleAllRights(String ROLE_ID) throws Exception {
		PageData p = new PageData();
		p.put("ROLE_ID", ROLE_ID);
		p.put("DATABASE_TYPE", BasicConst.DATABASE_TYPE);
		dao.delete("RoleMapper.delRoleMfByRoleId", p);
	}
	
	/**
	 * 获取用户的最高角色,0管理角色，1授权角色，2业务角色
	 * @param USER_Id
	 * @return
	 * @throws Exception
	 */
	public int getUserMaxRolesByUsId(String USER_Id) throws Exception{
		int roleType = 2;//默认业务角色
		List<Role> roleList = this.getUserRolesById(USER_Id);
		if(CollectionUtils.isEmpty(roleList)){ return roleType;}
		for(Role role:roleList){
			if(role.getROLE_TYPE()!=null ){
				roleType = role.getROLE_TYPE()<roleType?role.getROLE_TYPE():roleType;
			}
		}
		return roleType;
	}

	/**
	 * 验证是否有用户使用该角色
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	public boolean checkRoleUsed(String ROLE_ID) throws Exception {
		Integer count = (Integer) dao.findForObject("RoleMapper.checkRoleUsed", ROLE_ID);
		if(count!=null && count >=1){
			return true;
		}
		return false;
	}
	/**
	 * 根据类型查询角色列表 
	 * add by zhy
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listRolesByType(Page page) throws Exception {
		return (List<PageData>) dao.findForList("RoleMapper.listRolesByTypePage", page);
	}
}
