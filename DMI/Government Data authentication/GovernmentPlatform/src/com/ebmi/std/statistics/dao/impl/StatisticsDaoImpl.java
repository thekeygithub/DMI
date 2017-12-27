/**   
 * . 
 * All rights reserved.
 */
package com.ebmi.std.statistics.dao.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.core.db.dao.BaseDAO;
import com.ebmi.std.statistics.dao.StatisticsDao;
import com.ebmi.std.statistics.dto.PYearMedical;

/**
 * @author xiangfeng.guan
 *
 */
@Repository
public class StatisticsDaoImpl extends BaseDAO implements StatisticsDao {

	@Override
	public PYearMedical getPYearMedical(String p_mi_id, String year) throws Exception {
		PYearMedical r;
		Date start ;
		Date end  ;
		if (StringUtils.isBlank(year)) {
			r = getJdbcService().queryForObject(
					"SELECT * FROM P_YEAR_MEDICAL WHERE P_MI_ID=? ORDER BY YEAR DESC LIMIT 1", new Object[] { p_mi_id },
					PYearMedical.class);
			start = getYear(0) ;
			end = getYear(1) ;			
		} else {
			r = getJdbcService().queryForObject("SELECT * FROM P_YEAR_MEDICAL WHERE P_MI_ID=? AND YEAR=?",
					new Object[] { p_mi_id, year }, PYearMedical.class);
			start = getYear(Integer.parseInt(year)) ;
			end = getYear(Integer.parseInt(year) + 1) ;
		}
		
		if(r != null){
			StringBuffer sb = new StringBuffer() ;
			sb.append("SELECT SUM(PAY_FA_A) PAY_FA_A, SUM(PAY_FA_B) PAY_FA_B, SUM(PAY_FA_C) PAY_FA_C, SUM(PAY_FB_A) PAY_FB_A, SUM(PAY_FB_B) PAY_FB_B ") ;
		//	sb.append("SUM(INP_FEE) INP_FEE, SUM(OUP_FEE) OUP_FEE, SUM(SHOP_FEE) SHOP_FEE ") ;
			sb.append("FROM p_claim_head ") ;
			sb.append("WHERE P_MI_ID = ? AND CLM_DATE > ? AND CLM_DATE < ?") ;
			PYearMedical p = getJdbcService().queryForObject(sb.toString(), new Object[] { p_mi_id, start, end }, PYearMedical.class) ;
			if(p != null){
				r.setPay_tot(p.getPay_tot()) ;
				r.setPay_fa_a(p.getPay_fa_a());
				r.setPay_fa_b(p.getPay_fa_b());
				r.setPay_fa_c(p.getPay_fa_c());
				r.setPay_fb_a(p.getPay_fb_a());
				r.setPay_fb_b(p.getPay_fb_b());
			}			
		}
		
		return r;
	}
	
	private Date getYear(int year){
		Calendar c = Calendar.getInstance() ;
		if(year == 0){
			year = c.get(Calendar.YEAR) ;
		}
		if(year == 1){
			year = c.get(Calendar.YEAR) + 1 ;
		}
		c.clear();
		c.set(Calendar.YEAR, year); ;
		return c.getTime() ;
	}

}
