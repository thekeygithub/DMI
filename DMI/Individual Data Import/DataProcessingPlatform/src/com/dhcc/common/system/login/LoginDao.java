package com.dhcc.common.system.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.util.HashUtil;
import com.dhcc.modal.system.Tscorp;
import com.dhcc.modal.system.Tsrole;
import com.dhcc.modal.system.Tsuser;
 

public class LoginDao {
	
	private static final Logger logger = Logger.getLogger(LoginDao.class);

	
	/**
	 * 查询该用户名、密码是否正确
	 * @return 是否有改用户
	 */
	public Tsuser QueryUser(String username,String password){
		 Tsuser modal=null;
		 DBManager dbm = new DBManager();
		 password = HashUtil.hashCode(password);
		 try {
			
			String sql = "select * from tsuser where loginname=  '" + username
					+ "' and userpass='" + password + "' ";
			modal = (Tsuser) dbm.getObject(Tsuser.class, sql);
		} catch (Exception e) {
			logger.error("查询用户名、密码正确与否出错**********", e);
		}finally{
			dbm.close();
		}
		return modal;
	}
	
	/**
	 * 查询该用户所属的公司
	 * @param userid
	 * @return
	 */
	public String QueryUserCorpid(String userid){
		 DBManager dbm = new DBManager();
		 Tscorp model = null;
		 String result = "";
		 String sql = "SELECT tc.* FROM tsuser tu " +
					  "LEFT JOIN tslusercorp tsl  ON  tu.id = tsl.userid " +
					  "LEFT JOIN tscorp tc on tc.id = tsl.corpid " +
					  "WHERE tu.id = '"+userid+"'";
		model = (Tscorp) dbm.getObject(Tscorp.class, sql);
		dbm.close();
		if(model != null){
			result = model.getId();
		}
		return result;
	}
	
	/**
	 * 查询该用户所属的角色
	 * @param userid
	 * @return
	 */
	public String QueryUserRoleId(String userid){
		DBManager dbm = new DBManager();
		List<Tsrole> listmodel = new ArrayList<Tsrole>();
		String result = "";
//		String sql=" select distinct(group_concat(t.roleid)) as id from tsluserrole t where t.userid='"+userid+"'  group by t.userid "; 
		
		String sql = "select distinct t.roleid as id from tsluserrole t where t.userid='a0babdd2c52c403483b44d8ae66238fc'";
		listmodel = dbm.getObjectList(Tsrole.class, sql);
		dbm.close();
		if(!listmodel.isEmpty()){
			String str = "";
			for(Tsrole model : listmodel){
				str += model.getId()+",";
			}
			if(str.endsWith(",")){
				str = str.substring(0, str.length()-1);
			}
			result = str;
		}
		return result;
	}	
	
}
