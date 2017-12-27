package com.dhcc.common.system.page;

import com.dhcc.modal.system.PageModel;

/**
 * MySQL
 * @author GYR
 *
 */
public class MySqlFactory implements PageInter{

	@Override
	public String createSql(String querysql, PageModel pm) {
		String sql = querysql+" limit "+(pm.getCurrentPage()-1)*pm.getPerPage()+","+pm.getPerPage();
		return sql;
	}

}
