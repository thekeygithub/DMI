package com.aghit.task.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.aghit.task.common.entity.CpaTask;

public class CpaTaskMgr extends Manager{
	
	private Logger log = Logger.getLogger(CpaTaskMgr.class);
	
	private static CpaTaskMgr taskMgr = new CpaTaskMgr();
	
	private final String tableName = "CPA_SCENARIO_EXELOG"; 
	
	private CpaTaskMgr(){
	}
	
	public static CpaTaskMgr getInstance(){
		return taskMgr;
	}
	
	/**
	 * 根据taskId查询大任务的信息
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public CpaTask getTaskById(long taskId) throws Exception{
		
		String sql = "select * from "+ tableName +" where LOG_ID = ?";
		
		return this.getJdbcService().queryForObject(sql, new Object[] {taskId}, new taskMapper());
	}
	
	
	/**
	 * 修改大任务执行日志的结束时间和执行结果
	 * @param exeEndDT
	 * @param exeResult
	 * @throws Exception 
	 */
	public void updateTaskInfo(String taskId, Date exeEndDT, String exeResult) throws Exception{
		
		if(StringUtils.isBlank(taskId)){
			log.error("taskId为空");
			throw new RuntimeException("taskId为空");
		}
		
		String sql = "update " + tableName + " set EXE_END_DT = ?, EXE_RESULT = ? where LOG_ID = ?";
		this.getJdbcService().update(sql, new Object[]{exeEndDT, exeResult, Long.parseLong(taskId)});
	}
	
	
	
	private static final class taskMapper implements RowMapper<CpaTask> {

	    public CpaTask mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	CpaTask task = new CpaTask();
	       
	    	task.setLogId(rs.getLong("LOG_ID"));
	    	task.setScenarioId(rs.getLong("SCENARIO_ID"));
	    	task.setExeType(rs.getString("EXE_TYPE"));
	    	task.setBatchCode(rs.getString("BATCH_CODE"));
	    	task.setTaskStart(rs.getTimestamp("SCENARIO_START_DT"));
	    	task.setTaskEnd(rs.getTimestamp("SCENARIO_END_DT"));
	    	task.setHandleCount(rs.getLong("HANDLE_COUNT"));
	    	task.setExeResult(rs.getString("EXE_RESULT"));
	    	task.setExeUserId(rs.getLong("EXE_USER_ID"));
	    	task.setCrtUserId(rs.getLong("CRT_USER_ID"));
	    	task.setCrtDTTM(rs.getTimestamp("CRT_DTTM"));
	    	task.setMemo(rs.getString("MEMO"));
	    	task.setDataSrcId(rs.getLong("DATA_SRC_ID"));
	    	task.setClmCount(rs.getLong("CLM_COUNT"));
	    	
	        return task;
	    }
	}
}
