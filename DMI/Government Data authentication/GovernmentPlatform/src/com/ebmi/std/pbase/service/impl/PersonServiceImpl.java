package com.ebmi.std.pbase.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.core.utils.ConvertUtils;
import com.core.utils.DateUtils;
import com.ebmi.jms.entity.ViewDwjbxx;
import com.ebmi.jms.entity.ViewGrjbxx;
import com.ebmi.jms.entity.ViewGrjbxxGs;
import com.ebmi.jms.entity.ViewGrjbxxJm;
import com.ebmi.jms.entity.ViewGrjbxxSy;
import com.ebmi.jms.entity.ViewGrjbxxYl;
import com.ebmi.jms.entity.ViewGrjjkJm;
import com.ebmi.jms.entity.ViewGrjjkYl;
import com.ebmi.seng.AccHisRecord;
import com.ebmi.seng.BalanceApp;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.pbase.cond.PCollDetCond;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.pbase.cond.PSiEndowDetCond;
import com.ebmi.std.pbase.cond.PSiStatCond;
import com.ebmi.std.pbase.dao.PersonBaseDao;
import com.ebmi.std.pbase.dao.PersonCollDao;
import com.ebmi.std.pbase.dao.PersonInsDao;
import com.ebmi.std.pbase.dao.PersonPensionDao;
import com.ebmi.std.pbase.dto.PCollDet;
import com.ebmi.std.pbase.dto.PMiBaseInfo;
import com.ebmi.std.pbase.dto.PSiEndowDet;
import com.ebmi.std.pbase.dto.PSiStat;
import com.ebmi.std.pbase.service.PersonService;
import com.ebmi.std.util.BaseApiCallable;

@Service("personServiceImpl")
public class PersonServiceImpl implements PersonService {

	@Resource(name = "personBaseDaoImpl")
	private PersonBaseDao personBaseDao;
	@Resource(name = "personCollDaoImpl")
	private PersonCollDao personCollDao;
	@Resource(name = "personInsDaoImpl")
	private PersonInsDao personInsDao;
	@Resource(name = "personPensionDaoImpl")
	private PersonPensionDao personPensionDao;

	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;

	@Override
	public BalanceApp queryPAccBalance(PMiBaseInfoCond cond) throws Exception {
		if (cond != null) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());// 公民身份证号
			map.put("parm1", cond.getUsername());// 姓名
			map.put("parm2", cond.getSs_num());// 社会保障卡号
			// map.put("parm3", cond.getTable_stat());//parm3 String 必须 状态 1工作表
			// 2历史表
			TheKeyResultEntity res = null;

			// 根据 hsoft.yibao.pinsuinfo.get(个人参保信息查询) 接口返回的 col2 职工医疗保险 险种 col9
			// 险种代码 判断是否是居民
			// "col2":"居民医疗保险" "col9":"23" 为居民
//			map.put("parm3", "3");// 指定传入参数3 ，医疗保险； [险种 1养老保险 2失业保险 3医疗保险 4工伤保险// 5生育保险]
//			res = baseApi.getDataInfo(map, BaseEntity.pinsuinfo);// hsoft.common.pinsuinfo.get(个人参保信息查询)
			
			
			
			if(DateUtils.dts(new Date(), 9).equals(cond.getYear())){
				map.put("parm3", "1");// 状态 1工作表 2历史表
				map.put("parm4", DateUtils.dts(new Date(), 9));// 年度 在查询历史表时需要输入年度参数
			} else {
				map.put("parm3", "2");// 状态 1工作表 2历史表
				map.put("parm4", cond.getYear());// 年度 在查询历史表时需要输入年度参数
			}
			BalanceApp pmi = new BalanceApp();
			/// hsoft.yibao.pmetrinfo.get(个人医疗待遇信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.pmetrinfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							pmi.setCur_balance(ConvertUtils.getDouble(map2.get("col2"))); // 当年余额
							pmi.setPast_balance(ConvertUtils.getDouble(map2.get("col0"))); // 去年钱
						}
					}
				}
			}


			return pmi;
		}
		return null;
	}

	/**
	 * 查询账户变更记录
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<AccHisRecord> queryBalanceRecords(PMiBaseInfoCond cond) throws Exception {
		List<AccHisRecord> lists = new ArrayList<AccHisRecord>();
		if (cond != null) {
			TheKeyResultEntity res = null;
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());// 公民身份证号
			map.put("parm1", cond.getUsername());// 姓名
			map.put("parm2", cond.getSs_num());// 社会保障卡号
			map.put("parm3", cond.getStart_date());// 开始日期
			String endstrDate = cond.getEnd_date();
			map.put("parm4", endstrDate);// 终止日期
			// hsoft.yibao.pbameaccininfo.get(个人基本医疗账户缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.pbameaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("pbameaccininfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							DecimalFormat    df   = new DecimalFormat("######0.00");   
							AccHisRecord r = new AccHisRecord();
							if (StringUtils.isNotBlank(map2.get("col0"))) {
								r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							}
							r.setMoney(df.format(Double.parseDouble(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"))));// 变动金额
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);

						}
					}
				}
			}

			////////////////////////// hsoft.yibao.pmeendtranaccininfo.get(个人医疗年终结转账户缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.pmeendtranaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("pmeendtranaccininfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							if (StringUtils.isNoneBlank(map2.get("col0"))) {
								DecimalFormat    df   = new DecimalFormat("######0.00");   
								r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
								r.setMoney(df.format(Double.parseDouble(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"))));// 变动金额
								r.setType("利息");// 类型
								lists.add(r);
							}

						}
					}
				}
			}

			/*****************************************************************
			 *************************时间巨长修改*****************************
			 *****************************************************************/
			// hsoft.yibao.tspmapayinfo.get(医疗两定个人账户消费信息查询)
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date date = sdf.parse(cond.getEnd_date());
				Calendar now = Calendar.getInstance();
				now.setTime(date);
				int year = now.get(Calendar.YEAR);
				int month = now.get(Calendar.MONTH) + 1;
				ExecutorService pool = Executors.newFixedThreadPool(month);
				List<Future> list = new ArrayList<Future>();
				for (int i = 1; i <= month; i++) {
					Callable c = new BaseApiCallable(baseApi, BaseEntity.tspmapayinfo, cond.getIdcard(), cond.getUsername(), cond.getSs_num(), year, i);
					Future f = pool.submit(c);
					list.add(f);
				}
				pool.shutdown();
				for (Future f : list) {
					res = (TheKeyResultEntity) f.get();
					if (res != null) {
						if ("ok".equals(res.getStatus())) {
							List<Map<String, String>> lm = res.getData();
							if (lm != null && (!lm.isEmpty())) {// 有元素的时候
								for (Map<String, String> map2 : lm) {
									AccHisRecord r = new AccHisRecord();
									if (StringUtils.isNoneBlank(map2.get("col0"))) {
										DecimalFormat    df   = new DecimalFormat("######0.00");   
										r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
										r.setMoney(df.format(Double.parseDouble((StringUtils.isBlank(map2.get("col1")) ? "0.00" : map2.get("col1")))));// 变动金额
										r.setType("支出");// 类型
										r.setBalance(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"));// 余额
										r.setPlace(map2.get("col3"));// 交易地点 //消费地点
										r.setCategory(map2.get("col4"));// 类别（支出明细）
										lists.add(r);
									}
								}
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			

			// hsoft.yibao.crpapayinfo.get(现金报销个人账户消费信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.crpapayinfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						System.out.println("crpapayinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							if (StringUtils.isNoneBlank(map2.get("col0"))) {
								DecimalFormat df = new DecimalFormat("######0.00");
								r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
								// r.setMoney("-" +
								// (StringUtils.isBlank(map2.get("col1")) ?
								// "0.00" : map2.get("col1")));// 变动金额
								r.setMoney(df.format(Double.parseDouble(
										StringUtils.isBlank(map2.get("col1")) ? "0.00" : map2.get("col1"))));// 变动金额
								r.setType("支出");// 类型
								r.setBalance(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"));// 余额
								r.setPlace(map2.get("col3"));// 交易地点 //消费地点
								r.setCategory(map2.get("col4"));// 类别（支出明细）
								lists.add(r);
							}

						}
					}
				}
			}
			
			//-------------------------------- liyang
			//  hsoft.yibao.spcapaccininfo.get(特殊人员收缴个人账户缴费收入信息查询) --缴费收入
			res = baseApi.getDataInfo(map, BaseEntity.spcapaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}
			
			// hsoft.yibao.spcapaccseininfo.get(特殊人员收缴个人账户二次缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.spcapaccseininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}
			
			// hsoft.yibao.dfundaccininfo.get(铺底资金账户缴费收入信息查询)----统筹外转入
			res = baseApi.getDataInfo(map, BaseEntity.dfundaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("统筹");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}
			
			// hsoft.yibao.semesupaccininfo.get(公务员医疗补助个人账户缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.semesupaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}

			// 根据日期倒序 即 最新的一条记录在第一条
			Collections.sort(lists, new Comparator<AccHisRecord>() {
				@Override
				public int compare(AccHisRecord paramT1, AccHisRecord paramT2) {
					return paramT2.getDate().compareTo(paramT1.getDate());
				}
			});

			return lists;
		}
		return null;
	}

	@Override
	public PMiBaseInfo queryInsuranceinfo(PMiBaseInfoCond cond) throws Exception {
		return personBaseDao.queryInsuranceinfo(cond);
	}

	@Override
	public PMiBaseInfo queryPersonBase(PMiBaseInfoCond cond) throws Exception {
		return personBaseDao.query(cond);
	}

	@Override
	public List<PSiStat> queryPersonIns(PSiStatCond cond) throws Exception {
		return personInsDao.query(cond);
	}

	@Override
	public List<PCollDet> queryPersonColl(PCollDetCond cond) throws Exception {
		return personCollDao.query(cond);
	}

	@Override
	public List<PSiEndowDet> queryPersonPension(PSiEndowDetCond cond) throws Exception {
		return personPensionDao.query(cond);
	}

	@Override
	public List<String> queryPSiCatList(String p_mi_id) throws Exception {
		return personCollDao.queryPSiCatList(p_mi_id);
	}

	@Override
	public ViewGrjbxx queryGrjbxx(String grbh) throws Exception {
		return personBaseDao.queryGrjbxx(grbh);
	}

	@Override
	public ViewGrjbxxJm queryGrjbxxJm(String grbh) throws Exception {
		return personInsDao.queryGrjbxxJm(grbh);
	}

	@Override
	public ViewGrjbxxYl queryGrjbxxYl(String grbh) throws Exception {
		return personInsDao.queryGrjbxxYl(grbh);
	}

	@Override
	public ViewDwjbxx queryDwjbxx(String dwbh) throws Exception {
		return personBaseDao.queryDwjbxx(dwbh);
	}

	@Override
	public ViewGrjbxxGs queryGrjbxxGs(String grbh) throws Exception {
		return personBaseDao.queryGrjbxxGs(grbh);
	}

	@Override
	public ViewGrjbxxSy queryGrjbxxSy(String grbh) throws Exception {
		return personBaseDao.queryGrjbxxSy(grbh);
	}

	@Override
	public List<ViewGrjjkYl> queryGrjjkYl(String grbh, Integer year) throws Exception {
		return personCollDao.queryGrjjkYl(grbh, year);
	}

	@Override
	public List<ViewGrjjkJm> queryGrjjkJm(String grbh, Integer year) throws Exception {
		return personCollDao.queryGrjjkJm(grbh, year);
	}
	
	/**
	 * @描述：queryBalanceRecords方法备份
	 * @作者：SZ
	 * @时间：2017年12月18日 上午10:52:25
	 * @param cond
	 * @return
	 * @throws Exception
	 */
	public List<AccHisRecord> queryBalanceRecords_bak(PMiBaseInfoCond cond) throws Exception {
		List<AccHisRecord> lists = new ArrayList<AccHisRecord>();
		if (cond != null) {
			TheKeyResultEntity res = null;
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());// 公民身份证号
			map.put("parm1", cond.getUsername());// 姓名
			map.put("parm2", cond.getSs_num());// 社会保障卡号

			// 根据 hsoft.yibao.pinsuinfo.get(个人参保信息查询) 接口返回的 col2 职工医疗保险 险种 col9
			// 险种代码 判断是否是居民
			// "col2":"居民医疗保险" "col9":"23" 为居民
			map.put("parm3", "3");// 指定传入参数3 ，医疗保险； [险种 1养老保险 2失业保险 3医疗保险 4工伤保险
									// 5生育保险]
//			res = baseApi.getDataInfo(map, BaseEntity.pinsuinfo);// hsoft.yibao.pinsuinfo.get(个人参保信息查询)
//			if (res != null) {
//				if ("ok".equals(res.getStatus())) {
//					List<Map<String, String>> lm = res.getData();
//					System.out.println("hsoft.yibao.pinsuinfo.get(个人参保信息查询) lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
//					if ((!lm.isEmpty())) {// 有元素的时候
//						for (Map<String, String> map2 : lm) {
//							String code = map2.get("col9");
//							String codeName = map2.get("col2");
//							System.out.println("code>>>>>" + code + " & codeName==" + codeName);
//						}
//					}
//				}
//			}

			map.put("parm3", cond.getStart_date());// 开始日期
			String endstrDate = cond.getEnd_date();
			map.put("parm4", endstrDate);// 终止日期
			// hsoft.yibao.pbameaccininfo.get(个人基本医疗账户缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.pbameaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("pbameaccininfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							DecimalFormat    df   = new DecimalFormat("######0.00");   
							AccHisRecord r = new AccHisRecord();
							if (StringUtils.isNotBlank(map2.get("col0"))) {
								r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							}
							r.setMoney(df.format(Double.parseDouble(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"))));// 变动金额
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);

						}
					}
				}
			}

			////////////////////////// hsoft.yibao.pmeendtranaccininfo.get(个人医疗年终结转账户缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.pmeendtranaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("pmeendtranaccininfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							if (StringUtils.isNoneBlank(map2.get("col0"))) {
								DecimalFormat    df   = new DecimalFormat("######0.00");   
								r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
								r.setMoney(df.format(Double.parseDouble(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"))));// 变动金额
								r.setType("利息");// 类型
								lists.add(r);
							}

						}
					}
				}
			}

			/*****************************************************************
			 ****************************时间巨长*****************************
			 *****************************************************************/
			// hsoft.yibao.tspmapayinfo.get(医疗两定个人账户消费信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.tspmapayinfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						System.out.println("tspmapayinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							if (StringUtils.isNoneBlank(map2.get("col0"))) {
								DecimalFormat    df   = new DecimalFormat("######0.00");   
								r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
								// r.setMoney("-" +
								// (StringUtils.isBlank(map2.get("col1")) ?
								// "0.00" : map2.get("col1")));// 变动金额
								r.setMoney(df.format(Double.parseDouble((StringUtils.isBlank(map2.get("col1")) ? "0.00" : map2.get("col1")))));// 变动金额
								r.setType("支出");// 类型
								r.setBalance(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"));// 余额
								r.setPlace(map2.get("col3"));// 交易地点 //消费地点
								r.setCategory(map2.get("col4"));// 类别（支出明细）
								lists.add(r);
							}

						}
					}
				}
			}

			// hsoft.yibao.crpapayinfo.get(现金报销个人账户消费信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.crpapayinfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						System.out.println("crpapayinfo lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							if (StringUtils.isNoneBlank(map2.get("col0"))) {
								DecimalFormat df = new DecimalFormat("######0.00");
								r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
								// r.setMoney("-" +
								// (StringUtils.isBlank(map2.get("col1")) ?
								// "0.00" : map2.get("col1")));// 变动金额
								r.setMoney(df.format(Double.parseDouble(
										StringUtils.isBlank(map2.get("col1")) ? "0.00" : map2.get("col1"))));// 变动金额
								r.setType("支出");// 类型
								r.setBalance(StringUtils.isBlank(map2.get("col2")) ? "0.00" : map2.get("col2"));// 余额
								r.setPlace(map2.get("col3"));// 交易地点 //消费地点
								r.setCategory(map2.get("col4"));// 类别（支出明细）
								lists.add(r);
							}

						}
					}
				}
			}
			
			//-------------------------------- liyang
			//  hsoft.yibao.spcapaccininfo.get(特殊人员收缴个人账户缴费收入信息查询) --缴费收入
			res = baseApi.getDataInfo(map, BaseEntity.spcapaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}
			
			// hsoft.yibao.spcapaccseininfo.get(特殊人员收缴个人账户二次缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.spcapaccseininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}
			
			// hsoft.yibao.dfundaccininfo.get(铺底资金账户缴费收入信息查询)----统筹外转入
			res = baseApi.getDataInfo(map, BaseEntity.dfundaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("统筹");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}
			
			// hsoft.yibao.semesupaccininfo.get(公务员医疗补助个人账户缴费收入信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.semesupaccininfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					if (lm != null && (!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							AccHisRecord r = new AccHisRecord();
							r.setDate(DateUtils.std(map2.get("col0"), 2));// 日期
							r.setMoney(map2.get("col2")) ;
							r.setType("缴费收入");// 类型
							r.setPaymentDate(map2.get("col3"));
							lists.add(r);
						}
					}
				}
			}

			// 根据日期倒序 即 最新的一条记录在第一条
			Collections.sort(lists, new Comparator<AccHisRecord>() {
				@Override
				public int compare(AccHisRecord paramT1, AccHisRecord paramT2) {
					return paramT2.getDate().compareTo(paramT1.getDate());
				}
			});

			return lists;
		}
		return null;
	}
}