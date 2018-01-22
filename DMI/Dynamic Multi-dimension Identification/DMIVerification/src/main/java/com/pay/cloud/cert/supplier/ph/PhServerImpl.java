package com.pay.cloud.cert.supplier.ph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.cloud.cert.exception.CodeException;
import com.pay.cloud.cert.info.CertResult;
import com.pay.cloud.cert.info.EbaoFourParam;
import com.pay.cloud.cert.supplier.SupplierServer;
import com.pay.cloud.cert.supplier.ph.info.PhFourParam;
import com.pay.cloud.cert.supplier.ph.info.PhResult;
import com.pay.cloud.cert.utils.HttpsNoCert;
import com.pay.cloud.constants.PropertiesConstant;
import com.pay.cloud.core.common.JsonStringUtils;
import com.pay.cloud.util.hint.Hint;
import com.pay.cloud.util.hint.Propertie;
import com.pay.secrity.crypto.RSA;
/**
 * 普惠实名认证
 * @author yanjie.ji
 * @date 2016年7月13日 
 * @time 下午2:06:57
 */
public class PhServerImpl implements SupplierServer {
	
	Logger logger = LoggerFactory.getLogger(PhServerImpl.class);
	
	private static final String CHAR_CODE = "UTF-8";

	@Override
	public CertResult doCertFour(EbaoFourParam param) throws CodeException {
		PhFourParam phParam = new PhFourParam(param);
		if(phParam.checkSelf()){
			//接口URL
			String url = Propertie.APPLICATION.value(PropertiesConstant.CERT_CONFIG_PH_FOUR_URL);
			if(StringUtils.isEmpty(url))
				throw new CodeException(String.valueOf(Hint.CERT_30002_CONFIG_PH_FOUR_URL_ERROR.getCode()));
			//普惠安全校验码
			String safety_code = Propertie.APPLICATION.value(PropertiesConstant.CERT_CONFIG_PH_SAFETY_CODE);
			if(StringUtils.isEmpty(safety_code))
				throw new CodeException(String.valueOf(Hint.CERT_30003_CONFIG_PH_SAFETY_CODE_EMPTY.getCode()));
			//待签名字符串
			String paramStr = phParam.getParam();
			String signStr=paramStr+"&key="+safety_code;
			if(logger.isInfoEnabled()) logger.info("待签名字符串:"+signStr);
			//签名
			String key = getPrivateKey();
			String sign = RSA.sign(signStr, key);
			if(StringUtils.isEmpty(sign)) throw new CodeException(String.valueOf(Hint.CERT_30105_PH_RSA_SIGN_EMPTY.getCode()));
			TreeMap<String,Object> propertys = phParam.getProperties();
			propertys.put("sign", sign);
			//实际参数
			String allParam = urlEncode(propertys);
			if(logger.isInfoEnabled()) logger.info("实际参数:"+allParam);
			//HTTPS请求
			String resultStr = null;
			try {
				resultStr = HttpsNoCert.doPost(url, allParam);
				if(logger.isInfoEnabled()) logger.info("普惠实名认证结果:"+resultStr);
			} catch (Exception e) {
				e.printStackTrace();
				throw new CodeException(String.valueOf(Hint.CERT_30106_PH_FOUR_POST_ERROR.getCode()));
			}
			//处理返回结果
			if(StringUtils.isEmpty(resultStr)) throw new CodeException(String.valueOf(Hint.CERT_30106_PH_FOUR_POST_ERROR.getCode()));
			PhResult phResult = null;
			try {
				phResult = JsonStringUtils.jsonToObjectIgnorePro(resultStr, PhResult.class);
			} catch (Exception e) {
				throw new CodeException(String.valueOf(Hint.CERT_30107_PH_FOUR_RESULT_ERROR.getCode()));
			}
			return phResult.createCertRes();
		}else{
			CertResult result = new CertResult();
			result.setCode(String.valueOf(Hint.CERT_31006_PARAM_ERROR.getCode()));
			result.setMsg(Hint.CERT_31006_PARAM_ERROR.getMessage());
		}
		return null;
	}
	/**
	 * 获取RSA密钥
	 * @return
	 * @throws CodeException
	 */
	private String getPrivateKey()throws CodeException{
		String filePath = Propertie.APPLICATION.value(PropertiesConstant.CERT_CONFIG_PH_RSA_PRIVATE_KEY_FILE_PATH);
		if(StringUtils.isEmpty(filePath)) throw new CodeException(String.valueOf(Hint.CERT_30004_CONFIG_PH_PRIVATE_KEY_FILE_EMPTY.getCode()));
		File file = new File(this.getClass().getClassLoader().getResource("").getPath()+filePath);
		String key = getFileCont(file);
		if(StringUtils.isEmpty(key)) throw new CodeException(String.valueOf(Hint.CERT_30104_PH_RSA_PRIVATE_KEY_EMPTY.getCode()));
		return key.replaceAll("\n", "").replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "");
	}
	/**
	 * 读取密钥文件
	 * @param file
	 * @return
	 * @throws CodeException
	 */
	private String getFileCont(File file)throws CodeException{
		FileInputStream fis = null;
		BufferedReader br = null;
		StringBuffer key = new StringBuffer();
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
			String str = null;
			while((str = br.readLine())!=null){
				key.append(str);
			}
		} catch (FileNotFoundException e) {
			throw new CodeException(String.valueOf(Hint.CERT_30102_PH_RSA_PRIVATE_KEY_FILE_NOT_FOUND.getCode()));
		} catch (IOException e) {
			throw new CodeException(String.valueOf(Hint.CERT_30103_PH_RSA_PRIVATE_KEY_ERROR.getCode()));
		}finally{
			try {
				if(fis!=null)
					fis.close();
				if(br!=null)
					br.close();
			} catch (IOException e) {
			}
		}
		
		return key.toString();
	}
	/**
	 * 对参数进行URL编码
	 * @param data
	 * @return
	 */
	private static String urlEncode(Map<String,Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Object> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"",CHAR_CODE)).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
}
