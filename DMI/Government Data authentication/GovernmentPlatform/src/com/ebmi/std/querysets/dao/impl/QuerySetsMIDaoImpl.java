package com.ebmi.std.querysets.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.core.cache.CacheService;
import com.core.db.utils.PageQueryUtil;
import com.core.utils.DateUtils;
import com.ebmi.std.common.dao.impl.BaseDao;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.querysets.dao.QuerySetsMIDao;
import com.ebmi.std.querysets.dao.mapper.AbstractRowMapper;
import com.ebmi.std.querysets.dto.MaternReimbItem;
import com.ebmi.std.querysets.dto.TurnoutItem;

@Service("querySetsMIDaoImpl")
public class QuerySetsMIDaoImpl extends BaseDao implements QuerySetsMIDao {
	
	@Resource
	private CacheService cacheService;
	
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;
	
	@Override
	public List<TurnoutItem> queryTurnoutTreatList(Map<String, Object> condition) throws Exception {
		List<TurnoutItem> items = new ArrayList<TurnoutItem>();
		if(!condition.isEmpty()){
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", ConvertUtils.convert(condition.get("idcard")));//公民身份证号
			map.put("parm1", ConvertUtils.convert(condition.get("username")));//姓名
			map.put("parm2", ConvertUtils.convert(condition.get("ss_num")));//社会保障卡号
			//TheKeyResultEntity res =  PmeoutreinfoApi.getPmeoutreinfo(map);
			TheKeyResultEntity res =  baseApi.getDataInfo(map, BaseEntity.pmeoutreinfo);//hsoft.yibao.pmeoutreinfo.get(个人医疗转外就医登记信息查询)
			
			if(res!=null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
					if((!lm.isEmpty()) ){//有元素的时候
						Map<String, Object> retrmap = new HashMap<String, Object>();
						
						for (Map<String, String> map2 : lm) {
							TurnoutItem item = new TurnoutItem();
							////审批日期  == 有效截止时间 
							item.setApproval_date(StringUtils.isNotBlank(map2.get("col11"))?DateUtils.std(map2.get("col11"), 1):null);
							item.setNow_hospital(map2.get("col0"));//转出医院
							
							if(StringUtils.isNotBlank(map2.get("col6"))){//转往医院 
								item.setTo_hospital(map2.get("col6"));//转往医院 col4<col5<col6
							}else if(StringUtils.isNotBlank(map2.get("col5"))){
								item.setTo_hospital(map2.get("col5"));//转往医院 col4<col5<col6
							}else if(StringUtils.isNotBlank(map2.get("col4"))){
								item.setTo_hospital(map2.get("col4"));//转往医院 col4<col5<col6
							}
							item.setProvince_in_out(map2.get("col3"));//省内省外
							item.setClinical_cause(map2.get("col1"));//临床诊断病因  
							
						
							
							if(retrmap.get("approval_date")!=null&&StringUtils.isNotBlank(map2.get("col11"))){//根据//审批日期 排序 
								Date first =  (Date) retrmap.get("approval_date");
								Date second = DateUtils.std(map2.get("col11"), 1);
								//下一个数据的时间  比较 上一条时间 还早的话 就继续 下一条 
								if(second.getTime()>first.getTime()){//second 在  first 前 即 老时间  继续
									items.clear();
									items.add(item);
								}
							}else{
								items.add(item);
							}
							retrmap.put("approval_date", item.getApproval_date());
							
						}
					}
				}
			}
		}
		
		return items;
		
//		StringBuffer sql = new StringBuffer();
//		sql.append("select SPSJ as approval_date, ")
//			.append("trim(SZYY) as now_hospital, ")
//			.append("trim(ZWYY) as to_hospital, ")
//			.append("trim(SNSW) as province_in_out, ")
//			.append("trim(JBYY) as clinical_cause, ")
//			.append("trim(KS) as hospital_section, ")
//			.append("trim(YS) as turnout_doctor ")
//			.append("from view_zzzy where 1=1 ");
//		
//		List<Object> args = new ArrayList<Object>();
//		String p_mi_id = String.valueOf(condition.get("p_mi_id"));
//		if (StringUtils.isNotBlank(p_mi_id) && !"null".equals(p_mi_id)) {
//			sql.append(" and grbh = ?");
//			args.add(p_mi_id);
//		}
//		
//		sql.append(" order by ").append(" spsj desc ");
//		
//		Integer recordStart = (Integer) condition.get("recordStart");
//		Integer recordCount = (Integer) condition.get("recordCount");
//		if (recordStart == null || recordStart < 0)
//			recordStart = 0;
//		
//		if (recordCount == null || recordCount <= 0)
//			recordCount = 10;
//
//		final Map<String, Map<String, Object>> view_yyjbxx = (Map<String, Map<String, Object>>) cacheService.get(APPCacheKeyConst.VIEW_YYJBXX);
//		String ssql = PageQueryUtil.pageQuerySql(sql.toString(), recordStart, recordStart + recordCount);
//		return getJdbcServiceMI().findListByQuery(ssql, args.toArray(), new AbstractRowMapper<TurnoutItem>() {
//
//			@Override
//			public TurnoutItem mapRow(ResultSet rs, int rowNum) throws SQLException {
//				TurnoutItem item = new TurnoutItem();
//				item.setApproval_date(rs.getDate("approval_date"));
//				item.setClinical_cause(convertStr(rs.getString("clinical_cause")));
//				String now_hospital =  rs.getString("now_hospital");
//				if (isNeedConvertHosp(now_hospital)) {
//					Map<String, Object> row = view_yyjbxx.get(now_hospital);
//					now_hospital = row.get("yymc") == null ? null : String.valueOf(row.get("yymc"));
//				}
//				String to_hospital =  rs.getString("to_hospital");
//				if (isNeedConvertHosp(to_hospital)) {
//					Map<String, Object> row = view_yyjbxx.get(to_hospital);
//					to_hospital = row.get("yymc") == null ? null : String.valueOf(row.get("yymc"));
//				}
//				item.setNow_hospital(convertStr(now_hospital));
//				item.setTo_hospital(convertStr(to_hospital));
//				item.setHospital_section(convertStr(rs.getString("hospital_section")));
//				item.setTurnout_doctor(convertStr(rs.getString("turnout_doctor")));
//				item.setProvince_in_out(rs.getString("province_in_out"));
//				return item;
//			}
//			
//		});
	}
	
	@Override
	public Map<String, Object> queryAllopatryTreatMI(Map<String, Object> condition) throws Exception {
		if(condition != null && !condition.isEmpty()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("parm0", condition.get("p_mi_id").toString());//公民身份证号
			map.put("parm1", condition.get("name").toString());//姓名
			map.put("parm2", "");//社会保障卡号
			map.put("parm3", "1");//社会保障卡号
			TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.pmediinfo);// hsoft.yibao.pmediinfo.get(医疗个人参保信息查询)
			if(res != null){
				if("ok".equals(res.getStatus())){
					List<Map<String, String>> list = res.getData();
					if(!list.isEmpty()){
						if("异地定居".equals(list.get(0).get("col5"))){
							Map<String, String> param = new HashMap<String, String>();
							param.put("parm0", condition.get("p_mi_id").toString());//公民身份证号
							param.put("parm1", condition.get("name").toString());//姓名
							param.put("parm2", "");//社会保障卡号
							TheKeyResultEntity ere = baseApi.getDataInfo(map, BaseEntity.pmerelocatreinfo);// hsoft.yibao.pmerelocatreinfo.get 个人医疗异地安置登记信息查询
							if(ere != null){
								if("ok".equals(ere.getStatus())){
									List<Map<String, String>> lm = ere.getData() ;
									Map<String, Object> result = new HashMap<String, Object>() ;
									for (Map<String, String> map2 : lm) {
										result.put("allopatry_addr", map2.get("col1")) ; // 异地安置地址
										result.put("first_hosp", map2.get("col2")) ; // 首选医院
										result.put("second_hosp", map2.get("col5")) ; // 第二定点
										result.put("third_hosp", map2.get("col8")) ; // 第三定点
										return result ;
									}
								}
							}
						}
					}
				}
			}
		}
		return null ;
	}


	@Override
	public List<MaternReimbItem> queryMaternReimbMI(Map<String, Object> condition) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select trim(c.dwmc) as work_station, ")
			.append("a.sysj as bear_date, ")
			.append("a.cjts as matern_leavedays, ")
			.append("a.fyxj1 as medical_fee, ")
			.append("a.fyxj2 as benefit, ")
			.append("a.fyxj3 as subsidy, ")
			.append("a.fyxj as matern_fee_sum, ")
			.append("a.fyxj as reimb_sum, ")
			.append("a.pdrq as reimb_date ")
			.append("from view_pdk_sy a inner join view_grjbxx b on a.grbh = b.grbh ")
			.append("left join view_dwjbxx c on b.dwbh = c.dwbh ")
			.append(" where 1=1 ");
		
		List<Object> args = new ArrayList<Object>();
		String p_mi_id = String.valueOf(condition.get("p_mi_id"));
		if (StringUtils.isNotBlank(p_mi_id) && !"null".equals(p_mi_id)) {
			sql.append(" and a.grbh = ?");
			args.add(p_mi_id);
		}
		
		sql.append(" order by ").append(" a.sysj desc ");
		
		Integer recordStart = (Integer) condition.get("recordStart");
		Integer recordCount = (Integer) condition.get("recordCount");
		if (recordStart == null || recordStart < 0)
			recordStart = 0;
		
		if (recordCount == null || recordCount <= 0)
			recordCount = 10;

		String ssql = PageQueryUtil.pageQuerySql(sql.toString(), recordStart, recordStart + recordCount);
		return getJdbcServiceMI().findListByQuery(ssql, args.toArray(), new AbstractRowMapper<MaternReimbItem>() {
			@Override
			public MaternReimbItem mapRow(ResultSet rs, int rowNum) throws SQLException {
				MaternReimbItem item = new MaternReimbItem();
				item.setBear_date(rs.getDate("bear_date"));
				item.setBenefit(rs.getBigDecimal("benefit"));
				item.setBenefit_base(null);
				item.setReimb_sum(rs.getBigDecimal("reimb_sum"));
				item.setMatern_fee_sum(rs.getBigDecimal("matern_fee_sum"));
				Integer leaveDays = rs.getBigDecimal("matern_leavedays") == null ? null : rs.getBigDecimal("matern_leavedays").intValue();
				item.setMatern_leavedays(leaveDays);
				item.setMedical_fee(rs.getBigDecimal("medical_fee"));
				item.setReimb_date(rs.getDate("reimb_date"));
				item.setSubsidy(rs.getBigDecimal("subsidy"));
				item.setWork_station(convertStr(rs.getString("work_station")));
				return item;
			}
			
		});
	}

	@Override
	public Map<String, Object> queryChronicBenefitMI(Map<String, Object> condition) throws Exception {
		///xuhai add start
		Map<String, Object> retrmap = new HashMap<String, Object>();
		Map<String, Object> retrmap2 = new HashMap<String, Object>();
		if(condition != null){
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", String.valueOf(condition.get("idcard")));//公民身份证号
			map.put("parm1",  String.valueOf(condition.get("username")));//姓名
			map.put("parm2",  String.valueOf(condition.get("ss_num")));//社会保障卡号
			// hsoft.yibao.pspdireinfo.get(个人特殊疾病登记信息查询)
			//TheKeyResultEntity res = PspdireinfoApi.getPspdireinfo(map);
			TheKeyResultEntity res =baseApi.getDataInfo(map, BaseEntity.pspdireinfo);
			if(res!=null){
				if("ok".equals(res.getStatus())){
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() ;
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
					if((!lm.isEmpty()) ){//有元素的时候
						for (Map<String, String> map2 : lm) {
							// 1.门诊特殊疾病
							if("1".equals(map2.get("col0").trim())){
								if(retrmap.get("approval_date")!=null&&map2.get("col3")!=null){//根据//审批日期 排序 
									Date first =  (Date) retrmap.get("approval_date");
									Date second = DateUtils.std(map2.get("col3"), 3);
									//下一个数据的时间  比较 上一条时间 还早的话 就继续 下一条 
									if(second.getTime()<first.getTime()){//second 在  first 前 即 老时间  继续
									     continue;
									}
								}
								retrmap.put("chronic_type", map2.get("col1")); // 类别名
								retrmap.put("approval_date", DateUtils.std(map2.get("col3"), 3));//审批日期	初步认定时间
//								retrmap.put("sickness_type", map2.get("col2"));//病种	特殊门诊疾病名称
								retrmap.put("sickness_type_list", sicknessTypeList(map2.get("col2").toString(), map2.get("col7").toString(), map2.get("col8").toString(), map2.get("col9").toString(), map2.get("col10").toString(), map2.get("col11").toString())) ;
								retrmap.put("benefit_start_date", DateUtils.std(map2.get("col4"), 3));//享受日期
								retrmap.put("benefit_end_date", DateUtils.std(map2.get("col5"), 3));//截止日期
								continue ;
								
								// 2. 门诊特殊治疗
							} else if("2".equals(map2.get("col0").trim())) {
								if(retrmap2.get("approval_date")!=null&&map2.get("col3")!=null){//根据//审批日期 排序 
									Date first =  (Date) retrmap2.get("approval_date");
									Date second = DateUtils.std(map2.get("col3"), 3);
									//下一个数据的时间  比较 上一条时间 还早的话 就继续 下一条 
									if(second.getTime()<first.getTime()){//second 在  first 前 即 老时间  继续
									     continue;
									}
								}
								retrmap2.put("chronic_type", map2.get("col1"));
								retrmap2.put("approval_date", DateUtils.std(map2.get("col3"), 3));//审批日期	初步认定时间
								retrmap2.put("sickness_type", map2.get("col2"));//病种	特殊门诊疾病名称
								retrmap2.put("benefit_start_date", DateUtils.std(map2.get("col4"), 3));//享受日期
								retrmap2.put("benefit_end_date", DateUtils.std(map2.get("col5"), 3));//截止日期
							}
						}
						list.add(retrmap) ;
						list.add(retrmap2) ;
						Map<String, Object> result = new HashMap<String, Object>() ;
						result.put("list", list) ;
						return result ;
					}
				}
			}
			
		} ;
		return null ;
		/// xuhai add end
	}
	
	// 特殊门诊疾病名称
	private List<Map<String, Object>> sicknessTypeList(String col2, String col7, String col8, String col9, String col10, String col11){
		List<Map<String, Object>> sickness_type_list = new ArrayList<Map<String, Object>>() ; // 合并症
		Map<String, Object> map = new HashMap<String, Object>() ;
		if(StringUtils.isNotBlank(col2)){
			map.put("type_name", col2.split("\\[")[0]) ;
			map.put("complication1", col8) ;
			map.put("complication2", col9) ;
			sickness_type_list.add(map) ;
		}
		
		if(StringUtils.isNotBlank(col7)){
			map = new HashMap<String, Object>() ;
			map.put("type_name", col7.split("\\[")[0]) ;
			map.put("complication1", col10) ;
			map.put("complication2", col11) ;
			sickness_type_list.add(map) ;
		}
		
		return sickness_type_list ;
	}

}