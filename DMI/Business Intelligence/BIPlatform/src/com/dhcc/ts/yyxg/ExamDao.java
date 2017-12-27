package com.dhcc.ts.yyxg;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.dhcc.ts.database.DBManager_HIS;

public class ExamDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExamList(Map<String, String> param) throws SQLException {
		
		DBManager_HIS db_his = new DBManager_HIS();
		StringBuilder sql = new StringBuilder();
		String type = param.get("type");
		String h_no = param.get("h_no");
		sql.append("select he.id, "+ h_no +" h_no, hv.drysj, he.title, he.img_url from h_exam he ")
			.append("left join h_visit hv on hv.czyh = he.h_no ")
			.append("where 1 = 1 ");
		
		String queryStr = ("1".equals(type) ? "and he.s_no = '":"and he.h_no = '") + h_no + "'";
				
		List<Map<String, Object>> result = null;
		try {
			result = db_his.executeQueryListHashMap(sql.toString() + queryStr);
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExamRecordList(Map<String, String> param) throws SQLException {
		
		DBManager_HIS db_his = new DBManager_HIS();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from h_exam where 1 = 1 ");
		sql.append("and id = '").append(param.get("exam_id")).append("'");
		
		List<Map<String, Object>> result = null;
		try {
			result = db_his.executeQueryListHashMap(sql.toString());
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		return result;
	}

	
}
