package com.pay.cloud.pay.escrow.alipay.param;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.pay.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;


public abstract class AlipayRefundDetailParam {
	
	protected String reason_regular = "/^|/||/$|#";
	/**
	 * 分隔符
	 * @return
	 */
	protected String getSplit() {
		return "^";
	}
	/**
	 * 检查
	 * @return
	 * @throws UnmatchedParamException
	 */
	public boolean check()throws UnmatchedParamException{
		List<String> emptyList = new ArrayList<String>();
		if(StringUtils.isEmpty(this.getRefund_amount()))emptyList.add("refund_amount");
		if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
		
		List<String> paramList = new ArrayList<String>();
		if(!NumberUtils.isNumber(this.getRefund_amount())||Double.parseDouble(this.getRefund_amount())<0)paramList.add("refund_amount");
		if(!paramList.isEmpty()) throw new UnmatchedParamException(paramList);
		
		Pattern pattern = Pattern.compile(reason_regular);
		Matcher matcher = pattern.matcher(this.getRefund_reason());
		if(matcher.matches())
			throw new UnmatchedParamException("退款理由包含非法字符");
		if(this.getRefund_reason().getBytes().length>256)
			throw new UnmatchedParamException("退款理由过长");
		return checkSelf();
	}

	/**
	 * 获取数据拼接串
	 * @return
	 */
	public abstract String getData();
	
	/**
	 * 检查自身数据
	 * @return
	 * @throws UnmatchedParamException
	 */
	protected abstract boolean checkSelf()throws UnmatchedParamException;
	
	/**
	 * 退款总金额
	 */
	protected String refund_amount;
	/**
	 * 退款理由
	 */
	protected String refund_reason;
	
	public String getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}
	
	/**
	 * 交易退款数据
	 * @author yanjie.ji
	 * @date 2016年8月31日 
	 * @time 上午11:00:25
	 */
	public static class TradeRefundData extends AlipayRefundDetailParam{
		
		/**
		 * 原付款支付宝交易号
		 */
		private String trade_no;

		@Override
		protected boolean checkSelf()throws UnmatchedParamException{
			List<String> emptyList = new ArrayList<String>();
			if(StringUtils.isEmpty(this.getTrade_no()))emptyList.add("trade_no");
			if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
			return true;
		}
		
		@Override
		public String getData() {
			StringBuffer data = new StringBuffer();
			if(check()){
				data.append(this.getTrade_no()).append(this.getSplit()).append(this.getRefund_amount());
				if(StringUtils.isNotEmpty(this.getRefund_reason()))
					data.append(this.getSplit()).append(this.getRefund_reason());
			}
			return data.toString();
		}

		public String getTrade_no() {
			return trade_no;
		}

		public void setTrade_no(String trade_no) {
			this.trade_no = trade_no;
		}
	}

	/**
	 * 分润退款数据
	 * @author yanjie.ji
	 * @date 2016年8月31日 
	 * @time 下午2:24:08
	 */
	public static class ShareRefundData extends AlipayRefundDetailParam{
		/**
		 * 转出人支付宝账号[原收到分润金额的账户]
		 */
		private String turn_out_account;
		/**
		 * 转出人支付宝账号对应用户ID[2088开头16位纯数字]
		 */
		private String turn_out_user_id;
		
		/**
		 * 转入人支付宝账号[原付出分润金额的账户]
		 */
		private String into_account;
		/**
		 * 转入人支付宝账号对应用户ID
		 */
		private String into_user_id;

		@Override
		public String getData() {
			StringBuffer data = new StringBuffer();
			if(check()){
				data.append(this.getTurn_out_account()).append(this.getSplit())
					.append(this.getTurn_out_user_id()).append(this.getSplit())
					.append(this.getInto_account()).append(this.getSplit())
					.append(this.getInto_user_id()).append(this.getSplit())
					.append(this.getRefund_amount());
				if(StringUtils.isNotEmpty(this.getRefund_reason()))
					data.append(this.getSplit()).append(this.getRefund_reason());
			}
			return data.toString();
		}
		@Override
		protected boolean checkSelf() throws UnmatchedParamException {
			if(StringUtils.isEmpty(this.getTurn_out_account())&&StringUtils.isEmpty(this.getTurn_out_user_id()))
				throw new UnmatchedParamException("“转出人支付宝账号”与“转出人支付宝账号对应用户ID”至少填写一项");
			if(StringUtils.isEmpty(this.getInto_account())&&StringUtils.isEmpty(this.getInto_user_id()))
				throw new UnmatchedParamException("“转入人支付宝账号”与“转入人支付宝账号对应用户ID”至少填写一项");
			
			return true;
		}

		public String getTurn_out_account() {
			return turn_out_account;
		}

		public void setTurn_out_account(String turn_out_account) {
			this.turn_out_account = turn_out_account;
		}

		public String getTurn_out_user_id() {
			return turn_out_user_id;
		}

		public void setTurn_out_user_id(String turn_out_user_id) {
			this.turn_out_user_id = turn_out_user_id;
		}

		public String getInto_account() {
			return into_account;
		}

		public void setInto_account(String into_account) {
			this.into_account = into_account;
		}

		public String getInto_user_id() {
			return into_user_id;
		}

		public void setInto_user_id(String into_user_id) {
			this.into_user_id = into_user_id;
		}
	}

	/**
	 * 子交易退款数据
	 * @author yanjie.ji
	 * @date 2016年8月31日 
	 * @time 下午2:29:26
	 */
	public static class ChildRefundData extends AlipayRefundDetailParam{
		
		@Override
		public String getData() {
			StringBuffer data = new StringBuffer();
			if(check()){
				data.append(this.getRefund_amount());
				if(StringUtils.isNotEmpty(this.getRefund_reason()))
					data.append(this.getSplit()).append(this.getRefund_reason());
			}
			return data.toString();
		}
		
		@Override
		protected boolean checkSelf() throws UnmatchedParamException {
			return true;
		}
	}
}
