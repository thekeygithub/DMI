package com.dhcc.ts.yyxg;

import java.sql.SQLException;
import java.util.Map;

import com.dhcc.ts.database.DBManager_HSD;

public class PersonInfoDao {

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPersonInfo(Map<String, String> param) throws SQLException {
		DBManager_HSD db_hsd = new DBManager_HSD();
		StringBuilder sql = new StringBuilder();
		sql.append("select b.AAB004,a.AAC001,decode(a.AAC003, null, '', 'null', '', a.AAC003) aac003, a.AAC006,a.AAC004,decode(a.AAC005, null, '', 'null', '', a.AAC005) aac005, a.AKC020,decode(a.AAE005, null, '', 'null', '', a.AAE005) AAE005,a.AAC006,a.AIC162,a.AAC026,a.AAE006,a.AAE007 ")
			.append("from AC01 a left join AB01 b on a.aab001 = b.aab001 ").append("where 1 = 1 ");
		sql.append("and a.AAC002 = '").append(param.get("card_no")).append("' ");
		
		Map<String, Object> result = null;
		try {
			result = db_hsd.executeQueryHashMap(sql.toString());
		} catch (SQLException e) {
			throw e;
		} finally {
			db_hsd.close();
		}
		return result;
	}

}
