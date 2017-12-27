package com.dhcc.common.system.station;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.system.user.UserQueryModel;
import com.dhcc.common.util.CommDao;
import com.dhcc.modal.system.CommModel;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tspost;
import com.dhcc.modal.system.Tsstation;
import com.opensymphony.xwork2.ActionContext;




public class StationDao {
	private static final Logger logger=Logger.getLogger(StationDao.class);
	
	 /**
	  * 岗位列表查询 
	  * 带有分页
     */
	public PageModel stationQueryList(PageModel pm,String sortname,String sortorder){
		DBManager dbm=new DBManager();
		try {
			String querysql = "select DISTINCT tp.id,tp.stationname,tp.remark,tcp.corpname as topcorpid  from tsstation tp left join tscorp tcp on tcp.id = tp.topcorpid ";
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
			int count =dbm.executeQueryCount(countsql);
			int total = count % pm.getPerPage() == 0 ? count /pm.getPerPage() : count / pm.getPerPage() + 1;
			pm.setTotalPage(total);
			pm.setTotalRecord(count);
			/**
			 * 分页sql构造
			 */
			PageFactory pageFactory = new PageFactory();
			String sql = pageFactory.createPageSQL(querysql, pm);
			pageFactory = null;
			
			List<Tsstation> list = new ArrayList<Tsstation>();
			List<Tsstation> list1= dbm.getObjectList(Tsstation.class, sql);
			if(type){
				Tsstation modelnew;
				for(Tsstation model:list1){
					modelnew = new Tsstation();
					modelnew.setId(model.getId());
					modelnew.setStationname(model.getStationname()+"("+model.getTopcorpid()+")");
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
     * 添加一个新的岗位信息
     * @作者 SZ
     * @日期 2014 10:59:31 AM
     * @param modal
     * @return
     */
    public  boolean  stationAdd(Tsstation modal){
    	DBManager dbm=new DBManager();
    	boolean b=false;
		try {
			b = dbm.insertObject(modal, "tsstation");
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("添加岗位信息", "添加ID为"+ modal.getId() +"的岗位记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.error(e);
		}finally{
    	  dbm.close();
		}
    	return b;
    }
    
	public String stationDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		SaveLog saveLog = new SaveLog();
		try {
			for (String id : ids) {
				sql = "delete from tsstation where id='" + id + "'";
				dbm.addBatch(sql);
				sql = "delete from tsluserstation where stationid='" + id + "'";
				dbm.addBatch(sql);
				saveLog.saveLog("删除岗位信息", "删除ID为"+ id +"的岗位记录信息！");
			}
			dbm.executeBatch();
			saveLog = null;
		} catch (Exception e) {
			logger.error("岗位信息删除--失败",e);
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
    public Tsstation stationQueryById(String id){
    	DBManager dbm=new DBManager();
    	String sql="select * from tsstation where id='"+id+"'";
    	Tsstation model=null;
		try {
			model = (Tsstation) dbm.getObject(Tsstation.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
    	   dbm.close();
		}
    	return model;
    }
    
    
    /**
	 * 
	 * @param model
	 *  岗位信息修改
	 */
	public int stationModify(Tsstation model){
		DBManager dbm=new DBManager();
		String sql = "";
	    int i = 0;
		try {
			sql = "update tsstation set stationname='" + model.getStationname()
				+ "',remark='" + model.getRemark() + "' where id='"
				+ model.getId() + "'";
			i = dbm.executeUpdate(sql);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("修改岗位信息", "修改ID为"+ model.getId() +"的岗位记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.info(e);
		}finally{
			dbm.close();
		}
		return i;
		
	}
	
	/**
	 * 得到所有的岗位的json数据
	 */
	public  JSONArray  getAllStation(){
		DBManager dbm=new DBManager();
		List<Tsstation> list = new ArrayList<Tsstation>();
		List<CommModel> list1 = new ArrayList<CommModel>();
		try {
			 list = dbm.getObjectList(Tsstation.class, "select * from tsstation");
		} catch (Exception e) {
			logger.error("查询所有的岗位出错！", e);
		}finally{
			dbm.close();
		}
		CommModel model = null;
		CommModel model1 = new CommModel();
		model1.setId("-1");
		model1.setText("--请选择--");
		list1.add(model1);
		for(int i=0;i<list.size();i++){
			model = new CommModel();
			model.setId(list.get(i).getId());
			list1.add(model);
		}
		
		JSONArray json = JSONArray.fromObject(list1);
		return json;
	}
	/**
	 * 删除职务、岗位、角色时查看是否有人员分配该信息
	 * @作者 SZ
	 * @日期 2014 1:45:54 PM
	 * @param type station岗位、role角色、post职务、area所属区域
	 * @return
	 */
	public boolean QueryIsDelete(String type,String[] ids){
		DBManager dbm = new DBManager();
		String sql = null;
		for(String id : ids){
			if("post".equals(type)){
				sql = "select userid from tsluserpost where postid = '"+id+"'";
			}else if("station".equals(type)){
				sql = "select userid from tsluserstation where stationid = '"+id+"'";
			}else if("role".equals(type)){
				sql = "select userid from tsluserrole where roleid = '"+id+"'";
			}else if("area".equals(type)){
				sql = "select id as userid from tsuser where area = '"+id+"'";
			}
			Tspost model = (Tspost)dbm.getObject(Tspost.class, sql);
			if(null!=model){
				return false;
			}
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	public List queryStationHaveUserList(String stationid){
		DBManager dbm = new DBManager();
		List<UserQueryModel> list = new ArrayList<UserQueryModel>();
		String querysql = "SELECT * FROM tsuser tu LEFT JOIN tsluserstation tus ON tus.userid=tu.id WHERE tus.stationid='"+stationid+"'";
		list = dbm.getObjectList(UserQueryModel.class,querysql);
		System.out.println(list);
		dbm.close();
		return list;
	}
	public void stationDelUser(String stationid,String[] ids){
		DBManager dbm = new DBManager();
		String sql;
		for(String userid : ids){
			sql = "DELETE FROM tsluserstation WHERE userid='"+userid+"' AND stationid = '"+stationid+"'";
			dbm.addBatch(sql);
		}
		dbm.executeBatch();
		dbm.close();
	}
}
