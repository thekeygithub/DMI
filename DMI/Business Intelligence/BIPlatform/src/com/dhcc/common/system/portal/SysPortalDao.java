package com.dhcc.common.system.portal;

import java.util.List;

import com.dhcc.common.database.DBManager;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsportalres;

/**
 * @author GYR
 * 门户资源dao
 */
public class SysPortalDao {
	/**
	 *@作者 GYR
	 *@日期 Aug 19, 2014 4:47:13 PM
	 *@描述 门户资源列表查询
	 *@param pm
	 */
	public PageModel sysPortalQueryList(PageModel pm){
		DBManager dbm=new DBManager();
		try {
			String querysql = "select * from tsportalres order by pname";
			String countsql = "select count(*) from (" + querysql + ") t";
			int count =dbm.executeQueryCount(countsql);
			int total = count % pm.getPerPage() == 0 ? count /pm.getPerPage() : count / pm.getPerPage() + 1;
			pm.setTotalPage(total);
			pm.setTotalRecord(count);
			querysql = querysql+" limit "+(pm.getCurrentPage()-1)*pm.getPerPage()+","+pm.getPerPage();
			List list = dbm.getObjectList(Tsportalres.class, querysql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return pm;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 19, 2014 5:01:26 PM
	 *@描述 删除
	 *@param ids
	 */
	public String sysPortalDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		try {
			for (String id : ids) {
				sql = "delete from tsportalres where id = '" + id + "'";
				dbm.addBatch(sql);
			}
			dbm.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbm.close();
		}
		return "success";
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 20, 2014 9:30:51 AM
	 *@描述 添加门户资源信息
	 */
	public boolean addInfo(Tsportalres model){
		DBManager dbm = new DBManager();
		boolean b = false;
		try {
			b = dbm.insertObject(model);
		} catch (Exception e) {
			e.printStackTrace();
			return b;
		} finally {
			dbm.close();
		}
		return b;
	}
	
	
	/**
	 *@作者 GYR
	 *@日期 Aug 20, 2014 9:34:56 AM
	 *@描述 修改门户资源信息
	 */
	public boolean  modifyInfo(Tsportalres model){
		DBManager dbm = new DBManager();
		boolean b = false;
		try {
			b = dbm.updateObject(model);
		} catch (Exception e) {
			e.printStackTrace();
			return b;
		} finally {
			dbm.close();
		}
		return b;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Aug 20, 2014 9:36:21 AM
	 *@描述 根据ID查询单个的信息
	 */
	public Tsportalres queryInfoById(String id){
		DBManager dbm = new DBManager();
		Tsportalres model = null;
		try {
			String sql = "select * from tsportalres where id = '"+id+"'";
			model = (Tsportalres) dbm.getObject(Tsportalres.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			return model;
		} finally {
			dbm.close();
		}
		return model;
	}

}
