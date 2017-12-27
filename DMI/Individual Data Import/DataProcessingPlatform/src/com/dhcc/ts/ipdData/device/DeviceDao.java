package com.dhcc.ts.ipdData.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.ipdData.model.CurrentModel;

/** 
* @作者:EbaoWeixun 
* @创建时间：2017年2月27日 下午1:06:56 
* @类说明：可穿戴设备
*/
public class DeviceDao {

	private final static Logger logger=Logger.getLogger(DeviceDao.class);

	/**  
	* @标题: findDeviceList  
	* @描述: TODO 查询可穿戴设备数据
	* @作者 EbaoWeixun
	* @param cardno
	* @param bname
	* @param bdate
	* @param edate
	* @param flag
	* @return
	*/  
	public List<CurrentModel> findDeviceList(String cardno,String bname,String bdate,String edate,String flag){
		List<CurrentModel> list=null;
    	DBManager_HIS dbm=new DBManager_HIS();		
    	try{
    		//门诊
    	    StringBuffer sql=new StringBuffer("SELECT id,m_time rdate,case type when 1 then '血压(可穿戴)' else '血糖(可穿戴)' end as dtype ");
    	    sql.append(" ,'医疗可穿戴设备' as mtype,'' unit,position as corp,case type when 1 then dbp||'-'||sbp||'mmHg' else trunc(bg, 2)||'mmol/L' end as detail,'' as corp_id,'' as dcname ");
    	    sql.append(" FROM h_device ");
    	    sql.append(" WHERE user_id='"+cardno+"' ");
    	    
    	    if(!StringUtil.isNullOrEmpty(flag)){
    	    	sql.append(" AND type="+flag );
    	    }
    	    if(!StringUtil.isNullOrEmpty(bdate)){
    	    	sql.append(" AND m_time>=to_date('"+bdate+"','yyyy-mm-dd hh24:mi:ss') ");
    	    }
    	    if(!StringUtil.isNullOrEmpty(edate)){
    	    	sql.append(" AND m_time<=to_date('"+edate+"','yyyy-mm-dd hh24:mi:ss')+1  ");
    	    }
    	    list=dbm.getObjectList(CurrentModel.class, sql.toString());
    	    
    	    
    	}catch(Exception e){
    		logger.error("查询可穿戴设备出错"+e.getMessage());
    		e.printStackTrace();
    	}finally{
    		dbm.close();
    	}
    	
    	return list;
	}
	/**  
	* @标题: getDevice  
	* @描述: TODO主键查询可穿戴设备信息
	* @作者 EbaoWeixun
	* @param id
	* @return
	*/  
	public Map<String,Object> getDevice(String id){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			String sql="SELECT d.id,d.device_id,d.brand,d.heart_rate,d.sbp,d.dbp,d.bg,d.longitude,d.latitude,to_char(d.m_time,'yyyy-mm-dd hh24:mi:ss hh24:mi:ss') sj,d.position position FROM h_device d WHERE d.id='"+id+"'";
			map=dbm.executeQueryHashMap(sql);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("查询可穿戴设备出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
		
	}
}
