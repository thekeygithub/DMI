package com.dhcc.common.system.corp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.CommDao;
import com.dhcc.modal.system.CommModel;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.TreeModal;
import com.dhcc.modal.system.Tsconfig;
import com.dhcc.modal.system.Tscorp;
import com.opensymphony.xwork2.ActionContext;




public class CorpDao {
	private static final Logger logger = Logger.getLogger(CorpDao.class);
	
	 /**
     * @param sname
     * @param pm
     * @return
     */
	public PageModel corpQueryList(PageModel pm,String sortname,String sortorder){
		DBManager dbm=new DBManager();
		try {
			String querysql = "select * from tscorp ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				querysql += " where type='1' ";
			}else{
				String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
				querysql += " where type='1' and topcorpid='"+topcorpid+"' ";
			}
			querysql += " order by "+sortname+" "+sortorder+" ";
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
			List<Tscorp> list = dbm.getObjectList(Tscorp.class, sql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询公司列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		
		return pm;
		
	}
	
	/**
	 * @描述：单位中部门的列表查询
	 * @作者：SZ
	 * @时间：2014-10-14 上午09:54:19
	 * @param pm
	 * @return
	 */
	public PageModel deptQueryList(PageModel pm,String sortname,String sortorder){
		DBManager dbm=new DBManager();
		try {
			String querysql = "select * from tscorp ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				querysql += " where type='2' ";
			}else{
				String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
				querysql += " where type='2' and topcorpid='"+topcorpid+"' ";
			}
			querysql += " order by "+sortname+" "+sortorder+" ";
			String countsql = "select count(*) from (" + querysql + ") t";
			int count =dbm.executeQueryCount(countsql);
			int total = count % pm.getPerPage() == 0 ? count /pm.getPerPage() : count / pm.getPerPage() + 1;
			pm.setTotalPage(total);
			pm.setTotalRecord(count);
			String sql = querysql + " limit "+(pm.getCurrentPage()-1)*pm.getPerPage()+","+pm.getPerPage();
			List<Tscorp> list = dbm.getObjectList(Tscorp.class, sql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询公司列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		
		return pm;
		
	}
	
	 
    /**
     * 添加一个新的公司信息
     * @param menu
     * @return 执行结果
     */
    public  boolean  corpAdd(Tscorp model){
    	DBManager dbm=new DBManager();
    	boolean b=true;
		try {
			b = dbm.insertObject(model, "tscorp");
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("单位信息添加", "添加一条ID为"+model.getId()+"的记录！");
			saveLog = null;
		} catch (Exception e) {
			logger.error(e);
		}finally{
    	  dbm.close();
		}
    	return b;
    }
	
    
    /**
     * @param ids 公司id的集合
     * 公司信息删除
     * @return
     */
	public String corpDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		SaveLog saveLog = new SaveLog();
		try {
			for (String id : ids) {
				sql = "delete from tscorp where id='" + id + "'";
				dbm.addBatch(sql);
				sql = "delete from tslusercorp where corpid ='"+id+"'";
				dbm.addBatch(sql);
				saveLog.saveLog("单位信息删除", "删除了一条ID为"+id+"的单位记录！");
			}
			dbm.executeBatch();
		} catch (Exception e) {
			logger.error("公司信息删除--失败",e);
		} finally {
			dbm.close();
		}
		return "success";
	}
	/**
	 * @描述：判断所选部门是否可以删除。条件是有未选择的子节点就不能删除这些部门
	 * @作者：SZ
	 * @时间：2014-10-21 下午03:06:06
	 * @param ids 前台所选id的数组
	 * @return
	 */
	public boolean isDelcorp(String[] ids){
		DBManager dbm = new DBManager();
		boolean result = false;
		List<Tscorp> listcorp = dbm.getObjectList(Tscorp.class, "select * from tscorp where 1=1");
		dbm.close();
		Set<String> setlist = new HashSet<String>();
		for(String id : ids){
			setlist = havaCorpid(id, listcorp, setlist);//循环获取所选部门的所有子部门集合
		}
		if(ids.length==setlist.size()){//如果所选的部门id和其拥有的子部门id数目相同就视为可以删除
			result = true;
		}
		return result;
	}
	/**
	 * @描述：递归方法把给定部门的子部门都装到setlist里面（包括当前部门）
	 * @作者：SZ
	 * @时间：2014-10-21 下午03:08:00
	 * @param id 部门id
	 * @param listcorp 单位的list集合
	 * @param setlist 所有子部门的集合（包括当前部门）
	 * @return
	 */
	public static Set<String> havaCorpid(String id,List<Tscorp> listcorp,Set<String> setlist){
		int i = 0;
		String newid = "";
		setlist.add(id);
		for(Tscorp model : listcorp){
			if(id.equals(model.getPid())){
				setlist.add(model.getId());
				newid += model.getId()+",";
				i++;
			}
		}
		if(i==0){
			return setlist;
		}else{
			String[] ids = newid.split(",");
			for(String a : ids){
				return havaCorpid(a, listcorp, setlist);
			}
		}
		return setlist;
	}
	/**
     * 根据id得到单个信息
     * @param sql 查询的sql语句
     * @return 查询到的信息模型
     */
    public Tscorp corpQueryById(String id){
    	DBManager dbm = new DBManager();
    	String sql = "select * from tscorp where id='"+id+"'";
    	Tscorp model = null;
		try {
			model = (Tscorp) dbm.getObject(Tscorp.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
    	   dbm.close();
		}
    	return model;
    }
    
    
    /**
     * @param model 公司信息模型
     * 修改公司信息
     * @return 执行结果
     */
	public boolean corpModify(Tscorp model){
		DBManager dbm = new DBManager();
	    boolean b = false;
		try {
			b = dbm.updateObject(model);
		} catch (Exception e) {
			logger.info(e);
			return b;
		}finally{
			dbm.close();
		}
		SaveLog saveLog = new SaveLog();
		saveLog.saveLog("单位信息修改", "修改一条ID为"+model.getId()+"的记录！");
		saveLog = null;
		return b;
	}
	
	/**
	 * 查询所有顶级公司下的公司信息，组成下拉框所需的数据
	 */
	public  List  getAllCorp(){
		DBManager dbm = new DBManager();
		List<TreeModal> list = null;
		try {
			String querysql = "select id as id,corpname as text,pid as pid from tscorp ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				querysql += " where type='1' order by ordernum,corpname asc ";
			}else{
				String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
				querysql += " where type='1' and topcorpid='"+topcorpid+"' order by ordernum ";
			}
			list = dbm.getObjectList(TreeModal.class, querysql);
		} catch (Exception e) {
			logger.error("查询所有的公司出错！", e);
			return list;
		}finally{
			dbm.close();
		}
		return list;
	}
	
	/**
	 * @描述：单位树形排序（公司和部门的树形排序）
	 * @作者：SZ
	 * @时间：2014-10-14 下午12:41:25
	 * @return
	 */
	public  List<TreeModal>  QueryCropDeptAll(){
		DBManager dbm=new DBManager();
		List<TreeModal> list = null;
		Tsconfig tsconfigmodel = (Tsconfig)dbm.getObject(Tsconfig.class, "SELECT * FROM tsconfig WHERE dtype='SUPERUSERID' AND dkey='SUPERUSERID'");
		String querysql = "select id as id,pid as pid,corpname as text,type as url from tscorp ";
		String userid = (String)ActionContext.getContext().getSession().get("userid");
		if(tsconfigmodel.getDvalue().equals(userid)){//数据权限控制
			querysql += " where type !=0 ORDER BY ordernum,corpname asc ";
		}else{
			String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
			querysql += " where type !=0 and topcorpid='"+topcorpid+"' ORDER BY ordernum,corpname asc ";
		}
		
		try {
			list = dbm.getObjectList(TreeModal.class, querysql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
	/**
	 *@作者 GYR
	 *@日期 Oct 22, 2014 11:00:11 AM
	 *@描述 根据type和key查询信息
	 *@param dtype
	 *@param dkey
	 */
	public List<Tsconfig> ConfigQueryByType(String dtype){
		DBManager dbm=new DBManager();
    	String sql = "select * from tsconfig where dtype ='"+dtype+"'";
    	List<Tsconfig> list = null;
		try {
			list =  dbm.getObjectList(Tsconfig.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}finally{
    	   dbm.close();
		}
		return list;
	}
}
