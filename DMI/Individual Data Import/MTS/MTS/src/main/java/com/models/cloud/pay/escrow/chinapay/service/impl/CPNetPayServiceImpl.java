package com.models.cloud.pay.escrow.chinapay.service.impl;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.chinapay.secss.SecssUtil;
import com.models.cloud.pay.common.http.model.HttpResponseBean;
import com.models.cloud.pay.common.http.utils.HttpUtils;
import com.models.cloud.pay.escrow.chinapay.service.CPNetPayService;
import com.models.cloud.pay.escrow.chinapay.utils.ChinaPayConstants;
import com.models.cloud.pay.escrow.chinapay.utils.ParaUtils;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

/**
 * 网址支付实现类
 */
@Service("cPNetPayServiceImpl")
public class CPNetPayServiceImpl implements CPNetPayService{
	
	//日志
	private static Logger logger = Logger.getLogger(CPNetPayServiceImpl.class);
		
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.chinapay.service.CPNetPayService#consumePayment(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumePayment(Map<String, Object> params) throws Exception {
		
		logger.info("##### consumePayment() #####");
		logger.info(params);
		//返回值
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		
		//校验入口参数是否合法
		try{
			//资金平台ID
			if(false == ValidateUtils.checkParam(params,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//订单号 MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			if(false == ValidateUtils.checkParam(params,"MerOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			if(false == ValidateUtils.checkParam(params,"TranDate",ValidateUtils.TYPE_DATE,false,8,null,"yyyyMMdd",resultMap)){
				return resultMap;
			}
			//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			if(false == ValidateUtils.checkParam(params,"TranTime",ValidateUtils.TYPE_DATE,false,6,null,"HHmmss",resultMap)){
				return resultMap;
			}
			//订单金额	OrderAmt	Y	N1..20	单位：分
			if(false == ValidateUtils.checkParam(params, "Amount", ValidateUtils.TYPE_MONEY, false, 20, null, null,resultMap)){
				return resultMap;
			}
			//商品信息	CommodityMsg	N	ANS0..128	用来描述购买商品的信息，ChinaPay原样返回
			if(false == ValidateUtils.checkParam(params,"CommodityMsg",ValidateUtils.TYPE_STRING,true,128,null,"[\u4e00-\u9fa5\\w]+",resultMap)){
				return resultMap;
			}
			//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			if(false == ValidateUtils.checkParam(params,"MerResv",ValidateUtils.TYPE_STRING,true,1024,null,"[\u4e00-\u9fa5\\w]+",resultMap)){
				return resultMap;
			}
			String fundId = ConvertUtils.getString(params.get("FundId")).trim(); //资金平台ID
			String version = ParaUtils.getVersion(fundId);//20140728" ; //版本号 Version	Y AN8	固定值：20140728
//			String accessType = "0";//接入类型	AccessType	N N1	0：商户身份接入（默认） 1：机构身份接入
			String merId = ParaUtils.getMerId(fundId);//"000001608030876";//商户号	MerId	Y	N15	由ChinaPay分配的15位定长数字，用于确认商户身份
			String merOrderNo = ConvertUtils.getString(params.get("MerOrderNo")).trim();//商户订单号	MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			String url = ParaUtils.getConsumeTradeUrl(fundId);//"http://newpayment-test.chinapay.com/CTITS/service/rest/page/nref/000000000017/0/0/0/0/0";
			String tranDate = ConvertUtils.getString(params.get("TranDate")).trim();//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			String tranTime = ConvertUtils.getString(params.get("TranTime")).trim();//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			String orderAmt =ConvertUtils.getString((int)(Double.parseDouble(ConvertUtils.getString(params.get("Amount")).trim())*100));//订单金额	OrderAmt	Y	N1..20	单位：分
			String busiType = ParaUtils.getBusiType(fundId);//"0001";//业务类型	BusiType	Y	N4	固定值：0001
//			String curryNo = "CNY";//交易币种	CurryNo	N	A3	默认为人民币：CNY
			String merPageUrl = ParaUtils.getConsumeTradeMerpageurl(fundId);//"www.sina.com";//商户前台通知地址	MerPageUrl	N	ANS0..256	页面接受应答地址，用于引导使用者支付后返回商户网站页面
			String merBgUrl = ParaUtils.getConsumeTradeMerbgurl(fundId);//"www.baidu.com"; //商户后台通知地址	MerBgUrl	Y	ANS0..256	商户后台交易应答接收地址，ChinaPay会根据后台商户响应来判定是否重新发送后台应答流水，以确保后台应答的接收
			String commodityMsg =ConvertUtils.getString(params.get("CommodityMsg")).trim();//商品信息	CommodityMsg	N	ANS0..128	用来描述购买商品的信息，ChinaPay原样返回
			String merResv = ConvertUtils.getString(params.get("MerResv")).trim();//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			String signature ="";//签名	Signature	Y		商户报文签名信息，报文中的所有字段都参与签名（Signature除外）
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("Version", version);
//			map.put("AccessType", accessType);
			map.put("MerId", merId);
			map.put("MerOrderNo", merOrderNo);
			map.put("TranDate", tranDate);
			map.put("TranTime", tranTime);
			map.put("OrderAmt", orderAmt);
			map.put("BusiType", busiType);
//			map.put("CurryNo", curryNo);
			map.put("MerPageUrl", merPageUrl);
			map.put("MerBgUrl", merBgUrl);
			map.put("CommodityMsg", commodityMsg);
			map.put("MerResv", merResv);
			//签名
			SecssUtil secssUtil = new SecssUtil();
			if( false == secssUtil.init()){
				logger.error(secssUtil.getErrCode() + secssUtil.getErrMsg());
				resultMap.put("resultCode", secssUtil.getErrCode());
				resultMap.put("resultDesc", secssUtil.getErrMsg());
				return resultMap;
			}
			secssUtil.sign(map);
			signature = secssUtil.getSign();
			map.put("Signature", signature);
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			resultMap.put("url",url);
			resultMap.put("params", JSON.toJSON(map));
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		
		return resultMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.chinapay.service.CPNetPayService#consumeSplitPayment(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumeSplitPayment(Map<String, Object> params) throws Exception {
		logger.info("##### consumeSplitPayment() #####");
		logger.info(params);
		//返回值
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		
		//校验入口参数是否合法
		try{
			//资金平台ID
			if(false == ValidateUtils.checkParam(params,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//订单号 MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			if(false == ValidateUtils.checkParam(params,"MerOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			if(false == ValidateUtils.checkParam(params,"TranDate",ValidateUtils.TYPE_DATE,false,8,null,"yyyyMMdd",resultMap)){
				return resultMap;
			}
			//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			if(false == ValidateUtils.checkParam(params,"TranTime",ValidateUtils.TYPE_DATE,false,6,null,"HHmmss",resultMap)){
				return resultMap;
			}
			//订单金额	OrderAmt	Y	N1..20	单位：分
			if(false == ValidateUtils.checkParam(params, "Amount", ValidateUtils.TYPE_MONEY, false, 20, null, null,resultMap)){
				return resultMap;
			}
			//商品信息	CommodityMsg	N	ANS0..128	用来描述购买商品的信息，ChinaPay原样返回
			if(false == ValidateUtils.checkParam(params,"CommodityMsg",ValidateUtils.TYPE_STRING,true,128,null,"[\u4e00-\u9fa5\\w]+",resultMap)){
				return resultMap;
			}
			//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			if(false == ValidateUtils.checkParam(params,"MerResv",ValidateUtils.TYPE_STRING,true,1024,null,"[\u4e00-\u9fa5\\w]+",resultMap)){
				return resultMap;
			}
			//分账商户号
			if(false == ValidateUtils.checkParam(params,"MerSplitMsg",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			
			String fundId = ConvertUtils.getString(params.get("FundId")).trim(); //资金平台ID
			String version = ParaUtils.getVersion(fundId);//20140728" ; //版本号 Version	Y AN8	固定值：20140728
			String merId = ParaUtils.getMerId(fundId);//"000001608030876";//商户号	MerId	Y	N15	由ChinaPay分配的15位定长数字，用于确认商户身份
			String merOrderNo = ConvertUtils.getString(params.get("MerOrderNo")).trim();//商户订单号	MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			String url = ParaUtils.getConsumeTradeUrl(fundId);//"http://newpayment-test.chinapay.com/CTITS/service/rest/page/nref/000000000017/0/0/0/0/0";	
			String tranDate = ConvertUtils.getString(params.get("TranDate")).trim();//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			String tranTime = ConvertUtils.getString(params.get("TranTime")).trim();//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			String orderAmt =ConvertUtils.getString((int)(Double.parseDouble(ConvertUtils.getString(params.get("Amount")).trim())*100));//订单金额	OrderAmt	Y	N1..20	单位：分
			String busiType = ParaUtils.getBusiType(fundId);//"0001";//业务类型	BusiType	Y	N4	固定值：0001
			String merPageUrl = ParaUtils.getConsumeTradeMerpageurl(fundId);//"www.sina.com";//商户前台通知地址	MerPageUrl	N	ANS0..256	页面接受应答地址，用于引导使用者支付后返回商户网站页面
			String merBgUrl = ParaUtils.getConsumeTradeMerbgurl(fundId);//"www.baidu.com"; //商户后台通知地址	MerBgUrl	Y	ANS0..256	商户后台交易应答接收地址，ChinaPay会根据后台商户响应来判定是否重新发送后台应答流水，以确保后台应答的接收
			String commodityMsg =ConvertUtils.getString(params.get("CommodityMsg")).trim();//商品信息	CommodityMsg	N	ANS0..128	用来描述购买商品的信息，ChinaPay原样返回
			String merResv = ConvertUtils.getString(params.get("MerResv")).trim();//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			String splitType = ParaUtils.getSplitType(fundId); //分账类型 分账类型	SplitType	N	N4	不分账不填写此域；如需要分账，填写格式如下：0001：实时分账 0002：延时分账
			String splitMethod = ChinaPayConstants.SPLIT_METHOD_RATIO; //比例模式分账
			String merSplitMsg = ConvertUtils.getString(params.get("MerSplitMsg")).trim() + "^100" ; 
			String signature ="";//签名	Signature	Y		商户报文签名信息，报文中的所有字段都参与签名（Signature除外）
		
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("Version", version);
			map.put("MerId", merId);
			map.put("MerOrderNo", merOrderNo);
			map.put("TranDate", tranDate);
			map.put("TranTime", tranTime);
			map.put("OrderAmt", orderAmt);
			map.put("BusiType", busiType);
			map.put("MerPageUrl", merPageUrl);
			map.put("MerBgUrl", merBgUrl);
			map.put("CommodityMsg", commodityMsg);
			map.put("MerResv", merResv);
			map.put("SplitType",splitType );
			map.put("SplitMethod",splitMethod );
			map.put("MerSplitMsg",merSplitMsg );
			//签名
			SecssUtil secssUtil = new SecssUtil();
			if( false == secssUtil.init()){
				logger.error(secssUtil.getErrCode() + secssUtil.getErrMsg());
				resultMap.put("resultCode", secssUtil.getErrCode());
				resultMap.put("resultDesc", secssUtil.getErrMsg());
				return resultMap;
			}
			secssUtil.sign(map);
			signature = secssUtil.getSign();
			map.put("Signature", signature);
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
			resultMap.put("url",url);
			resultMap.put("params", JSON.toJSONString(map));
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		
		return resultMap;
	}


	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.chinapay.service.CPNetPayService#consumePaymentQuery(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumePaymentQuery(Map<String, Object> params) throws Exception {
		logger.info("##### consumePaymentQuery() #####");
		logger.info(params);
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			//校验入口参数是否合法
			//资金平台ID
			if(false == ValidateUtils.checkParam(params,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//订单号 MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			if(false == ValidateUtils.checkParam(params,"MerOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//商户交易日期	TranDate	Y	N8	格式：yyyyMMdd
			if(false == ValidateUtils.checkParam(params,"TranDate",ValidateUtils.TYPE_DATE,false,8,null,"yyyyMMdd",resultMap)){
				return resultMap;
			}
			String fundId = ConvertUtils.getString(params.get("FundId")).trim();
			String version = ParaUtils.getVersion(fundId);//"20140728" ; //版本号 Version	Y AN8	固定值：20140728
			String merId = ParaUtils.getMerId(fundId);//"000001608030876";//商户号	MerId	Y	N15	由ChinaPay分配的15位定长数字，用于确认商户身份
			String merOrderNo = ConvertUtils.getString(params.get("MerOrderNo")).trim();//商户订单号	MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			String tranDate =  ConvertUtils.getString(params.get("TranDate")).trim(); //商户交易日期	TranDate	Y	N8	格式：yyyyMMdd
			String tranType=ParaUtils.getTradeQueryTranType(fundId);//"0502";//交易类型	TranType	Y	N4	0502	
			String busiType = ParaUtils.getBusiType(fundId);//"0001";//业务类型	BusiType	Y	N4	固定值：0001
			//交易日期
			String url = ParaUtils.getTradeQueryUrl(fundId);//"http://newpayment-test.chinapay.com/CTITS/service/rest/forward/syn/000000000060/0/0/0/0/0";
			String signature ="";//签名	Signature	Y		商户报文签名信息，报文中的所有字段都参与签名（Signature除外）
			map.put("Version", version);
			map.put("MerId", merId);
			map.put("MerOrderNo", merOrderNo);
			map.put("TranDate", tranDate);
			map.put("TranType", tranType);
			map.put("BusiType", busiType);
			//签名
			SecssUtil secssUtil = new SecssUtil();
			if( false == secssUtil.init()){
				resultMap.put("resultCode", secssUtil.getErrCode());
				resultMap.put("resultDesc", secssUtil.getErrMsg());
				logger.error(secssUtil.getErrCode() + secssUtil.getErrMsg());
				return resultMap;
			}
			secssUtil.sign(map);
			signature = secssUtil.getSign();
			map.put("Signature", signature);
			//Http请求
			HttpResponseBean bean = HttpUtils.httpPost1(url,map,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
			//返回字符串
			String returnStr = bean.getEntityContent();
			//返回值转换为Map
			if(StringUtils.isBlank(returnStr)){
				resultMap.put("resultCode", Hint.SP_14102_PARAMS_INVALID.getCodeString());
	        	resultMap.put("resultDesc", Hint.SP_14102_PARAMS_INVALID.getMessage());
				return resultMap;
			}
			Map<String,Object> returnMap = convert2Map(returnStr);
			//校验返回值
			if(null != returnMap && null != returnMap.get("respCode")){
				String respCode =  ConvertUtils.getString(returnMap.get("respCode")).trim(); 
				if(false == "0000".equals(respCode)){
					//若查询失败，只返回应答码和应答信息
					resultMap.put("resultCode", respCode);
		        	resultMap.put("resultDesc", ConvertUtils.getString(returnMap.get("respMsg")).trim());
		        	resultMap.putAll(returnMap);
		        	return resultMap;
				}
			}else{
				//没有返回数据
				resultMap.put("resultCode", Hint.SP_14103_NO_DATA_RETURN.getCodeString());
	        	resultMap.put("resultDesc", Hint.SP_14103_NO_DATA_RETURN.getMessage());
				return resultMap;
			}
			//验签
			secssUtil.verify(returnMap);
			if(!"00".equals(secssUtil.getErrCode())){
				resultMap.put("resultCode", Hint.SP_14107_CHECK_SIGN_FAIL.getCodeString());
	        	resultMap.put("resultDesc", Hint.SP_14107_CHECK_SIGN_FAIL.getMessage());
				return resultMap;
			}
			returnMap.remove("Signature");
			resultMap.putAll(returnMap);
			//获取返回值respCode
			String respCode = ConvertUtils.getString(returnMap.get("respCode")).trim();
			//转换返回值 0000：成功 1003：商户已审核
			if(respCode.equals("0000")){
				respCode = Hint.SYS_SUCCESS.getCodeString();
			}
			resultMap.put("resultCode",respCode);
			resultMap.put("resultDesc",returnMap.get("respMsg"));
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		
		return resultMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.chinapay.service.CPNetPayService#consumePaymentRefund(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumePaymentRefund(Map<String, Object> params) throws Exception {
		logger.info("##### consumePaymentRefund() #####");
		logger.info(params);
		//返回值
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//校验入口参数是否合法
			//资金平台ID
			if(false == ValidateUtils.checkParam(params,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//订单号 MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			if(false == ValidateUtils.checkParam(params,"MerOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			if(false == ValidateUtils.checkParam(params,"TranDate",ValidateUtils.TYPE_DATE,false,8,null,"yyyyMMdd",resultMap)){
				return resultMap;
			}
			//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			if(false == ValidateUtils.checkParam(params,"TranTime",ValidateUtils.TYPE_DATE,false,6,null,"HHmmss",resultMap)){
				return resultMap;
			}
			//商户交易订单号	OriOrderNo	Y	AN1..32	原始交易订单号
			if(false == ValidateUtils.checkParam(params,"OriOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//原始商户交易日期		Y	N8	商户原始支付交易日期
			if(false == ValidateUtils.checkParam(params,"OriTranDate",ValidateUtils.TYPE_DATE,false,8,null,"yyyyMMdd",resultMap)){
				return resultMap;
			}
			//退款订单金额	RefundAmt	Y	N1..20	单位：分
			if(false == ValidateUtils.checkParam(params, "RefundAmt", ValidateUtils.TYPE_MONEY, false, 20, null, null,resultMap)){
				return resultMap;
			}
//			//商品信息	CommodityMsg	N	ANS0..128	用来描述购买商品的信息，ChinaPay原样返回
//			if(false == ValidateUtils.checkParam(params,"CommodityMsg",ValidateUtils.TYPE_STRING,true,128,null,null,resultMap)){
//				return resultMap;
//			}
			//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			if(false == ValidateUtils.checkParam(params,"MerResv",ValidateUtils.TYPE_STRING,true,1024,null,null,resultMap)){
				return resultMap;
			}
			String fundId = ConvertUtils.getString(params.get("FundId")).trim();
			String version = ParaUtils.getVersion(fundId);//"20140728" ; //版本号 Version	Y AN8	固定值：20140728
//			String accessType = "0";//接入类型	AccessType	N N1	0：商户身份接入（默认） 1：机构身份接入
			String merId = ParaUtils.getMerId(fundId);//"000001608030876";//商户号	MerId	Y	N15	由ChinaPay分配的15位定长数字，用于确认商户身份
			String merOrderNo = ConvertUtils.getString(params.get("MerOrderNo")).trim();//商户订单号	MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			String url =ParaUtils.getContinueTradeUrl(fundId);// "http://newpayment-test.chinapay.com/CTITS/service/rest/forward/syn/000000000065/0/0/0/0/0";
			String tranDate = ConvertUtils.getString(params.get("TranDate")).trim();//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			String tranTime = ConvertUtils.getString(params.get("TranTime")).trim();//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			String oriOrderNo = ConvertUtils.getString(params.get("OriOrderNo")).trim();//商户交易订单号	OriOrderNo	Y	AN1..32	原始交易订单号
			String oriTranDate = ConvertUtils.getString(params.get("OriTranDate")).trim();//原始商户交易日期		Y	N8	商户原始支付交易日期 
			String refundAmt=ConvertUtils.getString((int)(Double.parseDouble(ConvertUtils.getString(params.get("RefundAmt")).trim())*100)); //退款订单金额	RefundAmt	Y	N1..20	单位：分
			String tranType = ParaUtils.getContinueTradeRefundTranType(fundId);//"0401";//交易类型	TranType	N	N4	前台页面方式提交交易：商户可以不填此域，ChinaPay会根据商户交易配置在持卡人页面显示商户开通的交易类型，由持卡人选择商户已开通的交易类型完成支付
			String busiType = ParaUtils.getBusiType(fundId);//"0001";//业务类型	BusiType	Y	N4	固定值：0001
//			String curryNo = "CNY";//交易币种	CurryNo	N	A3	默认为人民币：CNY
			String merBgUrl = ParaUtils.getContinueTradeMerbgurl(fundId);//"www.baidu.com"; //商户后台通知地址	MerBgUrl	Y	ANS0..256	商户后台交易应答接收地址，ChinaPay会根据后台商户响应来判定是否重新发送后台应答流水，以确保后台应答的接收
//			String commodityMsg =ConvertUtils.getString(params.get("CommodityMsg")).trim();//商品信息	CommodityMsg	N	ANS0..128	用来描述购买商品的信息，ChinaPay原样返回
			String merResv = ConvertUtils.getString(params.get("MerResv")).trim();//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			String signature ="";//签名	Signature	Y		商户报文签名信息，报文中的所有字段都参与签名（Signature除外）
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("Version", version);
//			map.put("AccessType", accessType);
			map.put("MerId", merId);
			map.put("MerOrderNo", merOrderNo);
			map.put("TranDate", tranDate);
			map.put("TranTime", tranTime);
			map.put("OriOrderNo", oriOrderNo);
			map.put("OriTranDate", oriTranDate);
			map.put("RefundAmt", refundAmt);
			map.put("TranType", tranType);
			map.put("BusiType", busiType);
//			map.put("CurryNo", curryNo);
			map.put("MerBgUrl", merBgUrl);
//			map.put("CommodityMsg", commodityMsg);
			map.put("MerResv", merResv);
			//签名
			SecssUtil secssUtil = new SecssUtil();
			if( false == secssUtil.init()){
				resultMap.put("resultCode", secssUtil.getErrCode());
				resultMap.put("resultDesc", secssUtil.getErrMsg());
				logger.error(secssUtil.getErrCode() + secssUtil.getErrMsg());
				return resultMap;
			}
			secssUtil.sign(map);
			signature = secssUtil.getSign();
			map.put("Signature", signature);
			//HTTP请求
			HttpResponseBean bean = HttpUtils.httpPost1(url,map,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
			//返回字符串
			String returnStr = bean.getEntityContent();
			if(StringUtils.isBlank(returnStr)){
				resultMap.put("resultCode", Hint.SP_14102_PARAMS_INVALID.getCodeString());
	        	resultMap.put("resultDesc", Hint.SP_14102_PARAMS_INVALID.getMessage());
				return resultMap;
			}
			//返回值转换为Map
			Map<String,Object> returnMap = convert2Map(returnStr);
			//验签
			secssUtil.verify(returnMap);
			if(!"00".equals(secssUtil.getErrCode())){
				resultMap.put("resultCode", Hint.SP_14107_CHECK_SIGN_FAIL.getCodeString());
	        	resultMap.put("resultDesc", Hint.SP_14107_CHECK_SIGN_FAIL.getMessage());
				return resultMap;
			}
			returnMap.remove("Signature");
			resultMap.putAll(returnMap);
			String respCode = ConvertUtils.getString(returnMap.get("respCode")).trim();
			//转换返回值 0000：成功 1003：商户已审核
			if(respCode.equals("0000") || respCode.equals("1003")){
				respCode = Hint.SYS_SUCCESS.getCodeString();
			}
			resultMap.put("resultCode",respCode);
			resultMap.put("resultDesc",returnMap.get("respMsg"));
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		
		return resultMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.chinapay.service.CPNetPayService#consumeSplitPaymentRefund(java.util.Map)
	 */
	@Override
	public Map<String, Object> consumeSplitPaymentRefund( Map<String, Object> params) throws Exception {
		logger.info("##### consumePaymentRefund() #####");
		logger.info(params);
		//返回值
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			//校验入口参数是否合法
			//资金平台ID
			if(false == ValidateUtils.checkParam(params,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//订单号 MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			if(false == ValidateUtils.checkParam(params,"MerOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			if(false == ValidateUtils.checkParam(params,"TranDate",ValidateUtils.TYPE_DATE,false,8,null,"yyyyMMdd",resultMap)){
				return resultMap;
			}
			//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			if(false == ValidateUtils.checkParam(params,"TranTime",ValidateUtils.TYPE_DATE,false,6,null,"HHmmss",resultMap)){
				return resultMap;
			}
			//商户交易订单号	OriOrderNo	Y	AN1..32	原始交易订单号
			if(false == ValidateUtils.checkParam(params,"OriOrderNo",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//原始商户交易日期		Y	N8	商户原始支付交易日期
			if(false == ValidateUtils.checkParam(params,"OriTranDate",ValidateUtils.TYPE_DATE,false,8,null,"yyyyMMdd",resultMap)){
				return resultMap;
			}
			//退款订单金额	RefundAmt	Y	N1..20	单位：分
			if(false == ValidateUtils.checkParam(params, "RefundAmt", ValidateUtils.TYPE_MONEY, false, 20, null, null,resultMap)){
				return resultMap;
			}
			//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			if(false == ValidateUtils.checkParam(params,"MerResv",ValidateUtils.TYPE_STRING,true,1024,null,null,resultMap)){
				return resultMap;
			}
			//分账商户号
			if(false == ValidateUtils.checkParam(params,"MerSplitMsg",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			String fundId = ConvertUtils.getString(params.get("FundId")).trim();
			String version = ParaUtils.getVersion(fundId);//"20140728" ; //版本号 Version	Y AN8	固定值：20140728
			String merId = ParaUtils.getMerId(fundId);//"000001608030876";//商户号	MerId	Y	N15	由ChinaPay分配的15位定长数字，用于确认商户身份
			String merOrderNo = ConvertUtils.getString(params.get("MerOrderNo")).trim();//商户订单号	MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
			String url =ParaUtils.getContinueTradeUrl(fundId);// "http://newpayment-test.chinapay.com/CTITS/service/rest/forward/syn/000000000065/0/0/0/0/0";
			String tranDate = ConvertUtils.getString(params.get("TranDate")).trim();//商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
			String tranTime = ConvertUtils.getString(params.get("TranTime")).trim();//商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
			String oriOrderNo = ConvertUtils.getString(params.get("OriOrderNo")).trim();//商户交易订单号	OriOrderNo	Y	AN1..32	原始交易订单号
			String oriTranDate = ConvertUtils.getString(params.get("OriTranDate")).trim();//原始商户交易日期		Y	N8	商户原始支付交易日期 
			String refundAmt=ConvertUtils.getString((int)(Double.parseDouble(ConvertUtils.getString(params.get("RefundAmt")).trim())*100)); //退款订单金额	RefundAmt	Y	N1..20	单位：分
			String tranType = ParaUtils.getContinueTradeRefundTranType(fundId);//"0401";//交易类型	TranType	N	N4	前台页面方式提交交易：商户可以不填此域，ChinaPay会根据商户交易配置在持卡人页面显示商户开通的交易类型，由持卡人选择商户已开通的交易类型完成支付
			String busiType = ParaUtils.getBusiType(fundId);//"0001";//业务类型	BusiType	Y	N4	固定值：0001
			String merBgUrl = ParaUtils.getContinueTradeMerbgurl(fundId);//"www.baidu.com"; //商户后台通知地址	MerBgUrl	Y	ANS0..256	商户后台交易应答接收地址，ChinaPay会根据后台商户响应来判定是否重新发送后台应答流水，以确保后台应答的接收
			String merResv = ConvertUtils.getString(params.get("MerResv")).trim();//商户私有域	MerResv	N	ANS0..1024	商户自定义，ChinaPay原样返回
			String splitType = ParaUtils.getSplitType(fundId); //分账类型 分账类型	SplitType	N	N4	不分账不填写此域；如需要分账，填写格式如下：0001：实时分账 0002：延时分账
			String splitMethod = ChinaPayConstants.SPLIT_METHOD_RATIO; //比例模式分账
			String merSplitMsg = ConvertUtils.getString(params.get("MerSplitMsg")).trim() + "^100" ; 
			String signature ="";//签名	Signature	Y		商户报文签名信息，报文中的所有字段都参与签名（Signature除外）
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("Version", version);
			map.put("MerId", merId);
			map.put("MerOrderNo", merOrderNo);
			map.put("TranDate", tranDate);
			map.put("TranTime", tranTime);
			map.put("OriOrderNo", oriOrderNo);
			map.put("OriTranDate", oriTranDate);
			map.put("RefundAmt", refundAmt);
			map.put("TranType", tranType);
			map.put("BusiType", busiType);
//			map.put("CurryNo", curryNo);
			map.put("MerBgUrl", merBgUrl);
			map.put("SplitType",splitType );
			map.put("SplitMethod",splitMethod );
			map.put("MerSplitMsg",merSplitMsg );
			map.put("MerResv", merResv);
			//签名
			SecssUtil secssUtil = new SecssUtil();
			if( false == secssUtil.init()){
				resultMap.put("resultCode", secssUtil.getErrCode());
				resultMap.put("resultDesc", secssUtil.getErrMsg());
				logger.error(secssUtil.getErrCode() + secssUtil.getErrMsg());
				return resultMap;
			}
			secssUtil.sign(map);
			signature = secssUtil.getSign();
			map.put("Signature", signature);
			//HTTP请求
			HttpResponseBean bean = HttpUtils.httpPost1(url,map,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
			//返回字符串
			String returnStr = bean.getEntityContent();
			if(StringUtils.isBlank(returnStr)){
				resultMap.put("resultCode", Hint.SP_14102_PARAMS_INVALID.getCodeString());
	        	resultMap.put("resultDesc", Hint.SP_14102_PARAMS_INVALID.getMessage());
				return resultMap;
			}
			//返回值转换为Map
			Map<String,Object> returnMap = convert2Map(returnStr);
			//验签
			secssUtil.verify(returnMap);
			if(!"00".equals(secssUtil.getErrCode())){
				resultMap.put("resultCode", Hint.SP_14107_CHECK_SIGN_FAIL.getCodeString());
	        	resultMap.put("resultDesc", Hint.SP_14107_CHECK_SIGN_FAIL.getMessage());
				return resultMap;
			}
			returnMap.remove("Signature");
			resultMap.putAll(returnMap);
			String respCode = ConvertUtils.getString(returnMap.get("respCode")).trim();
			//转换返回值 0000：成功 1003：商户已审核
			if(respCode.equals("0000") || respCode.equals("1003")){
				respCode = Hint.SYS_SUCCESS.getCodeString();
			}
			resultMap.put("resultCode",respCode);
			resultMap.put("resultDesc",returnMap.get("respMsg"));
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		
		return resultMap;
	}

	//URL参数转换为Map
	private Map<String,Object> convert2Map(String params) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		if(StringUtils.isBlank(params)){
			logger.info("convert2Map params为空");
			return result;
		}
		logger.info("URLconvert2Map_Params:" + params);
		String[] paramsArray = params.split("&");
		for(int i=0;i<paramsArray.length;i++){
			String[] array = paramsArray[i].split("=");
			if(array.length ==2){
				result.put(array[0], array[1]);
			}else if(array.length ==1){
				result.put(array[0], "");
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.chinapay.service.CPNetPayService#validateSign(java.lang.String)
	 */
	@Override
	public Map<String, Object> getNotifyData(String params) throws Exception {
		logger.info("##### getNotifyData() #####");
		logger.info(params);
		//返回值
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//参数校验
		if(StringUtils.isBlank(params)){
			resultMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
			return resultMap;
		}
		SecssUtil secssUtil = new SecssUtil();
		if(false == secssUtil.init()){
			resultMap.put("resultCode", secssUtil.getErrCode());
			resultMap.put("resultDesc", secssUtil.getErrMsg());
			logger.error(secssUtil.getErrCode() + secssUtil.getErrMsg());
			return resultMap;
		}
		//返回值转换为Map
		Map<String,Object> returnMap = convert2Map(params);
		//验签
		secssUtil.verify(returnMap);
		if(!"00".equals(secssUtil.getErrCode())){
			resultMap.put("resultCode", Hint.SP_14107_CHECK_SIGN_FAIL.getCodeString());
        	resultMap.put("resultDesc", Hint.SP_14107_CHECK_SIGN_FAIL.getMessage());
			return resultMap;
		}
		returnMap.remove("Signature");
		resultMap.putAll(returnMap);
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	
	/**
	 * 
	 */
	@Override
	public Map<String, Object> getNotifyData(Map<String, Object> returnMap) throws Exception {
		logger.info("##### getNotifyData() #####");
		logger.info(returnMap);
		//返回值
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//参数校验
		if(null == returnMap || 0== returnMap.size()){
			resultMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
			resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
			return resultMap;
		}
		//格式化中文字符
		//签名
		if(null != returnMap.get("Signature")){
			String tmp = URLDecoder.decode(ConvertUtils.getString(returnMap.get("Signature")).trim(),"UTF-8");
			returnMap.put("Signature", tmp);
		}
		//商品的信息
		if(null != returnMap.get("CommodityMsg")){
			String tmp = URLDecoder.decode(ConvertUtils.getString(returnMap.get("CommodityMsg")).trim(),"UTF-8");
			returnMap.put("CommodityMsg", tmp);
		}
		//商户私有域
		if(null != returnMap.get("MerResv")){
			String tmp = URLDecoder.decode(ConvertUtils.getString(returnMap.get("MerResv")).trim(),"UTF-8");
			returnMap.put("MerResv", tmp);
		}
		//商户订单分账信息
		if(null != returnMap.get("MerSplitMsg")){
			String tmp = URLDecoder.decode(ConvertUtils.getString(returnMap.get("MerSplitMsg")).trim(),"UTF-8");
			returnMap.put("MerSplitMsg", tmp);
		}
		//交易扩展域	
		if(null != returnMap.get("TranReserved")){
			String tmp = URLDecoder.decode(ConvertUtils.getString(returnMap.get("TranReserved")).trim(),"UTF-8");
			returnMap.put("TranReserved", tmp);
		}
		SecssUtil secssUtil = new SecssUtil();
		if(false == secssUtil.init()){
			resultMap.put("resultCode", secssUtil.getErrCode());
			resultMap.put("resultDesc", secssUtil.getErrMsg());
			logger.error(secssUtil.getErrCode() + secssUtil.getErrMsg());
			return resultMap;
		}
		//验签
		secssUtil.verify(returnMap);
		if(!"00".equals(secssUtil.getErrCode())){
			resultMap.put("resultCode", Hint.SP_14107_CHECK_SIGN_FAIL.getCodeString());
        	resultMap.put("resultDesc", Hint.SP_14107_CHECK_SIGN_FAIL.getMessage());
			return resultMap;
		}
		returnMap.remove("Signature");
		resultMap.putAll(returnMap);
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
		resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
		return resultMap;
	}

	
	



	
	
}
