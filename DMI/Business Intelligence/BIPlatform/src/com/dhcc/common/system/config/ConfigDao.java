package com.dhcc.common.system.config;

import com.dhcc.common.util.StringUtil;

public class ConfigDao {
	public String QueryList(String dtype,String dvalue,String sortname,String sortorder){
		String querysql = "select * from tsconfig where 1=1";
		
		if (!StringUtil.isNullOrEmpty(dtype)) {
			querysql += " and dtype like '%" + dtype + "%'";
		}
		if (!StringUtil.isNullOrEmpty(dvalue)) {
			querysql += " and dvalue like '%" + dvalue + "%'";
		}
		querysql += " order by "+sortname+" "+sortorder+" ";
		return querysql;
	}
	public String QuerySql(String id){
		return "select * from tsconfig where id = '"+id+"'";
	}
}
