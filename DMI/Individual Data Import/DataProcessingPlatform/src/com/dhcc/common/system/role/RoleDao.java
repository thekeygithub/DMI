package com.dhcc.common.system.role;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.system.user.UserQueryModel;
import com.dhcc.common.util.CommDao;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsrole;
import com.dhcc.ts.database.DBManager_HIS;
import com.opensymphony.xwork2.ActionContext;

public class RoleDao {

	private static final Logger logger = Logger.getLogger(RoleDao.class);
	private static final String supperRoleId="411a2bd78114475dbe8fa852e907ff23";
	 /**
     * @param sname
     * @param pm
     * @return
     */
	@SuppressWarnings("unchecked")
	public PageModel roleQueryList(PageModel pm,String rolename,String sortname,String sortorder){
		DBManager dbm=new DBManager();
		try {
			String querysql = "select tp.id,tp.rolename,tp.remark,tcp.corpname as topcorpid  from tsrole tp left join tscorp tcp on tcp.id = tp.topcorpid ";
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			boolean type = userid.equals(superUserId);
			if(type){//数据权限控制
				querysql += " where 1 = 1 ";
			}else{
				String topcorpid = (String)ActionContext.getContext().getSession().get("topcorpid");
				querysql += " where 1 = 1 and tp.topcorpid='"+topcorpid+"' ";
			}
			if (!StringUtil.isNullOrEmpty(rolename)) {
				querysql += " and tp.rolename like '%" + rolename + "%'";
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
			
			List<Tsrole> list = new ArrayList<Tsrole>();
			List<Tsrole> list1= dbm.getObjectList(Tsrole.class, sql);
			if(type){
				Tsrole modelnew;
				for(Tsrole model:list1){
					modelnew = new Tsrole();
					modelnew.setId(model.getId());
					modelnew.setRolename(model.getRolename()+"("+model.getTopcorpid()+")");
					modelnew.setRemark(model.getRemark());
					list.add(modelnew);
				}
			}else{
				list = list1;
			}
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询角色列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		
		return pm;
		
	}
	
	 
    /**
     * 添加一个新的角色
     * @param menu
     * @return 执行结果
     */
    public  boolean roleAdd(Tsrole model){
    	DBManager dbm = new DBManager();
    	boolean b = true;
		try {
			b = dbm.insertObject(model, "tsrole");
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("添加", "添加ID为"+model.getId()+"的角色记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.error(e);
		}finally{
    	  dbm.close();
		}
    	return b;
    }
	
    /**
     *@作者 GYR
     *@日期 Oct 14, 2014 10:37:56 AM
     *@描述 删除角色信息
     *@param ids
     */
	public String roleDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		SaveLog saveLog = new SaveLog();
		try {
			for (String id : ids) {
				sql = "delete from tsrole where id='" + id + "'";
				dbm.addBatch(sql);
				sql = "delete from tsluserrole where roleid='" + id + "'";
				dbm.addBatch(sql);
				saveLog.saveLog("删除角色信息", "删除ID为"+ id +"的角色记录信息！");
			}
			dbm.executeBatch();
		} catch (Exception e) {
			logger.error("角色信息删除--失败",e);
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
    public Tsrole roleQueryById(String id){
    	DBManager dbm = new DBManager();
    	String  sql = "select * from tsrole where id='"+id+"'";
    	Tsrole model = null;
		try {
			model = (Tsrole) dbm.getObject(Tsrole.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
    	   dbm.close();
		}
    	return model;
    }
    
    
    /**
	 * 更新角色信息
	 * @param model
	 */
	public  boolean  roleModify(Tsrole model){
		DBManager dbm = new DBManager();
		String sql = "";
		try {
			sql = "update tsrole set rolename='" + model.getRolename() + "',remark='" + model.getRemark()  + "' where id='" + model.getId() + "'";
			dbm.executeUpdate(sql);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("修改角色信息", "修改ID为"+model.getId()+"的角色记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.info(e);
			return false;
		}finally{
			dbm.close();
		}
		return true;
		
	}
	
	/**
	 * 修改角色的菜单
	 * @param role_id  角色ID
	 * @param ids 角色对应的menu_id集合
	 * @return 处理结果
	 */
	public String roleMenuModify(String role_id, String ids[],String sort) {
		DBManager dbm = new DBManager();
		List<String> list = new ArrayList<String>();
		for (String str : ids) {
			String strs[] = str.split(",");
			for (int k = 0; k < strs.length; k++) {
				addMyList(list, strs[k].trim());
			}

		}
		String sql2 = "";
		String sql3 = "";
		try {
			String userid = (String)ActionContext.getContext().getSession().get("userid");
			String superUserId = new CommDao().queryConfigSuperUserID();//获取最大权限id
			if(userid.equals(superUserId)){//数据权限控制
				sql2 = "delete from tslrolemenu where roleid='" + role_id + "' and sort = '"+sort+"'";
			}else{
				sql2 = "delete trm.* from tslrolemenu trm " +
						" left join tsmenu tm on tm.id=trm.menuid " +
						" where trm.roleid='" + role_id + "' and trm.sort = '"+sort+"' and tm.whetherpublic='01'";
			}
			
			dbm.addBatch(sql2);

			if (null != list)
				for (int i = 0; i < list.size(); i++) {
					sql3 = "insert into tslrolemenu values('" + role_id + "','"
							+ list.get(i) + "','"+sort+"')";
					dbm.addBatch(sql3);
				}
			dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		} finally {
			dbm.close();
		}
		return "success";
	}
	
	/**
	 *@作者 GYR
	 *@日期 Oct 14, 2014 1:58:02 PM
	 *@描述 判断list中是否包含有该字段
	 *@param list
	 *@param str
	 */
	@SuppressWarnings("unchecked")
	private void addMyList(List list, String str) {
		if (!list.contains(str)) {
			list.add(str);
		}
	}
	@SuppressWarnings("unchecked")
	public List queryroleHaveUserList(String roleid){
		DBManager dbm = new DBManager();
		List<UserQueryModel> list = new ArrayList<UserQueryModel>();
		String querysql = "SELECT * FROM tsuser tu LEFT JOIN tsluserrole tus ON tus.userid=tu.id WHERE tus.roleid='"+roleid+"'";
		list = dbm.getObjectList(UserQueryModel.class,querysql);
		System.out.println(list);
		dbm.close();
		return list;
	}
	public void roleDelUser(String roleid,String[] ids){
		DBManager dbm = new DBManager();
		String sql;
		for(String userid : ids){
			sql = "DELETE FROM tsluserrole WHERE userid='"+userid+"' AND roleid = '"+roleid+"'";
			dbm.addBatch(sql);
		}
		dbm.executeBatch();
		dbm.close();
	}
	
	
	/**  
	* @标题: queryUserHaveRole  
	* @描述: TODO判断用户是否有管理员权限,没有返回false,有返回true
	* @作者 EbaoWeixun
	* @param userid
	* @return
	*/  
	public boolean queryUserHaveRole(String userid){
		DBManager_HIS dbm=new DBManager_HIS();
		boolean flag=false;
		try{
			String sql="select count(*) from TSLUSERROLE t inner join TSCONFIG c ON c.dkey=t.roleid AND c.dtype='ISAUTH' where t.userid='"+userid+"' ";
			int cnt=dbm.executeQueryCount(sql);
			
			if(cnt>0){
				flag=true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return flag;
	}
}
