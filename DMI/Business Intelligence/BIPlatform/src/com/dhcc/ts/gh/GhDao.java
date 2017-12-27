package com.dhcc.ts.gh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;

import com.dhcc.common.util.CommUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.ts.database.DBManager_HIS;



/** 
* @作者:EbaoWeixun 
* @创建时间：2017年4月7日 下午1:30:59 
* @类说明：智能挂号
*/
public class GhDao {
	private final static Logger logger=Logger.getLogger(GhDao.class);
	private final static int pageSize=6;
	//排序
	private final static Map<String,String> orderMap=new HashMap<String,String>();
	private final static Map<String,String> preferMap=new HashMap<String,String>();
	private final static Map<String,String> dateMap=new HashMap<String,String>();
	static{
		orderMap.put("0", "distance");
		orderMap.put("1", "p_num");
		orderMap.put("2", "hlvl");
		orderMap.put("3", "dlvl");
		
		
		preferMap.put("0", "distance");//距离优先
		preferMap.put("1", "is_yibao desc");//医保优先
		preferMap.put("2", "p_num desc");//有号优先
		preferMap.put("3", "max(hylx) desc");//专家号优先
		preferMap.put("4", "max(hylx) ");//普通号优先
		preferMap.put("5", "dlvl desc ");//主任医师优先
		preferMap.put("6", "hlvl ");//三甲医院优先
		
		dateMap.put("星期一", "1");
		dateMap.put("星期二", "2");
		dateMap.put("星期三", "3");
		dateMap.put("星期四", "4");
		dateMap.put("星期五", "5");
		dateMap.put("星期六", "6");
		dateMap.put("星期日", "7");
	}
	/**  
	* @标题: queryUserInfo  
	* @描述: TODO个人信息接口
	* @作者 EbaoWeixun
	* @param q_addr_id
	* @return 
	*/  
	public Map<String,Object> queryUserInfo(String q_user_id){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			String sql="select * from reg_user where user_id='"+q_user_id+"'";
			map=dbm.executeQueryHashMap(sql);
		}catch(Exception e){
			logger.error("查询个人信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: setPreference  
	* @描述: TODO修改偏好设置
	* @作者 EbaoWeixun
	* @param q_user_id
	* @param q_prefer_list
	* @return
	*/  
	public Map<String,Object> setPreference(String q_user_id,String q_prefer_list){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		int iresult=-1;
		map.put("status", 1);
		try{
			String sql="update reg_user set prefer='"+q_prefer_list+"' where user_id='"+q_user_id+"'";
			iresult=dbm.executeUpdate(sql);
			if(iresult>0){
				map.put("user_id",q_user_id );
				map.put("status", 0);
			}
			
		}catch(Exception e){
			logger.error("修改偏好设置出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: queryDoctorPlans  
	* @描述: TODO根据病症查询医生信息接口
	* @作者 EbaoWeixun
	* @param q_date
	* @param q_period
	* @param q_need_prefer
	* @param q_addr_id
	* @param q_sort_field
	* @param q_sort_type
	* @param q_hospital_name
	* @param q_doctor_name
	* @param q_hospital_id
	* @param q_disease
	* @param q_service_type
	* @param q_is_yibao
	* @param q_hosp_level
	* @param q_plan_type
	* @param q_hosp_type
	* @param q_doctor_title
	* @param page
	* @return
	*/  
	public Map<String,Object> queryDoctorPlans(String q_date,String q_period,String q_need_prefer,String q_addr_id
			,String q_sort_field,String q_sort_type,String q_hospital_name,String q_doctor_name,String q_hospital_id
			,String q_disease,String q_service_type,String q_is_yibao,String q_hosp_level,String q_plan_type,String q_hosp_type,String q_doctor_title,int page
			,String ws_lng,String ws_lat,String ne_lng,String ne_lat){
		DBManager_HIS dbm=new DBManager_HIS();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
	
		int count=0;
		String latitude="";
		String longitude="";
		String prefer="";
		try{
			//查询地址信息
			StringBuffer sql=new StringBuffer();
			sql.append("select u.prefer from reg_user u inner join reg_address d on d.user_id=u.user_id where d.addr_id='"+q_addr_id+"'  ");
			map=dbm.executeQueryHashMap(sql.toString());
			//使用偏好设置
			if(!StringUtil.isNullOrEmpty(q_need_prefer)&&map.get("PREFER")!=null){
				prefer=map.get("PREFER").toString();
				for(String key:preferMap.keySet()){
					prefer=prefer.replace(key, preferMap.get(key));
					
				}
			}
			sql=new StringBuffer("select * from reg_address where addr_id='"+q_addr_id+"'");
			map=dbm.executeQueryHashMap(sql.toString());
			longitude=map.get("LONGITUDE").toString();
			latitude=map.get("LATITUDE").toString();
			
			sql=new StringBuffer("select  sum(plan_num) p_num,distance,is_yibao,hlvl,dlvl,doctor_id,doctor_name,hosp_name,hosp_lvl,hosp_dept,doctor_lvl,hosp_coordinate,doctor_sex,max(hylx),hosid from(select d.work_type hylx,case when h.medical_code is null then '0' else '1' end is_yibao,h.hos_rank hlvl,m.skill_title dlvl,m.med_id doctor_id,m.staff_name doctor_name,h.hos_name hosp_name,tsh.dvalue hosp_lvl,t.dep_name hosp_dept,tsd.dvalue doctor_lvl,h.longitude||','||h.latitude hosp_coordinate,d.queue_quantity plan_num,d.queue_state plan_state,m.sex doctor_sex,h.hos_id hosid ");
			sql.append(",case when ("
+"  h.LATITUDE = ''"
+"  OR h.LATITUDE IS NULL"
+"  OR h.LONGITUDE = ''"
+"  OR h.LONGITUDE IS NULL) then"
+"  0 "
+"  else "
+"    ("
 +"   2 * ATAN2("
+"      SQRT("
       +" SIN("
       +"   (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
       +" ) * SIN("
       +"   (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
          +" ) + COS("+latitude+" * ACOS(-1) / 180) * COS(h.LATITUDE * ACOS(-1) / 180) * SIN("
       +"   (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
       +" ) * SIN("
       +"   (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
       +" )"
       +"      ),"
    		  +"      SQRT("
    		  +"        1 - SIN("
    		  +"          (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
    		  +" ) * SIN("
    		  +"  (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
    		  +" ) + COS("+latitude+" * ACOS(-1) / 180) * COS(h.LATITUDE * ACOS(-1) / 180) * SIN("
    		  +"   (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
    		  +" ) * SIN("
    		  +"    (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
    		  +"  )"
    		  +"  )"
    		  +"  )"
    		  +" ) * 6378140"
    		  +" end distance ");
			sql.append(" from fac_duty_app d ");
            sql.append(" left join fac_medical_app m on m.med_id=d.med_id ");
            sql.append(" left join fac_department_app t on t.dep_id=m.dep_id ");
		    sql.append(" left join fac_hospital_app h on h.hos_id=t.hos_id ");
		    sql.append(" left join tsdict tsh on tsh.dkey=h.hos_rank and tsh.dtype='HOSP_LVL' ");
		    sql.append(" left join tsdict tsd on tsd.dkey=m.skill_title and tsd.dtype='DOCTOR_LVL' ");
		    //疾病名称
		    sql.append("where m.dep_id is not null and t.hos_id is not null ");
		    if(!StringUtil.isNullOrEmpty(q_disease)){
		    	sql.append(" and (m.good_skill like '%"+q_disease+"%' or m.standard_disease like '%"+q_disease+"%')  ");
		    }
		    //日期   和 上下午
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
		    Calendar cal1 = Calendar.getInstance();  
		    cal1.setTime(date);  
		    cal1.set(Calendar.HOUR_OF_DAY, 0);  
		    cal1.set(Calendar.MINUTE, 0);  
		    cal1.set(Calendar.SECOND, 0);   
		    cal1.set(Calendar.MILLISECOND, 0);  
		    
		    Calendar cal2 = Calendar.getInstance();  
		    cal2.setTime(sdf.parse(q_date));  
		    cal2.set(Calendar.HOUR_OF_DAY, 0);  
		    cal2.set(Calendar.MINUTE, 0);  
		    cal2.set(Calendar.SECOND, 0);  
		    cal2.set(Calendar.MILLISECOND, 0);  
		    //预约日期
		   
		    
		    sql.append(" and case d.week when '7' then '1' else to_char(to_number(d.week)+1) end =to_char(to_date('"+q_date.trim()+"','yyyy-mm-dd'),'D') ");
		    
		    if(!StringUtil.isNullOrEmpty(q_period)){
		    	sql.append(" and d.work_section='"+q_period+"' ");
		    }
		    
		    //医院id
		    if(!StringUtil.isNullOrEmpty(q_hospital_id)){
		    	sql.append(" and h.hos_id="+q_hospital_id);
		    }
		    //医生职称
		    if(!StringUtil.isNullOrEmpty(q_doctor_title)){
		    	sql.append(" and m.skill_title='"+q_doctor_title+"' ");
		    }
		    //医院等级
		    if(!StringUtil.isNullOrEmpty(q_hosp_level)){
		    	sql.append("and h.hos_rank='"+q_hosp_level+"' ");
		    }
		    //医院类型
		    if(!StringUtil.isNullOrEmpty(q_hosp_type)){
		    	sql.append("and h.major_type='"+q_hosp_type+"' ");
		    }
		    //医院名称
		    if(!StringUtil.isNullOrEmpty(q_hospital_name)){
		    	sql.append("and (h.hos_name like '%"+q_hospital_name+"%' or m.staff_name like '%"+q_hospital_name+"%' or h.address like '%"+q_hospital_name+"%') ");
		    }
		    //医生名称
		    if(!StringUtil.isNullOrEmpty(q_doctor_name)){
		    	sql.append("and m.staff_name like '%"+q_doctor_name+"%' ");
		    }
		    //服务类别
		    if(!StringUtil.isNullOrEmpty(q_service_type)){
		    	sql.append("and d.queue_state ='"+q_service_type+"' ");
		    }
		    //号源类型
		    if(!StringUtil.isNullOrEmpty(q_plan_type)){
		    	sql.append("and d.work_type ='"+q_plan_type+"' ");
		    }
		    if(!StringUtil.isNullOrEmpty(ws_lat)){
		    	sql.append(" and h.longitude>"+ws_lng+" and h.longitude<"+ne_lng+" and h.latitude>"+ws_lat+" and h.latitude<"+ne_lat+" " );
		    }
		    //医保定点
		    if(!StringUtil.isNullOrEmpty(q_is_yibao)){
		    	if("0".equals(q_is_yibao)){
		    		sql.append("and h.medical_code is not null  ");
		    	}else{
		    		sql.append("and h.medical_code is null  ");
		    	}	
		    }
            if(!StringUtil.isNullOrEmpty(q_sort_field)&&"4".equals(q_sort_field)){
            	sql.append(" and (h.province='北京' or h.province='上海' or h.city='广州' or h.city='郑州' ) and h.hos_rank='01' and d.work_type='2' ");
            }
		    sql.append(" ) b where 1=1 ");
		    if(StringUtil.isNullOrEmpty(ws_lat)){
		    	sql.append(" and distance<=5000 ");
		    }
		    if(StringUtil.isNullOrEmpty(q_sort_field)||!"4".equals(q_sort_field)){
		    	
		    } 
		    
		    sql.append(" group by distance,is_yibao,hlvl,dlvl,doctor_id,doctor_name,hosp_name,hosp_lvl,hosp_dept,doctor_lvl,hosp_coordinate,doctor_sex,hosid ");
		    if(!StringUtil.isNullOrEmpty(q_sort_field)){
		    	if("4".equals(q_sort_field)){
		    		sql.append(" order by dlvl desc ");
		    	}else{
		    		sql.append("  order by "+orderMap.get(q_sort_field) );
			    	if(!StringUtil.isNullOrEmpty(q_sort_type)){
			    		sql.append(" "+q_sort_type);
			    	}
		    	}
		    	
		    }else{
		    	//偏好设置
			    if(!"".equals(prefer)){
			    	sql.append(" order by "+prefer);
			    }else{
			    	sql.append(" order by max(hylx) ");
			    }
		    }
		    count=dbm.executeQueryCount("select count(*) from ("+sql.toString()+") t");
		    String query = "SELECT * FROM " +
				     "(SELECT a.*,ROWNUM  rnn from ("+sql.toString()+") a " +
		     		 "where  ROWNUM<="+pageSize*page+") " +
	 		 		 "where rnn>"+(page-1)*pageSize+"";
			list=dbm.executeQueryListHashMap(query);
			//map.clear();
			map.put("result_cnt",count );
			map.put("result_list",list);
		}catch(Exception e){
			logger.error("查询医生列表信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: queryHospitalPlans  
	* @描述: TODO地图医院查询显示医院位置，展示医生个数
	* @作者 EbaoWeixun
	* @param q_addr_id
	* @return 
	*/  
	public List<Map<String,Object>> queryHospitalPlans(String q_date,String q_period,String q_disease,String q_service_type
			,String q_is_yibao,String q_hosp_level,String q_plan_type,String q_hosp_type,String q_addr_id,String q_doctor_title,String q_sort_field,String q_hospital_name
			,String ws_lng,String ws_lat,String ne_lng,String ne_lat,int zoom_lvl){
		DBManager_HIS dbm=new DBManager_HIS();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		String latitude="";
		String longitude="";
		String prefer="";
		try{
			//查询地址信息
			StringBuffer sql=new StringBuffer();
			sql.append("select * from reg_address where addr_id='"+q_addr_id+"'");
			map=dbm.executeQueryHashMap(sql.toString());
			longitude=map.get("LONGITUDE").toString();
			latitude=map.get("LATITUDE").toString();
			sql=new StringBuffer("select ");
			if(zoom_lvl<=11){
				sql.append(" city,count(med_id) ");
			}else{
				sql.append(" hos_id,hosp_name,hosp_lvl,longitude,latitude,count(med_id) cnt,is_yibao ,hos_type ");
			}
			
		    sql.append(" from (select distinct h.hos_id hos_id,h.hos_name hosp_name,h.major_type hos_type,case  when h.medical_code is null then '0' else '1' end is_yibao,tsh.dvalue hosp_lvl,m.med_id med_id,h.longitude longitude,h.latitude latitude,h.province province,h.city city  ");
			sql.append(",case when ("
					+"  h.LATITUDE = ''"
					+"  OR h.LATITUDE IS NULL"
					+"  OR h.LONGITUDE = ''"
					+"  OR h.LONGITUDE IS NULL) then"
					+"  0 "
					+"  else "
					+"    ("
					 +"   2 * ATAN2("
					+"      SQRT("
					       +" SIN("
					       +"   (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
					       +" ) * SIN("
					       +"   (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
					          +" ) + COS("+latitude+" * ACOS(-1) / 180) * COS(h.LATITUDE * ACOS(-1) / 180) * SIN("
					       +"   (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
					       +" ) * SIN("
					       +"   (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
					       +" )"
					       +"      ),"
					    		  +"      SQRT("
					    		  +"        1 - SIN("
					    		  +"          (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
					    		  +" ) * SIN("
					    		  +"  (h.LATITUDE - "+latitude+") * ACOS(-1) / 180 / 2"
					    		  +" ) + COS("+latitude+" * ACOS(-1) / 180) * COS(h.LATITUDE * ACOS(-1) / 180) * SIN("
					    		  +"   (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
					    		  +" ) * SIN("
					    		  +"    (h.LONGITUDE - "+longitude+") * ACOS(-1) / 180 / 2"
					    		  +"  )"
					    		  +"  )"
					    		  +"  )"
					    		  +" ) * 6378140"
					    		  +" end distance ");
			sql.append("from fac_duty_app d ");
			sql.append("left join fac_medical_app m on m.med_id=d.med_id ");
		    sql.append("left join fac_department_app t on t.dep_id=m.dep_id ");
			sql.append("left join fac_hospital_app h on h.hos_id=t.hos_id ");
			sql.append(" left join tsdict tsh on tsh.dkey=h.hos_rank and tsh.dtype='HOSP_LVL' ");

		    //疾病名称
			sql.append("where m.dep_id is not null and t.hos_id is not null ");
		    if(!StringUtil.isNullOrEmpty(q_disease)){
		    	sql.append(" and (m.good_skill like '%"+q_disease+"%' or m.standard_disease like '%"+q_disease+"%') and m.dep_id is not null ");
		    }
			//日期   和 上下午
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
		    Calendar cal1 = Calendar.getInstance();  
		    cal1.setTime(date);  
		    cal1.set(Calendar.HOUR_OF_DAY, 0);  
		    cal1.set(Calendar.MINUTE, 0);  
		    cal1.set(Calendar.SECOND, 0);  
		    cal1.set(Calendar.MILLISECOND, 0);  
		   
		    Calendar cal2 = Calendar.getInstance();  
		    cal2.setTime(sdf.parse(q_date));  
		    cal2.set(Calendar.HOUR_OF_DAY, 0);  
		    cal2.set(Calendar.MINUTE, 0);  
		    cal2.set(Calendar.SECOND, 0);  
		    cal2.set(Calendar.MILLISECOND, 0);  
		    //预约日期
		    sql.append(" and case d.week when '7' then '1' else to_char(to_number(d.week)+1) end =to_char(to_date('"+q_date.trim()+"','yyyy-mm-dd'),'D') ");
		    
	        
	        if(!StringUtil.isNullOrEmpty(q_period)){
		    	sql.append(" and d.work_section='"+q_period+"' ");
		    }
	      //医生职称
		    if(!StringUtil.isNullOrEmpty(q_doctor_title)){
		    	sql.append(" and m.skill_title='"+q_doctor_title+"' ");
		    }
		    if(!StringUtil.isNullOrEmpty(q_hospital_name)){
		    	sql.append("and (h.hos_name like '%"+q_hospital_name+"%' or m.staff_name like '%"+q_hospital_name+"%' or h.address like '%"+q_hospital_name+"%') ");
		    }
		    //医院等级
		    if(!StringUtil.isNullOrEmpty(q_hosp_level)){
		    	sql.append("and h.hos_rank='"+q_hosp_level+"' ");
		    }
		    //医院类型
		    if(!StringUtil.isNullOrEmpty(q_hosp_type)){
		    	sql.append("and h.major_type='"+q_hosp_type+"' ");
		    }
		    if(!StringUtil.isNullOrEmpty(ws_lat)){
		    	sql.append(" and h.longitude>"+ws_lng+" and h.longitude<"+ne_lng+" and h.latitude>"+ws_lat+" and h.latitude<"+ne_lat+" " );
		    }
		    //服务类别
		    if(!StringUtil.isNullOrEmpty(q_service_type)){
		    	sql.append("and d.queue_state ='"+q_service_type+"' ");
		    }
		    //号源类型
		    if(!StringUtil.isNullOrEmpty(q_plan_type)){
		    	sql.append("and d.work_type ='"+q_plan_type+"' ");
		    }
		    //医保定点
		    if(!StringUtil.isNullOrEmpty(q_is_yibao)){
		    	if("0".equals(q_is_yibao)){
		    		sql.append("and h.medical_code is not null  ");
		    	}else{
		    		sql.append("and h.medical_code is null  ");
		    	}	
		    }
		    if(!StringUtil.isNullOrEmpty(q_sort_field)&&"4".equals(q_sort_field)){
		    	sql.append(" and h.hos_rank='01' and (h.province='北京' or h.province='上海' or h.city='广州' or h.city='郑州' ) and d.work_type='2' ");
		    }
			sql.append(") b where 1=1 ");
			
			if(StringUtil.isNullOrEmpty(ws_lat)){
		    	sql.append(" and distance<=5000 ");
		    }
			if(zoom_lvl<=11){
				sql.append(" group by city");
			}else{
				sql.append(" group by hos_id,hosp_name,hosp_lvl,longitude,latitude,is_yibao,hos_type ");
			}
		    
		    
			
			list=dbm.executeQueryListHashMap(sql.toString());
		}catch(Exception e){
			logger.error("查询地图信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return list;
	}
	/**  
	* @标题: queryDetailPlans  
	* @描述: TODO查询号源接口
	* @作者 EbaoWeixun
	* @param q_addr_id
	* @return 
	*/  
	public List<Map<String,Object>> queryDetailPlans(String q_doctor_id,String q_date){
		DBManager_HIS dbm=new DBManager_HIS();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		try{
			String sql="";
			String ssql="select duty_id,week plan_date,work_section plan_period,queue_quantity plan_num,work_type ,work_fee from fac_duty_app where med_id='"+q_doctor_id+"' ";
			Date date=new Date();
		    Calendar cal1 = Calendar.getInstance();  
		    cal1.setTime(date);  
		    cal1.set(Calendar.HOUR_OF_DAY, 0);  
		    cal1.set(Calendar.MINUTE, 0);  
		    cal1.set(Calendar.SECOND, 0);  
		    cal1.set(Calendar.MILLISECOND, 0);  
		    
		    Calendar cal2 = Calendar.getInstance();  
		    cal2.setTime(sdf.parse(q_date));  
		    cal2.set(Calendar.HOUR_OF_DAY, 0);  
		    cal2.set(Calendar.MINUTE, 0);  
		    cal2.set(Calendar.SECOND, 0);  
		    cal2.set(Calendar.MILLISECOND, 0);  
		    //预约日期
		    if(cal1.compareTo(cal2)==0){
		    	map.put("rq", sdf.format(date));
		    	map.put("xq", "今日");
		    	map.put("week", dateMap.get(dateFm.format(date)));
		    	map.put("1", "");
		    	map.put("2", "");
		    	resultList.add(map);
		    	for(int i=0;i<3;i++){
		    		cal2.add(Calendar.DAY_OF_MONTH, 1);
		    		Map<String,Object> nmap=new HashMap<String,Object>();
		    		nmap.put("rq", sdf.format(cal2.getTime()));
		    		nmap.put("xq", dateFm.format(cal2.getTime()));
		    		nmap.put("week", dateMap.get(dateFm.format(cal2.getTime())));
			    	nmap.put("1", "");
			    	nmap.put("2", "");
			    	resultList.add(nmap);
		    	}
		    	//如果选的是今天
		    	sql=ssql+" and (case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd'),'D')  ";
		    	sql+=" or case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd')+1,'D') ";
		    	sql+=" or case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd')+2,'D') ";
		    	sql+=" or case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd')+3,'D') ) ";
		    }else{
		    	cal2.add(Calendar.DAY_OF_MONTH, -1);
		    	map.put("rq", sdf.format(cal2.getTime()));
		    	if(cal2.compareTo(cal1)==0){
		    		map.put("xq", "今日");
		    	}else{
		    		map.put("xq", dateFm.format(cal2.getTime()));
		    	}
		    	map.put("week", dateMap.get(dateFm.format(cal2.getTime())));
		    	map.put("1", "");
		    	map.put("2", "");
		    	resultList.add(map);
		    	for(int i=0;i<3;i++){
		    		cal2.add(Calendar.DAY_OF_MONTH, 1);
		    		Map<String,Object> nmap=new HashMap<String,Object>();
		    		nmap.put("rq", sdf.format(cal2.getTime()));
		    		nmap.put("xq", dateFm.format(cal2.getTime()));
		    		nmap.put("week", dateMap.get(dateFm.format(cal2.getTime())));
			    	nmap.put("1", "");
			    	nmap.put("2", "");
			    	resultList.add(nmap);
		    	}
		    	sql=ssql+" and (case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd')-1,'D')  ";
		    	sql+=" or case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd'),'D') ";
		    	sql+=" or case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd')+1,'D') ";
		    	sql+=" or case week when '7' then '1' else to_char(to_number(week)+1) end=to_char(to_date('"+q_date+"','yyyy-mm-dd')+2,'D') ) ";
		    }
			list=dbm.executeQueryListHashMap(sql);
			if(!list.isEmpty()){
				for(Map<String,Object> m:list){
					for(int j=0;j<resultList.size();j++){
						
						if(resultList.get(j).get("week").toString().equals(m.get("PLAN_DATE").toString())){
							
							resultList.get(j).put(m.get("PLAN_PERIOD").toString(), m);
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("查询挂号信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return resultList;
	}
	public static void main(String[] args) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
	    Calendar cal1 = Calendar.getInstance();  
	    cal1.setTime(date);  
	    cal1.set(Calendar.HOUR_OF_DAY, 0);  
	    cal1.set(Calendar.MINUTE, 0);  
	    cal1.set(Calendar.SECOND, 0);  
	    cal1.set(Calendar.MILLISECOND, 0);  
	 
	    Calendar cal2 = Calendar.getInstance();  
	    cal2.setTime(sdf.parse("2017-04-10"));  
	    cal2.set(Calendar.HOUR_OF_DAY, 0);  
	    cal2.set(Calendar.MINUTE, 0);  
	    cal2.set(Calendar.SECOND, 0);  
	    cal2.set(Calendar.MILLISECOND, 0);
	    cal2.add(Calendar.DAY_OF_MONTH, 1);
	    SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
	    System.out.println(dateFm.format(cal2.getTime()));
	}
	/**  
	* @标题: queryUserInfoList  
	* @描述: TODO查询医生信息
	* @作者 EbaoWeixun
	* @param q_user_id
	* @return
	*/  
	public Map<String,Object> queryDoctorInfo(String q_doctor_id){
		DBManager_HIS dbm=new DBManager_HIS();
		Map<String,Object> map=new HashMap<String,Object>();
		try{
			String sql="select m.sex sex,m.staff_name doctor_name,m.skill_title doctor_lvl,m.good_skill doctor_expert,h.hos_name hosp_name,ts.dvalue hosp_lvl,h.address hosp_addr,d.dep_name DEP_NAME "
            +"from fac_medical_app m "
            +"left join fac_department_app d on d.dep_id=m.dep_id " 
            +"left join fac_hospital_app h on h.hos_id=d.hos_id "
            +"left join tsdict ts on ts.dkey=h.hos_rank and ts.dtype='HOSP_LVL' "
            +"where m.med_id='"+q_doctor_id+"'";
		    map=dbm.executeQueryHashMap(sql);	
		}catch(Exception e){
			logger.error("查询医生信息出错"+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: queryUserInfoList  
	* @描述: TODO用户地址列表
	* @作者 EbaoWeixun
	* @param q_user_id
	* @return
	*/  
	public List<Map<String,Object>> queryUserInfoList(String q_user_id){
		DBManager_HIS dbm=new DBManager_HIS();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try{
			String sql="select * from reg_address where user_id ='"+q_user_id+"' order by is_default desc";
			list=dbm.executeQueryListHashMap(sql);
		}catch(Exception e){
			logger.error("查询用户列表出错："+e.getMessage());
		}finally{
			dbm.close();
		}
		return list;
	}
	/**  
	* @标题: addUserInfo  
	* @描述: TODO添加个人信息
	* @作者 EbaoWeixun
	* @param q_user_id
	* @return
	*/  
	public Map<String,Object> addUserInfo(String user_id ,String user_name,String gender,String mobile,String address,String is_default,String age,String card_no, String x, String y){
		DBManager_HIS dbm=new DBManager_HIS();
		int cnt=-1;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", 1);
		try{
			if ("1".equals(is_default))
				dbm.executeUpdate("update reg_address set IS_DEFAULT = '0' where USER_ID = '" + user_id + "'");
			
			String uuid=CommUtil.getID();
			String sql="insert into reg_address values('"+uuid+"','"+user_id+"','"+user_name+"','"+gender+"','"+age+"','"+mobile+"','"+is_default+"','"+y+"','"+x+"','"+address+"','"+card_no+"')";
			cnt=dbm.executeUpdate(sql);
			if(cnt>0){
				map.put("status", 0);
				map.put("addr_id", uuid);
			}
		}catch(Exception e){
			logger.error("添加个人信息出错："+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: modifyUserInfo  
	* @描述: TODO修改个人信息
	* @作者 EbaoWeixun
	* @param user_id
	* @param user_name
	* @param gender
	* @param mobile
	* @param address
	* @param is_default
	* @param age
	* @param card_no
	* @return
	*/  
	public Map<String,Object> modifyUserInfo(String q_addr_id,String user_name,String gender,String mobile,String address,String is_default,String age,String card_no, String x, String y){
		DBManager_HIS dbm=new DBManager_HIS();
		int cnt=-1;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", 1);
		try{
			
			String sql="update reg_address set user_name='"+user_name+"',gender='"+gender+"',age='"+age+"',mobile='"+mobile+"',is_default='"+is_default+"',address='"+address+"',card_no='"+card_no+"',LATITUDE='"+x+"',LONGITUDE='"+y+"' where addr_id='"+q_addr_id+"'";
			cnt=dbm.executeUpdate(sql);
			if(cnt>0){
				map.put("addr_id",q_addr_id );
				map.put("status", 0);
			}
		}catch(Exception e){
			logger.error("修改个人信息出错："+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	
	public Map<String,Object> delAddressInfo(String q_addr_id){
		DBManager_HIS dbm=new DBManager_HIS();
		int cnt=-1;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", 1);
		try{
			
			String sql="delete from reg_address where ADDR_ID='"+q_addr_id+"'";
			cnt=dbm.executeUpdate(sql);
			if(cnt>0){
				map.put("status", 0);
			}
		}catch(Exception e){
			logger.error("删除个人信息出错："+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: queryAddressInfo  
	* @描述: TODO查询个人地址信息
	* @作者 EbaoWeixun
	* @param q_addr_id
	* @return
	*/  
	public Map<String,Object> queryAddressInfo(String q_addr_id){
		DBManager_HIS dbm=new DBManager_HIS();
		
		Map<String,Object> map=new HashMap<String,Object>();
		
		try{
			
			String sql="select * from reg_address where addr_id='"+q_addr_id+"'";
			map = dbm.executeQueryHashMap(sql);
		}catch(Exception e){
			logger.error("查询个人地址信息出错："+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
	/**  
	* @标题: modifyUserDefault  
	* @描述: TODO修改默认地址
	* @作者 EbaoWeixun
	* @param q_user_id
	* @param q_addr_id
	* @return
	*/  
	public Map<String,Object> modifyUserDefault(String q_user_id,String q_addr_id){
		DBManager_HIS dbm=new DBManager_HIS();
		int cnt=-1;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", 1);
		try{
			String sql="update reg_address set is_default=(case addr_id when '"+q_addr_id+"' then '1' else '0' end) where user_id='"+q_user_id+"'";
			cnt=dbm.executeUpdate(sql);
			if(cnt>0){
				map.put("addr_id",q_addr_id);
				map.put("status", 0);
			}
		}catch(Exception e){
			logger.error("修改默认地址出错："+e.getMessage());
		}finally{
			dbm.close();
		}
		return map;
	}
}
