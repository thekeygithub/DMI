/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.statistics.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.core.utils.ConvertUtils;
import com.core.utils.DateUtils;
import com.core.utils.MathUtils;
import com.ebmi.seng.ConsumpteModel;
import com.ebmi.seng.YearStatistics;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.statistics.dao.StatisticsDao;
import com.ebmi.std.statistics.dto.PYearMedical;
import com.ebmi.std.statistics.service.StatisticsService;

/**
 * 统计数据访问
 * 
 * @author xiangfeng.guan
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Resource
	private StatisticsDao dao;
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;
	
	@Override
	public YearStatistics getPyearmedicalDatil(PMiBaseInfoCond cond) throws Exception {
		if(cond != null){
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getDept_num_one());//机构编号
			map.put("parm1", cond.getDept_num_two());//定点医疗服务机构编号
			map.put("parm2", cond.getItem_id());//流水号
			//hsoft.yibao.micdinfo.get(医疗保险费用明细信息查询)
			YearStatistics pmi = new YearStatistics();
			TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.micdinfo);
			/* col0	String	阿替洛尔	                                            收费项目名称
			   col1	String		                   收费项目类别小类
			   col2	String	甲类	                                                        收费项目等级
			   col3	String	0.0000	                                            自付比例
			   col4	String	0.00	                                            支付限额
			   col5	String	1.00	                                            数量
			   col6	String	2.2000	                                            单价
			   col7	String	2.2000	                                            总金额
			   col8	String	2015-12-01 17:55:11.0     处方日期
			   col9 String                         收费项目类别大类
			*/
			List<ConsumpteModel> dlist = new ArrayList<ConsumpteModel>();
			if(res!=null && "ok".equals(res.getStatus())){
				List<Map<String, String>> lm = res.getData();
				if(!lm.isEmpty()){//有元素的时候
					for (Map<String, String> map2 : lm) {
						ConsumpteModel model = new ConsumpteModel();
						model.setSubject_name(map2.get("col0"));
						model.setSubject_grade(map2.get("col2"));
						model.setPay_rate(map2.get("col3"));
						model.setAmount(map2.get("col5"));
						model.setSum_moey(map2.get("col7"));
						dlist.add(model);
					}
					
				}
			
			}
			pmi.setConsumpteList(dlist);
			return pmi;
		}
		
		return null;
	}
	
	
/*	@Override
	public YearStatistics getPyearmedicalDatil(PMiBaseInfoCond cond)
			throws Exception {
		if(cond != null){
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());//公民身份证号
			map.put("parm1", cond.getUsername());//姓名
			map.put("parm2", cond.getSs_num());//社会保障卡号
			map.put("parm3", cond.getTable_stat());//parm3 String 必须 状态 1工作表 2历史表 
			
			YearStatistics pmi = new YearStatistics();
			if(StringUtils.isBlank(cond.getMed_type_name())){
				return pmi;
			}
			//if("3".equals(cond.getSta_type())){//3就诊类型
//				parm0 String 必须 公民身份证号  parm1 String 必须 姓名   parm2 String 必须 社会保障卡号  parm3 String 必须 日期 
			      //获取指定年份的第一天 
				String datastr = formatDate(getYearFirst(Integer.valueOf(cond.getYear())), "yyyyMMdd");
				map.put("parm3", datastr);//parm3 String 日期 
				map.put("parm4", formatDate(new Date(), "yyyyMMdd"));//parm4	String	必须	终止日期
				//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
				TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.demeinpayinfo);
				//存储 明细 根据 类型名称、年份   过滤  
				List<PayDatil> dlist = new ArrayList<PayDatil>();
				if(res!=null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						System.out.println("demeinpayinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
						if((!lm.isEmpty()) ){//有元素的时候
							
							for (Map<String, String>  map2 : lm) {
								
								String typename =  map2.get("col3");
								String jiesj =  map2.get("col1");
								//System.out.println("demeinpayinfo typename>>>"+typename+"&jiesj>>"+jiesj);
								int datayear = 0;
								if(StringUtils.isNotBlank(jiesj)){
									Calendar calendar = Calendar.getInstance();
									calendar.clear();
									calendar.setTime(DateUtils.std(jiesj, 1));
									datayear =  calendar.get(Calendar.YEAR); 
								}else{//结算时间为空的直接跳过
									continue;
								}
								//获取  类型名称以及结算单日期的年份和数据查询参数的类型名称和年份一致  数据
								if(cond.getMed_type_name().equals(typename)&&datayear==Integer.valueOf(cond.getYear())){
									PayDatil pd = new PayDatil();
									pd.setPay_tot(ConvertUtils.getDouble(map2.get("col4")));
									pd.setMed_type_name(typename);
									pd.setOrg_name(map2.get("col2"));
									
									if(StringUtils.isNotBlank(map2.get("col1"))){
										pd.setClm_date(DateUtils.std(map2.get("col1"), 1));
									}
									dlist.add(pd);
								}
//								System.out.println("type>>>"+type+"&money>>"+money);

							 }
						}
					}
					
				}
				//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询) 
				res = baseApi.getDataInfo(map, BaseEntity.miaacareinfo);
				//要过滤出 医疗类别 和  对应的   医疗类别总额    本次发生医疗费总额
				if(res!=null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						System.out.println("miaacareinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
						if((!lm.isEmpty()) ){//有元素的时候
							for (Map<String, String>  map2 : lm) {
								String typename =  map2.get("col3");
								String jiesj =  map2.get("col1");
								//System.out.println("demeinpayinfo typename>>>"+typename+"&jiesj>>"+jiesj);
								int datayear = 0;
								if(StringUtils.isNotBlank(jiesj)){
									Calendar calendar = Calendar.getInstance();
									calendar.clear();
									calendar.setTime(DateUtils.std(jiesj, 1));
									datayear =  calendar.get(Calendar.YEAR); 
								}else{//结算时间为空的直接跳过
									continue;
								}
								//获取  类型名称以及结算单日期的年份和数据查询参数的类型名称和年份一致  数据
								if(cond.getMed_type_name().equals(typename)&&datayear==Integer.valueOf(cond.getYear())){
									PayDatil pd = new PayDatil();
									pd.setPay_tot(ConvertUtils.getDouble(map2.get("col4")));
									pd.setMed_type_name(typename);
									pd.setOrg_name(map2.get("col2"));
									
									if(StringUtils.isNotBlank(map2.get("col1"))){
										pd.setClm_date(DateUtils.std(map2.get("col1"), 1));
									}
									dlist.add(pd);
								}
							 }
						}
					}
				}
				pmi.setDlist(dlist);
				return pmi;
			//}
		}
		return null;
	}*/
	
	@Override
	public PYearMedical getPYearMedical(String p_mi_id, String year) throws Exception {
		return dao.getPYearMedical(p_mi_id, year);
	}
    //xuhai add 2016-5-25 15:22:09
	@Override
	public YearStatistics getPYearMedicalNew(PMiBaseInfoCond cond)
			throws Exception {
		if(cond != null){
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());//公民身份证号
			map.put("parm1", cond.getUsername());//姓名
			map.put("parm2", cond.getSs_num());//社会保障卡号
			map.put("parm3", cond.getTable_stat());//parm3 String 必须 状态 1工作表 2历史表 
			YearStatistics pmi = new YearStatistics();
			if("1".equals(cond.getSta_type())){//年度费用统计类型（1支付构成、3就诊类型）
				//////////////////////////////////////////////////////////////
//				parm0 String 必须 公民身份证号  parm1 String 必须 姓名   parm2 String 必须 社会保障卡号  parm3 String 必须 日期 
				String datastr = formatDate(getYearFirst(Integer.valueOf(cond.getYear())), "yyyyMMdd");
				map.put("parm3", datastr);//parm3 String 日期 
				map.put("parm4", formatDate(new Date(), "yyyyMMdd"));//parm4	String	必须	终止日期
				//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
				TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.demeinpayinfo);
				
//				//个人支付：本次个人帐户支付金额（col6）+本次个人支付金额(col9)
		        Double person_paytotal=0.00d;
		        Double person_zhanghuzfje=0.00d;//：本次个人帐户支付金额（col6）
		        Double person_zhujine=0.00d;//本次个人支付金额(col9)
		         
//			  ////医保支付：本次统筹金支付金额(col5)+本次公务员基金支付金额(col7)+本次大额医疗补助支付金额（col8）
		         Double curyear_paytotal=0.00d;// 本次统筹金支付金额(col5)
		         Double civil_paytotal=0.00d;//本次公务员基金支付金额(col7)
		         Double large_paytotal=0.00d;//本次大额医疗补助支付金额（col8）
				
				if(res!=null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						System.out.println("demeinpayinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
						if((!lm.isEmpty()) ){//有元素的时候
							
							for (Map<String, String>  map2 : lm) {
								//时间过滤 start 结算时间
								String datestr =  map2.get("col1");
								if(StringUtils.isBlank(datestr)){//时间未获取的到 直接过滤
									continue;
								}
								Date d = DateUtils.std(datestr,2);
								Calendar calendar = Calendar.getInstance();
										calendar.setTime(d);
								int year =calendar.get(Calendar.YEAR);
								if(year != Integer.valueOf(cond.getYear())){//结算单时间年份不等于传入的年份的时候直接过滤
									continue;
								}
                               //时间过滤 end 								
								///////////////////////////////////////////////
							//个人支付：本次个人帐户支付金额（col6）+本次个人支付金额(col9)
								person_zhanghuzfje= MathUtils.sum(person_zhanghuzfje,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));
								pmi.setPerson_zhanghuzfje(person_zhanghuzfje);
								////本次个人支付金额(col9)
								person_zhujine= MathUtils.sum(person_zhujine,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));
								pmi.setPerson_zhujine(person_zhujine);
								
								
							////医保支付：本次统筹金支付金额(col5)+本次公务员基金支付金额(col7)+本次大额医疗补助支付金额（col8）
								// // 本次统筹金支付金额(col5)
								curyear_paytotal = MathUtils.sum(curyear_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
								pmi.setCuryear_paytotal(curyear_paytotal);
								
								// col7(本年统筹支付累计 )
								civil_paytotal = MathUtils.sum(civil_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
								pmi.setCivil_paytotal(civil_paytotal);
								
								 //本次大额医疗补助支付金额（col8）
								large_paytotal = MathUtils.sum(large_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
								pmi.setLarge_paytotal(large_paytotal);
								
								///////////////////////////////////////////////							
							 }
						}
					}
					
				}
				//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询) 
				res = baseApi.getDataInfo(map, BaseEntity.miaacareinfo);
				//要过滤出 医疗类别 和  对应的   医疗类别总额    本次发生医疗费总额
				if(res!=null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						System.out.println("miaacareinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
						if((!lm.isEmpty()) ){//有元素的时候
							
							for (Map<String, String>  map2 : lm) {
								//时间过滤 start 结算时间
								String datestr =  map2.get("col1");
								if(StringUtils.isBlank(datestr)){//时间未获取的到 直接过滤
									continue;
								}
								Date d = DateUtils.std(datestr,2);
								Calendar calendar = Calendar.getInstance();
										calendar.setTime(d);
								int year =calendar.get(Calendar.YEAR);
								if(year != Integer.valueOf(cond.getYear())){//结算单时间年份不等于传入的年份的时候直接过滤
									continue;
								}
		                 ///////////////////////////////////////////////
						//个人支付：本次个人帐户支付金额（col6）+本次个人支付金额(col9)
							person_zhanghuzfje= MathUtils.sum(person_zhanghuzfje,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));
							pmi.setPerson_zhanghuzfje(person_zhanghuzfje);
							////本次个人支付金额(col9)
							person_zhujine= MathUtils.sum(person_zhujine,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));
							pmi.setPerson_zhujine(person_zhujine);
							
							
						////医保支付：本次统筹金支付金额(col5)+本次公务员基金支付金额(col7)+本次大额医疗补助支付金额（col8）
							// // 本次统筹金支付金额(col5)
							curyear_paytotal = MathUtils.sum(curyear_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
							pmi.setCuryear_paytotal(curyear_paytotal);
							
							// col7(本年统筹支付累计 )
							civil_paytotal = MathUtils.sum(civil_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
							pmi.setCivil_paytotal(civil_paytotal);
							
							 //本次大额医疗补助支付金额（col8）
							large_paytotal = MathUtils.sum(large_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
							pmi.setLarge_paytotal(large_paytotal);
							
							///////////////////////////////////////////////	
								
							 }
						}
					}
				}
				return pmi;
				
				////////////////////////////////////////////////////////////////////end
				
				
				
				///hsoft.yibao.pmetrinfo.get(个人医疗待遇信息查询)
//				TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.pmetrinfo);
//				if(res!=null){
//					if("ok".equals(res.getStatus())){
//						List<Map<String, String>> lm = res.getData();
//						System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
//						if((!lm.isEmpty()) ){//有元素的时候
//							//个人支付金额 col3(本年帐户支付累计金额 ) 
//					        Double person_paytotal=0.00d;
//
//						  ////医保支付金额=col5(本年统筹支付累计)+col7(本年公务员基金支付累计)+col6(本年大额医疗补助支付累计)
//						    Double curyear_paytotal=0.00d;// col5(本年统筹支付累计 )
//						    Double civil_paytotal=0.00d;//col7( 本年公务员补助支付累计 )
//						    Double large_paytotal=0.00d;//col6(本年大额医疗补助支付累计 )
//						     
//							for (Map<String, String> map2 : lm) {//这样写时因为查询历史年的时候 是返回的多条记录 ，多条记录的时候 进行累加操作
//								//个人支付金额 col3(本年帐户支付累计金额 ) 
//								person_paytotal = MathUtils.sum(person_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col3"))?"0.00":map2.get("col3")));
//								pmi.setPerson_paytotal(person_paytotal);
//							    
//								////医保支付金额=col5(本年统筹支付累计)+col7(本年公务员基金支付累计)+col6(本年大额医疗补助支付累计)
//								// col5(本年统筹支付累计 )
//								curyear_paytotal = MathUtils.sum(curyear_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
//								pmi.setCuryear_paytotal(curyear_paytotal);
//								
//								// col7(本年统筹支付累计 )
//								civil_paytotal = MathUtils.sum(civil_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
//								pmi.setCivil_paytotal(civil_paytotal);
//								
//								 //col6(本年大额医疗补助支付累计 )
//								large_paytotal = MathUtils.sum(civil_paytotal,ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));
//								pmi.setLarge_paytotal(large_paytotal);
//								
//							}
//						}
//					}
//					return pmi;
//				}
			}
			if("3".equals(cond.getSta_type())){//3就诊类型
//				parm0 String 必须 公民身份证号  parm1 String 必须 姓名   parm2 String 必须 社会保障卡号  parm3 String 必须 日期 
				String datastr = formatDate(getYearFirst(Integer.valueOf(cond.getYear())), "yyyyMMdd");
				map.put("parm3", datastr);//parm3 String 日期 
				map.put("parm4", formatDate(new Date(), "yyyyMMdd"));//parm4	String	必须	终止日期
				//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
				TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.demeinpayinfo);
				//要过滤出 医疗类别 和  对应的   医疗类别总额    本次发生医疗费总额
				Map<String,Double> mapdata = new HashMap<String, Double>();
				if(res!=null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						System.out.println("demeinpayinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
						if((!lm.isEmpty()) ){//有元素的时候
							
							for (Map<String, String>  map2 : lm) {
								//时间过滤 start 结算时间
								String datestr =  map2.get("col1");
								if(StringUtils.isBlank(datestr)){//时间未获取的到 直接过滤
									continue;
								}
								Date d = DateUtils.std(datestr,2);
								Calendar calendar = Calendar.getInstance();
										calendar.setTime(d);
								int year =calendar.get(Calendar.YEAR);
								if(year != Integer.valueOf(cond.getYear())){//结算单时间年份不等于传入的年份的时候直接过滤
									continue;
								}
                               //时间过滤 end 								
								
								
								String type =  map2.get("col3");
								String money =  map2.get("col4");
//								System.out.println("type>>>"+type+"&money>>"+money);
								if(mapdata.containsKey(type)){//包含的话 就累计金额
									if(StringUtils.isNotBlank(money)){
										Double oldm = mapdata.get(type);
										mapdata.put(type, MathUtils.sum(oldm,Double.valueOf(money)));
									}
								}else{//不存在的话 就新放入
									if(StringUtils.isNotBlank(money)){
									   mapdata.put(type,Double.valueOf(money));
									}
								}
							 }
						}
					}
					
				}
				//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询) 
				res = baseApi.getDataInfo(map, BaseEntity.miaacareinfo);
				//要过滤出 医疗类别 和  对应的   医疗类别总额    本次发生医疗费总额
				if(res!=null){
					if("ok".equals(res.getStatus())){
						List<Map<String, String>> lm = res.getData();
						System.out.println("miaacareinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
						if((!lm.isEmpty()) ){//有元素的时候
							
							for (Map<String, String>  map2 : lm) {
								//时间过滤 start 结算时间
								String datestr =  map2.get("col1");
								if(StringUtils.isBlank(datestr)){//时间未获取的到 直接过滤
									continue;
								}
								Date d = DateUtils.std(datestr,2);
								Calendar calendar = Calendar.getInstance();
										calendar.setTime(d);
								int year =calendar.get(Calendar.YEAR);
								if(year != Integer.valueOf(cond.getYear())){//结算单时间年份不等于传入的年份的时候直接过滤
									continue;
								}
                               //时间过滤 end 					
								String type =  map2.get("col3");
								String money =  map2.get("col4");
								//System.out.println("miaacareinfo type>>>"+type+"&money>>"+money);
								if(mapdata.containsKey(type)){//包含的话 就累计金额
									if(StringUtils.isNotBlank(money)){
										Double oldm = mapdata.get(type);
										mapdata.put(type, MathUtils.sum(oldm,Double.valueOf(money)));
									}
								}else{//不存在的话 就新放入
									if(StringUtils.isNotBlank(money)){
									   mapdata.put(type,Double.valueOf(money));
									}
								}
							 }
						}
					}
				}
				pmi.setMapdata(mapdata);
				return pmi;
			}
			
		}
		return null;
	}
	
	
	/**
	 * 获取某年第一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year){
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	
	/**
	 * 格式化日期
	 * @param date 日期对象
	 * @return String 日期字符串
	 */
	public static String formatDate(Date date,String format){
		SimpleDateFormat f = new SimpleDateFormat(format);
		String sDate = f.format(date);
		return sDate;
	}

	
	
}
