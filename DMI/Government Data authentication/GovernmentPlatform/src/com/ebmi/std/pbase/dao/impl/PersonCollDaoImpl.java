package com.ebmi.std.pbase.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.core.utils.ConvertUtils;
import com.core.utils.MathUtils;
import com.ebmi.jms.entity.ViewGrjjkJm;
import com.ebmi.jms.entity.ViewGrjjkYl;
import com.ebmi.std.common.dao.impl.BaseDao;
import com.ebmi.std.interfaceapi.BaseEntity;
import com.ebmi.std.interfaceapi.TheKeyResultEntity;
import com.ebmi.std.interfaceapi.IBaseApi;
import com.ebmi.std.pbase.cond.PCollDetCond;
import com.ebmi.std.pbase.dao.PersonCollDao;
import com.ebmi.std.pbase.dto.PCollDet;

@Service("personCollDaoImpl")
public class PersonCollDaoImpl extends BaseDao implements PersonCollDao {
	@Resource(name = "baseApiImpl")
	private IBaseApi baseApi;

	@Override
	public List<PCollDet> query(PCollDetCond cond) throws Exception {
		List<PCollDet> r = new ArrayList<PCollDet>();

		if (cond != null) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("parm0", cond.getIdcard());// 公民身份证号
			map.put("parm1", cond.getUsername());// 姓名
			map.put("parm2", cond.getSs_num());// 社会保障卡号
			// hsoft.yibao.pbaseinfo.get(个人基本信息查询)
			TheKeyResultEntity res = baseApi.getDataInfo(map, BaseEntity.pbaseinfo);
			String p_si_cat_name = null;
			String self_num = null;
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							self_num = map2.get("col0");// 个人编号
						}
					}
				}
			}

			map.put("parm3", cond.getTable_stat());// parm3 String 必须 状态 1工作表
													// 2历史表
			/// hsoft.yibao.pmediinfo.get(医疗个人参保信息查询) 在职 灵活就业的调用此接口
			res = baseApi.getDataInfo(map, BaseEntity.pmediinfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						for (Map<String, String> map2 : lm) {
							p_si_cat_name = map2.get("col1");// 待遇类别
						}
					}
				}
			}

			map.put("parm3", cond.getStart_date());// 开始属期
			map.put("parm4", cond.getEnd_date());// 终止属期
			map.put("parm5", cond.getQuery_flag());// 查询状态 0全部 1已缴 2欠缴

			PCollDet pmi = null;

			// hsoft.yibao.pbamepayinfo.get(个人基本医疗缴费信息查询)
			res = baseApi.getDataInfo(map, BaseEntity.pbamepayinfo);
			if (res != null) {
				if ("ok".equals(res.getStatus())) {
					List<Map<String, String>> lm = res.getData();
					System.out.println("lm11>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm.size());
					if ((!lm.isEmpty())) {// 有元素的时候
						// 大病缴费金额
						Double big_pay_coll = 0.00d;
						res = baseApi.getDataInfo(map, BaseEntity.pbigmepayinfo);
						if (res != null) {
							if ("ok".equals(res.getStatus())) {
								List<Map<String, String>> lm2 = res.getData();
								System.out.println("lm22>>>>>>>>>>>>>>>>>>>>>>>>>>" + lm2.size());
								if (lm2 != null && !lm2.isEmpty()) {// 有元素的时候
									Map<String, String> map3 = lm2.get(0);
									big_pay_coll = StringUtils.isNotBlank(map3.get("col3"))
											? Double.valueOf(map3.get("col3")) : 0.00d;
								}
							}
						}
						// 大病缴费金额 end
						Double coll_baseinnit = 0.00d;
						Double org_collinnit = 0.00d;
						Double self_collinnit = 0.00d;
						for (Map<String, String> map2 : lm) {
							pmi = new PCollDet();
							// 缴费年度
							pmi.setColl_mon(map2.get("col0"));// 应缴属期
							// 个人编号
							pmi.setSelf_num(self_num);
							// 待遇类别
							pmi.setTreat_type(p_si_cat_name);
							// 缴费状态
							pmi.setColl_stat(map2.get("col6"));
							// 缴费基数
							coll_baseinnit = Double.valueOf(ConvertUtils.getFormatDouble(
									MathUtils.sum(coll_baseinnit, StringUtils.isNotBlank(map2.get("col1"))
											? Double.valueOf(map2.get("col1")) : 0.00d)));
							pmi.setColl_base(coll_baseinnit);
							// 单位缴费
							org_collinnit = Double.valueOf(ConvertUtils.getFormatDouble(
									MathUtils.sum(org_collinnit, StringUtils.isNotBlank(map2.get("col4"))
											? Double.valueOf(map2.get("col4")) : 0.00d)));
							pmi.setOrg_coll(org_collinnit);
							// 个人缴费金额
							self_collinnit = Double.valueOf(ConvertUtils.getFormatDouble(
									MathUtils.sum(self_collinnit, StringUtils.isNotBlank(map2.get("col3"))
											? Double.valueOf(map2.get("col3")) : 0.00d)));
							pmi.setSelf_coll(self_collinnit);
							// 大病缴费金额
							pmi.setBig_pay_coll(big_pay_coll);
						}
						if (pmi != null) {
							if (p_si_cat_name != null && p_si_cat_name.indexOf("灵活就业") != -1) {
								// 灵活就业人员，新增字段：缴费金额=单位缴费+个人缴费+大额医疗缴费
								pmi.setTotal_pay_coll(
										MathUtils.sum(pmi.getOrg_coll(), pmi.getSelf_coll(), pmi.getBig_pay_coll()));
							}
							r.add(pmi);
						}
					}
				}
			}
		}

		// String grbh = cond.getP_mi_id();
		// String ny = cond.getColl_mon() == null ? "%" : cond.getColl_mon() +
		// "%";
		// StringBuffer sql = new StringBuffer();
		// sql.append(
		// "select sjrq arrive_date, jfjs coll_base, jfjs coll_lvl_id, trim(ny)
		// coll_mon, a.xzqhdm coll_regn_id, 1 coll_stat_id, jflx coll_type_id,
		// dwyj org_coll, 0 other_coll, gryj self_coll, 1 si_cat_id,
		// trim(b.dwmc) si_org_name from view_grjjk_yl a left join view_dwjbxx b
		// on a.dwbh = b.dwbh where a.grbh = ? and a.ny like ? union select sjrq
		// arrive_date, jfjs coll_base, jfjs coll_lvl_id, trim(ny) coll_mon,
		// a.xzqhdm coll_regn_id, 1 coll_stat_id, jflx coll_type_id, 0 org_coll,
		// 0 other_coll, jfxj self_coll, 9 si_cat_id, trim(b.dwmc) si_org_name
		// from view_grjjk_jm a left join view_dwjbxx b on a.dwbh = b.dwbh where
		// a.grbh = ? and a.ny like ? union select sjrq arrive_date, jfjs
		// coll_base, jfjs coll_lvl_id, trim(ny) coll_mon, a.xzqhdm
		// coll_regn_id, 1 coll_stat_id, jflx coll_type_id, jfxj org_coll, 0
		// other_coll, 0 self_coll, 4 si_cat_id, trim(b.dwmc) si_org_name from
		// view_grjjk_gs a left join view_dwjbxx b on a.dwbh = b.dwbh where
		// a.grbh = ? and a.ny like ? union select sjrq arrive_date, jfjs
		// coll_base, jfjs coll_lvl_id, trim(ny) coll_mon, a.xzqhdm
		// coll_regn_id, 1 coll_stat_id, jflx coll_type_id, jfxj org_coll, 0
		// other_coll, 0 self_coll, 5 si_cat_id, trim(b.dwmc) si_org_name from
		// view_grjjk_sy a left join view_dwjbxx b on a.dwbh = b.dwbh where
		// a.grbh = ? and a.ny like ? union select sjrq arrive_date, jfjs
		// coll_base, jfjs coll_lvl_id, trim(ny) coll_mon, a.xzqhdm
		// coll_regn_id, 1 coll_stat_id, jflx coll_type_id, jfxj org_coll, 0
		// other_coll, 0 self_coll, 3 si_cat_id, trim(b.dwmc) si_org_name from
		// view_grjjk_ywsh a left join view_dwjbxx b on a.dwbh = b.dwbh where
		// a.grbh = ? and a.ny like ? ");
		//
		// List<PCollDet> r =
		// getJdbcServiceMI().findListByQueryMapper(sql.toString(),
		// new Object[] { grbh, ny, grbh, ny, grbh, ny, grbh, ny, grbh, ny },
		// PCollDet.class);
		// for (PCollDet i : r) {
		// i.setSi_org_name(EncodingUtils.transCP1252ToGBK(i.getSi_org_name()));
		// }
		// Collections.sort(r, new Comparator<PCollDet>() {
		// @Override
		// public int compare(PCollDet o1, PCollDet o2) {
		// return o1.getSi_cat_id().compareTo(o2.getSi_cat_id());
		// }
		// });
		return r;
	}

	@Override
	public List<String> queryPSiCatList(String p_mi_id) throws Exception {
		String sql = "SELECT DISTINCT SI_CAT_ID FROM P_COLL_DET WHERE P_MI_ID=?";
		return getJdbcService().queryForList(sql, new Object[] { p_mi_id }, String.class);
	}

	@Override
	public List<ViewGrjjkYl> queryGrjjkYl(String grbh, Integer year) throws Exception {
		String sql = "select * from view_grjjk_yl t where grbh=? and ny like ? order by ny desc";
		String yearCond = "%";
		if (year != null) {
			yearCond = year + yearCond;
		}
		return getJdbcService().query(sql, new Object[] { grbh, yearCond }, ViewGrjjkYl.class);
	}

	@Override
	public List<ViewGrjjkJm> queryGrjjkJm(String grbh, Integer year) throws Exception {
		String sql = "select * from view_grjjk_jm t where grbh=? and ny like ? order by ny desc";
		String yearCond = "%";
		if (year != null) {
			yearCond = year + yearCond;
		}
		return getJdbcService().query(sql, new Object[] { grbh, yearCond }, ViewGrjjkJm.class);
	}

}
