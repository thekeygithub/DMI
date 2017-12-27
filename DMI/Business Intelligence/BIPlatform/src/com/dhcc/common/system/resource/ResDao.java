package com.dhcc.common.system.resource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsbtnresource;

public class ResDao {
	private static final Logger logger = Logger.getLogger(ResDao.class);

	/**
	 * @param sname
	 * @param pm
	 * @return
	 */
	public PageModel resQueryList(PageModel pm,String menuid, String sortname,
			String sortorder) {
		DBManager dbm = new DBManager();
		try {
			String querysql = "select * from tsbtnresource where 1=1 " ;
				if(!StringUtil.isNullOrEmpty(menuid)){
					querysql = querysql + " and menuid = '"+menuid+"'";
				}
			querysql = querysql + " order by btnsort ," + sortname + " " + sortorder + " ";
			String countsql = "select count(*) from (" + querysql + ") t";
			int count = dbm.executeQueryCount(countsql);
			int total = count % pm.getPerPage() == 0 ? count / pm.getPerPage()
					: count / pm.getPerPage() + 1;
			pm.setTotalPage(total);
			pm.setTotalRecord(count);
			/**
			 * 分页sql构造
			 */
			PageFactory pageFactory = new PageFactory();
			String sql = pageFactory.createPageSQL(querysql, pm);
			pageFactory = null;
			List list = dbm.getObjectList(Tsbtnresource.class, sql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询资源列表时候出错！" + e.getMessage());
		} finally {
			dbm.close();
		}

		return pm;

	}

	/**
	 * 添加一个新的资源信息
	 * 
	 * @param
	 * @return 执行结果
	 */
	public boolean resAdd(Tsbtnresource model) {
		DBManager dbm = new DBManager();
		boolean b = false;
		try {
			b = dbm.insertObject(model);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("添加", "添加ID为"+model.getId()+"的按钮资源记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.error(e);
			return b;
		} finally {
			dbm.close();
		}
		return b;
	}

	/**
	 *@作者 GYR
	 *@日期 Oct 14, 2014 3:06:03 PM
	 *@描述 删除按钮资源信息
	 *@param ids
	 *@return
	 */
	public String resDel(String[] ids) {
		String sql = "";
		DBManager dbm = new DBManager();
		SaveLog saveLog = new SaveLog();
		try {
			for (String id : ids) {
				sql = "delete from tsbtnresource where id ='" + id + "'";
				dbm.addBatch(sql);
				saveLog.saveLog("删除", "删除ID为"+id+"的按钮资源记录信息！");
			}
			dbm.executeBatch();
			saveLog = null;
		} catch (Exception e) {
			logger.error("资源信息删除--失败", e);
			return "error";
		} finally {
			dbm.close();
		}
		return "success";
	}

	/**
	 * 根据id得到单个信息
	 * 
	 * @param sql
	 *            查询的sql语句
	 * @return
	 */
	public Tsbtnresource resQueryById(String id) {
		DBManager dbm = new DBManager();
		String sql = "select tb.* from tsbtnresource tb where tb.id='" + id + "'";
		Tsbtnresource model = null;
		try {
			model = (Tsbtnresource) dbm.getObject(Tsbtnresource.class, sql);
		} catch (Exception e) {
		} finally {
			dbm.close();
		}
		return model;
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	public boolean resModify(Tsbtnresource model) {
		DBManager dbm = new DBManager();
		boolean b = false;
		try {
			b = dbm.updateObject(model);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("修改", "修改ID为"+model.getId()+"的按钮资源记录信息！");
			saveLog = null;
		} catch (Exception e) {
			logger.info(e);
			return b;
		} finally {
			dbm.close();
		}
		return b;
	}
	
	
	public List<Tsbtnresource> queryList(){
		DBManager dbm = new DBManager();
		String sql = "select * from tsbtnresource  order by pagename,resourceid";
		List<Tsbtnresource> model = dbm.getObjectList(Tsbtnresource.class, sql);
		dbm.close();
		return model;
	}
	
	
	public List<Tsbtnresource> queryListByPageName(String userid,String pagename){
		DBManager dbm=new DBManager();
		String sql = "select DISTINCT m.* from tsuser u " +
				"left join tsluserrole ur on  u.id=ur.userid  " +
				"left join tsrole r on   r.id=ur.roleid   " +
				"left join tslroleresource rm on rm.roleid = r.id  " +
				"left join tsbtnresource m  on m.id = rm.resourceid  " +
				"where u.id='"+userid+"' and m.pageurl = '"+pagename+"' order by m.btnsort";
		List<Tsbtnresource> model = dbm.getObjectList(Tsbtnresource.class, sql);
		dbm.close();
		return model;
	}
	
	/**
	 * 通过菜单页面的id 查询对应的按钮
	 * @param menuid
	 */
	public List<Tsbtnresource> queryListByMenuId(String userid,String menuid){
		DBManager dbm = new DBManager();
		String sql = "select m.* from tsuser u " +
				"left join tsluserrole ur on  u.id=ur.userid  " +
				"left join tsrole r on   r.id=ur.roleid   " +
				"left join tslroleresource rm on rm.roleid = r.id  " +
				"left join tsbtnresource m  on m.id = rm.resourceid " +
				"where u.id='"+userid+"' and m.menuid = '"+menuid+"'  GROUP BY m.id order by m.btnsort";
		List<Tsbtnresource> model = dbm.getObjectList(Tsbtnresource.class, sql);
		dbm.close();
		return model;
	}
	
	/**
	 * 修改角色的菜单
	 * 
	 * @param role_id
	 *            角色ID
	 * @param ids
	 *            角色对应的menu_id集合
	 * @return 处理结果
	 */
	public String roleBtnModify(String role_id,String menuSort, String ids[]) {
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
			sql2 = "delete from tslroleresource where roleid='" + role_id + "' and menusort = '"+menuSort+"'";
			dbm.addBatch(sql2);

			if (null != list)
				for (int i = 0; i < list.size(); i++) {
					String id = CommUtil.getID();
					sql3 = "insert into tslroleresource values('"+id+"','" + role_id + "','" + menuSort + "','"
							+ list.get(i) + "')";
					dbm.addBatch(sql3);
				}
			dbm.executeBatch();
		} catch (Exception e) {

		} finally {
			dbm.close();
		}

		return "success";

	}
	
	private void addMyList(List list, String str) {
		if (!list.contains(str)) {
			list.add(str);
		}
	}
	
	
	
	/**
	 * 查询角色所拥有的权限访问的btn
	 * @return list
	 */
	public List getAllBtnByRole(String roleId,String menuSort){
		List list = new ArrayList();
		String sql="select * from tslroleresource where roleid = '"+roleId+"' and menuSort = '"+menuSort+"'";
		DBManager dbm = new DBManager();
		ResultSet rs = dbm.executeQuery(sql);
		try {
			while(rs.next()){
				list.add(rs.getString("resourceid"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbm.close();
		}
		return list;
	}
	
	/**
	 * 查询角色所拥有的权限访问的btn
	 * @return list
	 */
	public List getAllBtn(String menuSort){
		List list = new ArrayList();
//		String sql = "select tb.pagename,tb.pageurl,GROUP_CONCAT(tb.id,'|',tb.resourcename) as btnStr" +
//				" from tsbtnresource tb " +
//				" where  menuid = '"+menuSort+"' group by pagename order by tb.btnsort";
		String sql = "select pagename,pageurl,listagg(id||'|'||resourcename, ',') within group( order by btnsort) as btnStr from tsbtnresource "
				+ "where  menuid = '"+menuSort+"' group by pagename,pageurl";
		DBManager dbm = new DBManager();
		try {
			list = dbm.getObjectList(ResBtn.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
}
