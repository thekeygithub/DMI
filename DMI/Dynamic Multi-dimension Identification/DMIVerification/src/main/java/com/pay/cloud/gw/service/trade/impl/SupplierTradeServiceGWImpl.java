package com.pay.cloud.gw.service.trade.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.gw.service.trade.SupplierTradeServiceGW;
import com.pay.cloud.pay.escrow.yeepay.service.InstantPayService;
import com.pay.cloud.pay.payment.service.PaymentService;
import com.pay.cloud.pay.proplat.dao.PpFundBankRelMapper;
import com.pay.cloud.pay.proplat.entity.PpFundBankRel;
import com.pay.cloud.pay.supplier.entity.ActSp;
import com.pay.cloud.pay.supplier.entity.ActSpBank;
import com.pay.cloud.pay.supplier.entity.ActSpExt;
import com.pay.cloud.pay.supplier.service.SupplierService;
import com.pay.cloud.pay.trade.entity.TdOrder;
import com.pay.cloud.pay.trade.service.SupplierTradeService;
import com.pay.cloud.util.CacheUtils;
import com.pay.cloud.util.DimDictEnum;
import com.pay.cloud.util.HttpRequest;
import com.pay.cloud.util.HttpsPost;
import com.pay.cloud.util.hint.Hint;
import com.pay.secrity.crypto.AesCipher;
import com.pay.secrity.crypto.RSA;

@Service("supplierTradeServiceGWImpl")
public class SupplierTradeServiceGWImpl implements SupplierTradeServiceGW {

	private static final Logger logger = Logger.getLogger(SupplierTradeServiceGWImpl.class);

	@Resource(name = "yeepayPaymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "supplierTradeServiceImpl")
	private SupplierTradeService supplierTradeService;

	@Resource(name = "supplierServiceImpl")
	private SupplierService supplierService;
	
	@Resource(name="instantPayServiceImpl")
	private InstantPayService instantPayService;

	@Resource
	private PpFundBankRelMapper ppFundBankRelMapper;

	private static ExecutorService executor = Executors.newFixedThreadPool(10);

	@Override
	public Map<String, Object> withdrawForSupplier(
			Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = null;
		// 发起账户id
		Long operId = Long.parseLong(receiveMap.get("operId").toString());
		// 目标账户id
		Long actId = Long.parseLong(receiveMap.get("actId").toString());
		// 提现金额
		BigDecimal amount = new BigDecimal(receiveMap.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);

		String serial = receiveMap.get("serial").toString();

		// 交易单保存、
		receiveMap.put("operId", operId);
		receiveMap.put("actId", actId);
		receiveMap.put("amount", amount);

		// 生成转账交易单
		try {
			resultMap = supplierTradeService.saveTdOrdWithdr(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
			return getReturnMap(Hint.TD_13024_WITH_SAVE_ERROR);
		}
		if (!Hint.SYS_SUCCESS.getCodeString().equals(
				resultMap.get("resultCode"))) {
			return resultMap;
		}
		Long tdId = (Long) resultMap.get("tdId");
		receiveMap.put("tdId", resultMap.get("tdId"));
		// 目标账户查询 银行账户
		ActSpBank actSpBank = (ActSpBank) resultMap.get("actSpBank");
		if (actSpBank == null) {
			return updateTdOrdFailAndgetReturnMap(receiveMap,
					Hint.TD_13032_WITH_ACT_NOT_HAVE_VALID_CARD);
		}
		// 目标账户审核
		ActSp actSP = null;
		try {
			actSP = supplierService.findSpActByActId(actId);
		} catch (Exception e2) {
			return updateTdOrdFailAndgetReturnMap(receiveMap,
					Hint.SP_12002_SEARCH_ACCOUNT_INFO_ERROR);
		}
		if (actSP == null
				|| BaseDictConstant.ACT_STAT_ID_FREEZEN == actSP.getActStatId()
				|| BaseDictConstant.SP_OPEN_CHK_FLAG_OK != actSP
						.getSpOpenChkFlag()) {
			return updateTdOrdFailAndgetReturnMap(receiveMap,
					Hint.TD_13023_WITH_DEST_ACT_FREEZEN);
		}
		// 查询大账户余额 如果做到数据同步 不做 易宝支付控制
		resultMap = checkYeepayAccount(receiveMap);

		if (!Hint.SYS_SUCCESS.getCodeString().equals(
				resultMap.get("resultCode"))) {
			return resultMap;
		}

		try {
			// 预扣款
			resultMap = supplierTradeService.updatePreWithdraw(receiveMap);
		} catch (Exception e1) {
			logger.error("error", e1);
			// 提现预扣款失败
			return updateTdOrdFailAndgetReturnMap(receiveMap,
					Hint.TD_13025_WITH_PRE_SUBSTRAT_ERROR);
		}
		if (!Hint.SYS_SUCCESS.getCodeString().equals(
				resultMap.get("resultCode"))) {
			return resultMap;
		}

		Map<String, String> payparams = new HashMap<String, String>();

		Map<String, Object> payresultMap = null;
		String batchNo = (String)resultMap.get("batchNo");
		if(batchNo == null){
			return withdrFailAndReturn(receiveMap,
					Hint.TD_13034_WITH_YEEPAY_BATCH_NO_GEN_ERROR, null,
					null);
		}
		receiveMap.put("batchNo", batchNo);
		logger.info("付款生成的批次号为："+batchNo);
		//批次号 
		payparams.put("batch_No", batchNo);
		// 订单号 order_Id
		payparams.put("order_Id", tdId.toString());
		// amount 打款金额是非负浮点数，以元为单位，小数
		payparams.put("amount", amount.toString());
		// account_Name 帐户名称是收款帐户的开户名称Z(1,100
		payparams.put("account_Name", actSpBank.getActName());
		// account_Number 帐户号
		payparams.put("account_Number", actSpBank.getBankAccount());
		// 账户类型
		payparams.put("account_Type", getType(actSpBank.getPayActTypeId()));
		if (actSpBank.getBankId() != null) {
			String bankCode = "";
			try {
				String fundId = CacheUtils
						.getDimSysConfConfValue("CUR_PP_FUND_ID");
				if (fundId != null) {
					PpFundBankRel ppFundBankRel = new PpFundBankRel();
					ppFundBankRel.setFundId(fundId);
					ppFundBankRel.setBankId(actSpBank.getBankId());
					ppFundBankRel.setValidFlag(Short.valueOf(String
							.valueOf(DimDictEnum.VALID_FLAG_VALID.getCode())));
					List<PpFundBankRel> ppFundBankRelList = ppFundBankRelMapper
							.findPpFundBankRelList(ppFundBankRel);
					if (ppFundBankRelList != null
							&& ppFundBankRelList.size() != 0) {
						bankCode = ppFundBankRelList.get(0).getPpBankCode();
					}
				}

			} catch (Exception e) {
				logger.error("error", e);
			}
			payparams.put("bank_Code", bankCode);
		}
		if (actSpBank.getBankName() != null) {
			payparams.put("bank_Name", actSpBank.getBankName());
		}
		// 支行名称
		if (actSpBank.getPayPropA() != null) {
			payparams.put("branch_Bank_Name", actSpBank.getPayPropA());

		}
		// payee_Email
		// payparams.put("payee_Email", "");
		// // payee_Mobile 收款人手机号
		// payparams.put("payee_Mobile", "");
		// fee_Type 手续费收取方式是取值：“SOURCE” 商户承担“TARGET”用户承担
		payparams.put("fee_Type", "SOURCE");
		Object desc = receiveMap.get("desc");
		// leave_Word
		if (desc != null) {
			payparams.put("leave_Word", desc.toString());
		}
		String retCode = null;
		try {
			// 发起实际交易
			payresultMap = paymentService.transferSingle(payparams);
			retCode = String.valueOf(payresultMap.get("ret_Code"));
		} catch (Exception e) {
			logger.error("error", e);
			retCode = "9999";
		}

		// 提交成功
		if ("1".equals(retCode)) {
			// 打款码
			String rl_code = String.valueOf(payresultMap.get("rl_code"));
			// 已拒绝
			if ("0028".equals(rl_code)) {
				// 失败
				return withdrFailAndReturn(receiveMap,
						Hint.TD_13029_WITH_REJECTED_ERROR, rl_code,
						String.valueOf(payresultMap.get("error_Msg")));

			} else if ("0030".equals(rl_code)) {
				return withdrExceptionAndReturn(receiveMap);
			} else {
				// 等待通知 
			}
		} else if ("9999".equals(retCode)) {
			// 异常
			return withdrExceptionAndReturn(receiveMap);
		} else {
			// 失败
			return withdrFailAndReturn(receiveMap,
					Hint.TD_13029_WITH_REJECTED_ERROR,
					String.valueOf(payresultMap.get("ret_Code")), String.valueOf(payresultMap.get("error_Msg")));
		}
		resultMap = getReturnMap(Hint.SYS_SUCCESS);
		// 支付平台流水
		resultMap.put("paySerial", tdId.toString());
		// 请求流水
		resultMap.put("serial", serial);
		return resultMap;
	}

	@Override
	public void WithdrawActiveNotify(String batchNo, String tdId, String status, String message) throws Exception {
		Map<String, Object> receiveMap = new HashMap<String, Object>();
		receiveMap.put("tdId", Long.parseLong(tdId));
		receiveMap.put("batchNo", batchNo);
		// 结果成功
		if ("S".equals(status)) {
			supplierTradeService.updateWithdrawSuccess(receiveMap);
		} else {
			// 提现失败
			withdrFailAndReturn(receiveMap, Hint.TD_13030_WITH_FAIL, status,
					message);
		}
		// 通知业务系统
		executor.execute(new AysnNotify(tdId, status, message));
	}

	/**
	 * 
	 * @Description: 更新交易失败并返回结果,无需回滚金额
	 * @Title: updateTdOrdFailAndgetReturnMap
	 * @param receiveMap
	 * @param hint
	 * @return Map<String,Object>
	 */
	private Map<String, Object> updateTdOrdFailAndgetReturnMap(
			Map<String, Object> receiveMap, Hint hint) {
		try {
			receiveMap.put("errorCode", hint.getCodeString());
			supplierTradeService.updateTdOrdWithdrFail(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
		}
		return getReturnMap(hint);
	}

	private Map<String, Object> getReturnMap(Hint hint) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", hint.getCodeString());
		resultMap.put("resultDesc", hint.getMessage());
		return resultMap;
	}

	private Map<String, Object> checkYeepayAccount(
			Map<String, Object> receiveMap) {
		BigDecimal amount = (BigDecimal) receiveMap.get("amount");
		Map<String, String> payparams = new HashMap<String, String>();
		Map<String, Object> payresult = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ds = sdf.format(date);
		payparams.put("date", ds);
		try {
			payresult = paymentService.accountBalanaceQuery(payparams);
			// 查询失败
			if (!"1".equals(payresult.get("ret_Code"))) {
				receiveMap.put("ret_Code", payresult.get("ret_Code"));
				receiveMap.put("error_msg", payresult.get("error_msg"));
				// 更新失败原因
				return updateTdOrdFailAndgetReturnMap(receiveMap,
						Hint.TD_13026_SEARCH_YEEPAY_ACCOUNT_ERROR);
			}
		} catch (Exception e) {
			logger.error("error", e);
			// 查询大账户可用金额失败
			return updateTdOrdFailAndgetReturnMap(receiveMap,
					Hint.TD_13026_SEARCH_YEEPAY_ACCOUNT_ERROR);
		}

		BigDecimal validAmount = new BigDecimal(String.valueOf(payresult.get("valid_Amount")));
		if(logger.isInfoEnabled()){
			logger.info("大账户余额为" + validAmount.doubleValue());
		}
		// 提现金额超过大账户金额
		if (validAmount.doubleValue() < amount.doubleValue()) {
			// 更新失败原因
			return updateTdOrdFailAndgetReturnMap(receiveMap,
					Hint.TD_13027_YEEPAY_ACCOUNT_NOT_ENOUGH_ERROR);
		}
		return getReturnMap(Hint.SYS_SUCCESS);
	}

	/**
	 * 
	 * @Description: 判断是否对公账户
	 * @Title: getType
	 * @param payActTypeId
	 * @return boolean
	 */
	private String getType(short payActTypeId) {
		String type = "pr";
		if (payActTypeId == BaseDictConstant.PAY_ACT_TYPE_ID_PUB) {
			type = "pu";
		}
		return type;
		// 101 对公银行账号
		// 102 个人借记卡
		// 103 个人信用卡
		// 109 银联支付
		// 201 支付宝账号
		// 301 微信支付账号
		// 401 医保个人账户
		// 402 医保当年个人账户
		// 403 医保历年个人账户
	}

	/**
	 * 
	 * @Description: 更新交易异常
	 * @Title: updateTdOrdWithdrException
	 * @param receiveMap
	 *            void
	 */
	private Map<String, Object> withdrExceptionAndReturn(
			Map<String, Object> receiveMap) {
		// 交易失败
		try {
			supplierTradeService.updateTdOrdWithdrException(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
		}
		return getReturnMap(Hint.TD_13031_WITH_EXCEPTION);
	}

	/**
	 * 
	 * @Description: 交易失败，回滚金额
	 * @Title: withdrFail
	 * @param receiveMap
	 *            void
	 */
	private Map<String, Object> withdrFailAndReturn(
			Map<String, Object> receiveMap, Hint hint, String ret_Code,
			String error_msg) {
		// 交易失败
		try {
			receiveMap.put("errorCode", hint.getCodeString());
			receiveMap.put("ret_Code", ret_Code);
			receiveMap.put("error_msg", error_msg);
			supplierTradeService.updateWithdrawFail(receiveMap);
		} catch (Exception e) {
			logger.error("error", e);
		}
		return getReturnMap(hint);
	}

	@Override
	public Map<String, Object> searchTdOrdWithdr(Map<String, Object> receiveMap) {

		try {
			 Map<String, Object> resultMap = getReturnMap(Hint.SYS_SUCCESS);
			 resultMap.putAll(supplierTradeService.searchTdOrdWithdr(receiveMap));
			return resultMap;
		} catch (Exception e) {
			logger.error("error", e);
			return getReturnMap(Hint.TD_13033_WITH_SEARCH_FAIL);
		}
	}

	/**
	 * 
	 * @Description: 异步通知业务系统提现操作结果 
	 * @ClassName: AysnNotify 
	 * @author: danni.liao
	 * @date: 2016年4月27日 上午10:15:28
	 */
	private class AysnNotify implements Runnable {
		private String paySerial;
		private String state;
		private String message;
		AysnNotify(String paySerial, String state, String message) {
			this.paySerial = paySerial;
			this.state = state;
			this.message = message;
		}

		@Override
		public void run() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("paySerial", paySerial);
			map.put("state", state);
			map.put("message", message);
			if(logger.isInfoEnabled()){
				logger.info("异步提现通知开始 交易单号:" + paySerial + ",state:" + state);
			}
			Long tdId = Long.parseLong(paySerial);
			TdOrder tdOrder = null;
			// 查询交易信息
			try {
				tdOrder = supplierTradeService.findTdOrderByTdId(tdId);
			} catch (Exception e) {
				logger.error("查询交易单失败");
				return;
			}
			if (tdOrder == null) {
				logger.error("交易单不存在");
				return;
			}
			map.put("serial", tdOrder.getOrigOrdCode());
			ActSpExt actSpExt = null;
			try {
				actSpExt = supplierService.findActSpExtByActId(tdOrder
						.getChanActId());
			} catch (Exception e) {
				logger.error("查询商户密钥信息失败");
				return;
			}
			if (actSpExt == null) {
				logger.error("商户密钥信息不存在");
				return;
			}
			Map<String, Object> encryptMap;
			try {
				encryptMap = encode(map, actSpExt);
			} catch (Exception e) {
				logger.error("加密失败");
				return;
			}
			// 调用地址
			String callBackUrl = String.valueOf(tdOrder.getCbAddr()).trim();
			String json2 = JSON.toJSONString(encryptMap);
			String resultStr;

			try {
				if(callBackUrl.startsWith("https")){
					resultStr = HttpsPost.doPost(callBackUrl, json2, HttpsPost.CONTENT_TYPE_JSON, HttpsPost.DEFAULT_CHARSET, HttpsPost.CONNECT_TIMEOUT, HttpsPost.READ_TIMEOUT);
				}else {
					resultStr = HttpRequest.doPostJson(callBackUrl, json2);
				}
				if(logger.isInfoEnabled()){
					logger.info("通知结果" + resultStr);
				}
			}catch (Exception e) {
				logger.error("通知失败：" + e.getMessage());
				e.printStackTrace();
			}
		}

		private Map<String, Object> encode(Map<String, Object> map,
				ActSpExt actSpExt) throws Exception {
			Map<String, Object> encryptMap = new HashMap<String, Object>();
			String dataJson = JSON.toJSONString(map);
			/**
			 * 生成AESkey和AESIv秘钥
			 */
			AesCipher aes = new AesCipher();
			String aesKey = aes.generateKeyToBase64(128);
			String aesIv = aes.generateIvBytesToBase64();// 生成aeskey 和 aesiv
			Map<String, Object> keysMap = new HashMap<String, Object>();
			keysMap.put("aesKey", aesKey);
			keysMap.put("aesIv", aesIv);
			String keysJson = JSON.toJSONString(keysMap);

			/**
			 * RSA易保私密解密 输入：动态AES KEY,动态 AES Iv 输出 ：keys(RSA加密
			 * AES密钥)，ybPrivateKEY(RSA易保PrivateKEY)
			 */
			String keysEncrypt = RSA.encrypt(keysJson, actSpExt.getSpKeyPub());

			/**
			 * 签名数据 输入：原文数据，易保RSA privateKey 输出：密文
			 */
			String signEncrypt = RSA.sign(dataJson, actSpExt.getServerKeyPri());
			/**
			 * 数据加密 输入：原文数据，动态AES KEY,动态 AES Iv
			 */
			String dataEncrypt = aes.encrypt(dataJson, aesKey, aesIv);

			encryptMap.put("sign", signEncrypt);
			encryptMap.put("keys", keysEncrypt);
			encryptMap.put("data", dataEncrypt);
			return encryptMap;
		}
	}

	@Override
	public Map<String, Object> handleExceptionOrder(
			Map<String, Object> receiveMap) {
		// TODO Auto-generated method stub
		
		//paymentService.batchDetailQuery(params)
		return null;
	}
	
	@Override
	public Map<String, Object> accountBalanaceQuery(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		try{
			Map<String, String> payparams = new HashMap<String, String>();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String ds = sdf.format(date);
			payparams.put("date", ds);
			Map<String, Object> payresult = paymentService.accountBalanaceQuery(payparams);
			// 查询失败
			if (!"1".equals(payresult.get("ret_Code"))) {
				resultMap.put("resultCode", payresult.get("ret_Code"));
				resultMap.put("resultDesc", payresult.get("error_msg"));
				// 查询失败
				return resultMap;
			}
			resultMap.put("resultCode",Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc",Hint.SYS_SUCCESS.getMessage());
			resultMap.put("balanceAmount", payresult.get("balance_Amount"));
			resultMap.put("validAmount", payresult.get("valid_Amount"));
			return resultMap;
			
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
	}

	@Override
	public Map<String, Object> accountPayClearData(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String startdate = receiveMap.get("startdate")==null?"":receiveMap.get("startdate").toString();
			String enddate = receiveMap.get("enddate")==null?"":receiveMap.get("enddate").toString();
			Map<String, String> map = instantPayService.getPayClearData(startdate,enddate);
			String error_code = map.get("error_code");
			if(error_code!=null && error_code.equals("1")){
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				resultMap.put("payClearDate", map.get("payClearDate"));
				return resultMap;
			}else{
				if(error_code==null || error_code.equals("")){
					resultMap.put("resultCode",  Hint.SYS_10001_SYSTEM_ERROR.getCodeString());
					resultMap.put("resultDesc",  Hint.SYS_10001_SYSTEM_ERROR.getMessage());
					return resultMap;
				}
				resultMap.put("resultCode",  map.get("error_code"));
				resultMap.put("resultDesc",  map.get("error"));
				return resultMap;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
	}

	@Override
	public Map<String, Object> refundClearData(Map<String, Object> receiveMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			String startdate = receiveMap.get("startdate")==null?"":receiveMap.get("startdate").toString();
			String enddate = receiveMap.get("enddate")==null?"":receiveMap.get("enddate").toString();
			Map<String, String> map = instantPayService.getRefundClearData(startdate,enddate);
			String error_code = map.get("error_code");
			if(error_code!=null && error_code.equals("1")){
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
				resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
				resultMap.put("refundClearDate", map.get("refundClearDate"));
				return resultMap;
			}else{
				if(error_code==null || error_code.equals("")){
					resultMap.put("resultCode",  Hint.SYS_10001_SYSTEM_ERROR.getCodeString());
					resultMap.put("resultDesc",  Hint.SYS_10001_SYSTEM_ERROR.getMessage());
					return resultMap;
				}
				resultMap.put("resultCode",  map.get("error_code"));
				resultMap.put("resultDesc",  map.get("error"));
				return resultMap;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			resultMap.put("resultCode", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10005_SYSTEM_BUSY_ERROR.getMessage());
			return resultMap;
		}
	}
}
