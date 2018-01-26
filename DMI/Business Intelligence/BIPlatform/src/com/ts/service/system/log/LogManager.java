package com.ts.service.system.log;

import com.ts.entity.system.Menu;
import com.ts.entity.system.MenuFun;
import com.ts.entity.system.Role;
import com.ts.entity.system.User;
import com.ts.util.PageData;


/**MenuService 系统日志处理接口
 * @author xsl
 */
public interface LogManager {


	public void saveUserLog(User user, int type,String name) throws Exception;
	public void saveUserLog(String user_ids, int type,String name) throws Exception;
	public void saveUserLog(PageData pd, int type, String username) throws Exception;

	
	public void saveRoleLog(Role user, int type,String name) throws Exception;
	public void saveRoleLog(String user_ids, int type,String name) throws Exception;
	public void saveRoleLog(PageData pd, int type, String username) throws Exception;

	public void saveMenuLog(Menu menu, int type,String name) throws Exception;
	public void saveMenuLog(String user_ids, int type,String name) throws Exception;
	public void saveMenuLog(PageData pd, int type, String username) throws Exception;
	
	public void saveMenuFunLog(MenuFun menufun, int type,String name) throws Exception;
	public void saveMenuFunLog(String user_ids, int type,String name) throws Exception;
	public void saveMenuFunLog(PageData pd, int type, String name)throws Exception;

}