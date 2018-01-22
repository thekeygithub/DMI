package com.pay.cloud.pay.escrow.yeepay.service.impl;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.cfca.util.pki.api.CertUtil;
import com.cfca.util.pki.api.KeyUtil;
import com.cfca.util.pki.api.SignatureUtil;
import com.cfca.util.pki.cert.X509Cert;
import com.cfca.util.pki.cipher.JCrypto;
import com.cfca.util.pki.cipher.JKey;
import com.pay.cloud.pay.common.http.utils.HttpUtils;
import com.pay.cloud.pay.escrow.yeepay.service.TransferPayService;
import com.pay.cloud.pay.escrow.yeepay.utils.CallbackUtils;
import com.pay.cloud.pay.escrow.yeepay.utils.Constants;
import com.pay.cloud.pay.escrow.yeepay.utils.Digest;
import com.pay.cloud.pay.escrow.yeepay.utils.XmlUtils;
import com.pay.cloud.util.CacheUtils;
import com.pay.cloud.util.XmlToMap;

/**
 * 代发代付实现接口
 */
@Service("transferPayServiceImpl")
public class TransferPayServiceImpl implements TransferPayService{
	
	//日志
	private static Logger log = Logger.getLogger(TransferPayServiceImpl.class);
	
	
	//总公司在易宝支付的客户编
	private String getGroupId(){
		return CacheUtils.getPpIntfProp(Constants.FUND_ID).getPpSpCode();
	}
	
	//发起付款的总（分）公司在易宝支付的客户编号
	private String getMerId(){
		return CacheUtils.getPpIntfProp(Constants.FUND_ID).getPpSpCode();
	}
	
	//获取账户余额查询地址
	private String getAccountBalanceQueryUrl() {
		return CacheUtils.getDimSysConfConfValue("ACCOUNT_BALANCE_QUERY_URL");
	}
	
	//获取其它接口地址
	private String getOtherUrl(){
		return CacheUtils.getDimSysConfConfValue("TRANSFER_OTHER_URL");
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.TransferPayService#transferPayBatch(java.util.Map)
	 */
	@Override
	public Map<String, Object> transferPayBatch(Map<String, Object> params) throws Exception {
		if (null == params || 0==params.size()){
			log.info("TransferPayServiceImpl。transferSingle 传入参数为空");
			throw new Exception("传入参数为空");
		}
		//填充map其它值
		params.put("cmd", "TransferBatch");  //命令是固定值: TransferBatch
		params.put("Version", Constants.TRANSFER_VERSION_1_0);  //接口版本否固定值:1.0
		params.put("group_Id", getGroupId());     //总公司商户编号
		params.put("mer_Id", getMerId()) ;      // 实际发起付款的交易商户编号
		params.put("is_Repay", Constants.YES_NO_1);  //0-不需要判断1-需要判断。如果需要判断重复打款，系统会判断本批次中是否存在金额和收款账号相同的记录。 
		//调用公共接口
		String url = getOtherUrl();
		String digest = "cmd,mer_Id,batch_No,total_Num,total_Amt,is_Repay,hmacKey";
		String backDigest = "cmd,ret_Code,mer_Id,batch_No,total_Amt,total_Num,r1_Code,hmacKey";
		Map<String,Object> result = transfer(url, params,digest,backDigest);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.TransferPayService#transferSingle(java.util.Map)
	 */
	@Override
	public Map<String, Object> transferSingle(Map<String,String> params) throws Exception {
		if (null == params || 0==params.size()){
			log.info("TransferPayServiceImpl。transferSingle 传入参数为空");
			throw new Exception("传入参数为空");
		}
		//填充map其它值
		params.put("cmd", "TransferSingle");  //命令是固定值: TransferBatch
		params.put("version", Constants.TRANSFER_VERSION_1_1);  //接口版本否固定值:1.1
		params.put("group_Id", getGroupId());     //总公司商户编号
		params.put("mer_Id", getMerId()) ;      // 实际发起付款的交易商户编号
		//调用公共接口
		String url = getOtherUrl();
		String digest = "cmd,mer_Id,batch_No,order_Id,amount,account_Number,hmacKey";
		String backDigest = "cmd,ret_Code,mer_Id,batch_No,total_Amt,total_Num,r1_Code,hmacKey";
		Map<String,Object> result = transfer(url, params,digest,backDigest);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.TransferPayService#accountBalanaceQuery(java.util.Map)
	 */
	@Override
	public Map<String, Object> accountBalanaceQuery(Map<String, String> params)
			throws Exception {
		if (null == params || 0==params.size()){
			log.info("TransferPayServiceImpl。transferSingle 传入参数为空");
			throw new Exception("传入参数为空");
		}
		params.put("cmd", "AccountBalanaceQuery");  //命令是固定值: TransferBatch
		params.put("version", Constants.TRANSFER_VERSION_1_0);  //接口版本否固定值:1.0
		params.put("mer_Id", getMerId()) ;      // 实际发起付款的交易商户编号
		//调用公共接口
		String url = getAccountBalanceQueryUrl();
		String digest = "cmd,mer_Id,date,hmacKey";
		String backDigest = "cmd,ret_Code,balance_Amount,valid_Amount,hmacKey";
		Map<String,Object> result = transfer(url, params,digest,backDigest);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.TransferPayService#batchDetailQuery(java.util.Map)
	 */
	@Override
	public Map<String, Object> batchDetailQuery(Map<String, String> params) throws Exception {
		if (null == params || 0==params.size()){
			log.info("TransferPayServiceImpl。transferSingle 传入参数为空");
			throw new Exception("传入参数为空");
		}
		params.put("cmd", "BatchDetailQuery");  //命令是固定值: TransferBatch
		params.put("version", Constants.TRANSFER_VERSION_1_0);  //接口版本否固定值:1.0
		params.put("group_Id", getGroupId());     //总公司商户编号
		params.put("mer_Id", getMerId()) ;      // 实际发起付款的交易商户编号
		//调用公共接口
		String url = getOtherUrl();
		String digest = "cmd,mer_Id,batch_No,order_Id,page_No,hmacKey";
		String backDigest = "cmd,ret_Code,batch_No,total_Num,end_Flag,hmacKey";
		Map<String,Object> result = transferForbatchDetailQuery(url, params,digest,backDigest);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.TransferPayService#transferNotify(java.lang.String)
	 */
	@Override
	public Map<String, Object> transferNotify(String notifyXml) throws Exception {
		//回调签名顺序
		String backDigest = "cmd,mer_Id,batch_No,order_Id,status,message,hmacKey";
		Map<String,Object> result = XmlUtils.xml2Map(notifyXml);
		//校验签名
		verifySignature(result,backDigest,null);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.ebaonet.cloud.pay.escrow.yeepay.service.TransferPayService#transferNotifyBack(java.util.Map)
	 */
	@Override
	public String transferNotifyBack(Map<String, String> params) throws Exception {
		//校验传入参数有效性
		if(null == params || 0 == params.size()){
			log.info("TransferPayServiceImpl。transferNotifyBack 传入参数为空");
			throw new Exception("传入参数为空");
		}
		//填充Map其它值
		params.put("cmd", "TransferNotify");  //命令固定值: TransferNotify
		params.put("version", Constants.TRANSFER_VERSION_1_1);         //接口版本否固定值:1.1
		params.put("mer_Id", getMerId());             //企业编号
		params.put("ret_Code", "S");          //返回码成功接收通知请返回S
		String digest = "cmd,mer_Id,batch_No,order_Id,ret_Code,hmacKey";
		String hmacStr = getHmacStr(params, digest);
		String signMessgae = getSignMessage(hmacStr, getSesstion());
		params.put("hmac", signMessgae);      //签名信息
		String result = XmlUtils.map2Xml(params, HttpUtils.URL_PARAM_DECODECHARSET_GBK);
		return result;
	}
	
	
	//生成Hmac（哈希运算消息认证码（Hash-based Message Authentication Code））
	private String getHmacStr(Map params,String digest) throws Exception{
		//商户密钥
		String hmacKey = CacheUtils.getPpIntfProp(Constants.FUND_ID).getEbaoDesKey();
		//格式化为数组
		String[] digestValues = digest.split(",");
		String hmacStr="";
		for(int i=0;i<digestValues.length;i++){
			if(digestValues[i].equals("hmacKey")){
				hmacStr = hmacStr+hmacKey;
				continue;
			}
			String tempStr = (String)params.get(digestValues[i]);
			hmacStr = hmacStr + ((tempStr == null) ? "" : tempStr);
		}
		return hmacStr;
	}
	
	//数字签名
	private String getSignMessage(String hmacStr,com.cfca.util.pki.cipher.Session session ) throws Exception{
		//下面用数字证书进行签名
		String ALGORITHM = SignatureUtil.SHA1_RSA;
		//证书路径
		String sysPath =Thread.currentThread().getContextClassLoader().getResource("").getPath();
		log.info("------"+sysPath+"------"+File.separator+"------");
		JKey jkey = KeyUtil.getPriKey(sysPath + "10013422857.pfx", "ebaonetpay");
		X509Cert cert = CertUtil.getCert(sysPath+ "10013422857.pfx", "ebaonetpay");
		log.info(cert.getSubject());
		
		X509Cert[] cs=new X509Cert[1];
		cs[0]=cert;
		String signMessage ="";
		SignatureUtil signUtil =null;
		try{
			// 第二步:对请求的串进行MD5对数据进行签名
			String yphs = Digest.hmacSign(hmacStr);
			signUtil = new SignatureUtil();
			byte[] b64SignData;
			// 第三步:对MD5签名之后数据调用CFCA提供的api方法用商户自己的数字证书进行签名
			b64SignData = signUtil.p7SignMessage(true, yphs.getBytes(),ALGORITHM, jkey, cs, session);
			signMessage = new String(b64SignData, "UTF-8");
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return signMessage;
	}
	
	//获取会话session
	private com.cfca.util.pki.cipher.Session getSesstion() throws Exception{
		com.cfca.util.pki.cipher.Session tempsession = null;
		JCrypto jcrypto =null;
		if(tempsession==null){
			try {
		        //初始化加密库，获得会话session
		        //多线程的应用可以共享一个session,不需要重复,只需初始化一次
		        //初始化加密库并获得session。
		        //系统退出后要jcrypto.finalize()，释放加密库
		        jcrypto = JCrypto.getInstance();
		        jcrypto.initialize(JCrypto.JSOFT_LIB, null);
		        tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
		        if(jcrypto!=null){
			         jcrypto.finalize (com.cfca.util.pki.cipher.JCrypto.JSOFT_LIB,null);
			     }
		    } catch (Exception ex) {
		    	log.error(ex.toString());
		    	throw ex;
		    }
		}
		return tempsession;
	}
	
	//调用易宝支付代发代付接口公用方法
	private Map<String,Object> transfer(String url,Map params,String digest,String backDiest) throws Exception {
		//判断入口参数是否有效
		if (null == params || 0==params.size() || StringUtils.isBlank(url) ||  StringUtils.isBlank(digest) || StringUtils.isBlank(backDiest)){
			log.info("TransferPayServiceImpl。transferSingle 传入参数为空");
			throw new Exception("传入参数为空");
		}
		//获取Hmac字符串
		String hmacStr=getHmacStr(params,digest);
		log.info("签名之前的源数据为---||" + hmacStr+"||");
		//获取会话
		com.cfca.util.pki.cipher.Session tempsession = getSesstion();
		//下面用数字证书进行签名
		String signMessage = getSignMessage(hmacStr,tempsession);
		log.info("经过md5和数字证书签名之后的数据为---||"+signMessage+"||");	
		//修改hmac
		params.put("hmac", signMessage);
		//map转化为XML
		String xml =XmlUtils.map2Xml(params,HttpUtils.URL_PARAM_DECODECHARSET_GBK);
		//第四步:发送https请求
		String responseMsg = CallbackUtils.httpRequest(url, xml, "POST", "gbk","text/xml ;charset=gbk", false);
		log.info("服务器响应xml报文:" + responseMsg);
		//格式化XML为Map
		Map<String,Object> responseMap = XmlUtils.xml2Map(responseMsg);
		if(null == responseMap){
			throw new Exception("XML转换MAP失败");
		}
		//第五步:对服务器响应报文进行验证签名
		verifySignature(responseMap, backDiest,tempsession);
		//返回
		return responseMap;
	}

	//调用易宝支付代发代付接口公用方法
	private Map<String,Object> transferForbatchDetailQuery(String url,Map params,String digest,String backDiest) throws Exception {
		//判断入口参数是否有效
		if (null == params || 0==params.size() || StringUtils.isBlank(url) ||  StringUtils.isBlank(digest) || StringUtils.isBlank(backDiest)){
			log.info("TransferPayServiceImpl。transferSingle 传入参数为空");
			throw new Exception("传入参数为空");
		}
		//获取Hmac字符串
		String hmacStr=getHmacStr(params,digest);
		log.info("签名之前的源数据为---||" + hmacStr+"||");
		//获取会话
		com.cfca.util.pki.cipher.Session tempsession = getSesstion();
		//下面用数字证书进行签名
		String signMessage = getSignMessage(hmacStr,tempsession);
		log.info("经过md5和数字证书签名之后的数据为---||"+signMessage+"||");
		//修改hmac
		params.put("hmac", signMessage);
		//map转化为XML
		String xml =XmlUtils.map2Xml(params,HttpUtils.URL_PARAM_DECODECHARSET_GBK);
		//第四步:发送https请求
		String responseMsg = CallbackUtils.httpRequest(url, xml, "POST", "gbk","text/xml ;charset=gbk", false);
		log.info("服务器响应xml报文:" + responseMsg);
		//格式化XML为Map
		Map<String,Object> responseMap = XmlToMap.xml2mapWithAttr(responseMsg, false);
		if(null == responseMap){
			throw new Exception("XML转换MAP失败");
		}
		//第五步:对服务器响应报文进行验证签名
		verifySignature(responseMap, backDiest,tempsession);
		//返回
		return responseMap;
	}
	
	//校验签名
	private void verifySignature(Map<String,Object> responseMap,String backDigestStr,com.cfca.util.pki.cipher.Session session) throws Exception{
		//获取会话
		if (null == session){
			 session = getSesstion();
		}
		//获取返回报文hmac
		String cmdValue = responseMap.get("hmac").toString();
		//报文进行验证签名
		SignatureUtil signUtil =new SignatureUtil();
		boolean sigerCertFlag = false;
		if(cmdValue!=null){
			sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), session);
			String backmd5hmac = "";
			if(sigerCertFlag){
				log.info("证书验签成功");
				backmd5hmac = new String(signUtil.getSignedContent());
				log.info("证书验签获得的MD5签名数据为----" + backmd5hmac);
				log.info("证书验签获得的证书dn为----" + new String(signUtil.getSigerCert()[0].getSubject()));
				//第六步.将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
				String backHmacStr=getHmacStr(responseMap,backDigestStr);
				String newmd5hmac = Digest.hmacSign(backHmacStr);
				log.info("提交返回源数据为---||" + backHmacStr+"||");
				log.info("经过md5签名后的验证返回hmac为---||" + newmd5hmac+"||");
				log.info("提交返回的hmac为---||" + backmd5hmac+"||");
				if(newmd5hmac.equals(backmd5hmac)){
					log.info("易宝响应报文：md5验签成功");
					//第七步:判断该证书DN是否为易宝
					if(signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf("OU=YEEPAY,") > 0){
						log.info("易宝响应报文：证书DN是易宝的");
					}else{
						log.info("易宝响应报文出错：证书DN不是易宝的");
						throw new Exception("易宝响应报文出错：证书DN不是易宝的");
					}
				}else{
					log.info("易宝响应报文出错：md5验签失败");
					throw new Exception("易宝响应报文出错：md5验签失败");
				}
			}else{
				log.info("易宝响应报文出错：证书验签失败");
				throw new Exception("易宝响应报文出错：证书验签失败");
			}
		}
	}

}
