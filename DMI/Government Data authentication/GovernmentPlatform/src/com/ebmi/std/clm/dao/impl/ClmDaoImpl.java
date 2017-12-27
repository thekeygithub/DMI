package com.ebmi.std.clm.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.ebmi.std.clm.cond.ClmChrgSumCond;
import com.ebmi.std.clm.cond.ClmDiagStdCondition;
import com.ebmi.std.clm.cond.ClmInfoCondition;
import com.ebmi.std.clm.cond.ClmItemCondition;
import com.ebmi.std.clm.dao.ClmDao;
import com.ebmi.std.clm.dto.PClaimChrgSum;
import com.ebmi.std.clm.dto.PClaimDiagStd;
import com.ebmi.std.clm.dto.PClaimHead;
import com.ebmi.std.clm.dto.PClaimItem;
import com.ebmi.std.common.dao.impl.BaseDao;

@Service("clmDaoImpl")
public class ClmDaoImpl extends BaseDao implements ClmDao {

	@Override
	public List<PClaimHead> queryClmInfoList(ClmInfoCondition condition) throws Exception {
		String table_name = "P_CLAIM_HEAD";
		StringBuffer sql = new StringBuffer();
		sql.append("select CLM_ID,ORIG_CLM_CODE,MIC_ID,P_MI_ID,ENT_ID,ENT_TYPE_ID,ENT_MI_CODE,ORIG_ENT_NAME,")
				.append("MED_TYPE_ID,ORIG_MED_TYPE_ID,P_MI_CAT_ID,P_MI_TYPE_ID,CLM_TYPE_ID,CLM_DATE,MED_DATE,ENTR_DATE,")
				.append("DISCH_DATE,ORIG_DR_NAME,HOSP_DEPT,OUP_DIAG,SD_DESC,SD_DIAG,INP_IN_DIAG,INP_OUT_DIAG,PAY_TOT,")
				.append("P_ACCT_LEFT,PAY_P_ACCT,PAY_P_CUR_ACCT,PAY_P_HIS_ACCT,PAY_C_TOT,PAY_C_OUT_OF_POCK,PAY_C_COPAY,")
				.append("PAY_MI_SCOPE,DEDUC,REIMB_RATIO,PAY_TC_PARA,PAY_F_TOT,PAY_FA_TOT,PAY_FA_A,PAY_FA_B,PAY_FA_C,PAY_FB_TOT,")
				.append("PAY_FB_A,PAY_FB_B,PAY_FB_C,PAY_FB_D,PAY_FB_E,PAY_FC_TOT,PAY_FC_A,PAY_FC_B,PAY_FC_C,PAY_FC_D,")
				.append("PAY_FC_E,PAY_BK1,PAY_BK2,PAY_BK3,ETL_BATCH,ETL_TIME,CRT_TIME,UPD_TIME,DRUG_FEE,EXAM_FEE,CSUM_FEE")
				.append(" from ").append(table_name).append(" where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if (condition != null) {
			if (!StringUtils.isEmpty(condition.getClm_id())) {
				sql.append(" and clm_id=?");
				args.add(condition.getClm_id());
			}
			if (!StringUtils.isEmpty(condition.getOrig_clm_code())) {
				sql.append(" and orig_clm_code=?");
				args.add(condition.getOrig_clm_code());
			}
			if (!StringUtils.isEmpty(condition.getP_mi_id())) {
				sql.append(" and P_mi_id=?");
				args.add(condition.getP_mi_id());
			}
			if(!StringUtils.isEmpty(condition.getMedType())){
				sql.append(" and MED_TYPE_ID=? ");
				args.add(condition.getMedType());
			}
			if (!StringUtils.isEmpty(condition.getEtl_batch())) {
				sql.append(" and etl_batch=?");
				args.add(condition.getEtl_batch());
			}
			if (condition.getClm_date_start() != null) {
				sql.append(" and clm_date>=?");
				args.add(condition.getClm_date_start());
			}
			if (condition.getClm_date_end() != null) {
				sql.append(" and clm_date<?");
				args.add(condition.getClm_date_end());
			}
			if (StringUtils.isNotBlank(condition.getOrder_by())) {
				sql.append(" order by ").append(condition.getOrder_by());
			}
			if (condition.getRecordCount() != null) {
				sql.append(" limit ");
				if (condition.getRecordStart() != null) {
					sql.append("?,");
					args.add(condition.getRecordStart());
				}
				sql.append("?");
				args.add(condition.getRecordCount());
			}
		}
		return getJdbcService().query(sql.toString(), args.toArray(), PClaimHead.class);
	}

	@Override
	public List<PClaimItem> queryClmItemList(ClmItemCondition condition) throws Exception {
		String table_name = "P_CLAIM_ITEM";
		StringBuffer sql = new StringBuffer();
		sql.append("select ITEM_ID,CLM_ID,ORIG_ITEM_ID,ORIG_CLM_CODE,CLM_DATE,P_MI_ID,ITEM_CAT_ID,CHRG_TYPE_ID,")
				.append("COPAY_TYPE_ID,ORIG_ORD_CODE,ORIG_PRE_CODE,ORD_CRT_TIME,ORD_EXE_TIME,CHRG_TIME,CHRG_SEQ,")
				.append("MI_ITEM_CODE,MI_ITEM_NAME,MI_FORM_NAME,HOSP_ITEM_CODE,HOSP_ITEM_NAME,HOSP_FORM_NAME,")
				.append("MI_SPEC_DESC,HOSP_SPEC_DESC,PRE_DRUG_FLAG,DOSE_CNT,SALE_UNIT,CNT,UNIT_PRICE,FEE,FUND_MAX,")
				.append("OVERRUN_SELF,SELF_RATIO,OUT_OF_POCK,COPAY,MI_SCOPE,UPD_TIME,CRT_TIME,ETL_BATCH,ETL_TIME")
				.append(" from ").append(table_name).append(" where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if (condition != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (!StringUtils.isEmpty(condition.getClm_id())) {
				sql.append(" and clm_id=?");
				args.add(condition.getClm_id());
			}
			if (!StringUtils.isEmpty(condition.getOrig_clm_code())) {
				sql.append(" and orig_clm_code=?");
				args.add(condition.getOrig_clm_code());
			}
			if (!StringUtils.isEmpty(condition.getP_mi_id())) {
				sql.append(" and P_mi_id=?");
				args.add(condition.getP_mi_id());
			}
			if (!StringUtils.isEmpty(condition.getItem_cat_id())
					&&NumberUtils.isNumber(condition.getItem_cat_id())) {
				sql.append(" and ITEM_CAT_ID=?");
				args.add(condition.getItem_cat_id());
			}
			if (condition.getChrg_time_start() != null) {
				sql.append(" AND CHRG_TIME >=TIMESTAMP('").append(df.format(condition.getChrg_time_start())).append("')");
			}
			if (condition.getChrg_time_end() != null) {
				sql.append(" AND CHRG_TIME <TIMESTAMP('")
						.append(df.format(DateUtils.addDays(condition.getChrg_time_end(), 1))).append("')");
			}
		}
		return getJdbcService().findListByQueryMapper(sql.toString(), args.toArray(), PClaimItem.class);
	}

	@Override
	public List<PClaimDiagStd> queryClmDiagStd(ClmDiagStdCondition cond) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT * FROM P_CLAIM_DIAG_STD WHERE 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if (cond != null) {
			if (!StringUtils.isEmpty(cond.getP_mi_id())) {
				sql.append(" AND P_MI_ID=?");
				args.add(cond.getP_mi_id());
			}
			if (!StringUtils.isEmpty(cond.getClm_id())) {
				sql.append(" AND CLM_ID=?");
				args.add(cond.getClm_id());
			}
		}
		sql.append(" ORDER BY DIAG_ID");
		return getJdbcService().findListByQueryMapper(sql.toString(), args.toArray(), PClaimDiagStd.class);
	}

	@Override
	public List<PClaimChrgSum> queryClmChrgSum(ClmChrgSumCond cond) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select CLM_ID,CHRG_TYPE_ID,P_MI_ID,FEE,OUT_OF_POCK,COPAY,UPD_TIME,CRT_TIME,ORIG_DBID,ETL_BATCH,ETL_TIME")
				.append(" from P_CLM_PAY_COL where 1=1 ");
		List<Object> args = new ArrayList<Object>();
		if (cond != null) {
			if (!StringUtils.isEmpty(cond.getP_mi_id())) {
				sql.append(" and P_MI_ID=?");
				args.add(cond.getP_mi_id());
			}
			if (!StringUtils.isEmpty(cond.getClm_id())) {
				sql.append(" and CLM_ID=?");
				args.add(cond.getClm_id());
			}
		}
		return getJdbcService().findListByQueryMapper(sql.toString(), args.toArray(), PClaimChrgSum.class);
	}

	@Override
	public PClaimHead findPClaimHead(String clm_id, String p_mi_id) throws Exception {
		String sql = "select * from p_claim_head h where h.clm_id = ? and h.p_mi_id = ?";
		return getJdbcService().queryForObject(sql, new Object[] { clm_id, p_mi_id }, PClaimHead.class);

	}

	@Override
	public List<PClaimHead> queryClmInfoList(String[] clm_id, String p_mi_id) throws Exception {
		String sql = "select * from p_claim_head h where h.p_mi_id = ? and h.clm_id in (?)";
		if (clm_id != null && clm_id.length > 0) {
			StringBuffer strbuff = new StringBuffer();
			for (int i = 0; i < clm_id.length; i++) {
				strbuff.append(clm_id[i]);
				strbuff.append(",");
			}
			return getJdbcService().findListByQueryMapper(sql, new Object[] { p_mi_id, strbuff.toString() },
					PClaimHead.class);
		}
		return null;
	}

}
