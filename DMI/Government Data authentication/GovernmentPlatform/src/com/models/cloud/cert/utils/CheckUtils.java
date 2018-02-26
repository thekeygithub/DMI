package com.models.cloud.cert.utils;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.models.cloud.constants.PropertiesConstant;
import com.models.cloud.util.hint.Propertie;

/**
 * 检查工具类
 * @author yanjie.ji
 * @date 2016年8月3日 
 * @time 上午10:47:21
 */
public class CheckUtils {
	
	public static boolean checkPhone(String phone){
		String regular = Propertie.APPLICATION.value(PropertiesConstant.CERT_REGULAR_CHINESE_PHONE_NO);
		if(StringUtils.isEmpty(regular)){
			regular = "^[1][3,4,5,7,8][0-9]{9}$";
		}
		return Pattern.compile(regular).matcher(phone).find();
		
	}
	public static boolean checkChineseName(String name){
		String regular = Propertie.APPLICATION.value(PropertiesConstant.CERT_REGULAR_CHINESE_NAME);
		if(StringUtils.isEmpty(regular)){
//			regular = "^([\\xe4-\\xe9][\\x80-\\xbf]{2}){2,25}$";//普惠的规则 
			regular = "^([\u4e00-\u9fa5.•▪·]{2,25})$";
		}
		return Pattern.compile(regular).matcher(name).find();
	}
	
	public static void main(String[] args) {
		String[] str = {"你好1","","1","halo","嗨","哈.哈","哈~哈","哈·哈","你好·你好",
						"】","〖","#","…","·","你好ajj","aa","jj"};
		for (String string : str) {
			System.out.println(string+":"+CheckUtils.checkChineseName(string));
		}
		
	}

}
