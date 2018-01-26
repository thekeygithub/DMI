package com.ts.service.system.log.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ts.dao.DaoSupport;
import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.service.system.log.LogManager;
import com.ts.util.PageData;
import com.ts.util.PublicUtil;
import com.ts.util.UuidUtil;

/** 
 * 类名称： 系统日志处理
 * 创建人：xingsilong
 * 修改时间：2015年10月27日
 * @version v2
 */
@Service("logService")
public class LogService implements LogManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 保存日志
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	private void saveLog(PageData pd) throws Exception {
		dao.save("LogMapper.saveLog", pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param user
	 * @param type
	 * @param username
	 * @throws Exception
	 */
	public void saveUserLog(User user,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", user.getUSER_ID());
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_user");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(user));
		this.saveLog(pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param user
	 * @param type
	 * @param username
	 * @throws Exception
	 */
	public void saveUserLog(PageData user,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", user.get("USER_ID"));
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_user");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(user));
		this.saveLog(pd);
	}
	

	public void saveUserLog(String user_ids, int type,String name) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", user_ids);
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_user");
		pd.put("UPDATE_MAN", name);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", "");
		this.saveLog(pd);
		
	}
	
	
	
	/**
	 * 保存更改用户信息的日志
	 * @param user
	 * @param type
	 * @param username
	 * @throws Exception
	 */
	public void saveRoleLog(Role role,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", role.getROLE_ID());
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_role");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(role));
		this.saveLog(pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param user
	 * @param type
	 * @param username
	 * @throws Exception
	 */
	public void saveRoleLog(PageData role,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", role.get("ROLE_ID"));
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_role");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(role));
		this.saveLog(pd);
	}
	

	public void saveRoleLog(String role_ids, int type,String name) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", role_ids);
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_role");
		pd.put("UPDATE_MAN", name);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", "");
		this.saveLog(pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param user
	 * @param type
	 * @param username
	 * @throws Exception
	 */
	public void saveMenuLog(Menu menu,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", menu.getMENU_ID());
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_menu");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(menu));
		this.saveLog(pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param user
	 * @param type
	 * @param username
	 * @throws Exception
	 */
	public void saveMenuLog(PageData menu,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", menu.get("MENU_ID"));
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_menu");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(menu));
		this.saveLog(pd);
	}
	

	public void saveMenuLog(String menu_ids, int type,String name) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", menu_ids);
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_menu");
		pd.put("UPDATE_MAN", name);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", "");
		this.saveLog(pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param user
	 * @param type
	 * @param username
	 * @throws Exception
	 */
	public void saveMenuFunLog(MenuFun menu,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", menu.getMF_ID());
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_mf");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(menu));
		this.saveLog(pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param menu_ids
	 * @param username
	 * @throws Exception
	 */
	public void saveMenuFunLog(String menu_ids, int type,String name) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", menu_ids);
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_mf");
		pd.put("UPDATE_MAN", name);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", "");
		this.saveLog(pd);
	}
	
	/**
	 * 保存更改用户信息的日志
	 * @param menu
	 * @param username
	 * @throws Exception
	 */
	public void saveMenuFunLog(PageData menu,int type, String username) throws Exception{
		PageData pd = new PageData();
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("T_KEY", menu.get("MF_ID"));
		pd.put("OP_TYPE", type);
		pd.put("TABLE_NAME", "sys_mf");
		pd.put("UPDATE_MAN", username);
		pd.put("UPDATE_TIME", new Date());
		pd.put("CONTENT", PublicUtil.getObjectString(menu));
		this.saveLog(pd);
	}
}
