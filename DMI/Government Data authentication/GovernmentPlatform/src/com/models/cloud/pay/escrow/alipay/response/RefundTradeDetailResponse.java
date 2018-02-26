package com.models.cloud.pay.escrow.alipay.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.models.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
import com.models.cloud.pay.escrow.alipay.response.RefundTradeDetailResponse.AlipayRefundDetailResponse.ChargeRefundResponse;
import com.models.cloud.pay.escrow.alipay.response.RefundTradeDetailResponse.AlipayRefundDetailResponse.ChildRefundResponse;
import com.models.cloud.pay.escrow.alipay.response.RefundTradeDetailResponse.AlipayRefundDetailResponse.ShareRefundResponse;
import com.models.cloud.pay.escrow.alipay.response.RefundTradeDetailResponse.AlipayRefundDetailResponse.TradeRefundResponse;

/**
 * 退款交易数据明细
 * 返回的数据格式参看svn
 * https://192.168.2.45/svn/ebaonet/docs/trunk/PaymentProd/EbaopaymentPlatform/V1.0/02 软件开发/0202 设计文档/020204 接口/第三方支付/支付宝/即时到账批量退款无密接口-refund_fastpay_by_platform_nopwd(20160302).zip
 * @author yanjie.ji
 * @date 2016年8月31日 
 * @time 下午3:59:10
 */
public class RefundTradeDetailResponse{
	
	/**
	 * 交易退款数据
	 */
	private TradeRefundResponse tradeRefund;
	/**
	 * 收费退款数据
	 */
	private ChargeRefundResponse chargeRefund;
	/**
	 * 分润退款数据
	 */
	private List<ShareRefundResponse> shareRefund;
	/**
	 * 子交易退款数据
	 */
	private ChildRefundResponse childRefund;

	public TradeRefundResponse getTradeRefund() {
		return tradeRefund;
	}

	public void setTradeRefund(TradeRefundResponse tradeRefund) {
		this.tradeRefund = tradeRefund;
	}

	public List<ShareRefundResponse> getShareRefund() {
		return shareRefund;
	}

	public void setShareRefund(List<ShareRefundResponse> shareRefund) {
		this.shareRefund = shareRefund;
	}

	public ChildRefundResponse getChildRefund() {
		return childRefund;
	}

	public void setChildRefund(ChildRefundResponse childRefund) {
		this.childRefund = childRefund;
	}
	
	public ChargeRefundResponse getChargeRefund() {
		return chargeRefund;
	}

	public void setChargeRefund(ChargeRefundResponse chargeRefund) {
		this.chargeRefund = chargeRefund;
	}
	/**
	 * 构造函数
	 * @param str
	 */
	public RefundTradeDetailResponse(String str){
		if(StringUtils.isEmpty(str)) return;
		String[] arr =null;
		//先设置子交易
		if(str.indexOf("$$")>-1){
			arr = str.split("\\$\\$");
			str=arr[0];
			this.setChildRefund(new ChildRefundResponse(arr[1]));
		}
		//设置分润退款数据
		if(str.indexOf("|")>-1){
			arr = str.split("\\|");
			str=arr[0];
			this.setShareRefund(new ArrayList<AlipayRefundDetailResponse.ShareRefundResponse>());
			for(int i=1;i<arr.length;i++){
				this.getShareRefund().add(new ShareRefundResponse(arr[i]));
			}
		}
		//再设置收费退款数据
		if(str.indexOf("$")>-1){
			arr = str.split("\\$");
			str = arr[0];
			this.setChargeRefund(new ChargeRefundResponse(arr[1]));
		}
		//设置交易退款数据
		this.setTradeRefund(new TradeRefundResponse(str));
		check();
	}
	
	/**
	 * 获取数据字符拼接串
	 * @return
	 */
	public String getRefundData(){
		StringBuffer data = new StringBuffer();
		if(check()){
			data.append(this.getTradeRefund().getData());
			if(this.getShareRefund()!=null&&!this.getShareRefund().isEmpty()){
				for (ShareRefundResponse share : this.getShareRefund()) {
					data.append("|").append(share.getData());
				}
			}
			if(this.getChildRefund()!=null){
				data.append("$$").append(this.getChildRefund().getData());
			}
		}
		return data.toString();
	}
	
	/**
	 * 检查
	 * @return
	 * @throws UnmatchedParamException
	 */
	public boolean check()throws UnmatchedParamException {
		if(tradeRefund==null) throw new UnmatchedParamException("交易退款数据不能为空");
		tradeRefund.check();
		if(shareRefund!=null&&!shareRefund.isEmpty()){
			for (ShareRefundResponse data : shareRefund) {
				data.check();
			}
		}
		if(childRefund!=null) childRefund.check();
		if(chargeRefund!=null)chargeRefund.check();
		return true;
	}
	
	/**
	 * 根据字符串数据获取退款交际结果集
	 * @param str
	 * @return
	 */
	public static List<RefundTradeDetailResponse> getDetails(String str){
		List<RefundTradeDetailResponse> list = new ArrayList<RefundTradeDetailResponse>();
		if(StringUtils.isEmpty(str)) return list;
		String[] arr = null;
		if(str.indexOf("#")>-1){
			arr = str.split("#");
		}else{
			arr = new String[1];
			arr[0] = str;
		}
		for (String string : arr) {
			list.add(new RefundTradeDetailResponse(string));
		}
		return list;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @author yanjie.ji
	 * @date 2016年9月5日 
	 * @time 下午3:42:46
	 */
	public static abstract class AlipayRefundDetailResponse {
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
			if(StringUtils.isEmpty(this.getResult_code()))emptyList.add("result_code");
			if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
			
			List<String> paramList = new ArrayList<String>();
			if(!NumberUtils.isNumber(this.getRefund_amount())||Double.parseDouble(this.getRefund_amount())<0)paramList.add("refund_amount");
			if(!paramList.isEmpty()) throw new UnmatchedParamException(paramList);
			
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
		 * 处理结果码
		 */
		protected String result_code;
		
		public String getRefund_amount() {
			return refund_amount;
		}

		public void setRefund_amount(String refund_amount) {
			this.refund_amount = refund_amount;
		}
		
		public String getResult_code() {
			return result_code;
		}
		public void setResult_code(String result_code) {
			this.result_code = result_code;
		}

		/**
		 * 交易退款数据
		 * @author yanjie.ji
		 * @date 2016年8月31日 
		 * @time 上午11:00:25
		 */
		public static class TradeRefundResponse extends AlipayRefundDetailResponse{
			
			/**
			 * 原付款支付宝交易号
			 */
			private String trade_no;
			/**
			 * 批次号
			 */
			private String batch_no;
			/**
			 * 是否充退
			 */
			private String is_dback;
			/**
			 * 充退处理结果
			 */
			private String dback_result;
			
			
			public TradeRefundResponse(String str){
				if(StringUtils.isEmpty(str)) return;
				String[] array = str.split("\\^");
				if(array==null||(array.length!=3&&array.length!=4&&array.length!=6)) return;
				if(array.length==3){
					this.setTrade_no(array[0]);
					this.setRefund_amount(array[1]);
					this.setResult_code(array[2]);
				}else if(array.length==4){
					this.setBatch_no(array[0]);
					this.setTrade_no(array[1]);
					this.setRefund_amount(array[2]);
					this.setResult_code(array[3]);
				}else if(array.length==6){
					this.setBatch_no(array[0]);
					this.setTrade_no(array[1]);
					this.setRefund_amount(array[2]);
					this.setResult_code(array[3]);
					this.setIs_dback(array[4]);
					this.setDback_result(array[5]);
				}
				super.check();
			}

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
					data.append(this.getSplit()).append(this.getResult_code());
				}
				return data.toString();
			}

			public String getTrade_no() {
				return trade_no;
			}

			public void setTrade_no(String trade_no) {
				this.trade_no = trade_no;
			}

			public String getBatch_no() {
				return batch_no;
			}

			public void setBatch_no(String batch_no) {
				this.batch_no = batch_no;
			}

			public String getIs_dback() {
				return is_dback;
			}

			public void setIs_dback(String is_dback) {
				this.is_dback = is_dback;
			}

			public String getDback_result() {
				return dback_result;
			}

			public void setDback_result(String dback_result) {
				this.dback_result = dback_result;
			}
		}

		/**
		 * 分润退款数据
		 * @author yanjie.ji
		 * @date 2016年8月31日 
		 * @time 下午2:24:08
		 */
		public static class ShareRefundResponse extends AlipayRefundDetailResponse{
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
			
			public ShareRefundResponse(String str){
				if(StringUtils.isEmpty(str)) return;
				String[] array = str.split("\\^");
				if(array==null||array.length!=6) return;
				this.setTurn_out_account(array[0]);
				this.setTurn_out_user_id(array[1]);
				this.setInto_account(array[2]);
				this.setInto_user_id(array[3]);
				this.setRefund_amount(array[4]);
				this.setResult_code(array[5]);
				super.check();
			}

			@Override
			public String getData() {
				StringBuffer data = new StringBuffer();
				if(check()){
					data.append(this.getTurn_out_account()).append(this.getSplit())
						.append(this.getTurn_out_user_id()).append(this.getSplit())
						.append(this.getInto_account()).append(this.getSplit())
						.append(this.getInto_user_id()).append(this.getSplit())
						.append(this.getRefund_amount());
					data.append(this.getSplit()).append(this.getResult_code());
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
		public static class ChildRefundResponse extends AlipayRefundDetailResponse{
			/**
			 * 子交易共补款金额（总补款金额减去总退款金额）
			 */
			private String supplement_amount;
			
			public ChildRefundResponse(String str){
				if(StringUtils.isEmpty(str)) return;
				String[] array = str.split("\\^");
				if(array==null||array.length!=3) return;
				this.setRefund_amount(array[0]);
				this.setSupplement_amount(array[1]);
				this.setResult_code(array[2]);
				super.check();
			}
			
			@Override
			public String getData() {
				StringBuffer data = new StringBuffer();
				if(check()){
					data.append(this.getRefund_amount());
					data.append(this.getSplit()).append(this.getSupplement_amount());
					data.append(this.getSplit()).append(this.getResult_code());
				}
				return data.toString();
			}
			
			@Override
			protected boolean checkSelf() throws UnmatchedParamException {
				return true;
			}

			public String getSupplement_amount() {
				return supplement_amount;
			}

			public void setSupplement_amount(String supplement_amount) {
				this.supplement_amount = supplement_amount;
			}
		}
		
		/**
		 * 收费退款数据
		 * @author yanjie.ji
		 * @date 2016年8月31日 
		 * @time 下午2:29:26
		 */
		public static class ChargeRefundResponse extends AlipayRefundDetailResponse{
			/**
			 * 被收费人支付宝账号[交易时支付宝收取服务费的账户]
			 */
			private String charged_account;
			/**
			 * 被收费人支付宝账号对应用户ID[2088开头16位纯数字]
			 */
			private String charged_user;
			
			public ChargeRefundResponse(String str){
				if(StringUtils.isEmpty(str)) return;
				String[] array = str.split("\\^");
				if(array==null||array.length!=4) return;
				this.setCharged_account(array[0]);
				this.setCharged_user(array[1]);
				this.setRefund_amount(array[2]);
				this.setResult_code(array[3]);
				this.check();
			}
			
			@Override
			public String getData() {
				StringBuffer data = new StringBuffer();
				if(check()){
					data.append(this.getCharged_account());
					data.append(this.getSplit()).append(this.getCharged_user());
					data.append(this.getSplit()).append(this.getRefund_amount());
					data.append(this.getSplit()).append(this.getResult_code());
				}
				return data.toString();
			}
			
			@Override
			protected boolean checkSelf() throws UnmatchedParamException {
				List<String> emptyList = new ArrayList<String>();
				if(StringUtils.isEmpty(this.getCharged_account()))emptyList.add("charged_account");
				if(StringUtils.isEmpty(this.getCharged_user()))emptyList.add("charged_user");
				if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
				return true;
			}

			public String getCharged_account() {
				return charged_account;
			}

			public void setCharged_account(String charged_account) {
				this.charged_account = charged_account;
			}

			public String getCharged_user() {
				return charged_user;
			}

			public void setCharged_user(String charged_user) {
				this.charged_user = charged_user;
			}
		}
	}
}