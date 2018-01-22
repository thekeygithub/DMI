package com.pay.cloud.pay.escrow.alipay.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alipay.api.internal.util.WebUtils;
import com.alipay.api.internal.util.XmlUtils;
import com.pay.cloud.pay.escrow.alipay.dto.BaseDTO;
import com.pay.cloud.pay.escrow.alipay.exception.UnmatchedParamException;
import com.pay.cloud.pay.escrow.alipay.param.AlipayImmediatePayParam;
import com.pay.cloud.pay.escrow.alipay.param.AlipayMobileParam;
import com.pay.cloud.pay.escrow.alipay.param.AlipayRefundParam;
import com.pay.cloud.pay.escrow.alipay.param.AlipayRefundQueryParam;
import com.pay.cloud.pay.escrow.alipay.param.AlipaySingleQueryParam;
import com.pay.cloud.pay.escrow.alipay.request.AlipayImmediatePayRequest;
import com.pay.cloud.pay.escrow.alipay.request.AlipayMolilePayRequest;
import com.pay.cloud.pay.escrow.alipay.request.AlipayRefundRequest;
import com.pay.cloud.pay.escrow.alipay.request.AlipaySingleQueryRequest;
import com.pay.cloud.pay.escrow.alipay.response.AliErrorMsg;
import com.pay.cloud.pay.escrow.alipay.response.AlipayRefundQueryResponse;
import com.pay.cloud.pay.escrow.alipay.response.AlipaySingleQueryResponse;
import com.pay.cloud.pay.escrow.alipay.response.AlipaySynchroRefundResponse;
import com.pay.cloud.pay.escrow.alipay.response.AlipaySingleQueryResponse.SingleQueryResponseTrade;
import com.pay.cloud.pay.escrow.alipay.service.AlipayService;
import com.pay.cloud.pay.escrow.alipay.utils.AlipayConstants;
import com.pay.cloud.pay.escrow.alipay.utils.AlipayCore;
import com.pay.cloud.pay.escrow.alipay.utils.AlipayNotify;
import com.pay.cloud.pay.escrow.alipay.utils.AlipayRSA;
import com.pay.cloud.pay.escrow.alipay.utils.CharUtil;
import com.pay.cloud.pay.escrow.alipay.utils.EBaoConstants;
import com.pay.cloud.pay.escrow.alipay.utils.EbaoXmlConverter;
import com.pay.cloud.pay.escrow.alipay.utils.TransformMapAndInfoUtil;
import com.pay.cloud.util.CacheUtils;
/**
 * 阿里支付service实现
 * @author yanjie.ji
 * @date 2016年8月17日 
 * @time 下午3:31:02
 */
@Service("aliPayServiceImpl")
public class AliPayServiceImpl implements AlipayService {
	
	/**
	 * 对支付宝返回数据进行验签
	 * @param params
	 * @return
	 */
	@Override
	public BaseDTO<Boolean> verify(Map<String, Object> params,String fund_id) {
		String partner = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PARTNER}));
		String public_key = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_ALI_PUBLIC_KEY}));
		//验签
		boolean right = AlipayNotify.verify(params, public_key, partner);
		BaseDTO<Boolean> dto = new BaseDTO<Boolean>();
		dto.setResult(right);
		if(right){
			dto.setResultCode(AliErrorMsg.SUCCESS.getError_code());
			dto.setResultDesc(AliErrorMsg.SUCCESS.getMsg());
		}else{
			dto.setResultCode(AliErrorMsg.VERFIY_FAIL.getError_code());
			dto.setResultDesc(AliErrorMsg.VERFIY_FAIL.getMsg());
		}
		return dto;
	}

	/**
	 * 获取移动支付所需的url
	 * @param param
	 * @return
	 * @throws RuntimeException
	 */
	@Override
	public BaseDTO<String> getMobilePayUrl(AlipayMobileParam param,String fund_id){
		BaseDTO<String> dto = new BaseDTO<String>();
		try{
			if(param.checkSelf()){
				//构造请求参数
				AlipayMolilePayRequest request = new AlipayMolilePayRequest();
				request.setNotify_url(param.getNotify_url());
				request.setApp_id(param.getApp_id());
				request.setAppenv(param.getAppenv());
				request.setOut_trade_no(param.getOut_trade_no());
				request.setSubject(param.getSubject());
				request.setPayment_type(param.getPayment_type());
				request.setTotal_fee(param.getTotal_fee());
				request.setBody(param.getBody());
				request.setGoods_type(param.getGoods_type());
				request.setHb_fq_param(param.getHb_fq_param());
				request.setRn_check(param.getRn_check());
				request.setIt_b_pay(param.getIt_b_pay());
				request.setExtern_token(param.getExtern_token());
				request.setPromo_params(param.getPromo_params());
				if(StringUtils.isNotEmpty(param.getSeller_id()))
					request.setSeller_id(param.getSeller_id());
				else
					request.setSeller_id(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_SELLER_ID})));
				request.setPartner(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PARTNER})));
				request.setService(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_MOBILE_PAY_METHOD})));
				//获取私钥
				String private_key = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PRIVATE_KEY}));
				
				if(AlipayConstants.SIGN_TYPE.equalsIgnoreCase(request.getSign_type())){
					//拼接参数
					String signParam = AlipayCore.createLinkStringWithQuotes(request.getProperties());
					//签名
					String sign = AlipayRSA.sign(signParam, private_key, AlipayConstants.CHARSET_UTF8);
					//需要对签名进行UTF-8编码
					request.setSign(URLEncoder.encode(sign, AlipayConstants.CHARSET_UTF8));
					if(request.checkself()){
						dto.setResult(signParam+"&sign=\""+request.getSign()+"\"&sign_type=\""+request.getSign_type()+"\"");
						dto.setResultCode(AliErrorMsg.SUCCESS.getError_code());
						dto.setResultDesc(AliErrorMsg.SUCCESS.getMsg());
					}
				}else {
					List<String> errParamList = new ArrayList<String>();
					errParamList.add("sign_type");
					throw new UnmatchedParamException(errParamList);
				}
			}
		}catch(UnmatchedParamException e){
			dto.setResultCode(AliErrorMsg.PARAM_ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}catch(RuntimeException e){
			dto.setResultCode(AliErrorMsg.ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		} catch (UnsupportedEncodingException e) {
			dto.setResultCode(AliErrorMsg.ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}
		return dto;
	}

	/**
	 * 支付交易查询（单笔）
	 * @param param
	 * @param fund_id
	 */
	@Override
	public BaseDTO<SingleQueryResponseTrade> querySingleTrade(AlipaySingleQueryParam param, String fund_id) {
		BaseDTO<SingleQueryResponseTrade> dto = new BaseDTO<SingleQueryResponseTrade>();
		try{
			if(param.checkSelf()){
				//构造请求参数
				AlipaySingleQueryRequest request = new AlipaySingleQueryRequest();
				request.setOut_trade_no(param.getOut_trade_no());
				request.setTrade_no(param.getTrade_no());
				request.setPartner(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PARTNER})));
				request.setService(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_QUERY_METHOD})));
				//获取私钥
				String private_key = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PRIVATE_KEY}));
				//获取网关
				String gateway = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_ALI_GATEWAY}));
				if(AlipayConstants.SIGN_TYPE.equalsIgnoreCase(request.getSign_type())){
					//拼接参数
					String signParam = AlipayCore.createLinkString(request.getProperties());
					//签名
					String sign = AlipayRSA.sign(signParam, private_key, AlipayConstants.CHARSET_UTF8);
					request.setSign(sign);
					if(request.checkself()){
						System.out.println("请求地址："+gateway+"?"+WebUtils.buildQuery( request.getProperties(), AlipayConstants.CHARSET_UTF8));
						//请求
						String result = WebUtils.doPost(gateway, request.getProperties(),AlipayConstants.CHARSET_UTF8, AlipayConstants.CONNECT_TIMEOUT, AlipayConstants.READ_TIMEOUT);
						System.out.println("请求结果："+result);
						//解析数据
						AlipaySingleQueryResponse response = EbaoXmlConverter.getModelFromXML(XmlUtils.getRootElementFromString(result), AlipaySingleQueryResponse.class);
						if(response.getIs_success().equals("T")){
							if(response.getResponse()!=null&&response.getResponse().getTrade()!=null){
								//拿到待验签集合
								Map<String,Object> map = TransformMapAndInfoUtil.transformInfoToMap(response.getResponse().getTrade());
								 //验签
								boolean verify = AlipayNotify.getSignVeryfy(map, response.getSign(), CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_ALI_PUBLIC_KEY})));
								if(verify){
									dto.setResultCode(AliErrorMsg.SUCCESS.getError_code());
									dto.setResultDesc(AliErrorMsg.SUCCESS.getMsg());
									dto.setResult(response.getResponse().getTrade());
								}else{
									dto.setResultCode(AliErrorMsg.VERFIY_FAIL.getError_code());
									dto.setResultDesc(AliErrorMsg.VERFIY_FAIL.getMsg());
								}
							}else{
								dto.setResultCode(AliErrorMsg.TRADE_NOT_FOUD.getError_code());
								dto.setResultDesc(AliErrorMsg.TRADE_NOT_FOUD.getMsg());
							}
						}else{
							dto.setResultCode(response.getError());
							dto.setResultDesc(AliErrorMsg.getAliErr(response.getError()).getMsg());
						}
					}
				}else {
					List<String> errParamList = new ArrayList<String>();
					errParamList.add("sign_type");
					throw new UnmatchedParamException(errParamList);
				}
			}
		}catch(UnmatchedParamException e){
			dto.setResultCode(AliErrorMsg.PARAM_ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		} catch (Exception e) {
			dto.setResultCode(AliErrorMsg.ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}
		return dto;
	}

	/**
	 * 退款
	 * @param param
	 * @param fund_id
	 * @return
	 */
	@Override
	public BaseDTO<Boolean> refundTrade(AlipayRefundParam param, String fund_id) {
		BaseDTO<Boolean> dto = new BaseDTO<Boolean>();
		try{
			if(param.checkSelf()){
				//构造请求
				AlipayRefundRequest request = new AlipayRefundRequest();
				request.setNotify_url(param.getNotify_url());
				request.setDback_notify_url(param.getDback_notify_url());
				request.setBatch_no(param.getBatch_no());
				request.setBatch_num(param.getBatch_num());
				request.setRefund_date(param.getRefund_date());
				request.setUse_freeze_amount(param.getUse_freeze_amount());
				request.setDetail_data(param.getRefundDetailData());
				
				request.setPartner(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PARTNER})));
				request.setService(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_REFUND_METHOD})));
				//获取私钥
				String private_key = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PRIVATE_KEY}));
				//获取网关
				String gateway = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_ALI_GATEWAY}));
				if(AlipayConstants.SIGN_TYPE.equalsIgnoreCase(request.getSign_type())){
					//拼接参数
					String signParam = AlipayCore.createLinkString(request.getProperties());
					//签名
					String sign = AlipayRSA.sign(signParam, private_key, AlipayConstants.CHARSET_UTF8);
					request.setSign(sign);
					if(request.checkself()){
						System.out.println(gateway+"?"+WebUtils.buildQuery(request.getProperties(), AlipayConstants.CHARSET_UTF8));
						//请求
						String result = WebUtils.doPost(gateway, request.getProperties(),AlipayConstants.CHARSET_UTF8, AlipayConstants.CONNECT_TIMEOUT, AlipayConstants.READ_TIMEOUT);
						System.out.println("请求结果："+result);
						//解析数据
						AlipaySynchroRefundResponse response = EbaoXmlConverter.getModelFromXML(XmlUtils.getRootElementFromString(result), AlipaySynchroRefundResponse.class);
						if("T".equals(response.getIs_success())){
							dto.setResult(true);
							dto.setResultCode(AliErrorMsg.SUCCESS.getError_code());
							dto.setResultDesc(AliErrorMsg.SUCCESS.getMsg());
						}else if("P".equals(response.getIs_success())){//处理中
							dto.setResult(true);
							dto.setResultCode(AliErrorMsg.DOING.getError_code());
							dto.setResultDesc(AliErrorMsg.DOING.getMsg());
						}else{
							dto.setResult(false);
							dto.setResultCode(response.getError());
							dto.setResultDesc(AliErrorMsg.getAliErr(response.getError()).getMsg());
						}
					}
				}else {
					List<String> errParamList = new ArrayList<String>();
					errParamList.add("sign_type");
					throw new UnmatchedParamException(errParamList);
				}
			}
		}catch(UnmatchedParamException e){
			dto.setResultCode(AliErrorMsg.PARAM_ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}catch(Exception e){
			dto.setResultCode(AliErrorMsg.ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}
		return dto;
	}

	/**
	 * 退款查询
	 * @param param
	 * @param fund_id
	 * @return
	 */
	@Override
	public BaseDTO<AlipayRefundQueryResponse> refundQuery(
			AlipayRefundQueryParam param, String fund_id) {
		BaseDTO<AlipayRefundQueryResponse> dto = new BaseDTO<AlipayRefundQueryResponse>();
		try{
			if(param.checkSelf()){
				//构造请求参数
				AlipaySingleQueryRequest request = new AlipaySingleQueryRequest();
				request.setOut_trade_no(param.getBatch_no());
				request.setTrade_no(param.getTrade_no());
				request.setPartner(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PARTNER})));
				request.setService(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_REFUND_QUERY_METHOD})));
				//获取私钥
				String private_key = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PRIVATE_KEY}));
				//获取网关
				String gateway = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_ALI_GATEWAY}));
				if(AlipayConstants.SIGN_TYPE.equalsIgnoreCase(request.getSign_type())){
					//拼接参数
					String signParam = AlipayCore.createLinkString(request.getProperties());
					//签名
					String sign = AlipayRSA.sign(signParam, private_key, AlipayConstants.CHARSET_UTF8);
					request.setSign(sign);
					if(request.checkself()){
						System.out.println("请求地址："+gateway+"?"+WebUtils.buildQuery( request.getProperties(), AlipayConstants.CHARSET_UTF8));
						//请求
						String result = WebUtils.doPost(gateway, request.getProperties(),AlipayConstants.CHARSET_UTF8, AlipayConstants.CONNECT_TIMEOUT, AlipayConstants.READ_TIMEOUT);
						System.out.println("请求结果："+result);
						//解析数据
						String[] array = result.split("&");
						Map<String,Object> mapData = new HashMap<String,Object>();
						for (String string : array) {
							if(StringUtils.isEmpty(string)) continue;
							String[] data = string.split("=");
							if(data==null||data.length!=2) continue;
							mapData.put(data[0], data[1]);
						}
						AlipayRefundQueryResponse response = (AlipayRefundQueryResponse) TransformMapAndInfoUtil.transformMapToInfo(mapData, AlipayRefundQueryResponse.class);
						if(response!=null){
							if(response.getIs_success().equals("T")){
								dto.setResultCode(AliErrorMsg.SUCCESS.getError_code());
								dto.setResultDesc(AliErrorMsg.SUCCESS.getMsg());
								dto.setResult(response);
							}else{
								dto.setResultCode(response.getError_code());
								dto.setResultDesc(AliErrorMsg.getAliErr(response.getError_code()).getMsg());
							}
						}
					}
				}else {
					List<String> errParamList = new ArrayList<String>();
					errParamList.add("sign_type");
					throw new UnmatchedParamException(errParamList);
				}
			}
		}catch(UnmatchedParamException e){
			dto.setResultCode(AliErrorMsg.PARAM_ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		} catch (Exception e) {
			dto.setResultCode(AliErrorMsg.ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}
		return dto;
	}

	@Override
	public BaseDTO<String> getImmediatePay(AlipayImmediatePayParam param,String fund_id) {
		BaseDTO<String> dto = new BaseDTO<String>();
		try{
			if(param.checkSelf()){
				//构造请求参数
				AlipayImmediatePayRequest request = new AlipayImmediatePayRequest();
				request.setNotify_url(param.getNotify_url());
				request.setReturn_url(param.getReturn_url());
				request.setOut_trade_no(param.getOut_trade_no());
				request.setSubject(param.getSubject());
				request.setTotal_fee(param.getTotal_fee());
				request.setBuyer_id(param.getBuyer_id());
				request.setBuyer_email(param.getBuyer_email());
				request.setBuyer_account_name(param.getBuyer_account_name());
				request.setPrice(param.getPrice());
				request.setQuantity(param.getQuantity());
				request.setBody(param.getBody());
				request.setShow_url(param.getShow_url());
				request.setPaymethod(param.getPaymethod());
				request.setEnable_paymethod(param.getEnable_paymethod());
				request.setAnti_phishing_key(param.getAnti_phishing_key());
				request.setExter_invoke_ip(param.getExter_invoke_ip());
				request.setExtra_common_param(param.getExtra_common_param());
				request.setIt_b_pay(param.getIt_b_pay());
				request.setToken(param.getToken());
				request.setQr_pay_mode(param.getQr_pay_mode());
				request.setQrcode_width(param.getQrcode_width());
				request.setNeed_buyer_realnamed(param.getNeed_buyer_realnamed());
				request.setHb_fq_param(param.getHb_fq_param());
				request.setGoods_type(param.getGoods_type());
				
				boolean seller_ok = false;
				if(StringUtils.isNotEmpty(param.getSeller_id())){
					request.setSeller_id(param.getSeller_id());
					seller_ok = true;
				}
				if(StringUtils.isNotEmpty(param.getSeller_email())){
					request.setSeller_email(param.getSeller_email());
					seller_ok = true;
				}
				if(StringUtils.isNotEmpty(param.getSeller_account_name())){
					request.setSeller_account_name(param.getSeller_account_name());
					seller_ok = true;
				}
				if(!seller_ok&&StringUtils.isNotEmpty(fund_id)){
					request.setSeller_email(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_SELLER_ID})));
				}else throw new UnmatchedParamException("fund_id和seller信息不能同时为空");
				
				request.setPartner(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PARTNER})));
				request.setService(CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_IMMEDIATE_PAY_METHOD})));
				//获取私钥
				String private_key = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_PRIVATE_KEY}));
				//获取网关
				String gateway = CacheUtils.getPpIntfPara(EBaoConstants.joinStr(new String[]{fund_id,EBaoConstants.PARA_TYPE_ALI_GATEWAY}));
				if(AlipayConstants.SIGN_TYPE.equalsIgnoreCase(request.getSign_type())){
					//拼接参数
					String signParam = AlipayCore.createLinkString(request.getProperties());
					//签名
					String sign = AlipayRSA.sign(signParam, private_key, AlipayConstants.CHARSET_UTF8);
					//需要对签名进行UTF-8编码
					request.setSign(URLEncoder.encode(sign, AlipayConstants.CHARSET_UTF8));
					//由于各浏览器对URL的URLEncode的方式不同，现在后台将所有中文进行URLEncode，不让浏览器处理
					signParam = encodeParam(signParam);
					if(request.checkself()){
						dto.setResult(gateway+"&"+signParam+"&sign="+request.getSign()+"&sign_type="+request.getSign_type()+"");
						dto.setResultCode(AliErrorMsg.SUCCESS.getError_code());
						dto.setResultDesc(AliErrorMsg.SUCCESS.getMsg());
					}
				}else {
					List<String> errParamList = new ArrayList<String>();
					errParamList.add("sign_type");
					throw new UnmatchedParamException(errParamList);
				}
			}
		}catch(UnmatchedParamException e){
			dto.setResultCode(AliErrorMsg.PARAM_ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}catch(RuntimeException e){
			dto.setResultCode(AliErrorMsg.ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		} catch (UnsupportedEncodingException e) {
			dto.setResultCode(AliErrorMsg.ERROR.getError_code());
			dto.setResultDesc(e.getMessage());
			dto.setResult(null);
		}
		return dto;
	}
	/**
	 * 对字符串中的中文字符进行URLEncode编码转换
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String encodeParam(String param)throws UnsupportedEncodingException{
		if(StringUtils.isEmpty(param)) return "";
		StringBuffer result = new StringBuffer();
		char[] cArr = param.toCharArray();
		for (char c : cArr) {
			if(CharUtil.isChinese(c)){
				result.append(URLEncoder.encode(new String(new char[]{c}), AlipayConstants.CHARSET_UTF8));
			}else{
				result.append(new String(new char[]{c}));
			}
		}
		return result.toString();
	}
}
