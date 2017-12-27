package com.framework.db.jdbc;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.framework.pager.PaginationSupport;

/**
 * @author wangxiao
 */
public interface JdbcService  {
	
	
	/**
	 * 分页查询，根据页号查询
	 * @param <T> 
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageNo 当前页号
	 * @param rm VO对象
	 * @return PaginationSupport
	 * @throws Exception
	 */
	public <T> PaginationSupport<T> findPageByQuery(String sql, Object[] args, int pageNo, RowMapper<T> rm) throws Exception;
	
	/**
	 * 分页查询，根据页号与每页记录数查询
	 * @param <T>
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageSize 设置的每页显示记录数
	 * @param pageNo 当前页号
	 * @param rm VO对象
	 * @return PaginationSupport
	 * @throws Exception
	 */
	public <T> PaginationSupport<T> findPageByQuery(String sql, Object[] args, int pageSize, int pageNo, RowMapper<T> rm) throws Exception;
	
	
	/**
	 * 分页查询结果PaginationSupport items属性为List VO封装
	 * @param <T>
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageInfo 分页对象
	 * @param rm VO对象
	 * @throws Exception
	 */
	public <T> void findPageByQuery(String sql, Object[] args, PaginationSupport<T> pageInfo, RowMapper<T> rm) throws Exception;
	
	
	/**
	 * 分页查询，根据页号查询
	 * @param <T> 
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageNo 当前页号
	 * @param cl VO class对象
	 * @return PaginationSupport
	 * @throws Exception
	 */
	public <T> PaginationSupport<T> findPageByQuery(String sql, Object[] args, int pageNo, Class<T> cl) throws Exception;
	
	/**
	 * 分页查询结果PaginationSupport items属性为List map封装
	 * @param <T>
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageInfo 分页对象
	 * @return PaginationSupport
	 * @throws Exception
	 */
	public void findPageByQuery(String sql, Object[] args, PaginationSupport<Map<String, Object>> pageInfo) throws Exception;
	
	/**
	 * page by query
	 * Mapping :
	 * Blob : Blob
	 * Clob : String / Clob
	 * Date: Date / String
	 * @param sql 查询SQL
	 * @param args 对应的参数
	 * @param pageInfo 翻页信息
	 * @param cl VO映射的对象
	 * @return PaginationSupport<T>
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void findPageByQuery(String sql, Object[] args, PaginationSupport pageInfo, Class cl) throws Exception;

	/**
	 * 根据SQL 查询唯一记录，如果查不到记录返回NULL，如果查到多条报异常
	 * @param <T>
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param rm VO映射的对象
	 * @return T
	 * @throws Exception
	 */
	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rm) throws Exception;
	
	/**
	 * @param sql
	 * @param args
	 * @return the result Map (one entry for each column, using the column name as the key)
	 * @throws Exception
	 */
	public Map<String, Object> queryForMap(String sql, Object[] args) throws Exception;
	
	/**
	 * @param sql
	 * @param args
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public List<Map<String, Object>> findForList(String sql, Object[] args) throws Exception;

	/**
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param rm
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> findListByQuery(String sql, Object[] args, RowMapper<T> rm) throws Exception;
	
	/**
	 * @param sql
	 * @param args
	 * @param cl
	 * @return List<T>
	 * @throws Exception
	 */
	public <T> List<T> findListByQueryMapper(String sql, Object[] args, Class<T> cl) throws Exception;
	
	/**
	 * @param sql
	 * @param args
	 * @param cl
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List findListByQuery(String sql, Object[] args, Class cl) throws Exception;
	
	/**
	 * @param sql
	 * @param args
	 * @return SqlRowSet
	 * @throws Exception
	 */
	public SqlRowSet findByQuery(String sql, Object[] args) throws Exception;
	
	/**
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public int update(String sql, Object[] args) throws Exception;
	
	/**
	 * 批量更新数据
	 * @param sql
	 * @param ls
	 * @return int[]
	 * @throws Exception
	 */
	public int[] updateBatch(String sql, List<Object[]> ls) throws Exception;
	
	/**
	 * @param sql
	 * @throws Exception
	 */
	public void execute(String sql) throws Exception;
	
	/**
	 * @param sql
	 * @param ls
	 * @throws Exception
	 */
	public void executeOracleLob(String sql, final List<Object[]> ls) throws Exception;
	
	/**
	 * @param sql
	 * @param args
	 * @throws Exception
	 */
	public void executeOracleLob(String sql, final Object[] args) throws Exception;
	
	/**
	 * no transaction
	 * @param sql
	 * @throws Exception
	 */
	public int noTransUpdate(String sql, Object[] args) throws Exception;
	
	/**
	 * 存储过程执行
	 * @param procName 存储过程名称
	 * @param inArgs 存储过程参数
	 * @param outArgs 返回的参数数量
	 * @return String[] 返回内容，字符串数组
	 * @throws Exception
	 */
	public String[] updateProcedure(final String procName, final String[] inArgs, final int outArgs) throws Exception;
}