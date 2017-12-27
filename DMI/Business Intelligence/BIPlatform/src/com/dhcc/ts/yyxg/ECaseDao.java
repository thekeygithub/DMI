package com.dhcc.ts.yyxg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dhcc.ts.database.DBManager_HIS;
import com.dhcc.ts.database.DBManager_HSD;

public class ECaseDao {

	@SuppressWarnings("unchecked")
	public Map<String, Object> getHospitalVisitRecord(Map<String, String> param) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ")
			.append("fh.HOS_NAME, ")
			.append("'000013' as ZZJGDM, ")
			.append("9 AS YLFFFS, ")
			.append("'--' AS YBXFL, ")
			.append("'--' AS JKKH, ")
			.append("'' AS DJH, ")
			.append("'' AS DJCZY, ")
			.append("HV.CBAH, ")
			.append("HV.CXM, ")
			.append("decode(HV.CXB, '男', '1', '女', '2', '') as CXB, ")
			.append("HV.DCSNY, ")
			.append("HV.CBRNL, ")
			.append("HV.CGJ, ")
			.append("'--' AS XXECSTZ, ")
			.append("'--' AS XXERYTZ, ")
			.append("HV.CHKDZP||HV.CHKDZC||HV.CHKDZX AS CSD, ")
			.append("HV.CHKDZP||HV.CHKDZC||HV.CHKDZX AS JG, ")
			.append("HV.CMZ, ")
			.append("HV.CSFZH, ")
			.append("HV.CZY, ")
			.append("decode(HV.CHYZK, '未婚', '1', '已婚', '2', '丧偶', '3', '离婚', '4', '9') as CHYZK, ")
			.append("HV.CHKDZP||HV.CHKDZC||HV.CHKDZX||HV.CHKDZMX AS XZZ, ")
			.append("HV.CLXDH, ")
			.append("HV.CHKDZYB, ")
			.append("HV.CHKDZP||HV.CHKDZC||HV.CHKDZX||HV.CHKDZMX AS HKDZ, ")
			.append("HV.CHKDZYB, ")
			.append("HV.CGZDW, ")
			.append("'' AS DWDH, ")
			.append("'' AS GZDWYB, ")
			.append("HV.CLXR, ")
			.append("HV.CLXRGX, ")
			.append("HV.CLGZDW, ")
			.append("HV.CLXDH, ")
			.append("decode(HV.CRYFS, '急诊', '1', '门诊', '2', '其他医疗机构转入', '3', '9') as CRYFS, ")
			.append("HV.DRYSJ, ")
			.append("HV.CRYKSMC, ")
			.append("HV.CRYBQBM, ")
			.append("'' AS ZKKB, ")
			.append("HV.DCYSJ, ")
			.append("HV.CCYKSMC, ")
			.append("HV.CCYBQMC, ")
			.append("trunc(to_number(to_date(substr(hv.DCYSJ, 0, 19),'yyyy-mm-dd hh24:mi:ss')-to_date(substr(hv.DRYSJ, 0, 19),'yyyy-mm-dd hh24:mi:ss')), 0) as ZYTS, ")
			.append("HV.CMZZDMC, ")
			.append("'' AS JBBM, ")
			.append("HV.CRYZDMC, ")
			.append("'' AS RYBQ, ")
			.append("'' AS WBYY, ")
			.append("'' AS BLZD, ")
			.append("'' AS BLH, ")
			.append("'1' AS YWGM, ")
			.append("HV.CGMYW, ")
			.append("decode(HV.BSJ, '是', '1', '否', '2', '-') as BSJ, ")
			.append("decode(HV.CXX, 'A', '1', 'B', '2', 'O', '3', 'AB', '4', '不详', '5', '6') as CXX, ")
			.append("decode(HV.CRH, '阴', '1', '阳', '2', '不详', '3', '4') as CRH, ")
			.append("HV.CKZR, ")
			.append("HV.CZRYS, ")
			.append("HV.CZYYS, ")
			.append("'' AS ZRHS, ")
			.append("'' AS JXYS, ")
			.append("'' AS SXYS, ")
			.append("HV.CBMY, ")
			.append("'' AS BAZL, ")
			.append("'' AS ZKYS, ")
			.append("'' AS ZKHS, ")
			.append("'' AS ZKRQ, ")
			.append("decode(HV.CCYQX, '医嘱离院', '1', '医嘱转院', '2', '医嘱转社区', '3', '非医嘱', '4', '死亡', '5', '9') as CCYQX, ")
			.append("'' AS NJSYLJGMC, ")
			.append("'' AS JZSS, ")
			.append("'' AS WZBR, ")
			.append("'1' AS SFYCY31TNZZYJH, ")
			.append("'' AS MD, ")
			.append("'' AS QJCS, ")
			.append("'' AS CGCS, ")
			.append("'' LNSSHZHMSJ ")
			.append("from h_visit hv ")
			.append("left join fac_hospital fh on hv.corp_id = fh.hos_id ")
			.append("where 1 = 1 ").append(" and hv.CZYH = '")
			.append(param.get("h_no")).append("'");
		
		Map<String, Object> retMap = null;
		
		try {
			retMap = db_his.executeQueryHashMap(sql.toString());
		} catch (Exception e) {
			throw e;
		} finally {
			db_his.close();
		}
		return retMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getCFHList(Map<String, String> param) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		List<Map<String, String>> result = null;
		
		// 查询处方号
		try {
			result = db_his.executeQueryListHashMap("select distinct CCFH from h_advice where CZYH = '" + param.get("h_no") + "'");
			if (result == null) result = new ArrayList<Map<String, String>>();
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getHospitalVisitFee(String cfhIn) throws SQLException {
		// TODO Auto-generated method stub
		DBManager_HSD db_hsd = new DBManager_HSD();
		Map<String, String> retMap = null;
		
		try {
			// 查询费用明细汇总
			StringBuilder sql = new StringBuilder();
			sql.append("select ")
				.append("max(decode(AKA063, '01', JE, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_1, ")
				.append("max(decode(AKA063, '01', 0, '02', JE, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_2, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', JE, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_3, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', JE, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_4, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', JE, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_5, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', JE, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_6, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', JE, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_7, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', JE, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_8, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', JE, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_9, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', JE, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_10, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', JE, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_11, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', JE, '13', 0, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_12, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', JE, '14', 0, '15', 0, '16', 0, '34', 0, 0)) as JE_13, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', JE, '15', 0, '16', 0, '34', 0, 0)) as JE_14, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', JE, '16', 0, '34', 0, 0)) as JE_15, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', JE, '34', 0, 0)) as JE_16, ")
				.append("max(decode(AKA063, '01', 0, '02', 0, '03', 0, '04', 0, '05', 0, '06', 0, '07', 0, '08', 0, '09', 0, '10', 0, '11', 0, '12', 0, '13', 0, '14', 0, '15', 0, '16', 0, '34', JE, 0)) as JE_17  ")
				.append("from ( ")
				.append(" select AKA063, ")
				.append("   sum(AKC227) AS JE ")
				.append(" from KC22 ")
				.append(" where AKC220 in ").append(cfhIn)
				.append(" group by AKA063 ) a ");
			retMap = db_hsd.executeQueryHashMap(sql.toString());
		} catch (SQLException e) {
			throw e;
		} finally {
			db_hsd.close();
		}
		
		return retMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getHospitalVisitSumFee(String cfhIn) throws SQLException {
		DBManager_HSD db_hsd = new DBManager_HSD();
		Map<String, String> retMap = null;

		StringBuilder sql = new StringBuilder();
		sql.append("select ")
			.append("  sum(AKC227) AS JE, ")
			.append("  sum(AKC253) AS JE_ZF ")
			.append(" from KC22 ")
			.append(" where AKC220 in ").append(cfhIn);
//			.append(" group by AKA063 ");
		
		try {
			retMap = db_hsd.executeQueryHashMap(sql.toString());
		} catch (SQLException e) {
			throw e;
		} finally {
			db_hsd.close();
		}
		
		return retMap;
	}

	
}
