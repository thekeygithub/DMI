package com.dhcc.ts.yyxg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dhcc.ts.database.DBManager_HSD;

public class InsureDao {

	/**
	 * http://localhost:9090/MDEPlatform/yyxg/getInsureList.action?card_no=41020419850220402X
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInsureList(Map<String, String> param) throws SQLException {
		DBManager_HSD db_hsd = new DBManager_HSD();
		List<Map<String, Object>> result = null;
		StringBuilder sql = new StringBuilder();
		
		sql.append("select AAE002, ")
			.append("  sum(decode(substr(AAE140, 0, 1), '1', AAC123, '2', 0, '3', 0, '4', 0, '5', 0)) yanglao, ")
			.append("  sum(decode(substr(AAE140, 0, 1), '1', 0, '2', AAC123, '3', 0, '4', 0, '5', 0)) shiye, ")
			.append("  sum(decode(substr(AAE140, 0, 1), '1', 0, '2', 0, '3', AAC123, '4', 0, '5', 0))yiliao, ")
			.append("  sum(decode(substr(AAE140, 0, 1), '1', 0, '2', 0, '3', 0, '4', AAC123, '5', 0)) gongshang, ")
			.append("  sum(decode(substr(AAE140, 0, 1), '1', 0, '2', 0, '3', 0, '4', 0, '5', AAC123)) shengyu ")
			.append("from ( ")
			.append("  (select AC13.AAE002, AC13.AAC123, substr(AC13.AAE140, 0, 1) AAE140 from AC13 ")
			.append("   inner join AC01 on AC01.AAC001 = AC13.AAC001 ")
			.append("   where AC01.AAC002='").append(param.get("card_no")).append("') ")
			.append("   union all ")
			.append("   (select AB08.AAE002, AB08.AAB210 AAC123, substr(AB08.AAE140, 0, 1) AAE140 from AB08 ")
			.append("   inner join AC01 on AC01.AAC001 = AB08.AAC001 ")
			.append("   where AC01.AAC002='").append(param.get("card_no")).append("') ")
			.append(") group by AAE002 ")
			.append("  order by AAE002 desc ");
		
		try {
			result = db_hsd.executeQueryListHashMap(sql.toString());
			if (result == null) result = new ArrayList<Map<String, Object>>();
		} catch (SQLException e) {
			throw e;
		} finally {
			db_hsd.close();
		}
		
		return result;
	}
	/*
select AAE002, 
sum(decode(substr(AAE140, 0, 1), '1', AAC123, '2', 0, '3', 0, '4', 0, '5', 0)) yanglao,
sum(decode(substr(AAE140, 0, 1), '1', 0, '2', AAC123, '3', 0, '4', 0, '5', 0)) shiye,
sum(decode(substr(AAE140, 0, 1), '1', 0, '2', 0, '3', AAC123, '4', 0, '5', 0))yiliao,
sum(decode(substr(AAE140, 0, 1), '1', 0, '2', 0, '3', 0, '4', AAC123, '5', 0)) gongshang,
sum(decode(substr(AAE140, 0, 1), '1', 0, '2', 0, '3', 0, '4', 0, '5', AAC123)) shengyu
from (
(select AC13.AAE002, AC13.AAC123, substr(AC13.AAE140, 0, 1) AAE140
from AC13
inner join AC01 on AC01.AAC001 = AC13.AAC001
where AC01.AAC002='110108197011253921')
union all
(select AB08.AAE002, AB08.AAB210 AAC123, substr(AB08.AAE140, 0, 1) AAE140
from AB08
inner join AC01 on AC01.AAC001 = AB08.AAC001
where AC01.AAC002='110108197011253921'))
group by aae002
order by aae002 desc
	 */

}
