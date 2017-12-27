package com.dhcc.ts.hzlb.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dhcc.common.util.DateUtil;
import com.dhcc.common.util.StringUtil;
import com.dhcc.modal.system.PageModel;
import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.hzlb.model.HzlbModel;
import com.dhcc.ts.mtsService.MTSUtil;
import com.dhcc.ts.mtsService.ResultModel;

public class HzlbDao {
	
	/**
	 * @描述：返回该医生用户下的患者列表
	 * @作者：SZ
	 * @时间：2017年3月6日 下午2:14:20
	 * @param med_id
	 * @param type
	 * @param query
	 * @return
	 * 建立物化视图，提高查询效率
	 */
	public PageModel getHzlbModel(String query,int pageNum,int pageSize){
		DBManager_HIS dbm = new DBManager_HIS();
		//List<HzlbModel> returnList = new ArrayList<HzlbModel>();
		
		PageModel model=null;
		try{
			//三个字的模糊查询
			//门诊
			String sql = "";
			if(StringUtil.isNullOrEmpty(query)){
				sql = "SELECT MP.*,MP.NAME AS NAMETRUE FROM MV_PATIENT MP ORDER BY MP.ORDERS,MP.RN";
				long t1 = System.currentTimeMillis();
				model  = dbm.getObjectByPage(sql, HzlbModel.class, pageNum, pageSize);
				
				//List<HzlbModel> listModelZY = dbm.getObjectList(HzlbModel.class,sqlzy);
				long t2 = System.currentTimeMillis();
				System.out.println("读取数据时长               ="+(t2-t1)+"ms");
			}else{
				long t1 = System.currentTimeMillis();
				List<String> queryList = queryMTS(query);//需要查询的list集合
				sql = "SELECT * FROM ( SELECT TEMP.*, ROW_NUMBER () OVER ( PARTITION BY CARD_NO ORDER BY CARD_NO ) AS PXX FROM ("
						+ "SELECT DISTINCT MP.*,MP.NAME AS NAMETRUE FROM "
						+ "MV_PATIENT MP "
						+ "LEFT JOIN H_REG HR ON MP.CARD_NO = HR.CSFZH "
						+ "LEFT JOIN H_RECIPE HRE ON HRE.CMZH = HR.CMZH "
						+ "LEFT JOIN H_RECIPE_DETAIL HRD ON HRD.CCFH = HRE.CCFH "
						+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HR.CORP_ID "
						+ "INNER JOIN H_BUY_DRUG HBD on HBD.card_id = MP.card_no "
						+ "WHERE ";
				if(!queryList.isEmpty()){
					sql += addStr("MP.NAME", queryList).replaceFirst("OR", "");
					sql += addStr("MP.CARD_NO",queryList);
					sql += addStr("MP.SB_CARD_NO", queryList);
					sql += addStr("HRE.CCFZD", queryList);
					sql += addStr("HRE.MTS_ZD", queryList);
					sql += addStr("HRD.CYPMC", queryList);
					sql += addStr("HRD.MTS_YP", queryList);
					sql += addStr("HR.CKSMC",queryList);
					sql += addStr("FH.HOS_NAME", queryList);
					sql += addStr("HBD.DRUG_STORE", queryList);
				}else{
					sql += addStr2("MP.NAME", query).replaceFirst("OR", "");
					sql += addStr2("MP.CARD_NO",query);
					sql += addStr2("MP.SB_CARD_NO", query);
					sql += addStr2("HRE.CCFZD", query);
					sql += addStr2("HRE.MTS_ZD", query);
					sql += addStr2("HRD.CYPMC", query);
					sql += addStr2("HRD.MTS_YP", query);
					sql += addStr2("HR.CKSMC",query);
					sql += addStr2("FH.HOS_NAME", query);
					sql += addStr2("HBD.DRUG_STORE", query);
				}
				
				sql	+= "UNION ALL "
						+ "SELECT DISTINCT MP.*,MP.NAME AS NAMETRUE FROM MV_PATIENT MP "
						+ "LEFT JOIN H_VISIT HV ON HV.CSFZH = MP.CARD_NO "
						+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HV.CORP_ID "
						+ "WHERE ";
				if(!queryList.isEmpty()){
					sql += addStr("MP.NAME",queryList).replaceFirst("OR", "");
					sql += addStr("MP.CARD_NO",queryList);
					sql += addStr("MP.SB_CARD_NO", queryList);
					sql += addStr("HV.CMZZDMC", queryList);
					sql += addStr("HV.MTS_ZD", queryList);
					sql += addStr("HV.CRYKSMC", queryList);
					sql += addStr("FH.HOS_NAME", queryList);
				}else{
					sql += addStr2("MP.NAME",query).replaceFirst("OR", "");
					sql += addStr2("MP.CARD_NO",query);
					sql += addStr2("MP.SB_CARD_NO", query);
					sql += addStr2("HV.CMZZDMC", query);
					sql += addStr2("HV.MTS_ZD", query);
					sql += addStr2("HV.CRYKSMC", query);
					sql += addStr2("FH.HOS_NAME", query);
				}
			
				
				
				/*sql +=  "UNION ALL "
						+ "SELECT DISTINCT MP.*,MP.NAME AS NAMETRUE FROM MV_PATIENT MP "
						+ "LEFT JOIN H_VISIT HV ON HV.CSFZH = MP.CARD_NO "
						+ "LEFT JOIN H_ADVICE HA ON HA.CZYH = HV.CZYH "
						+ "WHERE ";
                if(!queryList.isEmpty()){
                	sql += addStr("HA.CYPMC", queryList).replaceFirst("OR", "");
    				sql += addStr("HA.MTS_YZ", queryList);
				}else{
					sql += addStr2("HA.CYPMC", query).replaceFirst("OR", "");
					sql += addStr2("HA.MTS_YZ", query);
				}*/
            
				sql += ") TEMP ) TEMP1 WHERE PXX = '1' ORDER BY ORDERS,rn";
			
				model  = dbm.getObjectByPage(sql, HzlbModel.class, pageNum, pageSize);
				//model.setList(new ArrayList<HzlbModel>());
				if(model.getList()==null||model.getList().isEmpty()){
					if(!queryList.isEmpty()){
						
						queryList=queryStdMTS(query);
						
						if(!queryList.isEmpty()){
							sql = "SELECT * FROM ( SELECT TEMP.*, ROW_NUMBER () OVER ( PARTITION BY CARD_NO ORDER BY CARD_NO ) AS PXX FROM ("
									+ "SELECT DISTINCT MP.*,MP.NAME AS NAMETRUE FROM MV_PATIENT MP "
									+ "LEFT JOIN H_REG HR ON MP.CARD_NO = HR.CSFZH "
									+ "LEFT JOIN H_RECIPE HRE ON HRE.CMZH = HR.CMZH "
									+ "LEFT JOIN H_RECIPE_DETAIL HRD ON HRD.CCFH = HRE.CCFH "
									+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HR.CORP_ID "
									+ "INNER JOIN H_BUY_DRUG HBD on HBD.card_id = MP.card_no "
									+ "WHERE ";
							sql += addStr("MP.NAME", queryList).replaceFirst("OR", "");
							sql += addStr("MP.CARD_NO",queryList);
							sql += addStr("MP.SB_CARD_NO", queryList);
							sql += addStr("HRE.CCFZD", queryList);
							sql += addStr("HRE.MTS_ZD", queryList);
							sql += addStr("HRD.CYPMC", queryList);
							sql += addStr("HRD.MTS_YP", queryList);
							sql += addStr("HR.CKSMC", queryList);
							sql += addStr("FH.HOS_NAME", queryList);
							sql += addStr("HBD.DRUG_STORE", queryList);
							
							sql	+= "UNION ALL "
									+ "SELECT DISTINCT MP.*,MP.NAME AS NAMETRUE FROM MV_PATIENT MP "
									+ "LEFT JOIN H_VISIT HV ON HV.CSFZH = MP.CARD_NO "
									+ "LEFT JOIN FAC_HOSPITAL FH ON FH.HOS_ID = HV.CORP_ID "
									+ "WHERE ";
							sql += addStr("MP.NAME", queryList).replaceFirst("OR", "");
							sql += addStr("MP.CARD_NO", queryList);
							sql += addStr("MP.SB_CARD_NO", queryList);
							sql += addStr("HV.CMZZDMC", queryList);
							sql += addStr("HV.MTS_ZD", queryList);
							sql += addStr("HV.CRYKSMC",queryList);
							sql += addStr("FH.HOS_NAME",queryList);
							
							
							/*sql +=  "UNION ALL "
									+ "SELECT DISTINCT MP.*,MP.NAME AS NAMETRUE FROM MV_PATIENT MP "
									+ "LEFT JOIN H_VISIT HV ON HV.CSFZH = MP.CARD_NO "
									+ "LEFT JOIN H_ADVICE HA ON HA.CZYH = HV.CZYH "
									+ "WHERE ";
							sql += addStr("HA.CYPMC", queryList).replaceFirst("OR", "");
							sql += addStr("HA.MTS_YZ",queryList);*/
							sql += ") TEMP ) TEMP1 WHERE PXX = '1' ORDER BY ORDERS,rn";
							model  = dbm.getObjectByPage(sql, HzlbModel.class, pageNum, pageSize);
						}
					}
				}
				long t2 = System.currentTimeMillis();
				System.out.println("读取数据时长               ="+(t2-t1)+"ms");	
						
				
				//List<HzlbModel> listModelZY = dbm.getObjectList(HzlbModel.class,sqlzy);
				
			}
			
			
			//listModel.addAll(listModelZY);
			//returnList = addListModel(listModel, returnList);
			//System.out.println("读取数据时长               ="+(System.currentTimeMillis()-t2)+"ms");
		}catch(Exception e){
			return model;
		}finally {
			dbm.close();
		}
		return model;
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
		
		return listResult;
	}
	/**
	 * @描述：调取MTS，返回标准词的上位词
	 * @作者：SZ
	 * @时间：2017年7月4日 下午4:45:38
	 * @param query
	 * @return
	 */
	public List<String> queryStdMTS(String query){		
		List<String> resultList=new ArrayList<String>();
		String result="";
		List<ResultModel> list = MTSUtil.getMTSResult(query, null);
		if(!list.isEmpty()){
			for(ResultModel rm:list){
				if(!StringUtil.isNullOrEmpty(rm.getMtsresult())){
					result=MTSUtil.getStdMTSResult(rm.getMtsresult());
					if(!StringUtil.isNullOrEmpty(result)){
						resultList.add(result);
					}
				}

			}
		}
		System.out.println("MTS标准词的上位词查询结果："+resultList);
		
		return resultList;
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
	public String addStr2(String field,String str){
		StringBuffer sb = new StringBuffer();
		sb.append("OR ");
		sb.append(field);
		sb.append(" like ");
		sb.append("'%");
		sb.append(str);
		sb.append("%' ");
		return sb.toString();
	}
	/*public PageModel getHzlbModel(String query,int pageNum,int pageSize){
		DBManager_HIS dbm = new DBManager_HIS();
		List<HzlbModel> returnList = new ArrayList<HzlbModel>();
		PageModel model=null;
		try{
			//三个字的模糊查询
			//门诊
			String sql = "SELECT * FROM(SELECT TEMP.*,"
					+ "ROW_NUMBER () OVER (PARTITION BY CARD_NO ORDER BY CARD_NO)  AS PX "
					+ "FROM ( "
					+ "SELECT AC.AAC002 AS ID,HR.CXM AS NAME,AC.AAC002 AS CARD_NO,"
					+ "AC.AKC020 AS SB_CARD_NO,AC2.AAC031 AS CBZT,HR.ORDERS AS ORDERS "
					+ "FROM H_REG HR "
					+ "LEFT JOIN AC01 AC ON AC.AAC002 = HR.CSFZH "
					+ "LEFT JOIN AC02 AC2 ON AC2.AAC001 = AC.AAC001 ";
					if(!StringUtil.isNullOrEmpty(query)){
					
					sql+=" LEFT JOIN H_RECIPE HRE ON HRE.CMZH  = HR.CMZH "
						+ "LEFT JOIN H_RECIPE_DETAIL HRD ON HRD.CCFH  = HRE.CCFH ";
					
					}		
					sql+="WHERE AC.AKC020 != 'Null' ";
					if(!StringUtil.isNullOrEmpty(query)){
						sql += " AND (HR.CXM LIKE '%"+query+"%'"
							+ " OR AC.AAC002 LIKE '%"+query+"%'"
							+ " OR AC.AKC020 LIKE '%"+query+"%' "
							+ " OR HRE.CCFZD LIKE '%"+query+"%'"
							+ " OR HRD.CYPMC LIKE '%"+query+"%') ";
					}
					sql += " UNION ALL "
					+ "SELECT AC.AAC002 AS ID,HR.CXM AS NAME,AC.AAC002 AS CARD_NO,"
					+ "AC.AKC020 AS SB_CARD_NO,AC2.AAC031 AS CBZT,HR.ORDERS AS ORDERS "
					+ "FROM H_VISIT HR "
					+ "LEFT JOIN AC01 AC ON AC.AAC002 = HR.CSFZH "
					+ "LEFT JOIN AC02 AC2 ON AC2.AAC001 = AC.AAC001 ";
					if(!StringUtil.isNullOrEmpty(query)){
						sql+= "LEFT JOIN H_ADVICE HA ON HA.CZYH = HR.CZYH ";
					}
						sql+= "WHERE AC.AKC020 != 'Null' ";
					if(!StringUtil.isNullOrEmpty(query)){
						sql += " AND (HR.CXM LIKE '%"+query+"%' "
							+ " OR AC.AAC002 LIKE '%"+query+"%'"
							+ " OR AC.AKC020 LIKE '%"+query+"%'"
							+ " OR HR.CMZZDMC LIKE '%"+query+"%'"
							+ " OR HA.CYPMC  LIKE '%"+query+"%') ";
					}
					sql += ") TEMP) TEMP1  WHERE PX='1' ORDER BY ORDERS   ";
			long t1 = System.currentTimeMillis();
			model  = dbm.getObjectByPage(sql, HzlbModel.class, pageNum, pageSize);
			//List<HzlbModel> listModelZY = dbm.getObjectList(HzlbModel.class,sqlzy);
			long t2 = System.currentTimeMillis();
			System.out.println("读取数据时长               ="+(t2-t1)+"ms");
			//listModel.addAll(listModelZY);
			//returnList = addListModel(listModel, returnList);
			//System.out.println("读取数据时长               ="+(System.currentTimeMillis()-t2)+"ms");
		}catch(Exception e){
			return model;
		}finally {
			dbm.close();
		}
		return model;
		
	}*/
	private List<HzlbModel> addListModel(List<HzlbModel> before,List<HzlbModel> after){
		Set<String> card_noSet = new HashSet<String>();
		int temp = card_noSet.size();
		for(HzlbModel model : before){
			card_noSet.add(model.getCard_no());
			if(card_noSet.size()>temp){
				after.add(model);
				temp +=1;
			}
		}
		return after;
	}
	/*public List<HzlbModel> getHzlbModel(String med_id,String type,String query){
		DBManager_HIS dbm = new DBManager_HIS();
		List<HzlbModel> returnList = new ArrayList<HzlbModel>();
		try{
			//三个字的模糊查询
			//门诊
			String sql = "SELECT AC.AAC002 AS ID,SUBSTR (HR.DGH, 0, 19) AS SHIJIAN,HR.CXM AS NAME,"
					+ "AC.AAC002 AS CARD_NO,AC.AKC020 AS SB_CARD_NO "
					+ "FROM H_REG HR LEFT JOIN AC01 AC ON AC.AAC002 = HR.CSFZH ";
					if(!StringUtil.isNullOrEmpty(query)){
					
					sql+=" LEFT JOIN H_RECIPE HRE ON HRE.CMZH  = HR.CMZH "
						+ "LEFT JOIN H_RECIPE_DETAIL HRD ON HRD.CCFH  = HRE.CCFH "
						+ "LEFT JOIN H_VISIT HV ON HV.CSFZH = HR.CSFZH "
						+ "LEFT JOIN H_EXAM_ADVICE HEA ON HEA.CZYH = HV.CZYH ";
					
					}		
					sql+="WHERE AC.AKC020 != 'Null' ";
					if(!StringUtil.isNullOrEmpty(query)){
						sql += " AND (HR.CXM LIKE '%"+query+"%' OR AC.AAC002 LIKE '%"+query+"%' OR AC.AKC020 LIKE '%"+query+"%' "
							+ " OR HRE.CCFZD LIKE '%"+query+"%' OR HV.CMZZDMC LIKE '%"+query+"%' OR HRD.CYPMC LIKE '%"+query+"%' "
									+ " OR HEA.CYZNR LIKE '%"+query+"%') ";
					}
					sql += "ORDER BY SHIJIAN DESC";
			//住院
			String sqlzy = "SELECT AC.AAC002 AS ID,SUBSTR (HR.DRYSJ, 0, 19) AS SHIJIAN,HR.CXM AS NAME,AC.AAC002 AS CARD_NO,AC.AKC020 AS SB_CARD_NO "
					+ "FROM H_VISIT HR LEFT JOIN AC01 AC ON AC.AAC002 = HR.CSFZH "
					+ "WHERE AC.AKC020 != 'Null' ";
					if(!StringUtil.isNullOrEmpty(query)){
						sqlzy += " AND (HR.CXM LIKE '%"+query+"%' OR AC.AAC002 LIKE '%"+query+"%' OR AC.AKC020 LIKE '%"+query+"%') ";
					}
					sqlzy+= "ORDER BY SHIJIAN DESC";
			
			List<HzlbModel> listModel = dbm.getObjectList(HzlbModel.class,sql);
			List<HzlbModel> listModelZY = dbm.getObjectList(HzlbModel.class,sqlzy);
			Set<String> card_noSet = new HashSet<String>();
			Map<String ,Collection> map = new HashMap<String ,Collection>();
			map = addListModel(card_noSet, listModel, returnList);
			map = addListModel((Set)map.get("set"), listModelZY, (List)map.get("list"));
			
			HzlbModel model = new HzlbModel();
			returnList = (List<HzlbModel>) map.get("list");
			Collections.sort(returnList, model);
		}catch(Exception e){
			return returnList;
		}finally {
			dbm.close();
		}
		return returnList;
		
	}
	
	private Map<String ,Collection> addListModel(Set<String> card_noSet,List<HzlbModel> before,List<HzlbModel> after){
		Map<String ,Collection> result = new HashMap<String ,Collection>();
		int temp = card_noSet.size();
		for(HzlbModel model : before){
			card_noSet.add(model.getCard_no());
			if(card_noSet.size()>temp){
				after.add(model);
				temp +=1;
			}else{
				for(HzlbModel afterModel : after){
					if(afterModel.getCard_no().equals(model.getCard_no())&& JudgeTime(model.getShijian(), afterModel.getShijian())){
						after.remove(afterModel);
						after.add(model);
						break;
					}
				}
			}
		}
		result.put("set", card_noSet);
		result.put("list", after);
		return result;
	}*/
	
	private boolean JudgeTime(String firstTime,String afterTime){
		boolean result = false;
		if(DateUtil.toTime(firstTime)>DateUtil.toTime(afterTime)){
			result = true;
		}
		return result;
	}
//	/**
//	 * @描述：返回该医生用户下的患者列表
//	 * 
//	 * @作者：SZ
//	 * @时间：2017年2月22日 下午8:24:19
//	 * @param med_id
//	 * @param type 
//	 * @return
//	 */
//	public List<HzlbModel> getHzlbModel(String med_id,String type,String query){
//		DBManager_HIS dbm = new DBManager_HIS();
//		List<HzlbModel> returnList = new ArrayList<HzlbModel>();
//		try{
//			//根据type前台下拉菜单类型分sql查询隐藏信息
//			//0:参保人姓名;1:参保人身份证号;2:参保人社保卡号
//			String sql = "";//门诊
//			String sqlzy = "";//住院
//			if("1".equals(type)){
//				sql = "SELECT DISTINCT SUBSTR(HR.DGH,0,19) AS SHIJIAN,AC.AAC002 AS ID,HR.CXM AS NAMETRUE,AC.AAC002 AS CARD_NO,'***' AS NAME,'**********' AS SB_CARD_NO "
//						+ "FROM H_REG HR "
//						+ "LEFT JOIN FAC_MEDICAL FM ON FM.STAFF_NAME = HR.CYSMC "
//						+ "LEFT JOIN AC01 AC ON AC.AAC002 = HR.CSFZH "
//						+ "WHERE FM.MED_ID = '"+med_id+"' " ;
//						if(!StringUtil.isNullOrEmpty(query)){
//							sql += " AND AC.AAC002 LIKE '%"+query+"%' ";
//						}
//						sql += " ORDER BY SHIJIAN DESC";
//				sqlzy = "SELECT DISTINCT SUBSTR(HV.DRYSJ,0,19) AS SHIJIAN,AC.AAC002 AS ID,HV.CXM AS NAMETRUE,AC.AAC002 AS CARD_NO,'***' AS NAME,'**********' AS SB_CARD_NO "
//						+ "FROM H_VISIT HV "
//						+ "LEFT JOIN FAC_MEDICAL FM ON FM.STAFF_NAME = HV.CZYYS "
//						+ "LEFT JOIN AC01 AC ON AC.AAC002 = HV.CSFZH "
//						+ "WHERE FM.MED_ID = '"+med_id+"' " ;
//						if(!StringUtil.isNullOrEmpty(query)){
//							sqlzy += " AND AC.AAC002 LIKE '%"+query+"%' ";
//						}
//						sqlzy += " ORDER BY SHIJIAN DESC";
//				
//			}else if("2".equals(type)){
//				sql = "SELECT DISTINCT SUBSTR(HR.DGH,0,19) AS SHIJIAN,AC.AAC002 AS ID,HR.CXM AS NAMETRUE,'******************' AS CARD_NO,'***' AS NAME,AC.AKC020 AS SB_CARD_NO "
//						+ "FROM H_REG HR "
//						+ "LEFT JOIN FAC_MEDICAL FM ON FM.STAFF_NAME = HR.CYSMC "
//						+ "LEFT JOIN AC01 AC ON AC.AAC002 = HR.CSFZH "
//						+ "WHERE FM.MED_ID = '"+med_id+"' " ;
//						if(!StringUtil.isNullOrEmpty(query)){
//							sql += " AND AC.AKC020 LIKE '%"+query+"%' ";
//						}
//						sql += " ORDER BY SHIJIAN DESC";
//				sqlzy = "SELECT DISTINCT SUBSTR(HV.DRYSJ,0,19) AS SHIJIAN,AC.AAC002 AS ID,HV.CXM AS NAMETRUE,'******************' AS CARD_NO,'***' AS NAME,AC.AKC020 AS SB_CARD_NO "
//						+ "FROM H_VISIT HV "
//						+ "LEFT JOIN FAC_MEDICAL FM ON FM.STAFF_NAME = HV.CZYYS "
//						+ "LEFT JOIN AC01 AC ON AC.AAC002 = HV.CSFZH "
//						+ "WHERE FM.MED_ID = '"+med_id+"' " ;
//						if(!StringUtil.isNullOrEmpty(query)){
//							sqlzy += " AND AC.AKC020 LIKE '%"+query+"%' ";
//						}
//						sqlzy += " ORDER BY SHIJIAN DESC";
//				
//			}else{
//				sql = "SELECT DISTINCT SUBSTR(HR.DGH,0,19) AS SHIJIAN,AC.AAC002 AS ID,HR.CXM AS NAMETRUE,'******************' AS CARD_NO,HR.CXM AS NAME,'**********' AS SB_CARD_NO "
//						+ "FROM H_REG HR "
//						+ "LEFT JOIN FAC_MEDICAL FM ON FM.STAFF_NAME = HR.CYSMC "
//						+ "LEFT JOIN AC01 AC ON AC.AAC002 = HR.CSFZH "
//						+ "WHERE FM.MED_ID = '"+med_id+"' " ;
//						if(!StringUtil.isNullOrEmpty(query)){
//							sql += " AND HR.CXM LIKE '%"+query+"%' ";
//						}
//						sql += " ORDER BY SHIJIAN DESC";
//				sqlzy = "SELECT DISTINCT SUBSTR(HV.DRYSJ,0,19) AS SHIJIAN,AC.AAC002 AS ID,HV.CXM AS NAMETRUE,'******************' AS CARD_NO,HV.CXM AS NAME,'**********' AS SB_CARD_NO "
//						+ "FROM H_VISIT HV "
//						+ "LEFT JOIN FAC_MEDICAL FM ON FM.STAFF_NAME = HV.CZYYS "
//						+ "LEFT JOIN AC01 AC ON AC.AAC002 = HV.CSFZH "
//						+ "WHERE FM.MED_ID = '"+med_id+"' " ;
//						if(!StringUtil.isNullOrEmpty(query)){
//							sqlzy += " AND HR.CXM LIKE '%"+query+"%' ";
//						}
//						sqlzy += " ORDER BY SHIJIAN DESC";
//			}
//			List<HzlbModel> listModel = dbm.getObjectList(HzlbModel.class,sql);
//			List<HzlbModel> listModelZY = dbm.getObjectList(HzlbModel.class,sqlzy);
//			returnList.addAll(listModel);
//			returnList.addAll(listModelZY);
//		}catch(Exception e){
//			return returnList;
//		}finally {
//			dbm.close();
//		}
//		return returnList;
//		
//	}
	
	public HzlbModel queryHospital(String hos_id){
		DBManager_HIS dbm = new DBManager_HIS();
		String sql = "SELECT HOS_NAME AS NAME,INTRODUCTION AS id FROM FAC_HOSPITAL WHERE HOS_ID='493'";
		HzlbModel model = (HzlbModel) dbm.getObject(HzlbModel.class, sql);
		dbm.close();
		return model;
	}
}
