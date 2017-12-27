package com.ebmi.std.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.core.db.dao.BaseDAO;
import com.ebmi.std.common.cond.DimClmPayColCond;
import com.ebmi.std.common.entity.DimClmPayCol;
import com.ebmi.std.common.service.DimClmPayColDao;

@Service("dimClmPayColDaoImpl")
public class DimClmPayColDaoImpl extends BaseDAO implements DimClmPayColDao {

	@Override
	public List<DimClmPayCol> queryClmPayCol(DimClmPayColCond condition)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select FEE_COL_NAME,PA_COL_NAME,DISP_NAME,ORIG_COL_NAME,GP_CODE,ORD_NUM,SUM_COL_NAME,VALID_FLAG,REMARK")
			.append(" from DIM_CLM_PAY_COL where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if(condition!=null){
			if(!StringUtils.isEmpty(condition.getFee_col_name())){
				sql.append(" and FEE_COL_NAME=?");
				args.add(condition.getFee_col_name());
			}
			if(!StringUtils.isEmpty(condition.getGp_code())){
				sql.append(" and GP_CODE=?");
				args.add(condition.getGp_code());
			}
			if(!StringUtils.isEmpty(condition.getValid_flag())){
				sql.append(" and VALID_FLAG=?");
				args.add(condition.getValid_flag());
			}
		}
		return getJdbcService().findListByQueryMapper(sql.toString(), args.toArray(), DimClmPayCol.class);
	}

}
