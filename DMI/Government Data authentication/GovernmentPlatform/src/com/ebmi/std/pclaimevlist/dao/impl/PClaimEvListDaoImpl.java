package com.ebmi.std.pclaimevlist.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.core.db.jdbc.JdbcService;
import com.ebmi.std.pclaimevlist.dao.PClaimEvListDao;
import com.ebmi.std.pclaimevlist.dto.PClaimEvList;

@Repository
public class PClaimEvListDaoImpl implements PClaimEvListDao {

	@Resource(name = "jdbcService")
	private JdbcService jdbcService;

	@Override
	public List<PClaimEvList> queryPMiClaimEvList(String p_mi_id,
			String ent_id, Integer stat_id, Date crt_time, Integer start,
			Integer count) throws Exception {

		StringBuffer sql = new StringBuffer();
		List<Object> Param = new ArrayList<Object>();
		sql.append("select e.clm_id,e.p_mi_id,e.ent_id,e.ent_name,e.med_type_id,e.med_date,e.ev_list_stat_id");
		sql.append(" from p_claim_ev_list e ");
		sql.append(" where 1=1 ");

		if (StringUtils.isNoneBlank(p_mi_id)) {
			sql.append(" and e.p_mi_id = ? ");
			Param.add(p_mi_id);
		}
		if (StringUtils.isNoneBlank(ent_id)) {
			sql.append(" and e.ent_id = ? ");
			Param.add(ent_id);
		}

		if (crt_time != null) {
			sql.append(" and e.med_date <= ? ");
			Param.add(crt_time);
		}
		if (stat_id != null) {
			sql.append(" and e.ev_list_stat_id = ? ");
			Param.add(stat_id);
		}
		sql.append(" order by e.med_date ");
		int st = 0;
		int c = 0;
		if (start != null) {
			st = Integer.valueOf(start);
		}
		if (count != null) {
			c = Integer.valueOf(count);

		}
		if (c != 0) {
			sql.append(" limit ?,?");
			Param.add(st);
			Param.add(c);
		}
		// Param.add(Constant.EV_LIST_STAT_N);
		return jdbcService.findListByQueryMapper(sql.toString(), Param.toArray(),
				PClaimEvList.class);

	}

	@Override
	public void updatePMiClaimEvListState(List<Object[]> ls) throws Exception {
		String sql = "update p_claim_ev_list set ev_list_stat_id = ?, upd_time = ? where p_mi_id = ? and clm_id = ? ";
		jdbcService.updateBatch(sql, ls);
	}

	@Override
	public String queryPMiClaimEvListCount(String p_mi_id,
			Integer stat_id) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> Param = new ArrayList<Object>();
		sql.append("select count(*)");
		sql.append(" from p_claim_ev_list e ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNoneBlank(p_mi_id)) {
			sql.append(" and e.p_mi_id = ? ");
			Param.add(p_mi_id);
		}
		if (stat_id != null) {
			sql.append(" and e.ev_list_stat_id = ? ");
			Param.add(stat_id);
		}
		// Param.add(Constant.EV_LIST_STAT_N);
		int count = jdbcService.findForInt(sql.toString(), Param.toArray());
		return String.valueOf(count);
		
	}

	
	public void updatePMiClaimEvListState(String p_mi_id, String clm_id, Date date,
			Integer stat_id) throws Exception {
		
		String sql = "update p_claim_ev_list set ev_list_stat_id = ?, upd_time = ? where p_mi_id = ? and clm_id = ? ";
		jdbcService.update(sql, new Object[]{stat_id, date, p_mi_id, clm_id});
	}

}
