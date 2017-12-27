package com.framework.db.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.framework.db.utils.PageQueryUtil;
import com.framework.pager.PaginationSupport;

/**
 * @author wangxiao
 */
public class JdbcOracleService implements JdbcService {
	
	private JdbcTemplate jdbcTemplate;
	
	private LobHandler lobHandler;
	
	/**
	 * 分页查询，根据页号查询
	 * @param <T> 
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageNo 当前页号
	 * @param rm VO对象
	 * @return PaginationSupport
	 * @throws Exception
	 *  (non-Javadoc)
	 * @see com.framework.db.jdbc.JdbcService#findPageByQuery(java.lang.String, java.lang.Object[], int, org.springframework.jdbc.core.RowMapper)
	 */
	@Override
	public <T> PaginationSupport<T> findPageByQuery(String sql, Object[] args, int pageNo, RowMapper<T> rm) throws Exception {
		PaginationSupport<T> pageInfo = new PaginationSupport<T>();
		pageInfo.setCurrentPage(pageNo);
		this.findPageByQuery(sql, args, pageInfo, rm);
		return pageInfo;
	}
	
	@Override
	public <T> PaginationSupport<T> findPageByQuery(String sql, Object[] args, int pageNo, Class<T> cl)
			throws Exception {
		return this.findPageByQuery(sql, args, pageNo, new BeanPropertyRowMapper<T>(cl));
	}

	/**
	 *  分页查询，根据页号与每页记录数查询
	 * @param <T>
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageSize 设置的每页显示记录数
	 * @param pageNo 当前页号
	 * @param rm VO对象
	 * @return PaginationSupport
	 * @throws Exception
	 *  (non-Javadoc)
	 * @see com.framework.db.jdbc.JdbcService#findPageByQuery(java.lang.String, java.lang.Object[], int, int, org.springframework.jdbc.core.RowMapper)
	 */
	@Override
	public <T> PaginationSupport<T> findPageByQuery(String sql, Object[] args, int pageSize, int pageNo, RowMapper<T> rm) throws Exception {
		PaginationSupport<T> pageInfo = new PaginationSupport<T>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNo);
		this.findPageByQuery(sql, args, pageInfo, rm);
		return pageInfo;
	}

	/**
	 * 分页查询结果PaginationSupport items属性为List VO封装
	 * @param <T>
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageInfo 分页对象
	 * @param rm VO对象
	 * @throws Exception
	 */
	@Override
	public <T> void findPageByQuery(String sql, Object[] args, PaginationSupport<T> pageInfo, RowMapper<T> rm) throws Exception {
		List<T> ls = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				String cSql = PageQueryUtil.pageQuerySqlCount(sql);
				int rowCount = this.getJdbcTemplate().queryForInt(cSql, args);
				if (pageInfo.getStartIndex() >= rowCount) {
					int count = rowCount / pageInfo.getPageSize();
					if (rowCount % pageInfo.getPageSize() > 0)
						count++;
					pageInfo.setCurrentPage(count);
				}

				if (rowCount > 0) {
					String sSql = PageQueryUtil.pageQuerySql(sql, pageInfo.getStartIndex(), pageInfo.getStartIndex() + pageInfo.getPageSize());
					ls = this.getJdbcTemplate().query(sSql, args, rm);
				}

				pageInfo.setTotalCount(rowCount);
				pageInfo.setItems(ls);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 分页查询结果PaginationSupport items属性为List map封装
	 * @param <T>
	 * @param sql sql 查询SQL
	 * @param args 对应的参数
	 * @param pageInfo 分页对象
	 * @throws Exception
	 */
	@Override
	public void findPageByQuery(String sql, Object[] args, PaginationSupport<Map<String, Object>> pageInfo) throws Exception {
		List<Map<String, Object>> ls = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				String cSql = PageQueryUtil.pageQuerySqlCount(sql);
				int rowCount = this.getJdbcTemplate().queryForInt(cSql, args);
				if (pageInfo.getStartIndex() >= rowCount) {
					int count = rowCount / pageInfo.getPageSize();
					if (rowCount % pageInfo.getPageSize() > 0)
						count++;
					pageInfo.setCurrentPage(count);
				}

				if (rowCount > 0) {
					String sSql = PageQueryUtil.pageQuerySql(sql, pageInfo.getStartIndex(), pageInfo.getStartIndex() + pageInfo.getPageSize());
					ls = this.getJdbcTemplate().queryForList(sSql, args);
				}

				pageInfo.setTotalCount(rowCount);
				pageInfo.setItems(ls);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
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
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void findPageByQuery(String sql, Object[] args, PaginationSupport pageInfo, Class cl) throws Exception {
		List ls = null;
		try {
			if(StringUtils.isNotEmpty(sql)) {
				String cSql = PageQueryUtil.pageQuerySqlCount(sql);
				int rowCount = this.getJdbcTemplate().queryForInt(cSql, args);
				if(pageInfo.getStartIndex() >= rowCount) {
					int count = rowCount / pageInfo.getPageSize();
		    		if (rowCount % pageInfo.getPageSize() > 0)
		    			count++;
		    		pageInfo.setCurrentPage(count);
				}
				
				QueryRunner run = new QueryRunner(this.getJdbcTemplate().getDataSource());
				ResultSetHandler h = new BeanListHandler(cl);
				if(rowCount > 0) {
					String sSql = PageQueryUtil.pageQuerySql(sql, pageInfo.getStartIndex(), pageInfo.getStartIndex() + pageInfo.getPageSize());
					ls = (List) run.query(sSql, h, args);
				}
				
				pageInfo.setTotalCount(rowCount);
				pageInfo.setItems(ls);
			}
		} catch(Exception e) {
			throw e;
		}
	}

	/**
	 * 根据SQL 查询唯一记录，如果查不到记录返回NULL，如果查到多条报异常
	 * @param sql 查询SQL
	 * @param args 对应的参数
	 * @param rm VO映射的对象
	 * @return the single mapped object
	 * @throws Exception
	 */
	@Override
	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rm) throws Exception {
		T obj = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				obj = this.getJdbcTemplate().queryForObject(sql, args, rm);
			}
		} catch (EmptyResultDataAccessException e) {
			// 处理结果数为零的情况
			obj = null;
		} catch (IncorrectResultSizeDataAccessException e) {
			// 处理查询的记录不唯一的情况
			throw new Exception(e.getMessage() + " : queryForObject : more than 1 rows");
		} catch (Exception e) {
			throw e;
		}
		return obj;
	}

	/**
	 * query map
	 * @param sql
	 * @param args
	 * @return Map<String,Object> the result Map (one entry for each column, using the column name as the key)
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> queryForMap(String sql, Object[] args) throws Exception {
		Map<String, Object> m = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				m = this.getJdbcTemplate().queryForMap(sql, args);
			}
		} catch (EmptyResultDataAccessException e) {
			// 处理结果数为零的情况
			m = null;
		} catch (IncorrectResultSizeDataAccessException e) {
			// 处理查询的记录不唯一的情况
			throw new Exception(e.getMessage() + " : queryForMap : query does not return exactly one row");
		} catch (Exception e) {
			throw e;
		}
		return m;
	}

	/**
	 * query list element map
	 * @param sql
	 * @param args
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> findForList(String sql, Object[] args) throws Exception {
		return this.getJdbcTemplate().queryForList(sql, args);
	}

	/**
	 * 根据SQL查询，返回结果集List。
	 * List节点是VO
	 * @param <T>
	 * @param sql 查询SQL
	 * @param args 对应的参数
	 * @param rm VO映射的对象
	 * @return List
	 * @throws Exception
	 */
	@Override
	public <T> List<T> findListByQuery(String sql, Object[] args, RowMapper<T> rm) throws Exception {
		List<T> ls = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				ls = this.getJdbcTemplate().query(sql, args, rm);
			}
		} catch (Exception e) {
			throw e;
		}
		return ls;
	}

	/**
	 * SQL 字段必须与BEAN属性名称一致
	 * @param sql
	 * @param args
	 * @param cl
	 * @return List<T>
	 * @throws Exception
	 */
	@Override
	public <T> List<T> findListByQueryMapper(String sql, Object[] args, Class<T> cl) throws Exception {
		return this.findListByQuery(sql, args, new BeanPropertyRowMapper<T>(cl));
	}

	/**
	 * 返回SqlRowSet 可自行处理结果集
	 * @param sql 查询SQL
	 * @param args 对应的参数
	 * @return SqlRowSet
	 * @throws Exception
	 */
	@Override
	public SqlRowSet findByQuery(String sql, Object[] args) throws Exception {
		SqlRowSet srs = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				srs = this.getJdbcTemplate().queryForRowSet(sql, args);
			}
		} catch (Exception e) {
			throw e;
		}
		return srs;
	}
	
	/**
	 * query by sql
	 * Mapping :
	 * Blob : Blob
	 * Clob : String / Clob
	 * Date: Date / String
	 * @param sql
	 * @param args example: Object[] {obj, obja}
	 * @param cl Entity.class example: Test.class
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List findListByQuery(String sql, Object[] args, Class cl) throws Exception {
		List ls = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				QueryRunner run = new QueryRunner(this.getJdbcTemplate().getDataSource());
				ResultSetHandler h = new BeanListHandler(cl);
				ls = (List) run.query(sql, h, args);
			}
		} catch (Exception e) {
			throw e;
		}
		return ls;
	}
	
	/**
	 * 根据SQL 和 对应的参数 执行insert、update、delete、procedure
	 * example call procedure： sql = "call procename(?, ?)" 
	 * 							args = new Object[] {obj, obja}
	 * @param sql 可以是SQL 或 call procedure
	 * @param args 对应的参数
	 * @return int
	 * @throws Exception
	 */
	@Override
	public int update(String sql, Object[] args) throws Exception {
		int i = 0;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				i = this.getJdbcTemplate().update(sql, args);
			}
		} catch (Exception e) {
			throw e;
		}
		return i;
	}
	
	/**
	 * 批量更新数据
	 * @param sql
	 * @param List<Object[]> ls element 参数数组
	 *  (non-Javadoc)
	 * @see com.isoftskill.framework.db.jdbc.JdbcService#executeBatch(java.lang.String, java.util.List)
	 */
	@Override
	public int[] updateBatch(String sql, final List<Object[]> ls) throws Exception {
		int[] rows = null;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				rows = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

					public int getBatchSize() {
						int size = 0;
						if (ls != null)
							size = ls.size();
						return size;
					}

					public void setValues(PreparedStatement ps, int i) throws SQLException {
						if (i < ls.size()) {
							Object[] objs = ls.get(i);
							for (int j = 0; j < objs.length; j++) {
								ps.setObject(j + 1, objs[j]);
							}
						}
					}

				});
			}
		} catch (Exception e) {
			throw e;
		}
		return rows;
	}

	/**
	 * 根据SQL 来执行操作，可执行DDL 语句，如建表语句。
	 * @param sql
	 * @throws Exception
	 */
	@Override
	public void execute(String sql) throws Exception {
		try {
			if (StringUtils.isNotEmpty(sql)) {
				this.getJdbcTemplate().execute(sql);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 到LOB 数据保存，ORACLE数据库,BLOB 用byte[]存储
	 * @param sql
	 * @param List<Object[]> ls
	 * @throws Exception
	 */
	@Override
	public void executeOracleLob(String sql, final List<Object[]> ls) throws Exception {
		try {
			if (StringUtils.isNotEmpty(sql)) {
				if (ls != null && ls.size() > 0) {
					for (int i = 0; i < ls.size(); i++) {
						Object[] obj = ls.get(i);
						this.executeOracleLob(sql, obj);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * LOB 数据保存，ORACLE数据库,BLOB 参数 用byte[]存储
	 * @param sql 执行的SQL
	 * @param args 参数数组
	 *  (non-Javadoc)
	 * @see com.isoftskill.framework.db.jdbc.JdbcService#executeOracleLob(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void executeOracleLob(String sql, final Object[] args) throws Exception {
		try {
			if (StringUtils.isNotEmpty(sql)) {
				this.getJdbcTemplate().execute(sql, new AbstractLobCreatingPreparedStatementCallback(this.lobHandler) {
					protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
						if (args != null && args.length > 0) {
							for (int i = 0; i < args.length; i++) {
								if (args[i] == null) {
									args[i] = "";
									ps.setObject(i + 1, args[i]);
								} else if (args[i] instanceof byte[]) {
									lobCreator.setBlobAsBytes(ps, i + 1, (byte[]) args[i]);
								} else {
									ps.setObject(i + 1, args[i]);
								}
							}
						}
					}
				});
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 独立事务处理，在整体事务之外。
	 * @param sql
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@Override
	public int noTransUpdate(String sql, Object[] args) throws Exception {
		int i = 0;
		try {
			if (StringUtils.isNotEmpty(sql)) {
				i = this.getJdbcTemplate().update(sql, args);
			}
		} catch (Exception e) {
			throw e;
		}
		return i;
	}
	
	/** 
	 * 存储过程执行
	 * @param procName 存储过程名称
	 * @param inArgs 存储过程参数
	 * @param outArgs 返回的参数数量
	 * @return String[] 返回内容，字符串数组
	 * @throws Exception
	 * (non-Javadoc)
	 * @see com.framework.db.jdbc.JdbcService#updateProcedure(java.lang.String, java.lang.String[], int)
	 */
	@Override
	public String[] updateProcedure(final String procName, final String[] inArgs, final int outArgs) throws Exception {
		String[] sOutArgs = null;
		try {
			if (StringUtils.isNotEmpty(procName)) {
				final int argsL = inArgs.length;
				
				sOutArgs = (String[]) this.getJdbcTemplate().execute(new CallableStatementCreator() {
					public CallableStatement createCallableStatement(final Connection conn) throws SQLException {
						final StringBuilder storedProc = new StringBuilder("{call " + procName + "(");
						for (int i = 1; i <= argsL + outArgs; i++) {
							storedProc.append("?,");
						}
						String sProcName = StringUtils.substringBeforeLast(storedProc.toString(), ",") + ")}";
						final CallableStatement cs = conn.prepareCall(sProcName);
						for (int i = 1; i <= argsL; i++) {
							cs.setString(i, inArgs[i - 1]);
						}
						for (int i = 1; i <= outArgs; i++) {
							cs.registerOutParameter(i + argsL, OracleTypes.VARCHAR);
						}
						return cs;
					}
				}, new CallableStatementCallback<Object>() {
					public String[] doInCallableStatement(final CallableStatement cs) throws SQLException, DataAccessException {
						cs.execute();
						final String[] obs = new String[outArgs];
						for (int i = 1; i <= outArgs; i++) {
							obs[i - 1] = cs.getString(i + argsL);
						}
						return obs;
					}
				});
			}
		} catch (Exception e) {
			throw e;
		}
		return sOutArgs;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}
}