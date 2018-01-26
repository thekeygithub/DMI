package com.ts.service.system.user.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.Page;
import com.ts.entity.system.User;
import com.ts.service.system.role.RoleManager;
import com.ts.service.system.user.UserManager;
import com.ts.util.PageData;


/** 系统用户
 * @author xsl
 * 修改时间：2015.11.2
 */
@Service("userService")
public class UserService implements UserManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name = "roleService")
	private RoleManager roleService;
	
	/**登录判断
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public User getUserByNameAndPwd(PageData pd)throws Exception{
		return (User)dao.findForObject("UserMapper.getUserInfo", pd);
	}
	
	/**更新登录时间
	 * @param pd
	 * @throws Exception
	 */
	public void updateLastLogin(User user)throws Exception{
		dao.update("UserMapper.updateLastLogin", user);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public User findUserById(String USER_ID)throws Exception{
		return (User)dao.findForObject("UserMapper.findUserById", USER_ID);
	}
	
	/**用户列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listUsers(Page page)throws Exception{
		return (List<PageData>) dao.findForList("UserMapper.userlistPage", page);
	}
	
	/**通过USERNAEME获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(String name)throws Exception{
		return (PageData)dao.findForObject("UserMapper.findByUsername", name);
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(User user)throws Exception{
		dao.save("UserMapper.saveU", user);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception{
		dao.delete("UserMapper.delUserRoleByUserId", pd.get("USER_ID"));
		dao.delete("UserMapper.deleteU", pd);
	}
	
	/**批量删除用户
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllU(String[] USER_IDS)throws Exception{
		dao.delete("UserMapper.deleteAllUserRoles", USER_IDS);
		dao.delete("UserMapper.deleteAllU", USER_IDS);
	}
	
	/**
	 * 更新用户角色
	 * @param uSER_ID
	 * @param pds
	 * @throws Exception
	 */
	public void updateUserRoles(String USER_ID, List<PageData> pds) throws Exception {
		dao.delete("UserMapper.delUserRoleByUserId", USER_ID);
		for(PageData pd:pds){
			dao.save("UserMapper.insertUserRoles", pd);
		}
	}

	/**
	 * 删除用户的所有角色
	 * @param uSER_ID
	 * @throws Exception
	 */
	public void deleteUserAllRoles(String USER_ID) throws Exception {
		dao.delete("UserMapper.delUserRoleByUserId", USER_ID);
	}
	
	/**保存用户IP
	 * @param pd
	 * @throws Exception
	 */
	public void saveIP(PageData pd)throws Exception{
		dao.update("UserMapper.saveIP", pd);
	}
	
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUE(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserMapper.findByUE", pd);
	}
	
	/**通过编号获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUN(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserMapper.findByUN", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserMapper.findById", pd);
	}
	
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception{
		dao.update("UserMapper.editU", pd);
	}
	
	/**用户列表(全部)
	 * @param USER_IDS
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllUser(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("UserMapper.listAllUser", pd);
	}
	
	/**用户列表(下拉框使用)
	 * @param USER_IDS
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listSimpleUser(PageData pd) throws Exception{
		return (List<PageData>) dao.findForList("UserMapper.listSimpleUser", pd);
	}
	
	/**获取总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData getUserCount(String value) throws Exception{
		return (PageData)dao.findForObject("UserMapper.getUserCount", value);
	}

	@Override
	public List<PageData> listSubUser(User user) throws Exception {
		//获取用户下的所有下级角色
		List<String> role_ids = roleService.listSubRoleByUsId(user.getUSER_ID(),false);
		//获取角色的所有用户
		List<PageData> list =  (List<PageData>) dao.findForList("UserMapper.listSubUser", role_ids);
		PageData p = new PageData();
		p.put("USER_ID", user.getUSER_ID());
		p.put("USERNAME", user.getUSERNAME());
		list.add(p);
		return list;
	}
	
}
