package com.dhcc.ts.gh;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dhcc.modal.system.PageModel;
import com.dhcc.ts.yyxg.BaseAction;
import com.dhcc.ts.yyxg.TsUtil;

public class OrderAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	/**
	 * 挂号列表接口
	 */
	public void queryHistoryOrders() {
//		Map<String, Object> resultMap = null;
		HttpServletRequest request = getRequest();
		Integer q_order_status = StringUtils.isBlank(request.getParameter("q_order_status")) ? null:getIntParam(request.getParameter("q_order_status"));
		Integer q_page_no = StringUtils.isBlank(request.getParameter("page")) ? 1:getIntParam(request.getParameter("page"));
		Integer q_page_size = StringUtils.isBlank(request.getParameter("rows")) ? 10:getIntParam(request.getParameter("rows"));
		OrderDao orderDao = new OrderDao();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("q_order_status", q_order_status);
			PageModel model = orderDao.getOderList(q_page_no, q_page_size, paramMap);
			
//			resultMap = getSuccessMap();
//			resultMap.put("orderCount", model.getTotalRecord());
//			resultMap.put("orderList", model.getList());
			
			TsUtil.outputJson(getWriter(), model);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
	}
	
	private Integer getIntParam(String param) {
		Integer num = null;
		try {
			num = Integer.parseInt(param);
		} catch (NumberFormatException e) {
			logger.error(param+"====>> int转换出错\n", e);
		}
		return num;
	}
	
	
	/**
	 * 挂号详情接口
	 */
	public void queryOrderDetail() {
		Map<String, Object> resultMap = null;
		HttpServletRequest request = getRequest();
		String q_order_id = request.getParameter("q_order_id");
		
		try {
			if (StringUtils.isBlank(q_order_id)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			
			OrderDao orderDao = new OrderDao();
			Map<String, Object> orderMap = orderDao.getOrderById(q_order_id);
			String duty_id = orderMap.get("DUTY_ID").toString();
			Map<String, Object> dutyMap = orderDao.getDutyById(duty_id);
			
			resultMap = getSuccessMap();
			setOrderDetail(resultMap, orderMap, dutyMap);
			
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
		
	}
	
	private void setOrderDetail(Map<String, Object> resultMap, Map<String, Object> orderMap,
			Map<String, Object> dutyMap) {
		
		Map<String, Object> hospInfo = new HashMap<String, Object>();
		Map<String, Object> orderInfo = new HashMap<String, Object>();
		Map<String, Object> wayInfo = new HashMap<String, Object>();
		resultMap.put("hospInfo", hospInfo);
		resultMap.put("orderInfo", orderInfo);
		resultMap.put("wayInfo", wayInfo);
		hospInfo.put("HOS_NAME", dutyMap.get("HOS_NAME"));
		hospInfo.put("DEP_NAME", dutyMap.get("DEP_NAME"));
		hospInfo.put("STAFF_NAME", dutyMap.get("STAFF_NAME"));
		hospInfo.put("WORK_DATE", orderMap.get("WORK_DATE"));
		orderInfo.put("PATIENT_NAME", orderMap.get("PATIENT_NAME"));
		orderInfo.put("CARD_NO", orderMap.get("CARD_NO"));
		orderInfo.put("ZG_AMOUNT", orderMap.get("ZG_AMOUNT"));
		orderInfo.put("SF_AMOUNT", orderMap.get("SF_AMOUNT"));
		orderInfo.put("TZ_AMOUNT", orderMap.get("TZ_AMOUNT"));
		wayInfo.put("TRAFFIC", dutyMap.get("TRAFFIC"));
		wayInfo.put("ADDRESS", dutyMap.get("ADDRESS"));
	}

	/**
	 * 确认挂号接口
	 */
	public void orderDoctorPlan() {
		Map<String, Object> resultMap = null;
		HttpServletRequest request = getRequest();
		String o_work_date = request.getParameter("o_work_date");
		String o_addr_id = request.getParameter("o_addr_id");
		String o_duty_id = request.getParameter("o_duty_id");
		
		try {
			if (StringUtils.isBlank(o_addr_id)||StringUtils.isBlank(o_duty_id)||StringUtils.isBlank(o_work_date)) {
				resultMap = getErrorMap("参数不能为空");
				TsUtil.outputJson(getWriter(), resultMap);
				return ;
			}
			
			OrderDao orderDao = new OrderDao();
			Map<String, Object> addrMap = orderDao.getAddressById(o_addr_id);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("o_duty_id", o_duty_id);
			paramMap.put("o_work_date", o_work_date);
			paramMap.putAll(addrMap);
			String orderId = orderDao.createOrder(paramMap);
			
			resultMap = getSuccessMap();
			resultMap.put("orderId", orderId);
		
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
	}
	
	/**
	 * 订单付款
	 */
	public void payOrder() {
		Map<String, Object> resultMap = null;
		HttpServletRequest request = getRequest();
		String o_id = request.getParameter("o_id");
		String o_sf_amount = request.getParameter("o_sf_amount");
		String o_pay_type = request.getParameter("o_pay_type");
		OrderDao orderDao = new OrderDao();
		try {
			Map<String, Object> orderMap = orderDao.getOrderById(o_id);
			// 验证订单
			validateOrder(orderMap, o_sf_amount);
			// 付款
			payment();
			// 更新订单
			orderDao.updateOrderStatus(o_id, 1, o_pay_type);
			resultMap = getSuccessMap();
		
			TsUtil.outputJson(getWriter(), resultMap);
		} catch (SQLException e) {
			logger.error("执行查询出错\n", e);
		} catch (IOException e) {
			logger.error("输出json数据出错\n", e);
		}
		
	}

	private boolean payment() {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean validateOrder(Map<String, Object> orderMap, String o_sf_amount) {
		// TODO Auto-generated method stub
		
		return true;
	}
}
