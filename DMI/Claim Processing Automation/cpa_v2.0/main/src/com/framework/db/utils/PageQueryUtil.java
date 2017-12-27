package com.framework.db.utils;

import java.text.MessageFormat;

/**
 * @author wangxiao
 */
public class PageQueryUtil {
	
	/**
	 * 根据传入的SQL 拼出翻页的SQL
	 * @param sql
	 * @param startIndex
	 * @param endIndex
	 * @return String
	 */
	public static String pageQuerySql(String sql, int startIndex, int endIndex) {
		String rtSql = "SELECT * FROM ( SELECT A.*, rownum r FROM ( {0} ) A WHERE rownum <= {2} ) B WHERE r > {1}";
		return MessageFormat.format(rtSql, new Object[] {sql, "" + startIndex , "" + endIndex});
	}
	
	/**
	 * 获得记录总数的SQL
	 * @param sql
	 * @return String
	 */
	public static String pageQuerySqlCount(String sql) {
		String rtSql = "SELECT count(*) FROM ( {0} )";
		return MessageFormat.format(rtSql, new Object[] {sql});
	}
}