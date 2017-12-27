package com.framework.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class SqlUtils {

	StringBuffer handle = new StringBuffer();

	// 直接获取插入语句
	public static String getMap2SqlByTabName(Map map, String tableName) {
		// 这里需要判断，判断本次拿到的值与数据库里的值进行判断 主键如果不存在 执行insert 存在 执行update
		/*
		 * if(map!=null) { Set key = map.keySet(); //获得key的集合 Iterator it =
		 * key.iterator(); while(it.hasNext()){ String colname =
		 * it.next().toString(); //获得key值 String.valueOf(map.get(colname));
		 * //获得vaule值
		 * System.out.println("+++++++"+map.get("tree_id"));//获得传过来的数据的tree_id
		 * 
		 * } } //获取数据库里tree_id的值 List list = new ArrayList(); String selectSQL =
		 * "select tree_id from " + tableName ; String
		 * rs=sqlCtrl.execute(selectSQL);
		 * 
		 * return null;
		 */

		StringBuffer getString = new StringBuffer(" insert into " + tableName);// 返回的sql
		StringBuffer col = new StringBuffer("(");// 拼接列名
		StringBuffer val = new StringBuffer("values(");// 拼接value值
		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {
				String colname = it.next().toString(); // 获得key值
				col.append(colname + ",");
				String colvalue = String.valueOf(map.get(colname));
				if (StringUtils.isNumeric(colvalue) == false
						&& colvalue.indexOf(".NEXTVAL") < 0
						&& !"SYSDATE".equals(colvalue)) {
					colvalue = "'" + colvalue + "'";// 不是数字，需要加单引号
				}
				if (StringUtils.isEmpty(colvalue))
					val.append("null,");
				else
					val.append(String.valueOf(map.get(colname)) + ","); // 获得vaule值
			}
			String colsp = col.substring(0, col.lastIndexOf(","));// 去掉最后逗号
			String valsp = val.substring(0, val.lastIndexOf(","));// 去掉最后逗号
			getString.append(colsp + ") ");
			getString.append(valsp + ")");
		}
		// System.out.println("打印sql语句："+getString.toString());
		return getString.toString();
	}

	// 获取update更新语句
	public static String getUpdateSqlByMap(Map map, String tableName,
			String whereSQL) {
		StringBuffer getString = new StringBuffer(" update " + tableName
				+ " set ");// 返回的sql
		StringBuffer setSQL = new StringBuffer("");// 拼接列名

		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {

				String colname = it.next().toString(); // 获得key值
				String colvalue = String.valueOf(map.get(colname));
				if (StringUtils.isNumeric(colvalue) == false
						&& colvalue.indexOf(".NEXTVAL") < 0
						&& !"SYSDATE".equals(colvalue)) {
					colvalue = "'" + colvalue + "'";// 不是数字，需要加单引号
				}
				if (StringUtils.isEmpty(colvalue))
					setSQL.append(colname).append("=").append("''").append(",");
				else
					setSQL.append(colname).append("=").append(colvalue).append(
							",");

			}
			String setSQLsp = setSQL.substring(0, setSQL.lastIndexOf(","));// 去掉最后逗号

			getString.append(setSQLsp).append(" where 1=1 ").append(whereSQL);

		}
		return getString.toString();
	}
	

	/**
	 *  返回sql串  带？号的形式，参数传给 ls变量   
	 * @param List<Object> ls  SQL实际参数变量  ls.add()
	 * @param map SQL 字段 健值对
	 * @param String tableName  表名
	 * @param String whereSQL
	 * @return sql  生成  update tablename set col_1=?, col_2=?;
	 * @throws Exception
	 */
	public static String getUpdateSqlByMap(List<Object> ls, Map map, String tableName,
			String whereSQL) {
		StringBuffer getString = new StringBuffer(" update " + tableName
				+ " set ");// 返回的sql
		StringBuffer setSQL = new StringBuffer("");// 拼接列名

		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {

				String colname = it.next().toString(); // 获得key值
				String colvalue = String.valueOf(map.get(colname));
				if ( colvalue.indexOf(".NEXTVAL") < 0 && !"SYSDATE".equals(colvalue) ) {				
					setSQL.append(colname).append("=").append("?").append(",");
					ls.add(colvalue);
				} else {
					// NEXTVAL  SYSDATE 
					setSQL.append(colname).append("=").append(colvalue).append(",");
				}				

			}
			String setSQLsp = setSQL.substring(0, setSQL.lastIndexOf(","));// 去掉最后逗号

			getString.append(setSQLsp).append(" where 1=1 ").append(whereSQL);

		}
		return getString.toString();
	}
	

	/**
	 *  返回sql串  带？号的形式，参数传给 ls变量    	
	 * @param List<Object> ls  SQL实际参数变量  ls.add()
	 * @param map SQL 字段 健值对
	 * @param String tableName  表名
	 * @param String whereSQL
	 * @return sql  生成  insert tablename (col_1, col_2) values (?,?)
	 * @throws Exception
	 */
	public static String getInsertSqlByMap(List<Object> ls, Map map, String tableName) {
		StringBuffer getString = new StringBuffer(" INSERT INTO " + tableName);// 返回的sql
		StringBuffer col = new StringBuffer("(");// 拼接列名
		StringBuffer val = new StringBuffer("VALUES(");// 拼接value值
		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {
				String colname = it.next().toString(); // 获得key值
				col.append(colname + ",");
				String colvalue = CommMethod.getObject2Str(map.get(colname));
				if ( colvalue.indexOf(".NEXTVAL") < 0 && !"SYSDATE".equals(colvalue) ) {								
					val.append("?,");
					ls.add(colvalue);
				} else {
					val.append(colvalue + ","); // 获得vaule值
					
				}
				
			}
			String colsp = col.substring(0, col.lastIndexOf(","));// 去掉最后逗号
			String valsp = val.substring(0, val.lastIndexOf(","));// 去掉最后逗号
			getString.append(colsp + ") ");
			getString.append(valsp + ")");
		}
		return getString.toString();
	}

	/*
	 * @return String 返回sql串
	 */
	public static String getInsertSqlByMap(Map map, String tableName) {
		StringBuffer getString = new StringBuffer(" INSERT INTO " + tableName);// 返回的sql
		StringBuffer col = new StringBuffer("(");// 拼接列名
		StringBuffer val = new StringBuffer("VALUES(");// 拼接value值
		if (map != null) {
			Set key = map.keySet(); // 获得key的集合
			Iterator it = key.iterator();
			while (it.hasNext()) {
				String colname = it.next().toString(); // 获得key值
				col.append(colname + ",");
				String colvalue = CommMethod.getObject2Str(map.get(colname));
				if (StringUtils.isNumeric(colvalue) == false
						&& colvalue.indexOf(".NEXTVAL") < 0
						&& !"SYSDATE".equals(colvalue)) {
					colvalue = "'" + colvalue + "'";// 不是数字，需要加单引号
				}
				if (StringUtils.isEmpty(colvalue))
					val.append("null,");
				else
					val.append(colvalue + ","); // 获得vaule值
			}
			String colsp = col.substring(0, col.lastIndexOf(","));// 去掉最后逗号
			String valsp = val.substring(0, val.lastIndexOf(","));// 去掉最后逗号
			getString.append(colsp + ") ");
			getString.append(valsp + ")");
		}
		return getString.toString();
	}
	
	

	// 获得Map的key,得到形式如：a,b,c,d
	public String obtainKey(Map map) {
		Set a = map.keySet();
		Iterator s = a.iterator();
		System.out.println(s.toString());
		String b = "";
		while (s.hasNext()) {
			if (b != "") {
				b = b + "," + s.next();
			} else {
				b = s.next() + "";
			}
		}
		return b;
	}

	// 获得Map的values,得到形式如：'a','b','c'
	public String obtainValues(Map map) {
		Collection collection = map.values();
		Iterator itertor = collection.iterator();

		String k = "";
		while (itertor.hasNext()) {
			if (k != "") {
				k = k + ",'" + itertor.next() + "'";
			} else {
				k = "'" + itertor.next() + "'";
			}
		}
		return k;
	}
}
