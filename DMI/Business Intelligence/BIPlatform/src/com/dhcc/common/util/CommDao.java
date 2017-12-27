/**
 * 
 */
package com.dhcc.common.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.modal.system.CommModel;
import com.dhcc.modal.system.Tsconfig;

/**
 * @author tiger
 *
 */
public class CommDao {
	private final static Logger logger = Logger.getLogger(CommDao.class);
	/**
	 * 根据数据字典的类型，返回前台使用的select的
	 * @param dictType
	 * @return
	 */
	public  List<CommModel> getSelectHTML(String dictType) {
		String sql = "select t.dkey as id,t.dvalue as text from tsdict t " +
				" where dtype='" + dictType + "' order by dkey";
		DBManager dbm = null;
		List<CommModel> list = null;
		try {
			dbm = new DBManager();
		    list = dbm.getObjectList(CommModel.class, sql);
		} catch (Exception e) {
			logger.error("");
		} finally {
			dbm.close();
		}
		return list;
	}
	
	/**
	 * 根据系统配置的类型，返回前台使用的select的
	 * @param dictType
	 * @return
	 */
	public  List<CommModel> getConfigSelectHTML(String dictType) {
		String sql = "select t.dkey as id,t.dvalue as text from tsconfig t " +
				" where dtype='" + dictType + "' order by dkey";
		DBManager dbm = null;
		List<CommModel> list = null;
		try {
			dbm = new DBManager();
		    list = dbm.getObjectList(CommModel.class, sql);
		} catch (Exception e) {
			logger.error("");
		} finally {
			dbm.close();
		}
		return list;
	}
	/**
	 * @描述：获取已部署的流程名称
	 * @作者：SZ
	 * @时间：2014-12-9 下午01:46:05
	 * @return
	 */
	public  List<CommModel> getBusinessSelectHTML() {
		String sql = "SELECT process_definition_name AS id, process_definition_name AS text " +
				" FROM jbpm_process_deployment " +
				" WHERE 1=1 " +
				" GROUP BY process_definition_name " +
				" ORDER BY process_definition_name";
		DBManager dbm = null;
		List<CommModel> list = null;
		try {
		dbm = new DBManager();
		list = dbm.getObjectList(CommModel.class, sql);
		} catch (Exception e) {
			logger.error(""+e.getMessage());
		} finally {
		dbm.close();
		}
		return list;
	}
	/**
	 * @描述：获取数据权限最大的用户id
	 * @作者：SZ
	 * @时间：2014-12-14 下午10:10:14
	 * @return
	 */
	public String queryConfigSuperUserID(){
		DBManager dbm=new DBManager();
		String dvalue = "";
		try {
			Tsconfig tsconfigmodel = (Tsconfig)dbm.getObject(Tsconfig.class, "SELECT * FROM tsconfig WHERE dtype='SUPERUSERID' AND dkey='SUPERUSERID'");
			dvalue = tsconfigmodel.getDvalue();
		}catch (Exception e) {
			logger.error(""+e.getMessage());
		}finally{
			dbm.close();
		}
		return dvalue;
	}
}
