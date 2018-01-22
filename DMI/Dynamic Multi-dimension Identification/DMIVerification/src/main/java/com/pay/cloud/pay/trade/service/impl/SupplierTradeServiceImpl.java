package com.pay.cloud.pay.trade.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import com.pay.cloud.constants.BaseDictConstant;
import com.pay.cloud.pay.supplier.dao.ActSpBankMapper;
import com.pay.cloud.pay.supplier.dao.ActSpFinMapper;
import com.pay.cloud.pay.supplier.entity.ActSpBank;
import com.pay.cloud.pay.supplier.entity.ActSpFin;
import com.pay.cloud.pay.trade.dao.TdActRecMapper;
import com.pay.cloud.pay.trade.dao.TdOrdExtWithdrMapper;
import com.pay.cloud.pay.trade.dao.TdOrderMapper;
import com.pay.cloud.pay.trade.dao.TdSearchTaskMapper;
import com.pay.cloud.pay.trade.entity.TdActRec;
import com.pay.cloud.pay.trade.entity.TdOrdExtWithdr;
import com.pay.cloud.pay.trade.entity.TdOrder;
import com.pay.cloud.pay.trade.entity.TdSearchTask;
import com.pay.cloud.pay.trade.service.SupplierTradeService;
import com.pay.cloud.util.CacheUtils;
import com.pay.cloud.util.IdCreatorUtils;
import com.pay.cloud.util.ValidateUtils;
import com.pay.cloud.util.hint.Hint;

/**
 * 
 * @Description:商戶交易服务接口
 * @ClassName: SupplierTradeServiceImpl 
 * @author: danni.liao
 * @date: 2016年4月27日 上午10:40:43
 */
@Service("supplierTradeServiceImpl")
public class SupplierTradeServiceImpl implements SupplierTradeService {

	@Resource
	private TdOrderMapper tdOrderMapper;

	@Resource
	private TdOrdExtWithdrMapper tdOrdExtWithdrMapper;

	@Resource
	private ActSpBankMapper actSpBankMapper;

	@Resource
	private ActSpFinMapper actSpFinMapper;

	@Resource
	private TdActRecMapper tdActRecMapper;

	@Resource
	private TdSearchTaskMapper tdSearchTaskMapper;

	@Override
	public Map<String, Object> saveTdOrdWithdr(Map<String, Object> receiveMap)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(checkRepeat(receiveMap)){
			resultMap.put("resultCode", Hint.TD_13035_WITH_REPEAT_SERIAL_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.TD_13035_WITH_REPEAT_SERIAL_ERROR.getMessage());
			return resultMap;
		}

		Date crtTime = new Date();
		// String serial = receiveMap.get("serial").toString();
		// Long tdCount = tdOrderMapper.findTdOrderCountByMerOrderId(serial);
		Long tdId = this.getSeqTdId();
		// 保存到交易单表
		this.saveTdOrder(receiveMap, tdId, crtTime);
		// 保存到交易单 提现
		this.saveTdOrdExtWithdr(receiveMap, resultMap, tdId, crtTime);
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		resultMap.put("tdId", tdId);
		return resultMap;
	}

	/**
	 * 
	 * @Description: 检查是否重复的业务流水号
	 * @Title: checkRepeat 
	 * @param receiveMap
	 * @return
	 * @throws Exception boolean
	 */
	private boolean checkRepeat(Map<String, Object> receiveMap) throws Exception{
		TdOrder tdOrder = new TdOrder();
		// 发起商户账户id operId
		tdOrder.setFromActId(Long.parseLong(receiveMap.get("operId")
				.toString()));
		// 业务订单流水号 serial
		tdOrder.setOrigOrdCode(receiveMap.get("serial").toString());
		// 交易状态 status

		List<TdOrder> orderList = tdOrderMapper
				.findTdOrderListByCondition(tdOrder);
		if(orderList != null && orderList.size() > 0){
			return true;
		}
		return false;
	}
	
	private void saveTdOrder(Map<String, Object> receiveMap, Long tdId,
			Date crtTime) throws Exception {
		// 发起账户id
		Long operId = (Long) receiveMap.get("operId");
		// 提现金额
		BigDecimal amount = (BigDecimal) receiveMap.get("amount");
		TdOrder tdOrder = new TdOrder();
		// 交易单ID
		tdOrder.setTdId(tdId);
		// 医保项目ID MIC_ID 
		tdOrder.setMicId(receiveMap.get("micId").toString());
		// 交易类型 TD_TYPE_ID 
		tdOrder.setTdTypeId(BaseDictConstant.TD_TYPE_ID_WITHDRAW);
		// 交易业务类型 TD_BUSI_TYPE_ID
		// tdOrder.setTdBusiTypeId(BaseDictConstant.TD_TYPE_ID_WITHDRAW);
		// 渠道账户ID CHAN_ACT_ID 
		tdOrder.setChanActId(operId);
		// 付款账户类型 FROM_ACT_TYPE_ID
		tdOrder.setFromActTypeId(BaseDictConstant.ACT_TYPE_ID_SP);
		// 渠道账户APPID
		tdOrder.setChanAppid(receiveMap.get("appId").toString());
		// 付款账户ID FROM_ACT_ID 
		tdOrder.setFromActId(operId);
		// 付款用户ID PAY_USER_ID
		// 收款账户类型 TO_ACT_TYPE_ID 
		tdOrder.setToActTypeId(BaseDictConstant.ACT_TYPE_ID_SP);
		// 收款账户ID TO_ACT_ID 
		tdOrder.setToActId(Long.parseLong(receiveMap.get("actId").toString()));
		// 显示商户名称 DISP_SP_NAME
		// 商品描述 GOODS_DESC 
		// 资金平台ID FUND_ID 
		String fundId = CacheUtils
				.getDimSysConfConfValue("CUR_PP_FUND_ID");
		tdOrder.setFundId(fundId);
		// 资金平台交易流水号 FUND_TD_CODE
		// 商户回调地址 CB_ADDR varchar(120) 
		Object callbackUrl = receiveMap.get("callbackUrl");
		if (callbackUrl != null && callbackUrl.toString().trim().length() != 0) {
			tdOrder.setCbAddr(callbackUrl.toString());
		}
		// 本次交易费率 SERV_CHRG_RATIO
		// 本次交易手续费 SERV_CHRG_AMT 
		// 交易总金额 PAY_TOT
		tdOrder.setPayTot(amount);
		// 本次实收金额 RECV_CUR 
		tdOrder.setPayTot(amount);
		// 渠道商户订单号 ORIG_ORD_CODE
		tdOrder.setOrigOrdCode(receiveMap.get("serial").toString());
		// SP_SUB_SYS_CODE
		// 交易状态 TD_STAT_ID 
		tdOrder.setTdStatId(BaseDictConstant.TD_STAT_ID_INIT);
		// 交易日期 TD_DATE
		tdOrder.setTdDate(com.pay.cloud.util.DateUtils.dateToString(
				crtTime, "yyyyMMdd"));
		// 申请交易时间 TD_START_TIME 
		tdOrder.setTdStartTime(crtTime);
		// 交易结束时间 TD_END_TIME 
		// 交易失败编码 ERR_CODE
		// 对账状态 CONFIRM_STAT_ID 
		tdOrder.setConfirmStatId(BaseDictConstant.CONFIRM_STAT_ID_NONE);
		// 对账时间 CONFIRM_TIME 
		// 更新时间 UPD_TIME
		// 创建时间 CRT_TIME 
		tdOrder.setCrtTime(crtTime);
		tdOrder.setUpdTime(crtTime);
		// 备注 REMARK varchar(120)
		Object o = receiveMap.get("desc");
		if (o != null && o.toString().trim().length() != 0) {
			tdOrder.setRemark(o.toString());
		}
		tdOrderMapper.saveTdOrder(tdOrder);
	}

	private void saveTdOrdExtWithdr(Map<String, Object> receiveMap,
			Map<String, Object> resultMap, Long tdId, Date crtTime) throws Exception {
		//String batchNo = updateBatchNo();
		// 目标账户id
		Long actId = (Long) receiveMap.get("actId");
		// 查询账户
		ActSpBank actSpBank = actSpBankMapper.selectByActId(actId);
		resultMap.put("actSpBank", actSpBank);
		//resultMap.put("batchNo", batchNo);
		TdOrdExtWithdr tdOrdExtWithdr = new TdOrdExtWithdr();
		// 交易单ID TD_ID
		tdOrdExtWithdr.setTdId(tdId);
		// 提现状态 TD_WITHDR_STAT_ID
		tdOrdExtWithdr
				.setTdWithdrStatId(BaseDictConstant.TD_WITHDR_STAT_ID_INIT);
		// 查询提现次数 SEARCH_CNT
		tdOrdExtWithdr.setSearchCnt(0);
		// 下次查询时间 NEXT_SEARCH_TIME
		// 资金平台交易流水号 FUND_TD_CODE 
		// 资金平台交易返回信息 FUND_TD_MSG 
		// 银行ID BANK_ID 
		if (actSpBank != null) {
			tdOrdExtWithdr.setBankId(actSpBank.getBankId());
			// 银行编码 BANK_CODE
			// 银行账号 BANK_ACCOUNT
			tdOrdExtWithdr.setBankAccount(actSpBank.getBankAccount());
			// 银行卡户名 BANK_ACT_NAME
			tdOrdExtWithdr.setBankActName(actSpBank.getActName());
			// 其他支付属性A PAY_PROP_A
			//tdOrdExtWithdr.setPayPropA(batchNo);
			// 其他支付属性B PAY_PROP_B 支行
			tdOrdExtWithdr.setPayPropB(actSpBank.getPayPropA());
		}

		// 其他支付属性C PAY_PROP_C
		// 更新时间 UPD_TIME
		tdOrdExtWithdr.setUpdTime(crtTime);
		// 创建时间 CRT_TIME
		tdOrdExtWithdr.setCrtTime(crtTime);
		// 备注 REMARK 
		tdOrdExtWithdrMapper.insert(tdOrdExtWithdr);
	}

	/**
	 * 
	 * @Description: 可用资金预扣除，在途资金增加
	 * @Title: avalBalSubstractTravel
	 * @param actId
	 * @param amount
	 * @param date
	 * @return String
	 */
	private synchronized Hint avalBalSubstractTravel(Long actId,
			BigDecimal amount, Date date) {

		// 查询发起账户资金属性
		ActSpFin actSpFin = actSpFinMapper.selectByActId(actId);
		if (actSpFin == null) {
			// 发起账户不存在
			return Hint.TD_13019_WITH_OPER_SP_NOT_EXIST;
		}
		if (actSpFin.getLimWT() != null
				&& actSpFin.getLimWT().doubleValue() != 0
				&& actSpFin.getLimWT().doubleValue() < amount.doubleValue()) {
			// 超出每次最大提现金额
			return Hint.TD_13020_WITH_EXCEED_LIMIT_WITHDRAW_TIME;
		}
		// 每日已提现金额
		BigDecimal withdayAmt = null;
		if (actSpFin.getLastWithTime() != null
				&& DateUtils.isSameDay(date, actSpFin.getLastWithTime())) {
			withdayAmt = actSpFin.getWithDayAmt();
		} else {
			withdayAmt = new BigDecimal("0");
		}
		withdayAmt = withdayAmt.add(amount);
		// 超出每日最大提现金额
		if (actSpFin.getLimWDay() != null
				&& actSpFin.getLimWDay().doubleValue() != 0
				&& actSpFin.getLimWDay().doubleValue() < withdayAmt
						.doubleValue()) {
			return Hint.TD_13021_WITH_EXCEED_LIMIT_WITHDRAW_DAY;
		}
		BigDecimal avalBal = actSpFin.getAvalBal().subtract(amount);
		// 发起账户余额不足
		if (avalBal.intValue() < 0) {
			return Hint.TD_13022_WITH_OPER_ACT_NOT_ENOUGH_MONEY;
		}
		ActSpFin actSpFinUpd = new ActSpFin();
		actSpFinUpd.setActId(actId);
		actSpFinUpd.setAvalBal(avalBal);
		// 在途资金
		actSpFinUpd.setTravel(actSpFin.getTravel().add(amount));
		actSpFinUpd.setWithDayAmt(withdayAmt);
		actSpFinUpd.setLastWithTime(date);
		actSpFinUpd.setUpdTime(date);
		Long version = actSpFin.getVerNum();
		actSpFinUpd.setVerNum(version + 1);
		actSpFinUpd.setOldVerNum(version);
		int result = actSpFinMapper.updateByActId(actSpFinUpd);
		if(result <= 0){
			throw new RuntimeException("更新商户账户信息出现异常");
		}
		return Hint.SYS_SUCCESS;
	}

	/**
	 * 
	 * @Description: 资金扣除，在途资金扣除
	 * @Title: travelCommit
	 * @param actId
	 * @param amount
	 * @param date
	 *            BigDecimal 交易前余额
	 */
	private synchronized BigDecimal travelCommit(Long actId, BigDecimal amount,
			Date date) {
		// 资金真是扣款
		ActSpFin actSpFin = actSpFinMapper.selectByActId(actId);
		Long version = actSpFin.getVerNum();

		ActSpFin actSpFinUpd = new ActSpFin();
		actSpFinUpd.setActId(actId);
		// 余额 真实扣款
		actSpFinUpd.setBal(actSpFin.getBal().subtract(amount));
		// 在途
		actSpFinUpd.setTravel(actSpFin.getTravel().subtract(amount));
		actSpFinUpd.setUpdTime(date);
		actSpFinUpd.setVerNum(version + 1);
		actSpFinUpd.setOldVerNum(version);
		int result = actSpFinMapper.updateByActId(actSpFinUpd);
		if(result <= 0){
			throw new RuntimeException("更新商户账户信息出现异常");
		}
		return actSpFin.getBal();
	}

	/**
	 * 
	 * @Description: 可用资金回滚，在途资金扣除
	 * @Title: travelRollback
	 * @param actId
	 * @param amount
	 * @param date
	 *            void
	 */
	private synchronized void travelRollback(Long actId, BigDecimal amount,
			Date date) {
		// 查询发起账户资金属性 资金回滚
		ActSpFin actSpFin = actSpFinMapper.selectByActId(actId);
		if (actSpFin != null) {
			// 可用余额增加
			ActSpFin actSpFinUpd = new ActSpFin();
			actSpFinUpd.setActId(actId);
			actSpFinUpd.setAvalBal(actSpFin.getAvalBal().add(amount));
			// 在途资金回滚
			actSpFinUpd.setTravel(actSpFin.getTravel().subtract(amount));
			Date lastwithTime = new Date();
			// 每日已提现金额
			BigDecimal withdayAmt = null;
			if (DateUtils.isSameDay(lastwithTime, actSpFin.getLastWithTime())) {
				withdayAmt = actSpFin.getWithDayAmt();
				withdayAmt = withdayAmt.subtract(amount);
			} else {
				withdayAmt = new BigDecimal("0");
			}
			// 当日已提现
			actSpFinUpd.setWithDayAmt(withdayAmt);
			actSpFinUpd.setUpdTime(date);
			Long version = actSpFin.getVerNum();
			actSpFinUpd.setVerNum(version + 1);
			actSpFinUpd.setOldVerNum(version);
			int result = actSpFinMapper.updateByActId(actSpFinUpd);
			if(result <= 0){
				throw new RuntimeException("更新商户账户信息出现异常");
			}
		}
	}

	private long getSeqTdId() throws Exception {

		return Long.parseLong(IdCreatorUtils.getCashTradeOrderId());
	}

	private long getSeqTdActId() throws Exception {

		return Long.parseLong(IdCreatorUtils.getCashTradeRecordId());
	}
	
	private void updateTdOrdWithdrApplySucccess(Long tdId, String batchNo) throws Exception {

		TdOrder tdOrder = new TdOrder();
		Date date = new Date();
		tdOrder.setTdId(tdId);
		// 交易进行中
		tdOrder.setTdStatId(BaseDictConstant.TD_STAT_ID_DOING);
		tdOrder.setUpdTime(date);

		TdOrdExtWithdr tdOrdExtWithdr = new TdOrdExtWithdr();
		tdOrdExtWithdr.setTdId(tdId);
		tdOrdExtWithdr.setPayPropA(batchNo);
		// 提现失败
		tdOrdExtWithdr
				.setTdWithdrStatId(BaseDictConstant.TD_WITHDR_STAT_ID_APPLY);
		tdOrdExtWithdr.setUpdTime(date);

		tdOrderMapper.updateTdOrder(tdOrder);

		tdOrdExtWithdrMapper.updateByPrimaryKeySelective(tdOrdExtWithdr);

	}

	private void updateTdOrdWithdrSuccess(String batchNo, Long tdId, Date date)
			throws Exception {

		TdOrder tdOrder = new TdOrder();
		tdOrder.setTdId(tdId);
		// 交易成功
		tdOrder.setTdStatId(BaseDictConstant.TD_STAT_ID_SUCCESS);
		tdOrder.setUpdTime(date);
		// 结束时间
		tdOrder.setTdEndTime(date);
		TdOrdExtWithdr tdOrdExtWithdr = new TdOrdExtWithdr();
		tdOrdExtWithdr.setTdId(tdId);
		// 提现成功
		tdOrdExtWithdr
				.setTdWithdrStatId(BaseDictConstant.TD_WITHDR_STAT_ID_SUCCESS);
		tdOrdExtWithdr.setUpdTime(date);
		if(batchNo != null){
			tdOrdExtWithdr.setPayPropA(batchNo);
		}
		tdOrderMapper.updateTdOrder(tdOrder);

		tdOrdExtWithdrMapper.updateByPrimaryKeySelective(tdOrdExtWithdr);
	}

	@Override
	public synchronized Map<String, Object> updatePreWithdraw(Map<String, Object> receiveMap)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long operId = (Long) receiveMap.get("operId");
		Long tdId = (Long) receiveMap.get("tdId");
		BigDecimal amount = (BigDecimal) receiveMap.get("amount");

		// 在途资金处理
		Hint err = avalBalSubstractTravel(operId, amount, new Date());
		if (!Hint.SYS_SUCCESS.getCodeString().equals(err.getCodeString())) {
			// 余额不足
			updateTdOrdWithdrFail(receiveMap);
			resultMap.put("resultCode", err.getCodeString());
			resultMap.put("resultDesc", err.getMessage());
		} else {

			String batchNo = this.updateBatchNo();
			resultMap.put("batchNo", batchNo);
			// 申请成功
			updateTdOrdWithdrApplySucccess(tdId, batchNo);
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		}

		return resultMap;
	}

	@Override
	public synchronized void updateWithdrawFail(Map<String, Object> receiveMap) throws Exception {
		Long actId = (Long) receiveMap.get("operId");
		BigDecimal amount = (BigDecimal) receiveMap.get("amount");
		// 在图资金回滚
		travelRollback(actId, amount, new Date());
		// 更新交易单
		updateTdOrdWithdrFail(receiveMap);
	}

	@Override
	public synchronized boolean updateWithdrawSuccess(Map<String, Object> receiveMap)
			throws Exception {
		Long operId = null;
		Long actId = null;
		BigDecimal amount = null;
		Long tdId = (Long) receiveMap.get("tdId");
		Date date = new Date();
		// 获取交易信息
		TdOrder td = tdOrderMapper.findTdOrderByTdId(tdId);
		if (td == null) {
			// 不做处理
			return false;
		}
		// 交易金额
		amount = td.getPayTot();
		operId = td.getFromActId();
		actId = td.getToActId();
		// 余额扣款
		BigDecimal oldbal = travelCommit(operId, amount, date);
		// 提现成功
		String batchNo = (String) receiveMap.get("batchNo");
		updateTdOrdWithdrSuccess(batchNo, tdId, date);
		saveTdActRec(tdId, operId, actId, oldbal, amount);
		return true;
	}

	private void saveTdActRec(Long tdId, Long operId, Long actId,
			BigDecimal oldbal, BigDecimal amount) throws Exception {
		TdActRec record = new TdActRec();
		// 账户流水ID ACT_REC_ID 
		record.setActRecId(getSeqTdActId());
		// 平台账户类型 ACT_TYPE_ID 
		record.setActTypeId(BaseDictConstant.ACT_TYPE_ID_SP);
		// 平台账户ID ACT_ID 
		record.setActId(operId);
		//是否主操作
		record.setMainFlag(new Short(BaseDictConstant.MAIN_FLAG_YES));
		// 收入支出标识 IN_OUT_FLAG
		record.setInOutFlag(BaseDictConstant.IN_OUT_FLAG_OUT);
		// 本次金额 CUR_AMT
		record.setCurAmt(amount);
		// 前余额 OLD_BANLANCE
		record.setOldBanlance(oldbal);
		// 后余额 NEW_BANLANCE
		record.setNewBanlance(oldbal.subtract(amount));
		// 交易单ID TD_ID 
		record.setTdId(tdId);
		// 交易类型 TD_TYPE_ID
		record.setTdTypeId(BaseDictConstant.TD_TYPE_ID_WITHDRAW);
		// 交易业务类型 TD_BUSI_TYPE_ID 
		// 渠道账户ID CHAN_ACT_ID
		record.setChanActId(operId);
		// 特殊标识1 SPEC_FLAG1
		// 特殊标识2 SPEC_FLAG2
		// 特殊标识3 SPEC_FLAG3
		// 更新时间 UPD_TIME
		// 创建时间 CRT_TIME
		// 备注 REMARK
		tdActRecMapper.saveTdActRecInfo(record);

		// 渠道商户给合作商户提现
		if (!operId.toString().equals(actId.toString())) {
			// 查询目标账户资金属性 资金
			ActSpFin actSpFin = actSpFinMapper.selectByActId(actId);
			oldbal = actSpFin.getBal();
			record = new TdActRec();
			// 账户流水ID ACT_REC_ID
			record.setActRecId(getSeqTdActId());
			// 平台账户类型 ACT_TYPE_ID 
			record.setActTypeId(BaseDictConstant.ACT_TYPE_ID_SP);
			// 平台账户ID ACT_ID
			record.setActId(actId);
			//是否主操作
			record.setMainFlag(new Short(BaseDictConstant.MAIN_FLAG_NO));
			// 收入支出标识 IN_OUT_FLAG 
			record.setInOutFlag(BaseDictConstant.IN_OUT_FLAG_IN);
			// 本次金额 CUR_AMT 
			record.setCurAmt(amount);
			// 前余额 OLD_BANLANCE
			record.setOldBanlance(oldbal);
			// 后余额 NEW_BANLANCE
			record.setNewBanlance(oldbal.add(amount));
			// 交易单ID TD_ID
			record.setTdId(tdId);
			// 交易类型 TD_TYPE_ID
			record.setTdTypeId(BaseDictConstant.TD_TYPE_ID_WITHDRAW);
			// 交易业务类型 TD_BUSI_TYPE_ID
			// 渠道账户ID CHAN_ACT_ID
			record.setChanActId(operId);
			tdActRecMapper.saveTdActRecInfo(record);

			record = new TdActRec();
			// 账户流水ID ACT_REC_ID 
			record.setActRecId(getSeqTdActId());
			// 平台账户类型 ACT_TYPE_ID
			record.setActTypeId(BaseDictConstant.ACT_TYPE_ID_SP);
			// 平台账户ID ACT_ID
			record.setActId(actId);
			//是否主操作
			record.setMainFlag(new Short(BaseDictConstant.MAIN_FLAG_NO));
			// 收入支出标识 IN_OUT_FLAG
			record.setInOutFlag(BaseDictConstant.IN_OUT_FLAG_OUT);
			// 本次金额 CUR_AMT 
			record.setCurAmt(amount);
			// 前余额 OLD_BANLANCE 
			record.setOldBanlance(oldbal.add(amount));
			// 后余额 NEW_BANLANCE
			record.setNewBanlance(oldbal);
			// 交易单ID TD_ID 
			record.setTdId(tdId);
			// 交易类型 TD_TYPE_ID 
			record.setTdTypeId(BaseDictConstant.TD_TYPE_ID_WITHDRAW);
			// 交易业务类型 TD_BUSI_TYPE_ID 
			// 渠道账户ID CHAN_ACT_ID
			record.setChanActId(operId);
			tdActRecMapper.saveTdActRecInfo(record);
		}

	}

	@Override
	public void updateTdOrdWithdrFail(Map<String, Object> receiveMap)
			throws Exception {

		Long tdId = (Long) receiveMap.get("tdId");
		String errorCode = (String) receiveMap.get("errorCode");
		TdOrder tdOrder = new TdOrder();
		Date date = new Date();
		tdOrder.setTdId(tdId);
		// 交易失败
		tdOrder.setTdStatId(BaseDictConstant.TD_STAT_ID_FAIL);
		tdOrder.setErrCode(errorCode);
		// 结束时间
		tdOrder.setTdEndTime(date);
		tdOrder.setUpdTime(date);

		TdOrdExtWithdr tdOrdExtWithdr = new TdOrdExtWithdr();
		tdOrdExtWithdr.setTdId(tdId);
		// 提现失败
		tdOrdExtWithdr
				.setTdWithdrStatId(BaseDictConstant.TD_WITHDR_STAT_ID_FAIL);
		String ret_Code = (String) receiveMap.get("ret_Code");
//		if (ret_Code != null) {
//			tdOrdExtWithdr.setFundTdCode(ret_Code);
//		}
		String fundTdMsg = (String) receiveMap.get("error_msg");
		fundTdMsg = ret_Code + fundTdMsg;
		if (fundTdMsg != null) {
			tdOrdExtWithdr.setFundTdMsg(fundTdMsg);
		}
		String batchNo = (String) receiveMap.get("batchNo");
		if(batchNo != null){
			tdOrdExtWithdr.setPayPropA(batchNo);
		}
		tdOrdExtWithdr.setUpdTime(date);
		tdOrderMapper.updateTdOrder(tdOrder);

		tdOrdExtWithdrMapper.updateByPrimaryKeySelective(tdOrdExtWithdr);
	}

	@Override
	public void updateTdOrdWithdrException(Map<String, Object> receiveMap)
			throws Exception {
		Long tdId = (Long) receiveMap.get("tdId");
		TdOrder tdOrder = new TdOrder();
		Date date = new Date();
		tdOrder.setTdId(tdId);
		// 交易异常
		tdOrder.setTdStatId(BaseDictConstant.TD_STAT_ID_EXCEPTION);
		tdOrder.setUpdTime(date);
		tdOrderMapper.updateTdOrder(tdOrder);
		
		//更新批次号
		String batchNo = (String) receiveMap.get("batchNo");
		if(batchNo != null){
			TdOrdExtWithdr tdOrdExtWithdr = new TdOrdExtWithdr();
			tdOrdExtWithdr.setTdId(tdId);
			tdOrdExtWithdr.setPayPropA(batchNo);
			tdOrder.setUpdTime(date);
			tdOrdExtWithdrMapper.updateByPrimaryKeySelective(tdOrdExtWithdr);
		}
		// 加入异常任务
		TdSearchTask tdSearchTask = new TdSearchTask();
		// 交易单ID TD_ID bigint
		tdSearchTask.setTdId(tdId);
		// 交易类型 TD_TYPE_ID smallint
		tdSearchTask.setTdTypeId(BaseDictConstant.TD_TYPE_ID_WITHDRAW);
		// 交易状态 TD_STAT_ID smallint
		tdSearchTask.setTdStatId(BaseDictConstant.TD_STAT_ID_EXCEPTION);
		// 查询提现次数 SEARCH_CNT int
		tdSearchTask.setSearchCnt(0);
		// 下次查询时间 NEXT_SEARCH_TIME DATETIME
		//交易任务处理状态 TD_PROC_STAT_ID
		tdSearchTask.setTdProcStatId(Short.valueOf("0"));
		//错误码
		//错误信息
		// 更新时间 UPD_TIME DATETIME
		tdSearchTask.setUpdTime(date);
		// 创建时间 CRT_TIME DATETIME
		tdSearchTask.setCrtTime(date);
		// 备注 REMARK varchar(120)
		tdSearchTaskMapper.saveTdSearchTask(tdSearchTask);
	}

	@Override
	public Map<String, Object> searchTdOrdWithdr(Map<String, Object> receiveMap)
			throws Exception {
		TdOrder tdOrder = new TdOrder();
		// 发起商户账户id operId
		if (!ValidateUtils.isEmpty(receiveMap, "operId")) {
			tdOrder.setFromActId(Long.parseLong(receiveMap.get("operId")
					.toString()));
		}
		// 目标商户账户id actId
		if (!ValidateUtils.isEmpty(receiveMap, "actId")) {
			tdOrder.setToActId(Long.parseLong(receiveMap.get("actId")
					.toString()));
		}
		// 交易时间 date
		if (!ValidateUtils.isEmpty(receiveMap, "date")) {
			tdOrder.setTdDate(receiveMap.get("date").toString());
		}
		// 交易单号 tdId
		if (!ValidateUtils.isEmpty(receiveMap, "tdId")) {
			tdOrder.setTdId(Long.parseLong(receiveMap.get("tdId").toString()));
		}
		// 业务订单流水号 orderNo
		if (!ValidateUtils.isEmpty(receiveMap, "orderNo")) {
			tdOrder.setOrigOrdCode(receiveMap.get("orderNo").toString());
		}
		// 交易状态 status
		if (!ValidateUtils.isEmpty(receiveMap, "status")) {
			tdOrder.setTdStatId(Short.parseShort(receiveMap.get("status")
					.toString()));
		}
		List<TdOrder> orderList = tdOrderMapper
				.findTdOrderListByCondition(tdOrder);
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> rl = new ArrayList<Map<String, Object>>();
		if(orderList != null && orderList.size() > 0){
			for(int i =0; i < orderList.size(); i++){
				TdOrder o = orderList.get(i);
				Map<String, Object> r = new HashMap<String, Object>();
				r.put("tdId", o.getTdId());
				r.put("origOrdCode", o.getOrigOrdCode());
				r.put("tdStateId", o.getTdStatId());
				rl.add(r);
			}
		}
		result.put("list", rl);
		return result;
	}

	@Override
	public TdOrder findTdOrderByTdId(Long tdId) throws Exception {
		return tdOrderMapper.findTdOrderByTdId(tdId);
	}
	
	
	public String updateBatchNo() throws Exception{
		tdOrderMapper.replaceSeqYeepayBatchNo();
		String batchNo = tdOrderMapper.selectLastInsertBatchNo();
		int len = batchNo.length();
		if(len > 15){
			batchNo = batchNo.substring(len-15);
		}
		return batchNo;
	}
}
