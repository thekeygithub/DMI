package com.dhcc.common.system.page;

import com.dhcc.modal.system.PageModel;

/**
 * Oracle
 * @author GYR
 *
 */
public class OracleFactory implements PageInter{

	@Override
	public String createSql(String querysql, PageModel pm) {
	String sql = "SELECT * FROM " +
			     "(SELECT a.*,ROWNUM  rnn from ("+querysql+")a " +
	     		 "where  ROWNUM<="+pm.getPerPage()*pm.getCurrentPage()+") " +
 		 		 "where rnn>"+(pm.getCurrentPage()-1)*pm.getPerPage()+"";
		return sql;
	}


}
