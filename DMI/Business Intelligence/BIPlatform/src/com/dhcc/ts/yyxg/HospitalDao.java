package com.dhcc.ts.yyxg;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.dhcc.ts.database.DBManager_HIS;

public class HospitalDao {

//	门诊号
//	病案号  CBAH
//	出院确定诊断aa 
//	医院名称aa
//	主治医师aa "CZYYS": "住院医生"
//	出院科室aa CCYKSMC CCYKSMC 
//	入院时科别aa       CRYKSMC  
//	住院医师aa CZYYS
//	入院日期aa DRYSJ
//	出院日期aa DCYSJ
//	责任医生CZZYS
//	门诊收治诊断aa CMZZDMC 
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOutHospitalInfo(Map<String, String> param) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		StringBuilder sql = new StringBuilder();
		String h_no = param.get("h_no");
		sql.append("select ").append("'" + h_no + "' as CBRH, ").append("hv.CZYH, ").append("hv.CXM, ")
			.append("hv.CXB, ").append("hv.CBRNL, ").append("hv.CCYKSMC, ").append("hv.CZYYS, ")
			.append("hv.CRYKSMC, ").append("hv.DRYSJ, ").append("hv.DCYSJ, ").append("hv.CMZZDMC, ")
			.append("fh.HOS_NAME, ").append("hv.CCYQX, ").append("hv.CRYZDMC, ").append("hv.CMZZDMC, ")
			.append("hv.CZZYS, ").append("hv.CZYYS ").append("from H_VISIT hv ")
			.append("left join fac_hospital fh on hv.corp_id = fh.hos_id ")
			.append("where 1 = 1 and hv.CZYH = '").append(h_no).append("'");

		Map<String, Object> result;
		try {
			result = db_his.executeQueryHashMap(sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			db_his.close();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOutHospitalRecord(Map<String, String> param) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		Map<String, Object> result = null;
		
		String sql = "select * from H_OUT_HOSPITAL where CBRH = '" + param.get("h_no") + "'";
		try {
			result = db_his.executeQueryHashMap(sql);
			
			if(result.get("XMLNR") == null) {
				sql = "select * from H_OUT_HOSPITAL where CBRH = '53069921'";
				result = db_his.executeQueryHashMap(sql);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		
		return result;
	}

	/**
	 * demo_url: http://localhost:8080/MDEPlatform/yyxg/getDiagsSTD.action?cfh=1610008171
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCfzdSTD(Map<String, String> param) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		String sql = "select * from H_DIAG_STD where ccfh = '" + param.get("cfh") + "'";
		List<Map<String, Object>> result = null;
		try {
			result = db_his.executeQueryListHashMap(sql);
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		return result;
	}
}
