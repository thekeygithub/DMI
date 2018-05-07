package com.models.cloud.pay.escrow.abc.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.PaymentRequest;
import com.models.cloud.pay.escrow.abc.service.AbcPayService;
import com.models.cloud.pay.escrow.abc.utils.AbcConstants;
import com.models.cloud.pay.escrow.abc.utils.AbcParaUtils;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;
/**
 * 农行支付
 */
@Service("abcMerPayServiceImpl")
public class AbcMerPayServiceImpl implements AbcPayService {
	
	//日志
	private static Logger logger = Logger.getLogger(AbcMerPayServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.abc.service.AbcPaymentService#perform(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> perform(Map<String, Object> receiveMap) throws Exception {
		logger.info("##### AbcMerPayServiceImpl.perform() #####");
		logger.info(receiveMap);
		//结果值
		Map<String, Object> resultMap	= new HashMap<String, Object>();
		//返回值
		Map<String, Object> returnMap	= new HashMap<String, Object>();
		
		try{	
			//校验入口参数是否合法
			//资金平台ID
			if(false == ValidateUtils.checkParam(receiveMap,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			} 
			//OrderDate 订单日期 必须设定 ，YYYY/MM/DD
			if(false == ValidateUtils.checkParam(receiveMap,"OrderDate",ValidateUtils.TYPE_DATE,false,10,null,"yyyy/MM/dd",resultMap)){
				return resultMap;
			}
			//OrderTime 订单时间 必须设定 ，HH:MM:SS
			if(false == ValidateUtils.checkParam(receiveMap,"OrderTime",ValidateUtils.TYPE_DATE,false,8,null,"HH:mm:ss",resultMap)){
				return resultMap;
			}
			//ExpiredDate 设定订单保存时间 非必须 精确到秒 20141019104901
			if(false == ValidateUtils.checkParam(receiveMap,"ExpiredDate",ValidateUtils.TYPE_DATE,true,14,null,"yyyyMMddHHmmss",resultMap)){
				return resultMap;
			}
			//OrderNo 订单编号 必须设定
			if(false == ValidateUtils.checkParam(receiveMap,"OrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//OrderAmount 交易金额 必须设定 保留小数点后两位数字,*必输
			if(false == ValidateUtils.checkParam(receiveMap, "OrderAmount", ValidateUtils.TYPE_MONEY, false, 20, null, null,resultMap)){
				return resultMap;
			}
			//Fee 手续费金额 非必须
			if(false == ValidateUtils.checkParam(receiveMap, "Fee", ValidateUtils.TYPE_MONEY, true, 20, null, null,resultMap)){
				return resultMap;
			}
			//OrderURL 订单说明 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"OrderURL",ValidateUtils.TYPE_CHINESE,true,200,null,null,resultMap)){
				return resultMap;
			}
			//ReceiverAddress 收货地址 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"ReceiverAddress",ValidateUtils.TYPE_CHINESE,true,200,null,null,resultMap)){
				return resultMap;
			}
			//CommodityType CommodityType 商品种类 必须设定，
//			充值类 0101:支付账户充值
//			消费类 0201:虚拟类,0202:传统类,0203:实名类
//			转账类 0301:本行转账,0302:他行转账
//			缴费类 0401:水费,0402:电费,0403:煤气费,0404:有线电视费,0405:通讯费,
//			0406:物业费,0407:保险费,0408:行政费用,0409:税费,0410:学费,0499:其他
//			理财类 0501:基金,0502:理财产品,0599:其他
			if(false == ValidateUtils.checkParam(receiveMap,"CommodityType",ValidateUtils.TYPE_STRING,false,4,null,null,resultMap)){
				return resultMap;
			}
			//BuyIP 客户IP 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"BuyIP",ValidateUtils.TYPE_STRING,true,20,null,"^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$",resultMap)){
				return resultMap;
			}
			//OrderDesc 订单说明 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"OrderDesc",ValidateUtils.TYPE_CHINESE,true,200,null,null,resultMap)){
				return resultMap;
			}
			//orderTimeoutDate 订单有效期 非必须 单位:天
			if(false == ValidateUtils.checkParam(receiveMap,"orderTimeoutDate",ValidateUtils.TYPE_INTEGER,true,5,null,null,resultMap)){
				return resultMap;
			}
			
			//订单明细
			List<Map<String,Object>> detailList = (List<Map<String, Object>>) receiveMap.get("DetailItems");
			if (null == detailList || 0 == detailList.size()){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "DetailItems"));
	            return resultMap;
			}
			
			for(Map<String,Object> map:detailList){
				//SubMerName 二级商户名称 非必须
				if(false == ValidateUtils.checkParam(map,"SubMerName",ValidateUtils.TYPE_CHINESE,true,200,null,null,resultMap)){
					return resultMap;
				}
				//SubMerId 二级商户代码 非必须
				if(false == ValidateUtils.checkParam(map,"SubMerId",ValidateUtils.TYPE_STRING,true,50,null,null,resultMap)){
					return resultMap;
				}
				//SubMerMCC 二级商户MCC码 非必须
				if(false == ValidateUtils.checkParam(map,"SubMerMCC",ValidateUtils.TYPE_STRING,true,50,null,null,resultMap)){
					return resultMap;
				}
				//SubMerchantRemarks 二级商户备注项 非必须
				if(false == ValidateUtils.checkParam(map,"SubMerchantRemarks",ValidateUtils.TYPE_CHINESE,true,50,null,null,resultMap)){
					return resultMap;
				}
				//ProductID 商品代码 非必须
				if(false == ValidateUtils.checkParam(map,"ProductID",ValidateUtils.TYPE_STRING,true,50,null,null,resultMap)){
					return resultMap;
				}
				//ProductName 商品名称 必须设定
				if(false == ValidateUtils.checkParam(map,"ProductName",ValidateUtils.TYPE_CHINESE,true,200,null,null,resultMap)){
					return resultMap;
				}
				//UnitPrice 商品总价 非必须
				if(false == ValidateUtils.checkParam(map, "UnitPrice", ValidateUtils.TYPE_MONEY, true, 20, null, null,resultMap)){
					return resultMap;
				}
				//Qty 商品数量 非必须
				if(false == ValidateUtils.checkParam(map, "Qty", ValidateUtils.TYPE_INTEGER, true, 10, null, null,resultMap)){
					return resultMap;
				}
				//ProductRemarks 商品备注项 非必须
				if(false == ValidateUtils.checkParam(map,"ProductRemarks",ValidateUtils.TYPE_CHINESE,true,200,null,null,resultMap)){
					return resultMap;
				}
				//ProductType 商品类型 非必须
				if(false == ValidateUtils.checkParam(map,"ProductType",ValidateUtils.TYPE_STRING,true,200,null,null,resultMap)){
					return resultMap;
				}
				//ProductDiscount 商品折扣 非必须
				if(false == ValidateUtils.checkParam(map,"ProductDiscount",ValidateUtils.TYPE_MONEY,true,20,null,null,resultMap)){
					return resultMap;
				}
				//ProductExpiredDate 商品有效期 非必须
				if(false == ValidateUtils.checkParam(map,"ProductExpiredDate",ValidateUtils.TYPE_INTEGER,true,5,null,null,resultMap)){
					return resultMap;
				}
			}
			//PaymentType 支付类型 必须设定1：农行卡支付 2：国际卡支付 3：农行贷记卡支付 5:基于第三方的跨行支付 A:支付方式合并 6：银联跨行支付，7:对公户  A:支付方式合并*必输
			if(false == ValidateUtils.checkParam(receiveMap,"PaymentType",ValidateUtils.TYPE_CHAR,false,1,new char[]{'1','2','3','4','5','6','7','A'},null,resultMap)){
				return resultMap;
			}
			//PaymentLinkType 交易渠道 必须设定，
			if(false == ValidateUtils.checkParam(receiveMap,"PaymentLinkType",ValidateUtils.TYPE_CHAR,false,1,new char[]{'1','2','3','4'},null,resultMap)){
				return resultMap;
			}
			String paymentType = ConvertUtils.getString(receiveMap.get("PaymentType")).trim();
			String paymentLinkType = ConvertUtils.getString(receiveMap.get("PaymentLinkType")).trim();
			//UnionPayLinkType 银联跨行移动支付接入方式 非必须，但是如果选择的支付帐户类型为6(银联跨行支付)交易渠道为2(手机网络接入)，必须设定
            //0：页面接入1：客户端接入（仅支持方式一，方式二不支持）
			if(AbcConstants.PAYMENTTYPE_6.equals(paymentType) && AbcConstants.PAYMENTLINKTYPE_PHONE.equals(paymentLinkType) ){
				if(false == ValidateUtils.checkParam(receiveMap,"UnionPayLinkType",ValidateUtils.TYPE_CHAR,false,1,new char[]{'0','1'},null,resultMap)){
					return resultMap;
				}
			}
			//ReceiveAccount 收款方账号 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"ReceiveAccount",ValidateUtils.TYPE_STRING,true,50,null,null,resultMap)){
				return resultMap;
			}
			//ReceiveAccName 收款方户名 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"ReceiveAccName",ValidateUtils.TYPE_CHINESE,true,50,null,null,resultMap)){
				return resultMap;
			}
			//NotifyType 通知方式 必须设定， 0：URL页面通知 1：服务器通知
			if(false == ValidateUtils.checkParam(receiveMap,"NotifyType",ValidateUtils.TYPE_CHAR,false,1,new char[]{'0','1'},null,resultMap)){
				return resultMap;
			}
			//MerchantRemarks 附言 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"MerchantRemarks",ValidateUtils.TYPE_CHINESE,true,200,null,null,resultMap)){
				return resultMap;
			}
			//IsBreakAccount 交易是否分账 必须设定，0:否；1:是
			if(false == ValidateUtils.checkParam(receiveMap,"IsBreakAccount",ValidateUtils.TYPE_CHAR,false,1,new char[]{'0','1'},null,resultMap)){
				return resultMap;
			}
			//SplitAccTemplate 分账模版编号 非必须
			if(false == ValidateUtils.checkParam(receiveMap,"SplitAccTemplate",ValidateUtils.TYPE_STRING,true,50,null,null,resultMap)){
				return resultMap;
			}
			//资金平台
			String fundId = ConvertUtils.getString(receiveMap.get("FundId")).trim();
			//商户顺序号
			int merNo = Integer.parseInt(AbcParaUtils.getMerNo(fundId));
			//生成订单对象
			PaymentRequest tPaymentRequest = new PaymentRequest();
			tPaymentRequest.dicOrder.put("PayTypeID", AbcConstants.PAYTYPEID_IMMEDIATEPAY);                   //设定交易类型
			tPaymentRequest.dicOrder.put("OrderDate", ConvertUtils.getString(receiveMap.get("OrderDate")).trim());                  //设定订单日期 （必要信息 - YYYY/MM/DD）
			tPaymentRequest.dicOrder.put("OrderTime", ConvertUtils.getString(receiveMap.get("OrderTime")).trim());                   //设定订单时间 （必要信息 - HH:MM:SS）
			tPaymentRequest.dicOrder.put("orderTimeoutDate", ConvertUtils.getString(receiveMap.get("OrderTimeoutDate")).trim());     //设定订单有效期
			tPaymentRequest.dicOrder.put("OrderNo", ConvertUtils.getString(receiveMap.get("OrderNo")).trim());                       //设定订单编号 （必要信息）
			tPaymentRequest.dicOrder.put("CurrencyCode", AbcConstants.CURRENCYCODE_CNY);             //设定交易币种
			tPaymentRequest.dicOrder.put("OrderAmount", ConvertUtils.getString(receiveMap.get("OrderAmount")).trim());      //设定交易金额
			tPaymentRequest.dicOrder.put("Fee",ConvertUtils.getString(receiveMap.get("Fee")).trim());                               //设定手续费金额
			tPaymentRequest.dicOrder.put("OrderDesc", ConvertUtils.getString(receiveMap.get("OrderDesc")).trim());                   //设定订单说明
			tPaymentRequest.dicOrder.put("OrderURL", ConvertUtils.getString(receiveMap.get("OrderURL")).trim());                     //设定订单地址
			tPaymentRequest.dicOrder.put("ReceiverAddress", ConvertUtils.getString(receiveMap.get("ReceiverAddress")).trim());       //收货地址
			tPaymentRequest.dicOrder.put("InstallmentMark", AbcConstants.INSTALLMENTMARK_NO);       //分期标识
			tPaymentRequest.dicOrder.put("CommodityType", ConvertUtils.getString(receiveMap.get("CommodityType")).trim());           //设置商品种类
			tPaymentRequest.dicOrder.put("BuyIP", ConvertUtils.getString(receiveMap.get("BuyIP")).trim());                           //IP
			tPaymentRequest.dicOrder.put("ExpiredDate", ConvertUtils.getString(receiveMap.get("ExpiredDate")).trim());               //设定订单保存时间
			//订单明细
			int i =0;
			for(Map<String,Object> map:detailList){
				i++;
				LinkedHashMap orderitem = new LinkedHashMap();
				orderitem.put("SubMerName", ConvertUtils.getString(map.get("SubMerName")).trim());    //设定二级商户名称
				orderitem.put("SubMerId", ConvertUtils.getString(map.get("SubMerId")).trim());    //设定二级商户代码
				orderitem.put("SubMerMCC", ConvertUtils.getString(map.get("SubMerMCC")).trim());   //设定二级商户MCC码 
				orderitem.put("SubMerchantRemarks", ConvertUtils.getString(map.get("SubMerchantRemarks")).trim());   //二级商户备注项
				orderitem.put("ProductID", ConvertUtils.getString(map.get("ProductID")).trim());//商品代码，预留字段
				orderitem.put("ProductName", ConvertUtils.getString(map.get("ProductName")).trim());//商品名称
				orderitem.put("UnitPrice", ConvertUtils.getString(map.get("UnitPrice")).trim());//商品总价
				orderitem.put("Qty", ConvertUtils.getString(map.get("Qty")).trim());//商品数量
				orderitem.put("ProductRemarks", ConvertUtils.getString(map.get("ProductRemarks")).trim()); //商品备注项
				orderitem.put("ProductType", ConvertUtils.getString(map.get("ProductType")).trim());//商品类型
				orderitem.put("ProductDiscount", ConvertUtils.getString(map.get("ProductDiscount")).trim());//商品折扣
				orderitem.put("ProductExpiredDate", ConvertUtils.getString(map.get("ProductExpiredDate")).trim());//商品有效期
				tPaymentRequest.orderitems.put(i, orderitem);
			}
            //生成支付请求对象
			tPaymentRequest.dicRequest.put("PaymentType", paymentType);            //设定支付类型                                       
			tPaymentRequest.dicRequest.put("PaymentLinkType", paymentLinkType);    //设定支付接入方式
			if (paymentType.equals(AbcConstants.PAYMENTTYPE_6) && paymentLinkType.equals(AbcConstants.PAYMENTLINKTYPE_PHONE)){
			    tPaymentRequest.dicRequest.put("UnionPayLinkType",ConvertUtils.getString(receiveMap.get("UnionPayLinkType")).trim());  //当支付类型为6，支付接入方式为2的条件满足时，需要设置银联跨行移动支付接入方式
			}
			tPaymentRequest.dicRequest.put("ReceiveAccount", ConvertUtils.getString(receiveMap.get("ReceiveAccount")).trim());      //设定收款方账号
			tPaymentRequest.dicRequest.put("ReceiveAccName", ConvertUtils.getString(receiveMap.get("ReceiveAccName")).trim());      //设定收款方户名
			tPaymentRequest.dicRequest.put("NotifyType", ConvertUtils.getString(receiveMap.get("NotifyType")).trim());              //设定通知方式
			tPaymentRequest.dicRequest.put("ResultNotifyURL", AbcParaUtils.getResultNotifyURL(fundId));    //设定通知URL地址
			tPaymentRequest.dicRequest.put("MerchantRemarks", ConvertUtils.getString(receiveMap.get("MerchantRemarks")).trim());    //设定附言
			tPaymentRequest.dicRequest.put("IsBreakAccount",ConvertUtils.getString(receiveMap.get("IsBreakAccount")).trim());      //设定交易是否分账
			tPaymentRequest.dicRequest.put("SplitAccTemplate", ConvertUtils.getString(receiveMap.get("SplitAccTemplate")).trim());  //分账模版编号        
			//传送支付请求并取得支付结果
//			JSON json = tPaymentRequest.postRequest();
			JSON json = tPaymentRequest.extendPostRequest(merNo);
            //判断支付结果状态，进行后续操作
			String returnCode = json.GetKeyValue("ReturnCode");
			String errorMessage = json.GetKeyValue("ErrorMessage");
			if (AbcConstants.RETURN_CODE_SUCCESS.equals(returnCode)){
				resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
	            returnMap.put("ReturnCode",returnCode);
	            returnMap.put("ErrorMessage",errorMessage);
	            returnMap.put("PaymentURL", json.GetKeyValue("PaymentURL"));
			}else{
				//支付请求失败
				resultMap.put("resultCode", returnCode);
	            resultMap.put("resultDesc", errorMessage);
	            returnMap.put("ReturnCode",returnCode);
	            returnMap.put("ErrorMessage",errorMessage);
			}
			//结果加入返回Map中
			resultMap.putAll(returnMap);
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		//记录返回结果
        logger.info(resultMap);
		return resultMap;
	}

}
