package com.ebmi.std.pbase.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.core.db.utils.QueryStringUtil;
import com.ebmi.std.common.dao.impl.BaseDao;
import com.ebmi.std.pbase.cond.PSiEndowDetCond;
import com.ebmi.std.pbase.dao.PersonPensionDao;
import com.ebmi.std.pbase.dto.PSiEndowDet;

@Service("personPensionDaoImpl")
public class PersonPensionDaoImpl extends BaseDao implements PersonPensionDao {

	@Override
	public List<PSiEndowDet> query(PSiEndowDetCond cond)
			throws Exception {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select P_MI_ID,DATA_DATE,ENDOW_MONEY_TYPE_ID,MONEY_RECV,BEGIN_DATE,UPD_TIME,CRT_TIME,ETL_BATCH,ETL_TIME")
			.append(" from P_SI_ENDOW_DET where 1=1 ");
		if(cond!=null){
			if( cond.getData_date()!=null && !"".equals(cond.getData_date().trim())){
				sql.append(" and DATA_DATE like '").append(QueryStringUtil.queryStringLike1(cond.getData_date())).append("%'");
			}
			if( cond.getP_mi_id()!=null && !"".equals(cond.getP_mi_id().trim())){
				sql.append(" and P_MI_ID=?");
				args.add(cond.getP_mi_id());
			}
			if( cond.getSi_cat_id()!=null && !"".equals(cond.getSi_cat_id().trim())){
				sql.append(" and SI_CAT_ID = ?");
				args.add(cond.getSi_cat_id());
			}
		}
		sql.append(" order by DATA_DATE desc,ENDOW_MONEY_TYPE_ID ");
		return getJdbcService().findListByQueryMapper(sql.toString(), args.toArray(),PSiEndowDet.class);
	
	}

}
