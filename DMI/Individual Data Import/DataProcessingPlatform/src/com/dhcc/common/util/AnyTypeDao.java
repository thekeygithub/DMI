package com.dhcc.common.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.LogDao;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.modal.system.PageModel;
import com.opensymphony.xwork2.ActionContext;



public class AnyTypeDao<Anytype,AnytypeMore> {
	private static final Logger logger = Logger.getLogger(AnyTypeDao.class);
	/**
	 * @描述：列表查询方法
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:15:29
	 * @param pm
	 * @param anytypeMore
	 * @param querySql
	 * @return
	 */
	public PageModel QueryList(PageModel pm,AnytypeMore anytypeMore,String querySql){
		DBManager dbm=new DBManager();
		try {
			String countsql = "select count(*) from (" + querySql + ") t";
			int count = dbm.executeQueryCount(countsql);
			int total = count % pm.getPerPage() == 0 ? count /pm.getPerPage() : count / pm.getPerPage() + 1;
			pm.setTotalPage(total);
			pm.setTotalRecord(count);
			/**
			 * 分页sql构造
			 */
			PageFactory pageFactory = new PageFactory();
			String sql = pageFactory.createPageSQL(querySql, pm);
			pageFactory = null;
			List<AnytypeMore> list = dbm.getObjectList(anytypeMore.getClass(), sql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--QueryList--失败"+e.getMessage());
		}finally{
			dbm.close();
		}
		
		return pm;
	}
	/**
	 * @描述：添加方法
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:15:33
	 * @param obj
	 * @param sysLogTitle 日志名称
	 * @param sysLogContent 日志内容
	 * @return
	 */
	public boolean Add(Object obj,String sysLogTitle,String sysLogContent){
	    DBManager dbm=new DBManager();
	    boolean result = false;
	    try {
    		result = dbm.insertObject(obj);//基本信息入库
    		SaveLog saveLog = new SaveLog();
			saveLog.saveLog(sysLogTitle, sysLogContent);
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			logger.error("--add--"+sysLogTitle+"失败"+e.getMessage());
		}finally{
			dbm.close();
		}
		return result;
	}
	/**
	 * @描述：修改方法
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:15:46
	 * @param obj
	 * @param sysLogTitle 日志名称
	 * @param sysLogContent 日志内容
	 * @return
	 */
	public boolean Update(Object obj,String sysLogTitle,String sysLogContent){
		DBManager dbm = new DBManager();
		boolean result = false;
		try {
			result = dbm.updateObject(obj);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog(sysLogTitle, sysLogContent);
		} catch (Exception e) {
			result = false;
			logger.error("--update--"+sysLogTitle+"失败"+e.getMessage());
		} finally {
			dbm.close();
		}
		return result;
	}
	/**
	 * @描述：查询方法
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:15:50
	 * @param anyTypeMore
	 * @param sql
	 * @return
	 */
	public AnytypeMore QueryById(AnytypeMore anyTypeMore,String sql){
		DBManager dbm = new DBManager();
		AnytypeMore anyTypemodel = null;
		try {
			anyTypemodel = (AnytypeMore) dbm.getObject(anyTypeMore.getClass(), sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--QueryById--失败"+e.getMessage());
			return null;
		}finally{
    	   dbm.close();
		}
		return anyTypemodel;
	}
	/**
	 * @描述：删除方法
	 * @作者：SZ
	 * @时间：2014-11-18 下午03:15:54
	 * @param ids
	 * @param tableName
	 * @param sysLogTitle 日志名称
	 * @param sysLogContent 日志内容
	 * @return
	 */
	public boolean Del(String[] ids,String tableName,String sysLogTitle){
		DBManager dbm = new DBManager();
		String sql="";
		String sysLogsql="";
		LogDao logdao = new LogDao();
		HttpServletRequest request = ServletActionContext.getRequest();
		String userid = (String)ActionContext.getContext().getSession().get("userid");
		String createtime = DateUtil.toTime() + "";//当前时间
	    try {
			for (int i = 0; i < ids.length; i++) {
				if (!StringUtil.isNullOrEmpty(ids[i])) {
					sql = "delete from "+tableName+" where id='" + ids[i] + "'";
					dbm.addBatch(sql);
					sysLogsql = String.format("insert into tslog(id,userid,logdate,ipaddress,otherMessage,action,message) " +
							" values('%s','%s','%s','%s','%s','%s','%s')",
							CommUtil.getID(),userid,createtime,logdao.getIpAddress(request),logdao.getOtherMessage(request),sysLogTitle,"删除了一条ID为"+ids[i]+"的记录");
					dbm.addBatch(sysLogsql);
				}
			}
			dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("--Del--"+sysLogTitle+"失败"+e.getMessage());
			return false;
		}finally{
			dbm.close();
		}
		return true;
	}
}
