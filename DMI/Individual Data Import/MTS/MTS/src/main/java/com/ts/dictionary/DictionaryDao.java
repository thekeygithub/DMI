package com.dhcc.common.system.dictionary;

import java.util.List;

import org.apache.log4j.Logger;

import com.dhcc.common.database.DBManager;
import com.dhcc.common.system.page.PageFactory;
import com.dhcc.common.system.systemLog.SaveLog;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.modal.system.Tsdict;

public class DictionaryDao {


	private static final Logger logger = Logger.getLogger(DictionaryDao.class);
	
	 /**
	  * @描述：查询数据字典列表
	  * @作者：SZ
	  * @时间：2014-10-11 下午02:37:47
	  * @param pm
	  * @param sortname
	  * @param sortorder
	  * @param dtype
	  * @param dvalue
	  * @return
	  */
	public PageModel dictQueryList(PageModel pm,String sortname,String sortorder,String dtype,String dvalue){
		DBManager dbm=new DBManager();
		try {
			String querysql = "select * from tsdict where 1=1";
			
			if (!StringUtil.isNullOrEmpty(dtype)) {
				querysql += " and dtype like '%" + dtype + "%'";
			}
			if (!StringUtil.isNullOrEmpty(dvalue)) {
				querysql += " and dvalue like '%" + dvalue + "%'";
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
			List<Tsdict> list = dbm.getObjectList(Tsdict.class, sql);
			pm.setList(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询数据字典列表时候出错！"+e.getMessage());
		}finally{
			dbm.close();
		}
		
		return pm;
		
	}
	
	 
    /**
     * @描述：添加一个新的数据字典
     * @作者：SZ
     * @时间：2014-10-11 下午02:34:32
     * @param model
     * @return
     */
    public  boolean  dictAdd(Tsdict model){
    	DBManager dbm=new DBManager();
    	boolean b=true;
		try {
			b = dbm.insertObject(model, "tsdict");
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("添加数据字典信息", "添加ID为"+model.getId()+"数据字典记录！");
		} catch (Exception e) {
			logger.error(e);
		}finally{
    	  dbm.close();
		}
    	return b;
    }
	
    /**
     * @描述：数据字典删除
     * @作者：SZ
     * @时间：2014-10-11 下午02:33:52
     * @param ids
     * @return
     */
	public String dictDel(String[] ids){
		String sql = "";
		DBManager dbm = new DBManager();
		SaveLog saveLog = new SaveLog();
		try {
			for (String id : ids) {
				sql = "delete from tsdict where id='" + id + "'";
				dbm.addBatch(sql);
				saveLog.saveLog("删除数据字典信息", "删除ID为"+id+"数据字典记录！");
			}
			dbm.executeBatch();
		} catch (Exception e) {
			logger.error("数据字典信息删除--失败",e);
			return "error";
		} finally {
			dbm.close();
		}
		return "success";
	}
	/**
	 * @描述：根据id得到单个信息 
	 * @作者：SZ
	 * @时间：2014-10-11 下午02:34:00
	 * @param id
	 * @return
	 */
    public Tsdict dictQueryById(String id){
    	DBManager dbm=new DBManager();
    	String sql="select * from tsdict where id='"+id+"'";
    	Tsdict model=null;
		try {
			model = (Tsdict) dbm.getObject(Tsdict.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
    	   dbm.close();
		}
    	return model;
    }
    
    
    /**
     * @描述：修改数据字典
     * @作者：SZ
     * @时间：2014-10-11 下午02:34:10
     * @param model
     * @return
     */
	public int dictModify(Tsdict model){
		DBManager dbm=new DBManager();
		String sql = "";
	    int i = 0;
		try {
			sql = "update tsdict set dtype='" + model.getDtype() + "',dkey='" + model.getDkey() + "',dvalue='" + model.getDvalue() + "',remark='" + model.getRemark() + "' where id='" + model.getId() + "'";
			i = dbm.executeUpdate(sql);
			SaveLog saveLog = new SaveLog();
			saveLog.saveLog("修改数据字典", "修改ID为"+model.getId()+"数据字典记录！");
			saveLog = null;
		} catch (Exception e) {
			logger.info(e);
		}finally{
			dbm.close();
		}
		return i;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Oct 22, 2014 11:00:11 AM
	 *@描述 根据type和key查询数据字典信息
	 *@param dtype
	 *@param dkey
	 */
	public List<Tsdict> dictQueryByType(String dtype){
		DBManager dbm=new DBManager();
    	String sql = "select * from tsdict where dtype ='"+dtype+"'";
    	List<Tsdict> list = null;
		try {
			list =  dbm.getObjectList(Tsdict.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			return list;
		}finally{
    	   dbm.close();
		}
		return list;
	}
	
	/**
	 *@作者 GYR
	 *@日期 Oct 22, 2014 11:00:11 AM
	 *@描述 根据type和key查询数据字典信息
	 *@param dtype
	 *@param dkey
	 *@return
	 */
	public Tsdict dictQueryByTypeANDKey(String dtype,String dkey){
		DBManager dbm=new DBManager();
    	String sql = "select * from tsdict where dtype ='"+dtype+"' and dkey = '"+dkey+"'";
    	Tsdict model=null;
		try {
			model = (Tsdict) dbm.getObject(Tsdict.class, sql);
		} catch (Exception e) {
			e.printStackTrace();
			return model;
		}finally{
    	   dbm.close();
		}
		return model;
	}
	
}
