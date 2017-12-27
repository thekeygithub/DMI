package com.dhcc.common.system.post;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.CommDao;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tspost;
import com.opensymphony.xwork2.ActionContext;




public class PostDao {
	private static final Logger logger=Logger.getLogger(PostDao.class);
	
	 /**
     * @param sname
     * @param pm
     */
	public PageModel postQueryList(PageModel pm,String queryname,String sortname,String sortorder){
		DBManager dbm = new DBManager();
		try {
			String querysql = "select tp.id,tp.postname,tp.remark,tp.ordernum,tcp.corpname as topcorpid  from tspost tp left join tscorp tcp on tcp.id = tp.topcorpid ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			boolean type = userid.equals(superUserId);
			if(type){//数据权限控制
				querysql += " where 1 = 1 ";
			}else{
				String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
				querysql += " where tp.topcorpid='"+topcorpid+"' ";
			}
			if(!StringUtil.isNullOrEmpty(queryname)){
				String nameall = queryname.trim();
				String [] names =  nameall.trim().split(" ");
				if(names.length>1){
					querysql += " and ";
					for(int i=0;i<names.length;i++){
						if(!StringUtil.isNullOrEmpty(names[i])){
							querysql += " tp.postname like '%"+names[i]+"%' ";
							if(i<names.length-2){
								querysql += " and ";
							}
						}
						
					}
					querysql += " or ";
					for(int i=0;i<names.length;i++){
						if(!StringUtil.isNullOrEmpty(names[i])){
							querysql += " tp.ordernum like '%"+names[i]+"%' ";
							if(i<names.length-2){
								querysql += " and ";
							}
						}
						
					}
					querysql += " or ";
					for(int i=0;i<names.length;i++){
						if(!StringUtil.isNullOrEmpty(names[i])){
							querysql += " tp.remark like '%"+names[i]+"%' ";
							if(i<names.length-2){
								querysql += " and ";
							}
						}
						
					}
				}else{
					querysql += " and tp.postname like '%"+nameall+"%' or tp.remark like '%"+nameall+"%' or tp.ordernum like '%"+nameall+"%' ";
				}
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
			List<Tspost> list = new ArrayList<Tspost>();
			List<Tspost> list1= dbm.getObjectList(Tspost.class, sql);
			if(type){
				Tspost modelnew;
				for(Tspost model:list1){
					modelnew = new Tspost();
					modelnew.setId(model.getId());
					modelnew.setOrdernum(model.getOrdernum());
					modelnew.setPostname(model.getPostname()+"("+model.getTopcorpid()+")");
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
     * 添加一个新的职务信息
     * @param menu
     * @return 执行结果
     */
    public  boolean  postAdd(Tspost model){
    	DBManager dbm = new DBManager();
    	boolean b = true;
		try {
			b = dbm.insertObject(model, "tspost");
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("添加", "添加ID为"+model.getId()+"的职务信息");
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
     * 删除职务信息
     */
	public String postDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		SaveLog saveLog = new SaveLog();
		try {
			for (String id : ids) {
				sql = "delete from tspost where id='" + id + "'";
				dbm.addBatch(sql);
				sql = "delete from tsluserpost where postid='" + id + "'";
				dbm.addBatch(sql);
				saveLog.saveLog("删除职务信息", "删除ID为"+id+"的职务记录信息！");
			}
			dbm.executeBatch();
			saveLog = null;
		} catch (Exception e) {
			logger.error("职务信息信息删除--失败",e);
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
    public Tspost postQueryById(String id){
    	DBManager dbm=new DBManager();
    	String sql="select * from tspost where id='"+id+"'";
    	Tspost model=null;
		try {
			model = (Tspost) dbm.getObject(Tspost.class, sql);
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
	public int postModify(Tspost model){
		DBManager dbm = new DBManager();
		String sql = "";
	    int i = 0;
		try {
			sql = "update tspost set postname='" + model.getPostname()+ "',ordernum='" + model.getOrdernum()
				+ "',remark='" + model.getRemark() + "' where id='"
				+ model.getId() + "'";
			i = dbm.executeUpdate(sql);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("修改", "修改ID为"+model.getId()+"的职务记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.info(e);
		}finally{
			dbm.close();
		}
		return i;
	}
	
}
