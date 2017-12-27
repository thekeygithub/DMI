package com.dhcc.common.system.area;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.CommDao;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsarea;
import com.dhcc.modal.system.Tspost;
import com.opensymphony.xwork2.ActionContext;




public class AreaDao {
	private static final Logger logger=Logger.getLogger(AreaDao.class);
	
	 /**
     * @param sname
     * @param pm
     */
	public PageModel postQueryList(PageModel pm,String sortname,String sortorder){
		DBManager dbm = new DBManager();
		try {
			String querysql = "select tp.id,tp.areaname,tp.remark,tcp.corpname as topcorpid  from tsarea tp left join tscorp tcp on tcp.id = tp.topcorpid ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			boolean type = userid.equals(superUserId);
			if(type){//数据权限控制
				querysql += " where 1 = 1 ";
			}else{
				String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
				querysql += " where 1 = 1 and tp.topcorpid='"+topcorpid+"' ";
			}
			querysql += " order by tp."+sortname+" "+sortorder+" ";
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
			
			List<Tsarea> list = new ArrayList<Tsarea>();
			List<Tsarea> list1= dbm.getObjectList(Tsarea.class, sql);
			if(type){
				Tsarea modelnew;
				for(Tsarea model:list1){
					modelnew = new Tsarea();
					modelnew.setId(model.getId());
					modelnew.setAreaname(model.getAreaname()+"("+model.getTopcorpid()+")");
					modelnew.setRemark(model.getRemark());
					list.add(modelnew);
				}
			}else{
				list = list1;
			}
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询岗位列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		return pm;
	}
	
	 
    /**
     * 添加一个新的所属区域信息
     * @param menu
     * @return 执行结果
     */
    public  boolean  areaAdd(Tsarea model){
    	DBManager dbm = new DBManager();
    	boolean b = true;
		try {
			b = dbm.insertObject(model, "tsarea");
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("添加", "添加ID为"+model.getId()+"的所属区域信息");
			saveLog = null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			dbm.close();
		}
    	return b;
    }
	
    /*
     * 删除所属区域信息
     */
	public String areaDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		SaveLog saveLog = new SaveLog();
		try {
			for (String id : ids) {
				sql = "delete from tsarea where id='" + id + "'";
				dbm.addBatch(sql);
				saveLog.saveLog("删除所属区域信息", "删除ID为"+id+"的所属区域记录信息！");
			}
			dbm.executeBatch();
			saveLog = null;
		} catch (Exception e) {
			logger.error("所属区域信息信息删除--失败",e);
			e.printStackTrace();
			return "error";
		} finally {
			dbm.close();
		}
		return "success";
		
	}
	/**
     * 根据id得到单个信息
     * @param sql 查询的sql语句
     * @return 
     */
    public Tsarea areaQueryById(String id){
    	DBManager dbm=new DBManager();
    	String sql="select * from tsarea where id='"+id+"'";
    	Tsarea model=null;
		try {
			model = (Tsarea) dbm.getObject(Tsarea.class, sql);
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
    	   dbm.close();
		}
    	return model;
    }
    
    
    /**
	 * 
	 * @param model
	 * @return
	 */
	public int areaModify(Tsarea model){
		DBManager dbm = new DBManager();
		String sql = "";
	    int i = 0;
		try {
			sql = "update tsarea set areaname='" + model.getAreaname()
				+ "',remark='" + model.getRemark() + "' where id='"
				+ model.getId() + "'";
			i = dbm.executeUpdate(sql);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("修改", "修改ID为"+model.getId()+"的所属区域记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.info(e);
		}finally{
			dbm.close();
		}
		return i;
	}
	
}
