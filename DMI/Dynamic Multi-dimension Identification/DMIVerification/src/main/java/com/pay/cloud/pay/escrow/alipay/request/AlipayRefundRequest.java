package com.pay.cloud.pay.escrow.alipay.request;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.pay.cloud.pay.escrow.alipay.exception.ParamEmptyException;
import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
/**
 * 无密退款
 * @author yanjie.ji
 * @date 2016年8月25日 
 * @time 下午5:25:50
 */
public class AlipayRefundRequest extends AlipayBaseRequest {
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
	 * 单笔数据集
	 * 必填
	 */
	private String detail_data;
	/**
	 * 是否使用冻结金额退款
	 */
	private String use_freeze_amount;
	/**
	 * 申请结果返回类型
	 */
	private String return_type="xml";
	
	@Override
	protected boolean check() throws UnmatchedParamException {
		List<String> emptyList = new ArrayList<String>();
		if(StringUtils.isEmpty(this.getBatch_no())) emptyList.add("batch_no");
		if(StringUtils.isEmpty(this.getRefund_date())) emptyList.add("refund_date");
		if(StringUtils.isEmpty(this.getBatch_num())) emptyList.add("batch_num");
		if(StringUtils.isEmpty(this.getDetail_data())) emptyList.add("detail_data");
		if(!emptyList.isEmpty()) throw new ParamEmptyException(emptyList);
		
		if(!NumberUtils.isNumber(this.getBatch_num()))
			throw new  UnmatchedParamException("退款总笔数(batch_num)需要是数字！");
		
		if(Integer.parseInt(this.getBatch_num())!=this.getDetail_data().split("#").length)
			throw new  UnmatchedParamException("退款总笔数(batch_num)与明细数据条数(detail_data)不符");
		if(Integer.parseInt(this.getBatch_num())>1000)
			throw new  UnmatchedParamException("每次最多支持1000笔退款");
		if(StringUtils.isNotEmpty(this.getUse_freeze_amount())
				&&!"Y".equals(this.getUse_freeze_amount())
				&&!"N".equals(this.getUse_freeze_amount()))
			throw new UnmatchedParamException("use_freeze_amount参数有误！");
		
		return true;
	}

	@Override
	protected TreeMap<String, String> getSelfProperties() {
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("notify_url", this.getNotify_url());
		map.put("dback_notify_url", this.getDback_notify_url());
		map.put("batch_no", this.getBatch_no());
		map.put("refund_date", this.getRefund_date());
		map.put("batch_num", this.getBatch_num());
		map.put("detail_data", this.getDetail_data());
		map.put("use_freeze_amount", this.getUse_freeze_amount());
		map.put("return_type", this.getReturn_type());
		return map;
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

	public String getDetail_data() {
		return detail_data;
	}

	public void setDetail_data(String detail_data) {
		this.detail_data = detail_data;
	}

	public String getUse_freeze_amount() {
		return use_freeze_amount;
	}

	public void setUse_freeze_amount(String use_freeze_amount) {
		this.use_freeze_amount = use_freeze_amount;
	}

	public String getReturn_type() {
		return return_type;
	}

	public void setReturn_type(String return_type) {
		this.return_type = return_type;
	}
}
