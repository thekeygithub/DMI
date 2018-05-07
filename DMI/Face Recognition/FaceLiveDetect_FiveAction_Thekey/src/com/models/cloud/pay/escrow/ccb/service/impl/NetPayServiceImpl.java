package com.models.cloud.pay.escrow.ccb.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.models.cloud.pay.escrow.ccb.service.NetPayService;
import com.models.cloud.pay.escrow.ccb.utils.CcbConstants;
import com.models.cloud.pay.escrow.ccb.utils.ParaUtils;
import com.models.cloud.pay.escrow.ccb.utils.TxCodeEnum;
import com.models.cloud.util.ConvertUtils;
import com.models.cloud.util.ValidateUtils;
import com.models.cloud.util.hint.Hint;

import CCBSign.RSASig;


/**
 * 网上支付接口实现类
 */
@Service("netPayServiceImpl")
public class NetPayServiceImpl implements NetPayService {
	
	//日志
	private static Logger logger = Logger.getLogger(NetPayServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.ccb.payment.netpay.service.NetPayService#antiPhishingPayment(java.util.Map)
	 */
	@Override
	public Map<String, Object> antiPhishingPayment(Map<String, Object> receiveMap) throws Exception {
		logger.info("##### antiPhishingPayment() #####");
		logger.info(receiveMap);
		//返回值
		Map<String, Object> resultMap	= new HashMap<String, Object>();
		
		try{	
			//校验入口参数是否合法
			//资金平台ID
			if(false == ValidateUtils.checkParam(receiveMap,"FundId",ValidateUtils.TYPE_STRING,false,32,null,null,resultMap)){
				return resultMap;
			}
			//订单号
			if(ValidateUtils.isBlank(receiveMap, "orderId") ){
				resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "orderId"));
			}
			if(false == ValidateUtils.isRegex(receiveMap.get("orderId").toString().trim(),"[A-Za-z0-9_]+")
					|| false == ValidateUtils.checkLength(receiveMap.get("orderId").toString().trim(), 1, 30)){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "orderId"));
			}
			//付款金额
			if(ValidateUtils.isBlank(receiveMap, "payment") ){
				resultMap.put("resultCode", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_10004_PARAM_NOT_NULL_ERROR.getMessage().replace("{param}", "payment"));
			}
			if(!ValidateUtils.isMoney(receiveMap.get("payment").toString(), false)
					|| false == ValidateUtils.checkLength(receiveMap.get("payment").toString().trim(), 1, 15)){
				resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
	            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "payment"));
			}
			//网关类型
			if(false == ValidateUtils.isBlank(receiveMap, "gateway")){
				if(false == ValidateUtils.isRegex(receiveMap.get("gateway").toString().trim(),"[A-Za-z0-9_]+")
						|| false == ValidateUtils.checkLength(receiveMap.get("gateway").toString().trim(), 0, 100)){
					resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
		            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "gateway"));
				}
			}
			//客户端IP
			if(false == ValidateUtils.isBlank(receiveMap, "clientIp")){
				if(false == ValidateUtils.checkIP(receiveMap.get("clientIp").toString().trim())){
					resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
		            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "clientIp"));
				}
			}
			//客户注册信息
			if(false == ValidateUtils.isBlank(receiveMap, "regInfo")){
				if(false == ValidateUtils.isRegex(receiveMap.get("regInfo").toString().trim(),"[\u4e00-\u9fa5\\w]+")
						|| false == ValidateUtils.checkLength(receiveMap.get("regInfo").toString().trim(), 0, 200)){
					resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
		            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "regInfo"));
				}
			}
			//商品信息
			if(false == ValidateUtils.isBlank(receiveMap, "proInfo")){
				if(false == ValidateUtils.isRegex(receiveMap.get("proInfo").toString().trim(),"[\u4e00-\u9fa5\\w]+")
						|| false == ValidateUtils.checkLength(receiveMap.get("proInfo").toString().trim(), 0, 200)){
					resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
		            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "proInfo"));
				}
			}
			//备注1
			if(false == ValidateUtils.isBlank(receiveMap, "remark1")){
				if(false == ValidateUtils.isRegex(receiveMap.get("remark1").toString().trim(),"[A-Za-z0-9_]+")
						|| false == ValidateUtils.checkLength(receiveMap.get("remark1").toString().trim(), 0, 200)){
					resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
		            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "remark1"));
				}
			}
			//备注2
			if(false == ValidateUtils.isBlank(receiveMap, "remark2")){
				if(false == ValidateUtils.isRegex(receiveMap.get("remark2").toString().trim(),"[A-Za-z0-9_]+")
						|| false == ValidateUtils.checkLength(receiveMap.get("remark2").toString().trim(), 0, 200)){
					resultMap.put("resultCode", Hint.SYS_10009_PARAM_INVALID_ERROR.getCodeString());
		            resultMap.put("resultDesc", Hint.SYS_10009_PARAM_INVALID_ERROR.getMessage().replace("{param}", "remark2"));
				}
			}
			String fundId = ConvertUtils.getString(receiveMap.get("FundId")).trim(); //资金平台ID
		    String url = ParaUtils.getNetPayUrl(fundId);
			String merchantId = ParaUtils.getMerchantId(fundId);   //商户代码
			String posId = ParaUtils.getPosId(fundId);               //商户柜台代码
			String branchId = ParaUtils.getBranchId(fundId);            //分行代码	
			String orderId = ConvertUtils.getString(receiveMap.get("orderId")).trim();  //订单号
			String payment = ConvertUtils.getString(receiveMap.get("payment")).trim();  //付款金额
			String curCode = CcbConstants.CURCODE;           //币种 人民币	
			String remark1= ConvertUtils.getString(receiveMap.get("remark1")).trim();              //备注1
			String remark2 = ConvertUtils.getString(receiveMap.get("remark2")).trim();             //备注2
			String txCode = TxCodeEnum.ANTIPHISHINGPAYMENT.getTxCode();        //交易码
			String mac= "";                  //MAC校验码
			String type = CcbConstants.TYPE_ANTIPHISHING;               //接口类型 防钓鱼执法
			String pub = ParaUtils.getPub(fundId); 
			pub = pub.substring(pub.length()-30,pub.length());
			String gateway = ConvertUtils.getString(receiveMap.get("gateway")).trim();     //网关类型
			String clientIp = ConvertUtils.getString(receiveMap.get("clientIp")).trim();  //客户端IP
			String regInfo = ConvertUtils.getString(receiveMap.get("regInfo")).trim();     //客户注册信息
			String proInfo =  ConvertUtils.getString(receiveMap.get("proInfo")).trim();    //商品信息
			String referer = "";             //商户URL 传空值
 
			//判读客户注册信息是否包含汉字
			if(true == ValidateUtils.isContainsChinese(regInfo)){
				regInfo = ConvertUtils.getEscape(regInfo);
			}
			//判断商品信息是否包含汉字
			if(true == ValidateUtils.isContainsChinese(proInfo)){
				proInfo = ConvertUtils.getEscape(proInfo);
			}
			
			StringBuffer str1 = new StringBuffer();
			str1.append("MERCHANTID=").append(merchantId)
			.append("&POSID=").append(posId)
			.append("&BRANCHID=").append(branchId)
			.append("&ORDERID=").append(orderId)
			.append("&PAYMENT=").append(payment)
			.append("&CURCODE=").append(curCode)
			.append("&TXCODE=").append(txCode)
			.append("&REMARK1=").append(remark1)
			.append("&REMARK2=").append(remark2)
			.append("&TYPE=").append(type);
			
			String pubStr = "&PUB=" +pub;
			
			StringBuffer str2 = new StringBuffer();
			str2.append("&GATEWAY=").append(gateway)
			.append("&CLIENTIP=").append(clientIp)
			.append("&REGINFO=").append(regInfo)
			.append("&PROINFO=").append(proInfo)
			.append("&REFERER=").append(referer);
			String md5Str = str1.toString() + pubStr + str2.toString();
			//MAC加密
			mac=DigestUtils.md5Hex(md5Str);
			str2.append("&MAC=").append(mac);
			
			//返回结果
			resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
            resultMap.put("url",url);
            resultMap.put("params", str1.toString() + str2.toString());
		}catch(Exception ex){
			logger.error(ex.getMessage());
			resultMap.put("resultCode", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getCodeString());
            resultMap.put("resultDesc", Hint.SYS_10008_ACCESS_INTERFACE_EXCEPTION_ERROR.getMessage());
		}
		
      return resultMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.ccb.service.NetPayService#getNotifyData(java.lang.String)
	 */
	@Override
	public Map<String, Object> getNotifyData(String params) throws Exception {
		logger.info("##### getNotifyData() #####");
		//返回值
		Map<String,Object> resultMap = new HashMap<String,Object>();
		logger.info("params=" + params);
		//校验入口参数
		if(StringUtils.isBlank(params)){
  			resultMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
  	        resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
  	        return resultMap;
  		}
		//参数转换为Map
		Map<String,Object> paramMap = ConvertUtils.urlParamsToMap(params);
		if(null == paramMap || 0== paramMap.size() ){
			resultMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
	        resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
	        return resultMap;
		}
		String posId = ConvertUtils.getString(paramMap.get("POSID")).trim();             //商户柜台代码
		String branchId = ConvertUtils.getString(paramMap.get("BRANCHID")).trim();        //分行代码	
		String orderId = ConvertUtils.getString(paramMap.get("ORDERID")).trim();          //订单号
		String payment = ConvertUtils.getString(paramMap.get("PAYMENT")).trim();          //付款金额
		String curCode = ConvertUtils.getString(paramMap.get("CURCODE")).trim();          //币种 人民币	
		String remark1= ConvertUtils.getString(paramMap.get("REMARK1")).trim();           //备注1
		String remark2 = ConvertUtils.getString(paramMap.get("REMARK2")).trim();          //备注2
		String success = ConvertUtils.getString(paramMap.get("SUCCESS")).trim();          //成功标志
		String type = ConvertUtils.getString(paramMap.get("TYPE")).trim();                //接口类型 防钓鱼执法
		String referer =ConvertUtils.getString(paramMap.get("REFERER")).trim();           //referer信息
		String clientIp = ConvertUtils.getString(paramMap.get("CLIENTIP")).trim();        //客户端IP		
	    String sign = ConvertUtils.getString(paramMap.get("SIGN")).trim();              //数字签名
	    //remark1为资金平台ID
	    String fundId = remark1;
	    //校验参数
  		if(StringUtils.isBlank(fundId)){
  			resultMap.put("resultCode", Hint.SYS_10002_PARAM_ERROR.getCodeString());
  	        resultMap.put("resultDesc", Hint.SYS_10002_PARAM_ERROR.getMessage());
  	        return resultMap;
  		}
	    //验签字符串拼接
        StringBuffer str = new StringBuffer();
		str.append("POSID=").append(posId)
		.append("&BRANCHID=").append(branchId)
		.append("&ORDERID=").append(orderId)
		.append("&PAYMENT=").append(payment)
		.append("&CURCODE=").append(curCode)
		.append("&REMARK1=").append(remark1)
		.append("&REMARK2=").append(remark2);
		//账户类型
		if(null != paramMap.get("ACC_TYPE")){
			String acc_type = ConvertUtils.getString(paramMap.get("ACC_TYPE"));       
			str.append("&ACC_TYPE=").append(acc_type);
		}
		str.append("&SUCCESS=").append(success)
		.append("&TYPE=").append(type)
		.append("&REFERER=").append(referer)
		.append("&CLIENTIP=").append(clientIp);
		//系统记账日期
		if(null != paramMap.get("ACCDATE")){
			String accDate = ConvertUtils.getString(paramMap.get("ACCDATE"));          
			str.append("&ACCDATE=").append(accDate);
		}
		//支付账户信息
		if(null != paramMap.get("USRMSG")){
			String usrMsg = ConvertUtils.getString(paramMap.get("USRMSG"));            
			str.append("&USRMSG=").append(usrMsg);
		}
		RSASig rsaSig = new RSASig();
		rsaSig.setPublicKey(ParaUtils.getPub(fundId));
		boolean signFlag = rsaSig.verifySigature(sign, str.toString());
		if(false == signFlag){
			resultMap.put("resultCode", Hint.SP_14107_CHECK_SIGN_FAIL.getCodeString());
	        resultMap.put("resultDesc", Hint.SP_14107_CHECK_SIGN_FAIL.getMessage());
	        return resultMap;
		}
		//拼接返回数据
        paramMap.remove("SIGN");
        resultMap.putAll(paramMap);
		resultMap.put("resultCode", Hint.SYS_SUCCESS.getCodeString());
        resultMap.put("resultDesc", Hint.SYS_SUCCESS.getMessage());
        
		return resultMap;
	}
	

}
