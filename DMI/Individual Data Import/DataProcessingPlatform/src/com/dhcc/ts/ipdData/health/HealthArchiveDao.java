package com.dhcc.ts.ipdData.health;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.ipdData.model.CurrentModel;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月27日 下午2:29:27 
* @类说明： 健康档案
*/
public class HealthArchiveDao {
	private final static Logger logger=Logger.getLogger(HealthArchiveDao.class);
	/**  
	* @标题: findHerArchiveList  
	* @描述: TODO 查询健康档案信息
	* @作者 EbaoWeixun
	* @param cardno
	* @param bname
	* @param bdate
	* @param edate
	* @return
	*/  
	public List<CurrentModel> findHerArchiveList(String cardno,String bname,String bdate,String edate){
		List<CurrentModel> list=null;
		DBManager_HIS dbm=new DBManager_HIS();
		try{
			StringBuffer sql=new StringBuffer("SELECT a.doc_no as id,to_char(a.create_date,'yyyy-mm-dd hh24:mi:ss') as rdate,'个人健康档案' as mtype,'健康档案建档' as dtype, ");
			sql.append(" a.department as corp,'' as corp_id,'' as unit,'健康档案建档' detail,a.doctor as dcname");
			sql.append(" FROM h_cover a ");
			sql.append(" WHERE a.id='"+cardno+"' ");
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND a.create_date>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND a.create_date<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
			list=dbm.getObjectList(CurrentModel.class, sql.toString());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询健康档案出错"+e.getMessage());
		}finally {
			dbm.close();
		}
		return list;
	}
	/**  
	* @标题: getHealthCover  
	* @描述: TODO插叙健康档案建档详细信息
	* @作者 EbaoWeixun
	* @param cardno
	* @param docno
	* @return
	*/  
	public Map<String,Object> getHealthCover(String docno){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		try{
		    String sql="SELECT * FROM h_cover WHERE doc_no='"+docno+"'";
		    map=dbm.executeQueryHashMap(sql);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询健康档案详细信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: findHerFollowList  
	* @描述: TODO 查询随访记录
	* @作者 EbaoWeixun
	* @param cardno
	* @param bname
	* @param bdate
	* @param edate
	* @return
	*/  
	public List<CurrentModel> findHerFollowList(String cardno,String bname,String bdate,String edate){
		List<CurrentModel> list=null;
		DBManager_HIS dbm=new DBManager_HIS();
		try{
			StringBuffer sql=new StringBuffer("SELECT f.id,to_char(f.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,'个人健康档案' as mtype,'慢病随访' as dtype, ");
			sql.append(" CASE WHEN f.r2='1' THEN c.department ELSE c.present_addr END corp,'' as corp_id,'' as unit,'糖尿病' detail,f.r14 as dcname ");
			sql.append(" FROM h_following_xt f ");
			sql.append(" LEFT JOIN h_cover c ON c.id=f.user_id ");
			sql.append(" WHERE f.user_id='"+cardno+"' ");
			
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND f.r1>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND f.r1<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
    	    sql.append(" UNION ALL ");
    	    sql.append(" SELECT y.id,to_char(y.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,'个人健康档案' as mtype,'慢病随访' as dtype, ");
			sql.append(" CASE WHEN y.r2='1' THEN v.department ELSE v.present_addr END corp,'' as corp_id,'' as unit,'高血压' detail,y.r13 as dcname ");
			sql.append(" FROM h_following_xy y ");
			sql.append(" LEFT JOIN h_cover v ON v.id=y.user_id ");
			sql.append(" WHERE y.user_id='"+cardno+"' ");
			if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND y.r1>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND y.r1<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
			list=dbm.getObjectList(CurrentModel.class, sql.toString());
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询慢病随访出错"+e.getMessage());
		}finally {
			dbm.close();
		}
		return list;
	}
	/**  
	* @标题: findHealthFollowing  
	* @描述: TODO查询随访详细信息
	* @作者 EbaoWeixun
	* @param id
	* @return
	*/  
	public List<Map<String,Object>> findHealthFollowing(String id,String flag){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		List<Map<String,Object>> list=null;
		try{
			String tableName="1".equals(flag)?"h_following_xt":"h_following_xy";
			String sql="SELECT xt.user_id,xt.doc_no,to_char(xt.r1,'yyyy-mm-dd hh24:mi:ss') rdate FROM "+tableName+" xt WHERE xt.id='"+id+"' ";
		    map=dbm.executeQueryHashMap(sql);
		    if(!map.isEmpty()){
		    	sql="SELECT * FROM "+tableName+" WHERE user_id='"+map.get("USER_ID")+"' AND doc_no='"+map.get("DOC_NO")+"' AND r1<=to_date('"+map.get("RDATE")+"','yyyy-mm-dd hh24:mi:ss') ORDER BY r1 asc"; 	
		    	list=dbm.executeQueryListHashMap(sql);	
		    }
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询随访记录出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return list;
	}
	/**  
	* @标题: findHerExList  
	* @描述: TODO查询体检信息
	* @作者 EbaoWeixun
	* @param cardno
	* @param bname
	* @param bdate
	* @param edate
	* @return
	*/  
	public List<CurrentModel> findHerExList(String cardno,String bname,String bdate,String edate){
		List<CurrentModel> list=null;
		DBManager_HIS dbm=new DBManager_HIS();
		try{
			StringBuffer sql=new StringBuffer("SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,'个人健康档案' as mtype,'健康体检' as dtype, ");
			sql.append(" h.department as corp,'' as corp_id,'' as unit,'健康体检' detail,e.doctor as dcname");
			sql.append(" FROM h_physical_examination e ");
			sql.append(" LEFT JOIN h_cover h ON h.id=e.id ");
			sql.append(" WHERE e.id='"+cardno+"' ");
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND e.ex_date>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND e.ex_date<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
			list=dbm.getObjectList(CurrentModel.class, sql.toString());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询健康体检出错"+e.getMessage());
		}finally {
			dbm.close();
		}
		return list;
	}

	/**  
	* @标题: getExamination  
	* @描述: TODO查询体检详细信息
	* @作者 EbaoWeixun
	* @param docno
	* @return
	*/  
	public Map<String,Object> getExamination(String docno){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		try{
		    String sql="SELECT * FROM h_physical_examination WHERE doc_no='"+docno+"'";
		    map=dbm.executeQueryHashMap(sql);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询健康档案体检信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
}
