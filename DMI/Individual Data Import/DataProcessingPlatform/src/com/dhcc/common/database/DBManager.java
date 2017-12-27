package com.dhcc.common.database;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.dhcc.common.system.page.PageFactory;
import com.dhcc.modal.system.PageModel;

/**
 * 
 * @author tiger
 * @创建日期 2010-8-24 上午01:05:06
 * @功能 实现数据库连接，提供数据库访问常用方法
 * 
 */
public class DBManager {
	private static final Logger logger = Logger.getLogger(DBManager.class);
	private Connection conn = null;// 数据连接对象
	private Statement stmt = null;
	private ResultSet rs = null;// 结果集

	/**
	 * 
	 * @author tiger
	 * @Date 2012-9-30 下午02:18:04
	 * @Description: 构造函数，用来创建一个数据库的链接 
	 *
	 */
	public DBManager() {
		logger.info("开始创建一个数据库连接.........");
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("无法连接到数据库!", e);
		}
	}

	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午01:03:11
	 * @方法名 init void
	 * @功能如下 初始化连接池连接和Statement
	 */
	private void init() {

		
		conn = DBConnectionManager.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			logger.error("设置自动提交为false出现异常");
			e1.printStackTrace();
		}
		try {
			stmt = conn.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("获取连接出现异常");
			
		}

	}
	
	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午01:01:27
	 * @方法名 close void
	 * @功能如下 关闭连接
	 */
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null && !conn.isClosed()) {
				logger.info("关闭connection2222222222222222222222222222222222222222222222");
				conn.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("关闭数据库出错!", se);
		}
	}

	

	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午01:02:42
	 * @方法名 executeUpdate
	 * @param sql
	 * @return int
	 * @功能如下 更新后，默认提交
	 */
	public int executeUpdate(String sql) {
		int iresult = -1;
		iresult = executeUpdate(sql, true);
		return iresult;
	}

	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午01:02:31
	 * @方法名 executeUpdate
	 * @param sql
	 * @param bCommit
	 * @return int
	 * @功能如下 更新，根据bCommit来决定是否直接提交
	 */
	private int executeUpdate(String sql, boolean bCommit) {
		int iresult = -1;
		try {
			if (conn == null) {
				init();
			}
			logger.info("执行sql语句如下" + sql);
			iresult = stmt.executeUpdate(sql);
			conn.commit();
			//if (bCommit) {
			//	conn.commit();
			//}
			return iresult;
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("DBManager.executeUpdate():更新数据出错:\n" + sql, se);
			try {
				conn.rollback();
				return iresult;
			} catch (Exception sqle) {
				return iresult;
			}

		}

	}


	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午01:01:58
	 * @方法名 addBatch
	 * @param sql
	 *            
	 * @功能如下 加入批处理
	 */
	public void addBatch(String sql) {
		try {

			if (null == conn) {
				init();
			}
			logger.info("加入了批处理sql语句如下" + sql);
			stmt.addBatch(sql);
		} catch (SQLException se) {
			logger.error("DBManager.addBatch() 出现错误!" + sql, se);
		}
	}
	

	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午01:01:44
	 * @方法名 executeBatch
	 * @return int[]
	 * @功能如下 执行批处理
	 */
	public int[] executeBatch() {
		int[] intlist = null;
		
		try {
			if (null == conn) {
				init();
			}

			intlist = stmt.executeBatch();
			conn.commit();
			stmt.clearBatch();
			return intlist;
		} catch (BatchUpdateException bse) {
			logger.error("至少有一条SQL语句错误", bse);
			return intlist;
		} catch (SQLException se) {
			logger.error("Error in DBManager.executeBatch()!", se);
			return intlist;
		}
	}

	
	/**
	 * 
	 * @author tiger
	 * @date 2010-8-26 下午12:43:40
	 * @param list
	 *            list中每个节点是一条sql语句
	 * @return boolean
	 * @Description: 批量执行更新类sql语句(insert delete update)
	 */
	public boolean excuteBatchSql(List<String> list) {
		if (list == null) {
			return false;
		}
		try {
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					addBatch(list.get(i));
				}
				int[] iResult = null;
				iResult = executeBatch();
				if (iResult != null) {
					String str = iResult.toString();
					if (str.indexOf(-1) >= 0) {
						return false;
					}
				} else {
					return false;
				}

			}
		} catch (Exception e) {
			logger.error("批量执行sql出现问题\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午01:02:56
	 * @方法名 executeQuery
	 * @param sql
	 * @return ResultSet
	 * @功能如下 执行查询，返回ResultSet
	 */
	public ResultSet executeQuery(String sql) {
		
		if (conn == null) {
			init();
		}
			logger.info("执行sql语句如下" + sql);
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("Error in DBManager.executeQuery(),SQL=\n" + sql);
		}

		return rs;
	}

	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午12:39:28
	 * @方法名 executeQueryOne
	 * @param sql
	 * @return
	 * @throws SQLException
	 *             Hashtable
	 * @功能如下 执行单条查询，sql语句必须保证只返回一条记录， 用于更新时提取该条记录信息
	 */
	public HashMap executeQueryHashMap(String sql) throws SQLException {
		HashMap hm = new HashMap();
		try {
			logger.info("执行sql如下：" + sql);
			rs = executeQuery(sql);
			// 取得列名,
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= numCols; i++) {
					String key = rsmd.getColumnName(i);
					String value = rs.getString(i);
					if (value == null) {
						value = "";
					}
					hm.put(key, value);
				}
			}
		} catch (SQLException e) {
			logger.error("执行sql出现问题：" + sql + e);
			return null;

		}
		return hm;
	}

	/**
	 * 
	 * @author tiger
	 * @创建时间 2010-8-24 上午12:58:26
	 * @方法名 executeQueryListHashMap
	 * @param sql
	 * @return
	 * @throws SQLException
	 *             List
	 * @功能如下 执行数据库查询语句返回向量类型的数据,向量的每一个值是哈希表 每个哈希表代表一条记录
	 */
	public List executeQueryListHashMap(String sql) throws SQLException {
		ResultSetMetaData rsmd = null;
		List list = new ArrayList();
		int columnCount = 0;
		try {
			logger.info("执行sql如下：" + sql);
			rs = executeQuery(sql);
			if (rs == null) {
				return null;
			}
			rsmd = rs.getMetaData();
			if (rsmd == null) {
				return null;
			}
			columnCount = rsmd.getColumnCount(); // 得到字段数量
			if (columnCount == 0) {
				return null;
			}

			// 向量的第一个值是列名集合
			String[] keys = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				keys[i - 1] = rsmd.getColumnName(i); // 获得字段名
			}
			// vct.add(keys);
			while (rs.next()) {
				HashMap hm = new HashMap();
				hm.clear();
				for (int i = 1; i <= columnCount; i++) {
					String result = rs.getString(i);
					if ((result == null) || (result.length() == 0)) {
						result = "";
					}
					hm.put(keys[i - 1], result); // 将每条记录保存到一个哈希表中，key为字段名，result为值
				}
				list.add(hm); // 将数据集的每一行插入向量
			}
		} catch (SQLException e) {
			logger.error("执行sql出现问题：" + sql + e);
			return null;
		}
		return list; // 返回SQL语言的查询结果集。
	}
	
	/**
	 * 根据sql 统计查询数据记录的总数，
	 * sql 中必须有count(*) 如果没有则直接返回0
	 * @方法名  executeQuery
	 * @param sql
	 * @return ResultSet
	 * @功能如下 执行查询，返回ResultSet
	 */
	public int executeQueryCount(String sql) {
		int i=0;
		
		if(sql.indexOf("count(*)")>0)
		{//如果sql 中没有count(*) 则不做统计
		try {
			if (conn == null) {
				this.init();
			}
			
			rs = stmt.executeQuery(sql);
			if(rs.next())
				i=rs.getInt(1);
			
			
			
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("Error in DBManager.executeQueryCount(),SQL=\n" + sql);
		}
		}else
		{
			
			logger.error("sql 的数据格式不对 DBManager.executeQueryCount(),SQL=\n" + sql);
		}
		
		return i;
	}


	/**
	 * 
	 * @Datetime 2013-2-27下午08:53:01
	 * @Description: 使用dbutils包来实现
	 * @param cl 类的.class
	 * @param sql 查询sql语句
	 * @return  List 每个节点表示一个对象
	 */
	public List getObjectList(Class cl,String sql){
		 List list = null;
		 try {
			ResultSet rs = executeQuery(sql);
			 if(rs!=null) {
			 ResultSetHandler<List<Object>> rsh = new BeanListHandler<Object>(cl);
			 list = new ArrayList<Object>();
			 list = (List<Object>)rsh.handle(rs);
			 }
		} catch (SQLException e) {
			logger.error("获取失败！"+e.getMessage());
		} 
		return list;
	}
	
	/**
	 * 
	 * @Datetime 2013-2-27下午09:03:33
	 * @Description: 使用dbutils包来实现
	 * @param cl Bean.class
	 * @param sql 查询语句
	 * @return  Object 返回bean
	 */
	public Object getObject(Class cl,String sql){
		Object obj=null;
		 try {
			ResultSet rs = executeQuery(sql);
			 if(rs!=null) {
			 ResultSetHandler<Object> rsh = new BeanHandler<Object>(cl);
			 obj= rsh.handle(rs);
			 }
		} catch (SQLException e) {
			logger.error("获取失败！");
		}		
		return obj;
	}
	
	

	/**
	 * 
	 * @author tiger
	 * @date 2012-9-26 下午02:05:28
	 * @param obj
	 * @param className
	 * @return boolean
	 * @Description: 根据实体类和实体类的类名插入一条记录
	 */
	public boolean insertObject(Object obj,String tableName){
		String sql = "insert into " + tableName + "(";
		String columns = "";
		String values = "values(";
		// 获得对应类的所有属性名称
		Field[] fs = obj.getClass().getDeclaredFields();
		// 根据属性获得对应的get方法，然后执行get方法获得实体类成员变量的值
		for (int i = 0; i < fs.length; i++) {
			PropertyDescriptor pd = null;
			try {
				pd = new PropertyDescriptor(fs[i].getName(), obj.getClass());
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Method method = pd.getReadMethod();

			if (i == fs.length - 1)
				columns = columns + pd.getName() + ")";
			else
				columns = columns + pd.getName() + ",";

			Object retObj = null;
			try {
				retObj = method.invoke(obj);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pd.getPropertyType().getSimpleName().equals("String")) {
				retObj = "'" + retObj + "'";
			}
			if (i == fs.length - 1)
				values = values + retObj + ")";
			else
				values = values + retObj + ",";
		}
		sql = sql + columns + values;
		System.out.println(sql);
		if (executeUpdate(sql, false) > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @author tiger
	 * @date 2012-9-26 下午02:05:28
	 * @param obj
	 * @return boolean
	 * @Description: 根据实体类插入一条记录,必须保持实体类和表名是一样的，表名必须是小写的
	 */
	public boolean insertObject(Object obj){
		String tableName = obj.getClass().getSimpleName().toLowerCase();
		String sql = "insert into " + tableName + "(";
		String columns = "";
		String values = "values(";
		// 获得对应类的所有属性名称
		Field[] fs = obj.getClass().getDeclaredFields();
		// 根据属性获得对应的get方法，然后执行get方法获得实体类成员变量的值
		for (int i = 0; i < fs.length; i++) {
			PropertyDescriptor pd = null;
			try {
				pd = new PropertyDescriptor(fs[i].getName(), obj.getClass());
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Method method = pd.getReadMethod();

			if (i == fs.length - 1)
				columns = columns + pd.getName() + ")";
			else
				columns = columns + pd.getName() + ",";

			Object retObj = null;
			try {
				retObj = method.invoke(obj);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pd.getPropertyType().getSimpleName().equals("String")) {
				retObj = "'" + retObj + "'";
			}
			if (i == fs.length - 1)
				values = values + retObj + ")";
			else
				values = values + retObj + ",";
		}
		sql = sql + columns + values;
		System.out.println(sql);
		if (executeUpdate(sql, false) > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 根据实体类中的id删除一条记录
	 * @param obj
	 * @return
	 */
	public boolean deleteObject(Object obj){
		String tableName = obj.getClass().getSimpleName().toLowerCase();
		String sql = "delete from " + tableName ;
		String idvalue="";
		// 获得对应类的所有属性名称
		Field[] fs = obj.getClass().getDeclaredFields();
		// 根据属性获得对应的get方法，然后执行get方法获得实体类成员变量的值
		for (int i = 0; i < fs.length; i++) {
			PropertyDescriptor pd = null;
			try {
				pd = new PropertyDescriptor(fs[i].getName(), obj.getClass());
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Method method = pd.getReadMethod();

			Object retObj = null;
			try {
				retObj = method.invoke(obj);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pd.getPropertyType().getSimpleName().equals("String")) {
				retObj = "'" + retObj + "'";
			}
			
			if (pd.getName().equals("id")){
				idvalue =(String) retObj; 
			}
		}
		sql = sql +"  where id="+idvalue;
		System.out.println(sql);
		if (executeUpdate(sql, false) > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 根据实体类更新表记录，表中必须有id字段
	 * @param obj
	 * @return
	 */
	public boolean updateObject(Object obj){
		String tableName = obj.getClass().getSimpleName().toLowerCase();
		String sql = "update " + tableName + "  set ";
		String sets="";
		String idvalue="";
		// 获得对应类的所有属性名称
		Field[] fs = obj.getClass().getDeclaredFields();
		// 根据属性获得对应的get方法，然后执行get方法获得实体类成员变量的值
		for (int i = 0; i < fs.length; i++) {
			PropertyDescriptor pd = null;
			try {
				pd = new PropertyDescriptor(fs[i].getName(), obj.getClass());
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Method method = pd.getReadMethod();

			Object retObj = null;
			try {
				retObj = method.invoke(obj);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (pd.getPropertyType().getSimpleName().equals("String")) {
				retObj = "'" + retObj + "'";
			}
			if (i == fs.length - 1)
				sets = sets+pd.getName()+"="+retObj;
			else
				sets = sets+pd.getName()+"="+retObj+",";
			
			if (pd.getName().equals("id")){
				idvalue =(String) retObj; 
			}
		}
		sql = sql + sets+"  where id="+idvalue;
		System.out.println(sql);
		if (executeUpdate(sql, false) > 0)
			return true;
		else
			return false;
	}

	/**
	 * 根据类名和select类型的sql语句返回对象列表,每个对象代表一条记录
	 * 
	 * @param className
	 * @param selectSQL
	 * @return className对应的对象列表用List保存
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	private List<Object> getObjectList(String className, String selectSQL) throws IntrospectionException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SQLException, InvocationTargetException {
		List resultList = new ArrayList();
		// 获得类
		Class objClass = Class.forName(className);

		// 获得对应类的所有属性名称
		Field[] fs = objClass.getDeclaredFields();

		// 获得sql对应的数据HashMap
		rs = executeQuery(selectSQL);

		// 得到类的实例
		Object obj = objClass.newInstance();

		// 根据属性获得对应的set方法，然后执行set方法进行属性的赋值
		while (rs.next()) {
			for (int i = 0; i < fs.length; i++) {
				PropertyDescriptor pd = new PropertyDescriptor(fs[i].getName(), objClass);
				Method method = pd.getWriteMethod();
				System.out.println(fs[i].getType() + "   " + fs[i].getName() + "   " + rs.getObject(fs[i].getName()));
				method.invoke(obj, rs.getObject(fs[i].getName()));
			}
			resultList.add(obj);
		}
		return resultList;
	}

	/**
	 * 
	 * @author litao
	 * @创建时间 2010-3-11 下午02:31:52
	 * @方法名 upCaseFirstChar
	 * @param str
	 * @return String
	 * @功能如下 把字符串的首字母变大写
	 */
	private String upCaseFirstChar(String str) {
		String upStr = str;
		String firstStr = upStr.substring(0, 1);
		String upfirstStr = firstStr.toUpperCase();
		upStr = upStr.replaceFirst(firstStr, upfirstStr);
		return upStr;
	}

	/**
	 * 
	 * @author tiger
	 * @date 2012-9-26 下午01:48:35
	 * @return
	 * @throws SQLException
	 *             String
	 * @Description: 生成库中的所有表对应的实体类
	 */
	public String getBeanFromTable(String dbName,String packName) throws SQLException {
		init();
		String Result = null;
		ResultSet rs = null;
		ResultSet rss = null;
		DatabaseMetaData dm = null;// 数据库表结构数据
		String[] pram = new String[1];
		List<String> tables = new ArrayList<String>();

		dm = conn.getMetaData();

		rs = dm.getTables(null, null, "jbpm_timelog_flow", null);

		while (rs.next()) {
			tables.add(rs.getString(3));
		}

		rs.close();

		for (String tb : tables) {
			rs = dm.getColumns(null, dbName, tb, "%");
			rss = dm.getColumns(null, dbName, tb, "%");
			// 查找当前表的字段
			System.out.println("\n当前表名为: " + tb);
			System.out.println("\n生成的文件名为： " + this.upCaseFirstChar(tb) + ".java");
			Result = "package "+packName+";";
			Result = Result + "\npublic class " + this.upCaseFirstChar(tb) + " {";
			int len = 0, type;
			String colType = "";
			while (rs.next()) {

				switch (rs.getInt("DATA_TYPE")) {
				case Types.INTEGER:
					colType = "Integer";
					break;
				case Types.CHAR:
				case Types.VARCHAR:
					colType = "String";
					break;

				}
				Result = Result + "\n    private " + colType + " " + rs.getString("COLUMN_NAME") + ";//"+ rs.getString("REMARKS");
			}
			Result = Result + "\n";
			while (rss.next()) {
				switch (rss.getInt("DATA_TYPE")) {
				case Types.INTEGER:
					colType = "Integer";
					break;
				case Types.CHAR:
				case Types.VARCHAR:
					colType = "String";
					break;

				}
				Result = Result + "\n    /** "+rss.getString("REMARKS")+" */";
				Result = Result + "\n    public " + colType + " get" + upCaseFirstChar(rss.getString("COLUMN_NAME")) + "() {";
				Result = Result + "\n        return " + rss.getString("COLUMN_NAME") + ";";
				Result = Result + "\n    }";
				Result = Result + "\n    /** "+rss.getString("REMARKS")+" */";
				Result = Result + "\n    public void set" + upCaseFirstChar(rss.getString("COLUMN_NAME")) + "(" + colType + " " + rss.getString("COLUMN_NAME") + ") {";
				Result = Result + "\n        this." + rss.getString("COLUMN_NAME") + " = " + rss.getString("COLUMN_NAME") + ";";
				Result = Result + "\n    }";
			}
			Result = Result + "\n}";
			String filename = this.upCaseFirstChar(tb);
			this.fileSC(filename, Result);
			System.out.println(Result);

		}
		rs.close();
		rss.close();
		return "结束了。。。。。。。。";
	}
	
	private void fileSC(String filename,String cont){
		String filepath = "D://model/"+filename+ ".java";
		File file = new File(filepath);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
			osw.write(cont);
			osw.flush();
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   
    
	public PageModel getObjectByPage(String sql,Class cl,int pageIndex,int pageSize){
    	PageModel model=new PageModel();
    	int count = this.executeQueryCount("select count(*) from ("+sql+") t");
		int total = count % pageSize == 0 ? count /pageSize : count / pageSize + 1;
		model.setPerPage(pageSize);
		model.setTotalPage(total);
		model.setTotalRecord(count);
		PageFactory pageFactory = new PageFactory();
		String querySql = pageFactory.createPageSQL(sql, model);
		pageFactory = null;
		List list=this.getObjectList(cl, querySql);
		model.setList(list);
    	return model;
    }
	/**
	 * 
	 * 测试方法
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 */
	public static void main(String arg[]) throws SQLException, IllegalArgumentException, IntrospectionException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {

		DBManager db = new DBManager();
//		String sql = "select * from tsuser where loginname='zuoxi1'";
//		Tsuser tsuser = (Tsuser) db.getObject(Tsuser.class, sql);
//		//tsuser.setUsername("坐席1");
//		//tsuser.setId(CommUtil.getID());
//		db.deleteObject(tsuser);
		
		db.getBeanFromTable("zc", "com.dhcc.modal.sxydidc");
		// String selectSQL="select * from system_user where user_id='admin' and
		// password='21232F297A57A5A743894A0E4A801FC3' ";
		// SysUser sysUser = (SysUser) db.getObject("SysUser", selectSQL);
		// System.out.println(sysUser);

		// db.getDaoClassStr();
/*		Flow flow = new Flow();
		flow.setID(CommUtil.getID());
		flow.setFlowVersion("1.0");
		flow.setFlowAuthor("李涛");
		flow.setFlowCatalogID("223292ersdsf");
		flow.setFlowName("我的流程");
		flow.setFlowRemarks("流程的备注");
		flow.setFlowStatus(12);
		db.insertObject(flow);
		System.out.println("===================");*/
		//flow = (Flow) db.getObject(Flow.class.getName(), "select * from flow");
		//List list = db.getObjectList(Flow.class.getName(), "select * from flow");
		//flow = (Flow) list.get(list.size() - 1);
		//System.out.println(flow.getID());
		db.close();
	}
}
