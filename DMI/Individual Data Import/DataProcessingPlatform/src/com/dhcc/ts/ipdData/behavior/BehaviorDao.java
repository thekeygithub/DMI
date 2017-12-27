package com.dhcc.ts.ipdData.behavior;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.ipdData.model.CurrentModel;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月27日 上午10:40:59 
* @类说明：app使用行为
*/
public class BehaviorDao {

	private final static Logger logger=Logger.getLogger(BehaviorDao.class);
	/**  
	* @标题: findBehaviorList  
	* @描述: TODO 查询app使用行为数据
	* @作者 EbaoWeixun
	* @param cardno
	* @param bname
	* @param bdate
	* @param edate
	* @return
	*/  
	public List<CurrentModel> findBehaviorList(String cardno,String bname,String bdate,String edate){
		List<CurrentModel> list=null;
		DBManager_HIS dbm=new DBManager_HIS();
		try{
			StringBuffer sql=new StringBuffer("SELECT id,use_date as rdate,'APP使用行为' as mtype,'APP使用行为' as dtype, ");
			sql.append(" longitude||'°E,'||latitude||'°N' as corp,'' as corp_id,'' as unit,content detail,'' as dcname ");
			sql.append(" FROM h_behavior ");
			sql.append(" WHERE user_id='"+cardno+"' ");
			
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND use_date>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND use_date<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
			list=dbm.getObjectList(CurrentModel.class, sql.toString());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询APP使用行为出错"+e.getMessage());
		}finally {
			dbm.close();
		}
		return list;
	}
	/**  
	* @标题: getBehavior  
	* @描述: TODO 主键查询app使用行为信息
	* @作者 EbaoWeixun
	* @param id
	* @return
	*/  
	public Map<String,Object> getBehavior(String id){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> mapQ=null;
		try{
			String sql="SELECT * FROM h_behavior WHERE id='"+id+"'";
			map=dbm.executeQueryHashMap(sql);
			String a [] = map.get("CONTENT").toString().replace("|", "&").split("&");
			String str = "";
			for(int i=0;i<a.length;i++){
				mapQ=new HashMap<String,Object>();
				String sqlQ = "SELECT DB_ID FROM KN_DRUG_BASE WHERE DRUG_NAME='"+a[i]+"'";
				mapQ = dbm.executeQueryHashMap(sqlQ);
				str+="<a href=\"../ipdData/drug.html?ypid="+mapQ.get("DB_ID")+"\">"+a[i]+"</a>|";
			}
			if(str.endsWith("|")){
				str = str.substring(0, str.length()-1);
			}
			map.put("CONTENT",str);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询app使用行为信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
}
