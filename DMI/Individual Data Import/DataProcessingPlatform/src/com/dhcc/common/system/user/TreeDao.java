package com.dhcc.common.system.user;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.util.CommDao;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.TreeModal;
import com.dhcc.modal.system.Tscorp;
import com.dhcc.modal.system.Tsuser;
import com.opensymphony.xwork2.ActionContext;


/**
 * 树形结构Dao
 * @作者 SZ
 * @日期 2014 9:38:57 AM
 *
 */
public class TreeDao {
	private final static Logger logger=Logger.getLogger(TreeDao.class);

	/**
	 * @param pm 分页类
	 * @param corpid 树形结构查询人的部门或公司id
	 * @param ids 查询过滤 的人员的id字符串 用“，”分割
	 * @return
	 */
	public PageModel userQueryList(PageModel pm,String corpid,String username,String ids,String sortname,String sortorder,String showsubdivision){
		DBManager dbm=new DBManager();
		try {
			String querysql = "SELECT DISTINCT tu.* FROM tsuser tu LEFT JOIN tslusercorp tuc ON tuc.userid = tu.id ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				querysql += " where 1 = 1 ";
			}else{
				String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
				querysql += " where 1 = 1 and topcorpid='"+topcorpid+"' ";
			}
			if("true".equals(showsubdivision)){
				if(!StringUtil.isNullOrEmpty(corpid) && !corpid.equals("null")){
					List<Tscorp> listcorp = dbm.getObjectList(Tscorp.class, "select * from tscorp where 1=1");
					Set<String> setlist = havaCorpid(corpid, listcorp, new HashSet<String>());//循环获取所选部门的所有子部门集合
					Iterator<String> it = setlist.iterator();
					String corpids = "";
					int i = 0;
					while(it.hasNext()){
						corpids += "'"+it.next()+"'";
						if(i<setlist.size()-1){
							corpids +=",";
						}
						i++;
					}
					querysql += " and tuc.corpid in ("+corpids+") ";
				}
			}else{
				if(!StringUtil.isNullOrEmpty(corpid) && !corpid.equals("null")){
					querysql += " and tuc.corpid = '"+corpid+"' ";
				}
			}
			if(!StringUtil.isNullOrEmpty(username)){
				String queryname = username.trim();
				querysql += " and (tu.username like '%"+queryname+"%' or tu.loginname like '%"+queryname+"%' " +
						" or tu.mobileprivate like'%"+queryname+"%' or tu.emailprivate like '%"+queryname+"%' or tu.remark like '%"+queryname+"%') ";
			}
			
			if (!StringUtil.isNullOrEmpty(ids) && !ids.equals("null")) {
				String[] useridArr = ids.split(",");
				String temp = "";
				for(int i=0;i<useridArr.length;i++){
					temp = temp +"'"+ useridArr[i] + "'";
					if(i<useridArr.length-1){
						temp +=  ",";
					}
				}
				querysql += " and id not in (" + temp + ")";
			}
			
			querysql += " order by tu."+sortname+" "+sortorder+" ";
			
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
			List<Tsuser> list=dbm.getObjectList(Tsuser.class, sql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询组织架构的用户列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		
		return pm;
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
				if(i!=0){
					newid+=",";
				}
				newid += model.getId();
				i++;
			}
		}
		if(i==0){
			return setlist;
		}else{
			String[] ids = newid.split(",");
			for(String a : ids){
				havaCorpid(a, listcorp, setlist);
			}
		}
		return setlist;
	}
	/**
	 * 获取公司或部门下的岗位列表
	 * @作者 SZ
	 * @日期 2014 11:38:37 AM
	 * @return
	 */
	public List<TreeModal> queryStationByCDid(String corpid){
		DBManager dbm = new DBManager();
		List<TreeModal> listModel = null;
		try {
			String sql = "SELECT id as id,stationname as text,topcorpid as pid " +
					" FROM tsstation WHERE topcorpid='"+corpid+"'";
			listModel = dbm.getObjectList(TreeModal.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取公司或部门下的岗位列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		return listModel;
	}
	/**
	 * 获取公司或部门下的职务列表
	 * @作者 SZ
	 * @日期 2014 12:12:43 PM
	 * @return
	 */
	public List<TreeModal> queryPostByCDid(String corpid){
		DBManager dbm = new DBManager();
		List<TreeModal> listModel = null;
		try {
			String sql = "SELECT id as id,postname as text,topcorpid as pid " +
					" FROM tspost WHERE topcorpid='"+corpid+"'";
			listModel = dbm.getObjectList(TreeModal.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取公司或部门下的职务列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		return listModel;
	}
	/**
	 * @描述：获取公司或部门下的所属区域列表
	 * @作者：SZ
	 * @时间：2014-12-15 下午02:49:19
	 * @param corpid
	 * @return
	 */
	public List<TreeModal> queryAreaByCDid(String corpid){
		DBManager dbm = new DBManager();
		List<TreeModal> listModel = null;
		try {
			String sql = "SELECT id as id,areaname as text,topcorpid as pid " +
					" FROM tsarea WHERE topcorpid='"+corpid+"'";
			listModel = dbm.getObjectList(TreeModal.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取公司或部门下的所属区域列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		return listModel;
	}
	/**
	 * @描述：获取数据中心列表
	 * @作者：wangruofeng
	 * @时间：2015-08-28 上午11:04:19
	 * @param corpid
	 * @return
	 */
	public List<TreeModal> queryDatacenter(){
		DBManager dbm = new DBManager();
		List<TreeModal> listModel = null;
		try {
			String sql = "SELECT id as id,name as text " +
					" FROM rsdatacenter  ";
			listModel = dbm.getObjectList(TreeModal.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取数据中心列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		return listModel;
	}
	
	
	
	/**
	 * 获取公司或部门下的权限列表
	 * @作者 SZ
	 * @日期 2014 12:14:19 PM
	 * @return
	 */
	public List<TreeModal> queryRoleByCDid(String corpid){
		DBManager dbm = new DBManager();
		List<TreeModal> listModel = null;
		try {
			String sql = "SELECT id as id,rolename as text,topcorpid as pid " +
					" FROM tsrole WHERE topcorpid='"+corpid+"'";
			listModel = dbm.getObjectList(TreeModal.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取公司或部门下的权限列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		return listModel;
	}
	public String queryTopCorpId(String corpid){
		DBManager dbm = new DBManager();
		String sql = "select * from tscorp where 1=1";
		List<Tscorp> listcorp = dbm.getObjectList(Tscorp.class,sql);
		dbm.close();
		TreeAddUserDao dao = new TreeAddUserDao();
		return dao.QueryTopCorpid(corpid, listcorp);
	}
}
