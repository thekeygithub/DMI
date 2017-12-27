package com.aghit.task.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.aghit.task.common.entity.CpaSubTask;
import com.aghit.task.common.entity.Task;
import com.aghit.task.common.entity.TqLog;

public class TaskMgr extends Manager {

	private Logger log = Logger.getLogger(TaskMgr.class);

	private static TaskMgr taskMgr = new TaskMgr();

	public ConcurrentHashMap<String, ConcurrentHashMap<String, Task>> tasks = new ConcurrentHashMap<String, ConcurrentHashMap<String, Task>>();

	private TaskMgr() {
	}

	public static TaskMgr getInstance() {
		return taskMgr;
	}

	/**
	 * 增加一个Task（此对象包含表AG_TASKQUEUE的字段），这个task是通用的task 确保两步骤在同一事务中
	 * 
	 * @param Task
	 * @throws SQLException
	 * @throws SQLException
	 */
	public void addTask(Task task) throws SQLException {
		Connection conn = this.getJdbcService().getJdbcTemplate()
				.getDataSource().getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;
		long idNum = 0;
		try {
			conn.setAutoCommit(false);

			// 查询AG_TASKQUEUE的下一个序列号
			String idSql = "SELECT SEQ_AG_TASKQUEUE.nextval FROM dual";
			ps = conn.prepareStatement(idSql);
			rs = ps.executeQuery();
			if (rs.next()) {
				idNum = rs.getLong(1);
			}

			// 1.先增加至表AG_TASKQUEUE，对于未明确的字段可为暂为空
			String agTasksql = "insert into AG_TASKQUEUE values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			ps = conn.prepareStatement(agTasksql);

			ps.setLong(1, idNum);
			ps.setInt(2, task.getAtType());
			ps.setString(3, task.getCuser());
			ps.setTimestamp(4, new Timestamp(task.getCTime().getTime()));
			ps.setTimestamp(5, new Timestamp(task.getAtStime().getTime()));
			ps.setTimestamp(6, new Timestamp(task.getAtEtime().getTime()));
			ps.setLong(7, task.getAtState());
			ps.setString(8, task.getAtLog());
			ps.setLong(9, task.getAtRerun());
			ps.setString(10, task.getAtReuser());
			ps.setLong(11, task.getAtStop());
			ps.setString(12, task.getAtScause());
			ps.setString(13, task.getAtSuser());
			ps.setString(14, task.getAtRunserver());

			ps.execute();

			task.setAtId(idNum);

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			log.error("添加Task错误" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				if (!conn.getAutoCommit()) {
					conn.setAutoCommit(true);
				}
				conn.close();
			}
		}
	}

	/**
	 * 增加一个CpaSubTask（此对象包含表AG_TASKQUEUE、表CPA_TASKQUEUE的字段） 确保两步骤在同一事务中
	 * 
	 * @param cpaSubTask
	 * @throws SQLException
	 */
	public void addCpaSubTask(CpaSubTask cpaSubTask) throws SQLException {
		Connection conn = this.getJdbcService().getJdbcTemplate()
				.getDataSource().getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;
		long idNum = 0;
		try {
			conn.setAutoCommit(false);

			// 查询AG_TASKQUEUE的下一个序列号
			String idSql = "SELECT SEQ_AG_TASKQUEUE.nextval FROM dual";
			ps = conn.prepareStatement(idSql);
			rs = ps.executeQuery();
			if (rs.next()) {
				idNum = rs.getLong(1);
			}

			// 1.先增加至表AG_TASKQUEUE，对于未明确的字段可为暂为空
			String agTasksql = "insert into AG_TASKQUEUE values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			ps = conn.prepareStatement(agTasksql);

			ps.setLong(1, idNum);
			ps.setInt(2, cpaSubTask.getAtType());
			ps.setString(3, cpaSubTask.getCuser());
			ps.setTimestamp(4, new Timestamp(cpaSubTask.getCTime().getTime()));
			ps
					.setTimestamp(5, new Timestamp(cpaSubTask.getAtStime()
							.getTime()));
			ps
					.setTimestamp(6, new Timestamp(cpaSubTask.getAtEtime()
							.getTime()));
			ps.setInt(7, cpaSubTask.getAtState());
			ps.setString(8, cpaSubTask.getAtLog());
			ps.setInt(9, cpaSubTask.getAtRerun());
			ps.setString(10, cpaSubTask.getAtReuser());
			ps.setInt(11, cpaSubTask.getAtStop());
			ps.setString(12, cpaSubTask.getAtScause());
			ps.setString(13, cpaSubTask.getAtSuser());
			ps.setString(14, cpaSubTask.getAtRunserver());

			ps.execute();

			ps.clearParameters();
			ps = null;

			// 2.获得AT_ID后再增加到CPA_TASKQUEUE
			String cpaTaskSql = "insert into CPA_TASKQUEUE values (?, ?, ?, ?, ?, ?, ?, ?)";

			ps = conn.prepareStatement(cpaTaskSql);

			ps.setLong(1, idNum);
			ps.setLong(2, cpaSubTask.getScenarioId());
			ps.setString(3, cpaSubTask.getExeStartDT());
			ps.setString(4, cpaSubTask.getExeEndDT());
			ps.setLong(5, cpaSubTask.getHandleStartNum());
			ps.setLong(6, cpaSubTask.getHandleEndNum());
			ps.setString(7, cpaSubTask.getTaskType());
			ps.setLong(8, cpaSubTask.getLogId());

			ps.execute();

			cpaSubTask.setAtId(idNum);
			cpaSubTask.setCpaAtId(idNum);

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			log.error("添加Task错误," + e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				if (!conn.getAutoCommit()) {
					conn.setAutoCommit(true);
				}
				conn.close();
			}
		}
	}

	/**
	 * 增加一个TqLog对象
	 * 
	 * @param tqLog
	 * @throws Exception
	 */
	public void addTqLog(TqLog tqLog) throws Exception {

		// 1.保存tgLog对象
		String sql = new String(
				"insert into AG_TQLOG values (SEQ_AG_TQLOG.nextval, ?, ?, ?, ?)");

		this.getJdbcService().update(
				sql,
				new Object[] { tqLog.getAtId(), tqLog.getAtRunserver(),
						tqLog.getCuser(), tqLog.getCtime() });
	}

	/**
	 * 更新状态
	 * 
	 * @param Task
	 * @throws Exception
	 */
	public void updateState(Task Task) throws Exception {
		String sql = "update AG_TASKQUEUE set AT_STATE = ? where AT_ID = ?";
		this.getJdbcService().update(sql,
				new Object[] { Task.getAtState(), Task.getAtId() });
	}

	/**
	 * 更新任务开始时间、运行状态、运行的server这三个字段
	 * 
	 * @param Task
	 * @throws Exception
	 */
	public void updateStartTime(Task Task) throws Exception {
		String sql = "update AG_TASKQUEUE set AT_STIME = ?, AT_STATE = ?, AT_RUNSERVER = ? where AT_ID = ?";
		this.getJdbcService().update(
				sql,
				new Object[] { Task.getAtStime(), Task.getAtState(),
						Task.getAtRunserver(), Task.getAtId() });
	}

	/**
	 * 更新任务结束时间、运行状态这二个字段
	 * 
	 * @param Task
	 * @throws Exception
	 */
	public void updateEndTime(Task Task) throws Exception {
		String sql = "update AG_TASKQUEUE set AT_ETIME = ?, AT_STATE = ? where AT_ID = ?";
		this.getJdbcService().update(
				sql,
				new Object[] { Task.getAtEtime(), Task.getAtState(),
						Task.getAtId() });
	}

	/**
	 * 更新运行状态、运行日志这二个字段
	 * 
	 * @param Task
	 * @throws Exception
	 */
	public void updateStateLog(Task Task) throws Exception {
		String sql = "update AG_TASKQUEUE set AT_STATE = ?, AT_LOG = ? where AT_ID = ?";
		this.getJdbcService().update(
				sql,
				new Object[] { Task.getAtState(), Task.getAtLog(),
						Task.getAtId() });
	}

	/**
	 * 根据cpa任务的日志id，任务状态查询一组任务。AG_TASKQUEUE和CPA_TASKQUEUE中的数据
	 * 
	 * @param logId
	 * @param atState
	 * @return
	 * @throws Exception
	 */
	public List<CpaSubTask> queryAllTask(long logId, int atState)
			throws Exception {
		StringBuffer sql = new StringBuffer(
				"select ct.at_id as id, ct.*, agt.*")
				.append(" from CPA_TASKQUEUE ct, AG_TASKQUEUE agt")
				.append(
						" where ct.log_id = ? and agt.at_state = ? and ct.at_id = agt.at_id");

		List<CpaSubTask> dataList = null;
		SqlRowSet srs = (SqlRowSet) this.getJdbcService().findByQuery(
				sql.toString(), new Object[] { logId, atState });
		if (srs != null) {
			dataList = new ArrayList<CpaSubTask>();
			while (srs.next()) {
				CpaSubTask cst = new CpaSubTask();

				cst.setCpaAtId(srs.getLong("ID"));
				cst.setScenarioId(srs.getLong("SCENARIO_ID"));
				cst.setExeStartDT(srs.getString("EXE_START_DT"));
				cst.setExeEndDT(srs.getString("EXE_END_DT"));
				cst.setHandleStartNum(srs.getLong("HANDLE_STARTNUM"));
				cst.setHandleEndNum(srs.getLong("HANDLE_ENDNUM"));
				cst.setTaskType(srs.getString("TASK_TYPE"));
				cst.setLogId(srs.getLong("LOG_ID"));

				cst.setAtId(srs.getLong("ID"));
				cst.setAtType(srs.getInt("AT_TYPE"));
				cst.setCuser(srs.getString("CUSER"));
				cst.setCTime(srs.getTimestamp("CTIME"));
				cst.setAtStime(srs.getTimestamp("AT_STIME"));
				cst.setAtEtime(srs.getTimestamp("AT_ETIME"));
				cst.setAtState(srs.getInt("AT_STATE"));
				cst.setAtLog(srs.getString("AT_LOG"));
				cst.setAtRerun(srs.getInt("AT_RERUN"));
				cst.setAtReuser(srs.getString("AT_REUSER"));
				cst.setAtStop(srs.getInt("AT_STOP"));
				cst.setAtScause(srs.getString("AT_SCAUSE"));
				cst.setAtSuser(srs.getString("AT_SUSER"));
				cst.setAtRunserver(srs.getString("AT_RUNSERVER"));

				dataList.add(cst);
			}
		}

		return dataList;
	}

	public CpaSubTask getCpaTask(long taskId) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select ct.at_id as id, ct.*, agt.*").append(
				" from CPA_TASKQUEUE ct, AG_TASKQUEUE agt").append(
				" where ct.at_id = ?  and ct.at_id = agt.at_id");

		CpaSubTask cst = null;
		try {
			SqlRowSet srs = (SqlRowSet) this.getJdbcService().findByQuery(
					sql.toString(), new Object[] { taskId });

			if (srs != null) {
				while (srs.next()) {
					cst = new CpaSubTask();

					cst.setCpaAtId(srs.getLong("ID"));
					cst.setScenarioId(srs.getLong("SCENARIO_ID"));
					cst.setExeStartDT(srs.getString("EXE_START_DT"));
					cst.setExeEndDT(srs.getString("EXE_END_DT"));
					cst.setHandleStartNum(srs.getLong("HANDLE_STARTNUM"));
					cst.setHandleEndNum(srs.getLong("HANDLE_ENDNUM"));
					cst.setTaskType(srs.getString("TASK_TYPE"));
					cst.setLogId(srs.getLong("LOG_ID"));

					cst.setAtId(srs.getLong("ID"));
					cst.setAtType(srs.getInt("AT_TYPE"));
					cst.setCuser(srs.getString("CUSER"));
					cst.setCTime(srs.getTimestamp("CTIME"));
					cst.setAtStime(srs.getTimestamp("AT_STIME"));
					cst.setAtEtime(srs.getTimestamp("AT_ETIME"));
					cst.setAtState(srs.getInt("AT_STATE"));
					cst.setAtLog(srs.getString("AT_LOG"));
					cst.setAtRerun(srs.getInt("AT_RERUN"));
					cst.setAtReuser(srs.getString("AT_REUSER"));
					cst.setAtStop(srs.getInt("AT_STOP"));
					cst.setAtScause(srs.getString("AT_SCAUSE"));
					cst.setAtSuser(srs.getString("AT_SUSER"));
					cst.setAtRunserver(srs.getString("AT_RUNSERVER"));
				}
			}
		} catch (Exception e1) {
			throw new Exception("查询CPA任务失败," + e1.getMessage());
		}

		return cst;
	}

	/**
	 * 根据任务状态和任务类型查询任务信息，AG_TASKQUEUE中的数据
	 * 
	 * @param atState
	 * @param atType
	 * @return
	 * @throws Exception
	 */
	public List<Task> getTask(int atState, int atType) throws Exception {
		String sql = "select * from AG_TASKQUEUE where AT_STATE = ? and AT_TYPE = ?";
		SqlRowSet srs = (SqlRowSet) this.getJdbcService().findByQuery(
				sql.toString(), new Object[] { atState, atType });
		List<Task> dataList = null;
		if (srs != null) {
			dataList = new ArrayList<Task>();
			while (srs.next()) {
				Task task = new Task();

				task.setAtId(srs.getLong("AT_ID"));
				task.setAtType(srs.getInt("AT_TYPE"));
				task.setCuser(srs.getString("CUSER"));
				task.setCTime(srs.getTimestamp("CTIME"));
				task.setAtStime(srs.getTimestamp("AT_STIME"));
				task.setAtEtime(srs.getTimestamp("AT_ETIME"));
				task.setAtState(srs.getInt("AT_STATE"));
				task.setAtLog(srs.getString("AT_LOG"));
				task.setAtRerun(srs.getInt("AT_RERUN"));
				task.setAtReuser(srs.getString("AT_REUSER"));
				task.setAtStop(srs.getInt("AT_STOP"));
				task.setAtScause(srs.getString("AT_SCAUSE"));
				task.setAtSuser(srs.getString("AT_SUSER"));
				task.setAtRunserver(srs.getString("AT_RUNSERVER"));

				dataList.add(task);
			}
		}

		return dataList;
	}
	
	/**
	 * 记录任务运行结果和最终状态
	 */
	public void updateSubTaskState(long taskNo, int atState, String atLog){
		String sql = "update AG_TASKQUEUE set AT_STATE = ?, AT_LOG = ?,AT_ETIME =? where AT_ID = ?";
		try {
			this.getJdbcService().update(sql, new Object[] {atState, atLog,new Date(),taskNo});
		} catch (Exception e) {
			log.error("执行记录任务运行结果和最终状态失败！" + e.getMessage());
		}
	}
}
