package com.models.cloud.cert.server.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.models.cloud.cert.exception.CodeException;
import com.models.cloud.cert.info.CertResult;
import com.models.cloud.cert.info.EbaoFourParam;
import com.models.cloud.cert.server.CertServer;
import com.models.cloud.cert.supplier.SupplierServer;
import com.models.cloud.cert.supplier.hf.HfServerImpl;
import com.models.cloud.cert.supplier.ph.PhServerImpl;
import com.models.cloud.constants.PropertiesConstant;
import com.models.cloud.util.hint.Hint;
import com.models.cloud.util.hint.Propertie;
/**
 * 实名认证接口实现类--单例
 * @author yanjie.ji
 * @date 2016年7月14日 
 * @time 上午10:52:34
 */
@Service("certServerImpl")
public class CertServerImpl implements CertServer {
	
	@Override
	public CertResult doCertFour(EbaoFourParam param) {
		CertResult result = null;
		//循环已有的供应商列表进行实名认证
		for (short type : CertServer.CERT_SUPPLIER_TYPES){
			result = doCertFour(type,param);
			if(result!=null) break;
		}
		if(result==null){
			result = new CertResult();
			result.setCode(String.valueOf(Hint.CERT_31005_NO_RESULT.getCode()));
			result.setMsg(Hint.CERT_31005_NO_RESULT.getMessage());
		}
		return result;
	}

	@Override
	public CertResult doCertFour(short type, EbaoFourParam param){
		CertResult result = new CertResult();
		try{
			testBaffle(param);
			//获取供应商服务
			SupplierServer server = getSupSer(type);
			result = server.doCertFour(param);
		}catch(CodeException codeEx){
			result.setCode(codeEx.getCode());
			result.setMsg(Hint.getMessage(Integer.parseInt(codeEx.getCode())));
		}catch (Exception e) {
			result.setCode(String.valueOf(Hint.CERT_30000_ERROR.getCode()));
			result.setMsg(e.getMessage());
		}
		return result;
	}
	/**
	 * 测试模式挡板
	 * 四要素姓名输入什么错误码就返回什么错误码
	 * @param param
	 * @throws CodeException
	 */
	private void testBaffle(EbaoFourParam param)throws CodeException{
		String testModel = Propertie.APPLICATION.value(PropertiesConstant.CERT_MODEL_TEST);
		if(!"true".equals(testModel)) return;
		//挡板截取手机号后5位，如果后5位在异常码在异常枚举中(仅包含5位数的异常码)则返回异常码，否则均返回成功
		String mobile = param.getMobile();
		if(StringUtils.isNotEmpty(mobile)&&mobile.length()>=5){
			mobile = mobile.substring(mobile.length()-5,mobile.length());
			if(!NumberUtils.isNumber(mobile)||mobile.startsWith("0")) throw new CodeException(String.valueOf(Hint.SYS_SUCCESS.getCode()));
			int code = Integer.parseInt(mobile);
			for (Hint hint : Hint.values()) {
				if(hint!=null&&hint.getCode()==code){
					throw new CodeException(String.valueOf(hint.getCode()));
				}
			}
		}
		throw new CodeException(String.valueOf(Hint.SYS_SUCCESS.getCode()));
	}
	
	/**
	 * 根据类型获取供应商服务
	 * 默认普惠
	 * @param type
	 * @return
	 */
	private SupplierServer getSupSer(short type){
		if(type==CertServer.CERT_SUPPLIER_TYPE_PH)
			return new PhServerImpl();
		else if(type==CertServer.CERT_SUPPLIER_TYPE_HF)
			return new HfServerImpl();
		else return new PhServerImpl();
	}
}
