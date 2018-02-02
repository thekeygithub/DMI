package com.models.cloud.gw.service.supplier.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.core.redis.RedisService;
import com.models.cloud.gw.service.supplier.SupplierServiceGW;
import com.models.cloud.gw.service.trade.SupplierTradeServiceGW;
import com.models.cloud.gw.service.trade.TdOrdExtWithdrService;
import com.models.cloud.pay.payment.service.PaymentService;
import com.models.cloud.pay.supplier.entity.ActSp;
import com.models.cloud.pay.supplier.service.SupplierService;
import com.models.cloud.pay.trade.entity.TdOrdExtWithdr;
import com.models.cloud.pay.trade.entity.TdOrder;
import com.models.cloud.pay.trade.service.TdOrderService;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

@Service("supplierServiceGWImpl")
public class SupplierServiceGWImpl implements SupplierServiceGW {
	
	@Resource(name="supplierServiceImpl")
	private SupplierService supplierService;
	@Resource
	private PaymentService yeepayPaymentServiceImpl;
	@Resource
	private TdOrderService tdOrderServiceImpl;
	@Resource
	private TdOrdExtWithdrService tdOrdExtWithdrServiceImpl;
	@Resource(name="supplierTradeServiceGWImpl")
	private SupplierTradeServiceGW supplierTradeServiceGW;
	@Resource
	private RedisService redisService;

	private static final Logger logger = Logger.getLogger(SupplierServiceGWImpl.class);

	@Override
	public Map<String, Object> addSupplier(Map<String, Object> receiveMap){
		
		Map<String, Object> result = null;
		try {
			result = supplierService.addSupplier(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
			result = new HashMap<String, Object>();
			result.put("resultCode", Hint.SP_12013_ADD_SP_ERROR.getCodeString());
			result.put("resultDesc", Hint.SP_12013_ADD_SP_ERROR.getMessage());
		
		}
		return result;
	}

	@Override
	public Map<String, Object> freezeSupplier(Map<String, Object> receiveMap) {
		Map<String, Object> result = null;
		try {
			return supplierService.updateSupplierState(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
			result = new HashMap<String, Object>();
			result.put("resultCode", Hint.SP_12014_FREEZE_SP_ERROR.getCodeString());
			result.put("resultDesc", Hint.SP_12014_FREEZE_SP_ERROR.getMessage());
		}
		return result;
	}

	@Override
	public Map<String, Object> subSupplierQuery(Map<String, Object> receiveMap) {
		
		Map<String, Object> result = null;
		try {
			return supplierService.subSupplierQuery(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
			result = new HashMap<String, Object>();
			result.put("resultCode", Hint.SP_12015_QUERY_SUBSP_ERROR.getCodeString());
			result.put("resultDesc", Hint.SP_12015_QUERY_SUBSP_ERROR.getMessage());
		}
		return result;
	}

	@Override
	public ActSp findSpActByActId(Long actId) throws Exception {
		return supplierService.findSpActByActId(actId);
	}

	@Override
	public Map<String, Object> bindBankCard(Map<String, Object> receiveMap) {
		Map<String, Object> result = null;
		try {
			return supplierService.bindBankCard(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
			result = new HashMap<String, Object>();
			result.put("resultCode", Hint.SP_12016_BIND_BANK_CARD_ERROR.getCodeString());
			result.put("resultDesc", Hint.SP_12016_BIND_BANK_CARD_ERROR.getMessage());
		}
		return result;
	}

	@Override
	public Map<String, Object> operBankCard(Map<String, Object> receiveMap) {
		Map<String, Object> result = null;
		try {
			return supplierService.operBankCard(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
			result = new HashMap<>();
			result.put("resultCode", Hint.SP_12017_OPER_BANK_CARD_ERROR.getCodeString());
			result.put("resultDesc", Hint.SP_12017_OPER_BANK_CARD_ERROR.getMessage());
		}
		return result;
	}

	public Map<String, Object> withdrawResultConfirm(Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String withdrawOrderId = String.valueOf(params.get("withdrawOrderId")).trim();
		if(ValidateUtils.isEmpty(withdrawOrderId)){
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "提现订单号不能为空");
			return resultMap;
		}
		TdOrder withdrawOrder = tdOrderServiceImpl.findTdOrderByPayOrderId(Long.parseLong(withdrawOrderId));
		if(null == withdrawOrder){
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "根据订单号未找到提现订单信息");
			return resultMap;
		}
		TdOrdExtWithdr tdOrdExtWithdr = tdOrdExtWithdrServiceImpl.findTdOrdExtWithdr(withdrawOrder.getTdId());
		if(null == tdOrdExtWithdr || ValidateUtils.isEmpty(tdOrdExtWithdr.getPayPropA())){
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "提现扩展信息为空或打款批次号为空");
			return resultMap;
		}
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("query_Mode", "1");//本级下级标识 1：查询本公司自己发的交易甲方填写1即可
		inputMap.put("product", "");
		inputMap.put("batch_No", tdOrdExtWithdr.getPayPropA());
		inputMap.put("order_Id", tdOrdExtWithdr.getTdId().toString());
		inputMap.put("page_No", "1");
		logger.info("请求YeePay查询提现结果：" + inputMap);
		Map<String, Object> returnMap = yeepayPaymentServiceImpl.batchDetailQuery(inputMap);
		logger.info("YeePay响应报文：" + returnMap);
		if(null == returnMap || returnMap.size() == 0 || !"1".equals(String.valueOf(returnMap.get("ret_Code")))){
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "查询YeePay获取提现结果出现异常");
			return resultMap;
		}
		Map list = (Map) returnMap.get("list");
		if(null == list || list.size() == 0){
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "提现明细信息异常 list=" + list);
			return resultMap;
		}
		Map items = (Map) list.get("items");
		if(null == items || items.size() == 0){
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "提现明细信息异常 items=" + items);
			return resultMap;
		}
		Map item = (Map) items.get("item");
		if(null == item || item.size() == 0){
			resultMap.put("resultCode", "1");
			resultMap.put("resultDesc", "提现明细信息异常 item=" + item);
			return resultMap;
		}
		if("S".equals(String.valueOf(item.get("bank_Status")).trim())){
			logger.info("打款-银行处理成功");
			String redisKey = "withdrawOrderResultExecute_".concat(tdOrdExtWithdr.getTdId().toString());
			int expire = 60;
			Long redisValue = redisService.setNx(redisKey, tdOrdExtWithdr.getTdId().toString(), expire);
			if(logger.isInfoEnabled()){
				logger.info("检测当前订单是否存在并发处理 redisKey=" + redisKey + ",redisValue=" + redisValue + ", expire=" + expire);
			}
			if(null == redisValue || redisValue == 0){
				if(logger.isInfoEnabled()){
					logger.info("当前订单发生并发请求，已阻止");
				}
				resultMap.put("resultCode", "1");
				resultMap.put("resultDesc", "并发请求已阻止");
				return resultMap;
			}
			supplierTradeServiceGW.WithdrawActiveNotify(tdOrdExtWithdr.getPayPropA(), tdOrdExtWithdr.getTdId().toString(), "S", "银行处理成功");
			redisService.delete(redisKey);
		}else {
			tdOrdExtWithdrServiceImpl.updateWithdrUpdDate(tdOrdExtWithdr.getTdId());
		}
		resultMap.put("resultCode", "0");
		resultMap.put("resultDesc", "成功");
		return resultMap;
	}
}
