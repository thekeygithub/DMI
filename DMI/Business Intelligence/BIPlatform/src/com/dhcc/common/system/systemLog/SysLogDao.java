package com.dhcc.common.system.systemLog;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.util.CommDao;
import com.dhcc.common.util.DateUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsconfig;
import com.dhcc.modal.system.Tsrole;
import com.dhcc.modal.system.Tsuser;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author GYR
 *
 */
public class SysLogDao {
	
	private static final Logger logger = Logger.getLogger(SysLogDao.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * @param pm
	 * @param starttime 开始时间
	 * @param endtime  结束时间
	 */
	public PageModel sysLogQueryList(PageModel pm,String starttime,String endtime,String username){
		DBManager dbm = new DBManager();
		try {
			String querysql = "select lg.*,tu.username as username from tslog lg left join tsuser tu on tu.id = lg.userid ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				querysql += " where 1 = 1 ";
			}else{
				querysql += " where 1 = 1 and tu.id = '"+userid+"' ";
//				String temprole = this.userRoleQuery();//查询该用户所拥有的最高权限
//				if(temprole.equals("3")){
//					String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
//					querysql += " where 1 = 1 and tu.topcorpid='"+topcorpid+"' and tu.id = '"+userid+"' ";
//				}else{
//					String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
//					querysql += " where 1 = 1 and tu.topcorpid='"+topcorpid+"' and tu.id != '"+superUserId+"' ";
//				}
			}
			
			/**
			 *根据时间查询
			 */
			if(!StringUtil.isNullOrEmpty(starttime)){
				querysql = querysql + " and lg.logdate >= '"+DateUtil.toTime(starttime)+"'";
			}
			if(!StringUtil.isNullOrEmpty(endtime)){
				 long date = DateUtil.toTime(endtime) + 24*60*60*1000;
				 querysql = querysql + " and lg.logdate <= '"+ date +"'";
			}
			/**
			 * 根据操作的用户名查询
			 */
			if(!StringUtil.isNullOrEmpty(username)){
				querysql = querysql + " and tu.username like '%"+username+"%'";
			}
			querysql = querysql + " order by lg.logdate desc";
			String countsql = "select count(*) from (" + querysql + ") t";
			int count = dbm.executeQueryCount(countsql);
			int total = count % pm.getPerPage() == 0 ? count /pm.getPerPage() : count / pm.getPerPage() + 1;
			pm.setTotalPage(total);
			pm.setTotalRecord(count);
			/**
			 * 分页sql构造
			 */
			PageFactory pageFactory = new PageFactory();
			String sql = pageFactory.createPageSQL(querysql, pm);
			pageFactory = null;
			List list = dbm.getObjectList(TslogModel.class, sql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询日志列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		return pm;
	}
	
	/**
	 * 执行日志删除
	 * @param ids
	 */
	public String sysLogDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		try {
			for (String id : ids) {
				sql = "delete from tslog where id='" + id + "'";
				dbm.addBatch(sql);
			}
			dbm.executeBatch();
		} catch (Exception e) {
			logger.error("日志信息删除--失败",e);
			return "error";
		} finally {
			dbm.close();
		}
		return "success";
	}

}
