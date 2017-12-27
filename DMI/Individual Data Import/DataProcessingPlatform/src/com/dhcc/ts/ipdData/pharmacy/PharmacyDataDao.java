package com.dhcc.ts.ipdData.pharmacy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.ipdData.model.CurrentModel;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月22日 上午10:48:47 
* @类说明：药店数据
*/
public class PharmacyDataDao {

	private final static Logger logger=Logger.getLogger(PharmacyDataDao.class);
	public List<CurrentModel> findPharmacyDataList(String cardno,String bname,String bdate,String edate){
		DBManager_HIS dbm=new DBManager_HIS();
		List<CurrentModel> list=null;
		try{
			StringBuffer sql=new StringBuffer("SELECT d.id,d.buy_date as rdate,'药店数据' as mtype, ");
			sql.append(" '购药' as dtype,d.drug_store as corp,'' as corp_id,'' as unit,d.drug detail,'' as dcname ");
			sql.append(" FROM h_buy_drug d ");
			sql.append(" WHERE d.card_id='"+cardno+"'");
			
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND d.buy_date>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND d.buy_date<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
			list=dbm.getObjectList(CurrentModel.class, sql.toString());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询购药数据出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return list;
	}
	public Map<String,Object> getPharmacyDrug(String id){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> mapQ=null;
		try{
			String sql="SELECT d.drug,d.drug_store,d.longitude||','||d.latitude gps,d.ph_name,to_char(d.buy_date,'yyyy-mm-dd hh24:mi:ss hh24:mi:ss') sj FROM h_buy_drug d WHERE d.id='"+id+"'";
			map=dbm.executeQueryHashMap(sql);
			String a [] = map.get("DRUG").toString().replace("|", "&").split("&");
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
			map.put("DRUG",str);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询购药详细信息"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
}
