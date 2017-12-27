package com.dhcc.common.system.page;

import com.dhcc.modal.system.PageModel;

/**
 * 分页SQL构造
 * @author GYR
 */
public class PageFactory {
	private static final String DATABASE_TYPE = "ORACLE";
	/**
	 * @param querysql 查询sql
	 * @param pm 分页bean
	 */
	public  String createPageSQL(String querysql,PageModel pm){
		String sql  = "";
		if(DATABASE_TYPE.equals("ORACLE")){
			OracleFactory temp = new  OracleFactory();
			sql = temp.createSql(querysql, pm);
			temp = null;
		}else if(DATABASE_TYPE.equals("MYSQL")){
			MySqlFactory temp = new MySqlFactory();
			sql = temp.createSql(querysql, pm);
		    temp = null;
		}
		return sql;
	}

}
