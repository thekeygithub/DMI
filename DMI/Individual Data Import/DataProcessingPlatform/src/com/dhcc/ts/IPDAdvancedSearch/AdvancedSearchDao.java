package com.dhcc.ts.IPDAdvancedSearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.mtsService.MTSUtil;
import com.dhcc.ts.mtsService.ResultModel;

import sun.util.logging.resources.logging;


public class AdvancedSearchDao {
	/**
	 * @描述：
	 * @作者：SZ
	 * @时间：2017年3月16日 上午11:27:01
	 * @param queryParam
	 * @return
	 * 数据源类型@#下拉框1@#问题1@#条件1@#下拉框2@#问题2@#条件2@#下拉框3@#问题3@#条件3@#下拉框4@#问题4@#是否加权
	 * 
	 * 数据源类型@#&开始时间@#&结束时间@#&下拉框1@#问题1@#条件1@#下拉框2@#问题2@#条件2@#下拉框3@#问题3@#条件3@#下拉框4@#问题4
	 * 
	 * AND:0
	 * OR:1
	 */
	public List<Map<String, String>> searchIPDData(String queryParam){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try{
			System.out.println(queryParam);
			String[] result = queryParam.split("@#&");
			String sjyType=result[0];//数据源类型
			String startTime = result[1];
			String endTime = result[2];
			String [] selected = result[3].split("@#");
			int counter = ((selected.length-2)/3)+1;	
			List<Map<String, String>> listNew = new ArrayList<Map<String, String>>();		
			switch (sjyType) {
				//诊断名称、主要诊断名称、药品名称、手术名称、检查名称、医院名称、科室名称、身份证号、医保卡号、姓名、住址
			/*
			 * 全部时显示人员信息，点击进入360视图页面
			 * 所有信息根据视图信息查询列表显示
			 */
				case "0"://全部    ipdSearchMap all
					for(int i=1;i<=counter;i++){
				
						if(i==1){
							list = searchIPDAllData(selected[i-1], selected[i], startTime, endTime);
						}else{
							int j=(i-2)*3+2;
							listNew = searchIPDAllData(selected[j+1], selected[j+2], startTime, endTime);
							list = equleListMap(selected[j], listNew, list, new ArrayList<Map<String, String>>());
						}
					}	
					break;
				case "1"://医院数据    ipdSearchMap 
					//诊断名称、主要诊断名称、药品名称、手术名称（无数据）、检查名称、医院名称、科室名称、身份证号、医保卡号、姓名、住址
					for(int i=1;i<=counter;i++){
						if(i==1){
							list = searchIPDHospitalData(selected[i-1], selected[i], startTime, endTime);
						}else{
							int j=(i-2)*3+2;
							listNew = searchIPDHospitalData(selected[j+1], selected[j+2], startTime, endTime);
							list = equleListMap(selected[j], listNew, list, new ArrayList<Map<String, String>>());
							
						}
					}	
					break;
				case "2"://药店数据    ipdSearchMap 
					//药品名称、身份证号、医保卡号、姓名、住址
					for(int i=1;i<=counter;i++){
						if(i==1){
							list = searchIPDDrugstoreData(selected[i-1], selected[i], startTime, endTime);
						}else{
							int j=(i-2)*3+2;
							listNew = searchIPDDrugstoreData(selected[j+1], selected[j+2], startTime, endTime);
							list = equleListMap(selected[j], listNew, list, new ArrayList<Map<String, String>>());
					
						}
					}
					break;
				case "3"://医保结算单    ipdSearchMap 
					//诊断名称、主要诊断名称、药品名称、手术名称、检查名称、医院名称、科室名称、身份证号、医保卡号、姓名、住址
					for(int i=1;i<=counter;i++){
						if(i==1){
							list = searchIPDJsdData(selected[i-1], selected[i], startTime, endTime);
						}else{
							int j=(i-2)*3+2;
							listNew = searchIPDJsdData(selected[j+1], selected[j+2], startTime, endTime);
							list = equleListMap(selected[j], listNew, list, new ArrayList<Map<String, String>>());
							
						}
					}
					break;
				case "4"://医疗可穿戴设备    ipdSearchMap 
					for(int i=1;i<=counter;i++){
						if(i==1){
							list = searchIPDUsedData(selected[i-1], selected[i], startTime, endTime);
						}else{
							int j=(i-2)*3+2;
							listNew = searchIPDUsedData(selected[j+1], selected[j+2], startTime, endTime);
							list = equleListMap(selected[j], listNew, list, new ArrayList<Map<String, String>>());
					
						}
					}
					break;
				case "5"://APP使用行为    ipdSearchMap 
					for(int i=1;i<=counter;i++){
						if(i==1){
							list = searchIPDAPPData(selected[i-1], selected[i], startTime, endTime);
						}else{
							int j=(i-2)*3+2;
							listNew = searchIPDAPPData(selected[j+1], selected[j+2], startTime, endTime);
							list = equleListMap(selected[j], listNew, list, new ArrayList<Map<String, String>>());
						}
					}
					break;
				case "6"://个人健康档案    ipdSearchMap 
					//诊断名称、主要诊断名称、药品名称、手术名称（无数据）、检查名称、医院名称、科室名称、身份证号、医保卡号、姓名、住址
					for(int i=1;i<=counter;i++){
						if(i==1){
							list = searchIPDGRJKDAData(selected[i-1], selected[i], startTime, endTime);
						}else{
						
							int j=(i-2)*3+2;
							listNew = searchIPDGRJKDAData(selected[j+1], selected[j+2], startTime, endTime);
							list = equleListMap(selected[j], listNew, list, new ArrayList<Map<String, String>>());
						}
					}
					break;
				default:
					break;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		return addListModel(list, new ArrayList<Map<String, String>>());
	}
	/**
	 * @描述：根据and或者是or来拼装结果集
	 * @作者：SZ
	 * @时间：2017年3月16日 下午8:32:40
	 * @param type 0:and  1:or  2:not
	 * @param list1
	 * @param list2
	 * @param returnlist
	 * @return
	 */
	public List<Map<String, String>> equleListMap(String type,List<Map<String, String>> list1,List<Map<String, String>> list2,List<Map<String, String>> returnlist){
		if("0".equals(type)){
			for(Map<String, String> map1:list1){
				for(Map<String, String> map2:list2){
					if(map1.equals(map2)){
						returnlist.add(map1);
						break;
					}
				}
			}
		}else if("1".equals(type)){
			returnlist.addAll(list1);
			returnlist.addAll(list2);
		}else if("2".equals(type)){
			for(Map<String, String> map2:list2){
				int result = 0;
				for(Map<String, String> map1:list1){
					if(map2.equals(map1)){
						result = 1;
						break;
					}
				}
				if(0 == result){
					returnlist.add(map2);
				}
			}
		}
		return returnlist;
	}
	/**
	 * @描述：调取MTS结果，增加查询参数
	 * @作者：SZ
	 * @时间：2017年3月16日 下午8:02:25
	 * @param str
	 * @return
	 */
	public List<String> queryMTS(String str){
		List<ResultModel> list = MTSUtil.getMTSResult(str, null);
		List<String> listResult = new ArrayList<String>();
		for(ResultModel model : list){
			if(!StringUtil.isNullOrEmpty(model.getNlpresult())){
				listResult.add(model.getNlpresult());
			}
			if(!StringUtil.isNullOrEmpty(model.getMtsresult())){
				listResult.add(model.getMtsresult());
			}
		}
		System.out.println("MTS标准化结果："+listResult);
		if(listResult.isEmpty()){
			listResult.add(str);
		}
		System.out.println("MTS标准化结果："+listResult);
		return listResult;
	}
	
	/**
	 * @描述：返回开头为OR的结果集
	 * @作者：SZ
	 * @时间：2017年3月16日 下午7:30:43
	 * @param field
	 * @param queryList
	 * @return
	 */
	public String addStr(String field,List<String> queryList){
		StringBuffer sb = new StringBuffer();
		for(String str : queryList){
			sb.append("OR ");
			sb.append(field);
			sb.append(" like ");
			sb.append("'%");
			sb.append(str);
			sb.append("%' ");
		}
		return sb.toString();
	}
	
	/**
	 * @描述：结果集去重 按key去重结果集
	 * @作者：SZ
	 * @时间：2017年3月17日 上午9:39:18
	 * @param before
	 * @param after
	 * @return
	 */
	private List<Map<String, String>> addListModel(List<Map<String, String>> before,List<Map<String, String>> after){
		Set<String> card_noSet = new HashSet<String>();
		int temp = card_noSet.size();
		for(Map<String, String> map : before){
			card_noSet.add(map.get("ID"));
			if(card_noSet.size()>temp){
				after.add(map);
				temp +=1;
			}
		}
		return after;
	}
	/**
	 * @描述：全部查询，返回IPD普通检索列表
	 * @作者：SZ
	 * @时间：2017年3月28日 上午8:51:34
	 * @param type
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> searchIPDAllData(String type,String query,String starttime,String endtime){
		DBManager_HIS dbm = new DBManager_HIS();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<String> queryList = null;//需要查询的list集合
		try {
			switch(type){
				case "1"://诊断名称  门诊 住院
					queryList = queryMTS(query);//需要查询的list集合
					String sqlmz = "SELECT DISTINCT MP.* FROM H_REG HR "
							+ "LEFT JOIN H_RECIPE HRE ON HR.CMZH = HRE.CMZH "
							+ "LEFT JOIN MV_PATIENT MP ON HR.CSFZH = MP.CARD_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND ";
					String sqlzy = "SELECT DISTINCT MP.* FROM H_VISIT HV "
							+ "LEFT JOIN MV_PATIENT MP ON HV.CSFZH = MP.CARD_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND ";
					if(queryList.isEmpty()){
						sqlmz += " HRE.CCFZD LIKE '%"+query+"%'";
						sqlzy += " HV.CMZZDMC LIKE '%"+query+"%'";
					}else{
						sqlmz += "("+addStr("HRE.CCFZD", queryList).replaceFirst("OR", "");
						sqlmz += addStr("HRE.MTS_ZD", queryList)+")";
						sqlzy += "("+addStr("HV.CMZZDMC", queryList).replaceFirst("OR", "");
						sqlzy += addStr("HV.MTS_ZD", queryList)+")";
					}
					if(!StringUtil.isNullOrEmpty(starttime)){
						sqlmz+= " AND HR.DGH>='"+starttime.replaceAll("/", "-")+"' ";
						sqlzy+= " AND HV.DRYSJ>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						sqlmz+= " AND HR.DGH<='"+endtime.replaceAll("/", "-")+"' ";
						sqlzy+= " AND HV.DRYSJ<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String zdsql = "SELECT DISTINCT * FROM ("+sqlmz+" UNION ALL "+sqlzy+") AA ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(zdsql);
					break;
				case "2"://主要诊断名称，第一诊断
					queryList = queryMTS(query);//需要查询的list集合
					String sqlfmz = "SELECT DISTINCT MP.* FROM H_REG HR "
							+ "LEFT JOIN H_RECIPE HRE ON HR.CMZH = HRE.CMZH "
							+ "LEFT JOIN MV_PATIENT MP ON HR.CSFZH = MP.CARD_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND";
					String sqlfzy = "SELECT DISTINCT MP.* FROM H_VISIT HV "
							+ "LEFT JOIN MV_PATIENT MP ON HV.CSFZH = MP.CARD_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND";
					if(queryList.isEmpty()){
						sqlfmz += " HRE.FIRST_ZD LIKE '%"+query+"%'";
						sqlfzy += " HV.FIRST_ZD LIKE '%"+query+"%'";
					}else{
						sqlfmz += "("+addStr("HRE.FIRST_ZD", queryList).replaceFirst("OR", "")+")";
						sqlfzy += "("+addStr("HV.FIRST_ZD", queryList).replaceFirst("OR", "")+")";
					}
					if(!StringUtil.isNullOrEmpty(starttime)){
						sqlfmz+= " AND HR.DGH>='"+starttime.replaceAll("/", "-")+"' ";
						
						sqlfzy+= " AND HV.DRYSJ>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						sqlfmz+= " AND HR.DGH<='"+endtime.replaceAll("/", "-")+"' ";
						sqlfzy+= " AND HV.DRYSJ<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String fzdsql = "SELECT DISTINCT * FROM ("+sqlfmz+" UNION ALL "+sqlfzy+") AA ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(fzdsql);
					break;
				case "3"://药品名称     处方明细和医嘱
					queryList = queryMTS(query);//需要查询的list集合
					String ypsql = "SELECT DISTINCT MP.* FROM MV_DIAG MD "
							+ "LEFT JOIN MV_PATIENT MP ON MP.CARD_NO = MD.CARD_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND ";
					if(queryList.isEmpty()){
						ypsql += " MD.YP LIKE '%"+query+"%'";
					}else{
						ypsql += "("+addStr("MD.YP", queryList).replaceFirst("OR", "");
						ypsql += addStr("MD.MTS_Y", queryList)+")";
					}
					if(!StringUtil.isNullOrEmpty(starttime)){
						ypsql+= " AND MD.JZSJ>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						ypsql+= " AND MD.JZSJ<='"+endtime.replaceAll("/", "-")+"' ";
					}
					ypsql +=" ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(ypsql);
					break;
				case "4"://手术名称
					
					break;
				case "5"://检查名称
					String jcsql = "SELECT DISTINCT * FROM (SELECT DISTINCT MP.* FROM H_REG HR "
							+ "LEFT JOIN MV_PATIENT MP ON HR.CSFZH = MP.CARD_NO "
							+ "LEFT JOIN H_EXAM HE ON HR.CMZH = HE.S_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND HE.TITLE LIKE '%"+query+"%' "
							+ "UNION ALL "
							+ "SELECT DISTINCT MP.* FROM H_VISIT HV "
							+ "LEFT JOIN MV_PATIENT MP ON HV.CSFZH = MP.CARD_NO "
							+ "LEFT JOIN H_EXAM HE ON HV.CZYH = HE.H_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND HE.TITLE LIKE '%"+query+"%'"
							+ ") AA ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(jcsql);
					break;
				case "6"://身份证号 CARD_NO
					String sfzhsql = "SELECT * FROM MV_PATIENT WHERE CARD_NO LIKE '%"+query+"%' ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(sfzhsql);
					break;
				case "7"://医保卡号
					String sbsql = "SELECT * FROM MV_PATIENT WHERE SB_CARD_NO LIKE '%"+query+"%' ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(sbsql);
					break;
				case "8"://姓名查询  
					String xmsql = "SELECT * FROM MV_PATIENT WHERE NAME LIKE '%"+query+"%' ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(xmsql);
					break;
				case "9"://住址查询
					String addrsql = "SELECT * FROM MV_PATIENT WHERE ADDRESS LIKE '%"+query+"%' ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(addrsql);
					break;
				case "10"://医院名称
					String yysql = "SELECT DISTINCT * FROM ("
							+ "SELECT DISTINCT MP.* FROM H_REG HR "
							+ "LEFT JOIN MV_PATIENT MP ON HR.CSFZH = MP.CARD_NO "
							+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HR.CORP_ID "
							+ "WHERE MP.CARD_NO IS NOT NULL AND FH.HOS_NAME LIKE '%"+query+"%' "
							+ "UNION ALL "
							+ "SELECT DISTINCT MP.* FROM H_VISIT HV "
							+ "LEFT JOIN MV_PATIENT MP ON HV.CSFZH = MP.CARD_NO "
							+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HV.CORP_ID "
							+ "WHERE MP.CARD_NO IS NOT NULL AND FH.HOS_NAME LIKE '%"+query+"%' "
							+ ") AA ORDER BY ORDERS,RN";
					
					list = dbm.executeQueryListHashMap(yysql);
					break;
				case "11"://科室名称
					String kssql = "SELECT DISTINCT * FROM ("
							+ "SELECT DISTINCT MP.* FROM H_REG HR "
							+ "LEFT JOIN MV_PATIENT MP ON HR.CSFZH = MP.CARD_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND HR.CKSMC LIKE '%"+query+"%' "
							+ "UNION ALL "
							+ "SELECT DISTINCT MP.* FROM H_VISIT HV "
							+ "LEFT JOIN MV_PATIENT MP ON HV.CSFZH = MP.CARD_NO "
							+ "WHERE MP.CARD_NO IS NOT NULL AND HV.CRYKSMC LIKE '%"+query+"%'"
							+ ") AA ORDER BY ORDERS,RN";
					list = dbm.executeQueryListHashMap(kssql);
					break;
				case "12"://app查询药名
					break;
				case "13"://app购买药名
					break;
				case "14"://血压
					break;
				case "15"://血糖
					break;
				default: 
		          break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbm.close();
		return list;
	}
	
	/**
	 * @描述：个人健康档案查询
	 * @作者：SZ
	 * @时间：2017年3月28日 上午8:50:35
	 * @param type
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> searchIPDGRJKDAData(String type,String query,String starttime,String endtime){
		DBManager_HIS dbm = new DBManager_HIS();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listbefore = new ArrayList<Map<String, String>>();
		List<String> queryList = null;//需要查询的list集合
		try {
			switch(type){
				case "1"://诊断名称 
					queryList = queryMTS(query);//需要查询的list集合
					String zdsql ="SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "h.department as corp,e.doctor as dcname,e.user_name as cxm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id "
							+ "WHERE ";
							if(queryList.isEmpty()){
								zdsql += " e.r9_1 LIKE '%"+query+"%' or r9_2 LIKE '%"+query+"%'";
							}else{
								zdsql += addStr("e.r9_1", queryList).replaceFirst("OR", "");
								zdsql += addStr("e.r9_2", queryList);
								zdsql += addStr("e.mts_r9_1", queryList);
								zdsql += addStr("e.mts_r9_2", queryList);
							}
							if(!StringUtil.isNullOrEmpty(starttime)){
								zdsql+= "AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
							}
							if(!StringUtil.isNullOrEmpty(endtime)){
								zdsql+= "AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
							}
							
					listbefore = dbm.executeQueryListHashMap(zdsql);
					list = addListModel(listbefore, list);
					break;
				case "2"://主要诊断名称，第一诊断
					
					break;
				case "3"://药品名称
					queryList = queryMTS(query);//需要查询的list集合
					/*String jktjyp = "SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,h.department as corp,"
							+ "e.doctor as dcname,e.user_name as xm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id "
							+ "WHERE ";
							if(queryList.isEmpty()){
								jktjyp += " e.r10 like '%"+query+"%'";
							}else{
								jktjyp += addStr("e.r10", queryList).replaceFirst("OR", "");
							}
							if(!StringUtil.isNullOrEmpty(starttime)){
								jktjyp+= "AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd') ";
							}
							if(!StringUtil.isNullOrEmpty(endtime)){
								jktjyp+= "AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd')+1 ";
							}*///没有数据不显示
					String xtsfsql = "SELECT f.id,to_char(f.r1,'yyyy-mm-dd') as rdate,"
							+ "CASE WHEN f.r2='1' THEN c.department ELSE c.present_addr END corp,f.r14 as dcname,f.user_name as cxm,'糖尿病随访' as detail "
							+ "FROM h_following_xt f "
							+ "LEFT JOIN h_cover c ON c.id=f.user_id  "
							+ "WHERE ";
					String xysfsql = " SELECT y.id,to_char(y.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "CASE WHEN y.r2='1' THEN v.department ELSE v.present_addr END corp,y.r13 as dcname,y.user_name as cxm,'高血压随访' as detail "
							+ "FROM h_following_xy y "
							+ "LEFT JOIN h_cover v ON v.id=y.user_id "
							+ "WHERE ";
					if(queryList.isEmpty()){
						xtsfsql += " f.r11_a like '%"+query+"%' or f.r11_b like '%"+query+"%' ";
						xysfsql += " y.r10_a like '%"+query+"%' or y.r10_b like '%"+query+"%' ";
					}else{
						xtsfsql += addStr("f.r11_a", queryList).replaceFirst("OR", "");
						xtsfsql += addStr("f.mts_r11_a", queryList);
						xtsfsql += addStr("f.r11_b", queryList);
						xtsfsql += addStr("f.mts_r11_b", queryList);
						xysfsql += addStr("y.r10_a", queryList).replaceFirst("OR", "");
						xysfsql += addStr("y.mts_r10_a", queryList);
						xysfsql += addStr("y.r10_b", queryList);
						xysfsql += addStr("y.mts_r10_b", queryList);
					}
					if(!StringUtil.isNullOrEmpty(starttime)){
						xtsfsql += " AND f.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')";
						xysfsql += " AND y.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss')";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						xtsfsql += " AND f.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1";
						xysfsql += " AND y.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1";
					}
					String ypsql = xtsfsql + " UNION ALL " + xysfsql;
					
					listbefore = dbm.executeQueryListHashMap(ypsql);
					list = addListModel(listbefore, list);
					break;
				case "4"://手术名称
					
					break;
				case "5"://检查名称
					String jxsql = "SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,h.department as corp "
							+ ",e.doctor as dcname,e.user_name as cxm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id "
							+ "WHERE ((case when '血常规' like '%"+query+"%' then e.r6_1 else null end) is not null or "
							+ "(case when '尿常规' like '%"+query+"%' then e.r6_2 else null end) is not null or "
							+ "(case when '空腹血糖' like '%"+query+"%' then e.r6_3 else null end) is not null or "
							+ "(case when '心电图' like '%"+query+"%' then e.r6_4 else null end) is not null or "
							+ "(case when '尿微量白蛋白' like '%"+query+"%' then e.r6_5 else null end) is not null or "
							+ "(case when '大便潜血' like '%"+query+"%' then e.r6_6 else null end) is not null or "
							+ "(case when '糖化血红蛋白' like '%"+query+"%' then e.r6_7 else null end) is not null or "
							+ "(case when '乙型肝炎表面抗原' like '%"+query+"%' then e.r6_8 else null end) is not null or "
							+ "(case when '肝功能' like '%"+query+"%' then e.r6_9 else null end) is not null or "
							+ "(case when '肾功能' like '%"+query+"%' then e.r6_10 else null end) is not null or "
							+ "(case when '血脂' like '%"+query+"%' then e.r6_11 else null end) is not null or "
							+ "(case when '胸部X线片' like '%"+query+"%' then e.r6_12 else null end) is not null or "
							+ "(case when 'B超' like '%"+query+"%' then e.r6_13 else null end) is not null or "
							+ "(case when '宫颈涂片' like '%"+query+"%' then e.r6_14 else null end) is not null ) ";
					
					if(!StringUtil.isNullOrEmpty(starttime)){
						jxsql+= " AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						jxsql+= " AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
					}
					listbefore = dbm.executeQueryListHashMap(jxsql);
					list = addListModel(listbefore, list);
					break;
				case "6"://身份证号 CARD_NO
					String jkdasfzhsql = " SELECT a.doc_no as id,to_char(a.create_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "a.department as corp,a.doctor as dcname,a.user_name as cxm,'健康档案建档' detail "
							+ "FROM h_cover a "
							+ "WHERE a.id like '%"+query+"%'";
					String jktjsfzhsql = "SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "h.department as corp,e.doctor as dcname,e.user_name as cxm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id"
							+ " WHERE h.id like '%"+query+"%'";
					String mbsfxtsfzhsql = "SELECT f.id,to_char(f.r1,'yyyy-mm-dd') as rdate,"
							+ "CASE WHEN f.r2='1' THEN c.department ELSE c.present_addr END corp,f.r14 as dcname,f.user_name as cxm,'糖尿病随访' as detail "
							+ "FROM h_following_xt f "
							+ "LEFT JOIN h_cover c ON c.id=f.user_id "
							+ "WHERE f.user_id like '%"+query+"%'";
					String mbsfxysfzhsql = " SELECT y.id,to_char(y.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "CASE WHEN y.r2='1' THEN v.department ELSE v.present_addr END corp,y.r13 as dcname,y.user_name as cxm,'高血压随访' as  detail "
							+ "FROM h_following_xy y "
							+ "LEFT JOIN h_cover v ON v.id=y.user_id "
							+ "WHERE y.user_id like '%"+query+"%'";
	             	if(!StringUtil.isNullOrEmpty(starttime)){
	             		jkdasfzhsql+= " AND a.create_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
	             		jktjsfzhsql+= " AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
	             		mbsfxtsfzhsql+= " AND f.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
	             		mbsfxysfzhsql+= " AND y.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						jkdasfzhsql+= " AND a.create_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						jktjsfzhsql+= " AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxtsfzhsql+= " AND f.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxysfzhsql+= " AND y.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
					}
					String sfzhsql = jkdasfzhsql + " UNION ALL " + jktjsfzhsql+ " UNION ALL " + mbsfxtsfzhsql+ " UNION ALL " + mbsfxysfzhsql;
					listbefore = dbm.executeQueryListHashMap(sfzhsql);
					list = addListModel(listbefore, list);
					break;
				case "7"://医保卡号
					String jkdaybsql = "SELECT a.doc_no as id,to_char(a.create_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "a.department as corp,a.doctor as dcname,a.user_name as cxm,'健康档案建档' detail "
							+ "FROM h_cover a "
							+ "INNER JOIN mv_patient p ON p.card_no=a.id "
							+ "WHERE p.sb_card_no like '%"+query+"%'";
					String jktjybsql = "SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,h.department as corp,"
							+ "e.doctor as dcname,e.user_name as cxm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id "
							+ "INNER JOIN mv_patient p ON p.card_no=e.id "
							+ "WHERE p.sb_card_no like '%"+query+"%' ";
					String mbsfxtybsql = "SELECT f.id,to_char(f.r1,'yyyy-mm-dd') as rdate,"
							+ "CASE WHEN f.r2='1' THEN c.department ELSE c.present_addr END corp,f.r14 as dcname,f.user_name as cxm,'糖尿病随访' as detail "
							+ "FROM h_following_xt f "
							+ "LEFT JOIN h_cover c ON c.id=f.user_id "
							+ "INNER JOIN mv_patient p ON p.card_no=f.user_id "
							+ " WHERE p.sb_card_no like '%"+query+"%'";
					String mbsfxyybsql = "SELECT y.id,to_char(y.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "CASE WHEN y.r2='1' THEN v.department ELSE v.present_addr END corp,y.r13 as dcname,y.user_name as cxm,'高血压随访' as  detail "
							+ "FROM h_following_xy y "
							+ "LEFT JOIN h_cover v ON v.id=y.user_id "
							+ "INNER JOIN mv_patient p ON p.card_no=y.user_id "
							+ "WHERE p.sb_card_no like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						jkdaybsql+= " AND a.create_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						jktjybsql+= " AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxtybsql+= " AND f.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxyybsql+= " AND y.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						jkdaybsql+= " AND a.create_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						jktjybsql+= " AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxtybsql+= " AND f.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxyybsql+= " AND y.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
					}
					String sbsql = jkdaybsql + " UNION ALL " + jktjybsql+ " UNION ALL " + mbsfxtybsql+ " UNION ALL " + mbsfxyybsql;
					listbefore = dbm.executeQueryListHashMap(sbsql);
					list = addListModel(listbefore, list);
					break;
				case "8"://姓名查询  
					String jkdaxmsql = "SELECT a.doc_no as id,to_char(a.create_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "a.department as corp,a.doctor as dcname,a.user_name as cxm ,'健康档案建档' detail "
							+ "FROM h_cover a "
							+ "WHERE a.user_name like '%"+query+"%'";
					String jktjxmsql = "SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "h.department as corp,e.doctor as dcname,e.user_name as cxm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id "
							+ "WHERE e.user_name like '%"+query+"%'";
					String mbsfxtxmsql = "SELECT f.id,to_char(f.r1,'yyyy-mm-dd') as rdate,"
							+ "CASE WHEN f.r2='1' THEN c.department ELSE c.present_addr END corp,f.r14 as dcname,f.user_name as cxm,'糖尿病随访' as detail "
							+ "FROM h_following_xt f "
							+ "LEFT JOIN h_cover c ON c.id=f.user_id "
							+ "WHERE f.user_name like '%"+query+"%'";
					String mbsfxyxmsql = " SELECT y.id,to_char(y.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "CASE WHEN y.r2='1' THEN v.department ELSE v.present_addr END corp,y.r13 as dcname,y.user_name as cxm,'高血压随访' as  detail "
							+ "FROM h_following_xy y "
							+ "LEFT JOIN h_cover v ON v.id=y.user_id "
							+ " WHERE y.user_name like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						jkdaxmsql+= " AND a.create_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						jktjxmsql+= " AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxtxmsql+= " AND f.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxyxmsql+= " AND y.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						jkdaxmsql+= " AND a.create_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						jktjxmsql+= " AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxtxmsql+= " AND f.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxyxmsql+= " AND y.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
					}
					String xmsql = jkdaxmsql + " UNION ALL " + jktjxmsql+ " UNION ALL " + mbsfxtxmsql+ " UNION ALL " + mbsfxyxmsql;
					listbefore = dbm.executeQueryListHashMap(xmsql);
					list = addListModel(listbefore, list);
					break;
				case "9"://住址查询
					String jkdazzsql = "SELECT a.doc_no as id,to_char(a.create_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "a.department as corp,a.doctor as dcname,a.user_name as cxm,'健康档案建档' detail "
							+ "FROM h_cover a "
							+ "WHERE a.present_addr like '%"+query+"%'";
					String jktjzzsql = "SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "h.department as corp,e.doctor as dcname,e.user_name as cxm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id "
							+ "WHERE e.present_addr like '%"+query+"%'";
					String mbsfxtzzsql = "SELECT f.id,to_char(f.r1,'yyyy-mm-dd') as rdate,"
							+ "CASE WHEN f.r2='1' THEN c.department ELSE c.present_addr END corp,f.r14 as dcname,f.user_name as cxm,'糖尿病随访' as detail "
							+ "FROM h_following_xt f "
							+ "LEFT JOIN h_cover c ON c.id=f.user_id "
							+ "WHERE f.present_addr like '%"+query+"%'";
					String mbsfxyzzsql = " SELECT y.id,to_char(y.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "CASE WHEN y.r2='1' THEN v.department ELSE v.present_addr END corp,y.r13 as dcname,y.user_name as cxm,'高血压随访' as  detail "
							+ "FROM h_following_xy y "
							+ "LEFT JOIN h_cover v ON v.id=y.user_id "
							+ " WHERE y.present_addr like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						jkdazzsql+= " AND a.create_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						jktjzzsql+= " AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxtzzsql+= " AND f.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxyzzsql+= " AND y.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						jkdazzsql+= " AND a.create_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						jktjzzsql+= " AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxtzzsql+= " AND f.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxyzzsql+= " AND y.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
					}
					String zzsql = jkdazzsql + " UNION ALL " + jktjzzsql+ " UNION ALL " + mbsfxtzzsql+ " UNION ALL " + mbsfxyzzsql;
					listbefore = dbm.executeQueryListHashMap(zzsql);
					list = addListModel(listbefore, list);
					break;
				case "10"://医院名称
					String jkadyysql = "SELECT a.doc_no as id,to_char(a.create_date,'yyyy-mm-dd hh24:mi:ss') as rdate,a.department as corp "
							+ ",a.doctor as dcname,a.user_name as cxm,'健康档案建档' detail "
							+ "FROM h_cover a "
							+ "WHERE a.department like '%"+query+"%' ";
					String jktjyysql = "SELECT e.doc_no id,to_char(e.ex_date,'yyyy-mm-dd hh24:mi:ss') as rdate,h.department as corp,"
							+ "e.doctor as dcname,e.user_name as cxm,'健康体检' as detail "
							+ "FROM h_physical_examination e "
							+ "LEFT JOIN h_cover h ON h.id=e.id "
							+ "WHERE h.department like '%"+query+"%'";
					String mbsfxtyysql = "SELECT f.id,to_char(f.r1,'yyyy-mm-dd') as rdate,"
							+ "CASE WHEN f.r2='1' THEN c.department ELSE c.present_addr END corp,f.r14 as dcname,f.user_name as cxm,'糖尿病随访' as detail "
							+ "FROM h_following_xt f "
							+ "LEFT JOIN h_cover c ON c.id=f.user_id "
							+ "WHERE (CASE WHEN f.r2='1' then c.department else null end) like '%"+query+"%'";
					String mbsfxyyysql = "SELECT y.id,to_char(y.r1,'yyyy-mm-dd hh24:mi:ss') as rdate,"
							+ "CASE WHEN y.r2='1' THEN v.department ELSE v.present_addr END corp,y.r13 as dcname,y.user_name as cxm,'高血压随访' as  detail "
							+ "FROM h_following_xy y "
							+ "LEFT JOIN h_cover v ON v.id=y.user_id "
							+ "WHERE (CASE WHEN y.r2='1' then v.department else null end) like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						jkadyysql+= " AND a.create_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						jktjyysql+= " AND e.ex_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxtyysql+= " AND f.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
						mbsfxyyysql+= " AND y.r1>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss') ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						jkadyysql+= " AND a.create_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						jktjyysql+= " AND e.ex_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxtyysql+= " AND f.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
						mbsfxyyysql+= " AND y.r1<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss')+1 ";
					}
					
					String yysql = jkadyysql + " UNION ALL " + jktjyysql+ " UNION ALL " + mbsfxtyysql+ " UNION ALL " + mbsfxyyysql;
					listbefore = dbm.executeQueryListHashMap(yysql);
					list = addListModel(listbefore, list);
					break;
				case "11"://科室名称
					break;
				case "12"://app查询药名
					queryList = queryMTS(query);//需要查询的list集合
					break;
				case "13"://app购买药名
					queryList = queryMTS(query);//需要查询的list集合
					break;
				case "14"://血压
					break;
				case "15"://血糖
					break;
				default: 
		          break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbm.close();
		}
		return list;
	}
	
	/**
	 * @描述：医院检索
	 * @作者：SZ
	 * @时间：2017年3月29日 上午9:08:41
	 * @param type
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> searchIPDHospitalData(String type,String query,String starttime,String endtime){
		DBManager_HIS dbm = new DBManager_HIS();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listbefore = new ArrayList<Map<String, String>>();
		List<String> queryList = null;//需要查询的list集合
		try {
			switch(type){
				case "1"://诊断名称
					queryList = queryMTS(query);//需要查询的list集合
					String mzsql = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "WHERE ";
					
					String zysql = "SELECT v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "WHERE ";
					if(queryList.isEmpty()){
						mzsql += " (r.ccfzd like '%"+query+"%' or r.mts_zd like '%"+query+"%') ";
						zysql += " (v.cmzzdmc like '%"+query+"%' or v.mts_zd like '%"+query+"%') ";
					}else{
						mzsql += addStr("r.ccfzd", queryList).replaceFirst("OR", "");
						mzsql += addStr("r.mts_zd", queryList);
						zysql += addStr("v.cmzzdmc", queryList).replaceFirst("OR", "");
						zysql += addStr("v.mts_zd", queryList);
					}
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzsql+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zysql += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzsql+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zysql+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String zdsql = mzsql +" UNION ALL "+ zysql ;
					
					listbefore = dbm.executeQueryListHashMap(zdsql);
					list = addListModel(listbefore, list);
					break;
				case "2"://主要诊断名称，第一诊断
					queryList = queryMTS(query);//需要查询的list集合
					String mzzsql = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "WHERE ";
					
					String zyzsql = "SELECT v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "WHERE ";
					if(queryList.isEmpty()){
						mzzsql += " r.first_zd like '%"+query+"%' ";
						zyzsql += " v.first_zd like '%"+query+"%' ";
					}else{
						mzzsql += addStr("r.first_zd", queryList).replaceFirst("OR", "");
						zyzsql += addStr("v.first_zd", queryList).replaceFirst("OR", "");
					}
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzzsql+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyzsql += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzzsql+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyzsql+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String zyzdsql = mzzsql +" UNION ALL "+ zyzsql ;
					listbefore = dbm.executeQueryListHashMap(zyzdsql);
					list = addListModel(listbefore, list);
					break;
				case "3"://药品名称
					queryList = queryMTS(query);//需要查询的list集合
					String mzyp = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "LEFT JOIN h_recipe_detail rd ON rd.ccfh=r.ccfh "
							+ "WHERE ";
					String zyyp = "SELECT distinct v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "LEFT JOIN h_advice a ON a.czyh=v.czyh "
							+ "WHERE ";
					if(queryList.isEmpty()){
						mzyp += " (rd.cypmc like '%"+query+"%' or rd.mts_yp like '%"+query+"%') ";
						zyyp += " (a.cypmc like '%"+query+"%' or a.mts_yz like '%"+query+"%') ";
					}else{
						mzyp += addStr("rd.cypmc", queryList).replaceFirst("OR", "");
						mzyp += addStr("rd.mts_yp", queryList);
						zyyp += addStr("a.cypmc", queryList).replaceFirst("OR", "");
						zyyp += addStr("a.mts_yz", queryList);
					}
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzyp+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyyp += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzyp+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyyp+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String ypsql = mzyp +" UNION ALL "+ zyyp ;
					listbefore = dbm.executeQueryListHashMap(ypsql);
					list = addListModel(listbefore, list);
					break;
				case "4"://手术名称
					
					break;
				case "5"://检查名称
					String mzjc = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "LEFT JOIN h_exam a ON a.s_no=m.cmzh "
							+ "WHERE a.title like '%"+query+"%'";
					String zyjc = "SELECT distinct v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "LEFT JOIN h_exam a ON v.czyh=a.h_no "
							+ "WHERE a.title like '%"+query+"%'";
					
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzjc+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyjc += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzjc+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyjc+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String jcsql = mzjc+" UNION ALL "+zyjc;
					listbefore = dbm.executeQueryListHashMap(jcsql);
					list = addListModel(listbefore, list);
					break;
				case "6"://身份证号 CARD_NO
					String mzsfzh = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "WHERE m.csfzh like '%"+query+"%'";
					String zysfzh = "SELECT  v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "WHERE v.csfzh like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzsfzh+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zysfzh += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzsfzh+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zysfzh+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String sfzhsql = mzsfzh+" UNION ALL "+zysfzh;
					listbefore = dbm.executeQueryListHashMap(sfzhsql);
					list = addListModel(listbefore, list);
					break;
				case "7"://医保卡号
					String mzyb = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "INNER JOIN mv_patient t ON t.card_no=m.csfzh "
							+ "WHERE t.sb_card_no like '%"+query+"%'";
					String zyyb = "SELECT  v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "INNER JOIN mv_patient t ON t.card_no=v.csfzh "
							+ "WHERE t.sb_card_no like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzyb+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyyb += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzyb+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyyb+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String ybksql = zyyb + " UNION ALL " + mzyb;
					listbefore = dbm.executeQueryListHashMap(ybksql);
					list = addListModel(listbefore, list);
					break;
				case "8"://姓名查询  
					String mzname = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "WHERE m.cxm like '%"+query+"%'";
					String zyname = "SELECT  v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "WHERE v.cxm like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzname+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyname += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzname+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyname+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String namesql = mzname + " UNION ALL " + zyname;
					listbefore = dbm.executeQueryListHashMap(namesql);
					list = addListModel(listbefore, list);
					break;
				case "9"://住址查询
					String mzaddr = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "INNER JOIN mv_patient t ON t.card_no=m.csfzh "
							+ "WHERE t.address like '%"+query+"%'";
					String zyaddr = "SELECT  v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "INNER JOIN mv_patient t ON t.card_no=v.csfzh "
							+ "WHERE t.address like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzaddr+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyaddr += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzaddr+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyaddr+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String addrsql = mzaddr + " UNION ALL " + zyaddr;
					listbefore = dbm.executeQueryListHashMap(addrsql);
					list = addListModel(listbefore, list);
					break;
				case "10"://医院名称
					String mzyy = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "WHERE p.hos_name like '%"+query+"%'";
					
					String zyyy = "SELECT  v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "WHERE c.hos_name like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzyy+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyyy += " AND v.drysj>='"+starttime+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzyy+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyyy+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String yysql = mzyy+" UNION ALL "+zyyy;
					listbefore = dbm.executeQueryListHashMap(yysql);
					list = addListModel(listbefore, list);
					break;
				case "11"://科室名称
					String mzks = "SELECT distinct m.cmzh id,m.dgh rdate,m.cxm xm,m.csfzh sfzh,"
							+ "'门诊' as dtype,m.cksmc unit,p.hos_name corp,r.ccfzd detail,p.hos_id corp_id ,m.cysmc as dcname "
							+ "FROM h_reg m "
							+ "LEFT JOIN fac_hospital p ON p.hos_id=m.corp_id "
							+ "LEFT JOIN h_recipe r ON r.cmzh=m.cmzh "
							+ "WHERE m.cksmc like '%"+query+"%'";
					String zyks = "SELECT  v.czyh id,v.drysj rdate,v.cxm xm,v.csfzh sfzh,'住院' as dtype,"
							+ "v.cryksmc unit,c.hos_name corp,v.cmzzdmc detail,c.hos_id corp_id,v.czyys as dcname "
							+ "FROM h_visit v "
							+ "LEFT JOIN fac_hospital c ON c.hos_id=v.corp_id "
							+ "WHERE v.cryksmc like '%"+query+"%'";
					if(!StringUtil.isNullOrEmpty(starttime)){
						mzks+= " AND m.dgh>='"+starttime.replaceAll("/", "-")+"' ";
						zyks += " AND v.drysj>='"+starttime.replaceAll("/", "-")+"' ";
					}
					if(!StringUtil.isNullOrEmpty(endtime)){
						mzks+= " AND m.dgh<='"+endtime.replaceAll("/", "-")+"' ";
						zyks+= " AND v.drysj<='"+endtime.replaceAll("/", "-")+"' ";
					}
					String kssql = mzks+" UNION ALL "+zyks;
					listbefore = dbm.executeQueryListHashMap(kssql);
					list = addListModel(listbefore, list);
					break;
				case "12"://app查询药名
					
					break;
				case "13"://app购买药名
					
					break;
				case "14"://血压
					
					break;
				case "15"://血糖
					
					break;
				default: 
		          break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbm.close();
		}
		return list;
	}
	
	/**
	 * @描述：药店数据
	 * @作者：SZ
	 * @时间：2017年3月29日 下午12:50:56
	 * @param type
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> searchIPDDrugstoreData(String type,String query,String starttime,String endtime){
		DBManager_HIS dbm = new DBManager_HIS();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listbefore = new ArrayList<Map<String, String>>();
		List<String> queryList = null;//需要查询的list集合
		try {
			String ydsql = "select a.*, b.sb_card_no, b.address "
					+ "from H_BUY_DRUG a "
					+ "INNER join mv_patient b on a.card_id = b.card_no ";
			switch(type){
				case "1"://诊断名称

					break;
				case "2"://主要诊断名称，第一诊断
					
					break;
				case "3"://药品名称
					queryList = queryMTS(query);
					ydsql += " where ";
					if(queryList.isEmpty()){
						ydsql += " a.drug like '%"+query+"%' ";
					}else{
						ydsql += addStr("a.drug", queryList).replaceFirst("OR", "");
					}
					break;
				case "4"://手术名称
					
					break;
				case "5"://检查名称
					
					break;
				case "6"://身份证号 CARD_NO
					ydsql += " where a.card_id like '%"+query+"%'";
					break;
				case "7"://医保卡号
					ydsql += " where b.sb_card_no like '%"+query+"%'";
					break;
				case "8"://姓名查询  
					ydsql += " where a.user_name like '%"+query+"%'";
					break;
				case "9"://住址查询
					ydsql += " where b.address like '%"+query+"%'";
					break;
				case "10"://医院名称
					
					break;
				case "11"://科室名称
					
					break;
				case "12"://app查询药名
					
					break;
				case "13"://app购买药名
					
					break;
				case "14"://血压
					
					break;
				case "15"://血糖
					
					break;
				case "16"://药店名称
					ydsql += " where a.DRUG_STORE like '%"+query+"%'";
					break;
				default: 
		          break;
			}
			if(!StringUtil.isNullOrEmpty(starttime)){
				ydsql+= " AND a.BUY_DATE>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			if(!StringUtil.isNullOrEmpty(endtime)){
				ydsql+= " AND a.BUY_DATE<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			listbefore = dbm.executeQueryListHashMap(ydsql);
			list = addListModel(listbefore, list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			dbm.close();
		}
		
		return list;
	}
	
	/**
	 * @描述：App使用行为数据
	 * @作者：SZ
	 * @时间：2017年3月29日 下午1:58:37
	 * @param type
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> searchIPDAPPData(String type,String query,String starttime,String endtime){
		DBManager_HIS dbm = new DBManager_HIS();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listbefore = new ArrayList<Map<String, String>>();
		System.out.println(type+"-------"+query);
		//queryList = queryMTS(query);//需要查询的list集合
		try {
			String appsql = "select a.*, b.sb_card_no, b.name, b.address "
					+ "from H_BEHAVIOR a "
					+ "INNER join mv_patient b on a.user_id = b.card_no "
					+ "where ";
			switch(type){
				case "1"://诊断名称

					break;
				case "2"://主要诊断名称，第一诊断
					
					break;
				case "3"://药品名称
					
					break;
				case "4"://手术名称
					
					break;
				case "5"://检查名称
					
					break;
				case "6"://身份证号 CARD_NO
					appsql +="a.user_id like '%"+query+"%'";
					break;
				case "7"://医保卡号
					appsql +=" b.sb_card_no like '%"+query+"%'";
					break;
				case "8"://姓名查询  
					appsql +="b.name like '%"+query+"%'";
					break;
				case "9"://住址查询
					appsql +=" b.address  like '%"+query+"%'";
					break;
				case "10"://医院名称
					
					break;
				case "11"://科室名称
					
					break;
				case "12"://app查询药名
					appsql +=" a.content like '%"+query+"%'";
					break;
				case "13"://app购买药名
					appsql +=" a.bought like '%"+query+"%'";
					break;
				case "14"://血压
					
					break;
				case "15"://血糖
					
					break;
				default: 
		          break;
			}
			if(!StringUtil.isNullOrEmpty(starttime)){
				appsql+= " AND a.use_date>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			if(!StringUtil.isNullOrEmpty(endtime)){
				appsql+= " AND a.use_date<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			listbefore = dbm.executeQueryListHashMap(appsql);
			list = addListModel(listbefore, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dbm.close();
		return list;
	}
	/**
	 * @描述：可穿戴数据
	 * @作者：SZ
	 * @时间：2017年3月29日 下午2:18:47
	 * @param type
	 * @param query
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> searchIPDUsedData(String type,String query,String starttime,String endtime){
		DBManager_HIS dbm = new DBManager_HIS();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listbefore = new ArrayList<Map<String, String>>();
		try {
			String appsql = "select a.*, b.sb_card_no, b.name, b.address "
					+ "from H_DEVICE a "
					+ "inner join mv_patient b on a.user_id = b.card_no "
					+ "where ";
			switch(type){
				case "1"://诊断名称

					break;
				case "2"://主要诊断名称，第一诊断
					
					break;
				case "3"://药品名称
					
					break;
				case "4"://手术名称
					
					break;
				case "5"://检查名称
					
					break;
				case "6"://身份证号 CARD_NO
					appsql +="a.user_id like '%"+query+"%'";
					break;
				case "7"://医保卡号
					appsql +=" b.sb_card_no like '%"+query+"%'";
					break;
				case "8"://姓名查询  
					appsql +="b.name like '%"+query+"%'";
					break;
				case "9"://住址查询
					appsql +=" b.address  like '%"+query+"%'";
					break;
				case "10"://医院名称
					
					break;
				case "11"://科室名称
					
					break;
				case "12"://app查询药名
					
					break;
				case "13"://app购买药名
					
					break;
				case "14"://血压高于
					appsql +=" type=1 and a.sbp>='"+query+"' ";
					break;
				case "15"://血糖高于
					appsql +=" type=2 and a.bg>='"+query+"'";
					break;
				case "20"://血压低于
					appsql +=" type=1 and a.dbp<='"+query+"' ";
					break;
				case "21"://血糖低于
					appsql +=" type=2 and a.bg<='"+query+"'";
					break;
				
				default: 
		          break;
			}
			if(!StringUtil.isNullOrEmpty(starttime)){
				appsql+= " AND a.m_time>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			if(!StringUtil.isNullOrEmpty(endtime)){
				appsql+= " AND a.m_time<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			listbefore = dbm.executeQueryListHashMap(appsql);
			list = addListModel(listbefore, list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
	
	/**
	 * @描述：结算单审核
	 * @作者：SZ
	 * @时间：2017年3月29日 下午3:01:47
	 * @param type
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> searchIPDJsdData(String type,String query,String starttime,String endtime){
		DBManager_HIS dbm = new DBManager_HIS();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> listbefore = new ArrayList<Map<String, String>>();
		List<String> queryList = null;//需要查询的list集合
		try {
			String appsql = "select a.* "
					+ "from mv_billdetail a  "
					+ "where ";
			switch(type){
				case "1"://诊断名称
					queryList = queryMTS(query);//需要查询的list集合
					if(queryList.isEmpty()){
						appsql +=" a.zd like '%"+query+"%'";
					}else{
						appsql +=addStr("a.zd", queryList).replaceFirst("OR", "");
						appsql +=addStr("a.mtszd", queryList);
					}
					
					break;
				case "2"://主要诊断名称，第一诊断
					queryList = queryMTS(query);//需要查询的list集合
					if(queryList.isEmpty()){
						appsql +=" a.zd like '%"+query+"%'";
					}else{
						appsql +=addStr("a.dyzd", queryList).replaceFirst("OR", "");
					}
					break;
				case "3"://药品名称
					queryList = queryMTS(query);//需要查询的list集合
					if(queryList.isEmpty()){
						appsql +=" a.yp like '%"+query+"%'";
					}else{
						appsql +=addStr("a.yp", queryList).replaceFirst("OR", "");
						appsql +=addStr("a.mtsyp", queryList);
					}
					break;
				case "4"://手术名称
					
					break;
				case "5"://检查名称
					
					break;
				case "6"://身份证号 CARD_NO
					appsql +=" a.sfzh like '%"+query+"%'";
					break;
				case "7"://医保卡号
					appsql +="  a.sbcard like '%"+query+"%'";
					break;
				case "8"://姓名查询  
					appsql +=" a.xm like '%"+query+"%'";
					break;
				case "9"://住址查询
					appsql +=" a.addr like '%"+query+"%'";
					break;
				case "10"://医院名称
					appsql +=" a.corp like '%"+query+"%'";
					break;
				case "11"://科室名称
					appsql +=" a.unit like '%"+query+"%'";
					break;
				case "12"://app查询药名
					
					break;
				case "13"://app购买药名
					
					break;
				case "14"://血压
					
					break;
				case "15"://血糖
					
					break;
				default: 
		          break;
			}
			if(!StringUtil.isNullOrEmpty(starttime)){
				appsql+= " AND a.rdate>=to_date('"+starttime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			if(!StringUtil.isNullOrEmpty(endtime)){
				appsql+= " AND a.rdate<=to_date('"+endtime+"','yyyy-mm-dd hh24:mi:ss' ) ";
			}
			listbefore = dbm.executeQueryListHashMap(appsql);
			list = addListModel(listbefore, list);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			dbm.close();
		}
		return list;
	}
	/**  
	* @标题: searchDataByPage  
	* @描述: TODO分页查询数据返回map格式
	* @作者 EbaoWeixun
	* @param sql
	* @param page
	* @param pageSize
	* @return
	*/  
	public PageModel searchDataByPage(String sql,int page,int pageSize){
		DBManager_HIS dbm = new DBManager_HIS();
		PageModel pageModel=new PageModel();
		try{
			pageModel=dbm.getMapByPage(sql, page, pageSize);
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			dbm.close();
		}
		return pageModel;
	}
}
