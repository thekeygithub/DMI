package com.ebmi.std.pbase.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.ebmi.std.common.controller.BaseController;
import com.ebmi.std.pbase.cond.PCollDetCond;
import com.ebmi.std.pbase.cond.PMiBaseInfoCond;
import com.ebmi.std.pbase.cond.PSiEndowDetCond;
import com.ebmi.std.pbase.cond.PSiStatCond;
import com.ebmi.std.pbase.dto.PCollDet;
import com.ebmi.std.pbase.dto.PMiBaseInfo;
import com.ebmi.std.pbase.dto.PSiEndowDet;
import com.ebmi.std.pbase.dto.PSiStat;
import com.ebmi.std.pbase.service.PersonService;

@Controller
@RequestMapping("/pbase")
public class PersonBaseController extends BaseController {

	@Resource(name = "personServiceImpl")
	private PersonService personService;
	
	
	
	/**
	 * 查询参保人账户余额
	 * xuhai add 
	 * @param p_mi_id 参保人id
	 * @param year 年度
	 * @return 参保人账户余额
	 * @throws Exception
	 */
	@RequestMapping(value = "/paccbalance.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody BalanceApp queryPAccBalance(String p_mi_id, String year
			,String idcard,String username,String ss_num ,String table_stat
			,String start_date,String end_date) throws Exception {
		PMiBaseInfoCond cond = new PMiBaseInfoCond();
		cond.setP_mi_id(p_mi_id);
		cond.setIdcard(idcard);
		cond.setUsername(username);
		cond.setSs_num(ss_num);
		cond.setTable_stat(table_stat);
		cond.setStart_date(start_date);
		cond.setEnd_date(end_date);
		cond.setYear(year);
		return personService.queryPAccBalance(cond);
	}

	
	/**
	 * 查询参保人账户余额
	 * xuhai add 
	 * @param p_mi_id 参保人id
	 * @param year 年度
	 * @return 参保人账户余额
	 * @throws Exception
	 */
	@RequestMapping(value = "/balanceRecords.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<AccHisRecord> queryBalanceRecords(String p_mi_id, String year
			,String idcard,String username,String ss_num ,String table_stat
			,String start_date,String end_date) throws Exception {
		PMiBaseInfoCond cond = new PMiBaseInfoCond();
		cond.setP_mi_id(p_mi_id);
		cond.setIdcard(idcard);
		cond.setUsername(username);
		cond.setSs_num(ss_num);
		cond.setTable_stat(table_stat);
		cond.setStart_date(start_date);
		cond.setEnd_date(end_date);
		List<AccHisRecord> hList = personService.queryBalanceRecords(cond);
		return hList;
	}
	/**
	 * 查询参保人个人信息
	 * 
	 * @param p_mi_id 参保人id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pbaseinfo.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody PMiBaseInfo queryPBase(String p_mi_id,String idcard,String username,String ss_num) throws Exception {
		PMiBaseInfoCond cond = new PMiBaseInfoCond();
		cond.setP_mi_id(p_mi_id);
		cond.setIdcard(idcard);
		cond.setUsername(username);
		cond.setSs_num(ss_num);
		PMiBaseInfo pinfo = personService.queryPersonBase(cond);

//		if (pinfo != null) {
//			// 临时对应一下：Key：佳木斯类别，Value：显示类别：1城镇职工、2城镇居民、3灵活就业
//			Map<Integer, Integer> m = new HashMap<Integer, Integer>();
//			m.put(1, 2);// 学生儿童
//			m.put(2, 1);// 二等乙
//			m.put(3, 1);// 退休
//			m.put(4, 1);// 离休
//			m.put(5, 1);// 在职
//			m.put(6, 1);// 老工人及老兵
//			m.put(7, 1);// 林业离休
//			m.put(8, 1);// 老工人及老兵
//			m.put(9, 1);// 郊区离休
//			m.put(11, 1);// 异地在职
//			m.put(12, 1);// 异地退休
//			m.put(13, 1);// 异地二等乙
//			m.put(14, 1);// 异地离休
//			m.put(15, 2);// 大学生
//			m.put(18, 2);// 异地居民
//			m.put(19, 2);// 一般居民
//
//			Integer catId = pinfo.getP_si_cat_id();
//			int c = m.get(catId);
//			if (c == 1) {
//				ViewGrjbxxYl yl = queryGrjbxxYl(p_mi_id);
//				if (yl != null) {
//					// 灵活统帐/灵活单建
//					if ("3".equals(yl.getJflx()) || "4".equals(yl.getJflx())) {
//						c = 3;
//					}
//				}
//			}
//			pinfo.setP_si_cat_id(c);
//			pinfo.setEmp_city_flag(c);
//		}
		return pinfo;
	}

	@RequestMapping(value = "/insuranceinfo.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody PMiBaseInfo queryInsuranceinfo(String p_mi_id,String idcard,String username,
			String ss_num,String table_stat,String si_type) throws Exception {
		PMiBaseInfoCond cond = new PMiBaseInfoCond();
		cond.setP_mi_id(p_mi_id);
		cond.setIdcard(idcard);
		cond.setUsername(username);
		cond.setSs_num(ss_num);
		cond.setTable_stat(table_stat);
		cond.setSi_type(si_type);
		PMiBaseInfo pinfo = personService.queryInsuranceinfo(cond);

		return pinfo;
	}
	
	
	/**
	 * 查询参保人社保缴费情况
	 * 
	 * @param p_mi_id 参保人id 如果为空则忽略该条件
	 * @param month 参保年月 采用like匹配 ：如果2015查询的是2015整年的数据；如果是2015-01则是1月份的数据；
	 *            如果为空则忽略该条件。
	 * @param si_type_id 社保险种类型 如果为空则忽略该条件
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pcoll.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PCollDet> queryPColl(String p_mi_id, String month
			, String si_type_id
			,String idcard,String username,String ss_num
			,String start_date,String end_date,String query_flag,String table_stat) throws Exception {
		PCollDetCond cond = new PCollDetCond();
		cond.setP_mi_id(p_mi_id);
		cond.setColl_mon(month);
		cond.setSi_type_id(si_type_id);
		//xuhai add 2016-5-17 17:22:38
		cond.setIdcard(idcard);
		cond.setUsername(username);
		cond.setSs_num(ss_num);
		cond.setStart_date(start_date);
		cond.setEnd_date(end_date);
		cond.setQuery_flag(query_flag);
		cond.setTable_stat(table_stat);
		//xuhai add 2016-5-17 17:22:38 end 
		
		return personService.queryPersonColl(cond);
	}

	/**
	 * 参保人社保状态
	 * 
	 * @param p_mi_id 参保人id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pins.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PSiStat> queryPIns(String p_mi_id) throws Exception {
		PSiStatCond cond = new PSiStatCond();
		cond.setP_mi_id(p_mi_id);
		return personService.queryPersonIns(cond);
	}

	/**
	 * 查询参保人养老金领取情况
	 * 
	 * @param p_mi_id 参保人id
	 * @param year_month 查询时间：采用like匹配
	 *            如果为空则查询全部数据，如果2015查询的是2015整年的数据，如果是2015-01则是1月份的数据
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ppension.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<PSiEndowDet> queryPPension(String p_mi_id, String year_month) throws Exception {
		PSiEndowDetCond cond = new PSiEndowDetCond();
		cond.setP_mi_id(p_mi_id);
		cond.setData_date(year_month);
		return personService.queryPersonPension(cond);
	}

	/**
	 * 查询参保人参保险种列表
	 * 
	 * @param p_mi_id 参保人id
	 * @return 参保人参保险种列表
	 * @throws Exception
	 */
	@RequestMapping(value = "/psicatlist.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<String> queryPSiCatList(String p_mi_id) throws Exception {
		return personService.queryPSiCatList(p_mi_id);
	}

	/**
	 * 查询个人基本信息
	 * 
	 * @param grbh 个人编号
	 * @return grjbxx
	 * @throws Exception
	 */
	@RequestMapping(value = "/grjbxx", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ViewGrjbxx queryGrjbxx(String grbh) throws Exception {
		ViewGrjbxx grjbxx = personService.queryGrjbxx(grbh);
		return grjbxx;
	}

	/**
	 * 获取职工医疗参保信息
	 * 
	 * @param grbh 个人编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grjbxxyl", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ViewGrjbxxYl queryGrjbxxYl(String grbh) throws Exception {
		ViewGrjbxxYl grjbxx = personService.queryGrjbxxYl(grbh);
		return grjbxx;
	}

	/**
	 * 获取居民医疗参保信息
	 * 
	 * @param grbh 个人编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grjbxxjm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ViewGrjbxxJm queryGrjbxxJm(String grbh) throws Exception {
		ViewGrjbxxJm grjbxx = personService.queryGrjbxxJm(grbh);
		return grjbxx;
	}

	/**
	 * 获取工伤参保信息
	 *
	 * @param grbh 个人编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grjbxxgs", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ViewGrjbxxGs queryGrjbxxGs(String grbh) throws Exception {
		ViewGrjbxxGs gs = personService.queryGrjbxxGs(grbh);
		return gs;
	}

	/**
	 * 获取生育参保信息
	 *
	 * @param grbh 个人编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grjbxxsy", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ViewGrjbxxSy queryGrjbxxSy(String grbh) throws Exception {
		ViewGrjbxxSy sy = personService.queryGrjbxxSy(grbh);
		return sy;
	}

	/**
	 * 获取参保单位信息
	 * 
	 * @param dwbh 单位编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dwjbxx", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ViewDwjbxx queryDwjbxx(String dwbh) throws Exception {
		return personService.queryDwjbxx(dwbh);
	}

	/**
	 * 获取职工医疗保险个人基金库信息
	 * 
	 * @param dwbh 单位编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grjjkyl", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<ViewGrjjkYl> queryGrjjkYl(String grbh, Integer year) throws Exception {
		return personService.queryGrjjkYl(grbh, year);
	}

	/**
	 * 获取居民医疗保险个人基金库信息
	 * 
	 * @param dwbh 单位编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grjjkjm", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody List<ViewGrjjkJm> queryGrjjkJm(String grbh, Integer year) throws Exception {
		return personService.queryGrjjkJm(grbh, year);
	}

}
