package com.models.cloud.pay.escrow.alipay.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.models.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundDetailParam.ChildRefundData;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundDetailParam.ShareRefundData;
import com.models.cloud.pay.escrow.alipay.param.AlipayRefundDetailParam.TradeRefundData;

/**
 * 阿里退款参数
 * @author yanjie.ji
 * @date 2016年8月31日 
 * @time 下午3:14:29
 */
public class AlipayRefundParam implements AlipayParam {
	
	/**
	 * 服务器异步通知页面路径
	 */
	private String notify_url;
	/**
	 * 充退通知地址
	 */
	private String dback_notify_url;
	/**
	 * 退款批次号
	 * 必填
	 * 必须保证唯一性
	 */
	private String batch_no;
	/**
	 * 退款请求时间
	 * 必填
	 */
	private String refund_date;
	/**
	 * 退款总笔数
	 * 必填
	 */
	private String batch_num;
	/**
	 * 是否使用冻结金额退款
	 * Y：可以使用冻结金额退款
	 * N：不可使用冻结金额退款
	 * 默认N
	 */
	private String use_freeze_amount="N";
	/**
	 * 退款明细数据集合
	 */
	private List<RefundTradeDetailParam> refund_detail_data;
	
	/**
	 * 退款交易数据明细
	 * @author yanjie.ji
	 * @date 2016年8月31日 
	 * @time 下午3:59:10
	 */
	public static class RefundTradeDetailParam{
		/**
		 * 交易退款数据
		 */
		private TradeRefundData tradeRefund;
		/**
		 * 分润退款数据
		 */
		private List<ShareRefundData> shareRefund;
		/**
		 * 子交易退款数据
		 */
		private ChildRefundData childRefund;

		public TradeRefundData getTradeRefund() {
			return tradeRefund;
		}

		public void setTradeRefund(TradeRefundData tradeRefund) {
			this.tradeRefund = tradeRefund;
		}

		public List<ShareRefundData> getShareRefund() {
			return shareRefund;
		}

		public void setShareRefund(List<ShareRefundData> shareRefund) {
			this.shareRefund = shareRefund;
		}

		public ChildRefundData getChildRefund() {
			return childRefund;
		}

		public void setChildRefund(ChildRefundData childRefund) {
			this.childRefund = childRefund;
		}
		
		public String getRefundData(){
			StringBuffer data = new StringBuffer();
			if(check()){
				data.append(this.getTradeRefund().getData());
				if(this.getShareRefund()!=null&&!this.getShareRefund().isEmpty()){
					for (ShareRefundData share : this.getShareRefund()) {
						data.append("|").append(share.getData());
					}
				}
				if(this.getChildRefund()!=null){
					data.append("$$").append(this.getChildRefund().getData());
				}
			}
			return data.toString();
		}
		
		public boolean check()throws UnmatchedParamException {
			if(tradeRefund==null) throw new UnmatchedParamException("交易退款数据不能为空");
			tradeRefund.check();
			if(shareRefund!=null&&!shareRefund.isEmpty()){
				for (ShareRefundData data : shareRefund) {
					data.check();
				}
			}
			if(childRefund!=null) childRefund.check();
			return true;
		}
	}
	
	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getDback_notify_url() {
		return dback_notify_url;
	}

	public void setDback_notify_url(String dback_notify_url) {
		this.dback_notify_url = dback_notify_url;
	}

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getRefund_date() {
		return refund_date;
	}

	public void setRefund_date(String refund_date) {
		this.refund_date = refund_date;
	}

	public String getBatch_num() {
		return batch_num;
	}

	public void setBatch_num(String batch_num) {
		this.batch_num = batch_num;
	}

	public String getUse_freeze_amount() {
		return use_freeze_amount;
	}

	public void setUse_freeze_amount(String use_freeze_amount) {
		this.use_freeze_amount = use_freeze_amount;
	}

	public List<RefundTradeDetailParam> getRefund_detail_data() {
		return refund_detail_data;
	}

	public void setRefund_detail_data(List<RefundTradeDetailParam> refund_detail_data) {
		this.refund_detail_data = refund_detail_data;
	}
	
	public String getRefundDetailData(){
		StringBuffer detail_data = new StringBuffer();
		boolean first = true;
		for (RefundTradeDetailParam data : this.getRefund_detail_data()) {
			if(first){
				detail_data.append(data.getRefundData());
				first = false;
			}else{
				detail_data.append("#").append(data.getRefundData());
			}
		}
		return detail_data.toString();
	}

	@Override
	public boolean checkSelf() throws UnmatchedParamException {
		List<String> emptyList = new ArrayList<String>();
		if(StringUtils.isEmpty(this.getBatch_no()))emptyList.add("batch_no");
		if(StringUtils.isEmpty(this.getRefund_date()))emptyList.add("refund_date");
		if(StringUtils.isEmpty(this.getBatch_num()))emptyList.add("batch_num");
		if(this.getRefund_detail_data()==null||this.getRefund_detail_data().isEmpty())
			emptyList.add("refund_detail_data");
		
		Map<String,Object> map = new HashMap<String,Object>();
		for (RefundTradeDetailParam data : this.getRefund_detail_data()) {
			data.check();
			if(map.containsKey(data.getTradeRefund().getTrade_no()))
				throw new UnmatchedParamException("一次批量退款中不能存在相同的交易号(trade_no)");
			else
				map.put(data.getTradeRefund().getTrade_no(), null);
		}
		
		if(!emptyList.isEmpty())  throw new ParamEmptyException(emptyList);
		if(!"Y".equals(this.getUse_freeze_amount())
				&&!"N".equals(this.getUse_freeze_amount()))
			throw new UnmatchedParamException("use_freeze_amount参数有误！");
		return true;
	}

}
