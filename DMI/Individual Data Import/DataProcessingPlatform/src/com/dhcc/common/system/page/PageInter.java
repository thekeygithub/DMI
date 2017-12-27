package com.dhcc.common.system.page;

import com.dhcc.modal.system.PageModel;


public interface PageInter {
	
	/**
	 * @param querysql 查询sql
	 * @param pm 分页model
	 * @return 分页语句
	 */
   String createSql(String querysql,PageModel pm);

}
