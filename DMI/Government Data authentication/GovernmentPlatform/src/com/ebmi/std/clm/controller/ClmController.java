/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.clm.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.core.utils.ConvertUtils;
import com.core.utils.DateUtils;
import com.ebmi.std.clm.cond.ClmChrgSumCond;
import com.ebmi.std.clm.cond.ClmDiagStdCondition;
import com.ebmi.std.clm.cond.ClmInfoCondition;
import com.ebmi.std.clm.cond.ClmItemCondition;
import com.ebmi.std.clm.dto.PClaimChrgSum;
import com.ebmi.std.clm.dto.PClaimConsume;
import com.ebmi.std.clm.dto.PClaimDiagStd;
import com.ebmi.std.clm.dto.PClaimHead;
import com.ebmi.std.clm.dto.PClaimItem;
import com.ebmi.std.clm.service.ClmService;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.util.BaseApiCallable;
import com.ebmi.std.util.DateTimeUtil;

/**
 * @author xiangfeng.guan
 *
 */
@Controller
@RequestMapping("/clm")
public class ClmController {

	private static final Logger logger = LoggerFactory.getLogger(ClmController.class);

	@Resource(name = "clmServiceImpl")
	private ClmService clmService;
	
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;
	
	//xuhai edit 2016-5-24
	//{recordCount=5, username=孙艳辉, recordStart=0, p_mi_id=222, pay_date=20120601, idcard=230403197611280546, ss_num=, order_by= CLM_DATE desc }
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PClaimHead> queryClmInfoList(String p_mi_id, String year, String clm_id, String order_by,
			Integer recordStart, Integer recordCount,String medType
			,String idcard,String username,String ss_num,String pay_date) {
//		System.out.println("queryClmInfoList pay_date>>>>>>>>>>>>>>>>>>>>>>>"+pay_date);
//		List<PClaimConsume> lists = new ArrayList<PClaimConsume>();
//		try {
//			Map<String, String> map = new LinkedHashMap<String, String>();
//			if(StringUtils.isNotBlank(idcard)){
//				map.put("parm0",idcard);//公民身份证号
//			}
//			if(StringUtils.isNotBlank(username)){
//				map.put("parm1",username);//姓名
//			}
//			if(StringUtils.isNotBlank(ss_num)){
//				map.put("parm2",ss_num);//社会保障卡号
//			}
//			if(StringUtils.isNotBlank(pay_date)){
//				map.put("parm3",pay_date);//日期
//			}
//			if(!map.isEmpty()){
//				//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
//				//TheKeyResultEntity res = DemeinpayinfoApi.getDemeinpayinfo(map);
//				TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.demeinpayinfo);
//				if(res!=null){
//					if("ok".equals(res.getStatus())){
//						List<Map<String, String>> lm = res.getData();
//						System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>"+lm.size());
//						if((lm!=null&&!lm.isEmpty()) ){//有元素的时候
//							for (Map<String, String> map2 : lm) {
//								PClaimConsume pmi = new PClaimConsume();
//								pmi.setDate(DateUtils.std(map2.get("col1"), 2));//结算日期
//								pmi.setAll_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//总额
//								pmi.setCash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//现金支付
//								pmi.setPersonal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//个人账户
//								pmi.setEnt(map2.get("col2"));//消费地点
//								pmi.setMed_type(map2.get("col3"));//医疗类别
//								pmi.setCur_all_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//本次发生医疗费 总额
//								pmi.setCur_cash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//本次个人现金支付金额
//								pmi.setCur_personal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//本次个人账户支付金额
//								pmi.setClm_id(map2.get("col13"));//结算单编号 级对应流水号
//								//本次统筹金支付金额+本次公务员基金支付金额+本次大额医疗补助支付金额	col5+col7+col8
//								pmi.setPay_five(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
//								pmi.setPay_seven(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
//								pmi.setPay_eight(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
//								if(StringUtils.isNotBlank(clm_id)){
//									if(clm_id.equals(map2.get("col13"))){
//										lists.add(pmi);
//										break;
//									}
//								}else{
//									lists.add(pmi);
//								}
//							}
//						}
//					}
//				}
//				//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询)
//				 //res = MiaacareinfoApi.getMiaacareinfo(map);
//				 res = baseApi.getDataInfo(map, BaseEntity.miaacareinfo);
//				 if(res!=null){
//					if("ok".equals(res.getStatus())){
//						List<Map<String, String>> lm = res.getData();
//						if((lm!=null&&!lm.isEmpty()) ){//有元素的时候
//							for (Map<String, String> map2 : lm) {
//								PClaimConsume pmi = new PClaimConsume();
//								pmi.setDate(DateUtils.std(map2.get("col1"), 2));//结算日期
//								pmi.setAll_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//总额
//								pmi.setCash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//现金支付
//								pmi.setPersonal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//个人账户
//								pmi.setEnt(map2.get("col2"));//消费地点
//								pmi.setMed_type(map2.get("col3"));//医疗类别
//								pmi.setCur_all_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//本次发生医疗费 总额
//								pmi.setCur_cash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//本次个人账户支付金额
//								pmi.setCur_personal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//本次个人现金支付金额
//								pmi.setClm_id(map2.get("col13"));//结算单编号 级对应流水号
//								
//								//本次统筹金支付金额+本次公务员基金支付金额+本次大额医疗补助支付金额	col5+col7+col8
//								pmi.setPay_five(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
//								pmi.setPay_seven(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
//								pmi.setPay_eight(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
//								if(StringUtils.isNotBlank(clm_id)){
//									if(clm_id.equals(map2.get("col13"))){
//										lists.add(pmi);
//										break;
//									}
//								}else{
//									lists.add(pmi);
//								}
//							}
//						}
//					}
//				}
//			}
//			//根据日期倒序 即 最新的一条记录在第一条
//			 Collections.sort(lists, new Comparator<PClaimConsume>() {      
//					@Override
//					public int compare(PClaimConsume paramT1, PClaimConsume paramT2) {
//						return paramT2.getDate().compareTo(paramT1.getDate());
//					}
//				 }); 
//			 
//			return lists;
//			
//		} catch (Exception e) {
//			logger.error("查询医保消费列表报错！", e);
//		}
//		return null;
		try {
			ClmInfoCondition condition = new ClmInfoCondition();
			condition.setP_mi_id(p_mi_id);
			condition.setClm_id(clm_id);
			condition.setOrder_by(order_by);
			condition.setRecordStart(recordStart);
			condition.setRecordCount(recordCount);
			condition.setMedType(medType);
			if (!StringUtils.isEmpty(year)) {
				int y = Integer.parseInt(year);
				Calendar c = Calendar.getInstance();
				c.set(y, 0, 1, 0, 0, 0);
				c.set(Calendar.MILLISECOND, 0);
				condition.setClm_date_start(c.getTime());
				c.add(Calendar.YEAR, 1);
				condition.setClm_date_end(c.getTime());
			}
			return clmService.queryClmInfoList(condition);
		} catch (Exception e) {
			logger.error("查询结算单列表报错！", e);
		}
		return null;
	}
	
	//组装支付构成和费用构成数据
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List contantCottion(Map map2) throws Exception{
		//定义初始化list和map
		List pay_list = new ArrayList();
		LinkedHashMap parram = new LinkedHashMap();
		//支付构成===hsoft.yibao.demeinpayinfo.get
		parram.put("disp_name", null);
		parram.put("money", null);
		parram.put("fee_col_name", "支付构成");
		pay_list.add(parram);
		
		parram = new LinkedHashMap();
		parram.put("disp_name", "统筹金支付");
		parram.put("money", map2.get("col5"));
		parram.put("fee_col_name", null);
		pay_list.add(parram);
		
		parram = new LinkedHashMap();
		parram.put("disp_name", "大额医疗补助");
		parram.put("money", map2.get("col8"));
		parram.put("fee_col_name", null);
		pay_list.add(parram);
		
		parram = new LinkedHashMap();
		parram.put("disp_name", "公务员补助");
		parram.put("money", map2.get("col7"));
		parram.put("fee_col_name", null);
		pay_list.add(parram);
		
		parram = new LinkedHashMap();
		parram.put("disp_name", "现金支付");
		parram.put("money", map2.get("col9"));
		parram.put("fee_col_name", null);
		pay_list.add(parram);
		
		parram = new LinkedHashMap();
		parram.put("disp_name", "个人账户");
		parram.put("money", map2.get("col6"));
		parram.put("fee_col_name", null);
		pay_list.add(parram);
		
		//费用构成===hsoft.yibao.micdinfo.get
		parram = new LinkedHashMap();
		parram.put("disp_name", null);
		parram.put("money", null);
		parram.put("fee_col_name", "费用构成");
		pay_list.add(parram);
		
		//调用费用构成接口，合并相同类型，金额相加
		LinkedHashMap temp = new LinkedHashMap();
		Map map = new HashMap();
		map.put("parm0", map2.get("col11"));//机构编号
		map.put("parm1", map2.get("col12"));//定点医疗服务机构编号
		map.put("parm2", map2.get("col13"));//流水号
		TheKeyResultEntity results = baseApi.getDataInfo(map, BaseEntity.micdinfo);
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
		if(results!=null && "ok".equals(results.getStatus())){
			List<Map<String, String>> datas = results.getData();
			for (Map<String, String> em : datas) {
				/*
				 * 02      诊疗项目
				 * 021   化验
				 * 022   检查
				 * 023   治疗
				 * 024   一般医用材料
				 * 025   特殊医用材料
				 * */
				String item_name = em.get("col9").toString();
				//if("0".equals(item_name)){
				//	continue;
				//}
//				if(item_name.length() > 2){//去除费用二字
//					item_name = item_name.substring(0, 2); 
//				}
				String item_money = em.get("col7").toString();
				boolean contains = temp.containsKey(item_name);
				if(contains){
					String moneys = temp.get(item_name).toString();
					temp.put(item_name, String.format("%.2f", Double.valueOf(item_money)+Double.valueOf(moneys)));
				}else{
					temp.put(item_name, item_money);
				}
			}
		}
		//遍历temp集合
		Iterator item = temp.keySet().iterator();
		while(item.hasNext()){
			parram = new LinkedHashMap();
			String key = (String)item.next();
			parram.put("disp_name", key);
			parram.put("money", temp.get(key));
			parram.put("fee_col_name", null);
			pay_list.add(parram);
		}
		
		return pay_list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/listNew", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PClaimConsume> queryClmInfoListNew(String p_mi_id, String year, String clm_id, String order_by, 
			Integer recordStart, Integer recordCount, String medType, String idcard, String username, String ss_num, String pay_date){
		
		List<PClaimConsume> lists = new ArrayList<PClaimConsume>();
		TheKeyResultEntity res = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(year+"12"+DateTimeUtil.getLastDate(year, "12"));
			Calendar now = Calendar.getInstance();
			now.setTime(date);
			int yearnew = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH) + 1;
			ExecutorService demeinpayinfopool = Executors.newFixedThreadPool(month);
			List<Future> demeinpayinfolist = new ArrayList<Future>();
			for (int i = 1; i <= month; i++) {
				//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
				Callable c = new ClmBaseApiCallable(baseApi, BaseEntity.demeinpayinfo, idcard, username, ss_num, yearnew, i);
				Future f = demeinpayinfopool.submit(c);
				demeinpayinfolist.add(f);
			}
			demeinpayinfopool.shutdown();
			for (Future f : demeinpayinfolist) {
				res = (TheKeyResultEntity) f.get();
				if(res!=null && "ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					if((lm!=null && !lm.isEmpty())){//有元素的时候
						for (Map<String, String> map2 : lm) {
							PClaimConsume pmi = new PClaimConsume();
							pmi.setDate(DateUtils.std(map2.get("col1"), 2));//结算日期
							pmi.setAll_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//总额
							pmi.setCash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//现金支付
							pmi.setPersonal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//个人账户
							pmi.setEnt(map2.get("col2"));//消费地点
							pmi.setMed_type(map2.get("col3"));//医疗类别
							pmi.setCur_all_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//本次发生医疗费 总额
							pmi.setCur_cash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//本次个人现金支付金额
							pmi.setCur_personal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//本次个人账户支付金额
							pmi.setOrg_code(map2.get("col11"));//机构编号
							pmi.setHosptial_server_code(map2.get("col12"));//定点医疗服务机构编号
							pmi.setClm_id(map2.get("col13"));//结算单编号 级对应流水号
							//本次统筹金支付金额+本次公务员基金支付金额+本次大额医疗补助支付金额	col5+col7+col8
							pmi.setPay_five(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
							pmi.setPay_seven(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
							pmi.setPay_eight(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
							if(StringUtils.isNotBlank(clm_id)){
								if(clm_id.equals(map2.get("col13"))){
									lists.add(pmi);
									break;
								}
							}else{
								List pay_list = contantCottion(map2);
								pmi.setPay_list(pay_list);
								lists.add(pmi);
							}
						}
					}
				}
			}
			
			ExecutorService poolmiaacareinfo = Executors.newFixedThreadPool(month);
			List<Future> listmiaacareinfo = new ArrayList<Future>();
			for (int i = 1; i <= month; i++) {
				//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询)
				Callable c = new ClmBaseApiCallable(baseApi, BaseEntity.miaacareinfo, idcard, username, ss_num, yearnew, i);
				Future f = poolmiaacareinfo.submit(c);
				listmiaacareinfo.add(f);
			}
			poolmiaacareinfo.shutdown();
			for (Future f : listmiaacareinfo) {
				res = (TheKeyResultEntity) f.get();
				if(res!=null && "ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					if((lm!=null && !lm.isEmpty())){//有元素的时候
						for (Map<String, String> map2 : lm) {
							PClaimConsume pmi = new PClaimConsume();
							pmi.setDate(DateUtils.std(map2.get("col1"), 2));//结算日期
							pmi.setAll_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//总额
							pmi.setCash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//现金支付
							pmi.setPersonal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//个人账户
							pmi.setEnt(map2.get("col2"));//消费地点
							pmi.setMed_type(map2.get("col3"));//医疗类别
							pmi.setCur_all_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//本次发生医疗费 总额
							pmi.setCur_cash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//本次个人账户支付金额
							pmi.setCur_personal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//本次个人现金支付金额
							pmi.setOrg_code(map2.get("col11"));//机构编号
							pmi.setHosptial_server_code(map2.get("col12"));//定点医疗服务机构编号
							pmi.setClm_id(map2.get("col13"));//结算单编号 级对应流水号
							//本次统筹金支付金额+本次公务员基金支付金额+本次大额医疗补助支付金额	col5+col7+col8
							pmi.setPay_five(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
							pmi.setPay_seven(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
							pmi.setPay_eight(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
							if(StringUtils.isNotBlank(clm_id)){
								if(clm_id.equals(map2.get("col13"))){
									lists.add(pmi);
									break;
								}
							}else{
								List pay_list = contantCottion(map2);
								pmi.setPay_list(pay_list);
								lists.add(pmi);
							}
						}
					}
				}
			}
			//根据日期倒序 即 最新的一条记录在第一条
			Collections.sort(lists, new Comparator<PClaimConsume>(){
				@Override
				public int compare(PClaimConsume paramT1, PClaimConsume paramT2) {
					return paramT2.getDate().compareTo(paramT1.getDate());
				}
			}); 
			 
			return lists;
			
		} catch (Exception e) {
			logger.error("查询医保消费列表报错！", e);
		}
		return null;
	}
	
	/**
	 * @描述：医保消费查询接口方法备份
	 * @作者：SZ
	 * @时间：2017年12月18日 上午11:46:49
	 * @param p_mi_id
	 * @param year
	 * @param clm_id
	 * @param order_by
	 * @param recordStart
	 * @param recordCount
	 * @param medType
	 * @param idcard
	 * @param username
	 * @param ss_num
	 * @param pay_date
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public @ResponseBody List<PClaimConsume> queryClmInfoListNew_bak(String p_mi_id, String year, String clm_id, String order_by, 
			Integer recordStart, Integer recordCount, String medType, String idcard, String username, String ss_num, String pay_date){
		
		List<PClaimConsume> lists = new ArrayList<PClaimConsume>();
		try {
			Map<String, String> map = new LinkedHashMap<String, String>();
				map.put("parm0", idcard);//公民身份证号
				map.put("parm1", username);//姓名
				map.put("parm2", ss_num);//社会保障卡号
				map.put("parm3", year+"0101");//开始日期
				map.put("parm4", year+"12"+DateTimeUtil.getLastDate(year, "12"));//终止日期
				//map.put("parm3", pay_date);
				//map.put("parm4", DateTimeUtil.formatDate(new Date(), "yyyyMMdd"));
			if(!map.isEmpty()){
				//hsoft.yibao.demeinpayinfo.get(定点医疗机构端消费信息查询)
				//TheKeyResultEntity res = DemeinpayinfoApi.getDemeinpayinfo(map);
				TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.demeinpayinfo);
				if(res!=null && "ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					if((lm!=null && !lm.isEmpty())){//有元素的时候
						for (Map<String, String> map2 : lm) {
							PClaimConsume pmi = new PClaimConsume();
							pmi.setDate(DateUtils.std(map2.get("col1"), 2));//结算日期
							pmi.setAll_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//总额
							pmi.setCash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//现金支付
							pmi.setPersonal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//个人账户
							pmi.setEnt(map2.get("col2"));//消费地点
							pmi.setMed_type(map2.get("col3"));//医疗类别
							pmi.setCur_all_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//本次发生医疗费 总额
							pmi.setCur_cash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//本次个人现金支付金额
							pmi.setCur_personal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//本次个人账户支付金额
							pmi.setOrg_code(map2.get("col11"));//机构编号
							pmi.setHosptial_server_code(map2.get("col12"));//定点医疗服务机构编号
							pmi.setClm_id(map2.get("col13"));//结算单编号 级对应流水号
							//本次统筹金支付金额+本次公务员基金支付金额+本次大额医疗补助支付金额	col5+col7+col8
							pmi.setPay_five(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
							pmi.setPay_seven(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
							pmi.setPay_eight(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
							if(StringUtils.isNotBlank(clm_id)){
								if(clm_id.equals(map2.get("col13"))){
									lists.add(pmi);
									break;
								}
							}else{
								List pay_list = contantCottion(map2);
								pmi.setPay_list(pay_list);
								lists.add(pmi);
							}
						}
					}
				}
				//hsoft.yibao.miaacareinfo.get(医疗保险经办机构现金报销信息查询)
				//res = MiaacareinfoApi.getMiaacareinfo(map);
				res = baseApi.getDataInfo(map, BaseEntity.miaacareinfo);
				if(res!=null && "ok".equals(res.getStatus())){
					List<Map<String, String>> lm = res.getData();
					if((lm!=null && !lm.isEmpty())){//有元素的时候
						for (Map<String, String> map2 : lm) {
							PClaimConsume pmi = new PClaimConsume();
							pmi.setDate(DateUtils.std(map2.get("col1"), 2));//结算日期
							pmi.setAll_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//总额
							pmi.setCash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//现金支付
							pmi.setPersonal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//个人账户
							pmi.setEnt(map2.get("col2"));//消费地点
							pmi.setMed_type(map2.get("col3"));//医疗类别
							pmi.setCur_all_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col4"))?"0.00":map2.get("col4")));//本次发生医疗费 总额
							pmi.setCur_cash_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col9"))?"0.00":map2.get("col9")));//本次个人账户支付金额
							pmi.setCur_personal_pay(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col6"))?"0.00":map2.get("col6")));//本次个人现金支付金额
							pmi.setOrg_code(map2.get("col11"));//机构编号
							pmi.setHosptial_server_code(map2.get("col12"));//定点医疗服务机构编号
							pmi.setClm_id(map2.get("col13"));//结算单编号 级对应流水号
							//本次统筹金支付金额+本次公务员基金支付金额+本次大额医疗补助支付金额	col5+col7+col8
							pmi.setPay_five(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col5"))?"0.00":map2.get("col5")));
							pmi.setPay_seven(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col7"))?"0.00":map2.get("col7")));
							pmi.setPay_eight(ConvertUtils.getDouble(StringUtils.isBlank(map2.get("col8"))?"0.00":map2.get("col8")));
							if(StringUtils.isNotBlank(clm_id)){
								if(clm_id.equals(map2.get("col13"))){
									lists.add(pmi);
									break;
								}
							}else{
								List pay_list = contantCottion(map2);
								pmi.setPay_list(pay_list);
								lists.add(pmi);
							}
						}
					}
				}
			}
			//根据日期倒序 即 最新的一条记录在第一条
			Collections.sort(lists, new Comparator<PClaimConsume>(){
				@Override
				public int compare(PClaimConsume paramT1, PClaimConsume paramT2) {
					return paramT2.getDate().compareTo(paramT1.getDate());
				}
			}); 
			 
			return lists;
			
		} catch (Exception e) {
			logger.error("查询医保消费列表报错！", e);
		}
		return null;
	}

	@RequestMapping(value = "/std", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PClaimDiagStd> queryClmStd(String p_mi_id, String clm_id) {
		try {
			ClmDiagStdCondition cond = new ClmDiagStdCondition();
			cond.setClm_id(clm_id);
			cond.setP_mi_id(p_mi_id);
			return clmService.queryClmDiagStd(cond);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/clmsum", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PClaimChrgSum> queryClmSum(String p_mi_id, String clm_id) {
		try {
			ClmChrgSumCond cond = new ClmChrgSumCond();
			cond.setClm_id(clm_id);
			cond.setP_mi_id(p_mi_id);
			return clmService.queryClmChrgSum(cond);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/clmdtl", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PClaimItem> queryClmSum(String p_mi_id, String clm_id, int item_cat_id, Date chrg_time) {
		try {
			ClmItemCondition cond = new ClmItemCondition();
			cond.setClm_id(clm_id);
			cond.setP_mi_id(p_mi_id);
			cond.setItem_cat_id(String.valueOf(item_cat_id));
			cond.setChrg_time_start(chrg_time);
			cond.setChrg_time_end(chrg_time);
			return clmService.queryClmItemList(cond);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/detail", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody PClaimHead getdetail(String pMiId, String clmId) {
		try {
			return clmService.findPClaimHead(clmId, pMiId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}