package com.dhcc.ts.gh;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dhcc.modal.system.PageModel;
import com.dhcc.ts.database.DBManager_HIS;

public class OrderDao {

	/**
	 * 根据地址标识查询地址
	 * @param o_addr_id
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAddressById(String o_addr_id) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		Map<String, Object> addrMap = null;
		try {
			String sql = "select * from REG_ADDRESS where ADDR_ID = '" + o_addr_id + "'";
			addrMap = db_his.executeQueryHashMap(sql);
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		return addrMap;
	}

	/**
	 * 创建挂号单
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public String createOrder(Map<String, Object> paramMap) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		
		String orderId = CreateNum.getNum("SYS");//CommUtil.getID();
		try {
			String sql = "select WORK_FEE from FAC_DUTY_APP where DUTY_ID = '" + paramMap.get("o_duty_id")+"' ";
			Map<String, Object> feeMap = db_his.executeQueryHashMap(sql);
			BigDecimal sf_fee = new BigDecimal(feeMap.get("WORK_FEE").toString());
			
			StringBuilder isql = new StringBuilder();
			isql.append("insert into REG_ORDER (ORDER_ID, DUTY_ID, SF_AMOUNT, TZ_AMOUNT, ZG_AMOUNT, STATUS, PATIENT_NAME, GENDER, AGE, MOBILE, ADDRESS, CARD_NO, WORK_DATE, CREATE_DATE, UPDATE_DATE) values ('")
				.append(orderId).append("', '").append(paramMap.get("o_duty_id")).append("', ").append(sf_fee)
				.append(", 0.00, ").append(sf_fee).append(", ").append(0).append(", '").append(paramMap.get("USER_NAME"))
				.append("', '").append(paramMap.get("GENDER")).append("', '").append(paramMap.get("AGE")).append("', '")
				.append(paramMap.get("MOBILE")).append("', '").append(paramMap.get("ADDRESS")).append("', '")
				.append(paramMap.get("CARD_NO")).append("', '").append(paramMap.get("o_work_date")).append("', sysdate, sysdate) ");
			
			db_his.executeUpdate(isql.toString());
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		return orderId;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrderById(String q_order_id) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		Map<String, Object> orderMap = null;
		try {
			String sql = "select * from REG_ORDER where ORDER_ID = '" + q_order_id + "'";
			orderMap = db_his.executeQueryHashMap(sql);
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		return orderMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getDutyById(String duty_id) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		Map<String, Object> dutyMap = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select fh.hos_id, fh.hos_name, fde.dep_id, fde.dep_name, ")
		.append("fm.med_id, fm.staff_name, fh.traffic, fh.address ")
		.append("from FAC_DUTY_APP fd ")
		.append("inner join FAC_MEDICAL_APP fm on fm.med_id = fd.med_id ")
		.append("inner join FAC_DEPARTMENT_APP fde on fde.dep_id = fd.dep_id ")
		.append("inner join FAC_HOSPITAL_APP fh on fh.hos_id = fde.hos_id ")
		.append(" where fd.DUTY_ID = ").append("'"+duty_id+"' ");
		try {
			dutyMap = db_his.executeQueryHashMap(sql.toString());
		} catch (SQLException e) {
			throw e;
		} finally {
			db_his.close();
		}
		return dutyMap;
	}

	public PageModel getOderList(Integer q_page_no, Integer q_page_size, Map<String, Object> paramMap) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		PageModel model = null;
		Object q_order_status = paramMap.get("q_order_status");
		StringBuilder sql = new StringBuilder();
		sql.append("select fh.HOS_NAME, fm.STAFF_NAME, o.AGE, o.ADDRESS, o.ZG_AMOUNT, o.STATUS, o.SF_AMOUNT, ")
			.append("o.TZ_AMOUNT, o.ORDER_ID, o.CARD_NO, o.PATIENT_NAME, o.MOBILE, o.GENDER, ")
			.append("to_char(o.CREATE_DATE,'yyyy-mm-dd HH24:mi:ss')  CREATE_DATE, ")
			.append("to_char(o.UPDATE_DATE,'yyyy-mm-dd HH24:mi:ss')  UPDATE_DATE ")
			.append(" from REG_ORDER o")
			.append(" inner join FAC_DUTY_APP fd on fd.DUTY_ID = o.DUTY_ID ")
			.append(" inner join FAC_MEDICAL_APP fm on fm.MED_ID = fd.MED_ID ")
			.append(" inner join FAC_DEPARTMENT_APP fda on fda.DEP_ID = fd.DEP_ID ")
			.append(" inner join FAC_HOSPITAL_APP fh on fh.HOS_ID = fda.HOS_ID ")
			.append(" where 1=1");
		if (q_order_status != null && StringUtils.isNotBlank(q_order_status.toString())) {
			sql.append(" and status = " + q_order_status.toString());
		}
		sql.append(" order by CREATE_DATE desc ");
		try {
			model = db_his.getMapByPage(sql.toString(), q_page_no, q_page_size);
		} catch (Exception e) {
			throw e;
		} finally {
			db_his.close();
		}
		return model;
	}

	public boolean updateOrderStatus(String o_id, int status, String o_pay_type) throws SQLException {
		DBManager_HIS db_his = new DBManager_HIS();
		int count = 0;
		String sql = "update REG_ORDER set pay_type = '" + o_pay_type + "', status = " + status + " where order_id = '" + o_id + "'";
		try {
			count = db_his.executeUpdate(sql);
		} finally {
			db_his.close();
		}
		return count >= 1 ? true:false;
	}

}
