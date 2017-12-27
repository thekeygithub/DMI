package com.ebmi.std.pbase.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ebmi.jms.entity.ViewGrjbxxJm;
import com.ebmi.jms.entity.ViewGrjbxxYl;
import com.ebmi.std.common.dao.impl.BaseDao;
import com.ebmi.std.pbase.cond.PSiStatCond;
import com.ebmi.std.pbase.dao.PersonInsDao;
import com.ebmi.std.pbase.dto.PSiStat;

@Service("personInsDaoImpl")
public class PersonInsDaoImpl extends BaseDao implements PersonInsDao {

	@Override
	public List<PSiStat> query(PSiStatCond cond) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DBID,P_MI_ID,SI_TYPE_ID,COLL_STAT_ID,TOT_COLL_MONTH,")
				.append(" CONTI_COLL_MONTH,SI_ORG_ID,SI_ORG_NAME,REGN_TC_ID,UPD_TIME,")
				.append(" CRT_TIME,ETL_BATCH,ETL_TIME").append(" FROM P_SI_STAT WHERE 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if (cond != null) {
			if (cond.getP_mi_id() != null && !"".equals(cond.getP_mi_id())) {
				sql.append(" and P_MI_ID=?");
				args.add(cond.getP_mi_id());
			}
			if (cond.getColl_stat_id() != null && !"".equals(cond.getColl_stat_id())) {
				sql.append(" and COLL_STAT_ID=?");
				args.add(cond.getColl_stat_id());
			}
			if (cond.getSi_type_id() != null && !"".equals(cond.getSi_type_id())) {
				sql.append(" and SI_TYPE_ID=?");
				args.add(cond.getSi_type_id());
			}
		}
		sql.append(" ORDER BY SI_TYPE_ID ");
		return getJdbcService().findListByQueryMapper(sql.toString(), args.toArray(), PSiStat.class);
	}

	@Override
	public ViewGrjbxxJm queryGrjbxxJm(String grbh) throws Exception {
		return getJdbcServiceMI().queryForObject("select b.* from view_grjbxx a, view_grjbxx_jm b where a.shbzh = b.shbzh and a.grbh=?",
				new Object[] { grbh }, ViewGrjbxxJm.class);
	}

	@Override
	public ViewGrjbxxYl queryGrjbxxYl(String grbh) throws Exception {
		return getJdbcServiceMI().queryForObject("select b.* from view_grjbxx a, view_grjbxx_yl b where a.shbzh = b.shbzh and a.grbh=?",
				new Object[] { grbh }, ViewGrjbxxYl.class);
	}

}
